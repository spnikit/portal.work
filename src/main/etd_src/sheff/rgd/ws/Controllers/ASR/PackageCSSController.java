package sheff.rgd.ws.Controllers.ASR;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import ru.aisa.rgd.etd.dao.ETDSyncServiceFacade;
import ru.aisa.rgd.etd.objects.ETDDocumentData;
import ru.aisa.rgd.ws.documents.ETDForm;
import ru.aisa.rgd.ws.exeption.ServiceException;
import ru.aisa.rgd.ws.utility.DataBinder;
import ru.aisa.rgd.ws.utility.TypeConverter;
import sheff.rgd.ws.Abstract.AbstractAction;
import sheff.rgd.ws.Abstract.DoAction;
import sheff.rjd.services.syncutils.SyncObj;
import sheff.rjd.services.syncutils.db2DateGenerator;
import sheff.rjd.services.transoil.TransPackTask;
import sheff.rjd.services.transoil.TransService;
import sheff.rjd.ws.OCO.AfterSign.SendToEtd;
//import javax.xml.crypto.Data;
import org.apache.log4j.Logger;

//import sheff.rjd.ws.OCO.AfterSign.TOR.CreateList;

public class PackageCSSController extends AbstractAction {

	private String parentform;
	private NamedParameterJdbcTemplate npjt;
	private SimpleJdbcTemplate sjt;
	private SendToEtd sendtoetd;
	protected final Logger log = Logger.getLogger(getClass());
	private TransService sendtotransoil;
	private ETDSyncServiceFacade etdsyncfacade;
	private ThreadPoolTaskExecutor taskExecutor;
	private DoAction formControllers;
	private static final String blobsql = "select bldoc from snt.docstore where id =:id";
	private static final String updt = "insert into snt.packages (id_pak, vagnum, etdid) values (?,?,?)";
	private static final String updtpackinfo = "insert into snt.packinfo (ID, OBJECT_KOD, REFER_ID, CODE_TEXT, REFER_TEXT) values (?,?,?,?,?)";
	private static final String list = "Лист согласований";

	public ETDSyncServiceFacade getEtdsyncfacade() {
		return etdsyncfacade;
	}

	public void setEtdsyncfacade(ETDSyncServiceFacade etdsyncfacade) {
		this.etdsyncfacade = etdsyncfacade;
	}

	public TransService getSendtotransoil() {
		return sendtotransoil;
	}

	public void setSendtotransoil(TransService sendtotransoil) {
		this.sendtotransoil = sendtotransoil;
	}

	public NamedParameterJdbcTemplate getNpjt() {
		return npjt;
	}

	public SendToEtd getSendtoetd() {
		return sendtoetd;
	}

	public void setSendtoetd(SendToEtd sendtoetd) {
		this.sendtoetd = sendtoetd;
	}

	public void setNpjt(NamedParameterJdbcTemplate npjt) {
		this.npjt = npjt;
	}

	public SimpleJdbcTemplate getSjt() {
		return sjt;
	}

	public void setSjt(SimpleJdbcTemplate sjt) {
		this.sjt = sjt;
	}

	public void setParentform(String parentname) {
		this.parentform = parentname;
	}

	public String getParentform() {
		return parentform;
	}

	public ThreadPoolTaskExecutor getTaskExecutor() {
		return taskExecutor;
	}

	public void setTaskExecutor(ThreadPoolTaskExecutor taskExecutor) {
		this.taskExecutor = taskExecutor;
	}
	
	public DoAction getFormControllers() {
		return formControllers;
	}

	public void setFormControllers(DoAction formControllers) {
		this.formControllers = formControllers;
	}

	@Override
	public void doAfterSign(String docName, String docdata, int predid,
			int signNumber, long id, String certserial, int WrkId) {
		Map<String, Object> hm1 = new HashMap<String, Object>();
		hm1.put("id", id);
		int drop = getNpjt()
				.queryForInt(
						"select CASE when dropid is null then 0 else 1 end from snt.docstore where id = :id",
						hm1);
		if (signNumber == 1 || drop == 1) {
			try {

				try {
					if (drop == 1) {
						final Map<String, Comparable> pp = new HashMap<String, Comparable>();
						String selectDocsInPack = "select id, dropid "
								+ "from snt.docstore where id_pak = (select id_pak from snt.docstore where id = :DOCID) "
								+ "and visible <> 1";
						pp.put("DOCID", id);
						SqlRowSet rs = npjt.queryForRowSet(selectDocsInPack, pp);
						String droptxt = getNpjt().queryForObject("select droptxt from snt.docstore where id = :DOCID", pp, String.class);
						while(rs.next()) {
							if(rs.getObject("dropid") == null) {			
								Long declId = rs.getLong("id");
								pp.put("CERTSERIAL", new BigInteger(certserial,16).toString());
								pp.put("WRKID", WrkId);
								pp.put("DOCID", declId);
								pp.put("REASON", droptxt);
								getNpjt().update("update snt.docstore set droptime = current timestamp,"
										+ " droptxt = :REASON, dropid = (select id from snt.personall where certserial = :CERTSERIAL), "
										+ "dropwrkid = :WRKID, DROPPREDID = (select predid from snt.docstore where id = :DOCID)  "
										+ "where id = :DOCID ", pp);
								String declName = (String)getNpjt().queryForObject("select name from snt.doctype where id = (select typeid from snt.docstore where id = :DOCID)", pp, String.class);
								String bl = new String((byte[])getNpjt().queryForObject("select bldoc from snt.docstore where id = :DOCID", pp, byte[].class), "UTF-8");
								Integer sign = getNpjt().queryForInt("select count(docid) from snt.docstoreflow where docid = :DOCID", pp);
								Integer predidDoc = getNpjt().queryForInt("select predid from snt.docstore where id = :DOCID", pp);
								declName = declName.trim();
								if(declName.equals("ФПУ-26 АСР")) {
									if (formControllers != null) {
										formControllers.doAfterSign(declName.trim(), bl, predidDoc, sign, (Long)pp.get("DOCID"), certserial, WrkId);
									}
								} else {
									byte[] bldoc = (byte[])npjt.queryForObject("select bldoc from snt.docstore "
											+ "where id = :DOCID", pp, byte[].class);
									String docdataDeclDoc = new String (bldoc, "UTF-8");
									sendtoetd.SendToEtdMessage(declId, docdataDeclDoc, declName, sign, 1, false);
								}
							}
						}
						sendtoetd.SendToEtdMessage(id, docdata, parentform, 1,
								drop, false);
						sendtotransoil.SendSigntoTransoil(id, signNumber, drop,
								predid);
					} else if (drop == 0) {
						try {
							getNpjt()
									.update("update snt.docstore set GROUPSGN =0 where id = :id",
											hm1);

//							sendtoetd.signpackdocs(id, certserial, WrkId,
//									predid);
							sendtotransoil.SendSigntoTransoil(id, signNumber,
									drop, predid);
							
						} catch (Exception e) {
							log.error(TypeConverter.exceptionToString(e));
						}

					}
				} catch (Exception e) {
					log.error(TypeConverter.exceptionToString(e));

				}
			} catch (ServiceException e) {
				log.error("Service Error:");
				log.error("code:" + e.getError().getCode() + " message : "
						+ e.getError().getMessage());
			} catch (Exception e) {
				log.error(TypeConverter.exceptionToString(e));
			}
		}

	}

	@Override
	public void doAfterSave(String docName, String docdata, int predid,
			int signNumber, String CertID, long id, int WrkId) throws Exception {

	}

	@Override
	public void doAfterSync(String formname, SyncObj syncobj, String sql,
			int signum) {
		try {
			ETDForm form = ETDForm.createFromArchive(syncobj.getBldoc());
			DataBinder b = form.getBinder();
			try {
				String content = b.getValue("P_29") + ", " + b.getValue("P_30");
				syncobj.setContent(content);
			} catch (Exception e) {
				log.error("no tag P_30 in Package: " + syncobj.getId_pak());
			}
			etdsyncfacade.insertDocstore(sql,
					etdsyncfacade.getWorkerWithorderNull(syncobj));
	//		etdsyncfacade.updateDSF(syncobj);


			String[] remplace = null;
			String[] etddocarray = b.getValuesAsArray("P_3a");
			
			if (b.getValue("P_18").length() > 0
					&& b.getValue("P_19").length() > 0)
				remplace = (b.getValue("P_19") + "," + b.getValue("P_18"))
						.split(",");
			String oldpackid = b.getValue("oldPackId");
			String torpackid = "";
			String servicetype = "";
			// int otc_code = -1;
			String price = "";
			String otc_code = "";
			try {
				servicetype = b.getValue("P_23");
			} catch (Exception e) {
				log.error("no tag P_23 in Package: " + syncobj.getId_pak());
			}

			try {
				price = b.getValue("P_31");
			} catch (Exception e) {
				log.error("no tag P_31 in Package: " + syncobj.getId_pak());
			}

			PackageUpdate(etddocarray, syncobj.getId_pak(),
					syncobj.getDogovor(), remplace, syncobj, oldpackid,
					torpackid, servicetype, otc_code, price);

			if (etdsyncfacade.getNamebyPredid(syncobj.getPredid()).indexOf(
					"ООО «Трансойл»") > -1) {

				if (formname.equals("Пакет документов ЦСС")) {
					sendtotransoil.SendtoTransoil(syncobj.getEtdid(), formname,
							syncobj.getDocdata(), new String(
									syncobj.getBldoc(), "UTF-8"));

					log.debug("trying to get documents for package "
							+ syncobj.getId_pak());
					if (etddocarray.length > 0) {
						log.debug("begin sending documents for package "
								+ syncobj.getId_pak());

						for (int i = 0; i < etddocarray.length; i++) {
							TransPackTask task = new TransPackTask(
									etddocarray[i], sendtotransoil,
									etdsyncfacade);
							taskExecutor.execute(task);
						}

					}

					else
						log.error("no documents to send for package "
								+ syncobj.getId_pak());

				}
			}
		} catch (Exception e) {
			log.error(TypeConverter.exceptionToString(e));
		}

	}

	private void PackageUpdate(String[] etddocarray, String id_pak,
			String dognum, String[] remplace, SyncObj syncobj,
			String oldpackid, String torpackid, String servicetype,
			String otc_code, String price) throws InterruptedException {

		List<Object[]> aa = new ArrayList<Object[]>();
		List<Object[]> pack = new ArrayList<Object[]>();
		int i = 0;
		String di = "";
		String rempred = "";
		if (remplace != null) {
			di = remplace[0];
			rempred = remplace[1];
		}

		while (i < etddocarray.length) {
			aa.add(new Object[] { id_pak, dognum, Long.valueOf(etddocarray[i]) });
			pack.add(new Object[] { id_pak, syncobj.getVagnum(),
					Long.valueOf(etddocarray[i]) });
			i++;
		}
		try {
			// ожидание с целью дождаться создания всех документов в пакете,
			// дабы не упал батч
			Thread.sleep(2000);
		} catch (Exception e) {
			log.error("sleep error: " + TypeConverter.exceptionToString(e));

		}
		try {
			sjt.batchUpdate(updt, pack);
		} catch (Exception e) {
			log.error("error inserting documents into snt.packages: "
					+ TypeConverter.exceptionToString(e));

		}

		try {
			sjt.batchUpdate(
					"update snt.docstore set id_pak =?, no = ? where etdid = ?",
					aa);
		} catch (Exception e) {
			log.error("error updating snt.docstore setting id_pak and no: "
					+ TypeConverter.exceptionToString(e));
		}

		try {

			db2DateGenerator gen = new db2DateGenerator();

			HashMap<String, Object> zz = new HashMap<String, Object>();
			zz.put("docid", syncobj.getDocid());
			zz.put("servicetype", servicetype);
			npjt.update(
					"update snt.docstore set di =:servicetype where id =:docid", zz);

		} catch (Exception e) {
			log.error(TypeConverter.exceptionToString(e));
		}

		try {
			HashMap<String, Object> pp = new HashMap<String, Object>();
			pp.put("oldpackid", oldpackid);
			pp.put("docid", syncobj.getDocid());
			npjt.update(
					"update snt.docstore set reqnum = :oldpackid where id =:docid",
					pp);
		} catch (Exception e) {
			log.error(TypeConverter.exceptionToString(e));
		}

		if (torpackid.length() > 2) {
			try {
				HashMap<String, Object> pp = new HashMap<String, Object>();
				pp.put("docid", syncobj.getDocid());
				pp.put("torpackid", torpackid);
				npjt.update(
						"update snt.docstore set torpackid = :torpackid where id =:docid",
						pp);
			} catch (Exception e) {
				log.error(TypeConverter.exceptionToString(e));
			}
		}

		if (otc_code.length() > 0) {
			try {
				

				if (otc_code.contains(",")) {
					List<Object[]> otc_obj = new ArrayList<Object[]>();
					ArrayList<Integer> refer = new ArrayList<Integer>();
					String[] otc_code_arr = otc_code.split(",");
					StringBuffer otc_name_str = new StringBuffer();
					HashMap<String, Object> otc = new HashMap<String, Object>();
					for (int j = 0; j < otc_code_arr.length; j++) {
						otc.put("object_kod", Integer.parseInt(otc_code_arr[j]));
						otc_name_str
								.append((String) npjt
										.queryForObject(
												"select rtrim(refer_name) from snt.dic_refer where refer_id =  (select refer from snt.dic_objects where object_kod = :object_kod)",
												otc, String.class));
						if ((j + 1) < otc_code_arr.length) {

							otc_name_str.append(",");
						}
						refer.add(npjt
								.queryForInt(
										"select refer from snt.dic_objects where object_kod = :object_kod",
										otc));

					}

					for (int z = 0; z < otc_code_arr.length; z++) {
						otc_obj.add(new Object[] { syncobj.getDocid(),
								Integer.parseInt(otc_code_arr[z]),
								refer.get(z), otc_code, otc_name_str.toString() });
					}

					sjt.batchUpdate(updtpackinfo, otc_obj);
				}

				else {
					 HashMap<String, Object> pp = new HashMap<String, Object>();
					 pp.put("docid", syncobj.getDocid());
					 pp.put("otc_code", Integer.parseInt(otc_code));
					 npjt.update("update snt.docstore set otc_code = :otc_code where id =:docid",
					 pp);
					
					
					try{
					List<Object[]> otc_obj = new ArrayList<Object[]>();
					ArrayList<Integer> refer = new ArrayList<Integer>();
					String otc_name_str = new String();
					HashMap<String, Object> otc = new HashMap<String, Object>();
					otc.put("object_kod", Integer.parseInt(otc_code));
					otc_name_str = (String) npjt
							.queryForObject(
									"select rtrim(refer_name) from snt.dic_refer where refer_id =  (select refer from snt.dic_objects where object_kod = :object_kod)",
									otc, String.class);
					refer.add(npjt
							.queryForInt(
									"select refer from snt.dic_objects where object_kod = :object_kod",
									otc));
					
					otc_obj.add(new Object[] { syncobj.getDocid(),
							Integer.parseInt(otc_code),
							refer.get(0), otc_code.toString(), otc_name_str.toString()});
					
					sjt.batchUpdate(updtpackinfo, otc_obj);
					} catch (Exception e){
						e.printStackTrace();
					}
					
					
					
				}
				

			} catch (Exception e) {
				log.error(TypeConverter.exceptionToString(e));
			}
		}
		if (price.length() > 0) {
			try {
				HashMap<String, Object> pp = new HashMap<String, Object>();
				pp.put("docid", syncobj.getDocid());
				pp.put("price", price);
				npjt.update(
						"update snt.docstore set price = :price where id =:docid",
						pp);
			} catch (Exception e) {
				log.error(TypeConverter.exceptionToString(e));
			}
		}
		
		
	}


	public ETDDocumentData EditBeforeOpen(String docName, ETDDocumentData doc) {
		// TODO Auto-generated method stub
		return doc;
	}
}

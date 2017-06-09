package sheff.rgd.ws.Controllers.TOR;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//import javax.xml.crypto.Data;







import javax.print.DocFlavor;

import org.apache.log4j.Logger;
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
import sheff.rjd.services.syncutils.SyncObj;
import sheff.rjd.services.syncutils.db2DateGenerator;
import sheff.rjd.services.transoil.TransPackTask;
import sheff.rjd.services.transoil.TransService;
import sheff.rjd.ws.OCO.TORLists;
import sheff.rjd.ws.OCO.AfterSign.SendToEtd;

//import sheff.rjd.ws.OCO.AfterSign.TOR.CreateList;

public class PackageController extends AbstractAction {

	private String parentform;
	private NamedParameterJdbcTemplate npjt;
	private SimpleJdbcTemplate sjt;
	private SendToEtd sendtoetd;
	protected final Logger log = Logger.getLogger(getClass());
	private TransService sendtotransoil;
	private ETDSyncServiceFacade etdsyncfacade;
	private ThreadPoolTaskExecutor taskExecutor;
	private static final String blobsql = "select bldoc from snt.docstore where id =:id";
	private static final String updt = "insert into snt.packages (id_pak, vagnum, etdid) values (?,?,?)";
	private static final String updtpackinfo = "insert into snt.packinfo (ID, OBJECT_KOD, REFER_ID, CODE_TEXT, REFER_TEXT) values (?,?,?,?,?)";
	private static final String list = "Лист согласований";
	private long id_pack;
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

	@Override
	public void doAfterSign(String docName, String docdata, int predid,
			int signNumber, long id, String certserial, int WrkId) {
		Map hm1 = new HashMap();
		hm1.put("id", id);
		int drop = getNpjt()
				.queryForInt(
						"select CASE when dropid is null then 0 else 1 end from snt.docstore where id = :id",
						hm1);

		if (signNumber == 2 || drop == 1) {
			try {
				try {
					if (drop == 1) {
						sendtoetd.SendToEtdMessage(id, docdata, parentform, 1,
								drop, false);
						sendtotransoil.SendSigntoTransoil(id, signNumber, drop,
								predid);
						//заполнение для отчета п.6
						Map<String, Object> pp = new HashMap<String, Object>();
						pp.put("id", id);
						String selectOpenpak = "select DT_open_pak from snt.PGKREPORT where id_pak = :id_pak with ur";
						Long etdid = null;
						Date droptime = null;
						String selectDroptimeAndETDId = "select droptime, etdid from snt.docstore where id = :id";
						SqlRowSet rs = npjt.queryForRowSet(selectDroptimeAndETDId, pp);
						while(rs.next()) {
							droptime = rs.getTimestamp("droptime");
							etdid = rs.getLong("etdid");
						}
						id_pack = etdid;
						pp.put("id_pak", etdid);		
						pp.put("DT_drop_pak", droptime);
						Date openPak = npjt.queryForObject(selectOpenpak, pp, Date.class);	
						if(openPak == null) {
							npjt.update("update snt.PGKREPORT set DT_drop_pak = :DT_drop_pak, "
									+ "DT_open_pak = :DT_drop_pak "
									+ "where id_pak = :id_pak", pp);          
						} else {
							npjt.update("update snt.PGKREPORT set DT_drop_pak = :DT_drop_pak "
									+ "where id_pak = :id_pak", pp); 
						}
					} else if (drop == 0) {

						try {

							getNpjt()
							.update("update snt.docstore set GROUPSGN =0 where id = :id",
									hm1);

							sendtoetd.signpackdocs(id, certserial, WrkId,
									predid);
							sendtotransoil.SendSigntoTransoil(id, signNumber,
									drop, predid);

							//заполнение для отчета п.5
							Map<String, Object> pp = new HashMap<String, Object>();
							pp.put("id", id);
							String selectOpenpak = "select DT_open_pak from snt.PGKREPORT where id_pak = :id_pak  with ur";
							String resultSelect = "select ldate, ltime, id_pak from snt.docstore where id = :id";
							SqlRowSet rs = npjt.queryForRowSet(resultSelect, pp);
							Date ldate = null;
							Time ltime = null;
							String id_pak = null;
							while(rs.next()) {
								ldate = rs.getDate("ldate");
								ltime = rs.getTime("ltime");
								id_pak = rs.getString("id_pak");
							}       	
							SimpleDateFormat myDateFormat = new SimpleDateFormat("yyyy-MM-dd");
							String ldt = myDateFormat.format(ldate);
							myDateFormat.applyPattern("yyyy-MM-dd HH:mm:ss");
							Date DT_accept_pak = myDateFormat.parse(ldt + " " + ltime);
							pp.put("DT_accept_pak", DT_accept_pak);
							pp.put("id_pak", id_pak);
							Date openPak = npjt.queryForObject(selectOpenpak, pp, Date.class);	
							if(openPak == null) {
								npjt.update("update snt.PGKREPORT set DT_accept_pak = :DT_accept_pak, "
										+ "DT_open_pak = :DT_accept_pak "
										+ "where id_pak = :id_pak", pp);
							} else {
								npjt.update("update snt.PGKREPORT set DT_accept_pak = :DT_accept_pak "
										+ "where id_pak = :id_pak", pp); 
							}


						} catch (Exception e) {
							log.error(TypeConverter.exceptionToString(e));
						}

					}
				} catch (Exception e) {
					log.error(TypeConverter.exceptionToString(e));

				}
			} catch (ServiceException e) {
				// e.printStackTrace(System.out);
				log.error("Service Error:");
				log.error("code:" + e.getError().getCode() + " message : "
						+ e.getError().getMessage());
			} catch (Exception e) {
				// e.printStackTrace(System.out);
				log.error(TypeConverter.exceptionToString(e));
				// SNMPSender.sendMessage(e);
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

		etdsyncfacade.insertDocstore(sql,
				etdsyncfacade.getWorkerWithorderNull(syncobj));
		etdsyncfacade.updateDSF(syncobj);

		try {
			ETDForm form = ETDForm.createFromArchive(syncobj.getBldoc());
			DataBinder b = form.getBinder();
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
				torpackid = b.getValue("torPackId");
			} catch (Exception e) {
				log.error("no tag torpackid in Package: " + syncobj.getId_pak());
			}
			try {
				servicetype = b.getValue("P_23");
			} catch (Exception e) {
				log.error("no tag P_23 in Package: " + syncobj.getId_pak());
			}

			try {
				// otc_code = Integer.parseInt(b.getValue("P_28"));
				otc_code = b.getValue("P_28");
			} catch (Exception e) {
				log.error("no tag P_28 in Package: " + syncobj.getId_pak());
			}
			try {
				price = b.getValue("P_27");
			} catch (Exception e) {
				log.error("no tag P_27 in Package: " + syncobj.getId_pak());
			}

			PackageUpdate(etddocarray, syncobj.getId_pak(),
					syncobj.getDogovor(), remplace, syncobj, oldpackid,
					torpackid, servicetype, otc_code, price);

			int a = etdsyncfacade.getNamebyPredid(syncobj.getPredid()).indexOf(
					"ООО «Трансойл»");

			if (etdsyncfacade.getNamebyPredid(syncobj.getPredid()).indexOf(
					"ООО «Трансойл»") > -1) {

				if (formname.equals(TORLists.Package)) {
					sendtotransoil.SendtoTransoil(syncobj.getEtdid(), formname,
							syncobj.getDocdata(), new String(
									syncobj.getBldoc(), "UTF-8"));
					Map<String, Object> paramMap = new HashMap<String, Object>();
					paramMap.put("id_pak", String.valueOf(syncobj.getEtdid()));
					getNpjt().update("update snt.packages set send = 0 where id_pak = :id_pak", paramMap);

					/*log.debug("trying to get documents for package "
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
								+ syncobj.getId_pak());*/

				}

				// if (formname.equals(TORLists.SF)){
				// sendtotransoil.SendtoTransoil(
				// spo.getEtddocid(), formname,
				// syncobj.getDocdata(), formblob);
				// }
			}

		} catch (Exception e) {
			log.error(TypeConverter.exceptionToString(e));
		}
		//		ETDForm form;
		try {
			//System.out.println("try");
			ETDForm form = ETDForm.createFromArchive(syncobj.getBldoc());
			DataBinder binder = form.getBinder();
			HashMap<String, Object> pp = new HashMap<String, Object>();
			Long id_pack = syncobj.getEtdid();
			Long id = syncobj.getDocid();
			pp.put("id", id);
			Date crdate = null;
			Time crtime = null;
			String selectCrdateAndCrtime = "select crdate, crtime from snt.docstore"
					+ " where id = :id";
			SqlRowSet rs = npjt.queryForRowSet(selectCrdateAndCrtime, pp);
			while(rs.next()) {
				crdate = rs.getDate("crdate");
				crtime = rs.getTime("crtime");
			}
			String vagon = binder.getValue("P_15");
			String rep_date = binder.getValue("P_22");
			String[] array = rep_date.split("\\.");
			StringBuffer result = new StringBuffer();
			result.append(array[2]).append("-").append(array[1]).append("-").
			append(array[0]);
			String pr = binder.getValue("P_27");
			String defect = binder.getValue("P_28");
			if(defect.length() > 0) {
				String[] arrayDefect = defect.split(",");

				String selectDefect = "select count(0) from snt.DIC_OBJECTS where "
						+ "OBJECT_KOD = :OBJECT_KOD and REFER = 19602";
				int number = 0;
				for(int i = 0; i < arrayDefect.length; i++) {
					pp.put("OBJECT_KOD", Integer.parseInt(arrayDefect[i]));
					number = number + npjt.queryForInt(selectDefect, pp);
				}
				if(number == arrayDefect.length) {
					pp.put("defect", 1);
				} else {
					pp.put("defect", 0);
				}
			} else {
				pp.put("defect", null);
			}
			pp.put("predid", syncobj.getPredid());
			if(pr.length()==0) {
				pp.put("price", null);
			} else {
				pp.put("price", pr);
			}
			pp.put("vagon", vagon);
			pp.put("rep_date", result);
			pp.put("id_pak", id_pack);
			SimpleDateFormat myDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Date DT_create_pak = myDateFormat.parse(crdate + " " + crtime);
			pp.put("DT_create_pak", DT_create_pak);
			Integer iter = npjt.queryForInt("select count(0) from snt.PGKREPORT where"
					+ " vagon = :vagon and rep_date = :rep_date and DT_drop_pak is not null  with ur", pp);
			Long wait_correct_pak = null;
			if(iter > 0) { 
				String selectDT_drop_pak = "select DT_drop_pak from snt.PGKREPORT where vagon = :vagon "
						+ "and rep_date = :rep_date "
						+ "and DT_drop_pak = (select max(DT_drop_pak) from snt.PGKREPORT where "
						+ "vagon = :vagon and rep_date = :rep_date)  with ur";
				Date DT_drop_pak = npjt.queryForObject(selectDT_drop_pak, pp, Date.class);
				pp.put("DT_drop_pak", DT_drop_pak);
				wait_correct_pak = DT_create_pak.getTime() - DT_drop_pak.getTime();
				wait_correct_pak = wait_correct_pak / (60*60*1000)%60;
			} 
			pp.put("count_drop", iter);
			Integer count = npjt.queryForInt("select count(0) from snt.PGKREPORT"
					+ " where vagon = :vagon and rep_date = :rep_date and id_pak is null with ur", pp);
			if(count > 0) {
				if(wait_correct_pak != null) {
					pp.put("wait_correct_pak", wait_correct_pak);
					npjt.update("update snt.PGKREPORT set id_pak = :id_pak, DT_create_pak = :DT_create_pak, predid = :predid, "
							+ "count_drop = :count_drop, wait_create_pak = TIMESTAMPDIFF(8,CHAR(CAST(:DT_create_pak AS TIMESTAMP) - DT_VU36)), "
							+ "wait_correct_pak = :wait_correct_pak, "
							+ "price = :price, "
							+ "defect = :defect "
							+ "where vagon = :vagon and rep_date = :rep_date and id_pak is null", pp);
				}else{
					npjt.update("update snt.PGKREPORT set id_pak = :id_pak, DT_create_pak = :DT_create_pak, predid = :predid, "
							+ "count_drop = :count_drop, wait_create_pak = TIMESTAMPDIFF(8,CHAR(CAST(:DT_create_pak AS TIMESTAMP) - DT_VU36)), "
							+ "price = :price, " 
							+ "defect = :defect "
							+ "where vagon = :vagon and rep_date = :rep_date and id_pak is null", pp);
				}
			} else {
				pp.put("Kleymo",  binder.getValue("P_10"));
				if(wait_correct_pak != null) {
					pp.put("wait_correct_pak", wait_correct_pak);
					String sqlInsert = "INSERT INTO snt.PGKREPORT ("
							+ "vagon, rep_date, Kleymo, id_pak, DT_create_pak, count_drop, price, defect, wait_correct_pak, predid) "
							+ "Values (:vagon, :rep_date, :Kleymo, :id_pak, :DT_create_pak, :count_drop, :price, :defect, :wait_correct_pak, :predid)";
					npjt.update(sqlInsert, pp);
				} else {
					String sqlInsert = "INSERT INTO snt.PGKREPORT ("
							+ "vagon, rep_date, Kleymo, id_pak, DT_create_pak, count_drop, price, defect, predid) "
							+ "Values (:vagon, :rep_date, :Kleymo, :id_pak, :DT_create_pak, :count_drop, :price, :defect, :predid)";
					npjt.update(sqlInsert, pp);
				}
			}
		} catch (Exception e) {
			log.error(TypeConverter.exceptionToString(e));
		}

	}
	public ETDDocumentData EditBeforeOpen(String docName, ETDDocumentData doc) {
		// TODO Auto-generated method stub
		return doc;
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
			pack.add(new Object[] { id_pak, syncobj.getVagnum(),Long.valueOf(etddocarray[i]) });
			i++;
		}
		//		try {
		//			// ожидание с целью дождаться создания всех документов в пакете,
		//			// дабы не упал батч
		//			Thread.sleep(2000);
		//			 
		//		} catch (Exception e) {
		//			log.error("sleep error: " + TypeConverter.exceptionToString(e));
		//
		//		}
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
		HashMap<String, Object> zz = new HashMap<String, Object>();
		try {

			db2DateGenerator gen = new db2DateGenerator();

			
			zz.put("docid", syncobj.getDocid());
			zz.put("reqdate", gen.Date(syncobj.getRepdate()));
			zz.put("servicetype", servicetype);
			zz.put("id_pak", id_pack);
			npjt.update(
					"update snt.docstore set reqdate = :reqdate, di =:servicetype where id =:docid",
					zz);

		} catch (Exception e) {
			log.error(TypeConverter.exceptionToString(e));
		}
		
		try{
			npjt.update("update snt.pgkreport set service_type =:servicetype where id_pak =:id_pak", zz);
		}catch (Exception e){
			log.error(TypeConverter.exceptionToString(e));
		}
		

		try {
			HashMap<String, Object> pp = new HashMap<String, Object>();
			pp.put("oldpackid", oldpackid);
			pp.put("docid", syncobj.getDocid());
			npjt.update(
					"update snt.docstore set reqnum = :oldpackid where id =:docid",
					pp);

			// etdsyncfacade.setLastPak(syncobj.getVagnum(), syncobj.getEtdid(),
			// syncobj.getRepdate());
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

		// if (otc_code>-1){
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
					//					System.out.println(otc_code);
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

		try {
			HashMap<String, Object> paramMap = new HashMap<String, Object>();
			paramMap.put("id_pak", id_pak);
			paramMap.put("id", syncobj.getDocid());
			int countFpuInPackage = npjt.queryForInt("select count(*) from snt.docstore where id_pak = :id_pak and typeid = (select id from snt.doctype where trim(name) = 'ФПУ-26')", paramMap);

			if(countFpuInPackage>0){
				
				String docDataFpu = npjt.queryForObject("select docdata from snt.docstore where id_pak = :id_pak and typeid = (select id from snt.doctype where trim(name) = 'ФПУ-26')", paramMap, String.class);
				ETDForm form = ETDForm.createFromDocData(docDataFpu);
				DataBinder binder = form.getBinder();
				String numberFpu = binder.getValue("P_7");
				paramMap.put("numberFpu", numberFpu);
				npjt.update("update snt.docstore set numfpu = :numberFpu where id = :id", paramMap);
				
			}
		} catch (Exception e) {
			log.error(TypeConverter.exceptionToString(e));
		}
		
		// etdsyncfacade.setLastPak(syncobj.getVagnum(), syncobj.getEtdid(),
		// syncobj.getRepdate());

	}
}

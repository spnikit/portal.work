package sheff.rgd.ws.Controllers.Invoice;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import ru.aisa.crypto.FieldType;
import ru.aisa.crypto.WhoType;
import ru.aisa.crypto.X509Parser;
import ru.aisa.crypto.X509ParserFactory;
import ru.aisa.mail.EmailHelper;
import ru.aisa.rgd.etd.dao.ETDSyncServiceFacade;
import ru.aisa.rgd.etd.objects.ETDDocumentData;
import ru.aisa.rgd.ws.documents.ETDForm;
import ru.aisa.rgd.ws.exeption.ServiceException;
import ru.aisa.rgd.ws.utility.DataBinder;
import ru.aisa.rgd.ws.utility.TypeConverter;
import sheff.rgd.ws.Abstract.AbstractAction;
import sheff.rjd.services.syncutils.SyncObj;
import sheff.rjd.services.transoil.TransService;
import sheff.rjd.utils.Base64;
import sheff.rjd.ws.OCO.AfterEdit.FakeSignature;
import sheff.rjd.ws.OCO.AfterSign.SendToEtd;
import sheff.rjd.ws.handlers.FormHandler;

import com.aisa.portal.invoice.ttk.SellerSignedInvoceToTTK;

public class SFController extends AbstractAction {

	protected final Logger log = Logger.getLogger(getClass());
	private NamedParameterJdbcTemplate npjt;
	private SendToEtd sendtoetd;
	private TransService sendtotransoil;
	private SellerSignedInvoceToTTK ssiobj;
	private String parentform;
	private FakeSignature fakesignature;
	private Map <Integer, FormHandler> handlers;
	private EmailHelper mail;
	public NamedParameterJdbcTemplate getNpjt() {
		return npjt;
	}

	public void setNpjt(NamedParameterJdbcTemplate npjt) {
		this.npjt = npjt;
	}

	public SellerSignedInvoceToTTK getSsiobj() {
		return ssiobj;
	}

	public void setSsiobj(SellerSignedInvoceToTTK ssiobj) {
		this.ssiobj = ssiobj;
	}

	private ETDSyncServiceFacade etdsyncfacade;

	public ETDSyncServiceFacade getEtdsyncfacade() {
		return etdsyncfacade;
	}

	public void setEtdsyncfacade(ETDSyncServiceFacade etdsyncfacade) {
		this.etdsyncfacade = etdsyncfacade;
	}

	public SendToEtd getSendtoetd() {
		return sendtoetd;
	}

	public String getParentform() {
		return parentform;
	}

	public void setParentform(String parentform) {
		this.parentform = parentform;
	}

	public void setSendtoetd(SendToEtd sendtoetd) {
		this.sendtoetd = sendtoetd;
	}

	public TransService getSendtotransoil() {
		return sendtotransoil;
	}

	public void setSendtotransoil(TransService sendtotransoil) {
		this.sendtotransoil = sendtotransoil;
	}


	public FakeSignature getFakesignature() {
		return fakesignature;
	}

	public void setFakesignature(FakeSignature fakesignature) {
		this.fakesignature = fakesignature;
	}

	public Map<Integer, FormHandler> getHandlers() {
		return handlers;
	}

	public void setHandlers(Map<Integer, FormHandler> handlers) {
		this.handlers = handlers;
	}

	public EmailHelper getMail() {
		return mail;
	}

	public void setMail(EmailHelper mail) {
		this.mail = mail;
	}

	@Override
	public void doAfterSign(String docName, String docdata, int predid,
			int signNumber, long id, String certserial, int WrkId) {
		System.out.println("Начало конца");
		ETDForm form=null;
		try {
			form = ETDForm.createFromArchive(docdata.getBytes("UTF-8"));
		} catch (UnsupportedEncodingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (ServiceException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		DataBinder b = form.getBinder();
		int inner = 0;
		try{
			inner= Integer.parseInt(b.getValue("VRK1"));
		}catch (Exception e){
			log.error("No tag VRK1 in document with id = "+id);	
		}


		Map<String, Object> hm1 = new HashMap<String, Object>();
		hm1.put("id", id);
		int drop = getNpjt()
				.queryForInt(
						"select CASE when dropid is null then 0 else 1 end from snt.docstore where id = :id",
						hm1);
		String id_pak = getNpjt().queryForObject(
				"select id_pak from snt.docstore where id = :id", hm1,
				String.class);
		if (drop == 1 || signNumber == 1) {
			//Для СФ на отправку в ЭТД
			if (inner==0){
				try {
					sendtoetd.SendToEtdMessage(id, docdata, parentform,
							signNumber, drop, drop==1?false:true);
					sendtotransoil.SendSigntoTransoil(id, signNumber, drop,
							predid);

					//обновление отчета СФ
					if (drop==1){
						npjt.update("update snt.sf_reports set responseid = (select dropid from snt.docstore where id =:id) where id =:id", hm1);
						if (signNumber==1){


							if (!isUtochnenie(id))
								SendEmailToIIT(id, certserial);
						}
					}
					else {
						try{
							String[] innkppbuyer = b.getValue("inn_pokypatel").split("/");
							hm1.put("innbuyer", innkppbuyer[0]);
							hm1.put("kppbuyer", innkppbuyer[1]);
							int topredid = getNpjt().queryForInt("select id from snt.pred where inn=:innbuyer and kpp=:kppbuyer and headid is null", hm1);
							hm1.put("topred", topredid);
							getNpjt().update("update snt.docstore set predid =:topred, ldate = null, ltime= null where id=:id", hm1);
							String bcabinetid = etdsyncfacade.getCabinetIdByPred(topredid);
							String scabinetid = etdsyncfacade.getCabinetIdByPred(predid);
							byte[] xml_text = Base64.decode(b.getValue("xml_text"));
							byte[] xml_sign = Base64.decode(b.getValue("xml_sign"));

							ssiobj.processSF(bcabinetid, scabinetid, id,
									xml_text, xml_sign, false);
							getNpjt().update("update snt.docstore set sf_sign = 0, sf_gfsgn =0  where id =:id", hm1);
						}catch (Exception e){
							e.printStackTrace();
							log.error(TypeConverter.exceptionToString(e));
						}


					}
					
					
					
					// sendsign.SendSign(id, signNumber, drop, predid);

				} catch (Exception e) {

					log.error(TypeConverter.exceptionToString(e));

				}

			}

			//Для СФ ВРК-1
			if (inner==1){
				try{
					String[] innkppbuyer = b.getValue("inn_pokypatel").split("/");
					hm1.put("innbuyer", innkppbuyer[0]);
					hm1.put("kppbuyer", innkppbuyer[1]);
					int topredid = getNpjt().queryForInt("select id from snt.pred where inn=:innbuyer and kpp=:kppbuyer and headid is null", hm1);
					hm1.put("topred", topredid);
					getNpjt().update("update snt.docstore set predid =:topred, ldate = null, ltime= null where id=:id", hm1);
					String bcabinetid = etdsyncfacade.getCabinetIdByPred(topredid);
					String scabinetid = etdsyncfacade.getCabinetIdByPred(predid);
					byte[] xml_text = Base64.decode(b.getValue("xml_text"));
					byte[] xml_sign = Base64.decode(b.getValue("xml_sign"));

					ssiobj.processSF(bcabinetid, scabinetid, id,
							xml_text, xml_sign, false);
					getNpjt().update("update snt.docstore set sf_sign = 0, sf_gfsgn =0  where id =:id", hm1);
				}catch (Exception e){
					e.printStackTrace();
					log.error(TypeConverter.exceptionToString(e));
				}


			}


		}



	}

	@Override
	public void doAfterSave(String docName, String docdata, int predid,
			int signNumber, String CertID, long id, int WrkId) throws Exception {
		int inner = 0;
		try{
			ETDForm form = ETDForm.createFromArchive(docdata.getBytes("UTF-8"));
			DataBinder b = form.getBinder();
			inner= Integer.parseInt(b.getValue("VRK1"));
		}catch (Exception e){
			log.error("No tag VRK1 in document with id = "+id);	
		}

		//Для СФ, которые пришли из ЭТД
		if (inner==0&&signNumber==1){
			try {
				sendtoetd.FakeSign(docName, docdata, predid, signNumber, CertID,
						id, WrkId);
			} catch (Exception e) {
				log.error(TypeConverter.exceptionToString(e));
			};

			try {
				HashMap<String, Object> pp = new HashMap<String, Object>();

				String cert = new BigInteger(CertID, 16).toString();
				pp.put("CertSerial", cert);
				int userid = npjt
						.queryForInt(
								"select id from snt.personall where certserial = :CertSerial",
								pp);
				pp.put("respid", userid);
				pp.put("docid", id);
				npjt.update(
						"update snt.sf_reports set responseid = :respid where id =:docid",
						pp);
			} catch (Exception e) {
				log.error(TypeConverter.exceptionToString(e));
			}

			sendtoetd.SendToEtdMessage(id, docdata, parentform, signNumber, 0,
					false);
			sendtotransoil.SendSigntoTransoil(id, signNumber, 0, predid);
			//		sendtotransoil.SendSFNotice(id,0);
		}
		if (inner==1&&signNumber==1){
			try {

				sendtoetd.FakeSign(docName, docdata, predid, signNumber, CertID,
						id, WrkId);
			} catch (Exception e) {
				log.error(TypeConverter.exceptionToString(e));
			};
		}
	}



	@Override
	public void doAfterSync(String formname, SyncObj syncobj, String sql,
			int signum) {

		ETDForm form = null;
		DataBinder b = null;
		try {
			form = ETDForm.createFromArchive(syncobj.getBldoc());
			b = form.getBinder();
		} catch (UnsupportedEncodingException e1) {
			log.error(TypeConverter.exceptionToString(e1));
		} catch (ServiceException e1) {
			log.error(TypeConverter.exceptionToString(e1));
		} catch (IOException e1) {
			log.error(TypeConverter.exceptionToString(e1));
		}
		
		
		if (syncobj.getSignlvl()==1){
			try {

				
				
				try {
					Calendar cal = Calendar.getInstance();
					SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
					b.setNodeValue("recieve_date", sdf.format(cal.getTime()));
					syncobj.setBldoc(form.encodeToArchiv());
					syncobj.setDocdata(form.transform("data"));

				} catch (Exception e) {
					log.error(TypeConverter.exceptionToString(e));
				}
				try {

					long ppsid = 0;
					ppsid = Long.parseLong(b.getValue("fpuDocId"));
					HashMap<String, Object> pp = new HashMap<String, Object>();
					pp.put("ppsid", ppsid);

					int predid = npjt.queryForInt(
							"select predid from snt.docstore where etdid = :ppsid",
							pp);
					syncobj.setPredid(predid);
				} catch (Exception e) {
					e.printStackTrace();
					log.error("no tag id_PPS found "
							+ TypeConverter.exceptionToString(e));

				}

				syncobj.setContent("");


				etdsyncfacade.insertDocstore(sql,
				etdsyncfacade.getWorkerWithorderNull(syncobj));
				etdsyncfacade.updateDSF(syncobj);

				if (syncobj.getId_pak().length() > 2) {
					try {
						HashMap<String, Object> pp = new HashMap<String, Object>();
						pp.put("docid", syncobj.getDocid());
						pp.put("typename", "Пакет документов");
						long packid = npjt
								.queryForLong(
										"select id from snt.docstore where "
												+ "id_pak = (select id_pak from snt.docstore where id =:docid) and typeid = (select id from snt.doctype where name =:typename)",
												pp);

						pp.put("packid", packid);

						npjt.update(
								"update snt.docstore set repdate =(select repdate from snt.docstore where id =:packid), "
										+ "reqdate = (select reqdate from snt.docstore where id =:packid) where id =:docid",
										pp);

					} catch (Exception e) {
						log.error(TypeConverter.exceptionToString(e));
						// e.printStackTrace();
					}
				}

				// заполнение в таблицу snt.sf_reports для отчета
				try {
					String innSeller = b.getValue("inn_prodavca");
					String sfType = "";
					String[] massInnSeller = innSeller.split("/");
					innSeller = massInnSeller[0];

					if (b.getValue("formname").equals(
							"Корректировочный счет-фактура")) {
						sfType = "Корректировочный";
					} else {
						if (b.getValue("num_isprav").length() == 0
								&& b.getValue("date_ot_isprav").length() == 0) {
							sfType = "Первичный";
							syncobj.setSf_type(2);
						} else {
							sfType = "Исправленный";
							syncobj.setSf_type(0);
						}
					}




					HashMap<String, Object> sfmap = new HashMap<String, Object>();
					sfmap.put("id", syncobj.getDocid());
					sfmap.put("predid", syncobj.getPredid());
					sfmap.put("vagnum", syncobj.getVagnum());
					sfmap.put("innseller", innSeller);
					sfmap.put("sfnumber", b.getValue("num_schet"));
					sfmap.put("sfdate", b.getValue("date_ot"));
					sfmap.put("summwithnds", b.getValue("vsego_stoimost_s_nalog"));
					sfmap.put("summnds", b.getValue("vsego_nalog"));
					sfmap.put("ndsvalue", b.getValue("nalog_stavka"));
					sfmap.put("recievedate", b.getValue("recieve_date"));
					sfmap.put("sftype", sfType);

					String sell = "";
					int sellid = -1;
					try{
						sfmap.put("mark", syncobj.getMark());

						sell = (String) npjt.queryForObject("select rtrim(name) from snt.dor where id = "
								+ "(select distinct(dorid) from snt.rem_pred where kleim = :mark)", sfmap, String.class);

						sellid = npjt.queryForInt("select distinct(dorid) from snt.rem_pred where kleim = :mark", sfmap);


					}	catch (Exception e){
						log.error(TypeConverter.exceptionToString(e));
					}			
					sfmap.put("seller", sell);
					sfmap.put("sellid", sellid);
					npjt.update(
							"INSERT INTO SNT.SF_REPORTS(ID,PREDID,SELLER,INNSELLER,SFNUMBER,SF_DATE,SUMMWITHNDS,SUMMNDS,NDSVALUE,SFTYPE,RECIEVE_DATE,VAGNUM,INCOME_DATE, SELLERID) values(:id,:predid,:seller,:innseller,:sfnumber,:sfdate,:summwithnds,:summnds,:ndsvalue,:sftype,:recievedate,:vagnum,current date, :sellid)",
							sfmap);

					sfmap.put("sftypeint", syncobj.getSf_type());
					sfmap.put("sf_gfsgn", 0);
					npjt.update("update snt.docstore set sf_type = :sftypeint, sf_gfsgn =:sf_gfsgn where id =:id", sfmap);

				} catch (Exception e) {
					log.error(TypeConverter.exceptionToString(e));
				}
				// ---
				//			System.out.println("12");
				String bcabinetid = etdsyncfacade.getCabinetIdByPred(syncobj
						.getPredid());
				String scabinetid = etdsyncfacade.getCabinetIdByPred(etdsyncfacade
						.getPredMaker("ОАО «РЖД»"));
				byte[] xml_text = Base64.decode(b.getValue("xml_text"));
				byte[] xml_sign = Base64.decode(b.getValue("xml_sign"));

				//			System.out.println(new String(xml_text, "windows-1251"));

				ssiobj.processSF(bcabinetid, scabinetid, syncobj.getDocid(),
						xml_text, xml_sign, false);

				// Отправка в Трансойл
				if (etdsyncfacade.getNamebyPredid(syncobj.getPredid()).indexOf(
						"ООО «Трансойл»") > -1) {
					sendtotransoil.SendtoTransoil(syncobj.getEtdid(), parentform,
							syncobj.getDocdata(), new String(syncobj.getBldoc(),
									"UTF-8"));
				}

				//для отчета п.8
				Long EtdId = syncobj.getEtdid();
				HashMap<String, Object> pp = new HashMap<String, Object>();
				pp.put("id", syncobj.getDocid());
				pp.put("etdid", b.getValue("fpuDocId"));
				String id_pak = npjt.queryForObject("select id_pak from snt.docstore "
						+ "where etdid = :etdid", pp, String.class);
				pp.put("id_pak", id_pak);
				java.sql.Date crdate = npjt.queryForObject("select crdate from snt.docstore "
						+ "where id = :id", pp, java.sql.Date.class);
				//			Time crtime = npjt.queryForObject("select crtime from snt.docstore "
				//					+ "where id = : id", pp, Time.class);
				//			SimpleDateFormat myDateFormat = new SimpleDateFormat("yyyy.MM.dd");
				//        	java.util.Date DT_create_sf = myDateFormat.parse(crdate.toString());
				pp.put("DT_create_sf", crdate);
				pp.put("id_sf", EtdId);
				npjt.update("update snt.PGKREPORT set DT_create_sf = :DT_create_sf, id_sf = :id_sf "
						+ "where id_pak = :id_pak", pp);
			} catch (Exception e) {
				log.error(TypeConverter.exceptionToString(e));
			}
		}

		if (syncobj.getSignlvl()==2){
			HashMap<String, Object> pp = new HashMap<String, Object>();
			pp.put("typeid", syncobj.getTypeid());
			pp.put("order", 4);
			pp.put("id", syncobj.getDocid());
			int wrkid = getNpjt().queryForInt("select wrkid from snt.doctypeflow where order = :order and dtid = :typeid ", pp);
			int predid = getNpjt().queryForInt("select predid from snt.docstore where id =:id ", pp);
			fakesignature.fakesign(syncobj.getDocid(), 0, wrkid, predid, syncobj.getBldoc(), syncobj.getDocdata());
		}

		//заполнение numsf, у пакета документов, таблицы snt.docstore

		String getDiSql = "select count(*) from "+
				"(select case when di in ('02') then 1 end as di from snt.docstore where "+
				"id_pak = :id_pak) as tab "+
				"where di = 1";
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("id_pak", syncobj.getId_pak());

		try{
			int count = getNpjt().queryForInt(getDiSql, paramMap);
			if(count == 1){
				paramMap.put("numberSf", b.getValue("num_schet"));
				getNpjt().update("update snt.docstore set numsf = :numberSf where id_pak = :id_pak and typeid = (select id from snt.doctype where trim(name) = 'Счет-фактура')", paramMap);
			}
		}catch(Exception e){
			log.error(TypeConverter.exceptionToString(e));
		}
		//


	}

	private String getEmailfromCert (String certstring){
		HashMap<String, Object> pp = new HashMap<String, Object>();
		pp.put("certserial", new BigInteger(certstring, 16).toString());

		byte[] filecontent = (byte[])npjt.queryForObject("select publickey from snt.personall where certserial =:certserial", pp, byte[].class);

		String email = null;
		try{
			X509Parser p = X509ParserFactory.getParser(filecontent);
			email = p.getValue(WhoType.SUBJECT,FieldType.E);
		} catch (Exception e){
			log.error(TypeConverter.exceptionToString(e));
		}

		return email;
	}

	private void SendEmailToIIT(long id, String certserial){
		HashMap<String, Object> pp = new HashMap<String, Object>();
		pp.put("id", id);
		String email = getEmailfromCert(certserial);
		String declinereason = npjt.queryForObject("select droptxt from snt.docstore where id=:id", pp, String.class);
		String predname = (String) npjt.queryForObject("select rtrim(vname) from snt.pred where id = (select predid from snt.docstore where id =:id)",pp, String.class);
		int etdid = getNpjt().queryForInt("select etdid from snt.docstore where id = :id", pp);
		String mes="<div style='background-color: #dddddd'><b>Информация о СФ</b></div>"+
				"<br>"+
				"<div>"+
				"<div>ID документа: "+id+"</div>"+
				"<br>"+
				"<div>ID документа в АС ЭТД: "+etdid+"</div>"+
				"<br>"+
				"<div>Контрагент: "+predname+"</div>"+
				"<br>"+
				"<div>E-mail отклонившего: "+email+"</div>"+
				"<br>"+
				"<div>Причина отклонения: "+declinereason+"</div>"+
				"</div>"+
				"</div>";



		try {

			mail.sendEmail("Техническое отклонение",  mes);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			log.error(TypeConverter.exceptionToString(e));
		}




	}
	private boolean isUtochnenie(long docid) throws InterruptedException{
		Thread.sleep(3000);
		HashMap<String, Object> pp = new HashMap<String, Object>();
		pp.put("id", docid);
		if (npjt.queryForInt("select count(0) from snt.dfkorr where id =:id", pp)>0) return true;
		else return false;
	}

	public ETDDocumentData EditBeforeOpen(String docName, ETDDocumentData doc) {
		try{
			ETDForm form = ETDForm.createFromArchive(doc.getBlDoc());
			DataBinder b= form.getBinder();
			b.setNodeValue("documentId", String.valueOf(doc.getId()));
			doc.setBlDoc(form.encodeToArchiv());
		}catch (Exception e){
			log.error("Error in EditBeforeOpen: "+TypeConverter.exceptionToString(e));
		}
		return doc;
	}

}

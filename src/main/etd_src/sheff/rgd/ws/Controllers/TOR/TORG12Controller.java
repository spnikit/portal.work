package sheff.rgd.ws.Controllers.TOR;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.transform.TransformerException;

import org.apache.log4j.Logger;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.w3c.dom.DOMException;

import ru.aisa.rgd.etd.dao.ETDSyncServiceFacade;
import ru.aisa.rgd.etd.objects.ETDDocumentData;
import ru.aisa.rgd.utils.Base64;
import ru.aisa.rgd.ws.dao.ServiceFacade;
import ru.aisa.rgd.ws.documents.ETDForm;
import ru.aisa.rgd.ws.domain.BankRequisites;
import ru.aisa.rgd.ws.domain.Document;
import ru.aisa.rgd.ws.exeption.InternalException;
import ru.aisa.rgd.ws.exeption.ServiceException;
import ru.aisa.rgd.ws.utility.DataBinder;
import ru.aisa.rgd.ws.utility.RowFiller;
import ru.aisa.rgd.ws.utility.RowHandler;
import ru.aisa.rgd.ws.utility.TypeConverter;
import sheff.rgd.ws.Abstract.AbstractAction;
import sheff.rjd.services.syncutils.SyncObj;
import sheff.rjd.services.transoil.TransService;
import sheff.rjd.ws.OCO.TORLists;
import sheff.rjd.ws.OCO.AfterEdit.FakeSignature;
import sheff.rjd.ws.OCO.AfterSign.FictUserChecker;
//import sheff.rjd.ws.OCO.AfterEdit.TOR.TORG12_afterEdit.Addres;
//import sheff.rjd.ws.OCO.AfterEdit.TOR.TORG12_afterEdit.AddresList;
//import sheff.rjd.ws.OCO.AfterEdit.TOR.TORG12_afterEdit.Table;
//import sheff.rjd.ws.OCO.AfterEdit.TOR.TORG12_afterEdit.TableEntry;
import sheff.rjd.ws.OCO.AfterSign.SendToEtd;

import com.aisa.portal.invoice.operator.obj.OperatorObject;


public class TORG12Controller extends AbstractAction{
	
	private String parentform;
	private NamedParameterJdbcTemplate npjt;
	private SendToEtd sendtoetd;
	private FakeSignature fakesignature;
	protected final Logger	log	= Logger.getLogger(getClass());
	private TransService sendtotransoil;  
	private OperatorObject oper;
	private ServiceFacade facade;
	private ETDSyncServiceFacade etdsyncfacade;
	private FictUserChecker checker;
	
	
	public FictUserChecker getChecker() {
		return checker;
	}

	public void setChecker(FictUserChecker checker) {
		this.checker = checker;
	}

	public ServiceFacade getFacade() {
		return facade;
	}

	public void setFacade(ServiceFacade facade) {
		this.facade = facade;
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
	
	public FakeSignature getFakesignature() {
		return fakesignature;
	}
	
	public void setFakesignature(FakeSignature fakesignature) {
		this.fakesignature = fakesignature;
	}
	
	public void setParentform(String parentname) {
		this.parentform = parentname;
	}

	public String getParentform() {
		return parentform;
	}
	
	public OperatorObject getOper() {
		return oper;
	}

	public void setOper(OperatorObject oper) {
		this.oper = oper;
	}
	
	public ETDSyncServiceFacade getEtdsyncfacade() {
		return etdsyncfacade;
	}

	public void setEtdsyncfacade(ETDSyncServiceFacade etdsyncfacade) {
		this.etdsyncfacade = etdsyncfacade;
	}

	public void doAfterSign(String docName, String docdata, int predid, int signNumber,  long id, String certserial,int WrkId) {
		
		Map<String, Object> hm1 = new HashMap<String, Object>();
		hm1.put("id", id);
		hm1.put("wrkid", WrkId);
		int drop = getNpjt().queryForInt("select CASE when dropid is null then 0 else 1 end from snt.docstore where id = :id", hm1);
		int issm = getNpjt().queryForInt("select issm from snt.wrkname where id = :wrkid", hm1);

		Document doc = facade.getDocumentById(id);
			
		if (drop==1){
			hm1.put("reqnum", "Отклонен");
		    npjt.update("update snt.docstore set reqnum = :reqnum where id=:id", hm1);
		    
		    sendtotransoil.SendSigntoTransoil(id, signNumber, drop, predid);
		}
		
		hm1.put("predid", predid);
		int metod = npjt.queryForInt("select torgtype from snt.pred where id = (select case  when headid is null then id else headid end from snt.pred where id = :predid)", hm1);
		
		if((signNumber == 1) || (signNumber == 2)) {
			if(metod != 2) {
				sendtotransoil.SendSigntoTransoil(id, signNumber, drop, predid);
		}
		}
			
		if(signNumber==3){
			try{
			ETDForm form = ETDForm.createFromArchive(doc.getBlDoc());
			DataBinder b  = form.getBinder();
			
			String xmlText = b.getValue("xml_text1");
			String sign = b.getValue("xml_sign1");
			byte[] bXmlText = Base64.decode(xmlText);
			byte[] sign_file = Base64.decode(sign);
			
		    long typeid = facade.getDocumentDao().getTypeId(id);
		    
		    String id_pack = facade.getDocumentDao().getId_pack(id);
		    
		    facade.getDocumentDao().insertXMLandSign(bXmlText, sign_file, id, typeid, predid, id_pack);
			
			
			//Для генерации xml (для 4 подписи)
			//Start
				String xmlRussian = new String(bXmlText, "windows-1251");
				xmlRussian = xmlRussian.replace("<?xml version=\"1.0\" encoding=\"windows-1251\"?>", "");
				DataBinder binder = new DataBinder(xmlRussian);
				binder.setRootElement("Файл");
				String dateDoc = binder.getNode("Документ").getAttributes().getNamedItem("ДатаДок").getTextContent();
				String timeDoc = binder.getNode("Документ").getAttributes().getNamedItem("ВремДок").getTextContent();
				String idFileFirstSign = binder.getRootEl().getAttribute("ИдФайл");

				
				b.setNodeValue("IdFileTN", idFileFirstSign);
				b.setNodeValue("DateDocTN", dateDoc.replaceAll("\"", ""));
				b.setNodeValue("TimeDocTN", timeDoc.replaceAll("\"", ""));
				
				doc.setDocData(form.transform("data"));
				doc.setBlDoc(form.encodeToArchiv());
				facade.updateDocumentData(doc);
			}
			
			catch (Exception e)
			{
				e.printStackTrace();
				log.error(TypeConverter.exceptionToString(e));
			}
			//End
			
						
		    try {
			
			sendtoetd.SendToEtdMessage(id, new String(doc.getBlDoc(), "UTF-8"), parentform, signNumber, 0,true);
			sendtotransoil.SendSigntoTransoil(id, signNumber, drop, predid);
		    } catch (Exception e) {
		    	log.error(TypeConverter.exceptionToString(e));
		
		    } 
		    
		    hm1.put("reqnum", "Требует подписи ТОРГ-12");
		    npjt.update("update snt.docstore set reqnum = 'Подписан' where id=:id", hm1);
			npjt.update(
					"update snt.docstore set reqnum = :reqnum where etdid = (select etdid from snt.docstore where id_pak = (select id_pak from snt.docstore where id =:id) and groupsgn =1)",
					hm1);
           
			//заполнение отчета п.9
//		    Document doc = facade.getDocumentById(id);
		    try {
				ETDForm form = ETDForm.createFromArchive(doc.getBlDoc());
				DataBinder binder = form.getBinder();
				binder.setRootElement("data");
				Long actTMC = Long.parseLong(binder.getValue("P_69"));
				hm1.put("etdid", actTMC);
				String id_pak = npjt.queryForObject("select id_pak from snt.docstore where etdid = :etdid", hm1, String.class);
				hm1.put("id_pak", id_pak);
				java.sql.Date crdate = null;
		        Time crtime = null; 
		        String selectDateAndTime = "select crdate, crtime from snt.docstore "
				+ "where id = :id";
				SqlRowSet rs = npjt.queryForRowSet(selectDateAndTime, hm1);
				while(rs.next()) {
					crdate = rs.getDate("crdate");
					crtime = rs.getTime("crtime");
				}
				
				SimpleDateFormat myDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	        	java.util.Date DT_create_torg12 = myDateFormat.parse(crdate + " " + crtime);
	        	hm1.put("DT_create_torg12", DT_create_torg12);
				npjt.update("update snt.PGKREPORT set id_torg12 = :id, DT_create_torg12 = :DT_create_torg12 "
						+ "where id_pak = :id_pak", hm1);
			} catch (Exception e) {
				log.error(TypeConverter.exceptionToString(e));
			}
		    
		    
		}
		
	}


   public void doAfterSave(String docName, String docdata, int predid, int signNumber, String CertID, long id,int WrkId) throws Exception{
	   try{
		    
		 ETDForm form=ETDForm.createFromArchive(docdata.getBytes("UTF-8"));
			
		 DataBinder kinder=form.getBinder();

//		System.out.println("P_13: "+kinder.getValue("P_13")); 
//		 if (kinder.getValue("P_13").length()==0)
//		 {
//		     Map<String, Object>  pp = new HashMap<String, Object>();
//		
//		     int torg_seq= npjt.queryForInt("select next value for SNT.TORG_SEQ from SNT.wrkname fetch first row only", pp);
//		   
//		     kinder.setNodeValue("P_13", torg_seq);
//		     kinder.setNodeValue("nameoper", oper.getNameUrLic());
//		     kinder.setNodeValue("innoper", oper.getInn());
//		     kinder.setNodeValue("idoper", oper.getId());  
//		     
//		     
//		     
//		     
//		       pp.put("NO", torg_seq);
//		       pp.put("BLDOC", form.encodeToArchiv());
//			   pp.put("DOCDATA", form.transform("data"));
//			   pp.put("ID", id);
//			
//		    npjt.update("update snt.docstore set OPISANIE = :NO, BLDOC = :BLDOC, docdata = :DOCDATA where id = :ID", pp);	    
//		    
//		}
		
		 
		 HashMap<String, Object> hm1 = new HashMap<String, Object>();
		 hm1.put("wrkid", WrkId);
		 hm1.put("ID", id);
		 int fr = npjt.queryForInt("select issm from snt.wrkname where id = :wrkid", hm1);
		 int readid = npjt.queryForInt("select readid from snt.docstore where id =:ID", hm1);
		 
		 if (fr==10){
		     kinder.setNodeValue("nameoper", oper.getNameUrLic());
		     kinder.setNodeValue("innoper", oper.getInn());
		     kinder.setNodeValue("idoper", oper.getId());  
//			 npjt.update("update snt.docstore set visible = 1 where id =:ID", hm1);
			 hm1.put("BLDOC", form.encodeToArchiv());
			 hm1.put("DOCDATA", form.transform("data"));
			 hm1.put("NO", kinder.getValue("P_13"));
			 npjt.update("update snt.docstore set visible = 1,OPISANIE = :NO, BLDOC = :BLDOC, docdata = :DOCDATA where id = :ID", hm1);	    
		 }
		 
		 if (fr!=10&&readid==-1){
			 npjt.update("update snt.docstore set readid = 1, reqnum = 'Согласован' where id =:ID", hm1);
			 
			 hm1.put("predid", predid);
			 int metod = npjt.queryForInt("select torgtype from snt.pred where id = (select case  when headid is null then id else headid end from snt.pred where id = :predid)", hm1);

 			 if (metod==3||metod==1){
 				 try{
				 kinder.setNodeValue("nameoper", oper.getNameUrLic());
			     kinder.setNodeValue("innoper", oper.getInn());
			     kinder.setNodeValue("idoper", oper.getId());  
				 hm1.put("BLDOC", form.encodeToArchiv());
				 hm1.put("DOCDATA", form.transform("data"));
				 hm1.put("NO", kinder.getValue("P_13"));
				 npjt.update("update snt.docstore set BLDOC = :BLDOC, OPISANIE = :NO, docdata = :DOCDATA where id = :ID", hm1);
 				 } catch (Exception e) {
 					log.error(TypeConverter.exceptionToString(e));
 				 }
			 }
 			 kinder.setNodeValue("sign_open", 1);
 			 hm1.put("BLDOC_s", form.encodeToArchiv());
			 hm1.put("DOCDATA_s", form.transform("data"));
			 npjt.update("update snt.docstore set BLDOC = :BLDOC_s, docdata = :DOCDATA_s where id = :ID", hm1);	    
		 }
		 
		} catch (Exception e){
			log.error(TypeConverter.exceptionToString(e));
		    }
	}
   
   public void doAfterSync(String formname, SyncObj obj, String sql, int signum){
	   Map<String, Object> hm1 = new HashMap<String, Object>();
	   hm1.put("etdid", obj.getEtdid());
		int drop = getNpjt()
				.queryForInt(
						"select CASE when dropid is null then 0 else 1 end from snt.docstore where etdid = :etdid",
						hm1);
		if (signum == 4 && drop == 0) {
			Map<String, Object> pp = new HashMap<String, Object>();
			pp.put("typeid", obj.getTypeid());
			pp.put("order", signum);
			pp.put("id", obj.getDocid());
			
			//Обработка ФИО подписанта со стороны АС ЭТД, заведение фиктивного пользователя если его нет в бд
			ETDForm form = null;
			Integer persId = null;
			FictUserChecker checker = getChecker();
			try {
				form = ETDForm.createFromArchive(obj.getBldoc());
				persId = checker.getFictUser(form, "sign");
			} catch (Exception e1) {
				log.error(TypeConverter.exceptionToString(e1));
				persId = 0;
			}
			int wrkid = getNpjt()
					.queryForInt(
							"select wrkid from snt.doctypeflow where order = :order and dtid = :typeid ",
							pp);
			int predid = getNpjt().queryForInt(
					"select predid from snt.docstore where id =:id ", pp);
			fakesignature.fakesign(obj.getDocid(), persId, wrkid, predid, obj.getBldoc(),
					obj.getDocdata());

			pp.put("reqnum", "Подписан ТОРГ-12");
			pp.put("lpersid", persId);
			npjt.update("update snt.docstore set readid = 1, lpersid =:lpersid, reqnum = 'Подписан РЖД' where etdid =:id", pp);
			
			DataBinder binder = form.getBinder();
			try {
				String text2 = binder.getValue("xml_text2");
				String sign2 = binder.getValue("xml_sign2");
				byte [] file2 = Base64.decode(text2);
			    byte [] file_sign2 = Base64.decode(sign2);
			    
			    String id_pack = facade.getDocumentDao().getId_pack(obj.getDocid());
			    
			    facade.getDocumentDao().insertXMLandSign(file2, file_sign2, obj.getDocid(), obj.getTypeid(), predid, id_pack);
			} catch (Exception e) {
				log.error(TypeConverter.exceptionToString(e));
			}
				
			//Обновляем подписанта со стороны ржд
			npjt.update(
					"update snt.docstore set reqnum = :reqnum where etdid = (select etdid from snt.docstore where id_pak = (select id_pak from snt.docstore where id =:id) and groupsgn =1)",
					pp);
			pp.put("predid", predid);
			int typetorg  = getNpjt().queryForInt("select torgtype from snt.pred where id = "
					+ "(select case when headid is null then id else headid end from snt.pred where id = :predid)", pp);
			
			npjt.update("update snt.docstoreflow set persid =:lpersid where order = 4 and docid = (select id from snt.docstore where etdid = :id)", pp);
			
			sendtotransoil.SendSigntoTransoil(obj.getDocid(), signum, drop, predid);
			
			//Create SF 
			if (typetorg!=4){
			try{
				createSF(obj.getBldoc(), predid, obj.getEtdid(), typetorg);
			} catch (Exception e){
				log.error(TypeConverter.exceptionToString(e));
			}
			}
		}

		if (drop == 1/*&&signum==0*/) {
			// Отклонение пакета

			Map<String, Object> pp = new HashMap<String, Object>();
			pp.put("id", obj.getEtdid());
			try {
// 				CREATE NEW TORG
//				pp.put("reqnum", "Отказ подписи ТОРГ-12");
//				npjt.update("update snt.docstore set readid = 1, reqnum = 'Отклонен РЖД' where etdid =:id", pp);
//				npjt.update(
//						"update snt.docstore set reqnum = :reqnum where etdid = (select etdid from snt.docstore where id_pak = (select id_pak from snt.docstore where etdid =:id) and groupsgn =1)",
//						pp);
//				int predid = getNpjt().queryForInt(
//						"select predid from snt.docstore where etdid =:id ", pp);
//				
//				pp.put("predid", predid);
//				int typetorg  = getNpjt().queryForInt("select torgtype from snt.pred where id = "
//						+ "(select case when headid is null then id else headid end from snt.pred where id = :predid)", pp);
//				 
//				if (typetorg!=4)
//
//				createTORG12(obj.getBldoc(), predid, typetorg);
				
//				DROP TORG-12 AFTER ETD
				
				//Обработка ФИО подписанта со стороны АС ЭТД, заведение фиктивного пользователя если его нет в бд
				ETDForm form = null;
				Integer persId = null;
				FictUserChecker checker = getChecker();
				try {
					form = ETDForm.createFromArchive(obj.getBldoc());
					persId = checker.getFictUser(form, "decline");
				} catch (Exception e1) {
					log.error(TypeConverter.exceptionToString(e1));
					persId = 0;
				}

				pp.put("dropid", persId);
				pp.put("reason", obj.getDropreason());
				pp.put("ETDID", obj.getEtdid());
				pp.put("reqnum", "Отклонен РЖД");
				pp.put("BLOB", obj.getBldoc());
				pp.put("docdata", obj.getDocdata());
				getNpjt()
						.update("update snt.docstore set droptime = CURRENT TIMESTAMP,"
								+ "droptxt = :reason,"
								+ "dropid = :dropid, "
								+ "droppredid = (select predid from snt.docstore where etdid = :ETDID), "
								+ "bldoc =:BLOB, "
								+ "docdata = :docdata, "
								+ "reqnum =:reqnum, "
								+ "dropwrkid = (select id from snt.wrkname where issm=4 fetch first row only) where etdid = :ETDID",
								pp);
				
				
				//Отправка уведомления в АСУ об отклонении
				int predid = getNpjt().queryForInt(
						"select predid from snt.docstore where etdid =:id ", pp);
				int docid = getNpjt().queryForInt("select id from snt.docstore where etdid =:id", pp);
				
				
				sendtotransoil.SendSigntoTransoil(docid, signum, drop, predid);
				
				//заполнение отчета п.9
				pp.put("id_torg12", obj.getDocid());
				npjt.update("update snt.PGKREPORT set id_torg12 = null, DT_create_torg12 = null "
						+ "where id_torg12 = :id_torg12", pp);

				
			} catch (Exception e) {
				log.error(TypeConverter.exceptionToString(e));
			}

			// TODO отправка уведомлений
		}

	}
   
   public ETDDocumentData EditBeforeOpen(String docName, ETDDocumentData doc) {
//	   try{
//			ETDForm form = ETDForm.createFromArchive(doc.getBlDoc());
//			DataBinder b= form.getBinder();
//			b.setNodeValue("documentId", String.valueOf(doc.getId()));
//			doc.setBlDoc(form.encodeToArchiv());
//			}catch (Exception e){
//				log.error("Error in EditBeforeOpen: "+TypeConverter.exceptionToString(e));
//			}
		return doc;
	}
   

   
  private void createTORG12(byte[] docdata, int predId, int typetorg) throws UnsupportedEncodingException, ServiceException, IOException, DOMException, InternalException, TransformerException{
	  
	  ETDForm form = ETDForm.createFromArchive(docdata);
       DataBinder binder = form.getBinder();
       ETDForm newTORG12Form = new ETDForm(facade.getDocumentTemplate("ТОРГ-12"));
	   DataBinder newTORG12binder = newTORG12Form.getBinder();
	   final Map<String, String> torgmap = new HashMap<String, String>();
       newTORG12binder.setRootElement("data");
	   newTORG12binder.setNodeValue("flag_ASU", binder.getValue("flag_ASU"));
		
		// Gruzotpr
		newTORG12binder.setNodeValue("P_1a", binder.getValue("P_1a"));
		if (binder.getValue("P_1v").length() > 0) {
		newTORG12binder.setNodeValue("P_1v", binder.getValue("P_1v"));
		}
		if (binder.getValue("P_1g").length() > 0) {
		newTORG12binder.setNodeValue("P_1g", binder.getValue("P_1g"));
		}
		if (binder.getValue("P_1d").length() > 0) {
		newTORG12binder.setNodeValue("P_1d", binder.getValue("P_1d"));
		}
		if (binder.getValue("P_1e").length() > 0) {
		newTORG12binder.setNodeValue("P_1e", binder.getValue("P_1e"));
		}
		if (binder.getValue("P_1j").length() > 0) {
		newTORG12binder.setNodeValue("P_1j", binder.getValue("P_1j"));
		}
		if (binder.getValue("P_1z").length() > 0) {
		newTORG12binder.setNodeValue("P_1z", binder.getValue("P_1z"));
		}
		if (binder.getValue("P_1i").length() > 0) {
		newTORG12binder.setNodeValue("P_1i", binder.getValue("P_1i"));
		}
		if (binder.getValue("P_1k").length() > 0) {
		newTORG12binder.setNodeValue("P_1k", binder.getValue("P_1k"));
		}
		if (binder.getValue("P_2").length() > 0) {
		newTORG12binder.setNodeValue("P_2", binder.getValue("P_2"));
		}
        
		binder.handleTable("GruzOtprAddr", "row", new RowHandler<Object>() {
			public void handleRow(DataBinder kinder, int rowNum, Object obj) throws InternalException
			{
				torgmap.put("kod", kinder.getValue("kod"));
				torgmap.put("ind", kinder.getValue("ind"));
				torgmap.put("raion", kinder.getValue("raion"));
				torgmap.put("punkt", kinder.getValue("punkt"));
				torgmap.put("town", kinder.getValue("town"));
				torgmap.put("street", kinder.getValue("street"));
				torgmap.put("house", kinder.getValue("house"));
				torgmap.put("korp", kinder.getValue("korp"));
				torgmap.put("flat", kinder.getValue("flat"));
			}
		}, null);
		
		newTORG12binder.handleTable("GruzOtprAddr", "row", new RowHandler<Object>() {
			public void handleRow(DataBinder kinder, int rowNum, Object obj) throws InternalException
			{
				kinder.setNodeValue("kod", torgmap.get("kod"));
				kinder.setNodeValue("ind", torgmap.get("ind"));
				kinder.setNodeValue("raion", torgmap.get("raion"));
				kinder.setNodeValue("punkt", torgmap.get("punkt"));
				kinder.setNodeValue("town", torgmap.get("town"));
				kinder.setNodeValue("street", torgmap.get("street"));
				kinder.setNodeValue("house", torgmap.get("house"));
				kinder.setNodeValue("korp", torgmap.get("korp"));
				kinder.setNodeValue("flat", torgmap.get("flat"));
			}
		}, null);
		
		if (binder.getValue("P_1b").length() > 0) {
			newTORG12binder.setNodeValue("P_1b", binder.getValue("P_1b"));
		}
		
		torgmap.clear();
		
		binder.handleTable("GruzOtprAddrIn", "row", new RowHandler<Object>() {
			public void handleRow(DataBinder kinder, int rowNum, Object obj) throws InternalException
			{
				torgmap.put("kod", kinder.getValue("kod"));
				torgmap.put("text", kinder.getValue("text"));
			}
		}, null);
		
		newTORG12binder.handleTable("GruzOtprAddrIn", "row", new RowHandler<Object>() {
			public void handleRow(DataBinder kinder, int rowNum, Object obj) throws InternalException
			{
				kinder.setNodeValue("kod", torgmap.get("kod"));
				kinder.setNodeValue("text", torgmap.get("text"));
			}
		}, null);
		
		torgmap.clear();
		
		// Gruzopoluch
		newTORG12binder.setNodeValue("P_4", binder.getValue("P_4"));
		if (binder.getValue("P_4b").length() > 0) {
			newTORG12binder.setNodeValue("P_4b", binder.getValue("P_4b"));
		}
		if (binder.getValue("P_4v").length() > 0) {
			newTORG12binder.setNodeValue("P_4v", binder.getValue("P_4v"));
		}
		
		binder.handleTable("GruzPolAddr", "row", new RowHandler<Object>() {
			public void handleRow(DataBinder kinder, int rowNum, Object obj) throws InternalException
			{
				torgmap.put("kod", kinder.getValue("kod"));
				torgmap.put("ind", kinder.getValue("ind"));
				torgmap.put("raion", kinder.getValue("raion"));
				torgmap.put("punkt", kinder.getValue("punkt"));
				torgmap.put("town", kinder.getValue("town"));
				torgmap.put("street", kinder.getValue("street"));
				torgmap.put("house", kinder.getValue("house"));
				torgmap.put("korp", kinder.getValue("korp"));
				torgmap.put("flat", kinder.getValue("flat"));
			}
		}, null);
		
		newTORG12binder.handleTable("GruzPolAddr", "row", new RowHandler<Object>() {
			public void handleRow(DataBinder kinder, int rowNum, Object obj) throws InternalException
			{
				kinder.setNodeValue("kod", torgmap.get("kod"));
				kinder.setNodeValue("ind", torgmap.get("ind"));
				kinder.setNodeValue("raion", torgmap.get("raion"));
				kinder.setNodeValue("punkt", torgmap.get("punkt"));
				kinder.setNodeValue("town", torgmap.get("town"));
				kinder.setNodeValue("street", torgmap.get("street"));
				kinder.setNodeValue("house", torgmap.get("house"));
				kinder.setNodeValue("korp", torgmap.get("korp"));
				kinder.setNodeValue("flat", torgmap.get("flat"));
			}
		}, null);
		
		if (binder.getValue("P_4a").length() > 0) {
			newTORG12binder.setNodeValue("P_4a", binder.getValue("P_4a"));
		}
		
		torgmap.clear();
		
		binder.handleTable("GruzPolAddrIn", "row", new RowHandler<Object>() {
			public void handleRow(DataBinder kinder, int rowNum, Object obj) throws InternalException
			{
				torgmap.put("kod", kinder.getValue("kod"));
				torgmap.put("text", kinder.getValue("text"));
			}
		}, null);
		
		newTORG12binder.handleTable("GruzPolAddrIn", "row", new RowHandler<Object>() {
			public void handleRow(DataBinder kinder, int rowNum, Object obj) throws InternalException
			{
				kinder.setNodeValue("kod", torgmap.get("kod"));
				kinder.setNodeValue("text", torgmap.get("text"));
			}
		}, null);
		
		torgmap.clear();
		
		// Post
		newTORG12binder.setNodeValue("P_5", binder.getValue("P_5"));
		newTORG12binder.setNodeValue("P_5z", binder.getValue("P_5z"));
		newTORG12binder.setNodeValue("P_5i",binder.getValue("P_5i"));
		if (binder.getValue("P_5b").length() > 0) {
			newTORG12binder.setNodeValue("P_5b", binder.getValue("P_5b"));
		}
		if (binder.getValue("P_5v").length() > 0) {
			newTORG12binder.setNodeValue("P_5v", binder.getValue("P_5v"));
		}
//		String predname = etdsyncfacade.getNamebyPredid(predId);
		try{
			BankRequisites req = new BankRequisites();
			req = facade.getContragDao().getBankRequisitesByPredId(predId);
			newTORG12binder.setNodeValue("P_5g", req.getAccount());
			newTORG12binder.setNodeValue("P_5d", req.getBankname());
			newTORG12binder.setNodeValue("P_5j", req.getKorraccount());
			newTORG12binder.setNodeValue("P_5e", req.getBik());
		} catch (Exception e){
			log.error(TypeConverter.exceptionToString(e));
		}
		
		newTORG12binder.setNodeValue("P_5_1a", binder.getValue("P_5_1a"));
		
		binder.handleTable("PostAddr", "row", new RowHandler<Object>() {
			public void handleRow(DataBinder kinder, int rowNum, Object obj) throws InternalException
			{
				torgmap.put("kod", kinder.getValue("kod"));
				torgmap.put("ind", kinder.getValue("ind"));
				torgmap.put("raion", kinder.getValue("raion"));
				torgmap.put("punkt", kinder.getValue("punkt"));
				torgmap.put("town", kinder.getValue("town"));
				torgmap.put("street", kinder.getValue("street"));
				torgmap.put("house", kinder.getValue("house"));
				torgmap.put("korp", kinder.getValue("korp"));
				torgmap.put("flat", kinder.getValue("flat"));
			}
		}, null);
		
		newTORG12binder.handleTable("PostAddr", "row", new RowHandler<Object>() {
			public void handleRow(DataBinder kinder, int rowNum, Object obj) throws InternalException
			{
				kinder.setNodeValue("kod", torgmap.get("kod"));
				kinder.setNodeValue("ind", torgmap.get("ind"));
				kinder.setNodeValue("raion", torgmap.get("raion"));
				kinder.setNodeValue("punkt", torgmap.get("punkt"));
				kinder.setNodeValue("town", torgmap.get("town"));
				kinder.setNodeValue("street", torgmap.get("street"));
				kinder.setNodeValue("house", torgmap.get("house"));
				kinder.setNodeValue("korp", torgmap.get("korp"));
				kinder.setNodeValue("flat", torgmap.get("flat"));
			}
		}, null);
		
		if (binder.getValue("P_5a").length() > 0) {
			newTORG12binder.setNodeValue("P_5a", binder.getValue("P_5a"));
		}
		
		torgmap.clear();
		
		binder.handleTable("PostAddrIn", "row", new RowHandler<Object>() {
			public void handleRow(DataBinder kinder, int rowNum, Object obj) throws InternalException
			{
				torgmap.put("kod", kinder.getValue("kod"));
				torgmap.put("text", kinder.getValue("text"));
			}
		}, null);
		
		newTORG12binder.handleTable("PostAddrIn", "row", new RowHandler<Object>() {
			public void handleRow(DataBinder kinder, int rowNum, Object obj) throws InternalException
			{
				kinder.setNodeValue("kod", torgmap.get("kod"));
				kinder.setNodeValue("text", torgmap.get("text"));
			}
		}, null);
		
		torgmap.clear();
		
		// Buyer
		newTORG12binder.setNodeValue("P_6",  binder.getValue("P_6"));
		newTORG12binder.setNodeValue("P_6v", binder.getValue("P_6v"));
		newTORG12binder.setNodeValue("P_6b", binder.getValue("P_6b"));
		newTORG12binder.setNodeValue("P_6g", binder.getValue("P_6g"));
		newTORG12binder.setNodeValue("P_6d", binder.getValue("P_6d"));
		newTORG12binder.setNodeValue("P_6e", binder.getValue("P_6e"));
		newTORG12binder.setNodeValue("P_6j", binder.getValue("P_6j"));
		newTORG12binder.setNodeValue("P_6_1a", binder.getValue("P_6_1a"));
		
		binder.handleTable("BuyerAddr", "row", new RowHandler<Object>() {
			public void handleRow(DataBinder kinder, int rowNum, Object obj) throws InternalException
			{
				torgmap.put("kod", kinder.getValue("kod"));
				torgmap.put("ind", kinder.getValue("ind"));
				torgmap.put("raion", kinder.getValue("raion"));
				torgmap.put("punkt", kinder.getValue("punkt"));
				torgmap.put("town", kinder.getValue("town"));
				torgmap.put("street", kinder.getValue("street"));
				torgmap.put("house", kinder.getValue("house"));
				torgmap.put("korp", kinder.getValue("korp"));
				torgmap.put("flat", kinder.getValue("flat"));
			}
		}, null);
		
		newTORG12binder.handleTable("BuyerAddr", "row", new RowHandler<Object>() {
			public void handleRow(DataBinder kinder, int rowNum, Object obj) throws InternalException
			{
				kinder.setNodeValue("kod", torgmap.get("kod"));
				kinder.setNodeValue("ind", torgmap.get("ind"));
				kinder.setNodeValue("raion", torgmap.get("raion"));
				kinder.setNodeValue("punkt", torgmap.get("punkt"));
				kinder.setNodeValue("town", torgmap.get("town"));
				kinder.setNodeValue("street", torgmap.get("street"));
				kinder.setNodeValue("house", torgmap.get("house"));
				kinder.setNodeValue("korp", torgmap.get("korp"));
				kinder.setNodeValue("flat", torgmap.get("flat"));
			}
		}, null);
		
		if (binder.getValue("P_6a").length() > 0) {
			newTORG12binder.setNodeValue("P_6a", binder.getValue("P_6a"));
		}
		
		torgmap.clear();
		
		binder.handleTable("BuyerAddrIn", "row", new RowHandler<Object>() {
			public void handleRow(DataBinder kinder, int rowNum, Object obj) throws InternalException
			{
				torgmap.put("kod", kinder.getValue("kod"));
				torgmap.put("text", kinder.getValue("text"));
			}
		}, null);
		
		newTORG12binder.handleTable("BuyerAddrIn", "row", new RowHandler<Object>() {
			public void handleRow(DataBinder kinder, int rowNum, Object obj) throws InternalException
			{
				kinder.setNodeValue("kod", torgmap.get("kod"));
				kinder.setNodeValue("text", torgmap.get("text"));
			}
		}, null);
		
		torgmap.clear();

		newTORG12binder.setNodeValue("P_7", binder.getValue("P_7"));
		newTORG12binder.setNodeValue("P_8", binder.getValue("P_8"));
		newTORG12binder.setNodeValue("P_9", binder.getValue("P_9"));
//		newTORG12binder.setNodeValue("P_13", binder.getValue("P_13"));
		newTORG12binder.setNodeValue("P_14", binder.getValue("P_14"));

		// Заполнение таблицы ТН
		newTORG12binder.setNodeValue("P_15", binder.getValue("P_15"));
		newTORG12binder.setNodeValue("P_16", binder.getValue("P_16"));
		newTORG12binder.setNodeValue("P_16.1", binder.getValue("P_16.1"));
		newTORG12binder.setNodeValue("P_16.2", binder.getValue("P_16.2"));
		if(binder.getValue("P_17").length() > 0) {
		newTORG12binder.setNodeValue("P_17", binder.getValue("P_17"));
		}
		newTORG12binder.setNodeValue("P_18", binder.getValue("P_18"));
		newTORG12binder.setNodeValue("P_19", binder.getValue("P_19"));
		newTORG12binder.setNodeValue("P_24", binder.getValue("P_24"));
		newTORG12binder.setNodeValue("P_25", binder.getValue("P_25"));
		newTORG12binder.setNodeValue("P_26", binder.getValue("P_26"));
		newTORG12binder.setNodeValue("P_27", binder.getValue("P_27"));
		newTORG12binder.setNodeValue("P_28", binder.getValue("P_28"));
		newTORG12binder.setNodeValue("P_29", binder.getValue("P_29"));

		// Itogo
		newTORG12binder.setNodeValue("P_32", binder.getValue("P_32"));
		newTORG12binder.setNodeValue("P_33", binder.getValue("P_33"));
		newTORG12binder.setNodeValue("P_34", binder.getValue("P_34"));
		newTORG12binder.setNodeValue("P_35", binder.getValue("P_35"));

		// Vsego
		newTORG12binder.setNodeValue("P_38", binder.getValue("P_38"));
		newTORG12binder.setNodeValue("P_39", binder.getValue("P_39"));
		newTORG12binder.setNodeValue("P_40", binder.getValue("P_40"));
		newTORG12binder.setNodeValue("P_41", binder.getValue("P_41"));
		newTORG12binder.setNodeValue("P_44", binder.getValue("P_44"));
		newTORG12binder.setNodeValue("P_44d", binder.getValue("P_44d"));
		newTORG12binder.setNodeValue("P_44m", binder.getValue("P_44m"));
		newTORG12binder.setNodeValue("P_44ms", binder.getValue("P_44ms"));
		newTORG12binder.setNodeValue("P_44y", binder.getValue("P_44y"));
		newTORG12binder.setNodeValue("P_46", binder.getValue("P_46"));
		newTORG12binder.setNodeValue("P_51", binder.getValue("P_51"));
		newTORG12binder.setNodeValue("P_51_1", binder.getValue("P_51_1"));
		newTORG12binder.setNodeValue("P_51_2", binder.getValue("P_51_2"));
		newTORG12binder.setNodeValue("P_56", binder.getValue("P_56"));
		newTORG12binder.setNodeValue("P_58", binder.getValue("P_58"));
		newTORG12binder.setNodeValue("P_59", binder.getValue("P_59"));
		newTORG12binder.setNodeValue("P_69", binder.getValue("P_69"));

		Map<String, Object> map = new HashMap<String, Object>();
	
        Document document = new Document();
        Long docId = null;
		document.setBlDoc(newTORG12Form.encodeToArchiv());
		document.setDocData(newTORG12Form.transform("data"));
		document.setSignLvl(0); 
		document.setPredId(predId);
		document.setType("ТОРГ-12");
		docId =  facade.insertDocument(document);
		map.put("ID", docId);
		map.put("vagnum", newTORG12binder.getValue("P_58"));
		map.put("etdid", newTORG12binder.getValue("P_69"));
		//код услуги
		map.put("di", "08");
		map.put("dogovor", newTORG12binder.getValue("P_8")+" от "+newTORG12binder.getValue("P_9"));
		map.put("onbaseid", Integer.parseInt(newTORG12binder.getValue("P_59")));
		map.put("repdate", newTORG12binder.getValue("P_44"));

		
		int visible =-1;
		int readid = -1;
		
		switch(typetorg) {
		case 1: visible =1;
				readid=-1;
				break;
		case 2: visible =0;
				readid=-1;
				break;
		case 3: visible =1;
				readid=-1;
				break;
		}
		
		map.put("visible", visible);
		map.put("readid", readid);
		
		
		
		
		npjt.update("update snt.docstore set readid = :readid, visible = :visible, di=:di, vagnum =:vagnum, no =:dogovor, onbaseid =:onbaseid, "
				+ "id_pak = (select id_pak from snt.docstore where etdid =:etdid), repdate = :repdate where id =:ID ", map);

   }
   
	
	private void createSF(byte[] docdata, int predId, long torgetdid, int typetorg) throws UnsupportedEncodingException, ServiceException, IOException, DOMException, InternalException{
		
		
		
		
		ETDForm torgform = ETDForm.createFromArchive(docdata);

		final DataBinder torgbinder = torgform.getBinder();

		ETDForm sffrom = new ETDForm(
				facade.getDocumentTemplate(TORLists.SF));
		DataBinder sfbinder = sffrom.getBinder();
		
		try{
		final String[] NameAddresSF  = {"SellAddr", "SendAddr", "RecvAddr", "CustAddr"};
		
		final String[] NameAddresTorg12  = {"PostAddr", "GruzOtprAddr", "GruzPolAddr", "BuyerAddr"};
		
		sfbinder.setNodeValue("date_ot",torgbinder.getValue("P_14"));
		sfbinder.setNodeValue("prodavec",torgbinder.getValue("P_5"));
		sfbinder.setNodeValue("adres_prodavca",torgbinder.getValue("P_5a"));
		sfbinder.setNodeValue("inn_prodavca",torgbinder.getValue("P_5z")+"/"+torgbinder.getValue("P_5i"));
		sfbinder.setNodeValue("gryzootprav",torgbinder.getValue("P_1")+", "+torgbinder.getValue("P_1a"));
		sfbinder.setNodeValue("gryzopolych",torgbinder.getValue("P_4")+", "+torgbinder.getValue("P_4a"));
//		sfbinder.setNodeValue("gryzootprav",torgbinder.getValue("P_1all"));
//		sfbinder.setNodeValue("gryzopolych",torgbinder.getValue("P_4all"));
		sfbinder.setNodeValue("pokypatel",torgbinder.getValue("P_6"));
		sfbinder.setNodeValue("adres_pokypatel",torgbinder.getValue("P_6a"));
		sfbinder.setNodeValue("inn_pokypatel",torgbinder.getValue("P_6v")+"/"+torgbinder.getValue("P_6b"));
		sfbinder.setNodeValue("valyuta","643");
		sfbinder.setNodeValue("currency","643");
		sfbinder.setNodeValue("currency_name","российский рубль");
		
		Table t = new Table();
		
		
		torgbinder.handleTable("table1", "row", new RowHandler<Table>(){
					public void handleRow(DataBinder b, int rowNum,Table t) throws InternalException{
						
						TableEntry te = new TableEntry();
						
						te.numrow = b.getValue("P_15");
						te.denomination = b.getValue("P_16");
						te.kod_ed_iz = b.getValue("P_19");
						te.symbol_ed_iz = b.getValue("P_18");
						te.amount = b.getValue("P_24");
						te.price = b.getValue("P_25");
						te.sumExNDS = b.getValue("P_26");
						te.rate = b.getValue("P_27");
						te.sumNDS = b.getValue("P_28");
						te.sumWithNDS = b.getValue("P_29");
						
						t.rowList.add(te);
					}
					
		},t);
		
		
		
		
		sfbinder.setRootElement("data");
		
		TableEntry [] tea = t.rowList.toArray(new TableEntry[t.rowList.size()]);

		sfbinder.fillTable(tea, new RowFiller<TableEntry, Object>()
				{
					public void fillRow(DataBinder b, TableEntry te, int numRow, Object opt) throws DOMException, InternalException{
						b.setNodeValue("name_tovar", /*te.numrow+" "+*/te.denomination);
						b.setNodeValue("kod_ed_izmer", te.kod_ed_iz);
						b.setNodeValue("us_obozn", te.symbol_ed_iz);
						b.setNodeValue("kolvo", te.amount);
						b.setNodeValue("cena", te.price);
						b.setNodeValue("stoimoct_bez_nalog", te.sumExNDS);
						b.setNodeValue("akciz", te.sumexcise);
						b.setNodeValue("nalog_stavka", te.rate/*+te.typerate*/);
						b.setNodeValue("sum_nalog", te.sumNDS);
						b.setNodeValue("stoimoct_s_nalog", te.sumWithNDS);
						b.setNodeValue("kod_strana", te.codeorigin);
						b.setNodeValue("sname_strana", te.countryorigin);
						b.setNodeValue("num_deklar", te.numTD);
						b.setNodeValue("sum_nalog", te.sumNDS);
						
						
					}

				}
				
				, "tabel", "row");
		
		
		sfbinder.setNodeValue("vsego_stoimost_bez_nalog",torgbinder.getValue("P_39"));
		sfbinder.setNodeValue("vsego_nalog",torgbinder.getValue("P_40"));
		sfbinder.setNodeValue("vsego_stoimost_s_nalog",torgbinder.getValue("P_41"));
		
		sfbinder.setNodeValue("gp",sfbinder.getValue("gryzopolych"));
		sfbinder.setNodeValue("go",sfbinder.getValue("gryzootprav"));
		
		AddresList addres = new AddresList();

		for (int i=0; i < NameAddresTorg12.length; i++ )
		{
			addres.rowList.clear();
			
			torgbinder.handleTable(NameAddresTorg12[i], "row", new RowHandler<AddresList>(){
				public void handleRow(DataBinder b, int rowNum, AddresList a) throws InternalException{
					
					Addres ad = new Addres();
					
					ad.ind = b.getValue("ind");
					ad.kod = b.getValue("kod");
					ad.raion = b.getValue("raion");
					ad.punkt = b.getValue("punkt");
					ad.town = b.getValue("town");
					ad.street = b.getValue("street");
					ad.house = b.getValue("house");
					ad.korp = b.getValue("korp");
					ad.flat = b.getValue("flat");
					
					a.rowList.add(ad);
				}
				
			},addres);
		
		
			sfbinder.setRootElement("data");
		
		Addres [] add = addres.rowList.toArray(new Addres[addres.rowList.size()]);

		sfbinder.fillTable(add, new RowFiller<Addres, Object>()
				{
					public void fillRow(DataBinder b, Addres ad, int numRow, Object opt) throws DOMException, InternalException{
						
						b.setNodeValue("ind", ad.ind);
						b.setNodeValue("kod",ad.kod);
						b.setNodeValue("raion", ad.raion);
						b.setNodeValue("punkt", ad.punkt);
						b.setNodeValue("town", ad.town);
						b.setNodeValue("street", ad.street);
						b.setNodeValue("house", ad.house);
						b.setNodeValue("korp", ad.korp);
						b.setNodeValue("flat", ad.flat);					
						
					}

				}
				
				, NameAddresSF[i], "row");
		}
		} catch (Exception e){
			log.error(TypeConverter.exceptionToString(e));
		}
		String[] innkppsell = sfbinder.getValue("inn_prodavca").split("/");
		String[] innkppbuy = sfbinder.getValue("inn_pokypatel").split("/");
		try
			{
			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("innsell", innkppsell[0]);
			map.put("kppsell", innkppsell[1]);
			map.put("innbuy", innkppbuy[0]);
			map.put("kppbuy", innkppbuy[1]);
			String cabinetId = (String) getNpjt().queryForObject(
					"Select contr_code from snt.pred where inn = :innsell and kpp =:kppsell and headid is null", map,
					String.class);
			String cabinetIdContr = (String) getNpjt().queryForObject(
							"Select contr_code from snt.pred where inn = :innbuy and kpp =:kppbuy and headid is null",
							map, String.class);
			
			
			
			sfbinder.setNodeValue("cabinetIdSell", cabinetId);
			sfbinder.setNodeValue("cabinetIdRecv", cabinetIdContr);
		
			sfbinder.setNodeValue("nameoper", oper.getNameUrLic());
			sfbinder.setNodeValue("innoper", oper.getInn());
			sfbinder.setNodeValue("idoper", oper.getId());
			
			
			}
		catch (Exception e)
			{
			log.error("No cabinet for contrcode = " +TypeConverter.exceptionToString(e));
			
			}
		
		try{
			sfbinder.setNodeValue("id_torg12_atd", torgetdid);
		}
		catch (Exception e)
		{
			log.error("No tag id_torg12_atd in SF " +TypeConverter.exceptionToString(e));
			
			}
		
		try{
			sfbinder.setNodeValue("vid_sf", 1);
		}
		catch (Exception e)
		{
			log.error("No tag vid_sf in SF " +TypeConverter.exceptionToString(e));
			
			}
		try{
			sfbinder.setNodeValue("flag_ASU", 0);
		} catch (Exception e){
			log.error("No tag flag_ASU in SF " +TypeConverter.exceptionToString(e));
		}
		
		try{
		Document document = new Document();
		long docid = facade.getNextDocumentId();
		sfbinder.setNodeValue("documentId", docid);
		document.setId(docid);
		document.setBlDoc(sffrom.encodeToArchiv());
		document.setDocData(sffrom.transform("data"));
		document.setSignLvl(0);
		document.setPredId(predId);
		document.setType(TORLists.SF);
		
		facade.insertDocumentWithDocid(document);
		
		HashMap<String, Object> sftype = new HashMap<String, Object>();
		sftype.put("id", docid);
		sftype.put("sf_gfsgn", -1);
		int visible =-1;
		int readid = -1;
		
		switch(typetorg) {
		case 1: visible =1;
				readid=-1;
				break;
		case 2: visible =0;
				readid=-1;
				break;
		case 3: visible =1;
				readid=-1;
				break;
		}
		
		sftype.put("visible", visible);
		sftype.put("readid", readid);
		facade.getNpjt().update("update snt.docstore set sf_type = 2, sf_gfsgn =:sf_gfsgn, visible=:visible , readid = :readid where id =:id", sftype);
		
		} catch (Exception e){
			log.error(TypeConverter.exceptionToString(e));
		}
		
	}
	private class Table
	{
		public List<TableEntry> rowList = new ArrayList<TableEntry>();

	}
	
	
	private class TableEntry
	{
		public String numrow;
		public String denomination;
		public String kod_ed_iz;
		public String symbol_ed_iz;
		public String amount;
		public String price;
		public String sumExNDS;
		final public String sumexcise = "без акциза";
		public String rate;
		final public String typerate = " процент";
		public String sumNDS;
		public String sumWithNDS;
		final public String codeorigin = "";
		final public String countryorigin = "";
		final public String numTD = "";
		
	}
	
	private class AddresList {
		public List<Addres> rowList = new ArrayList<Addres>();

	}
	
	private class Addres{
		
		public String ind;
		public String kod;
		public String raion;
		public String punkt;
		public String town;
		public String street;
		public String house;
		public String korp;
		public String flat;
		
	}
}
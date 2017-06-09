package sheff.rgd.ws.Controllers.TOR;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import ru.aisa.rgd.etd.dao.ETDSyncServiceFacade;
import ru.aisa.rgd.etd.objects.ETDDocumentData;
import ru.aisa.rgd.utils.Base64;
import ru.aisa.rgd.ws.dao.ServiceFacade;
import ru.aisa.rgd.ws.documents.ETDForm;
import ru.aisa.rgd.ws.domain.Document;
import ru.aisa.rgd.ws.exeption.InternalException;
import ru.aisa.rgd.ws.exeption.ServiceException;
import ru.aisa.rgd.ws.utility.DataBinder;
import ru.aisa.rgd.ws.utility.TypeConverter;
import sheff.rgd.ws.Abstract.AbstractAction;
import sheff.rjd.services.syncutils.SyncObj;
import sheff.rjd.services.transoil.TransService;
import sheff.rjd.ws.OCO.AfterSign.SendToEtd;



public class FPU26Controller extends AbstractAction{
	
	private String parentform;
	private ETDSyncServiceFacade etdsyncfacade;
	private NamedParameterJdbcTemplate npjt;
	private SendToEtd sendtoetd;
	private TransService sendtotransoil;  
	private ServiceFacade facade;
	protected final Logger	log	= Logger.getLogger(getClass());
	
	public NamedParameterJdbcTemplate getNpjt() {
		return npjt;
	}
	
	public void setNpjt(NamedParameterJdbcTemplate npjt) {
		this.npjt = npjt;
	}
	
	public void setParentform(String parentname) {
		this.parentform = parentname;
	}

	public String getParentform() {
		return parentform;
	}
	public SendToEtd getSendtoetd() {
	    return sendtoetd;
	}
	public TransService getSendtotransoil() {
		return sendtotransoil;
	}
	public void setSendtotransoil(TransService sendtotransoil) {
		this.sendtotransoil = sendtotransoil;
	}
	public void setSendtoetd(SendToEtd sendtoetd) {
	    this.sendtoetd = sendtoetd;
	}
	
	public ETDSyncServiceFacade getEtdsyncfacade() {
		return etdsyncfacade;
	}

	public void setEtdsyncfacade(ETDSyncServiceFacade etdsyncfacade) {
		this.etdsyncfacade = etdsyncfacade;
	}
	
	public ServiceFacade getFacade() {
		return facade;
	}

	public void setFacade(ServiceFacade facade) {
		this.facade = facade;
	}

	@Override
	public void doAfterSign(String docName, String docdata, int predid, int signNumber,  long id, String certserial,int WrkId) {
	
		Map hm1 = new HashMap();
		hm1.put("id", id);
		int drop = getNpjt().queryForInt("select CASE when dropid is null then 0 else 1 end from snt.docstore where id = :id", hm1);
		String id_pak = getNpjt().queryForObject("select id_pak from snt.docstore where id = :id", hm1, String.class);

		//NOT USED
		if (signNumber==1){
			Document doc = facade.getDocumentById(id);
			try{
			ETDForm form = ETDForm.createFromArchive(doc.getBlDoc());
			DataBinder b  = form.getBinder();
			//Для генерации xml (для 2 подписи)
			//Start
			String xmlText = b.getValue("text1");
			byte[] bXmlText = Base64.decode(xmlText);
			
				String xmlRussian = new String(bXmlText, "windows-1251");
				xmlRussian = xmlRussian.replace("<?xml version=\"1.0\" encoding=\"windows-1251\"?>", "");
				DataBinder binder = new DataBinder(xmlRussian);
				binder.setRootElement("Файл");
				String dateDoc = binder.getNode("Документ").getAttributes().getNamedItem("ДатаДок").toString();
				String timeDoc = binder.getNode("Документ").getAttributes().getNamedItem("ВремДок").toString();
				String idFileFirstSign = binder.getRootEl().getAttribute("ИдФайл");

				b.setNodeValue("idFileAct", idFileFirstSign);
				b.setNodeValue("dateDocAct", dateDoc);
				b.setNodeValue("timeDocAct", timeDoc);
				facade.updateDocumentData(doc);
			}
			catch (Exception e)
			{
				log.error(TypeConverter.exceptionToString(e));
			}
			//End
		}
		
		
		if(drop==1||signNumber==2){
			try {
			Document doc = facade.getDocumentById(id);
			ETDForm form = ETDForm.createFromArchive(doc.getBlDoc());
			DataBinder binder  = form.getBinder();
			
			String text2 = binder.getValue("text2");
			String sign2 = binder.getValue("sign2");

			byte [] file2 = Base64.decode(text2);
		    byte [] file_sign2 = Base64.decode(sign2);

		    long typeid = facade.getDocumentDao().getTypeId(id);
		    
		    String id_pack = facade.getDocumentDao().getId_pack(id);
		    
		    facade.getDocumentDao().insertXMLandSign(file2, file_sign2, id, typeid, predid, id_pack);
		    
			sendtoetd.SendToEtdMessage(id, docdata, parentform, signNumber, drop,false);
			sendtotransoil.SendSigntoTransoil(id, signNumber, drop, predid);

		    } catch (Exception e) {
		    	log.error(TypeConverter.exceptionToString(e));
		    } 
		    
		    if (drop==0&&id_pak.length()>2)
				try {
					sendtoetd.PackUpdate(id);
				} catch (Exception e) {
					log.error(TypeConverter.exceptionToString(e));
				}
			
			if (drop==1&&id_pak.length()>2)
				sendtoetd.updateVisible(id);	
			
		}		
		
	}

   @Override
   public void doAfterSave(String docName, String docdata, int predid, int signNumber, String CertID, long id,int WrkId) throws Exception{
//		    try{
//		    	sendtoetd.updateVisible(id);	
//		    } catch (Exception e){
//			  StringWriter outError = new StringWriter();
//			   PrintWriter errorWriter = new PrintWriter( outError );
//			   e.printStackTrace(errorWriter);
//			   
//			   log.error(outError.toString());
//		    };
	}
   
   @Override
   public void doAfterSync(String formname, SyncObj syncobj, String sql, int signum){
	   
	   ETDForm form = null;
	   DataBinder b = null;
	   try{
	   
	   form = ETDForm.createFromArchive(syncobj.getBldoc());
	   b = form.getBinder();
	   
	   String text1 = b.getValue("text1");
	   String sign1 = b.getValue("sign1");
	   byte [] file1 = Base64.decode(text1);
	   byte [] file_sign1 = Base64.decode(sign1);
	   
	   facade.getDocumentDao().insertXMLandSign(file1, file_sign1, syncobj.getDocid(), syncobj.getTypeid(), syncobj.getPredid(), syncobj.getId_pak());
	
	   } catch (UnsupportedEncodingException e) {
		   log.error(TypeConverter.exceptionToString(e));
	   } catch (ServiceException e) {
		   log.error(TypeConverter.exceptionToString(e));
	   } catch (IOException e) {
		   log.error(TypeConverter.exceptionToString(e));
	   } catch (InternalException e) {
		   log.error(TypeConverter.exceptionToString(e));
	   } catch(Exception e){
		   log.error(TypeConverter.exceptionToString(e));
	   }
	   
	   //
	   etdsyncfacade.insertDocstore(sql, etdsyncfacade.getWorkerWithorderNull(syncobj));
	   etdsyncfacade.updateDSF(syncobj);
	   
	   //заполнение numfpu, у пакета документов, таблицы snt.docstore
	   String getDiSql = "select count(*) from "+
			   "(select case when di in ('02', '04') then 1 end as di from snt.docstore where "+
			   "id_pak = :id_pak) as tab "+
			   "where di = 1";
	   Map<String, Object> paramMap = new HashMap<String, Object>();
	   paramMap.put("id_pak", syncobj.getId_pak());

	   int count = getNpjt().queryForInt(getDiSql, paramMap);
	   if(count == 1){
		   try {
			paramMap.put("numberFpu", b.getValue("P_7"));
		} catch (InternalException e) {
			log.error(TypeConverter.exceptionToString(e));
		}
		   getNpjt().update("update snt.docstore set numfpu = :numberFpu where id_pak = :id_pak and typeid = (select id from snt.doctype where trim(name) = 'Пакет документов')", paramMap);
	   }
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
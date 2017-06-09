package sheff.rgd.ws.Controllers.ASR;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.sql.Time;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import ru.aisa.rgd.etd.dao.ETDSyncServiceFacade;
import ru.aisa.rgd.etd.objects.ETDDocumentData;
import ru.aisa.rgd.utils.Base64;
import ru.aisa.rgd.ws.dao.ServiceFacade;
import ru.aisa.rgd.ws.documents.ETDForm;
import ru.aisa.rgd.ws.domain.Document;
import ru.aisa.rgd.ws.exeption.ServiceException;
import ru.aisa.rgd.ws.utility.DataBinder;
import ru.aisa.rgd.ws.utility.TypeConverter;
import sheff.rgd.ws.Abstract.AbstractAction;
import sheff.rgd.ws.Abstract.DoAction;
import sheff.rjd.services.syncutils.SyncObj;
import sheff.rjd.services.transoil.TransService;
import sheff.rjd.ws.OCO.AfterSign.DropList;
import sheff.rjd.ws.OCO.AfterSign.SendToEtd;



public class FPU26CSSController extends AbstractAction{
	
	private String parentform;
	private ETDSyncServiceFacade etdsyncfacade;
	private NamedParameterJdbcTemplate npjt;
	private SendToEtd sendtoetd;
	private TransService sendtotransoil;  
	protected final Logger	log	= Logger.getLogger(getClass());
	private DropList droplist;
	private DoAction action;
	private ServiceFacade facade;
	
	public ServiceFacade getFacade() {
		return facade;
	}

	public void setFacade(ServiceFacade facade) {
		this.facade = facade;
	}

	public DropList getDroplist() {
		return droplist;
	}

	public void setDroplist(DropList droplist) {
	 	this.droplist = droplist;
	}
	
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
	
	public void setFormControllers (DoAction action){
		this.action = action;
		}

	public DoAction getFormControllers(){
		return action;
	}
	
	@Override
	public void doAfterSign(String docName, String docdata, int predid, int signNumber,  long id, String certserial,int WrkId) {
	
		Map<String, Object> hm1 = new HashMap<String, Object>();
		hm1.put("id", id);
		int drop = getNpjt().queryForInt("select CASE when dropid is null then 0 else 1 end "
				+ "from snt.docstore where id = :id", hm1);
		
		if(signNumber == 2 || drop == 1){
			try {
				
				String id_pak = (String) getNpjt().queryForObject("select id_pak from snt.docstore "
						+ "where id = :id", hm1, String.class);
				hm1.put("id_pak", id_pak);		
				
				//ОПИСАНИЕ
				int countsf = getNpjt().queryForInt("select count(0) from snt.docstore "
						+ "where typeid in (select id from snt.doctype "
						+ "where name like '%Счет-фактура%') and id_pak = :id_pak", hm1);

				if (countsf==0) {
					String content = (String) getNpjt().queryForObject("select rtrim(opisanie) from snt.docstore where id = :id", hm1, String.class);
					hm1.put("content", content+" - Счет-фактура не сформирована");
					getNpjt().update("update snt.docstore set opisanie = :content where id = :id", hm1);		 
				}
				
				try {
					sendtoetd.SendToEtdMessage(id, docdata, parentform, signNumber, drop,false);
					sendtotransoil.SendSigntoTransoil(id, signNumber, drop, predid);
					if (drop == 0 && id_pak.length() > 2) {
						try {
							Long idPakDoc = npjt.queryForLong("select id from snt.docstore "
									+ " where id_pak = :id_pak and typeid = "
									+ "(select id from snt.doctype where rtrim(name) = 'Пакет документов ЦСС')", hm1); 
							sendtoetd.PackUpdate(id);
							sendtoetd.signpackdocs(idPakDoc, certserial, WrkId,
									predid);
							String selectDateAndTime = "select ldate, ltime from snt.docstore where id = :id";
							SqlRowSet rs = npjt.queryForRowSet(selectDateAndTime, hm1);
							Date ldate = null;
		            		Time ltime = null;
		            		while(rs.next()) {
		            			ldate = rs.getDate("ldate");
		            			ltime = rs.getTime("ltime");
		            		}
		            		hm1.put("ldate", ldate);
		            		hm1.put("ltime", ltime);
		                 	npjt.update("update snt.docstore set ldate = :ldate, ltime = :ltime "
		                 			+ "where id in (select id from snt.docstore where id_pak = :id_pak "
		                 			+ "and typeid in (select id from snt.doctype where rtrim(name) = 'Расшифровка' "
		                 			+ "and rtrim(name) = 'Счет'))", hm1);
		                 	
		                 	Document doc = facade.getDocumentById(id);
		                 	ETDForm form = ETDForm.createFromArchive(doc.getBlDoc());
		        			DataBinder b  = form.getBinder();
		                 	
		                 	String text2 = b.getValue("text2");
		        			String sign2 = b.getValue("sign2");
		        			byte[] bXmlText = Base64.decode(text2);
		        			byte[] sign_file = Base64.decode(sign2);
		        			
		        		    long typeid = facade.getDocumentDao().getTypeId(id);
		        		    
		        		    String id_pack = facade.getDocumentDao().getId_pack(id);
		        		    
		        		    facade.getDocumentDao().insertXMLandSign(bXmlText, sign_file, id, typeid, predid, id_pack);
		                 	
						} catch (Exception e) {
							log.error(TypeConverter.exceptionToString(e));
						}
					}
					if (drop == 1 && id_pak.length() > 2) {
						sendtoetd.updateVisible(id);	
					}
			    } catch (Exception e) {
			    	log.error(TypeConverter.exceptionToString(e));
			
			    } 
			} catch (ServiceException e) {
				log.error("Service Error:");
				log.error("code:" + e.getError().getCode() + " message : " + e.getError().getMessage());
			}
			catch (Exception e) {
				log.error(TypeConverter.exceptionToString(e));
			}
		}
	}

   @Override
   public void doAfterSave(String docName, String docdata, int predid, int signNumber, String CertID, long id,int WrkId) throws Exception{

   }
   
   @Override
   public void doAfterSync(String formname, SyncObj syncobj, String sql, int signum) {
	   etdsyncfacade.insertDocstore(sql, etdsyncfacade.getWorkerWithorderNull(syncobj));
	   etdsyncfacade.updateDSF(syncobj);
	   ETDForm form;
	try {
   	   form = ETDForm.createFromArchive(syncobj.getBldoc());
	   DataBinder binder = form.getBinder();
	   String text1 = binder.getValue("text1");
	   String sign1 = binder.getValue("sign1");
	   byte [] file1 = Base64.decode(text1);
	   byte [] file_sign1 = Base64.decode(sign1);
	   
	   facade.getDocumentDao().insertXMLandSign(file1, file_sign1, syncobj.getDocid(), syncobj.getTypeid(), syncobj.getPredid(), syncobj.getId_pak());
	} catch (Exception e) {
		log.error(TypeConverter.exceptionToString(e));
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
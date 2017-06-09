package sheff.rgd.ws.Controllers.PPS;



import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import ru.aisa.rgd.etd.dao.ETDSyncServiceFacade;
import ru.aisa.rgd.etd.objects.ETDDocumentData;
import ru.aisa.rgd.ws.dao.ServiceFacade;
import ru.aisa.rgd.ws.documents.ETDForm;
import ru.aisa.rgd.ws.utility.DataBinder;
import ru.aisa.rgd.ws.utility.TypeConverter;
import sheff.rgd.ws.Abstract.AbstractAction;
import sheff.rgd.ws.Abstract.DoAction;
import sheff.rjd.services.syncutils.SyncObj;
import sheff.rjd.ws.OCO.AfterSign.SendToEtd;


public class CorrFPU26PPSController extends AbstractAction{

	private String parentform;
	private NamedParameterJdbcTemplate npjt;
	private ETDSyncServiceFacade etdsyncfacade;
	private SendToEtd sendtoetd;
	private DoAction formControllers;
	private ServiceFacade facade;
	protected final Logger	log	= Logger.getLogger(getClass());
	
	public NamedParameterJdbcTemplate getNpjt() {
		return npjt;
	}
	public void setNpjt(NamedParameterJdbcTemplate npjt) {
		this.npjt = npjt;
	}
	
	
	public ETDSyncServiceFacade getEtdsyncfacade() {
		return etdsyncfacade;
	}
	public void setEtdsyncfacade(ETDSyncServiceFacade etdsyncfacade) {
		this.etdsyncfacade = etdsyncfacade;
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
	public void setSendtoetd(SendToEtd sendtoetd) {
		this.sendtoetd = sendtoetd;
	}
	
	public DoAction getFormControllers() {
		return formControllers;
	}
	public void setFormControllers(DoAction formControllers) {
		this.formControllers = formControllers;
	}
	
	public ServiceFacade getFacade() {
		return facade;
	}
	public void setFacade(ServiceFacade facade) {
		this.facade = facade;
	}
	@Override
	public void doAfterSign(String docName, String docdata, int predid, int signNumber, long id, String certserial, int WrkId) {
		Map hm1 = new HashMap();
		hm1.put("id", id);
		int drop = getNpjt().queryForInt("select CASE when dropid is null then 0 else 1 end from snt.docstore where id = :id", hm1);
		if (signNumber==2||drop==1){
			
		try {
			   sendtoetd.SendToEtdMessage(id, docdata, parentform, signNumber, drop, false);
			
			    
			    } catch (Exception e) {
			    	log.error(TypeConverter.exceptionToString(e));
			    }
			    } 
		
	}

	@Override
	public void doAfterSave(String docName, String docdata, int predid, int signNumber, String CertID, long id, int WrkId) throws Exception {
		
		try{
		 HashMap<String, Object> hm1 = new HashMap<String, Object>();
		  hm1.put("ID", id);
		  int readid = npjt.queryForInt("select readid from snt.docstore where id =:ID", hm1);
		 
		 if (readid==-1){
			 hm1.put("reqnum", "Согласовано");
			 npjt.update("update snt.docstore set readid = 1, reqnum =:reqnum where id =:ID", hm1);
		 }
		} catch (Exception e){
			log.error(TypeConverter.exceptionToString(e));
		    }
		    
	    }

	@Override
	public void doAfterSync(String formname, SyncObj obj, String sql, int signum) {
		
		ETDForm form = null;
		DataBinder kinder = null;
		
		try{
			form = ETDForm.createFromArchive(obj.getBldoc());
			kinder = form.getBinder();
		} catch (Exception e){
			log.error(TypeConverter.exceptionToString(e));
//			e.printStackTrace();
		}
		
		
		try {
		
		long ppsid = 0;
		ppsid = Long.parseLong(kinder.getValue("id_PPS"));
		
		HashMap<String, Object> pp = new HashMap<String, Object>();
		pp.put("ppsid", ppsid);
		
		int predid = npjt.queryForInt("select predid from snt.docstore where etdid = :ppsid", pp);
		obj.setPredid(predid);
	} catch (Exception e){
		log.error(TypeConverter.exceptionToString(e));
//		e.printStackTrace();
	}
		
		try{
			 StringBuffer content = new StringBuffer();
			 content.append("№ "+kinder.getValue("P_7")+" от "+kinder.getValue("P_8"));
			 content.append(" с "+kinder.getValue("P_26")+" по "+kinder.getValue("P_27"));
			 StringBuffer id_pak = new StringBuffer();
			 id_pak.append(kinder.getValue("P_45"));
			 StringBuffer dogovor = new StringBuffer();
			 dogovor.append(kinder.getValue("P_9"));
			obj.setContent(content.toString());
			 obj.setDogovor(dogovor.toString());
			 obj.setId_pak(id_pak.toString());
			 obj.setMark(4054);
//			 facade.getNpjt().update("update snt.docstore set opisanie =:content, id_pak =:id_pak, no = :no where id=:id", pp);
			} catch (Exception e){
				log.error(TypeConverter.exceptionToString(e));
//				e.printStackTrace();
			}
		etdsyncfacade.insertDocstore(sql, etdsyncfacade.getWorkerWithorderNull(obj));
		etdsyncfacade.updateDSF(obj);
		
		
	}
	public ETDDocumentData EditBeforeOpen(String docName, ETDDocumentData doc) {
		// TODO Auto-generated method stub
		return doc;
	}
}
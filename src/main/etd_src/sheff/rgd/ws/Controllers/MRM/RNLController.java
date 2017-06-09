package sheff.rgd.ws.Controllers.MRM;

import javax.sql.DataSource;

import org.apache.log4j.Logger;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import ru.aisa.rgd.etd.dao.ETDSyncServiceFacade;
import ru.aisa.rgd.etd.objects.ETDDocumentData;
import ru.aisa.rgd.ws.documents.ETDForm;
import ru.aisa.rgd.ws.utility.DataBinder;
import ru.aisa.rgd.ws.utility.TypeConverter;
import sheff.rgd.ws.Abstract.AbstractAction;
import sheff.rjd.services.syncutils.SyncObj;
import sheff.rjd.ws.OCO.AfterSign.SendToEtd;



public class RNLController extends AbstractAction{
	
	private String parentform;
	private NamedParameterJdbcTemplate npjt;
	private SendToEtd sendtoetd;
	private DataSource ds;
	private ETDSyncServiceFacade etdsyncfacade;
	protected final Logger	log	= Logger.getLogger(getClass());
	
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
	
	public void setParentform(String parentname) {
		this.parentform = parentname;
	}

	
	public String getParentform() {
		return parentform;
	}
	
	public DataSource getDs() {
		return ds;
	}

	public void setDs(DataSource ds) {
		this.ds = ds;
	}
	
	
	
	public ETDSyncServiceFacade getEtdsyncfacade() {
		return etdsyncfacade;
	}
	public void setEtdsyncfacade(ETDSyncServiceFacade etdsyncfacade) {
		this.etdsyncfacade = etdsyncfacade;
	}
	public void doAfterSign(String docName, String docdata, int predid, int signNumber,  long id, String certserial,int WrkId) {
		
		if(signNumber==1){
			try {
				
			} catch (Exception e) {
		    	log.error(TypeConverter.exceptionToString(e));
		
		    } 
		}
		
	}


   public void doAfterSave(String docName, String docdata, int predid, int signNumber, String CertID, long id,int WrkId) throws Exception{
		
	}
   
   public void doAfterSync(String formname, SyncObj syncobj, String sql, int signum){

	   
	   try{
		   ETDForm form = ETDForm.createFromArchive(syncobj.getBldoc());
			  DataBinder binder = form.getBinder();
			  
			  //В зависимости от заполненности полей проставляем pred_creator для второго предприятия
			  
			  
			  if (binder.getValue("P_8a").length()==0&&binder.getValue("P_8b").length()==0&&binder.getValue("P_8v").length()==0){
				 
			  }
			  
			  else  if (binder.getValue("P_7a").length()==0&&binder.getValue("P_7b").length()==0&&binder.getValue("P_7v").length()==0){
				
				  int predid = syncobj.getPredcreator();
				  int predcreator = syncobj.getPredid();
				  
				  syncobj.setPredid(predid);
				syncobj.setPredcreator(predcreator);
			  }
			  
			  else {
				  
				  if (binder.getValue("P_8a").length()>0){
					  syncobj.setPredcreator(etdsyncfacade.getpredIdByIOKPO(Integer.parseInt(binder.getValue("P_8a"))));
					}
					
					else if (binder.getValue("P_8b").length()>0&&binder.getValue("P_8v").length()>0){
						syncobj.setPredcreator(etdsyncfacade.getpredIdByINNKPP(binder.getValue("P_8b"), binder.getValue("P_8v")));
								
					}
				  
			  }
			  //no
			  
			  StringBuffer no = new StringBuffer();
			  no.append(binder.getValue("P_2"));
			  syncobj.setDogovor(no.toString());
			  
			  //opisanie
			  StringBuffer content = new StringBuffer();
			  content.append(binder.getValue("P_2_1a"));
			  content.append(", ");
			  content.append(binder.getValue("P_2_2"));
			  content.append(", ");
			  content.append(binder.getValue("P_2_3a"));
			  content.append(", ");
			  content.append(binder.getValue("P_3"));
			  content.append(", ");
			  content.append(binder.getValuesAsArray("P_6_1").length);
			  content.append(" вагона(ов)");
			  syncobj.setContent(content.toString());
				  etdsyncfacade.insertDocstore(sql, etdsyncfacade.getWorkerWithorderNull(syncobj));
			
		   } catch (Exception e){
			   log.error(TypeConverter.exceptionToString(e));
		   }
   }
   public ETDDocumentData EditBeforeOpen(String docName, ETDDocumentData doc) {
		// TODO Auto-generated method stub
		return doc;
	}

   
}
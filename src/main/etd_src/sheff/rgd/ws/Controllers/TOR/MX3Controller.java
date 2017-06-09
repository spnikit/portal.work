package sheff.rgd.ws.Controllers.TOR;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import ru.aisa.rgd.etd.dao.ETDSyncServiceFacade;
import ru.aisa.rgd.etd.objects.ETDDocumentData;
import ru.aisa.rgd.ws.exeption.ServiceException;
import ru.aisa.rgd.ws.utility.TypeConverter;
import sheff.rgd.ws.Abstract.AbstractAction;
import sheff.rjd.services.syncutils.SyncObj;
import sheff.rjd.services.transoil.TransService;
import sheff.rjd.ws.OCO.AfterSign.SendToEtd;


public class MX3Controller extends AbstractAction{

	   private String parentform;
	   private NamedParameterJdbcTemplate npjt;
	   private SendToEtd sendtoetd;
	   private TransService sendtotransoil;  
	   private ETDSyncServiceFacade etdsyncfacade;
	   protected final Logger	log	= Logger.getLogger(getClass());
	      
		public NamedParameterJdbcTemplate getNpjt() {
			return npjt;
		}
		
		public SendToEtd getSendtoetd() {
		    return sendtoetd;
		}
		
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
	
	
	@Override
	public void doAfterSign(String docName, String docdata, int predid, int signNumber, long id, String certserial, int WrkId) {
		Map hm1 = new HashMap();
		hm1.put("id", id);
		int drop = getNpjt().queryForInt("select CASE when dropid is null then 0 else 1 end from snt.docstore where id = :id", hm1);
		
		if(/*signNumber==2*/signNumber==3||signNumber==4||drop==1){
			
			try{
			    
			    try {
				sendtoetd.SendToEtdMessage(id, docdata, parentform, signNumber, drop,false);
				sendtotransoil.SendSigntoTransoil(id, signNumber, drop, predid);
			    } catch (Exception e) {
			    	log.error(TypeConverter.exceptionToString(e));
			    }
				   if (drop==0&&signNumber==4)
						sendtoetd.PackUpdate(id);
					/*else if (drop ==1)
						sendtoetd.PackDrop(id, docName);*/
				   
				   if (drop==1)
					sendtoetd.updateVisible(id);		
			    
			}catch (ServiceException e)
			{
				//e.printStackTrace(System.out);
				log.error("Service Error:");
				log.error("code:" + e.getError().getCode() + " message : " + e.getError().getMessage());
			}
			catch (Exception e)
			{
				//e.printStackTrace(System.out);
				log.error(TypeConverter.exceptionToString(e));
				//SNMPSender.sendMessage(e);
			}
		}
		
	}

	@Override
	public void doAfterSave(String docName, String docdata, int predid,int signNumber, String CertID, long id, int WrkId) throws Exception {	
//			    try{
//			    	 
//			    sendtoetd.updateVisible(id);	
//			    } catch (Exception e){
//				  StringWriter outError = new StringWriter();
//				   PrintWriter errorWriter = new PrintWriter( outError );
//				   e.printStackTrace(errorWriter);
//				   
//				   log.error(outError.toString());
//			    };
		
	}

	@Override
	public void doAfterSync(String formname, SyncObj syncobj, String sql,int signum) {
		etdsyncfacade.getWorkerWithorderNull(syncobj);
		etdsyncfacade.insertDocstore(sql, syncobj);
		etdsyncfacade.updateDSF(syncobj);

		etdsyncfacade.getWorkerWithorder(syncobj);
		etdsyncfacade.updateDSF(syncobj);
	}

	public ETDDocumentData EditBeforeOpen(String docName, ETDDocumentData doc) {
		// TODO Auto-generated method stub
		return doc;
	}
}

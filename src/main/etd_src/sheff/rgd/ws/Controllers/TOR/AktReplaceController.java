package sheff.rgd.ws.Controllers.TOR;

import org.apache.log4j.Logger;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import ru.aisa.rgd.etd.dao.ETDSyncServiceFacade;
import ru.aisa.rgd.etd.objects.ETDDocumentData;
import ru.aisa.rgd.ws.utility.TypeConverter;
import sheff.rgd.ws.Abstract.AbstractAction;
import sheff.rjd.services.syncutils.SyncObj;
import sheff.rjd.services.transoil.TransService;
import sheff.rjd.ws.OCO.AfterSign.SendToEtd;


public class AktReplaceController extends AbstractAction{
	
	private String parentform;
	private NamedParameterJdbcTemplate npjt;
	private SendToEtd sendtoetd;
	protected final Logger	log	= Logger.getLogger(getClass());
	private TransService sendtotransoil;  
	private ETDSyncServiceFacade etdsyncfacade;
	
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
	
	
	public void setParentform(String parentname) {
		this.parentform = parentname;
	}

	public String getParentform() {
		return parentform;
	}
	
	
	public void doAfterSign(String docName, String docdata, int predid, int signNumber,  long id, String certserial,int WrkId) {
		
		
	}


   public void doAfterSave(String docName, String docdata, int predid, int signNumber, String CertID, long id,int WrkId) throws Exception{
	 
	   try{
	    	sendtoetd.FakeSign(docName, docdata, predid, signNumber, CertID, id, WrkId);
	    	sendtoetd.PackUpdate(id);
	    } catch (Exception e){
	    	log.error(TypeConverter.exceptionToString(e));
	    };
	    
	    sendtoetd.SendToEtdMessage(id, docdata, parentform, signNumber, 0,false);
	    sendtotransoil.SendBlobToTransoil(id, docName, predid);    
		  
	}
   
   public void doAfterSync(String formname, SyncObj syncobj, String sql, int signum){
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
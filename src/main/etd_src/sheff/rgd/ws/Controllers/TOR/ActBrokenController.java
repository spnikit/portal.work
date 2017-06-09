package sheff.rgd.ws.Controllers.TOR;

import org.apache.log4j.Logger;

import ru.aisa.rgd.etd.dao.ETDSyncServiceFacade;
import ru.aisa.rgd.etd.objects.ETDDocumentData;
import ru.aisa.rgd.ws.utility.TypeConverter;
import sheff.rgd.ws.Abstract.AbstractAction;
import sheff.rjd.services.syncutils.SyncObj;
import sheff.rjd.services.transoil.TransService;
import sheff.rjd.ws.OCO.AfterSign.SendToEtd;

public class ActBrokenController extends AbstractAction{

    protected final Logger	log	= Logger.getLogger(getClass());
    private SendToEtd sendtoetd;
    private TransService sendtotransoil;  
    private String parentform;
    private ETDSyncServiceFacade etdsyncfacade;
   
    
    public SendToEtd getSendtoetd() {
        return sendtoetd;
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
	
	 public String getParentform() {
			return parentform;		
     }

    public void setParentform(String parentname) {
			this.parentform = parentname;	
	}
    
	public ETDSyncServiceFacade getEtdsyncfacade() {
		return etdsyncfacade;
	}

	public void setEtdsyncfacade(ETDSyncServiceFacade etdsyncfacade) {
		this.etdsyncfacade = etdsyncfacade;
	}

	@Override
	public void doAfterSave(String docName, String docdata, int predid,
		    int signNumber, String CertID, long id, int WrkId) throws Exception {
		    try{	
		    	sendtoetd.FakeSign(docName, docdata, predid, signNumber, CertID, id, WrkId);
		    	sendtoetd.PackUpdate(id);
		    	
		    	
		    	//sendtoetd.PackUpdate(id);
		    } catch (Exception e){
		    	log.error(TypeConverter.exceptionToString(e));
		    };
		    
		    sendtoetd.SendToEtdMessage(id, docdata, parentform, signNumber, 0,false);
		 	    sendtotransoil.SendBlobToTransoil(id, docName, predid);
	    }

	@Override
	public void doAfterSign(String docName, String docdata, int predid,
			int signNumber, long id, String certserial, int WrkId) {
		// TODO Auto-generated method stub
		
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

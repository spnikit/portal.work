package sheff.rgd.ws.Controllers.Perevoz;

import java.io.PrintWriter;
import java.io.StringWriter;

import org.apache.log4j.Logger;

import ru.aisa.rgd.etd.dao.ETDSyncServiceFacade;
import ru.aisa.rgd.etd.objects.ETDDocumentData;
import sheff.rgd.ws.Abstract.AbstractAction;
import sheff.rjd.services.syncutils.SyncObj;
import sheff.rjd.ws.OCO.AfterSign.SendToEtd;

public class ReestrController extends AbstractAction{

	private String parentform;
	private ETDSyncServiceFacade etdsyncfacade;
    private SendToEtd sendtoetd;
    protected final Logger	log	= Logger.getLogger(getClass());
   
    public SendToEtd getSendtoetd() {
        return sendtoetd;
    }

    public void setSendtoetd(SendToEtd sendtoetd) {
        this.sendtoetd = sendtoetd;
    }
    
	public String getParentform() {
		return parentform;
	}

	public ETDSyncServiceFacade getEtdsyncfacade() {
		return etdsyncfacade;
	}

	public void setEtdsyncfacade(ETDSyncServiceFacade etdsyncfacade) {
		this.etdsyncfacade = etdsyncfacade;
	}

	public void setParentform(String parentform) {
		this.parentform = parentform;
	}


	@Override
	public void doAfterSign(String docName, String docdata, int predid, int signNumber, long id, String certserial, int WrkId) {
		
	}

	@Override
	public void doAfterSave(String docName, String docdata, int predid, int signNumber, String CertID, long id, int WrkId) throws Exception {
		 try{
		    	sendtoetd.FakeSign(docName, docdata, predid, signNumber, CertID, id, WrkId);
		    } catch (Exception e){
			  StringWriter outError = new StringWriter();
			   PrintWriter errorWriter = new PrintWriter( outError );
			   e.printStackTrace(errorWriter);
			   
			   log.error(outError.toString());
		    };
		    
		    try{
			    sendtoetd.SendToEtdMessage(id, docdata, parentform, signNumber, 0,false);
			    }catch (Exception e){
					  StringWriter outError = new StringWriter();
					   PrintWriter errorWriter = new PrintWriter( outError );
					   e.printStackTrace(errorWriter);
					    log.error(outError.toString());
		    }
		
	}

	@Override
	public void doAfterSync(String formname, SyncObj syncobj, String sql, int signum) {
		etdsyncfacade.insertDocstore(sql, etdsyncfacade.getWorkerWithorderNull(syncobj));
		etdsyncfacade.updateDSF(syncobj);
		
	}
	public ETDDocumentData EditBeforeOpen(String docName, ETDDocumentData doc) {
		// TODO Auto-generated method stub
		return doc;
	}
}

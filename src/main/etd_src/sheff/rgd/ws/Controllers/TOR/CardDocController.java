package sheff.rgd.ws.Controllers.TOR;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import ru.aisa.rgd.etd.dao.ETDSyncServiceFacade;
import ru.aisa.rgd.etd.objects.ETDDocumentData;
import ru.aisa.rgd.ws.utility.TypeConverter;
import sheff.rgd.ws.Abstract.AbstractAction;
import sheff.rjd.services.syncutils.SyncObj;
import sheff.rjd.services.transoil.TransService;
import sheff.rjd.ws.OCO.AfterEdit.FakeSignature;
import sheff.rjd.ws.OCO.AfterSign.SendToEtd;


public class CardDocController extends AbstractAction{
	
	private String parentform;
	private NamedParameterJdbcTemplate npjt;
	private SendToEtd sendtoetd;
	private FakeSignature fakesignature;
	protected final Logger	log	= Logger.getLogger(getClass());
	private TransService sendtotransoil;  
	private ETDSyncServiceFacade etdsyncfacade;
	
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
	
	public ETDSyncServiceFacade getEtdsyncfacade() {
		return etdsyncfacade;
	}

	public void setEtdsyncfacade(ETDSyncServiceFacade etdsyncfacade) {
		this.etdsyncfacade = etdsyncfacade;
	}
	
	public void doAfterSign(String docName, String docdata, int predid, int signNumber,  long id, String certserial,int WrkId) {
		Map hm1 = new HashMap();
		hm1.put("id", id);
		int drop =0;
		
		drop = getNpjt().queryForInt("select CASE when dropid is null then 0 else 1 end from snt.docstore where id = :id", hm1);
		
		
		if(signNumber==2||drop==1){
		    
		    try {
			sendtoetd.SendToEtdMessage(id, docdata, parentform, signNumber, drop,false);
		
		    
		    } catch (Exception e) {
		    	log.error(TypeConverter.exceptionToString(e));
		
		    } 
		}
		
	}


   public void doAfterSave(String docName, String docdata, int predid, int signNumber, String CertID, long id,int WrkId) throws Exception{
	   try{
	    	sendtoetd.FakeSign(docName, docdata, predid, signNumber, CertID, id, WrkId);
	    	sendtoetd.PackUpdate(id);
	    } catch (Exception e){
	    	log.error(TypeConverter.exceptionToString(e));
	    };
	    
//	    sendtoetd.SendToEtdMessage(id, docdata, parentform, signNumber, 0,false);
	    sendtoetd.SendToEtdMessage(id, docdata, parentform, 2, 0,false);
	    sendtotransoil.SendBlobToTransoil(id, docName, predid);
	}
   
   public void doAfterSync(String formname, SyncObj syncobj, String sql, int signum){
		etdsyncfacade.insertDocstore(sql, etdsyncfacade.getWorkerWithorderNull(syncobj));
		etdsyncfacade.updateDSF(syncobj);
   }
   public ETDDocumentData EditBeforeOpen(String docName, ETDDocumentData doc) {
		// TODO Auto-generated method stub
		return doc;
	}
}
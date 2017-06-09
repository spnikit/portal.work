package sheff.rgd.ws.Controllers.MRM;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import ru.aisa.rgd.etd.dao.ETDSyncServiceFacade;
import ru.aisa.rgd.etd.objects.ETDDocumentData;
import ru.aisa.rgd.ws.utility.TypeConverter;
import sheff.rgd.ws.Abstract.AbstractAction;
import sheff.rjd.services.syncutils.SyncObj;
import sheff.rjd.ws.OCO.AfterSign.SendToEtd;

public class PVController extends AbstractAction{

	private String parentform;
    private SendToEtd sendtoetd;
	private NamedParameterJdbcTemplate npjt;
	private ETDSyncServiceFacade etdsyncfacade;
	protected final Logger	log	= Logger.getLogger(getClass());
	
	public NamedParameterJdbcTemplate getNpjt() {
		return npjt;
	}

	public void setNpjt(NamedParameterJdbcTemplate npjt) {
		this.npjt = npjt;
	}
   
    public SendToEtd getSendtoetd() {
        return sendtoetd;
    }

    public void setSendtoetd(SendToEtd sendtoetd) {
        this.sendtoetd = sendtoetd;
    }
    
	public String getParentform() {
		return parentform;
	}

	public void setParentform(String parentform) {
		this.parentform = parentform;
	}


	public ETDSyncServiceFacade getEtdsyncfacade() {
		return etdsyncfacade;
	}

	public void setEtdsyncfacade(ETDSyncServiceFacade etdsyncfacade) {
		this.etdsyncfacade = etdsyncfacade;
	}

	@Override
	public void doAfterSign(String docName, String docdata, int predid, int signNumber, long id, String certserial, int WrkId) {
		
		Map hm1 = new HashMap();
		hm1.put("id", id);
		int drop = getNpjt().queryForInt("select CASE when dropid is null then 0 else 1 end from snt.docstore where id = :id", hm1);
		
		
		if(signNumber==1||drop==1){
		    
		    try {
			sendtoetd.SendToEtdMessage(id, docdata, parentform, signNumber, drop,false);
		    
//			sendtotransoil.SendSigntoTransoil(id, signNumber, drop, predid);
	
		    } catch (Exception e) {
		    	log.error(TypeConverter.exceptionToString(e));
		
		    } 
		} 
	}

	@Override
	public void doAfterSave(String docName, String docdata, int predid, int signNumber, String CertID, long id, int WrkId) throws Exception {		
		
		
	}

	@Override
	public void doAfterSync(String formname, SyncObj syncobj, String sql, int signum) {
		etdsyncfacade.insertDocstore(sql, etdsyncfacade.getWorkerWithorderNull(syncobj));
	}

	public ETDDocumentData EditBeforeOpen(String docName, ETDDocumentData doc) {
		// TODO Auto-generated method stub
		return doc;
	}

}

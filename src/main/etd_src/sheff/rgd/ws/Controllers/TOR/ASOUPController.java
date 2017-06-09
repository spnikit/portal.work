package sheff.rgd.ws.Controllers.TOR;

import java.util.HashMap;

import org.apache.log4j.Logger;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import com.ibm.db2.jcc.a.f;

import ru.aisa.rgd.etd.dao.ETDSyncServiceFacade;
import ru.aisa.rgd.etd.objects.ETDDocumentData;
import ru.aisa.rgd.ws.documents.ETDForm;
import ru.aisa.rgd.ws.utility.DataBinder;
import ru.aisa.rgd.ws.utility.TypeConverter;
import sheff.rgd.ws.Abstract.AbstractAction;
import sheff.rjd.services.syncutils.SendToVRKService;
import sheff.rjd.services.syncutils.SyncObj;
import sheff.rjd.services.transoil.TransService;
import sheff.rjd.ws.OCO.AfterSign.SendToEtd;

public class ASOUPController extends AbstractAction{

	 private String parentform;
	 protected final Logger log = Logger.getLogger(getClass());
	 private ETDSyncServiceFacade etdsyncfacade;
	 private NamedParameterJdbcTemplate npjt;
	 private SendToVRKService signvrkservice;
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
	
	public NamedParameterJdbcTemplate getNpjt() {
		return npjt;
	}

	public void setNpjt(NamedParameterJdbcTemplate npjt) {
		this.npjt = npjt;
	}

	public SendToVRKService getSignvrkservice() {
		return signvrkservice;
	}

	public void setSignvrkservice(SendToVRKService signvrkservice) {
		this.signvrkservice = signvrkservice;
	}

	@Override
	public void doAfterSign(String docName, String docdata, int predid, int signNumber, long id, String certserial, int WrkId) {
		
	}

	@Override
	public void doAfterSave(String docName, String docdata, int predid, int signNumber, String CertID, long id, int WrkId) throws Exception {		
	  
	}

	@Override
	public void doAfterSync(String formname, SyncObj syncobj, String sql, int signum) {
		String exp = "";
		HashMap<String, Object> pp = new HashMap<String, Object>();
		pp.put("id", syncobj.getEtdid());
		int predid = getNpjt().queryForInt("select predid from snt.docstore where etdid =:id ", pp);
		try{
		ETDForm form = ETDForm.createFromArchive(syncobj.getBldoc());
		DataBinder db = form.getBinder();
		 exp = db.getValue("P_2");
		 } catch (Exception e){
			 e.printStackTrace();
			 log.error(TypeConverter.exceptionToString(e));
		 }
		signvrkservice.SendSign(syncobj.getEtdid(), 0, 0, predid, exp, parentform);
	}
	public ETDDocumentData EditBeforeOpen(String docName, ETDDocumentData doc) {
		// TODO Auto-generated method stub
		return doc;
	}
}

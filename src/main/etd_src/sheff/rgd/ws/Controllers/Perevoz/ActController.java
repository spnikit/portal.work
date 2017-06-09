package sheff.rgd.ws.Controllers.Perevoz;

import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import ru.aisa.rgd.etd.dao.ETDSyncServiceFacade;
import ru.aisa.rgd.etd.objects.ETDDocumentData;
import sheff.rgd.ws.Abstract.AbstractAction;
import sheff.rjd.services.syncutils.SyncObj;

public class ActController extends AbstractAction{

	private String parentform;
	private ETDSyncServiceFacade etdsyncfacade;
	private NamedParameterJdbcTemplate npjt;
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

	public NamedParameterJdbcTemplate getNpjt() {
		return npjt;
	}

	public void setNpjt(NamedParameterJdbcTemplate npjt) {
		this.npjt = npjt;
	}


	@Override
	public void doAfterSign(String docName, String docdata, int predid, int signNumber, long id, String certserial, int WrkId) {
		
	}

	@Override
	public void doAfterSave(String docName, String docdata, int predid, int signNumber, String CertID, long id, int WrkId) throws Exception {
		
		
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

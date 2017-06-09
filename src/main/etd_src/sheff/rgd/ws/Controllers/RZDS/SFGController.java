package sheff.rgd.ws.Controllers.RZDS;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import ru.aisa.rgd.etd.dao.ETDSyncServiceFacade;
import ru.aisa.rgd.etd.objects.ETDDocumentData;
import ru.aisa.rgd.ws.exeption.ServiceException;
import ru.aisa.rgd.ws.utility.TypeConverter;
import sheff.rgd.ws.Abstract.AbstractAction;
import sheff.rgd.ws.Abstract.DoAction;
import sheff.rjd.services.syncutils.SyncObj;
//import sheff.rjd.ws.OCO.AfterSave.AfterSave;
import sheff.rjd.ws.OCO.AfterSign.DropList;
import sheff.rjd.ws.OCO.AfterSign.SendToEtd;

public class SFGController extends AbstractAction {

	private String parentform;
	private ETDSyncServiceFacade etdsyncfacade;
	private NamedParameterJdbcTemplate npjt;
	private SendToEtd sendtoetd;
	protected final Logger log = Logger.getLogger(getClass());

	public NamedParameterJdbcTemplate getNpjt() {
		return npjt;
	}

	public void setNpjt(NamedParameterJdbcTemplate npjt) {
		this.npjt = npjt;
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

	public SendToEtd getSendtoetd() {
		return sendtoetd;
	}

	public void setSendtoetd(SendToEtd sendtoetd) {
		this.sendtoetd = sendtoetd;
	}

	@Override
	public void doAfterSign(String docName, String docdata, int predid,
			int signNumber, long id, String certserial, int WrkId) {

	}

	@Override
	public void doAfterSave(String docName, String docdata, int predid,
			int signNumber, String CertID, long id, int WrkId) throws Exception {
		try{
			sendtoetd.FakeSign(docName, docdata, predid, signNumber, CertID, id, WrkId);
				} catch (Exception e){
					log.error(TypeConverter.exceptionToString(e));
				}
	}

	@Override
	public void doAfterSync(String formname, SyncObj syncobj, String sql,
			int signum) {

	}
	public ETDDocumentData EditBeforeOpen(String docName, ETDDocumentData doc) {
		// TODO Auto-generated method stub
		return doc;
	}
}

package sheff.rgd.ws.Controllers.PPS;

import java.util.HashMap;

import javax.sql.DataSource;

import org.apache.log4j.Logger;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import ru.aisa.rgd.etd.dao.ETDSyncServiceFacade;
import ru.aisa.rgd.etd.objects.ETDDocumentData;
import ru.aisa.rgd.ws.dao.ServiceFacade;
import ru.aisa.rgd.ws.documents.ETDForm;
import ru.aisa.rgd.ws.utility.DataBinder;
import ru.aisa.rgd.ws.utility.TypeConverter;
import sheff.rgd.ws.Abstract.AbstractAction;
import sheff.rjd.services.syncutils.SyncObj;
import sheff.rjd.ws.OCO.AfterEdit.FakeSignature;
import sheff.rjd.ws.OCO.AfterSign.SendToEtd;

public class CardPPSController extends AbstractAction{

    protected final Logger	log	= Logger.getLogger(getClass());
	private NamedParameterJdbcTemplate npjt;
	private String parentform;
	private DataSource ds;
	private ServiceFacade facade;
	private ETDSyncServiceFacade etdsyncfacade;
	private FakeSignature fakesignature;
	private SendToEtd sendtoetd;
	
	public String getParentform() {
		return parentform;

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

	public NamedParameterJdbcTemplate getNpjt() {
		return npjt;
	}

	public void setNpjt(NamedParameterJdbcTemplate npjt) {
		this.npjt = npjt;
	}

	public DataSource getDs() {
		return ds;
	}

	public void setDs(DataSource ds) {
		this.ds = ds;
	}

	public ServiceFacade getFacade() {
		return facade;
	}

	public void setFacade(ServiceFacade facade) {
		this.facade = facade;
	}
	
	public ETDSyncServiceFacade getEtdsyncfacade() {
		return etdsyncfacade;
	}
	
	public void setEtdsyncfacade(ETDSyncServiceFacade etdsyncfacade) {
		this.etdsyncfacade = etdsyncfacade;
	}
	
	public SendToEtd getSendtoetd() {
	    return sendtoetd;
	}
	public void setSendtoetd(SendToEtd sendtoetd) {
	    this.sendtoetd = sendtoetd;
	}

	
	@Override
	public void doAfterSign(String docName, String docdata, int predid, int signNumber, long id, String certserial, int WrkId) {}

	@Override
	public void doAfterSave(String docName, String docdata, int predid, int signNumber, String CertID, long id, int WrkId) throws Exception {}

	@Override
	public void doAfterSync(String formname, SyncObj obj, String sql, int signum) {
		
		ETDForm form = null;
		DataBinder kinder = null;
		
		try{
			form = ETDForm.createFromArchive(obj.getBldoc());
			kinder = form.getBinder();
		} catch (Exception e){
			log.error(TypeConverter.exceptionToString(e));
		}
		
		
		try {
		
		long ppsid = 0;
		ppsid = Long.parseLong(kinder.getValue("id_PPS"));
		
		HashMap<String, Object> pp = new HashMap<String, Object>();
		pp.put("ppsid", ppsid);
		
		int predid = npjt.queryForInt("select predid from snt.docstore where etdid = :ppsid", pp);
		obj.setPredid(predid);
	} catch (Exception e){
		log.error(TypeConverter.exceptionToString(e));
	}
		
		
		obj.setSignlvl(0);
		etdsyncfacade.insertDocstore(sql, etdsyncfacade.getWorkerWithorderNull(obj));

		
//		try{
//		HashMap<String, Object> pp = new HashMap<String, Object>();
//		pp.put("id", obj.getDocid());
//		npjt.update("update snt.docstore set nwrkid = 1, visible = 0 where id =:id", pp); 
//		} catch (Exception e){
//			log.error(TypeConverter.exceptionToString(e));
//		}
		
		
	}
	public ETDDocumentData EditBeforeOpen(String docName, ETDDocumentData doc) {
		// TODO Auto-generated method stub
		return doc;
	}
}
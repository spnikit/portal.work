package sheff.rgd.ws.Controllers.MRM;


import java.io.UnsupportedEncodingException;
import java.util.HashMap;

import org.apache.log4j.Logger;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import ru.aisa.rgd.etd.dao.ETDSyncServiceFacade;
import ru.aisa.rgd.etd.objects.ETDDocumentData;
import sheff.rgd.ws.Abstract.AbstractAction;
import sheff.rjd.services.syncutils.SendToRTKService;
import sheff.rjd.services.syncutils.SyncObj;
import sheff.rjd.ws.OCO.AfterEdit.FakeSignature;

public class SDGController extends AbstractAction{

	private String parentform;
	private ETDSyncServiceFacade etdsyncfacade;
	private NamedParameterJdbcTemplate npjt;
	private SendToRTKService signservice;
	private FakeSignature fakesignature;
	protected final Logger	log	= Logger.getLogger(getClass());
	
    public ETDSyncServiceFacade getEtdsyncfacade() {
		return etdsyncfacade;
	}

	public void setEtdsyncfacade(ETDSyncServiceFacade etdsyncfacade) {
		this.etdsyncfacade = etdsyncfacade;
	}

	public SendToRTKService getSignservice() {
		return signservice;
	}

	public void setSignservice(SendToRTKService signservice) {
		this.signservice = signservice;
	}

	public FakeSignature getFakesignature() {
		return fakesignature;
	}

	public void setFakesignature(FakeSignature fakesignature) {
		this.fakesignature = fakesignature;
	}
	
	public NamedParameterJdbcTemplate getNpjt() {
		return npjt;
	}

	public void setNpjt(NamedParameterJdbcTemplate npjt) {
		this.npjt = npjt;
	}
    
	public String getParentform() {
		return parentform;
	}

	public void setParentform(String parentform) {
		this.parentform = parentform;
	}


	@Override
	public void doAfterSign(String docName, String docdata, int predid, int signNumber, long id, String certserial, int WrkId) {}

	@Override
	public void doAfterSave(String docName, String docdata, int predid, int signNumber, String CertID, long id, int WrkId) throws Exception {}

	@Override
	public void doAfterSync(String formname, SyncObj syncobj, String sql, int signum) {
		HashMap<String, Object> pp = new HashMap<String, Object>();
		pp.put("docid", syncobj.getDocid());
		pp.put("predid", syncobj.getPredid());
		
		
		if (syncobj.getSignlvl()==0){
			etdsyncfacade.insertDocstore(sql, etdsyncfacade.getWorkerWithorderNull(syncobj));
			
			
			}
		
		if (syncobj.getSignlvl()==1){
		etdsyncfacade.insertDocstore(sql, etdsyncfacade.getWorkerWithorderNull(syncobj));
		etdsyncfacade.updateDSF(syncobj);
		try {
			
			if (etdsyncfacade.getNamebyPredid(syncobj.getPredid()).indexOf("Усть-Луга Ойл")>-1)
			{
			signservice.SendXML(syncobj.getEtdid(), formname, syncobj.getDocdata(), new String (syncobj.getBldoc(), "UTF-8"));
			}
		} catch (UnsupportedEncodingException e) {
			
		}
		
		}
		
		if (syncobj.getSignlvl()==2){
			pp.put("typeid", syncobj.getTypeid());
			pp.put("order", signum);
			pp.put("id", syncobj.getDocid());
			int wrkid = getNpjt().queryForInt("select wrkid from snt.doctypeflow where order = :order and dtid = :typeid ", pp);
			int predid = getNpjt().queryForInt("select predid from snt.docstore where id =:id ", pp);
			fakesignature.fakesign(syncobj.getDocid(), 0, wrkid, predid, syncobj.getBldoc(), syncobj.getDocdata());
		}
		if (etdsyncfacade.getNamebyPredid(syncobj.getPredid()).indexOf("Усть-Луга Ойл")>-1){
		signservice.SendSign(syncobj.getDocid(), syncobj.getSignlvl(), 0, syncobj.getPredid());
		}
		
	}

	public ETDDocumentData EditBeforeOpen(String docName, ETDDocumentData doc) {
		// TODO Auto-generated method stub
		return doc;
	}

}

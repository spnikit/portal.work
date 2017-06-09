package sheff.rgd.ws.Controllers.MRM;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import ru.aisa.rgd.etd.dao.ETDSyncServiceFacade;
import ru.aisa.rgd.etd.objects.ETDDocumentData;
import ru.aisa.rgd.ws.documents.ETDForm;
import ru.aisa.rgd.ws.domain.Document;
import ru.aisa.rgd.ws.utility.DataBinder;
import ru.aisa.rgd.ws.utility.TypeConverter;
import sheff.rgd.ws.Abstract.AbstractAction;
import sheff.rjd.services.syncutils.SyncObj;
import sheff.rjd.services.transoil.TransService;
import sheff.rjd.ws.OCO.AfterEdit.FakeSignature;


public class GU2VController extends AbstractAction{

	private String parentform;
	private ETDSyncServiceFacade etdsyncfacade;
	private NamedParameterJdbcTemplate npjt;
	private FakeSignature fakesignature;
	private TransService sendtotransoil;  
	
	protected final Logger	log	= Logger.getLogger(getClass());
    
	public String getParentform() {
		return parentform;
	}

	public ETDSyncServiceFacade getEtdsyncfacade() {
		return etdsyncfacade;
	}


	public NamedParameterJdbcTemplate getNpjt() {
		return npjt;
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
	
	public void setEtdsyncfacade(ETDSyncServiceFacade etdsyncfacade) {
		this.etdsyncfacade = etdsyncfacade;
	}

	public void setParentform(String parentform) {
		this.parentform = parentform;
	}
	
	public TransService getSendtotransoil() {
		return sendtotransoil;
	}

	public void setSendtotransoil(TransService sendtotransoil) {
		this.sendtotransoil = sendtotransoil;
	}

	@Override
	public void doAfterSign(String docName, String docdata, int predid, int signNumber, long id, String certserial, int WrkId) {
		Map<String, Object> hm1 = new HashMap<String, Object>();
		hm1.put("id", id);
		int drop = getNpjt().queryForInt("select CASE when dropid is null then 0 else 1 end from snt.docstore where id = :id", hm1);
		
		sendtotransoil.SendSignMRMtoTransoil(id, signNumber, drop, predid);
	}

	@Override
	public void doAfterSave(String docName, String docdata, int predid, int signNumber, String CertID, long id, int WrkId) throws Exception {		
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("id", id);

		String docData = getNpjt().queryForObject("select docdata from snt.docstore where id = :id", param, String.class);

		fakesignature.fakesign(id, 0, WrkId, predid, docdata.getBytes(), docData);
		sendtotransoil.SendSignMRMtoTransoil(id, 2, 0, predid);
		
	}

	@Override
	public void doAfterSync(String formname, SyncObj syncobj, String sql, int signum) {
		
		if (signum==1){
			try{
				ETDForm form = ETDForm.createFromArchive(syncobj.getBldoc());
				  DataBinder binder = form.getBinder();
				  
			
				  StringBuffer no = new StringBuffer();
				  no.append(binder.getValue("P_2"));
				  syncobj.setDogovor(no.toString());
				  
				  StringBuffer content = new StringBuffer();
				  content.append(binder.getValue("P_4"));
				  content.append(", ");
				  content.append(binder.getValue("P_5"));
				  content.append(", ");
				  content.append(binder.getValue("P_7_1"));
				  content.append(", ");
				  content.append(binder.getValue("P_7_2"));
				  content.append(", ");
				  content.append(binder.getValuesAsArray("P_9_1").length);
				  content.append(" вагона(ов)");
				  syncobj.setContent(content.toString());
		}catch(Exception e){
				log.error(TypeConverter.exceptionToString(e));
			}
			etdsyncfacade.insertDocstore(sql, etdsyncfacade.getWorkerWithorderNull(syncobj));
			etdsyncfacade.updateDSF(syncobj);
			
			try {
				if (etdsyncfacade.getNamebyPredid(syncobj.getPredid()).indexOf(
						"ООО «Трансойл»") > -1) {
					sendtotransoil.SendMRMtoTransoil(syncobj.getEtdid(), formname, syncobj.getDocdata(),
							new String(syncobj.getBldoc(), "UTF-8"));
				}
			} catch (UnsupportedEncodingException e) {
		    	log.error(TypeConverter.exceptionToString(e));
			}
		}
		if (signum==2){
			Map<String, Object> pp = new HashMap<String, Object>();
			pp.put("typeid", syncobj.getTypeid());
			pp.put("order", signum);
			pp.put("id", syncobj.getDocid());
			int wrkid = getNpjt().queryForInt("select wrkid from snt.doctypeflow where order = :order and dtid = :typeid ", pp);
			int predid = getNpjt().queryForInt("select predid from snt.docstore where id =:id ", pp);
			fakesignature.fakesign(syncobj.getDocid(), 0, wrkid, predid, syncobj.getBldoc(), syncobj.getDocdata());
			
			sendtotransoil.SendSignMRMtoTransoil(syncobj.getDocid(), signum, 0, predid);

		}
	}

	public ETDDocumentData EditBeforeOpen(String docName, ETDDocumentData doc) {
		// TODO Auto-generated method stub
		return doc;
	}

}

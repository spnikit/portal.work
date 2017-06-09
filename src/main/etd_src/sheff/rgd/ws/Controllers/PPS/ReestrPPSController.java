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

public class ReestrPPSController extends AbstractAction{

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
	public void doAfterSave(String docName, String docdata, int predid, int signNumber, String CertID, long id, int WrkId) throws Exception {
		HashMap<String, Object> pp = new HashMap<String, Object>();
		pp.put("DOCTYPE", parentform);
		int truewrk = facade.getNpjt().queryForInt("select wrkid from snt.doctypeflow where wrkid in (select id from snt.wrkname where issm=1) and dtid in "+
				"(select id FROM SNT.doctype where name = :DOCTYPE)  fetch first row only", pp);
		sendtoetd.FakeSign(docName, docdata, predid, signNumber, CertID, id, truewrk);
	}

	

	@Override
	public void doAfterSync(String formname, SyncObj obj, String sql, int signum) {
//	System.out.println("doafter");
		ETDForm form = null;
		DataBinder kinder = null;
		
		try{
			form = ETDForm.createFromArchive(obj.getBldoc());
			kinder = form.getBinder();
		} catch (Exception e){
			log.error(TypeConverter.exceptionToString(e));
//			e.printStackTrace();
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
//		e.printStackTrace();
	}
		
		
		
		try{
//			System.out.println("content");
		 StringBuffer content = new StringBuffer();
//		 content.append("к акту "+kinder.getValue("P_7"+" от "+kinder.getValue("P_8")));
//		 content.append(", ");
		 content.append("с "+kinder.getValue("P_3")+" по "+kinder.getValue("P_4"));
		 content.append(" "+kinder.getValue("P_23")+" цс");
		 StringBuffer id_pak = new StringBuffer();
		 id_pak.append(kinder.getValue("P_20"));
		
		 obj.setContent(content.toString());
		 obj.setId_pak(id_pak.toString());
		 obj.setMark(4054);
//		 facade.getNpjt().update("update snt.docstore set opisanie =:content, id_pak =:id_pak where id=:id", pp);
		} catch (Exception e){
			log.error(TypeConverter.exceptionToString(e));
//			e.printStackTrace();
		}
		etdsyncfacade.getWorkerWithorderNull(obj);
		etdsyncfacade.insertDocstore(sql, obj);
		etdsyncfacade.updateDSF(obj);
	}
	public ETDDocumentData EditBeforeOpen(String docName, ETDDocumentData doc) {
		// TODO Auto-generated method stub
		return doc;
	}
}
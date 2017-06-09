package sheff.rgd.ws.Controllers.TOR;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import ru.aisa.rgd.etd.objects.ETDDocumentData;
import ru.aisa.rgd.ws.documents.ETDForm;
import ru.aisa.rgd.ws.utility.DataBinder;
import ru.aisa.rgd.ws.utility.TypeConverter;
import sheff.rgd.ws.Abstract.AbstractAction;
import sheff.rjd.services.syncutils.SyncObj;
import sheff.rjd.ws.OCO.AfterSign.SendToEtd;

public class PretensionController extends AbstractAction{

	 private String parentform;
	 private SendToEtd sendtoetd;
	 protected final Logger log = Logger.getLogger(getClass());
	 private NamedParameterJdbcTemplate npjt;
	 
	 public String getParentform() {
		return parentform;
	 }

	public void setParentform(String parentname) {
		this.parentform = parentname;	
	}
	 
	 public SendToEtd getSendtoetd() {
	     return sendtoetd;
	 }

	 public void setSendtoetd(SendToEtd sendtoetd) {
	     this.sendtoetd = sendtoetd;
	 }
	
	public NamedParameterJdbcTemplate getNpjt() {
		return npjt;
	}

	public void setNpjt(NamedParameterJdbcTemplate npjt) {
		this.npjt = npjt;
	}

	@Override
	public void doAfterSign(String docName, String docdata, int predid, int signNumber, long id, String certserial, int WrkId) {
		try {
		    ETDForm form = ETDForm.createFromArchive(docdata.getBytes("UTF-8"));
            DataBinder binder = form.getBinder();
			binder.setNodeValue("archive", "drop");
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("id", id);
            map.put("bldoc", form.encodeToArchiv());
            map.put("docdata", form.transform("data"));
            getNpjt().update("update snt.docstore set bldoc = :bldoc, docdata = :docdata "
            		+ " where id = :id", map);
		} catch(Exception e) {
			log.error(TypeConverter.exceptionToString(e));
		}
	}

	@Override
	public void doAfterSave(String docName, String docdata, int predid, int signNumber, String CertID, long id, int WrkId) throws Exception {		
		try {
		    ETDForm form = ETDForm.createFromArchive(docdata.getBytes("UTF-8"));
            DataBinder binder = form.getBinder();
           	binder.setNodeValue("archive", "sign");
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("opisanie", binder.getValue("P_1") + ", " + binder.getValue("P_4"));
            map.put("price", binder.getValue("P_29"));
            map.put("id", id);
            map.put("bldoc", form.encodeToArchiv());
            map.put("docdata", form.transform("data"));
            getNpjt().update("update snt.docstore set opisanie = :opisanie, price = :price, "
            		+ " bldoc = :bldoc, docdata = :docdata where id = :id", map);
			sendtoetd.FakeSign(docName, docdata, predid, signNumber, CertID, id, WrkId);
		} catch (Exception e){
			log.error(TypeConverter.exceptionToString(e));
		}
	}

	@Override
	public void doAfterSync(String formname, SyncObj syncobj, String sql, int signum) {

	}
	
	public ETDDocumentData EditBeforeOpen(String docName, ETDDocumentData doc) {
		
		return doc;
	}
}

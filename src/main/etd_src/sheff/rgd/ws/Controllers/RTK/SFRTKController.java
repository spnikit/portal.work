package sheff.rgd.ws.Controllers.RTK;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.xml.transform.TransformerException;

import org.apache.log4j.Logger;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.w3c.dom.DOMException;

import ru.aisa.rgd.etd.dao.ETDSyncServiceFacade;
import ru.aisa.rgd.etd.objects.ETDDocumentData;
import ru.aisa.rgd.ws.documents.ETDForm;
import ru.aisa.rgd.ws.exeption.InternalException;
import ru.aisa.rgd.ws.exeption.ServiceException;
import ru.aisa.rgd.ws.utility.DataBinder;
import ru.aisa.rgd.ws.utility.TypeConverter;
import sheff.rgd.ws.Abstract.AbstractAction;
import sheff.rjd.services.syncutils.SendToRTKService;
import sheff.rjd.services.syncutils.SyncObj;
import sheff.rjd.ws.OCO.AfterEdit.FakeSignature;
import sheff.rjd.ws.OCO.AfterSign.SendToEtd;

import com.aisa.portal.invoice.ttk.SellerSignedInvoceToTTK;

public class SFRTKController extends AbstractAction{

	private String parentform;
	private ETDSyncServiceFacade etdsyncfacade;
	private NamedParameterJdbcTemplate npjt;
	private FakeSignature fakesignature;
	private SendToEtd sendtoetd;
	private SendToRTKService sendsign;
	private SellerSignedInvoceToTTK ssiobj;
	protected final Logger	log	= Logger.getLogger(getClass());
	

	public SellerSignedInvoceToTTK getSsiobj() {
		return ssiobj;
	}

	public void setSsiobj(SellerSignedInvoceToTTK ssiobj) {
		this.ssiobj = ssiobj;
	}
 
	public SendToEtd getSendtoetd() {
		return sendtoetd;
	}

	public void setSendtoetd(SendToEtd sendtoetd) {
		this.sendtoetd = sendtoetd;
	}

	public SendToRTKService getSendsign() {
		return sendsign;
	}

	public void setSendsign(SendToRTKService sendsign) {
		this.sendsign = sendsign;
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

	public ETDSyncServiceFacade getEtdsyncfacade() {
		return etdsyncfacade;
	}

	public void setEtdsyncfacade(ETDSyncServiceFacade etdsyncfacade) {
		this.etdsyncfacade = etdsyncfacade;
	}

	public void setParentform(String parentform) {
		this.parentform = parentform;
	}


	@Override
	public void doAfterSign(String docName, String docdata, int predid, int signNumber, long id, String certserial, int WrkId) {
		Map<String, Object> hm1 = new HashMap<String, Object>();
		hm1.put("id", id);
		int drop = getNpjt().queryForInt("select CASE when dropid is null then 0 else 1 end from snt.docstore where id = :id", hm1);
		String id_pak = getNpjt().queryForObject("select id_pak from snt.docstore where id = :id", hm1, String.class);
		
		
		byte[] bldoc = getNpjt().queryForObject("select bldoc from snt.docstore where id = :id", hm1, byte[].class);
		Date sendDateDocBd = getNpjt().queryForObject("select dt from snt.docstoreflow where docid = :id", hm1, Date.class); 
		ETDForm form = null;
		try {
			form = ETDForm.createFromArchive(bldoc);
		} catch (UnsupportedEncodingException e1) {
			log.error(TypeConverter.exceptionToString(e1));
		} catch (ServiceException e1) {
			log.error(TypeConverter.exceptionToString(e1));
		} catch (IOException e1) {
			log.error(TypeConverter.exceptionToString(e1));
		}
		DataBinder binder = form.getBinder();

		final SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
				
		if(drop==1||signNumber==1){
	
			try {
				if (drop==0){ 
					String sendDateDoc = sdf.format(sendDateDocBd);
					try{
						binder.setNodeValue("data_otpr_in_ekasufr", sendDateDoc);
					}catch(Exception e){
						log.error(TypeConverter.exceptionToString(e));
					}
					try{	
						binder.setNodeValue("data_otpr_in_ekasufrfilial", sendDateDoc);
					}catch(Exception e){
						log.error(TypeConverter.exceptionToString(e));
					}
					/*document.setBlDoc(form.encodeToArchiv());
					document.setDocData(form.transform("data"));
					document.setPredId(predid);
					document.setSignLvl(1);
					document.setType(docName);
					document.setId(id);*/
					//facade.insertDocumentWithDocid(document);
					hm1.put("bldoc", form.encodeToArchiv());
					hm1.put("docdata", form.transform("data"));
					getNpjt().update("update snt.docstore set bldoc = :bldoc, docdata = :docdata where id = :id",  hm1);
					String docdataDeclDoc = new String (form.encodeToArchiv(), "UTF-8");
					sendtoetd.SendToEtdMessage(id, docdataDeclDoc, parentform, signNumber, drop, true);
					//sendtoetd.SendToEtdMessage(id, docdata, parentform, signNumber, drop, true);

				}
//		   sendsign.SendSign(id, signNumber, drop, predid);
		    
		    } catch (Exception e) {
			  
		    	log.error(TypeConverter.exceptionToString(e));
		    	
		    } 
		}		
	}

	@Override
	public void doAfterSave(String docName, String docdata, int predid, int signNumber, String CertID, long id, int WrkId) throws Exception {
		
		
	}

	@Override
	public void doAfterSync(String formname, SyncObj obj, String sql, int signum) {
		if (signum==2){
			Map<String, Object> pp = new HashMap<String, Object>();
			pp.put("typeid", obj.getTypeid());
			pp.put("order", signum);
			pp.put("id", obj.getDocid());
			int wrkid = getNpjt().queryForInt("select wrkid from snt.doctypeflow where order = :order and dtid = :typeid ", pp);
			int predid = getNpjt().queryForInt("select predid from snt.docstore where id =:id ", pp);
			fakesignature.fakesign(obj.getDocid(), 0, wrkid, predid, obj.getBldoc(), obj.getDocdata());
			}
	}
	public ETDDocumentData EditBeforeOpen(String docName, ETDDocumentData doc) throws UnsupportedEncodingException, ServiceException, IOException, DOMException, InternalException, TransformerException {
		try{
		ETDForm form = ETDForm.createFromArchive(doc.getBlDoc());
		DataBinder b= form.getBinder();
		b.setNodeValue("documentId", String.valueOf(doc.getId()));
		doc.setBlDoc(form.encodeToArchiv());
		}catch (Exception e){
			log.error("Error in EditBeforeOpen: "+TypeConverter.exceptionToString(e));
		}
		return doc;
	}
}

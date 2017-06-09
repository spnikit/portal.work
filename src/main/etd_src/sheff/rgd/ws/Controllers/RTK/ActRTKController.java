package sheff.rgd.ws.Controllers.RTK;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.apache.tools.ant.util.facade.FacadeTaskHelper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import ru.aisa.rgd.etd.dao.ETDSyncServiceFacade;
import ru.aisa.rgd.etd.objects.ETDDocumentData;
import ru.aisa.rgd.utils.Base64;
import ru.aisa.rgd.ws.dao.ServiceFacade;
import ru.aisa.rgd.ws.documents.ETDForm;
import ru.aisa.rgd.ws.domain.Document;
import ru.aisa.rgd.ws.exeption.ServiceException;
import ru.aisa.rgd.ws.utility.DataBinder;
import ru.aisa.rgd.ws.utility.TypeConverter;
import sheff.rgd.ws.Abstract.AbstractAction;
import sheff.rjd.services.syncutils.SendToRTKService;
import sheff.rjd.services.syncutils.SyncObj;
import sheff.rjd.ws.OCO.AfterEdit.FakeSignature;
import sheff.rjd.ws.OCO.AfterSign.SendToEtd;

public class ActRTKController extends AbstractAction{

	private String parentform;
	private ETDSyncServiceFacade etdsyncfacade;
	private NamedParameterJdbcTemplate npjt;
	private FakeSignature fakesignature;
	private SendToEtd sendtoetd;
	private SendToRTKService sendsign;
	private ServiceFacade facade;
	protected final Logger	log	= Logger.getLogger(getClass());
  
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

	public NamedParameterJdbcTemplate getNpjt() {
		return npjt;
	}

	public void setNpjt(NamedParameterJdbcTemplate npjt) {
		this.npjt = npjt;
	}

	public String getParentform() {
		return parentform;
	}

	public FakeSignature getFakesignature() {
		return fakesignature;
	}

	public void setFakesignature(FakeSignature fakesignature) {
		this.fakesignature = fakesignature;
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
	

	public ServiceFacade getFacade() {
		return facade;
	}

	public void setFacade(ServiceFacade facade) {
		this.facade = facade;
	}

	@Override
	public void doAfterSign(String docName, String docdata, int predid, int signNumber, long id, String certserial, int WrkId) {
		Map<String, Object> hm1 = new HashMap<String, Object>();
		hm1.put("id", id);
		int drop = getNpjt().queryForInt("select CASE when dropid is null then 0 else 1 end from snt.docstore where id = :id", hm1);
//		String id_pak = getNpjt().queryForObject("select id_pak from snt.docstore where id = :id", hm1, String.class);
		
		
			if(signNumber==1||drop==1) {
		    
			try {
				if (drop==0) {
					Document doc = facade.getDocumentById(id);
					//XML
					try{
					ETDForm form = ETDForm.createFromArchive(doc.getBlDoc());
					DataBinder binder = form.getBinder();
					String xml_text1 = binder.getValue("text1");
					ETDForm form2 = ETDForm.createFromArchive(xml_text1.getBytes("UTF-8"));
					DataBinder binder2 = form2.getBinder();
					String idFileAct = binder2.getRootEl().getAttribute("ИдФайл");
					String dateDocAct = binder2.getElement("Документ").getAttribute("ДатаДок");
				    String timeDocAct = binder2.getElement("Документ").getAttribute("ВремДок"); 
				    binder.setNodeValue("idFileAct", idFileAct);
				    binder.setNodeValue("dateDocAct", dateDocAct);
				    binder.setNodeValue("timeDocAct", timeDocAct);
				    binder.setNodeValue("innZakaz", binder.getValue("P_33"));
				    
					String sign1 = binder.getValue("sign1");
					byte [] file1 = Base64.decode(xml_text1);
				    byte [] file_sign1 = Base64.decode(sign1);
				    
				    long typeid = facade.getDocumentDao().getTypeId(id);
				    
				    String id_pack = facade.getDocumentDao().getId_pack(id);
				    
				    facade.getDocumentDao().insertXMLandSign(file1, file_sign1, id, typeid, predid, id_pack);
				    
//				    hm1.put("bldoc", form.encodeToArchiv());
//				    hm1.put("docdata", form.transform("data"));
//				    getNpjt().update("update snt.docstore set bldoc = :bldoc, docdata = :docdata"
//				    		+ " where id = :id", hm1);
				    doc.setDocData(form.transform("data"));
					doc.setBlDoc(form.encodeToArchiv());
					facade.updateDocumentData(doc);
					} catch (Exception e){
						log.error(TypeConverter.exceptionToString(e));
					}
					sendtoetd.SendToEtdMessage(id, new String(doc.getBlDoc(), "UTF-8"), parentform, signNumber, drop, true);
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
			
			ETDForm form = null;

			try {
			form = ETDForm.createFromArchive(obj.getBldoc());
			DataBinder binder = form.getBinder();
			String text2 = binder.getValue("text2");
			String sign2 = binder.getValue("sign2");
			byte [] file2 = Base64.decode(text2);
		    byte [] file_sign2 = Base64.decode(sign2);
		    
		    String id_pack = facade.getDocumentDao().getId_pack(obj.getDocid());
		    
		    facade.getDocumentDao().insertXMLandSign(file2, file_sign2, obj.getDocid(), obj.getTypeid(), predid, id_pack);
		    
			} catch (Exception e) {
				log.error(TypeConverter.exceptionToString(e));
			}
		}
		
	}
	public ETDDocumentData EditBeforeOpen(String docName, ETDDocumentData doc) {
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

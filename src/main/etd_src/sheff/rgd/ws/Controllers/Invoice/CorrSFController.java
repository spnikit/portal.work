package sheff.rgd.ws.Controllers.Invoice;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import ru.aisa.rgd.etd.dao.ETDSyncServiceFacade;
import ru.aisa.rgd.etd.objects.ETDDocumentData;
import ru.aisa.rgd.ws.documents.ETDForm;
import ru.aisa.rgd.ws.utility.DataBinder;
import ru.aisa.rgd.ws.utility.TypeConverter;
import sheff.rgd.ws.Abstract.AbstractAction;
import sheff.rjd.services.syncutils.SyncObj;
import sheff.rjd.services.transoil.TransService;
import sheff.rjd.utils.Base64;
import sheff.rjd.ws.OCO.AfterSign.SendToEtd;

import com.aisa.portal.invoice.ttk.SellerSignedInvoceToTTK;


public class CorrSFController extends AbstractAction{

	 protected final Logger	log	= Logger.getLogger(getClass());
	 private NamedParameterJdbcTemplate npjt;
	 private SendToEtd sendtoetd;
     private TransService sendtotransoil;  
     private SellerSignedInvoceToTTK ssiobj;
 	 private String parentform;
 	 
     public NamedParameterJdbcTemplate getNpjt() {
		return npjt;
	}

	public void setNpjt(NamedParameterJdbcTemplate npjt) {
		this.npjt = npjt;
	}

	public SellerSignedInvoceToTTK getSsiobj() {
		return ssiobj;
	}

	public void setSsiobj(SellerSignedInvoceToTTK ssiobj) {
		this.ssiobj = ssiobj;
	}

     private ETDSyncServiceFacade etdsyncfacade;
	 
     
	 public ETDSyncServiceFacade getEtdsyncfacade() {
		return etdsyncfacade;
	}

	public void setEtdsyncfacade(ETDSyncServiceFacade etdsyncfacade) {
		this.etdsyncfacade = etdsyncfacade;
	}

	public SendToEtd getSendtoetd() {
	        return sendtoetd;
	 }

	 public String getParentform() {
		return parentform;
	}

	public void setParentform(String parentform) {
		this.parentform = parentform;
	}

	public void setSendtoetd(SendToEtd sendtoetd) {
	        this.sendtoetd = sendtoetd;
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
		String id_pak = getNpjt().queryForObject("select id_pak from snt.docstore where id = :id", hm1, String.class);
		
		
		
		if(drop==1){
		    
		    try {
			sendtoetd.SendToEtdMessage(id, docdata, parentform, 1, drop,false);
			sendtotransoil.SendSigntoTransoil(id, signNumber, drop, predid);

			
		    } catch (Exception e) {
		    	log.error(TypeConverter.exceptionToString(e));
		
		    } 
		}		
		
		//Обновление SF_SIGN
		
//		if (signNumber==1&&drop==0){
//			try{
//			 ETDForm form=ETDForm.createFromArchive(docdata.getBytes("UTF-8"));
//				DataBinder b = form.getBinder();
//			String scabinetid = etdsyncfacade
//					.getCabinetIdByPred(predid);
//			String bcabinetid = etdsyncfacade
//					.getCabinetIdByPred(etdsyncfacade.getPredMaker("ОАО «РЖД»"));
//
//			byte[] xml_text = Base64.decode(b
//					.getValue("xml_text"));
//
//			byte[] xml_sign = Base64.decode(b.getValue("xml_sign"));
//						
//			ssiobj.processSF(bcabinetid, scabinetid, id,
//					xml_text, xml_sign);
//		
//			sendtoetd.SendToEtdMessage(id, docdata, parentform, 1, drop,true);
//			
//		} catch (Exception e){
//			log.error(TypeConverter.exceptionToString(e));
//		}
//			npjt.update("update snt.docstore set sf_sign = 0 where id =:id", hm1);
//			
//			
//			
//		}
		
	}

	@Override
	public void doAfterSave(String docName, String docdata, int predid, int signNumber, String CertID, long id, int WrkId) throws Exception {
		 try{
		    	
			    	
			    	sendtoetd.FakeSign(docName, docdata, predid, signNumber, CertID, id, WrkId);
			    } catch (Exception e){
			    	log.error(TypeConverter.exceptionToString(e));
			    };
			    
			    sendtoetd.SendToEtdMessage(id, docdata, parentform, signNumber, 0, false);
			    sendtotransoil.SendSigntoTransoil(id, signNumber, 0, predid);
		
		
	}

	@Override
	public void doAfterSync(String formname, SyncObj syncobj, String sql, int signum) {
		etdsyncfacade.insertDocstore(sql, etdsyncfacade.getWorkerWithorderNull(syncobj));
		etdsyncfacade.updateDSF(syncobj);
		
		
		HashMap<String, Object> pp = new HashMap<String, Object>();
		pp.put("id", syncobj.getDocid());
		pp.put("sf_gfsgn", 0);
		npjt.update("update snt.docstore set sf_type = 1, sf_gfsgn =:sf_gfsgn where id =:id", pp);
		
		try{
		 ETDForm form=ETDForm.createFromArchive(syncobj.getBldoc());
			DataBinder b = form.getBinder();
		String bcabinetid = etdsyncfacade.getCabinetIdByPred(syncobj.getPredid());
		String scabinetid = etdsyncfacade.getCabinetIdByPred(etdsyncfacade.getPredMaker("ОАО «РЖД»"));
		byte[] xml_text = Base64.decode(b.getValue("xml_text"));
		byte[] xml_sign = Base64.decode(b.getValue("xml_sign"));
					
		ssiobj.processSF(bcabinetid, scabinetid, syncobj.getDocid(),xml_text, xml_sign, true);
	
		
	} catch (Exception e){
		log.error(TypeConverter.exceptionToString(e));
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


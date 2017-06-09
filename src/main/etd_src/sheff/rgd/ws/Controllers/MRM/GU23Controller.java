package sheff.rgd.ws.Controllers.MRM;

import java.io.UnsupportedEncodingException;
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
import sheff.rjd.services.syncutils.SendToRTKService;
import sheff.rjd.services.syncutils.SyncObj;
import sheff.rjd.services.transoil.TransService;
import sheff.rjd.ws.OCO.AfterEdit.FakeSignature;
import sheff.rjd.ws.OCO.AfterSign.SendToEtd;

public class GU23Controller extends AbstractAction{

	private String parentform;
	private ETDSyncServiceFacade etdsyncfacade;
    private SendToEtd sendtoetd;
    
    private String TAG_NAME_SIGN_COUNT = "sign_count";
	private NamedParameterJdbcTemplate npjt;
	private SendToRTKService signservice;
	private FakeSignature fakesignature;
	private TransService sendtotransoil;  
	
	
	public TransService getSendtotransoil() {
		return sendtotransoil;
	}

	public void setSendtotransoil(TransService sendtotransoil) {
		this.sendtotransoil = sendtotransoil;
	}

	public FakeSignature getFakesignature() {
		return fakesignature;
	}

	public void setFakesignature(FakeSignature fakesignature) {
		this.fakesignature = fakesignature;
	}

	protected final Logger	log	= Logger.getLogger(getClass());
	
    public SendToRTKService getSignservice() {
		return signservice;
	}

	public void setSignservice(SendToRTKService signservice) {
		this.signservice = signservice;
	}

	public NamedParameterJdbcTemplate getNpjt() {
		return npjt;
	}

	public void setNpjt(NamedParameterJdbcTemplate npjt) {
		this.npjt = npjt;
	}
   
    public SendToEtd getSendtoetd() {
        return sendtoetd;
    }

    public void setSendtoetd(SendToEtd sendtoetd) {
        this.sendtoetd = sendtoetd;
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
		
		
		if(signNumber==1||drop==1){
		    
		    try {
		    	
		    
			sendtoetd.SendToEtdMessage(id, docdata, parentform, signNumber, drop,false);
			
			sendtotransoil.SendSignMRMtoTransoil(id, signNumber, drop, predid);
			if (etdsyncfacade.getNamebyPredid(predid).indexOf("Усть-Луга Ойл")>-1){
				signservice.SendSign(id, signNumber, drop, predid);
			}
		    } catch (Exception e) {
		    	log.error(TypeConverter.exceptionToString(e));
		
		    } 
		}
	}

	@Override
	public void doAfterSave(String docName, String docdata, int predid, int signNumber, String CertID, long id, int WrkId) throws Exception {		
		 try{
//			 System.out.println("fake");
			  sendtoetd.FakeSign(docName, docdata, predid, signNumber, CertID, id, WrkId);
			  sendtoetd.PackUpdate(id);
			    } catch (Exception e){
			    	log.error(TypeConverter.exceptionToString(e));
			    };
			    sendtotransoil.SendBlobToTransoil(id, docName, predid);
		
	}

	@Override
	public void doAfterSync(String formname, SyncObj syncobj, String sql, int signum) {
		if (signum == 0 && !syncobj.isUpdate() && syncobj.getDrop() != 1){
				try{
				ETDForm form = ETDForm.createFromArchive(syncobj.getBldoc());
				  DataBinder binder = form.getBinder();
				  StringBuffer no = new StringBuffer();
				  no.append(binder.getValue("P_1"));
				  syncobj.setDogovor(no.toString());
				  StringBuffer content = new StringBuffer();
				  content.append(binder.getValue("P_5"));
				  content.append(", ");
				  content.append(binder.getValue("P_5a"));
				  content.append(", ");
				  content.append(binder.getValue("P_6"));
				  content.append(", ");
				  
				  if(binder.getValuesAsArray("P_22").length>0){
					  content.append(binder.getValuesAsArray("P_22")[0]); 
				  }
				  else{
					 content.append("-");
				  }
				    
				  content.append(", ");
				  
				  if(binder.getValuesAsArray("P_23").length>0){
					  content.append(binder.getValuesAsArray("P_23")[0]); 
				  }
				  else{
					 content.append("-");
				  }
				  
				  content.append(", ");
				  
				  if(binder.getValuesAsArray("P_24").length>0){
					  content.append(binder.getValuesAsArray("P_24")[0]); 
				  }
				  else{
					 content.append("-");
				  }
				  
				  syncobj.setContent(content.toString());
				}catch (Exception e){
					log.error(TypeConverter.exceptionToString(e));
				}
				
				if (syncobj.getId_pak().length()>2){
					syncobj.setVisible(0);
				}
				etdsyncfacade.insertDocstore(sql, etdsyncfacade.getWorkerWithorderNull(syncobj));
	//			etdsyncfacade.updateDSF(syncobj);
				try {
//					Map<String, Object> pp = new HashMap<String, Object>();
//					pp.put("id", syncobj.getDocid());
//					int predid = getNpjt().queryForInt("select predid from snt.docstore where id =:id ", pp);
					if (etdsyncfacade.getNamebyPredid(syncobj.getPredid()).indexOf(
							"ООО «Трансойл»") > -1) {
						sendtotransoil.SendMRMtoTransoil(syncobj.getEtdid(), formname, syncobj.getDocdata(),
								new String(syncobj.getBldoc(), "UTF-8"));
					}
				} catch (UnsupportedEncodingException e) {
					log.error(TypeConverter.exceptionToString(e));
				}
			} else if (syncobj.isUpdate() || syncobj.getDrop() == 1){
				if(syncobj.getDrop() == 1) {
					Map<String, Object> pp = new HashMap<String, Object>();
					pp.put("id", syncobj.getEtdid());
					int predid = npjt.queryForInt("select id from snt.pred where headid = "
							+ " (select predid from snt.docstore where etdid = :id) or id = (select predid from snt.docstore where etdid = :id)", pp);
					Long docid = npjt.queryForLong("select id from snt.docstore where etdid = :id", pp);
					sendtotransoil.SendSignMRMtoTransoil(docid, signum, 1, predid);
				} else {
					try{
						if (syncobj.getSignlvl()==0)
							syncobj.setOrder(1);
						else if (syncobj.getSignlvl()==1)
							syncobj.setOrder(2);
					 	Map<String, Object> pp = new HashMap<String, Object>();
						pp.put("typeid", syncobj.getTypeid());
						pp.put("order", syncobj.getOrder());
						pp.put("id", syncobj.getDocid());
						int wrkid = getNpjt().queryForInt("select wrkid from snt.doctypeflow where order = :order and dtid = :typeid ", pp);
						int predid = getNpjt().queryForInt("select predid from snt.docstore where id =:id ", pp);
						fakesignature.fakesign(syncobj.getDocid(), 0, wrkid, predid, syncobj.getBldoc(), syncobj.getDocdata());
//						if(syncobj.getOrder() == 2) {
//							sendtotransoil.SendSignMRMtoTransoil(syncobj.getDocid(), 2, 0, predid);
//						}
					} catch(Exception e){
						log.error(TypeConverter.exceptionToString(e));
					}
				}
			 }
	}

	public ETDDocumentData EditBeforeOpen(String docName, ETDDocumentData doc) {
		// TODO Auto-generated method stub
		return doc;
	}

}

package sheff.rgd.ws.Controllers.MRM;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

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



public class SPPVController extends AbstractAction{
	
	private String parentform;
	private NamedParameterJdbcTemplate npjt;
	private SendToEtd sendtoetd;
	private FakeSignature fakesignature;
	private SendToRTKService signservice;
	protected final Logger	log	= Logger.getLogger(getClass());
	
	public FakeSignature getFakesignature() {
		return fakesignature;
	}

	public void setFakesignature(FakeSignature fakesignature) {
		this.fakesignature = fakesignature;
	}

	public NamedParameterJdbcTemplate getNpjt() {
		return npjt;
	}
	
	public SendToEtd getSendtoetd() {
	    return sendtoetd;
	}
	public void setSendtoetd(SendToEtd sendtoetd) {
	    this.sendtoetd = sendtoetd;
	}
	public void setNpjt(NamedParameterJdbcTemplate npjt) {
		this.npjt = npjt;
	}
	
	public void setParentform(String parentname) {
		this.parentform = parentname;
	}

	public String getParentform() {
		return parentform;
	}
	
	public SendToRTKService getSignservice() {
		return signservice;
	}
	
	public void setSignservice(SendToRTKService signservice) {
		this.signservice = signservice;
	}
	
	public void doAfterSign(String docName, String docdata, int predid, int signNumber,  long id, String certserial,int WrkId) {
		
		Map<String, Object> hm1 = new HashMap<String, Object>();
		hm1.put("id", id);
		int drop = getNpjt().queryForInt("select CASE when dropid is null then 0 else 1 end from snt.docstore where id = :id", hm1);
		
		
		if(signNumber==1&&drop==0){
			
			 try{
				  ETDForm form = ETDForm.createFromArchive(docdata.getBytes("UTF-8"));
				  DataBinder binder = form.getBinder();
	                              
				  
				  if (binder.getValue("P_13a").length()>0){
	                
					HashMap<String, Object> pp = new HashMap<String, Object>();
					pp.put("okpo",  Integer.parseInt(binder.getValue("P_13a")));
					pp.put("id", id);
					int typeid = npjt.queryForInt("select typeid from snt.docstore where id = :id", pp);
					pp.put("typeid", typeid);
					npjt.update("update snt.docstore set pred_creator = (select id from snt.pred where okpo_kod = :okpo and headid is null), "
							+ "nwrkid = (select wrkid from snt.docacceptflow where dtid = :typeid) where id =:id", pp);
				 
					
				  
				  }
				  else if (binder.getValue("P_13a").length()==0){
					  	HashMap<String, Object> pp = new HashMap<String, Object>();
					  	pp.put("id", id);
					  	int typeid = npjt.queryForInt("select typeid from snt.docstore where id = :id", pp);
						pp.put("typeid", typeid);
						pp.put("order", 2);
						int wrkid = getNpjt().queryForInt("select wrkid from snt.doctypeflow where order = :order and dtid = :typeid ", pp);
						fakesignature.fakesign(id, 0, wrkid, predid, form.encodeToArchiv(), form.transform("data"));	  
//						sendtoetd.SendToEtdMessage(id, new String(form.encodeToArchiv(), "UTF-8"), parentform, signNumber, 0, true);
						
				  }
				  getContent(docdata.getBytes("UTF-8"), signNumber, id);
				  sendtoetd.SendToEtdMessage(id, new String(form.encodeToArchiv(), "UTF-8"), parentform, signNumber, 0, true);
				  
			  } catch (Exception e) {
				  log.error(TypeConverter.exceptionToString(e));
			
			    } 
			 
//			 signservice.SendSign(id, signNumber, drop, predid);
		}
		if(signNumber==2||drop==1){
			try {
					getContent(docdata.getBytes("UTF-8"), signNumber, id);
				   sendtoetd.SendToEtdMessage(id, docdata, parentform, signNumber, drop, false);
				
				    
				    } catch (Exception e) {
				    	log.error(TypeConverter.exceptionToString(e));
				    
				    } 
		}
	}


   public void doAfterSave(String docName, String docdata, int predid, int signNumber, String CertID, long id,int WrkId) throws Exception{
	
	}
   
   public void doAfterSync(String formname, SyncObj syncobj, String sql, int signum){
	   
	   
	   
	   HashMap<String, Object> pp = new HashMap<String, Object>();
		pp.put("docid", syncobj.getDocid());
//		pp.put("predid", syncobj.getPredid());
		pp.put("typeid", syncobj.getTypeid());
		pp.put("order", syncobj.getSignlvl());
		pp.put("id", syncobj.getDocid());
		int wrkid = getNpjt().queryForInt("select wrkid from snt.doctypeflow where order = :order and dtid = :typeid ", pp);
		int predid = -1;
		if (syncobj.getSignlvl()==2){
			predid = getNpjt().queryForInt("select pred_creator from snt.docstore where id =:id ", pp);
		}else 
		 predid = getNpjt().queryForInt("select predid from snt.docstore where id =:id ", pp);
		try {
			getContent(syncobj.getBldoc(), syncobj.getSignlvl(), syncobj.getDocid());
		} catch (Exception e) {
		log.error(TypeConverter.exceptionToString(e));
		} 
		fakesignature.fakesign(syncobj.getDocid(), 0, wrkid, predid, syncobj.getBldoc(), syncobj.getDocdata());
		
//		signservice.SendSign(syncobj.getEtdid(), signum, 0, syncobj.getPredid());
   }
   
   private void getContent(byte[] blob, int signcount, long docid) throws UnsupportedEncodingException, ServiceException, IOException, InternalException{
	   ETDForm form = ETDForm.createFromArchive(blob);
	   DataBinder db = form.getBinder();
	   String name = "";
	   switch (signcount){
	   case 1: name = db.getValue("P_12_1");
	   break;
	   case 2: name = db.getValue("P_13_1");
	   break;
	   case 3: name = db.getValue("P_14_1");
	   break;
	   }
	   try{
			StringBuffer content = new StringBuffer();
			content.append(name);
			content.append(", ");
			content.append(db.getValue("P_1"));
			content.append(", ");
			content.append(db.getValue("P_3_1"));
			content.append(", ");
			content.append(db.getValue("P_4"));
			content.append(", ");
			content.append(db.getValuesAsArray("P_9_1").length);
			content.append(" подач");
			
			HashMap<String, Object> pp = new HashMap<String, Object>();
			pp.put("id", docid);
			pp.put("content", content.toString());
			getNpjt().update("update snt.docstore set opisanie = :content where id =:id", pp);
			}catch(Exception e){
				System.err.println(e);
			}
	   
	   
   }

   public ETDDocumentData EditBeforeOpen(String docName, ETDDocumentData doc) {
		// TODO Auto-generated method stub
		return doc;
	}

}
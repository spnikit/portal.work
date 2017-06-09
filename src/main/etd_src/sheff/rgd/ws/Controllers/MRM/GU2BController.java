package sheff.rgd.ws.Controllers.MRM;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.log4j.Logger;
import org.springframework.jdbc.core.SqlOutParameter;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import ru.aisa.rgd.etd.objects.ETDDocumentData;
import ru.aisa.rgd.ws.documents.ETDForm;
import ru.aisa.rgd.ws.exeption.ServiceException;
import ru.aisa.rgd.ws.utility.DataBinder;
import ru.aisa.rgd.ws.utility.TypeConverter;
import sheff.rgd.ws.Abstract.AbstractAction;
import sheff.rjd.services.syncutils.SyncObj;
import sheff.rjd.services.transoil.TransService;
import sheff.rjd.utils.MyStoredProc;
import sheff.rjd.ws.OCO.AfterSign.SendToEtd;



public class GU2BController extends AbstractAction{
	
	private String parentform;
	private NamedParameterJdbcTemplate npjt;
	private SendToEtd sendtoetd;
	private DataSource ds;
	protected final Logger	log	= Logger.getLogger(getClass());
    private TransService sendtotransoil;  
	
	public TransService getSendtotransoil() {
		return sendtotransoil;
	}

	public void setSendtotransoil(TransService sendtotransoil) {
		this.sendtotransoil = sendtotransoil;
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
	
	public DataSource getDs() {
		return ds;
	}

	public void setDs(DataSource ds) {
		this.ds = ds;
	}
	
	private Map callNumProcedure(int typeid, int stancode) {
		MyStoredProc sproc = new MyStoredProc(getDs());
		sproc.setSql("SNT.GETDOC_M_NUM_GU2B");
		sproc.declareParameter(new SqlOutParameter("out", Types.CHAR));
		sproc.declareParameter(new SqlParameter("sFormID", Types.INTEGER));
		sproc.declareParameter(new SqlParameter("sCode", Types.INTEGER));
		sproc.declareParameter(new SqlOutParameter("DocNum", Types.INTEGER));
		sproc.compile();
		Map input = new HashMap();
		input.put("sFormID", typeid);
		input.put("sCode", stancode);
		return sproc.execute(input);
	}
	
	public void doAfterSign(String docName, String docdata, int predid, int signNumber,  long id, String certserial,int WrkId) {
		Map<String, Object> hm1 = new HashMap<String, Object>();
		hm1.put("id", id);
		int drop = getNpjt().queryForInt("select CASE when dropid is null then 0 else 1 end from snt.docstore where id = :id", hm1);
		
		if(signNumber==1&&drop==0){
			try {
				  ETDForm form = ETDForm.createFromArchive(docdata.getBytes("UTF-8"));
				  DataBinder binder = form.getBinder();
				  HashMap<String, Object> map = new HashMap<String, Object>();
				  map.put("id", id);
				  int typeId = npjt.queryForInt("select typeid from snt.docstore where id = :id", map);
				  int stancode;
				  String code = binder.getValue("P_1a");
				  
				  if (code.length()==6){
					  stancode=  Integer.parseInt(code.substring(0, 5));	
				    }
				    
				    else stancode = Integer.parseInt(code);
				  
				  
				  String no = callNumProcedure(typeId, stancode).get("DocNum").toString();
				  binder.setNodeValue("P_2", no); 
				  updateContent(docdata, id);
				  map.put("NO", no);
				  map.put("BLDOC", form.encodeToArchiv());
				  map.put("DOCDATA", form.transform("data"));
				  
			      npjt.update("update snt.docstore set NO = :NO, BLDOC = :BLDOC, docdata = :DOCDATA where id = :id", map);	    		
			      sendtoetd.SendToEtdMessage(id, new String(form.encodeToArchiv(), "UTF-8"), parentform, signNumber, 0, true);
			     
			      //для отчета ппс
				  String P_2AndP_3_1 = no + " " + binder.getValue("P_3_1");
				  String[] arrayVagnum = binder.getValuesAsArray("P_9_2");
				  map.put("vagnumlist", Arrays.asList(arrayVagnum));
				  map.put("NUM_GU2B", P_2AndP_3_1);
				  map.put("predid", predid);
				  npjt.update("update snt.ppsreport set NUM_GU2B = :NUM_GU2B where vagnum in (:vagnumlist)" +
				   "and predid in (select id from snt.pred where headid = :predid or id = :predid)", map);	    		
				 
				  if(signNumber == 1) {
					  sendtotransoil.SendSignMRMtoTransoil(id, signNumber, drop, predid);  
				  }
				  
		    } catch (Exception e) {
		    	log.error(TypeConverter.exceptionToString(e));
		
		    } 
		}
		
		if (drop==1 && signNumber == 0){
			sendtotransoil.SendSignMRMtoTransoil(id, signNumber, drop, predid);
		}
		
	}

   public void doAfterSave(String docName, String docdata, int predid, int signNumber, String CertID, long id,int WrkId) throws Exception{
	  
	   updateContent(docdata, id);
	}
   
   public void doAfterSync(String formname, SyncObj syncobj, String sql, int signum){
	   
   }
   
   private void updateContent(String docdata, long id) throws UnsupportedEncodingException, ServiceException, IOException{
	   
	   ETDForm form = ETDForm.createFromArchive(docdata.getBytes("UTF-8"));
		  DataBinder binder = form.getBinder();
		  
	   
	   try{
			StringBuffer content = new StringBuffer();
			content.append(binder.getValue("P_4"));
			content.append(", ");
			content.append(binder.getValue("P_5"));
			content.append(", ");
			content.append(binder.getValue("P_6"));
			content.append(", ");
			content.append(binder.getValue("P_7"));
			content.append(", ");
			content.append(binder.getValuesAsArray("P_9_1").length-1);
			content.append(" вагона(ов)");
			
			HashMap<String, Object> pp = new HashMap<String, Object>();
			pp.put("id", id);
			pp.put("content", content.toString());
			getNpjt().update("update snt.docstore set opisanie = :content where id =:id", pp);
			}catch(Exception e){
				log.error(TypeConverter.exceptionToString(e));
			}
   }
   public ETDDocumentData EditBeforeOpen(String docName, ETDDocumentData doc) {
		// TODO Auto-generated method stub
		return doc;
	}
   
}
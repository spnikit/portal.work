package sheff.rgd.ws.Controllers.MRM;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.sql.Time;
import java.sql.Types;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.log4j.Logger;
import org.springframework.jdbc.core.SqlOutParameter;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import ru.aisa.rgd.ws.documents.ETDForm;
import ru.aisa.rgd.ws.exeption.InternalException;
import ru.aisa.rgd.ws.exeption.ServiceException;
import ru.aisa.rgd.ws.utility.DataBinder;
import ru.aisa.rgd.ws.utility.TypeConverter;
import sheff.rgd.ws.Abstract.AbstractAction;
import sheff.rjd.services.syncutils.SendToRTKService;
import sheff.rjd.services.syncutils.SyncObj;
import sheff.rjd.utils.MyStoredProc;
import sheff.rjd.utils.XMLUtil;
import sheff.rjd.ws.OCO.AfterEdit.FakeSignature;
import sheff.rjd.ws.OCO.AfterSign.SendToEtd;
import ru.aisa.rgd.etd.objects.ETDDocumentData;

public class ZPVController extends AbstractAction{
	
	private String parentform;
	private NamedParameterJdbcTemplate npjt;
	private SendToEtd sendtoetd;
	private FakeSignature fakesignature;
	private DataSource ds;
	private SendToRTKService signservice;
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
	
	public SendToEtd getSendtoetd() {
	    return sendtoetd;
	}
	
	public void setSendtoetd(SendToEtd sendtoetd) {
	    this.sendtoetd = sendtoetd;
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
	
	private Map callNumProcedure(int predid, int typeid) {
		MyStoredProc sproc = new MyStoredProc(getDs());
		sproc.setSql("SNT.GETDOC_M_NUM_ZUV");
		sproc.declareParameter(new SqlOutParameter("out", Types.CHAR));
		sproc.declareParameter(new SqlParameter("PredId", Types.INTEGER));
		sproc.declareParameter(new SqlParameter("sFormID", Types.INTEGER));
		sproc.declareParameter(new SqlOutParameter("DocNum", Types.INTEGER));
		sproc.compile();
		Map input = new HashMap();
		input.put("PredId", predid);
		input.put("sFormID", typeid);
		return sproc.execute(input);
	}
	
	public ETDDocumentData EditBeforeOpen(String docName, ETDDocumentData doc) {
		
		return doc;
	}

	public void doAfterSign(String docName, String docdata, int predid, int signNumber,  long id, String certserial,int WrkId) {
		Map<String, Object> hm1 = new HashMap<String, Object>();
		hm1.put("id", id);
		int drop = getNpjt().queryForInt("select CASE when dropid is null then 0 else 1 end from snt.docstore where id = :id", hm1);
		if(signNumber == 1 && drop == 0) {
			try {
				byte[] blob = docdata.getBytes("UTF-8");
				ETDForm form = ETDForm.createFromArchive(blob);
				DataBinder binder = form.getBinder();
				HashMap<String, Object> map = new HashMap<String, Object>();
				map.put("id", id);
				int typeId = npjt.queryForInt("select typeid from snt.docstore where id = :id", map);
				String no = callNumProcedure(predid, typeId).get("DocNum").toString();
				binder.setNodeValue("P_2", no);    
				map.put("NO", no);
				map.put("BLDOC", form.encodeToArchiv());
				map.put("DOCDATA", form.transform("data"));
				npjt.update("update snt.docstore set NO = :NO, BLDOC = :BLDOC, docdata = :DOCDATA where id = :id", map);	    
				if(binder.getValue("P_13a").length() > 0) {
					HashMap<String, Object> pp = new HashMap<String, Object>();
					pp.put("okpo",  Integer.parseInt(binder.getValue("P_13a")));
					pp.put("id", id);
					int typeid = npjt.queryForInt("select typeid from snt.docstore where id = :id", pp);
					pp.put("typeid", typeid);
					npjt.update("update snt.docstore set pred_creator = (select id from snt.pred where okpo_kod = :okpo and headid is null), "
							+ "nwrkid = (select wrkid from snt.docacceptflow where dtid = :typeid) where id =:id", pp);
				}
				getContent(blob, signNumber, id);
				String exttext = getExttext(binder);
				signservice.SendSignMRM(id, signNumber, drop, predid, exttext);
				sendtoetd.SendToEtdMessage(id, new String(form.encodeToArchiv(), "UTF-8"), parentform, signNumber, 0, true);
			} catch(Exception e) {
				log.error(TypeConverter.exceptionToString(e));
			}
		} else if(signNumber > 1 || drop == 1) {
			try {
				byte[] blob = docdata.getBytes("UTF-8");
				ETDForm form = ETDForm.createFromArchive(blob);
				DataBinder binder = form.getBinder();
				String P_15 = binder.getValue("P_15");
				String P_12_4 = binder.getValue("P_12_4");
			    String P_13_1 = binder.getValue("P_13_1");
			    
				if(drop == 1) {
					signservice.SendSignMRM(id, signNumber, drop, predid, "");
				} else {
					if(!P_15.equals("-") && (!P_13_1.isEmpty() && P_12_4.isEmpty())) {
						addInReport(id, drop, docdata, docName, signNumber);
					}
					getContent(blob, signNumber, id);
					String exttext = getExttext(binder);
					signservice.SendSignMRM(id, signNumber, drop, predid, exttext);
					sendtoetd.SendToEtdMessage(id, docdata, parentform, signNumber, drop, false);	
				}
			} catch (Exception e) {
				log.error(TypeConverter.exceptionToString(e));
			} 
		}

	}


	public void doAfterSave(String docName, String docdata, int predid, int signNumber, String CertID, long id,int WrkId) throws Exception{
		try {
			ETDForm form = ETDForm.createFromArchive(docdata.getBytes("UTF-8"));
			DataBinder binder = form.getBinder();
			String P_15 = binder.getValue("P_15");
			int drop = binder.getInt("drop");
			String certId = new BigInteger(CertID, 16).toString();
			HashMap<String, Object> pp = new HashMap<String, Object>();
			pp.put("certid", certId);
			String sqlFioByCertId = "select id from SNT.personall where certserial = :certid";
			int idByCertId = getNpjt().queryForInt(sqlFioByCertId, pp);
			if(drop == 1) {
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("id", id);
				map.put("droptxt", P_15);
				map.put("dropid", idByCertId);
				map.put("droppredid", predid);
				map.put("dropwrkid", idByCertId);
				getNpjt().update("update snt.docstore set droptxt=:droptxt, dropid=:dropid, " +
	    				"droppredid=:droppredid, dropwrkid=:dropwrkid, droptime = current timestamp where id = :id", map);
				signservice.SendSignMRM(id, signNumber, drop, predid, "");
				sendtoetd.SendToEtdMessage(id, docdata, parentform, signNumber, drop, false);
				if(!P_15.equals("-")) {
					addInReport(id, drop, docdata, docName, signNumber);
				}
			}
		} catch (Exception e) {
			log.error(TypeConverter.exceptionToString(e));
		} 
	}

	public void doAfterSync(String formname, SyncObj syncobj, String sql, int signum) {
		  try {
			   ETDForm form = ETDForm.createFromArchive(syncobj.getBldoc());
			   DataBinder binder = form.getBinder();   
			   String P_15 = binder.getValue("P_15");
			   String P_14_1 = binder.getValue("P_14_1");
			   HashMap<String, Object> pp = new HashMap<String, Object>();
			   pp.put("predid", syncobj.getPredid());	
			   pp.put("etdid", syncobj.getEtdid());
			   int iddoc = getNpjt().queryForInt("select id from snt.docstore where etdid = :etdid", pp);
			   pp.put("id", iddoc);

			   int wrkid = getNpjt().queryForInt("select id from snt.wrkname where rtrim(name) = 'АС ЭТД'", pp);
			   int predid = getNpjt().queryForInt("select predid from snt.docstore where id =:id ", pp);
			   int drop = getNpjt().queryForInt("select CASE when dropid is null then 0 else 1 end from snt.docstore where id = :id", pp);

			   String P_13a = binder.getValue("P_13a");
			   pp.put("docdata", form.transform("data"));
			   npjt.update("update snt.docstore set docdata = :docdata where id = :id", pp);
			   if(drop == 0) {
				   if(!P_15.equals("-") && (!P_14_1.isEmpty() && P_13a.isEmpty())) {
					   addInReport(iddoc, drop, new String(syncobj.getBldoc()), formname, signum);
				   }
				   getContent(syncobj.getBldoc(), syncobj.getSignlvl(), syncobj.getDocid());
				   String exttext = getExttext(binder);
				   signservice.SendSignMRM(syncobj.getDocid(), signum, drop, predid, exttext);
			   } else {
				   if(!P_15.equals("-") && P_13a.isEmpty()) {
					   addInReport(iddoc, drop, new String(syncobj.getBldoc()), formname, signum);
				   }
				   signservice.SendSignMRM(syncobj.getDocid(), signum, 1, predid, "");
			   }
			   fakesignature.fakesign(syncobj.getDocid(), 0, wrkid, predid, syncobj.getBldoc(), syncobj.getDocdata());
		   } catch (Exception e) {
				log.error(TypeConverter.exceptionToString(e));
			} 
	}
  
  private void getContent(byte[] blob, int signcount, long docid) throws UnsupportedEncodingException, ServiceException, IOException, InternalException{
	   ETDForm form = ETDForm.createFromArchive(blob);
	   DataBinder db = form.getBinder();
	   StringBuffer content = new StringBuffer();
	   String name = "";
	   String P_12_1 = db.getValue("P_12_1");
	   String P_12_4 = db.getValue("P_12_4");
	   String P_13_1 = db.getValue("P_13_1");
	   String P_14_1 = db.getValue("P_14_1");
	   if(!P_12_1.isEmpty() && P_13_1.isEmpty() && P_14_1.isEmpty()) {
		   name = db.getValue("P_12_1"); 
	   } else if(!P_13_1.isEmpty() && P_12_4.isEmpty() && P_14_1.isEmpty()) {
		   name = db.getValue("P_13_1"); 
	   } else if(!P_12_4.isEmpty() && P_14_1.isEmpty()) {
		   name = db.getValue("P_12_4"); 
	   }  else if(!P_14_1.isEmpty() && P_12_4.isEmpty()) {
	    	name = db.getValue("P_14_1"); 
	    } else if(!P_14_1.isEmpty() && !P_12_4.isEmpty() && signcount == 3){
	    	name = db.getValue("P_12_4"); 
	    } else if(!P_14_1.isEmpty() && !P_12_4.isEmpty() && signcount == 4){
	    	name = db.getValue("P_14_1"); 
	    }
	   
	   try {
			content.append(name);
			content.append(", ");
			content.append(db.getValue("P_1"));
			content.append(", ");
			content.append(db.getValue("P_7_1"));
			content.append(", ");
			content.append(db.getValue("P_7_2"));
			content.append(", ");
			content.append(db.getValue("P_8_1"));
			content.append(", ");
			content.append(db.getValue("P_8_2"));
			content.append(", ");
			content.append(db.getValuesAsArray("P_9_1").length);
			content.append(" вагона(ов)");
			
			HashMap<String, Object> pp = new HashMap<String, Object>();
			pp.put("id", docid);
			pp.put("content", content.toString());
			getNpjt().update("update snt.docstore set opisanie = :content where id =:id", pp);
		} catch(Exception e) {
			log.error(TypeConverter.exceptionToString(e));
		}
	   
  }
  
  
  private void addInReport(long id, int drop, String docdata, String formname, int signcount) {
	  Map<String, Object> pp = new HashMap<String, Object>();
	  pp.put("id", id);
	  String selectForDrop = "select droptxt, droptime, opisanie, predid, no, typeid, " + 
			  "(select rtrim(cast(rtrim(fname) || ' ' || rtrim(mname) || ' ' || rtrim(lname) as char(240) ccsid unicode)) " + 
			  "from SNT.personall where id = ds.dropid) as fio " + 
			  "from snt.docstore ds where id = :id";
	  String selectForSign = "select ldate, ltime, opisanie, predid, no, typeid, " + 
			  "(select rtrim(cast(rtrim(fname) || ' ' || rtrim(mname) || ' ' || rtrim(lname) as char(240) ccsid unicode)) " + 
			  "from SNT.personall where id = ds.lpersid) as fio " + 
			  "from snt.docstore ds where id = :id";
	  String insert = "INSERT INTO snt.REPORT_ZUV_ZPV (docid, typeid, predid, status, num, description, "
	  		+ "reason, num_reasongroup, wrkname, dt_sign_or_drop)"
	  		+ " Values (:docid, :typeid, :predid, :status, :num, :description, :reason, :reason_group, :wrkname, :dt_sign_or_drop)";
	  ETDForm form = null;
	  DataBinder db = null;
	  try {
		  form = ETDForm.createFromArchive(docdata.getBytes());
		  db = form.getBinder();
		  int P_15a = db.getInt("P_15a");
		  Integer okpoSobst = db.getInt("P_1a");
		  pp.put("okpo_kod", okpoSobst);
		  int predId = npjt.queryForInt("select id from snt.pred where okpo_kod = :okpo_kod and headid is null", pp);
		  if(drop == 1) {
			  SqlRowSet rs = getNpjt().queryForRowSet(selectForDrop, pp);
			  while(rs.next()) {
				  pp.put("predid", predId);
				  String name_otkl = db.getValue("name_otkl");
				  if(name_otkl.isEmpty()) {
					  pp.put("wrkname", rs.getString("fio"));
				  } else {
					  pp.put("wrkname", name_otkl);
				  }
				  pp.put("num", rs.getString("no"));
				  pp.put("dt_sign_or_drop", rs.getTimestamp("droptime"));
				  pp.put("typeid",  rs.getString("typeid"));
			  }
			  pp.put("status", "Отклонен");
			  pp.put("docid", id);
			  pp.put("description", getDescription(db, formname));
			  pp.put("reason", db.getValue("P_15"));
			  pp.put("reason_group", getReasonGroupNum(P_15a));
			  npjt.update(insert, pp);
			  
			  if(db.getValue("P_13a").length() > 0) {
				  Integer okpoOp = db.getInt("P_13a");
				  if(!okpoOp.equals(okpoSobst)) {
					  pp.put("okpo_kod", okpoOp);
					  predId = npjt.queryForInt("select id from snt.pred where okpo_kod = :okpo_kod and headid is null", pp);
					  pp.put("predid", predId);
					  npjt.update(insert, pp);
				  }
			  }
		  } else {
			  SqlRowSet rs = getNpjt().queryForRowSet(selectForSign, pp);
			  Date ldate = null;
			  Time ltime = null;
			  String name = getName(db, signcount);
			  pp.put("wrkname", name);
			  while(rs.next()) {
				  ldate = rs.getDate("ldate");
				  ltime = rs.getTime("ltime");
				  pp.put("predid", predId);
				  pp.put("num", rs.getString("no"));
				  pp.put("typeid",  rs.getString("typeid"));
			  }
			  SimpleDateFormat myDateFormat = new SimpleDateFormat("yyyy-MM-dd");
			  String ldt = myDateFormat.format(ldate);
			  myDateFormat.applyPattern("yyyy-MM-dd HH:mm:ss");
			  Date DT_sign_or_drop = myDateFormat.parse(ldt + " " + ltime);
			  pp.put("dt_sign_or_drop", DT_sign_or_drop);
			  pp.put("docid", id);
			  pp.put("status", "Подписан");
			  pp.put("description", getDescription(db, formname));
			  pp.put("reason", db.getValue("P_15"));
			  pp.put("reason_group", getReasonGroupNum(P_15a));
			  npjt.update(insert, pp);
			  if(db.getValue("P_13a").length() > 0) {
				  Integer okpoOp = db.getInt("P_13a");
				  if(!okpoOp.equals(okpoSobst)) {
					  pp.put("okpo_kod", okpoOp);
					  predId = npjt.queryForInt("select id from snt.pred where okpo_kod = :okpo_kod and headid is null", pp);
					  pp.put("predid", predId);
					  npjt.update(insert, pp);
				  }
			  }
		  } 
	  } catch (Exception e) {
		  log.error(TypeConverter.exceptionToString(e));
	  }
 }
  
  private String getDescription(DataBinder binder, String formname) throws InternalException {
	  StringBuilder description = new StringBuilder();
	  if(formname.equals("ЗУВ")) {
		  description.append(binder.getValue("P_4"));
		  description.append(" ");
		  description.append(binder.getValue("P_7_1"));
		  description.append(" ");
		  description.append(binder.getValue("P_7_2"));
		  description.append(" ");
		  description.append(binder.getValue("P_8_1"));
		  description.append(" ");
		  description.append(binder.getValue("P_8_2"));
		  description.append(" ");
		  description.append(binder.getValue("P_7_3"));
		  description.append(" ");
		  description.append(binder.getValue("P_7_4"));
		  description.append(" ");
		  description.append(binder.getValue("P_8_3"));
		  description.append(" ");
		  description.append(binder.getValue("P_8_4"));
		  description.append(" ");
		  description.append(binder.getValuesAsArray("P_9_1").length);
		  description.append(" вагона(ов)");  
	  } else if(formname.equals("ЗПВ")) {
		  description.append(binder.getValue("P_4"));
		  description.append(" ");
		  description.append(binder.getValue("P_7_1"));
		  description.append(" ");
		  description.append(binder.getValue("P_7_2")); 
		  description.append(" ");
		  description.append(binder.getValue("P_8_1"));
		  description.append(" ");
		  description.append(binder.getValue("P_8_2"));
		  description.append(" ");
		  description.append(binder.getValue("P_7_3"));
		  description.append(" ");
		  description.append(binder.getValue("P_7_4"));
		  description.append(" ");
		  description.append(binder.getValuesAsArray("P_9_1").length);
		  description.append(" вагона(ов)");
	  }
	  	  
	  return description.toString();
  }

  private String getExttext(DataBinder binder) {
	    String exttext = "";
		try {
		    String P_12_1 = binder.getValue("P_12_1");
			String P_12_4 = binder.getValue("P_12_4");
			String P_13_1 = binder.getValue("P_13_1");
			String P_14_1 = binder.getValue("P_14_1");
			int flag = binder.getValue("flag").length();
			if(!P_12_1.isEmpty() && P_13_1.isEmpty()) {
				exttext = P_12_1;
			} else if(!P_13_1.isEmpty() && P_12_4.isEmpty() && P_14_1.isEmpty() && flag > 0) {
				String[] tags = {"P_13_1", "P_7_3", "P_7_4", "P_15a"};
				exttext = generateExt(binder, tags);
			} else if(!P_13_1.isEmpty() && P_12_4.isEmpty() && P_14_1.isEmpty() && flag == 0) {
				exttext = P_13_1;
			} else if(!P_12_4.isEmpty() && P_14_1.isEmpty()) {
				exttext = P_12_4;
			} else if(!P_14_1.isEmpty() && !P_12_4.isEmpty()){
				exttext = P_12_4;
			} else if(!P_14_1.isEmpty() && P_12_4.isEmpty() && flag > 0) {
				String[] tags = {"P_14_1", "P_7_3", "P_7_4", "P_15a"};
				exttext = generateExt(binder, tags);
			}
		} catch (Exception e) {
			log.error(TypeConverter.exceptionToString(e));
			e.printStackTrace();
		}
	  return exttext;
  }
  
  private String generateExt(final DataBinder binder, String[] tags) {
      org.w3c.dom.Document newDocument = binder.createDocumentByCloningNodes("data", "data", tags);
      return XMLUtil.getFormattedStringWithoutHeader(newDocument);
  }
  
  private String getReasonGroupNum(int reasonCode) {
	  String groupNum = null;
	  if(reasonCode <= 7) {
		  groupNum = "0";
	  } else if(reasonCode <= 25) {
		  groupNum = "1";
	  } else if(reasonCode <= 46) {
		  groupNum = "2";
	  }
	  return groupNum;
  }
  
  private String getName(DataBinder binder, int signcount) {
		 String name = "";
		 try {
			 DataBinder db = binder;
			 String P_12_1 = db.getValue("P_12_1");
			 String P_12_4 = db.getValue("P_12_4");
			 String P_13_1 = db.getValue("P_13_1");
			 String P_14_1 = db.getValue("P_14_1");
			 if(!P_12_1.isEmpty() && P_13_1.isEmpty() && P_14_1.isEmpty()) {
				 name = db.getValue("fio1"); 
			 } else if(!P_13_1.isEmpty() && P_12_4.isEmpty() && P_14_1.isEmpty()) {
				 name = db.getValue("fio2"); 
			 } else if(!P_12_4.isEmpty() && P_14_1.isEmpty()) {
				 name = db.getValue("fio4"); 
			 }  else if(!P_14_1.isEmpty() && P_12_4.isEmpty()) {
				 name = db.getValue("fio3"); 
			 } else if(!P_14_1.isEmpty() && !P_12_4.isEmpty() && signcount == 3){
				 name = db.getValue("fio4"); 
			 } else if(!P_14_1.isEmpty() && !P_12_4.isEmpty() && signcount == 4){
				 name = db.getValue("fio3"); 
			 }
		 } catch(Exception e) {
			 log.error(TypeConverter.exceptionToString(e));
		 }

		return name;
	 }
}

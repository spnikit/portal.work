package sheff.rgd.ws.Controllers.MRM;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.activemq.transport.tcp.ExceededMaximumConnectionsException;
import org.apache.log4j.Logger;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import ru.aisa.etdportal.controllers.Vagnum;
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

public class GU45Controller extends AbstractAction{

	private String parentform;
	private ETDSyncServiceFacade etdsyncfacade;
    private SendToEtd sendtoetd;
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
		Map hm1 = new HashMap();
		hm1.put("id", id);
		int drop = getNpjt().queryForInt("select CASE when dropid is null then 0 else 1 end from snt.docstore where id = :id", hm1);
		
		
		if(signNumber==2||drop==1){
		    
		    try {
				sendtoetd.SendToEtdMessage(id, docdata, parentform, signNumber, drop,false);
			    
				sendtotransoil.SendSignMRMtoTransoil(id, signNumber, drop, predid);
				
				if (etdsyncfacade.getNamebyPredid(predid).indexOf("Усть-Луга Ойл")>-1) {
					signservice.SendSign(id, signNumber, drop, predid);
				}

		    } catch (Exception e) {
		    	log.error(TypeConverter.exceptionToString(e));
		
		    } 
		}
	}

	@Override
	public void doAfterSave(String docName, String docdata, int predid, int signNumber, String CertID, long id, int WrkId) throws Exception {		
		
		
	}

	@Override
	public void doAfterSync(String formname, SyncObj syncobj, String sql, int signum) {
		if (signum==1&&!syncobj.isUpdate() && syncobj.getDrop() != 1){
			try{
			  ETDForm form = ETDForm.createFromArchive(syncobj.getBldoc());
			  DataBinder binder = form.getBinder();
			  StringBuffer no = new StringBuffer();
			  no.append(binder.getValue("P_2"));
			  syncobj.setDogovor(no.toString());
			  StringBuffer content = new StringBuffer();
			  content.append("на ");
			  content.append(binder.getValue("P_3"));
			  content.append(", ");
			  content.append(binder.getValue("P_4"));
			  content.append(", ");
			  content.append(binder.getValue("P_5"));
			  content.append(", ");
			  content.append(binder.getValue("P_8_1"));
			  content.append(", ");
			  content.append(binder.getValue("P_8_2"));
			  content.append(", ");
			  content.append(binder.getValue("P_8_3"));
			  content.append(", ");
			  content.append(binder.getValuesAsArray("P_9_1").length);
			  content.append(" вагона(ов)");
			  syncobj.setContent(content.toString());
			  Map<String, Object> pp = new HashMap<String, Object>();
			 // pp.put("id", syncobj.getDocid());
			  int priznak = binder.getInt("P_3a");

			  try {
//				  int predid = getNpjt().queryForInt("select predid from snt.docstore where id =:id ", pp);
				  if (etdsyncfacade.getNamebyPredid(syncobj.getPredid()).indexOf(
						  "ООО «Трансойл»") > -1) {
					  sendtotransoil.SendMRMtoTransoil(syncobj.getEtdid(), formname, syncobj.getDocdata(),
							  new String(syncobj.getBldoc(), "UTF-8"));
				  }
			  } catch (UnsupportedEncodingException e) {
//				  e.printStackTrace();
				  log.error(TypeConverter.exceptionToString(e));
			  }
			  
			  doSomethingWithReport(priznak, binder, syncobj.getPredid(), syncobj);  
			}catch (Exception e){
//				e.printStackTrace();
				log.error(TypeConverter.exceptionToString(e));
			}
			
			
			
			etdsyncfacade.insertDocstore(sql, etdsyncfacade.getWorkerWithorderNull(syncobj));
			etdsyncfacade.updateDSF(syncobj);
			}
			
			else if (syncobj.isUpdate() ||  syncobj.getDrop() == 1){
				if(syncobj.getDrop() == 1) {
					Map<String, Object> pp = new HashMap<String, Object>();
					pp.put("id", syncobj.getEtdid());
					int predid = npjt.queryForInt("select id from snt.pred where headid = "
							+ " (select predid from snt.docstore where etdid = :id) or id = (select predid from snt.docstore where etdid = :id)", pp);
					Long docid = npjt.queryForLong("select id from snt.docstore where etdid = :id", pp);
					sendtotransoil.SendSignMRMtoTransoil(docid, signum, 1, predid);
				} else {
					try{
						
						ETDForm form = ETDForm.createFromArchive(syncobj.getBldoc());
						  DataBinder binder = form.getBinder();
						  StringBuffer no = new StringBuffer();
						  no.append(binder.getValue("P_2"));
						  syncobj.setDogovor(no.toString());
						  StringBuffer content = new StringBuffer();
						  content.append("на ");
						  content.append(binder.getValue("P_3"));
						  content.append(", ");
						  content.append(binder.getValue("P_4"));
						  content.append(", ");
						  content.append(binder.getValue("P_5"));
						  content.append(", ");
						  content.append(binder.getValue("P_8_1"));
						  content.append(", ");
						  content.append(binder.getValue("P_8_2"));
						  content.append(", ");
						  content.append(binder.getValue("P_8_3"));
						  content.append(", ");
						  content.append(binder.getValuesAsArray("P_9_1").length);
							content.append(" вагона(ов)");
						  
						  syncobj.setContent(content.toString());
						}catch (Exception e){
							log.error(TypeConverter.exceptionToString(e));
						}
					try{
						
						
						if (syncobj.getSignlvl()==1)
							syncobj.setOrder(2);
						
						else if (syncobj.getSignlvl()==3||syncobj.getSignlvl()==2)
							syncobj.setOrder(3);
				 
						Map<String, Object> pp = new HashMap<String, Object>();
						pp.put("typeid", syncobj.getTypeid());
						pp.put("order", syncobj.getOrder());
						pp.put("id", syncobj.getDocid());
						pp.put("content",syncobj.getContent());
						int wrkid = getNpjt().queryForInt("select wrkid from snt.doctypeflow where order = :order and dtid = :typeid ", pp);
						int predid = getNpjt().queryForInt("select predid from snt.docstore where id =:id ", pp);
						fakesignature.fakesign(syncobj.getDocid(), 0, wrkid, predid, syncobj.getBldoc(), syncobj.getDocdata());
						getNpjt().update("update snt.docstore set opisanie =:content where id =:id", pp);
						
						if(syncobj.isNo_sign()) {
							ETDForm form = ETDForm.createFromArchive(syncobj.getBldoc());
							DataBinder binder = form.getBinder();
							sendtotransoil.refusalToSign(syncobj.getDocid(), predid, binder);
						}
				
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
	
	private void doSomethingWithReport(int priznak, DataBinder binder, int predid, SyncObj syncobj) throws Exception{
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("id", syncobj.getDocid());
		map.put("predid", predid);
		String okpo_kode = getNpjt().queryForObject("select okpo_kod from snt.pred where id = :predid", map, String.class);
		if(okpo_kode.equals("92399820")) {
			Long iddoc = syncobj.getDocid();
			if(priznak == 1) {
				String[] p_9_2_1ValueArray = binder.getValuesAsArray("P_9_2_1");
				String[] P_9_6_1ValueArray = binder.getValuesAsArray("P_9_6_1");
				for(int i = 0; i < p_9_2_1ValueArray.length; i++) {
						map.put("vagnum", p_9_2_1ValueArray[i]);
						map.put("predid", predid);
						Integer count = getNpjt().queryForInt("select count(0) "
								+ "from snt.PPSREPORT where vagnum = :vagnum  and predid in " 
								+ "(select id from snt.pred where headid = :predid or id = :predid)  and complete is null", map);

						SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
						Calendar calendar = Calendar.getInstance();
						String d = P_9_6_1ValueArray[i] + "." + calendar.get(Calendar.YEAR);
						Date date = dateFormat.parse(d);
						if(count > 0) {
							SqlRowSet rs = getNpjt().queryForRowSet("select ID, NUM_GU45_POD, gu45_pod_date, "
									+ "ID_GU45_POD, CODE_STATION from snt.PPSREPORT "
									+ "where vagnum = :vagnum and predid in "
									+ "(select id from snt.pred where headid = :predid or id = :predid)  and complete is null", map);
							while(rs.next()) {
								if(rs.getObject("ID_GU45_POD") != null) {
									map.put("NUM_GU45_POD_1", binder.getValue("P_2"));
									map.put("gu45_pod_date_1", date);
									map.put("ID_GU45_POD_1", iddoc);
									map.put("CODE_STATION", binder.getValue("P_1a"));
									map.put("OKPO_PERFORMER", binder.getValue("P_4a"));
									map.put("ID", rs.getLong("ID"));
									getNpjt().update("update snt.PPSREPORT set NUM_GU45_POD_1 = :NUM_GU45_POD_1, "
											+ "gu45_pod_date_1 = :gu45_pod_date_1, ID_GU45_POD_1 = :ID_GU45_POD_1, "
											+ "CODE_STATION = :CODE_STATION, OKPO_PERFORMER = :OKPO_PERFORMER"
											+ " where ID = :ID", map);
								} else if(rs.getObject("ID_GU45_POD") == null) {
									map.put("NUM_GU45_POD", binder.getValue("P_2"));
									map.put("gu45_pod_date", date);
									map.put("ID_GU45_POD", iddoc);
									map.put("CODE_STATION", binder.getValue("P_1a"));
									map.put("OKPO_PERFORMER", binder.getValue("P_4a"));
									map.put("ID", rs.getLong("ID"));
									getNpjt().update("update snt.PPSREPORT set NUM_GU45_POD = :NUM_GU45_POD, "
											+ "gu45_pod_date = :gu45_pod_date, ID_GU45_POD = :ID_GU45_POD, "
											+ "CODE_STATION = :CODE_STATION, OKPO_PERFORMER = :OKPO_PERFORMER "
											+ "where ID = :ID", map);
								} else {
									throw new Exception();
								}
							}
						} else {
							String insert = "insert into snt.ppsreport(VAGNUM, NUM_GU45_POD,"
									+ " gu45_pod_date, ID_GU45_POD, CODE_STATION, PREDID)"
									+ " values(:VAGNUM, :NUM_GU45_POD, :gu45_pod_date, :ID_GU45_POD, :CODE_STATION, "
									+ ":predid)";
							map.put("NUM_GU45_POD", binder.getValue("P_2"));
							map.put("gu45_pod_date", date);
							map.put("ID_GU45_POD", iddoc);
							map.put("CODE_STATION", binder.getValue("P_1a"));
							map.put("OKPO_PERFORMER", binder.getValue("P_4a"));
							map.put("VAGNUM", p_9_2_1ValueArray[i]);
							getNpjt().update(insert, map);
						}
					}
				
			} else if(priznak == 2) {
				String[] p_9_2_1ValueArray = binder.getValuesAsArray("P_9_2_1");
				String[] P_9_8_1ValueArray = binder.getValuesAsArray("P_9_8_1");
				for(int i = 0; i < p_9_2_1ValueArray.length; i++) {
						map.put("vagnum", p_9_2_1ValueArray[i]);
						map.put("predid", predid);
						Integer count = getNpjt().queryForInt("select count(0) "
								+ "from snt.PPSREPORT where vagnum = :vagnum  and predid in " 
								+ "(select id from snt.pred where headid = :predid or id = :predid) and complete is null", map);
						if(count > 0) {
							SqlRowSet rs = getNpjt().queryForRowSet("select ID, NUM_GU45_POD, gu45_pod_date, "
									+ " NUM_ACT_1, ID_GU45_POD, ID_APP_TREATMENT_1, NUM_ACT, ID_APP_TREATMENT, "
									+ " ID_GU45_UB, CODE_STATION from snt.PPSREPORT "
									+ " where vagnum = :vagnum and predid in "
									+ "(select id from snt.pred where headid = :predid or id = :predid)"
									+ " and complete is null", map);
							while(rs.next()) {
								if(rs.getObject("ID_GU45_UB") != null) {
									map.put("NUM_GU45_UB_1", binder.getValue("P_2"));
									map.put("DATE_UB_GU45_1", P_9_8_1ValueArray[i]);
									map.put("ID_GU45_UB_1", iddoc);
									map.put("CODE_STATION", binder.getValue("P_1a"));
									map.put("ID", rs.getLong("ID"));
									
									int countRec = getNpjt().queryForInt("select count(0) from snt.ppsreport "
											+ " where ID_APP_TREATMENT_1 is not null and "
											+ "(NUM_ACT_1 is not null or type_act_1 = 3) and ID_GU45_POD is not null "
											+ " and id = :ID", map);
									
									if(countRec > 0) {
										map.put("complete", 1);
										getNpjt().update("update snt.PPSREPORT set NUM_GU45_UB_1 = :NUM_GU45_UB_1, "
												+ " DATE_UB_GU45_1 = :DATE_UB_GU45_1, ID_GU45_UB_1 = :ID_GU45_UB_1, "
												+ " CODE_STATION = :CODE_STATION, COMPLETE = :complete "
												+ " where ID = :ID", map);
									} else {
										getNpjt().update("update snt.PPSREPORT set NUM_GU45_UB_1 = :NUM_GU45_UB_1, "
												+ " DATE_UB_GU45_1 = :DATE_UB_GU45_1, ID_GU45_UB_1 = :ID_GU45_UB_1, "
												+ " CODE_STATION = :CODE_STATION where ID = :ID", map);
									}
									
								} else if(rs.getObject("ID_GU45_UB") == null) {
									map.put("NUM_GU45_UB", binder.getValue("P_2"));
									map.put("DATE_UB_GU45", P_9_8_1ValueArray[i]);
									map.put("ID_GU45_UB", iddoc);
									map.put("CODE_STATION", binder.getValue("P_1a"));
									map.put("ID", rs.getLong("ID"));
									
									int countRec = getNpjt().queryForInt("select count(0) from snt.ppsreport "
											+ " where ID_APP_TREATMENT is not null and "
											+ "(NUM_ACT is not null or (type_act = 3 and (ID_APP_TREATMENT_1 is null or "
											+ "(id_act_workability_1 is not null and  ID_APP_TREATMENT_1 is not null)))) "
											+ " and ID_GU45_POD is not null and id = :ID", map);
									
									if(countRec > 0) {
										map.put("complete", 1);
										getNpjt().update("update snt.PPSREPORT set NUM_GU45_UB = :NUM_GU45_UB, "
												+ "DATE_UB_GU45 = :DATE_UB_GU45, ID_GU45_UB = :ID_GU45_UB, "
												+ "CODE_STATION = :CODE_STATION, COMPLETE = :complete where ID = :ID", map);
									} else {
										getNpjt().update("update snt.PPSREPORT set NUM_GU45_UB = :NUM_GU45_UB, "
												+ "DATE_UB_GU45 = :DATE_UB_GU45, ID_GU45_UB = :ID_GU45_UB, "
												+ "CODE_STATION = :CODE_STATION where ID = :ID", map);
									}
								} else {
									throw new Exception();
								}
							} 
						} else {
							String insert = "insert into snt.ppsreport(VAGNUM, NUM_GU45_UB,"
									+ " DATE_UB_GU45, ID_GU45_UB, PREDID)"
									+ " values(:VAGNUM, :NUM_GU45_UB, :DATE_UB_GU45, :ID_GU45_UB, :predid)";
							map.put("NUM_GU45_UB", binder.getValue("P_2"));
							map.put("DATE_UB_GU45", P_9_8_1ValueArray[i]);
							map.put("ID_GU45_UB", iddoc);
							map.put("VAGNUM", p_9_2_1ValueArray[i]);
							getNpjt().update(insert, map);
						}
					}
	
			}
		}
	}

}

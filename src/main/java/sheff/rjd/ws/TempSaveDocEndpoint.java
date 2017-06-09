package sheff.rjd.ws;


import java.io.PrintWriter;
import java.io.StringWriter;
import java.math.BigInteger;
import java.sql.Types;
import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.log4j.Logger;
import org.springframework.jdbc.core.SqlOutParameter;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.oxm.Marshaller;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;

import ru.aisa.edt.SaveRequestDocument;
import ru.aisa.edt.SaveResponseDocument;
import ru.aisa.edt.Signature;
import ru.aisa.edt.Singledoc;
import ru.aisa.rgd.etd.dao.ETDFacade;
import ru.aisa.rgd.etd.ws.ETDAbstractSecurityEndoint;
import sheff.rgd.ws.Abstract.DoAction;
import sheff.rjd.utils.MyStoredProc;
import sheff.rjd.utils.TSPutils;
import sheff.rjd.utils.XMLUtil;
import sheff.rjd.ws.OCO.TORLists;

public class TempSaveDocEndpoint
		extends
		ETDAbstractSecurityEndoint<SaveRequestDocument, SaveResponseDocument> {
	
//	Форматирование даты и времени
	//private SimpleDateFormat dateFormater;
	//private SimpleDateFormat timeFormater;
	
	private NamedParameterJdbcTemplate npjt;
	
	private DataSource ds;
//	public AfterSave getAfterSave() {
//	    return afterSave;
//	}
//
//	public void setAfterSave(AfterSave afterSave) {
//	    this.afterSave = afterSave;
//	}

	public TSPutils getTsp() {
	    return tsp;
	}

	public void setTsp(TSPutils tsp) {
	    this.tsp = tsp;
	}

	private DoAction formControllers;
	private TransactionTemplate transTemplate;
//	private AfterSign afterSign;
//	private AfterSave afterSave;
	private static Logger	log	= Logger.getLogger(TempSaveDocEndpoint.class);
	private TSPutils tsp;
	
	
//	Вещь, создающая контент для столбца "Краткое содержание"
	//private ShortContentCreator shortContentCreator;

	public TempSaveDocEndpoint(Marshaller marshaller) {
		super(marshaller);
	}

	@Override
	protected SaveResponseDocument composeInvalidSecurityResponce(Signature signature) 
	{
		SaveResponseDocument responseDocument = SaveResponseDocument.Factory.newInstance();
		//responseDocument.setSaveResponse("bad signature"); 
		
		responseDocument.getSaveResponse().setResponse("bad signature");
		responseDocument.getSaveResponse().setDocid("-1");
		return responseDocument;
	}

	@Override
	protected SaveRequestDocument convertRequest(Object obj) {
		SaveRequestDocument requestDocument = (SaveRequestDocument) obj;		
		return requestDocument;
	}

	@Override
	protected Signature getSecurity(SaveRequestDocument requestDocument) 
	{
		Signature s = requestDocument.getSaveRequest().getSecurity();
		return s;
	}
	
	private void writeException(Exception ex){
		StringWriter outError = new StringWriter();
		PrintWriter errorWriter = new PrintWriter( outError );
		ex.printStackTrace(errorWriter);
		log.error(outError.toString());
	}

	
	@Override
	protected SaveResponseDocument processRequest(SaveRequestDocument requestDocument, Signature signature1, ETDFacade facade) throws Exception 
	{
		
		
		SaveRequestDocument srd = (SaveRequestDocument )requestDocument;
		final SaveResponseDocument srespd  = SaveResponseDocument.Factory.newInstance();
		srespd.addNewSaveResponse();
		
		
		//System.out.println("SAVE");
		
		final Singledoc doc = srd.getSaveRequest();
		byte[] signature = doc.getSecurity().getSign();
		
		final String certid = doc.getSecurity().getCertid();
		String username = doc.getSecurity().getUsername();
		
		
		
		//realform check
		
		
		String realname = XMLUtil.parseXmlWithSax(doc.getXdata(), "formname").trim(); 
		String name = doc.getName();
		
		/*System.out.println("realname: "+realname);
		System.out.println("name: "+name);*/
		
		if (name.equals(realname)){
		
		try {			
					    final Map<String, Object> pp = new HashMap<String, Object>();
						pp.put("NO", doc.getNumber());
						pp.put("BLDOC", ("application/vnd.xfdl;content-encoding=\"base64-gzip\"\n"+doc.getXmldata()).getBytes("UTF-8"));//doc.getBldoc()); //sheff
						pp.put("DOCTYPE", name);
						pp.put("PREDID", doc.getPredid());						
						pp.put("WHEREID", doc.getWhereId());
						pp.put("LPERSID", doc.getLastsignpersid());
						pp.put("CERTSERIAL", new BigInteger(certid,16).toString());
						final String[] sigs = doc.getSignaturesArray();
						
						
						//Обрушение всех устоев маршрутизации документов систем на базе АС ЭТД
//						System.out.println("FR: "+doc.getFr());
						
						if ((doc.getFr()==2||doc.getFr()==3||doc.getFr()==1||doc.getFr()==17) && !name.equals("Претензия")){
						int truewrk = getNpjt().queryForInt("select wrkid from snt.doctypeflow where wrkid in (select id from snt.wrkname where issm=1 or issm=17) and dtid in "+
"(select id FROM SNT.doctype where name = :DOCTYPE)  fetch first row only", pp);
						pp.put("WRKID", doc.getWrkid()==truewrk?doc.getWrkid():truewrk);
						doc.setWrkid(truewrk);
						}
						else pp.put("WRKID", doc.getWrkid());
						
						pp.put("DOCDATA", doc.getXdata());
						pp.put("OPIS", doc.getOpisanie());
//						System.out.println(doc.getXdata());
					//	System.out.println("wrkid: "+doc.getWrkid());
						
						
						//--FIX-ME
					
						if (doc.getDatedoc().length()<9) pp.put("DATDOC", "01.01.1901"); else
							pp.put("DATDOC", doc.getDatedoc());
						//pp.put("DATDOC","2009-11-22 21:58:11.531");
						//System.out.println(doc);
//						int persCId = -1;
//					            String pStr = "";
//					            if(doc.getCounterPers() == null || doc.getCounterPers().length() == 0 || doc.getCounterPers().equals("default") || doc.getName().equals("ФПУ-26"))
//					            {
//					                persCId = -1;
//					                pStr = pStr+" ";
//					            } else
//					           if(!doc.getCounterPers().equals("Отправить всем"))
//					            {
//					                pStr = pStr+" PERSID = :COUNTERPERS, ";
//					                persCId = getNpjt().queryForInt("select id from SNT.personall where fname = '"+doc.getCounterPers().split(" ")[0]+"' and mname='"+doc.getCounterPers().split(" ")[1]+"' and lname = '"+doc.getCounterPers().split(" ")[2]+"'", new HashMap());	
//					            } else
//					            {
//					                persCId = -2;
//					            }
//					            int counterPersId = persCId;
//					            int counterPredId = -1;
//					            if(doc.getCounterPred().length() == 0 || doc.getCounterPred() == null)
//					            {
//					            	 counterPredId = doc.getPredid();
//					               
//					            } else 
//					            {
//					          //  	System.out.println(doc.getCounterPred());
//					            	Map n= new HashMap();
//					            	n.put("cntpred", doc.getCounterPred());
//					                counterPredId = getNpjt().queryForInt("select id from SNT.pred where vname =:cntpred", n);	
//					         
//					            
//					            }
//					           
//					            
//					            pp.put("COUNTERPRED", Integer.valueOf(counterPredId));
//					            pp.put("COUNTERPERS", Integer.valueOf(counterPersId));
//						
//						final String persSql= "" + pStr;
						 pp.put("COUNTERPERS", -1);				
						try {
						if (doc.getId() > -1 ) {
						//update	
							
							pp.put("ID", doc.getId());
							
							MyStoredProc sproc = new MyStoredProc(getDs());
							sproc.setSql("SNT.UnLockDoc");
							sproc.declareParameter(new SqlOutParameter("out", Types.CHAR));
							sproc.declareParameter(new SqlParameter("docid", Types.INTEGER));
							sproc.declareParameter(new SqlParameter("userid", Types.INTEGER));
							sproc.declareParameter(new SqlOutParameter("lockid", Types.INTEGER));
							sproc.declareParameter(new SqlOutParameter("lockname", Types.CHAR));
							sproc.compile();
							Map input = new HashMap();
							input.put("docid", pp.get("ID"));
							input.put("userid", getNpjt().queryForInt("select id from SNT.personall where CERTSERIAL = :CERTSERIAL ", pp));
							Map output = sproc.execute(input);	
										
							
						
						final int existingsigs = getNpjt().queryForInt("select count(dt) from SNT.docstoreflow where docid = :ID and dt is not null", pp);	
                  
//						System.out.println(existingsigs);
//						System.out.println(sigs.length);
//						
						
						
							if ( existingsigs == sigs.length ){
							
								int archive = getNpjt().queryForInt("select case when signlvl is null then 1 else 0 end from snt.docstore where id =:ID", pp);
								
								
								if (archive==0){
								
								if (!TORLists.acceptlist.contains(name)){
//								getNpjt().update("update SNT.docstore set persId =:COUNTERPERS, bldoc = :BLDOC, docdata = :DOCDATA where id = :ID",pp);  
									getNpjt().update("update SNT.docstore set bldoc = :BLDOC, docdata = :DOCDATA where id = :ID",pp);  
								}
//							   	if (doc.getName().equals("Счет-фактура"))
//					        		getNpjt().update("update SNT.DOCSTORE set sf_sign =1 where id=:ID",pp); 
									 if(formControllers != null)
							                        {
							                            formControllers.doAfterSave(doc.getName(), doc.getXmldata(), doc.getPredid(), sigs.length, certid, doc.getId(), doc.getWrkid());
							                        }
									
									
									getNpjt().update("update SNT.ENCLOSEDDOCS set template=bcp_template, updt=0, bcp_template=null where docid = :ID and updt=1",pp);  
						        	getNpjt().update("delete from SNT.ENCLOSEDDOCS where docid=:ID and updt=-1",pp);  
							    
						        	srespd.getSaveResponse().setDocid(String.valueOf(doc.getId()));
							       srespd.getSaveResponse().setResponse("updated successfully");
								} else if (archive==1){
									if (TORLists.mrm.contains(name)){
									srespd.getSaveResponse().setDocid(String.valueOf(doc.getId()));
								    srespd.getSaveResponse().setResponse("archive document");
								       }
									
									else {
										srespd.getSaveResponse().setDocid(String.valueOf(doc.getId()));
									    srespd.getSaveResponse().setResponse("updated successfully");
									}
								}
							
							
							}
							else if (existingsigs+1<sigs.length) {
							//	System.out.println("2");
								srespd.getSaveResponse().setDocid(String.valueOf(doc.getId()));
								       srespd.getSaveResponse().setResponse("morethanone");
							}
							
						    else if (existingsigs>sigs.length){   //delete  signature
						    if (sigs.length!=1){
//						    	System.out.println("3");
							String reason=doc.getXdata().substring(doc.getXdata().indexOf("<Prich>")+7, doc.getXdata().indexOf("</Prich>"))+". ";
							if (doc.getXdata().indexOf("<PrichMore/>") == -1) reason += doc.getXdata().substring(doc.getXdata().indexOf("<PrichMore>")+11, doc.getXdata().indexOf("</PrichMore>"));
							if (reason.equals(". ")){
								reason=doc.getXdata().substring(doc.getXdata().indexOf("<Prich1>")+8, doc.getXdata().indexOf("</Prich1>"))+". ";
								if (doc.getXdata().indexOf("<PrichMore1/>") == -1) reason += doc.getXdata().substring(doc.getXdata().indexOf("<PrichMore1>")+12, doc.getXdata().indexOf("</PrichMore1>"));
							}
							pp.put("REASON", reason);
							getTransTemplate().execute(new TransactionCallbackWithoutResult() {
								
								  protected void doInTransactionWithoutResult(TransactionStatus status) {
									    try {
									          	getNpjt().update("delete from SNT.docstoreflow where DOCID = :ID ", pp);
									    	
									    	
									    	getNpjt().update("update SNT.docstore set bldoc = :BLDOC, " +
									    			" docdata = :DOCDATA, LDATE = null, " +
									    			" LTIME = null, LPERSID = null, " +
									    			" LWRKID = null,DROPWRKID = :WRKID ," +
									    			" DROPID = (select id from SNT.personall where CERTSERIAL = :CERTSERIAL), " +
									    			" DROPTXT = :REASON, DROPTIME = current timestamp, SIGNLVL = 0 where ID = :ID ", pp);
										
									    	if (doc.getName().equals("Счет-фактура"))
								        		getNpjt().update("update SNT.DOCSTORE set sf_sign =1 where id=:ID",pp);
									    	
										getNpjt().update("delete from SNT.docstoreflow where DOCID = :ID ", pp);
										
										
										
									    	getNpjt().update("update SNT.ENCLOSEDDOCS set template=bcp_template, updt=0, bcp_template=null where docid = :ID and updt=1",pp);  
								        	getNpjt().update("delete from SNT.ENCLOSEDDOCS where docid=:ID and updt=-1",pp);  
									    
									    	
									    	srespd.getSaveResponse().setDocid(String.valueOf(doc.getId()));
										       srespd.getSaveResponse().setResponse("inserted successfully");
									    	 
									    } catch (Exception ex) {
									    	 StringWriter outError = new StringWriter();
											   PrintWriter errorWriter = new PrintWriter( outError );
											   ex.printStackTrace(errorWriter);
											   ex.printStackTrace(System.out);
											   log.error(outError.toString());
									           status.setRollbackOnly();
									     
									       srespd.getSaveResponse().setDocid(String.valueOf(doc.getId()));
									       srespd.getSaveResponse().setResponse("error when inserting or updating");
									    }
									  }
									  
								});
							
						} 
						
							  
						   }
//--------------------------
								else {
									getTransTemplate().execute(new TransactionCallbackWithoutResult() {
										
										  protected void doInTransactionWithoutResult(TransactionStatus status) {
											    try {
//											    	System.out.println("4");
											//  	getNpjt().update("update SNT.docstore set no = :NO,persId =:COUNTERPERS,bldoc = :BLDOC, docdata = :DOCDATA, DROPID = null , DROPTXT = null, DROPTIME = null  where id = :ID",pp); 
										
//											  	getNpjt().update("update SNT.docstore set persId =:COUNTERPERS,bldoc = :BLDOC, docdata = :DOCDATA, DROPID = null , DROPTIME = null  where id = :ID",pp); 

											    	getNpjt().update("update SNT.docstore set bldoc = :BLDOC, docdata = :DOCDATA, DROPID = null , DROPTIME = null  where id = :ID",pp); 
											    	Map output = callSignProcedure(doc.getId(), doc.getLastsignpersid(), doc.getWrkid(), doc.getPredid(), tsp.getTSP(certid));
											    	if (doc.getName().equals("Счет-фактура"))
										        		getNpjt().update("update SNT.DOCSTORE set sf_sign =1 where id=:ID",pp);  
													
											    	if(formControllers != null)
								                                {
											    		formControllers.doAfterSign(doc.getName(), doc.getXmldata(), doc.getPredid(), sigs.length, doc.getId(), certid, doc.getWrkid());
								                                }
											    	
													if (output.get("timestamp") == null || output.get("timestamp").toString().trim().length()<2){
														
													    
													    srespd.getSaveResponse().setDocid(String.valueOf(doc.getId()));
														       srespd.getSaveResponse().setResponse("doctypeflow_invalid");
														
														status.setRollbackOnly();
													}
													
													else { 
													
													if (existingsigs==0){
													
//														getNpjt().update("update SNT.docstore set "+ persSql +
//															 	" PREDID = :COUNTERPRED where id=:ID",pp);
														
														
													 }
													
													getNpjt().update("update SNT.ENCLOSEDDOCS set template=bcp_template, updt=0, bcp_template=null where docid = :ID and updt=1",pp);  
										        	getNpjt().update("delete from SNT.ENCLOSEDDOCS where docid=:ID and updt=-1",pp);  
											    
                                               
													
										        	srespd.getSaveResponse().setResponse("inserted successfully");
										        	srespd.getSaveResponse().setDocid(String.valueOf(doc.getId()));
													}
												
											    								    	
											    } catch (Exception ex) {
											    	 StringWriter outError = new StringWriter();
													   PrintWriter errorWriter = new PrintWriter( outError );
													   ex.printStackTrace(errorWriter);
													   ex.printStackTrace(System.out);
													   log.error(outError.toString());
											           status.setRollbackOnly();
											          srespd.getSaveResponse().setResponse("error when inserting or updating");
											        	srespd.getSaveResponse().setDocid(String.valueOf(doc.getId()));
											           
											    
											    }
											  }
											  
										});									  				
								}												
																		
						}
						
	/////////////////////////////////////////////////////////////////////////////////////	//				
						else {
						if (sigs.length == 0){
							getTransTemplate().execute(new TransactionCallbackWithoutResult() {
								
							  protected void doInTransactionWithoutResult(TransactionStatus status) {
								    try {
								    //	System.out.println("5");
									int id = doc.getNewDocId();
								     	pp.put("globalid", id);
								   pp.put("WRKID", doc.getWrkid());
								     	
								     	
								    
								     	getNpjt().update(" insert into SNT.DocStore (ID,PREDID,PRED_CREATOR,PERSID,PERS_CREATOR,TYPEID,CRDATE,CRTIME,BLDOC,NWRKID,LWRKID,LDATE,LTIME,LPERSID,INUSEID,DOCDATA, SIGNLVL, VISIBLE, MADEID) "+
										    	" values ( :globalid, :PREDID,:PREDID,:COUNTERPERS,:LPERSID, (select id FROM SNT.doctype where name = :DOCTYPE fetch first row only), current date,current time, :BLDOC  , null , NULL,NULL,NULL ,NULL,NULL, :DOCDATA, " +
										    	" (CASE  (select flowcnt from SNT.doctype where name = :DOCTYPE fetch first row only )  WHEN 0 THEN null ELSE 0 END),1, :WRKID)  ",pp  );
								     	
								   	
								     	
								    	getNpjt().update("update SNT.ENCLOSEDDOCS set template=bcp_template, updt=0, bcp_template=null where docid = :globalid and updt=1",pp);  
							        	getNpjt().update("delete from SNT.ENCLOSEDDOCS where docid=:globalid and updt=-1",pp);  
							        	
//							        	if (doc.getName().equals("Счет-фактура"))
//							        		getNpjt().update("update SNT.DOCSTORE set sf_sign =1 where id=:globalid",pp);  
							        	if(formControllers != null)
							                        {
							        		formControllers.doAfterSave(doc.getName(), doc.getXmldata(), doc.getPredid(), sigs.length, certid, id, doc.getWrkid());
							                        }
								    	 srespd.getSaveResponse().setResponse("inserted successfully");
								        	srespd.getSaveResponse().setDocid(String.valueOf(id));
								         
								    	
								    	
								    } catch (Exception ex) {
								        status.setRollbackOnly();
								        StringWriter outError = new StringWriter();
									    PrintWriter errorWriter = new PrintWriter( outError );
									    ex.printStackTrace(errorWriter);
									    ex.printStackTrace(System.out);
									    log.error(outError.toString());
									    
									    srespd.getSaveResponse().setResponse("error when inserting or updating");
								        	srespd.getSaveResponse().setDocid(String.valueOf(-1));
								         
								    }
								  }
								  
							});														
							}
													
								else{
	
									getTransTemplate().execute(new TransactionCallbackWithoutResult() {
										
										  protected void doInTransactionWithoutResult(TransactionStatus status) {
											    try {
											    //	System.out.println("6");
											    	int id = doc.getNewDocId();
											    	pp.put("globalid", id);
											    	
											    	getNpjt().update(" insert into SNT.DocStore (ID,PREDID,PRED_CREATOR,PERSID,PERS_CREATOR,TYPEID,CRDATE,CRTIME,BLDOC,NWRKID,LWRKID,LDATE,LTIME,LPERSID,INUSEID,DOCDATA, SIGNLVL, VISIBLE) "+
													    	" values ( :globalid , :PREDID,:PREDID, :COUNTERPERS,:LPERSID,(select id FROM SNT.doctype where name = :DOCTYPE fetch first row only ), current date,current time, :BLDOC  , null , NULL,NULL,NULL ,NULL,NULL, :DOCDATA, " +
													    	" (CASE  (select flowcnt from SNT.doctype where name = :DOCTYPE fetch first row only )  WHEN 0 THEN null ELSE 0 END), 1) ",pp  );
											    	
											    		    	
											    	
											    		if (doc.getName().equals("Счет-фактура"))
											        		getNpjt().update("update SNT.DOCSTORE set sf_sign =1 where id=:globalid",pp);  
											    	
											    	Map output = callSignProcedure(id, doc.getLastsignpersid(), doc.getWrkid(), doc.getPredid(), tsp.getTSP(certid));
													
											    	if(formControllers != null)
								                                {
											    		formControllers.doAfterSign(doc.getName(), doc.getXmldata(), doc.getPredid(), sigs.length, id, certid, doc.getWrkid());
								                                }
													
													if (output.get("timestamp") == null || output.get("timestamp").toString().trim().length()<2){
														
														 
														 srespd.getSaveResponse().setResponse("doctypeflow_invalid");
													        	srespd.getSaveResponse().setDocid(String.valueOf(id));
														 
														 status.setRollbackOnly();
													}
													else { 
													
											    
//														getNpjt().update("update SNT.docstore set "+ persSql +
//															 	" PREDID = :COUNTERPRED where id=:globalid",pp);
//														 
															 getNpjt().update("update SNT.ENCLOSEDDOCS set template=bcp_template, updt=0, bcp_template=null where docid = :globalid and updt=1",pp);  
													        getNpjt().update("delete from SNT.ENCLOSEDDOCS where docid=:globalid and updt=-1",pp);  
														    
													
													 srespd.getSaveResponse().setResponse("inserted successfully");
												        	srespd.getSaveResponse().setDocid(String.valueOf(id));
													 }
											    } catch (Exception ex) {
											    	
											        status.setRollbackOnly();
											        StringWriter outError = new StringWriter();
												    PrintWriter errorWriter = new PrintWriter( outError );
												    ex.printStackTrace(errorWriter);
												    ex.printStackTrace(System.out);
												    log.error(outError.toString());
												  
												    srespd.getSaveResponse().setResponse("error when inserting or updating");
											        	srespd.getSaveResponse().setDocid(String.valueOf(-1));
											        	status.setRollbackOnly();
												    //SNMPSender.sendMessage(ex);
											    }
											  }
											  
										});	
			
				
							}

						
			
						}
						} catch (Exception ex){
							    StringWriter outError = new StringWriter();
							    PrintWriter errorWriter = new PrintWriter( outError );
							    ex.printStackTrace(errorWriter);
							    log.error(outError.toString());
						        ex.printStackTrace();
						       
						        srespd.getSaveResponse().setResponse("error when inserting or updating");
					        	srespd.getSaveResponse().setDocid(String.valueOf(-1));
					
						}
				
				}
				
				catch (Exception ex){
					 StringWriter outError = new StringWriter();
					   PrintWriter errorWriter = new PrintWriter( outError );
					   ex.printStackTrace(errorWriter);
					     log.error(outError.toString());
					   srespd.getSaveResponse().setResponse("error when inserting or updating");
			        	srespd.getSaveResponse().setDocid(String.valueOf(-1));
				}
		} else {
			srespd.getSaveResponse().setResponse("wrong document");
			srespd.getSaveResponse().setDocid(String.valueOf(-1));
		}	
	
		return srespd;
		
		
	}
	
	private Map callSignProcedure(long id,int userid,int wrkid, int predid, byte[] tsp){	
	 	MyStoredProc sproc = new MyStoredProc(getDs());
		sproc.setSql("SNT.SignDoc");
		sproc.declareParameter(new SqlOutParameter("out", Types.CHAR));
		sproc.declareParameter(new SqlParameter("docid", Types.BIGINT));
		sproc.declareParameter(new SqlParameter("userid", Types.INTEGER));
		sproc.declareParameter(new SqlParameter("wrkid", Types.INTEGER));
		sproc.declareParameter(new SqlParameter("predid", Types.INTEGER));
		sproc.declareParameter(new SqlParameter("ts", Types.BLOB));
		sproc.declareParameter(new SqlOutParameter("timestamp", Types.CHAR));
		sproc.compile();
		Map input = new HashMap();
		input.put("docid", id);
		input.put("userid", userid);
		input.put("wrkid", wrkid);
		input.put("predid", predid);
		input.put("ts", tsp);
		return sproc.execute(input);
}

	
	public DataSource getDs() {
		return ds;
	}


	public void setDs(DataSource ds) {
		this.ds = ds;
	}


	
	
	public NamedParameterJdbcTemplate getNpjt() {
		return npjt;
	}
	public void setNpjt(NamedParameterJdbcTemplate npjt) {
		this.npjt = npjt;
	}


//	public void setAfterSign(AfterSign dispatcher3) {
//		this.afterSign = dispatcher3;
//	}
//
//
//	public AfterSign getAfterSign() {
//		return afterSign;
//	}
	public TransactionTemplate getTransTemplate() {
		return transTemplate;
	}


	public void setTransTemplate(TransactionTemplate transTemplate) {
		this.transTemplate = transTemplate;
	}

	public DoAction getFormControllers() {
		return formControllers;
	}

	public void setFormControllers(DoAction formControllers) {
		this.formControllers = formControllers;
	}

	

}

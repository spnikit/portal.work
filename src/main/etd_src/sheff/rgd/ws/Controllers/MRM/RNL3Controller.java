package sheff.rgd.ws.Controllers.MRM;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.sql.DataSource;

import org.apache.log4j.Logger;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import ru.aisa.rgd.etd.dao.ETDSyncServiceFacade;
import ru.aisa.rgd.etd.objects.ETDDocumentData;
import ru.aisa.rgd.ws.documents.ETDForm;
import ru.aisa.rgd.ws.exeption.InternalException;
import ru.aisa.rgd.ws.utility.DataBinder;
import ru.aisa.rgd.ws.utility.RowHandler;
import ru.aisa.rgd.ws.utility.TypeConverter;
import sheff.rgd.ws.Abstract.AbstractAction;
import sheff.rjd.services.syncutils.SyncObj;
import sheff.rjd.ws.OCO.AfterSign.SendToEtd;




public class RNL3Controller extends AbstractAction{
	
	private String parentform;
	private String parentformrnl2;
	private String user_role1;
	private String user_role2;
	private NamedParameterJdbcTemplate npjt;
	private DataSource ds;
	private ETDSyncServiceFacade etdsyncfacade;
	private SendToEtd sendtoetd;
	protected final Logger	log	= Logger.getLogger(getClass());
	
	public NamedParameterJdbcTemplate getNpjt() {
		return npjt;
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
	
	public String getParentformrnl2() {
		return parentformrnl2;
	}
	public void setParentformrnl2(String parentformrnl2) {
		this.parentformrnl2 = parentformrnl2;
	}
	
	public String getUser_role1() {
		return user_role1;
	}
	public void setUser_role1(String user_role1) {
		this.user_role1 = user_role1;
	}
	public String getUser_role2() {
		return user_role2;
	}
	public void setUser_role2(String user_role2) {
		this.user_role2 = user_role2;
	}
	public DataSource getDs() {
		return ds;
	}

	public void setDs(DataSource ds) {
		this.ds = ds;
	}
	
	
	
	public ETDSyncServiceFacade getEtdsyncfacade() {
		return etdsyncfacade;
	}
	public void setEtdsyncfacade(ETDSyncServiceFacade etdsyncfacade) {
		this.etdsyncfacade = etdsyncfacade;
	}
	
	public SendToEtd getSendtoetd() {
		return sendtoetd;
	}
	public void setSendtoetd(SendToEtd sendtoetd) {
		this.sendtoetd = sendtoetd;
	}
	public void doAfterSign(String docName, String docdata, int predid, int signNumber,  long id, String certserial,int WrkId) {
		
		if(signNumber==1){
			try {
				
				HashMap<String, Object> pp = new HashMap<String, Object>();
				pp.put("docid", id);
				pp.put("formname", parentform);
			
				//Разбираем РНЛ 3
				byte[] rnl3blob = npjt.queryForObject("select bldoc from snt.docstore where id =:docid", pp, byte[].class);
				
				ETDForm formrnl3 = ETDForm.createFromArchive(rnl3blob);
				  DataBinder sourcebinder = formrnl3.getBinder();
				  Table bt = new Table();
				  sourcebinder.handleTable("table2", "row2", new RowHandler<Table>() {
						public void handleRow(DataBinder sourcebinder, int rowNum, Table bt)
								throws InternalException {
							TableEntry t = new TableEntry();
							t.P_6_7= sourcebinder.getValue("P_6_7");
							t.P_6_9_1= sourcebinder.getValue("P_6_9_1");
							t.P_6_9_2= sourcebinder.getValue("P_6_9_2");
							bt.rowList.add(t);
						}

					}, bt);
				    
				
				//Достаем РНЛ 2
				pp.put("formnamernl2", parentformrnl2);
				
				
				long rnl2id= npjt.queryForLong("select id from snt.docstore where id_pak = (select id_pak from snt.docstore where id =:docid) "
						+ "and typeid = (select id from snt.doctype where name = :formnamernl2) ", pp);
				
				pp.put("rnl2id", rnl2id);
				
				byte[] rnl2blob = npjt.queryForObject("select bldoc from snt.docstore where id =:rnl2id", pp, byte[].class);
				
				ETDForm form = ETDForm.createFromArchive(rnl2blob);
				  DataBinder binder = form.getBinder();
				 
				  //Заполняем РНЛ 2 в зависимости от того, кто подписал РНЛ 3
				  
				  if (sourcebinder.getValue("user_role").equals(user_role1)){
				
					                       						
					                       						
					                       						binder.handleTable("table2", "row2", new RowHandler<Table>() {
					                       							public void handleRow(DataBinder binder, int rowNum, Table bt)
					                       									throws InternalException {
					                       								binder.setNodeValue("P_6_7", bt.rowList.get(rowNum).P_6_7);
					                       							}

					                       						}, bt);             						
					                       						
					                       						
				  }
				  
				  
				  
				  
				  
				  if (sourcebinder.getValue("user_role").equals(user_role2)){

					               								binder.handleTable("table3", "row3", new RowHandler<Table>() {
					                       							public void handleRow(DataBinder binder, int rowNum, Table bt)
					                       									throws InternalException {
					                       								binder.setNodeValue("P_6_9_1", bt.rowList.get(rowNum).P_6_9_1);
					                       								binder.setNodeValue("P_6_9_2", bt.rowList.get(rowNum).P_6_9_2);
					                       							}

					                       						}, bt);                      								
					               								
					               								
				  }
				  
				  pp.put("bldoc", form.encodeToArchiv());
				  pp.put("docdata", form.transform("data"));
				  
				  npjt.update("update snt.docstore set bldoc=:bldoc, docdata =:docdata where id =:rnl2id ", pp);
				  
				  
				  
				  //Определяем количество неподписанных перечней РНЛ 3
				  int countpv= npjt.queryForInt("select count(id) from snt.docstore where "
							+ "id_pak = (select id_pak from snt.docstore where id =:docid) "
							+ "and typeid = (select id from snt.doctype where name = :formname) and signlvl is not null", pp);
					
				  
				  
				if (countpv==0){
					
					
					npjt.update("update snt.docstore set visible = 1 where id =:rnl2id", pp);
				}
				
				
				
				
				
				sendtoetd.SendToEtdMessage(id, docdata, parentform, signNumber, 0, false);
				
			} catch (Exception e) {
		    	log.error(TypeConverter.exceptionToString(e));
		    	e.printStackTrace();
		
		    } 
		}
		
	}


   public void doAfterSave(String docName, String docdata, int predid, int signNumber, String CertID, long id,int WrkId) throws Exception{
		
	}
   
   public void doAfterSync(String formname, SyncObj syncobj, String sql, int signum){
	   int visible = -1;
	   
	   
	   try{
	   ETDForm form = ETDForm.createFromArchive(syncobj.getBldoc());
		  DataBinder binder = form.getBinder();
		  		  
		  syncobj.setId_pak(binder.getValue("IdRnl"));
		  visible = Integer.parseInt(binder.getValue("priznak"))-1;
		 
		  if (binder.getValue("P_8a").length()>0||binder.getValue("P_8b").length()>0||binder.getValue("P_8v").length()>0){
			  syncobj.setPredcreator(syncobj.getPredid());
			 syncobj.setPredid(etdsyncfacade.getPredMaker("ОАО «РЖД»"));
		  }
		  
		  
	   } catch (Exception e){
		   log.error(TypeConverter.exceptionToString(e));
	   }
	   
	   
	   etdsyncfacade.insertDocstore(sql, etdsyncfacade.getWorkerWithorderNull(syncobj));
	   if (visible==0){
		   HashMap<String , Object> pp = new HashMap<String, Object>();
		   pp.put("docid", syncobj.getDocid());
		   npjt.update("update snt.docstore set visible = 0 where id =:docid", pp);
	   }
   }
   private class Table
	{
		public List<TableEntry> rowList = new ArrayList<TableEntry>();

	}
	private class TableEntry {
		
		public String P_6_7;
		public String P_6_9_1;
		public String P_6_9_2;

		@Override
		public String toString() {
			return "TableEntry [P_6_7=" + P_6_7 + ",P_6_9_1=" + P_6_9_1 + ",P_6_9_2=" + P_6_9_2 + "]";
		}
}
	public ETDDocumentData EditBeforeOpen(String docName, ETDDocumentData doc) {
		// TODO Auto-generated method stub
		return doc;
	}
}
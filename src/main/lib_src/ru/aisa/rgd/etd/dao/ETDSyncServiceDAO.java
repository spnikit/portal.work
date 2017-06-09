package ru.aisa.rgd.etd.dao;


import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.log4j.Logger;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcDaoSupport;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.ParameterizedRowMapper;

import ru.aisa.rgd.etd.objects.ETDDocument;
import ru.aisa.rgd.ws.domain.Persona;
import sheff.rjd.services.syncutils.SyncObj;
import sheff.rjd.services.syncutils.VchdeObj;
import sheff.rjd.services.transoil.TransDocForSendObj;

public class ETDSyncServiceDAO extends NamedParameterJdbcDaoSupport{
    //private NamedParameterJdbcTemplate npjt;
    
    protected final Logger	log	= Logger.getLogger(ETDSyncServiceDAO.class);

    class TransDocMapper implements ParameterizedRowMapper<TransDocForSendObj>
	{
		public TransDocForSendObj mapRow(ResultSet rs, int n) throws SQLException 
		{
			TransDocForSendObj doc = new TransDocForSendObj();
			doc.setEtdid(rs.getLong("ETDID"));
			doc.setXml(rs.getString("DOCDATA"));
			try {
				doc.setBlob(new String(rs.getBytes("BLDOC"), "UTF-8"));
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			doc.setForname(rs.getString("FORMNAME"));
			return doc;
		}

	}
    
	public ETDSyncServiceDAO(DataSource ds)
	{
		super();
		setDataSource(ds);
	}
//	private String sql;
	
	 
	
	static private final String sqlwrkordernull = "select order, wrkid from snt.doctypeflow where dtid = :TYPEID and " +
	"order = (select min(order) from snt.doctypeflow where  dtid = :TYPEID and parent is not null)";
	
	static private final String sqlwrkorder = "select order, wrkid, parent from snt.doctypeflow where dtid = :TYPEID and " +
	"order = (select min(order) from snt.doctypeflow where  dtid = :TYPEID and parent =:order)";
	
	static private final String sqlcabinetid = "select cabinet_id from snt.pred where id =:PREDID fetch first row only";
	
	static private final String sqlinnkpp = "select id from snt.pred where inn = :inn and kpp = :kpp and headid is null fetch first row only";
	
	static private final String sqlinnkppmark = "select id from snt.pred p where inn = :inn and kpp = :kpp and p.id in (select pid from snt.pred_fil where kid = :mark) fetch first row only";
	
	static private final String sqlinnkppmarkcount = "select count(id) from snt.pred p where inn = :inn and kpp = :kpp " +
			"and p.id in (select pid from snt.pred_fil where kid = :mark)";
	
	static private final String sqlokpo = "select id from snt.pred where okpo_kod = :okpo and headid is null ";
	
	static private final String sqlokpomark = "select id from snt.pred p where okpo_kod = :okpo and p.id in (select pid from snt.pred_fil where kid = :mark) fetch first row only";
	
	static private final String sqlokpomarkcount = "select count(id) from snt.pred p where okpo_kod = :okpo " +
			"and p.id in (select pid from snt.pred_fil where kid = :mark)";
	
	
	static private final String sqlnextdocid = "select next value for SNT.vu_seq from SNT.wrkname fetch first row only ";
	
	static private final String sqltypeid = "select id FROM SNT.doctype where name = :DOCTYPE and ptype = 0";
	
	static private final String sqlpredname = "select rtrim(VNAME) NAME from snt.pred where id=:PREDID";
	
	static private final String sqlupdatedsf = " insert into SNT.DocStoreflow (DOCID,ORDER,WRKID,DT,PERSID,PARENT,PREDID) "+
    	" values ( :globalid, :order,:LWRKID, CURRENT TIMESTAMP,NULL,  " +
    	"(select parent from snt.doctypeflow where dtid = :TYPEID and order = :order), :PREDCREATOR)";
	
	
	
	static private final String sqlrealcountfpuact = "select count(*) from snt.docstore where typeid = " +
	  		"(select id from snt.doctype where name like '%ФПУ-26 АСР%') and id_pak = :id_pak and signlvl is null";
	
	static private final String sqlcountfpuact = "select id from snt.docstore where typeid = " +
		    	  		"(select id from snt.doctype where name like '%ФПУ-26 АСР%') and id_pak = :id_pak and signlvl is null";
	
	static private final String sqlupdatefpuact = "update snt.docstore set opisanie = (select opisanie from snt.docstore where id_pak =:id_pak and typeid =" +
		    		  		"(select id from snt.doctype where name like '%Расшифровка%')) where id = :fpuactid";
	
	static private final String sqltypename = "select rtrim(name) name from snt.docgroups where id = "
					    + "(select groupid from snt.doctype where id = :TYPEID)";
	
	static private final String sqlgetidbyetdid = "select id from snt.docstore where etdid =:ETDID";
	
	static private final String sqlrem = "select rtrim(name) name, (select rtrim(name)" +
			" dorname from snt.dor where id = rp.dorid)" +
			" from snt.rem_pred rp where kleim = :kleim fetch first row only";
	
	
	static private final String sqlmarkcount= "select count(0) from snt.rem_pred where kleim =:kleim";
	
	static private final String selectlastpack  ="select id_pak from snt.docstore where id = (select max(id) from snt.docstore ds where vagnum = :vagnum "+
			" and typeid = (select id from snt.doctype where name = :name)) and etdid<>:etdid";
	
	
	static private final String setlastpack  ="update snt.docstore ds set reqnum = " +
			"(select id_pak from snt.docstore where id = (select max(id) from snt.docstore ds where vagnum = :vagnum "+
			" and typeid = (select id from snt.doctype where name = :name) and etdid<>:etdid and repdate =:repdate)) where ds.etdid = :etdid";
	
	static private final String updatelastpack  ="update snt.docstore ds set reqnum = :reqnum where ds.etdid = :etdid";
	
	static private final String fpucredentials  ="select vagnum, id_pak from snt.docstore where etdid = :etdid";
	
	static private final String predmaker = "select id from snt.pred where name = :name_rzd fetch first row only";
	
	static private final String TransDocsql = "select etdid, docdata, bldoc, (select rtrim(name) from snt.doctype where id = ds.typeid) formname from snt.docstore ds where etdid =:etdid";
	
	static private final String Countfortransdoc = "select count(id) from snt.docstore where etdid =:etdid";
	
	public SyncObj getWorkerWithorderNull (SyncObj obj){

	 Map<String, Object> pp = new HashMap<String, Object>();
	 pp.put("TYPEID", obj.getTypeid());
	   
	   List wrkorder = getNamedParameterJdbcTemplate().queryForList(sqlwrkordernull, pp);
	   
	   if (wrkorder.size()>0){
		    Map wrk = (Map) wrkorder.get(0);
		
		 obj.setLwrkid(Integer.parseInt( wrk.get("WRKID").toString()));
	   obj.setOrder(Integer.parseInt(wrk.get("ORDER").toString()));
	  
	   }
	    return obj;
	    
	}
	
	
	public VchdeObj getVchde (int kleim){
		 
		VchdeObj obj = new VchdeObj();
//		sql = sqlrem;
		 Map<String, Object> pp = new HashMap<String, Object>();
		 pp.put("kleim", kleim);
		  try{
		 List vchde = getNamedParameterJdbcTemplate().queryForList(sqlrem, pp);
		 if (vchde.size()>0){
			 Map v= (Map) vchde.get(0);
			 
			 obj.setDi(v.get("DORNAME").toString());
			 obj.setRem_pred(v.get("NAME").toString());
		 }
		 } catch(Exception e){
			 
		 }
		 	 
		 
		    return obj;
		    
		}
	
	public SyncObj getWorkerWithorder(SyncObj obj){
		  
		Map<String, Object> pp = new HashMap<String, Object>();
		pp.put("TYPEID", obj.getTypeid());
		pp.put("order", obj.getOrder());
//		sql = sqlwrkorder;
		    List wrkorder = getNamedParameterJdbcTemplate().queryForList(sqlwrkorder, pp);
		    if (wrkorder.size()>0){
			    Map wrk = (Map) wrkorder.get(0);
			
			 obj.setLwrkid(Integer.parseInt(wrk.get("WRKID").toString()));
		   obj.setOrder(Integer.parseInt(wrk.get("ORDER").toString()));
		   obj.setParent(Integer.parseInt(wrk.get("PARENT").toString()));
		 
		    }
		    
		    return obj;
		    
		}
	public String getCabinetIdByPred(int predid){
		 
		Map<String, Object> pred = new HashMap<String, Object>();
	    pred.put("PREDID", predid);
//	    sql = sqlcabinetid;
		    String cabinetid = (String)getNamedParameterJdbcTemplate().queryForObject(sqlcabinetid, pred, String.class);
	    return cabinetid;
		    
		}
	
	public int getpredIdByINNKPP(String inn, String kpp){
		 
	    Map<String, Object> innkpp = new HashMap<String, Object>();
	    innkpp.put("inn", inn);
	    innkpp.put("kpp", kpp);
//	    sql = sqlinnkpp;
		    int pred = getNamedParameterJdbcTemplate().queryForInt(sqlinnkpp, innkpp);
	    return pred;
		    
		}
	
	public int getpredIdByINNKPPMARK(String inn, String kpp, int mark){
		 
	    Map<String, Object> innkpp = new HashMap<String, Object>();
	    innkpp.put("inn", inn);
	    innkpp.put("kpp", kpp);
	    innkpp.put("mark", mark);
//	    sql = sqlinnkppmark;
		    int pred = getNamedParameterJdbcTemplate().queryForInt(sqlinnkppmark, innkpp);
	    return pred;
		    
		}
	
	public int getCountpredIdByINNKPPMARK(String inn, String kpp, int mark){
		 
	    Map<String, Object> innkpp = new HashMap<String, Object>();
	    innkpp.put("inn", inn);
	    innkpp.put("kpp", kpp);
	    innkpp.put("mark", mark);
//	    sql = sqlinnkppmarkcount;
		    int count = getNamedParameterJdbcTemplate().queryForInt(sqlinnkppmarkcount, innkpp);
	    return count;
		    
		}
	
	public int getpredIdByOKPO(int okpo){
		 
	    Map<String, Object> okpomap = new HashMap<String, Object>();
	    okpomap.put("okpo", okpo);
	    
//	    sql = sqlokpo;
		    int pred = getNamedParameterJdbcTemplate().queryForInt(sqlokpo, okpomap);
	    return pred;
		    
		}
	
	public int getpredIdByOKPOMARK(int okpo, int mark){
		 
	    Map<String, Object> okpomap = new HashMap<String, Object>();
	    okpomap.put("okpo", okpo);
	    okpomap.put("mark", mark);
//	    sql = sqlokpomark;
		    int pred = getNamedParameterJdbcTemplate().queryForInt(sqlokpomark, okpomap);
	    return pred;
		    
		}
	
	public int getCountpredIdByOKPOMARK(int okpo, int mark){
		 
	    Map<String, Object> okpomap = new HashMap<String, Object>();
	    okpomap.put("okpo", okpo);
	    okpomap.put("mark", mark);
//	    sql = sqlokpomarkcount;
		    int count = getNamedParameterJdbcTemplate().queryForInt(sqlokpomarkcount, okpomap);
	    return count;
		    
		}
	
	public int getNextDocid(){
		
//	    sql = sqlnextdocid;
		    int docid = getNamedParameterJdbcTemplate().queryForInt(sqlnextdocid, new HashMap());
	    return docid;
		    
		}
	public int getTypeid(SyncObj obj){
		Map<String, Object> pp = new HashMap<String, Object>();
		pp.put("DOCTYPE", obj.getDoctype());
//	    sql = sqltypeid;
		    int typeid = getNamedParameterJdbcTemplate().queryForInt(sqltypeid, pp);
	    return typeid;
		    
		}
	public String getNamebyPredid(int predid){
		Map<String, Object> pred= new HashMap<String, Object>();
		pred.put("PREDID", predid);
//	    sql = sqlpredname;
		    String name = (String)getNamedParameterJdbcTemplate().queryForObject(sqlpredname, pred, String.class);
	       
		    return name;
		}
	
	public void updateDSF(SyncObj obj){
	  	    Map<String, Object> pp = new HashMap<String, Object>();
	  	 pp.put("globalid", obj.getDocid());
	  	 pp.put("order", obj.getOrder());
	  	 pp.put("LWRKID", obj.getLwrkid());
	  	 pp.put("TYPEID", obj.getTypeid());
	  	 pp.put("PREDCREATOR", obj.getPredcreator());
//	    sql = sqlupdatedsf;
	    getNamedParameterJdbcTemplate().update(sqlupdatedsf, pp);
	
	
	}
	
	public void insertDocstore(String sqlinsert, SyncObj obj){
  	    Map <String ,Object> pp = new HashMap<String, Object>();
  	    pp.put("ETDID", obj.getEtdid());
  	    pp.put("id_pak", obj.getId_pak());
  	    pp.put("BLDOC", obj.getBldoc());
  	    pp.put("DOCDATA", obj.getDocdata());
  	    pp.put("DOCTYPE", obj.getDoctype());
  	    pp.put("TYPEID", obj.getTypeid());
  	    pp.put("PREDID", obj.getPredid());
  	    pp.put("PREDCREATOR", obj.getPredcreator());
  	    pp.put("globalid", obj.getDocid());
  	    pp.put("fpuactid", obj.getFpuactid());
  	    pp.put("vagnum", obj.getVagnum());
  	    pp.put("repdate", obj.getRepdate());
  	    pp.put("SIGNLVL", obj.getSignlvl());
  	    pp.put("dognum", obj.getDogovor());
		pp.put("content", obj.getContent());
		pp.put("LWRKID", obj.getLwrkid());
		pp.put("mark", !(obj.getMark()==-1)?obj.getMark():"null");
		pp.put("visible", obj.getVisible());
		try{
	    getNamedParameterJdbcTemplate().update(sqlinsert, pp);
		} catch (Exception e){
			e.printStackTrace();
		}
	
	}
	
	public long getCountFpuact (SyncObj obj){
		long docid=-1;
		Map<String, Object> pp = new HashMap<String, Object>();
		pp.put("id_pak", obj.getId_pak());
		int count  =getNamedParameterJdbcTemplate().queryForInt(sqlrealcountfpuact, pp);
		if(count > 0){
			docid = getNamedParameterJdbcTemplate().queryForInt(sqlcountfpuact, pp);
		}
		//if (tempId != null) docid = (long) tempId;
		
		return docid;
	}
	
	public void UpdateFpuact(SyncObj obj){
		Map<String, Object> pp = new HashMap<String, Object>();
		pp.put("fpuactid", obj.getFpuactid());
		pp.put("id_pak", obj.getId_pak());
		try{
		getNamedParameterJdbcTemplate().update(sqlupdatefpuact, pp);
		} catch (Exception e){
			e.printStackTrace();
		}
	}
	
	public String getTypeName(int typeid){
		String typename;
		Map<String, Object> pp = new HashMap<String, Object>();
		pp.put("TYPEID", typeid);
		typename = (String) getNamedParameterJdbcTemplate().queryForObject(sqltypename, pp, String.class);
	return typename;
	}
	
	public long getIdByEtdid(long etdid){
		long id = 0;
		Map<String, Object> pp = new HashMap<String, Object>();
		pp.put("ETDID", etdid);
		id = getNamedParameterJdbcTemplate().queryForLong(sqlgetidbyetdid, pp);
		return id;
	}
	
	public int getMarkCount(int mark){
		int count;
		Map<String, Object> pp = new HashMap<String, Object>();
		pp.put("kleim", mark);
		count = getNamedParameterJdbcTemplate().queryForInt(sqlmarkcount, pp);
		return count;
	}
	
	public void setLastPack(String vagnum, long etdid, String repdate){
		HashMap<String, Object> pp = new HashMap<String, Object>();
		pp.put("vagnum", vagnum);
		pp.put("etdid", etdid);
		pp.put("name", "Пакет документов");
		pp.put("repdate", repdate);
		getNamedParameterJdbcTemplate().update(setlastpack, pp);
		
		/*String lastid = (String) getNamedParameterJdbcTemplate().queryForObject(selectlastpack, pp, String.class);
		pp.put("reqnum", lastid);
		getNamedParameterJdbcTemplate().update(updatelastpack, pp);
		
		int declined = getNamedParameterJdbcTemplate().queryForInt("select  case when droptxt is null then 0 else 1 end as drop from snt.docstore where etdid = :reqnum", pp);
		
		if (declined==0){
			pp.put("droptxt", "Документ отклонен автоматически как дубликат");
	    	pp.put("dropid", Integer.parseInt(zz.get("DROPID").toString()));
	    	pp.put("droppredid", Integer.parseInt(zz.get("DROPPREDID").toString()));
	    	pp.put("dropwrkid", Integer.parseInt(zz.get("DROPWRKID").toString()));
	    	pp.put("id_pak", zz.get("ID_PAK").toString());
	    	
	    	try{
	    		getNpjt().update("update snt.docstore set droptxt=:droptxt, dropid=:dropid, " +
	    				"droppredid=:droppredid, dropwrkid=:dropwrkid, droptime = current timestamp where etdid = :id_pak ", pp);
	    	} catch(Exception e){
	    		 StringWriter outError = new StringWriter();
				   PrintWriter errorWriter = new PrintWriter( outError );
				   e.printStackTrace(errorWriter);
				   
				   log.error(outError.toString());
	    	}
			
			
		}*/
		
		
		
		
		/*int count;
		Map pp = new HashMap();
		pp.put("kleim", mark);
		count = getNamedParameterJdbcTemplate().queryForInt(sqlmarkcount, pp);*/
		
	}
	
	public SyncObj getFpuCredentials(long docid, SyncObj syncobj){
		HashMap <String, Object> pp = new HashMap<String, Object>();
		pp.put("etdid", docid);
		List aa  = getNamedParameterJdbcTemplate().queryForList(fpucredentials, pp);
		
		if (aa.size()>0){
		    Map cred = (Map) aa.get(0);
		    
		    if (cred.get("VAGNUM")!=null){
		    syncobj.setVagnum(cred.get("VAGNUM").toString());
		    }
		    else syncobj.setVagnum(null);
		    syncobj.setId_pak(cred.get("ID_PAK").toString());		
		    syncobj.setContent(cred.get("ID_PAK").toString());
	   }
		
		
		return syncobj;
	}
	
	public int getPredMaker(String name){
		HashMap<String, Object> pp = new HashMap<String, Object>();
		pp.put("name_rzd", name);
		
		return getNamedParameterJdbcTemplate()
		.queryForInt(
						predmaker,
						pp);
	}

	
	
	
public TransDocForSendObj getTransDocument(String etdid){
	
	TransDocForSendObj doc = null;
	try{
		doc = (TransDocForSendObj) getNamedParameterJdbcTemplate().queryForObject(TransDocsql, 
				new MapSqlParameterSource("etdid", Long.valueOf(etdid)), new TransDocMapper());
	}catch (Exception e){

		StringWriter outError = new StringWriter();
		PrintWriter errorWriter = new PrintWriter(outError);
		e.printStackTrace(errorWriter);
		logger.error(outError.toString());
		e.printStackTrace();
	
	}
	return doc;
	}

public boolean getCountPortalDoc(String etdid){
	try{
		HashMap<String, Object> pp = new HashMap<String, Object>();
		pp.put("etdid", Long.valueOf(etdid));
		int count  = getNamedParameterJdbcTemplate().queryForInt(Countfortransdoc, pp);
		if (count==0) return false;
		else return true;
		
	}catch (Exception e){
		StringWriter outError = new StringWriter();
		PrintWriter errorWriter = new PrintWriter(outError);
		e.printStackTrace(errorWriter);
		logger.error(outError.toString());
		e.printStackTrace();
		return false;
	}
	
}

}

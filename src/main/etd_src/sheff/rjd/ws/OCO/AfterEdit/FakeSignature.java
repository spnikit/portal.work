package sheff.rjd.ws.OCO.AfterEdit;

import java.sql.Types;
import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;
import javax.xml.crypto.Data;

import org.springframework.jdbc.core.SqlOutParameter;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import sheff.rjd.utils.MyStoredProc;

public class FakeSignature {
	
	private NamedParameterJdbcTemplate npjt;
	private DataSource ds;
	private static final String updtblob = "update snt.docstore set bldoc = :bldoc, docdata = :docdata where id =:id";
	
	
	public NamedParameterJdbcTemplate getNpjt() {
		return npjt;
	}



	public void setNpjt(NamedParameterJdbcTemplate npjt) {
		this.npjt = npjt;
	}



	public DataSource getDs() {
		return ds;
	}



	public void setDs(DataSource ds) {
		this.ds = ds;
	}



	public void fakesign(long docid, int userid, int wrkid, int predid, byte[] blob, String docdata){
		HashMap<String, Object> pp = new HashMap<String, Object>();
		pp.put("id", docid);
		pp.put("bldoc", blob);
		pp.put("docdata", docdata);
		try{
		getNpjt().update(updtblob, pp);
		} catch (Exception e){
		e.printStackTrace();
		}
		callSignProcedure(docid, userid, wrkid, predid, new byte[1]);
		
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
}

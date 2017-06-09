package sheff.rjd.ws.OCO.AfterSave;

import java.math.BigInteger;
import java.sql.Types;
import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.jdbc.core.SqlOutParameter;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import sheff.rjd.utils.MyStoredProc;

public class FakeSignProcedure {

    private NamedParameterJdbcTemplate npjt;
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

    private DataSource ds;
    
    
    
    public void FakeSign(String docName, String docdata, int predid,
	    int signNumber, String CertID, long id, int WrkId){
	
	 Map pp = new HashMap();
	    pp.put("docid", id);
	    pp.put("docname", docName);
	    pp.put("docdata", docdata);
	    pp.put("predid", predid);
	    pp.put("signnumber", signNumber);
	    pp.put("CertSerial", new BigInteger(CertID, 16).toString());
	    pp.put("wrkid", WrkId);
	    
	    int userid = npjt.queryForInt("select id from snt.personall where certserial = :CertSerial", pp);
	    
	    
	    callSignProcedure(id, userid, WrkId, predid, new byte[1]);
	
	
	
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

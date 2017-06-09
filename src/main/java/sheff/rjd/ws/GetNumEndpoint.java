package sheff.rjd.ws;

import java.io.PrintWriter;
import java.io.StringWriter;
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
import org.springframework.ws.server.endpoint.AbstractMarshallingPayloadEndpoint;

import ru.aisa.edt.GetNumDocument;
import ru.aisa.rgd.etd.extend.SNMPSender;
import sheff.rjd.utils.MyStoredProc;
import sheff.rjd.utils.SecurityManager;

public class GetNumEndpoint extends AbstractMarshallingPayloadEndpoint {
	private NamedParameterJdbcTemplate npjt;
    private SecurityManager securityManager;
    private DataSource ds; 
    private static Logger	log	= Logger.getLogger(GetNumEndpoint.class);
    
	public DataSource getDs() {
		return ds;
	}


	public void setDs(DataSource ds) {
		this.ds = ds;
	}


	public SecurityManager getSecurityManager() {
		return securityManager;
	}


	public void setSecurityManager(SecurityManager securityManager) {
		this.securityManager = securityManager;
	}


	public GetNumEndpoint(Marshaller marshaller) {
		super(marshaller);
	}
	
	
	


	protected Object invokeInternal(Object obj)  {
		
		GetNumDocument gn = (GetNumDocument) obj;
		try{
		MyStoredProc sproc = new MyStoredProc(getDs());
		if (gn.getGetNum().getPeriod()==1)
			sproc.setSql("letd.GetDoc_M_Num");
		else sproc.setSql("letd.GetDoc_Y_Num");
		sproc.declareParameter(new SqlOutParameter("out", Types.CHAR));
		sproc.declareParameter(new SqlParameter("predid", Types.INTEGER));
		sproc.declareParameter(new SqlParameter("formid", Types.INTEGER));
		sproc.declareParameter(new SqlOutParameter("DocNum", Types.INTEGER));
		sproc.compile();
		Map<String, Integer> input = new HashMap<String, Integer>();
		input.put("predid", gn.getGetNum().getPredid());
//		System.out.println("PREDID+++++++ "+gn.getGetNum().getPredid());
		Map<String, String> pp = new HashMap<String, String>();
		pp.put("DOCTYPE", gn.getGetNum().getDoctype());
//		System.out.println("DOCTYPE++++++++ "+gn.getGetNum().getDoctype());
		input.put("formid", getNpjt().queryForInt(" select id from letd.doctype where name = :DOCTYPE ", pp));
		Map output = sproc.execute(input);
		String formnumber = output.get("DocNum").toString();
		gn.getGetNum().setDoctype(formnumber);
		return gn;
		}
		catch (Exception e) {
			StringWriter outError = new StringWriter();
			   PrintWriter errorWriter = new PrintWriter( outError );
			   e.printStackTrace(errorWriter);
			   log.error(outError.toString());
			   SNMPSender.sendMessage(e);
			gn.getGetNum().setDoctype("");
			return gn;
		}
		
	}
	public NamedParameterJdbcTemplate getNpjt() {
		return npjt;
	}
	public void setNpjt(NamedParameterJdbcTemplate npjt) {
		this.npjt = npjt;
	}

}

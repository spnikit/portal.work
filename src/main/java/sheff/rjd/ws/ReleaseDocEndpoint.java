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
import org.springframework.ws.server.endpoint.AbstractMarshallingPayloadEndpoint;

import ru.aisa.edt.ReleaseDocDocument;
import ru.aisa.rgd.etd.extend.SNMPSender;
import sheff.rjd.utils.MyStoredProc;


public class ReleaseDocEndpoint extends AbstractMarshallingPayloadEndpoint {
	private NamedParameterJdbcTemplate npjt;  
	private DataSource ds;
    private static Logger	log	= Logger.getLogger(ReleaseDocEndpoint.class);
	

	public DataSource getDs() {
		return ds;
	}

	public void setDs(DataSource ds) {
		this.ds = ds;
	}

	public ReleaseDocEndpoint(Marshaller marshaller) {
		super(marshaller);
	}

	protected Object invokeInternal(Object obj) {
		ReleaseDocDocument rdr = (ReleaseDocDocument)obj;
		try {	
		HashMap<String,Comparable> pp = new HashMap<String,Comparable>();
		pp.put("DOCID", rdr.getReleaseDoc().getId());
		pp.put("CERTSERIAL", new BigInteger(rdr.getReleaseDoc().getCertserial(),16).toString());
		
		MyStoredProc sproc = new MyStoredProc(getDs());
		sproc.setSql("letd.UnLockDoc");
		sproc.declareParameter(new SqlOutParameter("out", Types.CHAR));
		sproc.declareParameter(new SqlParameter("docid", Types.BIGINT));
		sproc.declareParameter(new SqlParameter("userid", Types.INTEGER));
		sproc.declareParameter(new SqlOutParameter("lockid", Types.INTEGER));
		sproc.declareParameter(new SqlOutParameter("lockname", Types.CHAR));
		sproc.compile();
		HashMap<String,Comparable> input = new HashMap<String,Comparable>();
		input.put("docid", rdr.getReleaseDoc().getId());
		input.put("userid", getNpjt().queryForInt("select id from letd.personall where CERTSERIAL = :CERTSERIAL ", pp));
		Map output = sproc.execute(input);	
							
		rdr.getReleaseDoc().setResp("ok");					
		return rdr;
		}
		catch (Exception e) {
			StringWriter outError = new StringWriter();
			   PrintWriter errorWriter = new PrintWriter( outError );
			   e.printStackTrace(errorWriter);
			   log.error(outError.toString());
			   SNMPSender.sendMessage(e);
			rdr.getReleaseDoc().setResp("error");					
			return rdr;
			
		}
	}


	public NamedParameterJdbcTemplate getNpjt() {
		return npjt;
	}


	public void setNpjt(NamedParameterJdbcTemplate npjt) {
		this.npjt = npjt;
	}

}

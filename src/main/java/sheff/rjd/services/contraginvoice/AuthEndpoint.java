package sheff.rjd.services.contraginvoice;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.bouncycastle.util.encoders.Hex;

import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import org.springframework.oxm.Marshaller;
import org.springframework.ws.server.endpoint.AbstractMarshallingPayloadEndpoint;

import sheff.rjd.utils.SecurityManager;



public class AuthEndpoint extends
	AbstractMarshallingPayloadEndpoint {

    private static Log log = LogFactory
	    .getLog(AuthEndpoint.class);

  
	public SecurityManager getSecuritymanager() {
		return securitymanager;
	}

	public void setSecuritymanager(SecurityManager securitymanager) {
		this.securitymanager = securitymanager;
	}


	private SecurityManager securitymanager;
    
    
    private NamedParameterJdbcTemplate npjt;
  

    public NamedParameterJdbcTemplate getNpjt() {
	return npjt;
    }

    public void setNpjt(NamedParameterJdbcTemplate npjt) {
	this.npjt = npjt;
    }

    public AuthEndpoint(Marshaller marshaller) {
	super(marshaller);
    }

	@Override
	protected Object invokeInternal(Object arg0) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

   


}

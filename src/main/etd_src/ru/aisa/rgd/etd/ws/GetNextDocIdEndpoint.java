package ru.aisa.rgd.etd.ws;


import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.HashMap;

import javax.sql.DataSource;

import org.apache.log4j.Logger;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.oxm.Marshaller;
import org.springframework.ws.server.endpoint.AbstractMarshallingPayloadEndpoint;

import ru.aisa.edt.NextDocIdResponseDocument;
import ru.aisa.edt.SaveRequestDocument;
import sheff.rjd.utils.SecurityManager;

public class GetNextDocIdEndpoint extends AbstractMarshallingPayloadEndpoint {
	private NamedParameterJdbcTemplate npjt;
    private SecurityManager securityManager;
    private DataSource ds;
    private static Logger	log	= Logger.getLogger(GetNextDocIdEndpoint.class);

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

	public GetNextDocIdEndpoint(Marshaller marshaller) {
		super(marshaller);
	}

	protected Object invokeInternal(Object obj)  {
		SaveRequestDocument srd = (SaveRequestDocument )obj;
		final NextDocIdResponseDocument doc = NextDocIdResponseDocument.Factory.newInstance();

		try{
	
				
				//byte[] signature = srd.getSaveRequest().getSecurity().getSign();
				//String certid = srd.getSaveRequest().getSecurity().getCertid();
				//String username = srd.getSaveRequest().getSecurity().getUsername();
				//if (getSecurityManager().checkSignature(username, signature, certid)) 
				{
					
		        int id = getNpjt().queryForInt("select next value for SNT.vu_seq from SNT.wrkname fetch first row only ", new HashMap());
		        doc.addNewNextDocIdResponse().setId(id);
		        doc.getNextDocIdResponse().setResponse("ok");
		     
		        return doc;
				}
				
			
			
		}
		catch (Exception e) {
			 StringWriter outError = new StringWriter();
			   PrintWriter errorWriter = new PrintWriter( outError );
			   e.printStackTrace(errorWriter);
			   log.error(outError.toString());
			doc.addNewNextDocIdResponse().setResponse("error");
			return doc;
			 
		}

	}


	public NamedParameterJdbcTemplate getNpjt() {
		return npjt;
	}


	public void setNpjt(NamedParameterJdbcTemplate npjt) {
		this.npjt = npjt;
	}

}

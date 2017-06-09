package com.aisa.HttpClient;

import java.io.PrintWriter;
import java.io.StringWriter;

import javax.xml.ws.soap.Addressing;
import javax.xml.ws.soap.AddressingFeature;

import org.apache.commons.httpclient.Credentials;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.MultiThreadedHttpConnectionManager;
import org.apache.commons.httpclient.UsernamePasswordCredentials;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.auth.AuthScope;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.log4j.Logger;

public class AisaHttpClientTransoil extends  HttpClient{
	
	private static Logger log = Logger.getLogger(AisaHttpClientTransoil.class);
    private AisaRetryHandler httpRetryHandler ;
    private int httptimeout;
    public void setHttptimeout (int httptimeout){
    	this.getHttpConnectionManager().getParams().setConnectionTimeout(httptimeout);
    	this.httptimeout=httptimeout;
    }
    public int getHttptimeout(){
    	return this.httptimeout;
    }
  
    public AisaHttpClientTransoil(MultiThreadedHttpConnectionManager cm) {
    super(cm);
    
    Credentials defaultcreds = new UsernamePasswordCredentials("RZD_WEB",
			"trans@il");
	this.getState().setCredentials(
			new AuthScope("sapws.transoil.su", 8040, AuthScope.ANY_REALM),
			defaultcreds);
	
    }
    public void setHttpRetryHandler (AisaRetryHandler httpRetryHandler){
    	this.httpRetryHandler=httpRetryHandler;
    }
    public AisaRetryHandler getHttpRetryHandler (){
    	return this.httpRetryHandler;
    }
  
	public int executeMethod (HttpMethod method){

		try {
			this.getHttpConnectionManager().getParams().setDefaultMaxConnectionsPerHost(6);
			this.getHttpConnectionManager().getParams().setMaxTotalConnections(100);
		
		    method.getParams().setParameter( HttpMethodParams.RETRY_HANDLER, httpRetryHandler );
		   
			return	this.executeMethod(null ,method,null);
		} 
		catch (Exception e) {
			 StringWriter outError = new StringWriter();
				PrintWriter errorWriter = new PrintWriter( outError );
				e.printStackTrace(errorWriter);
				log.error(outError.toString());
			return-1;
			
		}

		
	}

	
}

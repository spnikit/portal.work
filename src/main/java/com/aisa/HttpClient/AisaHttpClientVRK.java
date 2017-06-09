package com.aisa.HttpClient;

import java.io.PrintWriter;
import java.io.StringWriter;

import org.apache.commons.httpclient.Credentials;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.MultiThreadedHttpConnectionManager;
import org.apache.commons.httpclient.UsernamePasswordCredentials;
import org.apache.commons.httpclient.auth.AuthScope;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.apache.log4j.Logger;

public class AisaHttpClientVRK extends  HttpClient{
	
	private static Logger log = Logger.getLogger(AisaHttpClientVRK.class);
    private AisaRetryHandler httpRetryHandler ;
    private int httptimeout;
    public void setHttptimeout (int httptimeout){
    	this.getHttpConnectionManager().getParams().setConnectionTimeout(httptimeout);
    	this.httptimeout=httptimeout;
    }
    public int getHttptimeout(){
    	return this.httptimeout;
    }
  
   
	public AisaHttpClientVRK(MultiThreadedHttpConnectionManager cm) {
    super(cm);
    
    Credentials defaultcreds = new UsernamePasswordCredentials("vrk1_warecs@1vrk.rzd",
    		"WaReCS");
	this.getState().setCredentials(
			new AuthScope("warecs.ru", AuthScope.ANY_PORT, AuthScope.ANY_REALM),
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
			method.getParams().setParameter( HttpMethodParams.RETRY_HANDLER, httpRetryHandler );
			method.setDoAuthentication(true);
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

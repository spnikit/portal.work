package com.aisa.HttpClient;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.HttpMethodRetryHandler;
import org.apache.commons.httpclient.URIException;
import org.apache.commons.logging.Log;
import org.apache.log4j.Logger;

public class  AisaRetryHandler  implements HttpMethodRetryHandler{ 
	private static Logger log = Logger.getLogger(AisaRetryHandler.class);
	
	private int countInt;
	public void setCountInt(int countInt){
		this.countInt=countInt;
	}
	public int getCount(){
		return this.countInt;
	}
    

	public boolean retryMethod(HttpMethod arg0, IOException arg1, int arg2) {
		
		if (arg2 >= countInt) {
	
				            try {
								log.error(" More than "+countInt+ " tryes to send data to "+arg0.getURI());
							} catch (URIException e) {
								StringWriter outError = new StringWriter();
								PrintWriter errorWriter = new PrintWriter( outError );
								e.printStackTrace(errorWriter);
								log.error(outError.toString());
								return false;
								
							}
	    	 	            return false;
	        }
		return true;
	}

};

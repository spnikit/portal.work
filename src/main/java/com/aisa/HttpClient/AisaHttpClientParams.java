package com.aisa.HttpClient;

import org.apache.commons.httpclient.params.HttpClientParams;

public class AisaHttpClientParams extends HttpClientParams {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int httptimeout;
	public void setHttptimeout(int httptimeout){
		this.setParameter("http.connection.timeout", new Integer(httptimeout));
		this.httptimeout=httptimeout;
	}
	public int getHttptimeout(){
		return this.httptimeout;
	}

}

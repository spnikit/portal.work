package com.aisa.crypto.ocsp;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.ByteArrayRequestEntity;
import org.apache.commons.httpclient.methods.PostMethod;

public class OCSPoverHTTP {

private String OCSPserverURL;
private int timeout;





public int getTimeout() {
	return timeout;
}

public void setTimeout(int timeout) {
	this.timeout = timeout;
}

public String getOCSPserverURL() {
	return OCSPserverURL;
}

public void setOCSPserverURL(String pserverURL) {
	OCSPserverURL = pserverURL;
}

public OCSPoverHTTP() {
	}

public OCSPoverHTTP(String OCSPserverURL, int timeout) {
	this.OCSPserverURL = OCSPserverURL;
	this.timeout = timeout;
}

public byte[] getResp(OCSPReq ocspr) throws OCSPException {
	return getResp(ocspr.getReq());
}

public byte[] getResp(byte[] req) throws OCSPException {
	
	HttpClient client = new HttpClient();
	client.getParams().setParameter("http.socket.timeout", timeout);
	client.getParams().setParameter("http.connection.timeout", timeout);
	client.getParams().setParameter("http.useragent", "ais@ ocsp client");
	final PostMethod method = new PostMethod(OCSPserverURL);
	method.addRequestHeader("Content-Type", "application/ocsp-request");
	method.addRequestHeader("Accept", "application/ocsp-response");
	final ByteArrayRequestEntity ent = new ByteArrayRequestEntity(req);
	method.setRequestEntity(ent);
	try {
		final int returnCode = client.executeMethod(method);
		if (returnCode == HttpStatus.SC_NOT_IMPLEMENTED) {
			method.getResponseBodyAsString();
		} else {
			return method.getResponseBody();
		}
	} catch (final Exception e) {
		final OCSPException ex = new OCSPException();
		ex.initCause(e);
		throw ex;
	} finally {
		method.releaseConnection();
		
	}
	return null;
}
}
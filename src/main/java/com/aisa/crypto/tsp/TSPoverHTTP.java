package com.aisa.crypto.tsp;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.ByteArrayRequestEntity;
import org.apache.commons.httpclient.methods.PostMethod;

/**
 * created 23.10.2009 15:54:00 by @author kunina
 * last modified $Date: 2009-10-27 16:46:22 +0300 (Вт, 27 окт 2009) $ by
 * $Author: kunina $
 */
public class TSPoverHTTP {

private final String TSPserverURL;
private final String timeout;

public TSPoverHTTP(String TSPserverURL,String timeout) {
	this.TSPserverURL = TSPserverURL;
	this.timeout = timeout;
}

public byte[] getResp(TSPRequest tspr) throws TSPException {
	final byte[] req = tspr.getReq();
	HttpClient client = new HttpClient();
	client.getParams().setParameter("http.socket.timeout", Integer.valueOf(timeout));
	client.getParams().setParameter("http.connection.timeout", Integer.valueOf(timeout));
	client.getParams().setParameter("http.useragent", "ais@ tsp client");
	final PostMethod method = new PostMethod(TSPserverURL);
	method.addRequestHeader("Content-Type", "application/timestamp-query");
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
		final TSPException ex = new TSPException();
		ex.initCause(e);
		throw ex;
	} finally {
		method.releaseConnection();
	}
	return null;
}
}
package ru.aisa.etdportal.controllers;

import java.util.ArrayList;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;

import pfr.NewHttpSender;



public class RestTest extends AbstractPortalSimpleController{

	public RestTest() throws JSONException {
		super();
		
	}

	private NewHttpSender httpsender;
	
	public NewHttpSender getHttpsender() {
		return httpsender;
	}

	public void setHttpsender(NewHttpSender httpsender) {
		this.httpsender = httpsender;

	}

	@Override
	public JSONArray get(HttpServletRequest request) {
		String GetOrdersURL =  "http://10.0.0.46:48080/IIT/portal/forms/count";
		ArrayList<NameValuePair> params = new ArrayList<NameValuePair>(); 
		params.add(new BasicNameValuePair("docid", "10001"));
		
		return httpsender.Sendhttprequest(GetOrdersURL, params);
	}

	
}

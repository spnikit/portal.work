package ru.aisa.etdportal.controllers;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;



public class getServDateTime extends AbstractPortalSimpleController {

	public getServDateTime() throws JSONException {
		super();
	}

	
	@Override
	public JSONArray get(HttpServletRequest request){
		
		JSONArray response = new JSONArray();
//		HashMap<String, Object> pp = new HashMap<String, Object>();
		
		DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
		Date date = new Date();
		JSONObject one = new JSONObject();
		try {
			one.put(success, true);
			one.put("date", dateFormat.format(date).toString());
			response.put(one);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			
			e.printStackTrace();
		}
		
		
		return response;
		
	}
	
	
}
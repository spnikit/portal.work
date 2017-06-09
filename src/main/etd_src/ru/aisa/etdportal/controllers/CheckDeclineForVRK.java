package ru.aisa.etdportal.controllers;

import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class CheckDeclineForVRK extends AbstractPortalSimpleController {

	public CheckDeclineForVRK() throws JSONException {
		super();
	}

	@Override
	public JSONArray get(HttpServletRequest request){
		final JSONArray response = new JSONArray();
		if (request.getParameter("id") != null){
			try{
				HashMap<String, Object> pp = new HashMap<String, Object>();
				pp.put("id",Long.parseLong(request.getParameter("id")));
				String select = "select count(0) from snt.marsh where docid = :id";
				int count = getNpjt().queryForInt(select, pp);
				JSONObject js = new JSONObject();
				if(count > 0) {
					js.put("result", false);
				} else {
					js.put("result", true);
				}
				response.put(js);
			} catch (Exception e) {
				JSONObject js = new JSONObject();
				try {
					js.put(success, false);
				} catch (JSONException e1) {
					e1.printStackTrace();
				}
				response.put(js);
			}
		}
	
	
		return response;
	}
	
	
}
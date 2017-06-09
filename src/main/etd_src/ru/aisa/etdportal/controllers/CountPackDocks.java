package ru.aisa.etdportal.controllers;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Enumeration;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;



public class CountPackDocks extends AbstractPortalSimpleController {

	public CountPackDocks() throws JSONException {
		super();
	}

	private static final String sql  = "select count(0) from snt.docstore where etdid in "
			+ "(select etdid from snt.packages where id_pak = (select id_pak from snt.docstore where id =:id))"
			+ " and signlvl is not null";

	@Override
	public JSONArray get(HttpServletRequest request){
		
		JSONArray response = new JSONArray();
		HashMap<String, Object> pp = new HashMap<String, Object>();
		if (request.getParameter("docid")!=null){
			
		pp.put("id", Long.parseLong(request.getParameter("docid")));
		
		int count = getNpjt().queryForInt(sql, pp);
		
		JSONObject one = new JSONObject();
		try {
			one.put(success, true);
			one.put("count", count);
			response.put(one);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			
			e.printStackTrace();
		}
		
		
		}

		return response;
		
	}
	
	
}
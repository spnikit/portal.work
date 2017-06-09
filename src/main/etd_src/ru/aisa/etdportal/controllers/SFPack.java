package ru.aisa.etdportal.controllers;

import java.util.HashMap;
import javax.servlet.http.HttpServletRequest;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class SFPack extends AbstractPortalSimpleController {

	public SFPack() throws JSONException {
		super();
	}

	private static final String sql  = "select id from snt.docstore where etdid = :id_pak";

	@Override
	public JSONArray get(HttpServletRequest request){
		final JSONArray response = new JSONArray();
		HashMap<String, Object> pp = new HashMap<String, Object>();
		if (request.getParameter("idpak")!=null){
			try{
			//	System.out.println(request.getParameter("idpak"));
			pp.put("id_pak",Long.parseLong(request.getParameter("idpak")));
		
		
		long docid= getNpjt().queryForLong(sql, pp);
		JSONObject js = new JSONObject();
		js.put("docid", docid);
		js.put("success", true);
		response.put(js);
		
			} catch (Exception e) {
				e.printStackTrace();
				JSONObject js = new JSONObject();
				try {
					js.put(success, false);
				} catch (JSONException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				response.put(js);
			}
		}
	
	
		return response;
	}
	
	
}
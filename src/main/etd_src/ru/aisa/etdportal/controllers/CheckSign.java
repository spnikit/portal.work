package ru.aisa.etdportal.controllers;

import java.util.HashMap;
import javax.servlet.http.HttpServletRequest;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class CheckSign extends AbstractPortalSimpleController {

	public CheckSign() throws JSONException {
		super();
	}

	private static final String sql  = "select case when signlvl=0 then 0 else 1 end from snt.docstore where id = :id";

	@Override
	public JSONArray get(HttpServletRequest request){
		final JSONArray response = new JSONArray();
		HashMap<String, Object> pp = new HashMap<String, Object>();
		if (request.getParameter("id")!=null){
			try{
			//	System.out.println(request.getParameter("idpak"));
			pp.put("id",Long.parseLong(request.getParameter("id")));
		
		int sign = getNpjt().queryForInt(sql, pp);
				
		
		JSONObject js = new JSONObject();
		js.put("docid", request.getParameter("id"));
		js.put("signed", sign==0?false:true);
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
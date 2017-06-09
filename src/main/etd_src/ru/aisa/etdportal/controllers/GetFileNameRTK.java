package ru.aisa.etdportal.controllers;


import java.util.HashMap;
import javax.servlet.http.HttpServletRequest;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class GetFileNameRTK extends AbstractPortalSimpleController {

	public GetFileNameRTK() throws JSONException {
		super();
	}

	private static final String sql  = "select rem_pred from snt.docstore where id = :id";
	@Override
	public JSONArray get(HttpServletRequest request){
		final JSONArray response = new JSONArray();
		HashMap<String, Object> pp = new HashMap<String, Object>();
		if (request.getParameter("id")!=null){
			try{
//				System.out.println(request.getParameter("id"));
			pp.put("id",Long.parseLong(request.getParameter("id")));
		
		
		String filename= (String)getNpjt().queryForObject(sql, pp, String.class);
//		System.out.println("filename: "+filename);
		
		
		
		JSONObject js = new JSONObject();
		js.put("filename", filename);
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
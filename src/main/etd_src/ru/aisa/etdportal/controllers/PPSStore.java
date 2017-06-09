package ru.aisa.etdportal.controllers;


import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import javax.servlet.http.HttpServletRequest;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.jdbc.core.simple.ParameterizedRowMapper;


public class PPSStore extends AbstractPortalSimpleController {

	public PPSStore() throws JSONException {
		super();
	}

	private static final String sql  = "select distinct(code) code, rtrim (name) name from snt.pps_pred with ur";

	
	@Override
	public JSONArray get(HttpServletRequest request){
		final JSONArray js = new JSONArray();
		HashMap<String, Object> pp = new HashMap<String, Object>();
		if (request.getParameter("predid")!=null){
			try{
//			pp.put("predid",Integer.parseInt(request.getParameter("predid")));
		
			//System.out.println(request.getParameter("predid"));
		getNpjt().query(sql,pp, new ParameterizedRowMapper<Object>() {

			public Object mapRow(ResultSet rs, int numrow) throws SQLException {
								
				JSONObject rr= new JSONObject();
				try {
					rr.put("value", rs.getString("name"));
					rr.put("id", rs.getInt("code"));
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				js.put(rr);
				
				
				return null;
			}});
	
			} catch (Exception e) {
				e.printStackTrace();
				}
		}
//		System.out.println(js);
		return js;
	}
	
	
}
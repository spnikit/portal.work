package ru.aisa.etdportal.controllers;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import javax.servlet.http.HttpServletRequest;
import org.json.JSONArray;
import org.json.JSONException;
import org.springframework.jdbc.core.simple.ParameterizedRowMapper;



public class PredRelatesNames extends AbstractPortalSimpleController {

	public PredRelatesNames() throws JSONException {
		super();
	}

	@Override
	public JSONArray get(HttpServletRequest request) {
		final JSONArray response = new JSONArray();		
		HashMap<String, Integer> pp = new HashMap<String, Integer>();
		String sql;
		if(request.getParameter("predId")!=null&&request.getParameter("typeId")!=null){		
			sql = "select id, rtrim(vname) name from LETD.pred where id in (select relatePredId from LETD.predRelates where predId=:predId and typeid=:typeId) order by vname";
			pp.put("predId", Integer.parseInt(request.getParameter("predId")));
			pp.put("typeId", Integer.parseInt(request.getParameter("typeId")));		
		getNpjt().query(sql,pp, new ParameterizedRowMapper<Object>() {

			public Object mapRow(ResultSet rs, int numrow) throws SQLException {
				
				JSONArray js = new JSONArray();
				js.put(rs.getInt("id"));
				js.put(rs.getString("name"));
				response.put(js);
				System.out.println("js "+js);
				return null;
			}});
		}
		return response;
	}

	
}

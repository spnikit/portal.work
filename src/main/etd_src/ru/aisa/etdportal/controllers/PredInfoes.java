package ru.aisa.etdportal.controllers;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import javax.servlet.http.HttpServletRequest;
import org.json.JSONArray;
import org.json.JSONException;
import org.springframework.jdbc.core.simple.ParameterizedRowMapper;



public class PredInfoes extends AbstractPortalSimpleController {

	public PredInfoes() throws JSONException {
		super();
	}

	@Override
	public JSONArray get(HttpServletRequest request) {
		final JSONArray response = new JSONArray();
		HashMap<String, Integer> pp = new HashMap<String, Integer>();
		String sql;
		if(request.getParameter("predId")!=null/*&&request.getParameter("typeId")!=null*/){		
			sql = "select id, INNSend, INNRec, rtrim(Send) Send, rtrim(Rec) Rec, rtrim(addrSend) addrSend, rtrim(addrRec) addrRec  from LETD.pred where Id=:predId";
			pp.put("predId", Integer.parseInt(request.getParameter("predId")));
			//pp.put("typeId", Integer.parseInt(request.getParameter("typeId")));		
		getNpjt().query(sql,pp, new ParameterizedRowMapper<Object>() {

			public Object mapRow(ResultSet rs, int numrow) throws SQLException {
				
				JSONArray js = new JSONArray();
				js.put(rs.getInt("id"));
				js.put(rs.getInt("INNSend"));
				js.put(rs.getInt("INNRec"));
				js.put(rs.getString("Send"));
				js.put(rs.getString("Rec"));
				js.put(rs.getString("addrSend"));
				js.put(rs.getString("addrRec"));
				response.put(js);
				System.out.println("js "+js);
				return null;
			}});
		}		
		return response;
	}

	
	
}
package ru.aisa.etdadmin.controllers.SimpleImpl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import org.json.JSONArray;
import org.json.JSONException;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.ParameterizedRowMapper;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;

import ru.aisa.etdadmin.Utils;
import ru.aisa.etdadmin.controllers.AbstractSimpleController;

public class WrkNamesOCO extends AbstractSimpleController {

	public WrkNamesOCO() throws JSONException {
		super();
	}

	
	@Override
	public JSONArray get(HttpServletRequest request) {
		final JSONArray response = new JSONArray();
		
		HashMap<String, Integer> pp = new HashMap<String, Integer>();
		String sql = "select id,rtrim(name) as name,rtrim(cast(name as char(100) ccsid " +
				Utils.code + ")) as name_, issm from SNT.wrkname" ;
				//+" where issm in " + idds;
		if(!Utils.isAdmin()) {
			sql = sql + " where dorid = :DORID ";
			pp.put("DORID", Utils.getDorIdForCurrentUser());
		}
	
		
		getNpjt().query(sql + " order by name_",pp, new ParameterizedRowMapper<Object>() {
			
			public Object mapRow(ResultSet rs, int numrow) throws SQLException {
				//System.out.println("here " +numrow);
				JSONArray js = new JSONArray();
				js.put(rs.getInt("id"));
				js.put(rs.getString("name")+" ("+Issm.getISSMName(rs.getInt("issm"))+")");
				response.put(js);
				return null;
				}});

		return response;
	}

}

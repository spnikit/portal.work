package ru.aisa.etdadmin.controllers.SimpleImpl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.springframework.jdbc.core.simple.ParameterizedRowMapper;

import ru.aisa.etdadmin.Utils;
import ru.aisa.etdadmin.controllers.AbstractSimpleController;

public class DocDocType extends AbstractSimpleController{
public DocDocType() throws JSONException {
		super();	
	}

	public JSONArray get(HttpServletRequest request) {
		final JSONArray response = new JSONArray();
		String sql = "select id,rtrim(name) as name from SNT.docgroups order by name ";
		getNpjt().query(sql, new HashMap(), new ParameterizedRowMapper<Object>() {			
			public Object mapRow(ResultSet rs, int numrow) throws SQLException {
				JSONArray js = new JSONArray();
				js.put(rs.getInt("id"));
				js.put(rs.getString("name"));				
				response.put(js);
				return null;
				}});
		return response;
		}	
}

package ru.aisa.etdadmin.controllers.SimpleImpl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.ParameterizedRowMapper;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;

import ru.aisa.etdadmin.Utils;
import ru.aisa.etdadmin.controllers.AbstractSimpleController;

public class Departments extends AbstractSimpleController {

	public Departments() throws JSONException {
		super();
	}

	@Override
	public JSONArray get(HttpServletRequest request) {
		
		final JSONArray response= new JSONArray();
		getNpjt().query("select id,rtrim(name) as name,rtrim(cast(name as char(100) ccsid "+
				Utils.code+
				")) as name_ from SNT.department order by name_"
				,new HashMap(), new ParameterizedRowMapper<Object>() {

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

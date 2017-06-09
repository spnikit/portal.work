package ru.aisa.etdadmin.controllers.SimpleImpl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServlet;
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

public class FormTypes extends AbstractSimpleController {

	public FormTypes() throws JSONException {
		super();
	}

	@Override
	public JSONArray get(HttpServletRequest request) {
		
		final JSONArray response = new JSONArray();
		String tmp = (request.getParameter("all")!=null && request.getParameter("all").equals("true"))? " where name not in ( "+Utils.FORMS_SNT+")":" where ptype=0 and name not in ( "+Utils.FORMS_SNT+")";
		getNpjt().query("select id,rtrim(name) as name_utf," +
				"rtrim(cast(name as char(100) ccsid "+
				Utils.code+
				")) as name from SNT.doctype "+tmp +" order by name",
				new HashMap(),
				new ParameterizedRowMapper<Object>() {
			public Object mapRow(ResultSet rs, int numrow) throws SQLException {
				
				JSONArray js = new JSONArray();
				js.put(rs.getString("id"));
				js.put(rs.getString("name_utf"));
				response.put(js);
				return null;
			}});
		//return null;
		return response;
	}

}

package ru.aisa.etdadmin.controllers.SimpleImpl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.Cookie;
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
import org.springframework.jdbc.core.simple.SimpleJdbcTemplate;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;

import ru.aisa.etdadmin.Utils;
import ru.aisa.etdadmin.controllers.AbstractSimpleController;

public class PredNames extends AbstractSimpleController {

	public PredNames() throws JSONException {
		super();
	}

	@Override
	public JSONArray get(HttpServletRequest request) {
		final JSONArray response = new JSONArray();

		HashMap<String, Integer> pp = new HashMap<String, Integer>();
		String sql;
		if(request.getParameter("dorname")!=null){

			sql = "select id,rtrim(vname) as vname_utf,rtrim( cast(vname as char(100) ccsid "+Utils.code+")) as vname from SNT.pred where type <> 1 and dorid = :dorname order by vname";
			//sql = "select id,rtrim(vname) as vname_utf,rtrim( cast(vname as char(100) ccsid "+Utils.code+")) as vname from SNT.pred where dorid = :dorname";
			pp.put("dorname", Integer.parseInt(request.getParameter("dorname")));
		}else 		
			sql = "select id,rtrim(vname) as vname_utf, rtrim( cast(vname as char(100) ccsid "+Utils.code+") ) as vname from SNT.pred where type <> 1 order by vname ";

		getNpjt().query(sql,pp, new ParameterizedRowMapper<Object>() {

			public Object mapRow(ResultSet rs, int numrow) throws SQLException {

				JSONArray js = new JSONArray();
				js.put(rs.getInt("id"));
				js.put(rs.getString("vname_utf"));
				response.put(js);
				return null;
			}});
System.out.println("sisockho");
		return response;
	}

}

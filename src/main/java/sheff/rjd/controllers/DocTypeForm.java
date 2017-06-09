package sheff.rjd.controllers;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.JSONException;
import org.springframework.jdbc.core.simple.ParameterizedRowMapper;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;

import ru.aisa.etdadmin.controllers.AbstractSimpleController;

public class DocTypeForm extends AbstractSimpleController {

	public DocTypeForm() throws JSONException {
		super();
		//System.out.println("tyt");
	}
	

	
	
	
	
	@Override
	public JSONArray get(HttpServletRequest request) {
//		System.out.println("tyt");
		final JSONArray response = new JSONArray();
		
		
		String sql = "select distinct dt.id, rtrim(dt.name) name from snt.doctype dt, snt.tor_reasons tr  where dt.id = tr.formid";
		
		getNpjt().query(sql,new HashMap(), new ParameterizedRowMapper<Object>() {
			
			public Object mapRow(ResultSet rs, int numrow) throws SQLException {
				JSONArray js = new JSONArray();
				js.put(rs.getInt("id"));
				js.put(rs.getString("name"));
				response.put(js);
				return null;
				}});
		
		
//		System.out.println(response);
		return response;
	}

}

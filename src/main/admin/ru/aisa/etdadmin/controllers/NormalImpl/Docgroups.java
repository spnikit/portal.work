package ru.aisa.etdadmin.controllers.NormalImpl;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
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
import org.springframework.jdbc.core.simple.SimpleJdbcTemplate;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;


import ru.aisa.etdadmin.Utils;
import ru.aisa.etdadmin.controllers.AbstractNormalController;


public class Docgroups extends AbstractNormalController {

	public Docgroups() throws JSONException {
		super();
	}

	@Override
	protected JSONObject delete(HttpServletRequest request)
			throws JSONException {
		if(Utils.isReadonly()) return FORBIDDEN;
		JSONObject response = new JSONObject();
		response.put(success, false);
		
			getSjt()
					.update(
							"delete from SNT.docgroups where id= ?",
							new Object[] { Integer.parseInt(request
									.getParameter("docid")) });
			response.put(success, true);
		
		return response;
	}

	@Override
	protected JSONObject edit(HttpServletRequest request) throws JSONException {
		if(Utils.isReadonly()) return FORBIDDEN;
		JSONObject response = new JSONObject();
		response.put(success, false);			
		
		System.out.println(request.getParameter("docid"));
		
		getSjt().update("update snt.docgroups set name = ? where id = ?", new Object[] {
				request.getParameter("doctypename"), request.getParameter("docid")});
		response.put(success, true);
		return response;
	}

	@Override
	protected JSONObject get(HttpServletRequest request) throws JSONException {
		System.out.println("get");
		final JSONObject response = new JSONObject();
		try {
			response.put("data", new JSONArray());
		

		String sort = request.getParameter("sort");
		if (sort == null
				|| !sort.equalsIgnoreCase("doctypename"))
			sort = "name";
		else if(sort.equalsIgnoreCase("doctypename"))
		{
			sort = "name";
		}
		String dir = request.getParameter("dir");
		if (dir == null || !dir.equalsIgnoreCase("desc"))
			dir = "asc";
		final int start;
		final int limit;
		if (request.getParameter("start") != null)
			start = Integer.parseInt(request.getParameter("start"));
		else
			start = 0;
		if (request.getParameter("limit") != null)
			limit = Integer.parseInt(request.getParameter("limit"));
		else
			limit = 20;
		String sql1;		
		sql1 = "select id from snt.docgroups order by "+ sort + " "+dir;
		
		List result = getNpjt().queryForList(sql1, new HashMap());
		response.put("count", result.size());
		List<Integer> idList = new ArrayList<Integer>();
		for (int i = start; i < Math.min(start + limit, result.size()); i++) {
			idList.add((Integer) ((HashMap) result.get(i)).get("ID"));
		}
		HashMap<String, List> idsMap = new HashMap<String, List>();
		idsMap.put("idList", idList);
		if(idList.size()==0)
			return response;
		getNpjt()
				.query("select id, rtrim(name) as name from snt.docgroups order by "+ sort + " "+dir, idsMap,
						new ParameterizedRowMapper<Object>() {
							public Object mapRow(ResultSet rs, int numrow)
									throws SQLException {
								JSONObject json = new JSONObject();
								try {
									json.put("docid", rs.getInt("ID"));
									json.put("doctypename", rs.getString("name"));									
									response.accumulate("data", json);
								} catch (JSONException e) {
								}
								return null;
							}
						});
		} catch (Exception e1) {
			e1.printStackTrace(System.out);
		}
		return response;
	}

	@Override
	protected JSONObject add(HttpServletRequest request) throws JSONException {
		if(Utils.isReadonly()) return FORBIDDEN;
		JSONObject response = new JSONObject();
		response.put(success, false);
		if (getSjt().queryForInt(
				"select count(0) from SNT.docgroups where name = ?",
				new Object[] { request.getParameter("doctypename") }) != 0) {
			response.put(description, "Такая запись уже существует");
			return response;		}

		getSjt()
				.update(
						"insert into SNT.docgroups (id,name) values(COALESCE((select max(id)+1 from SNT.docgroups),0),?)",
						new Object[] {
								request
										.getParameter("doctypename")});
		response.put(success, true);
		return response;
	}

}

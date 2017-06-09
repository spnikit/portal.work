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
import org.springframework.security.authentication.encoding.Md5PasswordEncoder;

import ru.aisa.etdadmin.Utils;
import ru.aisa.etdadmin.controllers.AbstractNormalController;

public class Admins extends AbstractNormalController {

	public Admins() throws JSONException {
		super();
	}

	private static Md5PasswordEncoder md5 = new Md5PasswordEncoder();

	@Override
	protected JSONObject delete(HttpServletRequest request)
			throws JSONException {
		if(Utils.isReadonly()) return FORBIDDEN;
		JSONObject response = new JSONObject();
		response.put(success, false);
		if (Integer.parseInt(request.getParameter("id")) == Utils
				.getIdForCurrentUser()) {
			response.put(description, "Вы не можете удалить	  себя");
		} else {
			getSjt()
					.update(
							"delete from SNT.doruser where id= ?",
							new Object[] { Integer.parseInt(request
									.getParameter("id")) });
			response.put(success, true);
		}
		return response;
	}

	@Override
	protected JSONObject edit(HttpServletRequest request) throws JSONException {
		if(Utils.isReadonly()) return FORBIDDEN;
		JSONObject response = new JSONObject();
		response.put(success, false);
		
		
		
		
		if (Integer.parseInt(request.getParameter("id")) == Utils
				.getIdForCurrentUser()
				&& !request.getParameter("role").equals(Utils.ROLE_ADMIN)) {
			response.put(description,
					"Вы не можете убрать права администатора у себя");
			return response;
		}
		int dorid = Integer.parseInt(request.getParameter("dorid"));
		try {
			dorid = Integer.parseInt(request.getParameter("dorname"));
		} catch (Exception e) {
		}
		String sql;
		HashMap<String, Comparable> parameterMap = new HashMap<String, Comparable>();
		if (request.getParameter("password").length() > 0) {
			sql = "update	  SNT.doruser set username = :username ,dorid = :dorid,authority =	  :role,password= :password where id = :id";
			parameterMap.put("password", md5.encodePassword(request
					.getParameter("password"), null));
		} else
			sql = "update SNT.doruser set username = :username ,dorid = :dorid	  ,authority=:role where id = :id";
		parameterMap.put("username", request.getParameter("username"));
		parameterMap.put("dorid", dorid);
		parameterMap.put("id", Integer.parseInt(request.getParameter("id")));
		parameterMap.put("role", request.getParameter("role"));
		if (getSjt()
				.queryForInt(
						"select count (0)	  from SNT.doruser where username = ? and id <> ?",
						new Object[] { request.getParameter("username"),
								Integer.parseInt(request.getParameter("id")) }) != 0) {
			response.put(description, "Такой пользователь уже существует");
			return response;
		}
		
		//FIXME remove authantificated status when user change his username!
		getSjt().update(sql, parameterMap);
		response.put(success, true);
		return response;
	}

	@Override
	protected JSONObject get(HttpServletRequest request) throws JSONException {
		
		final JSONObject response = new JSONObject();
		try {
			response.put("data", new JSONArray());
		} catch (JSONException e1) {
			e1.printStackTrace();
		}

		String sort = request.getParameter("sort");
		if (sort == null
				|| !(sort.equalsIgnoreCase("dorname") || sort
						.equalsIgnoreCase("role")))
			sort = "username";
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
		if (sort.equalsIgnoreCase("dorname")) {
			sql1 = "select id from (select id ,(" +
					"select rtrim(name) from SNT.dor as dor where dor.id = doruser.dorid" +
					") as dorname from SNT.doruser as doruser) as w ";
		} else {
			sql1 = "select id from SNT.doruser ";
		}
		if(sort.equalsIgnoreCase("role")){
			sql1 = sql1 + " order by authority " + dir;
		}else{
			sql1 = sql1 + " order by " + sort + " " + dir;
		}
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
				.query(
						"select id,rtrim(username) as username," +
						"(select rtrim(name) from SNT.dor as dor where dor.id = doruser.dorid) as dorname," +
						"dorid,rtrim(authority) as role from SNT.doruser as doruser where id in(:idList) order by "
								+ sort + " " + dir, idsMap,
						new ParameterizedRowMapper<Object>() {
							public Object mapRow(ResultSet rs, int numrow)
									throws SQLException {
								JSONObject json = new JSONObject();
								try {
									json.put("id", rs.getInt("ID"));
									json.put("username", rs
											.getString("username"));
									json.put("dorid", rs.getInt("dorid"));
									json
											.put("dorname", rs
													.getString("dorname"));
									json.put("role", rs.getString("role"));
									response.accumulate("data", json);
								} catch (JSONException e) {
								}
								return null;
							}
						});
		return response;
	}

	@Override
	protected JSONObject add(HttpServletRequest request) throws JSONException {
		
		
		if(Utils.isReadonly()) return FORBIDDEN;
		JSONObject response = new JSONObject();
		response.put(success, false);
		if (getSjt().queryForInt(
				"select count(0)	  from SNT.doruser where username = ?",
				new Object[] { request.getParameter("username") }) != 0) {
			response.put(description, "Такой	  пользователь уже существует");
			return response;
		}

		
		//System.out.println("как я и думал будет null: "+request.getParameter("dorname"));
		String dorname = request
				.getParameter("dorname");
		if(dorname ==null){
			dorname = "1";
		}
		getSjt()
				.update(
						"insert into SNT.doruser (id,dorid,username,password,authority) values(COALESCE((select max(id)+1	  from SNT.doruser),0),?,?,?,?)",
						new Object[] {
								Integer.parseInt(dorname),
								request.getParameter("username"),
								md5.encodePassword(request
										.getParameter("password"), null),
								request.getParameter("role") });
		response.put(success, true);
		return response;
	}

}

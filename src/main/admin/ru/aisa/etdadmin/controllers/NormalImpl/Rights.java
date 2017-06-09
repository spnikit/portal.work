package ru.aisa.etdadmin.controllers.NormalImpl;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.ParameterizedRowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcTemplate;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.json.*;

import ru.aisa.etdadmin.Utils;
import ru.aisa.etdadmin.controllers.AbstractNormalController;
//import sheff.rjd.utils.ListHelper;
import ru.aisa.etdadmin.controllers.SimpleImpl.Issm;

public class Rights extends AbstractNormalController {

	public Rights() throws JSONException {
		super();
	}

	@Override
	protected JSONObject delete(HttpServletRequest request)
			throws JSONException {

		JSONObject response = new JSONObject();
		getSjt().update(
				"delete FROM SNT.doctypeacc WHERE dtid = ? and wrkid = ?",
				new Object[] { Integer.parseInt(request.getParameter("dtid")),
						Integer.parseInt(request.getParameter("wrkid")) });
		response.put(success, true);

		return response;
	}

	@Override
	protected JSONObject edit(HttpServletRequest request) throws JSONException {
		if (Utils.isReadonly())
			return FORBIDDEN;
		JSONObject response = new JSONObject();
		response.put(success, false);
		int cview, cedit, cnew;
		if (request.getParameter("cedit") != null)
			cedit = 1;
		else
			cedit = 0;
		if (request.getParameter("cview") != null)
			cview = 1;
		else
			cview = 0;
		if (request.getParameter("cnew") != null)
			cnew = 1;
		else
			cnew = 0;
		int dtid = Integer.parseInt(request.getParameter("dtid"));
		int wrkid = Integer.parseInt(request.getParameter("wrkid"));
		boolean changed = false;
		try {
			dtid = Integer.parseInt(request.getParameter("docname"));
			changed = true;
		} catch (Exception e) {
		}
		try {
			wrkid = Integer.parseInt(request.getParameter("wrkname"));
			changed = true;
		} catch (Exception e) {
		}
		if (changed)
			if (getSjt()
					.queryForInt(
							"select count(0) from SNT.doctypeacc where dtid = ? and wrkid = ?",
							new Object[] { dtid, wrkid }) != 0) {
				response.put(description,
						"Нельзя создать два правила с одинаковыми формой и ролью.");
				return response;
			}

		getSjt().update(
				"update SNT.doctypeacc set dtid = ? ,  wrkid = ? , cview = ? , cedit = ?, cnew = ? where dtid = ? and wrkid = ? ",
				new Object[] { dtid, wrkid, cview, cedit, cnew,
						Integer.parseInt(request.getParameter("dtid")),
						Integer.parseInt(request.getParameter("wrkid")) });
		response.put(success, true);
		return response;
	}

	@Override
	protected JSONObject get(HttpServletRequest request) throws JSONException {
		final JSONObject response = new JSONObject();
		response.put("data", new JSONArray());

		String sort = request.getParameter("sort");
		if (sort == null
				|| !(sort.equalsIgnoreCase("wrkname")
						|| sort.equalsIgnoreCase("cview")
						|| sort.equalsIgnoreCase("cedit") 
						|| sort.equalsIgnoreCase("cnew"))){
			sort = "docname";
		}
		String dir = request.getParameter("dir");
		if (dir == null || !dir.equalsIgnoreCase("desc")){
			dir = "asc";
		}
		final int start;
		final int limit;
		if (request.getParameter("start") != null){
			start = Integer.parseInt(request.getParameter("start"));
		}else{
			start = 0;
		}
		if (request.getParameter("limit") != null){
			limit = Integer.parseInt(request.getParameter("limit"));
		}else{
			limit = 20;
		}
		/* dta.wrkid */
		String id_sql = "select dtid,wrkid";
		String sql = "select dtid,(select rtrim(name) from SNT.doctype where id = dta.dtid) as docname_utf,"
				+ "wrkid,(select rtrim(name) from SNT.wrkname where id = dta.wrkid "
				+ ") as wrkname_utf,"
				+ "(select issm from snt.wrkname where id = dta.wrkid) issm,"
				+ "cview,cedit,cnew";
		String ss = " from SNT.doctypeacc as dta ";
		String ss1 = "where dta.dtid not in (select id from SNT.doctype where name in("
				+ Utils.FORMS_SNT + ") )";
		HashMap<String, Comparable> pp1 = new HashMap<String, Comparable>();
//		System.out.println(request.getParameter("search"));
//		System.out.println(request.getParameter("search3"));
		if(request.getParameter("search") != null){
			ss1 = ss1
					+ " and dta.dtid in (select id from SNT.doctype where UPPER(name) like :search)";
			pp1.put("search", "%" + request.getParameter("search") + "%");
			
			}
		if (request.getParameter("search3") != null) {
			ss1 = ss1 + " and dta.wrkid in (select id from SNT.wrkname where UPPER(name) like :search3)";
			pp1.put("search3", "%" + request.getParameter("search3") + "%");
//			System.out.println(ss1);
			}

		if (!Utils.isAdmin()) {
			ss1 = ss1
					+ " and dta.wrkid in (select id from SNT.wrkname where dorid = :dorid ) ";
			pp1.put("dorid", Utils.getDorIdForCurrentUser());
		}
		
		if (sort.equalsIgnoreCase("wrkname")) {
			String temp = ",cast((select rtrim(name) from SNT.wrkname where id = dta.wrkid "
					+ ") as char(100) ccsid "
					+ Utils.code
					+ ") as wrkname "
					+ ss;
			id_sql = "select dtid,wrkid from (" + id_sql + temp + ss1
					+ " ) as w ";
			sql = sql + temp;
		} else if (sort.equalsIgnoreCase("docname")) {

			String temp = ",cast((select rtrim(name) from SNT.doctype where id = dta.dtid "
					+ ") as char(100) ccsid "
					+ Utils.code
					+ ") as docname "
					+ ss;
			id_sql = "select dtid,wrkid from (" + id_sql + temp + ss1
					+ " ) as w ";
			sql = sql + temp;
		} else {
			id_sql = id_sql + ss + ss1;
			sql = sql + ss;
		}
		sql = sql + " where ";
		id_sql = id_sql + " order by " + sort + " " + dir;
		List result = getNpjt().queryForList(id_sql, pp1);
		response.put("count", result.size());
		if (result.size() == 0){
			return response;
		}
		for (int i = start; i < Math.min(start + limit, result.size()); i++) {
			sql = sql + " ( dtid = " + ((HashMap) result.get(i)).get("DTID")
					+ " and wrkid = " + ((HashMap) result.get(i)).get("WRKID")
					+ " ) ";
			if (i != (Math.min(start + limit, result.size()) - 1)) {
				sql = sql + " or ";
			} else {
				sql = sql + " order by " + sort + " " + dir;
			}
		}

		getNpjt().query(sql, new HashMap(),
				new ParameterizedRowMapper<Object>() {

					public Object mapRow(ResultSet rs, int numrow)
							throws SQLException {
						JSONObject js = new JSONObject();
						try {
							js.put("dtid", rs.getInt("DTID"));
							js.put("docname", rs.getString("DOCNAME_UTF"));
							js.put("wrkid", rs.getInt("WRKID"));
							js.put("wrkname",
									rs.getString("WRKNAME_UTF")
											+ " ("
											+ Issm.getISSMName(rs
													.getInt("issm")) + ")");
							js.put("cview", rs.getInt("CVIEW"));
							js.put("cedit", rs.getInt("CEDIT"));
							js.put("cnew", rs.getInt("CNEW"));
							response.accumulate("data", js);
						} catch (JSONException e) {
						}
						return null;
					}
				});
		return response;

	}

	@Override
	protected JSONObject add(HttpServletRequest request) throws JSONException {
		if (Utils.isReadonly())
			return FORBIDDEN;
		JSONObject response = new JSONObject();
		response.put(success, false);
		int cedit, cview, cnew;

		if (request.getParameter("cedit") != null)
			cedit = 1;
		else
			cedit = 0;
		if (request.getParameter("cview") != null)
			cview = 1;
		else
			cview = 0;
		if (request.getParameter("cnew") != null)
			cnew = 1;
		else
			cnew = 0;
		List l = getSjt().queryForList(
				"SELECT * FROM SNT.doctypeacc WHERE dtid = ? and wrkid = ?",
				new Object[] {
						Integer.parseInt(request.getParameter("docname")),
						Integer.parseInt(request.getParameter("wrkname")) });
		if (l.size() > 0) {
			response.put(description,
					"Нельзя создать два правила с одинаковыми формой и ролью.");
			return response;
		}

		getSjt().update(
				"insert into SNT.doctypeacc(dtid,wrkid,cview,cedit,cnew) values(?,?,?,?,?) ",
				new Object[] {
						Integer.parseInt(request.getParameter("docname")),
						Integer.parseInt(request.getParameter("wrkname")),
						cview, cedit, cnew });

		response.put(success, true);
		return response;

	}

}

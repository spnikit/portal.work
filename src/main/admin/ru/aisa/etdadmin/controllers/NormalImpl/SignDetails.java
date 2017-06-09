package ru.aisa.etdadmin.controllers.NormalImpl;

import java.io.PrintWriter;
import java.io.StringWriter;
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
import org.springframework.jdbc.core.simple.SimpleJdbcTemplate;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;

import ru.aisa.etdadmin.Utils;
import ru.aisa.etdadmin.controllers.AbstractNormalController;

public class SignDetails extends AbstractNormalController {

	public SignDetails() throws JSONException {
		super();
	}

	@Override
	protected JSONObject delete(HttpServletRequest request)
			throws JSONException {
		
		JSONObject response = new JSONObject();
		getSjt()
				.update(
						"delete FROM SNT.doctypeflow WHERE dtid = ? and wrkid = ? and order = ?",
						new Object[] {
								Integer.parseInt(request.getParameter("dtid")),
								Integer.parseInt(request.getParameter("wrkid")),
								Integer.parseInt(request
										.getParameter("old_order")) });
		response.put(success, true);

		return response;
	}

	@Override
	protected JSONObject edit(HttpServletRequest request) throws JSONException {
		
		JSONObject response = new JSONObject();
		response.put(success, false);
		int oldorder = Integer.parseInt(request.getParameter("old_order"));
		int order = Integer.parseInt(request.getParameter("order"));
		int docid = Integer.parseInt(request.getParameter("dtid"));
		int wrkid = Integer.parseInt(request.getParameter("wrkid"));
		boolean changed = (order != oldorder);
		if (order < 0) {
			response.put(description, "Номер должен быть больше 0");
			return response;
		}
		try {
			docid = Integer.parseInt(request.getParameter("documentname"));
			changed = true;
		} catch (Exception e) {
		}
		try {
			wrkid = Integer.parseInt(request.getParameter("rolename"));
		} catch (Exception e) {
		}

		if (changed)
			if (getSjt()
					.queryForInt(
							"select count(0) from SNT.doctypeflow where dtid = ? and order = ? ",
							new Object[] { docid, order }) != 0) {
				response
						.put(description,
								"Подпись с таким номером для этого документа уже существует.");
				return response;
			}
		getSjt()
				.update(
						// "update SNT.doctypeacc set dtid = ? , wrkid
						// = ? , cview = ? , cedit = ?, cnew = ? where
						// dtid = ? and wrkid = ? ",
						"update SNT.doctypeflow set dtid = ? ,order = ?, wrkid =?,parent=?,exp=? where dtid = ? and order = ? ",
						new Object[] { docid, order, wrkid,
								request.getParameter("parent").length()>=1?Integer.parseInt(request.getParameter("parent")):null,
								//request.getParameter("exp").length()>=1?request.getParameter("exp"):null ,
								null,
								Integer.parseInt(request.getParameter("dtid")),
								oldorder });
		response.put(success, true);
		return response;
	}

	@Override
	protected JSONObject get(HttpServletRequest request) throws JSONException {
	
		final JSONObject response = new JSONObject();
		response.put("data", new JSONArray());
		String sort = request.getParameter("sort");
		if (sort == null
				|| !(sort.equalsIgnoreCase("rolename") || sort
						.equalsIgnoreCase("order") || sort.equalsIgnoreCase("parent")
						|| sort.equalsIgnoreCase("exp")))
			sort = "documentname";
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
		String id_sql = "select dtid,order,wrkid";
		String sql = "select DTID , (SELECT rtrim(NAME) FROM SNT.DOCTYPE WHERE ID = dtf.DTID ) AS DOCUMENTNAME_UTF ," +
				" ORDER , WRKID , (SELECT rtrim(NAME) FROM SNT.WRKNAME WHERE ID = dtf.WRKID ) AS ROLENAME_UTF,parent,exp ";
		String tmp = "";
		HashMap<String, Comparable> hm = new HashMap<String, Comparable>();
		if(request.getParameter("search")!=null){
			id_sql = id_sql + ",(select name from SNT.doctype where id = dtf.dtid) as docname ";
			tmp = " where docname like :search " ;
			hm.put("search", "%"+request.getParameter("search")+"%");
			
			
			if(!Utils.isAdmin()){
				tmp = tmp + " and wrkid in (select id from SNT.wrkname where dorid = :dorid ) ";
				hm.put("dorid", Utils.getDorIdForCurrentUser());
			}
		}else if(!Utils.isAdmin()){
			tmp = " where wrkid in (select id from SNT.wrkname where dorid = :dorid ) ";
			hm.put("dorid", Utils.getDorIdForCurrentUser());
		} 
		
		if(sort.equalsIgnoreCase("rolename")){
			String temp = ",cast((select rtrim(name) from SNT.wrkname where id = dtf.wrkid ) as char(100) ccsid " +
					Utils.code+ ") as rolename from SNT.doctypeflow as dtf ";
			id_sql = "select dtid,order,wrkid from (" + id_sql+ temp + ") as w " + tmp;
			sql = sql + temp;
			
		}else if(sort.equalsIgnoreCase("documentname")){
			String temp = ",cast((select rtrim(name) from SNT.doctype where id = dtf.dtid ) as char(100) ccsid " +
				Utils.code+ ") as documentname from SNT.doctypeflow as dtf ";
			id_sql = "select dtid,order,wrkid from (" + id_sql+ temp + ") as w " + tmp;
			sql = sql + temp;
		}else if(request.getParameter("search") == null){
			id_sql = id_sql + " from SNT.doctypeflow as dtf " + tmp;
			sql = sql + " from SNT.doctypeflow as dtf ";
		}else{
			sql = sql + " from SNT.doctypeflow as dtf ";
			if(sort.equalsIgnoreCase("order")){
				id_sql = "select dtid,order,wrkid from ( " + id_sql 
				+ " from SNT.doctypeflow as dtf  ) as w " + tmp;	
			}else if(sort.equalsIgnoreCase("parent")){
				id_sql = "select dtid,order,wrkid from ( " + id_sql 
				+ ", parent from SNT.doctypeflow as dtf  ) as w " + tmp;
			}else {
				id_sql = "select dtid,order,wrkid from ( " + id_sql 
				+ ", exp from SNT.doctypeflow as dtf  ) as w " + tmp;
			}
		}
			
			
			
		sql = sql + " where ";
		
		id_sql = id_sql + " order by " + sort + " " + dir;
		List result = getNpjt().queryForList(id_sql,hm);
		response.put("count", result.size());
		
		if(result.size()==0)
			return response;
		for (int i = start; i < Math.min(start + limit, result.size()); i++) {
			sql  = sql +" ( dtid = " +((HashMap) result.get(i)).get("DTID") +
				" and order = " + ((HashMap) result.get(i)).get("ORDER") + 
				" and wrkid = " + ((HashMap) result.get(i)).get("WRKID") + " ) "	;
			if(i!=(Math.min(start+limit, result.size()) -1)){
				sql = sql + " or ";
			}else{
				sql = sql + " order by " + sort + " " + dir;
			}
		}
		getNpjt().query(sql, new HashMap(),
				new ParameterizedRowMapper<Object>() {

			public Object mapRow(ResultSet rs, int numrow) throws SQLException {
				JSONObject js = new JSONObject();
				try {
						js.put("dtid", rs.getInt("DTID"));
						js
								.put("documentname", rs
										.getString("DOCUMENTNAME_UTF"));
						js.put("order", rs.getInt("ORDER"));
						js.put("old_order", rs.getInt("order"));
						js.put("wrkid", rs.getInt("WRKID"));
						js.put("rolename", rs.getString("ROLENAME_UTF"));
						js.put("parent", rs.getObject("parent"));
						//js.put("exp", rs.getString("exp"));
						response.accumulate("data", js);
				} catch (JSONException e) {
				}
				return null;
			}
		});
		return response;
		
	
		
	}

	@Override
	protected JSONObject add(HttpServletRequest request)
			throws JSONException {
		JSONObject response = new JSONObject();
		response.put(success, false);
		int order = Integer.parseInt(request.getParameter("order"));
		if (order < 0) {
			response.put(description, "Номер должен быть больше 0");
			return response;
		}
		// List l =
		if (getSjt()
				.queryForInt(
						"SELECT count(0) FROM SNT.DOCTYPEFLOW WHERE DTID = ? AND ORDER = ?",
						new Object[] {
								Integer.parseInt(request
										.getParameter("documentname")), order }) != 0) {
			response
					.put(description,
							"Подпись с таким номером для этого документа уже существует.");
			return response;
		}

		getSjt()
				.update(
						"INSERT INTO SNT.DOCTYPEFLOW (DTID,ORDER,WRKID,parent,exp) values(?,?,?,?,?)",
						new Object[] {
								Integer.parseInt(request
										.getParameter("documentname")),
								order,
								Integer.parseInt(request
										.getParameter("rolename")),
										request.getParameter("parent").length()>=1?Integer.parseInt(request.getParameter("parent")):null,
										//request.getParameter("exp").length()>=1?request.getParameter("exp"):null 
										null});
		response.put(success, true);

		return response;
	}

}

package ru.aisa.etdadmin.controllers.NormalImpl;

import java.io.PrintWriter;
import java.io.Serializable;
import java.io.StringWriter;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
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
import ru.aisa.etdadmin.controllers.AbstractNormalController;
import ru.aisa.etdadmin.controllers.SimpleImpl.Issm;

public class Pred extends AbstractNormalController {

	public Pred() throws JSONException {
		super();
	}

	@Override
	protected JSONObject delete(HttpServletRequest request) throws JSONException {
		if(Utils.isReadonly()) return FORBIDDEN;
		JSONObject response = new JSONObject();
		int role = Integer.parseInt(request.getParameter("wrkid"));
		//int predpr=Integer.parseInt(request.getParameter("predid"));
		int dor = getSjt().queryForInt("SELECT dorid FROM SNT.WRKNAME WHERE ID="+role+"");

		{
			if ((Utils.getAuth().equals(Utils.ROLE_POWER_USER) && Utils.getDorIdForCurrentUser()==dor)||!Utils.getAuth().equals(Utils.ROLE_POWER_USER) )
			{
				getSjt().update("delete from SNT.perswrk where pid =? and wrkid = ? ", 
						new Object[]{Integer.parseInt(request.getParameter("pid")), 
						Integer.parseInt(request.getParameter("wrkid"))});


				response.put(success, true);
				return response;
			}
			else response.put(success, true);
			return response;
		}
	}

	@Override
	protected JSONObject edit(HttpServletRequest request) throws JSONException {
		if(Utils.isReadonly()) return FORBIDDEN;
		JSONObject response = new JSONObject();
		response.put(success, false);
		int role = Integer.parseInt(request.getParameter("wrkid"));
		int predpr = Integer.parseInt(request.getParameter("predid"));
		int dor = getSjt().queryForInt("SELECT dorid FROM SNT.WRKNAME WHERE ID="+role+"");
		if ((Utils.getAuth().equals(Utils.ROLE_POWER_USER) && Utils.getDorIdForCurrentUser()==dor)||!Utils.getAuth().equals(Utils.ROLE_POWER_USER) )

		{	
			int uid;
			try{
				uid = getSjt().queryForInt("select id from SNT.personall where certserial = ? ", new Object[]{request.getParameter("certserial")});
			}catch (org.springframework.dao.IncorrectResultSizeDataAccessException e) {
				response.put(description, "Пользователь не найден");
				return response;
			}
			int pid = Integer.parseInt(request.getParameter("pid"));
			boolean changed = (uid!=pid);
			try{
				role = Integer.parseInt(request.getParameter("rolename"));
				changed = true;
			}catch (Exception e) {
			}
			try{
				predpr = Integer.parseInt(request.getParameter("predprs"));
				changed = true;
			}catch (Exception e) {
			}

			if(!changed){
				response.put(success, true);
				return response;
			}
			if(getSjt().queryForInt("select count(0) from SNT.perswrk where wrkid = ?  and pid = ? and predid=? ", 
					new Object[]{	role,
					uid,predpr})!=0){
				response.put(description,"Такая запись уже есть в базе данных");
				return response;
			}


			int persid = getSjt().queryForInt("select id from SNT.personall where certserial = ?", new Object[]{request.getParameter("certserial")});
			getSjt().update("update SNT.perswrk set pid = ? , wrkid = ?,predid=? where pid = ? and wrkid = ? and predid=? ", new Object[]{persid,role,predpr,
					Integer.parseInt(request.getParameter("pid")),Integer.parseInt(request.getParameter("wrkid")),Integer.parseInt(request.getParameter("predid"))});
			response.put(success,true);
			return response;}
		response.put(success,true);
		return response;
	}


	@Override
	protected JSONObject get(HttpServletRequest request) throws JSONException {

		final JSONObject response = new JSONObject();
		response.put("data", new JSONArray());
		final int start;
		final int limit;
		String sort=request.getParameter("sort");
	if (sort==null) sort = "fio";
//		if (sort==null||!(sort.equalsIgnoreCase("rolename")||sort.equalsIgnoreCase("predname"))) sort = "fio";
		String dir= request.getParameter("dir");
		if(dir==null || !dir.equalsIgnoreCase("desc")) dir = "asc";
		if (request.getParameter("start")!=null) start = Integer.parseInt(request.getParameter("start"));
		else start =0 ;
		if(request.getParameter("limit")!=null) limit = Integer.parseInt(request.getParameter("limit"));
		else limit = 20;
		String id_sql = "select pid,wrkid,predid from (select pid,wrkid,predid,";
		String sql = "select pid,wrkid,predid," +
		"(select COALESCE(rtrim(fname),'')||' '||COALESCE(rtrim(mname),'')||' '||COALESCE(rtrim(lname),'') from SNT.personall where id =pswk.pid) as fio_utf," +
		"(select certserial from SNT.personall where id = pswk.pid ) as certserial ," +
		"(select rtrim(vname) from SNT.pred where id = pswk.predid) as predname_utf,  " +
		"(select rtrim(name) from SNT.wrkname where id = pswk.wrkid) as rolename_utf,"+
		"(select issm from snt.wrkname where id = pswk.wrkid) issm, ";
		String temp;
		
		
		if(sort.equalsIgnoreCase("fio")){
			temp = "cast((" +
			"select COALESCE(rtrim(fname),'')||' '||COALESCE(rtrim(mname),'')||' '||COALESCE(rtrim(lname),'')" +
			" from SNT.personall where id =pswk.pid" +
			") as char(250) ccsid " +
			Utils.code + ") as fio ";
		}else if(sort.equalsIgnoreCase("predprs")){
			sort = "predname";
			temp = "cast((select rtrim(vname) from SNT.pred where id = pswk.predid) as char(100) ccsid " +
			Utils.code+" ) as predname";
		}else {
			temp = "cast((select rtrim(name) from SNT.wrkname where id = pswk.wrkid) as char(100) ccsid " +
			Utils.code+" ) as rolename";
		}
		id_sql = id_sql +temp;
		sql = sql +temp;
		if(request.getParameter("search")!=null){
			id_sql = id_sql + ",(select COALESCE(rtrim(fname),'')||' '||COALESCE(rtrim(mname),'')||' '||COALESCE(rtrim(lname),'')" +
			" as fio_utf from SNT.personall where id =pswk.pid" +
			") as fio_utf ";
		}
		id_sql = id_sql + " from SNT.perswrk as pswk";
		HashMap<String, Comparable> parameterMap = new HashMap<String, Comparable>();
		if(request.getParameter("search")!=null){
			id_sql = id_sql + " ) as w where upper(fio_utf) like :pattern ";
			parameterMap.put("pattern", "%"+ request.getParameter("search")+"%");
			if(/*Utils.getAuth().equals(Utils.ROLE_POWER_USER)||*/Utils.getAuth().equals(Utils.ROLE_USER)){
				id_sql = id_sql + " and w.wrkid in (select id from SNT.wrkname where dorid = :dorid ) ";
				parameterMap.put("dorid", Utils.getDorIdForCurrentUser());
			}

		}else{
			id_sql = id_sql + " ) as w ";
			if(/*Utils.getAuth().equals(Utils.ROLE_POWER_USER)||*/Utils.getAuth().equals(Utils.ROLE_USER)){
				id_sql = id_sql + " where w.wrkid in (select id from SNT.wrkname where dorid = :dorid ) ";
				parameterMap.put("dorid", Utils.getDorIdForCurrentUser());
			}
		}
		id_sql=id_sql+" order by " + sort + " " + dir;
		List result = getNpjt().queryForList(id_sql, parameterMap);
		response.put("count", result.size());
		sql = sql + " from SNT.perswrk as pswk where ";
		if(result.size()==0)
			return response;

		for (int i = start; i < Math.min(start + limit, result.size()); i++) {
			sql  = sql +" ( pid = " +((HashMap) result.get(i)).get("PID") +
			" and wrkid = " + ((HashMap) result.get(i)).get("WRKID") + 
			" and predid = " + ((HashMap) result.get(i)).get("PREDID") + " ) "	;
			if(i!=(Math.min(start+limit, result.size()) -1)){
				sql = sql + " or ";
			}else{
				sql = sql + " order by " + sort + " " + dir;
			}
		}

		getNpjt().query(sql,new HashMap(), 
				new ParameterizedRowMapper<Object>() {

			public Object mapRow(ResultSet rs, int numrow) throws SQLException {

				JSONObject js = new JSONObject();
				try {

					js.put("FIO", rs.getString("FIO_UTF"));
					js.put("certserial", rs.getString("certserial"));
					js.put("wrkid", rs.getInt("WRKID"));
					js.put("rolename", rs.getString("rolename_utf") + " ("+Issm.getISSMName(rs.getInt("issm"))+")");
					js.put("predid", rs.getInt("PREDID"));					
					js.put("predprs", rs.getString("predname_utf"));
					js.put("pid", rs.getInt("pid"));
					response.accumulate("data", js);

				} catch (JSONException e) {
				}
				return null;
			}
		}
		);
		
		return response;


	}
	@Override
	protected JSONObject add(HttpServletRequest request) throws JSONException {
		if(Utils.isReadonly()) return FORBIDDEN;
		JSONObject response = new JSONObject();
		response.put(success, false);
		if(getSjt().queryForInt("select count(0) from SNT.personall where certserial = ?", 
				new Object[]{request.getParameter("certserial")})==0){

			response.put(description, "Пользователя с таким ID сертификата не существует.");
			return response;
		}
		if(getSjt().queryForInt("select count(0) from SNT.perswrk where pid= (select id from SNT.personall where certserial = ? ) and wrkid = ? and predid=?", 
				new Object[]{request.getParameter("certserial"),
				Integer.parseInt(request.getParameter("rolename")), 
						Integer.parseInt(request.getParameter("predprs"))})!=0){

			response.put(description, "Такая запись уже имеется в базе данных");

			return response;

		}

		int persid = getSjt().queryForInt("select id from SNT.personall where certserial = ?", new Object[]{request.getParameter("certserial")});
		getSjt().update("insert into SNT.perswrk (pid,wrkid,predid) values (?,?,?)", 
				persid, 
				Integer.parseInt(request.getParameter("rolename")),
				Integer.parseInt(request.getParameter("predprs")));
		response.put(success, true);

		return response;
	}

}

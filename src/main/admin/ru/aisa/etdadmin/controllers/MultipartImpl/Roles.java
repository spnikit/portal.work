package ru.aisa.etdadmin.controllers.MultipartImpl;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.jdbc.core.simple.ParameterizedRowMapper;

import ru.aisa.etdadmin.Utils;
import ru.aisa.etdadmin.controllers.AbstractMultipartController;
import ru.aisa.etdadmin.controllers.SimpleImpl.Issm;
import ru.aisa.rgd.ws.utility.TypeConverter;
import sheff.rjd.ws.OCO.TORLists;
public class Roles extends AbstractMultipartController {

	public Roles() throws JSONException {
		super();
	}

	@Override
	protected JSONObject delete(HttpServletRequest request)
			throws JSONException {
		if(Utils.isReadonly()) return FORBIDDEN;
		JSONObject response = new JSONObject();
		response.put(success, false);
		Object[] param = new Object[]{ Integer.parseInt(request.getParameter("id"))};
		if (getSjt().queryForInt(
				"select count(0) from SNT.perswrk where wrkid =?",
				param) != 0) {
			response
					.put(description,
							"Нельзя удалить предприятие, потому что на нем зарегистрированы пользователи.");
			return response;
		}
		if(getSjt().queryForInt("select count(0) from SNT.doctypeflow where wrkid = ?", param)!=0){
			response.put(description, "Нельзя удалить предприятие, потому что оно необходимо для подписания документов.");
			return response;
		}
		if(getSjt().queryForInt("select count(0) from SNT.doctypeacc where wrkid = ?", param)!=0){
			response.put(description, "Нельзя удалить предприятие, потому что для данного предприятия созданы права для форм.");
			return response;
		}
		//getSjt().update("delete from snt.wrk_logo where wrkid = ?", param);
		getSjt().update("delete FROM SNT.wrkname WHERE id = ?",
				param);
		response.put(success, true);
		return response;
	}

	@Override
	protected JSONObject edit(
			HashMap<String, String> requestParameters,
			HashMap<String, byte[]> requestFiles,HashMap<String, String> requestFileNames) throws JSONException, IOException{
		if(Utils.isReadonly()) return FORBIDDEN;
		JSONObject response = new JSONObject();
		response.put(success, false);
		
		int dorid =1;
		int issm = Integer.parseInt(requestParameters.get("issmid"));
		try{
			issm = Integer.parseInt(requestParameters.get("issm"));
		}catch (Exception e) {
		}
		
		if (getSjt()
				.queryForInt(
						"select count(0) from SNT.wrkname where name = ? and id <> ? ",
						new Object[] {requestParameters.get("name"),
								Integer.parseInt(requestParameters.get("id")) }) != 0) {
			response.put(description, "Такая запись уже есть в базе данных.");
			return response;
		}
		getSjt().update(				
				"update SNT.wrkname set name = ?,issm=?,dorid = ? where id = ? ",
				new Object[] {requestParameters.get("name"),issm,dorid,
						Integer.parseInt(requestParameters.get("id")) });
		response.put(success, true);

		return response;
	}

	@Override
	protected JSONObject get(HttpServletRequest request) throws JSONException {
		final JSONObject response = new JSONObject();
		response.put("data", new JSONArray());
		final int start;
		final int limit;
		String sort = request.getParameter("sort");
		if (sort == null || !(sort.equalsIgnoreCase("issm")||sort.equalsIgnoreCase("dor")))
			sort = "name";
		String dir = request.getParameter("dir");
		if (dir == null || !dir.equalsIgnoreCase("desc"))
			dir = "asc";
		if (request.getParameter("start") != null)
			start = Integer.parseInt(request.getParameter("start"));
		else
			start = 0;
		if (request.getParameter("limit") != null)
			limit = Integer.parseInt(request.getParameter("limit"));
		else
			limit = 20;
		String sql,sql1;
		HashMap<String, String> pp1 = new HashMap<String, String>();
		if(sort.equals("name")){
			sql1 = "select id,cast(rtrim(name) as char(100) ccsid " +
				Utils.code+") as name ";
			sql = ",rtrim(cast(name as char(100) ccsid " +
				Utils.code + ")) as name ";
		}else if(sort.equals("dor")){
			sql1 = "select id,cast((" +
					"select rtrim(name) from SNT.dor where id = wrk.dorid " +
					") as char(100) ccsid " +
					Utils.code+") as dor ";
			sql = ",(select rtrim(cast(name as char(100) ccsid " +
				Utils.code +
				")) from SNT.dor where id = wrk.dorid ) as dor ";
		}else{
			sql1 = "select id,issm ";
		sql = "";
		}
		if(request.getParameter("search")==null)
			sql1 = "select id from ("+sql1 +" from SNT.wrkname as wrk) as w order by " + sort + " " + dir;
		else{
			sql1 = "select id from ("+sql1 +" from SNT.wrkname as wrk where upper(wrk.name) like :search) as w order by " + sort + " " + dir;
			pp1.put("search", "%" + request.getParameter("search") + "%");
		}
		List result = getNpjt().queryForList(sql1, pp1);
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
						"select rtrim(name) as name_utf,id,issm,(select rtrim(name) from SNT.dor dor where dor.id = wrk.dorid) as dorname, dorid "
														+ sql
								+ " from SNT.wrkname as wrk where id in (:idList) order by "
								+ sort + " " + dir, idsMap,
						new ParameterizedRowMapper<Object>() {

							public Object mapRow(ResultSet rs, int numrow)
									throws SQLException {
								JSONObject js = new JSONObject();
								try {
									js.put("name", rs.getString("NAME_utf"));
									js.put("id", rs.getInt("ID"));
									int issm = rs.getInt("issm");
									js.put("issm", Issm.getISSMName(issm));
									js.put("issmid", issm);
									js.put("dor", rs.getString("dorname"));
									//js.put("par",rs.getString("par"));
									//js.put("bal", rs.getInt("balval"));
									js.put("dorid", rs.getInt("dorid"));
									
									response.accumulate("data", js);
									} catch (JSONException e) {
								}
								return null;
							}
						});
		return response;
	}

	@SuppressWarnings("deprecation")
	@Override
	protected JSONObject add(
			HashMap<String, String> requestParameters,
			HashMap<String, byte[]> requestFiles,HashMap<String, String> requestFileNames) throws JSONException, IOException{
		if(Utils.isReadonly()) return FORBIDDEN;
		JSONObject response = new JSONObject();
		response.put(success, false);
		int issm = Integer.parseInt(requestParameters.get("issm"));
//		int dorid = Integer.parseInt(requestParameters.get("dor"));
		int dorid = 1;
		if (getSjt()
				.queryForInt(
						"select count(0) from SNT.wrkname where name = ? and issm =?",
						new Object[] {
								requestParameters.get("name"), Integer.parseInt(requestParameters.get("issm"))}) != 0) {
			response.put(description, "Такая запись уже есть в базе данных.");
			return response;
		}
		
		int maxWrkId = getSjt().queryForInt("select COALESCE(max(id)+1,0) from snt.wrkname", new Object[]{});
		
		getSjt()
				.update(
"insert into SNT.wrkname (id,name,depid,issm,dorid) values( ?,?,(select min(id) from SNT.department),?,?)",
						new Object[] {maxWrkId,
		requestParameters.get("name"),issm,dorid });
		
		
		
		
		try{
		List<Object[]> rights = new ArrayList<Object[]>(); 
		HashMap<String, Integer> listttt = null;
		
		if (issm==1||issm==3) listttt = TORLists.adminlissign;
		else if (issm==2) listttt = TORLists.adminlistacc;
		else if (issm==6) listttt = TORLists.adminlistview;
		for (int i=0;i<TORLists.adminlistTOR.size(); i++){
		int typeid = getSjt().queryForInt("select id from snt.doctype where name = ?", new Object[]{TORLists.adminlistTOR.get(i)});
			rights.add(new Object[]{typeid,maxWrkId,1,listttt.get(TORLists.adminlistTOR.get(i)),0});
			}
		
		getSjt().batchUpdate("insert into SNT.doctypeacc(dtid,wrkid,cview,cedit,cnew) values(?,?,?,?,?)", rights);
		
		}catch (Exception e){
			log.error(TypeConverter.exceptionToString(e));
			e.printStackTrace();
		}
		
		
		response.put(success, true);
		
		return response;
	}

}

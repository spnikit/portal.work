package ru.aisa.etdadmin.controllers.MultipartImpl;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.codec.binary.Base64;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.jdbc.core.simple.ParameterizedRowMapper;

import ru.aisa.etdadmin.Utils;
import ru.aisa.etdadmin.controllers.AbstractMultipartController;

public class Forms extends AbstractMultipartController {

	
	public Forms() throws JSONException {
		super();
	}

	@Override
	protected JSONObject add(HashMap<String, String> requestParameters,
			HashMap<String, byte[]> requestFiles,HashMap<String, String> requestFileNames) throws JSONException,
			IOException {
		if(Utils.isReadonly()) return FORBIDDEN;
		JSONObject response = new JSONObject();
		response.put(success, false); 
		String formName = requestParameters.get("docname");
		Integer groupid = Integer.parseInt(requestParameters.get("docdoctype"));
		byte[] fileContent = requestFiles.get("formfile");
//		int ptype= Integer.parseInt(requestParameters.get("ptype"));
		int ptype=0;
		if (fileContent != null && fileContent.length > 0) {
			if (getSjt().queryForInt(
					"select count(id) from SNT.doctype where name=?",
					new Object[] { formName }) != 0) {

				response.put(description,
						"Форма с таким названием уже существует");
				return response;
			}
			Map<String, Object> parameterMap = new HashMap<String, Object>();
			parameterMap.put("contentBlob", fileContent);
			parameterMap.put("name", formName);
			parameterMap.put("ptype", ptype);
			parameterMap.put("groupid", groupid);
			
			getNpjt()
					.update(
							"INSERT INTO SNT.DOCTYPE(TEMPLATE, ID , NAME,ptype, groupid) VALUES(:contentBlob, COALESCE((select max(id)+1 from SNT.doctype),0), :name,:ptype,:groupid)",
							parameterMap);
			response.put(success, true);
		} else {
			response.put(description, "Нет файла");
		}

		return response;
	}

	@Override
	protected JSONObject delete(HttpServletRequest request)
			throws JSONException {
		if(Utils.isReadonly()) return FORBIDDEN;
		JSONObject response = new JSONObject();
		response.put(success, false);


		if (getSjt().queryForInt(
				"select count(0) from SNT.docstore where typeid = ? ",
				Integer.parseInt(request.getParameter("docid"))) != 0) {
			response
					.put(description,
							"Форму нельзя удалить, потому что есть сохранённые документы с ней.");
			return response;
		}

		getSjt().update("delete from SNT.doctypeacc where dtid = ?",
				new Object[] { Integer.parseInt(request
						.getParameter("docid")) });

		getSjt().update("delete from SNT.doctypeflow where dtid = ?",
				new Object[] { Integer.parseInt(request
						.getParameter("docid")) });
		getSjt()
				.update(
						"delete FROM SNT.doctype WHERE id = ?",
						new Object[] { Integer.parseInt(request
								.getParameter("docid")) });
		response.put(success, true);

		return response;
	}

	@Override
	protected JSONObject edit(HashMap<String, String> requestParameters,
			HashMap<String, byte[]> requestFiles,HashMap<String,String> requestFileNames) throws JSONException,
			IOException {
		if(Utils.isReadonly()) return FORBIDDEN;
		JSONObject response = new JSONObject();
		response.put(success, false);
		String formName = requestParameters.get("docname");
//		int ptype = Integer.parseInt(requestParameters.get("ptype"));
		int ptype = 0;
		int id = Integer.parseInt(requestParameters.get("docid"));
		int groupid = -1;
		
		
		if (requestParameters.get("docdoctype").matches("\\d+")){
			 groupid = Integer.parseInt(requestParameters.get("docdoctype"));
		}
		else {
			 groupid = getSjt().queryForInt("select id from SNT.docgroups where name = ?", new Object[] {requestParameters.get("docdoctype")});
					
		}
		
		
		
		byte[] fileContent = requestFiles.get("formfile");
		if (getSjt().queryForInt(
				"select count(id)  from SNT.doctype where id<>? and name = ? and groupid =?",
				new Object[] { id, formName, groupid}) != 0) {
			response.put(description, "Форма с таким названием уже существует");
			return response;
		}
		getSjt().update("UPDATE SNT.DOCTYPE SET NAME = ? ,ptype=?, groupid=? WHERE ID = ? ",
				new Object[] { formName,ptype ,groupid ,id});
		if (fileContent != null && fileContent.length > 0) {
			Map<String, Object> pp = new HashMap<String, Object>();
			pp.put("contentBlob", fileContent);
			pp.put("id", id);
			getNpjt()
					.update(
							"UPDATE SNT.DOCTYPE SET TEMPLATE = :contentBlob WHERE ID = :id ",
							pp);
		}
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
		if(sort==null||!(sort.equalsIgnoreCase("ptype") || sort.equalsIgnoreCase("docdoctype"))){
			sort = "docname";
		}
		if(sort.equalsIgnoreCase("docdoctype")) sort = "doctypename";
		
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
		String sql;
		HashMap<String,String> pp1 = new HashMap<String, String>();
		
		if(request.getParameter("search")==null)
			sql = "select id from (select id,rtrim(cast(name as char(100) ccsid "
				+Utils.code
				+ "))as docname,ptype,(Select rtrim(name) from snt.docgroups where id = dt.groupid) as doctypename from SNT.doctype dt) as w order by "+sort+" "+dir;
		else
		{
			sql = "select id from (select id,rtrim(cast(name as char(100) ccsid "
					+Utils.code
					+ "))as docname,ptype from SNT.doctype where UPPER(name) like :search) as w order by "+sort+" "+dir;
			pp1.put("search", "%" + request.getParameter("search") + "%");
		}
		
		List result = getNpjt().queryForList(sql,pp1);
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
						"select rtrim(name) as docname_utf,rtrim(cast(name as char(100) ccsid "
								+ Utils.code
								+ "))as docname,id as docid,ptype,(Select rtrim(name) from snt.docgroups where id = dt.groupid) as doctypename, (Select id from snt.docgroups where id = dt.groupid) as doctypeid from SNT.doctype dt where id in (:idList) order by "
								+ sort + " " + dir, idsMap,
						new ParameterizedRowMapper<Object>() {

							public Object mapRow(ResultSet rs, int numrow)
									throws SQLException {

								try {
									JSONObject js = new JSONObject();
									js.put("docid", rs.getInt("docid"));
									js.put("docname", rs
											.getString("docname_utf"));
									js.put("ptype", rs.getInt("ptype"));
									js.put("docdoctype", rs.getString("doctypename"));
									js.put("docdoctypeid", rs.getInt("doctypeid"));
									response.accumulate("data", js);
								} catch (JSONException e) {
								}
								return null;
							}
						});
		return response;
	}

	@Override
	protected void export(HttpServletRequest request, HttpServletResponse response) throws IOException {
		
		HashMap<String, String> hm = new HashMap<String, String>();
		PrintWriter out = response.getWriter();
		if(request.getParameter("formname")==null||request.getParameter("formname").length()<1){
			out.println("name is not specified");
			return;
		}
		String name = new String(Base64.decodeBase64(request.getParameter("formname").getBytes()),"UTF-8"); 
		hm.put("NAME", name);
		try{
			byte[] template = (byte[]) getNpjt().queryForObject("select template from SNT.doctype where name = :NAME", hm, byte[].class);
			response.setHeader("Content-disposition", "attachment; filename=form.xfdl");
			response.setContentType("application/vnd.xfdl");
			out.println(new String(template,codepage));
		}catch (Exception e) {
			out.println(e.getLocalizedMessage());
		}
		
	}

}




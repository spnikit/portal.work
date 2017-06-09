package ru.aisa.etdadmin.controllers.MultipartImpl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.jdbc.core.simple.ParameterizedRowMapper;

import ru.aisa.etdadmin.Utils;
import ru.aisa.etdadmin.controllers.AbstractNormalController;

public class DirectoryService extends AbstractNormalController {

	private static String insertSql = "insert into SNT.SERVICES(id, pred_id, type_id, service_code, service_name, services_price) "
			+ "values (COALESCE((select max(id)+1 from SNT.SERVICES),0),?,(select id from snt.doctype where name = 'РДВ'),?,?,?)";
	private static String updateSql = "UPDATE SNT.SERVICES SET PRED_ID=? , SERVICE_CODE=? ,SERVICE_NAME=?, SERVICES_PRICE=? WHERE ID=?";
	private static String sqlDeleteAllServicesByPredId = "delete from snt.services where pred_id=? and type_id = (select id from snt.doctype where name = 'РДВ')"; 
	
	
	public DirectoryService() throws JSONException {
		super();
	}
	
	protected JSONObject get(HttpServletRequest request) throws JSONException {
		
		String sql="";
		final JSONObject response = new JSONObject();
		response.put("data", new JSONArray());
		Map <String, Object> sqlParam = new HashMap<String, Object>();
		//sqlParam.put("type_id", 4);
		response.put("data", new JSONArray());
		String sort = request.getParameter("sort");
		if (sort == null || !(sort.equalsIgnoreCase("service_code") || sort.equalsIgnoreCase("service_name") || sort.equalsIgnoreCase("service_price"))){
			sort = "name_pred";
		}
		String dir = request.getParameter("dir");
		if (dir == null || !dir.equalsIgnoreCase("desc")){
			dir = "asc";
		}
		int start;
		int limit;
		int rowCount;
		int to ;
		String sql_count = "select COUNT(*) from SNT.SERVICES";
		if (request.getParameter("start") != null)
			start = Integer.parseInt(request.getParameter("start"));
		else
			start = 0;

		if (request.getParameter("limit") != null)
			limit = Integer.parseInt(request.getParameter("limit"));
		else
			limit = 20;

		to = start+limit;
		sqlParam.put("from", start);
		sqlParam.put("to", to);
		rowCount = getNpjt().queryForInt(sql_count, sqlParam);
		if(rowCount == 0) return response;
		
		if(request.getParameter("search")!=null){
			
			sql = "Select * from (select ROW_NUMBER() OVER() AS rownum, s.id, p.vname as name_pred, s.service_code, s.service_name, s.services_price from SNT.SERVICES s, SNT.PRED p where s.pred_id = p.id and s.type_id = (select d.id from snt.doctype d where d.name = 'РДВ' ) and s.service_code like :service_code) AS tmp where rownum > :from and rownum <= :to order by ";
			//Integer stringToInt = 0;
			/*try{
				stringToInt=Integer.parseInt(request.getParameter("search"));
			}catch(Exception e){
				stringToInt = 0;
			}*/
			sqlParam.put("service_code", request.getParameter("search"));
		}else{
			sql = "select * from (select ROW_NUMBER() OVER() AS rownum, s.id, p.vname as name_pred, s.service_code, s.service_name, s.services_price from SNT.SERVICES s, SNT.PRED p where s.pred_id = p.id and s.type_id = (select d.id from snt.doctype d where d.name = 'РДВ' )) AS tmp where rownum > :from and rownum <= :to order by ";
		}
		response.put("count", rowCount);
		getNpjt().query(sql+sort+" "+dir, sqlParam,
				new ParameterizedRowMapper<Object>() {

			public Object mapRow(ResultSet rs, int numrow)
					throws SQLException {

				try {
					JSONObject js = new JSONObject();
					js.put("id", rs.getInt("ID"));
					js.put("name_pred", rs.getString("NAME_PRED"));
					js.put("service_code", rs.getString("SERVICE_CODE"));
					js.put("service_name", rs.getString("SERVICE_NAME"));
					js.put("service_price", rs.getDouble("SERVICES_PRICE"));
					response.accumulate("data", js);
				} catch (JSONException e) {
				}
				return null;
			}

		});
		return response;
	}
	
	protected JSONObject add(HttpServletRequest request) throws JSONException {
		Object obj = new Object[] {
				Integer.parseInt(request.getParameter("name_pred")),
				Integer.parseInt(request.getParameter("service_code")),
				request.getParameter("service_name"),
				Double.parseDouble(request.getParameter("service_price"))
		};		
		if(Utils.isReadonly()) return FORBIDDEN;
		JSONObject response = new JSONObject();
		response.put(success, false);
		getSjt().update(insertSql,obj);
		response.put(success, true);
		return response;
	}
		
	protected JSONObject delete(HttpServletRequest request)
			throws JSONException {
		
		if(Utils.isReadonly()) return FORBIDDEN;
		String predidStr = request.getParameter("predid");
		Integer predid = null;
		if(predidStr!=null){
			predid = Integer.parseInt(request.getParameter("predid"));
		}
		Map <String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("predid", predid);
		JSONObject response = new JSONObject();
		response.put(success, false);
		if(predid!=null){
			int count = getNpjt().queryForInt("select count(*) from snt.services where pred_id= :predid and type_id = (select id from snt.doctype where name = 'РДВ')", paramMap);
			if(count>0){
				getSjt().update(sqlDeleteAllServicesByPredId,new Object[] { predid });
				response.put(success, true);
			}else{
				response.put(success, false);
			}
			
		}else{
			getSjt().update("delete from SNT.SERVICES where id = ?",new Object[] { Integer.parseInt(request.getParameter("id")) });
			response.put(success, true);
		}
		
		return response;
	}

	protected JSONObject edit(HttpServletRequest request) throws JSONException {
		if(Utils.isReadonly()) return FORBIDDEN;
		JSONObject response = new JSONObject();
		response.put(success, false);
		Integer id = Integer.parseInt(request.getParameter("id"));
		String pred_name = request.getParameter("name_pred");
		Integer pred_name_i = 0;
		try{
			pred_name_i =Integer.parseInt(pred_name);
		}catch(Exception e){
			Map<String, Object> paramMap = new HashMap<String, Object>();
			paramMap.put("dorname",pred_name);
			pred_name_i = getNpjt().queryForInt("select id from SNT.pred where type <> 1 and vname = :dorname order by vname", paramMap);
		}
		//Integer service_code = Integer.parseInt(request.getParameter("service_code"));
		String service_name = request.getParameter("service_name");
		Double service_price = Double.parseDouble(request.getParameter("service_price"));
		getSjt().update(updateSql,new Object[] {pred_name_i, request.getParameter("service_code"), service_name, service_price,id });
		response.put(success, true);
		return response;
	}
	
}
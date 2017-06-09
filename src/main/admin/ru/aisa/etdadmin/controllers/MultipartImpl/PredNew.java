package ru.aisa.etdadmin.controllers.MultipartImpl;


import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.jdbc.core.simple.ParameterizedRowMapper;

import ru.aisa.etdadmin.Utils;
import ru.aisa.etdadmin.controllers.AbstractNormalController;

public class PredNew extends AbstractNormalController {

	public PredNew() throws JSONException {
		super();
	}

	protected JSONObject get(HttpServletRequest request) throws JSONException {

		final JSONObject response = new JSONObject();
		response.put("data", new JSONArray());

		String sort = request.getParameter("sort");
		//System.out.println(sort);
		if (sort == null
				|| !(sort.equalsIgnoreCase("name")
				||sort.equalsIgnoreCase("okpo_kod")
				||sort.equalsIgnoreCase("lname")
				||sort.equalsIgnoreCase("cabinet_id")
				||sort.equalsIgnoreCase("inn")
				||sort.equalsIgnoreCase("kpp")
				||sort.equalsIgnoreCase("headid")
				||sort.equalsIgnoreCase("contr_code")
				||sort.equalsIgnoreCase("priceCheck")
				||sort.equalsIgnoreCase("pdfCheck")))
			sort = "vname";
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
		
		String sql;
		String sql_count;
		int rowCount;
		Map <String, Object> sqlParam = new HashMap<String, Object>();

		int to = start+limit;
		
		sqlParam.put("from", start);
		sqlParam.put("to", to);
		sql_count = "select COUNT(*) from SNT.PRED";
		 rowCount = getNpjt().queryForInt(sql_count, sqlParam);

		if(rowCount == 0) return response;
		
		//System.out.println(request.getParameter("search"));
		//System.out.println(request.getParameter("text"));
		//HashMap<String, Object> parameterMap = new HashMap<String, Object>();
			if(request.getParameter("search") != null&&request.getParameter("text") != null){
				if(request.getParameter("search").equals("lname")){
				
				}
				sql = "Select * from (select ROW_NUMBER() OVER() AS rownum, SNT.PRED.* from SNT.PRED where "+ /*upper(rtrim(:search))*/request.getParameter("search").toUpperCase() +" in :text) AS tmp where rownum > :from and rownum <= :to order by ";
				
				
				//sqlParam.put("search", request.getParameter("search"));
				if(request.getParameter("search").equals("lname") || request.getParameter("search").equals("vname") || request.getParameter("search").equals("name")){
					sql = "Select * from (select ROW_NUMBER() OVER() AS rownum, SNT.PRED.* from SNT.PRED where "+ /*upper(rtrim(:search))*/request.getParameter("search").toUpperCase() +" like :text) AS tmp where rownum > :from and rownum <= :to order by ";
					sqlParam.put("text","%"+request.getParameter("text")+"%");
					String sql_count_search_all_name = "Select count(*) from (select ROW_NUMBER() OVER() AS rownum, SNT.PRED.* from SNT.PRED where "+ request.getParameter("search").toUpperCase() +" like :text) AS tmp";
					rowCount = getNpjt().queryForInt(sql_count_search_all_name, sqlParam);
				}else{
					sqlParam.put("text", request.getParameter("text"));
					String sql_count_search = "Select count(*) from (select ROW_NUMBER() OVER() AS rownum, SNT.PRED.* from SNT.PRED where "+ request.getParameter("search").toUpperCase() +" in :text) AS tmp";
					rowCount = getNpjt().queryForInt(sql_count_search, sqlParam);
				}
				/*sql_count = "select 1 from SNT.PREDTEST";
				rowCount = getNpjt().queryForInt(sql_count,sqlParam);*/
			}else{
				
				 sql = "Select * from (select ROW_NUMBER() OVER() AS rownum, SNT.PRED.* from SNT.PRED) AS tmp where rownum > :from and rownum <= :to order by ";	
			}
		
		response.put("count", rowCount);

		getNpjt().query(sql+sort+" "+dir, sqlParam,
				new ParameterizedRowMapper<Object>() {

			public Object mapRow(ResultSet rs, int numrow)
					throws SQLException {

				try {
					JSONObject js = new JSONObject();
					js.put("id", rs.getInt("ID"));
					js.put("vname", rs.getString("VNAME"));
					js.put("name", rs.getString("NAME"));
					js.put("okpo_kod", rs.getInt("OKPO_KOD"));
					js.put("lname", rs.getString("LNAME"));
					js.put("cabinet_id", rs.getString("CABINET_ID"));
					js.put("inn", rs.getString("INN"));
					js.put("kpp", rs.getString("KPP"));
					js.put("headid", rs.getInt("HEADID"));
					js.put("contr_code", rs.getString("CONTR_CODE"));
					js.put("priceCheck", rs.getString("PRICECHECK"));
					js.put("pdfCheck", rs.getString("PDFCHECK"));
					
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
		
		String sql = "insert into SNT.pred(id,vname,name,okpo_kod,lname,cabinet_id,inn, kpp, headid, contr_code, pricecheck, pdfcheck) values (COALESCE((select max(id)+1	  from SNT.pred),0),?,?,?,?,?,?,?,?,?,?,?)";
		String cabinet_id = UUID.randomUUID().toString().toUpperCase();
		String headid = request.getParameter("headid");
		Integer priceCheck;
		Integer pdfCheck;
		if (request.getParameter("priceCheck") != null){
			priceCheck = 1;
		}else{
			priceCheck = 0;
		}
		
		if (request.getParameter("pdfCheck") != null){
			pdfCheck = 1;
		}else{
			pdfCheck = 0;
		}

		Object obj = new Object[] {
				request.getParameter("vname"),
				request.getParameter("name"),
				Integer.parseInt(request
						.getParameter("okpo_kod")),
				request.getParameter("lname"),
				cabinet_id,
				request.getParameter("inn"),
				request.getParameter("kpp"),
				headid,
				request.getParameter("contr_code"),
				priceCheck,
				pdfCheck
		};
		
		if(headid == "" || headid == null ){
			sql = "insert into SNT.pred(id,vname,name,okpo_kod,lname,cabinet_id,inn, kpp, contr_code,pricecheck, pdfcheck) values (COALESCE((select max(id)+1	  from SNT.pred),0),?,?,?,?,?,?,?,?,?,?)";
		obj = new Object[] {
				request.getParameter("vname"),
				request.getParameter("name"),
				Integer.parseInt(request
						.getParameter("okpo_kod")),
				request.getParameter("lname"),
				cabinet_id,
				request.getParameter("inn"),
				request.getParameter("kpp"),
				request.getParameter("contr_code"),
				priceCheck,
				pdfCheck
		};
		
		
		}
		
		if(Utils.isReadonly()) return FORBIDDEN;
		JSONObject response = new JSONObject();
		response.put(success, false);
		getSjt().update(sql,obj);
		response.put(success, true);
		return response;
	}

	@Override
	protected JSONObject delete(HttpServletRequest request)
			throws JSONException {
		if(Utils.isReadonly()) return FORBIDDEN;
		JSONObject response = new JSONObject();
		response.put(success, false);

		getSjt()
		.update(
				"delete from SNT.pred where id = ?",
				new Object[] { Integer.parseInt(request
						.getParameter("id")) });

		response.put(success, true);
		return response;
	}

	@Override
	protected JSONObject edit(HttpServletRequest request) throws JSONException {
		if(Utils.isReadonly()) return FORBIDDEN;
		JSONObject response = new JSONObject();
		response.put(success, false);
		Integer id = Integer.parseInt(request.getParameter("id"));
		String vname = request.getParameter("vname");
		String name = request.getParameter("name");
		Integer okpo_kod = Integer.parseInt(request
				.getParameter("okpo_kod"));
		String lname = request.getParameter("lname");
		String cabinet_id = request.getParameter("cabinet_id");
		String inn = request.getParameter("inn");
		String kpp = request.getParameter("kpp");
		Integer headid = Integer.parseInt(request
				.getParameter("headid"));
		Integer priceCheck;
		Integer pdfCheck;
		
		if (request.getParameter("priceCheck") != null){
			priceCheck = 1;
		}else{
			priceCheck = 0;
		}
		
		if (request.getParameter("pdfCheck") != null){
			pdfCheck = 1;
		}else{
			pdfCheck = 0;
		}
		if(headid == 0) headid = null;
		String contr_code = request.getParameter("contr_code");
		getSjt()
		.update(
				"UPDATE SNT.pred SET VNAME=? , NAME=? ,OKPO_KOD=?, LNAME=? , CABINET_ID=?, INN=?, KPP=?, HEADID=?, CONTR_CODE=?, PRICECHECK=?,PDFCHECK=? WHERE ID=?",
				new Object[] {vname, name, okpo_kod, lname, cabinet_id, inn, kpp, headid, 
						contr_code,priceCheck,pdfCheck,id});

		response.put(success, true);
		return response;
	}
}
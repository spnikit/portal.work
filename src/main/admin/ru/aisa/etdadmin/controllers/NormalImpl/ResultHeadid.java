package ru.aisa.etdadmin.controllers.NormalImpl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.jdbc.core.simple.ParameterizedRowMapper;

import ru.aisa.etdadmin.controllers.AbstractNormalController;

public class ResultHeadid extends AbstractNormalController {

	public ResultHeadid() throws JSONException{
		super();
	}
	private boolean isSet = false;


	protected JSONObject get(HttpServletRequest request) throws JSONException {

		final JSONObject response = new JSONObject();
		response.put("data", new JSONArray());

		String sql1;
		
		Map <String, Object> sqlParam = new HashMap<String, Object>();
		

		sql1 =  "select etdid, vagnum, (select rtrim(name) name from snt.doctype where id = ds.typeid) from snt.docstore ds where signlvl is null and typeid in (64,69,70,72) and month(ldate)=:month and predid in (select id from snt.pred where id = :predid union select id from snt.pred where headid = :predid)";
		
		System.out.println(request.getParameter("month"));
		System.out.println(request.getParameter("predid"));
		
		Map <String, Integer> defaultParam = new HashMap<String, Integer>();
		if(request.getParameter("predid")!= null&&request.getParameter("month")!= null){
			isSet = true;
			String str1 =  request.getParameter("predid");
			int value1 = Integer.parseInt(str1);
			String str2 =  request.getParameter("month");
			int value2 = Integer.parseInt(str2);
		defaultParam.put("month", value2);
		defaultParam.put("predid", value1);
//		System.out.println("параметры месяцы: " +defaultParam.get("month"));
//		System.out.println("параметры predid: " +defaultParam.get("predid"));
		}else{
			defaultParam.put("month", 0);
			defaultParam.put("predid", 0);
//			System.out.println("Нули месяцы: " +defaultParam.get("month"));
//			System.out.println("Нули predid: " + defaultParam.get("predid"));
		}
		
		getNpjt().query(sql1/*+sort+" "+dir*/, defaultParam,
				new ParameterizedRowMapper<Object>() {

			public Object mapRow(ResultSet rs, int numrow)
					throws SQLException {

				try {
					JSONObject js = new JSONObject();
					js.put("etdid", rs.getInt("ETDID"));
					js.put("vugnum", rs.getInt("VAGNUM"));
					js.put("name", rs.getString("NAME"));
					response.accumulate("data", js);
				} catch (JSONException e) {
				}
				return null;
			}

		});

		return response;
	}
	
	
	
	

}

package ru.aisa.etdadmin.controllers.NormalImpl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.jdbc.core.simple.ParameterizedRowMapper;

import ru.aisa.etdadmin.controllers.AbstractMultipartController;


public class Headid extends AbstractMultipartController {

	public Headid() throws JSONException{
	super();
	}

	protected JSONObject get(HttpServletRequest request) throws JSONException {
			
		String headidData = request.getParameter("gridData");
		final JSONObject response1 = new JSONObject();
		response1.put("data", new JSONArray());

		
		int start;
		final int limit;
		int from = 0;
		int to = 0;
		if (request.getParameter("start") != null)
			start = Integer.parseInt(request.getParameter("start"));
		else
			start = 0;

		if (request.getParameter("limit") != null)
			limit = Integer.parseInt(request.getParameter("limit"));
		else
			limit = 20;
		
		
		
		Map  defaultParam = new HashMap();
		String sql_count_search_all_name;
		
		String nameColumn = request.getParameter("search");		
		String searchText = request.getParameter("text");
		if(searchText!=null){
			searchText = searchText.trim();
		}
		
		String predid = request.getParameter("predid");
		String dateStart = request.getParameter("dateFrom");
		String dateEnd = request.getParameter("dateTo");
		DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");; 
		Date dateFrom = Calendar.getInstance().getTime() ; 
		Date dateTo = Calendar.getInstance().getTime() ; 
		
			
		if(predid!= null&&dateStart != null&&dateEnd != null){	
			
			int valuePredid = Integer.parseInt(predid); 
		  
			    
			    try {
					dateFrom = formatter.parse(dateStart);
					dateTo = formatter.parse(dateEnd);
				} catch (ParseException e1) {
					
					e1.printStackTrace();
				}
		
			defaultParam.put("predid", valuePredid);
			defaultParam.put("dateFrom", dateFrom);
			defaultParam.put("dateTo", dateTo);
		}else if(predid!= null&&dateStart != null&&dateEnd == null){
			
			int valuePredid = Integer.parseInt(predid); 
			    
			    try {
					dateFrom = formatter.parse(dateStart);
					dateTo = formatter.parse(dateEnd);
					//System.out.println(dateTo.toString());
				} catch (ParseException e1) {
					
					e1.printStackTrace();
				}
		
			defaultParam.put("predid", valuePredid);
			defaultParam.put("dateFrom", dateFrom);
			defaultParam.put("dateTo", dateTo);
		}else{
			defaultParam.put("dateTo", null);
			defaultParam.put("dateFrom", null);
			defaultParam.put("predid", 0);
		}
		
		
		
		
		
		
		String sql_count = "select count(*)"
				+ " from snt.docstore ds where signlvl is null "
				+ "and typeid in (select id from snt.doctype where name in ('ФПУ-26','МХ-1','МХ-3','Акт приема передачи ТМЦ'))"
				+ " and ldate between :dateFrom and :dateTo and predid in (select id from snt.pred where id = :predid or headid = :predid)";
		int rowCount = getNpjt().queryForInt(sql_count, defaultParam);
		
		if(rowCount == 0) return response1;
		
		String sql1 =  "select * from (select ROW_NUMBER() OVER() AS rownum, etdid, id_pak, vagnum, (select rtrim(name) name from snt.doctype where id = ds.typeid), "
				+ "(select rtrim(vname) vname from snt.pred where id = ds.predid),"
				+ "(select repdate from snt.docstore where id_pak = ds.id_pak and typeid = (select id from snt.doctype where name = 'Пакет документов')) "
				+ " from snt.docstore ds where signlvl is null "
				+ "and typeid in (select id from snt.doctype where name in ('ФПУ-26','МХ-1','МХ-3','Акт приема передачи ТМЦ'))"
				+ " and ldate between :dateFrom and :dateTo and predid in (select id from snt.pred where id = :predid or headid = :predid)) as tmp where rownum > :from and rownum <= :to order by ";
		
		if(nameColumn != null&&nameColumn.equals("repdate")){
			sql1 ="select * from (select ROW_NUMBER() OVER() AS rownum, etdid, id_pak,vagnum, (select rtrim(name) name from snt.doctype where id = ds.typeid), "
					+ "(select rtrim(vname) vname from snt.pred where id = ds.predid),"
					+ "(select repdate from snt.docstore where id_pak = ds.id_pak and typeid = (select id from snt.doctype where name = 'Пакет документов'))"
					+ " from snt.docstore ds where signlvl is null "
					+ "and typeid in (select id from snt.doctype where name in ('ФПУ-26','МХ-1','МХ-3','Акт приема передачи ТМЦ')) "
					+ "and (select repdate from snt.docstore "
					+ "where id_pak = ds.id_pak "
					+"and typeid = (select id from snt.doctype where name = 'Пакет документов')) in :text "
					+"and ldate between :dateFrom and :dateTo "
					+ "and predid in (select id from snt.pred where id = :predid or headid = :predid)) as tmp where rownum > :from and rownum <= :to order by "; 
			defaultParam.put("text", searchText);
			defaultParam.put("dateFrom", dateFrom);
			defaultParam.put("dateTo", dateTo);
			sql_count_search_all_name = "select count(*)"
					+ " from snt.docstore ds where signlvl is null "
					+ "and typeid in (select id from snt.doctype where name in ('ФПУ-26','МХ-1','МХ-3','Акт приема передачи ТМЦ')) "
					+ "and (select repdate from snt.docstore "
					+ "where id_pak = ds.id_pak "
					+"and typeid = (select id from snt.doctype where name = 'Пакет документов')) in :text "
					+"and ldate between :dateFrom and :dateTo "
					+ "and predid in (select id from snt.pred where id = :predid or headid = :predid)"; 
			rowCount = getNpjt().queryForInt(sql_count_search_all_name, defaultParam);
			
		}else if(nameColumn != null&&nameColumn.equals("vagnum")){
			sql1 ="select * from (select ROW_NUMBER() OVER() AS rownum, etdid, id_pak,vagnum, (select rtrim(name) name from snt.doctype where id = ds.typeid), "
					+ "(select rtrim(vname) vname from snt.pred where id = ds.predid),"
					+ "(select repdate from snt.docstore where id_pak = ds.id_pak and typeid = (select id from snt.doctype where name = 'Пакет документов'))"
					+ "  from snt.docstore ds where signlvl is null "
					+ "and typeid in (select id from snt.doctype where name in ('ФПУ-26','МХ-1','МХ-3','Акт приема передачи ТМЦ')) "
					+ "and "+request.getParameter("search") + " like :text and ldate between :dateFrom and :dateTo "
							+ "and predid in (select id from snt.pred where id = :predid or headid = :predid)) as tmp where rownum > :from and rownum <= :to order by "; 
			defaultParam.put("text", "%"+searchText+"%");
			defaultParam.put("dateFrom", dateFrom);
			defaultParam.put("dateTo", dateTo);
			sql_count_search_all_name = "select count(*)"
					+ "  from snt.docstore ds where signlvl is null "
					+ "and typeid in (select id from snt.doctype where name in ('ФПУ-26','МХ-1','МХ-3','Акт приема передачи ТМЦ')) "
					+ "and "+request.getParameter("search") + " like :text and ldate between :dateFrom and :dateTo "
							+ "and predid in (select id from snt.pred where id = :predid or headid = :predid)"; 
			rowCount = getNpjt().queryForInt(sql_count_search_all_name, defaultParam);
			
		}else if(nameColumn != null&&nameColumn.equals("name")){
			sql1 ="select * from (select ROW_NUMBER() OVER() AS rownum, etdid, id_pak,vagnum, (select rtrim(name) name from snt.doctype where id = ds.typeid), "
					+ "(select rtrim(vname) vname from snt.pred where id = ds.predid),"
					+ "(select repdate from snt.docstore where id_pak = ds.id_pak and typeid = (select id from snt.doctype where name = 'Пакет документов'))"
					+ "  from snt.docstore ds where signlvl is null "
					+ "and typeid in (select id from snt.doctype where name in ('ФПУ-26','МХ-1','МХ-3','Акт приема передачи ТМЦ')) "
					+ "and typeid in (select id from snt.doctype where name like :text) and ldate between :dateFrom and :dateTo "
							+ "and predid in (select id from snt.pred where id = :predid or headid = :predid)) as tmp where rownum > :from and rownum <= :to order by "; 
			defaultParam.put("text", "%"+searchText+"%");
			defaultParam.put("dateFrom", dateFrom);
			defaultParam.put("dateTo", dateTo);
			
			sql_count_search_all_name = "select count(*)"
					+ "  from snt.docstore ds where signlvl is null "
					+ "and typeid in (select id from snt.doctype where name in ('ФПУ-26','МХ-1','МХ-3','Акт приема передачи ТМЦ')) "
					+ "and typeid in (select id from snt.doctype where name like :text) and ldate between :dateFrom and :dateTo "
							+ "and predid in (select id from snt.pred where id = :predid or headid = :predid)"; 
			rowCount = getNpjt().queryForInt(sql_count_search_all_name, defaultParam);
			
		}else if(nameColumn != null&&nameColumn.equals("vname")){
			sql1 ="select * from (select ROW_NUMBER() OVER() AS rownum, etdid, id_pak,vagnum, (select rtrim(name) name from snt.doctype where id = ds.typeid), "
					+ "(select rtrim(vname) vname from snt.pred where id = ds.predid),"
					+ "(select repdate from snt.docstore where id_pak = ds.id_pak and typeid = (select id from snt.doctype where name = 'Пакет документов'))"
					+ "  from snt.docstore ds where signlvl is null "
					+ "and typeid in (select id from snt.doctype where name in ('ФПУ-26','МХ-1','МХ-3','Акт приема передачи ТМЦ')) "
					+ "and predid in (select id from snt.pred where vname like :text) and ldate between :dateFrom and :dateTo "
					+ "and predid in (select id from snt.pred where id = :predid or headid = :predid)) as tmp where rownum > :from and rownum <= :to order by "; 
			defaultParam.put("text", "%"+searchText+"%");
			defaultParam.put("dateFrom", dateFrom);
			defaultParam.put("dateTo", dateTo);
			
			sql_count_search_all_name ="select count(*)"
					+ "  from snt.docstore ds where signlvl is null "
					+ "and typeid in (select id from snt.doctype where name in ('ФПУ-26','МХ-1','МХ-3','Акт приема передачи ТМЦ')) "
					+ "and predid in (select id from snt.pred where vname like :text) and ldate between :dateFrom and :dateTo "
					+ "and predid in (select id from snt.pred where id = :predid or headid = :predid)"; 
			rowCount = getNpjt().queryForInt(sql_count_search_all_name, defaultParam);
			
		}else if(nameColumn != null&&searchText != null){
			sql1 ="select * from (select  ROW_NUMBER() OVER() AS rownum, etdid, id_pak,vagnum, (select rtrim(name) name from snt.doctype where id = ds.typeid), "
					+ "(select rtrim(vname) vname from snt.pred where id = ds.predid),"
					+ "(select repdate from snt.docstore where id_pak = ds.id_pak and typeid = (select id from snt.doctype where name = 'Пакет документов'))"
					+ "  from snt.docstore ds where signlvl is null "
					+ "and typeid in (select id from snt.doctype where name in ('ФПУ-26','МХ-1','МХ-3','Акт приема передачи ТМЦ')) "
					+ "and "+request.getParameter("search") + " in :text and ldate between :dateFrom and :dateTo "
							+ "and predid in (select id from snt.pred where id = :predid or headid = :predid)) as tmp where rownum > :from and rownum <= :to order by "; 
			defaultParam.put("text", searchText);
			defaultParam.put("dateFrom", dateFrom);
			defaultParam.put("dateTo", dateTo);
			
			sql_count_search_all_name ="select count(*)"
					+ "  from snt.docstore ds where signlvl is null "
					+ "and typeid in (select id from snt.doctype where name in ('ФПУ-26','МХ-1','МХ-3','Акт приема передачи ТМЦ')) "
					+ "and "+request.getParameter("search") + " in :text and ldate between :dateFrom and :dateTo "
							+ "and predid in (select id from snt.pred where id = :predid or headid = :predid)"; 
			rowCount = getNpjt().queryForInt(sql_count_search_all_name, defaultParam);
			
		}/*else{
			sql1 =  "select * from (select ROW_NUMBER() OVER() AS rownum, etdid, id_pak, vagnum, (select rtrim(name) name from snt.doctype where id = ds.typeid), "
					+ "(select rtrim(vname) vname from snt.pred where id = ds.predid),"
					+ "(select repdate from snt.docstore where id_pak = ds.id_pak and typeid = (select id from snt.doctype where name = 'Пакет документов')) "
					+ " from snt.docstore ds where signlvl is null "
					+ "and typeid in (select id from snt.doctype where name in ('ФПУ-26','МХ-1','МХ-3','Акт приема передачи ТМЦ'))"
					+ " and ldate between :dateFrom and :dateTo and predid in (select id from snt.pred where id = :predid or headid = :predid)) as tmp where  rownum > :from and rownum <= :to order by ";
		}*/
		
		
		String sort = request.getParameter("sort");
		
		
		if (sort == null
				|| !(sort.equalsIgnoreCase("vagnum")
				||sort.equalsIgnoreCase("name")
				||sort.equalsIgnoreCase("vname")
				||sort.equalsIgnoreCase("repdate"))){
			sort = "etdid";
		}
		String dir = request.getParameter("dir");
		if (dir == null || !dir.equalsIgnoreCase("desc")){
			dir = "asc";	
			from = start;
			to = start+limit;
		}
		if(dir.equalsIgnoreCase("desc")){
			from = rowCount - limit - start;
			to = rowCount - start;
		}
		
		defaultParam.put("from", from);
		defaultParam.put("to", to);
	
		response1.put("count", rowCount);
		
		//System.out.println(dateFrom);
		//System.out.println(dateTo);	
		
		getNpjt().query(sql1+sort+" "+dir, defaultParam,
				new ParameterizedRowMapper<Object>() {

			public Object mapRow(ResultSet rs, int numrow)
					throws SQLException {

				try {
					JSONObject js = new JSONObject();
					js.put("etdid", rs.getInt("ETDID"));
					js.put("id_pak", rs.getString("ID_PAK"));
					js.put("vagnum", rs.getString("VAGNUM"));
					js.put("name", rs.getString("NAME"));
					js.put("vname", rs.getString("VNAME"));
					js.put("repdate", rs.getString("REPDATE"));
					response1.accumulate("data", js);
				} catch (JSONException e) {
					e.printStackTrace();
				}
				return null;
			}

		});
	//}
		
		return response1;
	}
}

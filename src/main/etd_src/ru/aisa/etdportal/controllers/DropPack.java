package ru.aisa.etdportal.controllers;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import javax.servlet.http.HttpServletRequest;
import org.json.JSONArray;
import org.json.JSONException;
import org.springframework.jdbc.core.simple.ParameterizedRowMapper;
import sheff.rjd.ws.OCO.TORLists;

public class DropPack extends AbstractPortalSimpleController {

	public DropPack() throws JSONException {
		super();
	}

	private static final String sql  = "select visible, signlvl, " +
			"(select rtrim(name) name from snt.doctype where id= ds.typeid), droptxt, stat, case when dropid is null then 0 else 1 end as drop from snt.docstore ds "+
" where etdid in (select etdid from snt.packages where id_pak = (select id_pak from snt.docstore where id =:id)) and signlvl is not null";

	
	private static final String blobsql = "select bldoc from snt.docstore where id =:id";	
	
	@Override
	public JSONArray get(HttpServletRequest request){
		
//		System.out.println("deop");
		final JSONArray response = new JSONArray();
		HashMap<String, Object> pp = new HashMap<String, Object>();
		if (request.getParameter("docid")!=null){
//		System.out.println(request.getParameter("docid"));
			
			try{
//				System.out.println("docid: "+request.getParameter("docid"));
			pp.put("id",Integer.parseInt(request.getParameter("docid")));
		
		
		getNpjt().query(sql,pp, new ParameterizedRowMapper<Object>() {

			public Object mapRow(ResultSet rs, int numrow) throws SQLException {
				
				JSONArray js = new JSONArray();
				if (!TORLists.autosign.contains(rs.getString("name"))){
				//	System.out.println(rs.getString("name"));
				if (TORLists.acceptlist.contains(rs.getString("name"))&&rs.getInt("visible")==0){ 
					//System.out.println("if");
				js.put(rs.getInt("visible"));
				js.put(rs.getString("name"));
				js.put(rs.getInt("signlvl"));
				response.put(js);
				}
				
				else if (rs.getString("name").equals("РДВ")&&rs.getInt("signlvl")>0){
					
					if (rs.getInt("drop")==0){
					js.put(rs.getInt("visible"));
					js.put(rs.getString("name"));
					js.put(rs.getInt("signlvl"));
					response.put(js);
					}
				}
				
				}
			//	System.out.println("js "+js);
				return null;
			}});
		
			} catch (Exception e) {
				
				e.printStackTrace();
				}
	//	System.out.println(response);
		}
		if (response.length()>0)
			response.put(false);
		else response.put(true);
			//System.out.println(response);
		return response;
	}
	
	
}
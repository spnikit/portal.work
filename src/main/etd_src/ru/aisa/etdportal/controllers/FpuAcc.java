package ru.aisa.etdportal.controllers;


import java.util.HashMap;
import javax.servlet.http.HttpServletRequest;
import org.json.JSONArray;
import org.json.JSONException;


public class FpuAcc extends AbstractPortalSimpleController {

	public FpuAcc() throws JSONException {
		super();
	}

//	private static final String sql  = "select count(0) from snt.docstore where id_pak = " +
//			"(select id_pak from snt.docstore where id = :id) "
//			+ "and signlvl is not null and dropid is null and id!= :id with ur";
	
	
	private static final String sql = "select count(0) from snt.docstore where etdid in "+
			" (select etdid from snt.packages where id_pak = (select id_pak from snt.docstore where id = :packid) "+
			 "and etdid!= (select etdid from snt.docstore where id = :id)) "+
			" and signlvl is not null and dropid is null with ur";
	
	
	@Override
	public JSONArray get(HttpServletRequest request){
		
		final JSONArray response = new JSONArray();
		HashMap<String, Object> pp = new HashMap<String, Object>();
		if (request.getParameter("docid")!=null&&request.getParameter("packid")!=null){
//		System.out.println(request.getParameter("docid"));
//			System.out.println(request.getParameter("packid"));
			try{
				Long docid = Long.parseLong(request.getParameter("docid"));
				Long packid = Long.parseLong(request.getParameter("packid"));
				
//				System.out.println(docid+" "+packid);
				
				
				
			pp.put("id",docid);
	pp.put("packid", packid);
			int count = getNpjt().queryForInt(sql, pp);
			
			if (count>0)
				response.put(false);
			else if (count==0)
					response.put(true);
			
			} catch (Exception e) {
				
				e.printStackTrace();
				response.put(false);
				}
	
		}
//		System.out.println(response);
		return response;
	}
	
	
}
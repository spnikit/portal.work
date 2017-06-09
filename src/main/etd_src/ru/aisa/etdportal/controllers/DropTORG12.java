package ru.aisa.etdportal.controllers;


import java.util.HashMap;
import javax.servlet.http.HttpServletRequest;
import org.json.JSONArray;
import org.json.JSONException;


public class DropTORG12 extends AbstractPortalSimpleController {

	public DropTORG12() throws JSONException {
		super();
	}

	private static final String updtread = "update snt.docstore set readid = -1, droptxt =:reason, visible = 0, reqnum = 'Отклонен' where id =:id";	
	
	@Override
	public JSONArray get(HttpServletRequest request){
		
		final JSONArray response = new JSONArray();
		HashMap<String, Object> pp = new HashMap<String, Object>();
		if (request.getParameter("docid")!=null&&request.getParameter("reason")!=null){

			try{
				Long docid = Long.parseLong(request.getParameter("docid"));
			pp.put("id",docid);
			pp.put("reason", request.getParameter("reason"));
		
				getNpjt().update(updtread, pp);
			response.put(true);
			
		
		
			} catch (Exception e) {
				
				e.printStackTrace();
				response.put(false);
				}
	
		}
		
		return response;
	}
	
	
}
package ru.aisa.etdportal.controllers;


import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

import org.json.JSONArray;
import org.json.JSONException;

import ru.aisa.rgd.ws.documents.ETDForm;
import ru.aisa.rgd.ws.utility.DataBinder;
import ru.aisa.rgd.ws.utility.TypeConverter;


public class DropFPU26PPS extends AbstractPortalSimpleController {

	public DropFPU26PPS() throws JSONException {
		super();
	}

	private static final String updtread = "update snt.docstore set reqnum =:reqnum, droptxt =:reason, readid = 1 where id =:id";	
	private static final String getfpu26bytes = "select bldoc from snt.docstore where id =:id";	
	private static final String updtreestr = "update snt.docstore set reqnum =:reqnum where etdreestrid =:etdreestrid";	
	@Override
	public JSONArray get(HttpServletRequest request){
		
		final JSONArray response = new JSONArray();
		HashMap<String, Object> pp = new HashMap<String, Object>();
		if (request.getParameter("docid")!=null&&request.getParameter("reason")!=null){
			try{
				Long docid = Long.parseLong(request.getParameter("docid"));
			pp.put("id",docid);
			pp.put("reason", request.getParameter("reason"));
			pp.put("reqnum", "Забраковано");
				getNpjt().update(updtread, pp);
			
				try{
					byte[] fpu26form = getNpjt().queryForObject(getfpu26bytes, pp, byte[].class);
					
					ETDForm form = ETDForm.createFromArchive(fpu26form);
					DataBinder db = form.getBinder();					
					long etdreestrid = Long.parseLong(db.getValue("P_46"));
					pp.put("etdreestrid", etdreestrid);
					getNpjt().update(updtreestr, pp);
					
				} catch (Exception e){
					log.error(TypeConverter.exceptionToString(e));
				}
				
				response.put(true);
			
			} catch (Exception e) {
				
				e.printStackTrace();
				response.put(false);
				}
	
		}
		
		return response;
	}
	
	
}
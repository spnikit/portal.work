package ru.aisa.etdportal.controllers;


import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

import org.json.JSONArray;
import org.json.JSONException;


public class FirstPack extends AbstractPortalSimpleController {

	public FirstPack() throws JSONException {
		super();
	}

	private static final String sql  = "select readid from snt.docstore where id = :id with ur";
	private static final String updtread = "update snt.docstore set readid = 1 where id =:id";	
	
	@Override
	public JSONArray get(HttpServletRequest request){
		
		final JSONArray response = new JSONArray();
		HashMap<String, Object> pp = new HashMap<String, Object>();
		if (request.getParameter("docid")!=null){

			try{
				Long docid = Long.parseLong(request.getParameter("docid"));
			pp.put("id",docid);
		
		int readid = getNpjt().queryForInt(sql, pp);
		
		if (readid==-1){
			
			boolean succ = getSendtoetd().SendOpenPackToEtd(docid, true, 1);
			
			if (succ){
				getNpjt().update(updtread, pp);
				//отчет п.4
				String selectEtdid = "select etdid from snt.docstore where id = :id";
				Long etdId = getNpjt().queryForObject(selectEtdid, pp, Long.class);
				pp.put("id_pak", etdId);
			    Date currentDate = new Date();
			    SimpleDateFormat myDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			    currentDate = myDateFormat.parse(myDateFormat.format(currentDate));
				pp.put("DT_open_pak", currentDate);
				String update = "update snt.PGKREPORT set DT_open_pak = :DT_open_pak"
						+ " where id_pak = :id_pak";
				getNpjt().update(update, pp);
				
			    response.put(true);
			}
			else response.put(false);
			
			
		}
		else response.put(true);
		
			} catch (Exception e) {
				
				e.printStackTrace();
				}
	
		}
		
		return response;
	}
	
	
}
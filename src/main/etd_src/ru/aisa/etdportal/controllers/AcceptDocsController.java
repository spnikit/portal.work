package ru.aisa.etdportal.controllers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;

import ru.aisa.etdadmin.controllers.AbstractMultipartController;
import ru.aisa.rgd.ws.utility.TypeConverter;
import sheff.rjd.ws.OCO.TORLists;

public class AcceptDocsController extends AbstractMultipartController {

	public AcceptDocsController() throws JSONException {
		super();
	}

	@Override
	protected JSONObject get(HttpServletRequest request) throws JSONException {
		JSONObject obj = new JSONObject();
		int numberOfDoc = 0;
		int numberOfPack = 0;
		try{
			String[] idPakArray = request.getParameter("idPak").split(";");
			Map<String, Object> map = new HashMap<String, Object>();
			MapSqlParameterSource parameters = new MapSqlParameterSource();
			List<Long> list = new ArrayList<Long>();
			String selectDocsInPackage = "select id from snt.docstore where etdid in "
					+ "(select etdid from snt.packages where id_pak = :id_pak) and dropid is null "
					+ "and ldate is null";
			for(int j = 0; j < idPakArray.length; j++) {
        		numberOfPack++;
				map.put("id_pak", idPakArray[j]);
				List<Long> listId = getNpjt().queryForList(selectDocsInPackage, map, Long.class);
        		for(int i = 0; i < listId.size(); i++) {
    				Long id = listId.get(i);
    		  	    map.put("id", id); 
    		  	    String formname = getNpjt().queryForObject("select rtrim(name) from snt.doctype where id = "
    		  	    		+ "(select typeid from snt.docstore where id = :id)", map, String.class);
    		  	    if(TORLists.acceptlist.contains(formname)) {
    					list.add(id);
    		  	    	numberOfDoc++;
    		  	    }
    			}
			}
			parameters.addValue("listId", list);
			String upd = "update snt.docstore set visible = 2 where id in (:listId)";
			getNpjt().update(upd, parameters);
		} catch(Exception e) {
			log.error(TypeConverter.exceptionToString(e));
		}
	    obj.put("numberOfDocs", numberOfDoc);
	    obj.put("numberOfPack", numberOfPack);
		return obj;
	}
}
package ru.aisa.etdportal.controllers;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import ru.aisa.etdadmin.controllers.AbstractMultipartController;

public class GetDocumentsForPrintController extends AbstractMultipartController {

	public GetDocumentsForPrintController() throws JSONException {
		super();
	}

	@Override
	protected JSONObject get(HttpServletRequest request) throws JSONException{
		JSONObject jsonObj = new JSONObject();
		jsonObj.put("data", new JSONArray());
		try{
			String[] idPakArray = request.getParameter("idPak").split(";");
			
			for(int j = 0; j < idPakArray.length; j++) {
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("id_pak", idPakArray[j]);
				String selectPack = "select id, vagnum from snt.docstore where id_pak = :id_pak and visible = 1";
				
				String selectDocs = "with main as (select etdid from snt.packages where id_pak = :id_pak)," + 
						"t1 as (select id, typeid from snt.docstore where etdid in (select etdid from main))" + 
						"select t1.id, rtrim(name) name from snt.doctype t3, t1 where t3.id = t1.typeid";
				SqlRowSet rsPack = getNpjt().queryForRowSet(selectPack, map);
				Long idPack = null;
				String vagnum = null;
				while(rsPack.next()) {
					idPack = rsPack.getLong("id");
					vagnum = rsPack.getString("vagnum");
				}
				
				SqlRowSet rsDocs = getNpjt().queryForRowSet(selectDocs, map); 
				Long id = null;
				String formname = null;
				while(rsDocs.next()) {
					if(rsDocs.isFirst()) {
						AddInTempJSONObject(idPack, jsonObj, idPakArray[j], vagnum, "Пакет документов");
					}
					id = rsDocs.getLong("id");
					formname = rsDocs.getString("name");
					AddInTempJSONObject(id, jsonObj, idPakArray[j], vagnum, formname);
				}
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
		return jsonObj;
	}
	
	private void AddInTempJSONObject(Long id, JSONObject obj, String id_pack, 
			String vagnum, String formname) throws Exception {
		JSONObject objTemp = new JSONObject();
	    objTemp.put("id_doc", id.toString());
	    objTemp.put("formname", formname);
	    objTemp.put("vagnum", vagnum);
	    objTemp.put("id_pak", id_pack);
	    obj.accumulate("data", objTemp);
	}
	
}
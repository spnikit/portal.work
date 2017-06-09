package ru.aisa.etdportal.controllers;

import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

import org.json.JSONArray;
import org.json.JSONException;

import ru.aisa.rgd.ws.utility.TypeConverter;


public class SignPackRTK extends AbstractPortalSimpleController {

	public SignPackRTK() throws JSONException {
		super();
	}

	private static final String sql = "select count(0) from snt.docstore where visible = 0 "
			+ "and id_pak = (select id_pak from snt.docstore where id =:id) and signlvl=0";
	
	private static final String checkdocs = "select case when (select count(id) from snt.docstore where id_pak = (select id_pak from snt.docstore where id =:id) "+
" and typeid in (select id from snt.doctype where name = :name))=1 then 0 else 1 end from snt.docstore where id =:id";
	private static final String checkdocs2 = "select case when (select count(id) from snt.docstore where id_pak = (select id_pak from snt.docstore where id =:id) "+
			" and typeid in (select id from snt.doctype where name = :name2))=1 then 0 else 1 end from snt.docstore where id =:id";

	@Override
	public JSONArray get(HttpServletRequest request) {
		final JSONArray response = new JSONArray();
		HashMap<String, Object> pp = new HashMap<String, Object>();
		if (request.getParameter("docid") != null) {
			try {
				pp.put("id", Integer.parseInt(request.getParameter("docid")));

				int count = getNpjt().queryForInt(sql, pp);
				if (count>0){
					response.put(false);
				}
				//Убрать чтобы не было проверки на наличие сф
				
				
//				else {
//					pp.put("name", "Счет-фактура РТК");
//					pp.put("name2", "Корректировочный счет-фактура РТК");
//					int count2 = getNpjt().queryForInt(checkdocs, pp);
//					
//					if (count2==1){
//						
//						int count3 = getNpjt().queryForInt(checkdocs2, pp);
//												
//						if (count3==1){
//						response.put(false);
//						}
//						else {
//							response.put(true);
//							}
//						}
//					else 
//					response.put(true);
//				}
				
				else {
					response.put(true);
				}
			

			} catch (Exception e) {
				log.error(TypeConverter.exceptionToString(e));
			}
		}
	
		return response;
	}

}
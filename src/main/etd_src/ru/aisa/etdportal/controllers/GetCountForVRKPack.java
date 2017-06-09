package ru.aisa.etdportal.controllers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.xmlbeans.impl.jam.JSourcePosition;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class GetCountForVRKPack extends AbstractPortalSimpleController {

	public GetCountForVRKPack() throws JSONException {
		super();
	}

	private static final String sql  = "select docid, (select rtrim(name) name from snt.doctype where id = (select typeid from snt.docstore where id = ds.docid)) from snt.vrkids ds where packid = :id_pak";

	@Override
	public JSONArray get(HttpServletRequest request){
		final JSONArray response = new JSONArray();
		HashMap<String, Object> pp = new HashMap<String, Object>();
		if(request.getParameter("id") != null) {
			pp.put("id",request.getParameter("id"));
			Integer countSign = getNpjt().queryForInt("select count(0) from snt.vrkdocflow where docid = :id", pp);
			if(countSign > 0) {
				JSONObject success = new JSONObject();
				try {
					success.put("success", false);
					response.put(success);
					return response;
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		}
		if (request.getParameter("idPak")!=null) {
			try {
				pp.put("id_pak",request.getParameter("idPak"));
				List<Map<String, Object>> docids = getNpjt().queryForList(sql, pp);
				JSONArray ids = new JSONArray();
				for (int i=0;i<docids.size();i++){
					JSONObject doc = new JSONObject();
					doc.put("docid", docids.get(i).get("DOCID"));
					doc.put("name", docids.get(i).get("NAME"));
					ids.put(doc);

				}
				JSONObject success = new JSONObject();
				success.put("success", true);
				response.put(success);
				JSONObject idsarray = new JSONObject();
				idsarray.put("docs", ids);
				response.put(idsarray);
			} catch (Exception e) {
				e.printStackTrace();
				JSONObject success = new JSONObject();
				try {
					success.put("success", false);
				} catch (JSONException e1) {
					e1.printStackTrace();
				}
				response.put(success);
			}
		}
	
//		System.out.println(response);
		return response;
	}
	
	
}
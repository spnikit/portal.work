package ru.aisa.etdportal.controllers;


import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import sheff.rgd.ws.Controllers.RTK.PfmCnsiCodeConvertionTable;


public class MainPack extends AbstractPortalSimpleController {

	public MainPack() throws JSONException {
		super();
	}

	private static final String sql  = "select torpackid from snt.docstore where id = :id";
	private static final String sql2  = "select id from snt.docstore where etdid = :torid";
	@Override
	public JSONArray get(HttpServletRequest request){
		final JSONArray response = new JSONArray();
		HashMap<String, Object> pp = new HashMap<String, Object>();
		if (request.getParameter("id")!=null){
			try{
//				System.out.println(request.getParameter("id"));
			pp.put("id",Long.parseLong(request.getParameter("id")));
		
		
		long torid= (Long)getNpjt().queryForLong(sql, pp);
		
		
		pp.put("torid",torid);
		
		long docid = getNpjt().queryForLong(sql2, pp);
		
		JSONObject js = new JSONObject();
		js.put("docid", docid);
		js.put("success", true);
		response.put(js);
		
			} catch (Exception e) {
				e.printStackTrace();
				JSONObject js = new JSONObject();
				try {
					js.put(success, false);
				} catch (JSONException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				response.put(js);
			}
		}
	
//		PfmCnsiCodeConvertionTable p = new PfmCnsiCodeConvertionTable();
//		p.insertintotable(getNpjt());
		
	
		return response;
	}
	
	
}
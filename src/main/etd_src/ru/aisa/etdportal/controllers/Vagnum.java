package ru.aisa.etdportal.controllers;

import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Vagnum extends AbstractPortalSimpleController {

	public Vagnum() throws JSONException {
		super();
	}

	private static final String sql = "select rtrim(vagnum) vagnum from snt.docstore where id =:docid";
	private static final String sql2 = "select repdate from snt.docstore where id =:docid";
	@Override
	public JSONArray get(HttpServletRequest request) {
		final JSONArray js = new JSONArray();
		HashMap<String, Object> pp = new HashMap<String, Object>();
		if (request.getParameter("docid") != null) {
			JSONObject rr = new JSONObject();
			try {
				pp.put("docid", Long.parseLong(request.getParameter("docid")));

				
				String vagnum = (String) getNpjt().queryForObject(sql, pp, String.class);
				String repdate = (String) getNpjt().queryForObject(sql2, pp, String.class);
				rr.put("vagnum", vagnum);
				rr.put("repdate", repdate);
				rr.put("success", true);
				js.put(rr);

			} catch (Exception e) {
				try {
					rr.put("success", false);
					js.put(rr);
				} catch (JSONException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				e.printStackTrace();
			}
		}

		return js;
	}

}
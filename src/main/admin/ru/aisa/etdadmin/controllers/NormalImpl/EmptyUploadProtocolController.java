package ru.aisa.etdadmin.controllers.NormalImpl;

import javax.servlet.http.HttpServletRequest;

import org.json.JSONException;
import org.json.JSONObject;

import ru.aisa.etdadmin.controllers.AbstractMultipartController;

public class EmptyUploadProtocolController  extends AbstractMultipartController {

	public EmptyUploadProtocolController() throws JSONException{
		super();
	}

	protected JSONObject get(HttpServletRequest request) throws JSONException {	
		JSONObject response = new JSONObject();	
		JSONObject js = new JSONObject();
		js.put("etdid", 1);
		response.accumulate("data", js);
		return response;
	}
	
}

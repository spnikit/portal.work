package ru.aisa.etdadmin.controllers;

import java.io.PrintWriter;
import java.io.StringWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.web.servlet.ModelAndView;

import ru.aisa.etdadmin.controllers.AbstractContr;

public abstract class AbstractSimpleController extends AbstractContr {

	
	public AbstractSimpleController() throws JSONException {
		super();
	}

	@Override
	protected ModelAndView do_action(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		response.setCharacterEncoding("UTF-8");
		response.setContentType("text/plain");
		try{
		get(request).write(response.getWriter());
		}catch (Exception e) {
			JSONObject json = new JSONObject();
			json.put("description", e.getLocalizedMessage());
			json.write(response.getWriter());
			StringWriter outError = new StringWriter();
			PrintWriter errorWriter = new PrintWriter(outError);
			e.printStackTrace(errorWriter);
			log.error(outError.toString());
		}
		return null;
	}
	
	public abstract JSONArray get(HttpServletRequest request);

}

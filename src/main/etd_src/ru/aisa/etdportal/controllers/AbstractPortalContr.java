package ru.aisa.etdportal.controllers;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import org.apache.log4j.Logger;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcTemplate;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;

import sheff.rjd.ws.OCO.AfterSign.SendToEtd;

public abstract class AbstractPortalContr extends AbstractController {

	protected final Logger log = Logger.getLogger(this.getClass());
			
	protected static final String success = "success";

	protected static final String description = "description";

	//private TransactionTemplate transTemplate;
	   
	private DataSource ds;
	
	private NamedParameterJdbcTemplate npjt;

	private SimpleJdbcTemplate sjt;
	
	private SendToEtd sendtoetd;
	
	protected final JSONObject FORBIDDEN;
	protected final JSONObject NOT_IMPLEMENTED;
	public AbstractPortalContr() throws JSONException{
		NOT_IMPLEMENTED=(new JSONObject()).put(success, false).put(description, "Not implemented");
		FORBIDDEN=(new JSONObject()).put(success, false).put(description, "Forbidden");
	}
	@Override
	protected ModelAndView handleRequestInternal(HttpServletRequest arg0,
			HttpServletResponse arg1) throws Exception {
		return do_action(arg0,arg1);
		//return null;
	}
	protected void export(HttpServletRequest request, HttpServletResponse response) throws IOException{
		try {
			NOT_IMPLEMENTED.write(response.getWriter());
		} catch (JSONException e) {
			StringWriter outError = new StringWriter();
			PrintWriter errorWriter = new PrintWriter(outError);
			e.printStackTrace(errorWriter);
			log.error(outError.toString());
		}
	}
	protected abstract ModelAndView do_action(HttpServletRequest arg0,
			HttpServletResponse arg1) throws Exception;

	public DataSource getDs() {
		return ds;
	}

	public void setDs(DataSource ds) {
		this.ds = ds;
	}

	public NamedParameterJdbcTemplate getNpjt() {
		return npjt;
	}
	
	public void setNpjt(NamedParameterJdbcTemplate npjt) {
		this.npjt = npjt;
	}
	public SimpleJdbcTemplate getSjt() {
		return sjt;
	}
	public void setSjt(SimpleJdbcTemplate sjt) {
		this.sjt = sjt;
	}


	public SendToEtd getSendtoetd() {
		return sendtoetd;
	}
	public void setSendtoetd(SendToEtd sendtoetd) {
		this.sendtoetd = sendtoetd;
	}
	/*public TransactionTemplate getTransTemplate() {
		return transTemplate;
	}


	public void setTransTemplate(TransactionTemplate transTemplate) {
		this.transTemplate = transTemplate;
	}*/
	

}

package ru.aisa.etdadmin.controllers;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import org.apache.log4j.Logger;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcTemplate;
import org.springframework.transaction.support.TransactionTemplate;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;

import ru.aisa.rgd.etd.dao.ETDDocumentDao;
import ru.aisa.rgd.etd.dao.ETDUserDao;

public abstract class AbstractContr extends AbstractController {

	protected final Logger log = Logger.getLogger(getClass());
			
	protected static final String success = "success";

	protected static final String description = "description";

	private DataSource ds;
	
	private NamedParameterJdbcTemplate npjt;
	private TransactionTemplate transT;

	private SimpleJdbcTemplate sjt;

	protected final JSONObject FORBIDDEN;
	protected final JSONObject NOT_IMPLEMENTED;
	public AbstractContr() throws JSONException{
		NOT_IMPLEMENTED=(new JSONObject()).put(success, false).put(description, "Not implemented");
		FORBIDDEN=(new JSONObject()).put(success, false).put(description, "Forbidden");
	}
	@Override
	protected ModelAndView handleRequestInternal(HttpServletRequest arg0,
			HttpServletResponse arg1) throws Exception {
		return do_action(arg0,arg1);
		//return null;
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

	
	public TransactionTemplate getTransT() {
		return transT;
	}
	public void setTransT(TransactionTemplate transT) {
		this.transT = transT;
	}
	
	public SimpleJdbcTemplate getSjt() {
		return sjt;
	}

	public void setSjt(SimpleJdbcTemplate sjt) {
		this.sjt = sjt;
	}
	
	private ETDUserDao userDao;
	
	private ETDUserDao getUserDao(){
		return userDao;
	}
	
	private void setUserDao(ETDUserDao userDao){
		this.userDao = userDao;
	}
	
	
	
	private ETDDocumentDao documentDao;
	
	public ETDDocumentDao getDocumentDao(){
		return documentDao;
	}
	
	public void setDocumentDao(ETDDocumentDao documentDao){
		this.documentDao = documentDao;
	}
	

}

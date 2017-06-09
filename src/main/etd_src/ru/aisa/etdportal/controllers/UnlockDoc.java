package ru.aisa.etdportal.controllers;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.math.BigInteger;
import java.sql.Types;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.sql.DataSource;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.jdbc.core.SqlOutParameter;
import org.springframework.jdbc.core.SqlParameter;
import sheff.rjd.utils.MyStoredProc;


public class UnlockDoc extends AbstractPortalSimpleController {

	public UnlockDoc() throws JSONException {
		super();
	}
	private DataSource ds;
	private Unlockdoc proc;
	
	public void setDs(DataSource ds) {
		this.ds = ds;
	}

	public DataSource getDs() {
		return ds;
	}
	
	public Unlockdoc getProc() {
		return proc;
	}

	public void setProc(Unlockdoc proc) {
		this.proc = proc;
	}

	@Override
	public JSONArray get(HttpServletRequest request){
		
		Long startrequest = System.currentTimeMillis();
		
		JSONArray response = new JSONArray();
		HashMap<String, Object> pp = new HashMap<String, Object>();
		if (request.getParameter("docid")!=null&&request.getParameter("cert")!=null){
		pp.put("CERTSERIAL", new BigInteger(request.getParameter("cert"),16).toString());
		
		try {	
			
			Long tryreq = System.currentTimeMillis()-startrequest;
			
			
			int userid = getNpjt().queryForInt("select id from snt.personall where CERTSERIAL = :CERTSERIAL ", pp);
			
			Long getuser = System.currentTimeMillis()-tryreq;
			
			long docid = Long.parseLong(request.getParameter("docid"));
			proc.execute(docid, userid);
			
			Long getdocid = System.currentTimeMillis()-getuser;
			
			JSONObject one = new JSONObject();
			one.put(success, true);
			response.put(one);
			
			
			
		}catch (Exception e) {
			StringWriter outError = new StringWriter();
			PrintWriter errorWriter = new PrintWriter( outError );
			e.printStackTrace(errorWriter);
			log.error(outError.toString());
		//	System.out.println(outError.toString());
			JSONObject one = new JSONObject();
			try {
				one.put(success, false);
			} catch (JSONException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			response.put(one);
		}
		
		}
		//System.out.println(response);
		return response;
	}
	
	
}
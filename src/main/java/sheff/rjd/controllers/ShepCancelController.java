package sheff.rjd.controllers;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.math.BigInteger;
import java.sql.Types;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.log4j.Logger;
import org.springframework.jdbc.core.SqlOutParameter;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;
import org.springframework.ws.client.core.WebServiceTemplate;
import org.springframework.ws.soap.client.core.SoapActionCallback;

import ru.aisa.edt.ReleaseDocDocument;
import ru.aisa.rgd.etd.extend.SNMPSender;
import sheff.rjd.utils.MyStoredProc;

public class ShepCancelController extends AbstractController {
	private NamedParameterJdbcTemplate npjt;  
	private DataSource ds;
	String URL;
	WebServiceTemplate wst;
	private static Logger	log	= Logger.getLogger(ShepCancelController.class);
	
	public String getURL() {
		return URL;
	}

	public void setURL(String url) {
		URL = url;
	}
	
	public void setNpjt(NamedParameterJdbcTemplate npjt) {
		this.npjt = npjt;
	}

	public NamedParameterJdbcTemplate getNpjt() {
		return npjt;
	}

	public void setDs(DataSource ds) {
		this.ds = ds;
	}

	public DataSource getDs() {
		return ds;
	}
	
	public WebServiceTemplate getWst() {
		return wst;
	}

	public void setWst(WebServiceTemplate wst) {
		this.wst = wst;
	}

	
	

	public ShepCancelController() {
		}
	
	
	
	
		
	protected ModelAndView handleRequestInternal(HttpServletRequest request, HttpServletResponse response){
		try{
		String docid="-1";
		String certserial="-1";
		boolean quick = false;
//		if (request.getCookies() != null){
//			Cookie ck;
//			for(int loopIndex = 0; loopIndex < request.getCookies().length; loopIndex++) { 
//	            ck = request.getCookies()[loopIndex];
//	            if (ck.getName().equals("quickcookie")) quick = true;
//        } 
//			for(int loopIndex = 0; loopIndex < request.getCookies().length; loopIndex++) { 
//	            ck = request.getCookies()[loopIndex];
//	            if (quick){
//	            	 if (ck.getName().equals("quickdocid")) docid = ck.getValue();   
//	            	 if (ck.getName().equals("quickcertserial")) certserial = ck.getValue();   
//	            }
//	            else {
//	            	 if (ck.getName().equals("docid")) docid = ck.getValue();   
//	            	 if (ck.getName().equals("certserial")) certserial = ck.getValue();             	
//	            }
//	           
//        } 					
//		}
		response.setCharacterEncoding("UTF-8");
		
		
		response.setContentType("text/html; charset=UTF-8");
		response.getWriter().print("<html><head><meta content='text/html; charset=UTF-8' "
				+ "http-equiv='Content-Type'/></head><body><script type=\"text/javascript\"> "
				+ "history.back(); parent.resizeLargeAfterCancel();</script></body></html>");
		
		
			
		}
		catch (Exception e) {		
			 StringWriter outError = new StringWriter();
			   PrintWriter errorWriter = new PrintWriter( outError );
			   e.printStackTrace(errorWriter);
			   log.error(outError.toString());
		}
		return null;
	}

}

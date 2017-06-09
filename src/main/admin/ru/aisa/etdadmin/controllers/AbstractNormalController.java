package ru.aisa.etdadmin.controllers;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.web.servlet.ModelAndView;

import ru.aisa.etdadmin.controllers.AbstractContr;

public abstract class AbstractNormalController extends AbstractContr {

	public AbstractNormalController() throws JSONException {
		super();
	}

	@Override
	protected ModelAndView do_action(final HttpServletRequest request,
			final HttpServletResponse response) throws Exception {
		final String action = (String) request.getParameter("act");
		response.setContentType("text/plain");
		response.setCharacterEncoding("UTF-8");
		try {
			if (action == null || action.equals("get")) {
				get(request).write(response.getWriter());
				return null;
			}else{
				if(getTransT()!=null)
				{
				//--------begin transaction
				getTransT().execute(new TransactionCallback() {
				
					public Object doInTransaction(TransactionStatus status) {
						try{
						if (action.equals("new")) {
							add(request).write(response.getWriter());
						}else if (action.equals("delete")) {
							delete(request).write(response.getWriter());
						}else if (action.equals("edit")) {
							edit(request).write(response.getWriter());
						}else if (action.equals("checkCertificat")) {
							try {
								response.getWriter().print(checkCertificat(request));
							}catch (Exception e) {
								status.setRollbackOnly();
								StringWriter outError = new StringWriter();
								PrintWriter errorWriter = new PrintWriter(outError);
								e.printStackTrace(errorWriter);
								log.error(outError.toString());
							}
						}else{
							JSONObject json = new JSONObject();
							json.put(success, false);
							json.put(description,"unknown action"); 
							json.write(response.getWriter());
						}
						}catch (JSONException e) {
							StringWriter outError = new StringWriter();
							PrintWriter errorWriter = new PrintWriter( outError );
							e.printStackTrace(errorWriter);
							log.error(outError.toString());
						}catch (IOException e) {
							StringWriter outError = new StringWriter();
							PrintWriter errorWriter = new PrintWriter( outError );
							e.printStackTrace(errorWriter);
							log.error(outError.toString());
						}catch (Exception e) {
							status.setRollbackOnly();
							StringWriter outError = new StringWriter();
							PrintWriter errorWriter = new PrintWriter( outError );
							e.printStackTrace(errorWriter);
							log.error(outError.toString());
						}
						return null;
					}
		
				});
				//------end transaction
				}
				else{
					try{
						if (action.equals("new")) {
							add(request).write(response.getWriter());
						}else if (action.equals("delete")) {
							delete(request).write(response.getWriter());
						}else if (action.equals("edit")) {
							edit(request).write(response.getWriter());
						}else if (action.equals("checkCertificat")) {
							try {
								response.getWriter().print(checkCertificat(request));
							}catch (Exception e) {
								StringWriter outError = new StringWriter();
								PrintWriter errorWriter = new PrintWriter(outError);
								e.printStackTrace(errorWriter);
								log.error(outError.toString());
							}
						}else{
							JSONObject json = new JSONObject();
							json.put(success, false);
							json.put(description,"unknown action"); 
							json.write(response.getWriter());
						}
						}catch (JSONException e) {
							StringWriter outError = new StringWriter();
							PrintWriter errorWriter = new PrintWriter( outError );
							e.printStackTrace(errorWriter);
							log.error(outError.toString());
						}catch (IOException e) {
							StringWriter outError = new StringWriter();
							PrintWriter errorWriter = new PrintWriter( outError );
							e.printStackTrace(errorWriter);
							log.error(outError.toString());
						}
				}

				return null;
			}
		} catch (Exception e) {
			JSONObject json = new JSONObject();
			json.put(success, false);
			json.put(description, e.getLocalizedMessage());
			json.write(response.getWriter());
			StringWriter outError = new StringWriter();
			PrintWriter errorWriter = new PrintWriter(outError);
			e.printStackTrace(errorWriter);
			log.error(outError.toString());
			return null;
		}
	}

	protected  JSONObject edit(HttpServletRequest request)
			throws JSONException{
		return NOT_IMPLEMENTED;
	}

	protected  JSONObject delete(HttpServletRequest request)
			throws JSONException{
		return NOT_IMPLEMENTED;
	}

	protected JSONObject add(HttpServletRequest request)
			throws JSONException{
		return NOT_IMPLEMENTED;
	}

	protected JSONObject get(HttpServletRequest request)
			throws JSONException{
		return NOT_IMPLEMENTED;
	}

	protected String checkCertificat(HttpServletRequest request)
			throws IOException{
		return null;
	}
	
}

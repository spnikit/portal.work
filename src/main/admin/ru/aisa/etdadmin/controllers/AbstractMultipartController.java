package ru.aisa.etdadmin.controllers;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.security.cert.X509Certificate;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathFactory;

import org.apache.commons.fileuploadnamesconf.FileItemIterator;
import org.apache.commons.fileuploadnamesconf.FileItemStream;
import org.apache.commons.fileuploadnamesconf.FileUploadException;
import org.apache.commons.fileuploadnamesconf.servlet.ServletFileUpload;
import org.apache.commons.fileuploadnamesconf.util.Streams;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.web.servlet.ModelAndView;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.aisa.cms.CMSSignedData;
import com.aisa.htmlgenerator.AutoCompleteHTML;

import ru.aisa.crypto.FieldType;
import ru.aisa.crypto.WhoType;
import ru.aisa.crypto.X509Parser;
import ru.aisa.crypto.X509ParserFactory;
import ru.aisa.rgd.utils.Base64;
import ru.aisa.rgd.ws.documents.ETDForm;
import ru.aisa.rgd.ws.utility.TypeConverter;

public abstract class AbstractMultipartController extends AbstractContr {
	public AbstractMultipartController() throws JSONException {
		super();
	}

	protected static final String codepage = "ISO-8859-1";  

	@Override
	protected ModelAndView do_action(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		try {
			if (ServletFileUpload.isMultipartContent(request)) {
				return handleRequestMultipart(request, response);
			} else
				return handleRequestNormal(request, response);
		} catch (Exception e) {
			JSONObject json = new JSONObject();
			json.put(success, false);
			json.put(description, e.getLocalizedMessage());
		//	json.write(response.getWriter());
			StringWriter outError = new StringWriter();
			PrintWriter errorWriter = new PrintWriter(outError);
			e.printStackTrace(errorWriter);
			log.error(outError.toString());
			return null;
		}

	}

	private ModelAndView handleRequestNormal(final HttpServletRequest request,
			final HttpServletResponse response) throws JSONException, IOException {
		String action = (String) request.getParameter("act");
		response.setCharacterEncoding("UTF-8");
		response.setContentType("text/plain");

		if (action == null || action.equals("get")) {
			get(request).write(response.getWriter());
			return null;
		}else if (action.equals("delete")) {
			getTransT().execute(new TransactionCallback(){

				public Object doInTransaction(TransactionStatus status) {
					try {
						delete(request).write(response.getWriter());
					} catch (JSONException e) {
						StringWriter outError = new StringWriter();
						PrintWriter errorWriter = new PrintWriter(outError);
						e.printStackTrace(errorWriter);
						log.error(outError.toString());
					} catch (IOException e) {
						StringWriter outError = new StringWriter();
						PrintWriter errorWriter = new PrintWriter(outError);
						e.printStackTrace(errorWriter);
						log.error(outError.toString());
					}catch (Exception e) {
						status.setRollbackOnly();
						StringWriter outError = new StringWriter();
						PrintWriter errorWriter = new PrintWriter(outError);
						e.printStackTrace(errorWriter);
						log.error(outError.toString());
					}
					return null;
				}});
			
			return null;
		}else if (action.equals("checkCertificat")) {
			getTransT().execute(new TransactionCallback(){

				public Object doInTransaction(TransactionStatus status) {
					try {
						response.getWriter().print(checkCertificat(request));
					}catch (Exception e) {
						status.setRollbackOnly();
						StringWriter outError = new StringWriter();
						PrintWriter errorWriter = new PrintWriter(outError);
						e.printStackTrace(errorWriter);
						log.error(outError.toString());
					}
					return null;
				}});
			
			return null;
		}else if (action.equals("export")) {
			response.setCharacterEncoding("UTF-8");
			export(request,response);
			return null;
		}
		else{
			JSONObject json = new JSONObject();
			json.put(success, false);
			json.put(description,"unknown action"); 
			json.write(response.getWriter());
			return null;
		}
	}

	private ModelAndView handleRequestMultipart(HttpServletRequest request,
			final HttpServletResponse response) throws FileUploadException,
			IOException, JSONException {
		response.setCharacterEncoding("UTF-8");
		response.setContentType("text/html");
		ServletFileUpload upload = new ServletFileUpload();
		FileItemIterator iter = upload.getItemIterator(request);
		final HashMap<String, String> requestParameters = new HashMap<String, String>();
		final HashMap<String, String> requestFileNames = new HashMap<String, String>();
		final HashMap<String, byte[]> requestFiles = new HashMap<String, byte[]>();
		while (iter.hasNext()) {
			FileItemStream item = iter.next();
			String name = item.getFieldName();
			InputStream stream = item.openStream();
			if (item.isFormField()) {

				
				requestParameters.put(name, Streams.asString(stream, "UTF-8"));
			} else {
				ByteArrayOutputStream bos = new ByteArrayOutputStream();
				byte[] buf = new byte[1024];
				int len;
				while ((len = stream.read(buf)) > 0) {
					bos.write(buf, 0, len);
				}
				byte[] fileContent = bos.toByteArray();
				bos.close();
				requestFiles.put(name, fileContent);
				requestFileNames.put(name, item.getName());
			}
			stream.close();
		}
		if (requestParameters.get("act").equalsIgnoreCase("new")) {
			//
			getTransT().execute(new TransactionCallback(){

				public Object doInTransaction(TransactionStatus status) {
					try {
						add(requestParameters, requestFiles, requestFileNames).write(response.getWriter());
					} catch (JSONException e) {
						StringWriter outError = new StringWriter();
						PrintWriter errorWriter = new PrintWriter(outError);
						e.printStackTrace(errorWriter);
						log.error(outError.toString());
					} catch (IOException e) {
						StringWriter outError = new StringWriter();
						PrintWriter errorWriter = new PrintWriter(outError);
						e.printStackTrace(errorWriter);
						log.error(outError.toString());
					}
					catch (Exception e) {
						JSONObject json = new JSONObject();
						try {
							json.put(success, false);
							json.put(description, e.getMessage());
							json.write(response.getWriter());
						} catch (Exception e1) {
							//do nothing
						}
						
						status.setRollbackOnly();
						StringWriter outError = new StringWriter();
						PrintWriter errorWriter = new PrintWriter(outError);
						e.printStackTrace(errorWriter);
						log.error(outError.toString());
					}
					return null;
				}});
			//
            //add(requestParameters, requestFiles, requestFileNames).write(response.getWriter());
			return null;
		}else if (requestParameters.get("act").equalsIgnoreCase("edit")) {
			getTransT().execute(new TransactionCallback(){

				public Object doInTransaction(TransactionStatus status) {
					try {
						edit(requestParameters, requestFiles,requestFileNames).write(response.getWriter());
					} catch (JSONException e) {
						StringWriter outError = new StringWriter();
						PrintWriter errorWriter = new PrintWriter(outError);
						e.printStackTrace(errorWriter);
						log.error(outError.toString());
					} catch (IOException e) {
						StringWriter outError = new StringWriter();
						PrintWriter errorWriter = new PrintWriter(outError);
						e.printStackTrace(errorWriter);
						log.error(outError.toString());
					}catch (Exception e) {
						status.setRollbackOnly();
						StringWriter outError = new StringWriter();
						PrintWriter errorWriter = new PrintWriter(outError);
						e.printStackTrace(errorWriter);
						log.error(outError.toString());
					}
					return null;
				}});
			//edit(requestParameters, requestFiles,requestFileNames).write(response.getWriter());
			return null;
		}else if (requestParameters.get("act").equalsIgnoreCase("getCertInfo")) {
			//
			getTransT().execute(new TransactionCallback(){

				public Object doInTransaction(TransactionStatus status) {
					try {
						getCertInfo(requestParameters, requestFiles, requestFileNames).write(response.getWriter());
					} catch (JSONException e) {
						StringWriter outError = new StringWriter();
						PrintWriter errorWriter = new PrintWriter(outError);
						e.printStackTrace(errorWriter);
						log.error(outError.toString());
					} catch (IOException e) {
						StringWriter outError = new StringWriter();
						PrintWriter errorWriter = new PrintWriter(outError);
						e.printStackTrace(errorWriter);
						log.error(outError.toString());
					}
					catch (Exception e) {
						JSONObject json = new JSONObject();
						try {
							json.put(success, false);
							json.put(description, e.getMessage());
							json.write(response.getWriter());
						} catch (Exception e1) {
							//do nothing
						}
						
						status.setRollbackOnly();
						StringWriter outError = new StringWriter();
						PrintWriter errorWriter = new PrintWriter(outError);
						e.printStackTrace(errorWriter);
						log.error(outError.toString());
					}
					return null;
				}});
			//
            //add(requestParameters, requestFiles, requestFileNames).write(response.getWriter());
			return null;
		}else{
			JSONObject json = new JSONObject();
			json.put(success, false);
			json.put(description,"unknown action"); 
			json.write(response.getWriter());
			return null;
		}
	}

	protected JSONObject get(HttpServletRequest request)
			throws JSONException{
		return NOT_IMPLEMENTED;
	}

	protected JSONObject add(
			HashMap<String, String> requestParameters,
			HashMap<String, byte[]> requestFiles,HashMap<String, String> requestFileNames) throws JSONException, IOException{
		return NOT_IMPLEMENTED;
	}

	protected JSONObject edit(
			HashMap<String, String> requestParameters,
			HashMap<String, byte[]> requestFiles,HashMap<String, String> requestFileNames) throws JSONException, IOException{
		return NOT_IMPLEMENTED;
	}

	protected JSONObject delete(HttpServletRequest request)
			throws JSONException{
		return NOT_IMPLEMENTED;
	}
	
	protected void export(HttpServletRequest request, HttpServletResponse response) throws IOException{
		try {
			System.out.println(request.getCharacterEncoding());
			request.setCharacterEncoding("UTF-8");
			response.setCharacterEncoding("UTF-8");
			NOT_IMPLEMENTED.write(response.getWriter());
		} catch (JSONException e) {
			StringWriter outError = new StringWriter();
			PrintWriter errorWriter = new PrintWriter(outError);
			e.printStackTrace(errorWriter);
			log.error(outError.toString());
		}
	}
	
	protected String checkCertificat(HttpServletRequest request) throws IOException{
		return null;
}
	
	protected JSONObject getCertInfo(HashMap<String, String> requestParameters,
	HashMap<String, byte[]> requestFiles,HashMap<String, String> requestFileNames) throws JSONException, IOException{
		return NOT_IMPLEMENTED;
	}

}

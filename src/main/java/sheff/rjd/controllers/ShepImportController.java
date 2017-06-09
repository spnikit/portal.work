package sheff.rjd.controllers;

import java.io.InputStream;
import java.io.PrintWriter;
import java.io.StringWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.context.support.ServletContextResource;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;

import sheff.rjd.utils.XmlSupport;


public class ShepImportController extends AbstractController {
	
	public ShepImportController() {
		}
	
	
	
		
	protected ModelAndView handleRequestInternal(HttpServletRequest request, HttpServletResponse response) throws Exception {
		response.setCharacterEncoding("UTF-8");
		 response.setContentType("text/html; charset=UTF-8");
		try{
			
			ServletContextResource pref = new ServletContextResource(request.getSession().getServletContext(),"WEB-INF/classes/prefs.xml");
			InputStream is = pref.getInputStream();
			XmlSupport.importPreferences(is);
			is.close();
			response.getWriter().print("ok");
		}
		catch (Exception e) {
			 StringWriter outError = new StringWriter();
			   PrintWriter errorWriter = new PrintWriter( outError );
			   e.printStackTrace(errorWriter);
			   response.getWriter().print(outError.toString());
		}
		return null;

}
	
}

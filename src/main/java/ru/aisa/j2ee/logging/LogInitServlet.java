package ru.aisa.j2ee.logging;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.apache.log4j.xml.DOMConfigurator;

public class LogInitServlet extends HttpServlet {
	
	protected final Logger	log	= Logger.getLogger(getClass());
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		resp.getWriter().write("OK");
	}

	@Override
	public void init() throws ServletException {
//		String prefix = getServletContext().getRealPath("/");
		String file = getInitParameter("log4j-init-file");
		String xml = getInitParameter("log4j-init-xml");
	//	 System.out.println("FILE : "+getClass().getClassLoader().getResource(file).getFile().replace("%20", " "));
		//System.out.println(System.getProperty("os.name"));
		
		if (System.getProperty("os.name").indexOf("z/OS") == 0) {
			if (file != null) {
				if (xml != null) {
					DOMConfigurator.configure(getClass().getClassLoader().getResource(file).getFile() );
				} else {
					PropertyConfigurator.configure(getClass().getClassLoader().getResource(file).getFile() );//prefix + file);
				}
			}
		} else {
			if (file != null) {
				DOMConfigurator.configure(getClass().getClassLoader().getResource("log-config.xml").getFile().replace("%20", " "));
			} else {
				DOMConfigurator.configure(getClass().getClassLoader().getResource("log-config.xml").getFile().replace("%20", " "));
			}
		}
	}

}

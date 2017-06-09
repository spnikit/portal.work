package ru.aisa.j2ee.logging;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

public class LogTestServlet extends HttpServlet {
	static Logger log = Logger.getLogger(LogTestServlet.class);

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		log.warn("logger warn  BEGIN");
		log.info("logger info  BEGIN");
		log.error("logger error  BEGIN");
		log.fatal("logger fatal  BEGIN");
		log.debug("logger debug  BEGIN");
		
		resp.getWriter().write("OK<br/>");
		resp.getWriter().write(System.getProperty("os.name")+"<br/>");
		resp.getWriter().write(System.getProperty("file.encoding"));
	}

}

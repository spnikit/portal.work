package sheff.rjd.controllers;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;


public class Torg12DropController extends AbstractController {
	
	public Torg12DropController() {
		}
	
	
	
		
	protected ModelAndView handleRequestInternal(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		response.setCharacterEncoding("UTF-8");
		 response.setContentType("text/html; charset=UTF-8");
		response.getWriter().print("<html><head><meta content='text/html; charset=UTF-8' http-equiv='Content-Type'/></head>" +
						"<body style=\"background: url('../pics/2.jpg') no-repeat bottom center fixed;\"><center><h2>Пожалуйста, подождите.</h2></center>" +
						"<script type=\"text/javascript\">history.back();parent.torg12drop();</script></body></html>");
		return null;

}
	
}

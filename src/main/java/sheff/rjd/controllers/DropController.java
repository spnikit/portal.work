package sheff.rjd.controllers;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;


public class DropController extends AbstractController {
	
	public DropController() {
		}
	
	
	
		
	protected ModelAndView handleRequestInternal(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		response.setCharacterEncoding("UTF-8");
		 response.setContentType("text/html; charset=UTF-8");
		response.getWriter().print("<html><head><meta content='text/html; charset=UTF-8' http-equiv='Content-Type'/></head>" +
						"<body style=\"background: url('../pics/2.jpg') no-repeat bottom center fixed;\"><center><h2>Пожалуйста, подождите.</h2></center><p><center><h2>Документ бракуется.</h2></center>" +
	//		"<body BGCOLOR=#e6f6ff><center><h2>Пожалуйста, подождите.</h2></center><p><center><h2>Документ сохраняется.</h2></center>" +
		"<script type=\"text/javascript\">history.back();parent.resizeLargeDrop(); </script></body></html>");
		return null;

}
	
}

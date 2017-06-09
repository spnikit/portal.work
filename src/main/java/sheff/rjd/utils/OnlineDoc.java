package sheff.rjd.utils;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.log4j.Logger;

public class OnlineDoc extends HttpServlet {
	private static Logger	log	= Logger.getLogger(OnlineDoc.class);
	  public void doGet(HttpServletRequest request, HttpServletResponse response)
	       {
		  try {
	   doWork(request,response);
		  }
		  catch (Exception e) {
			  StringWriter outError = new StringWriter();
			   PrintWriter errorWriter = new PrintWriter( outError );
			   e.printStackTrace(errorWriter);
			   log.error(outError.toString());
		}
	  }
	  public void doPost(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
   doWork(request,response);
  }
	  public void doWork(HttpServletRequest request,HttpServletResponse response) throws IOException{
		  response.setCharacterEncoding("UTF-8");
		  response.setContentType("text/html; charset=UTF-8");
		  PrintWriter out = response.getWriter();
		    out.println("<!-- DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Strict//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-Strict.dtd\"-->"+
		    		"<html>" +
		    		"<meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\">"+
		    		"<meta http-equiv=\"X-UA-Compatible\" content=\"IE=EmulateIE8\" />"+
		    		"<link rel=\"stylesheet\" type=\"text/css\" href=\"resources/css/ext-all.css\" >"+
		    		"<link rel=\"stylesheet\" type=\"text/css\" href=\"resources/css/xtheme-gray.css\" >"+
		    		"<style type=\"text/css\">body { margin:0; padding:0;}.white-body{ background: url('pics/2.jpg') no-repeat bottom center fixed;}</style>" +
		    		"<head><title>Портал электронного документооборота</title></head>" +
		    		"<script type=\"text/javascript\" src=\"adapter/ext/ext-base.js\"></script>"+
		    		"<script type=\"text/javascript\" src=\"js/startInit.js\"></script>"+
					"<script type=\"text/javascript\" src=\"ext-all.js\"></script>"+
					"<script type=\"text/javascript\" src=\"etd-quick.js?v=2\"></script>"+
					"<script type=\"text/javascript\" src=\"quickutils.js?v=3\"></script>"+
					"<script type=\"text/javascript\" src=\"webtoolkit.utf8.js\"></script>"+
		    		"<script type=\"text/javascript\" src=\"js/NPAPI/BrowserDetect.js\"></script>"+
		    		"<script type=\"text/javascript\" src=\"js/NPAPI/NPAPIplugin.js?v=2\"></script>"+
		    		"<script type=\"text/javascript\" src=\"js/Functions.js?v=9\"></script>"+
		    		"<script type=\"text/javascript\" src=\"js/globalInit.js\"></script>");
   
		    
		    String id = request.getParameter("id");
		    if (id != null){
		    	out.println("  	<body class=\"white-body\">" +
		    			 		"<OBJECT ID=\"aisa.aisaplugin.1\" CLASSID=\"clsid:5E8C4033-CF35-5085-B393-97BB896D5238\" > "+
		    					"<param name=\"jvmPath\" value=\"C:\\aisa\\jre\\bin\\client\">"+
		    					"<param name=\"classPath\" value=\"C:\\aisa\\applet.jar\">"+
		    					"<param name=\"className\" value=\"mypack/zzz\">"+
		    					"<param name=\"jarsPath\" value=\"C:\\aisa\\dependency\">"+
		    					"<param name=\"typeApplet\" value=\"quickapplet\">"+
		    					"<param name=\"id\" value=\""+ id+ "\">"+
		    					"</object>"+
"<IFRAME id=\"viewerid\" name=\"viewername\" SRC=\"viewer.html\" WIDTH=\"100%\" HEIGHT=\"0\" SCROLLING=NO frameborder=\"0\" style=\"position: absolute; top: 0px; left:0px;z-index:40000;\">" +
"IFRAME not supported" +
"</IFRAME>"+
"<img id=\"loading\" src=\"loadpic.png\" height=\"75 px\" width=\"200 px\" alt=\"\" style=\"z-index: 100000; visibility: hidden; position:absolute;\" />" +
"<div id=\"ret\" style=\"width:100%; z-index:500;position:absolute;left:0px; top:0px; font-family: verdana,tahoma,arial,helvetica,sans-serif;font-size: 21px; text-align: center;\"></div>");
		    }
		    else {
		    	out.println("<body bgcolor=#96a2ad> <center>ID документа не определен</center>");
	    	
	    	}
		    
		    out.println(" </body> </html>");
		   
		    
		  
	  }
	}

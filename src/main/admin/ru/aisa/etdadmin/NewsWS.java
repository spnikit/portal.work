package ru.aisa.etdadmin;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;

public class NewsWS extends AbstractController {

	private StringHolder news;
	private boolean isSet = false;
	private String success = "success";
	
	private String header = "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:ns=\"http://etd.gvc.rzd:8888/WAS/ETD/getnotice\">\n"+
	   "<soapenv:Header/>\n"+
	  " <soapenv:Body>\n"+
	      "<ns:ServiceResponse>\n";
	
	
	private String bottom = "</ns:ServiceResponse>\n"+
	   "</soapenv:Body>\n"+
	"</soapenv:Envelope>\n";
	
	
	public StringHolder getNews() {
		return news;
	}

	public void setNews(StringHolder news) {
		this.news = news;
	}

	@Override
	protected ModelAndView handleRequestInternal(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		
		
		/*
		 * 
		 * <soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/" xmlns:ns="http://etd.gvc.rzd:8888/WAS/ETD/getnotice">
   <soapenv:Header/>
   <soapenv:Body>
      <ns:ServiceResponse>
          <isNoticeSet>1</isNoticeSet>
          <notice>Объявление из Консоли АС ЭТД</notice> 
      </ns:ServiceResponse>
   </soapenv:Body>
</soapenv:Envelope>

		 * 
		 */	
		response.setCharacterEncoding("UTF-8");
		response.setContentType("text/xml");
		response.setHeader("SOAPAction", "\"\"");
		
		
		if (getNews().getStr().trim().length()>0){
			response.getWriter().write(header+
					"<isNoticeSet>1</isNoticeSet>\n"+
					"<notice>"+getNews().getStr()+"</notice>\n"
					+bottom);
		}
		else{
			response.getWriter().write(header+"<isNoticeSet>0</isNoticeSet>\n"+bottom);
		}
		return null;
	}

}

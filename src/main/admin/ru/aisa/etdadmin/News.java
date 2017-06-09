package ru.aisa.etdadmin;

import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;

public class News extends AbstractController {

	private StringHolder news;
	private boolean isSet = false;
	private String success = "success";

	public StringHolder getNews() {
		return news;
	}


	public void setNews(StringHolder news) {
		this.news = news;
	}

	private static NamedParameterJdbcTemplate npjt;
	public static NamedParameterJdbcTemplate getNpjt() {
		return npjt;
	}
	public void setNpjt(NamedParameterJdbcTemplate npjt) {
		News.npjt = npjt;
	}
	@Override
	protected ModelAndView handleRequestInternal(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		JSONObject resp = new JSONObject();
		response.setCharacterEncoding("UTF-8");
		response.setContentType("text/plain");
		if(Utils.getAuth().equals(Utils.ROLE_ANONYMOUS)||(request.getParameter("edit")==null&&request.getParameter("del")==null)){
			resp.put(success, true);
			JSONObject rr = new JSONObject();
			rr.put("isset", isSet);
			//rr.put("news", news);
			rr.put("news", news.getStr());
			JSONArray ja = new JSONArray();
			ja.put(rr);
			resp.put("data", ja);
		}else{
			if(Utils.getAuth().equals(Utils.ROLE_ADMIN)){
				if(request.getParameter("edit")!=null&&request.getParameter("edit").equals("true")&&request.getParameter("news")!=null){
					isSet = true;
					//news = request.getParameter("news");
					news.setStr(request.getParameter("news"));
					//npjt.update("update letd.news set news = '"+news+"'", new HashMap());
					npjt.update("update letd.news set news = '"+news.getStr()+"'", new HashMap());
					npjt.update("update letd.personall set ISNEWS = 1", new HashMap());
					resp.put(success, true);
				}else if(request.getParameter("del")!=null&&request.getParameter("del").equals("true")){
					isSet = false;
					npjt.update("update letd.PERSONALL set ISNEWS = 0", new HashMap());
					news.setStr("");
					//news = "";
					resp.put(success, true);
				}else {
					resp.put(success, false);
					resp.put("desc", "I don't know what to do");
				}
			}
		}
		resp.write(response.getWriter());
		
		return null;
	}

}

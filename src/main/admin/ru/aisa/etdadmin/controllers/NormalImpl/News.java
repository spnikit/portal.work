package ru.aisa.etdadmin.controllers.NormalImpl;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.web.servlet.ModelAndView;
import ru.aisa.etdadmin.Utils;
import ru.aisa.etdadmin.controllers.AbstractContr;

public class News extends AbstractContr {

	
	public News() throws JSONException {
		super();
	}
	private String news;
	private boolean isSet = false;
	private String success = "success";

	public String getNews() {
		return news;
	}


	public void setNews(String news) {
		this.news = news;
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
			rr.put("news", news);
			JSONArray ja = new JSONArray();
			ja.put(rr);
			resp.put("data", ja);
		}else{
			if(Utils.getAuth().equals(Utils.ROLE_ADMIN)){
				if(request.getParameter("edit")!=null&&request.getParameter("edit").equals("true")&&request.getParameter("news")!=null){
					isSet = true;
					news = request.getParameter("news");
					getSjt().update("update SNT.news set news = '"+news+"'");
					getSjt().update("update SNT.personall set nread = 1");
					//news=request.getParameter("news");
					resp.put(success, true);
				}else if(request.getParameter("del")!=null&&request.getParameter("del").equals("true")){
					isSet = false;
					getSjt().update("update SNT.personall set nread = 0");
					news = "";
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


	@Override
	protected ModelAndView do_action(HttpServletRequest arg0,
			HttpServletResponse arg1) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

}

package ru.aisa.etdadmin.controllers.NormalImpl;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.security.KeyStore.Entry;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.ParameterizedRowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcTemplate;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;


import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.json.*;

import ru.aisa.etdadmin.Utils;
import ru.aisa.etdadmin.controllers.AbstractNormalController;

public class manyRights extends AbstractNormalController {
	
	static private final String hasIntersection = "hasintersect";	//true if has intersections with
																	//previous rights configuration
	public manyRights() throws JSONException {
		super();
	}

		
	@Override
	protected JSONObject add(HttpServletRequest request)
			throws JSONException {
		if(Utils.isReadonly()) return FORBIDDEN;
		JSONObject response = new JSONObject();
		response.put(success, false);
		response.put(hasIntersection, false);	
		Integer cedit, cview, cnew;
		if (request.getParameter("cedit") != null)
			cedit = 1;
		else
			cedit = 0;
		if (request.getParameter("cview") != null)
			cview = 1;
		else
			cview = 0;
		if (request.getParameter("cnew") != null)
			cnew = 1;
		else
			cnew = 0;
		
		String[] docnames =request.getParameter("docname").split(",");
		String[] wrknames =request.getParameter("wrkname").split(",");
		List l = getSjt().queryForList(
				"SELECT * FROM SNT.doctypeacc WHERE dtid in ("+request.getParameter("docname")+") and wrkid in ("+request.getParameter("wrkname")+")",
				new Object[] { });
		
		//если вводимые права пересекаются с существующими...
		String condit = "";
		if (l.size() > 0) {
			for(int i = 0; i<l.size(); i++)
			{
				Map<String, Object> m = (Map<String, Object>)l.get(i);
				Integer a = (Integer)m.get("CNEW");
				Integer b = (Integer)m.get("CVIEW");
				Integer c = (Integer)m.get("CEDIT");
				//если вводимые права отличаются от существующих...
				if(cnew.intValue()!=a.intValue() || cview.intValue()!=b.intValue() || cedit.intValue()!=c.intValue())
				{
					//если первый запрос - шлем сообщение о необходимости подтверждения 
					if( request.getParameter("reqcount").equals("first")){
						response.put(success, true);
						response.put(hasIntersection, true);
						response.put(description,
							"Вводимые данные пересекаются с существующими и отличаются от них.");
						return response;
					}
				}
				Integer dtid = (Integer)m.get("DTID");
				Integer wrkid = (Integer)m.get("WRKID");	
				condit += "(wrkid = " + wrkid + " and dtid = " + dtid + ") or";
			}
		}
			
		//удаляем старые права (если таковые имелись)
		if(l.size()>0)
		{
			String final_request = "delete from SNT.doctypeacc where " + condit.substring(0, condit.length()-2);
			getSjt().update(final_request, new Object[]{});
		}
		//заносим новые права
		ArrayList<Object[]> aa = new ArrayList<Object[]>();
		for (int i=0; i<docnames.length;i++){
			for (int i1=0;i1<wrknames.length;i1++){
				aa.add(new Object[]{docnames[i],wrknames[i1],cview,cedit,cnew});
			}
		}
		getSjt().batchUpdate("insert into SNT.doctypeacc(dtid,wrkid,cview,cedit,cnew) values(?,?,?,?,?)",aa);
		response.put(success, true);
		return response;

	}

}

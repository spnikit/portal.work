package ru.aisa.etdportal.controllers;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.ParameterizedRowMapper;

import sheff.rjd.services.transoil.SendObject;
import sheff.rjd.ws.OCO.TORLists;
import sheff.rjd.ws.OCO.AfterSign.SendToEtd;
import ru.aisa.rgd.ws.documents.ETDForm;
import ru.aisa.rgd.ws.exeption.InternalException;
import ru.aisa.rgd.ws.exeption.ServiceException;
import ru.aisa.rgd.ws.utility.DataBinder;
import ru.aisa.rgd.ws.utility.TypeConverter;

public class ResendToTransoil extends AbstractPortalSimpleController {

	public ResendToTransoil() throws JSONException {
		super();
	}
	
	private String sql1  ="select etdid from snt.docstore where "+
"crdate= '2014-04-26' and predid = 20006077";
	private String sql2 ="select etdid from snt.docstore where "+
			"crdate= '2014-04-27' and predid = 20006077";
	private String sql3  ="select etdid from snt.docstore where "+
			"crdate= '2014-04-28' and crtime<'10:30:00' and predid = 20006077";
	private String sql4  ="select etdid from snt.docstore where "+
			"crdate= current_date and etdid is not null";
	private SendObject sendtotrans;
	
	
	public SendObject getSendtotrans() {
		return sendtotrans;
	}

	public void setSendtotrans(SendObject sendtotrans) {
		this.sendtotrans = sendtotrans;
	}
	
	@Override
	public JSONArray get(HttpServletRequest request){
		
		final JSONArray response = new JSONArray();
		HashMap<String, Object> pp = new HashMap<String, Object>();
		if (request.getParameter("etdid")!=null){

			try{
//				System.out.println(request.getParameter("etdid"));
//				int type = Integer.parseInt(request.getParameter("etdid"));
//				String finalsql = null;
//				
//				switch(type){
//				case 1:
//					finalsql = sql1;
//					break;
//				case 2:
//					finalsql = sql2;
//					break;
//				case 3:
//					finalsql = sql3;
//					break;
//				}
//				List ids = getNpjt().queryForList(finalsql, new HashMap<String, Object>()); 
//				for (int i=0; i<ids.size(); i++){
//					
//					
//				sendtotrans.Send((((Map) ids.get(i)).get("ETDID"))
//						.toString());
//				}
				
				
				sendtotrans.Send(request.getParameter("etdid"));
				
		 response.put(true);
		
			} catch (Exception e) {
				
				log.error(TypeConverter.exceptionToString(e));
				 response.put(false);
				}
	
		}
		
		return response;
	}
	
	
}
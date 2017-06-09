package ru.aisa.etdadmin.controllers.NormalImpl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.util.Date;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.jdbc.core.simple.ParameterizedRowMapper;

import ru.aisa.etdadmin.Utils;
import ru.aisa.etdadmin.controllers.AbstractNormalController;


public class JoinMessage extends AbstractNormalController {

	public JoinMessage() throws JSONException {
		super();
	}


	public JSONObject add(HttpServletRequest request) throws JSONException {

		// TODO Auto-generated method stub
		final JSONObject response = new JSONObject();
		try{
		response.put("success", false);

		final String message = request.getParameter("message");
	
		int count = getSjt().queryForInt("select count(0) from snt.join_utils", new Object[]{});
		if(count == 0){
			getSjt().update("insert into snt.join_utils (id, message) values (0, null)", new Object[]{}); 
		}
		
		getSjt().update("update snt.join_utils set message = ?", new Object[]{message});
		
		response.put("success", true);
		}
		catch(Exception e){
			e.printStackTrace();
			response.put("msg", e.toString());
			return response;}
		
		response.put("msg", "Операция выполнена успешно");
		return response;
	}
	
	public JSONObject delete(HttpServletRequest request) throws JSONException {
		// TODO Auto-generated method stub
		final JSONObject response = new JSONObject();
		try{
		response.put("success", false);
		
		getSjt().update("update snt.join_utils set message = null", new Object[]{});
		
		}
		catch(Exception e){
			e.printStackTrace();
			response.put("description", e.toString());
			return response;}
		response.put("success", true);
		response.put("msg", "Операция выполнена успешно");

		return response;
	}


}

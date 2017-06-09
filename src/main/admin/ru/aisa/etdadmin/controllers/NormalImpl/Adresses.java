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


public class Adresses extends AbstractNormalController {

	public Adresses() throws JSONException {
		super();
	}


	public JSONObject add(HttpServletRequest request) throws JSONException {

		// TODO Auto-generated method stub
		final JSONObject response = new JSONObject();
		try{
		response.put("success", false);

		final String adress = request.getParameter("adress");

		int count = getSjt().queryForInt("select count(0) from snt.join_adresses where adress = ? ", new Object[]{adress});
		if(count>0){
			response.put("msg", "Такой адрес уже зарегистрирован");
			return response;
		}
		
		getSjt().update("INSERT INTO snt.join_adresses (id, adress, active) VALUES (coalesce((select max(id)+1 from snt.join_adresses),0), ?, 1)", new Object[]{adress});

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
		
	
		final String addr_id_str = request.getParameter("addrr");
		final int addr_id = Integer.parseInt(addr_id_str);
		
		getSjt().update("DELETE FROM snt.join_adresses WHERE id = ?", new Object[]{addr_id});
		

		}
		catch(Exception e){
			e.printStackTrace();
			response.put("description", e.toString());
			return response;}
		response.put("success", true);
		response.put("msg", "Операция выполнена успешно");

		return response;
	}
	
	public JSONObject edit(HttpServletRequest request) throws JSONException {
		// TODO Auto-generated method stub
		final JSONObject response = new JSONObject();
		try{
		response.put("success", false);
		

		final String addr_id_str = request.getParameter("addr");
		//final String[] addresses = addr_id_str.split(",");
		//final int addr_id = Integer.parseInt(addr_id_str);
		
		getSjt().update("update snt.join_adresses set active = 0", new Object[]{});		
		if(addr_id_str.length()>0)
		getSjt().update("update snt.join_adresses set active = 1 WHERE id in ("+addr_id_str+")", new Object[]{});
		

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

package ru.aisa.etdadmin.controllers.SimpleImpl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.jdbc.core.simple.ParameterizedRowMapper;

import ru.aisa.etdadmin.controllers.AbstractSimpleController;

public class ActiveAdresses extends AbstractSimpleController{
	
	public ActiveAdresses() throws JSONException {
		super();
	}

	public JSONArray get(HttpServletRequest request){
		final JSONArray json = new JSONArray();
		getNpjt().query("select id,  active from snt.join_adresses", new HashMap(), new ParameterizedRowMapper(){

			public Object mapRow(ResultSet rs, int arg1) throws SQLException {
				// TODO Auto-generated method stub
					JSONArray js = new JSONArray();
					js.put(rs.getInt("id"));
					js.put(rs.getShort("active"));	
		
					//System.out.println(rs.getInt("adress"));
					json.put(js);

				return null;
			}
			
		});
		return json;
	}


}

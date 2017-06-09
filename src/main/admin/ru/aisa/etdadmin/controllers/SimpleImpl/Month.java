package ru.aisa.etdadmin.controllers.SimpleImpl;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;

import org.json.JSONArray;
import org.json.JSONException;

import ru.aisa.etdadmin.controllers.AbstractSimpleController;

public class Month extends AbstractSimpleController{
	
	public Month() throws JSONException {
		super();
	}

	private static HashMap<Integer,String> month = new HashMap<Integer, String>();
	
	static{
		month.put(1, "Январь");
		month.put(2, "Февраль");
		month.put(3, "Март");
		month.put(4, "Апрель");
		month.put(5, "Май");
		month.put(6, "Июнь");
		month.put(7, "Июль");
		month.put(8, "Август");
		month.put(9, "Сентябрь");
		month.put(10, "Октябрь");
		month.put(11, "Ноябрь");
		month.put(12, "Декабрь");
	
	}
	
	@Override
	public JSONArray get(HttpServletRequest request){
		final JSONArray response = new JSONArray();
		Iterator<Entry<Integer,String>> iter = month.entrySet().iterator();
		while(iter.hasNext()){
			Entry<Integer,String> val = iter.next();
			JSONArray js = new JSONArray();
			js.put(val.getKey());
			js.put(val.getValue());
			response.put(js);
		}
		return response;
	}
	
	public static String getISSMName(int id) {
		return month.get(id);
	}
	

}

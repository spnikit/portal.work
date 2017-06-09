package ru.aisa.etdadmin.controllers.SimpleImpl;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;

import org.json.JSONArray;
import org.json.JSONException;

import ru.aisa.etdadmin.controllers.AbstractSimpleController;

public class DocType extends AbstractSimpleController{
	
	public DocType() throws JSONException {
		super();
	}

	private static HashMap<Integer,String> doctype = new HashMap<Integer, String>();
	
	static{
		doctype.put(0, "Документ");
		//doctype.put(1, "Отчет");
		/*doctype.put(2, "Форма НТ");
		doctype.put(3, "Отчет cоздателя запроса  НТ");
		doctype.put(4, "Отчет cоздателя ответа НТ");*/
	}
	
	@Override
	public JSONArray get(HttpServletRequest request){
		
		final JSONArray response = new JSONArray();
		Iterator<Entry<Integer,String>> iter = doctype.entrySet().iterator();
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
		return doctype.get(id);
	}
	

}

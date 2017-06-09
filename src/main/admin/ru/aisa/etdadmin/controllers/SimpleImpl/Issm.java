package ru.aisa.etdadmin.controllers.SimpleImpl;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;

import org.json.JSONArray;
import org.json.JSONException;

import ru.aisa.etdadmin.controllers.AbstractSimpleController;

public class Issm extends AbstractSimpleController{
	
	public Issm() throws JSONException {
		super();
	}

	private static HashMap<Integer,String> issms = new HashMap<Integer, String>();
	
	static{
		issms.put(1, "настоящая роль на подпись");
		issms.put(2, "Согласование документов");
		issms.put(3, "Подписание документов");
		issms.put(4, "Роль для смежников");
		issms.put(5, "МРМ");
		issms.put(6, "Роль на просмотр");
		issms.put(7, "ППС");
		issms.put(8, "ППС Роль 4");
		issms.put(9, "РТК");
		issms.put(10, "Мат. лицо");
		issms.put(11, "МРМ Роль 2");
		issms.put(12, "МРМ Роль 3 (РНЛ)");
		issms.put(13, "Архив");
		issms.put(14, "ВРК");
		issms.put(15, "Контрагент РЖДС");
		issms.put(16, "РЖДС");
		issms.put(17, "ЦСС");
		issms.put(18, "ППС Роль 2");
		issms.put(19, "ППС Роль 3");
		issms.put(20, "ВРК Ремонт");
		issms.put(21, "ВРК роль 2");
		issms.put(22, "Отклонение СФ");
	}
	
	@Override
	public JSONArray get(HttpServletRequest request){
		final JSONArray response = new JSONArray();
		Iterator<Entry<Integer,String>> iter = issms.entrySet().iterator();
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
		return issms.get(id);
	}
	

}

package sheff.rjd.ws.OCO;

import java.util.HashMap;

public class ServiceTypes {
	public static HashMap<String ,String> servicetypes = new HashMap<String , String>();
	static {
		servicetypes.put("01", "ТР-1");
		servicetypes.put("02", "ТР-2");
		servicetypes.put("03", "Рекламация");
		servicetypes.put("04", "Хранение запасных частей и лома");
		servicetypes.put("05", "Погрузка/выгрузка");
		servicetypes.put("06", "Продажа деталей заказчика подрядчику");
		servicetypes.put("07", "Претензионная работа");
		servicetypes.put("08", "Продажа з/ч");
		servicetypes.put("09", "Связь");
		
	}
}

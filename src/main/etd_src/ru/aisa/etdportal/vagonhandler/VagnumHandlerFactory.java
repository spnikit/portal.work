package ru.aisa.etdportal.vagonhandler;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class VagnumHandlerFactory {
	private static final String SF = "Счет-фактура";
	   private static final String Package = "Пакет документов";
	   private static final String FPu26 = "ФПУ-26";
	   private static final String MH3 = "МХ-3";
	   private static final String TORG12 = "ТОРГ-12";
	   private static final String MH1 = "МХ-1";
	   private static final String ACTzud = "Акт ЗУД";
	   private static final String ACTpp = "Акт приема передачи ТМЦ";
	   private static final String ACTbrak = "Акт браковки";
	   private static final String VU22 = "ВУ-22";
	   private static final String VU23 = "ВУ-23_О";
	   private static final String VU36 = "ВУ-36М_О";
	   private static final String RDV = "РДВ";
	   private static final String Card = "Карточка документ";
	   private static final String Spravka = "Справка 2612";
	private static Log log = LogFactory
		    .getLog(VagnumHandlerFactory.class);
	
	
	private static final Map<String, Class<? extends VagnumHandler>>	handlerMap;
	
	static {
		handlerMap = new HashMap<String, Class<? extends VagnumHandler>>();
		handlerMap.put(VU22, VagVU22Handler.class);
		handlerMap.put(VU23, VagVU23Handler.class);
		handlerMap.put(VU36, VagVU36Handler.class);
		handlerMap.put(Package, VagPackageHandler.class);
		handlerMap.put(MH1, VagMH1Handler.class);
		handlerMap.put(MH3, VagMH3Handler.class);
		handlerMap.put(ACTzud, VagActZudHandler.class);
		handlerMap.put(ACTbrak, VagActbrakHandler.class);
		handlerMap.put(ACTpp, VagActTMCHandler.class);
		handlerMap.put(Card, VagCardHandler.class);
		handlerMap.put(RDV, VagRDVHandler.class);
		handlerMap.put(FPu26, VagFPU26Handler.class);
		handlerMap.put(Spravka, VagSpravkaHandler.class);
	}
	
	public static boolean handlerExist(String name)
	{
	return handlerMap.containsKey(name != null ? name.trim() : "");
	}
	
	private static VagnumHandler getHandler(String type)
	{
	type = type.trim();
	VagnumHandler handlerObject = null;
	
	log.debug("Get handler for type: <" + type + ">");
	
	if (handlerMap.containsKey(type))
		{
		try
			{
			handlerObject = handlerMap.get(type).newInstance();
			log.debug("Resived handler: <" + handlerMap.get(type).getName()
					+ ">");
			}
		catch (InstantiationException e)
			{
			log.error(e.getMessage());
			}
		catch (IllegalAccessException e)
			{
			log.error(e.getMessage());
			}
		}
	else
		{
		log.debug("Handler didn't set");
		}
	return handlerObject;
	}
	
	public static String[] handleForm(String type, String formblob)
	{
	
		VagnumHandler handler = getHandler(type);
	if (handler != null)
		{
		log.debug("Processing form");
		String[] content = null;
		
		try
		
			{
			
			content = handler.Content(formblob);
			
			return content;
			}
		catch (Exception e)
			{
			log.error(e.getMessage());
			return content ;
			}
		}
	else
		{
		return null ;
		}
	}
	
	
}

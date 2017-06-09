package ru.aisa.etdportal.contenthandler;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class ContentHandlerFactory {

	private static Log log = LogFactory
		    .getLog(ContentHandlerFactory.class);
	
	
	private static final Map<String, Class<? extends ContentHandler>>	handlerMap;
	
	static {
		handlerMap = new HashMap<String, Class<? extends ContentHandler>>();
		handlerMap.put("Пакет документов", PackageHandler.class);
		handlerMap.put("Карточка документ", CardDocHandler.class);
		handlerMap.put("ВУ-22", VU22Handler.class);
	}
	
	public static boolean handlerExist(String name)
	{
	return handlerMap.containsKey(name != null ? name.trim() : "");
	}
	
	private static ContentHandler getHandler(String type)
	{
	type = type.trim();
	ContentHandler handlerObject = null;
	
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
	
	public static String handleForm(String type, String formblob)
	{
	
		ContentHandler handler = getHandler(type);
	if (handler != null)
		{
		log.debug("Processing form");
		String content;
		try
			{
			
			content = handler.Content(formblob);
			
			return content;
			}
		catch (Exception e)
			{
			log.error(e.getMessage());
			return "";
			}
		}
	else
		{
		return "";
		}
	}
	
	
}

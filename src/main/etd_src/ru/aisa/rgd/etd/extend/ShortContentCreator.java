package ru.aisa.rgd.etd.extend;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import ru.aisa.rgd.ws.exeption.InternalException;
import ru.aisa.rgd.ws.exeption.ServiceException;
import ru.aisa.rgd.ws.utility.SaxXmlParser;
import ru.aisa.rgd.ws.utility.TypeConverter;
import ru.aisa.rgd.ws.utility.XmlParser;

/**
 * @author anton
 * Класс для создания краткого описания документа на основе xml описания
 */
public class ShortContentCreator {
	private static Logger	logger	= Logger.getLogger(ShortContentCreator.class);
	private Map<String, ShortContentBean> typeMap; 
	
	
	public ShortContentCreator() {
		super();
	}


	public String createValue(String formName, String data) throws ServiceException, IOException, InternalException
	{
		logger.debug("Creating short decsription for from type <" + formName + ">");
		String value = "";
		try
		{
			//String name = formName.trim();
			StringBuffer buffer;
			
			ShortContentBean bean = typeMap.get(formName.trim());
			XmlParser parser = new SaxXmlParser(data);
			
			if (bean == null)
			{
				logger.debug("Content creator didn't set");
			}
			else
			{
				buffer = new StringBuffer();
				List<ShortContentDescriptor> list = bean.getDescriptions();
				for (ShortContentDescriptor descriptor : list)
				{
					buffer.append(descriptor.getPrefix());
					String tagValue = "";
					if (descriptor.all){
						List<String> tagValuesList = parser.getAllValues(descriptor.getTagName());
						Iterator<String> it = tagValuesList.iterator(); 
						while (it.hasNext()){
							buffer.append(it.next());
							if (it.hasNext()){
								buffer.append(descriptor.getAllPostfix());
							}
						}
					}else if (descriptor.last)
					{
						tagValue = parser.getLastValue(descriptor.getTagName());
					}
					else
					{
						tagValue = parser.getValue(descriptor.getTagName());
					}
					if (descriptor.getLimit() > 0 && tagValue.length() > descriptor.getLimit())
					{
						tagValue = tagValue.substring(0, descriptor.getLimit());
					};
					buffer.append(tagValue);
					if (tagValue.length()>0){
						buffer.append(descriptor.getNotEmptyTagPostfix());
					}
					buffer.append(descriptor.getPostfix());
				};
				value = buffer.toString();
			}
			logger.debug("Value  <" + value + ">");
		}
		catch (Exception e)
		{
			logger.error(TypeConverter.exceptionToString(e));
		};
		return value;
	}


	public Map<String, ShortContentBean> getTypeMap() {
		return typeMap;
	}


	public void setTypeMap(Map<String, ShortContentBean> typeMap) {
		this.typeMap = typeMap;
	}

}

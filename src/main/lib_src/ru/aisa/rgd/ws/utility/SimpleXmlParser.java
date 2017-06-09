package ru.aisa.rgd.ws.utility;

import java.util.ArrayList;
import java.util.List;

import ru.aisa.rgd.ws.documents.ETDForm;

public class SimpleXmlParser {
	
	private String source;
	
	public static String getValue(String xml, String tagName)
	{
		try{
			int i = xml.indexOf("<"+tagName+">")+tagName.length()+2;
			return xml.substring(i, xml.indexOf("</"+tagName+">"));
		}
		catch (Exception e) {
			return "";
		} 
	}
	
	public static int getStageFlag(String xml)
	{
		String flag = getValue(xml, ETDForm.STAGE_FLAG);
		if (flag!= null && !"".equalsIgnoreCase(flag))
			return Integer.parseInt(flag);
		else 
			return -1;
	}
	
	public SimpleXmlParser(String source){
		super();
		this.source = source;
	}
	
	public String getValue(String tagName)
	{
		try{
			int i = source.indexOf("<"+tagName+">")+tagName.length()+2;
			return source.substring(i, source.indexOf("</"+tagName+">"));
		}
		catch (Exception e) {
			return "";
		} 
	}
	
	public String getLastValue(String tagName)
	{
		try{
			int i = source.lastIndexOf("<"+tagName+">")+tagName.length()+2;
			return source.substring(i, source.lastIndexOf("</"+tagName+">"));
		}
		catch (Exception e) {
			return "";
		} 
	}
	
	public List<String> getAllValues(String tagName){
		List<String> list = new ArrayList<String>();
		try{
			int elBegin = 0;
			int elEnd = 0;
			while (elBegin>=0){
				elBegin = source.indexOf("<"+tagName+">", elEnd);
				elEnd = source.indexOf("</"+tagName+">", elEnd+tagName.length()+3);
				if (elBegin>=0 && elBegin < source.length() && elBegin < elEnd){
					list.add(source.substring(elBegin+tagName.length()+2, elEnd));
				}
			}
		}catch (Exception e){
	    	
	    }
		return list;
	}

}

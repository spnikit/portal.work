package com.aisa.portal.invoice.ttk.endpoints;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;






public class FromTTKParser {


private static List<String> regexpppp = new ArrayList<String>();


static {
	regexpppp.add(" xmlns=\"http://aisa.ru/pdpol\"");
	regexpppp.add(" xmlns=\"http://aisa.ru/pdotpr\"");
	regexpppp.add(" xmlns=\"http://aisa.ru/izvpol\"");
	regexpppp.add(" xmlns=\"\"");
}

 
	public byte[] parse (byte[]xml) throws UnsupportedEncodingException{
		
		String xmldata = new String(xml, "windows-1251");
		int i= 0;
		/*while (i<regexpppp.size()){
			xmldata= xmldata.replaceAll(regexpppp.get(i), "");
			i++;
		}*/
		
		return xmldata.getBytes("windows-1251");
	
	}
	
	
}

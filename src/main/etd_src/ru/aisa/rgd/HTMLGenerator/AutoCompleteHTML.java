package ru.aisa.rgd.HTMLGenerator;

import java.util.List;
import java.util.Map;

public class AutoCompleteHTML {
	
	public byte[] generateHTML(byte[] xml, byte[] html) throws Exception {
		List<Map<String,String>> listmaps = DocdataParser.createList(xml);
		String document = HTMLParser.fillHTML(listmaps, html);
		return document.getBytes();
	}
	

}

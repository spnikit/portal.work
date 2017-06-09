/**
 * SaxXmlParser.java (c) AIS@ 2010 - 2010
 */
package ru.aisa.rgd.ws.utility;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * @author maloi
 *
 */
public class SaxXmlParser implements XmlParser{

	private String data;
	/**
	 * 
	 */
	public SaxXmlParser(String data) {
		this.data = data;
	}
	/* (non-Javadoc)
	 * @see ru.aisa.rgd.ws.utility.XmlParser#getAllValues(java.lang.String)
	 */
	public List<String> getAllValues(String tagName) {
		try {
			  javax.xml.parsers.SAXParserFactory saxFactory = javax.xml.parsers.SAXParserFactory.newInstance();  
			  javax.xml.parsers.SAXParser saxParser = saxFactory.newSAXParser();
		        ValueGettor vg = new ValueGettor();
		        vg.setTagName(tagName);
		        ByteArrayInputStream is = new ByteArrayInputStream(data.getBytes("UTF-8"));
		        saxParser.parse(is, vg );
		        is.close();
		        return vg.getResults();
		  } catch (Throwable err) {
			  return new ArrayList<String>();
		  }	
	}

	/* (non-Javadoc)
	 * @see ru.aisa.rgd.ws.utility.XmlParser#getLastValue(java.lang.String)
	 */
	public String getLastValue(String tagName) {
		List<String> vals = getAllValues(tagName);
		return vals.size()>1?vals.get(vals.size()-1):"";
	}
	/* (non-Javadoc)
	 * @see ru.aisa.rgd.ws.utility.XmlParser#getValue(java.lang.String)
	 */
	public String getValue(String tagName) {
		List<String> vals = getAllValues(tagName);
		return vals.size()>0?vals.get(0):"";
	}

}

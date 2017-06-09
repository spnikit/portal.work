/*
 * ValueGettor.java (c) AIS@ 2010-2010
 */
package ru.aisa.rgd.ws.utility;

import java.util.ArrayList;
import java.util.List;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;


public class ValueGettor extends DefaultHandler {
	List<String> results = new ArrayList<String>();
	String tagName = null;
	boolean inTag = false;
	StringBuffer sb = new StringBuffer();
	public void characters(char[] ch, int start, int length) throws SAXException {
		if (inTag) {
			sb.append(new String(ch, start, length));
		}
	}

	public void endElement(String uri, String localName, String name) throws SAXException {
		if (name.equals(tagName)) {
			results.add(new String(sb.toString().trim()));
			sb = new StringBuffer();
		}

		inTag = !name.equals(tagName);
	}

	public void startDocument() throws SAXException {
		results = new ArrayList<String>();
		if (tagName == null) throw new SAXException("tagName not defined.");
	}

	public void startElement(String uri, String localName, String name, Attributes atts) throws SAXException {
		inTag = name.equals(tagName);
	}

	public String getTagName() {
		return tagName;
	}

	public void setTagName(String tagName) {
		this.tagName = tagName;
	}

	public List<String> getResults() {
		return results;
	}

}
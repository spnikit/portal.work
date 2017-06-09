package sheff.rjd.utils;



import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;


public class AisaSaxHandler extends DefaultHandler{
	private String tagname="";
	boolean isend=false;
	private boolean inTagname=false;
	private StringBuffer aa= new StringBuffer();
	public String GetTagData(){
		return aa.toString();
	}
	public void SetTagName(String tagname){
		this.tagname=tagname;
	}
	public void startElement(String uri, String localName, String name, Attributes atts) throws SAXException {
		if (inTagname) aa.append("<"+name+">");
		if (name.equals(tagname) && !isend) {
			inTagname = true;
			aa.append("<"+tagname+">");
		}
		
	}
	public void characters(char[] ch, int start, int length) throws SAXException {
		if (inTagname) {
			aa.append(new String(ch, start, length));
		}
	}
	public void endElement(String uri, String localName, String name) throws SAXException {
		if (inTagname) aa.append("</"+name+">");
		if (name.equals(tagname)) {
			inTagname = false;
			isend=true;
		//	aa.append("</"+tagname+">");
		}
	}
}

package sheff.rjd.dispatcher;

import java.util.LinkedHashSet;
import java.util.Set;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;


public class ValueGettor extends DefaultHandler {
	Set results = new LinkedHashSet();
	String tagName = null;
	boolean inTag = false;
	StringBuffer sb = new StringBuffer();
	public void characters(char[] ch, int start, int length) throws SAXException {
		if (inTag) {
			/*char[] dch = new char[length];
			System.arraycopy(ch, start, dch, 0, length);
			sb.append(dch);
			results.add(new String(dch));*/
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
		results = new LinkedHashSet();
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

	public Set getResults() {
		return results;
	}

}
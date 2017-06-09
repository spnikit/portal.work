package ru.aisa.rgd.utils;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Writer;
import java.util.Properties;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;


public class XMLUtil {
	
	private static Logger	log	= Logger.getLogger(XMLUtil.class);
	
	public static Document createDocument() {
		try {
			DocumentBuilderFactory documentFactory = DocumentBuilderFactory.newInstance();
			documentFactory.setNamespaceAware(true);
			documentFactory.setValidating(false);
			DocumentBuilder docBuilder = documentFactory.newDocumentBuilder();
			return docBuilder.newDocument();
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}

	public static String toXML(Document doc, Source xslt,Writer sw) {
		try 
		{
			Properties format = new Properties();
			format.put(OutputKeys.METHOD, "xml");
			format.put(OutputKeys.ENCODING, "utf-8");
			format.put(OutputKeys.OMIT_XML_DECLARATION, "no");
			format.put(OutputKeys.INDENT, "yes");
			format.put(OutputKeys.STANDALONE, "yes");

			StreamResult sr = new StreamResult();
			
			sr.setWriter(sw);
			DOMSource ds = new DOMSource();
			
			ds.setNode(doc);
			Transformer tr;
			if (xslt == null) {
				tr = TransformerFactory.newInstance().newTransformer();
			} else {
				tr = TransformerFactory.newInstance().newTransformer(xslt);
			}
			
			tr.setOutputProperties(format);
			tr.transform(ds, sr);
			return sr.getWriter().toString();
		} catch (Exception ex) {
			log.error("toXML()", ex);
			return "<error />";
		}
	}	
	static public Document getDOM(String in) {
		try {
			InputStream ins = new ByteArrayInputStream(in.getBytes("UTF-8"));

			return getDOM(ins);
		} catch (Exception pce) {
			pce.printStackTrace();
			return null;
		}
	}

	static public Document getDOM(InputStream in) throws SAXException{
		try {
			DocumentBuilderFactory documentFactory = DocumentBuilderFactory.newInstance();
			documentFactory.setNamespaceAware(true);
			documentFactory.setValidating(false);
			DocumentBuilder docBuilder = documentFactory.newDocumentBuilder();
			return docBuilder.parse(in);
			
		} catch (IOException ex) {
			ex.printStackTrace();
			return null;
		} catch (ParserConfigurationException ex) {
			ex.printStackTrace();
			return null;
		} 
	}
	static public String getValue(Element el) {
		String out = "";
		out = el.getTextContent();
		return out;
	}
	static public Element getChldElement(Element el) {
		NodeList nl = el.getChildNodes();
		for (int i=0;i<nl.getLength();i++) {
			if (nl.item(i).getNodeType() == org.w3c.dom.Node.ELEMENT_NODE) {
				return (Element) nl.item(i);
			}
		}
		return null;
	}
}

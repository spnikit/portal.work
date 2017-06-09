package sheff.rjd.utils;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Properties;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.log4j.Logger;
import org.apache.xml.serialize.OutputFormat;
import org.apache.xml.serialize.XMLSerializer;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import sheff.rjd.dispatcher.ValueGettor;


public class XMLUtil {
	private static Logger				log				= Logger.getLogger(XMLUtil.class);
	private static final String XML_HEADER = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";
	public static Document createDocument() {
		try {
			DocumentBuilderFactory documentFactory = DocumentBuilderFactory.newInstance();
			documentFactory.setNamespaceAware(true);
			documentFactory.setValidating(false);
			DocumentBuilder docBuilder = documentFactory.newDocumentBuilder();
			return docBuilder.newDocument();
		} catch (Exception ex) {
			 StringWriter outError = new StringWriter();
			   PrintWriter errorWriter = new PrintWriter( outError );
			   ex.printStackTrace(errorWriter);
			   log.error(outError.toString());
			return null;
		}
	}
public static String FixString(String in){
	
	return 	in.replace((char) 26, ' ');
	
}
	public static String toXML(Document doc, Source xslt,Writer sw) {
		try {
			Properties format = new Properties();
			format.put(OutputKeys.METHOD, "xml");
			String os = System.getProperty("os.name");
//			if (os != null && os.toUpperCase().indexOf("WIN") != -1) {
//				format.put(OutputKeys.ENCODING, "cp1251");
//			} else {
				format.put(OutputKeys.ENCODING, "utf-8");
//			}

			format.put(OutputKeys.OMIT_XML_DECLARATION, "no");
			format.put(OutputKeys.INDENT, "yes");
			format.put(OutputKeys.STANDALONE, "yes");

//			StringWriter sw = new StringWriter();
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
			 StringWriter outError = new StringWriter();
			   PrintWriter errorWriter = new PrintWriter( outError );
			   pce.printStackTrace(errorWriter);
			   log.error(outError.toString());
			return null;
		}
	}

	static public Document getDOM(InputStream in) {
		try {
			DocumentBuilderFactory documentFactory = DocumentBuilderFactory.newInstance();
			documentFactory.setNamespaceAware(true);
			documentFactory.setValidating(false);
			
			
			DocumentBuilder docBuilder = documentFactory.newDocumentBuilder();
			// docBuilder.setEntityResolver(new NoEntityResolver());
			
			return docBuilder.parse(in);
		} catch (Exception pce) {
			 StringWriter outError = new StringWriter();
			   PrintWriter errorWriter = new PrintWriter( outError );
			   pce.printStackTrace(errorWriter);
			   log.error(outError.toString());
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
	
	static public String parseXmlWithSax(String xml,String value){
		  try {
			  javax.xml.parsers.SAXParserFactory saxFactory = javax.xml.parsers.SAXParserFactory.newInstance();  
			  javax.xml.parsers.SAXParser saxParser = saxFactory.newSAXParser();
		        ValueGettor vg = new ValueGettor();
		        vg.setTagName(value);
		        ByteArrayInputStream is = new ByteArrayInputStream(xml.getBytes("UTF-8"));
		        saxParser.parse(is, vg );
		        is.close();
		        String ret = (String) vg.getResults().iterator().next();
		        return (ret == null?"":ret);
		  } catch (Throwable err) {
			  return "";
		        //err.printStackTrace ();
		  }		  		
	}
	
	public static String getFormattedString(Document document)
	{
		try
		{
			OutputFormat format = new OutputFormat(document);
			format.setLineWidth(65);
			format.setIndenting(true);
			format.setIndent(2);
			Writer out = new StringWriter();
			XMLSerializer serializer = new XMLSerializer(out, format);
			serializer.serialize(document);
			return out.toString();
		}
		catch (IOException e)
		{
			throw new RuntimeException(e);
		}
	}
	
	public static String getFormattedStringWithoutHeader(Document document)
	{
		return getFormattedString(document).replace(XML_HEADER, "");
	}
}

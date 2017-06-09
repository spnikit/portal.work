package ru.aisa.rgd.ws.documents;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

import ru.aisa.rgd.utils.Base64;
import ru.aisa.rgd.utils.XMLUtil;
import ru.aisa.rgd.ws.exeption.ServiceError;
import ru.aisa.rgd.ws.exeption.ServiceException;
import ru.aisa.rgd.ws.utility.DataBinder;


public class ETDForm {
	
	//	Loginning
	protected final Logger	log	= Logger.getLogger(getClass());
	
	private Document document;
	
	public static final String STAGE_FLAG = "sys_stage_flag";
	public static final String STAGE_ROOT_ELEMENT = "data";
	public static final int STAGE_SERVICE = 1;
	public static final int STAGE_MANUAL = 2;
	
	
	
	// Constructors
	public ETDForm(Document document)
	{
		this.document = document;

	};
	
	public static ETDForm createFromDocData(String data) throws ServiceException, IOException
	{
		String str = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" + data;
		ETDForm doc = new ETDForm(str.getBytes("UTF-8"));
		return doc;
	}
	
	public ETDForm(InputStream stream) throws ServiceException, IOException
	{
		try 
		{
			setDocument(XMLUtil.getDOM(stream));
		} 
		catch (SAXException e) 
		{
			throw new ServiceException(e, ServiceError.ERR_DOCUMENT_PARSING);
		}
		finally
		{
			if (stream != null) stream.close();
		}
	};
	
	public ETDForm(byte[] arr) throws ServiceException, IOException
	{
		InputStream stream = new ByteArrayInputStream(arr);
		try 
		{
			setDocument(XMLUtil.getDOM(stream));
		} 
		catch (SAXException e) 
		{
			throw new ServiceException(e, ServiceError.ERR_DOCUMENT_PARSING);
		}
		finally
		{
			if (stream != null) stream.close();
		}
		
		
	};
	
	public Document getDocument() {
		return document;
	}

	public void setDocument(Document document) {
		this.document = document;
	}
	
	// Methods
	public String transform() throws TransformerException
	{
		StringWriter ow = new StringWriter();
		Transformer trans = TransformerFactory.newInstance().newTransformer();
		trans.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
		
		trans.transform(new DOMSource(getDocument()), new StreamResult(ow));
	
		return ow.toString();
	};
	
	public String transform(String tagName) throws TransformerException
	{
		StringWriter ow = new StringWriter();
		Transformer trans = TransformerFactory.newInstance().newTransformer();
		
		Node node =  getDocument().getElementsByTagName(tagName).item(0);
		trans.transform(new DOMSource(node), new StreamResult(ow));
		
		return ow.toString();
	}
	
	public String transformWithId(String rootId, String tagName) throws TransformerException
	{
		StringWriter ow = new StringWriter();
		Transformer trans = TransformerFactory.newInstance().newTransformer();
		
		Element el = getDocument().getElementById(rootId);
		Node node = el.getElementsByTagName(tagName).item(0);
		trans.transform(new DOMSource(node), new StreamResult(ow));
		
		return ow.toString();
	}
	
	public byte[] encodeToArchiv() throws  TransformerException, UnsupportedEncodingException
	{
		String finalbytes = Base64.encodeBytes(transform().getBytes("UTF-8"), Base64.GZIP);
		return ("application/vnd.xfdl;content-encoding=\"base64-gzip\"\n"+finalbytes).getBytes();
		
	}
	
	public static byte[] encodeFormToArchiv(String data) throws  TransformerException, UnsupportedEncodingException
	{
		String finalbytes = Base64.encodeBytes(data.getBytes("UTF-8"), Base64.GZIP);
		return ("application/vnd.xfdl;content-encoding=\"base64-gzip\"\n"+finalbytes).getBytes();
		
	}
	
	static public byte[] decodeFromArchiv(byte[] gzip) throws UnsupportedEncodingException
	{
		String str = new String(gzip, "UTF-8");
		String zipStr = str.replaceFirst("application/vnd.xfdl;content-encoding=\"base64-gzip\"\n", "");
		byte[] data =  Base64.decode(zipStr, Base64.GZIP);
		return data;
	}
	static public byte[] decodeFromTemplate(byte[] gzip) throws UnsupportedEncodingException
	{
		String str = new String(gzip, "UTF-8");
		if (str.startsWith("application/vnd.xfdl;content-encoding=\"base64-gzip\"")){
			String zipStr = str.replaceFirst("application/vnd.xfdl;content-encoding=\"base64-gzip\"\n", "");
			byte[] data =  Base64.decode(zipStr, Base64.GZIP);
			return data;
		} else 
		
		return gzip;
	}
	static public ETDForm createFromArchive(byte[] gzip) throws UnsupportedEncodingException, ServiceException, IOException
	{
		return new ETDForm(decodeFromArchiv(gzip));
	}
	
	public DataBinder getBinder()
	{
		return new DataBinder(this.document);
	}

	@Override
	public String toString() 
	{
		String str = null;
		try
		{
			str =  transform();
		}
		catch (Exception e)
		{
			log.error("Can not transform Xml document");
		}
		return str;
	};
	
	

}

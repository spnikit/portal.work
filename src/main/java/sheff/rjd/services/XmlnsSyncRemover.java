package sheff.rjd.services;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class XmlnsSyncRemover extends DefaultHandler {
    private StringBuffer request =new StringBuffer();
   
    public Object GetRequest(){
	
	
	return  request;
	
    }
    
    
    
    public void startElement(String uri, String localName,
	    String qName, Attributes attributes) throws SAXException {
	    
	
	    if (!qName.contains("Response")){
		
		request.append("<"+qName);
		    
		    request.append(">");
	    }
	    
	    
	    }
    
    public void characters(char ch[], int start, int length)
    throws SAXException {
      
	
	//if (isresponse)
	    request.append(new String(ch, start, length));
      
	
        }
    public void endElement(String uri, String localName, String qName) throws SAXException{
   
	

    if (!qName.contains("Response")){
	request.append("</"+qName+">");
        }
    }
    
}

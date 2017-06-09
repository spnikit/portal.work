/*package sheff.rjd.utils;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class XmlnsRemover extends DefaultHandler{ 
	private StringBuffer request =new StringBuffer();
	public String GetRequest (){
	 	return request.toString();
	}
	public void startElement(String uri, String localName,
	         String qName, Attributes attributes) throws SAXException {
		request.append("<"+qName);
		for (int i=0; i<attributes.getLength(); i++){
			
			if (!attributes.getQName(i).startsWith("xmlns") &&!attributes.getQName(i).startsWith("xsi"))
			{
			    if (attributes.getQName(i).startsWith("id")){
			   request.append(" id"+attributes.getLocalName(i)+"=\""+attributes.getValue(i)+"\"");
		}
			    else request.append(" "+attributes.getLocalName(i)+"=\""+attributes.getValue(i)+"\""); 	    }
		}
		request.append(">");
	}
	public void characters(char ch[], int start, int length)
    throws SAXException {
		request.append(new String(ch, start, length));
	}
	  public void endElement(String uri, String localName, String qName)  throws SAXException{
		  request.append("</"+qName+">");
	  }
}*/

package sheff.rjd.utils;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;
public class XmlnsRemover extends DefaultHandler{ 
private StringBuffer request =new StringBuffer();
private StringBuffer cipher =new StringBuffer();
private boolean CipherValue=false;
public String GetRequest (){
   // System.out.println(request.toString());
    return request.toString(); 
   
    }
public String GetCipher (){ return cipher.toString(); 
}
public void startElement(String uri, String localName,
String qName, Attributes attributes) throws SAXException {
request.append("<"+qName);
if (qName.contains("CipherValue")){ CipherValue=true; }
/*	 for (int i=0; i<attributes.getLength(); i++){ if (!attributes.getQName(i).startsWith("xmlns") &&!attributes.getQName(i).startsWith("xsi")) request.append(" "+attributes.getLocalName(i)+"=\""+attributes.getValue(i)+"\""); }*/
request.append(">");
}
public void characters(char ch[], int start, int length)
throws SAXException {
    request.append(new String(ch, start, length));
    if (CipherValue)cipher.append(new String(ch, start, length)); }
public void endElement(String uri, String localName, String qName) throws SAXException{
request.append("</"+qName+">");

if (qName.contains("CipherValue")){
    CipherValue=false; }
}
}

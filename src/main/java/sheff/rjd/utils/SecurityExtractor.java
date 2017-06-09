/*package sheff.rjd.utils;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class SecurityExtractor extends DefaultHandler{ 

boolean bBinarySecurityToken=false;	
boolean bSignatureValue=false;	
boolean bDigestValue=false;	
	private StringBuffer BinarySecurityToken =new StringBuffer();
	public String getBinarySecurityToken() {
		return BinarySecurityToken.toString();
	}

	public void setBinarySecurityToken(StringBuffer binarySecurityToken) {
		BinarySecurityToken = binarySecurityToken;
	}

	public String  getSignatureValue() {
		return SignatureValue.toString();
	}

	public void setSignatureValue(StringBuffer signatureValue) {
		SignatureValue = signatureValue;
	}

	public String  getDigestValue() {
		return DigestValue.toString();
	}

	public void setDigestValue(StringBuffer digestValue) {
		DigestValue = digestValue;
	}
	private StringBuffer SignatureValue =new StringBuffer();
	private StringBuffer DigestValue =new StringBuffer();

	public void startElement(String uri, String localName,
	         String qName, Attributes attributes) throws SAXException {
 if (qName.contains("BinarySecurityToken")) bBinarySecurityToken=true;
 if (qName.contains("SignatureValue")) bSignatureValue=true;
 if (qName.contains("DigestValue")) bDigestValue=true;
	}
	public void characters(char ch[], int start, int length)
    throws SAXException {
		if (bBinarySecurityToken){BinarySecurityToken.append(new String(ch, start, length));}
		if (bSignatureValue){SignatureValue.append(new String(ch, start, length));}
		if (bDigestValue){DigestValue.append(new String(ch, start, length));}
	}
	  public void endElement(String uri, String localName, String qName)  throws SAXException{
		  if (qName.contains("BinarySecurityToken")) bBinarySecurityToken=false;
		  if (qName.contains("SignatureValue")) bSignatureValue=false;
		  if (qName.contains("DigestValue")) bDigestValue=false;
	  }
}*/

package sheff.rjd.utils;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;
public class SecurityExtractor extends DefaultHandler{
boolean bBinarySecurityToken=false;	
boolean bSignatureValue=false;	
boolean bDigestValue=false;	
private StringBuffer Created =new StringBuffer();
private StringBuffer Expires =new StringBuffer();
private boolean wsuTimestamp=false;
private boolean wsuCreated=false;
private boolean wsuExpires=false;



private boolean certserial = false;
private boolean signature = false;
private boolean isLogin= false;
private boolean isPassword = false;

public String GetExpires (){ return Expires.toString(); }
public String GetCreated (){ return Created.toString(); }
private StringBuffer BinarySecurityToken =new StringBuffer();
public String getBinarySecurityToken() { return BinarySecurityToken.toString(); }
public void setBinarySecurityToken(StringBuffer binarySecurityToken) { BinarySecurityToken = binarySecurityToken; }
public String getSignatureValue() { return SignatureValue.toString(); }
public void setSignatureValue(StringBuffer signatureValue) { SignatureValue = signatureValue; }
public String getDigestValue() { return DigestValue.toString(); }
public void setDigestValue(StringBuffer digestValue) { DigestValue = digestValue; }
private StringBuffer SignatureValue =new StringBuffer();
private StringBuffer DigestValue =new StringBuffer();
private StringBuffer Certserial =new StringBuffer();
private StringBuffer Signedresp =new StringBuffer();
private StringBuffer login =new StringBuffer();
private StringBuffer password =new StringBuffer();


public String getCertserial() {
	return Certserial.toString();
}
public String getLogin() {
	return login.toString();
}
public void setLogin(StringBuffer login) {
	this.login = login;
}
public String getPassword() {
	return password.toString();
}
public void setPassword(StringBuffer password) {
	this.password = password;
}
public void setCertserial(StringBuffer certserial) {
	Certserial = certserial;
}
public String getSignedresp() {
	return Signedresp.toString();
}
public void setSignedresp(StringBuffer signedresp) {
	Signedresp = signedresp;
}
public void startElement(String uri, String localName,
String qName, Attributes attributes) throws SAXException {
if (qName.contains("BinarySecurityToken")) bBinarySecurityToken=true;
if (qName.contains("SignatureValue")) bSignatureValue=true;
if (qName.contains("DigestValue")) bDigestValue=true;
if (qName.contains("Timestamp")){ wsuTimestamp=true; }
if (qName.contains("Created")){ wsuCreated=true; }
if (qName.contains("Expires")){ wsuExpires=true; }
if (qName.contains("certserial")){ certserial=true; }
if (qName.contains("signature")){ signature=true; }
if (qName.contains("Username")){ isLogin=true; }
if (qName.contains("Password")){ isPassword=true; }
}
public void characters(char ch[], int start, int length)
throws SAXException {
if (bBinarySecurityToken){BinarySecurityToken.append(new String(ch, start, length));}
if (bSignatureValue){SignatureValue.append(new String(ch, start, length));}
if (bDigestValue){DigestValue.append(new String(ch, start, length));}
if (wsuTimestamp && wsuCreated) Created.append(new String(ch, start, length));
if (wsuTimestamp && wsuExpires) Expires.append(new String(ch, start, length));
if (certserial){Certserial.append(new String(ch, start, length));}
if (signature){Signedresp.append(new String(ch, start, length));}
if (isLogin){login.append(new String(ch, start, length));}
if (isPassword){password.append(new String(ch, start, length));}
}
public void endElement(String uri, String localName, String qName) throws SAXException{
if (qName.contains("BinarySecurityToken")) bBinarySecurityToken=false;
if (qName.contains("SignatureValue")) bSignatureValue=false;
if (qName.contains("DigestValue")) bDigestValue=false;
if (qName.contains("Timestamp")){ wsuTimestamp=false; }
if (qName.contains("Created")){ wsuCreated=false; }
if (qName.contains("Expires")){ wsuExpires=false; }
if (qName.contains("certserial")){ certserial=false; }
if (qName.contains("signature")){ signature=false; }
if (qName.contains("Username")){ isLogin=false; }
if (qName.contains("Password")){ isPassword=false; }
}
}



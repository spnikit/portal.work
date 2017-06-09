

package sheff.rjd.utils;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.namespace.QName;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;

import org.apache.log4j.Logger;
import org.springframework.ws.WebServiceMessage;
import org.springframework.ws.context.MessageContext;
import org.springframework.ws.server.EndpointInterceptor;
import org.springframework.ws.soap.AbstractSoapMessage;
import org.springframework.ws.soap.SoapBody;
import org.springframework.ws.soap.SoapEnvelope;
import org.springframework.ws.soap.SoapHeader;
import org.springframework.ws.soap.saaj.SaajSoapMessage;
import org.springframework.xml.transform.StringResult;
import org.springframework.xml.transform.StringSource;

import ru.aisa.rgd.utils.Base64;
//import ru.aisa.rgd.utils.XMLFormatter;
import ru.aisa.rgd.ws.exeption.ServiceCode;
import ru.aisa.rgd.ws.exeption.ServiceException;
public class SecurityAuthInterceptor implements EndpointInterceptor {
SecurityManager securityManager;
private String fmtStr = "yyyy-MM-dd'T'HH:mm:ss.SSSZ"; 
private int tsOffset=4000;
public int getTsOffset() { return tsOffset; }
public void setTsOffset(int tsOffset) { this.tsOffset = tsOffset; }
public String getFmtStr() { return fmtStr; }
public void setFmtStr(String fmtStr) { this.fmtStr = fmtStr; }
private static Logger	log	= Logger.getLogger(SecurityInterceptor.class);
public SecurityManager getSecurityManager() { 
    return securityManager; 
    
}
public void setSecurityManager(SecurityManager securityManager) { 
    this.securityManager = securityManager;
    
}
private int checkSig=0;
public boolean handleRequest(MessageContext messageContext,Object arg1) throws Exception{
	
   	long start1 = System.currentTimeMillis();
   
 //System.out.println(start1);
    WebServiceMessage requestMessage = messageContext.getRequest();

    AbstractSoapMessage abstractSaajMessage = (AbstractSoapMessage) requestMessage;
    SaajSoapMessage saajSoapMessage = (SaajSoapMessage) abstractSaajMessage;
   
    TransformerFactory tf=TransformerFactory.newInstance();
    Transformer transformer=tf.newTransformer();
    
    SoapEnvelope soapEnvelop = saajSoapMessage.getEnvelope();
   StringResult hed=new StringResult();

    SoapHeader soapHeader = soapEnvelop.getHeader();
   
   
    transformer.transform(soapHeader.getSource(),hed);
   // System.out.println(transformer.transform(soapHeader.getSource(),hed));
  //System.out.println(hed);
    
    SAXParserFactory factory = SAXParserFactory.newInstance();
    SecurityExtractor SecHandler = new SecurityExtractor();
   
    InputStream ins = new ByteArrayInputStream(hed.toString().getBytes());
    SAXParser saxParser = factory.newSAXParser();
    saxParser.parse(ins, SecHandler); 
    
    ins.close();
   String login = SecHandler.getLogin();
   String password = SecHandler.getPassword();
//System.out.println(login);
//System.out.println(password);
   // if (CheckTS(start, end)){
	if (true){
    StringResult sr=new StringResult();
   // transformer.setOutputProperty(OutputKeys.CDATA_SECTION_ELEMENTS, "{http://www.aisa.ru/edt}xdata");
    transformer.transform(saajSoapMessage.getPayloadSource(),sr);
    System.out.println(sr.toString());
   /* ins = new ByteArrayInputStream(sr.toString().getBytes("UTF-8"));
    //System.out.println(sr.toString());
    saxParser = factory.newSAXParser();
    XmlnsRemover handler = new XmlnsRemover();
   saxParser.parse(ins, handler); 
        ins.close();*/
   /* String ts = GetSecurityWithTs(start, end);
    checkSig = securityManager.checkSignatureXml((ts+handler.GetRequest()).getBytes("UTF-8"),Base64.decode(signature),cert, hash);	
   */
  
    //checkSig = securityManager.checkSignatureXml(sr.toString().getBytes("UTF-8"), Base64.decode(signature), cert, "");
    
    checkSig=0;
    
    switch (checkSig){
        
    case 0:{
	//System.out.println("check");
    	StringSource bodySource=new StringSource(sr.toString()); 
	transformer.transform(bodySource, saajSoapMessage.getSoapBody().getPayloadResult());
	
    return true;

    
	/*StringResult hed2 = new StringResult();
	transformer.transform(soapEnvelop.getSource(), hed2); 
	System.out.println("soapEnvelop.getSource() "+hed2.toString());
	 return true;*/
    
    }
    default :{


    throw new ServiceException(null,ServiceCode.ERR_INTERCEPTOR_SGN_ERROR,"Ошибка проверки ЭП ");/*handleFault(messageContext, checkSig);*/

    }
    }

    } else{

    checkSig =5;
    throw new ServiceException(null,ServiceCode.ERR_INTERCEPTOR_SGN_ERROR,"Ошибка проверки ЭП ");
    } 

}
public boolean handleFault(MessageContext arg0, Object arg1 )
throws Exception {
 
WebServiceMessage responseMessage = arg0.getResponse();
AbstractSoapMessage abstractSaajMessage = (AbstractSoapMessage) responseMessage;
SaajSoapMessage saajSoapMessage = (SaajSoapMessage) abstractSaajMessage;

String faulttext="";
switch ( checkSig){
case 1:{ faulttext="Пользователь не зарегестрирован в БД"; break; }
case 2:{ faulttext="Сертификат пользователя отозван"; break; }
case 3:{ faulttext="ЭП неверна"; break; }
case 4:{ faulttext="Не удалось расшифровать поток данныx"; break; }
case 5:{ faulttext="Метка времени неверна"; break; }
default :{ faulttext="Неизвестная ошибка"; break; }
}
SoapEnvelope soapEnvelop = saajSoapMessage.getEnvelope();

SoapBody soapBody = soapEnvelop.getBody();
soapBody.addServerOrReceiverFault(faulttext,Locale.getDefault());
QName code =new QName("code");
soapBody.getFault().addFaultDetail().addAttribute(code, String.valueOf(checkSig));
return false;
}

private boolean CheckTS(String start, String end) {
try{ SimpleDateFormat sdf = new java.text.SimpleDateFormat(fmtStr);
Date date_start= sdf.parse(start);
Date date_end= sdf.parse(end);
Calendar calendar = Calendar.getInstance(); 
Date myDate= calendar.getTime(); 
calendar.add(calendar.MILLISECOND, tsOffset); 
Date lastdate=calendar.getTime(); 
if (myDate.after(date_end)) return false; 
else if (lastdate.after(date_end)) return false;
else if (myDate.before(date_start))return false; 
else return true; 
} catch (Exception e){
    StringWriter outError = new StringWriter();
    PrintWriter errorWriter = new PrintWriter( outError );
    e.printStackTrace(errorWriter);
    log.error(outError.toString()); }
return false; 
}
public byte[] encrypt_GHOST(byte[] encrypted_text) {
try{
    byte[]decrypted_text= securityManager.EncryptMsg(encrypted_text);
   // System.out.println("securityManager.DecryptMsg "+new String(securityManager.DecryptMsg(decrypted_text), "UTF-8"));
    return decrypted_text ;
    } catch (Exception e){
	StringWriter outError = new StringWriter(); 
	PrintWriter errorWriter = new PrintWriter( outError ); 
	e.printStackTrace(errorWriter); 
	log.error(outError.toString()); 
	checkSig=4;
	}
return null;
}

private String GetSecurityWithTs(String start, String end){
    String data="<wsse:Security xmlns:wsse=\"http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-secext-1.0.xsd\" xmlns:wsu=\"http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-utility-1.0.xsd\">"+ " <wsu:Timestamp wsu:Id=\"Timestamp-1\" xmlns:wsu=\"http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-utility-1.0.xsd\">"+ " <wsu:Created>"+start+"</wsu:Created>"+ " <wsu:Expires>"+end+"</wsu:Expires>"+ " </wsu:Timestamp>"+ "</wsse:Security>";
    return data;
    }

public String decrypt_GHOST(byte[] encrypted_text) {
try{
    byte[]decrypted_text = securityManager.DecryptMsg(encrypted_text);
   // System.out.println("DecryptMsg "+new String (decrypted_text, "UTF-8"));
    return new String (decrypted_text, "UTF-8");



} 
catch (Exception e){ e.printStackTrace();
StringWriter outError = new StringWriter(); 
PrintWriter errorWriter = new PrintWriter( outError ); 
e.printStackTrace(errorWriter); 
log.error(outError.toString()); 
checkSig=4; 	
}
return null;
}
public String decrypt_AES(byte[] encrypted_text) {
try{
    String decrypted_text = null;
    KeyGenerator kgen = KeyGenerator.getInstance("AES"); 
    kgen.init(128); 
    //SecretKey skey = kgen.generateKey(); 
    byte[] raw = "aaaaaaaaaaaaaaaa".getBytes("UTF-8"); 
    SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES"); 
    Cipher cipher = Cipher.getInstance("AES"); 
    cipher.init(Cipher.DECRYPT_MODE, skeySpec);
    byte[] original = cipher.doFinal(encrypted_text);
    // decrypted_text = new String(original); 
     return decrypted_text; 
    } catch (Exception e){
	StringWriter outError = new StringWriter(); 
	PrintWriter errorWriter = new PrintWriter( outError ); 
	e.printStackTrace(errorWriter); 
	log.error(outError.toString()); 
}
return null;
}
public boolean handleResponse(MessageContext messageContext, Object arg1) throws Exception {
  
    long start1 = System.currentTimeMillis();
    WebServiceMessage requestMessage = messageContext.getResponse();
   AbstractSoapMessage abstractSaajMessage = (AbstractSoapMessage) requestMessage; 
    SaajSoapMessage SaajSoapMessage = (SaajSoapMessage) abstractSaajMessage; 
   
    TransformerFactory tf=TransformerFactory.newInstance();
   
     StringResult sr=new StringResult(); 
    Transformer transformer=tf.newTransformer(); 
    
    transformer.transform(SaajSoapMessage.getPayloadSource(),sr); 
    SoapEnvelope soapEnvelop = SaajSoapMessage.getEnvelope();
    SoapHeader soapHeader = soapEnvelop.getHeader();

   // String body=ConstractBody(sr.toString().getBytes("UTF-8"));
    String body=sr.toString();
     StringSource bodySource=new StringSource(body); 
    
    soapEnvelop.getBody().getPayloadResult();
   
    soapEnvelop.getBody().getPayloadResult();
    
    transformer.transform(bodySource, soapEnvelop.getBody().getPayloadResult()); 
   
    return true; 
    }
private String ConstractBody(byte[] in) throws Exception{ 
    return CreateBody(Base64.encodeBytes(encrypt_GHOST(in) , Base64.GZIP), "Secret"); 
    }
private String CreateBody (String cipher, String KeyName){ 
    String body=" <xenc:EncryptedData xmlns:xenc=\"http://www.w3.org/2006/10/xmlenc#\" Type=\"http://www.w3.org/2006/10/xmlenc#Content\">" + "<xenc:EncryptionMethod Algorithm=\"http://www.w3.org/2006/10/xmldsig-gost#gost28147\"/> "+ "<ds:KeyInfo xmlns:ds=\"http://www.w3.org/2000/09/xmldsig#\">"+ "<ds:KeyName>"+KeyName+"</ds:KeyName>"+ " </ds:KeyInfo>"+ " <xenc:CipherData>" +"<xenc:CipherValue>"+cipher+"</xenc:CipherValue>" +"</xenc:CipherData>" + "</xenc:EncryptedData>"; 
   
    return body;
    }
public void afterCompletion(MessageContext arg0, Object arg1, Exception arg2)
	throws Exception {
    // TODO Auto-generated method stub
    
}
}



package sheff.rjd.controllers;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.StringWriter;
import java.net.URLDecoder;
import java.sql.Types;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.GZIPInputStream;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.log4j.Logger;
import org.springframework.jdbc.core.SqlOutParameter;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;
import org.springframework.ws.client.core.WebServiceTemplate;
import org.springframework.ws.soap.client.core.SoapActionCallback;
import org.w3c.dom.CDATASection;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import sheff.rjd.utils.Base64;
import sheff.rjd.utils.Base64Codec;
import sheff.rjd.utils.MyStoredProc;
import sheff.rjd.utils.XMLUtil;

public class ShepSaveController extends AbstractController {
	
	public ShepSaveController() {
		}
	
	private static Logger	log	= Logger.getLogger(ShepSaveController.class);
	private String URL;
	private DataSource ds;
	private NamedParameterJdbcTemplate npjt;
	
	
	public DataSource getDs() {
		return ds;
	}

	public void setDs(DataSource ds) {
		this.ds = ds;
	}

	public NamedParameterJdbcTemplate getNpjt() {
		return npjt;
	}

	public void setNpjt(NamedParameterJdbcTemplate npjt) {
		this.npjt = npjt;
	}

	public String getURL() { 
		return URL;
	}

	public void setURL(String url) {
		URL = url;
	}
	
	private byte[] Base64GzipToBinary(byte theData[]) throws Exception
{
    byte theDecodedBytes[] = null;
    theDecodedBytes = Base64Codec.base64Decode(theData);
    try
    {
        ByteArrayInputStream theStream = new ByteArrayInputStream(theDecodedBytes) {

            public synchronized int read()
            {
                int test = super.read();
                return test;
            }

        };
        GZIPInputStream theGzipStream = new GZIPInputStream(theStream);
        ByteArrayOutputStream theOutput = new ByteArrayOutputStream(2048);
        byte theBytes[] = new byte[2048];
        do
        {
            if(theGzipStream.available() != 1)
            {
                break;
            }
            int numBytes = theGzipStream.read(theBytes);
            if(numBytes < 1)
            {
                break;
            }
            theOutput.write(theBytes, 0, numBytes);
        } while(true);
        theGzipStream.close();
        theDecodedBytes = theOutput.toByteArray();
    }
    catch(Exception e)
    {
        log.error(e);
    }
    return theDecodedBytes;
}

		
	protected ModelAndView handleRequestInternal(HttpServletRequest request, HttpServletResponse response) throws Exception {
		//System.out.println("cookie"+request.getCookies().length);	
		
	    System.out.println(URL);
	    
	    String docnumber="-1";
		String signature="00";
		String username="empty";
		
		String lastsignpersid="-1";
		String predid="-1";
		String wrkid="-1";
		boolean quick = false;
		String certserial="-1";
		if (request.getCookies() != null){
			//System.out.println("cookie"+request.getCookies().length);
			//System.out.println("cookie"+request.getCookies()[0].getName());
			Cookie ck;
			for(int loopIndex = 0; loopIndex < request.getCookies().length; loopIndex++) { 
	            ck = request.getCookies()[loopIndex];
	            if (ck.getName().equals("quickcookie")) quick = true;
        } 
			for(int loopIndex = 0; loopIndex < request.getCookies().length; loopIndex++) { 
	            ck = request.getCookies()[loopIndex];
	            if (quick){
	            	if (ck.getName().equals("quickdocid")) docnumber = ck.getValue();
	                if (ck.getName().equals("quickusername")) username = ck.getValue();
	                if (ck.getName().equals("quicksignature")) signature =ck.getValue();
	                if (ck.getName().equals("quicklastsignpersid")) lastsignpersid =ck.getValue(); 
	                if (ck.getName().equals("quickpredid")) predid =ck.getValue(); 
	                if (ck.getName().equals("quickwrkid")) wrkid =ck.getValue(); 
	                if (ck.getName().equals("quickcertserial")) certserial =ck.getValue();
	               // System.out.println(ck.getValue()+"---");           	
	            }
	            else {
	            	if (ck.getName().equals("docid")) docnumber = ck.getValue();
	                if (ck.getName().equals("username")) username = ck.getValue();
	                if (ck.getName().equals("signature")) signature =ck.getValue();
	                if (ck.getName().equals("lastsignpersid")) lastsignpersid =ck.getValue(); 
	                if (ck.getName().equals("predid")) predid =ck.getValue(); 
	                if (ck.getName().equals("wrkid")) wrkid =ck.getValue(); 
	                if (ck.getName().equals("certserial")) certserial =ck.getValue();
	               // System.out.println(ck.getValue()+"---");	            	
	            }
        } 		
		}
		//URLDecoder dec = new URLDecoder();		
		username = URLDecoder.decode(username,"UTF-8");
		
		response.setContentType("text/html; charset=UTF-8");
		try {
			
			 TransformerFactory tf = TransformerFactory.newInstance();
		     Transformer trans = tf.newTransformer();
		     
		     
			InputStream is = request.getInputStream();
			ByteArrayOutputStream stream = new ByteArrayOutputStream();
			int bt = 0; 
			int counter=0;
			while((bt = is.read()) > -1) {
				if (counter>51) stream.write(bt);
				counter++;
			}
			stream.flush();
	
		
			byte[] bt64 = Base64GzipToBinary(stream.toByteArray());
			String content = new String(bt64,"UTF-8");
			Document doc1 = XMLUtil.getDOM(content);
		
			
			NodeList signatures = doc1.getElementsByTagName("signature");
			ArrayList<String> sigsarr = new ArrayList<String>();
			for (int i = 0; i < signatures.getLength(); i++) {
				Element e = (Element) signatures.item(i);
				if (e.getElementsByTagName("signer").getLength() >0 ){
					sigsarr.add(XMLUtil.getValue((Element)e.getElementsByTagName("signer").item(0)));
				}				
			}
					  
			
			NodeList list = doc1.getElementsByTagName("xforms:instance");
					
		//	for (int i=0;i<list.getLength();i++) {
				Element e = (Element) list.item(0);
				Element ell = XMLUtil.getChldElement(e);  //<data>
				String formnumber="";
				
			//byte[] finalbytes = new byte[0];
				String finalbytes="";
				if (ell.getElementsByTagName("vu_number").getLength()>0){
					if (XMLUtil.getValue((Element) ell.getElementsByTagName("vu_number").item(0)).length() < 1){
						
						if (sigsarr.size()>0){
							 response.getWriter().print("<html><head><meta content='text/html; charset=UTF-8' http-equiv='Content-Type'/></head><body><script type=\"text/javascript\"> parent.resizeLarge('Не разрешается подписывать несформированный документ');history.back();  </script></body></html>");
							 return null;	
						}
						else{
						MyStoredProc sproc = new MyStoredProc(getDs());
						sproc.setSql("snt.GetDoc_M_Num");
						sproc.declareParameter(new SqlOutParameter("out", Types.CHAR));
						sproc.declareParameter(new SqlParameter("predid", Types.INTEGER));
						sproc.declareParameter(new SqlParameter("formid", Types.INTEGER));
						sproc.declareParameter(new SqlOutParameter("DocNum", Types.INTEGER));
						sproc.compile();
						Map<String, Comparable> input = new HashMap<String, Comparable>();
						input.put("predid", predid);
						HashMap<String, String> pp = new HashMap<String, String>();
						pp.put("DOCTYPE", XMLUtil.getValue((Element) ell.getElementsByTagName("formname").item(0)));
						input.put("formid", getNpjt().queryForInt(" select id from snt.doctype where name = :DOCTYPE ", pp));
						Map output = sproc.execute(input);
						formnumber = output.get("DocNum").toString();
						ell.getElementsByTagName("vu_number").item(0).setTextContent(formnumber);
						
						StringWriter temp = new StringWriter();					    
				        trans.transform(new DOMSource(doc1), new StreamResult(temp));
				       	finalbytes = Base64.encodeBytes(temp.toString().getBytes("UTF-8"), Base64.GZIP	);
						}
						
					}
					else formnumber = XMLUtil.getValue((Element) ell.getElementsByTagName("vu_number").item(0));    	
			     }
				
				
				    DocumentBuilderFactory documentFactory = DocumentBuilderFactory.newInstance();
					documentFactory.setNamespaceAware(true);
					documentFactory.setValidating(false);
					DocumentBuilder docBuilder = documentFactory.newDocumentBuilder();
					Document doc =  docBuilder.newDocument();		
					 Element reqElement = doc.createElementNS("http://www.aisa.ru/edt","saveRequest");
				     reqElement.setPrefix("edt");
				     reqElement.setAttribute("id", docnumber);
				     Element el = doc.createElementNS("http://www.aisa.ru/edt","name");
				     el.setPrefix("edt");
				     if (ell.getElementsByTagName("formname").getLength()>0){
				    	 el.setTextContent(XMLUtil.getValue((Element) ell.getElementsByTagName("formname").item(0)));
				     }
				     else el.setTextContent("");
				     reqElement.appendChild(el);
				     el = doc.createElementNS("http://www.aisa.ru/edt","number");
				     el.setPrefix("edt");
				     el.setTextContent(formnumber);
				     //if (ell.getElementsByTagName("vu_number").getLength()>0){
				    //	 el.setTextContent(XMLUtil.getValue((Element) ell.getElementsByTagName("vu_number").item(0)));				    	 
				    // }
				    // else el.setTextContent("-");
				     reqElement.appendChild(el);
				     el = doc.createElementNS("http://www.aisa.ru/edt","crdatetime");
				     el.setPrefix("edt");
				     reqElement.appendChild(el);
				    for (int i = 0; i <sigsarr.size(); i++) {
				    	el = doc.createElementNS("http://www.aisa.ru/edt","signatures");
					     el.setPrefix("edt");
					    el.setTextContent(sigsarr.get(i));	
					     reqElement.appendChild(el);
						
					}			     
				     el = doc.createElementNS("http://www.aisa.ru/edt","xmldata");
				     el.setPrefix("edt");
				     CDATASection cdata;
				     if (finalbytes.length() == 0 ) cdata = doc.createCDATASection(new String(stream.toByteArray(),"UTF-8"));
				     else   cdata = doc.createCDATASection(finalbytes);
				    el.appendChild(cdata);
				     reqElement.appendChild(el);
				     el = doc.createElementNS("http://www.aisa.ru/edt","bldoc");
				     el.setPrefix("edt");
				     el.setTextContent("00");
				     reqElement.appendChild(el);
				     el = doc.createElementNS("http://www.aisa.ru/edt","security");
				     el.setPrefix("edt");
				     Element el2 = doc.createElementNS("http://www.aisa.ru/edt","username");
				     el2.setPrefix("edt");
				     el2.setTextContent(username);
				     el.appendChild(el2);
				     el2 = doc.createElementNS("http://www.aisa.ru/edt","timestamp");
				     el2.setPrefix("edt");
				     el2.setTextContent("123");
				     el.appendChild(el2);
				     el2 = doc.createElementNS("http://www.aisa.ru/edt","sign");
				     el2.setPrefix("edt");
				     el2.setTextContent(signature);
				     el.appendChild(el2);				 
				     el2 = doc.createElementNS("http://www.aisa.ru/edt","logonstatus");
				     el2.setPrefix("edt");
				     el2.setTextContent("true");
				     el.appendChild(el2); 
				     el2 = doc.createElementNS("http://www.aisa.ru/edt","certid");
				     el2.setPrefix("edt");
				     el2.setTextContent(certserial);
				     el.appendChild(el2);
				     reqElement.appendChild(el);
				     el = doc.createElementNS("http://www.aisa.ru/edt","xdata");
				     el.setPrefix("edt");
				     
				    
				     StringWriter sw = new StringWriter();
				     trans.transform(new DOMSource(ell), new StreamResult(sw));
				     cdata = doc.createCDATASection(sw.toString());				    
				     el.appendChild(cdata);
				     reqElement.appendChild(el);
				     doc.appendChild(reqElement);
				     
				     el = doc.createElementNS("http://www.aisa.ru/edt","lastsignpersid");
				     el.setPrefix("edt");
				     el.setTextContent(lastsignpersid);
				     reqElement.appendChild(el);
				     el = doc.createElementNS("http://www.aisa.ru/edt","predid");
				     el.setPrefix("edt");
				     el.setTextContent(predid);
				     reqElement.appendChild(el);
				     el = doc.createElementNS("http://www.aisa.ru/edt","wrkid");
				     el.setPrefix("edt");
				     el.setTextContent(wrkid);
				     reqElement.appendChild(el);
				     
				   /*  TransformerFactory transfomerFactory = TransformerFactory.newInstance();
						Transformer transformer = transfomerFactory.newTransformer();
				        trans.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");       
				        trans.setOutputProperty(OutputKeys.INDENT, "yes");*/
				        DOMSource source = new DOMSource(doc);
				        StringWriter output = new StringWriter();
				  //      StringWriter output2 = new StringWriter();
				  //      trans.transform(source, new StreamResult(output2));
				  //     FileOutputStream fio = new FileOutputStream("save2.xml");
				  //      fio.write((output2.toString().getBytes("UTF-8")));
				  //      fio.close();
				    response.getWriter().print("<html><head><meta content='text/html; charset=UTF-8' http-equiv='Content-Type'/></head><body><center><h2>Пожалуйста, подождите.</h2></center><p><center><h2>Документ сохраняется.</h2></center></body></html>");
				      
				 		   
				    WebServiceTemplate wst = new WebServiceTemplate();
					 wst.setDefaultUri(URL);
					 System.out.println(URL);
					wst.sendSourceAndReceiveToResult(source, new SoapActionCallback("http://www.aisa.ru/edt/saveRequest"), new StreamResult(output));
					
					String saveparam="";
				     String saveresp = output.toString();
				     if (quick){
				    	 if (saveresp.indexOf("no num")>-1) saveparam="111";
					     if (saveresp.indexOf("success")>-1) saveparam="1";
					     if (saveresp.indexOf("error when")>-1) saveparam="222";
					     if (saveresp.indexOf("sign")>-1) saveparam="333";
					     if (saveresp.indexOf("morethan")>-1) saveparam="444";
					     if (saveresp.indexOf("doctypeflow")>-1) saveparam="555";
				     }
				     else {
				    	 if (saveresp.indexOf("no num")>-1) saveparam="Повторяющийся номер документа.";
					     if (saveresp.indexOf("success")>-1) saveparam="1";
					     if (saveresp.indexOf("error when")>-1) saveparam="Ошибка базы данных.";
					     if (saveresp.indexOf("sign")>-1) saveparam="Неверная подпись.";
					     if (saveresp.indexOf("morethan")>-1) saveparam="Не разрешается больше одной подписи за раз.";
					     if (saveresp.indexOf("doctypeflow")>-1) saveparam="Неверная последовательность подписей.";
				     }
				     
				     response.getWriter().print("<html><head><meta content='text/html; charset=UTF-8' http-equiv='Content-Type'/></head><body><script type=\"text/javascript\">history.back(); parent.resizeLarge('"+saveparam+"');  </script></body></html>");
		
				//}				
			
				
		} catch (Exception ex) {
			log.error("shepSaveCont"+ex);
			response.getWriter().print("<html><head><meta content='text/html; charset=UTF-8' http-equiv='Content-Type'/></head><body><script type=\"text/javascript\">parent.resizeLarge('"+ex.getStackTrace()[0].toString()+"');  history.back();</script></body></html>");
			
		}
		
		return null;
	}

	/*private boolean checkformatofsigs(ArrayList<String> sigsarr) {
		for (Iterator iter = sigsarr.iterator(); iter.hasNext();) {
			String element = (String) iter.next();
			if (element.split(" ").length!=3 || element.contains("@") || element.contains(",")) return false;
			
		}
		return true;
	}*/
	
	
	
	

}

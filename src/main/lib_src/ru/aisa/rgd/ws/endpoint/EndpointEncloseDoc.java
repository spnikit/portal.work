package ru.aisa.rgd.ws.endpoint;

import java.io.ByteArrayInputStream;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.ContentHandlerFactory;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.xmlbeans.XmlException;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.ParameterizedRowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcTemplate;
import org.springframework.oxm.Marshaller;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;
import org.springframework.ws.server.endpoint.AbstractSaxPayloadEndpoint;
import org.xml.sax.ContentHandler;

import ru.aisa.edt.DocumentsTableRequestDocument;
import ru.aisa.edt.DocumentsTableResponseDocument;
import ru.aisa.edt.EnclosedDocDocument;
import ru.aisa.edt.Signature;
import ru.aisa.edt.DocumentsTableRequestDocument.DocumentsTableRequest;
import ru.aisa.edt.DocumentsTableResponseDocument.DocumentsTableResponse;
import ru.aisa.edt.DocumentsTableResponseDocument.DocumentsTableResponse.Data;
import ru.aisa.edt.DocumentsTableResponseDocument.DocumentsTableResponse.Data.Doc;
import ru.aisa.edt.EnclosedDocDocument.EnclosedDoc;
import ru.aisa.rgd.etd.dao.ETDFacade;
import ru.aisa.rgd.etd.extend.ShortContentCreator;
import ru.aisa.rgd.etd.objects.ETDDocument;
import ru.aisa.rgd.etd.objects.ETDDocumentFilter;
import ru.aisa.rgd.etd.ws.DocumentSorter;
import ru.aisa.rgd.etd.ws.ETDUpdateBdSecurityEndpoint;
import sheff.rjd.extend.SNMPSender;
import sheff.rjd.utils.BigDocHandler;
import sheff.rjd.utils.SecurityManager;

public class EndpointEncloseDoc
extends
ETDUpdateBdSecurityEndpoint<ContentHandler, EnclosedDocDocument> {

//Форматирование даты и времени
private SimpleDateFormat dateFormater;
private SimpleDateFormat timeFormater;

//Вещь, создающая контент для столбца "Краткое содержание"
private ShortContentCreator shortContentCreator;

public EndpointEncloseDoc(Marshaller marshaller) {
super(marshaller);
}

@Override
protected EnclosedDocDocument composeInvalidSecurityResponce(Signature signature) 
{
EnclosedDocDocument responseDocument = EnclosedDocDocument.Factory.newInstance();
EnclosedDoc response = responseDocument.addNewEnclosedDoc();
response.addNewSecurity().setLogonstatus("false");
//InputStream in=new ByteArrayInputStream(responseDocument.toString().getBytes());
//StreamSource source=new StreamSource(in);
return responseDocument;//source;
}

@Override
protected ContentHandler convertRequest(Object obj) {
	
	BigDocHandler requestDocument = (BigDocHandler) obj;
	return requestDocument;
}


@Override
protected EnclosedDocDocument processRequest(ContentHandler requestDocument, Signature signature, ETDFacade facade) throws Exception 
{
	
	StreamSource source;
	
  	final BigDocHandler input=(BigDocHandler)requestDocument;
	
	final EnclosedDocDocument out=EnclosedDocDocument.Factory.newInstance();
	out.addNewEnclosedDoc();
	out.getEnclosedDoc().setSecurity(signature);
	final Map pp = new HashMap();
	pp.put("DOCID",Integer.parseInt(input.DocId));
	final int docid=Integer.parseInt(input.DocId);
	if (input.request.equals("clear")){
		
		byte[] signatur = input.sign;
		String certid = input.certid;
		String username = input.username;
		//if (getSecurityManager().checkSignature(username, signature, certid)) {
			
			getTransTemplate().execute(new TransactionCallbackWithoutResult() {
				
				  protected void doInTransactionWithoutResult(TransactionStatus status) {
			        try{
			 if (!input.BaseDocId.equals("-1")){
				 final List <Integer> result=new ArrayList<Integer>();
						 pp.put("BASEDOCID", Integer.parseInt(input.BaseDocId));
						
						 getNpjt().query("select ID from SNT.encloseddocs where DOCID=:BASEDOCID", pp, new ParameterizedRowMapper<Object>()	{
				    		   
				    			public Object mapRow(ResultSet rs, int numrow) {
				    				try{
				    					
				    				result.add(rs.getInt("ID"));
				    				
				    				}
				    				catch (Exception e) {
				    					 StringWriter outError = new StringWriter();
				    					   PrintWriter errorWriter = new PrintWriter( outError );
				    					   e.printStackTrace(errorWriter);
				    					   log.error(outError.toString());
				    				}
				    				return null;
				    			}
				    			
				    		});
						
						 for (int i=0; i<result.size();i++){
						 pp.put("ENCID",result.get(i));
							getNpjt().update("insert into SNT.ENCLOSEDDOCS (DOCID,NAME,fname,datagroup,ID,template,updt, TSCREATE) "+
								" values ( :DOCID,(select NAME from SNT.ENCLOSEDDOCS where docid=:BASEDOCID and ID=:ENCID )," +
								" (select FNAME from SNT.ENCLOSEDDOCS where docid=:BASEDOCID and ID=:ENCID )," +
								" (select datagroup from SNT.ENCLOSEDDOCS where docid=:BASEDOCID and ID=:ENCID )" +
								",:ENCID,(select template from SNT.ENCLOSEDDOCS where docid=:BASEDOCID and ID=:ENCID ),0,current timestamp ) ", pp);
						 }
						
						 
						 
						 } else {
							 getNpjt().update("update SNT.ENCLOSEDDOCS set updt=0 where DOCID=:DOCID and updt<>0",pp);
							
						 }
			 
			    String idstodel=input.IdsForDel;
			    
				if(idstodel.indexOf("|")>-1){
					String[] ids=idstodel.split("\\|");
					 ArrayList<Object[]> aa = new ArrayList<Object[]>();
					for (int i=0; i<ids.length;i++) {aa.add(new Object[]{ids[i],docid});
					}
					getJt().batchUpdate("update SNT.ENCLOSEDDOCS set updt=-1 where id=? and DOCID=?", aa);
	              	
				}
				out.getEnclosedDoc().setResponse("ok");
			}
			catch(Exception ex){
				  StringWriter outError = new StringWriter();
				   PrintWriter errorWriter = new PrintWriter( outError );
				   ex.printStackTrace(errorWriter);
				   log.error(outError.toString());
		           status.setRollbackOnly();
		       	out.getEnclosedDoc().setResponse("save_error");
		        SNMPSender.sendMessage(ex);
		 	}
			}});
		
		//}
		
		//else out.getEnclosedDoc().setResponse("false");
		
		InputStream in=new ByteArrayInputStream(out.toString().getBytes());
		source=new StreamSource(in);
		return out;//source;
		

	}
	if (input.request.equals("save")){
		
		byte[] signatur = input.sign;
		String certid = input.certid;
		String username = input.username;
		//if (getSecurityManager().checkSignature(username, signature, certid)) {
		
		getTransTemplate().execute(new TransactionCallbackWithoutResult() {
			
	         protected void doInTransactionWithoutResult(TransactionStatus status) {
		try{
	
	final List <String> result=new ArrayList<String>();
	List <String> idList = new ArrayList<String>();
	int iter=0;
	
	while (iter<input.name.size()){
		idList.add(input.name.get(iter));
		iter++;
	}
	
	HashMap idsMap = new HashMap();
	idsMap.put("ID", idList);
	idsMap.put("DOCID",docid);
	
	
	getNpjt().query("select id from SNT.ENCLOSEDDOCS where docid=:DOCID", idsMap, new ParameterizedRowMapper<Object>()	{
		   
		   
		public Object mapRow(ResultSet rs, int numrow) {
			try{
				
			result.add(rs.getString("Id"));
		
			}
			catch (Exception e) {
				 StringWriter outError = new StringWriter();
				   PrintWriter errorWriter = new PrintWriter( outError );
				   e.printStackTrace(errorWriter);
				   e.printStackTrace(System.out);
				   log.error(outError.toString());
			}
			return null;
		}
		
	});
  
 ArrayList<Object[]> aa = new ArrayList<Object[]>();
 for (int i=0; i<result.size();i++){

    	for (int test=0; test<input.name.size();test++)
    	{	
      	if( input.name.get(test).equals(result.get(i))){
        aa.add(new Object[]{input.blobs.get(test).getBytes("UTF-8"),result.get(i), docid});
         
        input.name.remove(test);
             input.blobs.remove(test);
             input.fname.remove(test);
             input.datagroupref.remove(test);
    		break;
     	} 
    	}	
        }
 if (result.size()>0)getJt().batchUpdate("update SNT.ENCLOSEDDOCS set bcp_template=?, updt=1 where ID=? and DOCID=? ", aa);
  	

     aa= new ArrayList<Object[]>();
	for (int i=0; i<input.name.size();){

		 aa.add(new Object[]{docid,input.blobs.get(i).getBytes("UTF-8"),input.blobs.get(i).getBytes("UTF-8"),input.name.get(i),input.fname.get(i),input.datagroupref.get(i)});

input.name.remove(i);
input.blobs.remove(i);
input.fname.remove(i);
input.datagroupref.remove(i);
	}
	
	if (aa.size()>0)
	{	
	
getJt().batchUpdate("insert into SNT.ENCLOSEDDOCS (DOCID,NAME,template,bcp_template,ID,updt, TSCREATE,fname,datagroup) "+
		   	" values ( ?,'data',?, ?,?,0,current timestamp,?,? ) ",aa  );
	
	}
	out.getEnclosedDoc().setResponse("ok");
	
      }
		catch(Exception ex){
			  StringWriter outError = new StringWriter();
			   PrintWriter errorWriter = new PrintWriter( outError );
			   ex.printStackTrace(errorWriter);
			   ex.printStackTrace(System.out);
			   log.error(outError.toString());
	           status.setRollbackOnly();
	       	out.getEnclosedDoc().setResponse("save_error");
	        SNMPSender.sendMessage(ex);
	 	}
		}});
	
	//}
	//else out.getEnclosedDoc().setResponse("false");
	
InputStream in=new ByteArrayInputStream(out.toString().getBytes());
source=new StreamSource(in);

return out;//source;
       

	}
	

	return null;
}


public SimpleDateFormat getDateFormater() {
return dateFormater;
}

public void setDateFormater(SimpleDateFormat dateFormater) {
this.dateFormater = dateFormater;
}

public SimpleDateFormat getTimeFormater() {
return timeFormater;
}

public void setTimeFormater(SimpleDateFormat timeFormater) {
this.timeFormater = timeFormater;
}

public ShortContentCreator getShortContentCreator() {
return shortContentCreator;
}

public void setShortContentCreator(ShortContentCreator shortContentCreator) {
this.shortContentCreator = shortContentCreator;
}

@Override
protected Signature getSecurity(ContentHandler requestDocument) {
	Signature s = Signature.Factory.newInstance();
	s.setSign(((BigDocHandler)requestDocument).sign);//requestDocument.getDocumentsTableRequest().getSecurity();
	return s;
	
}

@Override
protected ContentHandler createContentHandler() throws Exception {
	return new BigDocHandler();
}




}

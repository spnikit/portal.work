package sheff.rjd.services;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.springframework.jdbc.core.simple.SimpleJdbcTemplate;
import org.springframework.ws.client.core.WebServiceTemplate;
import org.springframework.ws.soap.client.core.SoapActionCallback;
import rzd8888.gvc.etd.was.etd.initsync.InitsyncRequestDocument.InitsyncRequest;
import rzd8888.gvc.etd.was.etd.initsync.InitsyncRequestDocument;
public class SyncDetails implements Runnable{
   
    
    private WebServiceTemplate wst;
    private SimpleJdbcTemplate sjt;
    private String url;
    
    public SyncDetails (WebServiceTemplate wst, SimpleJdbcTemplate sjt, String url ){
	this.wst = wst;
	this.sjt = sjt;
	this.url = url;
	
}
    public void run() {
	
	new Thread(){
	    public void run(){
		try{
		    
		    //
		    
		    InitsyncRequestDocument doc = InitsyncRequestDocument.Factory.newInstance();
		    InitsyncRequest   req = doc.addNewInitsyncRequest();
		    req.setDosync(1);
		Thread.sleep(5000);
		int okpo = sjt.getNamedParameterJdbcOperations().queryForInt("select max(okpo_kod) from snt.egrpo", new HashMap());
		
		System.out.println(okpo);
		wst.marshalSendAndReceive(url, req);
		
		System.out.println("SyncDetails");
		}	catch (Exception e){
		    
		}
	    }

	    
	}.start();	
    }

}

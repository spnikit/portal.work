package ru.aisa.rgd.etd.extend;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Iterator;
import java.util.Map;
import java.util.TimerTask;

import org.apache.log4j.Logger;

import ru.aisa.rgd.etd.dao.ETDFacade;

public class DeleteDocsTask extends TimerTask{
	
	private Map signedDocsMap;
	private Map notSignedDocsMap;
	private ETDFacade etdFacade;
	
	private static Logger log = Logger.getLogger(DeleteDocsTask.class);

	public ETDFacade getEtdFacade() {
		return etdFacade;
	}



	public void setEtdFacade(ETDFacade etdFacade) {
		this.etdFacade = etdFacade;
	}



	public Map getSignedDocsMap() {
		return signedDocsMap;
	}



	public void setSignedDocsMap(Map signedDocsMap) {
		this.signedDocsMap = signedDocsMap;
	}



	public void setNotSignedDocsMap(Map notSignedDocsMap) {
		this.notSignedDocsMap = notSignedDocsMap;
	}



	public Map getNotSignedDocsMap() {
		return notSignedDocsMap;
	}



	@Override
	public void run() {
		try{		
			Iterator iterator = signedDocsMap.keySet().iterator();
		    while (iterator.hasNext()) {
		    	Thread.sleep(60000);   //time between each delete
		    	String docname = (String) iterator.next();
		      
		    	try{
		    		etdFacade.deleteByLastsign(signedDocsMap.get(docname).toString(), docname );
		    	}catch (Exception e) {
					StringWriter outError = new StringWriter();
					PrintWriter errorWriter = new PrintWriter(outError);
					e.printStackTrace(errorWriter);
					log.error(outError.toString());
				}
		    }
		    
		    iterator = notSignedDocsMap.keySet().iterator();
		    while (iterator.hasNext()) {
		    	Thread.sleep(60000);   //time between each delete
		    	String docname = (String) iterator.next();
		      
		    	try{
		    		etdFacade.deleteNotSignedDocsByCreateDate(notSignedDocsMap.get(docname).toString(), docname );
		    	}catch (Exception e) {
					StringWriter outError = new StringWriter();
					PrintWriter errorWriter = new PrintWriter(outError);
					e.printStackTrace(errorWriter);
					log.error(outError.toString());
				}
		    }
	    }catch (Exception e) {
			StringWriter outError = new StringWriter();
			PrintWriter errorWriter = new PrintWriter(outError);
			e.printStackTrace(errorWriter);
			log.error(outError.toString());
		}
	}
	

}

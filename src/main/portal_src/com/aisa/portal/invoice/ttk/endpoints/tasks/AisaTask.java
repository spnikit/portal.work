package com.aisa.portal.invoice.ttk.endpoints.tasks;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;

import org.apache.log4j.Logger;
import org.springframework.ws.client.core.WebServiceTemplate;

import ru.aisa.rgd.utils.Base64;
import ru.aisa.rgd.ws.utility.TypeConverter;
import rzd8888.gvc.etd.was.etd.recievenotice.RecieveNoticeRequestDocument;
import rzd8888.gvc.etd.was.etd.recievenotice.RecieveNoticeRequestDocument.RecieveNoticeRequest;

import com.aisa.portal.invoice.integration.endpoints.objects.DocumentsObj;
import com.aisa.portal.invoice.integration.facade.PortalSFFacade;

public class AisaTask implements Runnable {
	PortalSFFacade facade;
	ArrayList<DocumentsObj> documents;
	WebServiceTemplate wst;
	private String urltype = "EtdNoticeURL";
	
	
	
	
	protected final Logger	log	= Logger.getLogger(getClass());
	public AisaTask (	ArrayList<DocumentsObj> documents, 	PortalSFFacade facade, WebServiceTemplate wst){
		this.documents=documents;
		this.facade=facade;
		this.wst = wst;
	}
	public void run() {
  
				
		try
		{	
			for (int i=0 ; i<documents.size(); i++)
			{

				 String xml = new String (documents.get(i).getXML(), "windows-1251");
								 
				  String sgn = Base64.encodeBytes(documents.get(i).getSGN());
				  documents.get(i).setNotice(facade.GenerateNotice(documents.get(i).getTypeOfDocument(),xml,sgn));
				
//				  String notice = new String (documents.get(i).getNotice().Generate());
				  
				  RecieveNoticeRequestDocument xmldoc = RecieveNoticeRequestDocument.Factory.newInstance();
				  RecieveNoticeRequest xmlreq  = xmldoc.addNewRecieveNoticeRequest();
//				xmlreq.setXml(notice);
				xmlreq.setXmlbytes(documents.get(i).getNotice().Generate().getBytes("windows-1251"));
				xmlreq.setConfirmid(documents.get(i).getConfirmationId());
				
				long etdid = facade.GetEtdDocid(documents.get(i).getInvoiceId());
				if (etdid==0){
					
					xmlreq.setPortalDocId(documents.get(i).getInvoiceId());
					
				}
				
				xmlreq.setEtdid(facade.GetEtdDocid(documents.get(i).getInvoiceId()));
				
				
				
				xmlreq.setXtype(documents.get(i).getTypeOfNotice());
			
				
				
				
				try{
//					System.out.println(xmldoc);
					wst.marshalSendAndReceive(facade.GetEtdNoticeURL(urltype), xmldoc);
				
				//System.out.println(response);
				} catch (Exception e){
					StringWriter outError = new StringWriter(
							);
				    PrintWriter errorWriter = new PrintWriter(outError);
				    e.printStackTrace(errorWriter);
				    log.error(outError.toString());
				    e.printStackTrace();
				}

			
			
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
			log.error(TypeConverter.exceptionToString(e));
		 
		}
			 
		
	}

}

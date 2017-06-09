package com.aisa.portal.invoice.ttk.endpoints;

import java.util.ArrayList;

import org.apache.xmlbeans.XmlOptions;
import org.apache.xmlbeans.XmlValidationError;
import org.springframework.oxm.Marshaller;

import ru.aisa.pdotpr.ФайлDocument;
import ru.aisa.rgd.ws.dao.ServiceFacade;
import ru.aisa.rgd.ws.documents.StandartResponseWrapper;
import ru.aisa.rgd.ws.endpoint.ResponseAdapter;
import ru.aisa.rgd.ws.exeption.ServiceException;
import ru.aisa.rgd.ws.utility.TypeConverter;
import ru.iit.portal8888.portal.BInvoiceRequestDocument;
import ru.iit.portal8888.portal.BInvoiceRequestDocument.BInvoiceRequest;
import ru.iit.portal8888.portal.BInvoiceResponseDocument;
import ru.iit.portal8888.portal.BInvoiceResponseDocument.BInvoiceResponse;
import sheff.rjd.services.transoil.TransService;

import com.aisa.portal.invoice.integration.endpoints.objects.DocumentsObj;
import com.aisa.portal.invoice.integration.errors.TTKErrors;
import com.aisa.portal.invoice.integration.facade.PortalSFFacade;
import com.aisa.portal.invoice.ttk.endpoints.abstracts.PortalTTKAbstractEndpoint;

public class BuyerInvoiceEndpoint extends PortalTTKAbstractEndpoint<StandartResponseWrapper>{

	protected BuyerInvoiceEndpoint(Marshaller marshaller) {
		super(marshaller);
		// TODO Auto-generated constructor stub
	}
	private TransService sendtotransoil;
	private sheff.rjd.services.etd.TransService sendtonoticeetd;

	public TransService getSendtotransoil() {
		return sendtotransoil;
	}

	public void setSendtotransoil(TransService sendtotransoil) {
		this.sendtotransoil = sendtotransoil;
	}
	public sheff.rjd.services.etd.TransService getSendtonoticeetd() {
		return sendtonoticeetd;
	}

	public void setSendtonoticeetd(
			sheff.rjd.services.etd.TransService sendtonoticeetd) {
		this.sendtonoticeetd = sendtonoticeetd;
	}

	@Override
	protected ResponseAdapter<StandartResponseWrapper> processRequest(Object arg, PortalSFFacade facade)
			throws Exception {
		BInvoiceRequestDocument docreq=(BInvoiceRequestDocument)arg;
		BInvoiceRequest request = docreq.getBInvoiceRequest();
//		System.out.println("CabId: "+request.getCabinetId());
//		System.out.println("SendCabId: "+request.getSenderCabinetId());
	if (facade.CheckSignatureXML(request.getConfirmationXml(),request.getConfirmationSign())){
//			System.out.println("BuyerInvoiceEndpoint");
		  if (facade.getIntegrationDao().updateBuyerInvoice(request.getInvoiceUserId(), 3, request.getConfirmationId(), request.getConfirmationXml(), request.getConfirmationSign()))
			        
		{
			  
			  //Добавить отправку в трансойл
			  
		    	StandartResponseWrapper wrapper = new StandartResponseWrapper();  
		    
		    	
		      	 XmlOptions options = new XmlOptions(); 
		   		 options.setLoadReplaceDocumentElement(new javax.xml.namespace.QName("http://aisa.ru/pdotpr", "Файл")) ;
		   		// options.setValidateOnSet();
		   		 options.setUseDefaultNamespace();
		   		try{
		   			
		   		 String xml=new String(request.getConfirmationXml(), "windows-1251"); 
		   		 
		   		 ФайлDocument fdocument=ФайлDocument.Factory.parse(xml,options); 
		   		
		   		 
		   		 ArrayList<XmlValidationError> errorList=new ArrayList<XmlValidationError>();
		   		 fdocument.validate(options.setErrorListener(errorList)); 
		         if (errorList.size()>0)
		            {
		        	 for (int i=0;i<errorList.size(); i++){
		        		 
		        	 }
		         	 throw new ServiceException(new Exception(),  TTKErrors.ERR_XML_VALID_NOTICE );

		            } 
		    } catch (Exception e){
		    	log.error( TypeConverter.exceptionToString(e));
		     
		    	 throw new ServiceException(new Exception(),  TTKErrors.ERR_XML_VALID_NOTICE );
		    }
		   		getSendtotransoil().SendSFNotice(request.getInvoiceUserId(), 3);
		   		//if(facade.getIntegrationDao().IsSellerServerSGN(request.getInvoiceUserId())){
		   			getSendtonoticeetd().SendSFNotice(request.getInvoiceUserId(), 3,facade.getIntegrationDao().IsSellerServerSGN(request.getInvoiceUserId()));
		   		//}
		    	if (facade.getIntegrationDao().IsBuyerServerSGN(request.getInvoiceUserId())){
		    		
		    	  	ResponseAdapter<StandartResponseWrapper> adapter = new ResponseAdapter<StandartResponseWrapper>( true, wrapper, TTKErrors.ERR_OK);
					return adapter;
		    	}
		    	else {
//		    		System.out.println("sendtotrans");
//		    		 sendtotransoil.SendSFNotice(request.getInvoiceUserId());
		    	  	ResponseAdapter<StandartResponseWrapper> adapter = new ResponseAdapter<StandartResponseWrapper>(false, wrapper, TTKErrors.ERR_OK);
					return adapter;
		    	}
		 
				
		    }else {
		    	StandartResponseWrapper wrapper = new StandartResponseWrapper();  
				ResponseAdapter<StandartResponseWrapper> adapter = new ResponseAdapter<StandartResponseWrapper>(false, wrapper, TTKErrors.ERR_NO_SF_OR_WRONG_STAGE);
				return adapter;
		    }
		
			}
			else{
				StandartResponseWrapper wrapper = new StandartResponseWrapper();  
				ResponseAdapter<StandartResponseWrapper> adapter = new ResponseAdapter<StandartResponseWrapper>(false, wrapper, TTKErrors.ERR_SIGN_NOT_VALID);
				return adapter;
			}
	}

	@Override
	protected Object composeResponce(ResponseAdapter<StandartResponseWrapper> adapter) {
		BInvoiceResponseDocument respdoc=BInvoiceResponseDocument.Factory.newInstance();
		BInvoiceResponse response = respdoc.addNewBInvoiceResponse();
		if (adapter.isStatus())
		{
			response.setCode(adapter.getResponse().getCode());
		 
		}
		else
		{
			response.setCode(adapter.getError().getCode());
		 
		}
		return respdoc;
	}

	@Override
	protected ResponseAdapter<StandartResponseWrapper> processRequest(
			Object arg, ServiceFacade facade) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected ArrayList<DocumentsObj> createDocs(Object arg, String guid)
			throws Exception {
		ArrayList<DocumentsObj> docs=new ArrayList<DocumentsObj> ();
		BInvoiceRequestDocument docreq=(BInvoiceRequestDocument)arg;
		BInvoiceRequest request = docreq.getBInvoiceRequest();
	   	DocumentsObj doc=new DocumentsObj();
    	doc.setConfirmationId(request.getInvoiceId());
    	doc.setInvoiceId(request.getInvoiceUserId());
//    	try{
    	if (!getPortalSFFacade().IsKorrSF(Long.valueOf(request.getInvoiceUserId()))){
    	doc.setTypeOfDocument("Sfact");
    	} else {
    		doc.setTypeOfDocument("KorSfact");
    	}
//    	}catch (Exception e){
//    		e.printStackTrace();
//    	}
    	doc.setTypeOfNotice("BNI");
    	doc.setXML(request.getInvoiceXml());
       	doc.setSGN(request.getInvoiceSign());
    	doc.setKeyName("Test2");
    	docs.add(doc);
 
    	doc=new DocumentsObj();
    	doc.setKeyName("Test2");
    	doc.setConfirmationId(request.getConfirmationId());
    	doc.setInvoiceId(request.getInvoiceUserId());
    	doc.setTypeOfDocument("Pdotpr");
    	doc.setTypeOfNotice("BNIC");
    	doc.setXML(request.getConfirmationXml());
    	doc.setSGN(request.getConfirmationSign());
    
    	docs.add(doc);
		return docs;
	}

}

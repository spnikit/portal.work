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
import ru.iit.portal8888.portal.BConfirmationRequestDocument;
import ru.iit.portal8888.portal.BConfirmationRequestDocument.BConfirmationRequest;
import ru.iit.portal8888.portal.BConfirmationResponseDocument;
import ru.iit.portal8888.portal.BConfirmationResponseDocument.BConfirmationResponse;

import com.aisa.portal.invoice.integration.endpoints.objects.DocumentsObj;
import com.aisa.portal.invoice.integration.errors.TTKErrors;
import com.aisa.portal.invoice.integration.facade.PortalSFFacade;
import com.aisa.portal.invoice.ttk.endpoints.abstracts.PortalTTKAbstractEndpoint;
 

public class BuyerConfirmationEndpoint extends PortalTTKAbstractEndpoint<StandartResponseWrapper>{

	protected BuyerConfirmationEndpoint(Marshaller marshaller) {
		super(marshaller);
		// TODO Auto-generated constructor stub
	}
	private sheff.rjd.services.etd.TransService sendtonoticeetd;
	
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
//		FromTTKParser parsexml = new FromTTKParser();
		BConfirmationRequestDocument docreq=(BConfirmationRequestDocument)arg;
		BConfirmationRequest request = docreq.getBConfirmationRequest();
		
		if (facade.CheckSignatureXML(request.getXml(), request.getSign())){
			System.out.println("BuyerConfirmationEndpoint");
		   // if (facade.getIntegrationDao().updateBuyerConfirmation(request.getInvoiceUserId(), 6, request.getConfirmationId(), request.getXml(), request.getSign()))
			if (facade.getIntegrationDao().updateBuyerConfirmation(request.getInvoiceUserId(), 6, request.getConfirmationId(),request.getXml(), request.getSign()))
				   
			{
		    	StandartResponseWrapper wrapper = new StandartResponseWrapper();  
		    	
		    	 XmlOptions options = new XmlOptions(); 
				 options.setLoadReplaceDocumentElement(new javax.xml.namespace.QName("http://aisa.ru/pdotpr", "Файл")) ;
				 options.setValidateOnSet();
				 options.setUseDefaultNamespace();
			    try{
			    	String xml=new String(request.getXml(), "windows-1251");
				 ФайлDocument fdocument=ФайлDocument.Factory.parse(xml,options); 
		 		 ArrayList<XmlValidationError> errorList=new ArrayList<XmlValidationError>();
				 fdocument.validate(options.setErrorListener(errorList)); 
                 if (errorList.size()>0)
                 {
                   for (int i=0;i<errorList.size(); i++){
		        		 System.out.println(errorList.get(i));
		        	 }
                 	 throw new ServiceException(new Exception(),  TTKErrors.ERR_XML_VALID_NOTICE );
        	     } 
			    } catch (Exception e){
			    	log.error( TypeConverter.exceptionToString(e));
			    	 throw new ServiceException(new Exception(),  TTKErrors.ERR_XML_VALID_NOTICE );
			    }
			    getSendtotransoil().SendSFNotice(request.getInvoiceUserId(), 6);
			    //if(facade.getIntegrationDao().IsSellerServerSGN(request.getInvoiceUserId())){
		   			getSendtonoticeetd().SendSFNotice(request.getInvoiceUserId(), 6, facade.getIntegrationDao().IsSellerServerSGN(request.getInvoiceUserId()));
		   		//}
		    	if (facade.getIntegrationDao().IsBuyerServerSGN(request.getInvoiceUserId())){
		    	  	ResponseAdapter<StandartResponseWrapper> adapter = new ResponseAdapter<StandartResponseWrapper>(true, wrapper, TTKErrors.ERR_OK);
					return adapter;
		    	}
		    	else {
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
		BConfirmationResponseDocument respdoc=BConfirmationResponseDocument.Factory.newInstance();
		BConfirmationResponse response = respdoc.addNewBConfirmationResponse();
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
		BConfirmationRequestDocument docreq=(BConfirmationRequestDocument)arg;
		BConfirmationRequest request = docreq.getBConfirmationRequest();
//		ArrayList<DocumentsObj> allNoticeList  = new ArrayList<DocumentsObj>();
//		allNoticeList = getSendtonoticeetd().GetSFNotice(request.getInvoiceUserId(), request.getConfirmationId(), "SFactRtk");
		DocumentsObj doc = new DocumentsObj();
    	doc.setConfirmationId(request.getConfirmationId());
    	doc.setInvoiceId(request.getInvoiceUserId());
    	doc.setTypeOfDocument("Pdotpr");
    	doc.setTypeOfNotice("BNC");
    	doc.setKeyName("Test2");
    	  	doc.setXML(request.getXml());
    	doc.setSGN(request.getSign());
    	docs.add(doc);
    	
		return docs;
	}

}

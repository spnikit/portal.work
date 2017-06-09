package com.aisa.portal.invoice.ttk.endpoints;

import java.util.ArrayList;
import java.util.List;

import org.apache.xmlbeans.XmlOptions;
import org.apache.xmlbeans.XmlValidationError;
import org.springframework.oxm.Marshaller;

import ru.aisa.pdpol.ФайлDocument;
import ru.aisa.rgd.ws.dao.DocumentDao;
import ru.aisa.rgd.ws.dao.ServiceFacade;
import ru.aisa.rgd.ws.documents.StandartResponseWrapper;
import ru.aisa.rgd.ws.endpoint.ResponseAdapter;
import ru.aisa.rgd.ws.exeption.ServiceException;
import ru.aisa.rgd.ws.utility.TypeConverter;
import ru.iit.portal8888.portal.SConfirmationRequestDocument;
import ru.iit.portal8888.portal.SConfirmationRequestDocument.SConfirmationRequest;
import ru.iit.portal8888.portal.SConfirmationResponseDocument;
import ru.iit.portal8888.portal.SConfirmationResponseDocument.SConfirmationResponse;
import sheff.rjd.services.transoil.TransService;

import com.aisa.portal.invoice.integration.endpoints.objects.DocumentsObj;
import com.aisa.portal.invoice.integration.errors.TTKErrors;
import com.aisa.portal.invoice.integration.facade.PortalSFFacade;
import com.aisa.portal.invoice.ttk.endpoints.abstracts.PortalTTKAbstractEndpoint;

public  class SellerConfirmationEndpoint extends PortalTTKAbstractEndpoint<StandartResponseWrapper>{

	protected SellerConfirmationEndpoint(Marshaller marshaller) {
		super(marshaller);
		// TODO Auto-generated constructor stub
	}
	private TransService sendtotransoil;
	private sheff.rjd.services.etd.TransService sendtonoticeetd;

	 private DocumentDao documentDao;
	 
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

		
	public DocumentDao getDocumentDao() {
		return documentDao;
	}

	public void setDocumentDao(DocumentDao documentDao) {
		this.documentDao = documentDao;
	}

	@Override
	protected ResponseAdapter<StandartResponseWrapper> processRequest(Object arg, PortalSFFacade facade)
			throws Exception {
		SConfirmationRequestDocument docreq=(SConfirmationRequestDocument)arg;
		SConfirmationRequest request = docreq.getSConfirmationRequest(); 
		if (facade.CheckSignatureXML(request.getXml(), request.getSign())){
			
//			System.out.println("SellerConfirmationEndpoint");
			
			 if (facade.getIntegrationDao().updateSellerConfirmation(request.getInvoiceUserId(), 1, request.getConfirmationId(), request.getXml(), request.getSign()))
				    		
			{
			// Проверка наличия пакета в таблице отчетов snt.archivepackrreport

			checkPackage(request.getInvoiceUserId());
			
			
	    	StandartResponseWrapper wrapper = new StandartResponseWrapper();  
	   	   String xml=new String(request.getXml(), "windows-1251"); 
		   	
	    	
	    	 XmlOptions options = new XmlOptions(); 
	   		 options.setLoadReplaceDocumentElement(new javax.xml.namespace.QName("http://aisa.ru/pdpol", "Файл")) ;
	   		 options.setValidateOnSet();
	   		 options.setUseDefaultNamespace();
	   		try{
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
	   		getSendtotransoil().SendSFNotice(request.getInvoiceUserId(), 1);
	   		//if(facade.getIntegrationDao().IsSellerServerSGN(request.getInvoiceUserId())){
	   			getSendtonoticeetd().SendSFNotice(request.getInvoiceUserId(), 1, facade.getIntegrationDao().IsSellerServerSGN(request.getInvoiceUserId()));
	   		//}
	    	if (facade.getIntegrationDao().IsSellerServerSGN(request.getInvoiceUserId())){
//	    		System.out.println("sendtrans");
//	    		sendtotransoil.SendSFNotice(request.getInvoiceUserId());
	    	  	ResponseAdapter<StandartResponseWrapper> adapter = new ResponseAdapter<StandartResponseWrapper>(true, wrapper, TTKErrors.ERR_OK);
				return adapter;
	    	}
	    	else {
	    		
	    	  	ResponseAdapter<StandartResponseWrapper> adapter = new ResponseAdapter<StandartResponseWrapper>(false, wrapper, TTKErrors.ERR_OK);
				return adapter;
	    	}
	    	
	    }
			 
			else {
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
	
	private void checkPackage (long id) {
		try{
			Long pack_count = getDocumentDao().getPackageById(id);
			if (pack_count > 0){
				getDocumentDao().updatePackageForSF(id);
			}
		} catch (Exception e) {
			log.error( TypeConverter.exceptionToString(e));
		}
	}
	
	

	@Override
	protected Object composeResponce(ResponseAdapter<StandartResponseWrapper> adapter) {
		SConfirmationResponseDocument respdoc=SConfirmationResponseDocument.Factory.newInstance();
		SConfirmationResponse response = respdoc.addNewSConfirmationResponse();
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
	protected ArrayList<DocumentsObj> createDocs(Object arg, String guid)
			throws Exception {
		
		ArrayList<DocumentsObj> docs=new ArrayList<DocumentsObj> ();
		SConfirmationRequestDocument docreq=(SConfirmationRequestDocument)arg;
		SConfirmationRequest request = docreq.getSConfirmationRequest();
//		ArrayList<DocumentsObj> allNoticeList  = new ArrayList<DocumentsObj>();
//		allNoticeList = getSendtonoticeetd().GetSFNotice(request.getInvoiceUserId(), request.getConfirmationId(), "SFact");
		DocumentsObj doc=new DocumentsObj();
//		System.out.println(new String (request.getXml(), "windows-1251"));
    	doc.setXML( request.getXml());
    	doc.setSGN(request.getSign());
    	doc.setTypeOfDocument("Pdpol");
    	doc.setTypeOfNotice("SNC");
    	doc.setInvoiceId(request.getInvoiceUserId());
    	doc.setConfirmationId(request.getConfirmationId());
    	doc.setKeyName("Test1");
    	docs.add(doc);
    	
    	
    	
		return docs;
	}
}

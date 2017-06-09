package dumb;

import java.util.ArrayList;

import org.apache.xmlbeans.XmlOptions;
import org.apache.xmlbeans.XmlValidationError;
import org.springframework.oxm.Marshaller;


import com.aisa.portal.invoice.ttk.endpoints.abstracts.PortalTTKAbstractEndpoint;
 

 
 
import ru.aisa.pdpol.ФайлDocument;
import ru.aisa.rgd.ws.dao.ServiceFacade;
import ru.aisa.rgd.ws.documents.StandartResponseWrapper;
 
import ru.aisa.rgd.ws.endpoint.ETDAbstractEndpoint;
import ru.aisa.rgd.ws.endpoint.ResponseAdapter;
import ru.aisa.rgd.ws.exeption.ServiceError;
import ru.aisa.rgd.ws.exeption.ServiceException;
import ru.iit.portal8888.portal.SConfirmationRequestDocument;
import ru.iit.portal8888.portal.SConfirmationRequestDocument.SConfirmationRequest;
import ru.iit.portal8888.portal.SConfirmationResponseDocument;
import ru.iit.portal8888.portal.SConfirmationResponseDocument.SConfirmationResponse;

public  class SellerConfirmationEndpoint extends ETDAbstractEndpoint<StandartResponseWrapper>{

	protected SellerConfirmationEndpoint(Marshaller marshaller) {
		super(marshaller);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected ResponseAdapter<StandartResponseWrapper> processRequest(Object arg, ServiceFacade facade)
			throws Exception {
		SConfirmationRequestDocument docreq=(SConfirmationRequestDocument)arg;
		SConfirmationRequest request = docreq.getSConfirmationRequest(); 
		 
			StandartResponseWrapper wrapper = new StandartResponseWrapper();  
			ResponseAdapter<StandartResponseWrapper> adapter = new ResponseAdapter<StandartResponseWrapper>(false, wrapper, ServiceError.ERR_OK);
			return adapter;
	 
	
		
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



 



}

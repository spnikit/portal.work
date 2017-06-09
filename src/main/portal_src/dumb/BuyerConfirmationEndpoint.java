package dumb;

import java.util.ArrayList;

import org.apache.xmlbeans.XmlOptions;
import org.apache.xmlbeans.XmlValidationError;
import org.springframework.oxm.Marshaller;


import com.aisa.portal.invoice.ttk.endpoints.abstracts.PortalTTKAbstractEndpoint;
 

import ru.aisa.pdotpr.ФайлDocument;
import ru.aisa.rgd.ws.dao.ServiceFacade;
import ru.aisa.rgd.ws.documents.StandartResponseWrapper;
 
import ru.aisa.rgd.ws.endpoint.ETDAbstractEndpoint;
import ru.aisa.rgd.ws.endpoint.ResponseAdapter;
import ru.aisa.rgd.ws.exeption.ServiceError;
import ru.aisa.rgd.ws.exeption.ServiceException;
import ru.iit.portal8888.portal.BConfirmationRequestDocument;
import ru.iit.portal8888.portal.BConfirmationRequestDocument.BConfirmationRequest;
import ru.iit.portal8888.portal.BConfirmationResponseDocument;
import ru.iit.portal8888.portal.BConfirmationResponseDocument.BConfirmationResponse;
 

public class BuyerConfirmationEndpoint extends ETDAbstractEndpoint<StandartResponseWrapper>{

	protected BuyerConfirmationEndpoint(Marshaller marshaller) {
		super(marshaller);
		// TODO Auto-generated constructor stub
	}

	protected ResponseAdapter<StandartResponseWrapper> processRequest(Object arg, ServiceFacade facade)
			throws Exception {
		BConfirmationRequestDocument docreq=(BConfirmationRequestDocument)arg;
		BConfirmationRequest request = docreq.getBConfirmationRequest();
		 
				StandartResponseWrapper wrapper = new StandartResponseWrapper();  
				ResponseAdapter<StandartResponseWrapper> adapter = new ResponseAdapter<StandartResponseWrapper>(false, wrapper, ServiceError.ERR_OK);
				return adapter;
			 
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

 

}

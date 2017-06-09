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
 
import ru.iit.portal8888.portal.SNoticeInvoiceRequestDocument;
import ru.iit.portal8888.portal.SNoticeInvoiceRequestDocument.SNoticeInvoiceRequest;
import ru.iit.portal8888.portal.SNoticeInvoiceResponseDocument;
import ru.iit.portal8888.portal.SNoticeInvoiceResponseDocument.SNoticeInvoiceResponse;

public class SellerNoticeInvoiceEndpoint extends ETDAbstractEndpoint<StandartResponseWrapper>{

	protected SellerNoticeInvoiceEndpoint(Marshaller marshaller) {
		super(marshaller);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected ResponseAdapter<StandartResponseWrapper> processRequest(Object arg, ServiceFacade facade)
			throws Exception {
		SNoticeInvoiceRequestDocument docreq=(SNoticeInvoiceRequestDocument)arg;
		SNoticeInvoiceRequest request = docreq.getSNoticeInvoiceRequest();
		 
				StandartResponseWrapper wrapper = new StandartResponseWrapper();  
				ResponseAdapter<StandartResponseWrapper> adapter = new ResponseAdapter<StandartResponseWrapper>(false, wrapper, ServiceError.ERR_OK);
				return adapter;
	 
	}

	@Override
	protected Object composeResponce(ResponseAdapter<StandartResponseWrapper> adapter) {
		SNoticeInvoiceResponseDocument respdoc=SNoticeInvoiceResponseDocument.Factory.newInstance();
		SNoticeInvoiceResponse response = respdoc.addNewSNoticeInvoiceResponse();
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

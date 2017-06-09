package com.aisa.portal.invoice.notice.endpoints;

import org.springframework.oxm.Marshaller;

import com.aisa.portal.invoice.integration.facade.PortalSFFacade;

import ru.aisa.rgd.ws.dao.ServiceFacade;
import ru.aisa.rgd.ws.endpoint.ETDAbstractEndpoint;
import ru.aisa.rgd.ws.endpoint.ResponseAdapter;

public abstract class Abstract <T>  extends ETDAbstractEndpoint<T> {

	protected Abstract(Marshaller marshaller) {
		super(marshaller);
		// TODO Auto-generated constructor stub
	}
	abstract ResponseAdapter<T> processRequestInvoice(Object arg, PortalSFFacade facade) ;
	abstract Object composeResponceInvoice(ResponseAdapter<T> adapter) ;
	@Override
	protected ResponseAdapter<T> processRequest(Object arg, ServiceFacade facade)
			throws Exception { 
		return processRequestInvoice( arg, (PortalSFFacade) facade);
	}

	@Override
	protected Object composeResponce(ResponseAdapter<T> adapter) {
	 
		return composeResponceInvoice( adapter) ;
	}


}

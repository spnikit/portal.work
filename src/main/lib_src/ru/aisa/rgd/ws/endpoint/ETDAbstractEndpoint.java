package ru.aisa.rgd.ws.endpoint;

import org.apache.log4j.Logger;
import org.springframework.oxm.Marshaller;
import org.springframework.ws.server.endpoint.AbstractMarshallingPayloadEndpoint;

import ru.aisa.rgd.etd.extend.SNMPSender;
import ru.aisa.rgd.ws.dao.ServiceFacade;
import ru.aisa.rgd.ws.exeption.ServiceError;
import ru.aisa.rgd.ws.exeption.ServiceException;
import ru.aisa.rgd.ws.utility.TypeConverter;


public abstract class ETDAbstractEndpoint<T> extends AbstractMarshallingPayloadEndpoint {
	
//	Loginning
	protected final Logger	log	= Logger.getLogger(getClass());
	
//	Context
	protected String templateName;
	protected String needUrl;
	protected ServiceFacade facade;
	
	
	
	protected ETDAbstractEndpoint(Marshaller marshaller) {
		super(marshaller);
	}
	
	public String getNeedUrl() {
		return needUrl;
	}

	public void setNeedUrl(String needUrl) {
		this.needUrl = needUrl;
	}

	public void setTemplateName(String templateName) {
		this.templateName = templateName;
	}

	public String getTemplateName() {
		return templateName;
	}

	public ServiceFacade getFacade() {
		return facade;
	}

	public void setFacade(ServiceFacade facade) {
		this.facade = facade;
	}
	
	protected abstract  ResponseAdapter<T> processRequest(final Object arg, ServiceFacade facade) throws Exception;
	
	protected  abstract  Object composeResponce(ResponseAdapter<T> adapter);
	
	@Override
	protected Object invokeInternal(Object arg) throws Exception 
	{
		
		
		
		Object response = null;
		ResponseAdapter<T> adapter = new ResponseAdapter<T>();

		try
		{
		adapter = processRequest(arg, getFacade());
			
		
		
		}
		catch (ServiceException e)
		{
			e.printStackTrace();
			log.error("Service Error:");
			log.error("code:" + e.getError().getCode() + " message : " + e.getError().getMessage()+" message2 :" + e.getMessage());
			adapter.setError(e.getError());
			adapter.setStatus(false);
			adapter.setResponse(null);
		}
		catch (Exception e)
		{
			log.error(TypeConverter.exceptionToString(e));
			SNMPSender.sendMessage(e);
			adapter.setError(ServiceError.ERR_UNDEFINED);
			adapter.setStatus(false);
			adapter.setResponse(null);
		}
		
		response = composeResponce(adapter);

		return response;
	}
	
	
}

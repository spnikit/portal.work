package ru.aisa.rgd.ws.endpoint;

import org.apache.log4j.Logger;
import org.springframework.oxm.Marshaller;
import org.springframework.ws.server.endpoint.AbstractMarshallingPayloadEndpoint;

import ru.aisa.rgd.ws.dao.ServiceFacade;
import ru.aisa.rgd.ws.exeption.SyncServiceError;
import ru.aisa.rgd.ws.exeption.SyncServiceException;
import ru.aisa.rgd.ws.utility.TypeConverter;


public abstract class AbstractSyncEndpoint<T> extends AbstractMarshallingPayloadEndpoint {
	
//	Loginning
	protected final Logger	log= Logger.getLogger(getClass());
	
//	Context
	protected String templateName;
	protected String needUrl;
	protected ServiceFacade facade;
	
	
	
	protected AbstractSyncEndpoint(Marshaller marshaller) {
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
	
	protected abstract  SyncResponseAdapter<T> processRequest(final Object arg, ServiceFacade facade) throws Exception;
	
	protected  abstract  Object composeResponce(SyncResponseAdapter<T> adapter);
	
	@Override
	protected Object invokeInternal(Object arg) throws Exception 
	{
		
		Object response = null;
		SyncResponseAdapter<T> adapter = new SyncResponseAdapter<T>();

		try
		{
		adapter = processRequest(arg, getFacade());
			
		
		
		}
		catch (SyncServiceException e)
		{
			e.printStackTrace();
			log.error("Service Error:");
			log.error("etdid: "+e.getEtdid()+" code:" + e.getError().getCode() + " message : " + e.getError().getMessage()+" message2 :" + e.getMessage());
			adapter.setError(e.getError());
			adapter.setStatus(false);
			adapter.setResponse(null);
			adapter.setEtdid(e.getEtdid());
			
		}
		catch (Exception e)
		{
			
			log.error(TypeConverter.exceptionToString(e));
//			SNMPSender.sendMessage(e);
			adapter.setError(SyncServiceError.ERR_UNDEFINED);
			adapter.setStatus(false);
			adapter.setResponse(null);
			adapter.setEtdid(-1);
		}
		
		response = composeResponce(adapter);

		return response;
	}
	
	
}

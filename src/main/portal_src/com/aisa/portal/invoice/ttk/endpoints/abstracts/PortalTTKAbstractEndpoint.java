package com.aisa.portal.invoice.ttk.endpoints.abstracts;

import java.util.ArrayList;

import org.springframework.oxm.Marshaller;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import com.aisa.portal.invoice.integration.endpoints.objects.DocumentsObj;
import com.aisa.portal.invoice.integration.facade.PortalSFFacade;
import com.aisa.portal.invoice.ttk.endpoints.tasks.AisaTask;




import ru.aisa.rgd.etd.extend.SNMPSender;
import ru.aisa.rgd.utils.Base64;
import ru.aisa.rgd.ws.dao.ServiceFacade;
import ru.aisa.rgd.ws.documents.StandartResponseWrapper;
import ru.aisa.rgd.ws.endpoint.ETDAbstractEndpoint;
import ru.aisa.rgd.ws.endpoint.ResponseAdapter;
import ru.aisa.rgd.ws.exeption.ServiceError;
import ru.aisa.rgd.ws.exeption.ServiceException;
import ru.aisa.rgd.ws.utility.TypeConverter;
/*import ru.iit.portal.etdXml.EtdXmlRequestDocument;
import ru.iit.portal.etdXml.EtdXmlRequestDocument.EtdXmlRequest;*/

import sheff.rjd.services.transoil.TransService;

import org.springframework.ws.client.core.WebServiceTemplate;



public abstract class PortalTTKAbstractEndpoint<T>  extends ETDAbstractEndpoint<T> {

	private WebServiceTemplate wst;
	private TransService sendtotransoil;
	private sheff.rjd.services.etd.TransService sendtonoticeetd;
	protected PortalSFFacade PortalSFFacade;

	protected PortalTTKAbstractEndpoint(Marshaller marshaller) {
		super(marshaller); 
	}

	public WebServiceTemplate getWst() {
		return wst;
	}
	public void setWst(WebServiceTemplate wst) {
		this.wst = wst;
	}
	public ThreadPoolTaskExecutor getTaskExecutor() {
		return taskExecutor;
	}
	public void setTaskExecutor(ThreadPoolTaskExecutor taskExecutor) {
		this.taskExecutor = taskExecutor;
	}
	private  ThreadPoolTaskExecutor taskExecutor;

	public PortalSFFacade getPortalSFFacade() {
		return PortalSFFacade;
	}
	public void setPortalSFFacade(PortalSFFacade portalSFFacade) {
		PortalSFFacade = portalSFFacade;
	}
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
	protected ResponseAdapter<T> processRequest(
			Object arg, ServiceFacade facade) throws Exception {
		log.error(" PortalTTKAbstractEndpoint unimplemented ");
		return null;
	}
	protected abstract  ResponseAdapter<T> processRequest(final Object arg, PortalSFFacade facade) throws Exception;
	protected  abstract  ArrayList<DocumentsObj> createDocs(final Object arg,final String guid)throws Exception;
	@Override
	protected Object invokeInternal(Object arg) throws Exception 
	{
		
		Object response = null;
		ResponseAdapter<T> adapter = new ResponseAdapter<T>();

		try
		{
			adapter = processRequest(arg, getPortalSFFacade());
		}
		catch (ServiceException e)
		{
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
		 final ArrayList<DocumentsObj> documents=createDocs(arg, "");
		response = composeResponce(adapter);
		//
		if (adapter.isStatus())
		{
			
			AisaTask task=new AisaTask(documents, PortalSFFacade, wst);
			taskExecutor.execute(task);

		}
		
		return response;
	}
	
}

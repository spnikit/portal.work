package ru.aisa.rgd.ws.client;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.apache.log4j.Logger;
import org.springframework.ws.WebServiceMessage;
import org.springframework.ws.client.core.FaultMessageResolver;
import org.springframework.ws.client.core.WebServiceTemplate;
import org.springframework.ws.soap.SoapMessage;
import org.springframework.ws.soap.client.SoapFaultClientException;

import ru.aisa.rgd.ws.dao.ServiceFacade;


public abstract class SignificationTemplate {
	
	protected Logger	log	= Logger.getLogger(getClass());
	
	private WebServiceTemplate wst;
	private ServiceFacade facade;
	private String formname;
	private String dispatch;
	private String url; 
	private String urlUS;
	
	public String getDispatch() {
		return dispatch;
	}
	public void setDispatch(String dispatch) {
		this.dispatch = dispatch;
	}
	public ServiceFacade getFacade() {
		return facade;
	}
	public void setFacade(ServiceFacade facade) {
		this.facade = facade;
	}
	public String getFormname() {
		return formname;
	}
	public void setFormname(String formname) {
		this.formname = formname;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	
	public String getUrlUS() {
		return urlUS;
	}
	public void setUrlUS(String url) {
		this.urlUS = url;
	}
	public WebServiceTemplate getWst() {
		return wst;
	}
	public void setWst(WebServiceTemplate wst) {
		this.wst = wst;
		this.wst.setFaultMessageResolver(new FaultMessageResolver()
		{
			public void resolveFault(WebServiceMessage message) throws IOException 
			{
				ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
				message.writeTo(outputStream); 
				String str = outputStream.toString("UTF-8");
				log.error(str);
				SoapMessage soapMessage = (SoapMessage) message;
		        throw new SoapFaultClientException(soapMessage);
			}
		});
	}
	public abstract void executeUS(long docId, String xml, int signsNumber, String docurl, Integer predkod) throws Exception;
	public abstract void execute(long docId, String xml, int signsNumber, String docurl, Integer predkod, String url, int trynumber) throws Exception;
	public abstract boolean isFinished(String xml, int signsNumber,int allSignatures);
	public void dispatch(long docId,String xml,int signsNumber, String docurl, Integer predkod, String url, int trynumber) throws Exception
	{
		/*try
		{
			execute(docId, xml, signsNumber);
		}
		catch (Exception e)
		{
			getLogger().error(TypeConverter.exceptionToString(e));
		}*/
		execute(docId, xml, signsNumber, docurl, predkod, url, trynumber);
	}
	
	public void dispatchUS(long docId,String xml,int signsNumber, String docurl, Integer predkod) throws Exception
	{
		/*try
		{
			execute(docId, xml, signsNumber);
		}
		catch (Exception e)
		{
			getLogger().error(TypeConverter.exceptionToString(e));
		}*/
		executeUS(docId, xml, signsNumber, docurl, predkod);
	}
	
	
}

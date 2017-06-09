package ru.aisa.rgd.etd.ws;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;

import org.apache.log4j.Logger;
import org.apache.xmlbeans.XmlException;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcTemplate;
import org.springframework.oxm.Marshaller;
import org.springframework.transaction.support.TransactionTemplate;
import org.springframework.ws.server.endpoint.AbstractSaxPayloadEndpoint;
import org.xml.sax.ContentHandler;

import ru.aisa.edt.Signature;
import ru.aisa.rgd.etd.dao.ETDFacade;
import ru.aisa.rgd.etd.extend.SNMPSender;
import ru.aisa.rgd.etd.objects.ETDSignature;
import ru.aisa.rgd.ws.dao.ServiceFacade;
import ru.aisa.rgd.ws.utility.TypeConverter;


public abstract class ETDUpdateBdSecurityEndpoint<T,E> extends		AbstractSaxPayloadEndpoint 
{
	protected final Logger	log	= Logger.getLogger(getClass());
	
	    
	//private SecurityManager securityManager;
	private ETDFacade etdFacade;
	private ServiceFacade srvFacade;
	
	private NamedParameterJdbcTemplate npjt;
	private SimpleJdbcTemplate jt;
	private TransactionTemplate tt;
	
	protected ETDUpdateBdSecurityEndpoint(Marshaller marshaller) {
		super();
	}
	
	
	public NamedParameterJdbcTemplate getNpjt() {
		return npjt;
	}

	public void setNpjt(NamedParameterJdbcTemplate npjt) {
		this.npjt = npjt;
	}
	
	public SimpleJdbcTemplate getJt() {
		return jt;
	}

	public void setJt(SimpleJdbcTemplate jt) {
		this.jt = jt;
	}

	public TransactionTemplate getTransTemplate() {
		return tt;
	}

	public void setTransTemplate(TransactionTemplate tt) {
		this.tt = tt;
	}
	
	private ETDSignature getSecurityObject(Signature signature)
	{
		ETDSignature sign = new ETDSignature(signature);
		return sign;
	}
	private Signature createInvalidSecurity(Signature signature)
	{
		signature.setLogonstatus("false");
		return signature;
	}
	private Signature createValidSecurity(Signature signature)
	{
		signature.setLogonstatus("true");
		return signature;
	}
	protected abstract T convertRequest(final Object obj) throws XmlException;
	protected abstract Signature getSecurity(final T requestDocument);
	protected abstract  E processRequest(final T requestDocument, Signature signature, final ETDFacade facade) throws Exception;
	protected  abstract  E composeInvalidSecurityResponce(Signature signature);
	
	@Override
	protected Source/*Object*/ getResponse(ContentHandler arg0) throws Exception //invokeInternal(Object arg) throws Exception 
	{
		StreamSource source;
		E responseDocument = null;
		try
		{			
			
			//Приводим тип запроса
			
			T requestDocument =  convertRequest(arg0);
			
			//Получаем объект подписи
			Signature sign = getSecurity(requestDocument);
			
			//Преобразуем (можно убрать)
			ETDSignature s = getSecurityObject(sign);
			
			Signature signature = createValidSecurity(sign);
			
			responseDocument = processRequest(requestDocument,signature, etdFacade);
			
			InputStream in=new ByteArrayInputStream(responseDocument.toString().getBytes());
			source=new StreamSource(in);
			

			
		}
		catch(Exception e)
		{
			logger.error(TypeConverter.exceptionToString(e));
			SNMPSender.sendMessage(e);
			return null;
		}
		//return responseDocument;
		return source;
		
	}

	public ETDFacade getFacade() {
		return etdFacade;
	}

	public void setFacade(ETDFacade facade) {
		this.etdFacade = facade;
	}

	/*public SecurityManager getSecurityManager() {
		return securityManager;
	}

	public void setSecurityManager(SecurityManager securityManager) {
		this.securityManager = securityManager;
	}*/

	public ServiceFacade getSrvFacade() {
		return srvFacade;
	}

	public void setSrvFacade(ServiceFacade srvFacade) {
		this.srvFacade = srvFacade;
	}

	public ETDFacade getEtdFacade() {
		return etdFacade;
	}

	public void setEtdFacade(ETDFacade etdFacade) {
		this.etdFacade = etdFacade;
	}

}
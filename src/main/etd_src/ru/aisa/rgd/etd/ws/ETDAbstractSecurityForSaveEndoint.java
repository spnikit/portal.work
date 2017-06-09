package ru.aisa.rgd.etd.ws;

import org.apache.log4j.Logger;
import org.apache.xmlbeans.XmlException;
import org.springframework.oxm.Marshaller;
import org.springframework.ws.server.endpoint.AbstractMarshallingPayloadEndpoint;

import ru.aisa.edt.Signature;
import ru.aisa.rgd.etd.dao.ETDFacade;
import ru.aisa.rgd.etd.extend.SNMPSender;
import ru.aisa.rgd.etd.objects.ETDSignatureForSave;
import ru.aisa.rgd.ws.dao.ServiceFacade;
import ru.aisa.rgd.ws.utility.TypeConverter;

public abstract class ETDAbstractSecurityForSaveEndoint<T,E> extends		AbstractMarshallingPayloadEndpoint 
{
	protected final Logger	logger	= Logger.getLogger(getClass());
	
	
	//private SecurityManager securityManager;
	private ETDFacade etdFacade;
	private ServiceFacade srvFacade;
	
	protected ETDAbstractSecurityForSaveEndoint(Marshaller marshaller) {
		super(marshaller);
	}
	
	private ETDSignatureForSave getSecurityObject(Signature signature)
	{
	    ETDSignatureForSave sign = new ETDSignatureForSave(signature);
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
	protected Object invokeInternal(Object arg) throws Exception 
	{
		E responseDocument = null;
		try
		{
			
			//Приводим тип запроса
			T requestDocument =  convertRequest(arg);
			//Получаем объект подписи
			Signature sign = getSecurity(requestDocument);
			//Преобразуем (можно убрать)
			ETDSignatureForSave s = getSecurityObject(sign);
			//Проверяем подпись
			/*boolean validSignature;
			if (requestDocument instanceof FormTypesRequestDocument
					|| requestDocument instanceof PredsRequestDocument
					|| requestDocument instanceof DorsRequestDocument){
				System.out.println("1");
				validSignature = true;
			}else{
				System.out.println("2");
				validSignature = getSecurityManager().checkSignature(s.getUsername(), s.getSign(), s.getCertid());
			}
			if (s.getUsername().length() > 0 && validSignature)
			{
				System.out.println("3");
				//Если подпись верна
				logger.debug("Signature veriffied successfull");
				Signature signature = createValidSecurity(sign);
				System.out.println("33");
			//	System.out.println("requestDocument "+requestDocument);
				responseDocument = processRequest(requestDocument,signature, etdFacade);
				System.out.println("44");
			}
			else
			{
				System.out.println("4");
				//Если подпись не верна
				logger.debug("Signature invalid");
				Signature signature = createInvalidSecurity(sign);
				responseDocument = composeInvalidSecurityResponce(signature);
			}*/
			Signature signature = createValidSecurity(sign);
			responseDocument = processRequest(requestDocument,signature, etdFacade);
		}
		catch(Exception e)
		{
			logger.error(TypeConverter.exceptionToString(e));
			SNMPSender.sendMessage(e);
			return null;
		}
		return responseDocument;
		
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

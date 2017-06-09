package ru.aisa.rgd.etd.ws;

import org.apache.log4j.Logger;
import org.springframework.oxm.Marshaller;
import org.springframework.ws.server.endpoint.AbstractMarshallingPayloadEndpoint;

import ru.aisa.edt.LoginResponseDocument;
import ru.aisa.edt.LoginResponseDocument.LoginResponse;
import ru.aisa.edt.Signature;
import ru.aisa.rgd.etd.dao.ETDFacade;
import ru.aisa.rgd.etd.objects.ETDSignature;
import ru.aisa.rgd.ws.dao.ServiceFacade;
import sheff.rjd.utils.SecurityManager;

public class doLoginEndpoint extends	AbstractMarshallingPayloadEndpoint 
{
	protected final Logger	logger	= Logger.getLogger(getClass());
	
	
	private SecurityManager securityManager;
	private ETDFacade etdFacade;
	private ServiceFacade srvFacade;
	
	private String signature;
	
	public String getSignature() {
		return signature;
	}

	private void setSignature(String signature) {
		this.signature = signature;
	}
	
	protected doLoginEndpoint(Marshaller marshaller) {
		super(marshaller);
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
	@Override
	protected Object invokeInternal(Object arg) throws Exception 
	{
		
		LoginResponseDocument responseDocument = LoginResponseDocument.Factory.newInstance();
		LoginResponse response = responseDocument.addNewLoginResponse();
		
		response.setRes("ok");
		
		return responseDocument;
		
	
	}

	public ETDFacade getFacade() {
		return etdFacade;
	}

	public void setFacade(ETDFacade facade) {
		this.etdFacade = facade;
	}

	public SecurityManager getSecurityManager() {
		return securityManager;
	}

	public void setSecurityManager(SecurityManager securityManager) {
		this.securityManager = securityManager;
	}

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

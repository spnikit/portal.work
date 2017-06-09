package ru.aisa.rgd.etd.ws;

import org.springframework.oxm.Marshaller;

import com.aisa.portal.invoice.integration.security.Manager;

import ru.aisa.etdSignature.ETDSignatureCreateRequestDocument;
import ru.aisa.etdSignature.ETDSignatureCreateRespType;
import ru.aisa.etdSignature.ETDSignatureCreateResponseDocument;
import ru.aisa.rgd.ws.dao.ServiceFacade;
import ru.aisa.rgd.ws.documents.StandartResponseWrapper;
import ru.aisa.rgd.ws.endpoint.ETDAbstractEndpoint;
import ru.aisa.rgd.ws.endpoint.ResponseAdapter;
import ru.aisa.rgd.ws.exeption.ServiceError;

public class ETDSignatureEndpoint extends ETDAbstractEndpoint <ETSSignatureResponseWrapper>{

	Manager securityManager;
	
	private String keyAlias;
	
	public String getKeyAlias() {
		return keyAlias;
	}

	public void setKeyAlias(String keyAlias) {
		this.keyAlias = keyAlias;
	}

	public Manager getSecurityManager() {
		return securityManager;
	}

	public void setSecurityManager(Manager securityManager) {
		this.securityManager = securityManager;
	}

	protected ETDSignatureEndpoint(Marshaller marshaller) {
		super(marshaller);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected ResponseAdapter<ETSSignatureResponseWrapper> processRequest(Object arg, ServiceFacade facade)
			throws Exception {
		ETDSignatureCreateRequestDocument document =(ETDSignatureCreateRequestDocument)arg;
		ETSSignatureResponseWrapper wrapper =new ETSSignatureResponseWrapper();
		wrapper.setSgn(securityManager.ConsrtuctSignature(document.getETDSignatureCreateRequest().getData(),keyAlias));
		wrapper.setCode(0);
		wrapper.setDescription("OK");
		return new ResponseAdapter<ETSSignatureResponseWrapper>(false, wrapper, ServiceError.ERR_OK);
	}

	@Override
	protected Object composeResponce(ResponseAdapter <ETSSignatureResponseWrapper> adapter) {
		// TODO Auto-generated method stub
		ETDSignatureCreateResponseDocument response=ETDSignatureCreateResponseDocument.Factory.newInstance();
		ETDSignatureCreateRespType res = response.addNewETDSignatureCreateResponse(); 
	    res.setSgn(adapter.getResponse().getSgn());
		res.setCode(adapter.getResponse().getCode());
		res.setDescription(adapter.getResponse().getDescription());
	 
		return response;
	}

}


class ETSSignatureResponseWrapper extends StandartResponseWrapper{
	public byte[] getSgn() {
		return sgn;
	}

	public void setSgn(byte[] sgn) {
		this.sgn = sgn;
	}

	byte[] sgn;
	
}

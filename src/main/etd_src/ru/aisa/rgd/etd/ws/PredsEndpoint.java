package ru.aisa.rgd.etd.ws;

import java.util.Map;

import org.apache.xmlbeans.XmlException;
import org.springframework.oxm.Marshaller;

import ru.aisa.edt.PredsRequestDocument;
import ru.aisa.edt.PredsResponseDocument;
import ru.aisa.edt.Signature;
import ru.aisa.edt.PredsRequestDocument.PredsRequest;
import ru.aisa.edt.PredsResponseDocument.PredsResponse;
import ru.aisa.edt.PredsResponseDocument.PredsResponse.Enterprise;
import ru.aisa.rgd.etd.dao.ETDFacade;

public class PredsEndpoint extends ETDAbstractSecurityEndoint<PredsRequestDocument, PredsResponseDocument>{

	public PredsEndpoint(Marshaller marshaller){
		super(marshaller);
	}
	public static String FixString(String in){
		
		return 	in.replace((char) 26, ' ');
		
	}
	
	@Override
	protected PredsResponseDocument composeInvalidSecurityResponce(Signature signature) {
		PredsResponseDocument responseDocument = PredsResponseDocument.Factory.newInstance();
		PredsResponse response = responseDocument.addNewPredsResponse();
		response.addNewSecurity().setLogonstatus("false");
		return responseDocument;
	}

	@Override
	protected PredsRequestDocument convertRequest(Object obj) throws XmlException {
		PredsRequestDocument requestDocument = (PredsRequestDocument) obj;
		return requestDocument;
	}

	@Override
	protected Signature getSecurity(PredsRequestDocument requestDocument) {
		Signature s = requestDocument.getPredsRequest().getSecurity();
		return s;
	}

	@Override
	protected PredsResponseDocument processRequest(PredsRequestDocument requestDocument, Signature signature, ETDFacade facade) throws Exception {
		PredsRequest request = requestDocument.getPredsRequest();
		
		PredsResponseDocument responseDocument = PredsResponseDocument.Factory.newInstance();
		PredsResponse response = responseDocument.addNewPredsResponse();
		
		Map<Integer, String> map = facade.getPredsByDorid(request.getDorid());
		
		for (Integer id : map.keySet()){
			Enterprise ent = response.addNewEnterprise();
			ent.setId(id);
			String aa = FixString(map.get(id));
			ent.setName(aa);
		}
		response.setSecurity(signature);
		
		return responseDocument;
	}

}

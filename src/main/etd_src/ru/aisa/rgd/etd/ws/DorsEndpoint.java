package ru.aisa.rgd.etd.ws;

import java.util.Map;

import org.apache.xmlbeans.XmlException;
import org.springframework.oxm.Marshaller;

import ru.aisa.edt.DorsRequestDocument;
import ru.aisa.edt.DorsResponseDocument;
import ru.aisa.edt.Signature;
import ru.aisa.edt.DorsResponseDocument.DorsResponse;
import ru.aisa.edt.DorsResponseDocument.DorsResponse.AllDors;
import ru.aisa.rgd.etd.dao.ETDFacade;

public class DorsEndpoint extends ETDAbstractSecurityEndoint<DorsRequestDocument, DorsResponseDocument> {
	
	public DorsEndpoint(Marshaller marshaller){
		super(marshaller);
	}
	
	@Override
	protected DorsResponseDocument composeInvalidSecurityResponce(Signature signature) {
		DorsResponseDocument responseDocument = DorsResponseDocument.Factory.newInstance();
		DorsResponse response = responseDocument.addNewDorsResponse();
		response.addNewSecurity().setLogonstatus("false");
		return responseDocument;
	}

	@Override
	protected DorsRequestDocument convertRequest(Object obj) throws XmlException {
		DorsRequestDocument requestDocument = (DorsRequestDocument) obj;
		return requestDocument;
	}

	@Override
	protected Signature getSecurity(DorsRequestDocument requestDocument) {
		Signature s = requestDocument.getDorsRequest().getSecurity();
		return s;
	}

	@Override
	protected DorsResponseDocument processRequest(DorsRequestDocument requestDocument,
			Signature signature, ETDFacade facade) throws Exception {		
		DorsResponseDocument responseDocument = DorsResponseDocument.Factory.newInstance();
		DorsResponse response = responseDocument.addNewDorsResponse();
		
		Map<Integer, String> map = facade.getDors();
		
		for (Integer id : map.keySet()){
			AllDors dors = response.addNewAllDors();
			dors.setId(id);
			dors.setName(map.get(id));
		}
		
		response.setSecurity(signature);
		
		return responseDocument;
	}

}

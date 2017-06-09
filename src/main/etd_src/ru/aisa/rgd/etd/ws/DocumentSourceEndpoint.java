package ru.aisa.rgd.etd.ws;

import org.springframework.oxm.Marshaller;

import ru.aisa.edt.DocumentSourceRequestDocument;
import ru.aisa.edt.DocumentSourceResponseDocument;
import ru.aisa.edt.Signature;
import ru.aisa.edt.DocumentSourceRequestDocument.DocumentSourceRequest;
import ru.aisa.edt.DocumentSourceResponseDocument.DocumentSourceResponse;
import ru.aisa.edt.DocumentSourceResponseDocument.DocumentSourceResponse.Type;
import ru.aisa.rgd.etd.dao.ETDFacade;
import ru.aisa.rgd.etd.extend.DocumentSourceBuilder;
import ru.aisa.rgd.etd.objects.ETDTemplate;
import ru.aisa.rgd.ws.documents.ETDForm;
import ru.aisa.rgd.ws.domain.Document;



public class DocumentSourceEndpoint extends ETDAbstractSecurityEndoint<DocumentSourceRequestDocument, DocumentSourceResponseDocument>{
	
	
	protected DocumentSourceEndpoint(Marshaller marshaller) {
		super(marshaller);
	}

	@Override
	protected DocumentSourceResponseDocument composeInvalidSecurityResponce(Signature signature) {
		DocumentSourceResponseDocument responseDocument = DocumentSourceResponseDocument.Factory.newInstance();
		DocumentSourceResponse response = responseDocument.addNewDocumentSourceResponse();
		response.addNewSecurity().setLogonstatus("false");
		return responseDocument;
	}

	@Override
	protected DocumentSourceRequestDocument convertRequest(Object obj) {
		DocumentSourceRequestDocument requestDocument = (DocumentSourceRequestDocument) obj;
		return requestDocument;
	}

	@Override
	protected Signature getSecurity(DocumentSourceRequestDocument requestDocument) {
		Signature s = requestDocument.getDocumentSourceRequest().getSecurity();
		return s;
	}

	@Override
	protected DocumentSourceResponseDocument processRequest(DocumentSourceRequestDocument requestDocument, Signature signature, ETDFacade facade) throws Exception 
	{
		DocumentSourceRequest request = requestDocument.getDocumentSourceRequest();
		long sourceId = request.getSourceId();
		//int predId = request.getPredId();
		
		ETDTemplate template = facade.getTemplateByDocumentId(sourceId);
		Document source = getSrvFacade().getDocumentById(sourceId);
		byte[] data = ETDForm.decodeFromArchiv(source.getBlDoc());

		byte[] gen = DocumentSourceBuilder.createFromSource(source.getType(), data, template.getXml());
		
		//Какая-то дополнительная шняжка для АГУ-11
		byte[] xml = gen; //templateComposer.prepareTemplate(template.getName(), gen, predId);
		
		
		DocumentSourceResponseDocument responseDocument = DocumentSourceResponseDocument.Factory.newInstance();
		DocumentSourceResponse response = responseDocument.addNewDocumentSourceResponse();
		
		response.setSecurity(signature);
		Type type = response.addNewType();
		type.setId(template.getId());
		type.setName(template.getName());
		type.setData(xml);
		
		return responseDocument;
	}

}

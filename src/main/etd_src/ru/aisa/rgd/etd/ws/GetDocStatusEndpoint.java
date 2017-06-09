package ru.aisa.rgd.etd.ws;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.oxm.Marshaller;

import ru.aisa.edt.DocStatusRequestDocument;
import ru.aisa.edt.DocStatusRequestDocument.DocStatusRequest;
import ru.aisa.edt.DocStatusResponseDocument;
import ru.aisa.edt.DocStatusResponseDocument.DocStatusResponse;
import ru.aisa.edt.DocumentsTableRequestDocument;
import ru.aisa.edt.DocumentsTableResponseDocument;
import ru.aisa.edt.Signature;
import ru.aisa.edt.DocumentsTableRequestDocument.DocumentsTableRequest;
import ru.aisa.edt.DocumentsTableResponseDocument.DocumentsTableResponse;
import ru.aisa.edt.DocumentsTableResponseDocument.DocumentsTableResponse.Data;
import ru.aisa.edt.DocumentsTableResponseDocument.DocumentsTableResponse.Data.Doc;
import ru.aisa.rgd.etd.dao.ETDFacade;
import ru.aisa.rgd.etd.extend.ShortContentCreator;
import ru.aisa.rgd.etd.objects.ETDDocument;
import ru.aisa.rgd.etd.objects.ETDDocumentFilter;
import ru.aisa.rgd.etd.objects.ETDSFNotice;
import ru.aisa.edt.SFViewNoticeRequestDocument.SFViewNoticeRequest;
import ru.aisa.edt.SFViewNoticeResponseDocument.SFViewNoticeResponse;
import ru.aisa.edt.SFViewNoticeRequestDocument;
import ru.aisa.edt.SFViewNoticeResponseDocument;
public class GetDocStatusEndpoint
		extends
		ETDAbstractSecurityEndoint<DocStatusRequestDocument, DocStatusResponseDocument> {
	

	public GetDocStatusEndpoint(Marshaller marshaller) {
		super(marshaller);
	}

	@Override
	protected DocStatusResponseDocument composeInvalidSecurityResponce(Signature signature) 
	{
		DocStatusResponseDocument responseDocument = DocStatusResponseDocument.Factory.newInstance();
		DocStatusResponse response = responseDocument.addNewDocStatusResponse();
		response.setStatusmess("");
		response.addNewSecurity().setLogonstatus("false");
		return responseDocument;
	}

	@Override
	protected DocStatusRequestDocument convertRequest(Object obj) {
		DocStatusRequestDocument requestDocument = (DocStatusRequestDocument) obj;
		return requestDocument;
	}

	@Override
	protected Signature getSecurity(DocStatusRequestDocument requestDocument) 
	{
		Signature s = requestDocument.getDocStatusRequest().getSecurity();
		return s;
	}

	@Override
	protected DocStatusResponseDocument processRequest(DocStatusRequestDocument requestDocument, Signature signature, ETDFacade facade) throws Exception 
	{
		DocStatusRequest request = requestDocument.getDocStatusRequest();
		
		long id = request.getId();
		
		DocStatusResponseDocument responseDocument = DocStatusResponseDocument.Factory.newInstance();
		DocStatusResponse response = responseDocument.addNewDocStatusResponse();
		
		//String xml = facade.getEtdsfnoticedao().xml(id, count);
		String statusmess= facade.getEtdDocStatusdao().getStatusMess(id);
		response.setSecurity(signature);
response.setStatusmess(statusmess);
		
		return responseDocument;
		
	}

	
}

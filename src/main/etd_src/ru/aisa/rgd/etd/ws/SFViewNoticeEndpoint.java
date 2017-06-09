package ru.aisa.rgd.etd.ws;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.oxm.Marshaller;

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
public class SFViewNoticeEndpoint
		extends
		ETDAbstractSecurityEndoint<SFViewNoticeRequestDocument, SFViewNoticeResponseDocument> {
	

	public SFViewNoticeEndpoint(Marshaller marshaller) {
		super(marshaller);
	}

	@Override
	protected SFViewNoticeResponseDocument composeInvalidSecurityResponce(Signature signature) 
	{
		SFViewNoticeResponseDocument responseDocument = SFViewNoticeResponseDocument.Factory.newInstance();
		SFViewNoticeResponse response = responseDocument.addNewSFViewNoticeResponse();
		response.setId(-1);
		response.addNewSecurity().setLogonstatus("false");
		return responseDocument;
	}

	@Override
	protected SFViewNoticeRequestDocument convertRequest(Object obj) {
		SFViewNoticeRequestDocument requestDocument = (SFViewNoticeRequestDocument) obj;
		return requestDocument;
	}

	@Override
	protected Signature getSecurity(SFViewNoticeRequestDocument requestDocument) 
	{
		Signature s = requestDocument.getSFViewNoticeRequest().getSecurity();
		return s;
	}

	@Override
	protected SFViewNoticeResponseDocument processRequest(SFViewNoticeRequestDocument requestDocument, Signature signature, ETDFacade facade) throws Exception 
	{
		SFViewNoticeRequest request = requestDocument.getSFViewNoticeRequest();
		
		long id = request.getId();
		int count = request.getCount();
		SFViewNoticeResponseDocument responseDocument = SFViewNoticeResponseDocument.Factory.newInstance();
		SFViewNoticeResponse response = responseDocument.addNewSFViewNoticeResponse();
		
		String xml = facade.getEtdsfnoticedao().xml(id, count);
		
		response.setSecurity(signature);
response.setId(id);
		response.setNotice(xml);
		
		return responseDocument;
		
	}

	
}

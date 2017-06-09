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
import ru.aisa.edt.SFsignsResponseDocument.SFsignsResponse;
import ru.aisa.edt.SFsignsResponseDocument;
import ru.aisa.edt.SFsignsRequestDocument.SFsignsRequest;
import ru.aisa.edt.SFsignsRequestDocument;
import ru.aisa.edt.SFsignsResponseDocument.SFsignsResponse.Notice;


public class SFNoticeEndpoint
		extends
		ETDAbstractSecurityEndoint<SFsignsRequestDocument, SFsignsResponseDocument> {
	

	public SFNoticeEndpoint(Marshaller marshaller) {
		super(marshaller);
	}

	@Override
	protected SFsignsResponseDocument composeInvalidSecurityResponce(Signature signature) 
	{
		SFsignsResponseDocument responseDocument = SFsignsResponseDocument.Factory.newInstance();
		SFsignsResponse response = responseDocument.addNewSFsignsResponse();
		response.addNewNotice();
		response.addNewSecurity().setLogonstatus("false");
		return responseDocument;
	}

	@Override
	protected SFsignsRequestDocument convertRequest(Object obj) {
		SFsignsRequestDocument requestDocument = (SFsignsRequestDocument) obj;
		return requestDocument;
	}

	@Override
	protected Signature getSecurity(SFsignsRequestDocument requestDocument) 
	{
		Signature s = requestDocument.getSFsignsRequest().getSecurity();
		return s;
	}

	@Override
	protected SFsignsResponseDocument processRequest(SFsignsRequestDocument requestDocument, Signature signature, ETDFacade facade) throws Exception 
	{
		SFsignsRequest request = requestDocument.getSFsignsRequest();
		long id = request.getId();
		
		SFsignsResponseDocument responseDocument = SFsignsResponseDocument.Factory.newInstance();
		SFsignsResponse response = responseDocument.addNewSFsignsResponse();
		
		response.setSecurity(signature);
response.setId(id);
		List status = facade.getEtdsfnoticedao().status(id);
		 Map egrpo = (HashMap) status.get(0);
		for (int i = 1; i<9; i++)
		{
			Notice notice = response.addNewNotice();
			notice.setName("Квитанция №"+String.valueOf(i));
			notice.setStatus(Integer.valueOf(egrpo.get("SF_FVS"+(i)+"").toString()));
			notice.setStatusname(Integer.valueOf(egrpo.get("SF_FVS"+(i)+"").toString())==1?"Подписана":"Не оформлена");
		notice.setNo(i);
		}
		
		return responseDocument;
		
	}

	
}

package ru.aisa.rgd.etd.ws;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.oxm.Marshaller;

import ru.aisa.edt.PackDocRequestDocument;
import ru.aisa.edt.PackDocRequestDocument.PackDocRequest;
import ru.aisa.edt.PackDocResponseDocument;
import ru.aisa.edt.PackDocResponseDocument.PackDocResponse;
import ru.aisa.edt.PackDocResponseDocument.PackDocResponse.Data;
import ru.aisa.edt.PackDocResponseDocument.PackDocResponse.Data.Packdoc;
import ru.aisa.edt.Signature;

import ru.aisa.rgd.etd.dao.ETDFacade;
import ru.aisa.rgd.etd.extend.ShortContentCreator;
import ru.aisa.rgd.etd.objects.ETDDocument;
import ru.aisa.rgd.etd.objects.ETDDocumentFilter;
import ru.aisa.rgd.etd.objects.ETDSFNotice;
import ru.aisa.rgd.ws.documents.ETDForm;
import ru.aisa.rgd.ws.utility.DataBinder;
import sheff.rjd.ws.OCO.TORLists;

public class PackDocEndpoint
		extends
		ETDAbstractSecurityEndoint<PackDocRequestDocument, PackDocResponseDocument> {
	private static final String blobsql = "select bldoc from snt.docstore where id =:id";	
	

	public PackDocEndpoint(Marshaller marshaller) {
		super(marshaller);
	}

	@Override
	protected PackDocResponseDocument composeInvalidSecurityResponce(Signature signature) 
	{
		PackDocResponseDocument responseDocument = PackDocResponseDocument.Factory.newInstance();
		PackDocResponse response = responseDocument.addNewPackDocResponse();
		//response.setData(data)
		response.addNewSecurity().setLogonstatus("false");
		return responseDocument;
	}

	@Override
	protected PackDocRequestDocument convertRequest(Object obj) {
		PackDocRequestDocument requestDocument = (PackDocRequestDocument) obj;
		return requestDocument;
	}

	@Override
	protected Signature getSecurity(PackDocRequestDocument requestDocument) 
	{
		Signature s = requestDocument.getPackDocRequest().getSecurity();
		return s;
	}

	@Override
	protected PackDocResponseDocument processRequest(PackDocRequestDocument requestDocument, Signature signature, ETDFacade facade) throws Exception 
	{
		PackDocRequest request = requestDocument.getPackDocRequest();
		
		long id = request.getId();
		
		PackDocResponseDocument responseDocument = PackDocResponseDocument.Factory.newInstance();
		PackDocResponse response = responseDocument.addNewPackDocResponse();
		
		List<ETDDocument> document= facade.getEtdDocStatusdao().getPackdocs(id);
		
		if (document.size()>0){
		Data data = response.addNewData();
		
		for (ETDDocument doc : document)
		{
			Packdoc packdoc = data.addNewPackdoc();
			packdoc.setContent(doc.getContent());
			packdoc.setId(doc.getId());
			packdoc.setName(doc.getName());
			packdoc.setNumvag(doc.getVagnum());
			packdoc.setRemdate(doc.getReqdate());
			packdoc.setCount(doc.getCount());
			packdoc.setSigned(doc.isDrop()?"Забракован"
					:doc.getSigned()==0&&doc.getVisible()==3?"Согласован из АСУ"
					:doc.getSigned()==1&&TORLists.acceptlist.contains(doc.getName())?"Подписан"
					:doc.getSigned()==1&&doc.getName().equals("РДВ")?"Согласован"
					:doc.getSigned()==0&&TORLists.acceptlist.contains(doc.getName())&&doc.getVisible()==2?"Согласован"
					:doc.getSigned()==0&&!TORLists.autosign.contains(doc.getName())&&doc.getVisible()==0?"Не подписан"
					:"");
			packdoc.setStatus(doc.getStat());
			packdoc.setVisible(doc.getVisible());
		}
		}
		response.setSecurity(signature);
//System.out.println(responseDocument);
		return responseDocument;
		
	}

	
}

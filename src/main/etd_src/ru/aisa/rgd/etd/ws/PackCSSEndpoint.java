package ru.aisa.rgd.etd.ws;

import java.util.List;

import org.springframework.oxm.Marshaller;

import ru.aisa.edt.PackDocRequestDocument;
import ru.aisa.edt.PackDocRequestDocument.PackDocRequest;
import ru.aisa.edt.PackDocResponseDocument;
import ru.aisa.edt.PackDocResponseDocument.PackDocResponse;
import ru.aisa.edt.PackDocResponseDocument.PackDocResponse.Data;
import ru.aisa.edt.PackDocResponseDocument.PackDocResponse.Data.Packdoc;
import ru.aisa.edt.Signature;
import ru.aisa.rgd.etd.dao.ETDFacade;
import ru.aisa.rgd.etd.objects.ETDDocument;
import sheff.rjd.ws.OCO.TORLists;

public class PackCSSEndpoint
		extends
		ETDAbstractSecurityEndoint<PackDocRequestDocument, PackDocResponseDocument> {
	private static final String blobsql = "select bldoc from snt.docstore where id =:id";	
	

	public PackCSSEndpoint(Marshaller marshaller) {
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
		
		List<ETDDocument> document= facade.getEtdDocStatusdao().getPackCSS(id);
		if (document.size()>0){
		Data data = response.addNewData();
		try {
			for (ETDDocument doc : document) {
				Packdoc packdoc = data.addNewPackdoc();
				packdoc.setContent(doc.getContent());
				packdoc.setId(doc.getId());
				packdoc.setName(doc.getName());
				packdoc.setNumvag("");
				packdoc.setRemdate("");
				packdoc.setCount(0);
				packdoc.setSigned(doc.isDrop()?"Забракован"
						:doc.getSigned()==0&&doc.getVisible()==3?"Согласован из АСУ"
						:doc.getSigned()==1&&"ФПУ-26 АСР".equals(doc.getName())&&doc.getVisible()==2?"Согласован"
						:doc.getSigned()==0&&"ФПУ-26 АСР".equals(doc.getName())?"Подписан"
						:doc.getSigned()==1&&"ФПУ-26 АСР".equals(doc.getName())?"Не подписан"		
						//:doc.getSigned()==0&&!TORLists.autosign.contains(doc.getName())&&doc.getVisible()==0?"Не подписан"
						:"");
			//	System.out.println("name" + " " + doc.getName() +"status " + doc.getSigned());
				packdoc.setStatus(doc.getSigned());
				packdoc.setVisible(doc.getVisible());
			}
		} catch(Exception e) {
			System.out.println(e);
		}
		}
		response.setSecurity(signature);
//System.out.println(responseDocument);
		return responseDocument;
		
	}

	
}

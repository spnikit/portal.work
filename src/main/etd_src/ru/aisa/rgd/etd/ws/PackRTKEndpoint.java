package ru.aisa.rgd.etd.ws;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.oxm.Marshaller;

import ru.aisa.edt.PackRTKRequestDocument;
import ru.aisa.edt.PackRTKRequestDocument.PackRTKRequest;
import ru.aisa.edt.PackRTKResponseDocument;
import ru.aisa.edt.PackRTKResponseDocument.PackRTKResponse;
import ru.aisa.edt.PackRTKResponseDocument.PackRTKResponse.Data;
import ru.aisa.edt.PackRTKResponseDocument.PackRTKResponse.Data.Packdoc;
import ru.aisa.edt.Signature;
import ru.aisa.rgd.etd.dao.ETDFacade;
import ru.aisa.rgd.etd.objects.ETDDocument;


public class PackRTKEndpoint
		extends
		ETDAbstractSecurityEndoint<PackRTKRequestDocument, PackRTKResponseDocument> {
	
	public PackRTKEndpoint(Marshaller marshaller) {
		super(marshaller);
	}

	@Override
	protected PackRTKResponseDocument composeInvalidSecurityResponce(Signature signature) 
	{
		PackRTKResponseDocument responseDocument = PackRTKResponseDocument.Factory.newInstance();
		PackRTKResponse response = responseDocument.addNewPackRTKResponse();
		//response.setData(data)
		response.addNewSecurity().setLogonstatus("false");
		return responseDocument;
	}

	@Override
	protected PackRTKRequestDocument convertRequest(Object obj) {
		PackRTKRequestDocument requestDocument = (PackRTKRequestDocument) obj;
		return requestDocument;
	}

	@Override
	protected Signature getSecurity(PackRTKRequestDocument requestDocument) 
	{
		Signature s = requestDocument.getPackRTKRequest().getSecurity();
		return s;
	}

	@Override
	protected PackRTKResponseDocument processRequest(PackRTKRequestDocument requestDocument, Signature signature, ETDFacade facade) throws Exception 
	{
		PackRTKRequest request = requestDocument.getPackRTKRequest();
		long id = request.getId();
		PackRTKResponseDocument responseDocument = PackRTKResponseDocument.Factory.newInstance();
		PackRTKResponse response = responseDocument.addNewPackRTKResponse();
		List<ETDDocument> document= facade.getEtdDocStatusdao().getPackRTK(id);
		if (document.size()>0){
		Data data = response.addNewData();
		Map<String, Long> pp = new HashMap<String, Long>();
		for (ETDDocument doc : document)
		{
			pp.put("id", doc.getId());
			Packdoc packdoc = data.addNewPackdoc();
			packdoc.setContent(doc.getContent());
			packdoc.setId(doc.getId());
			packdoc.setName(doc.getName());
			if(doc.getName().equals("Карточка документ РТК")) {
				packdoc.setSigned("Файл прикреплён");
			} else {
			    packdoc.setSigned(doc.getSigned()==1?"Подписан":"Не подписан");
			}
			packdoc.setVisible(doc.getVisible());
		}
		}
		response.setSecurity(signature);
//		System.out.println(responseDocument);
		return responseDocument;
		
	}

	
}

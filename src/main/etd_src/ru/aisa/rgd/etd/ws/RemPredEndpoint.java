package ru.aisa.rgd.etd.ws;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.oxm.Marshaller;

import ru.aisa.edt.GetDiRequestDocument;
import ru.aisa.edt.GetDiRequestDocument.GetDiRequest;
import ru.aisa.edt.GetDiResponseDocument;
import ru.aisa.edt.GetDiResponseDocument.GetDiResponse;
import ru.aisa.edt.DiData;
import ru.aisa.edt.RemData;


import ru.aisa.edt.Signature;
import ru.aisa.rgd.etd.dao.ETDFacade;
import ru.aisa.rgd.etd.extend.ShortContentCreator;
import ru.aisa.rgd.etd.objects.DiObject;
import ru.aisa.rgd.etd.objects.ETDDocument;
import ru.aisa.rgd.etd.objects.ETDDocumentFilter;
import ru.aisa.rgd.etd.objects.ETDSFNotice;
import ru.aisa.rgd.etd.objects.RemObject;

public class RemPredEndpoint
		extends
		ETDAbstractSecurityEndoint<GetDiRequestDocument, GetDiResponseDocument> {
	

	public RemPredEndpoint(Marshaller marshaller) {
		super(marshaller);
	}

	@Override
	protected GetDiResponseDocument composeInvalidSecurityResponce(Signature signature) 
	{
		GetDiResponseDocument responseDocument = GetDiResponseDocument.Factory.newInstance();
		GetDiResponse response = responseDocument.addNewGetDiResponse();
		
		response.addNewSecurity().setLogonstatus("false");
		return responseDocument;
	}

	@Override
	protected GetDiRequestDocument convertRequest(Object obj) {
		GetDiRequestDocument requestDocument = (GetDiRequestDocument) obj;
		return requestDocument;
	}

	@Override
	protected Signature getSecurity(GetDiRequestDocument requestDocument) 
	{
		Signature s = requestDocument.getGetDiRequest().getSecurity();
		return s;
	}

	@Override
	protected GetDiResponseDocument processRequest(GetDiRequestDocument requestDocument, Signature signature, ETDFacade facade) throws Exception 
	{
		GetDiRequest request = requestDocument.getGetDiRequest();
		
		int predid= request.getPredid();
		
		GetDiResponseDocument responseDocument = GetDiResponseDocument.Factory.newInstance();
		GetDiResponse response = responseDocument.addNewGetDiResponse();
		//System.out.println(request);
		
		
		if (!request.getDiload()){
		int head = facade.getEtdDocStatusdao().getHeadforPred(predid);
		
		List <DiObject> result1= facade.getEtdDocStatusdao().getDiList(predid, head);
		
		for (DiObject di : result1){
			DiData data = response.addNewDi();
			data.setId(di.getId());
			data.setName(di.getName());
				
		}
		
		List<RemObject> result2 = facade.getEtdDocStatusdao().getREmList(predid, head);
		
		for (RemObject rem : result2){
			RemData data1= response.addNewRemPred();
			data1.setId(rem.getId());
			data1.setName(rem.getName());
			
		}
		}
		
		
		else if (request.getDiload())
		{
		
			if (request.isSetDistring()){
				int head = facade.getEtdDocStatusdao().getHeadforPred(predid);
				
				
List<RemObject> result2 = facade.getEtdDocStatusdao().getLoadRem(predid, head, request.getDistring());
				
				for (RemObject rem : result2){
					RemData data1= response.addNewRemPred();
					data1.setId(rem.getId());
					data1.setName(rem.getName());
				
				}
				
			}
			
			else {
				int head = facade.getEtdDocStatusdao().getHeadforPred(predid);
				List<RemObject> result2 = facade.getEtdDocStatusdao().getREmList(predid, head);
				
				for (RemObject rem : result2){
					RemData data1= response.addNewRemPred();
					data1.setId(rem.getId());
					data1.setName(rem.getName());
					
				}
				//do smthg
			}
			
		}
		
		
		response.setSecurity(signature);

//System.out.println(responseDocument);

		return responseDocument;
		
	}

	
}

package ru.aisa.rgd.etd.ws;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.oxm.Marshaller;

import ru.aisa.edt.GetPersTypesRequestDocument;
import ru.aisa.edt.GetPersTypesRequestDocument.GetPersTypesRequest;
import ru.aisa.edt.GetPersTypesResponseDocument;
import ru.aisa.edt.GetPersTypesResponseDocument.GetPersTypesResponse;
import ru.aisa.edt.LastSigner;
import ru.aisa.edt.Signature;
import ru.aisa.edt.ViewTypes;

import ru.aisa.edt.GetPersTypesRequestDocument;


import ru.aisa.rgd.etd.dao.ETDFacade;
import ru.aisa.rgd.etd.extend.ShortContentCreator;
import ru.aisa.rgd.etd.objects.ETDDocument;
import ru.aisa.rgd.etd.objects.ETDDocumentFilter;
import ru.aisa.rgd.etd.objects.ETDSFNotice;
import ru.aisa.rgd.etd.objects.PredTypedObject;
import sheff.rjd.ws.OCO.TORLists;

public class GetPersTypesEndpoint
		extends
		ETDAbstractSecurityEndoint<GetPersTypesRequestDocument, GetPersTypesResponseDocument> {
	

	public GetPersTypesEndpoint(Marshaller marshaller) {
		super(marshaller);
	}
	
	public SimpleDateFormat getDateFormater() {
		return dateFormater;
	}

	public void setDateFormater(SimpleDateFormat dateFormater) {
		this.dateFormater = dateFormater;
	}

	public SimpleDateFormat getTimeFormater() {
		return timeFormater;
	}

	public void setTimeFormater(SimpleDateFormat timeFormater) {
		this.timeFormater = timeFormater;
	}

	private SimpleDateFormat dateFormater;
	private SimpleDateFormat timeFormater;

	@Override
	protected GetPersTypesResponseDocument composeInvalidSecurityResponce(Signature signature) 
	{
		GetPersTypesResponseDocument responseDocument = GetPersTypesResponseDocument.Factory.newInstance();
		GetPersTypesResponse response = responseDocument.addNewGetPersTypesResponse();
		//response.setData(data)
		response.addNewSecurity().setLogonstatus("false");
		return responseDocument;
	}

	@Override
	protected GetPersTypesRequestDocument convertRequest(Object obj) {
		GetPersTypesRequestDocument requestDocument = (GetPersTypesRequestDocument) obj;
		return requestDocument;
	}

	@Override
	protected Signature getSecurity(GetPersTypesRequestDocument requestDocument) 
	{
		Signature s = requestDocument.getGetPersTypesRequest().getSecurity();
		return s;
	}

	@Override
	protected GetPersTypesResponseDocument processRequest(GetPersTypesRequestDocument requestDocument, Signature signature, ETDFacade facade) throws Exception 
	{
		GetPersTypesRequest request = requestDocument.getGetPersTypesRequest();
		GetPersTypesResponseDocument responseDocument = GetPersTypesResponseDocument.Factory.newInstance();
		GetPersTypesResponse response = responseDocument.addNewGetPersTypesResponse();
		
		int wrkid = request.getWrkid();
		int predid = request.getPredid();
		
		List<PredTypedObject> odin = facade.getEtdDocStatusdao().getPreds(predid);
		
		for (PredTypedObject pred : odin){
			
			LastSigner ls = response.addNewLastsign();
			ls.setId(pred.getId());
			ls.setName(pred.getName());
			
			
		}
		
		
		List <PredTypedObject> dva = facade.getEtdDocStatusdao().getTypes(wrkid);
		for (PredTypedObject types: dva){
			ViewTypes vt = response.addNewTypes();
			vt.setId(types.getId());
			vt.setName(types.getName());
		}
		
		
		
		
		
		
		response.setSecurity(signature);

		
		return responseDocument;
		
	}

	
}

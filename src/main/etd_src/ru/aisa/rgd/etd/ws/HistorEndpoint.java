package ru.aisa.rgd.etd.ws;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.oxm.Marshaller;

import ru.aisa.edt.HistoryRequestDocument;
import ru.aisa.edt.HistoryRequestDocument.HistoryRequest;
import ru.aisa.edt.HistoryResponseDocument;
import ru.aisa.edt.HistoryResponseDocument.HistoryResponse;
import ru.aisa.edt.HistoryResponseDocument.HistoryResponse.Data;
import ru.aisa.edt.HistoryResponseDocument.HistoryResponse.Data.History;
import ru.aisa.edt.Signature;

import ru.aisa.edt.HistoryRequestDocument;


import ru.aisa.rgd.etd.dao.ETDFacade;
import ru.aisa.rgd.etd.extend.ShortContentCreator;
import ru.aisa.rgd.etd.objects.ETDDocument;
import ru.aisa.rgd.etd.objects.ETDDocumentFilter;
import ru.aisa.rgd.etd.objects.ETDSFNotice;
import sheff.rjd.ws.OCO.TORLists;

public class HistorEndpoint
		extends
		ETDAbstractSecurityEndoint<HistoryRequestDocument, HistoryResponseDocument> {
	

	public HistorEndpoint(Marshaller marshaller) {
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
	protected HistoryResponseDocument composeInvalidSecurityResponce(Signature signature) 
	{
		HistoryResponseDocument responseDocument = HistoryResponseDocument.Factory.newInstance();
		HistoryResponse response = responseDocument.addNewHistoryResponse();
		//response.setData(data)
		response.addNewSecurity().setLogonstatus("false");
		return responseDocument;
	}

	@Override
	protected HistoryRequestDocument convertRequest(Object obj) {
		HistoryRequestDocument requestDocument = (HistoryRequestDocument) obj;
		return requestDocument;
	}

	@Override
	protected Signature getSecurity(HistoryRequestDocument requestDocument) 
	{
		Signature s = requestDocument.getHistoryRequest().getSecurity();
		return s;
	}

	@Override
	protected HistoryResponseDocument processRequest(HistoryRequestDocument requestDocument, Signature signature, ETDFacade facade) throws Exception 
	{
		HistoryRequest request = requestDocument.getHistoryRequest();
		HistoryResponseDocument responseDocument = HistoryResponseDocument.Factory.newInstance();
		HistoryResponse response = responseDocument.addNewHistoryResponse();
		
		String vagnum = request.getVagnum();
		String date = request.getDate();
		long docid = request.getDocid();
	//	System.out.println("vagnum: "+vagnum);
		List<ETDDocument> document= facade.getEtdDocStatusdao().getHistory(vagnum, date, docid);
		Data data = response.addNewData();
		
		for (ETDDocument doc : document)
		{
			History history = data.addNewHistory();
			history.setCrtime(getDateFormater().format(doc.getCreateDate()).concat(" ").concat(getTimeFormater().format(doc.getCreateTime())));
			history.setDroptime(getDateFormater().format(doc.getLastDate()).concat(" ").concat(getTimeFormater().format(doc.getLastTime())));
			history.setDroptxt(doc.getDroptxt());
			history.setId(doc.getId());
			history.setName(doc.getName());
			history.setIdPak(doc.getIdPak());
			
		}
		
		response.setSecurity(signature);

		//System.out.println(responseDocument);
		
		
		return responseDocument;
		
	}

	
}

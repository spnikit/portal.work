
package ru.aisa.rgd.etd.ws;

import java.util.ArrayList;
import java.util.List;

import org.springframework.oxm.Marshaller;

import ru.aisa.edt.CounterPersDocument;
import ru.aisa.edt.CounterPersResponseDocument;
import ru.aisa.edt.CounterpartsDocument;
import ru.aisa.edt.CounterpartsResponseDocument;
import ru.aisa.edt.FormTypesRequestDocument;
import ru.aisa.edt.FormTypesResponseDocument;
import ru.aisa.edt.Signature;
import ru.aisa.edt.CounterPersDocument.CounterPers;
import ru.aisa.edt.CounterPersResponseDocument.CounterPersResponse;
import ru.aisa.edt.CounterpartsDocument.Counterparts;
import ru.aisa.edt.CounterpartsResponseDocument.CounterpartsResponse;
import ru.aisa.edt.FormTypesRequestDocument.FormTypesRequest;
import ru.aisa.edt.FormTypesResponseDocument.FormTypesResponse;
import ru.aisa.edt.FormTypesResponseDocument.FormTypesResponse.Type;
import ru.aisa.rgd.etd.dao.ETDFacade;
import ru.aisa.rgd.etd.objects.ETDCounterPers;
import ru.aisa.rgd.etd.objects.ETDCounterparts;
import ru.aisa.rgd.etd.objects.ETDTemplate;

public class CounterPersEndpoint extends ETDAbstractSecurityEndoint<CounterPersDocument, CounterPersResponseDocument> {

	//Список форм отчетов
	private ArrayList<String> reportNames;
	
	public CounterPersEndpoint(Marshaller marshaller) {
		super(marshaller);
	}

	@Override
	protected CounterPersResponseDocument composeInvalidSecurityResponce(Signature signature) 
	{
		
		CounterPersResponseDocument responseDocument = CounterPersResponseDocument.Factory.newInstance();
		CounterPersResponse response = responseDocument.addNewCounterPersResponse();
		response.addNewSecurity().setLogonstatus("false");
		return responseDocument;
	}

	@Override
	protected CounterPersDocument convertRequest(Object obj) 
	{
	
		CounterPersDocument requestDocument = (CounterPersDocument) obj;
		return requestDocument;
	}

	@Override
	protected Signature getSecurity(CounterPersDocument requestDocument) 
	{
		
		Signature s = requestDocument.getCounterPers().getSecurity();
		return s;
	}

	@Override
	protected CounterPersResponseDocument processRequest(CounterPersDocument requestDocument, Signature signature, ETDFacade facade) throws Exception 
	{
		
		CounterPers request = requestDocument.getCounterPers();
		
		CounterPersResponseDocument responseDocument = CounterPersResponseDocument.Factory.newInstance();
		CounterPersResponse response = responseDocument.addNewCounterPersResponse();
		
		response.setSecurity(signature);

		//System.out.println(request.getId() + " "+request.getPredSnd()+" "+request.getPredId());
		List<ETDCounterPers> reportList = facade.getCounterPers(/*request.getId(),request.getPredSnd(), */request.getPredId());
		
		for (ETDCounterPers templ : reportList)
		{
			ru.aisa.edt.CounterPersResponseDocument.CounterPersResponse.Type type = response.addNewType();
			type.setId(templ.getId());
			type.setName(templ.getName());
		}
		
		return responseDocument;
	}

	public ArrayList<String> getReportNames() {
		return reportNames;
	}

	public void setReportNames(ArrayList<String> reportNames) {
		this.reportNames = reportNames;
	}
	


}
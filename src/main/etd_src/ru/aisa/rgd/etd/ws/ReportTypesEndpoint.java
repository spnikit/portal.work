package ru.aisa.rgd.etd.ws;

import java.util.ArrayList;
import java.util.List;

import org.springframework.oxm.Marshaller;

import ru.aisa.edt.FormTypesRequestDocument;
import ru.aisa.edt.FormTypesResponseDocument;
import ru.aisa.edt.Signature;
import ru.aisa.edt.FormTypesRequestDocument.FormTypesRequest;
import ru.aisa.edt.FormTypesResponseDocument.FormTypesResponse;
import ru.aisa.edt.FormTypesResponseDocument.FormTypesResponse.Type;
import ru.aisa.rgd.etd.dao.ETDFacade;
import ru.aisa.rgd.etd.objects.ETDTemplate;

public class ReportTypesEndpoint extends ETDAbstractSecurityEndoint<FormTypesRequestDocument, FormTypesResponseDocument> {

	//Список форм отчетов
	private ArrayList<String> reportNames;
	
	public ReportTypesEndpoint(Marshaller marshaller) {
		super(marshaller);
	}

	@Override
	protected FormTypesResponseDocument composeInvalidSecurityResponce(Signature signature) 
	{
		FormTypesResponseDocument responseDocument = FormTypesResponseDocument.Factory.newInstance();
		FormTypesResponse response = responseDocument.addNewFormTypesResponse();
		response.addNewSecurity().setLogonstatus("false");
		return responseDocument;
	}

	@Override
	protected FormTypesRequestDocument convertRequest(Object obj) 
	{
		FormTypesRequestDocument requestDocument = (FormTypesRequestDocument) obj;
		return requestDocument;
	}

	@Override
	protected Signature getSecurity(FormTypesRequestDocument requestDocument) 
	{
		Signature s = requestDocument.getFormTypesRequest().getSecurity();
		return s;
	}

	@Override
	protected FormTypesResponseDocument processRequest(FormTypesRequestDocument requestDocument, Signature signature, ETDFacade facade) throws Exception 
	{
		
		FormTypesRequest request = requestDocument.getFormTypesRequest();
		
		FormTypesResponseDocument responseDocument = FormTypesResponseDocument.Factory.newInstance();
		FormTypesResponse response = responseDocument.addNewFormTypesResponse();
		
		response.setSecurity(signature);

		
		List<ETDTemplate> reportList = facade.getReportTypesForRole(request.getWorkId(), reportNames);
		
		for (ETDTemplate templ : reportList)
		{
			Type type = response.addNewType();
			type.setId(templ.getId());
			type.setName(templ.getName());
			type.setTemplate(templ.getXml());
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


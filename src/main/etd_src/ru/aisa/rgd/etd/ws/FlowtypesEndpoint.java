
package ru.aisa.rgd.etd.ws;
import java.util.ArrayList;
import java.util.List;
import org.apache.qpid.proton.codec.transport.FlowType;
import org.springframework.oxm.Marshaller;
import ru.aisa.edt.FlowtypesDocument;
import ru.aisa.edt.FlowtypesDocument.Flowtypes;
import ru.aisa.edt.FlowtypesResponseDocument;
import ru.aisa.edt.FlowtypesResponseDocument.FlowtypesResponse;
import ru.aisa.edt.FormTypesRequestDocument;
import ru.aisa.edt.FormTypesResponseDocument;
import ru.aisa.edt.Signature;
import ru.aisa.edt.FormTypesRequestDocument.FormTypesRequest;
import ru.aisa.edt.FormTypesResponseDocument.FormTypesResponse;
import ru.aisa.edt.FormTypesResponseDocument.FormTypesResponse.Type;
import ru.aisa.rgd.etd.dao.ETDFacade;
import ru.aisa.rgd.etd.objects.ETDCounterPers;
import ru.aisa.rgd.etd.objects.ETDCounterparts;
import ru.aisa.rgd.etd.objects.ETDTemplate;
import ru.aisa.rgd.etd.objects.ETDFlowTypes;



public class FlowtypesEndpoint extends ETDAbstractSecurityEndoint<FlowtypesDocument, FlowtypesResponseDocument> {

	//Список форм отчетов
	private ArrayList<String> reportNames;
	
	public FlowtypesEndpoint(Marshaller marshaller) {
		super(marshaller);
	}

	@Override
	protected FlowtypesResponseDocument composeInvalidSecurityResponce(Signature signature) 
	{
		
		FlowtypesResponseDocument responseDocument = FlowtypesResponseDocument.Factory.newInstance();
		FlowtypesResponse response = responseDocument.addNewFlowtypesResponse();
		response.addNewSecurity().setLogonstatus("false");
		return responseDocument;
	}

	@Override
	protected FlowtypesDocument convertRequest(Object obj) 
	{
	
		FlowtypesDocument requestDocument = (FlowtypesDocument) obj;
		return requestDocument;
	}

	@Override
	protected Signature getSecurity(FlowtypesDocument requestDocument) 
	{
		
		Signature s = requestDocument.getFlowtypes().getSecurity();
		return s;
	}

	@Override
	protected FlowtypesResponseDocument processRequest(FlowtypesDocument requestDocument, Signature signature, ETDFacade facade) throws Exception 
	{
		
		Flowtypes request = requestDocument.getFlowtypes();
		
		FlowtypesResponseDocument responseDocument = FlowtypesResponseDocument.Factory.newInstance();
		FlowtypesResponse response = responseDocument.addNewFlowtypesResponse();
		
		response.setSecurity(signature);

		//System.out.println(request.getId() + " "+request.getPredSnd()+" "+request.getPredId());
		List<ETDFlowTypes> reportList = facade.getFlowTypes();
		
		for (ETDFlowTypes templ : reportList)
		{
			ru.aisa.edt.FlowtypesResponseDocument.FlowtypesResponse.Type type = response.addNewType();
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
package dumb;

 
import org.springframework.oxm.Marshaller;

 
 
 

 
import ru.aisa.rgd.ws.dao.ServiceFacade;
import ru.aisa.rgd.ws.documents.StandartResponseWrapper;
 
import ru.aisa.rgd.ws.endpoint.ETDAbstractEndpoint;
import ru.aisa.rgd.ws.endpoint.ResponseAdapter;
import ru.aisa.rgd.ws.exeption.ServiceError;
 
import ru.iit.portal8888.portal.BInvoiceRequestDocument;
import ru.iit.portal8888.portal.BInvoiceRequestDocument.BInvoiceRequest;
import ru.iit.portal8888.portal.BInvoiceResponseDocument;
import ru.iit.portal8888.portal.BInvoiceResponseDocument.BInvoiceResponse;
 
public class BuyerInvoiceEndpoint extends ETDAbstractEndpoint<StandartResponseWrapper>{

	protected BuyerInvoiceEndpoint(Marshaller marshaller) {
		super(marshaller);
		// TODO Auto-generated constructor stub
	}

	protected ResponseAdapter<StandartResponseWrapper> processRequest(Object arg, ServiceFacade facade)
			throws Exception {
		BInvoiceRequestDocument docreq=(BInvoiceRequestDocument)arg;
		BInvoiceRequest request = docreq.getBInvoiceRequest(); 
		
				StandartResponseWrapper wrapper = new StandartResponseWrapper();  
				ResponseAdapter<StandartResponseWrapper> adapter = new ResponseAdapter<StandartResponseWrapper>(false, wrapper, ServiceError.ERR_OK);
				return adapter;
		 
	}

	@Override
	protected Object composeResponce(ResponseAdapter<StandartResponseWrapper> adapter) {
		BInvoiceResponseDocument respdoc=BInvoiceResponseDocument.Factory.newInstance();
		BInvoiceResponse response = respdoc.addNewBInvoiceResponse();
		if (adapter.isStatus())
		{
			response.setCode(adapter.getResponse().getCode());
		 
		}
		else
		{
			response.setCode(adapter.getError().getCode());
		 
		}
		return respdoc;
	}
 

}

package sheff.rjd.services.repair;

import org.apache.log4j.Logger;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.oxm.Marshaller;
import org.springframework.ws.client.core.WebServiceTemplate;

import aj.org.objectweb.asm.Type;
import ru.aisa.rgd.ws.dao.ServiceFacade;
import ru.aisa.rgd.ws.documents.StandartResponseWrapper;
import ru.aisa.rgd.ws.endpoint.ETDAbstractEndpoint;
import ru.aisa.rgd.ws.endpoint.ResponseAdapter;
import ru.aisa.rgd.ws.exeption.ServiceError;
import ru.warecs.iit.receiveFromPortal.ReceivePortalRequestDocument;
import ru.warecs.iit.receiveFromPortal.ReceivePortalResponseDocument;
import ru.warecs.iit.receiveFromPortal.RequestType;
import ru.warecs.iit.receiveFromPortal.ResponseType;
import ru.warecs.iit.receiveFromPortal.TypeDoc;
import ru.warecs.iit.receiveFromPortal.TypeNotifInd;


public class Receive_from_portalEndpoint extends
		ETDAbstractEndpoint<StandartResponseWrapper> {

	private static Logger log = Logger.getLogger(Receive_from_portalEndpoint.class);
	private ServiceFacade facade;
	private NamedParameterJdbcTemplate npjt;
	private  WebServiceTemplate wst;
	
	public ServiceFacade getFacade() {
		return facade;
	}

	public void setFacade(ServiceFacade facade) {
		this.facade = facade;
	}

	
	public NamedParameterJdbcTemplate getNpjt() {
		return npjt;
	}

	public void setNpjt(NamedParameterJdbcTemplate npjt) {
		this.npjt = npjt;
	}

	public WebServiceTemplate getWst() {
		return wst;
	}

	public void setWst(WebServiceTemplate wst) {
		this.wst = wst;
	}

	public Receive_from_portalEndpoint(Marshaller marshaller) {
		super(marshaller);
	}

	@Override
	protected ResponseAdapter<StandartResponseWrapper> processRequest(
			Object arg, ServiceFacade facade) throws Exception {

		final ReceivePortalRequestDocument requestDocument = (ReceivePortalRequestDocument) arg;
		final RequestType request = requestDocument.getReceivePortalRequest();
//		System.out.println(new String(request.getXml(),"windows-1251"));
		StandartResponseWrapper wrapper = new StandartResponseWrapper();
		wrapper.setDescription("ok");
		wrapper.setCode(0);
		wrapper.setDocumentId(-1);
		ResponseAdapter<StandartResponseWrapper> adapter = new ResponseAdapter<StandartResponseWrapper>(
				true, wrapper, ServiceError.ERR_OK);
		return adapter;

	}

	@Override
	protected Object composeResponce(
			ResponseAdapter<StandartResponseWrapper> adapter) {
		ReceivePortalResponseDocument responsedoc = ReceivePortalResponseDocument.Factory
				.newInstance();
		ResponseType response = responsedoc.addNewReceivePortalResponse();

		if (adapter.isStatus()) {
			response.setCode(adapter.getResponse().getCode());
			response.setDescription(adapter.getResponse().getDescription());
			response.setDocid(adapter.getResponse().getDocumentId());

		} else {
			response.setCode(adapter.getError().getCode());
			response.setDescription(adapter.getError().getMessage());
			response.setDocid(Long.MIN_VALUE);
		}

		return responsedoc;
	}

}

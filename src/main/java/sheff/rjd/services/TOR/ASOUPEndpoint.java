package sheff.rjd.services.TOR;

import org.springframework.oxm.Marshaller;

import ru.aisa.rgd.ws.dao.ServiceFacade;
import ru.aisa.rgd.ws.documents.ETDForm;
import ru.aisa.rgd.ws.documents.StandartResponseWrapper;
import ru.aisa.rgd.ws.domain.Document;
import ru.aisa.rgd.ws.domain.Enterprise;
import ru.aisa.rgd.ws.endpoint.ETDAbstractEndpoint;
import ru.aisa.rgd.ws.endpoint.ResponseAdapter;
import ru.aisa.rgd.ws.exeption.ServiceError;
import ru.aisa.rgd.ws.utility.DataBinder;
import ru.aisa.rgd.ws.utility.TypeConverter;
import ru.iit.iit.tor.asoupmess.ASOUPType;
import ru.iit.iit.tor.asoupmess.ASOUPmessRequestDocument;
import ru.iit.iit.tor.asoupmess.ASOUPmessResponseDocument;
import ru.iit.iit.tor.asoupmess.ReportType;
import sheff.rjd.ws.OCO.AfterSign.SendToEtd;
import sheff.rjd.services.TOR.ASOUPTypes;

public class ASOUPEndpoint extends ETDAbstractEndpoint<StandartResponseWrapper> {
	public static final String lastBulidDate = "05.04.2015 10:09:28";

	private String templateName;
	private SendToEtd sendtoetd;

	public SendToEtd getSendtoetd() {
		return sendtoetd;
	}

	public void setSendtoetd(SendToEtd sendtoetd) {
		this.sendtoetd = sendtoetd;
	}

	public String getTemplateName() {
		return templateName;
	}

	public void setTemplateName(String templateName) {
		this.templateName = templateName;
	}

	public ASOUPEndpoint(Marshaller marshaller) {
		super(marshaller);
	}

	@Override
	protected Object composeResponce(
			ResponseAdapter<StandartResponseWrapper> adapter) {
		ASOUPmessResponseDocument responseDocument = ASOUPmessResponseDocument.Factory
				.newInstance();
		ReportType response = responseDocument.addNewASOUPmessResponse();
		if (adapter.isStatus()) {
			response.setCode(adapter.getResponse().getCode());
			response.setDocid(adapter.getResponse().getDocumentId());
			response.setDescription(adapter.getResponse().getDescription());
		} else {
			response.setCode(adapter.getError().getCode());
			response.setDocid(Integer.MIN_VALUE);
			response.setDescription(adapter.getError().getMessage());
		}
		return responseDocument;
	}

	@Override
	protected ResponseAdapter<StandartResponseWrapper> processRequest(
			Object arg, ServiceFacade facade) throws Exception {
		StandartResponseWrapper wrapper = null;
		final ASOUPmessRequestDocument requestDocument = (ASOUPmessRequestDocument) arg;

		final ASOUPType req = requestDocument.getASOUPmessRequest();

		ETDForm form = new ETDForm(facade.getDocumentTemplate(templateName));
		DataBinder binder = form.getBinder();

		Enterprise pred = facade.getEnterpriseByKleimo(req.getRepairPredKod());
		binder.setRootElement("data");
		
		if (req.isSetMess()){
		binder.setNodeValue("P_1", req.getMess());
		binder.setNodeValue("req_type", ASOUPTypes.MESS4624);
		}
		
		if (req.isSetVagons()){
		binder.setNodeValue("P_1", GetVagnums(req.getVagons().getVagnumberArray()));
		binder.setNodeValue("req_type", ASOUPTypes.VAGINFO);
		}
		if (req.isSetLocationType()){
		binder.setNodeValue("P_1", req.getLocationType());
		binder.setNodeValue("req_type", ASOUPTypes.LOCATION);
		}
		binder.setNodeValue("P_3", req.getRepairPredKod());
		
		
		
		Document document = new Document();
		// последняя версия привязки:
		document.setPredId(pred.getId());

		document.setBlDoc(form.encodeToArchiv());
		document.setDocData(form.transform("data"));
		// document.setNo(String.valueOf(number));FIXME
		document.setSignLvl(0);
		document.setType(templateName);
		Long id = facade.insertDocument(document);
		facade.updateVisibleTo0(document);

		sendtoetd.SendToEtdMessage(id,
				new String(document.getBlDoc(), "UTF-8"), templateName, 0, 0,
				true);

		wrapper = new StandartResponseWrapper();
		wrapper.setDocumentId(id);
		wrapper.setCode(ServiceError.ERR_OK.getCode());
		if (getNeedUrl().equals("0")) {
			wrapper.setDescription(ServiceError.ERR_OK.getMessage());
		} else {
			wrapper.setDescription(document.createUrl());
		}

		ResponseAdapter<StandartResponseWrapper> adapter = new ResponseAdapter<StandartResponseWrapper>(
				true, wrapper, ServiceError.ERR_OK);
		return adapter;
	}
	private String GetVagnums(String[] vagnum){
		StringBuffer sb = new StringBuffer();
		try{
		for (int i=0; i<vagnum.length-1; i++){
//			System.out.println(vagnum[i]);
			sb.append(vagnum[i]);
			sb.append(",");
		}
		sb.append(vagnum[vagnum.length-1]);
		} catch (Exception e){
			log.error(TypeConverter.exceptionToString(e));
		}
		return sb.toString();
	}
}

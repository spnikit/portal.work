package sheff.rjd.services.rzds;

import java.text.DecimalFormat;
import java.util.HashMap;

import org.apache.log4j.Logger;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.oxm.Marshaller;
import org.w3c.dom.DOMException;

import ru.aisa.rgd.ws.dao.ServiceFacade;
import ru.aisa.rgd.ws.documents.ETDForm;
import ru.aisa.rgd.ws.documents.StandartResponseWrapper;
import ru.aisa.rgd.ws.domain.Document;
import ru.aisa.rgd.ws.endpoint.ETDAbstractEndpoint;
import ru.aisa.rgd.ws.endpoint.ResponseAdapter;
import ru.aisa.rgd.ws.exeption.InternalException;
import ru.aisa.rgd.ws.exeption.ServiceError;
import ru.aisa.rgd.ws.utility.DataBinder;
import ru.aisa.rgd.ws.utility.RowFiller;
import ru.iit.rzds.act.FormCreateRequest;
import ru.iit.rzds.act.FormCreateRequest.Table1.Row;
import ru.iit.rzds.act.FormCreateRequestDocument;
import ru.iit.rzds.act.FormCreateResponse;
import ru.iit.rzds.act.FormCreateResponseDocument;


public class Act_Endpoint extends ETDAbstractEndpoint<ActRTKWrapper> {

	private static Logger log = Logger.getLogger(Act_Endpoint.class);

	public ServiceFacade getFacade() {
		return facade;
	}

	public void setFacade(ServiceFacade facade) {
		this.facade = facade;
	}

	private ServiceFacade facade;
	private String formname;
	private NamedParameterJdbcTemplate npjt;
	
	public String getFormname() {
		return formname;
	}

	public void setFormname(String formname) {
		this.formname = formname;
	}

	public NamedParameterJdbcTemplate getNpjt() {
		return npjt;
	}

	public void setNpjt(NamedParameterJdbcTemplate npjt) {
		this.npjt = npjt;
	}

	public Act_Endpoint(Marshaller marshaller) {
		super(marshaller);
	}

	DecimalFormat df = new DecimalFormat("##########.##");
	
	@Override
	protected ResponseAdapter<ActRTKWrapper> processRequest(Object arg,
			ServiceFacade facade) throws Exception {

		FormCreateRequestDocument requestDocument = (FormCreateRequestDocument) arg;
		FormCreateRequest request = requestDocument.getFormCreateRequest();

		Document document = null;
		ETDForm form = null;
		int ent1 = 0;
		Long id = null;
		document = new Document();
		form = new ETDForm(facade.getDocumentTemplate(formname));
		DataBinder binder = form.getBinder();
		binder.setNodeValue("P_1", request.getP1());
		binder.setNodeValue("P_2", request.getP2());
		binder.setNodeValue("P_3", request.getP3());
		binder.setNodeValue("P_4", request.getP4());
		binder.setNodeValue("P_5", request.getP5());
		binder.setNodeValue("P_29a", request.getP29A());
		binder.setNodeValue("P_29b", request.getP29B());
		binder.setNodeValue("P_29v", request.getP29V());
		binder.setNodeValue("P_29g", request.getP29G());
		binder.setNodeValue("P_6", request.getP6());
		binder.setNodeValue("P_30a", request.getP30A());
		binder.setNodeValue("P_30b", request.getP30B());
		binder.setNodeValue("P_30v", request.getP30V());
		binder.setNodeValue("P_30g", request.getP30G());
		binder.setNodeValue("P_7", request.getP7());
		
		binder.setRootElement("data");

		binder.fillTable(request.getTable1().getRowArray(),
				new RowFiller<FormCreateRequest.Table1.Row, Object>() {

					public void fillRow(DataBinder b, Row rowContent,
							int numRow, Object options) throws DOMException,
							InternalException {
						b.setNodeValue("P_8", rowContent.getP8());
						b.setNodeValue("P_9", rowContent.getP9());
						b.setNodeValue("P_10", rowContent.getP10());
						b.setNodeValue("P_11", rowContent.getP11());
						b.setNodeValue("P_12", rowContent.getP12());
						b.setNodeValue("P_13", rowContent.getP13());
						b.setNodeValue("P_14", rowContent.getP14());
						b.setNodeValue("P_15", rowContent.getP15());
						b.setNodeValue("P_16", rowContent.getP16());
						b.setNodeValue("P_17", rowContent.getP17());
						b.setNodeValue("P_18", rowContent.getP18());
						b.setNodeValue("P_19", rowContent.getP19());
						}
						}, "table", "row");

		binder.setNodeValue("P_20", request.getP20());
		binder.setNodeValue("P_21", request.getP21());
		binder.setNodeValue("P_22", request.getP22());
		binder.setNodeValue("P_23", request.getP23());
		binder.setNodeValue("P_24", request.getP24());
		binder.setNodeValue("P_25", request.getP25());
		binder.setNodeValue("P_26", request.getP26());
		binder.setNodeValue("P_31", request.getIdPak());
		binder.setNodeValue("okpo_code_sell", request.getOkpoCodeSell());
		binder.setNodeValue("okpo_code_buy", request.getOkpoCodeBuy());

		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("okpo", request.getOkpoCodeSell());
//		map.put("kpp", request.getP32());
		ent1 = facade.getNpjt().queryForInt(
						"select id from snt.pred where okpo_kod = :okpo",
						map);

		
		
		Long actid = facade.getDocumentDao().getNextId();
		document.setBlDoc(form.encodeToArchiv());
		document.setDocData(form.transform("data"));
		document.setPredId(ent1);
		document.setSignLvl(0);
		document.setType(formname);
		document.setId(actid);
		facade.insertDocumentWithDocid(document);

		HashMap<String, Object> pp = new HashMap<String, Object>();
		pp.put("docid", actid);
		pp.put("id_pak", request.getIdPak());
		pp.put("content", request.getP1());
		facade.getNpjt().update("update snt.docstore set id_pak =:id_pak, opisanie =:content where id =:docid", pp);

		ActRTKWrapper wrapper = new ActRTKWrapper();
		wrapper.setDescription(document.createUrl());
		wrapper.setCode(0);
		wrapper.setDocumentId(actid);
		wrapper.setPackid(request.getIdPak());
		ResponseAdapter<ActRTKWrapper> adapter = new ResponseAdapter<ActRTKWrapper>(
				true, wrapper, ServiceError.ERR_OK);
		return adapter;

	}

	@Override
	protected Object composeResponce(ResponseAdapter<ActRTKWrapper> adapter) {
		FormCreateResponseDocument responsedoc = FormCreateResponseDocument.Factory
				.newInstance();
		FormCreateResponse response = responsedoc.addNewFormCreateResponse();

		if (adapter.isStatus()) {
			response.setCode(adapter.getResponse().getCode());
			response.setDescription(adapter.getResponse().getDescription());
			response.setDocid(adapter.getResponse().getDocumentId());
			response.setPackageid(adapter.getResponse().getPackid());
		} else {
			response.setCode(adapter.getError().getCode());
			response.setDescription(adapter.getError().getMessage());
			response.setDocid(Long.MIN_VALUE);
		}

		return responsedoc;
	}


}

class ActRTKWrapper extends StandartResponseWrapper {
	String packid;

	public String getPackid() {
		return packid;
	}

	public void setPackid(String packid) {
		this.packid = packid;
	}

}

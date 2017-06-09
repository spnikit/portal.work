package sheff.rjd.services.transoil;

import java.util.HashMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.oxm.Marshaller;
import org.springframework.ws.client.core.WebServiceTemplate;

import ru.aisa.rgd.ws.dao.ServiceFacade;
import ru.aisa.rgd.ws.documents.StandartResponseWrapper;
import ru.aisa.rgd.ws.endpoint.ETDAbstractEndpoint;
import ru.aisa.rgd.ws.endpoint.ResponseAdapter;
import ru.aisa.rgd.ws.exeption.ServiceError;
import ru.iit.portal.sf.SFRequestDocument.SFRequest.SFInf;
import ru.iit.sFxmltotrans.SFXMLtype;
import ru.iit.sFxmltotrans.SFxmlDocument.SFxml;
import ru.iit.sFxmltotrans.SFxmlRequestDocument;
import ru.iit.sFxmltotrans.SFxmlRequestDocument.SFxmlRequest;
import ru.iit.sFxmltotrans.SFxmlRequestDocument.SFxmlRequest.Xmltable;
import ru.iit.sFxmltotrans.SFxmlResponseDocument;
import ru.iit.sFxmltotrans.SFxmlResponseDocument.SFxmlResponse;
import ru.iit.sFxmltotrans.XMLtype;


public class SFxmltotransEndpoint extends
ETDAbstractEndpoint<StandartResponseWrapper> {

	protected SFxmltotransEndpoint(Marshaller marshaller) {
		super(marshaller);
		// TODO Auto-generated constructor stub
	}

	private static Log log = LogFactory
			.getLog(SFxmltotransEndpoint.class);

	private NamedParameterJdbcTemplate npjt;
	   private  WebServiceTemplate wst;
	   private ServiceFacade facade;
	
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

	public ServiceFacade getFacade() {
		return facade;
	}

	public void setFacade(ServiceFacade facade) {
		this.facade = facade;
	}

	@Override
	protected ResponseAdapter<StandartResponseWrapper> processRequest(
			Object arg, ServiceFacade facade) throws Exception {
	
		SFxml doc = (SFxml) arg;		
		doc.addNewSFxmlRequest();
		SFxmlRequestDocument requestdoc = (SFxmlRequestDocument) arg;
		SFxmlRequest request = requestdoc.addNewSFxmlRequest();
		
		SFXMLtype sf = request.addNewSF();
		
		
		
		Xmltable table = request.addNewXmltable();
		
		XMLtype row = table.addNewRow();
		
		
		
		
		byte[] sf_d1 = npjt.queryForObject("select sf_d1 from snt.dfsigns where id = 25384", new HashMap(), byte[].class);
		byte[] sf_s1 = npjt.queryForObject("select sf_s1 from snt.dfsigns where id = 25384", new HashMap(), byte[].class);
		
		row.setNumber(1);
		row.setSign(sf_d1);
		row.setXml(sf_s1);
		
		byte[] sfxml_d1 = npjt.queryForObject("select sf_fd from snt.dfsigns where id = 25384", new HashMap(), byte[].class);
		byte[] sfxml_s1 = npjt.queryForObject("select sf_fds1 from snt.dfsigns where id = 25384", new HashMap(), byte[].class);
		
		
		sf.setSfsign(sfxml_d1);
		sf.setSfxml(sfxml_s1);
		
		System.out.println(requestdoc);
		
		
		StandartResponseWrapper wrapper = new StandartResponseWrapper();
		wrapper.setDescription("ok");
		wrapper.setCode(0);
		wrapper.setDocumentId(requestdoc.getSFxmlRequest().getEtdid());
		ResponseAdapter<StandartResponseWrapper> adapter = new ResponseAdapter<StandartResponseWrapper>(
				true, wrapper, ServiceError.ERR_OK);
		return adapter;
	}

	@Override
	protected Object composeResponce(
			ResponseAdapter<StandartResponseWrapper> adapter) {
		SFxmlResponseDocument responsedoc = SFxmlResponseDocument.Factory.newInstance();
		SFxmlResponse response = responsedoc.addNewSFxmlResponse();
		response.addNewReturn();
		if (adapter.isStatus()){
		
		response.getReturn().setCode(adapter.getResponse().getCode());
		response.getReturn().setDescription(adapter.getResponse().getDescription());
		response.getReturn().setEtdid(adapter.getResponse().getDocumentId());
		}
		else {
			response.getReturn().setCode(adapter.getError().getCode());
			response.getReturn().setDescription(adapter.getError().getMessage());
		
		}

		return responsedoc;
	}

}

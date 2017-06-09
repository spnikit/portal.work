package sheff.rjd.services.rzds;

import java.io.*;
import java.util.HashMap;
import java.util.zip.GZIPOutputStream;

import javax.xml.transform.TransformerException;

import sheff.rjd.utils.Base64;

import org.apache.log4j.Logger;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.oxm.Marshaller;

import ru.aisa.rgd.ws.dao.ServiceFacade;
import ru.aisa.rgd.ws.documents.ETDForm;
import ru.aisa.rgd.ws.documents.StandartResponseWrapper;
import ru.aisa.rgd.ws.domain.Document;
import ru.aisa.rgd.ws.endpoint.ETDAbstractEndpoint;
import ru.aisa.rgd.ws.endpoint.ResponseAdapter;
import ru.aisa.rgd.ws.exeption.InternalException;
import ru.aisa.rgd.ws.exeption.ServiceError;
import ru.aisa.rgd.ws.exeption.ServiceException;
import ru.aisa.rgd.ws.utility.Attribute;
import ru.aisa.rgd.ws.utility.DataBinder;
import ru.aisa.rgd.ws.utility.RowHandler;
import ru.aisa.rgd.ws.utility.TypeConverter;
import ru.iit.rzds.cardDoc.FormCreateRequest;
import ru.iit.rzds.cardDoc.FormCreateRequestDocument;
import ru.iit.rzds.cardDoc.FormCreateResponse;
import ru.iit.rzds.cardDoc.FormCreateResponseDocument;


public class CardDocEndPoint extends ETDAbstractEndpoint<StandartResponseWrapper>{

	private static Logger log = Logger.getLogger(CardDocEndPoint.class);
	private static String fileName;
	private String formname;
	private ServiceFacade facade;
	private NamedParameterJdbcTemplate npjt; 
	private final int MAX_SIZE = 716800;
	
	protected CardDocEndPoint(Marshaller marshaller) {
		super(marshaller);
	}
	
    public String getFormname() {
			return formname;
    }

	public void setFormname(String formname) {
		this.formname = formname;
	}
	
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

	@Override
	protected ResponseAdapter<StandartResponseWrapper> processRequest(Object arg,
			ServiceFacade facade) throws Exception {
		FormCreateRequestDocument requestDocument = (FormCreateRequestDocument) arg;
		FormCreateRequest request = requestDocument.getFormCreateRequest();
		Long id = null;
		Document document  = new Document(); 	
		HashMap<String, Object> map = new HashMap<String, Object>();
      	map.put("id_pak", request.getPackageid());         
      	map.put("inn", request.getInn());         
      	map.put("kpp", request.getKpp());
      	
      	
      	String selectPredId = "select id from snt.pred where inn = :inn and kpp =:kpp fetch first row only";
    	try {
    		
	    	ETDForm form = new ETDForm(facade.getDocumentTemplate(formname));
	    	DataBinder binder = form.getBinder();
	    	binder.setNodeValue("P_1", request.getFilename());
	    	binder.setNodeValue("P_2", request.getInn());
	    	binder.setNodeValue("P_3", request.getKpp());
	    	binder.setNodeValue("P_4", request.getOkpo());
	    	binder.setNodeValue("P_5", request.getP5());
			document.setBlDoc(form.encodeToArchiv());
			document.setDocData(form.transform("data"));
			Integer predId  = npjt.queryForInt(selectPredId, map);
			document.setPredId(predId);
			document.setSignLvl(0);
			document.setType(formname);
			id = facade.getNextDocumentId();
			document.setId(id);	
			facade.getDocumentDao().save(document);
//			map.put("filename", filename[0]);
			map.put("docid", id);
			map.put("content", request.getP5());
	        facade.getNpjt()
			.update("update snt.docstore set id_pak = :id_pak, opisanie =:content where id =:docid",
					map);
	    	
    	} catch(Exception e) {
	    	log.error(TypeConverter.exceptionToString(e));
	    	if(e.getClass().equals(ServiceException.class)) {
	    		throw e; 			
	    	}
	    }
		StandartResponseWrapper wrapper = new StandartResponseWrapper();
		wrapper.setDescription(document.createUrl());
		wrapper.setCode(0);
		wrapper.setDocumentId(document.getId());
		ResponseAdapter<StandartResponseWrapper> adapter = new ResponseAdapter<StandartResponseWrapper>(
				true, wrapper, ServiceError.ERR_OK);
		

		return adapter;
	}

	@Override
	protected Object composeResponce(ResponseAdapter<StandartResponseWrapper> adapter) {
		FormCreateResponseDocument responsedoc = FormCreateResponseDocument.Factory
				.newInstance();
		FormCreateResponse response = responsedoc.addNewFormCreateResponse();

		if (adapter.isStatus()) {
			response.setCode(adapter.getResponse().getCode());
			response.setDescription(adapter.getResponse().getDescription());
			response.setDocid(adapter.getResponse().getDocumentId());
		} else {
			response.setCode(adapter.getError().getCode());
			response.setDescription(adapter.getError().getMessage());
			response.setDocid(-1);
		}

		return responsedoc;
	}
	
	
	
	private static String base64GzipFrom(byte[] bytesInHex) throws IOException {
		ByteArrayOutputStream buffer = new ByteArrayOutputStream();
		GZIPOutputStream gout = new GZIPOutputStream(buffer);
		gout.write(bytesInHex);
		gout.close();
		buffer.close();
		return Base64.encodeBytes(buffer.toByteArray());
	}

}

package sheff.rjd.services.act;

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
import ru.iit.act.cardDocRTK.FormCreateRequest;
import ru.iit.act.cardDocRTK.FormCreateRequestDocument;
import ru.iit.act.cardDocRTK.FormCreateResponse;
import ru.iit.act.cardDocRTK.FormCreateResponseDocument;


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
    	String selectPredId = "select predid from snt.docstore where id_pak = :id_pak and visible = 1";
    	String selectBlobPackageRTK = "select bldoc from SNT.DOCSTORE where id_pak = :id_pak and visible = 1";
    	String selectOPENTIME = "select opentime from SNT.DOCSTORE where id_pak = :id_pak and visible = 1";
    	try {
    		Object isOpen = npjt.queryForObject(selectOPENTIME, map, Object.class);
    		if(isOpen != null) {
    			throw new ServiceException(new Exception(), new ServiceError(
						109, "Документ в данный момент открыт другим пользователем"));
    		}
	        byte[] blobPack = (byte[])npjt.queryForObject(selectBlobPackageRTK, map, Object.class);
	        int sizeAllAttachments = request.getFile().length + blobPack.length;
	        if(sizeAllAttachments > MAX_SIZE || request.getFile().length > MAX_SIZE) {
	        	throw new ServiceException(new Exception(), new ServiceError(
						110, "Превышен суммарный размер вложенных файлов"));
	        }
			ETDForm formPack =ETDForm.createFromArchive(blobPack);
		    DataBinder binderPack=formPack.getBinder();	
	        fileName = request.getFileName();
	        binderPack.setRootElement("data");
	        binderPack.handleTable("table3", "row3", new RowHandler<Object>() {
				public void handleRow(DataBinder kinder, int rowNum, Object obj) throws InternalException
				{  
					if(kinder.getValue("file_name").equals(fileName)) {
						throw new ServiceException(new Exception(), new ServiceError(
								111, "Файл с таким именем уже есть в пакете"));
					}
					
				}
			}, null);
	    	ETDForm form = new ETDForm(facade.getDocumentTemplate(formname));
	        DataBinder binder = form.getBinder();
	        binder.resetRootElement();
	        binder.createElement("data", "page");
	        binder.setRootElement("page");
	        binder.getElement("data").setAttribute("sid", "data1");
	        binder.createElement("mimetype", "data");
	        String[] filename = request.getFileName().split("\\.");
	        binder.getElement("mimetype").setTextContent("application/"+filename[1]);
	        binder.createElement("filename", "data");
	        binder.getElement("filename").setTextContent(request.getFileName());
	        binder.createElement("datagroup", "data");
	        binder.getLastNode("datagroup").appendChild(binder.createElement("datagroupref"));
	        binder.getLastNode("datagroupref").setTextContent("EncloseImage");
	        binder.createElement("mimedata", "data");
	        binder.getElement("mimedata").setAttribute("encoding", "base64-gzip");
	        binder.getElement("mimedata").setTextContent(base64GzipFrom(request.getFile()));
			document.setBlDoc(form.encodeToArchiv());
			document.setDocData(form.transform("data"));
			Integer predId  = npjt.queryForInt(selectPredId, map);
			document.setPredId(predId);
			document.setSignLvl(0);
			document.setType(formname);
			id = facade.getNextDocumentId();
			document.setId(id);	
			facade.getDocumentDao().save(document);
			map.put("filename", filename[0]);
			map.put("docid", id);
			npjt.update("update snt.docstore set opisanie = :filename where id =:docid", map);
	        facade.getNpjt()
			.update("update snt.docstore set id_pak = :id_pak, visible=0 where id =:docid",
					map);
	    	AddToPackage(request.getPackageid(), request.getFileName(), request.getFile(), id);  
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
	
	private void AddToPackage(String id_pak, String filename, byte[] array, long id) throws UnsupportedEncodingException, ServiceException, IOException, TransformerException{
    	 HashMap<String, Object> pp = new HashMap<String, Object>();
    	 pp.put("id_pak", id_pak);
    	 byte[] blob = (byte[])facade.getNpjt().queryForObject("select bldoc from snt.docstore where id_pak =:id_pak and visible = 1", pp, byte[].class);
		 ETDForm form=ETDForm.createFromArchive(blob);
		 DataBinder b=form.getBinder();
		 String[] filenameArray = filename.split("\\.");
		 try {
			b.setRootElement("data");
			b.setRootLastElement("row3");
			if(b.getValue("file_name").length() == 0 ) {
				b.setNodeValue("file_name", filename);
				b.setNodeValue("P_4", filenameArray[0]);
				b.setNodeValue("id_kard", id);
				b.resetRootElement();	
				b.setRootElement("page");
			    b.setRootElement(b.getElementWithAttribute("label", new Attribute("sid","LABEL24")));
			    b.setNodeValue("value", "1");
			} else {
			    b.resetRootElement();	
			    b.setRootElement("data");
				b.cloneNode("row3");
				b.setRootLastElement("row3");
				b.setNodeValue("P_4", filenameArray[0]);
				b.setNodeValue("file_name", filename);
				b.setNodeValue("id_kard", id);
			}
		   b.resetRootElement();
		   b.setRootElement("page");	     
		 } catch (Exception e) {
			 log.error(TypeConverter.exceptionToString(e));
		 }
		 try { 
		     if(b.getNodes("data").getLength() > 0) {
		    	 b.setRootLastElement("data");
		    	 String attr =  b.getRootEl().getAttribute("sid");
		    	 String newAttr = attr.substring(4);
		    	 b.resetRootElement();
		    	 b.setRootElement("page");
		    	 b.createElement("data", b.getRootEl()).setAttribute("sid", "data"+(Integer.parseInt(newAttr)+1));
		    	 b.setRootLastElement("data");
		    	 b.createElement("mimetype", b.getRootEl());
		         b.getElement("mimetype").setTextContent("application/"+filenameArray[1]);
		         b.createElement("filename", b.getRootEl());
		         b.getElement("filename").setTextContent(filename);
		         b.createElement("datagroup", b.getRootEl());
		         b.getLastNode("datagroup").appendChild(b.createElement("datagroupref"));
		         b.getLastNode("datagroupref").setTextContent("EncloseImage");
		         b.setRootLastElement("data");
		         b.createElement("mimedata", b.getRootEl());
		         b.getElement("mimedata").setAttribute("encoding", "base64-gzip");
		         b.getElement("mimedata").setTextContent(base64GzipFrom(array));	         
		         b.resetRootElement();
				 b.setRootElement("page");
				 b.setRootElement(b.getElementWithAttribute("label", new Attribute("sid","LABEL23")));
		         if(b.getValue("value").length() == 0) {
					 Integer sizeBlobRTKAfterAdd = base64GzipFrom(array).length();
					 b.getElement("value").setTextContent(sizeBlobRTKAfterAdd.toString());
		         } else {
		        	 Integer sizeBlobRTKAfterAdd = base64GzipFrom(array).length() + Integer.parseInt(b.getValue("value"));
		        	 b.getElement("value").setTextContent(sizeBlobRTKAfterAdd.toString());
		         }
				 pp.put("BLDOC", form.encodeToArchiv());
				 pp.put("DOCDATA", form.transform("data"));
				 pp.put("ID", id_pak.replaceAll("P_", ""));
				 facade.getNpjt().update("update snt.docstore set bldoc =:BLDOC, docdata =:DOCDATA where id=:ID", pp);	    	 
		     } else {
		         b.createElement("data", b.getRootEl());
			     b.setRootLastElement("data");
			     b.getRootEl().setAttribute("sid", "data1");
		         b.createElement("mimetype", b.getRootEl());
		         b.getElement("mimetype").setTextContent("application/"+filenameArray[1]);
		         b.createElement("filename", b.getRootEl());
		         b.getElement("filename").setTextContent(filename);
		         b.createElement("datagroup", b.getRootEl());
		         b.getLastNode("datagroup").appendChild(b.createElement("datagroupref"));
		         b.getLastNode("datagroupref").setTextContent("EncloseImage");
		         b.setRootLastElement("data");
		         b.createElement("mimedata", b.getRootEl());
		         b.getElement("mimedata").setAttribute("encoding", "base64-gzip");
		         b.getElement("mimedata").setTextContent(base64GzipFrom(array));
		    	 b.resetRootElement();
				 b.setRootElement("page");
				 b.setRootElement(b.getElementWithAttribute("label", new Attribute("sid","LABEL23")));
		         if(b.getValue("value").length() == 0) {
					 Integer sizeBlobRTKAfterAdd = base64GzipFrom(array).length();
					 b.getElement("value").setTextContent(sizeBlobRTKAfterAdd.toString());
		         } else {
		        	 Integer sizeBlobRTKAfterAdd = base64GzipFrom(array).length() + Integer.parseInt(b.getValue("value"));
		        	 b.getElement("value").setTextContent(sizeBlobRTKAfterAdd.toString());
		         }
				 pp.put("BLDOC", form.encodeToArchiv());
				 pp.put("DOCDATA", form.transform("data"));
				 pp.put("ID", id_pak.replaceAll("P_", ""));
				 facade.getNpjt().update("update snt.docstore set bldoc =:BLDOC, docdata =:DOCDATA where id=:ID", pp);
		     }
    	} catch (Exception e) {
    		log.error(TypeConverter.exceptionToString(e));
    		throw new ServiceException(e, -1, "Ошибка при обновлении пакета документов РТК "
					+ e.getMessage());
    	}
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

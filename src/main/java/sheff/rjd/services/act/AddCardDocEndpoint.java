package sheff.rjd.services.act;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.zip.GZIPOutputStream;

import javax.xml.transform.TransformerException;

import org.apache.log4j.Logger;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.oxm.Marshaller;

import ru.aisa.rgd.ws.dao.ServiceFacade;
import ru.aisa.rgd.ws.documents.ETDForm;
import ru.aisa.rgd.ws.documents.StandartResponseWrapper;
import ru.aisa.rgd.ws.domain.Document;
import ru.aisa.rgd.ws.endpoint.ETDAbstractEndpoint;
import ru.aisa.rgd.ws.endpoint.ResponseAdapter;
import ru.aisa.rgd.ws.exeption.ServiceError;
import ru.aisa.rgd.ws.exeption.ServiceException;
import ru.aisa.rgd.ws.utility.Attribute;
import ru.aisa.rgd.ws.utility.DataBinder;
import ru.aisa.rgd.ws.utility.TypeConverter;

import ru.iit.rtk.addCardDocRTK.FormCreateRequest;
import ru.iit.rtk.addCardDocRTK.FormCreateRequestDocument;
import ru.iit.rtk.addCardDocRTK.FormCreateResponse;
import ru.iit.rtk.addCardDocRTK.FormCreateResponseDocument;

import sheff.rjd.utils.Base64;

public class AddCardDocEndpoint extends ETDAbstractEndpoint<StandartResponseWrapper>{

	private static Logger log = Logger.getLogger(AddCardDocEndpoint.class);
	private String formname;
	private ServiceFacade facade;
	private NamedParameterJdbcTemplate npjt; 
	private final int MAX_SIZE = 716800;
	
	protected AddCardDocEndpoint(Marshaller marshaller) {
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
        Document document = new Document();
		HashMap<String, Object> map = new HashMap<String, Object>();
		String id_pak = request.getPackageid();
		String filename = request.getFileName();
		byte[] attachment = request.getFile();
      	map.put("id_pak", id_pak);         
    	
      	String selectCountPack = "select count(0) from snt.docstore "
      			+ "where id_pak = :id_pak and visible = 1";
    	String selectPackageRTK = "select bldoc, predid, opentime, inuseid, signlvl from snt.docstore "
    			+ "where id_pak = :id_pak and visible = 1";
    	String selectBlobRTK = "select bldoc from snt.docstore where id_pak = :id_pak and visible = 1";
    	String selectUsername = "select rtrim(fname) fname, rtrim(mname) mname, rtrim(lname) lname from snt.personall "
    			+ "where id = :id_usr";
    	
        try {
        	int countPack = npjt.queryForInt(selectCountPack, map);
        	if(countPack == 0) {
        		throw new ServiceException(new Exception(), new ServiceError(
						108, "Пакет документов не найден"));
        	}
        	
        	SqlRowSet rsPak = npjt.queryForRowSet(selectPackageRTK, map);
        	Object isOpen = null;
        	Integer inuseid = null;
        	Integer predId = null;
        	Integer signlvl = null;
			while(rsPak.next()) {
				isOpen = rsPak.getObject("opentime");
				inuseid = rsPak.getInt("inuseid");
				predId = rsPak.getInt("predid");
				signlvl = rsPak.getInt("signlvl");
			}
			if(signlvl > 0) {
				throw new ServiceException(new Exception(), new ServiceError(
						112, "Пакет документов подписан, добавление невозможно"));
			}
    		if(isOpen != null) {
    			map.put("id_usr", inuseid);
    			SqlRowSet rsUsr = npjt.queryForRowSet(selectUsername, map);
    			String fio = "";
    			while(rsUsr.next()) {
    				fio = rsUsr.getString("fname") + " " + rsUsr.getString("mname") + " " +rsUsr.getString("lname");
    			}
    			throw new ServiceException(new Exception(), new ServiceError(
						109, "Пакет документов редактируется пользователем " + fio));
    		} else {
    			java.sql.Date sqlDate = new java.sql.Date(new java.util.Date().getTime());
    			map.put("opentime", sqlDate);
    			npjt.update("update snt.docstore set opentime = :opentime where id_pak = :id_pak and visible = 1", map);
    		}
    		byte[] blobPack = (byte[])npjt.queryForObject(selectBlobRTK, map, Object.class);
        	ETDForm formPack =ETDForm.createFromArchive(blobPack);
    	    DataBinder binderPack=formPack.getBinder();	
	        binderPack.setRootElement("data");
	        String[] arrayFilename = binderPack.getValuesAsArray("file_name");
	        for(String str: arrayFilename) {
	        	if(str.equals(filename)) {
	        		throw new ServiceException(new Exception(), new ServiceError(
							111, "Документ с таким именем уже существует"));
	        	}
	        }

	        binderPack.resetRootElement();
	        binderPack.setRootElement("page");
	        binderPack.setRootElement(binderPack.getElementWithAttribute("label", new Attribute("sid","LABEL23")));
	        String value = binderPack.getValue("value");
	        int sizeAttach;
	        if(value.length() == 0) {
	        	sizeAttach = 0;
	        } else {
	        	sizeAttach = Integer.parseInt(binderPack.getValue("value"));
	        }
	        int sizeAllAttachments = base64GzipFrom(attachment).length() + sizeAttach;
	        if(sizeAllAttachments > MAX_SIZE || attachment.length > MAX_SIZE) {
	        	throw new ServiceException(new Exception(), new ServiceError(
						110, "Превышен объём максимального вложения"));
	        }
	        binderPack.setNodeValue("value", sizeAllAttachments);
   
	        ETDForm form = new ETDForm(facade.getDocumentTemplate(formname));
	        DataBinder binder = form.getBinder();
	        binder.setRootElement("data");
	        binder.setNodeValue("P_1", request.getFileName());
	        binder.resetRootElement();
	        binder.createElement("data", "page");
	        binder.setRootElement("page");
	        binder.getElement("data").setAttribute("sid", "data1");
	        binder.createElement("mimetype", "data");
	        String[] file_name = request.getFileName().split("\\.");
	        binder.getElement("mimetype").setTextContent("application/"+file_name[1]);
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
			document.setPredId(predId);
			document.setSignLvl(1);
			document.setType(formname);
			Long id = facade.getNextDocumentId();
			document.setId(id);	
			facade.getDocumentDao().save(document);
			map.put("filename", file_name[0]);
			map.put("docid", id);
			npjt.update("update snt.docstore set opisanie = :filename where id =:docid", map);
			npjt.update("update snt.docstore set signlvl = 1 where id =:docid", map);
	        npjt
			.update("update snt.docstore set id_pak = :id_pak, visible=0 where id =:docid",
					map);
	    	AddToPackage(id_pak, filename, attachment, id, sizeAllAttachments); 
	    	map.put("opentime", null);
			npjt.update("update snt.docstore set opentime = :opentime where id_pak = :id_pak and visible = 1", map);
        } catch(Exception e) {
	    	log.error(TypeConverter.exceptionToString(e));
	    	if(e.getClass().equals(ServiceException.class)) {
	    		throw e; 			
	    	}
	    } finally {
	    	map.put("opentime", null);
			npjt.update("update snt.docstore set opentime = :opentime where id_pak = :id_pak and visible = 1", map);
	    }
    	
        StandartResponseWrapper wrapper = new StandartResponseWrapper();
		wrapper.setDescription("ok");
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
	
	static public byte[] decodeFromArchiv(byte[] gzip) throws UnsupportedEncodingException {
		String str = new String(gzip, "UTF-8");
		String zipStr = str.replaceFirst("application/vnd.xfdl;content-encoding=\"base64-gzip\"\n", "");
		byte[] data =  Base64.decode(zipStr, Base64.GZIP);
		return data;
	}
	
	private void AddToPackage(String id_pak, String filename, byte[] array, long id, Integer size) throws UnsupportedEncodingException, ServiceException, IOException, TransformerException{
   	 HashMap<String, Object> pp = new HashMap<String, Object>();
   	 pp.put("id_pak", id_pak);
   	 byte[] blob = (byte[])npjt.queryForObject("select bldoc from snt.docstore where id_pak =:id_pak and visible = 1", pp, byte[].class);
		 ETDForm form=ETDForm.createFromArchive(blob);
		 DataBinder b=form.getBinder();
		 String[] filenameArray = filename.split("\\.");
		 try {
			b.setRootElement("data");
			b.setRootLastElement("row3");
			if(b.getValue("id_kard").length() == 0 ) {
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
				 b.getElement("value").setTextContent(size.toString());
				 pp.put("BLDOC", form.encodeToArchiv());
				 pp.put("DOCDATA", form.transform("data"));
				 pp.put("ID", id_pak.replaceAll("P_", ""));
				 npjt.update("update snt.docstore set bldoc =:BLDOC, docdata =:DOCDATA where id=:ID", pp);	    	 
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
				 b.getElement("value").setTextContent(size.toString());
				 b.resetRootElement();
				 b.setRootElement("page");
				 b.setRootElement(b.getElementWithAttribute("label", new Attribute("sid","LABEL24")));
				 b.getElement("value").setTextContent("1");
				 pp.put("BLDOC", form.encodeToArchiv());
				 pp.put("DOCDATA", form.transform("data"));
				 pp.put("ID", id_pak.replaceAll("P_", ""));
				 npjt.update("update snt.docstore set bldoc =:BLDOC, docdata =:DOCDATA where id=:ID", pp);
		     }
   	} catch (Exception e) {
   		log.error(TypeConverter.exceptionToString(e));
   		throw new ServiceException(e, 113, "Ошибка при обновлении пакета документов РТК "
					+ e.getMessage());
   	}
 }

}

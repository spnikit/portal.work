package sheff.rjd.services.act;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;

import javax.xml.transform.TransformerException;

import org.apache.log4j.Logger;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.oxm.Marshaller;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

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
import ru.iit.rtk.delCardDocRTK.FormCreateRequest;
import ru.iit.rtk.delCardDocRTK.FormCreateRequestDocument;
import ru.iit.rtk.delCardDocRTK.FormCreateResponse;
import ru.iit.rtk.delCardDocRTK.FormCreateResponseDocument;

public class DeleteCardDocEndpoint extends ETDAbstractEndpoint<StandartResponseWrapper>{

	private static Logger log = Logger.getLogger(DeleteCardDocEndpoint.class);
	private String formname;
	private ServiceFacade facade;
	private NamedParameterJdbcTemplate npjt; 
	
	protected DeleteCardDocEndpoint(Marshaller marshaller) {
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
		HashMap<String, Object> map = new HashMap<String, Object>();
		Long id = request.getDocid();
		try {
	      	map.put("id", id);         
	      	String selectCountCardDoc = "select count(0) coun from snt.docstore where id = :id";
	    	String selectPackageRTK = "select opentime, inuseid, id_pak, signlvl  from snt.docstore "
	    			+ "where id_pak = (select id_pak from snt.docstore where id = :id) and visible = 1";
	    	String selectUsername = "select rtrim(fname) fname, rtrim(mname) mname, rtrim(lname) lname from snt.personall "
	    			+ "where id = :id_usr";
	    	String selectDocumentFormname = "select rtrim(name) from snt.doctype where id = ("
	    			+ "select typeid from snt.docstore where id = :id)";
	    	
	    	int countCardDoc  = npjt.queryForInt(selectCountCardDoc, map);
	    	if(countCardDoc == 0) {
	    		throw new ServiceException(new Exception(), new ServiceError(
						114, "Документ не найден"));
	    	}
	    	String documentFormname = npjt.queryForObject(selectDocumentFormname, map, String.class);
	    	if(!documentFormname.equals("Карточка документ РТК")) {
		    	if(documentFormname.equals("Акт РТК")) {
		    		throw new ServiceException(new Exception(), new ServiceError(
							115, "Документ не подлежит удалению (не является вложением)"));
		    	} else if(documentFormname.equals("Счет-фактура РТК")) {
		    		throw new ServiceException(new Exception(), new ServiceError(
							115, "Документ не подлежит удалению (не является вложением)"));
		    	} else if(documentFormname.equals("Пакет документов РТК")) {
		    		throw new ServiceException(new Exception(), new ServiceError(
							115, "Документ не подлежит удалению (не является вложением)"));
		    		
		    	} else if(documentFormname.equals("Корретировочный Акт РТК")) {
		    		throw new ServiceException(new Exception(), new ServiceError(
							115, "Документ не подлежит удалению (не является вложением)"));
		    		
		    	} else if(documentFormname.equals("Корректировочный Счет-фактура РТК")) {
		    		throw new ServiceException(new Exception(), new ServiceError(
							115, "Документ не подлежит удалению (не является вложением)"));
		    	} else {
		    		throw new ServiceException(new Exception(), new ServiceError(
							116, "Документ не подлежит удалению (тип неизвестен)"));
		    	}
	    	}
	
	    	SqlRowSet rsPak = npjt.queryForRowSet(selectPackageRTK, map);
	    	Object isOpen = null;
	    	Integer inuseid = null;
	    	String id_pak = null;
	    	Integer signlvl = null;
			while(rsPak.next()) {
				isOpen = rsPak.getObject("opentime");
				inuseid = rsPak.getInt("inuseid");
				id_pak = rsPak.getString("id_pak");
				signlvl = rsPak.getInt("signlvl");
			}
			if(signlvl > 0) {
				throw new ServiceException(new Exception(), new ServiceError(
						117, "Пакет документов подписан, удаление невозможно"));
			}
			try {
				map.put("id_pak", id_pak);
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
				Document document = facade.getDocumentById(id);
				byte[] blobCardDoc = document.getBlDoc();
		    	ETDForm formCardDoc = ETDForm.createFromArchive(blobCardDoc);
			    DataBinder binderCardDoc = formCardDoc.getBinder();	
			    binderCardDoc.setRootElement("data");
			    String filename = binderCardDoc.getValue("P_1");
			    removeCardDocFromPackage(id_pak, filename, id);
			    facade.deleteDocument(id);
			} catch(Exception e) {
		    	log.error(TypeConverter.exceptionToString(e));
		    	if(e.getClass().equals(ServiceException.class)) {
		    		throw e; 			
		    	}
		    } finally {
		    	map.put("opentime", null);
				npjt.update("update snt.docstore set opentime = :opentime where id_pak = :id_pak and visible = 1", map);
		    }
		} catch(Exception e) {
	    	log.error(TypeConverter.exceptionToString(e));
	    	if(e.getClass().equals(ServiceException.class)) {
	    		throw e; 			
	    	}
	    } 
    	
    	StandartResponseWrapper wrapper = new StandartResponseWrapper();
		wrapper.setDescription("ok");
		wrapper.setCode(0);
		wrapper.setDocumentId(id);
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
			
		} else {
			response.setCode(adapter.getError().getCode());
			response.setDescription(adapter.getError().getMessage());
		}

		return responsedoc;
	}
	
	private void removeCardDocFromPackage(String id_pak, String filename,  long idCardDoc) throws UnsupportedEncodingException, ServiceException, IOException, TransformerException{
   	 HashMap<String, Object> map = new HashMap<String, Object>();
   	 map.put("id_pak", id_pak);
   	 byte[] blob = (byte[])npjt.queryForObject("select bldoc from snt.docstore "
   	 		+ "where id_pak =:id_pak and visible = 1", map, byte[].class);
	 ETDForm form = ETDForm.createFromArchive(blob);
	 DataBinder b = form.getBinder();
	 try {
		b.setRootElement("data");
		Node table3 = b.getNode("table3");
        NodeList listRow3 = b.getNodes("row3");
        boolean flag1 = false;
		int sizelistRow3 = listRow3.getLength();
        for(int i = 0; i < sizelistRow3; i++) {
        	Node currentRow3 = listRow3.item(i);
        	NodeList childRow3 = currentRow3.getChildNodes();
    		Node filenameNode = null;
        	for(int j = 0; j < childRow3.getLength(); j++) {
        		Node currentChild = childRow3.item(j);
        		String nodeName = currentChild.getNodeName();
        		if(nodeName.equals("file_name")) {
        			filenameNode = currentChild;
        		}
            	if(nodeName.equals("id_kard")) {
            		long id_card = Long.parseLong(currentChild.getTextContent());
            		if(id_card == idCardDoc && sizelistRow3 == 1) {
            			currentChild.setTextContent("");
            			filenameNode.setTextContent("");
            			flag1 = true;
            		}
            		else if(id_card == idCardDoc) {
            			table3.removeChild(currentRow3);
            			flag1 = true;
            		}
            	}
        	}
        	if(flag1) {
        		break;
        	}
        }
	    b.resetRootElement();
	    b.setRootElement("page");	
	    b.setRootElement(b.getElementWithAttribute("label", new Attribute("sid","LABEL23")));
		int sizeAllAttachments = Integer.parseInt(b.getElement("value").getTextContent());
		b.resetRootElement();
	    b.setRootElement("page");	
	    NodeList data = b.getNodes("data");
	    boolean flag2 = false;
	    String sizeCurrentAttach = null;
	    int sizeDataList = data.getLength();
	    for(int i = 0; i < sizeDataList; i++) {
		   Node currentData = data.item(i);
           NodeList childData = currentData.getChildNodes();
           int childDataLength = childData.getLength();
           for(int j = 0; j < childDataLength; j++) {
        	   Node currentChild = childData.item(j);
        	   String nodeName = currentChild.getLocalName();
        	   if(nodeName.equals("filename")) {
        		   String value = currentChild.getTextContent();
        		   if(value.equals(filename)) {
        			   flag2 = true;
        		   }
        	   } else if(nodeName.equals("mimedata")) {
        		   sizeCurrentAttach = currentChild.getTextContent();
        	   }
        	   if(j == childDataLength - 1 && flag2) {
        		   sizeAllAttachments = sizeAllAttachments - sizeCurrentAttach.length();
    			   Node parent = b.getRootEl();
    			   parent.removeChild(currentData);
    			   if(sizeDataList == 1) {
    				   b.setRootElement(b.getElementWithAttribute("label", new Attribute("sid","LABEL24")));
    				   b.getElement("value").setTextContent("");
    			   } 			  
        	   }
           }  
           if(flag2) {
        	   break;
           }
	    }
	    b.resetRootElement();
		b.setRootElement("page");
		Element el = b.getElementWithAttribute("label", new Attribute("sid","LABEL23"));
	    b.setRootElement(el);
		b.getElement("value").setTextContent(String.valueOf(sizeAllAttachments));	
		map.put("BLDOC", form.encodeToArchiv());
		map.put("DOCDATA", form.transform("data"));
		map.put("ID", id_pak.replaceAll("P_", ""));
		npjt.update("update snt.docstore set bldoc =:BLDOC, docdata =:DOCDATA where id=:ID", map);
	   	} catch (Exception e) {
	   		log.error(TypeConverter.exceptionToString(e));
	   		throw new ServiceException(e, 113, "Ошибка при обновлении пакета документов РТК "
						+ e.getMessage());
	   	}
	}

}

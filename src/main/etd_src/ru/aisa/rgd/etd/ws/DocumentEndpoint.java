package ru.aisa.rgd.etd.ws;

import java.util.Map;

import org.springframework.oxm.Marshaller;

import ru.aisa.edt.DocumentRequestDocument;
import ru.aisa.edt.DocumentResponseDocument;
import ru.aisa.edt.Signature;
import ru.aisa.edt.DocumentRequestDocument.DocumentRequest;
import ru.aisa.edt.DocumentResponseDocument.DocumentResponse;
import ru.aisa.rgd.etd.dao.ETDFacade;
import ru.aisa.rgd.etd.objects.ETDDocumentAccess;
import ru.aisa.rgd.etd.objects.ETDDocumentData;
import ru.aisa.rgd.etd.objects.ETDTemplate;
import sheff.rgd.ws.Abstract.DoAction;

public class DocumentEndpoint extends ETDAbstractSecurityEndoint<DocumentRequestDocument, DocumentResponseDocument> {

	protected DocumentEndpoint(Marshaller marshaller) {
		super(marshaller);
	}
	private DoAction formControllers;
	
	public DoAction getFormControllers() {
		return formControllers;
	}

	public void setFormControllers(DoAction formControllers) {
		this.formControllers = formControllers;
	}

	@Override
	protected DocumentResponseDocument composeInvalidSecurityResponce(Signature signature) {
		DocumentResponseDocument responseDocument = DocumentResponseDocument.Factory.newInstance();
		DocumentResponse response = responseDocument.addNewDocumentResponse();
		response.setId(-1);
		response.setData("");
		response.setStatus("false");
		response.addNewSecurity().setLogonstatus("false");
		return responseDocument;
	}

	@Override
	protected DocumentRequestDocument convertRequest(Object obj) {
		DocumentRequestDocument requestDocument = (DocumentRequestDocument) obj;
		return requestDocument;
	}

	@Override
	protected Signature getSecurity(DocumentRequestDocument requestDocument) {
		Signature s = requestDocument.getDocumentRequest().getSecurity();
		return s;
	}

	@Override
	protected DocumentResponseDocument processRequest(DocumentRequestDocument requestDocument, Signature signature, ETDFacade facade) throws Exception 
	{
		DocumentRequest request = requestDocument.getDocumentRequest();
	//	System.out.println("request "+request);
		DocumentResponseDocument responseDocument = DocumentResponseDocument.Factory.newInstance();
		DocumentResponse response = responseDocument.addNewDocumentResponse();
		response.setSecurity(signature);
		long documentId = request.getId();
		int workId = request.getRole();
		String certId = request.getSecurity().getCertid();
		ETDDocumentData doc = facade.getDocumentDataById(documentId);
		doc.setWrkid(workId);
		ETDTemplate template = facade.getTemplateByDocumentId(documentId);
		//Integer cnt = facade.getPakCountByDocumentId(documentId);
		//System.out.println("cnt "+cnt);
		Integer sf_sign = -1;
		
		//System.out.println(template.getName().trim());
		if (template.getName().trim().contains("чет-фактура")){
		   
		    
		    sf_sign = facade.getSfSignByDocid(documentId);
		    		}
		
		if (formControllers!=null){
			formControllers.EditBeforeOpen(template.getName().trim(), doc);
		}
		
		
		if (doc.getDropId() != null&&!request.getForceDrop()) //Документ отклонент
		{
			
			if (doc.getVisible()==1)
			response.setStatus("declinedprich");
			else if (doc.getVisible()!=1)
			response.setStatus("declinednoprich");
			
			response.setData(doc.getDropTxt());
			response.addExData(doc.getDropName());
			response.setId(doc.getId());
			response.setIsHV(template.getIsHV());
			response.setType(template.getName().trim());
			//response.setIsUnsignedSF(arg0)
			//response.setCnt(cnt);
		}
		else //Документ нормальный
		{
		   
			response.setStatus("success");
			response.setData(doc.getBlDocAsString());
			response.setId(doc.getId());
			response.setType(template.getName().trim());
			response.setIsHV(template.getIsHV());
			response.setIsSigned(doc.isSigned());
			response.setIsNew(doc.isNew());
//			if (template.getName().trim().equals("ТОРГ-12")||template.getName().trim().equals("Счет-фактура"))
			response.setRead(doc.getRead());
			
			
			
			if (sf_sign>-1)
			    response.setIsUnsignedSF(sf_sign);
			//System.out.println(response);
			
			ETDDocumentAccess access = facade.getDocumentAccessByRoleAndId(workId, documentId);

			
			
				int userId = facade.getUserIdByCertificateSerial(certId);
				
				Map map = facade.lockDocument(documentId, userId);
				
				
				if (((String) map.get("lockname")).trim().length() != 0 ) //Если документ уже редактируется
				{
					
					response.setStatus("busy");
					response.setData((String) map.get("lockname"));
					response.setId(doc.getId());
					
				}
			
			response.setIsEdit(access.getEdit() == 1?true:false);
			response.setVisible(doc.getVisible());
			
		}
//		System.out.println("responseDocument "+responseDocument);
		return responseDocument;
		
		
		
		

	}

}

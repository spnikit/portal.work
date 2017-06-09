package ru.aisa.rgd.etd.ws;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.springframework.oxm.Marshaller;
import org.w3c.dom.DOMException;

import ru.aisa.rgd.ws.utility.DataBinder;
import ru.aisa.rgd.ws.utility.RowFiller;
import ru.aisa.rgd.ws.utility.RowHandler;
import ru.aisa.edt.Signature;
import ru.aisa.edt.TemplateRequestDocument;
import ru.aisa.edt.TemplateResponseDocument;
import ru.aisa.edt.TemplateRequestDocument.TemplateRequest;
import ru.aisa.edt.TemplateResponseDocument.TemplateResponse;
import ru.aisa.edt.TemplateResponseDocument.TemplateResponse.Type;
import ru.aisa.rgd.etd.dao.ETDFacade;
import ru.aisa.rgd.etd.objects.ETDTemplate;
import ru.aisa.rgd.ws.documents.ETDForm;
import ru.aisa.rgd.ws.exeption.InternalException;
import ru.aisa.rgd.ws.exeption.ServiceException;
import sheff.rjd.utils.Base64;
import sheff.rjd.ws.OCO.BeforeOpen.CreateGU2b;
import sheff.rjd.ws.OCO.BeforeOpen.CreateZpps;

public class TemplateEndpoint extends ETDAbstractSecurityEndoint<TemplateRequestDocument, TemplateResponseDocument>{

	public TemplateEndpoint(Marshaller marshaller) {
		super(marshaller);
	}
	
	@Override
	protected TemplateResponseDocument composeInvalidSecurityResponce(Signature signature) {
		TemplateResponseDocument responseDocument = TemplateResponseDocument.Factory.newInstance();
		TemplateResponse response = responseDocument.addNewTemplateResponse();
		response.addNewSecurity().setLogonstatus("false");
		return responseDocument;
	}

	@Override
	protected TemplateRequestDocument convertRequest(Object obj) {
		TemplateRequestDocument requestDocument = (TemplateRequestDocument) obj;
		return requestDocument;
	}

	@Override
	protected Signature getSecurity(TemplateRequestDocument requestDocument) {
		Signature s = requestDocument.getTemplateRequest().getSecurity();
		return s;
	}

	@Override
	protected TemplateResponseDocument processRequest(TemplateRequestDocument requestDocument, Signature signature, ETDFacade facade) throws Exception 
	{
	    
	  
		TemplateRequest request = requestDocument.getTemplateRequest();
		int id = request.getId();
		int predId = request.getPredId();
		
		ETDTemplate template = null;
		if (request.isSetName())
			template = facade.getTemplateByName(request.getName());
		else 
		 template = facade.getTemplateById(id);
		//FIXME сделать понятно
		//Какая-то дополнительная шняжка для АГУ-11
		
		
		//ID для заполнения шаблона по первичному документу
		//Пока дл заявки ППС
		//тем, кто будет после меня переписать по уму
		
		byte[] formtemplate = template.getXml();
		
//		System.out.println(template.getName());
		if (request.isSetBaseid()&&template.getName().equals("Заявка ППС")){
			CreateZpps create = new CreateZpps();
			formtemplate= create.CreateZpss(template.getXml(), request.getBaseid(), facade);
			
		}

		if (template.getName().equals("ГУ-2б")){
			CreateGU2b create = new CreateGU2b();
			formtemplate = create.CreateGU2b(template.getXml(), predId, facade);
		}
		
		
		template.setXml(formtemplate);
		
			
		TemplateResponseDocument responseDocument = TemplateResponseDocument.Factory.newInstance();
		TemplateResponse response = responseDocument.addNewTemplateResponse();
		
		response.setSecurity(signature);
		Type type = response.addNewType();
		type.setId(template.getId());
		type.setName(template.getName());
		type.setTemplate(Base64.encodeBytes(formtemplate, Base64.GZIP));
		type.setIsHV(template.getIsHV());
		return responseDocument;
	}
	
	

	}

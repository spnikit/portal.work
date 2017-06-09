package com.aisa.portal.invoice.notice.endpoints;

import org.springframework.oxm.Marshaller;

import ru.aisa.rgd.ws.documents.StandartResponseWrapper;
import ru.aisa.rgd.ws.endpoint.ResponseAdapter;
import ru.aisa.rgd.ws.exeption.ServiceError;
import ru.aisa.rgd.ws.utility.TypeConverter;
import ru.iit.portal8888.portal.NoticeRequestDocument;
import ru.iit.portal8888.portal.SetNoticeRequestDocument;
import ru.iit.portal8888.portal.SetNoticeRequestDocument.SetNoticeRequest;
import ru.iit.portal8888.portal.SetNoticeResponseDocument;
import ru.iit.portal8888.portal.SetNoticeResponseDocument.SetNoticeResponse;

import com.aisa.portal.invoice.integration.facade.PortalSFFacade;

public class SetNoticeEndpoint extends Abstract<StandartResponseWrapper>{

	protected SetNoticeEndpoint(Marshaller marshaller) {
		super(marshaller);
		// TODO Auto-generated constructor stub
	}

	@Override
	ResponseAdapter <StandartResponseWrapper> processRequestInvoice(Object arg, PortalSFFacade facade) {
		SetNoticeRequestDocument docreq=(SetNoticeRequestDocument)arg;
		SetNoticeRequest request = docreq.getSetNoticeRequest();
	 
 
		try {
//			System.out.println("SetNotice");
			String invoiceId=facade.GetInvoiceId(request.getDocId());
			int predid=request.getPredId(); 
		    String cabinetid= facade.GetCabinetId(predid); 
		    boolean isseller= facade.IsSeller(cabinetid, request.getDocId());
			facade.processNotice(request.getNoticeType(), invoiceId, request.getDocId(), request.getNotice(), request.getSignature());
			StandartResponseWrapper wrapper=new StandartResponseWrapper();
			wrapper.setCode(0);
			wrapper.setDescription("OK");
			return new ResponseAdapter<StandartResponseWrapper>(false, wrapper,
					ServiceError.ERR_OK);
		} catch (Exception e) {
			StandartResponseWrapper wrapper=new StandartResponseWrapper();
			wrapper.setCode(-1); 
			wrapper.setDescription(e.toString());
	
//			log.error(TypeConverter.exceptionToString(e));
			log.error("error in process notice with id = "+request.getDocId());
			return new ResponseAdapter<StandartResponseWrapper>(false, wrapper,
					ServiceError.ERR_OK);
		}
		  
	}

	@Override
	Object composeResponceInvoice(ResponseAdapter<StandartResponseWrapper> adapter) {
		SetNoticeResponseDocument responseDoc=SetNoticeResponseDocument.Factory.newInstance();
		SetNoticeResponse resp = responseDoc.addNewSetNoticeResponse();
	 	resp.setCode(adapter.getResponse().getCode());
	 	resp.setDescription(adapter.getResponse().getDescription());
		return responseDoc;
	}

}

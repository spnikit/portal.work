package com.aisa.portal.invoice.notice.endpoints;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.UUID;

import org.springframework.oxm.Marshaller;
import org.springframework.ws.client.core.WebServiceTemplate;
import org.springframework.ws.soap.client.core.SoapActionCallback;

import com.aisa.portal.invoice.integration.endpoints.objects.DocumentsObj;
import com.aisa.portal.invoice.integration.facade.PortalSFFacade;
import com.aisa.portal.invoice.notice.NoticeObject;
import com.aisa.portal.invoice.notice.SFparser;
import com.aisa.portal.invoice.notice.UvedObject;
import com.aisa.portal.invoice.notice.Uvedparser;
import com.sun.org.apache.bcel.internal.generic.NEW;

import ru.aisa.rgd.ws.domain.Persona;
import ru.aisa.rgd.ws.endpoint.ETDAbstractEndpoint;
import ru.aisa.rgd.ws.endpoint.ResponseAdapter;
import ru.aisa.rgd.ws.exeption.ServiceError;
import ru.aisa.rgd.ws.utility.TypeConverter;
import ru.iit.portal8888.portal.SetUvedRequestDocument;
import ru.iit.portal8888.portal.SetUvedRequestDocument.SetUvedRequest;
import ru.iit.portal8888.portal.SetUvedResponseDocument;
import ru.iit.portal8888.portal.SetUvedResponseDocument.SetUvedResponse;
import ru.iit.portal8888.portal.UvedResponseDocument;
import ru.iit.portal8888.portal.UvedResponseDocument.UvedResponse;
import rzd8888.gvc.etd.was.etd.recievenotice.RecieveNoticeRequestDocument;
import rzd8888.gvc.etd.was.etd.recievenotice.RecieveNoticeResponseDocument;
import rzd8888.gvc.etd.was.etd.recievenotice.RecieveNoticeRequestDocument.RecieveNoticeRequest;
import sheff.rjd.services.transoil.TransService;
import sheff.rjd.utils.Base64;

import org.tempuri.SUvedInvoiceDocument;
import org.tempuri.SUvedInvoiceDocument.SUvedInvoice;
import org.tempuri.SUvedInvoiceResponseDocument;

public class SetUvedEndpoint   extends Abstract<NoticeWrapper> {

	protected SetUvedEndpoint(Marshaller marshaller) {
		super(marshaller);
		// TODO Auto-generated constructor stub

	}
	private WebServiceTemplate wst;
	private String urltype = "EtdNoticeURL";
	private TransService sendtotransoil;
	private sheff.rjd.services.etd.TransService sendtonoticeetd;

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public WebServiceTemplate getWst() {
		return wst;
	}

	public void setWst(WebServiceTemplate wst) {
		this.wst = wst;
	}

	private String url;

	public TransService getSendtotransoil() {
		return sendtotransoil;
	}

	public void setSendtotransoil(TransService sendtotransoil) {
		this.sendtotransoil = sendtotransoil;
	}
	public sheff.rjd.services.etd.TransService getSendtonoticeetd() {
		return sendtonoticeetd;
	}

	public void setSendtonoticeetd(
			sheff.rjd.services.etd.TransService sendtonoticeetd) {
		this.sendtonoticeetd = sendtonoticeetd;
	}

	@Override
	ResponseAdapter<NoticeWrapper> processRequestInvoice(Object arg, PortalSFFacade facade)  {
		
		System.out.println("SetUved");
		
	SetUvedRequestDocument docreq = (SetUvedRequestDocument) arg;
		long docid=	docreq.getSetUvedRequest().getDocId();
		byte[]sgn = docreq.getSetUvedRequest().getSignature();
		byte[]xml = docreq.getSetUvedRequest().getXml();
		String korrguid = docreq.getSetUvedRequest().getKorrguid();
		
		
		try{
		
		   String xtype="SetUved";
		   SUvedInvoiceDocument invoice = SUvedInvoiceDocument.Factory.newInstance();
		   SUvedInvoice req = invoice.addNewSUvedInvoice();
		   req.setDocid(docid);
		   req.setSign(sgn);
		   req.setXml(xml);
		   req.setKorrguid(korrguid);
		   Object wsresp = null;
		   SoapActionCallback sa = new SoapActionCallback("http://tempuri.org/IRzdOperations/SUvedInvoice");		   
		   System.out.println(invoice);
		   wsresp = wst.marshalSendAndReceive(getUrl(), invoice, sa);
			   SUvedInvoiceResponseDocument responseDocument = (SUvedInvoiceResponseDocument) wsresp;
			if (!responseDocument.getSUvedInvoiceResponse().getSUvedInvoiceResult().equals("-1")){
//				System.out.println("if");
//				facade.getIntegrationDao().updtdfkorr1(docid, responseDocument.getSUvedInvoiceResponse().getSUvedInvoiceResult(), 1, sgn);
				sendtotransoil.SendSFNotice(docid, 9);
				//  if(facade.getIntegrationDao().IsSellerServerSGN(docid)){
					  sendtonoticeetd.SendSFNotice(docid, 9, facade.getIntegrationDao().IsSellerServerSGN(docid));
				//  }
				NoticeObject not = new NoticeObject();
				not.setFileSignature(Base64.encodeBytes(sgn));
				Uvedparser parser = new Uvedparser();
				parser.ParseUved(not, new String(xml,"windows-1251" ));
			
			String guid  = UUID.randomUUID().toString();
			RecieveNoticeRequestDocument xmldoc = RecieveNoticeRequestDocument.Factory.newInstance();
			  RecieveNoticeRequest xmlreq  = xmldoc.addNewRecieveNoticeRequest();
			xmlreq.setXml(not.Generate());
			xmlreq.setConfirmid(guid);
			xmlreq.setEtdid(facade.GetEtdDocid(docid));
			xmlreq.setXtype("SNCUV");
			RecieveNoticeResponseDocument response = RecieveNoticeResponseDocument.Factory.newInstance();
			try{
//				response = (RecieveNoticeResponseDocument) wst.marshalSendAndReceive(facade.GetEtdNoticeURL(urltype), xmldoc);
				wst.marshalSendAndReceive(facade.GetEtdNoticeURL(urltype), xmldoc);
				} catch (Exception e){
				StringWriter outError = new StringWriter();
			    PrintWriter errorWriter = new PrintWriter(outError);
			    e.printStackTrace(errorWriter);
			    log.error(outError.toString());
			    e.printStackTrace();
			}
			
			}
			
			
			
			
			
			
			
			NoticeWrapper wrapper=new NoticeWrapper();
			
			wrapper.setStage(0);
			wrapper.setDescription("OK");
			wrapper.setCode(0);
			wrapper.setNoticeType(xtype);
			return new ResponseAdapter<NoticeWrapper>(true, wrapper,
					ServiceError.ERR_OK);
		
		 
		
		}
		catch (Exception e){
			e.printStackTrace();
			log.error(TypeConverter.exceptionToString(e));
		}
		
		NoticeWrapper wrapper=new NoticeWrapper();
		wrapper.setStage(-1);
		wrapper.setDescription("OK");
		wrapper.setCode(0);
		return new ResponseAdapter<NoticeWrapper>(true, wrapper,
				ServiceError.ERR_OK);
	}

	@Override
	Object composeResponceInvoice(ResponseAdapter<NoticeWrapper> adapter) {
		SetUvedResponseDocument response=SetUvedResponseDocument.Factory.newInstance();
		SetUvedResponse resp = response.addNewSetUvedResponse();
		
		if (adapter.isStatus()){
		resp.setCode(adapter.getResponse().getCode());
		resp.setDescription(adapter.getResponse().getDescription());
		
		}
		else{
			resp.setCode(adapter.getResponse().getCode());
	
		}
		System.out.println("response: "+response);
		return response;
	}

}


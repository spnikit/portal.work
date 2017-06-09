package com.aisa.portal.invoice.ttk;

import java.util.UUID;

import org.apache.tools.ant.util.facade.FacadeTaskHelper;
import org.springframework.ws.client.core.WebServiceTemplate;
import org.springframework.ws.soap.client.core.SoapActionCallback;
import org.tempuri.BNoticeInvoiceConfirmationDocument;
import org.tempuri.BNoticeInvoiceConfirmationDocument.BNoticeInvoiceConfirmation;
import org.tempuri.BNoticeInvoiceConfirmationResponseDocument;

import rzd8888.gvc.etd.was.etd.recievenotice.RecieveNoticeRequestDocument;
import rzd8888.gvc.etd.was.etd.recievenotice.RecieveNoticeRequestDocument.RecieveNoticeRequest;
import rzd8888.gvc.etd.was.etd.recievenotice.RecieveNoticeResponseDocument;
import sheff.rjd.services.transoil.TransService;

import com.aisa.portal.invoice.integration.dao.IntegrationDao;


public class SellerUvedConfirmationToTTK {
	private WebServiceTemplate wst;
	private String url;
	private String urltype = "EtdNoticeURL";
	private TransService sendtotransoil;
	private sheff.rjd.services.etd.TransService sendtonoticeetd;

	public IntegrationDao getIntegrationDao() {
		return integrationDao;
	}
	public void setIntegrationDao(IntegrationDao integrationDao) {
		this.integrationDao = integrationDao;
	}
	private IntegrationDao integrationDao;


	public WebServiceTemplate getWst() {
		return wst;
	}

	public void setWst(WebServiceTemplate wst) {
		this.wst = wst;
	}
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
	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}
		
		
	public boolean processSellerUvedConfirmation(String confirmationId, long docid, byte[] xmlFNS, byte[] sing) throws Exception{
		System.out.println("processSellerUvedConfirmation");
//		System.out.println("isSeller:"+ getIntegrationDao().IsSellerServerSGN(docid));
		String guid = UUID.randomUUID().toString();
		if (sing.length>4){
			int status = 2;
			if (getIntegrationDao().GetIsSendForKorr(docid, status)){
				if(getIntegrationDao().updtdfkorr2WithGUID(docid, guid, status, xmlFNS, sing)){
//					System.out.println("if1");
					sendtonoticeetd.SendSFNotice(docid, 10, getIntegrationDao().IsBuyerServerSGN(docid));
				}
			}
			else {
				if(getIntegrationDao().updtdfkorr2WithoutGUID(docid, guid, status, xmlFNS, sing)){
//					System.out.println("if2");
					sendtonoticeetd.SendSFNotice(docid, 10, getIntegrationDao().IsBuyerServerSGN(docid));
				}
			}
//			System.out.println("send");
			
			getIntegrationDao().updtgfsgn(docid);
			
			sendtotransoil.SendSFNotice(docid, 10);
			//if(getIntegrationDao().IsSellerServerSGN(docid)){
				//sendtonoticeetd.SendSFNotice(docid, 10, getIntegrationDao().IsSellerServerSGN(docid));
			//}
		}
		else {
			SendDataToSigner(docid, guid, xmlFNS, sing);
		}
		
		
		return true;
	
		
		
	}

	public String SendData(long docid,String Id, byte[] xmlFNS, byte[] sing) throws Exception{
		BNoticeInvoiceConfirmationDocument document=BNoticeInvoiceConfirmationDocument.Factory.newInstance();
		BNoticeInvoiceConfirmation invoce = document.addNewBNoticeInvoiceConfirmation();
		invoce.setInvoiceId(Id);
		invoce.setXml(xmlFNS);
		invoce.setInvoiceUserId(docid);
		invoce.setSign(sing);
		BNoticeInvoiceConfirmationResponseDocument response = (BNoticeInvoiceConfirmationResponseDocument) getWst().marshalSendAndReceive(getUrl(), document,new  SoapActionCallback("http://tempuri.org/IRzdOperations/BNoticeInvoiceConfirmation"));
		return  response.getBNoticeInvoiceConfirmationResponse().getBNoticeInvoiceConfirmationResult();
	}
	public String SendDataToSigner(long docid,String Id, byte[] xmlFNS, byte[] sing) throws Exception{
		
		RecieveNoticeRequestDocument xmldoc = RecieveNoticeRequestDocument.Factory.newInstance();
		  RecieveNoticeRequest xmlreq  = xmldoc.addNewRecieveNoticeRequest();
		  try{
		 getIntegrationDao().updtGUIDforkorr2(docid, Id);
		xmlreq.setXmlbytes(xmlFNS);
		xmlreq.setConfirmid(Id);
		xmlreq.setEtdid(getIntegrationDao().GetEtdDocid(docid));
		xmlreq.setXtype("SNCUV");
		RecieveNoticeResponseDocument response = RecieveNoticeResponseDocument.Factory.newInstance();

			wst.marshalSendAndReceive(getIntegrationDao().GetEtdNoticeURL(urltype), xmldoc);
			} catch (Exception e){
				e.printStackTrace();
			
		}
		return Id;
	}
}

package com.aisa.portal.invoice.ttk;

import org.springframework.ws.client.core.WebServiceTemplate;
import org.springframework.ws.soap.client.core.SoapActionCallback;
import org.tempuri.BNoticeConfirmationDocument;
import org.tempuri.BNoticeConfirmationDocument.BNoticeConfirmation;
import org.tempuri.BNoticeConfirmationResponseDocument;

import sheff.rjd.services.transoil.TransService;

import com.aisa.portal.invoice.integration.dao.IntegrationDao;
 

public class BuyerNoticeConfirmationToTTK {
	private WebServiceTemplate wst;
	public String getUrl() {
		return url;
	}
	public IntegrationDao getIntegrationDao() {
		return integrationDao;
	}

	public void setIntegrationDao(IntegrationDao integrationDao) {
		this.integrationDao = integrationDao;
	}
	private IntegrationDao integrationDao;
	
	public void setUrl(String url) {
		this.url = url;
	}
	
	private String url;
	public WebServiceTemplate getWst() {
		return wst;
	}

	public void setWst(WebServiceTemplate wst) {
		this.wst = wst;
	}
	 private TransService sendtotransoil;
	 private sheff.rjd.services.etd.TransService sendtonoticeetd;
	 
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
	public boolean processBuyerNoticeConfirmation(String confirmationId, long docid, byte[] xmlFNS, byte[] sing) throws Exception{
//		System.out.println("processBuyerNoticeConfirmation");
		int status=8;
		String guid=SendData(docid, confirmationId,xmlFNS,sing);
		
		if (getIntegrationDao().IsSellerServerSGN(docid)){
			getIntegrationDao().updtgfsgn(docid);
		}
		
		if (getIntegrationDao().updateBuyerNoticeConfirmation(docid, status, guid, xmlFNS, sing)){
			sendtotransoil.SendSFNotice(docid, status);
			//if(getIntegrationDao().IsSellerServerSGN(docid)){
				sendtonoticeetd.SendSFNotice(docid, status, getIntegrationDao().IsSellerServerSGN(docid));
			//}
			return true;
		}
		else return false;
//		return getIntegrationDao().updateBuyerNoticeConfirmation(docid, status, guid, xmlFNS, sing);
//		return getIntegrationDao().updateBuyerNoticeConfirmation(docid, status, guid, parsexml.parse(xmlFNS), sing);
	}

	public String SendData(long docid,String Id, byte[] xmlFNS, byte[] sing) throws Exception{
		BNoticeConfirmationDocument document=BNoticeConfirmationDocument.Factory.newInstance();
		BNoticeConfirmation invoce = document.addNewBNoticeConfirmation();
		invoce.setConfirmationId(Id);
		invoce.setXml(xmlFNS);
		invoce.setInvoiceUserId(docid);
		invoce.setSign(sing);
		BNoticeConfirmationResponseDocument response = (BNoticeConfirmationResponseDocument) getWst().marshalSendAndReceive(getUrl(), document ,new  SoapActionCallback("http://tempuri.org/IRzdOperations/BNoticeConfirmation"));
		return  response.getBNoticeConfirmationResponse().getBNoticeConfirmationResult();
	}
}

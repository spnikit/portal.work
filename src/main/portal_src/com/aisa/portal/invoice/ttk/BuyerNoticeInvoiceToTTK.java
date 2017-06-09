package com.aisa.portal.invoice.ttk;

import org.springframework.ws.client.core.WebServiceTemplate;
import org.springframework.ws.soap.client.core.SoapActionCallback;
import org.tempuri.BNoticeInvoiceDocument;
import org.tempuri.BNoticeInvoiceDocument.BNoticeInvoice;
import org.tempuri.BNoticeInvoiceResponseDocument;

import sheff.rjd.services.transoil.TransService;

import com.aisa.portal.invoice.integration.dao.IntegrationDao;


public class BuyerNoticeInvoiceToTTK {
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
		
	public boolean processBuyerNoticeInvoice(String invoiceId, long docid, byte[] xmlFNS, byte[] sing) throws Exception{
//		System.out.println("processBuyerNoticeInvoice");
		int status=4;
		String guid=SendData(docid,invoiceId,xmlFNS,sing);
		if (getIntegrationDao().updateBuyerNoticeInvoice(docid, status, guid, xmlFNS, sing)){
			sendtotransoil.SendSFNotice(docid, status);
			//if(getIntegrationDao().IsSellerServerSGN(docid)){
				sendtonoticeetd.SendSFNotice(docid, status, getIntegrationDao().IsSellerServerSGN(docid));
			//}
			return true;
		} else return false;
//		return getIntegrationDao().updateBuyerNoticeInvoice(docid, status, guid, parsexml.parse(xmlFNS), sing);
	}

	public String SendData(long docid,String Id, byte[] xmlFNS, byte[] sing) throws Exception{
		BNoticeInvoiceDocument document=BNoticeInvoiceDocument.Factory.newInstance();
		BNoticeInvoice invoice = document.addNewBNoticeInvoice();
		invoice.setInvoiceId(Id);
		invoice.setXml(xmlFNS);
		invoice.setSign(sing);
		invoice.setInvoiceUserId(docid);
		BNoticeInvoiceResponseDocument response = (BNoticeInvoiceResponseDocument) getWst().marshalSendAndReceive(getUrl(), document, new SoapActionCallback("http://tempuri.org/IRzdOperations/BNoticeInvoice"));
 
		return  response.getBNoticeInvoiceResponse().getBNoticeInvoiceResult();
		
	}
}

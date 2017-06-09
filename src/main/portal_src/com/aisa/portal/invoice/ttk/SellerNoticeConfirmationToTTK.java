package com.aisa.portal.invoice.ttk;

import org.springframework.ws.client.core.WebServiceTemplate;
import org.springframework.ws.soap.client.core.SoapActionCallback;
import org.tempuri.SNoticeConfirmationDocument;
import org.tempuri.SNoticeConfirmationDocument.SNoticeConfirmation;
import org.tempuri.SNoticeConfirmationResponseDocument;

import sheff.rjd.services.transoil.TransService;

import com.aisa.portal.invoice.integration.dao.IntegrationDao;


public class SellerNoticeConfirmationToTTK {
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
		
	public boolean processSellerNoticeConfirmation(String confirmationId, long docid, byte[] xmlFNS, byte[] sing) throws Exception{
//		System.out.println("processSellerNoticeConfirmation");
		int status=2;
		String guid=SendData(docid,confirmationId,xmlFNS,sing);
		
		if (getIntegrationDao().IsBuyerServerSGN(docid)){
			getIntegrationDao().updtgfsgn(docid);
		}
		
		if  (getIntegrationDao().updateSellerNoticeConfirmation(docid, status, guid, xmlFNS, sing)){
			sendtotransoil.SendSFNotice(docid, status);
			//if(getIntegrationDao().IsBuyerServerSGN(docid)){
				sendtonoticeetd.SendSFNotice(docid, status, getIntegrationDao().IsBuyerServerSGN(docid));
			//}
			return true;
		} else return false;
//		return getIntegrationDao().updateSellerNoticeConfirmation(docid, status, guid, parsexml.parse(xmlFNS), sing);
	}

	public String SendData(long docid,String Id, byte[] xmlFNS, byte[] sing) throws Exception{
		SNoticeConfirmationDocument document=SNoticeConfirmationDocument.Factory.newInstance();
		SNoticeConfirmation invoce = document.addNewSNoticeConfirmation();
		invoce.setConfirmationId(Id);
		invoce.setXml(xmlFNS);
		invoce.setSign(sing);
		invoce.setInvoiceUserId(docid);
		SNoticeConfirmationResponseDocument response = (SNoticeConfirmationResponseDocument) getWst().marshalSendAndReceive(getUrl(), document,new SoapActionCallback("http://tempuri.org/IRzdOperations/SNoticeConfirmation"));
		return  response.getSNoticeConfirmationResponse().getSNoticeConfirmationResult();
	}
}

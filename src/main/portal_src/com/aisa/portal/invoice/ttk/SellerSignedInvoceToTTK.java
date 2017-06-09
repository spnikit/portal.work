package com.aisa.portal.invoice.ttk;

import org.apache.log4j.Logger;
import org.springframework.ws.client.core.WebServiceTemplate;
import org.springframework.ws.soap.client.core.SoapActionCallback;
import org.tempuri.KorrSInvoiceDocument;
import org.tempuri.KorrSInvoiceDocument.KorrSInvoice;
import org.tempuri.KorrSInvoiceResponseDocument;
import org.tempuri.SInvoiceDocument;
import org.tempuri.SInvoiceDocument.SInvoice;
import org.tempuri.SInvoiceResponseDocument;

import sheff.rjd.services.transoil.TransService;

import com.aisa.portal.invoice.integration.dao.IntegrationDao;


public class SellerSignedInvoceToTTK {

	private static Logger	log	= Logger.getLogger(SellerSignedInvoceToTTK.class);


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
		
	public boolean processSF(String bcabinetid,String scabinetid, long docid, byte[] xmlFNS, byte[] sign, boolean iskorr) throws Exception{
		int status=0;

	try{
		getIntegrationDao().insertSellerSignedInvoceNoGuid(docid, status, xmlFNS, sign, scabinetid, bcabinetid)	;
	} catch (Exception e){
		e.printStackTrace();
	}
		String guid=SendData(bcabinetid,scabinetid ,docid,xmlFNS,sign, iskorr);
		if (getIntegrationDao().updateSignedInvoceGuid(docid, guid)){
			sendtotransoil.SendSFNotice(docid, status);
			//if(getIntegrationDao().IsSellerServerSGN(docid)){
				sendtonoticeetd.SendSFNotice(docid, status, getIntegrationDao().IsSellerServerSGN(docid));
			//}
			return true;
		} else return false;
		
	//	String guid=SendData(bcabinetid,scabinetid ,docid,xmlFNS,sign);
	//	return getIntegrationDao().insertSellerSignedInvoce(docid, status, guid, xmlFNS, sign,scabinetid,bcabinetid);
	
	
	}

	public String SendData(String bcabinetid,String  scabinetid,    long docid, byte[] xmlFNS, byte[] sign, boolean isKorr) throws Exception{
//		System.out.println(isKorr);
		if (!isKorr){
		SInvoiceDocument document=SInvoiceDocument.Factory.newInstance();
		SInvoice invoce = document.addNewSInvoice();
		invoce.setSCabinetId(scabinetid);
		invoce.setBCabinetId(bcabinetid);
		invoce.setInvoiceUserId(docid);
		invoce.setXml(xmlFNS);
		invoce.setSign(sign);
//		System.out.println(document);
		SInvoiceResponseDocument response = null;
		while(response == null){
		try{
			response = (SInvoiceResponseDocument) getWst().marshalSendAndReceive(getUrl(), document,new SoapActionCallback("http://tempuri.org/IRzdOperations/SInvoice"));	
		}catch(Exception e){
			response = null;
			Thread.sleep(1000);
		}
		}
		return response.getSInvoiceResponse().getSInvoiceResult();
		}
		else {
		KorrSInvoiceDocument document=KorrSInvoiceDocument.Factory.newInstance();
		KorrSInvoice invoce = document.addNewKorrSInvoice();
			invoce.setSCabinetId(scabinetid);
			invoce.setBCabinetId(bcabinetid);
			invoce.setInvoiceUserId(docid);
			invoce.setXml(xmlFNS);
			invoce.setSign(sign);
			KorrSInvoiceResponseDocument response = null;
			while(response == null){
				try{
				response = (KorrSInvoiceResponseDocument) getWst().marshalSendAndReceive(getUrl(), document,new SoapActionCallback("http://tempuri.org/IRzdOperations/KorrSInvoice"));	
				}catch(Exception e){
					response = null;
					Thread.sleep(1000);
				}
		
			
			}
		return response.getKorrSInvoiceResponse().getKorrSInvoiceResult();
		}
		
	}
}

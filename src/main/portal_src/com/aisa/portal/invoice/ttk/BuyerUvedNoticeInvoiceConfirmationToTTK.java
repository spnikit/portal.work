package com.aisa.portal.invoice.ttk;

import java.util.UUID;

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


public class BuyerUvedNoticeInvoiceConfirmationToTTK {
	private WebServiceTemplate wst;
	private String urltype = "EtdNoticeURL";
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
		
	public boolean processBuyerUvedNoticeInvoiceConfirmation(String confirmationId, long docid, byte[] xmlFNS, byte[] sing) throws Exception{
		System.out.println("processBuyerUvedNoticeInvoiceConfirmation");
//		System.out.println("isSeller:"+ getIntegrationDao().IsSellerServerSGN(docid));
		String guid = UUID.randomUUID().toString();
		if (sing.length>4){
//			System.out.println("processSellerUvedConfirmation");
			int status = 1;
			if (getIntegrationDao().GetIsSendForKorr(docid, status)){
				if(getIntegrationDao().updtdfkorr1WithGUID(docid, guid, status, xmlFNS, sing)){
//					System.out.println("if1");
					sendtonoticeetd.SendSFNotice(docid, 9, getIntegrationDao().IsSellerServerSGN(docid));
				}
			}
				else {		
					if(getIntegrationDao().updtdfkorr1WithoutGUID(docid, guid, status, xmlFNS, sing)){
//						System.out.println("if2");
						sendtonoticeetd.SendSFNotice(docid, 9, getIntegrationDao().IsBuyerServerSGN(docid));
					}
				}
//			System.out.println("send");
			
			sendtotransoil.SendSFNotice(docid, 9);
			//if(getIntegrationDao().IsSellerServerSGN(docid)){
			//	sendtonoticeetd.SendSFNotice(docid, 9, getIntegrationDao().IsSellerServerSGN(docid));
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
			  
		getIntegrationDao().updtGUIDforkorr1(docid, Id);
		xmlreq.setXmlbytes(xmlFNS);
		xmlreq.setConfirmid(Id);
		xmlreq.setEtdid(getIntegrationDao().GetEtdDocid(docid));
		xmlreq.setXtype("BNUV");
		RecieveNoticeResponseDocument response = RecieveNoticeResponseDocument.Factory.newInstance();
		System.out.println(xmldoc);
			wst.marshalSendAndReceive(getIntegrationDao().GetEtdNoticeURL(urltype), xmldoc);
			} catch (Exception e){
				e.printStackTrace();
			
		}
		return Id;
	}
}

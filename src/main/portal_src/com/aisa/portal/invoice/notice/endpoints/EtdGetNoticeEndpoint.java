package com.aisa.portal.invoice.notice.endpoints;

import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.oxm.Marshaller;
import org.springframework.ws.client.core.WebServiceTemplate;
import org.springframework.ws.server.endpoint.AbstractMarshallingPayloadEndpoint;
import org.springframework.ws.soap.client.core.SoapActionCallback;
import org.tempuri.BUvedInvoiceConfirmationDocument;
import org.tempuri.BUvedInvoiceConfirmationDocument.BUvedInvoiceConfirmation;

import ru.iit.portal.etdgetnotice.EtdGetnoticeRequestDocument;
import ru.iit.portal.etdgetnotice.EtdGetnoticeRequestDocument.EtdGetnoticeRequest;
import ru.iit.portal.etdgetnotice.EtdGetnoticeResponseDocument;
import ru.iit.portal.etdgetnotice.EtdGetnoticeResponseDocument.EtdGetnoticeResponse;
import sheff.rjd.services.transoil.TransService;

import com.aisa.portal.invoice.integration.facade.PortalSFFacade;
import com.aisa.portal.invoice.integration.security.Manager;
public class EtdGetNoticeEndpoint extends AbstractMarshallingPayloadEndpoint{
	private NamedParameterJdbcTemplate npjt;
	protected PortalSFFacade facade;
	private Manager securitymanager;
	private WebServiceTemplate wst;
	private String url;
	private TransService sendtotransoil;  
	
	public Manager getSecuritymanager() {
			return securitymanager;
		}

		public PortalSFFacade getFacade() {
		return facade;
	}

	public void setFacade(PortalSFFacade facade) {
		this.facade = facade;
	}

		public void setSecuritymanager(Manager securitymanager) {
			this.securitymanager = securitymanager;
		}

		public NamedParameterJdbcTemplate getNpjt() {
		return npjt;
	    }

	    public void setNpjt(NamedParameterJdbcTemplate npjt) {
		this.npjt = npjt;
	    }
	    
public WebServiceTemplate getWst() {
			return wst;
		}

		public void setWst(WebServiceTemplate wst) {
			this.wst = wst;
		}

		public String getUrl() {
			return url;
		}

		public void setUrl(String url) {
			this.url = url;
		}
//
public TransService getSendtotransoil() {
			return sendtotransoil;
		}

		public void setSendtotransoil(TransService sendtotransoil) {
			this.sendtotransoil = sendtotransoil;
		}

public EtdGetNoticeEndpoint(Marshaller marshaller){
	super(marshaller);
}
	
		protected Object invokeInternal(Object requestObject) throws Exception{
		
			EtdGetnoticeRequestDocument noticedoc = (EtdGetnoticeRequestDocument)requestObject;
			EtdGetnoticeRequest notice = noticedoc.getEtdGetnoticeRequest();
			
			EtdGetnoticeResponseDocument respdoc = EtdGetnoticeResponseDocument.Factory.newInstance();
			EtdGetnoticeResponse resp = respdoc.addNewEtdGetnoticeResponse();
			// TODO Auto-generated method stub
//			if (notice.getXtype().equals("SNCUV")){
//				
//				facade.getIntegrationDao().updtdfkorr2(facade.GetPortalid(notice.getEtdid()), notice.getConfirmid(), 2, notice.getXml(), notice.getSign());
//				 sendtotransoil.SendSFNotice(facade.GetPortalid(notice.getEtdid()),10);
//				try{
//					Object wsresp = null;
//					BUvedInvoiceConfirmationDocument invoice = BUvedInvoiceConfirmationDocument.Factory.newInstance();
//					BUvedInvoiceConfirmation req = invoice.addNewBUvedInvoiceConfirmation();
//					req.setDocid(facade.GetPortalid(notice.getEtdid()));
//					req.setKorrguid("-1");
//					req.setSign(notice.getSign());
//					req.setXml(notice.getXml());
//					req.setUuid(notice.getConfirmid());
//					
//					SoapActionCallback sa = new SoapActionCallback("http://tempuri.org/IRzdOperations/BUvedInvoiceConfirmation");		   
//					   wsresp = wst.marshalSendAndReceive(getUrl(), invoice, sa);
//				}catch(Exception e){
//					e.printStackTrace();
//				}	 		
//				
//				
//				
//				resp.setCode(0);
//				resp.setDescription("ok");
//				resp.setEtdid(notice.getEtdid());
//			}
//			else {
		try{
			facade.processNotice(notice.getXtype(), notice.getConfirmid(), facade.GetPortalid(notice.getEtdid()), notice.getXml(), notice.getSign());
			resp.setCode(0);
			resp.setDescription("ok");
			resp.setEtdid(notice.getEtdid());
		
		
		} catch (Exception e){
			e.printStackTrace();
			resp.setCode(-1);
			resp.setDescription(e.getMessage());
			resp.setEtdid(notice.getEtdid());
		
		}
//		}
			return respdoc;
		}
}

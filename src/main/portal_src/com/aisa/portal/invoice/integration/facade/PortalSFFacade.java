package com.aisa.portal.invoice.integration.facade;

import java.util.List;

import ru.aisa.rgd.ws.dao.ServiceFacade;
import ru.aisa.rgd.ws.domain.SFinfo;
import ru.aisa.rgd.ws.exeption.ServiceException;

import com.aisa.portal.invoice.integration.dao.IntegrationDao;
import com.aisa.portal.invoice.integration.endpoints.objects.DocumentsObj;
import com.aisa.portal.invoice.notice.NoticeObject;
import com.aisa.portal.invoice.notice.UvedObject;

public interface PortalSFFacade extends ServiceFacade {
	IntegrationDao getIntegrationDao();
	NoticeObject GenerateNotice(String xtype,String xml, String sign) throws Exception;
	UvedObject GenerateUvNotice(String xtype, String xml,String sign) throws Exception;
	void  processNotice(String xtype, String id, long  docid, byte[] xmlFNS, byte[] sign) throws Exception;
	
	boolean CheckSignatureXML(byte[] xml, byte[] sgn);
	byte[] ConsrtuctSignature(NoticeObject xml) throws Exception;
	byte[] ConsrtuctSignature(NoticeObject xml, String alias) throws Exception;
	byte[] ConsrtuctSignature(byte[] xml) throws Exception;
	String GetCabinetId (int predid)  throws Exception;
	boolean IsSeller (String cabinetid, long docid)  throws Exception;
	boolean IsKorrSF (long docid)  throws Exception;
	boolean IsDropped (long docid)  throws Exception;
	int GetInvoiceStage( long docid);
	int GetDropInvoiceStage( long docid);
	String GetInvoiceId( long docid);
	DocumentsObj GetInvoice(long docid);
	DocumentsObj GetInvoiceStageMap(long docid);
	DocumentsObj GetBuyerInvoiceConfirmation(long docid);
	DocumentsObj GetBuyerConfirmation(long docid);
	DocumentsObj GetSellerConfirmation(long docid);
	DocumentsObj GetBuyerUvedNotice(long docid);
	 boolean IsBuyerServerSGN(long docid);
	 long GetEtdDocid(long id);
	 long GetPortalid(long etdid);
	 String GetEtdNoticeURL(String url);
	 String GetSFXml (long docid);
	 List<SFinfo> getSF(int predid, int wrkid)throws ServiceException;
	 boolean GetIsSendForKorr(long docid, int Stage);
}

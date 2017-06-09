package com.aisa.portal.invoice.integration.facade;

import java.io.UnsupportedEncodingException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;

import org.springframework.jdbc.core.simple.ParameterizedRowMapper;

import com.aisa.portal.invoice.integration.dao.IntegrationDao;
import com.aisa.portal.invoice.integration.endpoints.objects.DocumentsObj;
import com.aisa.portal.invoice.integration.security.Manager;
import com.aisa.portal.invoice.notice.NoticeGenerator;
import com.aisa.portal.invoice.notice.NoticeObject;
import com.aisa.portal.invoice.notice.UvedObject;
import com.aisa.portal.invoice.ttk.dispatcher.PortalDispatcher;

import ru.aisa.rgd.ws.dao.JdbcServiceFacade;
import ru.aisa.rgd.ws.domain.SFinfo;
import ru.aisa.rgd.ws.exeption.ServiceException;
 

public class PortalSFServiceFacade extends JdbcServiceFacade implements PortalSFFacade {
	
	private IntegrationDao integrationDao;
	private NoticeGenerator noticeGenerator;
	 
    public NoticeGenerator getNoticeGenerator() {
		return noticeGenerator;
	}
   
	public void setNoticeGenerator(NoticeGenerator noticeGenerator) {
		this.noticeGenerator = noticeGenerator;
	}

	public Manager getSecuritymanager() {
		return securitymanager;
	}

	public void setSecuritymanager(Manager securitymanager) {
		this.securitymanager = securitymanager;
	}

	private Manager securitymanager;
	public IntegrationDao getIntegrationDao() {
		return integrationDao;
	}

	public void setIntegrationDao(IntegrationDao integrationDao) {
		if (integrationDao == null) throw new IllegalStateException("IntegrationDao hasn't been initialized");
		this.integrationDao = integrationDao;
	}

	 

	public boolean CheckSignatureXML(byte[] xml, byte[] sgn) {
		boolean chsgn=false;
		chsgn=securitymanager.CheckSignatureXML(xml, sgn);
		System.out.println("securitymanager.CheckSignatureXML(xml, sgn); "+chsgn);
		if (!chsgn){
			try {
				System.out.println(new String (xml, "UTF-8"));
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return chsgn;
	}

	public NoticeObject GenerateNotice(String xtype, String xml, String sign) throws Exception {
		//return	noticeGenerator.createNoticeTest(xtype, xml, sign);
		 return	noticeGenerator.createNotice(xtype, xml, sign);
		 
	}
	public UvedObject GenerateUvNotice(String xtype,String xml,String sign)throws Exception {
		
		//  facade.getIntegrationDao().insertDFKorr(docid, guid, 0, Korr)
		
		return noticeGenerator.createUVNotice(xtype, xml, sign);
	}
	public byte[] ConsrtuctSignature(NoticeObject xml) throws Exception {
		//return ((PortalDispatcher)getDispatcher()).CounstructSignature(xml);
		xml.setSignerName("Иван"); 
		xml.setSignerPatronymic("Иванович");
		xml.setSignerSurname("Тестовый");
		xml.setSignerPosition("Главный бухгалтер");
		return securitymanager.ConsrtuctSignature(xml.Generate().getBytes("UTF-8"),"Test1");
	}
	
	public byte[] ConsrtuctSignature(byte[] xml) throws Exception {
		//return ((PortalDispatcher)getDispatcher()).CounstructSignature(xml);
	 
		return securitymanager.ConsrtuctSignature(xml,"Test1");
	}

	public void processNotice(String xtype, String id, long docid,
			byte[] xmlFNS, byte[] sign) throws Exception {
//		System.out.println("xtype: "+xtype);
//		System.out.println("id: "+id);
//		System.out.println("docid: "+docid);
		//sign=securitymanager.ConsrtuctSignature(xmlFNS);
		noticeGenerator.ProcessAndSend(xtype, id, docid, xmlFNS, sign);
		
	}

	public String GetCabinetId(int predid) throws Exception { 
		return getIntegrationDao().GetCabinetId(predid);
	}

	public boolean IsSeller(String cabinetid, long docid) throws Exception {
		 
		return getIntegrationDao().IsSeller(cabinetid,docid);
	}
	public boolean IsKorrSF(long docid) throws Exception {
		 
		return getIntegrationDao().IsKorrSF(docid);
	}
	public int GetInvoiceStage(long docid) {
		return getIntegrationDao().GetInvoiceStage( docid);
	}
	public int GetDropInvoiceStage(long docid) {
		return getIntegrationDao().GetDropStatusForID(docid);
	}
	public DocumentsObj GetInvoice(long docid) {
		return getIntegrationDao().GetInvoice(docid);
	}

	public DocumentsObj GetBuyerInvoiceConfirmation(long docid) {
		// TODO Auto-generated method stub
		return getIntegrationDao().GetBuyerInvoiceConfirmation(docid);
	}

	public DocumentsObj GetBuyerConfirmation(long docid) {
		
		return getIntegrationDao().GetBuyerConfirmation(docid);
	}
public DocumentsObj GetBuyerUvedNotice(long docid) {
		
		return getIntegrationDao().GetBuyerUvedNotice(docid);
	}
	public byte[] ConsrtuctSignature(NoticeObject xml, String alias)
			throws Exception { 
		
		System.out.println(xml.Generate());
		
		
		xml.setSignerName("Иван"); 
		xml.setSignerPatronymic("Иванович");
		xml.setSignerSurname("Тестовый");
		xml.setSignerPosition("Главный бухгалтер");
		return securitymanager.ConsrtuctSignature(xml.Generate().getBytes("windows-1251"), alias);
	}

	public String GetInvoiceId(long docid) {
		return getIntegrationDao().GetInvoiceId(docid);
	}

	public DocumentsObj GetInvoiceStageMap(long docid) {
		return getIntegrationDao().GetStatusMapForID(docid); 
		 
	}
	public long GetEtdDocid(long id) {
		return getIntegrationDao().GetEtdDocid(id); 
		 
	}
	public long GetPortalid(long etdid) {
		return getIntegrationDao().GetPortalid(etdid); 
		 
	}
	public boolean IsBuyerServerSGN(long docid) {
	    // TODO Auto-generated method stub
	    return false;
	}

	@Override
	public String GetEtdNoticeURL(String url) {
		// TODO Auto-generated method stub
		return getIntegrationDao().GetEtdNoticeURL(url);
	}

	public String GetSFXml(long docid) {
		// TODO Auto-generated method stub
		return getIntegrationDao().GetSF(docid);
	}
	
	@Override
	public List<SFinfo> getSF(int predid, int wrkid){
		return getIntegrationDao().getSF(predid,wrkid);
		
	}

	@Override
	public DocumentsObj GetSellerConfirmation(long docid) {
		// TODO Auto-generated method stub
		return getIntegrationDao().GetSellerConfirmation(docid);
	}

	@Override
	public boolean IsDropped(long docid) throws Exception {
		return getIntegrationDao().IsDropped(docid);
	}

	@Override
	public boolean GetIsSendForKorr(long docid, int Stage) {
		return getIntegrationDao().GetIsSendForKorr(docid, Stage);
	}
	
}

package com.aisa.portal.invoice.notice;

import java.io.FileInputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

import com.aisa.portal.invoice.notice.documentsParsers.Abstract;
import com.aisa.portal.invoice.notice.documentsProcessors.AbstractProcessor;

public class NoticeGenerator {

	int xmltype = 0;

	public Abstract[] getParsers() {
		return parsers;
	}

	public void setParsers(Abstract[] parsers) {
		this.parsers = parsers;
	}

	public AbstractProcessor[] getProcessors() {
		return processors;
	}

	public void setProcessors(AbstractProcessor[] processors) {
		this.processors = processors;
	}

	Abstract[] parsers;
	AbstractProcessor[] processors;

	public String createNoticeTest(String xtype, String xml, String sign)
			throws Exception {
		FileInputStream in = new FileInputStream("/home/zpss/work/testSF3.xml");
		FileChannel fch = in.getChannel();

		ByteBuffer bf = ByteBuffer.allocate((int) fch.size());
		fch.read(bf);
		fch.close();
		in.close();
		byte[] xm1l = bf.array();
		return new String(xm1l);
	}

	public NoticeObject createNotice(String xtype, String xml, String sign)
			throws Exception {
		NoticeObject obj = new NoticeObject();

		for (int i = 0; i < parsers.length; i++) {
			if (parsers[i].getDocumentType().equals(xtype)) {

				obj = parsers[i].parse(xml, sign);

			}
		}

		return obj;
	}
	public UvedObject createUVNotice(String xtype,String xml,String sign) throws Exception {
		UvedObject uvobj = new UvedObject();
		if(xtype.equals("Sfact"))
		{
			SFparser sfp = new SFparser();
			sfp.ParseSF(uvobj, xml, sign);
			
		}
		else
		{
			KorrSFparser ksfp = new KorrSFparser();
			ksfp.ParseKorrSF(uvobj, xml, sign);
		
		}
		// byte[] Korr = uvobj.Generate().getBytes("windows-1251");
		 
		// facade.getIntegrationDao().insertDFKorr(docid, guid, 0, Korr);
		  
	
		return uvobj;
	}

	public void ProcessAndSend(String xtype, String id, long docid,
			byte[] xmlFNS, byte[] sign) throws Exception {

		for (int i = 0; i < processors.length; i++) {
			if (processors[i].getDocumentName().equals(xtype)) {
				processors[i].process(id, docid, xmlFNS, sign);
				break;
			}
		}

	}

}

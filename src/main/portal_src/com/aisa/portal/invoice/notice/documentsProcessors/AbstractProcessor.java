package com.aisa.portal.invoice.notice.documentsProcessors;

import com.aisa.portal.invoice.integration.facade.PortalSFFacade;
import com.aisa.portal.invoice.notice.NoticeObject;

public abstract  class AbstractProcessor {
	String documentName;

	public String getDocumentName() {
		return documentName;
	}
	public void setDocumentName(String documentName) {
		this.documentName = documentName;
	}
	
	public abstract void process ( String Id, long docid, byte[] xmlFNS, byte[] sing) throws Exception;
	
}

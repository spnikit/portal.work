package com.aisa.portal.invoice.notice.documentsParsers;

import com.aisa.portal.invoice.notice.NoticeObject;

public abstract  class Abstract {
	String documentName;
	String documentType;
	public String getDocumentType() {
		return documentType;
	}
	public void setDocumentType(String documentType) {
		this.documentType = documentType;
	}
	public String getDocumentName() {
		return documentName;
	}
	public void setDocumentName(String documentName) {
		this.documentName = documentName;
	}
	public abstract NoticeObject parse(String XML, String sign) throws Exception;
	
}

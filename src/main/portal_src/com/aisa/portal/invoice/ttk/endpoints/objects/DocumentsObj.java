package com.aisa.portal.invoice.ttk.endpoints.objects;

public class DocumentsObj {
	String TypeOfDocument; 
 
	String TypeOfNotice; 
	String ConfirmationId;
	public String getConfirmationId() {
		return ConfirmationId;
	}
	public void setConfirmationId(String confirmationId) {
		ConfirmationId = confirmationId;
	}
	public String getTypeOfNotice() {
		return TypeOfNotice;
	}
	public void setTypeOfNotice(String typeOfNotice) {
		TypeOfNotice = typeOfNotice;
	}
	public String getTypeOfDocument() {
		return TypeOfDocument;
	}
	public void setTypeOfDocument(String typeOfDocument) {
		TypeOfDocument = typeOfDocument;
	}
	long InvoiceId=0;
	
	public long getInvoiceId() {
		return InvoiceId;
	}
	public void setInvoiceId(long invoiceId) {
		InvoiceId = invoiceId;
	}
	
	byte[] XML;
	
	byte[] SGN;
	
	byte[] noticeSGN;
	
	public byte[] getNoticeSGN() {
		return noticeSGN;
	}
	public void setNoticeSGN(byte[] noticeSGN) {
		this.noticeSGN = noticeSGN;
	}
	String Notice;
 
	public String getNotice() {
		return Notice;
	}
	public void setNotice(String notice) {
		Notice = notice;
	}
	public byte[] getXML() {
		return XML;
	}
	public void setXML(byte[] xML) {
		XML = xML;
	}
	public byte[] getSGN() {
		return SGN;
	}
	public void setSGN(byte[] sGN) {
		SGN = sGN;
	}
	
}

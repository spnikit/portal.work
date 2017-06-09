package com.aisa.portal.invoice.integration.endpoints.objects;

import ru.aisa.rgd.utils.Base64;

import com.aisa.portal.invoice.notice.NoticeObject;

public class DocumentsObj {
	String TypeOfDocument; 
	String TypeOfNotice; 
	String ConfirmationId;
	String keyName;
	
	public int getSF_FVS1() {
		return SF_FVS1;
	}
	public void setSF_FVS1(int sF_FVS1) {
		SF_FVS1 = sF_FVS1;
	}
	public int getSF_FVS2() {
		return SF_FVS2;
	}
	public void setSF_FVS2(int sF_FVS2) {
		SF_FVS2 = sF_FVS2;
	}
	public int getSF_FVS3() {
		return SF_FVS3;
	}
	public void setSF_FVS3(int sF_FVS3) {
		SF_FVS3 = sF_FVS3;
	}
	public int getSF_FVS4() {
		return SF_FVS4;
	}
	public void setSF_FVS4(int sF_FVS4) {
		SF_FVS4 = sF_FVS4;
	}
	public int getSF_FVS5() {
		return SF_FVS5;
	}
	public void setSF_FVS5(int sF_FVS5) {
		SF_FVS5 = sF_FVS5;
	}
	public int getSF_FVS6() {
		return SF_FVS6;
	}
	public void setSF_FVS6(int sF_FVS6) {
		SF_FVS6 = sF_FVS6;
	}
	public int getSF_FVS7() {
		return SF_FVS7;
	}
	public void setSF_FVS7(int sF_FVS7) {
		SF_FVS7 = sF_FVS7;
	}
	public int getSF_FVS8() {
		return SF_FVS8;
	}
	public void setSF_FVS8(int sF_FVS8) {
		SF_FVS8 = sF_FVS8;
	}
	
	public int getSF_UV1() {
		return SF_UV1;
	}
	public void setSF_UV1(int sF_UV1) {
		SF_UV1 = sF_UV1;
	}
	public int getSF_UV2() {
		return SF_UV2;
	}
	public void setSF_UV2(int sF_UV2) {
		SF_UV2 = sF_UV2;
	}

	int SF_FVS1=0;
	int SF_FVS2=0;
	int SF_FVS3=0;
	int SF_FVS4=0;
	int SF_FVS5=0;
	int SF_FVS6=0;
	int SF_FVS7=0;
	int SF_FVS8=0;
	int SF_FULL=0;
	int SF_UV1=0;
	int SF_UV2=0;
	public boolean CheckIsFullSigned(){
		if (SF_FVS1==1 && SF_FVS2==1 && SF_FVS3==1 && SF_FVS4==1 && SF_FVS5==1 && SF_FVS6==1 && SF_FVS7==1 && SF_FVS8==1)
		return true;
		else return false;
		
	}
	public int getSF_FULL() {
		return SF_FULL;
	}
	public void setSF_FULL(int sF_FULL) {
		SF_FULL = sF_FULL;
	}
	public String getKeyName() {
		return keyName;
	}
	public void setKeyName(String keyName) {
		this.keyName = keyName;
	}
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
	NoticeObject Notice;
 
 
	public NoticeObject getNotice() {
		return Notice;
	}
	public void setNotice(NoticeObject notice) {
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
	public String getSGNBase64() {
		return Base64.encodeBytes(SGN);
	}
}

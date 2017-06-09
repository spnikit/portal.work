package ru.aisa.rgd.etd.objects;

import java.io.Serializable;

public class RtkReportObject implements Serializable{

	private String documentNumber;
	private String documentDate;
	private String FIO;
	private double sum;
	private String subdivision;
	private String dateSignatureRtk;
	private String dateSignatureDi;
	private String dataMatchingCbs;
	private String dateRejection;
	
	public String getDocumentNumber() {
		return documentNumber;
	}
	public void setDocumentNumber(String documentNumber) {
		this.documentNumber = documentNumber;
	}
	public String getDocumentDate() {
		return documentDate;
	}
	public void setDocumentDate(String documentDate) {
		this.documentDate = documentDate;
	}
	public String getFIO() {
		return FIO;
	}
	public void setFIO(String fIO) {
		FIO = fIO;
	}
	public double getSum() {
		return sum;
	}
	public void setSum(double sum) {
		this.sum = sum;
	}
	public String getSubdivision() {
		return subdivision;
	}
	public void setSubdivision(String subdivision) {
		this.subdivision = subdivision;
	}
	public String getDateSignatureRtk() {
		return dateSignatureRtk;
	}
	public void setDateSignatureRtk(String dateSignatureRtk) {
		this.dateSignatureRtk = dateSignatureRtk;
	}
	public String getDateSignatureDi() {
		return dateSignatureDi;
	}
	public void setDateSignatureDi(String dateSignatureDi) {
		this.dateSignatureDi = dateSignatureDi;
	}
	public String getDataMatchingCbs() {
		return dataMatchingCbs;
	}
	public void setDataMatchingCbs(String dataMatchingCbs) {
		this.dataMatchingCbs = dataMatchingCbs;
	}
	public String getDateRejection() {
		return dateRejection;
	}
	public void setDateRejection(String dateRejection) {
		this.dateRejection = dateRejection;
	}
	
	
	
}

package ru.aisa.rgd.etd.objects;

import java.io.Serializable;

public class SfSpsObject implements Serializable{

	String id;
	String predid;	
	String seller;
	String innSeller;
	String sfNumber;
	String sfDate;
	String summWithNds;
	String summNds;
	String ndsValue;
	String recieveDate; 
	String vagNum;
	String sfType;
	String incomeDate;
	String responseId;
	String status;
	String pending;
	
	public String getPending() {
		return pending;
	}
	public void setPending(String pending) {
		this.pending = pending;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getPredid() {
		return predid;
	}
	public void setPredid(String predid) {
		this.predid = predid;
	}
	public String getSeller() {
		return seller;
	}
	public void setSeller(String seller) {
		this.seller = seller;
	}
	public String getInnSeller() {
		return innSeller;
	}
	public void setInnSeller(String innSeller) {
		this.innSeller = innSeller;
	}
	public String getSfNumber() {
		return sfNumber;
	}
	public void setSfNumber(String sfNumber) {
		this.sfNumber = sfNumber;
	}
	public String getSfDate() {
		return sfDate;
	}
	public void setSfDate(String sfDate) {
		this.sfDate = sfDate;
	}
	public String getSummWithNds() {
		return summWithNds;
	}
	public void setSummWithNds(String summWithNds) {
		this.summWithNds = summWithNds;
	}
	public String getSummNds() {
		return summNds;
	}
	public void setSummNds(String summNds) {
		this.summNds = summNds;
	}
	public String getNdsValue() {
		return ndsValue;
	}
	public void setNdsValue(String ndsValue) {
		this.ndsValue = ndsValue;
	}
	public String getRecieveDate() {
		return recieveDate;
	}
	public void setRecieveDate(String recieveDate) {
		this.recieveDate = recieveDate;
	}
	public String getVagNum() {
		return vagNum;
	}
	public void setVagNum(String vagNum) {
		this.vagNum = vagNum;
	}
	public String getSfType() {
		return sfType;
	}
	public void setSfType(String sfType) {
		this.sfType = sfType;
	}
	public String getIncomeDate() {
		return incomeDate;
	}
	public void setIncomeDate(String incomeDate) {
		this.incomeDate = incomeDate;
	}
	public String getResponseId() {
		return responseId;
	}
	public void setResponseId(String responseId) {
		this.responseId = responseId;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	
	
	
}

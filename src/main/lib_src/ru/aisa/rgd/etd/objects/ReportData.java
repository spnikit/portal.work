package ru.aisa.rgd.etd.objects;

import java.io.Serializable;

public class ReportData implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	String repname;
	int mark;
	int dorid;
	int recieved;
	int accepted;
	int signed;
	int declined;
	int summ;
	int sf;
	int summtorek;
	
	public String getRepname() {
		return repname;
	}
	public void setRepname(String repname) {
		this.repname = repname;
	}
	public int getMark() {
		return mark;
	}
	public void setMark(int mark) {
		this.mark = mark;
	}
	public int getDorid() {
		return dorid;
	}
	public void setDorid(int dorid) {
		this.dorid = dorid;
	}
	public int getSf() {
		return sf;
	}
	public void setSf(int sf) {
		this.sf = sf;
	}
	public int getRecieved() {
		return recieved;
	}
	public void setRecieved(int recieved) {
		this.recieved = recieved;
	}
	public int getAccepted() {
		return accepted;
	}
	public void setAccepted(int accepted) {
		this.accepted = accepted;
	}
	public int getSigned() {
		return signed;
	}
	public void setSigned(int signed) {
		this.signed = signed;
	}
	public int getDeclined() {
		return declined;
	}
	public void setDeclined(int declined) {
		this.declined = declined;
	}
	public int getSumm() {
		return summ;
	}
	public void setSumm(int summ) {
		this.summ = summ;
	}
	public int getSummtorek() {
		return summtorek;
	}
	public void setSummtorek(int summtorek) {
		this.summtorek = summtorek;
	}
	
	
	
}

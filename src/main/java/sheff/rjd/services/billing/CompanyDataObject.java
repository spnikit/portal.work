package sheff.rjd.services.billing;

import java.util.ArrayList;
import java.util.List;

public class CompanyDataObject {
	
	private int signed;
	private String innCompany;
	private int countUserCompany;
	List<DocumentObject> sign;
	List<DocumentObject> all;
	
	public CompanyDataObject(){
		signed = 0;
		innCompany = "";
		countUserCompany = 0;
		sign = new ArrayList<DocumentObject>();
		all = new ArrayList<DocumentObject>();
	}
	
	
	public int getSigned(){
		return signed;
	}
	
	public void setSigned(int signed){
		this.signed = signed;
	}
	
	public String getInnCompany() {
		return innCompany;
	}
	public void setInnCompany(String innCompany) {
		this.innCompany = innCompany;
	}
	public int getCountUserCompany() {
		return countUserCompany;
	}
	public void setCountUserCompany(int countUserCompany) {
		this.countUserCompany = countUserCompany;
	}
	public List<DocumentObject> getSign() {
		return sign;
	}
	public void setSign(List<DocumentObject> sign) {
		this.sign = sign;
	}
	public List<DocumentObject> getAll() {
		return all;
	}
	public void setAll(List<DocumentObject> all) {
		this.all = all;
	}



	
}

package ru.aisa.rgd.etd.objects;

import java.lang.reflect.Field;

public class ETDWorkInformation{
	private Integer workId;
	private String workName;
	private Integer predId;
	private String predName;
	private String predRZD;
	private Integer dorId;
	private Integer fr;
	private String dorName;
	private Integer predCode;
	private String availableForms;
	private Integer headpredId;
	private String headpredName;
	private boolean expdoc;
	private boolean pdfcheck;
	
	public String getAvailableForms() {
		return availableForms;
	}
	public void setAvailableForms(String availableForms) {
		this.availableForms = availableForms;
	}
	public void addAvailableForm(String name){
		availableForms += ", " + name;
	}
	public Integer getDorId() {
		return dorId;
	}
	public void setDorId(Integer dorId) {
		this.dorId = dorId;
	}
	
	public Integer getFr() {
		return fr;
	}
	public void setFr(Integer fr) {
		this.fr = fr;
	}
	
	public String getDorName() {
		return dorName;
	}
	public void setDorName(String dorName) {
		this.dorName = dorName;
	}
	public Integer getPredCode() {
		return predCode;
	}
	public void setPredCode(Integer predCode) {
		this.predCode = predCode;
	}
	public Integer getPredId() {
		return predId;
	}
	public void setPredId(Integer predId) {
		this.predId = predId;
	}
	public String getPredName() {
		return predName;
	}
	public void setPredName(String predName) {
		this.predName = predName;
	}
	public String getPredRZD() {
		return predRZD;
	}
	public void setPredRZD(String predRZD) {
		this.predRZD = predRZD;
	}
	public Integer getWorkId() {
		return workId;
	}
	public void setWorkId(Integer workId) {
		this.workId = workId;
	}
	public String getWorkName() {
		return workName;
	}
	public void setWorkName(String workName) {
		this.workName = workName;
	}
	
	public Integer getHeadpredId() {
		return headpredId;
	}
	public void setHeadpredId(Integer headpredId) {
		this.headpredId = headpredId;
	}
	public String getHeadpredName() {
		return headpredName;
	}
	public void setHeadpredName(String headpredName) {
		this.headpredName = headpredName;
	}
	
	public boolean isExpdoc() {
		return expdoc;
	}
	public void setExpdoc(boolean expdoc) {
		this.expdoc = expdoc;
	}
	
	public boolean isPdfcheck() {
		return pdfcheck;
	}
	public void setPdfcheck(boolean pdfcheck) {
		this.pdfcheck = pdfcheck;
	}
	
	@Override
	public String toString() 
	{
		char NL = Character.LINE_SEPARATOR;
		StringBuffer buff = new StringBuffer();
		try
		{
			Field[] fields = getClass().getDeclaredFields();
	
			for(Field f : fields)
			{
				buff.append(f.getName() + " = " + f.get(this) + NL);
			}
		}
		catch(Exception e){}
		return buff.toString();
	}
	public ETDWorkInformation() {
		super();
		this.workId = -1;
		this.workName = "";
		this.predId = -1;
		this.predName = null;
		this.predRZD = "";
		this.dorId = -1;
		this.dorName = "";
		this.predCode = -1;
		this.availableForms = "";
		this.headpredId = -1;
		this.headpredName = null;
		this.expdoc = false;
		this.pdfcheck =false;
	}
	
}
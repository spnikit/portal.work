package ru.aisa.rgd.ws.documents;

public  class  SynchronizeDocResponseWrapper {
	int code;
	String description;
	boolean isUpdate;
	long etddocid;
	public int getCode() {
		return code;
	}
	public void setCode(int code) {
		this.code = code;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public boolean isUpdate() {
		return isUpdate;
	}
	public void setUpdate(boolean isUpdate) {
		this.isUpdate = isUpdate;
	}
	public long getEtddocid() {
		return etddocid;
	}
	public void setEtddocid(long etddocid) {
		this.etddocid = etddocid;
	}
	
}

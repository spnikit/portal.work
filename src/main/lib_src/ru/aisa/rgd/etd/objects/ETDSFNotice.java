package ru.aisa.rgd.etd.objects;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.sql.Date;
import java.sql.Time;

public class ETDSFNotice implements Serializable{

	private static final long serialVersionUID = 1L;
	
	
	String name;
	String statusname;
	Integer status;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getStatusname() {
		return statusname;
	}
	public void setStatusname(String statusname) {
		this.statusname = statusname;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	

}

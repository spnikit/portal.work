package ru.aisa.etdportal.controllers;
import java.util.HashMap;

import javax.sql.DataSource;

import org.springframework.jdbc.object.StoredProcedure;

public abstract class AbstractProc extends StoredProcedure {
	public AbstractProc(DataSource ds, String name) {
		super(ds, name);
		 
	}

	public HashMap<Integer, String> getErrors() {
		return errors;
	}

	public void setErrors(HashMap<Integer, String> errors) {
		this.errors = errors;
	}

	private HashMap <Integer, String> errors=new HashMap();
	
	 public String getErrorText(int code){
		 if (errors.containsKey(code)){
			 return errors.get(code);
		 }else return ("Unknown error"); 
		 
	 }
}

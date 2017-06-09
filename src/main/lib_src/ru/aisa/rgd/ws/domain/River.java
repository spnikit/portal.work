package ru.aisa.rgd.ws.domain;

import java.io.Serializable;
import java.lang.reflect.Field;

import org.apache.log4j.Logger;

public class River implements Serializable{
	
	private static final long serialVersionUID = 1L;
	protected final Logger	log= Logger.getLogger(getClass());
	
	Integer	id;
	Integer	objClass;
	Integer	code;
	String	name;
	
	public Integer getCode() {
		return code;
	}
	public void setCode(Integer code) {
		this.code = code;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Integer getObjClass() {
		return objClass;
	}
	public void setObjClass(Integer objClass) {
		this.objClass = objClass;
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
	

}

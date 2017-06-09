package ru.aisa.rgd.etd.objects;

import java.io.Serializable;
import java.lang.reflect.Field;

public class ETDTemplate implements Serializable{


	private static final long serialVersionUID = 1L;
	
	Integer id;
	String name;
	byte[] xml;
	Integer isHV;
	
	public ETDTemplate() {
		super();
		this.id = null;
		this.name = "";
		this.xml = new byte[1];
		this.isHV = 0;
		
	
	}
	public ETDTemplate(Integer id, String name, byte[] xml) {
		super();
		this.id = id;
		this.name = name;
		this.xml = xml;
		this.isHV = 0;
	}
	
	public ETDTemplate(Integer id, String name, byte[] xml, Integer isHV) {
		super();
		this.id = id;
		this.name = name;
		this.xml = xml;
		this.isHV = isHV;
		
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
	public byte[] getXml() {
		return xml;
	}
	public void setXml(byte[] xml) {
		this.xml = xml;
	}
	
	public Integer getIsHV() {
		return isHV;
	}

	public void setIsHV(Integer isHV) {
		this.isHV = isHV;
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

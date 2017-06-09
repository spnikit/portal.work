package ru.aisa.rgd.etd.objects;

import java.io.Serializable;
import java.lang.reflect.Field;

public class ETDFlowTypes implements Serializable{


	private static final long serialVersionUID = 1L;
	
	Integer id;
	String name;
	
	public ETDFlowTypes() {
		super();
		this.id = null;
		this.name = "";
		
	}
	public ETDFlowTypes(Integer id, String name) {
		super();
		this.id = id;
		this.name = name;
		
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
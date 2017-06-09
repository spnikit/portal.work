package ru.aisa.rgd.ws.domain;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import ru.aisa.rgd.ws.utility.Mappable;

public class ActivityKind implements Serializable, Mappable {

	private static final long serialVersionUID = 1L;
	protected final Logger	log	= Logger.getLogger(getClass());
	private Integer vdId;
	private Integer vdKod;
	private Integer pvdKod;
	private String name;
	private String sName;
	
	
	public void setVdId(Integer vdId) {
		this.vdId = vdId;
	}


	public Integer getVdId() {
		return vdId;
	}


	public void setVdKod(Integer vdKod) {
		this.vdKod = vdKod;
	}


	public Integer getVdKod() {
		return vdKod;
	}


	public void setPvdKod(Integer pvdKod) {
		this.pvdKod = pvdKod;
	}


	public Integer getPvdKod() {
		return pvdKod;
	}


	public void setName(String name) {
		this.name = name;
	}


	public String getName() {
		return name;
	}


	public void setSName(String sName) {
		this.sName = sName;
	}


	public String getSName() {
		return sName;
	}


	public Map<String, Object> map() {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("vdId", vdId);
		map.put("vdKod", vdKod);
		map.put("pvdKod", pvdKod);
		map.put("name", name);
		map.put("sName", sName);		
		return map;
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

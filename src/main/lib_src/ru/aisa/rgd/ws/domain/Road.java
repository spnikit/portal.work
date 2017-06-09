package ru.aisa.rgd.ws.domain;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import ru.aisa.rgd.ws.utility.Mappable;

public class Road implements Serializable, Mappable {

	private static final long serialVersionUID = -2349606495893318822L;
	
	/** letd.dor
	ID	SMALLINT	2	Нет
	ADMID	SMALLINT	2	Нет
	NAME	CHARACTER	60	Да
	SNAME	CHARACTER	12	Да
	 */
	private Integer id;
	private Integer admId;
	private String name;
	private String sName;
	
	
	public Integer getAdmId() {
		return admId;
	}

	public void setAdmId(Integer admId) {
		this.admId = admId;
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

	public String getSName() {
		return sName;
	}

	public void setSName(String name) {
		sName = name;
	}


	public Map<String, Object> map() 
	{
		Map<String, Object> map = new HashMap<String, Object>(4);
		map.put("id", id);
		map.put("admId", admId);
		map.put("name", name);
		map.put("sName", sName);
		return map;
	}

	@Override
	public String toString() 
	{
		char NL = Character.LINE_SEPARATOR;
		StringBuffer buff = new StringBuffer();
		Map map = map();
		
		for(String key : map().keySet())
		{
			buff.append(key + " = " + map.get(key) + NL);
		}
		return buff.toString();
	}

}

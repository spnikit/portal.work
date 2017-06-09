package ru.aisa.rgd.ws.domain;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import ru.aisa.rgd.ws.utility.Mappable;

public class Up implements Serializable, Mappable{

	private static final long serialVersionUID = 1L;
	
	/**
	  "ID" INTEGER , 
	  "MAGCODE" INTEGER 3, 
	  "CODE" INTEGER 5, 
	  "NAME" STRING , 
	  "STID1" INTEGER 6 , 
	  "STID2" INTEGER 6 , 
	 */
	
	/* Private Fields */
	
	private Integer id;
	private Integer magcode;
	private Integer code;
	private String name;
	private Integer stid1;
	private Integer stid2;
	
	/* JavaBeans Properties */
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getMagcode() {
		return magcode;
	}
	public void setMagcode(Integer magcode) {
		this.magcode = magcode;
	}
	public Integer getCode() {
		return code;
	}
	public void setCode(Integer code) {
		this.code = code;
	}
	public Integer getStId1() {
		return stid1;
	}
	public void setStId1(Integer stid1) {
		this.stid1 = stid1;
	}
	public Integer getStId2() {
		return stid2;
	}
	public void setStId2(Integer stid2) {
		this.stid2 = stid2;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	public Map<String, Object> map() {
		Map<String, Object> map = new HashMap<String, Object>(11);
		map.put("id", id);
		map.put("magcode", magcode);
		map.put("code", code);
		map.put("stid1", stid1);
		map.put("stid2", stid2);
		map.put("name", name);
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

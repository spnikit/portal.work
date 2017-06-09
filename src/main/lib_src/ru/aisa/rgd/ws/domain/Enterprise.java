package ru.aisa.rgd.ws.domain;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import ru.aisa.rgd.ws.utility.Mappable;

public class Enterprise implements Serializable, Mappable{

	private static final long serialVersionUID = 1L;
	
	public static final int
		TYPE_STATION = 0,
		TYPE_REPAIR = 1,
		TYPE_DEPO = 2,
		TYPE_DISTANCE = 2;
	
	/**
	  "ID" INTEGER NOT NULL , 
	  "TYPE" SMALLINT WITH DEFAULT  , 
	  "DORID" SMALLINT , 
	  "KOD" INTEGER , 
	  "VNAME" CHAR(120) , 
	  "NAME" CHAR(120) , 
	  "ONAME" CHAR(120) ,
	  "URL" VARCHAR(1024) WITH DEFAULT  , 
	  "ISSELF" CHAR(1) WITH DEFAULT 'Y' , 
	  "STPRIM" INTEGER , 
	  "ZONECNT" INTEGER WITH DEFAULT
	 */
	
	/* Private Fields */
	
	private Integer id;
	private Integer type;
	private Integer dorId;
	private Integer code;
	private Integer stpRim;
	private Integer zoneCnt;
	private String vName;
	private	String name;
	private String oName;
	private String url;
	private String isSelf;
	private String stName;
	private Integer stKod;
	
	/* JavaBeans Properties */
	
	public Integer getCode() {
		return code;
	}
	public void setCode(Integer code) {
		this.code = code;
	}
	public Integer getDorId() {
		return dorId;
	}
	public void setDorId(Integer dorId) {
		this.dorId = dorId;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getIsSelf() {
		return isSelf;
	}
	public void setIsSelf(String isSelf) {
		this.isSelf = isSelf;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Integer getStpRim() {
		return stpRim;
	}
	public void setStpRim(Integer stpRim) {
		this.stpRim = stpRim;
	}
	public Integer getType() {
		return type;
	}
	public void setType(Integer type) {
		this.type = type;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getVName() {
		return vName;
	}
	public void setVName(String name) {
		vName = name;
	}
	public String getOName() {
		return oName;
	}
	public void setOName(String name) {
		oName = name;
	}
	public Integer getZoneCnt() {
		return zoneCnt;
	}
	public void setZoneCnt(Integer zoneCnt) {
		this.zoneCnt = zoneCnt;
	}
	public String getStName() {
		return stName;
	}
	public void setStName(String name) {
		stName = name;
	}
	public Integer getStKod() {
		return stKod;
	}
	public void setStKod(Integer kod) {
		stKod = kod;
	}
	public Map<String, Object> map() {
		Map<String, Object> map = new HashMap<String, Object>(11);
		map.put("id", id);
		map.put("type", type);
		map.put("dorId", dorId);
		map.put("code", code);
		map.put("stpRim", stpRim);
		map.put("zoneCnt", zoneCnt);
		map.put("vName", vName);
		map.put("name", name);
		map.put("oName", oName);
		map.put("url", url);
		map.put("isSelf", isSelf);
		map.put("stName", stName);
		map.put("stKod", stKod);
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

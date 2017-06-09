package ru.aisa.rgd.ws.domain;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import ru.aisa.rgd.ws.utility.Mappable;

public class DicObjects implements Serializable, Mappable{

	private static final long serialVersionUID = 1L;
	
	/**
	  "OBJECT_ID" INTEGER , 
	  "CLASS_ID" INTEGER  , 
	  "OBJECT_KOD" INTEGER , 
	  "OBJECT_KODSTR" STRING , 
	  "OBJECT_VNAME" CHAR(120) , 
	  "OBJECT_NAME" CHAR(120) , 
	  "OBJECT_SNAME" CHAR(120) ,
	  "REFER" INTEGER  , 
	  "ACTIV" CHAR(1) , 
	 */
	
	/* Private Fields */
	
	private Integer object_id;
	private Integer class_id;
	private Integer object_kod;
	private String object_kodstr;
	private String object_vname;
	private String object_name;
	private String object_sname;
	private	Integer refer;
	private String activ;
	
	/* JavaBeans Properties */
	
	public Integer getObjectId() {
		return object_id;
	}
	public void setObjectId(Integer object_id) {
		this.object_id = object_id;
	}
	public Integer getClassId() {
		return class_id;
	}
	public void setClassId(Integer class_id) {
		this.class_id = class_id;
	}
	public Integer getObjectKod() {
		return object_kod;
	}
	public void setObjectKod(Integer object_kod) {
		this.object_kod = object_kod;
	}
	public String getObjectKodstr() {
		return object_kodstr;
	}
	public void setObjectKodstr(String object_kodstr) {
		this.object_kodstr = object_kodstr;
	}
	public String getVName() {
		return object_vname;
	}
	public void setVName(String object_vname) {
		this.object_vname = object_vname;
	}
	public String getName() {
		return object_name;
	}
	public void setName(String object_name) {
		this.object_name = object_name;
	}
	public String getSName() {
		return object_sname;
	}
	public void setSName(String object_sname) {
		this.object_sname = object_sname;
	}
	public Integer getRefer() {
		return refer;
	}
	public void setRefer(Integer refer) {
		this.refer = refer;
	}
	public String getActiv() {
		return activ;
	}
	public void setActiv(String activ) {
		this.activ = activ;
	}
	
	public Map<String, Object> map() {
		Map<String, Object> map = new HashMap<String, Object>(11);
		map.put("object_id", object_id);
		map.put("class_id", class_id);
		map.put("object_kod", object_kod);
		map.put("object_kodstr", object_kodstr);
		map.put("object_vname", object_vname);
		map.put("object_name", object_name);
		map.put("object_sname", object_sname);
		map.put("refer", refer);
		map.put("activ", activ);
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

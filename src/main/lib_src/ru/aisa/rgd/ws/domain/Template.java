package ru.aisa.rgd.ws.domain;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.Serializable;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import ru.aisa.rgd.ws.utility.Mappable;

public class Template implements Serializable, Mappable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 4591063435907322890L;
	/**
	ID	INTEGER	4	Нет
	NAME	CHARACTER	120	Да
	TEMPLATE	BLOB	1048576	Да
	PTYPE	SMALLINT	2	Да
	FLOWCNT	INTEGER	4	Да
	ACCCNT	INTEGER	4	Да
	*/
	private Integer id;
	private String name;
	private byte[] template;
	private Integer pType;
	private Integer flowCnt;
	private Integer accCnt;
	
	
	public Integer getAccCnt() {
		return accCnt;
	}


	public void setAccCnt(Integer accCnt) {
		this.accCnt = accCnt;
	}


	public Integer getFlowCnt() {
		return flowCnt;
	}


	public void setFlowCnt(Integer flowCnt) {
		this.flowCnt = flowCnt;
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


	public Integer getPType() {
		return pType;
	}


	public void setPType(Integer type) {
		pType = type;
	}
	
	
	public byte[] getTemplate() {
		return template;
	}


	public void setTemplate(byte[] template) {
		this.template = template;
	}


	public InputStream getTemplateAsStream() throws SQLException
	{
		return new ByteArrayInputStream(template);
	}


	


	public Map<String, Object> map() 
	{
		Map<String, Object> map = new HashMap<String, Object>(6);
		map.put("id", id);
		map.put("name", name);
		map.put("template", template);
		map.put("pType", pType);
		map.put("flowCnt", flowCnt);
		map.put("accCnt", accCnt);
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

package ru.aisa.rgd.ws.domain;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import ru.aisa.rgd.ws.utility.Mappable;

public class Signature implements Serializable, Mappable {

	private static final long serialVersionUID = 1L;
	/**
	*DOCID	INTEGER	4	Да
	*"ORDER"	INTEGER	4	Да
	*WRKID	INTEGER	4	Да
	*DT	TIMESTAMP	10	Да
	*PERSID	INTEGER	4	Да
	*PARENT	INTEGER	4	Да
	*EXP	VARCHAR	2048	Да
	**/
	private Long docId;
	private Integer order;
	private Integer wrkId;
	private Date date;
	private Integer persId;
	private Integer parent;
	private String exp;
	private String fio; 

	
	public String getFio() {
		return fio;
	}
	public void setFio(String fio) {
		this.fio = fio;
	}
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	public Long getDocId() {
		return docId;
	}
	public void setDocId(Long docId) {
		this.docId = docId;
	}
	public String getExp() {
		return exp;
	}
	public void setExp(String exp) {
		this.exp = exp;
	}
	public Integer getOrder() {
		return order;
	}
	public void setOrder(Integer order) {
		this.order = order;
	}
	public Integer getParent() {
		return parent;
	}
	public void setParent(Integer parent) {
		this.parent = parent;
	}
	public Integer getPersId() {
		return persId;
	}
	public void setPersId(Integer persId) {
		this.persId = persId;
	}
	public Integer getWrkId() {
		return wrkId;
	}
	public void setWrkId(Integer wrkId) {
		this.wrkId = wrkId;
	}
	public Map<String, Object> map() {
		Map<String, Object> map = new HashMap<String, Object>(8);
		SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy HH:mm");
		map.put("docId", docId);
		map.put("order", order);
		map.put("wrkId", wrkId);
		map.put("date", format.format(date));
		map.put("persId", persId);
		map.put("parent", parent);
		map.put("exp", exp);
		map.put("fio", fio);
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

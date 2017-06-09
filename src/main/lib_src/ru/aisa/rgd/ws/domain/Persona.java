package ru.aisa.rgd.ws.domain;

import java.io.Serializable;
import java.sql.Blob;
import java.util.HashMap;
import java.util.Map;

import ru.aisa.rgd.ws.utility.Mappable;

public class Persona implements Serializable, Mappable {
	
	private static final long serialVersionUID = -2274479846350525502L;
	
	/**
	ID	INTEGER	4	Нет
	DEPID	INTEGER	4	Нет
	WRKNOW	SMALLINT	2	Да
	FNAME	CHARACTER	80	Да
	MNAME	CHARACTER	80	Да
	LNAME	CHARACTER	80	Да
	PUBLICKEY	BLOB	102400	Да
	CERTSERIAL	VARCHAR	30	Да
	*/
	private Integer id;
	private Integer depId;
	private Integer wrkNow;
	private String fName;
	private String mName;
	private String lName;
	private Blob publicKey;
	private String  certSerial;
	private String  Duty;

	public Map<String, Object> map() 
	{
		Map<String, Object> map = new HashMap<String, Object>(8);
		map.put("id", id);
		map.put("depId", depId);
		map.put("wrkNow", wrkNow);
		map.put("fName", fName);
		map.put("mName", mName);
		map.put("lName", lName);
		map.put("publicKey", publicKey);
		map.put("certSerial", certSerial);
		map.put("Duty", Duty);
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
	
	public String getFio(){
		String str = new StringBuffer(getFName())
		.append(" ").append(getMName())
		.append(" ").append(getLName())
		.toString();
		return str;
	}

	public String getCertSerial() {
		return certSerial;
	}
	
	public void setCertSerial(String certSerial) {
		this.certSerial = certSerial;
	}
	
	public Integer getDepId() {
		return depId;
	}
	
	public void setDepId(Integer depId) {
		this.depId = depId;
	}
	
	public String getFName() {
		return fName;
	}
	
	public void setFName(String name) {
		fName = name;
	}
	
	public Integer getId() {
		return id;
	}
	
	public void setId(Integer id) {
		this.id = id;
	}
	
	public String getLName() {
		return lName;
	}
	
	public void setLName(String name) {
		lName = name;
	}
	
	public String getMName() {
		return mName;
	}
	
	public void setMName(String name) {
		mName = name;
	}
	
	public Blob getPublicKey() {
		return publicKey;
	}
	
	public void setPublicKey(Blob publicKey) {
		this.publicKey = publicKey;
	}
	
	public Integer getWrkNow() {
		return wrkNow;
	}
	
	public void setDuty(String Duty) {
		this.Duty = Duty;
	}
	public String getDuty() {
		return Duty;
	}
	
	public void setWrkNow(Integer wrkNow) {
		this.wrkNow = wrkNow;
	}
}

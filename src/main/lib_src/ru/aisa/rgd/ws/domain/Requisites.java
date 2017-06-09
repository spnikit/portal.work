package ru.aisa.rgd.ws.domain;

import java.io.Serializable;
import java.sql.Blob;
import java.util.HashMap;
import java.util.Map;

import ru.aisa.rgd.ws.utility.Mappable;

public class Requisites implements Serializable, Mappable {
	
	private String inn;
	private String kpp;
	private int okpo;
	public String getInn() {
		return inn;
	}
	public void setInn(String inn) {
		this.inn = inn;
	}
	public String getKpp() {
		return kpp;
	}
	public void setKpp(String kpp) {
		this.kpp = kpp;
	}
	public int getOkpo() {
		return okpo;
	}
	public void setOkpo(int okpo) {
		this.okpo = okpo;
	}
	@Override
	public Map<String, Object> map() 
	{
		Map<String, Object> map = new HashMap<String, Object>(3);
		map.put("kpp", kpp);
		map.put("inn", inn);
		map.put("okpo", okpo);
		return map;
	}
	
}

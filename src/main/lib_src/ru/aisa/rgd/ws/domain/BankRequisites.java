package ru.aisa.rgd.ws.domain;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import ru.aisa.rgd.ws.utility.Mappable;

public class BankRequisites implements Serializable, Mappable {
	
	private String bankname;
	private String bik;
	private String account;
	private String korraccount;
	
	public String getBankname() {
		return bankname;
	}

	public void setBankname(String bankname) {
		this.bankname = bankname;
	}

	public String getBik() {
		return bik;
	}

	public void setBik(String bik) {
		this.bik = bik;
	}

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public String getKorraccount() {
		return korraccount;
	}

	public void setKorraccount(String korraccount) {
		this.korraccount = korraccount;
	}

	@Override
	public Map<String, Object> map() {
		Map<String, Object> map = new HashMap<String, Object>(3);
		map.put("bankname", bankname);
		map.put("bik", bik);
		map.put("account", account);
		map.put("korraccount", korraccount);
		return map;
	}
	
}

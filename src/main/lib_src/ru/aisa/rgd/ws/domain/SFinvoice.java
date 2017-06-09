package ru.aisa.rgd.ws.domain;

import java.io.Serializable;
import java.sql.Blob;
import java.util.HashMap;
import java.util.Map;

import ru.aisa.rgd.ws.utility.Mappable;

public class SFinvoice implements Serializable, Mappable {
	
	private byte[] sf_d1;
	private byte[] sf_s1;
	private byte[] sf_d2;
	private byte[] sf_s2;
	private byte[] sf_d3;
	private byte[] sf_s3;
	private byte[] sf_d4;
	private byte[] sf_s4;
	private byte[] sf_d5;
	private byte[] sf_s5;
	private byte[] sf_d6;
	private byte[] sf_s6;
	private byte[] sf_d7;
	private byte[] sf_s7;
	private byte[] sf_d8;
	private byte[] sf_s8;
	private byte[] uv_d1;
	private byte[] uv_s1;
	private byte[] uv_d2;
	private byte[] uv_s2;
	private byte[] sf_fd;
	private byte[] sf_fds1;
	private String docdata;
	private String type;
	private String sf_vs1;
	private String sf_vs2;
	private String sf_vs3;
	private String sf_vs4;
	private String sf_vs5;
	private String sf_vs6;
	private String sf_vs7;
	private String sf_vs8;
	
	
	
	
	public String getSf_vs1() {
		return sf_vs1;
	}


	public void setSf_vs1(String sf_vs1) {
		this.sf_vs1 = sf_vs1;
	}


	public String getSf_vs2() {
		return sf_vs2;
	}


	public void setSf_vs2(String sf_vs2) {
		this.sf_vs2 = sf_vs2;
	}


	public String getSf_vs3() {
		return sf_vs3;
	}


	public void setSf_vs3(String sf_vs3) {
		this.sf_vs3 = sf_vs3;
	}


	public String getSf_vs4() {
		return sf_vs4;
	}


	public void setSf_vs4(String sf_vs4) {
		this.sf_vs4 = sf_vs4;
	}


	public String getSf_vs5() {
		return sf_vs5;
	}


	public void setSf_vs5(String sf_vs5) {
		this.sf_vs5 = sf_vs5;
	}


	public String getSf_vs6() {
		return sf_vs6;
	}


	public void setSf_vs6(String sf_vs6) {
		this.sf_vs6 = sf_vs6;
	}


	public String getSf_vs7() {
		return sf_vs7;
	}


	public void setSf_vs7(String sf_vs7) {
		this.sf_vs7 = sf_vs7;
	}


	public String getSf_vs8() {
		return sf_vs8;
	}


	public void setSf_vs8(String sf_vs8) {
		this.sf_vs8 = sf_vs8;
	}


	public byte[] getSf_d1() {
		return sf_d1;
	}


	public void setSf_d1(byte[] sf_d1) {
		this.sf_d1 = sf_d1;
	}


	public byte[] getSf_s1() {
		return sf_s1;
	}


	public void setSf_s1(byte[] sf_s1) {
		this.sf_s1 = sf_s1;
	}


	public byte[] getSf_d2() {
		return sf_d2;
	}


	public void setSf_d2(byte[] sf_d2) {
		this.sf_d2 = sf_d2;
	}


	public byte[] getSf_s2() {
		return sf_s2;
	}


	public void setSf_s2(byte[] sf_s2) {
		this.sf_s2 = sf_s2;
	}


	public byte[] getSf_d3() {
		return sf_d3;
	}


	public void setSf_d3(byte[] sf_d3) {
		this.sf_d3 = sf_d3;
	}


	public byte[] getSf_s3() {
		return sf_s3;
	}


	public void setSf_s3(byte[] sf_s3) {
		this.sf_s3 = sf_s3;
	}


	public byte[] getSf_d4() {
		return sf_d4;
	}


	public void setSf_d4(byte[] sf_d4) {
		this.sf_d4 = sf_d4;
	}


	public byte[] getSf_s4() {
		return sf_s4;
	}


	public void setSf_s4(byte[] sf_s4) {
		this.sf_s4 = sf_s4;
	}


	public byte[] getSf_d5() {
		return sf_d5;
	}


	public void setSf_d5(byte[] sf_d5) {
		this.sf_d5 = sf_d5;
	}


	public byte[] getSf_s5() {
		return sf_s5;
	}


	public void setSf_s5(byte[] sf_s5) {
		this.sf_s5 = sf_s5;
	}


	public byte[] getSf_d6() {
		return sf_d6;
	}


	public void setSf_d6(byte[] sf_d6) {
		this.sf_d6 = sf_d6;
	}


	public byte[] getSf_s6() {
		return sf_s6;
	}


	public void setSf_s6(byte[] sf_s6) {
		this.sf_s6 = sf_s6;
	}


	public byte[] getSf_d7() {
		return sf_d7;
	}


	public void setSf_d7(byte[] sf_d7) {
		this.sf_d7 = sf_d7;
	}


	public byte[] getSf_s7() {
		return sf_s7;
	}


	public void setSf_s7(byte[] sf_s7) {
		this.sf_s7 = sf_s7;
	}


	public byte[] getSf_d8() {
		return sf_d8;
	}


	public void setSf_d8(byte[] sf_d8) {
		this.sf_d8 = sf_d8;
	}


	public byte[] getSf_s8() {
		return sf_s8;
	}


	public void setSf_s8(byte[] sf_s8) {
		this.sf_s8 = sf_s8;
	}


	public byte[] getUv_d1() {
		return uv_d1;
	}


	public void setUv_d1(byte[] uv_d1) {
		this.uv_d1 = uv_d1;
	}


	public byte[] getUv_s1() {
		return uv_s1;
	}


	public void setUv_s1(byte[] uv_s1) {
		this.uv_s1 = uv_s1;
	}


	public byte[] getUv_d2() {
		return uv_d2;
	}


	public void setUv_d2(byte[] uv_d2) {
		this.uv_d2 = uv_d2;
	}


	public byte[] getUv_s2() {
		return uv_s2;
	}


	public void setUv_s2(byte[] uv_s2) {
		this.uv_s2 = uv_s2;
	}


	public byte[] getSf_fd() {
		return sf_fd;
	}


	public void setSf_fd(byte[] sf_fd) {
		this.sf_fd = sf_fd;
	}


	public byte[] getSf_fds1() {
		return sf_fds1;
	}


	public void setSf_fds1(byte[] sf_fds1) {
		this.sf_fds1 = sf_fds1;
	}


	public String getDocdata() {
		return docdata;
	}


	public void setDocdata(String docdata) {
		this.docdata = docdata;
	}
	public String getType() {
		return type;
	}


	public void setType(String type) {
		this.type = type;
	}


	@Override
	public Map<String, Object> map() 
	{
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("sf_d1", sf_d1);
		map.put("sf_s1", sf_s1);
		map.put("sf_d2", sf_d2);
		map.put("sf_s2", sf_s2);
		map.put("sf_d3", sf_d3);
		map.put("sf_s3", sf_s3);
		map.put("sf_d4", sf_d4);
		map.put("sf_s4", sf_s4);
		map.put("sf_d5", sf_d5);
		map.put("sf_s5", sf_s5);
		map.put("sf_d6", sf_d6);
		map.put("sf_s6", sf_s6);
		map.put("sf_d7", sf_d7);
		map.put("sf_s7", sf_s7);
		map.put("sf_d8", sf_d8);
		map.put("sf_s8", sf_s8);
		map.put("sf_d8", sf_d8);
		map.put("uv_d1", uv_d1);
		map.put("uv_s1", uv_s1);
		map.put("uv_d2", uv_d2);
		map.put("uv_s2", uv_s2);
		map.put("sf_fd", sf_fd);
		map.put("sf_fds1", sf_fds1);
		map.put("docdata", docdata);
		map.put("type", type);
		
		return map;
	}
	
}

package ru.aisa.rgd.etd.objects;

import java.io.Serializable;

public class AcceptDoc implements Serializable{
	private static final long  serialVersionUID = 1L;
	
	private long docid;
	private String doctype;
	private String sign;
	private String vis;
	private String gr;
	private String dropid;
	private int code;
	public AcceptDoc() {
		
	}
	public void setDropId(String dropid)
	{
		this.dropid = dropid;
	}
	public String getDropId()
	{
		return this.dropid;
	}
	public void setCode(int code)
	{
		this.code = code;
	}
	public int getCode()
	{
		return this.code;
	}
	public long getDocid() {
		return docid;
	}
	public void setDocid(long docid) {
		this.docid = docid;
	}
	public void setSign(String sign)
	{
		this.sign = sign;
	}
	public String getSign()
	{
		return sign;
	}
	
	public String getVis() {
		return vis;
	}
	public void setVis(String vis) {
		this.vis = vis;
	}
	public String getDoctype() {
		return doctype;
	}
	public void setDoctype(String doctype) {
		this.doctype = doctype;
	}
	public String getGr() {
		return gr;
	}
	public void setGr(String gr) {
		this.gr = gr;
	}
	
			

}

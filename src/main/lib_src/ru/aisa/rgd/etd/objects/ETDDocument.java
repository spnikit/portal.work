package ru.aisa.rgd.etd.objects;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.sql.Date;
import java.sql.Time;

public class ETDDocument implements Serializable{

	private static final long serialVersionUID = 1L;
	
	Long id;
	//Название формы
	String name;
	String number;
	Date createDate;
	Time createTime;
	String lastSigner;
	Date lastDate;
	Time lastTime;
	Integer cDel;
	Integer cCreateSourse;
	String data;
	String creator;
	String idpak;
	Integer sfSign;
	String dognum;
	String reqdate;
	String vagnum;
	Integer stat;
	String di;
	String rem_pred;
	String content;
	int count;
	int signed;
	String droptxt;
	int visible;
	String price;
	String otcname;
	String otctype;
	String sftype;
	int color;
	Long etdid;
	String numberFpu;
	String numberSf;
	
	public int getColor() {
		return color;
	}
	public void setColor(int color) {
		this.color = color;
	}
	public int getVisible() {
		return visible;
	}
	public void setVisible(int visible) {
		this.visible = visible;
	}
	public String getReqnum() {
		return reqnum;
	}
	public void setReqnum(String reqnum) {
		this.reqnum = reqnum;
	}
	String reqnum;
	String servicetype;
	
	public String getDroptxt() {
		return droptxt;
	}
	public void setDroptxt(String droptxt) {
		this.droptxt = droptxt;
	}
	boolean drop = false;
	
	
	public boolean isDrop() {
		return drop;
	}
	public void setDrop(boolean drop) {
		this.drop = drop;
	}
	public int getSigned() {
		return signed;
	}
	public void setSigned(int signed) {
		this.signed = signed;
	}
	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getDi() {
		return di;
	}
	public void setDi(String di) {
		this.di = di;
	}
	public String getRem_pred() {
		return rem_pred;
	}
	public void setRem_pred(String rem_pred) {
		this.rem_pred = rem_pred;
	}
	public Integer getStat() {
		return stat;
	}
	public void setStat(Integer stat) {
		this.stat = stat;
	}
	public String getDognum() {
		return dognum;
	}
	public void setDognum(String dognum) {
		this.dognum = dognum;
	}
	public String getIdPak() {
		return idpak;
	}
	public void setIdPak(String idpak) {
	    
	    this.idpak = idpak;
	}
	
	
	
	public Integer getSfSign() {
		return sfSign;
	}
	public void setSfSign(Integer sfSign) {
		this.sfSign = sfSign;
	}

	public String getCreator() {
		return creator;
	}
	public void setCreator(String creator) {
		this.creator = creator;
	}
	public String getData() {
		return data;
	}
	public void setData(String data) {
		this.data = data;
	}
	public Integer getCDel() {
		return cDel;
	}
	public void setCDel(Integer del) {
		cDel = del;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getLastSigner() {
		return lastSigner;
	}
	public void setLastSigner(String lastSigner) {
		this.lastSigner = lastSigner;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getNumber() {
		return number;
	}
	public void setNumber(String number) {
		this.number = number;
	}
	
	
	
	public Date getCreateDate() {
		return createDate;
	}
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	public Time getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Time createTime) {
		this.createTime = createTime;
	}
	public Date getLastDate() {
		return lastDate;
	}
	public void setLastDate(Date lastDate) {
		this.lastDate = lastDate;
	}
	public Time getLastTime() {
		return lastTime;
	}
	public void setLastTime(Time lastTime) {
		this.lastTime = lastTime;
	}
	public Integer getCCreateSourse() {
		return cCreateSourse;
	}
	public void setCCreateSourse(Integer createSourse) {
		cCreateSourse = createSourse;
	}
	public String getVagnum() {
		return vagnum;
	}
	public void setVagnum(String vagnum) {
		this.vagnum = vagnum;
	}
	
	public String getReqdate() {
		return reqdate;
	}
	public void setReqdate(String reqdate) {
		this.reqdate = reqdate;
	}

	
	public String getServicetype() {
		return servicetype;
	}
	public void setServicetype(String servicetype) {
		this.servicetype = servicetype;
	}
	
	public String getPrice() {
		return price;
	}
	public void setPrice(String price) {
		this.price = price;
	}
	public String getOtcname() {
		return otcname;
	}
	public void setOtcname(String otcname) {
		this.otcname = otcname;
	}
	
	public String getOtctype() {
		return otctype;
	}
	public void setOtctype(String otctype) {
		this.otctype = otctype;
	}
	
	
	
	public String getSftype() {
		return sftype;
	}
	public void setSftype(String sftype) {
		this.sftype = sftype;
	}
	
	
	public Long getEtdid() {
		return etdid;
	}
	public void setEtdid(Long etdid) {
		this.etdid = etdid;
	}	
	public String getNumberFpu() {
		return numberFpu;
	}
	public void setNumberFpu(String numberFpu) {
		this.numberFpu = numberFpu;
	}
	public String getNumberSf() {
		return numberSf;
	}
	public void setNumberSf(String numberSf) {
		this.numberSf = numberSf;
	}
@Override
public String toString() 
	{
		char NL = Character.LINE_SEPARATOR;
		StringBuffer buff = new StringBuffer();
		try
		{
			Field[] fields = getClass().getDeclaredFields();
	
			for(Field f : fields)
			{
				buff.append(f.getName() + " = " + f.get(this) + NL);
			}
		}
		catch(Exception e){}
		return buff.toString();
	}
	


}

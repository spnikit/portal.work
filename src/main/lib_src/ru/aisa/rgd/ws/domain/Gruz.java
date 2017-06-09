package ru.aisa.rgd.ws.domain;

import java.io.Serializable;
import java.lang.reflect.Field;

import org.apache.log4j.Logger;

public class Gruz implements Serializable {

	private static final long serialVersionUID = 1L;
	protected final Logger	log	= Logger.getLogger(getClass());
	
	long gruz_etsng2_id;
	String kod;
	String vname;
	String	sname;
	char	pr_alf;
	long kod_int;
	Integer tar_class;
	
	
	public long getGruzEtsng2Id() {
		return gruz_etsng2_id;
	}
	public void setGruzEtsng2Id(long code) {
		this.gruz_etsng2_id = code;
	}
	public String getKod() {
		return kod;
	}
	public void setKod(String kod) {
		this.kod = kod;
	}
	public String getVname() {
		return vname;
	}
	public void setVname(String vname) {
		this.vname = vname;
	}
	public String getSname() {
		return sname;
	}
	public void setSname(String sname) {
		this.sname = sname;
	}
	public char getPrAlf() {
		return pr_alf;
	}
	public void setPrAlf(char pr_alf) {
		this.pr_alf = pr_alf;
	}
	public long getKodInt() {
		return kod_int;
	}
	public void setKodInt(long kod_int) {
		this.kod_int = kod_int;
	}	
	public Integer getTarClass() {
		return tar_class;
	}
	public void setTarClass(Integer tar_class) {
		this.tar_class = tar_class;
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

package ru.aisa.rgd.ws.domain;

import java.io.Serializable;
import java.lang.reflect.Field;

import org.apache.log4j.Logger;

public class Vagsob implements Serializable {

	private static final long serialVersionUID = 1L;
	protected final Logger	log	= Logger.getLogger(getClass());
	
	long vag_sob_id;
	long loc_kod; 
	long okpo_kod;
	String	sname;
	String	reg_nom;
	Integer adm_kod;
	String activ;
	
	
	public long getVagSobId() {
		return vag_sob_id;
	}
	public void setVagSobId(long sob_id) {
		this.vag_sob_id = sob_id;
	}
	public long getLocKod() {
		return loc_kod;
	}
	public void setLocKod(long loc_kod) {
		this.loc_kod = loc_kod;
	}
	public long getOkpoKod() {
		return okpo_kod;
	}
	public void setOkpoKod(long okpo_kod) {
		this.okpo_kod = okpo_kod;
	}
	public String getSname() {
		return sname;
	}
	public void setSname(String sname) {
		this.sname = sname;
	}
	public String getRegNom() {
		return reg_nom;
	}
	public void setRegNom(String reg_nom) {
		this.reg_nom = reg_nom;
	}
	public Integer getAdmKod() {
		return adm_kod;
	}
	public void setAdmKod(Integer adm_kod) {
		this.adm_kod = adm_kod;
	}	
	public String getActiv() {
		return activ;
	}
	public void setActiv(String activ) {
		this.activ = activ;
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

package ru.aisa.rgd.ws.domain;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import ru.aisa.rgd.ws.utility.Mappable;

public class PeregMs implements Serializable, Mappable{

	private static final long serialVersionUID = 1L;
	
	/**
	  "PEREG_MS_ID" INTEGER , 
	  "UP_ID" INTEGER , 
	  "STAN1_ID" INTEGER , 
	  "STAN2_ID" INTEGER , 
	  "EXPL" DOUBLE , 
	  "CHET" INTEGER(1) , 
	  "ACTIV" CHAR(1) ,
	 */
	
	/* Private Fields */
	
	private Integer pereg_ms_id;
	private Integer up_id;
	private Integer stan1_id;
	private Integer stan2_id;
	private double expl;
	private Integer chet;
	private String activ;
	
	/* JavaBeans Properties */
	
	public Integer getPeregMsId() {
		return pereg_ms_id;
	}
	public void setPeregMsId(Integer pereg_ms_id) {
		this.pereg_ms_id = pereg_ms_id;
	}
	public Integer getUpId() {
		return up_id;
	}
	public void setUpId(Integer up_id) {
		this.up_id = up_id;
	}
	public Integer getStan1Id() {
		return stan1_id;
	}
	public void setStan1Id(Integer stan1_id) {
		this.stan1_id = stan1_id;
	}
	public Integer getStan2Id() {
		return stan2_id;
	}
	public void setStan2Id(Integer stan2_id) {
		this.stan2_id = stan2_id;
	}
	public double getExpl() {
		return expl;
	}
	public void setExpl(double expl) {
		this.expl = expl;
	}
	public Integer getChet() {
		return chet;
	}
	public void setChet(Integer chet) {
		this.chet = chet;
	}
	public String getActiv() {
		return activ;
	}
	public void setActiv(String activ) {
		this.activ = activ;
	}
	
	public Map<String, Object> map() {
		Map<String, Object> map = new HashMap<String, Object>(11);
		map.put("pereg_ms_id", pereg_ms_id);
		map.put("up_id", up_id);
		map.put("stan1_id", stan1_id);
		map.put("stan2_id", stan2_id);
		map.put("expl", expl);
		map.put("chet", chet);
		map.put("activ", activ);
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

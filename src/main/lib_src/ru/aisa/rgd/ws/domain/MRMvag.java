package ru.aisa.rgd.ws.domain;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import ru.aisa.rgd.ws.utility.Mappable;

public class MRMvag implements Serializable, Mappable
	{
	public static final String lastBulidDate = "26.06.2013 16:10:14";
	private static final long		serialVersionUID	= 1L;
	
	protected final Logger	log						= Logger.getLogger(getClass());
	
	private Integer							id_ses;
	private String							id_dev;
	private String							fio;
	private String							tabel_num;
	private String							card_num;
	private Integer							persid;
	private Integer							cnsi_dor;
	private Integer							cnsi_pred;
	private java.sql.Timestamp	opendate;
	private java.sql.Timestamp	closedate;
	
	public Integer getIdSes()
		{
		return id_ses;
		}
	
	public void setIdSes(Integer id_ses)
		{
		this.id_ses = id_ses;
		}
	
	public String getIdDev()
		{
		return id_dev;
		}
	
	public void setIdDev(String id_dev)
		{
		this.id_dev = id_dev;
		}
	
	public String getFio()
		{
		return fio;
		}
	
	public void setFio(String fio)
		{
		this.fio = fio;
		}
	
	public String getTabelNum()
		{
		return tabel_num;
		}
	
	public void setTabelNum(String tabel_num)
		{
		this.tabel_num = tabel_num;
		}
	
	public String getCardNum()
		{
		return card_num;
		}
	
	public void setCardNum(String card_num)
		{
		this.card_num = card_num;
		}
	
	public Integer getPersId()
		{
		return persid;
		}
	
	public void setPersId(Integer persid)
		{
		this.persid = persid;
		}
	
	public Integer getCnsiDor()
		{
		return cnsi_dor;
		}
	
	public void setCnsiDor(Integer cnsi_dor)
		{
		this.cnsi_dor = cnsi_dor;
		}
	
	public Integer getCnsiPred()
		{
		return cnsi_pred;
		}
	
	public void setCnsiPred(Integer cnsi_pred)
		{
		this.cnsi_pred = cnsi_pred;
		}
	
	public java.sql.Timestamp getOpendate()
		{
		return opendate;
		}
	
	public void setOpendate(java.sql.Timestamp opendate)
		{
		this.opendate = opendate;
		}
	
	public java.sql.Timestamp getClosedate()
		{
		return closedate;
		}
	
	public void setClosedate(java.sql.Timestamp closedate)
		{
		this.closedate = closedate;
		}
	
	public Map<String, Object> map()
		{
		Map<String, Object> map = new HashMap<String, Object>(7);
		map.put("id_ses", getIdSes());
		map.put("id_dev", getIdDev());
		map.put("fio", getFio());
		map.put("tabel_num", getTabelNum());
		map.put("card_num", getCardNum());
		map.put("persid", getPersId());
		map.put("cnsi_dor", getCnsiDor());
		map.put("cnsi_pred", getCnsiPred());
		map.put("opendate", getOpendate());
		map.put("closedate", getClosedate());
		// map.put("crtime", new java.sql.Time(getCrtime()+getCrdate().getTime()));
		return map;
		}
	
	}

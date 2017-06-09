package ru.aisa.rgd.ws.domain;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.ws.transport.context.TransportContext;
import org.springframework.ws.transport.context.TransportContextHolder;
import org.springframework.ws.transport.http.HttpServletConnection;

import ru.aisa.rgd.ws.utility.DataBinder;
import ru.aisa.rgd.ws.utility.Mappable;
import sheff.rjd.utils.IpTaker;

public class Document implements Serializable, Mappable{
	
	private static final long serialVersionUID = 1L;
	
	/**
	  "ID" INTEGER NOT NULL , 
	  "PREDID" INTEGER NOT NULL , 
	  "TYPEID" INTEGER NOT NULL , 
	  "NO" CHAR(40) , 
	  "CRDATE" DATE , 
	  "CRTIME" TIME , 
	  "BLDOC" BLOB(1048576) LOGGED NOT COMPACT , 
	  "NWRKID" INTEGER , 
	  "LWRKID" INTEGER , 
	  "LDATE" DATE , 
	  "LTIME" TIME , 
	  "LPERSID" INTEGER , 
	  "INUSEID" INTEGER , 
	  "DROPTIME" TIMESTAMP , 
	  "DROPTXT" VARCHAR(1000) , 
	  "DROPID" INTEGER , 
	  "DOCDATA" XML , 
	  "SIGNLVL" INTEGER )   
	 */
	
	

	//	Loginning
	protected final Logger	log= Logger.getLogger(getClass());
	
	/* Private Fields */
	
	private Long id;
	private Integer predId;
	private Integer signLvl;
	private String type;
	private String no;
	private String docData;
	private byte [] blDoc;
	private Long dropTime;
	private String dropTxt;
	private java.sql.Date crdate;
	private java.sql.Time crtime;

	
	
	/* JavaBeans Properties */
	public byte[] getBlDoc() {
		return blDoc;
	}
	public void setBlDoc(byte[] blDoc){
		this.blDoc = blDoc;
	}
	public String getDocData() {
		return docData;
	}
	public void setDocData(String docData) {
		this.docData = docData;
	}
	public java.sql.Time getCrtime() {
		return crtime;
	}
	public void setCrtime(java.sql.Time crtime) {
		this.crtime = crtime;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getNo() {
		return no;
	}
	public void setNo(String no) {
		this.no = no;
	}
	public Integer getPredId() {
		return predId;
	}
	public void setPredId(Integer predId) {
		this.predId = predId;
	}
	public Integer getSignLvl() {
		return signLvl;
	}
	public void setSignLvl(Integer signLvl) {
		this.signLvl = signLvl;
	}
	
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public java.sql.Date getCrdate(){
		return crdate;
	}
	public void setCrdate(java.sql.Date crdate){
		this.crdate=crdate;
	}
	public String createUrl()
	{
		if (this.id != null)
		{
			TransportContext context = TransportContextHolder.getTransportContext();
			HttpServletConnection conn = (HttpServletConnection )context.getConnection();
			HttpServletRequest request = conn.getHttpServletRequest();
			return "http://"+IpTaker.getUrl()+request.getContextPath() +"/portal/online?id="+id.toString();
		}
		else
		{
			throw new IllegalStateException("You must specify the value of the field 'id' to call the method");
		}
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
	
	public Map<String, Object> map()
	{
		Map<String, Object> map = new HashMap<String, Object>(7);
		
		map.put("id", getId());
		map.put("no", getNo());
		map.put("type", getType());
		map.put("blDoc", getBlDoc());
		map.put("docData", getDocData());
		map.put("predId", getPredId());
		map.put("signLvl", getSignLvl());
		map.put("dropTime", getDropTime());
		map.put("dropTxt", getDropTxt());
		//map.put("crdate", getCrdate());
		//map.put("crtime", new java.sql.Time(getCrtime()+getCrdate().getTime()));
	
		return map;
	}
	public void setDropTime(Long dropTime) {
		this.dropTime = dropTime;
	}
	public Long getDropTime() {
		return dropTime;
	}
	public void setDropTxt(String dropTxt) {
		this.dropTxt = dropTxt;
	}
	public String getDropTxt() {
		return dropTxt;
	}
	
	
}

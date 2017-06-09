package ru.aisa.rgd.etd.objects;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;

public class ETDDocumentData {
	
	private Long id;
	private byte [] blDoc;
	private String dropTxt;
	private Integer dropId;
	private Integer	inUseId;
	private String dropName;
	private Integer wrkid;
	private int read;
	public int getVisible() {
		return visible;
	}
	public void setVisible(int visible) {
		this.visible = visible;
	}

	private int visible;
	private boolean isSigned;
	private boolean isNew;
	/*private Integer	PREDID;
	private Integer	TYPEID;
	private String	NO;
	private Date	CRDATE;
	private Time	CRTIME;
	private Integer	NWRKID;
	private Integer	LWRKID;
	private Date	LDATE;
	private Time	LTIME;
	private Integer	LPERSID;
	private Date	DROPTIME;
	private String	DOCDATA;
	private Integer	SIGNLVL;
	private Date	OPENTIME;
	*/
	
	public String getDropName() {
		return dropName;
	}
	public boolean isSigned() {
		return isSigned;
	}
	public void setSigned(boolean isSigned) {
		this.isSigned = isSigned;
	}
	
	public boolean isNew() {
		return isNew;
	}
	public void setNew(boolean isNew) {
		this.isNew = isNew;
	}
	public void setDropName(String dropName) {
		this.dropName = dropName;
	}
	public byte[] getBlDoc() {
		return blDoc;
	}
	public String getBlDocAsString() throws UnsupportedEncodingException {
		return  new String(blDoc,"UTF-8");
	}
	public void setBlDoc(byte[] blDoc) {
		this.blDoc = blDoc;
	}
	public Integer getDropId() {
		return dropId;
	}
	public void setDropId(Integer dropId) {
		this.dropId = dropId;
	}
	public String getDropTxt() {
		return dropTxt;
	}
	public void setDropTxt(String dropTxt) {
		this.dropTxt = dropTxt;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Integer getInUseId() {
		return inUseId;
	}
	public void setInUseId(Integer inUseId) {
		this.inUseId = inUseId;
	}
	
	public int getRead() {
		return read;
	}
	public void setRead(int read) {
		this.read = read;
	}
	
	public Integer getWrkid() {
		return wrkid;
	}
	public void setWrkid(Integer wrkid) {
		this.wrkid = wrkid;
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

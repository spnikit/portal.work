package ru.aisa.rgd.etd.objects;

import java.lang.reflect.Field;

import org.apache.log4j.Logger;

import ru.aisa.edt.Signature;

public class ETDSignature {
	protected  Logger	log	= Logger.getLogger(getClass());
	
	String username;
	Long timestamp;
	byte[] sign;
	String logonstatus;
	String certid;
	
	public String getCertid() {
		return certid;
	}

	public ETDSignature(Signature signature){
		username = signature.getUsername();
		timestamp = signature.getTimestamp();
		sign = signature.getSign();
		logonstatus = signature.getLogonstatus();
		certid = signature.getCertid();
	}
	
	public String getLogonstatus() {
		return logonstatus;
	}
	public void setLogonstatus(String logonstatus) {
		this.logonstatus = logonstatus;
	}
	public byte[] getSign() {
		return sign;
	}
	public void setSign(byte[] sign) {
		this.sign = sign;
	}
	public Long getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(Long timestamp) {
		this.timestamp = timestamp;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public void setCertid(String certid) {
		this.certid = certid;
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

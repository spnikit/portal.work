package com.aisa.crypto.ocsp;

public class OCSPServerState {
	private boolean broken = false;
	private boolean needtosendOK = true;
	private boolean needtosetTimer = true;
	private long lastbadquery = 0;
	private String ip = "";
	private int timeout = 5000;
	
	public OCSPServerState(String ip, int timeout){
		this.ip = ip;
		this.timeout = timeout;
	}
	
	
	
	public boolean isNeedtosetTimer() {
		return needtosetTimer;
	}

	public void setNeedtosetTimer(boolean needtosetTimer) {
		this.needtosetTimer = needtosetTimer;
	}

	public boolean isNeedtosendOK() {
		return needtosendOK;
	}
	public void setNeedtosendOK(boolean needtosendOK) {
		this.needtosendOK = needtosendOK;
	}
	public boolean isBroken() {
		return broken;
	}
	public void setBroken(boolean broken) {
		this.broken = broken;
	}
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
	public long getLastbadquery() {
		return lastbadquery;
	}
	public void setLastbadquery(long lastbadquery) {
		this.lastbadquery = lastbadquery;
	}
	public int getTimeout() {
		return timeout;
	}
	public void setTimeout(int timeout) {
		this.timeout = timeout;
	}
	
	

	
}

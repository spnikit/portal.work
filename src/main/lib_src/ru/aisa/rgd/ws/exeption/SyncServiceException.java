package ru.aisa.rgd.ws.exeption;



public class SyncServiceException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	private SyncServiceError error;
	private long etdid;
	
	public SyncServiceException(Throwable arg0, SyncServiceError error, long etdid) {
		super(arg0);
		this.error = error;
		this.etdid = etdid;
	}
	
	public SyncServiceException(String arg0, SyncServiceError error, long etdid) {
		super(arg0);
		this.error = error;
		this.etdid = etdid;
	}

	public SyncServiceError getError() {
		return error;
	}

	public void setError(SyncServiceError error) {
		this.error = error;
	}
	public long getEtdid() {
		return etdid;
	}

	public void setETdid(long etdid) {
		this.etdid = etdid;
	}
	
	
	public SyncServiceException(Throwable arg, int code, String message, long etdid) 
	{
		super(arg);
		this.error = new SyncServiceError(code, message, etdid);
		this.etdid = etdid;
	}
	
	

}

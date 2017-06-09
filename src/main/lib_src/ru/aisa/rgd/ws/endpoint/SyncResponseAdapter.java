package ru.aisa.rgd.ws.endpoint;

import ru.aisa.rgd.ws.exeption.SyncServiceError;

public class SyncResponseAdapter<T>{
	
	private boolean status;
	T response;
	private SyncServiceError error;
	private long etdid;
	public SyncResponseAdapter()
	{
		error = null;
		status = false;
		response = null;
		etdid = -1;
	}
	
	public SyncResponseAdapter(SyncServiceError e)
	{
		error = e;
	}

	public SyncResponseAdapter(boolean status, T response, SyncServiceError error, long etdid) {
		super();
		this.status = status;
		this.response = response;
		this.error = error;
		this.etdid = etdid;
	}

	public SyncServiceError getError() {
		return error;
	}

	public void setError(SyncServiceError error) {
		this.error = error;
	}

	public T getResponse() {
		return response;
	}

	public void setResponse(T response) {
		this.response = response;
	}

	public boolean isStatus() {
		return status;
	}

	public void setStatus(boolean status) {
		this.status = status;
	}

	public long getEtdid() {
		return etdid;
	}

	public void setEtdid(long etdid) {
		this.etdid = etdid;
	}
	
}

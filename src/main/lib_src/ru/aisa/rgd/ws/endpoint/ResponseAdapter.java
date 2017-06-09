package ru.aisa.rgd.ws.endpoint;

import ru.aisa.rgd.ws.exeption.ServiceError;

public class ResponseAdapter<T>{
	
	private boolean status;
	T response;
	private ServiceError error;
	
	public ResponseAdapter()
	{
		error = null;
		status = false;
		response = null;
	}
	
	public ResponseAdapter(ServiceError e)
	{
		error = e;
	}

	public ResponseAdapter(boolean status, T response, ServiceError error) {
		super();
		this.status = status;
		this.response = response;
		this.error = error;
	}

	public ServiceError getError() {
		return error;
	}

	public void setError(ServiceError error) {
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
}

package ru.aisa.rgd.ws.exeption;



public class ServiceException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	private ServiceError error;
	
	
	public ServiceException(Throwable arg0, ServiceError error) {
		super(arg0);
		this.error = error;
	}
	
	public ServiceException(String arg0, ServiceError error) {
		super(arg0);
		this.error = error;
	}

	public ServiceError getError() {
		return error;
	}

	public void setError(ServiceError error) {
		this.error = error;
	}
	
	public ServiceException(Throwable arg, int code, String message) 
	{
		super(arg);
		this.error = new ServiceError(code, message);
	}
	
	/*
	/**
	 * @deprecated Not used
	 
	public ServiceException(Throwable arg, int code, String message, Object... params) 
	{
		super(arg);
		Pattern p = Pattern.compile(ServiceMessage.PATTERN);
		Matcher m = p.matcher( message);
		StringBuffer sb = new StringBuffer();
		int length = params.length;
		int i = 0;
		while (m.find() && i < length) 
		{
		     m.appendReplacement(sb, String.valueOf(params[i++]));
		}
		m.appendTail(sb);
		this.error = new ServiceError(code, sb.toString());
	}*/

}

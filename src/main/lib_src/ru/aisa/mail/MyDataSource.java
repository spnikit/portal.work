package ru.aisa.mail;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.OutputStream;

public class MyDataSource implements javax.activation.DataSource {

	private byte[] data = new byte[0];
	private String contentType = "text/plain";
	private String name = null;

	public void setData(byte[] data) {
		this.data = data;
	}

	public void setContentType(String contentType) {
		this.contentType = contentType;
	}

	public void setName(String name) {
		this.name = name;
	}

	public InputStream getInputStream() {
		return new ByteArrayInputStream(data);
	}

	public OutputStream getOutputStream() {
		return null;
	}

	public String getContentType() {
		return contentType;
	}

	public String getName() {
		return name;
	}
}
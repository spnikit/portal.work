package ru.aisa.etdportal.contenthandler;

import ru.aisa.rgd.ws.documents.ETDForm;

public abstract class AbstractContentHandler implements ContentHandler{

	public  AbstractContentHandler(){
		super();
	}

	public abstract String Content(String formblob);
}

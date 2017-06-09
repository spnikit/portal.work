package ru.aisa.etdportal.vagonhandler;

import ru.aisa.rgd.ws.documents.ETDForm;

public abstract class AbstractVagnumHandler implements VagnumHandler{

	public  AbstractVagnumHandler(){
		super();
	}

	public abstract String[] Content(String formblob);
}

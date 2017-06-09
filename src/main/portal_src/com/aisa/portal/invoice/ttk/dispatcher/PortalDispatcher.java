package com.aisa.portal.invoice.ttk.dispatcher;

import ru.aisa.etdSignature.ETDSignatureCreateRequestDocument;
import ru.aisa.etdSignature.ETDSignatureCreateResponseDocument;
import sheff.rjd.dispatcher.Dispatcher;

public class PortalDispatcher extends Dispatcher{
String urlToSign;
	public String getUrlToSign() {
	return urlToSign;
}
public void setUrlToSign(String urlToSign) {
	this.urlToSign = urlToSign;
}

	public byte[] CounstructSignature(byte[] indata ){
		ETDSignatureCreateRequestDocument document=ETDSignatureCreateRequestDocument.Factory.newInstance();
		document.addNewETDSignatureCreateRequest().setData(indata);
		ETDSignatureCreateResponseDocument response=(ETDSignatureCreateResponseDocument)getWst().marshalSendAndReceive(urlToSign,document);
		if (response.getETDSignatureCreateResponse().getCode()==0)
		return response.getETDSignatureCreateResponse().getSgn();
		else{
			return null;
		}
	}
	
}

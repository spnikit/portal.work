package com.aisa.portal.invoice.notice.documentsProcessors;

import com.aisa.portal.invoice.ttk.SellerUvedConfirmationToTTK;

public class SellerUvedConfirmationToTTKAbstract extends
		AbstractProcessor {
	public SellerUvedConfirmationToTTK getObj() {
		return obj;
	}

	public void setObj(SellerUvedConfirmationToTTK obj) {
		this.obj = obj;
	}

	SellerUvedConfirmationToTTK obj;

	@Override
	public void process(String id, long docid, byte[] xmlFNS, byte[] sing)
			throws Exception {
		obj.processSellerUvedConfirmation(id, docid, xmlFNS, sing);

	}

}

package com.aisa.portal.invoice.notice.documentsProcessors;

import com.aisa.portal.invoice.ttk.BuyerUvedNoticeInvoiceConfirmationToTTK;

public class BuyerUvedNoticeInvoiceConfirmationToTTKAbstract extends
		AbstractProcessor {
	public BuyerUvedNoticeInvoiceConfirmationToTTK getObj() {
		return obj;
	}

	public void setObj(BuyerUvedNoticeInvoiceConfirmationToTTK obj) {
		this.obj = obj;
	}

	BuyerUvedNoticeInvoiceConfirmationToTTK obj;

	@Override
	public void process(String id, long docid, byte[] xmlFNS, byte[] sing)
			throws Exception {
		obj.processBuyerUvedNoticeInvoiceConfirmation(id, docid, xmlFNS, sing);

	}

}

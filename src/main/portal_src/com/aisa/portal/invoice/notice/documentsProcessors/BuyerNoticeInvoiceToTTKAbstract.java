package com.aisa.portal.invoice.notice.documentsProcessors;

import com.aisa.portal.invoice.ttk.BuyerNoticeInvoiceToTTK;

public class BuyerNoticeInvoiceToTTKAbstract extends AbstractProcessor {

	public BuyerNoticeInvoiceToTTK getObj() {
		return obj;
	}

	public void setObj(BuyerNoticeInvoiceToTTK obj) {
		this.obj = obj;
	}

	BuyerNoticeInvoiceToTTK obj;

	@Override
	public void process(String id, long docid, byte[] xmlFNS, byte[] sing)
			throws Exception {
		obj.processBuyerNoticeInvoice(id, docid, xmlFNS, sing);

	}

}

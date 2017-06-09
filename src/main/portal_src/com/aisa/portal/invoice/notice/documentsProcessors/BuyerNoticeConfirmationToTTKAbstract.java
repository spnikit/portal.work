package com.aisa.portal.invoice.notice.documentsProcessors;

import com.aisa.portal.invoice.ttk.BuyerNoticeConfirmationToTTK;

public class BuyerNoticeConfirmationToTTKAbstract extends AbstractProcessor {
	BuyerNoticeConfirmationToTTK  obj;
	public BuyerNoticeConfirmationToTTK getObj() {
		return obj;
	}
	public void setObj(BuyerNoticeConfirmationToTTK obj) {
		this.obj = obj;
	}
	@Override
	public void process(String id, long docid, byte[] xmlFNS,
			byte[] sing) throws Exception {
		obj.processBuyerNoticeConfirmation(id, docid, xmlFNS, sing);
		
	}
	 
}

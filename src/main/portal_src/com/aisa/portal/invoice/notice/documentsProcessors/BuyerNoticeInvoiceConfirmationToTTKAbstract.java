package com.aisa.portal.invoice.notice.documentsProcessors;

 
import com.aisa.portal.invoice.ttk.BuyerNoticeInvoiceConfirmationToTTK;

public class BuyerNoticeInvoiceConfirmationToTTKAbstract extends AbstractProcessor {
	public BuyerNoticeInvoiceConfirmationToTTK getObj() {
		return obj;
	}

	public void setObj(BuyerNoticeInvoiceConfirmationToTTK obj) {
		this.obj = obj;
	}

	BuyerNoticeInvoiceConfirmationToTTK  obj;
 
	@Override
	public void process(String id, long docid, byte[] xmlFNS,
			byte[] sing) throws Exception {
		obj.processBuyerNoticeInvoiceConfirmation(id, docid, xmlFNS, sing);
		
	}
	 
}

package com.aisa.portal.invoice.notice.documentsProcessors;

 
import com.aisa.portal.invoice.ttk.BuyerNoticeInvoiceConfirmationToTTK;
import com.aisa.portal.invoice.ttk.BuyerNoticeInvoiceToTTK;
import com.aisa.portal.invoice.ttk.SellerNoticeConfirmationToTTK;

public class SellerNoticeConfirmationToTTKAbstract extends AbstractProcessor {
 

	SellerNoticeConfirmationToTTK  obj;
 
 

	public SellerNoticeConfirmationToTTK getObj() {
		return obj;
	}



	public void setObj(SellerNoticeConfirmationToTTK obj) {
		this.obj = obj;
	}



	@Override
	public void process(String id, long docid, byte[] xmlFNS,
			byte[] sing) throws Exception {
		obj.processSellerNoticeConfirmation(id, docid, xmlFNS, sing);
		
	}
	 
}

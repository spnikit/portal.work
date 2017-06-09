package com.aisa.portal.invoice.notice.endpoints;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.oxm.Marshaller;
import org.springframework.ws.server.endpoint.AbstractMarshallingPayloadEndpoint;

import rzd8888.gvc.etd.was.etd.recievenotice.RecieveNoticeResponseDocument;
import rzd8888.gvc.etd.was.etd.recievenotice.RecieveNoticeResponseDocument.RecieveNoticeResponse;

@SuppressWarnings("deprecation")
public class EtdGetNoticeResponseEndpoint extends
		AbstractMarshallingPayloadEndpoint {
	private static Log log = LogFactory
			.getLog(EtdGetNoticeResponseEndpoint.class);
	public EtdGetNoticeResponseEndpoint(Marshaller marshaller) {
		super(marshaller);
	}

	protected Object invokeInternal(Object requestObject) throws Exception {

		RecieveNoticeResponseDocument noticedoc = (RecieveNoticeResponseDocument) requestObject;
		RecieveNoticeResponse notice = noticedoc.getRecieveNoticeResponse();
if (notice.getCode()==0)
	log.debug("ETD recieved invoice message for ETDID "+notice.getEtdid());

else log.error("ETD didn't recieve invoice message for ETDID "+notice.getEtdid());
		return null;
	}
}

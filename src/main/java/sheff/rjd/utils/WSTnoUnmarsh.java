package sheff.rjd.utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import javax.xml.transform.TransformerException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.log4j.Logger;
import org.springframework.oxm.Marshaller;
import org.springframework.ws.WebServiceMessage;
import org.springframework.ws.client.core.WebServiceMessageCallback;
import org.springframework.ws.client.core.WebServiceMessageExtractor;
import org.springframework.ws.client.core.WebServiceTemplate;
import org.springframework.ws.support.MarshallingUtils;

public class WSTnoUnmarsh extends WebServiceTemplate{

private static Logger	logger	= Logger.getLogger(WSTnoUnmarsh.class);
	
	public Object marshalSendAndReceive(String uri,
            final Object requestPayload,
            final WebServiceMessageCallback requestCallback) {
			return sendAndReceive(uri, new WebServiceMessageCallback() {

				public void doWithMessage(WebServiceMessage request) throws IOException, TransformerException {
				if (requestPayload != null) {
				Marshaller marshaller = getMarshaller();
				if (marshaller == null) {
				throw new IllegalStateException(
				    "No marshaller registered. Check configuration of WebServiceTemplate.");
				}
				if(logger.isDebugEnabled()){
					ByteArrayOutputStream bout = new ByteArrayOutputStream();
					request.writeTo(bout);
					logger.debug("Sending Message to ASU: "+bout.toString("UTF-8"));
				}
				MarshallingUtils.marshal(marshaller, requestPayload, request);
				if (requestCallback != null) {
				requestCallback.doWithMessage(request);
				}
				}
				}
				}, new WebServiceMessageExtractor() {
				
				public Object extractData(WebServiceMessage response) throws IOException {
					/*Unmarshaller unmarshaller = getUnmarshaller();
					if (unmarshaller == null) {
						throw new IllegalStateException(
						"No unmarshaller registered. Check configuration of WebServiceTemplate.");
					}
					return MarshallingUtils.unmarshal(unmarshaller, response);*/
					if(logger.isDebugEnabled()){
						ByteArrayOutputStream bout = new ByteArrayOutputStream();
						response.writeTo(bout);
						logger.debug("RESPONSE FROM ASU is "+bout.toString("UTF-8"));
					}
					return null;
				}
				});
				}

	
}

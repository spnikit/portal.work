package ru.aisa.rgd.etd.validating;

import javax.xml.transform.Source;

import org.springframework.ws.WebServiceMessage;

public class PayloadValidatingInterceptor extends AbstractFaultCreatingValidatingInterceptor {

	/** Returns the payload source of the given message. */
    protected Source getValidationRequestSource(WebServiceMessage request) {
  	  return request.getPayloadSource();
    }

    /** Returns the payload source of the given message. */
    protected Source getValidationResponseSource(WebServiceMessage response) {
        return response.getPayloadSource();
   }


}

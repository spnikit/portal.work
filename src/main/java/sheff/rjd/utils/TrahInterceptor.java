package sheff.rjd.utils;

import org.apache.commons.httpclient.methods.PostMethod;
import org.springframework.ws.client.WebServiceClientException;
import org.springframework.ws.client.support.interceptor.ClientInterceptor;
import org.springframework.ws.context.MessageContext;
import org.springframework.ws.transport.context.TransportContext;
import org.springframework.ws.transport.context.TransportContextHolder;
import org.springframework.ws.transport.http.CommonsHttpConnection;

public class TrahInterceptor implements ClientInterceptor{

	@Override
	public boolean handleRequest(MessageContext messageContext)
			throws WebServiceClientException {
//		System.out.println("111");
		TransportContext context = TransportContextHolder.getTransportContext();
	    CommonsHttpConnection connection = (CommonsHttpConnection) context.getConnection();
	    PostMethod postMethod = connection.getPostMethod();
//	    postMethod.addRequestHeader( "Authorization	Basic", "dnJrMV93YXJlY3NAMXZyay5yemQ6V2FSZUNTIA==" );
//	   System.out.println(postMethod);
	    return true;
	}

	@Override
	public boolean handleResponse(MessageContext messageContext)
			throws WebServiceClientException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean handleFault(MessageContext messageContext)
			throws WebServiceClientException {
		// TODO Auto-generated method stub
		return false;
	}

}

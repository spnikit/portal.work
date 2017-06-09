package ru.aisa.etdportal.contenthandler;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import ru.aisa.rgd.ws.documents.ETDForm;
import ru.aisa.rgd.ws.exeption.InternalException;
import ru.aisa.rgd.ws.exeption.ServiceException;
import ru.aisa.rgd.ws.utility.DataBinder;

public class VU22Handler extends AbstractContentHandler{
	private static Log log = LogFactory
		    .getLog(VU22Handler.class);
	@Override
	public String Content(String formblob) {
		
		StringBuffer sb = new StringBuffer() ;		
		try {
		
			ETDForm form = ETDForm.createFromArchive(formblob.getBytes("UTF-8"));
		
			DataBinder db = form.getBinder();
		
		sb.append(db.getValue("P_12"));
		sb.append(", ");
		sb.append(db.getValue("P_26"));
		
		
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
						e.printStackTrace();
		} catch (ServiceException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InternalException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return sb.toString();
	}

}

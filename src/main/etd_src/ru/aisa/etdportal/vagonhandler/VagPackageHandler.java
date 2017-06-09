package ru.aisa.etdportal.vagonhandler;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import ru.aisa.rgd.ws.documents.ETDForm;
import ru.aisa.rgd.ws.exeption.InternalException;
import ru.aisa.rgd.ws.exeption.ServiceException;
import ru.aisa.rgd.ws.utility.DataBinder;

public class VagPackageHandler extends AbstractVagnumHandler{
	private static Log log = LogFactory
		    .getLog(VagPackageHandler.class);
	@Override
	public String[] Content(String formblob) {
		
		StringBuffer sb = new StringBuffer() ;		
		try {
			ETDForm form = ETDForm.createFromArchive(formblob.getBytes("UTF-8"));
		DataBinder db = form.getBinder();
		sb.append(db.getValue("P_15"));
		sb.append("&&");
		if (db.getValue("P_22").length()>0)
		sb.append(db.getValue("P_22"));
		else sb.append("nain");
		//System.out.println(sb.toString());
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
		
		return sb.toString().split("&&");
		
	}

}

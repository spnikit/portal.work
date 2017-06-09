package ru.aisa.etdportal.contenthandler;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import ru.aisa.rgd.ws.documents.ETDForm;
import ru.aisa.rgd.ws.exeption.InternalException;
import ru.aisa.rgd.ws.exeption.ServiceException;
import ru.aisa.rgd.ws.utility.DataBinder;

public class CardDocHandler extends AbstractContentHandler{
	private static Log log = LogFactory
		    .getLog(CardDocHandler.class);
	@Override
	public String Content(String formblob) {
		
		StringBuffer sb = new StringBuffer() ;		
		try {
			ETDForm form = ETDForm.createFromArchive(formblob.getBytes("UTF-8"));
		DataBinder db = form.getBinder();
		
		String p_1 = db.getValue("P_1");	
		sb.append(p_1);
		List<String> aa = new ArrayList<String>();
		String[] vagnum = db.getValuesAsArray("P_5");
		
		int j=0;
			
		while(j<vagnum.length)
		{
		aa.add(vagnum[j]);
		j++;
		}
		
		int i=0;
		
		if (aa.size()>3){
		while (i<3)
		{
			sb.append(", ");
			sb.append(aa.get(i));
			i++;
		}
		sb.append("...");
		
		}
		
		else {
			while (i<aa.size())
			{
				sb.append(", ");
				sb.append(aa.get(i));
				i++;
			}
			
		}
		
		
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

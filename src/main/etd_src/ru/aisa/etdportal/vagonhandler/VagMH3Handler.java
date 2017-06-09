package ru.aisa.etdportal.vagonhandler;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import ru.aisa.rgd.ws.documents.ETDForm;
import ru.aisa.rgd.ws.exeption.ServiceException;
import ru.aisa.rgd.ws.utility.DataBinder;

public class VagMH3Handler extends AbstractVagnumHandler{
	private static Log log = LogFactory
		    .getLog(VagMH3Handler.class);
	@Override
	public String[] Content(String formblob) {
		
		StringBuffer sb = new StringBuffer() ;		
		try {
			ETDForm form = ETDForm.createFromArchive(formblob.getBytes("UTF-8"));
		DataBinder db = form.getBinder();
		List<String> aa = new ArrayList<String>();
		String[] vagnum = db.getValuesAsArray("P_16");
		
		int j=0;
		
		while(j<vagnum.length)
		{
		aa.add(vagnum[j]);
		j++;
		}
		
		int i=0;
		
		if (aa.size()>=3){
		while (i<2)
		{
			
			sb.append(aa.get(i));
			sb.append(",");
			i++;
		}
		sb.append(aa.get(i));
		
		if (aa.size()>3)
		sb.append("...");
		
		}
		
		else {
			while (i<(aa.size()-1))
			{
				sb.append(aa.get(i));
				sb.append(",");
				i++;
			}
			sb.append(aa.get(i));
		}
	
		
		
		sb.append("&&");
		sb.append("nain");	
		
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ServiceException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return sb.toString().split("&&");
		
	}

}

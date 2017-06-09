package sheff.rjd.utils;

import java.io.PrintWriter;
import java.io.StringWriter;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.log4j.Logger;

import com.aisa.crypto.tsp.TSPRequest;
import com.aisa.crypto.tsp.TSPoverHTTP;

public class TSPutils {
	private String[] url;
	private String[] timeout;
	private String enabled;
	private static Logger	log	= Logger.getLogger(TSPutils.class);
	
	
	public String[] getTimeout() {
		return timeout;
	}

	public void setTimeout(String[] timeout) {
		this.timeout = timeout;
	}

	public String getEnabled() {
		return enabled;
	}

	public void setEnabled(String enabled) {
		this.enabled = enabled;
	}

	
	public String[] getUrl() {
		return url;
	}

	public void setUrl(String[] url) {
		this.url = url;
	}

	public TSPutils(){
		
	}
	public byte[] getTSP(String value){
	   	if (!getEnabled().equals("yes")) return null;
		else{
		byte[] ok = null;
		for (int i = 0; i < getUrl().length; i++) {
			try{
				TSPoverHTTP sr = new TSPoverHTTP(url[i],timeout[i]);
			    TSPRequest tr = new TSPRequest("1.2.643.2.2.9",value.getBytes("UTF-8"),null);
			    ok = sr.getResp(tr);
			    log.warn("TSP   url : "+getUrl()[i] + " success");
				break;
			}
			catch (Exception e) {
				StringWriter outError = new StringWriter();
				   PrintWriter errorWriter = new PrintWriter( outError );
				   e.printStackTrace(errorWriter);
				   log.error("TSP   url :  "+getUrl()[i]+"\n"+outError.toString());
//				   SNMPSender.sendMessage("CA_ERROR", "CA_0001", "0001", "", "OCSP/TSP ip="+getUrl()[i]+" connection problem");
			}
		}
		return ok;
		}
	}

}

package sheff.rjd.utils;

import java.io.ByteArrayInputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.LinkedHashMap;

import org.apache.log4j.Logger;

import com.aisa.crypto.ocsp.OCSPcheck;

public class OCSPutils {
	
	private int truevalue;
	private static Logger	 log	= Logger.getLogger(OCSPutils.class);
	private X509Certificate rootCert;
	private LinkedHashMap <String,X509Certificate> rootCertMap =new LinkedHashMap<String, X509Certificate>();
	private LinkedHashMap <String,String> rootUrlMap =new LinkedHashMap<String, String>();
	protected CertificateFactory cf;
	private String enabled;
	private OCSPcheck ocsp;
    private int hashCarzd;
	
	public void init(){
		
	}
	public int getHashCarzd() {
		return hashCarzd;
	}
	public void setHashCarzd(int hashCarzd) {
		this.hashCarzd = hashCarzd;
	}
	
	public int getTruevalue() {
		return truevalue;
	}
	public void setTruevalue(int truevalue) {
		this.truevalue = truevalue;
	}
		
	
	
	public String getEnabled() {
		return enabled;
	}
	public void setEnabled(String enabled) {
		this.enabled = enabled;
	}
	//private int HashCArzd=118292249;
	
	public OCSPutils(String[] certfile,String[] urls, String[] timeout){
		try{
		System.out.println("ocsputils");
			
		cf = CertificateFactory.getInstance("X.509");
		ocsp = new OCSPcheck(urls,timeout);
		
		System.out.println(certfile.length);
		
		for (int i=0;i<certfile.length;i++){
			System.out.println(i);
			System.out.println(certfile[i]);
			rootCert = (X509Certificate)cf.generateCertificate(AppCtxGetterUtils.getApplicationContext().getResource("classpath:"+certfile[i]).getInputStream());
	
//			System.out.println(rootCert.getSubjectDN().getName());
			
			rootCertMap.put(rootCert.getSubjectDN().getName(),rootCert);
			
//			rootUrlMap.put(rootCert.getSubjectDN().getName(), urls[i]);
			
		}
		
		}
		catch (Exception e) {
			StringWriter outError = new StringWriter();
			   PrintWriter errorWriter = new PrintWriter( outError );
			   e.printStackTrace(errorWriter);
			   log.error(outError.toString());
			}
	}
	
	public boolean checkOCSP( byte[] clientcer){
//		System.out.println("checkOCSP");
		
//		return true;
		
		if (!getEnabled().equals("yes")) return true;
		else {
		try{
		
		 X509Certificate interCert = (X509Certificate)cf.generateCertificate(new ByteArrayInputStream(clientcer));	
		 
		 X509Certificate localRootCert=rootCertMap.get(interCert.getIssuerDN().getName());
		
		 String url = (rootUrlMap.get(interCert.getIssuerDN().getName()));
//		 System.out.println(localRootCert.getIssuerDN().getName());
//		 System.out.println(interCert.getSubjectDN().getName());
		 
		 
			 if (localRootCert==null){
				 if (interCert.getSubjectDN().getName().contains("OCRV_ADM_USR")){
//					 return ocsp.quickCheck(interCert, localRootCert, "1.2.643.2.2.9", null);
				     	 return true;
				 } else  {
					     log.warn("OCSP : WARN, CRTID : "+interCert.getSerialNumber()+" NO ISSUER");
					     return false;
					     }
				 
			 } else{
				 System.out.println(interCert.getIssuerDN().getName());
				 System.out.println(localRootCert.getIssuerDN().getName());
//				 return ocsp.quickCheck(interCert, localRootCert, "1.2.643.2.2.9", null);
				 return ocsp.quickCheck(interCert, localRootCert, /*url,*/ "1.2.643.2.2.9", null);
			 }
	       }
		catch (Exception e) {
			StringWriter outError = new StringWriter();
			   PrintWriter errorWriter = new PrintWriter( outError );
			   e.printStackTrace(errorWriter);
			   log.error(outError.toString());
			   return false;
		}
		}
	}

}

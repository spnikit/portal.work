package com.aisa.portal.invoice.notice;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;

import sheff.rjd.utils.Base64;

import com.aisa.crypto.ocsp.OCSPException;
import com.aisa.crypto.ocsp.OCSPReq;
import com.aisa.crypto.ocsp.OCSPResp;
import com.aisa.crypto.ocsp.OCSPoverHTTP;
import com.aisa.crypto.tsp.TSPException;
import com.aisa.crypto.tsp.TSPRequest;
import com.aisa.crypto.tsp.TSPoverHTTP;
 
public class ConfirmationGenerator {
	
	private String sWVersion="1.0";
	
public String getsWVersion() {
		return sWVersion;
	}
	public void setsWVersion(String sWVersion) {
		this.sWVersion = sWVersion;
	}
public static void main(String[] args) throws  Exception{
	ocsp();
}
private static void tsp() throws  Exception
{
	FileInputStream in=new  FileInputStream("/home/zpss/CArzd2.cer");
	FileChannel fch = in.getChannel();

	ByteBuffer bf=  ByteBuffer.allocate((int) fch.size());
	fch.read(bf);
	String str=new String (bf.array(), "UTF-8");
	byte[] inbyte= Base64.decode(str);

	TSPoverHTTP sr = new TSPoverHTTP("http://10.250.2.148/tsp/tsp.srf","5000");
    TSPRequest tr = new TSPRequest("1.2.643.2.2.9",inbyte,null);
    
    byte[] ok = sr.getResp(tr);
    
    String outstr=  Base64.encodeBytes(ok,Base64.GZIP );
    
    FileOutputStream file=new FileOutputStream("/home/zpss/test.out");
    file.write(outstr.getBytes("UTF-8"));
    file.close();
  
}
 private static void ocsp() throws  Exception{
	 CertificateFactory cf = CertificateFactory.getInstance("X.509");
	 FileInputStream in=new  FileInputStream("/home/zpss/CArzd2.cer");
	 FileInputStream in2=new  FileInputStream("/home/zpss/CArzd.cer");
	 OCSPoverHTTP sr = new OCSPoverHTTP("http://10.250.2.148/ocsp/ocsp.srf", 5000);
     X509Certificate userCert = (X509Certificate)cf.generateCertificate( (in));	
     X509Certificate issuerCert = (X509Certificate)cf.generateCertificate( (in2));	
	 OCSPReq req = new OCSPReq(userCert, issuerCert,"1.2.643.2.2.9", null);
	 FileOutputStream file=new FileOutputStream("/home/zpss/ocsp.out");
	 file.write( sr.getResp(req));
	 file.close();
	 
 }
	
}

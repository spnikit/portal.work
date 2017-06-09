package ru.aisa.etdadmin.controllers.MultipartImpl;

import java.io.ByteArrayInputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.security.cert.X509Certificate;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.log4j.Logger;

import ru.aisa.rgd.etd.extend.SNMPSender;
import sheff.rjd.utils.OCSPutils;

public class OCSPUtilsForAdmin extends OCSPutils{
	

	public OCSPUtilsForAdmin(String[] certfile, String[] urls, String[] timeout) {
		super(certfile, urls, timeout);
	}
	
	private static Logger	log	= Logger.getLogger(OCSPUtilsForAdmin.class);
	
	public  String checkOCSPForAdmin( byte[] clientcer){
		try{
			
		 X509Certificate interCert = (X509Certificate)cf.generateCertificate(new ByteArrayInputStream(clientcer));
		 SimpleDateFormat formatter=new SimpleDateFormat("dd.MM.yyyy");
		 Date current=new Date();
		 Date d1=interCert.getNotAfter();
		 String data=formatter.format(d1);
		 
		 if(checkOCSP(clientcer)){
			 if(!d1.before(current)) return "Действителен до " + data;
					else return "Срок действия истек";
		}else return "Сертификат отозван";
		}
		 catch (Exception e) {
			StringWriter outError = new StringWriter();
			   PrintWriter errorWriter = new PrintWriter( outError );
			   e.printStackTrace(errorWriter);
			   log.error(outError.toString());
			   SNMPSender.sendMessage("SYS_ERROR", "SYS_0001", "0001", "ETD", "Crypto Lib Error");
			   return "";
		}
	}
}

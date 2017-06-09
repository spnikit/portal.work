package sheff.rjd.utils;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.Connection;
import java.sql.SQLWarning;

import javax.sql.DataSource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.log4j.Logger;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;

import ru.aisa.rgd.etd.extend.SNMPSender;



public class StartChecker implements ApplicationListener{
	private static Logger	log	= Logger.getLogger(StartChecker.class);
	private DataSource dsrjd;
	private String AppVer;
	
	
	public StartChecker(DataSource dsrjd,String AppVer){
				this.dsrjd = dsrjd;		
				
				this.AppVer = AppVer;	
		}
	
	
	public void init(){
		
		
		Connection con1=null;
		
		boolean ok=true;
	try{
		con1 = dsrjd.getConnection();
		SQLWarning warn = con1.getWarnings();
		if(warn!=null)
			log.warn("dsrjd has warnings");
		while (warn != null) {
	        log.warn("SQLState: " + warn.getSQLState());
	        log.warn("Message:  " + warn.getMessage());
	        log.warn("Vendor:   " + warn.getErrorCode());
	        warn = warn.getNextWarning();
	    }
		con1.createStatement();
		warn = con1.getWarnings();
		if(warn!=null)
			log.warn("dsrjd has warnings");
		while (warn != null) {
	        log.warn("SQLState: " + warn.getSQLState());
	        log.warn("Message:  " + warn.getMessage());
	        log.warn("Vendor:   " + warn.getErrorCode());
	        warn = warn.getNextWarning();
	    }
		con1.close();
		SNMPSender.sendMessage("ETD_EVENT", "ETD_0001", "0000", "DB=AS_ETD", "Test connection completed  successfully");
	}
	catch (Exception e) {
			ok = false;
			SNMPSender.sendMessage(e,"AS_ETD");
			try{con1.close();}catch (Exception exx) {}
			StringWriter outError = new StringWriter();
			PrintWriter errorWriter = new PrintWriter( outError );
			e.printStackTrace(errorWriter);
			log.error(outError.toString());
		}
	
	
		
	
	
	if (ok) SNMPSender.sendMessage("ETD_EVENT", "ETD_0002", "0000", "", "Application AS ETD version = "+AppVer+" initialization completed");
		
		
		
	}


	public void onApplicationEvent(ApplicationEvent event) {
		if (event instanceof ContextRefreshedEvent && event.toString().indexOf("Root WebApplicationContext")> -1){
			init();
		}
		
	}
		
		
	

}

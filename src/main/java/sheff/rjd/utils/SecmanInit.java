package sheff.rjd.utils;


import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.log4j.Logger;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import sun.util.logging.resources.logging;

public class SecmanInit {
public static SecurityManager secman;
private static Logger	log	= Logger.getLogger(SecmanInit.class);
	public static void init() {
	    try{
//	System.out.println("try");
	    ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext("secman.xml");		
	    secman =(SecurityManager) ctx.getBean("securitymanager");
	   	log.debug("Secman inited");
	}catch (Exception e){
	    e.printStackTrace();
	    log.error("secman was not inited");
	}
}
	
	
}

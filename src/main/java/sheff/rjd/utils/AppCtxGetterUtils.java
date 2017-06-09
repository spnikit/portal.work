package sheff.rjd.utils;

import org.springframework.context.ApplicationContext;

public class AppCtxGetterUtils {
	
	private static ApplicationContext ctx;  
	
	public static void setApplicationContext(ApplicationContext applicationContext) {  
        ctx = applicationContext;  
    }  
	
	public static ApplicationContext getApplicationContext() {  
        return ctx;  
    }  

}

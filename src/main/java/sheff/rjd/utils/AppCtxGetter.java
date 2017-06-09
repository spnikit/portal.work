package sheff.rjd.utils;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

public class AppCtxGetter implements ApplicationContextAware {

	public void setApplicationContext(ApplicationContext arg0) throws BeansException {
		AppCtxGetterUtils.setApplicationContext(arg0);
		System.setProperty("org.apache.activemq.default.directory.prefix", 
				(String)AppCtxGetterUtils.getApplicationContext().getBean("logsdir")+"amq/");		
		
	}

}

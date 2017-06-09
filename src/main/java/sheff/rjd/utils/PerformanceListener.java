package sheff.rjd.utils;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.log4j.Logger;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.web.context.support.RequestHandledEvent;

public class PerformanceListener implements ApplicationListener{
	protected final Logger logger = Logger.getLogger(getClass());

	
	public void onApplicationEvent(ApplicationEvent event) {	
		if (event instanceof RequestHandledEvent) {	
			RequestHandledEvent rhe = (RequestHandledEvent) event;
			
			logger.warn(rhe.getDescription());
		}
	}

}

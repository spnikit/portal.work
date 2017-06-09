package sheff.rjd.services;

import org.springframework.jdbc.core.simple.SimpleJdbcTemplate;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.ws.client.core.WebServiceTemplate;

import sheff.rjd.services.SyncDetails;


public class EtdSyncJob {
    
    public ThreadPoolTaskExecutor getTaskExecutor() {
	return taskExecutor;
}
public void setTaskExecutor(ThreadPoolTaskExecutor taskExecutor) {
	this.taskExecutor = taskExecutor;
}
private  ThreadPoolTaskExecutor taskExecutor;

    
public WebServiceTemplate getWst() {
    return wst;
}
public void setWst(WebServiceTemplate wst) {
    this.wst = wst;
}
public SimpleJdbcTemplate getSjt() {
    return sjt;
}
public void setSjt(SimpleJdbcTemplate sjt) {
    this.sjt = sjt;
}
private WebServiceTemplate wst;
public String getUrl() {
    return url;
}
public void setUrl(String url) {
    this.url = url;
}
private SimpleJdbcTemplate sjt;
private String url;
private void doIt(){
    System.out.println("Test");
    SyncDetails sd = new SyncDetails(wst, sjt, url);
   taskExecutor.execute(sd);
    
}
}

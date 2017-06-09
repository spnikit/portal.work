package sheff.rjd.services;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.ObjectOutputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcTemplate;
import org.springframework.oxm.Marshaller;
import org.springframework.ws.client.core.WebServiceTemplate;
import org.springframework.ws.server.endpoint.AbstractMarshallingPayloadEndpoint;

import ru.aisa.rgd.ws.utility.TypeConverter;

import rzd8888.gvc.etd.was.etd.etdegrposync.Table;

import rzd8888.gvc.etd.was.etd.etdegrposync.EtdEgrpoSyncRequestDocument.EtdEgrpoSyncRequest;
import rzd8888.gvc.etd.was.etd.etdegrposync.EtdEgrpoSyncRequestDocument;

import sheff.rjd.mappers.EgrporowMapper;

import rzd8888.gvc.etd.was.etd.initsync.InitsyncResponseDocument.InitsyncResponse;
import rzd8888.gvc.etd.was.etd.initsync.InitsyncResponseDocument;


public class EtdInitSyncEndpoint extends AbstractMarshallingPayloadEndpoint {
    
    private WebServiceTemplate wst;
  private SimpleJdbcTemplate sjt;
    private NamedParameterJdbcTemplate npjt;
    public WebServiceTemplate getWst() {
        return wst;
    }
    public void setWst(WebServiceTemplate wst) {
        this.wst = wst;
    }
    public NamedParameterJdbcTemplate getNpjt() {
	return npjt;
}
public void setNpjt(NamedParameterJdbcTemplate npjt) {
	this.npjt = npjt;
}
public EtdInitSyncEndpoint(Marshaller marshaller) {
	super(marshaller);
}

public SimpleJdbcTemplate getSjt() {
	return sjt;
}


public void setSjt(SimpleJdbcTemplate sjt) {
	this.sjt = sjt;
}


    @Override
    protected Object invokeInternal(Object obj) throws Exception {
	
	String url = "";
	  StringBuffer az= new StringBuffer();
	InitsyncResponseDocument init = (InitsyncResponseDocument) obj;
	String [] row  =init.getInitsyncResponse().getOkporow().split(",");
	int count = init.getInitsyncResponse().getCount();
	
	int tryes = count/1000;
	//Цикл вызовов синхронизации по 1к записей

	int i=0;
	
	for (int z= 0; z<tryes; z++){
	    int j = i+1000; 
	    
	    for (i = i; i<j; i++ )
	{
	
	  az.append(row[i]+",");
	 
	}
	    System.out.println(az);
	 //   System.out.println(az.toString().split(",")[4]);
	    System.out.println("do smthg with buffer");
	    EtdEgrpoSyncRequestDocument egrpo = EtdEgrpoSyncRequestDocument.Factory.newInstance();
		egrpo.addNewEtdEgrpoSyncRequest();
		egrpo.getEtdEgrpoSyncRequest().setOkporow(az.toString());
		wst.marshalSendAndReceive(url, egrpo);
	    
	    az.setLength(0);
		   
}
	
	
	return null;
    }

}

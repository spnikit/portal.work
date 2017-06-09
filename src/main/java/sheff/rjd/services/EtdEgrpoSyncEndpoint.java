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
import org.springframework.ws.server.endpoint.AbstractMarshallingPayloadEndpoint;

import ru.aisa.rgd.ws.utility.TypeConverter;

import rzd8888.gvc.etd.was.etd.etdegrposync.Table;

import rzd8888.gvc.etd.was.etd.etdegrposync.EtdEgrpoSyncResponseDocument;
import rzd8888.gvc.etd.was.etd.etdegrposync.EtdEgrpoSyncResponseDocument.EtdEgrpoSyncResponse;

import sheff.rjd.mappers.EgrporowMapper;


public class EtdEgrpoSyncEndpoint extends AbstractMarshallingPayloadEndpoint {
    
    
  private SimpleJdbcTemplate sjt;
    private NamedParameterJdbcTemplate npjt;
    public NamedParameterJdbcTemplate getNpjt() {
	return npjt;
}
public void setNpjt(NamedParameterJdbcTemplate npjt) {
	this.npjt = npjt;
}
public EtdEgrpoSyncEndpoint(Marshaller marshaller) {
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
	
	
	EtdEgrpoSyncResponseDocument resp  = (EtdEgrpoSyncResponseDocument) obj;
	EtdEgrpoSyncResponse egrporesp = resp.getEtdEgrpoSyncResponse();
List<Table> a = TypeConverter.arrayToArrayList(egrporesp.getTablesyncArray());
	
	
	EgrporowMapper rw = new EgrporowMapper(sjt);	
	
	rw.processRow(a);
	return null;
    }

}

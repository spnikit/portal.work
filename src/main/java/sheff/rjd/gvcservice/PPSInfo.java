package sheff.rjd.gvcservice;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.Types;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.DataFormatException;

import javax.sql.DataSource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.log4j.Logger;
import org.springframework.jdbc.core.SqlOutParameter;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.oxm.Marshaller;
import org.springframework.ws.server.endpoint.AbstractMarshallingPayloadEndpoint;

import ru.aisa.rgd.ws.dao.ServiceFacade;
import rzd.util.PpsinfoRequestDocument;
import rzd.util.PpsinfoResponseDocument;
import rzd.util.PpsinfoResponseDocument.PpsinfoResponse;
import sheff.rjd.utils.MyStoredProc;
public class PPSInfo extends AbstractMarshallingPayloadEndpoint {
    private NamedParameterJdbcTemplate npjt;
    private static Logger	log	= Logger.getLogger(PPSInfo.class);
	public  PPSInfo(Marshaller marshaller) {
		super(marshaller);
	}
	
	public NamedParameterJdbcTemplate getNpjt() {
		return npjt;
	}

	public void setNpjt(NamedParameterJdbcTemplate npjt) {
		this.npjt = npjt;
	}
private ServiceFacade facade;
	
	public ServiceFacade getFacade() {
	return facade;
}

public void setFacade(ServiceFacade facade) {
	this.facade = facade;
}

private DataSource ds;

	
	public DataSource getDs() {
	return ds;
}

public void setDs(DataSource ds) {
	this.ds = ds;
}

	protected Object invokeInternal(final Object inn)  {
	
		PpsinfoRequestDocument request = (PpsinfoRequestDocument) inn;
		String formname = request.getPpsinfoRequest().getFormname();
		int code = request.getPpsinfoRequest().getCode();
		int predid = request.getPpsinfoRequest().getPredid();
		
		HashMap<String, Object> pp = new HashMap<String, Object>();
		pp.put("name", formname);
		int typeid = facade.getNpjt().queryForInt("select id from snt.doctype where name =:name", pp);
		
		String no = callNumProcedure(predid, typeid, code).get("DocNum").toString();
		
		
		
		
		PpsinfoResponseDocument response = PpsinfoResponseDocument.Factory.newInstance();
		PpsinfoResponse resp = response.addNewPpsinfoResponse();
		resp.setInn("inn");
		resp.setKpp("kpp");
		resp.setNo(no);
		resp.setOkpo("okpo");
		return response;
		
	}

	private Map callNumProcedure(int predid, int typeid, int code){	
		MyStoredProc sproc = new MyStoredProc(getDs());
		sproc.setSql("SNT.GETDOC_M_NUM_ZPPS");
		sproc.declareParameter(new SqlOutParameter("out", Types.CHAR));
		sproc.declareParameter(new SqlParameter("PredId", Types.INTEGER));
		sproc.declareParameter(new SqlParameter("sFormID", Types.INTEGER));
		sproc.declareParameter(new SqlParameter("Code", Types.INTEGER));
		sproc.declareParameter(new SqlOutParameter("DocNum", Types.INTEGER));
		sproc.compile();
		Map input = new HashMap();
		input.put("PredId", predid);
		input.put("sFormID", typeid);
		input.put("Code", code);
		return sproc.execute(input);
	}
}


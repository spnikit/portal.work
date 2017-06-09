package sheff.rjd.gvcservice;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.log4j.Logger;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.ParameterizedRowMapper;
import org.springframework.oxm.Marshaller;
import org.springframework.ws.server.endpoint.AbstractMarshallingPayloadEndpoint;



import ru.aisa.rgd.etd.extend.SNMPEvent;
import ru.aisa.rgd.etd.extend.SNMPSender;
import rzd.util.ServDatetimeRequestDocument;
import rzd.util.ServDatetimeResponseDocument;


public class GetServ_Datetime extends AbstractMarshallingPayloadEndpoint {
    private NamedParameterJdbcTemplate npjt;
    private static Logger	log	= Logger.getLogger(GetServ_Datetime.class);
	public  GetServ_Datetime(Marshaller marshaller) {
		super(marshaller);
	}
	
	public NamedParameterJdbcTemplate getNpjt() {
		return npjt;
	}

	public void setNpjt(NamedParameterJdbcTemplate npjt) {
		this.npjt = npjt;
	}
	protected Object invokeInternal(final Object inn)  {
		String datetime;

		ServDatetimeResponseDocument resp = ServDatetimeResponseDocument.Factory.newInstance();
		resp.addNewServDatetimeResponse();
		
		try
		{	
			Date data = new Date();
			
			SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
			datetime = formatter.format(data);
			
			resp.getServDatetimeResponse().setDatatime(datetime);
		
			
		}
	
		catch (Exception e) {
			StringWriter outError = new StringWriter();
			PrintWriter errorWriter = new PrintWriter( outError );
			e.printStackTrace(errorWriter);
			log.error(outError.toString());
			datetime = "error";
			SNMPSender.sendMessage(e);
		}
	
		return resp;		
	}
}


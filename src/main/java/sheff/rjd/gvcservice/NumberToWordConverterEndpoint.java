package sheff.rjd.gvcservice;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.oxm.Marshaller;
import org.springframework.ws.server.endpoint.AbstractMarshallingPayloadEndpoint;

import ru.aisa.rgd.ws.dao.ServiceFacade;
import ru.aisa.rgd.ws.documents.ETDForm;
import ru.aisa.rgd.ws.domain.Document;
import ru.aisa.rgd.ws.utility.DataBinder;
import ru.aisa.rgd.ws.utility.TypeConverter;
import sheff.rgd.ws.Controllers.TOR.NumberOfWords;
import sheff.rjd.ws.handlers.ACTRTKXmlHandler;
import sheff.rjd.ws.handlers.CSFXmlHandler;
import sheff.rjd.ws.handlers.FPU26XmlHandler;
import sheff.rjd.ws.handlers.FormHandler;
import sheff.rjd.ws.handlers.SFXmlHandler;
import sheff.rjd.ws.handlers.TORG12XmlHandler;
import rzd.util.NumberToWordRequestDocument;
import rzd.util.NumberToWordResponseDocument;

public class NumberToWordConverterEndpoint extends AbstractMarshallingPayloadEndpoint {

	private static final Logger logger = Logger.getLogger(NumberToWordConverterEndpoint.class);

	public NumberToWordConverterEndpoint(Marshaller marshaller)
	{
		super(marshaller);
	}

	public NumberToWordConverterEndpoint()
	{
		super();
	}
	

	
	@Override
	protected Object invokeInternal(Object o) throws Exception
	{
		final NumberToWordResponseDocument respDoc = NumberToWordResponseDocument.Factory.newInstance();
		final NumberToWordResponseDocument.NumberToWordResponse resp = respDoc.addNewNumberToWordResponse();
		try {
			final DataBinder rBinder = new DataBinder(o.toString());
			String number = rBinder.getValue("number");
			NumberOfWords now = new NumberOfWords(number);
		    resp.setResult(now.num2str(false));
		} catch (Exception e){
			e.printStackTrace();
			logger.error(e.getMessage(), e);
		}
		
		return respDoc;
	}
	
}

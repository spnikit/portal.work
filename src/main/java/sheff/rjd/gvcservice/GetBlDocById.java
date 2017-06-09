package sheff.rjd.gvcservice;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.GZIPInputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.log4j.Logger;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.oxm.Marshaller;
import org.springframework.ws.server.endpoint.AbstractMarshallingPayloadEndpoint;
import org.w3c.dom.Document;

import rzd.util.BlDocByIdRequestDocument;
import rzd.util.BlDocByIdResponseDocument;
import sheff.rjd.utils.Base64Codec;

public class GetBlDocById extends AbstractMarshallingPayloadEndpoint {
    private NamedParameterJdbcTemplate npjt;
    private static Logger	log	= Logger.getLogger(GetBlDocById.class);
	public  GetBlDocById(Marshaller marshaller) {
		super(marshaller);
	}
	
	public NamedParameterJdbcTemplate getNpjt() {
		return npjt;
	}

	public void setNpjt(NamedParameterJdbcTemplate npjt) {
		this.npjt = npjt;
	}

	protected Object invokeInternal(final Object inn)  {
		
		 BlDocByIdRequestDocument req = (BlDocByIdRequestDocument) inn;
	    
	    BlDocByIdResponseDocument resp = BlDocByIdResponseDocument.Factory.newInstance();
		try
		{
		    Map pp = new HashMap();
		    pp.put("ETDID", req.getBlDocByIdRequest());
		    
		  byte[] blob =  (byte[])npjt.queryForObject("select bldoc from snt.docstore where etdid = :ETDID", pp, byte[].class);
		    
		  String tmp = new String(blob, "UTF-8");
		  
		
			BldocHandler handler  = new BldocHandler();

			resp.setBlDocByIdResponse(handler.open(tmp));
		}
	
		catch (Exception e) {
			StringWriter outError = new StringWriter();
			PrintWriter errorWriter = new PrintWriter( outError );
			e.printStackTrace(errorWriter);
			log.error(outError.toString());
			resp.setBlDocByIdResponse("");
			}
	
		return resp;		
	}
	
	private static byte[] Base64GzipToBinary(byte theData[]) throws Exception
	{
	    byte theDecodedBytes[] = null;
	    theDecodedBytes = Base64Codec.base64Decode(theData);
	    try
	    {
	        ByteArrayInputStream theStream = new ByteArrayInputStream(theDecodedBytes) {

	            public synchronized int read()
	            {
	                int test = super.read();
	                return test;
	            }

	        };
	        GZIPInputStream theGzipStream = new GZIPInputStream(theStream);
	        ByteArrayOutputStream theOutput = new ByteArrayOutputStream(2048);
	        byte theBytes[] = new byte[2048];
	        do
	        {
	            if(theGzipStream.available() != 1)
	            {
	                break;
	            }
	            int numBytes = theGzipStream.read(theBytes);
	            if(numBytes < 1)
	            {
	                break;
	            }
	            theOutput.write(theBytes, 0, numBytes);
	        } while(true);
	        theGzipStream.close();
	        theDecodedBytes = theOutput.toByteArray();
	    }
	    catch(Exception e)
	    {
	        e.printStackTrace();
	    }
	    return theDecodedBytes;
	}
}


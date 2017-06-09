package sheff.rjd.ws;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.log4j.Logger;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.oxm.Marshaller;

import ru.aisa.edt.DropDocDocument;
import ru.aisa.edt.Signature;
import ru.aisa.rgd.etd.dao.ETDFacade;
import ru.aisa.rgd.etd.extend.SNMPSender;
import ru.aisa.rgd.etd.ws.ETDAbstractSecurityEndoint;
import ru.aisa.rgd.ws.client.ETDStandartSignification;
import sheff.rjd.dispatcher.Dispatcher;



public class DropTorg12Endpoint extends
ETDAbstractSecurityEndoint<DropDocDocument, DropDocDocument>{
//extends AbstractMarshallingPayloadEndpoint {
	private NamedParameterJdbcTemplate npjt;
	private DataSource ds;	
    private static Logger	log	= Logger.getLogger(DropTorg12Endpoint.class);
    private static Logger logger = Logger.getLogger(Dispatcher.class);
  private ETDStandartSignification[] disp1array;
    private String formType; //Тип формы
	private String formNumber; //Номер, подтип
	
	public DataSource getDs() {
		return ds;
	}

	public void setDs(DataSource ds) {
		this.ds = ds;
	}
	
	@Override
	protected DropDocDocument composeInvalidSecurityResponce(Signature signature) 
	{
		DropDocDocument responseDocument = DropDocDocument.Factory.newInstance();
		//SaveResponse response = responseDocument.addNewSaveResponse();
		//responseDocument.addNewData();
		responseDocument.getDropDoc().setResp("bad signature"); //addNewSecurity().setLogonstatus("false");
		return responseDocument;
	}

	@Override
	protected DropDocDocument convertRequest(Object obj) {
		DropDocDocument requestDocument = (DropDocDocument) obj;		
		return requestDocument;
	}

	@Override
	protected Signature getSecurity(DropDocDocument requestDocument) 
	{
		Signature s = requestDocument.getDropDoc().getSecurity();
		return s;
	}

	public DropTorg12Endpoint(Marshaller marshaller) {
		super(marshaller);
	}

	//protected Object invokeInternal(Object obj) {
	protected DropDocDocument processRequest(DropDocDocument requestDocument, Signature signature1, ETDFacade facade) throws Exception 
	{
		//System.out.println("DropDocDocument ");
		//setAllcorrect(false);
		final boolean[] allcorrect=new boolean[1]; 
		allcorrect[0]=false;
		final DropDocDocument ddreq = requestDocument;//(DropDocDocument)obj;
		try {
		String username = ddreq.getDropDoc().getSecurity().getUsername();
		ddreq.getDropDoc().getSecurity().setUsername("0");
		byte[] signature = ddreq.getDropDoc().getSecurity().getSign();
		ddreq.getDropDoc().getSecurity().setSign(new byte[0]);
		String certid = ddreq.getDropDoc().getSecurity().getCertid();
		ddreq.getDropDoc().getSecurity().setCertid("0");
		final HashMap<String,Comparable> signlvl = new HashMap<String, Comparable>(2);
		final HashMap<String,String> name = new HashMap<String, String>(1);
		final Map<String, Comparable> pp = new HashMap<String, Comparable>();
//		System.out.println(ddreq.getDropDoc().getId());
		pp.put("DOCID", ddreq.getDropDoc().getId());
		pp.put("REASON", ddreq.getDropDoc().getReason());
		pp.put("CERTSERIAL", new BigInteger(certid,16).toString());
		pp.put("WRKID", ddreq.getDropDoc().getWrkid());		
		
				getNpjt().update("update snt.docstore set droptime = current timestamp, droptxt = :REASON, "
						+ "dropid = (select id from snt.personall where certserial = :CERTSERIAL), dropwrkid = :WRKID, DROPPREDID = (select predid from snt.docstore where id = :DOCID)  where id = :DOCID ", pp);
		
				
				
			ddreq.getDropDoc().setResp("true");
			
		
		}
		catch (Exception e) {
			StringWriter outError = new StringWriter();
			   PrintWriter errorWriter = new PrintWriter( outError );
			   e.printStackTrace(errorWriter);
			   //e.printStackTrace(System.out);
			   log.error(outError.toString());
			   SNMPSender.sendMessage(e);
			ddreq.getDropDoc().setResp("false");
		}
		return ddreq;
	}


	public NamedParameterJdbcTemplate getNpjt() {
		return npjt;
	}


	public void setNpjt(NamedParameterJdbcTemplate npjt) {
		this.npjt = npjt;
	}

	
	public String getFormNumber() {
		return formNumber;
	}
}


package sheff.rjd.ws;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.math.BigInteger;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.sql.DataSource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.log4j.Logger;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.ParameterizedRowMapper;
import org.springframework.oxm.Marshaller;
import org.springframework.ws.transport.context.TransportContext;
import org.springframework.ws.transport.context.TransportContextHolder;
import org.springframework.ws.transport.http.HttpServletConnection;

import ru.aisa.edt.DropDocDocument;
import ru.aisa.edt.Signature;
import ru.aisa.rgd.etd.dao.ETDFacade;
import ru.aisa.rgd.etd.extend.SNMPSender;
import ru.aisa.rgd.etd.ws.ETDAbstractSecurityEndoint;
import ru.aisa.rgd.ws.client.ETDStandartSignification;
import sheff.rgd.ws.Abstract.DoAction;
import sheff.rjd.dispatcher.Dispatcher;



public class DropDocEndpoint extends
ETDAbstractSecurityEndoint<DropDocDocument, DropDocDocument>{
//extends AbstractMarshallingPayloadEndpoint {
	private NamedParameterJdbcTemplate npjt;
	private DataSource ds;	
    private static Logger	log	= Logger.getLogger(DropDocEndpoint.class);
    private static Logger logger = Logger.getLogger(Dispatcher.class);
  private ETDStandartSignification[] disp1array;
    private String formType; //Тип формы
	private String formNumber; //Номер, подтип
	private DoAction formControllers;
//	private AfterSign afterSign;
	
//	public void setAfterSign(AfterSign dispatcher3) {
//		this.afterSign = dispatcher3;
//	}
//	
//	
//	public AfterSign getAfterSign() {
//		return afterSign;
//	}

	
	public DataSource getDs() {
		return ds;
	}

	public DoAction getFormControllers() {
		return formControllers;
	}

	public void setFormControllers(DoAction formControllers) {
		this.formControllers = formControllers;
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

	public DropDocEndpoint(Marshaller marshaller) {
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
		//if (getSecurityManager().checkSignature(/*username,*/ signature, certid)) {
		//List result = 
			getNpjt().query("select ID, "+
					//+ "INUSEID, "
					"case when opentime is not null and opentime + 30 minutes <current timestamp then null else INUSEID end INUSEID, "+
					"DROPID,SIGNLVL," +
				"(select rtrim(name) from snt.doctype where id = typeid) as NAME " +
				" from snt.DOCSTORE where ID = :DOCID ",pp, new ParameterizedRowMapper<Object>() {
			public Object mapRow(ResultSet rs, int numrow) {
				try{	
					
					
				if (rs.getObject("DROPID") != null) ddreq.getDropDoc().setResp("already");
					else if (rs.getObject("INUSEID") != null && rs.getInt("INUSEID") != getNpjt().queryForInt("select id from snt.personall where certserial = :CERTSERIAL ", pp)){ 
						pp.put("useid", rs.getInt("INUSEID"));
						String fiouser = (String) getNpjt().queryForObject("select rtrim(fname) || ' '||rtrim(mname) ||' '||rtrim(lname)   from snt.personall where id = :useid", pp, String.class);
						ddreq.getDropDoc().setResp("busy, "+fiouser+"");
						}
					//else if(rs.getInt("CDEL")==0) ddreq.getDropDoc().setResp("forbidden");
					else {	
						//setAllcorrect(true);
						allcorrect[0]=true;
						
											
					}	
				signlvl.put("sign", rs.getInt("SIGNLVL"));
				signlvl.put("id", rs.getLong("ID"));
				//signlvl.put("url", rs.getString("URL"));
				name.put("name", rs.getString("NAME"));

				}
				catch (Exception e) {
					StringWriter outError = new StringWriter();
					   PrintWriter errorWriter = new PrintWriter( outError );
					   e.printStackTrace(errorWriter);
					   //e.printStackTrace(System.out);
					   log.error(outError.toString());
//					   SNMPSender.sendMessage(e);
				}
				return null;
			}
			
		});
		if (allcorrect[0]){
			
			getNpjt().update("update snt.docstore set droptime = current timestamp, droptxt = :REASON, dropid = (select id from snt.personall where certserial = :CERTSERIAL), dropwrkid = :WRKID, DROPPREDID = (select predid from snt.docstore where id = :DOCID)  where id = :DOCID ", pp);
				
				TransportContext context = TransportContextHolder.getTransportContext();
				HttpServletConnection conn = (HttpServletConnection )context.getConnection();
				HttpServletRequest request = conn.getHttpServletRequest();
				
				//sendMessage(signlvl.get("url").toString(), (Long)signlvl.get("id"), pp);
				//work only with message broker
			//}
				String declName = (String)getNpjt().queryForObject("select name from snt.doctype where id = (select typeid from snt.docstore where id = :DOCID)", pp, String.class);
				//if(declName.trim().equals("РДВ")||declName.trim().equals("ФПУ-26"))
				
					String bl = new String((byte[])getNpjt().queryForObject("select bldoc from snt.docstore where id = :DOCID", pp, byte[].class), "UTF-8");
					int sign = getNpjt().queryForInt("select count(docid) from snt.docstoreflow where docid = :DOCID", pp);
					int predid = getNpjt().queryForInt("select predid from snt.docstore where id = :DOCID", pp);
					if (formControllers != null)
					{					
						formControllers.doAfterSign(declName.trim(), bl, predid, sign, ddreq.getDropDoc().getId(), certid, ddreq.getDropDoc().getWrkid());//(formname, docdata, predkod, signsnumber, cert,	id, workerid);
					}
				
				
			ddreq.getDropDoc().setResp("true");
			
		}
		//}
		//else ddreq.getDropDoc().setResp("invalid_sign");
		}
		catch (Exception e) {
			StringWriter outError = new StringWriter();
			   PrintWriter errorWriter = new PrintWriter( outError );
			   e.printStackTrace(errorWriter);
			   //e.printStackTrace(System.out);
			   log.error(outError.toString());
//			   SNMPSender.sendMessage(e);
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


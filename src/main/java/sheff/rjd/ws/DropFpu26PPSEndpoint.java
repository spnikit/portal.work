package sheff.rjd.ws;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.math.BigInteger;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.List;
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

import com.ibm.db2.jcc.b.bi;
import com.ibm.db2.jcc.b.dd;

import ru.aisa.edt.DropDocDocument;
import ru.aisa.edt.Signature;
import ru.aisa.rgd.etd.dao.ETDFacade;
import ru.aisa.rgd.etd.extend.SNMPSender;
import ru.aisa.rgd.etd.ws.ETDAbstractSecurityEndoint;
import ru.aisa.rgd.ws.documents.ETDForm;
import ru.aisa.rgd.ws.utility.DataBinder;
import ru.aisa.rgd.ws.utility.TypeConverter;
import sheff.rgd.ws.Abstract.DoAction;
import sheff.rjd.utils.Base64;



public class DropFpu26PPSEndpoint extends
ETDAbstractSecurityEndoint<DropDocDocument, DropDocDocument>{
//extends AbstractMarshallingPayloadEndpoint {
	private NamedParameterJdbcTemplate npjt;
	private DataSource ds;	
    private static Logger	log	= Logger.getLogger(DropFpu26PPSEndpoint.class);
    private String formNumber; //Номер, подтип
    private DoAction formControllers;
    private static String packsql = "select id ,(select rtrim(name) from snt.pred where id = ds.predid) pred, (case when "+
			" (select name from snt.doctype where id = ds.typeid) "+
			" like '%Пакет%' then 1 else 0 end ) type, stat from snt.docstore ds where id = :DOCID";
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

	public DropFpu26PPSEndpoint(Marshaller marshaller) {
		super(marshaller);
	}

	
	
	//protected Object invokeInternal(Object obj) {
	public DoAction getFormControllers() {
		return formControllers;
	}

	public void setFormControllers(DoAction formControllers) {
		this.formControllers = formControllers;
	}

	protected DropDocDocument processRequest(DropDocDocument requestDocument, Signature signature1, ETDFacade facade) throws Exception 
	{
		
//		System.out.println(requestDocument);
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
		final Map<String, Object> pp = new HashMap<String, Object>();
		final HashMap<String,Comparable> signlvl = new HashMap<String, Comparable>(2);
		final HashMap<String,String> name = new HashMap<String, String>(1);
		
		
		
		pp.put("DOCID", ddreq.getDropDoc().getId());
		pp.put("REASON", ddreq.getDropDoc().getReason());
		pp.put("CERTSERIAL", new BigInteger(certid,16).toString());
		pp.put("WRKID", ddreq.getDropDoc().getWrkid());		

		
		
		
		//Проверка на некорректный пакет для трансойла
		List data = npjt.queryForList(packsql, pp);
		Map aa = (HashMap) data.get(0);
		String predname = aa.get("PRED").toString();
		int ispack = Integer.parseInt(aa.get("TYPE").toString());
		int stat = Integer.parseInt(aa.get("STAT").toString());
		
		if (predname.contains("Трансойл")&&ispack==1&&stat!=2){
			ddreq.getDropDoc().setResp("true");
		}
		
		else {
		
		
		byte[] document = getNpjt().queryForObject("select bldoc from snt.docstore where id =:DOCID", pp, byte[].class);
		
		ETDForm form = ETDForm.createFromArchive(document);
		
		DataBinder binder = form.getBinder();		

		binder.setNodeValue("m_otkl", ddreq.getDropDoc().getMobyte());
		binder.setNodeValue("p_m_otkl", ddreq.getDropDoc().getMosignbyte());
				
		String mouser = (String) getNpjt().queryForObject("select rtrim(fname) || ' '||rtrim(mname) ||' '||rtrim(lname)   from snt.personall where certserial = :CERTSERIAL", pp, String.class);
		String mowrkname = (String) getNpjt().queryForObject("select rtrim(name)   from snt.wrkname where id = :WRKID", pp, String.class);
		
		try{
			binder.setNodeValue("fio_otkl", mouser);
			binder.setNodeValue("dolj_otkl", mowrkname);
		} catch (Exception e){
			log.error("Old version of form, no such tags: "+TypeConverter.exceptionToString(e));
		}
		

		pp.put("DOCDATA", form.transform("data"));
		pp.put("BLDOC", form.encodeToArchiv());
		
		getNpjt().query("select ID, "+
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
//				   SNMPSender.sendMessage(e);
			}
			return null;
		}
		
	});
	if (allcorrect[0]){
			getNpjt().update("update snt.docstore set bldoc = :BLDOC, docdata =:DOCDATA, droptime = current timestamp, droptxt = :REASON, dropid = (select id from snt.personall where certserial = :CERTSERIAL), dropwrkid = :WRKID, DROPPREDID = (select predid from snt.docstore where id = :DOCID)  where id = :DOCID ", pp);
			
			TransportContext context = TransportContextHolder.getTransportContext();
			HttpServletConnection conn = (HttpServletConnection )context.getConnection();
			HttpServletRequest request = conn.getHttpServletRequest();
			
			String declName = (String)getNpjt().queryForObject("select name from snt.doctype where id = (select typeid from snt.docstore where id = :DOCID)", pp, String.class);
			
				String bl = new String((byte[])getNpjt().queryForObject("select bldoc from snt.docstore where id = :DOCID", pp, byte[].class), "UTF-8");
				int sign = getNpjt().queryForInt("select count(docid) from snt.docstoreflow where docid = :DOCID", pp);
				int predid = getNpjt().queryForInt("select predid from snt.docstore where id = :DOCID", pp);
				if (formControllers != null)
				{
				formControllers.doAfterSign(declName.trim(), bl, predid, sign, ddreq.getDropDoc().getId(), certid, ddreq.getDropDoc().getWrkid());//(formname, docdata, predkod, signsnumber, cert,	id, workerid);
				}
				
			ddreq.getDropDoc().setResp("true");
	}	
	}
		}
		catch (Exception e) {
			log.error(TypeConverter.exceptionToString(e));
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


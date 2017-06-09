package sheff.rjd.ws.OCO.AfterSign;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.sql.Types;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;
import javax.xml.transform.TransformerException;

import org.apache.log4j.Logger;
import org.apache.xmlbeans.XmlOptions;
import org.springframework.jdbc.core.SqlOutParameter;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.ParameterizedRowMapper;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.ws.client.core.WebServiceTemplate;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import ru.aisa.rgd.etd.dao.ETDUrlDAO;
import ru.aisa.rgd.ws.documents.ETDForm;
import ru.aisa.rgd.ws.exeption.InternalException;
import ru.aisa.rgd.ws.exeption.ServiceException;
import ru.aisa.rgd.ws.utility.DataBinder;
import ru.aisa.rgd.ws.utility.TypeConverter;
import rzd8888.gvc.etd.was.etd.synchronizeXfdlDoc.Action;
import rzd8888.gvc.etd.was.etd.synchronizeXfdlDoc.AddParams;
import rzd8888.gvc.etd.was.etd.synchronizeXfdlDoc.AddParams.Param;
import rzd8888.gvc.etd.was.etd.synchronizeXfdlDoc.EncodingBlob;
import rzd8888.gvc.etd.was.etd.synchronizeXfdlDoc.NameSystem;
import rzd8888.gvc.etd.was.etd.synchronizeXfdlDoc.ReservedNamesOfParams;
import rzd8888.gvc.etd.was.etd.synchronizeXfdlDoc.SynchronizeXfdlDocRequest;
import rzd8888.gvc.etd.was.etd.synchronizeXfdlDoc.SynchronizeXfdlDocRequestDocument;
import rzd8888.gvc.etd.was.etd.synchronizeXfdlDoc.SignatureFlow;
import sheff.rgd.ws.Abstract.DoAction;
import sheff.rjd.services.syncutils.SyncObj;
import sheff.rjd.utils.MyStoredProc;
import sheff.rjd.ws.OCO.TORLists;

public class SendToEtd {

    private NamedParameterJdbcTemplate npjt;
	private WebServiceTemplate wst;
	private String url;
	private ETDUrlDAO etdurldao;
	
	public ETDUrlDAO getEtdurldao() {
		return etdurldao;
	}

	public void setEtdurldao(ETDUrlDAO etdurldao) {
		this.etdurldao = etdurldao;
	}

	public DataSource getDs() {
        return ds;
    }

    public void setDs(DataSource ds) {
        this.ds = ds;
    }

    private DataSource ds;
    public DoAction formControllers;
    
	
	public void setWst(WebServiceTemplate wst)
	{
		this.wst = wst;
	}

	public WebServiceTemplate getWst()
	{
		return wst;
	}
	
	public String getUrl() {
	    return url;
	}

	public void setUrl(String url) {
	    this.url = url;
	}

	public NamedParameterJdbcTemplate getNpjt() {
		return npjt;
	}
	public void setNpjt(NamedParameterJdbcTemplate npjt) {
		this.npjt = npjt;
	}
    
	

	protected final Logger	log	= Logger.getLogger(getClass());
	
	
	public DoAction getFormControllers() {
		return formControllers;
	}

	public void setFormControllers(DoAction formControllers) {
		this.formControllers = formControllers;
	}

	
	private static final String sqlpacklistnew = "select id, (select rtrim(name) name from snt.doctype where id = ds.typeid), "+
			" (select count(0) signlvl from snt.docstoreflow where docid = ds.id), bldoc from snt.docstore ds where etdid in (select etdid from snt.packages where id_pak = (select id_pak from snt.docstore where id =:id)) and signlvl is not null "+
			" and typeid in (select id from snt.doctype where name in ('"+TORLists.ACTbrak+"', '"+TORLists.ACTzud+"','"+TORLists.Card+"', '"+TORLists.Spravka+"', '"+TORLists.VU22+"', '"+TORLists.VU23+"', '"+TORLists.VU36+"','"+TORLists.GU23+"','"+TORLists.VU41+"','"+TORLists.RSH+"','"+TORLists.RSV+"','"+TORLists.Rashifr+"','"+TORLists.Schet+"')) ";
	
	
	
	private static final String updatevisible = "update snt.docstore set visible = 2 where id =:id";
	
	private static final String getreqv = "select bldoc, etdid from snt.docstore where id =:id with ur";
	
	private static final String getfpu26 = "select bldoc from snt.docstore where typeid = (select id from snt.doctype where name = 'ФПУ-26') and id_pak =:idpack with ur";
	
	private static final String updtblob = "update snt.docstore set bldoc = :bldoc, docdata = :docdata where id =:id";
	
	private static final String getpackids = " select id_pak from snt.docstore where id_pak in "
			+ "(select id_pak from snt.packages where etdid = (select etdid from snt.docstore where id =:id)) "
			+ " and typeid in (select id from snt.doctype where name like '%Пакет документов%') and dropid is null and groupsgn = 0";
	
	private static final String check_doc_sign_sql = "select count(0) from snt.docstore where etdid in (select distinct(etdid) from snt.packages "
			+ "where id_pak = (select id_pak from snt.docstore where id = :id)) and signlvl is not null";
    /**
     * @param id - docid in portal
     * @param docdata = bldoc in string format
     * @param parentform - name of template
     * @param signNumber - signature count
     * @param drop - 1 if dropped
     * @param newdoc - true if newdoc
     * @throws UnsupportedEncodingException
     * @throws ServiceException
     * @throws IOException
     * @throws TransformerException
     */
    @SuppressWarnings("unchecked")
	public void SendToEtdMessage(long id, String docdata, String parentform, int signNumber,  int drop, boolean newdoc) throws UnsupportedEncodingException, ServiceException, IOException, TransformerException{
	SimpleDateFormat actionSDF = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss");
	
	XmlOptions options = new XmlOptions();
	options.setValidateOnSet();
	
	Object r= null;
	
	SynchronizeXfdlDocRequestDocument pd = SynchronizeXfdlDocRequestDocument.Factory.newInstance();
	SynchronizeXfdlDocRequest req = pd.addNewSynchronizeXfdlDocRequest();
		
		Map hm = new HashMap();
		hm.put("id", id);
		String bl = "";
		StringBuffer sb = new StringBuffer();
	
		try{
			
		if (docdata.indexOf("application/vnd.xfdl;")>-1){
			sb.append(docdata);
		}else {
			sb.append("application/vnd.xfdl;content-encoding=\"base64-gzip\"\n");
			sb.append(docdata);
		}
		
		bl = sb.toString();
		
		
		} catch (Exception e){
			log.error(TypeConverter.exceptionToString(e));
		}
		
		req.setAction(Action.SIGNING);
		
		AddParams params = req.addNewParams();
		
		
		if (drop==1)
		{
		   req.setAction(Action.DECLINING);
		   Param decline = params.addNewParam();
		   decline.setName(ReservedNamesOfParams.DECLINE_REASON.toString());
		   decline.setStringValue((String) getNpjt().queryForObject("select droptxt from snt.docstore where id = :id", hm, String.class));
		   Param declinepers = params.addNewParam();
		   declinepers.setName(ReservedNamesOfParams.DECLINE_PERSON.toString());
		   declinepers.setStringValue((String) getNpjt().queryForObject("select rtrim(cast(rtrim(fname) || ' ' || rtrim(mname) || ' ' || rtrim(lname) as char(240) ccsid unicode)) "+
"from SNT.personall where id = (select dropid from snt.docstore where id = :id)", hm, String.class));
	
		} 
		
//Для передачи ФИО подписанта
		
		if (drop==0){
			
			int issign = getNpjt().queryForInt("select case when lpersid is null then 0 else 1 end from snt.docstore where id=:id", hm);
			if (issign==1){
			Param signer = params.addNewParam();
			signer.setName(ReservedNamesOfParams.SIGN_PERSON.toString());
			signer.setStringValue((String) getNpjt().queryForObject("select rtrim(cast(rtrim(fname) || ' ' || rtrim(mname) || ' ' || rtrim(lname) as char(240) ccsid unicode)) "+
"from SNT.personall where id = (select lpersid from snt.docstore where id = :id)", hm, String.class));
		}
		}
		
		req.addNewBlobDoc();
		req.getBlobDoc().setStringValue(bl);
		req.getBlobDoc().setEncoding(EncodingBlob.BASE_64_GZIP_HEADER);
		req.setDestSystem(NameSystem.ASETD);
		req.setSrcSystem(NameSystem.PORTAL);
		req.setEtdDocId(getNpjt().queryForInt("select etdid from snt.docstore where id = :id", hm));
		req.setFormName(parentform);
		req.setIsEditForm(false);
	
		req.setCountSignatures(signNumber);
		req.addNewSecurity();
		req.getSecurity().setByteArrayValue(("test").getBytes("UTF-8"));
		req.getSecurity().setTypeSecurity("test");
		
			
		
		 Param timestamp = params.addNewParam();
		 timestamp.setName("_actionTimestamp");
		 timestamp.setStringValue(actionSDF.format(System.currentTimeMillis()));
		 
		 
		if (parentform.equals("ФПУ-26")){
			Param fpupakid =  params.addNewParam();
			fpupakid.setName("_f_fpu26_torPackId");
			String FpuIdPack = (String) getNpjt().queryForObject("select id_pak from snt.docstore where id =:id", hm, String.class);
			fpupakid.setStringValue(FpuIdPack);
		}
		
		if (newdoc){
			Param portalid =  params.addNewParam();
			portalid.setName("_portalId");
			portalid.setStringValue(String.valueOf(id));
		}
		
		
		 
		try{
			req.validate();
		r = wst.marshalSendAndReceive(etdurldao.getURL(), req);
		
		}catch (Exception e){
			log.error(TypeConverter.exceptionToString(e));
		}
		
		log.debug("Message for docid = "+id+" sent to AS ETD");
	
	
    }
    
   
    
    /**
     * @param id
     * @param certserial
     * @param WrkId
     * @param predid
     * @throws Exception
     */
    public void signpackdocs(long id, String certserial,int WrkId, int predid) throws Exception{
    	
    	
    	HashMap<String, Object> pp = new HashMap<String, Object>();
    	pp.put("id", id);
    	try{
    	List<PackSignObject> list = getNpjt().query(sqlpacklistnew, pp, 
				new ParameterizedRowMapper<PackSignObject>() 
				{
					public PackSignObject mapRow(ResultSet rs, int n) throws SQLException 
					{
						PackSignObject obj = new PackSignObject();
						obj.setId(rs.getLong("id"));
						obj.setName(rs.getString("name"));
						obj.setSignlvl(rs.getInt("signlvl"));
						try {
							obj.setBldoc(new String(rs.getBytes("bldoc"), "UTF-8"));
						} catch (UnsupportedEncodingException e) {
							log.error(TypeConverter.exceptionToString(e));
						}
						return obj;
					}
				});
		
    
    	int i=0;
    	while (i<list.size()){
    		try{
    		formControllers.doAfterSave(list.get(i).getName(), list.get(i).getBldoc(),
    				predid, list.get(i).getSignlvl(), certserial, list.get(i).getId(), WrkId);
    	} catch (Exception e) {
    		
			   log.error(TypeConverter.exceptionToString(e));
		}
    		i++;
    	}
    	//проверка на подписанные документов
		int checkDocSign = npjt.queryForInt(check_doc_sign_sql,pp);
		if(checkDocSign==0){
			getNpjt().update("update snt.docstore set GROUPSGN = 1 where id = :id",pp);
			SendOpenPackToEtd(id, false, 2);
		}
    	}catch (Exception e){
    		log.error(TypeConverter.exceptionToString(e));
    	}
    	
    }
    
    
    
    public void FakeSign(String docName, String docdata, int predid,
	    int signNumber, String CertID, long id, int WrkId){
	
	 Map pp = new HashMap();
	    pp.put("docid", id);
	    pp.put("docname", docName);
	    pp.put("docdata", docdata);
	    pp.put("predid", predid);
	    pp.put("signnumber", signNumber);
	    pp.put("CertSerial", new BigInteger(CertID, 16).toString());
	    pp.put("wrkid", WrkId);
	    
	    int userid = npjt.queryForInt("select id from snt.personall where certserial = :CertSerial", pp);
	    
	    
	    callSignProcedure(id, userid, WrkId, predid, new byte[1]);
	
	
	
    }
    
    
    public void PackUpdate (long id) throws Exception{
    	HashMap<String, Object> pp = new HashMap<String, Object>();
    	pp.put("id", id);
    	
    	List<SyncObj> result = getNpjt().query(getpackids, pp, 
				new ParameterizedRowMapper<SyncObj>() 
				{
					public SyncObj mapRow(ResultSet rs, int n) throws SQLException 
					{
						SyncObj obj = new SyncObj();
						obj.setId_pak(rs.getString("ID_PAK"));
						return obj;
					}
				});
    	String formname  = getNpjt().queryForObject("select rtrim(name) from snt.doctype where id ="
    			+ " (select typeid from snt.docstore where id = :id)" , pp, String.class);
    	for (int i=0; i<result.size(); i++){
    		try{
    		pp.put("id_pak", result.get(i).getId_pak());
        	int count = getNpjt().queryForInt( "select count(id) from snt.docstore where etdid in (select etdid from snt.packages where id_pak = :id_pak) and signlvl is not null", pp);	
        	if (count==0){
        	
        		int packid = getNpjt().queryForInt("select id from snt.docstore where id_pak = :id_pak and groupsgn =0 ", pp);
        		pp.put("packid", packid);
        		getNpjt().update("update snt.docstore set groupsgn = 1, ldate = current date, ltime = current time, lpersid = (select lpersid from snt.docstore where id =:id) where id =:packid", pp);
        		
        		long id_pack = Long.parseLong(result.get(i).getId_pak());
        		boolean b = checkPackage(id_pack);
        		
        		if (b) {
        			try {
        			HashMap<String, Object> values = parsePack(id_pack);
         			insertToPackReport(values);
        			} catch (Exception e) {
        				log.error(TypeConverter.exceptionToString(e));
        			}

        		}
        		
        		//вызов метода с инсертом в таблицу с новым отчетом
        		
        		SendOpenPackToEtd(packid, false, 2);
        		
        		
        		if(!formname.equals("Счет") && !formname.equals("Расшифровка") && !formname.equals("ФПУ-26 АСР")) {
        			//заполнение отчет п.7
            		String resultSelect = "select ldate, ltime, etdid from snt.docstore where id = :packid";
            		SqlRowSet rs = npjt.queryForRowSet(resultSelect, pp);
            		Date ldate = null;
            		Time ltime = null;
            		Long ETDId = null;
            		while(rs.next()) {
            			ldate = rs.getDate("ldate");
            			ltime = rs.getTime("ltime");
            			ETDId = rs.getLong("ETDID");
            		}       	
                    SimpleDateFormat myDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                    String ldt = myDateFormat.format(ldate);
                    myDateFormat.applyPattern("yyyy-MM-dd HH:mm:ss");
                	Date DT_sign_pak = myDateFormat.parse(ldt + " " + ltime);
                	pp.put("DT_sign_pak", DT_sign_pak);
                	pp.put("id_pak", ETDId);
                	npjt.update("update snt.PGKREPORT set DT_sign_pak = :DT_sign_pak, wait_sign_pak = TIMESTAMPDIFF(8,CHAR(CAST(:DT_sign_pak AS TIMESTAMP) - DT_create_pak)) "
            						+ "where id_pak = :id_pak", pp);	
        		}
        	}
    		} catch (Exception e){
    			log.error(TypeConverter.exceptionToString(e));
 		
    		}
    	}
    }
    
    private boolean checkPackage (long idPack) {

    	HashMap<String, Object> params = new HashMap<String, Object>();
    	params.put("idpack", idPack);

    	Integer repDateCheck = getNpjt().queryForInt("select count(repdate) from snt.docstore where typeid = (select id from snt.doctype where name = 'Пакет документов') and id_pak =:idpack", params);
    	if (repDateCheck == null || repDateCheck < 1) return false;
    	
    	String DI = getNpjt().queryForObject("select DI from snt.docstore where typeid = (select id from snt.doctype where name = 'Пакет документов') and id_pak =:idpack", params, String.class);
    	long fpu26 = getNpjt().queryForLong("select count(id) from snt.docstore where typeid = (select id from snt.doctype where name = 'ФПУ-26') and id_pak =:idpack", params);
    	long sf = getNpjt().queryForLong("select count(id) from snt.docstore where typeid = (select id from snt.doctype where name = 'Счет-фактура') and id_pak =:idpack", params);

    	if (fpu26 > 0 && sf == 0 && DI.equals("02")) return true;

    	return false;
    }
    
    private DataBinder getDocument (long idPack) {
    	SyncObj	ss = getNpjt().queryForObject(getfpu26, new MapSqlParameterSource("idpack", String.valueOf(idPack)), new SyncMapper());

    	ETDForm form = null;
    	try {
    	form = ETDForm.createFromArchive(ss.getBldoc());
    	} catch (Exception e) {
    		log.error(TypeConverter.exceptionToString(e));
    	}
    	DataBinder binder = form.getBinder();

       	return binder;
    }
    
   
    private HashMap<String, Object> parsePack (long idPack) {
    	HashMap<String, Object> params = new HashMap<String, Object>();
    	params.put("idpack", idPack);
    	String dateSign = getNpjt().queryForObject("select ldate from snt.docstore where typeid = (select id from snt.doctype where name = 'ФПУ-26') and id_pak=:idpack", params, String.class);
    	Long vagon = getNpjt().queryForLong("select vagnum from snt.docstore where typeid = (select id from snt.doctype where name = 'ФПУ-26') and id_pak=:idpack", params);
    	Long predId = getNpjt().queryForLong("select predid from snt.docstore where typeid = (select id from snt.doctype where name = 'Пакет документов') and id_pak=:idpack", params);
    	long onbaseid = getNpjt().queryForLong("select onbaseid from snt.docstore where typeid = (select id from snt.doctype where name = 'ФПУ-26') and id_pak=:idpack", params);	
    	params.put("onbaseid", onbaseid);
    	long id_DI = getNpjt().queryForLong("select dorid from snt.rem_pred where kleim=:onbaseid", params);
    	params.put("id_DI", id_DI);
    	String name_pred = getNpjt().queryForObject("select name from snt.rem_pred where kleim=:onbaseid", params, String.class);
    	String name_DI = getNpjt().queryForObject("select name from snt.dor where id=:id_DI", params, String.class);
    	String dateRep = getNpjt().queryForObject("select repdate from snt.docstore where typeid = (select id from snt.doctype where name = 'Пакет документов') and id_pak =:idpack", params, String.class);
    	dateRep = dateToFormat(dateRep);
     	HashMap<String, Object> values = new HashMap<String, Object>();
    	try {
			DataBinder binder = getDocument (idPack);
			values.put("idAct", binder.getValue("P_7"));
    		values.put("cost", binder.getValue("P_24"));
    		values.put("summ", binder.getValue("P_31"));

			binder.setRootElement("table1");
			NodeList nodelist = binder.getNodes("row");
			StringBuilder serviceNameBuild = new StringBuilder();
			for(int i = 0; i < nodelist.getLength(); i++) {
				Element el = (Element) nodelist.item(i);
				String serv = el.getElementsByTagName("P_13").item(0).getTextContent();
				serviceNameBuild.append(serv);
				if(i == nodelist.getLength() -1) break;
				serviceNameBuild.append(",");
			}
    		values.put("serviceName", serviceNameBuild.toString());
    		
		} catch (InternalException e) {
			log.error(TypeConverter.exceptionToString(e));
		}
    	values.put("dateRep", dateRep);
    	values.put("dateSign", dateSign);
    	values.put("vagon", vagon.toString());
    	values.put("id_DI", id_DI);
    	values.put("name_pred", name_pred);
    	values.put("name_DI", name_DI.trim());
    	values.put("idPack", idPack);
    	values.put("predId", predId);
    	
    	return values;
    }
    
    private void insertToPackReport (HashMap<String, Object> values) {
    	try{
    	String SQL = "insert into snt.archivepackreport (id_di, id_pack, vagon, " +
    	"name_di, name_pred, id_act, repdate, signdate, servicename, cost, summ, predid) values " +
    			"(:id_DI, :idPack, :vagon, :name_DI, :name_pred, :idAct, :dateRep, " +
    	":dateSign, :serviceName, :cost, :summ, :predId)";
    	getNpjt().update(SQL, values);
    	} catch (Exception e) {
    		log.error(TypeConverter.exceptionToString(e));
    	}
    	}
    
     public void PackDrop (long id, String formname){
    	HashMap<String, Object> pp = new HashMap<String, Object>();
    	pp.put("id", id);
    	pp.put("packname", "Пакет документов");
    	
    	List drop = getNpjt().queryForList("select droptxt, dropid, droppredid, dropwrkid, id_pak from snt.docstore where id = :id", pp);
    	
    	HashMap zz= (HashMap) drop.get(0);
    	
    	pp.put("droptxt", "Отклонен документ ".concat(formname).concat(". ").concat(zz.get("DROPTXT").toString()));
    	pp.put("dropid", Integer.parseInt(zz.get("DROPID").toString()));
    	pp.put("droppredid", Integer.parseInt(zz.get("DROPPREDID").toString()));
    	pp.put("dropwrkid", Integer.parseInt(zz.get("DROPWRKID").toString()));
    	pp.put("id_pak", zz.get("ID_PAK").toString());
    	
    	try{
    		getNpjt().update("update snt.docstore set droptxt=:droptxt, dropid=:dropid, " +
    				"droppredid=:droppredid, dropwrkid=:dropwrkid, droptime = current timestamp where etdid = :id_pak ", pp);
    	} catch(Exception e){
    		log.error(TypeConverter.exceptionToString(e));
    	}
    	
    }
    
    public String dateToFormat (String repDate) {
    	SimpleDateFormat pre_sdf = new SimpleDateFormat("dd.MM.yyyy");
    	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    	Date date = null;
		try {
			date = pre_sdf.parse(repDate);
		} catch (ParseException e) {
    		log.error(TypeConverter.exceptionToString(e));
		}
    	String dateRep = sdf.format(date);
    	return dateRep;
    }
    
    public boolean SendOpenPackToEtd(long id, boolean open, int countsgns) throws UnsupportedEncodingException, ServiceException, IOException, TransformerException{
    	SimpleDateFormat actionSDF = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss");
    	boolean success = false;
    	
    	XmlOptions options = new XmlOptions();
    	options.setValidateOnSet();
    	
    	Object r= null;
    	
    	SynchronizeXfdlDocRequestDocument pd = SynchronizeXfdlDocRequestDocument.Factory.newInstance();
    	SynchronizeXfdlDocRequest req = pd.addNewSynchronizeXfdlDocRequest();
    		
    		Map hm = new HashMap();
    		hm.put("id", id);
    		String bl = "";
    		StringBuffer sb = new StringBuffer();
    		SyncObj ss = null;
    		ss = (SyncObj) getNpjt().queryForObject(getreqv, hm, new DocMapper());
    		
    		String formname = getNpjt().queryForObject("select rtrim(name) from snt.doctype where id = "
    				+ "(select typeid from snt.docstore where id = :id)", hm, String.class);
//    		String docdata = (String) getNpjt().queryForObject("select bldoc from snt.docstore where id = :id", hm, String.class);
    		
    		String docdata = new String(ss.getBldoc(), "UTF-8");
    		
    		
    		try{
    	 		if (docdata.indexOf("application/vnd.xfdl;")>-1){
    			sb.append(docdata);
    		}else {
    			sb.append("application/vnd.xfdl;content-encoding=\"base64-gzip\"\n");
    			sb.append(docdata);
    		}
    		
    		bl = sb.toString();
    		
    		
    		} catch (Exception e){
    			log.error(TypeConverter.exceptionToString(e));
    		
    		}
    		
    		req.setAction(Action.SIGNING);
    		
    		req.addNewBlobDoc();
    		req.getBlobDoc().setStringValue(bl);
    		req.getBlobDoc().setEncoding(EncodingBlob.BASE_64_GZIP_HEADER);
    		req.setDestSystem(NameSystem.ASETD);
    		req.setSrcSystem(NameSystem.PORTAL);
//    		req.setEtdDocId(getNpjt().queryForInt("select etdid from snt.docstore where id = :id", hm));
    		req.setEtdDocId(ss.getEtdid());
    		req.setFormName(formname);
    		req.setIsEditForm(false);
    	
    		req.setCountSignatures(countsgns);
    		req.addNewSecurity();
    		 req.getSecurity().setByteArrayValue(("test").getBytes("UTF-8"));
    		 req.getSecurity().setTypeSecurity("test");
    		
    			Param timestamp =  req.addNewParams().addNewParam();
    			timestamp.setName("_actionTimestamp");
    			timestamp.setStringValue(actionSDF.format(System.currentTimeMillis()));
    			
    			if (open){
    			Param onlyopen = req.getParams().addNewParam();
    			onlyopen.setName("_onlyOpen");
    			onlyopen.setStringValue("true");
    			}
    			
    			if (countsgns==4){
    				Param signer = req.getParams().addNewParam();
    				signer.setName(ReservedNamesOfParams.SIGN_PERSON.toString());
    				signer.setStringValue((String) getNpjt().queryForObject("select rtrim(cast(rtrim(fname) || ' ' || rtrim(mname) || ' ' || rtrim(lname) as char(240) ccsid unicode)) "+
    	"from SNT.personall where id = (select lpersid from snt.docstore where id = :id)", hm, String.class));
    			}
    			
    		try{
//    		System.out.println(req);
    			req.validate();
    		r = wst.marshalSendAndReceive(etdurldao.getURL(), req);
    		success = true;
    		}catch (Exception e){
    			log.error(TypeConverter.exceptionToString(e));
    			   success = false;
    		}
    		
    		log.debug("Message for docid = "+id+" sent to AS ETD");
			return success;
    	
    	
        }
    
    
    public void updateVisible(long docid){
    	
    	HashMap<String, Object> pp = new HashMap<String, Object>();
    	pp.put("id", docid);
    	getNpjt().update(updatevisible, pp);
    }
    
    
    public void fakesignpack(long docid, int userid, int wrkid, int predid, byte[] blob, String docdata){
    	try{
    	callSignProcedure(docid, userid, wrkid, predid, new byte[1]);
    	
    	HashMap<String, Object> pp = new HashMap<String, Object>();
    	pp.put("id", docid);
    	pp.put("bldoc", blob);
    	pp.put("docdata", docdata);
    	getNpjt().update(updtblob, pp);
    	} catch (Exception e){
    		log.error(TypeConverter.exceptionToString(e));
    	}
    }
    
    
    
    private Map callSignProcedure(long id,int userid,int wrkid, int predid, byte[] tsp){	
	 MyStoredProc sproc = new MyStoredProc(getDs());
	sproc.setSql("SNT.SignDoc");
	sproc.declareParameter(new SqlOutParameter("out", Types.CHAR));
	sproc.declareParameter(new SqlParameter("docid", Types.BIGINT));
	sproc.declareParameter(new SqlParameter("userid", Types.INTEGER));
	sproc.declareParameter(new SqlParameter("wrkid", Types.INTEGER));
	sproc.declareParameter(new SqlParameter("predid", Types.INTEGER));
	sproc.declareParameter(new SqlParameter("ts", Types.BLOB));
	sproc.declareParameter(new SqlOutParameter("timestamp", Types.CHAR));
	sproc.compile();
	Map input = new HashMap();
	input.put("docid", id);
	input.put("userid", userid);
	input.put("wrkid", wrkid);
	input.put("predid", predid);
	input.put("ts", tsp);
	return sproc.execute(input);
}
    
    class DocMapper implements ParameterizedRowMapper<SyncObj>
	{
		public SyncObj mapRow(ResultSet rs, int n) throws SQLException 
		{
			SyncObj doc = new SyncObj();
			doc.setBldoc(rs.getBytes("BLDOC"));
			doc.setEtdid(rs.getInt("ETDID"));
			
			return doc;
		}

	}
    
    class SyncMapper implements ParameterizedRowMapper<SyncObj>
	{
		public SyncObj mapRow(ResultSet rs, int n) throws SQLException 
		{
			SyncObj doc = new SyncObj();
			doc.setBldoc(rs.getBytes("BLDOC"));
						
			return doc;
		}

	}

    
    
}

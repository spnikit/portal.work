package sheff.rjd.services.transoil;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.log4j.Logger;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.ParameterizedRowMapper;
import org.springframework.ws.client.core.WebServiceTemplate;

import ru.aisa.rgd.utils.Base64;
import ru.aisa.rgd.ws.documents.ETDForm;
import ru.aisa.rgd.ws.domain.SFinvoice;
import ru.aisa.rgd.ws.exeption.InternalException;
import ru.aisa.rgd.ws.utility.DataBinder;
import ru.aisa.rgd.ws.utility.TypeConverter;
import ru.aisa.transoildocumentsender.TransoilDocumentSender;
import ru.iit.sFxmltotrans.SFXMLtype;
import ru.iit.sFxmltotrans.SFxmlDocument;
import ru.iit.sFxmltotrans.SFxmlDocument.SFxml;
import ru.iit.sFxmltotrans.SFxmlRequest;
import ru.iit.sFxmltotrans.SFxmlRequest.Xmltable;
import ru.iit.sFxmltotrans.XMLtype;
import sapComDocumentSapRfcFunctions.ZWSAARCHIVEDocument;
import sapComDocumentSapRfcFunctions.ZWSAARCHIVEDocument.ZWSAARCHIVE;
import sapComDocumentSapRfcFunctions.ZWSADOCUMENTSDocument;
import sapComDocumentSapRfcFunctions.ZWSADOCUMENTSDocument.ZWSADOCUMENTS;
import sapComDocumentSapRfcFunctions.ZWSASIGNREQUESTDocument;
import sapComDocumentSapRfcFunctions.ZWSASIGNREQUESTDocument.ZWSASIGNREQUEST;
import sapComDocumentSapRfcFunctions.ZWSKDOCUMENTSDocument;
import sapComDocumentSapRfcFunctions.ZWSKDOCUMENTSDocument.ZWSKDOCUMENTS;
import sapComDocumentSapRfcFunctions.ZWSKSIGNREQUESTDocument;
import sapComDocumentSapRfcFunctions.ZWSKSIGNREQUESTDocument.ZWSKSIGNREQUEST;;


public class TransService {
	private NamedParameterJdbcTemplate npjt;
	private WebServiceTemplate wst;
	private WebServiceTemplate wstoil;
	private boolean send;
	private boolean sendmrm;
	SimpleDateFormat dateformat;
	private static final String trns = "ООО «Трансойл»";
	private String url;
	private String urlsf;
	private String urlmrm;
	
	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getUrlsf() {
		return urlsf;
	}

	public void setUrlsf(String urlsf) {
		this.urlsf = urlsf;
	}

	public SimpleDateFormat getDatetimeformat() {
		return datetimeformat;
	}

	public void setDatetimeformat(SimpleDateFormat datetimeformat) {
		this.datetimeformat = datetimeformat;
	}

	SimpleDateFormat timeformat;
	SimpleDateFormat datetimeformat;
	
	public SimpleDateFormat getDateformat() {
		return dateformat;
	}

	public void setDateformat(SimpleDateFormat dateformat) {
		this.dateformat = dateformat;
	}

	public SimpleDateFormat getTimeformat() {
		return timeformat;
	}

	public void setTimeformat(SimpleDateFormat timeformat) {
		this.timeformat = timeformat;
	}

	private static Logger log = Logger.getLogger(TransService.class);

	public NamedParameterJdbcTemplate getNpjt() {
		return npjt;
	}

	public void setNpjt(NamedParameterJdbcTemplate npjt) {
		this.npjt = npjt;
	}

	public WebServiceTemplate getWst() {
		return wst;
	}

	public void setWst(WebServiceTemplate wst) {
		this.wst = wst;
	}

	public WebServiceTemplate getWstoil() {
		return wstoil;
	}

	public void setWstoil(WebServiceTemplate wstoil) {
		this.wstoil = wstoil;
	}

	public boolean isSend() {
		return send;
	}

	public void setSend(boolean send) {
		this.send = send;
	}

	public boolean isSendmrm() {
		return sendmrm;
	}

	public void setSendmrm(boolean sendmrm) {
		this.sendmrm = sendmrm;
	}
	
	public String getUrlmrm() {
		return urlmrm;
	}

	public void setUrlmrm(String urlmrm) {
		this.urlmrm = urlmrm;
	}

	public boolean SendtoTransoil(long docid, String formname, String docdata, String bin) {
		String Packreqvisits = "select onbaseid, (select rtrim(name) vchde from snt.rem_pred where kleim = ds.onbaseid), "+
				"(select rtrim(name) di from snt.dor where id = (select dorid from snt.rem_pred where kleim = ds.onbaseid)), "+
				"case when reqnum!='' then 'Вторичный' else 'Первичный' end status from snt.docstore ds where etdid= :DOCID fetch first row only";
		String SFreqvisits  = "select onbaseid, (select rtrim(name) vchde from snt.rem_pred where kleim = ds.onbaseid), "+
				"(select rtrim(name) di from snt.dor where id = (select dorid from snt.rem_pred where kleim = ds.onbaseid)), "+
				"vagnum, id_pak as pack_id from snt.docstore ds where etdid= :DOCID fetch first row only";
		boolean flag = false;
		if (isSend()) {
			
			ZWSADOCUMENTSDocument reqdoc = ZWSADOCUMENTSDocument.Factory
					.newInstance();
			ZWSADOCUMENTS req = reqdoc.addNewZWSADOCUMENTS();
			
			//упоротый дибилизм, впадлу писать по-человечески
			
			if (formname.equals("Пакет документов")){
				String di, vchde, status;
				HashMap<String, Object> pp = new HashMap<String, Object>();
				pp.put("DOCID", docid);
			try{
					List data = npjt.queryForList(Packreqvisits, pp);
					Map aa = (HashMap) data.get(0);
					di = aa.get("DI").toString();
					vchde = aa.get("VCHDE").toString();
					status = aa.get("STATUS").toString();
					docdata = docdata.replaceAll("</data>", "<di>"+di+"</di><vchde>"+vchde+"</vchde><status>"+status+"</status></data>");
					
				}catch (Exception e){
					log.error(TypeConverter.exceptionToString(e));
				}
				
				
			}
			if (formname.equals("Счет-фактура")){
				String di, vchde, vagnum, pack_id;
				HashMap<String, Object> pp = new HashMap<String, Object>();
				pp.put("DOCID", docid);
			try{
					List data = npjt.queryForList(SFreqvisits, pp);
					Map aa = (HashMap) data.get(0);
					di = aa.get("DI").toString();
					vchde = aa.get("VCHDE").toString();
					vagnum = aa.get("VAGNUM").toString();
					pack_id = aa.get("PACK_ID").toString();
					docdata = docdata.replaceAll("</data>", "<di>"+di+"</di><vchde>"+vchde+"</vchde><vagnum>"+vagnum+"</vagnum><package_id>"+pack_id+"</package_id></data>");
					
				}catch (Exception e){
					log.error(TypeConverter.exceptionToString(e));
				}
				
				
			}
			
			req.setETDDOCID(String.valueOf(docid));
			req.setFORMNAME(formname);
			req.setXML(docdata);
			req.setBIN(bin);
			Object r = null;

//			log.debug(reqdoc);
			try {
//				System.out.println(reqdoc);
				r = wst.marshalSendAndReceive(url, reqdoc);
				flag = true;
			} catch (Exception e) {
				TypeConverter.exceptionToString(e);
				
			}
			//getNpjt().update(sql, paramMap);
//			ZWSADOCUMENTSResponseDocument respdoc = (ZWSADOCUMENTSResponseDocument) r;
			// System.out.println(respdoc);
		}
		return flag;
		
	}
	
	
	
	public boolean SendMRMtoTransoil(long docid, String formname, String docdata, String bin) {
		
		boolean flag = false;
		if (isSendmrm()) {
			
			ZWSKDOCUMENTSDocument reqdoc = ZWSKDOCUMENTSDocument.Factory
					.newInstance();
			ZWSKDOCUMENTS req = reqdoc.addNewZWSKDOCUMENTS();
			
			req.setETDDOCID(String.valueOf(docid));
			req.setFORMNAME(formname);
			req.setXML(docdata);
			req.setBIN(bin);
			Object r = null;
			
			try {
				r = wst.marshalSendAndReceive(urlmrm, reqdoc);
				flag = true;
			} catch (Exception e) {
//				e.printStackTrace();
				TypeConverter.exceptionToString(e);
				
			}
			
		}
		return flag;
		
	}
	
	@SuppressWarnings("unchecked")
	public void SendSigntoTransoil(long docid, int signcount, int drop,
			int predid) {
		
		
		
		if (isSend()) {

			String formname = "";
			String contrsql = "select rtrim(name) from snt.pred where id = :PREDID";

			String sql1 = "select id, etdid, (select rtrim(name) formname from snt.doctype where id = ds.typeid), "
					+ "(select rtrim(cast(rtrim(fname) || ' ' || rtrim(mname) || ' ' || rtrim(lname) as char(240) ccsid unicode)) signname from SNT.personall where id = ds.lpersid),"
					//+"(select rtrim(name) wrkname from snt.wrkname where id =ds.lwrkid) wrkname, "
					+" (select rtrim(name) wrkname from snt.wrkname where id in (select wrkid from snt.perswrk where pid = ds.lpersid fetch first row only)) wrkname, "
					+ "ldate, ltime, id_pak from snt.docstore ds where id = :DOCID";
			String sql2 = "select id, etdid, (select rtrim(name) formname from snt.doctype where id = ds.typeid), "
					+ "(select rtrim(cast(rtrim(fname) || ' ' || rtrim(mname) || ' ' || rtrim(lname) as char(240) ccsid unicode)) signname from SNT.personall where id = ds.dropid), "
					+"(select rtrim(name) wrkname from snt.wrkname where id =ds.dropwrkid) wrkname, "
					+ "droptime, droptxt,  id_pak from snt.docstore ds where id = :DOCID";

			
			
			ZWSASIGNREQUESTDocument reqdoc = ZWSASIGNREQUESTDocument.Factory
					.newInstance();
			ZWSASIGNREQUEST req = reqdoc.addNewZWSASIGNREQUEST();
			Map<String, Object> pp = new HashMap<String, Object>();
			pp.put("DOCID", docid);
			pp.put("PREDID", predid);

			String contrname = (String) npjt.queryForObject(contrsql, pp,
					String.class);

			if (trns.equals(contrname)) {

				int finish = 0;

				if (drop == 0) {
					finish = npjt
							.queryForInt(
									"select case when signlvl is null then 1 else 0 end from snt.docstore where id = :DOCID",
									pp);
					List data = npjt.queryForList(sql1, pp);
					Map aa = (HashMap) data.get(0);
					formname = aa.get("FORMNAME").toString();
					if (formname.equals("ТОРГ-12")){
						req.setDOCID(aa.get("ID").toString());
					}
					else {
					if(aa.get("ETDID") != null) {
						req.setDOCID(aa.get("ETDID").toString());
					} else {
						req.setDOCID(aa.get("ID").toString());
					}
					}
					req.setFINISHED(finish);
					req.setFORMNAME(formname);
					req.setFIO(aa.get("SIGNNAME").toString());
					req.setSIGNNUM(signcount);
					req.setSIGNDATE(getDateformat().format(aa.get("LDATE")));
					req.setSIGNTIME(getTimeformat().format(aa.get("LTIME")));
					if(aa.get("ID_PAK") != null) {
						req.setPACKAGEID(aa.get("ID_PAK").toString());
					}
					req.setPOST(aa.get("WRKNAME")==null?"":aa.get("WRKNAME").toString());
				}

				if (drop == 1) {
					List data = npjt.queryForList(sql2, pp);
					Map aa = (HashMap) data.get(0);
					formname = aa.get("FORMNAME").toString();
					if (formname.equals("ТОРГ-12")){
						req.setDOCID(aa.get("ID").toString());
					}
					else {
					if(aa.get("ETDID") != null) {
						req.setDOCID(aa.get("ETDID").toString());
					} else {
						req.setDOCID(aa.get("ID").toString());
					}
					}
					// req.setFINISHED(finish);
					req.setFORMNAME(formname);
					
					req.setPOSTANDNAME(aa.get("SIGNNAME").toString());
					req.setREASON(aa.get("DROPTXT").toString());
					String[] datetime = getDatetimeformat()
							.format(aa.get("DROPTIME")).toString().split(" ");
					req.setSIGNDATE(datetime[0]);
					req.setSIGNTIME(datetime[1]);
					if(aa.get("ID_PAK") != null) {
						req.setPACKAGEID(aa.get("ID_PAK").toString());
					}
					req.setPOSTREJECT(aa.get("WRKNAME")==null?"":aa.get("WRKNAME").toString());
				}

				Object r = null;
//				 System.out.println("SendSigntoTransoil: "+reqdoc);
//				System.out.println("send sign: "+formname);
				try {
					r = wst.marshalSendAndReceive(url, reqdoc);
//					System.out.println(reqdoc);

				} catch (Exception e) {
					StringWriter outError = new StringWriter();
					PrintWriter errorWriter = new PrintWriter(outError);
					e.printStackTrace(errorWriter);
					log.error(outError.toString());
//					e.printStackTrace();
				}
//				ZWSASIGNREQUESTResponseDocument respdoc = (ZWSASIGNREQUESTResponseDocument) r;

				if (finish == 1) {
					SendBlobToTransoil(docid, formname, predid);
				}

			}
		}
	}
	
	@SuppressWarnings("unchecked")
	public void refusalToSign(long docid, int predid, DataBinder binder) throws InternalException {
		if (isSendmrm()) {

			String formname = "";
			String contrsql = "select rtrim(name) from snt.pred where id = :PREDID";

			String sql2 = "select id, etdid, (select rtrim(name) formname from snt.doctype where id = ds.typeid), "
					+ "(select rtrim(cast(rtrim(fname) || ' ' || rtrim(mname) || ' ' || rtrim(lname) as char(240) ccsid unicode)) signname from SNT.personall where id = 0) "
					+ " from snt.docstore ds where id = :DOCID";

			
			ZWSKSIGNREQUESTDocument reqdoc = ZWSKSIGNREQUESTDocument.Factory
					.newInstance();
			ZWSKSIGNREQUEST req = reqdoc.addNewZWSKSIGNREQUEST();
			Map<String, Object> pp = new HashMap<String, Object>();
			pp.put("DOCID", docid);
			pp.put("PREDID", predid);

			String contrname = (String) npjt.queryForObject(contrsql, pp,
					String.class);

			if (trns.equals(contrname)) {

				List data = npjt.queryForList(sql2, pp);
				Map aa = (HashMap) data.get(0);
				formname = aa.get("FORMNAME").toString();
				if(aa.get("ETDID") != null) {
					req.setDOCID(aa.get("ETDID").toString());
				} else {
					req.setDOCID(aa.get("ID").toString());
				}
				req.setFORMNAME(formname);
				req.setPOSTANDNAME(aa.get("SIGNNAME").toString());
				req.setREASON("отказ от подписи приёмосдатчиком ГУ-23  № " + 
				binder.getValue("P_11_5") + " От " + binder.getValue("P_11_6"));
				String[] datetime = getDatetimeformat()
						.format(new Date()).split(" ");
				req.setSIGNDATE(datetime[0]);
				req.setSIGNTIME(datetime[1]);
				Object r = null;
//				 System.out.println("SendSigntoTransoil: "+reqdoc);
//				System.out.println("send sign: "+formname);
				try {
					r = wst.marshalSendAndReceive(urlmrm, reqdoc);
				} catch (Exception e) {
					StringWriter outError = new StringWriter();
					PrintWriter errorWriter = new PrintWriter(outError);
					e.printStackTrace(errorWriter);
					log.error(outError.toString());
				}

			}
		}
	}
	
	
	@SuppressWarnings("unchecked")
	public void SendSignMRMtoTransoil(long docid, int signcount, int drop,
			int predid) {

		if (isSendmrm()) {

			String formname = "";
			String contrsql = "select rtrim(name) from snt.pred where id = :PREDID";

			String sql1 = "select id, etdid, (select rtrim(name) formname from snt.doctype where id = ds.typeid), "
					+ "(select rtrim(cast(rtrim(fname) || ' ' || rtrim(mname) || ' ' || rtrim(lname) as char(240) ccsid unicode)) signname from SNT.personall where id = ds.lpersid),"
					//+"(select rtrim(name) wrkname from snt.wrkname where id =ds.lwrkid) wrkname, "
					+" (select rtrim(name) wrkname from snt.wrkname where id in (select wrkid from snt.perswrk where pid = ds.lpersid fetch first row only)) wrkname, "
					+ "ldate, ltime, id_pak from snt.docstore ds where id = :DOCID";
			String sql2 = "select id, etdid, (select rtrim(name) formname from snt.doctype where id = ds.typeid), "
					+ "(select rtrim(cast(rtrim(fname) || ' ' || rtrim(mname) || ' ' || rtrim(lname) as char(240) ccsid unicode)) signname from SNT.personall where id = ds.dropid), "
					+"(select rtrim(name) wrkname from snt.wrkname where id =ds.dropwrkid) wrkname, "
					+ "droptime, droptxt,  id_pak from snt.docstore ds where id = :DOCID";

			
			
			ZWSKSIGNREQUESTDocument reqdoc = ZWSKSIGNREQUESTDocument.Factory
					.newInstance();
			ZWSKSIGNREQUEST req = reqdoc.addNewZWSKSIGNREQUEST();
			Map<String, Object> pp = new HashMap<String, Object>();
			pp.put("DOCID", docid);
			pp.put("PREDID", predid);

			String contrname = (String) npjt.queryForObject(contrsql, pp,
					String.class);

			if (trns.equals(contrname)) {

				int finish = 0;

				if (drop == 0) {
					finish = npjt
							.queryForInt(
									"select case when signlvl is null then 1 else 0 end from snt.docstore where id = :DOCID",
									pp);
					List data = npjt.queryForList(sql1, pp);
					Map aa = (HashMap) data.get(0);
					formname = aa.get("FORMNAME").toString();
					if(aa.get("ETDID") != null) {
						req.setDOCID(aa.get("ETDID").toString());
					} else {
						req.setDOCID(aa.get("ID").toString());
					}
					req.setFINISHED(finish);
					req.setFORMNAME(formname);
					req.setFIO(aa.get("SIGNNAME").toString());
					req.setSIGNNUM(signcount);
					req.setSIGNDATE(getDateformat().format(aa.get("LDATE")));
					req.setSIGNTIME(getTimeformat().format(aa.get("LTIME")));
					if(aa.get("ID_PAK") != null) {
						req.setPACKAGEID(aa.get("ID_PAK").toString());
					}
					req.setPOST(aa.get("WRKNAME")==null?"":aa.get("WRKNAME").toString());
				}

				if (drop == 1) {
					List data = npjt.queryForList(sql2, pp);
					Map aa = (HashMap) data.get(0);
					formname = aa.get("FORMNAME").toString();
					if(aa.get("ETDID") != null) {
						req.setDOCID(aa.get("ETDID").toString());
					}else {
						req.setDOCID(aa.get("ID").toString());
					}
					// req.setFINISHED(finish);
					req.setFORMNAME(formname);
					
					req.setPOSTANDNAME(aa.get("SIGNNAME").toString());
					req.setREASON(aa.get("DROPTXT").toString());
					String[] datetime = getDatetimeformat()
							.format(aa.get("DROPTIME")).toString().split(" ");
					req.setSIGNDATE(datetime[0]);
					req.setSIGNTIME(datetime[1]);
					if(aa.get("ID_PAK") != null) {
						req.setPACKAGEID(aa.get("ID_PAK").toString());
					}
					req.setPOSTREJECT(aa.get("WRKNAME")==null?"":aa.get("WRKNAME").toString());
				}

				Object r = null;
//				 System.out.println("SendSigntoTransoil: "+reqdoc);
//				System.out.println("send sign: "+formname);
				try {
					r = wst.marshalSendAndReceive(urlmrm, reqdoc);
//					System.out.println(reqdoc);

				} catch (Exception e) {
					StringWriter outError = new StringWriter();
					PrintWriter errorWriter = new PrintWriter(outError);
					e.printStackTrace(errorWriter);
					log.error(outError.toString());
//					e.printStackTrace();
				}
//				ZWSASIGNREQUESTResponseDocument respdoc = (ZWSASIGNREQUESTResponseDocument) r;

				if (finish == 1) {
					SendBlobToTransoil(docid, formname, predid);
				}

			}
		}
	}
	
	public void SendBlobToTransoilVU36(long docid, String formname, int predid){

		String getCountTransoilSql = "select count(predid) from snt.docstore where id_pak in "
				+ "(select id_pak from snt.packages where etdid = :etdid) "
				+ "and typeid = (select id from snt.doctype where name = 'Пакет документов') "
				+ "and predid  = :transpredid";
		String getEtdidByDocidSql = "select etedid from snt.docstore where id = :docid";

		HashMap<String, Object> pp = new HashMap<String, Object>();
		pp.put("trans", trns);
		pp.put("docid", docid);
		int etdid = npjt.queryForInt(getEtdidByDocidSql, pp);
		int transpredid = npjt.queryForInt("select id from snt.pred where trim(name) = :trans", pp);
		pp.put("etdid", etdid);
		pp.put("transpredid", transpredid);

		int count=npjt.queryForInt(getCountTransoilSql, pp);

		if (count>0){
			SendBlobToTransoil(docid,  formname,  transpredid);
		}	
	}

	public void SendBlobToTransoil(long docid, String formname, int predid) {
		if (isSend()) {

			Map<String, Object> pp = new HashMap<String, Object>();
			pp.put("ID", docid);
			pp.put("PREDID", predid);
			String sqldocid = "select etdid from snt.docstore where id = :ID";
			String sqlbldoc = "select bldoc from snt.docstore where id = :ID";
			String contrsql = "select rtrim(name) from snt.pred where id = :PREDID";
			String getDocDataSql = "select docdata from snt.docstore where id = :ID";

			String contr = (String)npjt.queryForObject(contrsql, pp, String.class);
			
			
			if (trns.equals(contr)) {
				try {
//					System.out.println("send blob: "+formname);
					byte[] blob = (byte[]) npjt.queryForObject(sqlbldoc, pp, byte[].class);
					long etdid = npjt.queryForLong(sqldocid, pp);
					String docdata = npjt.queryForObject(getDocDataSql, pp, String.class);
					ZWSAARCHIVEDocument reqdoc = ZWSAARCHIVEDocument.Factory.newInstance();
					ZWSAARCHIVE req = reqdoc.addNewZWSAARCHIVE();
					req.setBLDOC(new String(blob, "UTF-8"));
					req.setETDDOCID(String.valueOf(etdid));
					req.setFORMNAME(formname);
					if("Карточка документ".equals(formname)){
						ETDForm etdFormByDocData = ETDForm.createFromDocData(docdata);
					    DataBinder binder = etdFormByDocData.getBinder();
						req.setDOCNAME(binder.getValue("P_1"));
					}else{
						req.setDOCNAME("");
					}
					/*if("Пакет документов".equals(formname) && "Трансойл".equals(contr)){
						//Отправлять все ВУ-36МО в Трансойл
					}*/
					Object r = null;
					//System.out.println("SendBlobToTransoil: "+reqdoc);
					r = wst.marshalSendAndReceive(url, reqdoc);

				} catch (Exception e) {
					StringWriter outError = new StringWriter();
					PrintWriter errorWriter = new PrintWriter(outError);
					e.printStackTrace(errorWriter);
					log.error(outError.toString());
					e.printStackTrace();
				}
			}
		}

	}
	public void SendSFNotice(long docid, int number){
		HashMap<String, Object> pred = new HashMap<String, Object>();
		pred.put("docid", docid);
		String predname = (String)getNpjt().queryForObject("select rtrim(name) from snt.pred where id = (select predid from snt.docstore where id =:docid)", pred, String.class);
		String SFreqvisits  = "select onbaseid, (select rtrim(name) vchde from snt.rem_pred where kleim = ds.onbaseid), "+
				"(select rtrim(name) di from snt.dor where id = (select dorid from snt.rem_pred where kleim = ds.onbaseid)), "+
				"vagnum, id_pak as pack_id from snt.docstore ds where id= :docid fetch first row only";
		
//		System.out.println(isSend());
//		System.out.println(predname.indexOf(trns));
		if (isSend()&&predname.indexOf(trns)>-1){
			
		try{
			
			
//		SFinvoice req = getinvoicebyid(docid);
		SFinvoice req = getexactinvoicebyid(docid, number);
		SFxmlDocument sfdoc = SFxmlDocument.Factory.newInstance();
		SFxml sf = sfdoc.addNewSFxml();
		SFxmlRequest request = sf.addNewSFxmlRequest();
		SFXMLtype sftable = request.addNewSF();
		Xmltable table = request.addNewXmltable();
		boolean send =true;

		if (req.getSf_d1()!=null&&req.getSf_s1()!=null&&number!=0){
//		send = true;
		XMLtype invoice = table.addNewRow();
		invoice.setNumber(number);
		invoice.setSign(req.getSf_s1());
		invoice.setXml(req.getSf_d1());
				
	}
		
		
		
//		if (req.getSf_d1()!=null&&req.getSf_d3()!=null){
//			send = true;
//			XMLtype invoice = table.addNewRow();
//			invoice.setNumber(1);
//			invoice.setSign(req.getSf_s1());
//			invoice.setXml(req.getSf_d1());
//			
//			invoice = table.addNewRow();
//			invoice.setNumber(3);
//			invoice.setSign(req.getSf_s3());
//			invoice.setXml(req.getSf_d3());
//			
//		}
//
//	if (req.getSf_d2()!=null){
//		XMLtype invoice = table.addNewRow();
//		invoice.setNumber(2);
//		invoice.setSign(req.getSf_s2());
//		invoice.setXml(req.getSf_d2());
//	}
//
//	if (req.getSf_d4()!=null){
//		XMLtype invoice = table.addNewRow();
//		invoice.setNumber(4);
//		invoice.setSign(req.getSf_s4());
//		invoice.setXml(req.getSf_d4());
//	}
//	if (req.getSf_d5()!=null){
//		XMLtype invoice = table.addNewRow();
//		invoice.setNumber(5);
//		invoice.setSign(req.getSf_s5());
//		invoice.setXml(req.getSf_d5());
//	}
//	if (req.getSf_d6()!=null){
//		XMLtype invoice = table.addNewRow();
//		invoice.setNumber(6);
//		invoice.setSign(req.getSf_s6());
//		invoice.setXml(req.getSf_d6());
//	}
//	if (req.getSf_d7()!=null){
//		XMLtype invoice = table.addNewRow();
//		invoice.setNumber(7);
//		invoice.setSign(req.getSf_s7());
//		invoice.setXml(req.getSf_d7());
//	}
//	if (req.getSf_d8()!=null){
//		XMLtype invoice = table.addNewRow();
//		invoice.setNumber(8);
//		invoice.setSign(req.getSf_s8());
//		invoice.setXml(req.getSf_d8());
//	}
//	
//	if (req.getUv_d1()!=null&&req.getUv_s1()!=null){
//		XMLtype invoice = table.addNewRow();
//		invoice.setNumber(9);
//		invoice.setSign(req.getUv_s1());
//		invoice.setXml(req.getUv_d1());
//	}
//	if (req.getUv_d2()!=null&&req.getUv_s2()!=null){
//		XMLtype invoice = table.addNewRow();
//		invoice.setNumber(10);
//		invoice.setSign(req.getUv_s2());
//		invoice.setXml(req.getUv_d2());
//	}
	
	if (req.getSf_fd()!=null){
		sftable.setSfsign(req.getSf_fds1());
		sftable.setSfxml(req.getSf_fd());
	}
		try{
	List data = npjt.queryForList(SFreqvisits, pred);
	Map aa = (HashMap) data.get(0);
	String di = aa.get("DI").toString();
	String vchde = aa.get("VCHDE").toString();
	String vagnum = aa.get("VAGNUM").toString();
	String pack_id = aa.get("PACK_ID").toString();
//	String etdid = aa.get("ETDID").toString();
	request.setDocdata(req.getDocdata().replaceAll("</data>", "<di>"+di+"</di><vchde>"+vchde+"</vchde><vagnum>"+vagnum+"</vagnum><package_id>"+pack_id+"</package_id></data>"));
//	request.setEtdid(Long.parseLong(etdid));
		} catch (Exception e){
			request.setDocdata(req.getDocdata());
		log.error(TypeConverter.exceptionToString(e));
		}
	
	try{
		Long etdid = npjt.queryForLong("select etdid from snt.docstore where id =:docid", pred);
		request.setEtdid(etdid);
	}	catch (Exception e){
		log.error(TypeConverter.exceptionToString(e));
	}
		
		
	
		
		Object r = null;

		try {
			if (send){
//				System.out.println(sfdoc);
			r = wst.marshalSendAndReceive(urlsf, sfdoc);
			}
//			System.out.println(r.toString());
			
		} catch (Exception e) {
			log.error(TypeConverter.exceptionToString(e));
		}
		
		} catch (Exception e){
			log.error(TypeConverter.exceptionToString(e));
		}
		
		}
	}
	private class SFWrapper implements ParameterizedRowMapper<SFinvoice> {
		public SFinvoice mapRow(ResultSet rs, int n) throws SQLException {
			SFinvoice sf = new SFinvoice();
			sf.setSf_d1(rs.getBytes("SF_D1"));
//			sf.setSf_d2(rs.getBytes("SF_D2"));
//			sf.setSf_d3(rs.getBytes("SF_D3"));
//			sf.setSf_d4(rs.getBytes("SF_D4"));
//			sf.setSf_d5(rs.getBytes("SF_D5"));
//			sf.setSf_d6(rs.getBytes("SF_D6"));
//			sf.setSf_d7(rs.getBytes("SF_D7"));
//			sf.setSf_d8(rs.getBytes("SF_D8"));
			sf.setSf_s1(rs.getBytes("SF_S1"));
//			sf.setSf_s2(rs.getBytes("SF_S2"));
//			sf.setSf_s3(rs.getBytes("SF_S3"));
//			sf.setSf_s4(rs.getBytes("SF_S4"));
//			sf.setSf_s5(rs.getBytes("SF_S5"));
//			sf.setSf_s6(rs.getBytes("SF_S6"));
//			sf.setSf_s7(rs.getBytes("SF_S7"));
//			sf.setSf_s8(rs.getBytes("SF_S8"));
//			sf.setUv_d1(rs.getBytes("UV_D1"));
//			sf.setUv_d2(rs.getBytes("UV_D2"));
//			sf.setUv_s1(rs.getBytes("UV_S1"));
//			sf.setUv_s2(rs.getBytes("UV_S2"));
			sf.setSf_fd(rs.getBytes("SF_FD"));
			sf.setSf_fds1(rs.getBytes("SF_FDS1"));
			sf.setDocdata(rs.getString("DOCDATA"));
			return sf;
		}

	}
	private SFinvoice getinvoicebyid(long docid)
			throws IncorrectResultSizeDataAccessException {
		String sfsql = "select ID, SF_D1,SF_S1, SF_D2,SF_S2,SF_D3,SF_S3,SF_D4,SF_S4,SF_D5,SF_S5,SF_D6,SF_S6,SF_D7,SF_S7, SF_D8,SF_S8, "
				 +" (select UV_D1 from snt.dfkorr where id = DFS.ID), "
				 +" (select UV_S1 from snt.dfkorr where id = DFS.ID), "
				 +" (select UV_D2 from snt.dfkorr where id = DFS.ID), "
				 +" (select UV_S2 from snt.dfkorr where id = DFS.ID), "
				 +" (select DOCDATA from snt.docstore where id = DFS.ID), "
				 +" SF_FD, SF_FDS1 from snt.DFSIGNS DFS where id = :DOCID ";
		SFinvoice req = null;

		try {
			req = (SFinvoice) getNpjt().queryForObject(
					sfsql,
					new MapSqlParameterSource("DOCID", docid),
					new SFWrapper());
			
//			System.out.println(req.getSf_d3());
			
		} catch (Exception e) {
			e.printStackTrace();
			log.error(TypeConverter.exceptionToString(e));

		}

		return req;

	}
	
	private SFinvoice getexactinvoicebyid(long docid, int number)
			throws IncorrectResultSizeDataAccessException {
		String xmlname="";
		String signname="";
		String tablename="";
		switch (number){
			case 0:	
				xmlname = "SF_D1";
				signname = "SF_S1";
				tablename = "DFSIGNS";
			break;
			case 1:	
				xmlname = "SF_D1";
				signname = "SF_S1";
				tablename = "DFSIGNS";
				break;
			case 2:	
				xmlname = "SF_D2";
				signname = "SF_S2";
				tablename = "DFSIGNS";
				break;
			case 3:	
				xmlname = "SF_D3";
				signname = "SF_S3";
				tablename = "DFSIGNS";
				break;
			case 4:	
				xmlname = "SF_D4";
				signname = "SF_S4";
				tablename = "DFSIGNS";
				break;
			case 5:	
				xmlname = "SF_D5";
				signname = "SF_S5";
				tablename = "DFSIGNS";
				break;
			case 6:	
				xmlname = "SF_D6";
				signname = "SF_S6";
				tablename = "DFSIGNS";
				break;
			case 7:	
				xmlname = "SF_D7";
				signname = "SF_S7";
				tablename = "DFSIGNS";
				break;
			case 8:	
				xmlname = "SF_D8";
				signname = "SF_S8";
				tablename = "DFSIGNS";
				break;
			case 9:	
				xmlname = "UV_D1";
				signname = "UV_S1";
				tablename = "DFKORR";
				break;
			case 10:	
				xmlname = "UV_D2";
				signname = "UV_S2";
				tablename = "DFKORR";
				break;
		}
		
		String sfsql = "select SF_FD, SF_FDS1, "
				+ "(select DOCDATA from snt.docstore where id = DFS.ID),"
				+ "(select "+xmlname+" from snt."+tablename+" where id = DFS.ID) SF_D1,"
				+ "(select "+signname+" from snt."+tablename+" where id = DFS.ID) SF_S1 "
				+ "  from snt.DFSIGNS DFS where id = :DOCID ";
		SFinvoice req = null;

		try {
			req = (SFinvoice) getNpjt().queryForObject(
					sfsql,
					new MapSqlParameterSource("DOCID", docid),
					new SFWrapper());
			
//			System.out.println(req.getSf_d3());
			
		} catch (Exception e) {
			e.printStackTrace();
			log.error(TypeConverter.exceptionToString(e));

		}

		return req;

	}
}

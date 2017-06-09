package sheff.rjd.services.syncutils;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.ws.client.core.WebServiceTemplate;

import ru.aisa.rgd.ws.utility.TypeConverter;
import ru.iit.rtksign.DocdataRequestDocument;
import ru.iit.rtksign.DocdataRequestDocument.DocdataRequest;
import ru.iit.rtksign.SignRequestDocument;
import ru.iit.rtksign.SignRequestDocument.SignRequest;


public class SendToRTKService {
	private NamedParameterJdbcTemplate npjt;
	private WebServiceTemplate wst;
	private boolean send;
	SimpleDateFormat dateformat;
	private String url;
	private String urlClient;
	private String urlMO;

	public String getUrlClient() {
		return urlClient;
	}

	public void setUrlClient(String urlClient) {
		this.urlClient = urlClient;
	}

	public String getUrlMO() {
		return urlMO;
	}

	public void setUrlMO(String urlMO) {
		this.urlMO = urlMO;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
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

	private static Logger log = Logger.getLogger(SendToRTKService.class);

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

	public boolean isSend() {
		return send;
	}

	public void setSend(boolean send) {
		this.send = send;
	}

	public void SendXML(long docid, String formname, String docdata, String bin) {
			if (isSend()) {
			DocdataRequestDocument reqdoc = DocdataRequestDocument.Factory
					.newInstance();
			DocdataRequest req = reqdoc.addNewDocdataRequest();
			
			req.setDOCID(docid);
			req.setFORMNAME(formname);
			req.setXML(docdata);
			req.setBIN(bin);
			Object r = null;

//			log.debug(reqdoc);
			try {
//				System.out.println(reqdoc);
				reqdoc.validate();
				r = wst.marshalSendAndReceive(url, reqdoc);
			} catch (Exception e) {
				log.error(TypeConverter.exceptionToString(e));
			}
//			ZWSADOCUMENTSResponseDocument respdoc = (ZWSADOCUMENTSResponseDocument) r;
			// System.out.println(respdoc);
		}

	}

	@SuppressWarnings("unchecked")
	public void SendSign(long docid, int signcount, int drop,
			int predid) {
		
		
		
		if (isSend()) {

			String formname = "";
			
			String sql1 = "select id, etdid, (select rtrim(name) formname from snt.doctype where id = ds.typeid), "
					+ "(select rtrim(cast(rtrim(fname) || ' ' || rtrim(mname) || ' ' || rtrim(lname) as char(240) ccsid unicode)) signname from SNT.personall where id = ds.lpersid),"
					//+"(select rtrim(name) wrkname from snt.wrkname where id =ds.lwrkid) wrkname, "
					+" (select rtrim(name) wrkname from snt.wrkname where id in (select wrkid from snt.perswrk where pid = ds.lpersid fetch first row only)) wrkname, "
					+ "ldate, ltime, id_pak from snt.docstore ds where id = :DOCID";
			String sql2 = "select id, etdid, (select rtrim(name) formname from snt.doctype where id = ds.typeid), "
					+ "(select rtrim(cast(rtrim(fname) || ' ' || rtrim(mname) || ' ' || rtrim(lname) as char(240) ccsid unicode)) signname from SNT.personall where id = ds.dropid), "
					+"(select rtrim(name) wrkname from snt.wrkname where id =ds.dropwrkid) wrkname, "
					+ "droptime, droptxt,  id_pak from snt.docstore ds where id = :DOCID";

			
			
			SignRequestDocument reqdoc = SignRequestDocument.Factory
					.newInstance();
			SignRequest req = reqdoc.addNewSignRequest();
			Map<String, Object> pp = new HashMap<String, Object>();
			pp.put("DOCID", docid);
			pp.put("PREDID", predid);

			
				int finish = 0;

				if (drop == 0) {
					finish = npjt
							.queryForInt(
									"select case when signlvl is null then 1 else 0 end from snt.docstore where id = :DOCID",
									pp);
					List data = npjt.queryForList(sql1, pp);
					Map aa = (HashMap) data.get(0);
					formname = aa.get("FORMNAME").toString();
					
					req.setDOCID(aa.get("ETDID").toString());
				
					req.setFINISHED(finish);
					req.setFORMNAME(formname);
					req.setFIO(aa.get("SIGNNAME").toString());
					req.setSIGNNUM(signcount);
					req.setSIGNDATE(getDateformat().format(aa.get("LDATE")));
					req.setSIGNTIME(getTimeformat().format(aa.get("LTIME")));
					req.setPACKAGEID(aa.get("ID_PAK").toString());
					req.setPOST(aa.get("WRKNAME").toString());
				}

				if (drop == 1) {
					List data = npjt.queryForList(sql2, pp);
					Map aa = (HashMap) data.get(0);
					formname = aa.get("FORMNAME").toString();
					req.setDOCID(aa.get("ETDID").toString());
					// req.setFINISHED(finish);
					req.setFORMNAME(formname);
					req.setPOSTANDNAME(aa.get("SIGNNAME").toString());
					req.setREASON(aa.get("DROPTXT").toString());
					String[] datetime = getDatetimeformat()
							.format(aa.get("DROPTIME")).toString().split(" ");
					req.setSIGNDATE(datetime[0]);
					req.setSIGNTIME(datetime[1]);
					req.setPACKAGEID(aa.get("ID_PAK").toString());
					req.setPOSTREJECT(aa.get("WRKNAME").toString());
				}

				Object r = null;
//				 System.out.println("SendSigntoTransoil: "+reqdoc);
//				System.out.println("send sign: "+formname);
				try {
					reqdoc.validate();
					r = wst.marshalSendAndReceive(url, reqdoc);

				} catch (Exception e) {
					log.error(TypeConverter.exceptionToString(e));
				}
//				ZWSASIGNREQUESTResponseDocument respdoc = (ZWSASIGNREQUESTResponseDocument) r;

			
			
		}
	}
	
	@SuppressWarnings("unchecked")
	public void SendSignMRM(long docid, int signcount, int drop, int predid, String ext) {

		if (isSend()) {
			String formname = "";
			
			String sql1 = "select id, etdid, (select rtrim(name) formname from snt.doctype where id = ds.typeid), "
					+ "(select rtrim(cast(rtrim(fname) || ' ' || rtrim(mname) || ' ' || rtrim(lname) as char(240) ccsid unicode)) signname from SNT.personall where id = ds.lpersid),"
					//+"(select rtrim(name) wrkname from snt.wrkname where id =ds.lwrkid) wrkname, "
					+" (select rtrim(name) wrkname from snt.wrkname where id in (select wrkid from snt.perswrk where pid = ds.lpersid fetch first row only)) wrkname, "
					+ "ldate, ltime, id_pak from snt.docstore ds where id = :DOCID";
			String sql2 = "select id, etdid, (select rtrim(name) formname from snt.doctype where id = ds.typeid), "
					+ "(select rtrim(cast(rtrim(fname) || ' ' || rtrim(mname) || ' ' || rtrim(lname) as char(240) ccsid unicode)) signname from SNT.personall where id = ds.dropid), "
					+"(select rtrim(name) wrkname from snt.wrkname where id =ds.dropwrkid) wrkname, "
					+ "droptime, droptxt,  id_pak from snt.docstore ds where id = :DOCID";

			
			
			SignRequestDocument reqdoc = SignRequestDocument.Factory
					.newInstance();
			SignRequest req = reqdoc.addNewSignRequest();
			Map<String, Object> pp = new HashMap<String, Object>();
			pp.put("DOCID", docid);
			pp.put("PREDID", predid);
			int finish = 0;
			if (drop == 0) {
				try {
					finish = npjt
							.queryForInt(
									"select case when signlvl is null then 1 else 0 end from snt.docstore where id = :DOCID",
									pp);
					List data = npjt.queryForList(sql1, pp);
					Map aa = (HashMap) data.get(0);
					formname = aa.get("FORMNAME").toString();

					req.setDOCID(aa.get("ID").toString());

					req.setFINISHED(finish);
					req.setFORMNAME(formname);
					req.setFIO(aa.get("SIGNNAME").toString());
					req.setSIGNNUM(signcount);
					req.setSIGNDATE(getDateformat().format(aa.get("LDATE")));
					req.setSIGNTIME(getTimeformat().format(aa.get("LTIME")));
					req.setPACKAGEID("");
					req.setPOST(aa.get("WRKNAME").toString());
					req.setEXTTEXT(ext);
					try {
						log.debug("Отправка в АСУ Маневрового оператора " + reqdoc);
						wst.marshalSendAndReceive(urlClient, reqdoc);
					} catch(Exception e) {
						log.error(TypeConverter.exceptionToString(e));
					}
					log.debug("Отправка в АСУ Клиента " + reqdoc);
					wst.marshalSendAndReceive(urlMO, reqdoc);
				} catch(Exception e) {
					log.error(TypeConverter.exceptionToString(e));
				}
			}
			if (drop == 1) {
				try {
					List data = npjt.queryForList(sql2, pp);
					Map aa = (HashMap) data.get(0);
					formname = aa.get("FORMNAME").toString();
					req.setDOCID(aa.get("ID").toString());
					req.setFORMNAME(formname);
					req.setPOSTANDNAME(aa.get("SIGNNAME").toString());
					req.setREASON(aa.get("DROPTXT").toString());
					String[] datetime = getDatetimeformat()
							.format(aa.get("DROPTIME")).toString().split(" ");
					req.setSIGNDATE(datetime[0]);
					req.setSIGNTIME(datetime[1]);
					req.setPACKAGEID("");
					req.setPOSTREJECT(aa.get("WRKNAME").toString());
					reqdoc.validate();
					try {
						log.debug("Отправка в АСУ Маневрового оператора " + reqdoc);
						wst.marshalSendAndReceive(urlClient, reqdoc);
					} catch(Exception e) {
						log.error(TypeConverter.exceptionToString(e));
					}
					log.debug("Отправка в АСУ Клиента " + reqdoc);
					wst.marshalSendAndReceive(urlMO, reqdoc);
				} catch(Exception e) {
					log.error(TypeConverter.exceptionToString(e));
				}
			}
	}
	}
}

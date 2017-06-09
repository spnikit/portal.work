package sheff.rjd.services.syncutils;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.apache.tools.ant.util.facade.FacadeTaskHelper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.ws.client.core.WebServiceTemplate;
import org.springframework.ws.soap.client.core.SoapActionCallback;

import ru.aisa.rgd.ws.utility.TypeConverter;
import ru.warecs.iit.SignRequestDocument;
import ru.warecs.iit.SignRequestDocument.SignRequest;
import ru.warecs.iit.receiveFromPortal.ReceivePortalRequestDocument;
import ru.warecs.iit.receiveFromPortal.ReceivePortalResponseDocument;
import ru.warecs.iit.receiveFromPortal.RequestType;
import ru.warecs.iit.receiveFromPortal.TypeDoc;
import sheff.rjd.services.repair.TOREK_DocumentObject;

import org.springframework.security.crypto.codec.Base64;

public class SendToVRKService {
	private NamedParameterJdbcTemplate npjt;
	private WebServiceTemplate wst_vrk;
	private boolean send;
	private boolean sendvrk;
	SimpleDateFormat dateformat;
	private String url;
	private String urlrepair;
	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getUrlrepair() {
		return urlrepair;
	}

	public void setUrlrepair(String urlrepair) {
		this.urlrepair = urlrepair;
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

	private static Logger log = Logger.getLogger(SendToVRKService.class);

	public NamedParameterJdbcTemplate getNpjt() {
		return npjt;
	}

	public void setNpjt(NamedParameterJdbcTemplate npjt) {
		this.npjt = npjt;
	}



	public WebServiceTemplate getWst_vrk() {
		return wst_vrk;
	}

	public void setWst_vrk(WebServiceTemplate wst_vrk) {
		this.wst_vrk = wst_vrk;
	}

	public boolean isSend() {
		return send;
	}

	public void setSend(boolean send) {
		this.send = send;
	}

	public boolean isSendvrk() {
		return sendvrk;
	}

	public void setSendvrk(boolean sendvrk) {
		this.sendvrk = sendvrk;
	}

	private static final String sqlsign = "select id, etdid, (select rtrim(name) formname from snt.doctype where id = ds.typeid), "
			+ "(select rtrim(cast(rtrim(fname) || ' ' || rtrim(mname) || ' ' || rtrim(lname) as char(240) ccsid unicode)) signname from SNT.personall where id = ds.lpersid),"
			+" (select rtrim(name) wrkname from snt.wrkname where id = ds.lwrkid) wrkname, "
			+ "ldate, ltime, id_pak from snt.docstore ds where id = :DOCID with ur";

	private static final String sqldecline = "select id, etdid, (select rtrim(name) formname from snt.doctype where id = ds.typeid), "
			+ "(select rtrim(cast(rtrim(fname) || ' ' || rtrim(mname) || ' ' || rtrim(lname) as char(240) ccsid unicode)) signname from SNT.personall where id = ds.dropid), "
			+"(select rtrim(name) wrkname from snt.wrkname where id =ds.dropwrkid) wrkname, "
			+ "droptime, droptxt,  id_pak from snt.docstore ds where id = :DOCID with ur";

	private static final String sqlasoup = "select id, etdid, ldate, ltime from snt.docstore where etdid = :DOCID";

	@SuppressWarnings("unchecked")
	public void SendSign(long docid, int signcount, int drop,
			int predid, String exttext, String formname) {



		if (isSend()) {
			SignRequestDocument reqdoc = SignRequestDocument.Factory
					.newInstance();
			SignRequest req = reqdoc.addNewSignRequest();
			if (formname.equals("ВУ-36М_О")){
				Map<String, Object> pp = new HashMap<String, Object>();
				pp.put("DOCID", docid);
				pp.put("PREDID", predid);
				int finish = 0;

				if (drop == 0) {
					finish = npjt
							.queryForInt(
									"select case when signlvl is null then 1 else 0 end from snt.docstore where id = :DOCID",
									pp);

					try{					
						List data = npjt.queryForList(sqlsign, pp);
						Map aa = (HashMap) data.get(0);
						req.setDOCID(docid);
						req.setFINISHED(finish);
						req.setFORMNAME(formname);
						req.setFIO(aa.get("SIGNNAME").toString());
						req.setSIGNNUM(signcount);
						req.setSIGNDATE(getDateformat().format(aa.get("LDATE")));
						req.setSIGNTIME(getTimeformat().format(aa.get("LTIME")));
						switch(signcount){
						case 1: req.setPOST(aa.get("WRKNAME").toString());
						break;
						case 2: req.setPOST("Приемщик вагонов");
						break;
						case 3: req.setPOST("Дежурный по станции");
						break;
						}

					} catch (Exception e){
						e.printStackTrace();
						log.error(TypeConverter.exceptionToString(e));
					}
				}

				if (drop == 1) {
					try{
						List data = npjt.queryForList(sqldecline, pp);
						Map aa = (HashMap) data.get(0);
						req.setDOCID(docid);
						req.setFORMNAME(formname);
						req.setPOSTANDNAME(aa.get("SIGNNAME").toString());
						req.setREASON(aa.get("DROPTXT").toString());
						String[] datetime = getDatetimeformat()
								.format(aa.get("DROPTIME")).toString().split(" ");
						req.setSIGNDATE(datetime[0]);
						req.setSIGNTIME(datetime[1]);

						switch(signcount){
						case 0: req.setPOSTREJECT(aa.get("WRKNAME").toString());
						break;
						case 1: req.setPOSTREJECT("Приемщик вагонов");
						break;
						}
					} catch (Exception e){
						log.error(TypeConverter.exceptionToString(e));
					}
				}

				if (exttext.length()>0){
					req.setEXTTEXT(exttext);
				}


			}

			if (formname.equals("Сообщения АСОУП")){
				try{
					Map<String, Object> pp = new HashMap<String, Object>();
					pp.put("DOCID", docid);
					List data = npjt.queryForList(sqlasoup, pp);
					Map aa = (HashMap) data.get(0);
					req.setDOCID(Long.valueOf(aa.get("ID").toString()));
					req.setFINISHED(0);
					req.setFORMNAME(formname);
					req.setFIO("ЭДО СПС");
					req.setPOST("ЭДО СПС");
					req.setSIGNNUM(0);
					req.setSIGNDATE(getDateformat().format(aa.get("LDATE")));
					req.setSIGNTIME(getTimeformat().format(aa.get("LTIME")));
					req.setEXTTEXT(exttext);
				} catch (Exception e){
					log.error(TypeConverter.exceptionToString(e));
				}
			}

			try {
				reqdoc.validate();
				wst_vrk.setDefaultUri(url);
				wst_vrk.marshalSendAndReceive(req, new SoapActionCallback("http://warecs.ru/IIT/Sign"));
				//				System.out.println(reqdoc);
			} catch (Exception e) {
				log.error(TypeConverter.exceptionToString(e));
			}
		}
	}

	public boolean SendDocs(int typenotif, TOREK_DocumentObject obj) throws UnsupportedEncodingException{
		boolean success= false;

		if (isSendvrk()){

			ReceivePortalRequestDocument requestdoc  = ReceivePortalRequestDocument.Factory.newInstance();
			RequestType request = requestdoc.addNewReceivePortalRequest();

			HashMap<String, Object> pp = new HashMap<String, Object>();
			switch (typenotif) {
			case 0:

				request.setDocid(obj.getPortalid());
				request.setDate(obj.getSign().getDate());
				request.setFio(obj.getSign().getFio());
				request.setSignNum(obj.getSign().getSign_num());
				request.setType(TypeDoc.Enum.forString(obj.getDocspec()));
				request.setTypeNotif(typenotif);
				request.setSign(new String(Base64.encode(obj.getSign().getBinary()), "UTF-8"));
				try{
					ReceivePortalResponseDocument wsobj= null;

					requestdoc.validate();
					wst_vrk.setDefaultUri(urlrepair);
					wsobj = (ReceivePortalResponseDocument) wst_vrk.marshalSendAndReceive(request, new SoapActionCallback("http://warecs.ru/IIT/receive_from_portal/ReceivePortal"));
					//				System.out.println(requestdoc);
					wsobj.getReceivePortalResponse().getCode();
					wsobj.getReceivePortalResponse().getDescription();

					if (wsobj.getReceivePortalResponse().getCode()==0){


						success = true;
					}else {
						success = false;
						log.error("Error send to warecs with portalid = "+obj.getPortalid()+": "+wsobj.getReceivePortalResponse().getDescription());
					}

				}catch (Exception e) {
					TypeConverter.exceptionToString(e);
				}
				break;
			case 1:
				//			throw new ServiceException("Error send to warecs", ServiceError.ERR_OK);

				for (int i=0;i<obj.getSigns().size();i++){
					request.setDocid(obj.getPortalid());
					request.setType(TypeDoc.Enum.forString(obj.getDocspec()));
					request.setTypeNotif(typenotif);
					request.setSignNum(obj.getSigns().get(i).getSign_num());
					request.setSign(new String(Base64.encode(obj.getSigns().get(i).getBinary()), "UTF-8"));
					request.setXml(new String((Base64.encode(obj.getBinary())), "UTF-8"));

					if (obj.getDocspec().equals("C04")){
						request.setTorekid(obj.getTorekid());
					}
					try {
						ReceivePortalResponseDocument wsobj= null;

						requestdoc.validate();
						wst_vrk.setDefaultUri(urlrepair);
						wsobj = (ReceivePortalResponseDocument) wst_vrk.marshalSendAndReceive(request, new SoapActionCallback("http://warecs.ru/IIT/receive_from_portal/ReceivePortal"));
						//				System.out.println(requestdoc);
						wsobj.getReceivePortalResponse().getCode();
						wsobj.getReceivePortalResponse().getDescription();

						if (wsobj.getReceivePortalResponse().getCode()==0){

							//					if (obj.getDocspec().equals("C04")){
							pp.put("wareksid", wsobj.getReceivePortalResponse().getDocid());
							pp.put("docid", obj.getPortalid());
							npjt.update("update snt.vrkids set wareksid =:wareksid where docid =:docid", pp);
							//					}

							success = true;
						}else {
							success = false;
							log.error("Error send to warecs with portalid = "+obj.getPortalid()+": "+wsobj.getReceivePortalResponse().getDescription());
						}

					} catch (Exception e) {
						success = false;
						log.error(TypeConverter.exceptionToString(e));
					}


				}

				break;

			case 2:

				request.setDocid(obj.getPortalid());
				request.setDate(obj.getDroptime());
				request.setFio(obj.getFio());
				request.setReason(obj.getReason());
				request.setType(TypeDoc.Enum.forString(obj.getDocspec()));
				request.setTypeNotif(typenotif);
				try{
					ReceivePortalResponseDocument wsobj= null;

					requestdoc.validate();
					wst_vrk.setDefaultUri(urlrepair);
					wsobj = (ReceivePortalResponseDocument) wst_vrk.marshalSendAndReceive(request, new SoapActionCallback("http://warecs.ru/IIT/receive_from_portal/ReceivePortal"));
					//				System.out.println(requestdoc);
					wsobj.getReceivePortalResponse().getCode();
					wsobj.getReceivePortalResponse().getDescription();

					if (wsobj.getReceivePortalResponse().getCode()==0){


						success = true;
					}else {
						success = false;
						log.error("Error send to warecs with portalid = "+obj.getPortalid()+": "+wsobj.getReceivePortalResponse().getDescription());
					}

				}catch (Exception e) {
					TypeConverter.exceptionToString(e);
				}
				break;

			case 3:
				
				request.setDocid(obj.getPortalid());
				request.setDate(obj.getDroptime());
				request.setFio(obj.getFio());
				request.setReason(obj.getReason());
				request.setType(TypeDoc.Enum.forString(obj.getDocspec()));
				request.setTypeNotif(typenotif);
				
				try{
					ReceivePortalResponseDocument wsobj= null;

					requestdoc.validate();
					wst_vrk.setDefaultUri(urlrepair);
					wsobj = (ReceivePortalResponseDocument) wst_vrk.marshalSendAndReceive(request, new SoapActionCallback("http://warecs.ru/IIT/receive_from_portal/ReceivePortal"));
					//				System.out.println(requestdoc);
					wsobj.getReceivePortalResponse().getCode();
					wsobj.getReceivePortalResponse().getDescription();

					if (wsobj.getReceivePortalResponse().getCode()==0){


						success = true;
					}else {
						success = false;
						log.error("Error send to warecs with portalid = "+obj.getPortalid()+": "+wsobj.getReceivePortalResponse().getDescription());
					}

				}catch (Exception e) {
					TypeConverter.exceptionToString(e);
				}	

			break;
			}
		}
		return success;




	}

	public void SendPortalSignedDocs(String docspec, String fio){

	}


}

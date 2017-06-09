package sheff.rjd.dispatcher;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.ws.client.core.WebServiceTemplate;
import org.springframework.ws.transport.context.TransportContext;
import org.springframework.ws.transport.context.TransportContextHolder;
import org.springframework.ws.transport.http.HttpServletConnection;

import ru.aisa.rgd.ws.client.ETDStandartSignification;
import ru.aisa.rgd.ws.dao.ServiceFacade;
import ru.aisa.rjd.log4j.level.NotifLevel;
import rzd8888.gvc.etd.was.etd.sign.SignRequestDocument;

import sheff.rjd.utils.IpTaker;

public class Dispatcher {

	private String sql_getsgnf = "select SM_SGNF from snt.doctype where name = :typeid with ur";
	private String sql_getcrtn = "select SM_CRTN from snt.doctype where name = :typeid with ur";
	private String sql_getdcln = "select SM_DCLN from snt.doctype where id = (select typeid from snt.docstore where id = :id) with ur";

//	private TestJMSDAO jmsdao;
	private ETDStandartSignification[] disp1array;
	private WebServiceTemplate wst;
	private ServiceFacade facade;
	private Integer maxtrial;
	
	public void setMaxtrial(Integer n)
	{
		maxtrial = n;
	}
	public Integer getMaxtrial()
	{
		return maxtrial;
	}

	private NamedParameterJdbcTemplate npjt;

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

	private static Logger logger = Logger.getLogger(Dispatcher.class);

//	public TestJMSDAO getJmsdao() {
//		return jmsdao;
//	}
//
//	public void setJmsdao(TestJMSDAO jmsdao) {
//		this.jmsdao = jmsdao;
//	}
	public ServiceFacade getFacade() {
		return facade;
	}
	public void setFacade(ServiceFacade facade) {
		this.facade = facade;
	}

	public Dispatcher() {

		
	}

	public static void WstSend(WebServiceTemplate wst, String url, Long docid, Object requestDocument) throws Exception
	{
		WstSend(wst,url,docid, requestDocument,"");
	}
	
	public static void WstSend(WebServiceTemplate wst, String url, Long docid, Object requestDocument, String msgforlog) throws Exception
	{
//		System.out.println(msgforlog);
		logger.log(NotifLevel.NOTIF,"docid = "+docid+" | "+msgforlog+" | "+"Sending on "+url+" :\r"+requestDocument);
//		System.out.println("docid = "+docid+" | "+msgforlog+" | "+"Sending on "+url+" :\r"+requestDocument);
		Object r = null;
		try
		{
			r = wst.marshalSendAndReceive(url, requestDocument);
		}
		catch(Exception e)
		{
			logger.log(NotifLevel.NOTIF,"docid = "+docid+" | "+msgforlog+" | "+"(url = "+url+") "+"Error: " + e);
//			System.out.println("docid = "+docid+" | "+msgforlog+" | "+"(url = "+url+") "+"Error: " + e);
			throw e;
		}
		logger.log(NotifLevel.NOTIF,"docid = "+docid+" | "+msgforlog+" | "+"Recieved from "+url+" : \r" + r);
//		System.out.println("docid = "+docid+" | "+msgforlog+" | "+"Recieved from "+url+" : \r" + r);
	}
	
	public void DispatchSignNotif(String doctype, long docid, String xmldata,
			int sigsnumber, int trynumber, String docurl, Integer predkod,
			String url) {
		// sending second time after 10 seconds when first fail
		if (trynumber < maxtrial+1) {
			try {
				boolean sent = false;
				if (!sent) {
					for (int i = 0; i < disp1array.length; i++) {
						if (doctype.equals(disp1array[i].getFormname())) {
							disp1array[i].dispatch(docid, xmldata, sigsnumber,
									docurl, predkod, url, trynumber);
							sent = true;
							break;
						}
					}
				}
			} catch (Exception e) {
				StringWriter outError = new StringWriter();
				PrintWriter errorWriter = new PrintWriter(outError);
				e.printStackTrace(errorWriter);
				logger.error("DispError=" + doctype + "  " + outError.toString());
//				jmsdao.DispatchSignNotif(doctype, docid, xmldata, sigsnumber,
//						++trynumber, docurl, predkod, url);
			}
		}

	}

	public void dispcrnotif(long docid, String formtype, String formnum,
			Integer predid, String url, String docurl, String msgforlog) throws Exception {
		SignRequestDocument requestDocument = SignRequestDocument.Factory
				.newInstance();
		SignRequestDocument.SignRequest req = requestDocument
				.addNewSignRequest();
		Map <String, Object> pp = new HashMap<String, Object>();
		pp.put("predid", predid);
//		System.out.println("docid   ===   "+docid);
		String urls="";
		try{
		 urls = (String) npjt.queryForObject("select url from snt.pred where id =:predid with ur", pp, String.class);
		}
		catch (Exception e){
			StringWriter outError = new StringWriter();
			PrintWriter errorWriter = new PrintWriter(outError);
			e.printStackTrace(errorWriter);
//			System.out.println("URL ERROR   +++  "+outError.toString());
		}
		req.setPredurl(urls);
		req.setDocid(docid);
		req.setFormnum(formnum);
		req.setFormtype(formtype);
		req.setPredid(predid);
		req.setUrl(docurl);
		
		Object r = null;
		try
		{
			r = wst.marshalSendAndReceive(url, requestDocument);
		}
		catch(Exception e)
		{
//			System.out.println("ERRR2   "+e);
			logger.log(NotifLevel.NOTIF,"docid = "+docid+" | "+msgforlog+" | "+"(url = "+url+") "+"Error: " + e);
			throw e;
		}
		logger.log(NotifLevel.NOTIF,"docid = "+docid+" | "+msgforlog+" | "+"Recieved from "+url+" : \r" + r);
	}

	public void dispdeclnotif(long docid, String formtype, String formnum,
			Integer predid, String url, String docurl, String reason, String PostAndName, String msgforlog) throws Exception {
		SignRequestDocument requestDocument = SignRequestDocument.Factory
				.newInstance();
			SignRequestDocument.SignRequest req = requestDocument
				.addNewSignRequest();
		String urls = getFacade().getUrlByDocumentId(predid);
		req.setPredurl(urls);
		req.setDocid(docid);
		req.setFormnum(formnum);
		req.setFormtype(formtype);
		req.setPredid(predid);
		req.setUrl(docurl);
		//req.setReason(reason);
		//req.setPostAndName(PostAndName);
		Object r = null;
		try
		{
			r = wst.marshalSendAndReceive(url, requestDocument);
		}
		catch(Exception e)
		{
			logger.log(NotifLevel.NOTIF,"docid = "+docid+" | "+msgforlog+" | "+"(url = "+url+") "+"Error: " + e);
			throw e;
		}
		logger.log(NotifLevel.NOTIF,"docid = "+docid+" | "+msgforlog+" | "+"Recieved from "+url+" : \r" + r);
	}

	public void DispatchDeclineNotif(String doctype, long docid, int trynumber,
			String docurl, Integer predkod, String url, String reason, String PostAndName, String dropdata) {
		
			try {
				boolean sent = false;
				if (!sent) {
				for (int i = 0; i < disp1array.length; i++) {
					if (doctype.equals(disp1array[i].getFormname())) {
						ETDStandartSignification ess = disp1array[i];
						dispdeclnotif(docid, ess.getFormType(), ess
								.getFormNumber(), predkod, url, docurl, reason, PostAndName, "trynumber = "+String.valueOf(trynumber));
						sent = true;
						break;
					}
				}
				}
			} catch (Exception e) {
				StringWriter outError = new StringWriter();
				PrintWriter errorWriter = new PrintWriter(outError);
				e.printStackTrace(errorWriter);
				logger.error("DispError=" + doctype + "  " + outError.toString());
//				jmsdao.DispatchDeclineNotif(doctype, docid, ++trynumber, docurl,
//				 predkod, url, reason, PostAndName, dropdata);
			
		}
	}

	public void DispatchCreateNotif(String doctype, long docid, int trynumber, String docurl, Integer predkod,
			String url) {
		if (trynumber < maxtrial+1) {
			try {
				boolean sent = false;
				if (!sent) {
				for (int i = 0; i < disp1array.length; i++) {
					if (doctype.equals(disp1array[i].getFormname())) {
						ETDStandartSignification ess = disp1array[i];
//						System.out.println("docid== "+docid+" 1=== "+ess.getFormType()+" 2=== "+ess.getFormNumber()+" predkod== "+predkod+" url=== "+url+" docurl=== "+docurl);
						dispcrnotif(docid, ess.getFormType(), ess.getFormNumber(), predkod, url, docurl,"trynumber = "+String.valueOf(trynumber));
						sent = true;
						break;
					}
				}
				}
			} catch (Exception e) {
				
				//System.out.println("<<retry except. docid = "+docid +", " +(trynumber+1));
				StringWriter outError = new StringWriter();
				PrintWriter errorWriter = new PrintWriter(outError);
				e.printStackTrace(errorWriter);
//				System.out.println("ERRRROO   "+outError.toString());
				logger.error("DispError=" + doctype + "  " + outError.toString());
//				jmsdao.DispatchCreateNotif(doctype, docid, ++trynumber, docurl,
//						predkod, url);
			}
		}
	}
	
	public void DispatchUS(String doctype, long docid, int trynumber, String docurl, Integer predkod, Integer signsNumber, String xml) {
		if (trynumber < maxtrial+1) {
			try {
				boolean sent = false;
				if (!sent) {
					for (int i = 0; i < disp1array.length; i++) {
						if (doctype.equals(disp1array[i].getFormname())) {
							ETDStandartSignification ess = disp1array[i];
							ess.dispatchUS(docid, xml, signsNumber, docurl, predkod);
							sent = true;
							break;
						}
					}
				}
			} catch (Exception e) {
				StringWriter outError = new StringWriter();
				PrintWriter errorWriter = new PrintWriter(outError);
				e.printStackTrace(errorWriter);
				logger.error("DispError=" + doctype + "  " + outError.toString());
//				jmsdao.DispatchUS(doctype, docid, ++trynumber, docurl, predkod);
			}
		}
	}

//	public List<String> GetPredsUrls()
//	{
//		List<String> l = new ArrayList<String>();
//		List<Map> lm = npjt.queryForList(
//				"select distinct url from letd.pred where not(rtrim(url) = '')",
//				new HashMap<Object, Object>());
//		for (Map map : lm) {
//			l.add((String) map.get("URL"));
//		}
//		return l;
//	}
	public boolean ShouldSendCrtn(String typeid)
	{
		Integer r = null;
		Map<String, String> m = new HashMap<String, String>();
		m.put("typeid", typeid);
		r = (Integer) npjt.queryForObject(sql_getcrtn, m, Integer.class);
		if (r == 1)
			return true;
		else
			return false;
	}
	public boolean ShouldSendDcln(Long docid)
	{
		Integer r = null;
		Map<String, Long> m = new HashMap<String, Long>();
		m.put("id", docid);
		r = (Integer) npjt.queryForObject(sql_getdcln, m, Integer.class);
//		System.out.println("ShouldSendDCL"+r);
		if (r == 1)
			return true;
		else
			return false;
	}
	public boolean ShouldSendSgnf(String typeid)
	{
		Integer r = null;
		Map<String, String> m = new HashMap<String, String>();
		m.put("typeid", typeid);
		r = (Integer) npjt.queryForObject(sql_getsgnf, m, Integer.class);
		if (r == 1)
			return true;
		else
			return false;
	}
	public Boolean GetPredsUrls(String typeid, Boolean bb[], Integer flag,
			NamedParameterJdbcTemplate npjt) {

		/*  
		 * bb - в этот массив запишется информация о том, следует ли отправлять
		 * сообщения (проверяются флаги SM_SGNF и SM_CRTN)
		 * 
		 * flag = 0: собираемся отправлять и сообщение о подписании и сообщение о создании 
		 * flag = 1: собираемся отправлять сообщение о подписании 
		 * flag = 2: собираемся отправлять сообщение о создании
		 */
		bb[0] = false;
		bb[1] = false;
		Boolean b0 = false, b1 = false;
		switch (flag) {
		case 0:
			b0 = true;
			b1 = true;
			break;
		case 1:
			b0 = true;
			break;
		case 2:
			b1 = true;
			break;
		default:
			b0 = false;
			b1 = false;
			break;
		}
		
		if (b0) {
			bb[0] = ShouldSendSgnf(typeid);
		}
		if (b1) {
			bb[1] = ShouldSendCrtn(typeid);
		}
		if ((bb[0] && b0) || (bb[1] && b1)) {

			return true;
		} else
			return false;
	}

	public void SendConterminalSignNotif(String doctype, long docid,
			String xmldata, int sigsnumber, int trynumber, String docurl,
			Integer predkod, String urls) {
		
			DispatchSignNotif(doctype, docid, xmldata, sigsnumber,
					trynumber, docurl, predkod, urls);
		
	}

	public void SendConterminalCreateNotif(String doctype, long docid,
			int trynumber, String docurl, Integer predkod,String urls) {
		
			DispatchCreateNotif(doctype, docid, trynumber, docurl,
					predkod, urls);
		
	}
	
	public void SendConterminalDeclineNotif(String doctype, long docid,
			int trynumber, String docurl, Integer predkod, String urls, String reason, String PostAndName, String dropdata) {
		
			DispatchDeclineNotif(doctype, docid, trynumber, docurl, predkod, urls, reason, PostAndName, dropdata);
		
	}
	public ETDStandartSignification[] getDisp1array() {
		return disp1array;
	}

	public void setDisp1array(ETDStandartSignification[] disp1array) {
		this.disp1array = disp1array;
	}

	public String createUrl(Long id) {
		if (id != null) {
			TransportContext context = TransportContextHolder
					.getTransportContext();
			HttpServletConnection conn = (HttpServletConnection) context
					.getConnection();
			HttpServletRequest request = conn.getHttpServletRequest();
			return "http://" + IpTaker.getUrl()
					
					+
					// return
					// "http://"+request.getServerName()+":"+request.getServerPort()+
					request.getContextPath() + "/wsetd/online?id="
					+ id.toString();
		} else {
			throw new IllegalStateException("id is null");
		}
	}

	public Integer getPredIdbyDocId(Long docid) {
		Map<String, Long> m = new HashMap<String, Long>();
		m.put("id", docid);
		Integer kod = getNpjt().queryForInt(
				"select predid from snt.docstore where id = :id", m);
		return kod;
	}

	public Integer getPredKodByPredId(Integer predid) {
		Map<String, Integer> m = new HashMap<String, Integer>();
		m.put("id", predid);
		Integer kod = getNpjt().queryForInt(
				"select kod from snt.pred where id = :id", m);
		return kod;
	}
	
	public String getDoctypeByDocId(Long id) {
		Map<String, Long> m = new HashMap<String, Long>();
		m.put("id", id);
		String type = (String)getNpjt().queryForObject(
				"select name from snt.doctype where id = (select typeid from letd.docstore where id = :id)", m, String.class);
		return type.trim();
	}
}

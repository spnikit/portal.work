package ru.aisa.rgd.etd.ws;

import java.text.SimpleDateFormat;
import java.util.HashMap;

import org.apache.log4j.Logger;
import org.apache.poi.poifs.filesystem.NPOIFSDocument;
import org.springframework.oxm.Marshaller;
import org.springframework.security.crypto.codec.Base64;

import ru.aisa.edt.Signature;
import ru.aisa.edt.VRKsetDocsRequestDocument;
import ru.aisa.edt.VRKsetDocsRequestDocument.VRKsetDocsRequest;
import ru.aisa.edt.VRKsetDocsResponseDocument;
import ru.aisa.edt.VRKsetDocsResponseDocument.VRKsetDocsResponse;
import ru.aisa.rgd.etd.dao.ETDFacade;
import ru.aisa.rgd.ws.utility.TypeConverter;
import sheff.rjd.services.repair.SignInfoObject;
import sheff.rjd.services.repair.TOREK_DocumentObject;
import sheff.rjd.services.syncutils.SendToVRKService;
import sheff.rjd.utils.TOREK_WRK_DateAppender;

public class SetVRKSignDocsDocumentsEndpoint
		extends
		ETDAbstractSecurityEndoint<VRKsetDocsRequestDocument, VRKsetDocsResponseDocument> {
	
	private static Logger log = Logger.getLogger(SetVRKSignDocsDocumentsEndpoint.class);
	private SendToVRKService signvrkservice;

	public SendToVRKService getSignvrkservice() {
		return signvrkservice;
	}

	public void setSignvrkservice(SendToVRKService signvrkservice) {
		this.signvrkservice = signvrkservice;
	}

	public SetVRKSignDocsDocumentsEndpoint(Marshaller marshaller) {
		super(marshaller);
	}

	@Override
	protected VRKsetDocsResponseDocument composeInvalidSecurityResponce(Signature signature) 
	{
		VRKsetDocsResponseDocument responseDocument = VRKsetDocsResponseDocument.Factory.newInstance();
		VRKsetDocsResponse response = responseDocument.addNewVRKsetDocsResponse();
//		response.addNewData();
		response.addNewSecurity().setLogonstatus("false");
		return responseDocument;
	}

	@Override
	protected VRKsetDocsRequestDocument convertRequest(Object obj) {
		VRKsetDocsRequestDocument requestDocument = (VRKsetDocsRequestDocument) obj;
		return requestDocument;
	}

	@Override
	protected Signature getSecurity(VRKsetDocsRequestDocument requestDocument) 
	{
		Signature s = requestDocument.getVRKsetDocsRequest().getSecurity();
		return s;
	}

	@Override
	protected VRKsetDocsResponseDocument processRequest(VRKsetDocsRequestDocument requestDocument, Signature signature, ETDFacade facade) throws Exception 
	{
		
		VRKsetDocsRequest request = requestDocument.getVRKsetDocsRequest();
		HashMap<String, Object> pp = new HashMap<String, Object>();
		VRKsetDocsResponseDocument responseDocument = VRKsetDocsResponseDocument.Factory.newInstance();
		VRKsetDocsResponse response = responseDocument.addNewVRKsetDocsResponse();
		
		String signStr = new String(Base64.encode(request.getSign()), "UTF-8");

		if (signStr.length() > 10) {
			
		response.setError(false);	
		
		pp.put("docid", request.getDocid());
		pp.put("sign", request.getStage());
		pp.put("binary", request.getSign());
		pp.put("predid", request.getPredid());
		pp.put("persid", request.getPersid());
		pp.put("wrkid", request.getWrkid());
		
		try{
		facade.getNpjt().update("insert into snt.vrkdocflow (docid, sign, dt, persid, binary, predid) values (:docid, :sign, current timestamp, :persid, :binary, :predid)", pp);
		facade.getNpjt().update("update snt.docstore set ldate = current date, ltime = current time, lpersid =:persid, lwrkid = :wrkid, signlvl =:sign where id =:docid", pp);
		facade.getNpjt().update("update snt.vrkids set dt = current timestamp where docid =:docid", pp);
		
		//-----------------Отправка подписи в ВРК
		String datetime = facade.getNpjt().queryForObject("select (cast(ldate as varchar(20)) || ' ' || cast(ltime as varchar(20))) as char from snt.docstore where id = :docid ", pp, String.class);
		
		TOREK_WRK_DateAppender dateadapter = new TOREK_WRK_DateAppender();
		String signdatetime = dateadapter.tovrk(datetime);
		String formname = facade.getNpjt().queryForObject("select rtrim(name) from snt.doctype where id = (select typeid from snt.docstore where id = :docid) ", pp, String.class);
		String fio = facade.getNpjt().queryForObject("select rtrim(cast(rtrim(fname) || ' ' || rtrim(mname) || ' ' || rtrim(lname) as char(240) ccsid unicode)) "+
				"from SNT.personall where id = :persid", pp, String.class);
		
		SignInfoObject sign = new SignInfoObject(request.getStage(), request.getSign(), fio, signdatetime);
				
		TOREK_DocumentObject obj = new TOREK_DocumentObject(formname, request.getDocid(), sign);
		
		signvrkservice.SendDocs(0, obj);
		//----------------------------------------
		
		
		if ((request.getStage())==(facade.getNpjt().queryForInt("select count(0) from snt.doctypeflow where dtid = (select typeid from snt.docstore where id =:docid) and parent is not null", pp))){
		facade.getNpjt().update("update snt.docstore set signlvl = null where id = :docid", pp);
		}
		
		//Перегон входящего С04 в архив
		try{
		if (obj.getDocspec().equals("C04")){
			facade.getNpjt().update("update snt.docstore set opisanie ='Комплект подписан' where id = :docid", pp);
			facade.getNpjt().update("update snt.docstore set signlvl = null where id = (select docid from snt.vrkids where torekid = (select TORPACKID from snt.docstore where id =:docid))", pp);
		}
		} catch (Exception e){
			log.error(TypeConverter.exceptionToString(e));
		}
		response.setSuccess(true);
		} catch (Exception e){
//			e.printStackTrace();
			log.error(TypeConverter.exceptionToString(e));
			response.setSuccess(false);
		}
		} else {
			response.setError(true);
		}
		
		response.setSecurity(signature);
		
		
		return responseDocument;
	}

	
}

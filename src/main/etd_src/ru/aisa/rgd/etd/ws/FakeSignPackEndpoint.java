package ru.aisa.rgd.etd.ws;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

import org.apache.log4j.Logger;
import org.springframework.oxm.Marshaller;

import ru.aisa.edt.FakeSignPackRequestDocument;
import ru.aisa.edt.FakeSignPackRequestDocument.FakeSignPackRequest;
import ru.aisa.edt.FakeSignPackResponseDocument;
import ru.aisa.edt.FakeSignPackResponseDocument.FakeSignPackResponse;
import ru.aisa.edt.Signature;
import ru.aisa.rgd.etd.dao.ETDFacade;
import ru.aisa.rgd.ws.documents.ETDForm;
import ru.aisa.rgd.ws.utility.DataBinder;
import ru.aisa.rgd.ws.utility.TypeConverter;
import sheff.rgd.ws.Abstract.DoAction;
import sheff.rjd.ws.OCO.AfterSign.SendToEtd;


public class FakeSignPackEndpoint
		extends
		ETDAbstractSecurityEndoint<FakeSignPackRequestDocument, FakeSignPackResponseDocument> {
	

	public FakeSignPackEndpoint(Marshaller marshaller) {
		super(marshaller);
	}
	protected final Logger	log	= Logger.getLogger(getClass());
	private static final String blobsql = "select bldoc from snt.docstore where id =:id";	
	private static final String fiosql = "select rtrim(cast(rtrim(fname) || ' ' || rtrim(mname) || ' ' || rtrim(lname) as char(240) ccsid unicode)) " +
			"from SNT.personall where id =:persid";	
	private SendToEtd sendtoetd;
//	private AfterSign afterSign;
	private DoAction formControllers;
	
//	public AfterSign getAfterSign() {
//		return afterSign;
//	}
//
//	public void setAfterSign(AfterSign afterSign) {
//		this.afterSign = afterSign;
//	}

	public SendToEtd getSendtoetd() {
		return sendtoetd;
	}

	public void setSendtoetd(SendToEtd sendtoetd) {
		this.sendtoetd = sendtoetd;
	}

	public DoAction getFormControllers() {
		return formControllers;
	}

	public void setFormControllers(DoAction formControllers) {
		this.formControllers = formControllers;
	}

	@Override
	protected FakeSignPackResponseDocument composeInvalidSecurityResponce(Signature signature) 
	{
		FakeSignPackResponseDocument responseDocument = FakeSignPackResponseDocument.Factory.newInstance();
		FakeSignPackResponse response = responseDocument.addNewFakeSignPackResponse();
		//response.setData(data)
		response.addNewSecurity().setLogonstatus("false");
		return responseDocument;
	}

	@Override
	protected FakeSignPackRequestDocument convertRequest(Object obj) {
		FakeSignPackRequestDocument requestDocument = (FakeSignPackRequestDocument) obj;
		return requestDocument;
	}

	@Override
	protected Signature getSecurity(FakeSignPackRequestDocument requestDocument) 
	{
		Signature s = requestDocument.getFakeSignPackRequest().getSecurity();
		return s;
	}

	@Override
	protected FakeSignPackResponseDocument processRequest(FakeSignPackRequestDocument requestDocument, Signature signature, ETDFacade facade) throws Exception 
	{
		
		FakeSignPackRequest request = requestDocument.getFakeSignPackRequest();
		FakeSignPackResponseDocument responseDocument = FakeSignPackResponseDocument.Factory.newInstance();
		
		FakeSignPackResponse response = responseDocument.addNewFakeSignPackResponse();
		String datetime;
		String certid = request.getCertid();
		long docid = request.getDocid();
		HashMap <String, Object> pp = new HashMap<String, Object>();
		int persid = request.getPersid();
		pp.put("id", docid);
		pp.put("persid", persid);
		String fio = (String)facade.getNpjt().queryForObject(fiosql, pp, String.class);
		Date data = new Date();
		SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
		datetime = formatter.format(data);
		
		byte[] blob = (byte[])facade.getNpjt().queryForObject(blobsql, pp, byte[].class);
		 ETDForm form=ETDForm.createFromArchive(blob);
		
		 DataBinder kinder=form.getBinder();
		
		 String typeform = kinder.getValue("formname");		 
		 
		 if (typeform.equals("Пакет документов")||typeform.equals("Пакет документов ЦСС")){
		 try{
			
		 kinder.setNodeValue("blocksign2", 1);
		 kinder.setNodeValue("time_serv2", datetime);
		 kinder.setNodeValue("FIO", fio);
		 } catch (Exception e){
			 log.error(TypeConverter.exceptionToString(e));
		 }
		pp.put("DOCTYPE", typeform);
		int truewrk = facade.getNpjt().queryForInt("select wrkid from snt.doctypeflow where wrkid in (select id from snt.wrkname where issm=1) and dtid in "+
				"(select id FROM SNT.doctype where name = :DOCTYPE)  fetch first row only", pp);
		
try{
		sendtoetd.fakesignpack(request.getDocid(), persid, truewrk, request.getPredid(), form.encodeToArchiv(), form.transform("data"));
		if(typeform.equals("Пакет документов")) {
			formControllers.doAfterSign(typeform, new String(form.encodeToArchiv(), "UTF-8"), request.getPredid(), 2, docid, certid, truewrk);
		} else if(typeform.equals("Пакет документов ЦСС")){
//			System.out.println("do after sign in fspend");
			formControllers.doAfterSign(typeform, new String(form.encodeToArchiv(), "UTF-8"), request.getPredid(), 1, docid, certid, truewrk);
		}
		response.setSuccess(true);
		
} catch (Exception e) {
	log.error(TypeConverter.exceptionToString(e));
	   response.setSuccess(false);
	   
}	

	}
		 
		 else if (typeform.equals("Пакет документов РТК")){
			 
			 kinder.setNodeValue("fio_face", fio);
			 try{
			 kinder.setNodeValue("f_portal", "");
			 } catch (Exception e){
				 log.error("no tag f_portal in Pack with id: "+request.getDocid());
			 }
			 pp.put("DOCTYPE", "Пакет документов РТК");
				int truewrk = facade.getNpjt().queryForInt("select wrkid from snt.doctypeflow where wrkid in (select id from snt.wrkname where issm=9) and dtid in "+
						"(select id FROM SNT.doctype where name = :DOCTYPE)  fetch first row only", pp);
			 
				sendtoetd.fakesignpack(request.getDocid(), persid, truewrk, request.getPredid(), form.encodeToArchiv(), form.transform("data"));
				
				formControllers.doAfterSign("Пакет документов РТК", new String(form.encodeToArchiv(), "UTF-8"), request.getPredid(), 1, docid, certid, truewrk);
				
				response.setSuccess(true);
			 
		 } 
		 
		response.setSecurity(signature);
		
		

		return responseDocument;
		
	}
	
}

package ru.aisa.rgd.etd.ws;

import org.springframework.oxm.Marshaller;

import ru.aisa.edt.PackDeclineRequestDocument;
import ru.aisa.edt.PackDeclineResponseDocument;
import ru.aisa.edt.PackDeclineResponseDocument.PackDeclineResponse;
import ru.aisa.edt.Signature;
import ru.aisa.rgd.etd.dao.ETDFacade;
import sheff.rgd.ws.Abstract.DoAction;
import sheff.rjd.ws.OCO.AfterSign.SendToEtd;

public class MotivDeclineEndpoint
		extends
		ETDAbstractSecurityEndoint<PackDeclineRequestDocument, PackDeclineResponseDocument> {
	private static final String blobsql = "select bldoc from snt.docstore where id =:id";	
	private static final String fiosql = "select rtrim(cast(rtrim(fname) || ' ' || rtrim(mname) || ' ' || rtrim(lname) as char(240) ccsid unicode)) " +
			"from SNT.personall where id =:persid";	
	private SendToEtd sendtoetd;
//	private AfterSign afterSign;	
	private DoAction formControllers;
	
	
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

public MotivDeclineEndpoint(Marshaller marshaller) {
		super(marshaller);
	}

	@Override
	protected PackDeclineResponseDocument composeInvalidSecurityResponce(Signature signature) 
	{
		PackDeclineResponseDocument responseDocument = PackDeclineResponseDocument.Factory.newInstance();
		PackDeclineResponse response = responseDocument.addNewPackDeclineResponse();
		//response.setData(data)
		response.addNewSecurity().setLogonstatus("false");
		return responseDocument;
	}

	@Override
	protected PackDeclineRequestDocument convertRequest(Object obj) {
		PackDeclineRequestDocument requestDocument = (PackDeclineRequestDocument) obj;
		return requestDocument;
	}

	@Override
	protected Signature getSecurity(PackDeclineRequestDocument requestDocument) 
	{
		Signature s = requestDocument.getPackDeclineRequest().getSecurity();
		return s;
	}

	@Override
	protected PackDeclineResponseDocument processRequest(PackDeclineRequestDocument requestDocument, Signature signature, ETDFacade facade) throws Exception 
	{
		return null;
	
}
}
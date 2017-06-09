package ru.aisa.rgd.etd.ws;

import java.math.BigInteger;

import java.util.List;

import org.apache.xmlbeans.XmlException;
import org.springframework.oxm.Marshaller;

import ru.aisa.edt.Signature;
import ru.aisa.edt.UserRequestDocument;
import ru.aisa.edt.UserResponseDocument;
import ru.aisa.edt.UserResponseDocument.UserResponse;
import ru.aisa.edt.UserResponseDocument.UserResponse.Person;
import ru.aisa.edt.UserResponseDocument.UserResponse.Person.WorkInfo;
import ru.aisa.rgd.etd.dao.ETDFacade;
import ru.aisa.rgd.etd.objects.ETDUserInformation;
import ru.aisa.rgd.etd.objects.ETDWorkInformation;



public class UserInformationEndpoint extends
		ETDAbstractSecurityUserEndpoint<UserRequestDocument, UserResponseDocument> {
	
	

	protected UserInformationEndpoint(Marshaller marshaller) {
		super(marshaller);
	}
	
	@Override
	protected UserResponseDocument composeInvalidSecurityResponce(Signature signature) 
	{
		UserResponseDocument responseDocument = UserResponseDocument.Factory.newInstance();
		UserResponse response = responseDocument.addNewUserResponse();
		response.addNewSecurity();
		response.getSecurity().setLogonstatus("false");
		//response.setSecurity(signature);
		Person person = response.addNewPerson();
		person.setDepartmentName("Нет данных");
		person.setId(-1);
		person.setName("Нет данных");
		person.addNewWorkInfo();
		return responseDocument;
	}

	@Override
	public UserRequestDocument convertRequest(Object obj) throws XmlException {
		UserRequestDocument urd = UserRequestDocument.Factory.parse(obj.toString());
		return urd;
	}

	@Override
	public Signature getSecurity(UserRequestDocument requestDocument) {
		Signature s = requestDocument.getUserRequest().getSecurity();
		return s;
	}

	@Override
	protected UserResponseDocument processRequest(UserRequestDocument requestDocument, Signature signature, ETDFacade facade) throws Exception 
	{
	
		String c = requestDocument.getUserRequest().getSecurity().getCertid();
		//System.out.println("c "+c);
		String certificateId =  new BigInteger(c,16).toString();
		ETDUserInformation ui = facade.getUserInformation(certificateId);
		UserResponseDocument responseDocument = UserResponseDocument.Factory.newInstance();
		UserResponse response = responseDocument.addNewUserResponse();
		
		response.setSecurity(signature);
		
		Person person = response.addNewPerson();
		person.setDepartmentName(ui.getDepartmentName());
		person.setId(ui.getId());
		person.setName(ui.getName());
		person.setIsNews(ui.getIsNews());
		person.setNews(ui.getNews());
		person.setAutosgn(ui.isAutosgn());
		person.setIsSgn(ui.isIssgn());
		List<ETDWorkInformation> list = ui.getWorkInfo();
		for(ETDWorkInformation work : list)
		{
			WorkInfo w = person.addNewWorkInfo();
			w.setDorId(work.getDorId());
			
//			System.out.println("TTTTTTTTTTTTT");
			w.setDorName(work.getDorName());
			w.setPredCode(work.getPredCode());
			w.setPredId(work.getPredId());
			w.setFuncRole(work.getFr());
			w.setPredName(work.getPredName());
			w.setPredRZD(work.getPredRZD());
			w.setWorkId(work.getWorkId());
			w.setWorkName(work.getWorkName());
			w.setAvailableForms(work.getAvailableForms());
			w.setHeadpredid(work.getHeadpredId());
			w.setHeadpredname(work.getHeadpredName());
			w.setExpdoc(work.isExpdoc());
			w.setPdfcheck(work.isPdfcheck());
		}
//		System.out.println(responseDocument);
		return responseDocument;
	}

}

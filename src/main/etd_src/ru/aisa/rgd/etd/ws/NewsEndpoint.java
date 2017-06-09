package ru.aisa.rgd.etd.ws;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.math.BigInteger;
import java.util.HashMap;

import org.apache.log4j.Logger;
import org.apache.xmlbeans.XmlException;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.oxm.Marshaller;

import ru.aisa.edt.Signature;
import ru.aisa.edt.UserRequestDocument;
import ru.aisa.edt.UserResponseDocument;
import ru.aisa.edt.UserResponseDocument.UserResponse;
import ru.aisa.edt.UserResponseDocument.UserResponse.Person;
import ru.aisa.rgd.etd.dao.ETDFacade;
import ru.aisa.rgd.etd.objects.ETDUserInformation;

public class NewsEndpoint extends ETDAbstractSecurityEndoint<UserRequestDocument, UserResponseDocument>{
	protected final Logger	log	= Logger.getLogger(getClass());
	protected NewsEndpoint(Marshaller marshaller) {
		super(marshaller);
	}
	
	private NamedParameterJdbcTemplate npjt;
	public NamedParameterJdbcTemplate getNpjt() {
		return npjt;
	}
	public void setNpjt(NamedParameterJdbcTemplate npjt) {
		this.npjt = npjt;
	}
	
	@Override
	protected UserResponseDocument composeInvalidSecurityResponce(Signature signature) 
	{
		UserResponseDocument responseDocument = UserResponseDocument.Factory.newInstance();
		UserResponse response = responseDocument.addNewUserResponse();
		response.addNewSecurity();
		Person person = response.addNewPerson();
		person.setIsNews(0);
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
		String certificateId =  new BigInteger(c,16).toString();
		ETDUserInformation ui = facade.getUserInformation(certificateId);
		int isnews = requestDocument.getUserRequest().getIsNews();
		UserResponseDocument responseDocument = UserResponseDocument.Factory.newInstance();
		UserResponse response = responseDocument.addNewUserResponse();
		
		if (isnews!=0){
			try {
				getNpjt().update("update snt.personall set NREAD=0 where certserial='"+certificateId+"'", new HashMap());
			}catch (Exception e) {
				StringWriter outError = new StringWriter();
				PrintWriter errorWriter = new PrintWriter(outError);
				e.printStackTrace(errorWriter);
				log.error("NEWS ERROR "+e);
			}
		}
		
		return responseDocument;
	}
}

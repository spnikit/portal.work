package ru.aisa.rgd.etd.ws;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.apache.xmlbeans.XmlException;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.oxm.Marshaller;

import ru.aisa.edt.IsSignPackageRequestDocument;
import ru.aisa.edt.IsSignPackageResponseDocument;
import ru.aisa.edt.IsSignPackageResponseDocument.IsSignPackageResponse;
import ru.aisa.edt.Signature;
import ru.aisa.rgd.etd.dao.ETDFacade;


public class IsSignPackageEndpoint extends ETDAbstractSecurityEndoint<IsSignPackageRequestDocument, IsSignPackageResponseDocument>{

	protected final Logger	log	= Logger.getLogger(getClass());
	protected IsSignPackageEndpoint(Marshaller marshaller) {
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
	protected IsSignPackageResponseDocument composeInvalidSecurityResponce(Signature signature) 
	{
		IsSignPackageResponseDocument responseDocument = IsSignPackageResponseDocument.Factory.newInstance();
		IsSignPackageResponse response = responseDocument.addNewIsSignPackageResponse();
		response.addNewSecurity().setLogonstatus("false");
		return responseDocument;
	}

	@Override
	public IsSignPackageRequestDocument convertRequest(Object obj) throws XmlException {
		IsSignPackageRequestDocument urd = IsSignPackageRequestDocument.Factory.parse(obj.toString());
		return urd;
	}

	@Override
	public Signature getSecurity(IsSignPackageRequestDocument requestDocument) {
		Signature s = requestDocument.getIsSignPackageRequest().getSecurity();
		return s;
	}
	
	@Override
	protected IsSignPackageResponseDocument processRequest(IsSignPackageRequestDocument requestDocument, Signature signature, ETDFacade facade) throws Exception 
	{
		String sqlForTorId = "select (case when id_pak is not null then 1 else 0 end) as id_pak from snt.docstore where id = :docId";
		String sql = "select (case when dropid is not null then 1 else 0 end) as dropid from snt.docstore where id = :docId ";
		String sql1 = "select count(*) from snt.docstore where dropid is not null "
				+ "and id_pak = (select id_pak from snt.docstore where id = :docId) "
				+ "and typeid = (select id from snt.doctype"
				+ " where trim(name) = :packName)";
		Long docId = requestDocument.getIsSignPackageRequest().getId();
		int dropid;
		String packName = "Пакет документов";
		Map<String, Object> sqlParam = new HashMap<String, Object>();
		sqlParam.put("docId", docId);
		sqlParam.put("packName", packName);
		Map<String, Object> sqlParam1 = new HashMap<String, Object>();
		sqlParam1.put("docId", docId);
		sqlParam1.put("packName", packName);
		int IdPakForPack = getNpjt().queryForInt(sqlForTorId,sqlParam1);
		if (IdPakForPack == 0){
			dropid = getNpjt().queryForInt(sql, sqlParam);
		}else{
			dropid = getNpjt().queryForInt(sql1, sqlParam);
		}
		boolean isSign;
		if(dropid == 0){
			isSign = true;
		}else{
			isSign = false;
		}
		IsSignPackageResponseDocument responseDocument = IsSignPackageResponseDocument.Factory.newInstance();
		IsSignPackageResponse response = responseDocument.addNewIsSignPackageResponse();
		
		response.setIsSign(isSign);
		
		return responseDocument;
	}

}


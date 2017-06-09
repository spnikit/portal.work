package ru.aisa.rgd.etd.ws;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.oxm.Marshaller;

import ru.aisa.edt.Signature;
import ru.aisa.edt.VRKgetDocsRequestDocument;
import ru.aisa.edt.VRKgetDocsRequestDocument.VRKgetDocsRequest;
import ru.aisa.edt.VRKgetDocsResponseDocument;
import ru.aisa.edt.VRKgetDocsResponseDocument.VRKgetDocsResponse;
import ru.aisa.edt.VRKgetDocsResponseDocument.VRKgetDocsResponse.Data;
import ru.aisa.rgd.etd.dao.ETDFacade;
import ru.aisa.rgd.ws.utility.TypeConverter;

public class GetVRKSignDocsDocumentsEndpoint
		extends
		ETDAbstractSecurityEndoint<VRKgetDocsRequestDocument, VRKgetDocsResponseDocument> {
	
	private static Logger log = Logger.getLogger(GetVRKSignDocsDocumentsEndpoint.class);

	public GetVRKSignDocsDocumentsEndpoint(Marshaller marshaller) {
		super(marshaller);
	}

	@Override
	protected VRKgetDocsResponseDocument composeInvalidSecurityResponce(Signature signature) 
	{
		VRKgetDocsResponseDocument responseDocument = VRKgetDocsResponseDocument.Factory.newInstance();
		VRKgetDocsResponse response = responseDocument.addNewVRKgetDocsResponse();
//		response.addNewData();
		response.addNewSecurity().setLogonstatus("false");
		return responseDocument;
	}

	@Override
	protected VRKgetDocsRequestDocument convertRequest(Object obj) {
		VRKgetDocsRequestDocument requestDocument = (VRKgetDocsRequestDocument) obj;
		return requestDocument;
	}

	@Override
	protected Signature getSecurity(VRKgetDocsRequestDocument requestDocument) 
	{
		Signature s = requestDocument.getVRKgetDocsRequest().getSecurity();
		return s;
	}

	@Override
	protected VRKgetDocsResponseDocument processRequest(VRKgetDocsRequestDocument requestDocument, Signature signature, ETDFacade facade) throws Exception 
	{
//		System.out.println(requestDocument);
		VRKgetDocsRequest request = requestDocument.getVRKgetDocsRequest();

		HashMap<String, Object> pp = new HashMap<String, Object>();
//		pp.put("packid", request.getIdPak());
//		List<Map<String, Object>> ids = facade.getNpjt().queryForList("select docid from snt.vrkids where packid =:packid", pp);
		
		VRKgetDocsResponseDocument responseDocument = VRKgetDocsResponseDocument.Factory.newInstance();
		VRKgetDocsResponse response = responseDocument.addNewVRKgetDocsResponse();
		try{
//		for (int i=0; i<ids.size(); i++){
//			List<Integer> neededsgns = getsigncount(Long.valueOf(ids.get(i).get("DOCID").toString()), facade);
			List<Integer> neededsgns = getsigncount(request.getDocid(), facade);
			for (int j=0;j<neededsgns.size();j++){
				pp.put("docid", request.getDocid());
				Data d = response.addNewData();
				d.setStage(neededsgns.get(j));
				d.setDocid(request.getDocid());
				d.setXml((byte[])facade.getNpjt().queryForObject("select bldoc from snt.docstore where id =:docid ", pp, byte[].class));
			}
			
//		}
		} catch (Exception e){
			e.printStackTrace();
			log.error(TypeConverter.exceptionToString(e));
		}
		
		response.setSecurity(signature);
		
		
//		System.out.println(responseDocument);
		return responseDocument;
	}

	private List<Integer> getsigncount(long docid, ETDFacade facade){
		List<Integer> sgnorder = new ArrayList<Integer>();
		try{
		int sgns = 0;
		HashMap<String, Object> pp = new HashMap<String, Object>();
		pp.put("docid", docid);
		int ismh3 = facade.getNpjt().queryForInt("select case when (select name from snt.doctype where id = "+
				"(select typeid from snt.docstore where id=:docid))='МХ-3' then 1 else 0 end from snt.docstore where id =:docid", pp);
		
		int actualorder = facade.getNpjt().queryForInt("select case when signlvl is null then -1 else signlvl end from snt.docstore where id = :docid", pp);
		
		switch (ismh3){
		
		case 0:
			if (actualorder>-1){
			
			pp.put("order", actualorder);
			
			if (facade.getNpjt().queryForInt("select count(0) from snt.doctypeflow where dtid = "+
					" (select typeid from snt.docstore where id=:docid) and parent is not null and order>:order "+
					" fetch first row only", pp)>0){
			int neededorder = facade.getNpjt().queryForInt("select order from snt.doctypeflow where dtid = "+
						" (select typeid from snt.docstore where id=:docid) and parent is not null and order>:order "+
						" fetch first row only", pp);
			sgnorder.add(neededorder);
			}
			}
			
			break;
		case 1:
			//Для входящих
			if (actualorder==0){
				sgnorder.add(1);
				sgnorder.add(2);
			}
			//Для исохдящих
			else {
			sgnorder.add(3);
			sgnorder.add(4);
			}
			break;
		}
		} catch (Exception e){
			e.printStackTrace();
		}
		
		return sgnorder;
						
	}
	
	
}

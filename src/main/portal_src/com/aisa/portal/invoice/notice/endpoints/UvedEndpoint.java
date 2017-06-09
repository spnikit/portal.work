package com.aisa.portal.invoice.notice.endpoints;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.oxm.Marshaller;

import ru.aisa.rgd.ws.domain.Persona;
import ru.aisa.rgd.ws.endpoint.ResponseAdapter;
import ru.aisa.rgd.ws.exeption.ServiceError;
import ru.aisa.rgd.ws.utility.TypeConverter;
import ru.iit.portal8888.portal.UvedRequestDocument;
import ru.iit.portal8888.portal.UvedResponseDocument;
import ru.iit.portal8888.portal.UvedResponseDocument.UvedResponse;

import com.aisa.portal.invoice.integration.endpoints.objects.DocumentsObj;
import com.aisa.portal.invoice.integration.facade.PortalSFFacade;
import com.aisa.portal.invoice.notice.KorrSFparser;
import com.aisa.portal.invoice.notice.SFparser;
import com.aisa.portal.invoice.notice.UvedObject;
 

public class UvedEndpoint   extends Abstract<NoticeWrapper> {

	//private NamedParameterJdbcTemplate npjt;
	
	protected UvedEndpoint(Marshaller marshaller) {
		super(marshaller);
		// TODO Auto-generated constructor stub
		 
	}

 

 
	@Override
	ResponseAdapter<NoticeWrapper> processRequestInvoice(Object arg, PortalSFFacade facade)  {
	UvedRequestDocument docreq = (UvedRequestDocument) arg;
		//NoticeRequestDocument docreq=(NoticeRequestDocument)arg;
		
	String guid  = UUID.randomUUID().toString();
	long docid=	docreq.getUvedRequest().getDocId();
	//new
	Map<String, Object> hm1 = new HashMap<String, Object>();
	hm1.put("id", docid);
	Date sendDateDocBd = facade.getIntegrationDao().getNamedParameterJdbcTemplate().queryForObject("select crdate from snt.docstore where id = :id", hm1, Date.class);
	Date sendTimeDocBd = facade.getIntegrationDao().getNamedParameterJdbcTemplate().queryForObject("select crtime from snt.docstore where id = :id", hm1, Date.class);
	/*String time = new SimpleDateFormat("HH.mm.ss").format(sendTimeDocBd);
	String date = new SimpleDateFormat("dd.MM.yyyy").format(sendDateDocBd);*/
	//end
		try{
		
			
			
		    int presonid=docreq.getUvedRequest().getPersId();
		    Persona person = facade.getPersonaById(presonid);
		    String xtype="Uved";
		    UvedObject uved = new UvedObject();
		    String reason = docreq.getUvedRequest().getReason();
		    
		   DocumentsObj invoice=facade.GetInvoice(docid);
		  uved.setEcppolfile(invoice.getSGN());
		  uved.setDuty(person.getDuty());
		
		 
		  uved.setFname(person.getFName());
		  uved.setMname(person.getMName());
		  uved.setLname(person.getLName());
		  uved.setKorr(reason);
		
		  
		  if (!facade.IsKorrSF(docid)){
			//new
			  uved.setDate(sendDateDocBd);
			  uved.setTime(sendTimeDocBd);
			//end
			  SFparser sfparser = new SFparser();
			  sfparser.ParseSF(uved, new String(invoice.getXML(), "windows-1251"), "");
		  }
		  else {
			  KorrSFparser sfparser = new KorrSFparser();
		  sfparser.ParseKorrSF(uved, new String(invoice.getXML(), "windows-1251"), "");
		  }
		  
		  byte[] Korr = uved.Generate().getBytes("windows-1251");
		  
		  facade.getIntegrationDao().insertDFKorr(docid, guid, 0, Korr);
		  
		  
		  
			NoticeWrapper wrapper=new NoticeWrapper();
			
			wrapper.setXml(Korr);
			wrapper.setStage(0);
			wrapper.setDescription("OK");
			wrapper.setCode(0);
			wrapper.setNoticeType(xtype);
			wrapper.setDescription(guid);
			return new ResponseAdapter<NoticeWrapper>(true, wrapper,
					ServiceError.ERR_OK);
		
		 
		
		}
		catch (Exception e){
			e.printStackTrace();
			log.error(TypeConverter.exceptionToString(e));
		}
		
		NoticeWrapper wrapper=new NoticeWrapper();
		wrapper.setStage(-1);
		wrapper.setDescription("OK");
		wrapper.setCode(0);
		return new ResponseAdapter<NoticeWrapper>(true, wrapper,
				ServiceError.ERR_OK);
	}

	@Override
	Object composeResponceInvoice(ResponseAdapter<NoticeWrapper> adapter) {
		UvedResponseDocument response=UvedResponseDocument.Factory.newInstance();
		UvedResponse resp = response.addNewUvedResponse();
		
		if (adapter.isStatus()){
		resp.setCode(adapter.getResponse().getCode());
		resp.setStage(adapter.getResponse().getStage());
		resp.setNoticeType(adapter.getResponse().getNoticeType());
		resp.setNotice(adapter.getResponse().getXml());
		resp.setDescription(adapter.getResponse().getDescription());
		}
		else{
			resp.setCode(adapter.getResponse().getCode());
		 
		}
		return response;
	}

	/*public NamedParameterJdbcTemplate getNpjt() {
		return npjt;
	}
	
	public void setNpjt(NamedParameterJdbcTemplate npjt) {
		this.npjt = npjt;
	}	*/
}


package com.aisa.portal.invoice.notice.endpoints;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.oxm.Marshaller;

import ru.aisa.rgd.ws.documents.StandartResponseWrapper;
import ru.aisa.rgd.ws.domain.Persona;
import ru.aisa.rgd.ws.endpoint.ResponseAdapter;
import ru.aisa.rgd.ws.exeption.ServiceError;
import ru.aisa.rgd.ws.utility.TypeConverter;
import ru.iit.portal8888.portal.NoticeRequestDocument;
import ru.iit.portal8888.portal.NoticeResponseDocument;
import ru.iit.portal8888.portal.NoticeResponseDocument.NoticeResponse;

import com.aisa.portal.invoice.integration.endpoints.objects.DocumentsObj;
import com.aisa.portal.invoice.integration.facade.PortalSFFacade;
import com.aisa.portal.invoice.notice.NoticeObject;
import com.aisa.portal.invoice.notice.UvedObject;
 

public class GetNoticeEndpoint   extends Abstract<NoticeWrapper> {

	
	//private NamedParameterJdbcTemplate npjt;
	
	protected GetNoticeEndpoint(Marshaller marshaller) {
		super(marshaller);
		// TODO Auto-generated constructor stub
		 
	}

 

 
	@Override
	ResponseAdapter<NoticeWrapper> processRequestInvoice(Object arg, PortalSFFacade facade)  {
	
		NoticeRequestDocument docreq=(NoticeRequestDocument)arg;
		long docid=	docreq.getNoticeRequest().getDocId();
		//new
		Map<String, Object> hm1 = new HashMap<String, Object>();
		hm1.put("id", docid);
		Date sendDateDocBd = facade.getIntegrationDao().getNamedParameterJdbcTemplate().queryForObject("select crdate from snt.docstore where id = :id", hm1, Date.class);
		Date sendTimeDocBd = facade.getIntegrationDao().getNamedParameterJdbcTemplate().queryForObject("select crtime from snt.docstore where id = :id", hm1, Date.class);
		//end
		try{
		    int predid=docreq.getNoticeRequest().getPredId(); 
		    int presonid=docreq.getNoticeRequest().getPersonId();
		    Persona person = facade.getPersonaById(presonid);
		    String cabinetid= facade.GetCabinetId(predid);
//		     System.out.println(cabinetid);
//		     System.out.println(docid);
		    boolean isseller= facade.IsSeller(cabinetid, docid);
		    
//		    System.out.println(isseller);
		    DocumentsObj stageMap = facade.GetInvoiceStageMap(docid);
		    boolean isdropped = facade.IsDropped(docid);
		   
//  		   System.out.println(isdropped);
//		   System.out.println(isseller);
		if (!isseller){
		if (stageMap.getSF_FVS4()==0){
			DocumentsObj invoice=facade.GetInvoice(docid);
			String xml=new String(invoice.getXML(), "windows-1251");
			String sign=invoice.getSGNBase64();
			 String xtype="";
			if (!facade.IsKorrSF(docid)){		
		    xtype="Sfact";
		    } else {
		    	xtype = "KorSfact";
		    }
			
			NoticeObject notice = facade.GenerateNotice(xtype, xml, sign);
			notice.setSignerName(person.getMName());
			notice.setSignerPatronymic(person.getLName());
			notice.setSignerSurname(person.getFName());
			//System.out.println("duty: "+person.getDuty());
			notice.setSignerPosition(person.getDuty());
		
			NoticeWrapper wrapper=new NoticeWrapper();
			wrapper.setXml(notice.Generate().getBytes("windows-1251"));
		 
			wrapper.setStage(4);
			wrapper.setDescription("OK");
			wrapper.setCode(0);
			wrapper.setNoticeType("BNI");
			return new ResponseAdapter<NoticeWrapper>(true, wrapper,
					ServiceError.ERR_OK);
		}
		 else{
			if (stageMap.getSF_FVS5()==0 && stageMap.getSF_FVS3()==1){
				DocumentsObj InvoiceConfirmation=facade.GetBuyerInvoiceConfirmation(docid);
			String xml=new String(InvoiceConfirmation.getXML(),"windows-1251");
			String sign=InvoiceConfirmation.getSGNBase64();
			  String xtype="Pdotpr";
				NoticeObject notice = facade.GenerateNotice(xtype, xml, sign);
				notice.setSignerName(person.getMName());
				notice.setSignerPatronymic(person.getLName());
				notice.setSignerSurname(person.getFName());
				notice.setSignerPosition(person.getDuty());
				notice.Generate(); 
				NoticeWrapper wrapper=new NoticeWrapper();
				wrapper.setXml(notice.Generate().getBytes("windows-1251"));
				wrapper.setStage(5);
				wrapper.setDescription("OK");
				wrapper.setCode(0);
				wrapper.setNoticeType("BNIC");
				return new ResponseAdapter<NoticeWrapper>(true, wrapper,
						ServiceError.ERR_OK);
			}
			else {
				if (stageMap.getSF_FVS8()==0 && stageMap.getSF_FVS6()==1){
					DocumentsObj InvoiceConfirmation=facade.GetBuyerConfirmation(docid);
					String xml=new String(InvoiceConfirmation.getXML(),"windows-1251"); 
					 
					String sign=InvoiceConfirmation.getSGNBase64();
					  String xtype="Pdotpr";
						NoticeObject notice = facade.GenerateNotice(xtype, xml, sign);
						notice.setSignerName(person.getMName());
						notice.setSignerPatronymic(person.getLName());
						notice.setSignerSurname(person.getFName());
						notice.setSignerPosition(person.getDuty());
						notice.Generate(); 
						NoticeWrapper wrapper=new NoticeWrapper();
						wrapper.setXml(notice.Generate().getBytes("windows-1251"));
						wrapper.setStage(8);
						wrapper.setDescription("OK");
						wrapper.setCode(0);
						wrapper.setNoticeType("BNC");
						return new ResponseAdapter<NoticeWrapper>(true, wrapper,
								ServiceError.ERR_OK);
					
				}
				else if(isdropped)
				{

					int korrStat = facade.getIntegrationDao().GetKorrStat(docid);
					
						
						if(korrStat == 0){
							String xtype="";
							if (!facade.IsKorrSF(docid)){		
								xtype="Sfact";
						    } 
							else {
						    	xtype = "KorSfact";
						    }
						DocumentsObj invoice=facade.GetInvoice(docid);
						String xml=new String(invoice.getXML(), "windows-1251");
						String sign=invoice.getSGNBase64();
						
						//UvedObject
						
						UvedObject notice = facade.GenerateUvNotice(xtype, xml, sign);//добавить uvedObject generate notice
					
						//new
						if(xtype.equals("Sfact")){
							notice.setDate(sendDateDocBd);
							notice.setTime(sendTimeDocBd);
						}
						//end
						notice.setMname(person.getMName());
						notice.setLname(person.getLName());
						notice.setFname(person.getFName());
						notice.setDuty(person.getDuty());
						
						NoticeWrapper wrapper=new NoticeWrapper();
						wrapper.setXml(notice.Generate().getBytes("windows-1251"));
						wrapper.setStage(1);
						wrapper.setDescription("OK");
						wrapper.setCode(0);
						wrapper.setNoticeType("BNUV");//покупатель высылает уведомление об уточ
						
						return new ResponseAdapter<NoticeWrapper>(true, wrapper,
								ServiceError.ERR_OK);
							
						//generate notice
								
					}//покупатель
					if(korrStat == 1)
					{
						
			if (facade.GetIsSendForKorr(docid, 2)){
						int isExt = facade.getIntegrationDao().isExterna(docid);
						if(isExt > 0)
						{
						
							DocumentsObj invoice=facade.GetBuyerUvedNotice(docid);
							String xml=new String(invoice.getXML(), "windows-1251");
							String sign=invoice.getSGNBase64();
							String xtype = "BNUV";
							NoticeObject notice = facade.GenerateNotice(xtype, xml, sign);

							notice.Generate();
							NoticeWrapper wrapper = new NoticeWrapper();
							wrapper.setXml(notice.Generate().getBytes("windows-1251"));
							wrapper.setStage(0);
							wrapper.setDescription("OK");
							wrapper.setCode(0);
							wrapper.setNoticeType("SNCUV");	
							return new ResponseAdapter<NoticeWrapper>(true, wrapper,
									ServiceError.ERR_OK);

						}
						}
					
					}
					
				}
				else {
					NoticeWrapper wrapper=new NoticeWrapper();
					wrapper.setStage(-1);
					wrapper.setDescription("OK");
					wrapper.setCode(0);
					return new ResponseAdapter<NoticeWrapper>(true, wrapper,
							ServiceError.ERR_OK);
				}
			}
		}
		
		
		
		}
		else if (isseller){
			if (stageMap.getSF_FVS2()==0){
				DocumentsObj SellerConfirmation =facade.GetSellerConfirmation(docid);
				String xml=new String(SellerConfirmation.getXML(), "windows-1251");
				String sign=SellerConfirmation.getSGNBase64();
				  String xtype="Pdpol";
					NoticeObject notice = facade.GenerateNotice(xtype, xml, sign);
					notice.setSignerName(person.getMName());
					notice.setSignerPatronymic(person.getLName());
					notice.setSignerSurname(person.getFName());
					notice.setSignerPosition(person.getDuty());
					notice.Generate(); 
					NoticeWrapper wrapper=new NoticeWrapper();
					wrapper.setXml(notice.Generate().getBytes("windows-1251"));
					wrapper.setStage(2);
					wrapper.setDescription("OK");
					wrapper.setCode(0);
					wrapper.setNoticeType("SNC");
					return new ResponseAdapter<NoticeWrapper>(true, wrapper,
							ServiceError.ERR_OK);
				
			}
			else if(isdropped){

				//для продавана 
			int korrStat = facade.getIntegrationDao().GetKorrStat(docid);
			int isExt = facade.getIntegrationDao().isExterna(docid);
			if(korrStat == 0)// 0 - нет квитанции об уточнении
			{
				if (facade.GetIsSendForKorr(docid, 1)){
				if (isExt>0){
					String xtype="";
					if (!facade.IsKorrSF(docid)){		
						xtype="Sfact";
				    } 
					else {
				    	xtype = "KorSfact";
				    }
					DocumentsObj invoice=facade.GetInvoice(docid);
					String xml=new String(invoice.getXML(), "windows-1251");
					String sign=invoice.getSGNBase64();
					UvedObject notice = facade.GenerateUvNotice(xtype, xml, sign);//добавить uvedObject generate notice
					//new
					if(xtype.equals("Sfact")){
						notice.setDate(sendDateDocBd);
						notice.setTime(sendTimeDocBd);
					}
					//end
					NoticeWrapper wrapper=new NoticeWrapper();
					wrapper.setXml(notice.Generate().getBytes("windows-1251"));
					wrapper.setStage(0);
					wrapper.setDescription("OK");
					wrapper.setCode(0);
					wrapper.setNoticeType("BNUV");//покупатель высылает уведомление об уточ
					
					return new ResponseAdapter<NoticeWrapper>(true, wrapper,
							ServiceError.ERR_OK);
						
					//generate notice
				}			
				}
				
			//generate notice
					
		}
								
		//отправляем процессором из фасада
			
			
		if(korrStat == 1) //есть квитанция об уточнении
		{
			DocumentsObj invoice=facade.GetBuyerUvedNotice(docid);
			String xml=new String(invoice.getXML(), "windows-1251");
			String sign=invoice.getSGNBase64();
			String xtype = "BNUV";
			NoticeObject notice = facade.GenerateNotice(xtype, xml, sign);
			notice.setSignerName(person.getMName());
			notice.setSignerPatronymic(person.getLName());
			notice.setSignerSurname(person.getFName());
			notice.setSignerPosition(person.getDuty());
		
			notice.Generate();
			
			NoticeWrapper wrapper = new NoticeWrapper();
			wrapper.setXml(notice.Generate().getBytes("windows-1251"));
//			System.out.println(new String(wrapper.getXml(), "windows-1251"));
			wrapper.setStage(1);
			wrapper.setDescription("OK");
			wrapper.setCode(0);
			wrapper.setNoticeType("SNCUV");	
			return new ResponseAdapter<NoticeWrapper>(true, wrapper,
					ServiceError.ERR_OK);

		}

		 
			}
			else {
				NoticeWrapper wrapper=new NoticeWrapper();
				wrapper.setStage(-1);
				wrapper.setDescription("OK");
				wrapper.setCode(0);
				return new ResponseAdapter<NoticeWrapper>(true, wrapper,
						ServiceError.ERR_OK);
			}
		}
		
		
		}
		catch (Exception e){
//			e.printStackTrace();
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
		NoticeResponseDocument response=NoticeResponseDocument.Factory.newInstance();
		NoticeResponse resp = response.addNewNoticeResponse();
		
		if (adapter.isStatus()){
		resp.setCode(adapter.getResponse().getCode());
		resp.setStage(adapter.getResponse().getStage());
		resp.setNoticeType(adapter.getResponse().getNoticeType());
		resp.setNotice(adapter.getResponse().getXml());
		
		}
		else{
			resp.setCode(adapter.getResponse().getCode());
		 
		}
		//System.out.println("response: "+response);
		return response;
	}

}

class NoticeWrapper extends StandartResponseWrapper{
	byte[] xml;
    String NoticeType;
    
	public String getNoticeType() {
		return NoticeType;
	}
	public void setNoticeType(String noticeType) {
		NoticeType = noticeType;
	}
	public byte[] getXml() {
		return xml;
	}
	public void setXml(byte[] xml) {
		this.xml = xml;
	}
	public int getStage() {
		return stage;
	}
	public void setStage(int stage) {
		this.stage = stage;
	}
	int stage;
}


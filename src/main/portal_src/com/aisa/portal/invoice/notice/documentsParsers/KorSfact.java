package com.aisa.portal.invoice.notice.documentsParsers;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

import org.apache.xmlbeans.XmlOptions;

import ru.aisa.korsfact.ФайлDocument;

import com.aisa.portal.invoice.notice.NoticeObject;

public class KorSfact extends Abstract{
	 
		public NoticeObject parse (String XML, String sign) throws Exception{
		NoticeObject noticeObj=new NoticeObject(); 
		 XmlOptions options = new XmlOptions();
		 
		 options.setLoadReplaceDocumentElement(new javax.xml.namespace.QName("http://aisa.ru/korsfact", "Файл")) ;
		 
		ФайлDocument fdocument=ФайлDocument.Factory.parse(XML,options);
//		System.out.println(fdocument);
		//noticeObj.setFileName(fdocument.getФайл().getИдФайл()+".xml");
		noticeObj.setFileName(fdocument.getФайл().getИдФайл());
		noticeObj.setFileSignature(sign);
		//noticeObj.setFileDateOfCreation(fileDateOfCreation)
		noticeObj.setSForFirst(true);
		noticeObj.setIncomingDocumentName(documentName);
		noticeObj.setIncomingDocumentNumber(fdocument.getФайл().getДокумент().getСвКСчФ().getНомерКСчФ());
		noticeObj.setFileDateOfCreation(fdocument.getФайл().getДокумент().getСвКСчФ().getДатаКСчФ());
		
		
//		noticeObj.setSenderIdEDO(fdocument.getФайл().getСвУчДокОбор().getИдПок());
		
//		System.out.println(fdocument.getФайл().getСвУчДокОбор().getИдОтпр());
		noticeObj.setSenderIdEDO(fdocument.getФайл().getСвУчДокОбор().getИдОтпр());
		
		/*INFO O OTPRAVITELE*/
		/*if (fdocument.getФайл().getДокумент().getСвКСчФ().getСвПрод().getИдСв().isSetСвЮЛ()){
			noticeObj.setTypeSender(0);
			noticeObj.setSenderINN(fdocument.getФайл().getДокумент().getСвКСчФ().getСвПрод().getИдСв().getСвЮЛ().getИННЮЛ());
			noticeObj.setSenderKPP(fdocument.getФайл().getДокумент().getСвКСчФ().getСвПрод().getИдСв().getСвЮЛ().getКПП());
			noticeObj.setSenderCompanyName(fdocument.getФайл().getДокумент().getСвКСчФ().getСвПрод().getИдСв().getСвЮЛ().getНаимОрг());
		}
		else{
			noticeObj.setTypeSender(1);
			noticeObj.setSenderINN(fdocument.getФайл().getДокумент().getСвКСчФ().getСвПрод().getИдСв().getСвФЛ().getИННФЛ());
			noticeObj.setIPSenderName(fdocument.getФайл().getДокумент().getСвКСчФ().getСвПрод().getИдСв().getСвФЛ().getФИОИП().getИмя());
			
			if (fdocument.getФайл().getДокумент().getСвКСчФ().getСвПрод().getИдСв().getСвФЛ().getФИОИП().isSetОтчество())
			noticeObj.setIPSenderPatronymic(fdocument.getФайл().getДокумент().getСвКСчФ().getСвПрод().getИдСв().getСвФЛ().getФИОИП().getОтчество());
			noticeObj.setIPSenderSurname(fdocument.getФайл().getДокумент().getСвКСчФ().getСвПрод().getИдСв().getСвФЛ().getФИОИП().getФамилия());
			
		}*/
		/*INFO O OTPRAVITELE*/
		
		/*INFO O POLYCHATELE*/
		
		if (fdocument.getФайл().getДокумент().getСвКСчФ().getСвПокуп().getИдСв().isSetСвЮЛ()){
			noticeObj.setTypeSender(0);
//			noticeObj.setSenderINN(fdocument.getФайл().getДокумент().getСвКСчФ().getСвПокуп().getИдСв().getСвЮЛ().getИННЮЛ());
//			noticeObj.setSenderKPP(fdocument.getФайл().getДокумент().getСвКСчФ().getСвПокуп().getИдСв().getСвЮЛ().getКПП());
//			noticeObj.setSenderCompanyName(fdocument.getФайл().getДокумент().getСвКСчФ().getСвПокуп().getИдСв().getСвЮЛ().getНаимОрг());
			
			noticeObj.setSenderINN(fdocument.getФайл().getДокумент().getСвКСчФ().getСвПрод().getИдСв().getСвЮЛ().getИННЮЛ());
			noticeObj.setSenderKPP(fdocument.getФайл().getДокумент().getСвКСчФ().getСвПрод().getИдСв().getСвЮЛ().getКПП());
			noticeObj.setSenderCompanyName(fdocument.getФайл().getДокумент().getСвКСчФ().getСвПрод().getИдСв().getСвЮЛ().getНаимОрг());
			
			
			
			noticeObj.setCompanyName(fdocument.getФайл().getДокумент().getСвКСчФ().getСвПокуп().getИдСв().getСвЮЛ().getНаимОрг());
			noticeObj.setINN(fdocument.getФайл().getДокумент().getСвКСчФ().getСвПокуп().getИдСв().getСвЮЛ().getИННЮЛ());
			noticeObj.setKPP(fdocument.getФайл().getДокумент().getСвКСчФ().getСвПокуп().getИдСв().getСвЮЛ().getКПП());
			noticeObj.setСorporation(true);
		}else{
			
			noticeObj.setTypeSender(1);
			noticeObj.setSenderINN(fdocument.getФайл().getДокумент().getСвКСчФ().getСвПокуп().getИдСв().getСвФЛ().getИННФЛ());
			noticeObj.setIPSenderName(fdocument.getФайл().getДокумент().getСвКСчФ().getСвПокуп().getИдСв().getСвФЛ().getФИОИП().getИмя());
			
			noticeObj.setСorporation(false); 
			
			noticeObj.setINN(fdocument.getФайл().getДокумент().getСвКСчФ().getСвПокуп().getИдСв().getСвФЛ().getИННФЛ());
			noticeObj.setIPName(fdocument.getФайл().getДокумент().getСвКСчФ().getСвПокуп().getИдСв().getСвФЛ().getФИОИП().getИмя());
			if (fdocument.getФайл().getДокумент().getСвКСчФ().getСвПокуп().getИдСв().getСвФЛ().getФИОИП().isSetОтчество()){
				noticeObj.setIPPatronymic(fdocument.getФайл().getДокумент().getСвКСчФ().getСвПокуп().getИдСв().getСвФЛ().getФИОИП().getОтчество());
				noticeObj.setIPSenderPatronymic(fdocument.getФайл().getДокумент().getСвКСчФ().getСвПокуп().getИдСв().getСвФЛ().getФИОИП().getОтчество());
			}
			
			
		}
		noticeObj.setEDOId(fdocument.getФайл().getСвУчДокОбор().getИдПок());
		/*INFO O POLYCHATELE*/
	 
		Date date = new Date(); 
		noticeObj.setGetDate(new SimpleDateFormat("dd.MM.yyyy").format(date));
		noticeObj.setGetTime(new SimpleDateFormat("HH.mm.ss").format(date));
		
		String filename="DP_IZVPOL"+"_"+fdocument.getФайл().getСвУчДокОбор().getИдОтпр()+"_"+fdocument.getФайл().getСвУчДокОбор().getИдПок()+"_"+new SimpleDateFormat("yyyyMMdd").format(date)+"_"+UUID.randomUUID().toString();
//		String filename="DP_IZVPOL"+"_"+fdocument.getФайл().getСвУчДокОбор().getИдОтпр()+"_"+fdocument.getФайл().getСвУчДокОбор().getИдПок()+"_"+new SimpleDateFormat("yyyyMMdd").format(date)+UUID.randomUUID().toString();
		noticeObj.setIDFile(filename);
	
		
		//noticeObj.setIDFile(UUID.randomUUID().toString());
		return noticeObj;
}
}

package com.aisa.portal.invoice.notice.documentsParsers;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

import org.apache.xmlbeans.XmlException;
import org.apache.xmlbeans.XmlOptions;

import ru.aisa.sfact.ФайлDocument;

import com.aisa.portal.invoice.notice.NoticeObject;
import com.aisa.portal.invoice.notice.UvedObject;

public class Sfact extends Abstract{
	 
		public NoticeObject parse (String XML, String sign) throws Exception{
		NoticeObject noticeObj=new NoticeObject(); 
		 XmlOptions options = new XmlOptions();
		 
		 options.setLoadReplaceDocumentElement(new javax.xml.namespace.QName("http://aisa.ru/sfact", "Файл")) ;
		 
		ФайлDocument fdocument=ФайлDocument.Factory.parse(XML,options);
//		System.out.println(fdocument);
		//noticeObj.setFileName(fdocument.getФайл().getИдФайл()+".xml");
		noticeObj.setFileName(fdocument.getФайл().getИдФайл());
		noticeObj.setFileSignature(sign);
		//noticeObj.setFileDateOfCreation(fileDateOfCreation)
		noticeObj.setSForFirst(true);
		noticeObj.setIncomingDocumentName(documentName);
		noticeObj.setIncomingDocumentNumber(fdocument.getФайл().getДокумент().getСвСчФакт().getНомерСчФ());
		noticeObj.setFileDateOfCreation(fdocument.getФайл().getДокумент().getСвСчФакт().getДатаСчФ());
		
		
//		noticeObj.setSenderIdEDO(fdocument.getФайл().getСвУчДокОбор().getИдПок());
		
//		System.out.println(fdocument.getФайл().getСвУчДокОбор().getИдОтпр());
		noticeObj.setSenderIdEDO(fdocument.getФайл().getСвУчДокОбор().getИдОтпр());
		
		/*INFO O OTPRAVITELE*/
		/*if (fdocument.getФайл().getДокумент().getСвСчФакт().getСвПрод().getИдСв().isSetСвЮЛ()){
			noticeObj.setTypeSender(0);
			noticeObj.setSenderINN(fdocument.getФайл().getДокумент().getСвСчФакт().getСвПрод().getИдСв().getСвЮЛ().getИННЮЛ());
			noticeObj.setSenderKPP(fdocument.getФайл().getДокумент().getСвСчФакт().getСвПрод().getИдСв().getСвЮЛ().getКПП());
			noticeObj.setSenderCompanyName(fdocument.getФайл().getДокумент().getСвСчФакт().getСвПрод().getИдСв().getСвЮЛ().getНаимОрг());
		}
		else{
			noticeObj.setTypeSender(1);
			noticeObj.setSenderINN(fdocument.getФайл().getДокумент().getСвСчФакт().getСвПрод().getИдСв().getСвФЛ().getИННФЛ());
			noticeObj.setIPSenderName(fdocument.getФайл().getДокумент().getСвСчФакт().getСвПрод().getИдСв().getСвФЛ().getФИОИП().getИмя());
			
			if (fdocument.getФайл().getДокумент().getСвСчФакт().getСвПрод().getИдСв().getСвФЛ().getФИОИП().isSetОтчество())
			noticeObj.setIPSenderPatronymic(fdocument.getФайл().getДокумент().getСвСчФакт().getСвПрод().getИдСв().getСвФЛ().getФИОИП().getОтчество());
			noticeObj.setIPSenderSurname(fdocument.getФайл().getДокумент().getСвСчФакт().getСвПрод().getИдСв().getСвФЛ().getФИОИП().getФамилия());
			
		}*/
		/*INFO O OTPRAVITELE*/
		
		/*INFO O POLYCHATELE*/
		
		if (fdocument.getФайл().getДокумент().getСвСчФакт().getСвПокуп().getИдСв().isSetСвЮЛ()){
			noticeObj.setTypeSender(0);
//			noticeObj.setSenderINN(fdocument.getФайл().getДокумент().getСвСчФакт().getСвПокуп().getИдСв().getСвЮЛ().getИННЮЛ());
//			noticeObj.setSenderKPP(fdocument.getФайл().getДокумент().getСвСчФакт().getСвПокуп().getИдСв().getСвЮЛ().getКПП());
//			noticeObj.setSenderCompanyName(fdocument.getФайл().getДокумент().getСвСчФакт().getСвПокуп().getИдСв().getСвЮЛ().getНаимОрг());
			
			noticeObj.setSenderINN(fdocument.getФайл().getДокумент().getСвСчФакт().getСвПрод().getИдСв().getСвЮЛ().getИННЮЛ());
			noticeObj.setSenderKPP(fdocument.getФайл().getДокумент().getСвСчФакт().getСвПрод().getИдСв().getСвЮЛ().getКПП());
			noticeObj.setSenderCompanyName(fdocument.getФайл().getДокумент().getСвСчФакт().getСвПрод().getИдСв().getСвЮЛ().getНаимОрг());
			
			
			
			noticeObj.setCompanyName(fdocument.getФайл().getДокумент().getСвСчФакт().getСвПокуп().getИдСв().getСвЮЛ().getНаимОрг());
			noticeObj.setINN(fdocument.getФайл().getДокумент().getСвСчФакт().getСвПокуп().getИдСв().getСвЮЛ().getИННЮЛ());
			noticeObj.setKPP(fdocument.getФайл().getДокумент().getСвСчФакт().getСвПокуп().getИдСв().getСвЮЛ().getКПП());
			noticeObj.setСorporation(true);
		}else{
			
			noticeObj.setTypeSender(1);
			noticeObj.setSenderINN(fdocument.getФайл().getДокумент().getСвСчФакт().getСвПокуп().getИдСв().getСвФЛ().getИННФЛ());
			noticeObj.setIPSenderName(fdocument.getФайл().getДокумент().getСвСчФакт().getСвПокуп().getИдСв().getСвФЛ().getФИОИП().getИмя());
			
			noticeObj.setСorporation(false); 
			
			noticeObj.setINN(fdocument.getФайл().getДокумент().getСвСчФакт().getСвПокуп().getИдСв().getСвФЛ().getИННФЛ());
			noticeObj.setIPName(fdocument.getФайл().getДокумент().getСвСчФакт().getСвПокуп().getИдСв().getСвФЛ().getФИОИП().getИмя());
			if (fdocument.getФайл().getДокумент().getСвСчФакт().getСвПокуп().getИдСв().getСвФЛ().getФИОИП().isSetОтчество()){
				noticeObj.setIPPatronymic(fdocument.getФайл().getДокумент().getСвСчФакт().getСвПокуп().getИдСв().getСвФЛ().getФИОИП().getОтчество());
				noticeObj.setIPSenderPatronymic(fdocument.getФайл().getДокумент().getСвСчФакт().getСвПокуп().getИдСв().getСвФЛ().getФИОИП().getОтчество());
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
		public UvedObject parseUv(String SFxml, String sign) throws Exception{
			
			UvedObject obj = new UvedObject();
			XmlOptions options = new XmlOptions();
			options.setLoadReplaceDocumentElement(new javax.xml.namespace.QName("http://aisa.ru/sfact", "Файл")) ;
			options.setCharacterEncoding("windows-1251");
			options.setValidateOnSet();
			ФайлDocument SF = ФайлDocument.Factory.parse(SFxml, options);
			obj.setKND(SF.getФайл().getДокумент().getКНД().toString());
			obj.setIdotpr(SF.getФайл().getСвУчДокОбор().getИдОтпр());
			obj.setIdpok(SF.getФайл().getСвУчДокОбор().getИдПок());
			obj.setDateSF(SF.getФайл().getДокумент().getСвСчФакт().getДатаСчФ());
			obj.setNoSF(SF.getФайл().getДокумент().getСвСчФакт().getНомерСчФ());	
			
			if (SF.getФайл().getДокумент().getСвСчФакт().isSetИспрСчФ())
			{
				obj.setDateisprSF(SF.getФайл().getДокумент().getСвСчФакт().getИспрСчФ().getДатаИспрСчФ());
				obj.setNoisprSF(SF.getФайл().getДокумент().getСвСчФакт().getИспрСчФ().getНомИспрСчФ());
			}
				
				
			obj.setIdpolfile(SF.getФайл().getИдФайл());
			
			obj.setInnotpr(SF.getФайл().getДокумент().getСвСчФакт().getСвПокуп().getИдСв().getСвЮЛ().getИННЮЛ());
			obj.setKppotpr(SF.getФайл().getДокумент().getСвСчФакт().getСвПокуп().getИдСв().getСвЮЛ().getКПП());
			obj.setNaimotpr(SF.getФайл().getДокумент().getСвСчФакт().getСвПокуп().getИдСв().getСвЮЛ().getНаимОрг());
			obj.setInnuch(SF.getФайл().getДокумент().getСвСчФакт().getСвПрод().getИдСв().getСвЮЛ().getИННЮЛ());
			obj.setKppuch(SF.getФайл().getДокумент().getСвСчФакт().getСвПрод().getИдСв().getСвЮЛ().getКПП());
			obj.setNaimuch(SF.getФайл().getДокумент().getСвСчФакт().getСвПрод().getИдСв().getСвЮЛ().getНаимОрг());


			Date date = new Date(); 
			obj.setDateSF(new SimpleDateFormat("dd.MM.yyyy").format(date));
			//setGetTime(new SimpleDateFormat("HH.mm.ss").format(date));
			
			String filename="DP_UTOCH"+"_"+SF.getФайл().getСвУчДокОбор().getИдОтпр()+"_"+SF.getФайл().getСвУчДокОбор().getИдПок()+"_"+new SimpleDateFormat("yyyyMMdd").format(date)+"_"+UUID.randomUUID().toString();

			
			obj.setIdpolfile(filename); //setIDFile(filename);
			return obj;
		}
}

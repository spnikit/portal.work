package com.aisa.portal.invoice.notice.documentsParsers;

 

 

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

import org.apache.xmlbeans.XmlOptions;

import ru.aisa.pdpol.ФайлDocument;

import com.aisa.portal.invoice.notice.NoticeObject;
import com.aisa.portal.invoice.operator.obj.OperatorObject;

public class Pdpol  extends Abstract{
	 OperatorObject oper;
	 
		
		public OperatorObject getOper() {
			return oper;
		}


		public void setOper(OperatorObject oper) {
			this.oper = oper;
		}
	public NoticeObject parse (String XML, String sign) throws Exception{
		NoticeObject noticeObj=new NoticeObject();
		 XmlOptions options = new XmlOptions();
		 options.setLoadReplaceDocumentElement(new javax.xml.namespace.QName("http://aisa.ru/pdpol", "Файл")) ;
		  
		ФайлDocument fdocument=ФайлDocument.Factory.parse(XML,options);
//		System.out.println(fdocument);
		
		
		noticeObj.setFileName(fdocument.getФайл().getИдФайл());
		noticeObj.setFileSignature(sign);
		noticeObj.setSForFirst(false);
		noticeObj.setIncomingDocumentName(documentName);
		
		/*INFO O OTPRAVITELE TYT POLYBOMY OPERATOR*/
		/*
		noticeObj.setTypeSender(2);
		 
		noticeObj.setSenderINN(fdocument.getФайл().getДокумент().getОперЭДО().getИННЮЛ());
		noticeObj.setSenderIdEDoOper(fdocument.getФайл().getДокумент().getОперЭДО().getИдОперЭДО());
		noticeObj.setSenderIdEDO(fdocument.getФайл().getДокумент().getОтпрДок().getИдУчастЭДО());
		noticeObj.setSenderCompanyName(fdocument.getФайл().getДокумент().getОперЭДО().getНаимОрг());*/
		/*INFO O OTPRAVITELE TYT POLYBOMY OPERATOR*/
		
		
		/*INFO O POLYCHATELE*/
//		if (fdocument.getФайл().getДокумент().getПолДок().isSetЮЛ()){
		if (fdocument.getФайл().getИдФайл().contains("DP_PDPOL")){
			noticeObj.setTypeSender(0);
//			noticeObj.setSenderINN(fdocument.getФайл().getДокумент().getПолДок().getЮЛ().getИННЮЛ());
//			noticeObj.setSenderKPP(fdocument.getФайл().getДокумент().getПолДок().getЮЛ().getКПП());
//			noticeObj.setSenderIdEDO(fdocument.getФайл().getДокумент().getПолДок().getИдУчастЭДО());
//			noticeObj.setSenderCompanyName(fdocument.getФайл().getДокумент().getПолДок().getЮЛ().getНаимОрг());
			
			
//			System.out.println(oper.getIdEDO());
//			System.out.println(oper.getInnurlic());
//			System.out.println(oper.getKpp());
//			System.out.println(oper.getInn());
			
			noticeObj.setSenderINN(oper.getInn());
			noticeObj.setSenderKPP(oper.getKpp());
			noticeObj.setSenderCompanyName(oper.getNameUrLic());
			noticeObj.setSenderIdEDO(oper.getIdEDO());
			
			
//			noticeObj.setSenderINN(fdocument.getФайл().getДокумент().getОперЭДО().getИННЮЛ());
//			noticeObj.setSenderIdEDO(fdocument.getФайл().getДокумент().getОперЭДО().getИдОперЭДО());
//			noticeObj.setSenderCompanyName(fdocument.getФайл().getДокумент().getОперЭДО().getНаимОрг());
//			noticeObj.setSenderIdEDoOper(fdocument.getФайл().getДокумент().getОперЭДО().getИдОперЭДО());
			
		 	noticeObj.setCompanyName(fdocument.getФайл().getДокумент().getПолДок().getЮЛ().getНаимОрг());
			noticeObj.setINN(fdocument.getФайл().getДокумент().getПолДок().getЮЛ().getИННЮЛ());
			noticeObj.setKPP(fdocument.getФайл().getДокумент().getПолДок().getЮЛ().getКПП());
			noticeObj.setСorporation(true); 
		
		}else{
			noticeObj.setСorporation(false);
			noticeObj.setTypeSender(1);
			noticeObj.setSenderINN(fdocument.getФайл().getДокумент().getПолДок().getИП().getИННФЛ());
			noticeObj.setSenderIdEDO(fdocument.getФайл().getДокумент().getОтпрДок().getИдУчастЭДО());
			 noticeObj.setINN(fdocument.getФайл().getДокумент().getПолДок().getИП().getИННФЛ());
			noticeObj.setIPName(fdocument.getФайл().getДокумент().getПолДок().getИП().getФИО().getИмя()); 
			noticeObj.setIPSenderName(fdocument.getФайл().getДокумент().getПолДок().getИП().getФИО().getИмя());
			if (fdocument.getФайл().getДокумент().getПолДок().getИП().getФИО().isSetОтчество()){
				noticeObj.setIPSenderPatronymic(fdocument.getФайл().getДокумент().getПолДок().getИП().getФИО().getОтчество());
			}
			 	noticeObj.setIPPatronymic(fdocument.getФайл().getДокумент().getПолДок().getИП().getФИО().getОтчество());
			
			noticeObj.setIPSurname(fdocument.getФайл().getДокумент().getПолДок().getИП().getФИО().getФамилия()); 
			noticeObj.setIPSenderSurname(fdocument.getФайл().getДокумент().getПолДок().getИП().getФИО().getФамилия());
		}
		
		
		
		noticeObj.setEDOId(fdocument.getФайл().getДокумент().getПолДок().getИдУчастЭДО());
		
		
		Date date = new Date(); 
		noticeObj.setGetDate(new SimpleDateFormat("dd.MM.yyyy").format(date));
		noticeObj.setGetTime(new SimpleDateFormat("HH.mm.ss").format(date));
	
		String filename="DP_IZVPOL"+"_"+fdocument.getФайл().getДокумент().getОперЭДО().getИдОперЭДО()+"_"+fdocument.getФайл().getДокумент().getПолДок().getИдУчастЭДО()+"_"+new SimpleDateFormat("yyyyMMdd").format(date)+"_"+UUID.randomUUID().toString();
//		String filename="DP_IZVPOL"+"_"+fdocument.getФайл().getДокумент().getОперЭДО().getИдОперЭДО()+"_"+fdocument.getФайл().getДокумент().getПолДок().getИдУчастЭДО()+"_"+new SimpleDateFormat("yyyyMMdd").format(date)+UUID.randomUUID().toString();
				noticeObj.setIDFile(filename);
		 
		/*INFO O POLYCHATELE*/
		return noticeObj;
	}
}

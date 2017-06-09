package com.aisa.portal.invoice.notice.documentsParsers;

 

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.UUID;

import org.apache.xmlbeans.XmlOptions;
import org.apache.xmlbeans.XmlValidationError;
import org.w3c.dom.Node;

import ru.aisa.pdotpr.ФайлDocument;
import ru.aisa.pdotpr.ФайлDocument.Файл;




import com.aisa.portal.invoice.notice.NoticeObject;
import com.aisa.portal.invoice.operator.obj.OperatorObject;

public class Pdotpr extends Abstract{
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
		 
		 options.setLoadReplaceDocumentElement(new javax.xml.namespace.QName("http://aisa.ru/pdotpr", "Файл")) ;
		 options.setValidateOnSet();
		 options.setUseDefaultNamespace();
		// System.out.println(XML);
		ФайлDocument fdocument=ФайлDocument.Factory.parse(XML,options);
		
		ArrayList<XmlValidationError> errorList=new ArrayList<XmlValidationError>();
		fdocument.validate(options.setErrorListener(errorList)); 


		noticeObj.setFileName(fdocument.getФайл().getИдФайл()	);
		noticeObj.setFileSignature(sign);
		noticeObj.setSForFirst(false);
		noticeObj.setIncomingDocumentName(documentName);
		/*INFO O OTPRAVITELE TYT POLYBOMY OPERATOR*/
		
		/*noticeObj.setTypeSender(2);
		noticeObj.setSenderINN(fdocument.getФайл().getДокумент().getОперЭДО().getИННЮЛ());
		 
		noticeObj.setSenderIdEDoOper(fdocument.getФайл().getДокумент().getОперЭДО().getИдОперЭДО());
		noticeObj.setSenderIdEDO(fdocument.getФайл().getДокумент().getСвОтпрДок().getИдУчастЭДО());
		
		noticeObj.setSenderCompanyName(fdocument.getФайл().getДокумент().getОперЭДО().getНаимОрг());*/
		/*INFO O OTPRAVITELE TYT POLYBOMY OPERATOR*/
		
		
		/*INFO O POLYCHATELE*/
//		if (fdocument.getФайл().getДокумент().getСвПолДок().isSetОтпрЮЛ()){
		if (fdocument.getФайл().getИдФайл().contains("DP_PDOTPR")){
			noticeObj.setTypeSender(0);
//			noticeObj.setSenderINN(fdocument.getФайл().getДокумент().getСвПолДок().getОтпрЮЛ().getИННЮЛ());
//			noticeObj.setSenderIdEDO(fdocument.getФайл().getДокумент().getСвПолДок().getИдУчастЭДО());
//			noticeObj.setSenderCompanyName(fdocument.getФайл().getДокумент().getСвПолДок().getОтпрЮЛ().getНаимОрг()); 
//			noticeObj.setSenderKPP(fdocument.getФайл().getДокумент().getСвПолДок().getОтпрЮЛ().getКПП());
			
			
//			noticeObj.setSenderINN(fdocument.getФайл().getДокумент().getОперЭДО().getИННЮЛ());
//			noticeObj.setSenderIdEDO(fdocument.getФайл().getДокумент().getОперЭДО().getИдОперЭДО());
//			noticeObj.setSenderCompanyName(fdocument.getФайл().getДокумент().getОперЭДО().getНаимОрг());
//			noticeObj.setSenderIdEDoOper(fdocument.getФайл().getДокумент().getОперЭДО().getИдОперЭДО());
			
			noticeObj.setSenderINN(oper.getInn());
			noticeObj.setSenderKPP(oper.getKpp());
			noticeObj.setSenderCompanyName(oper.getNameUrLic());
			noticeObj.setSenderIdEDO(oper.getIdEDO());
			
			
			
			noticeObj.setCompanyName(fdocument.getФайл().getДокумент().getСвПолДок().getОтпрЮЛ().getНаимОрг());
			noticeObj.setINN(fdocument.getФайл().getДокумент().getСвПолДок().getОтпрЮЛ().getИННЮЛ());
			noticeObj.setKPP(fdocument.getФайл().getДокумент().getСвПолДок().getОтпрЮЛ().getКПП());
			noticeObj.setСorporation(true);
		}else{
			
			noticeObj.setTypeSender(1);
			noticeObj.setСorporation(false);
			noticeObj.setSenderINN(fdocument.getФайл().getДокумент().getСвПолДок().getОтпрИП().getИННФЛ());
			noticeObj.setIPSenderName(fdocument.getФайл().getДокумент().getСвПолДок().getОтпрИП().getФИО().getИмя());
			noticeObj.setSenderIdEDO(fdocument.getФайл().getДокумент().getСвПолДок().getИдУчастЭДО());
			 
			
			noticeObj.setINN(fdocument.getФайл().getДокумент().getСвПолДок().getОтпрИП().getИННФЛ());
			noticeObj.setIPName(fdocument.getФайл().getДокумент().getСвПолДок().getОтпрИП().getФИО().getИмя());
			if (fdocument.getФайл().getДокумент().getСвПолДок().getОтпрИП().getФИО().isSetОтчество()){
				noticeObj.setIPSenderPatronymic(fdocument.getФайл().getДокумент().getСвПолДок().getОтпрИП().getФИО().getОтчество());
				noticeObj.setIPPatronymic(fdocument.getФайл().getДокумент().getСвПолДок().getОтпрИП().getФИО().getОтчество());
			}
			
			noticeObj.setIPSurname(fdocument.getФайл().getДокумент().getСвПолДок().getОтпрИП().getФИО().getФамилия());
			noticeObj.setIPSenderSurname(fdocument.getФайл().getДокумент().getСвПолДок().getОтпрИП().getФИО().getФамилия());
		}
		noticeObj.setEDOId(fdocument.getФайл().getДокумент().getСвПолДок().getИдУчастЭДО());
		/*INFO O POLYCHATELE*/
	 
		Date date = new Date(); 
		noticeObj.setGetDate(new SimpleDateFormat("dd.MM.yyyy").format(date));
		noticeObj.setGetTime(new SimpleDateFormat("HH.mm.ss").format(date));
		
		
		String filename="DP_IZVPOL"+"_"+fdocument.getФайл().getДокумент().getОперЭДО().getИдОперЭДО()+"_"+fdocument.getФайл().getДокумент().getСвПолДок().getИдУчастЭДО()+"_"+new SimpleDateFormat("yyyyMMdd").format(date)+"_"+UUID.randomUUID().toString();
//		String filename="DP_IZVPOL"+"_"+fdocument.getФайл().getДокумент().getОперЭДО().getИдОперЭДО()+"_"+fdocument.getФайл().getДокумент().getСвПолДок().getИдУчастЭДО()+"_"+new SimpleDateFormat("yyyyMMdd").format(date)+UUID.randomUUID().toString();
		noticeObj.setIDFile(filename);
		
		return noticeObj;
	}

	 
}

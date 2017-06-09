package com.aisa.portal.invoice.notice;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

import org.apache.xmlbeans.XmlOptions;

import ru.aisa.uvutoch.ФайлDocument;

public class Uvedparser {
	public   void ParseUved(NoticeObject obj, String Uvxml) throws Exception{
		XmlOptions options = new XmlOptions();
		 options.setLoadReplaceDocumentElement(new javax.xml.namespace.QName("http://aisa.ru/uvutoch", "Файл")) ;
		 options.setCharacterEncoding("windows-1251");
		 options.setValidateOnSet();
		 String uuid  = UUID.randomUUID().toString();
		 Date date = new Date();
		ФайлDocument Uv = ФайлDocument.Factory.parse(Uvxml, options);
		
		String name = Uv.getФайл().getИдФайл();
//		System.out.println(name);
		String Idsender = Uv.getФайл().getДокумент().getУчастЭДО().getИдУчастЭДО();
		String innsender = Uv.getФайл().getДокумент().getУчастЭДО().getЮЛ().getИННЮЛ();
		String kppsender = Uv.getФайл().getДокумент().getУчастЭДО().getЮЛ().getКПП();
		String namesender = Uv.getФайл().getДокумент().getУчастЭДО().getЮЛ().getНаимОрг(); 
	
		
		String Idrec = Uv.getФайл().getДокумент().getОтпрДок().getИдУчастЭДО();
		String innrec = Uv.getФайл().getДокумент().getОтпрДок().getЮЛ().getИННЮЛ();
		String kpprec = Uv.getФайл().getДокумент().getОтпрДок().getЮЛ().getКПП();
		String namerec = Uv.getФайл().getДокумент().getОтпрДок().getЮЛ().getНаимОрг();
		
		obj.setTypeSender(0);
		obj.setSenderCompanyName(namesender);
		obj.setSenderIdEDO(Idsender);
		obj.setSenderINN(innsender);
		obj.setSenderKPP(kppsender);

		
		obj.setСorporation(true);
		obj.setEDOId(Idrec);
		obj.setINN(innrec);
		obj.setKPP(kpprec);
		obj.setCompanyName(namerec);
		
		
		obj.setGetDate(new SimpleDateFormat("dd.MM.yyyy").format(date));
		obj.setGetTime(new SimpleDateFormat("HH.mm.ss").format(date));
		obj.setFileName(name);
		obj.setIDFile("DP_IZVPOL_"+Idsender+"_"+Idrec+"_"+new SimpleDateFormat("yyyyMMdd").format(date) + "_"+ uuid);
	
	
	
	}
}

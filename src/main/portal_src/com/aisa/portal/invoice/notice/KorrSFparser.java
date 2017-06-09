package com.aisa.portal.invoice.notice;

import org.apache.xmlbeans.XmlOptions;

import ru.aisa.korsfact.ФайлDocument;

public class KorrSFparser {
	
	String remove(String in){
		
		
		return in.replaceAll("<xml-fragment>", "").replaceAll("</xml-fragment>", "");
	}
	
	
	public   void ParseKorrSF(UvedObject obj, String SFxml, String sign) throws Exception{
//		System.out.println(SFxml);
		XmlOptions options = new XmlOptions();
		 options.setLoadReplaceDocumentElement(new javax.xml.namespace.QName("http://aisa.ru/korsfact", "Файл")) ;
		 options.setCharacterEncoding("windows-1251");
		 options.setValidateOnSet();
		ФайлDocument SF = ФайлDocument.Factory.parse(SFxml, options);
		obj.setKND(SF.getФайл().getДокумент().getКНД().toString());
		obj.setIdotpr(SF.getФайл().getСвУчДокОбор().getИдОтпр());
		obj.setIdpok(SF.getФайл().getСвУчДокОбор().getИдПок());
		obj.setDateSF(SF.getФайл().getДокумент().getСвКСчФ().getДатаКСчФ());
		obj.setNoSF(SF.getФайл().getДокумент().getСвКСчФ().getНомерКСчФ());	
		
		if (SF.getФайл().getДокумент().getСвКСчФ().isSetИспрКСчФ())
		{
			obj.setDateisprSF(SF.getФайл().getДокумент().getСвКСчФ().getИспрКСчФ().getДатаИспрКСчФ());
			obj.setNoisprSF(SF.getФайл().getДокумент().getСвКСчФ().getИспрКСчФ().getНомИспрКСчФ());
		}
			
			
		obj.setIdpolfile(SF.getФайл().getИдФайл());
		
		obj.setInnotpr(SF.getФайл().getДокумент().getСвКСчФ().getСвПокуп().getИдСв().getСвЮЛ().getИННЮЛ());
		obj.setKppotpr(SF.getФайл().getДокумент().getСвКСчФ().getСвПокуп().getИдСв().getСвЮЛ().getКПП());
		obj.setNaimotpr(SF.getФайл().getДокумент().getСвКСчФ().getСвПокуп().getИдСв().getСвЮЛ().getНаимОрг());
		obj.setInnuch(SF.getФайл().getДокумент().getСвКСчФ().getСвПрод().getИдСв().getСвЮЛ().getИННЮЛ());
		obj.setKppuch(SF.getФайл().getДокумент().getСвКСчФ().getСвПрод().getИдСв().getСвЮЛ().getКПП());
		obj.setNaimuch(SF.getФайл().getДокумент().getСвКСчФ().getСвПрод().getИдСв().getСвЮЛ().getНаимОрг());
		}
	
//	public String changeValues (byte[] xml) throws UnsupportedEncodingException, XmlException{
//		String SFxml = new String (xml, "windows-1251");
//		XmlOptions options = new XmlOptions();
//		 options.setLoadReplaceDocumentElement(new javax.xml.namespace.QName("http://aisa.ru/sfact", "Файл")) ;
//		 options.setCharacterEncoding("windows-1251");
//		 options.setValidateOnSet();
//		ФайлDocument SF = ФайлDocument.Factory.parse(SFxml, options);
//
//		if (SF.getФайл().getДокумент().getСвСчФакт().isSetИспрСчФ()){
//			try{
//			String no=  SF.getФайл().getДокумент().getСвСчФакт().getИспрСчФ().xgetДатаИспрСчФ().toString();
//			String date=  SF.getФайл().getДокумент().getСвСчФакт().getИспрСчФ().xgetНомИспрСчФ().toString();
//
//			} catch (Exception e){
//				e.printStackTrace();
//			}
//		
//		}
//
//		return "<?xml version=\"1.0\" encoding=\"windows-1251\"?>\n"+
//		SF.toString().replaceAll(" xmlns=\"http://aisa.ru/sfact\"", "").replaceAll(" xmlns=\"\"", "");
//	}
}

package com.aisa.portal.invoice.notice;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

import org.apache.xmlbeans.XmlException;
import org.apache.xmlbeans.XmlOptions;

import ru.aisa.uvutoch.ФайлDocument;
import ru.aisa.uvutoch.ФайлDocument.Файл;
import ru.aisa.uvutoch.ФайлDocument.Файл.ВерсФорм;
import ru.aisa.uvutoch.ФайлDocument.Файл.Документ;
import ru.aisa.uvutoch.ФайлDocument.Файл.Документ.КНД;
import ru.aisa.uvutoch.ФайлDocument.Файл.Документ.СвУведУточ;
import sheff.rjd.utils.Base64;


public class UvedObject {
	
	Date date;
	Date time;
	String idotpr;
	String idpok;
	String KND;
	String dateSF;
	String noSF;
	String innuch;
	String kppuch;
	String naimuch;
	String innotpr;
	String kppotpr;
	String naimotpr;
	String duty;
	String fname;
	String mname;
	String lname;
	Integer noisprSF=0;
	String dateisprSF = "";
	String korr;
	String idpolfile;
	byte[] ecppolfile;
	String ecpstring;
	
	public String getKorr() {
		return korr;
	}
	public void setKorr(String korr) {
		this.korr = korr;
	}
	public Integer getNoisprSF() {
		return noisprSF;
	}
	public void setNoisprSF(Integer noisprSF) {
		this.noisprSF = noisprSF;
	}
	public String getDateisprSF() {
		return dateisprSF;
	}
	public void setDateisprSF(String dateisprSF) {
		this.dateisprSF = dateisprSF;
	}
	public String getInnuch() {
		return innuch;
	}
	public void setInnuch(String innuch) {
		this.innuch = innuch;
	}
	public String getKppuch() {
		return kppuch;
	}
	public void setKppuch(String kppuch) {
		this.kppuch = kppuch;
	}
	public String getNaimuch() {
		return naimuch;
	}
	public void setNaimuch(String naimuch) {
		this.naimuch = naimuch;
	}
	public String getInnotpr() {
		return innotpr;
	}
	public void setInnotpr(String innotpr) {
		this.innotpr = innotpr;
	}
	public String getKppotpr() {
		return kppotpr;
	}
	public void setKppotpr(String kppotpr) {
		this.kppotpr = kppotpr;
	}
	public String getNaimotpr() {
		return naimotpr;
	}
	public void setNaimotpr(String naimotpr) {
		this.naimotpr = naimotpr;
	}
	public String getDuty() {
		return duty;
	}
	public void setDuty(String duty) {
		this.duty = duty;
	}
	public String getFname() {
		return fname;
	}
	public void setFname(String fname) {
		this.fname = fname;
	}
	public String getMname() {
		return mname;
	}
	public void setMname(String mname) {
		this.mname = mname;
	}
	public String getLname() {
		return lname;
	}
	public void setLname(String lname) {
		this.lname = lname;
	}
	public String getIdpolfile() {
		return idpolfile;
	}
	public void setIdpolfile(String idpolfile) {
		this.idpolfile = idpolfile;
	}
	public byte[] getEcppolfile() {
		return ecppolfile;
	}
	public void setEcppolfile(byte[] ecppolfile) {
		this.ecppolfile = ecppolfile;
	}
	
	
	
	public String getIdotpr() {
		return idotpr;
	}
	public void setIdotpr(String idotpr) {
		this.idotpr = idotpr;
	}
	public String getIdpok() {
		return idpok;
	}
	public void setIdpok(String idpok) {
		this.idpok = idpok;
	}
	public String getKND() {
		return KND;
	}
	public void setKND(String kND) {
		KND = kND;
	}
	public String getDateSF() {
		return dateSF;
	}
	public void setDateSF(String dateSF) {
		this.dateSF = dateSF;
	}
	public String getNoSF() {
		return noSF;
	}
	public void setNoSF(String noSF) {
		this.noSF = noSF;
	}
	
	public String getEcpstring() {
		return ecpstring;
	}
	public void setEcpstring(String ecpstring) {
		this.ecpstring = ecpstring;
	}
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	public Date getTime() {
		return time;
	}
	public void setTime(Date time) {
		this.time = time;
	}
	public String Generate() throws XmlException, UnsupportedEncodingException{
		XmlOptions options = new XmlOptions();
		 String regex1 = " xmlns=\"http://aisa.ru/uvutoch\"";
		 String regex2 = " xmlns=\"\"";
		// options.setLoadReplaceDocumentElement(new javax.xml.namespace.QName("http://aisa.ru/uvutoch", "Файл")) ;
		 options.setCharacterEncoding("windows-1251");
		 options.setValidateOnSet();
		// options.setUseDefaultNamespace();
		 ФайлDocument filedoc = ФайлDocument.Factory.newInstance();
			 
			String uuid = UUID.randomUUID().toString();
			
			if(date==null){
				date = new Date();
			}
			if(time == null){
				time = new Date();
			}
			//Date date = new Date();
			Файл file = filedoc.addNewФайл();
			file.setВерсФорм(ВерсФорм.Enum.forInt(1));
			file.setВерсПрог("1.0");
			
			file.setИдФайл("DP_UVUTOCH"+"_"+idotpr+"_"+idpok+"_"+ new SimpleDateFormat("yyyyMMdd").format(date) + "_"+ uuid);
			
			Документ doc = file.addNewДокумент();
			
//			System.out.println("КНД: "+КНД.Enum.forInt(1));
			int knd = КНД.INT_X_1115113;
			doc.setКНД(КНД.Enum.forInt(1));
			
			//doc.setКНД(knd);
			
			doc.addNewОтпрДок();
			doc.getОтпрДок().setИдУчастЭДО(idotpr);
			doc.getОтпрДок().addNewЮЛ();
			
			doc.getОтпрДок().getЮЛ().setИННЮЛ(innuch);
			doc.getОтпрДок().getЮЛ().setКПП(kppuch);
			doc.getОтпрДок().getЮЛ().setНаимОрг(naimuch);
			// TODO Auto-generated method stub
			
			
			doc.addNewУчастЭДО();
			doc.getУчастЭДО().setИдУчастЭДО(idpok);
			doc.getУчастЭДО().addNewЮЛ();
			
			doc.getУчастЭДО().getЮЛ().setИННЮЛ(innotpr);
			doc.getУчастЭДО().getЮЛ().setКПП(kppotpr);
			doc.getУчастЭДО().getЮЛ().setНаимОрг(naimotpr);
			
			
			
			СвУведУточ svutoch = doc.addNewСвУведУточ();
			
			/*svutoch.setВремяПол(new SimpleDateFormat("HH.mm.ss").format(date));
			svutoch.setДатаПол(new SimpleDateFormat("dd.MM.yyyy").format(date));*/
			svutoch.setВремяПол(new SimpleDateFormat("HH.mm.ss").format(time));
			svutoch.setДатаПол(new SimpleDateFormat("dd.MM.yyyy").format(date));
			svutoch.addNewСведПолФайл();
			
			svutoch.getСведПолФайл().setИмяПостФайла(idpolfile);
//			svutoch.getСведПолФайл().setЭЦППолФайл(Base64.encodeBytes(ecppolfile));
			svutoch.getСведПолФайл().setЭЦППолФайл(ecpstring);
			doc.addNewПодписант();
			doc.getПодписант().setДолжность(duty);
			doc.getПодписант().addNewФИО();
			doc.getПодписант().getФИО().setИмя(mname);
			doc.getПодписант().getФИО().setОтчество(lname);
			doc.getПодписант().getФИО().setФамилия(fname);
			svutoch.addNewДанПолучДок();
			svutoch.getДанПолучДок().setНаимДок(KND.equals("1115101")?"Счет-фактура":KND.equals("1115108")?"Корректировочный счет-фактура":"");
			
			svutoch.getДанПолучДок().setДатаСФ(dateSF);
			svutoch.getДанПолучДок().setНомСФ(noSF);
			
			if (dateisprSF.length()>0&&noisprSF>0)
			{
				svutoch.getДанПолучДок().setДатаИспрСФ(dateisprSF);
				svutoch.getДанПолучДок().setНомИспрСФ(noisprSF.toString());
			}
			
			svutoch.setТекстУведУточ("<![CDATA[<Корректировка>"+korr+"</Корректировка>]]>");
//			svutoch.setТекстУведУточ(korr);

			String findoc = "<?xml version=\"1.0\" encoding=\"windows-1251\"?>\n"+filedoc.toString().replaceAll(regex1, "").replaceAll(regex2, "");

			return findoc;
	}
	
}

package com.aisa.portal.invoice.notice;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.xmlbeans.XmlOptions;
import org.apache.xmlbeans.XmlValidationError;
import ru.aisa.izvpol.ФИОТип;
import ru.aisa.izvpol.ФЛТип;
import ru.aisa.izvpol.ФайлDocument;
import ru.aisa.izvpol.ФайлDocument.Файл;
import ru.aisa.izvpol.ФайлDocument.Файл.Документ;
import ru.aisa.izvpol.ФайлDocument.Файл.Документ.ОтпрДок;
import ru.aisa.izvpol.ФайлDocument.Файл.Документ.ОтпрДок.ОперЭДО;
import ru.aisa.izvpol.ФайлDocument.Файл.Документ.Подписант;
import ru.aisa.izvpol.ФайлDocument.Файл.Документ.СвИзвПолуч;
import ru.aisa.izvpol.ФайлDocument.Файл.Документ.СвИзвПолуч.СведПолФайл;
import ru.aisa.izvpol.ФайлDocument.Файл.Документ.СвИзвПолуч.СведПолФайл.ДанПолучДок;
import ru.aisa.izvpol.ФайлDocument.Файл.Документ.УчастЭДО;
import ru.aisa.izvpol.ЮЛТип;

 

public class NoticeObject {
	
	String senderIdEDoOper;
	public String getSenderIdEDoOper() {
		return senderIdEDoOper;
	}
	public void setSenderIdEDoOper(String senderIdEDoOper) {
		this.senderIdEDoOper = senderIdEDoOper;
	}

	private String sWVersion="1.0";
	
   public String getsWVersion() {
		return sWVersion;
	}
	public void setsWVersion(String sWVersion) {
		this.sWVersion = sWVersion;
	}
	public String getSignerPosition() {
		return SignerPosition;
	}
	public void setSignerPosition(String signerPosition) {
		SignerPosition = signerPosition;
	}
	public String getSignerName() {
		return SignerName;
	}
	public void setSignerName(String signerName) {
		SignerName = signerName;
	}
	public String getSignerPatronymic() {
		return SignerPatronymic;
	}
	public void setSignerPatronymic(String signerPatronymic) {
		SignerPatronymic = signerPatronymic;
	}
	public String getSignerSurname() {
		return SignerSurname;
	}
	public void setSignerSurname(String signerSurname) {
		SignerSurname = signerSurname;
	}
	public String getEDOId() {
		return EDOId;
	}
	public void setEDOId(String eDOId) {
		EDOId = eDOId;
	}
	public boolean isСorporation() {
		return isСorporation;
	}
	public void setСorporation(boolean isСorporation) {
		this.isСorporation = isСorporation;
	}
	public String getINN() {
		return INN;
	}
	public void setINN(String iNN) {
		INN = iNN;
	}
	public String getKPP() {
		return KPP;
	}
	public void setKPP(String kPP) {
		KPP = kPP;
	}
	public String getCompanyName() {
		return CompanyName;
	}
	public void setCompanyName(String companyName) {
		CompanyName = companyName;
	}
	public String getIPName() {
		return IPName;
	}
	public void setIPName(String iPName) {
		IPName = iPName;
	}
	public String getIPPatronymic() {
		return IPPatronymic;
	}
	public void setIPPatronymic(String iPPatronymic) {
		IPPatronymic = iPPatronymic;
	}
	public String getIPSurname() {
		return IPSurname;
	}
	public void setIPSurname(String iPSurname) {
		IPSurname = iPSurname;
	}
 
 
 
	public String getSenderIdEDO() {
		return SenderIdEDO;
	}
	public void setSenderIdEDO(String senderIdEDO) {
		SenderIdEDO = senderIdEDO;
	}
	public int getTypeSender() {
		return TypeSender;
	}
	public void setTypeSender(int typeSender) {
		TypeSender = typeSender;
	}
	public String getSenderINN() {
		return SenderINN;
	}
	public void setSenderINN(String senderINN) {
		SenderINN = senderINN;
	}
	public String getSenderKPP() {
		return SenderKPP;
	}
	public void setSenderKPP(String senderKPP) {
		SenderKPP = senderKPP;
	}
	public String getSenderCompanyName() {
		return SenderCompanyName;
	}
	public void setSenderCompanyName(String senderCompanyName) {
		SenderCompanyName = senderCompanyName;
	}
	public String getIPSenderName() {
		return IPSenderName;
	}
	public void setIPSenderName(String iPSenderName) {
		IPSenderName = iPSenderName;
	}
	public String getIPSenderPatronymic() {
		return IPSenderPatronymic;
	}
	public void setIPSenderPatronymic(String iPSenderPatronymic) {
		IPSenderPatronymic = iPSenderPatronymic;
	}
	public String getIPSenderSurname() {
		return IPSenderSurname;
	}
	public void setIPSenderSurname(String iPSenderSurname) {
		IPSenderSurname = iPSenderSurname;
	}
	
	public String getIncomingDocumentName() {
		return incomingDocumentName;
	}
	public void setIncomingDocumentName(String incomingDocumentName) {
		this.incomingDocumentName = incomingDocumentName;
	}
	public String getIncomingDocumentNumber() {
		return incomingDocumentNumber;
	}
	public void setIncomingDocumentNumber(String incomingDocumentNumber) {
		this.incomingDocumentNumber = incomingDocumentNumber;
	}
	public boolean isSForFirst() {
		return isSForFirst;
	}
	public void setSForFirst(boolean isSForFirst) {
		this.isSForFirst = isSForFirst;
	}

	String incomingDocumentName ;
	String incomingDocumentNumber;
	
	String SignerPosition;
	String SignerName;
	String SignerPatronymic;
	String SignerSurname;
	String EDOId; 
	boolean isСorporation;
	boolean isSForFirst=false;
	String INN;
	String KPP;
	String CompanyName; 
	String IPName;
	String IPPatronymic;
	String IPSurname;
	String GetDate;
	String GetTime;
	String FileSignature;
	String FileName;
	String FileDateOfCreation;
	String SenderIdEDO;
	int TypeSender=0;  /*0-UR, 1-FIZ, 2- OPERATOR*/
	String SenderINN;
	String SenderKPP;
	String SenderCompanyName;
	String IPSenderName;
	String IPSenderPatronymic;
	String IPSenderSurname;
	String IDFile;
	
	private static final String regex1=" xmlns=\"http://aisa.ru/izvpol\"";
	private static final String regex2=" xmlns=\"\"";
	
	
	
	public   String Generate() throws Exception{
	XmlOptions options = new XmlOptions();
		 
		 options.setLoadReplaceDocumentElement(new javax.xml.namespace.QName("http://aisa.ru/izvpol", "Файл")) ;
		 options.setCharacterEncoding("windows-1251");
	    HashMap<String, String> nsmap=new HashMap<String, String>();
	//	nsmap.put("http://aisa.ru/izvpol","");
	//	options.setSaveImplicitNamespaces(nsmap);
		// options.setUseDefaultNamespace();
		ФайлDocument fdocument=ФайлDocument.Factory.newInstance();//options);
		Файл file = fdocument.addNewФайл(); 
 
		file.setВерсПрог(sWVersion);
		file.setВерсФорм(Файл.ВерсФорм.Enum.forInt(1)); 
		Документ document = file.addNewДокумент();
		document.setКНД(Документ.КНД.Enum.forInt(1));
		file.setИдФайл(IDFile);
		
		/* Подписант */
		Подписант signer = document.addNewПодписант();
		signer.setДолжность(SignerPosition);
		ФИОТип SName=ФИОТип.Factory.newInstance();
		SName.setИмя(SignerName);
		SName.setОтчество(SignerPatronymic);
		SName.setФамилия(SignerSurname);
		signer.setФИО(SName);
		/* Подписант */
		
		 УчастЭДО edouser = document.addNewУчастЭДО();
		 edouser.setИдУчастЭДО(EDOId);
		if (isСorporation){
			ЮЛТип arg1=ЮЛТип.Factory.newInstance();
			arg1.setИННЮЛ(INN);
			arg1.setКПП(KPP);
			arg1.setНаимОрг(CompanyName);
			edouser.setЮЛ(arg1);
		
			
		} else{
		 ФЛТип arg0 = ФЛТип.Factory.newInstance();
		 arg0.setИННФЛ(INN);
		 ФИОТип IpName=ФИОТип.Factory.newInstance();
		 IpName.setИмя(IPName);
		 IpName.setОтчество(IPPatronymic);
		 IpName.setФамилия(IPSurname);
		 arg0.setФИО(IpName);
		 edouser.setИП(arg0);
		}
		
		СвИзвПолуч infoNotice = document.addNewСвИзвПолуч();
		infoNotice.setВремяПол(GetTime);
		infoNotice.setДатаПол(GetDate);
		
		
		
		СведПолФайл arg0=СведПолФайл.Factory.newInstance();
		arg0.addЭЦППолФайл(FileSignature);
		
		arg0.setИмяПостФайла(FileName);
		if (isSForFirst)
				{
		ДанПолучДок arg1=ДанПолучДок.Factory.newInstance();
		arg1.setДатаДок(FileDateOfCreation);
		arg1.setНаимДок(incomingDocumentName);
		arg1.setНомДок(incomingDocumentNumber);
		arg0.setДанПолучДок(arg1);
				}
		infoNotice.setСведПолФайл(arg0);
		
		
		ОтпрДок sender = document.addNewОтпрДок();
		
		switch(TypeSender){
		case 0:{
			ЮЛТип senderType=ЮЛТип.Factory.newInstance();
			senderType.setИННЮЛ(SenderINN);
			senderType.setКПП(SenderKPP);
			senderType.setНаимОрг(SenderCompanyName);
			sender.setЮЛ(senderType);
			break;
		}
		case 1:{
			 ФЛТип senderType = ФЛТип.Factory.newInstance();
			 senderType.setИННФЛ(SenderINN);
			 ФИОТип IpSenderName=ФИОТип.Factory.newInstance();
			 IpSenderName.setИмя(IPSenderName);
			 IpSenderName.setОтчество(IPSenderPatronymic);
			 IpSenderName.setФамилия(IPSenderSurname);
			 senderType.setФИО(IpSenderName);
			 sender.setИП(senderType);
			break;
		}
		case 2:{
			ОперЭДО senderType=ОперЭДО.Factory.newInstance();
//			System.out.println("IDOPER: "+getSenderIdEDoOper());
			senderType.setИдОперЭДО(this.senderIdEDoOper);
			senderType.setИННЮЛ(SenderINN);
			senderType.setНаимОрг(SenderCompanyName);
			sender.setОперЭДО(senderType);
			break;
		}
		}
		
		
		
		
		sender.setИдУчастЭДО(this.SenderIdEDO);
		
		ArrayList<XmlValidationError> errorList=new ArrayList<XmlValidationError>();
  		 fdocument.validate(options.setErrorListener(errorList)); 

  		return "<?xml version=\"1.0\" encoding=\"windows-1251\"?>\n"+fdocument.toString().replaceAll(regex1, "").replaceAll(regex2, "");
//  		return "<?xml version=\"1.0\" encoding=\"windows-1251\"?>\n"+fdocument.toString();
	}
	public String getGetDate() {
		return GetDate;
	}
	public void setGetDate(String getDate) {
		GetDate = getDate;
	}
	public String getGetTime() {
		return GetTime;
	}
	public void setGetTime(String getTime) {
		GetTime = getTime;
	}
	public String getFileSignature() {
		return FileSignature;
	}
	public void setFileSignature(String fileSignature) {
		FileSignature = fileSignature;
	}
	public String getFileName() {
		return FileName;
	}
	public void setFileName(String fileName) {
		FileName = fileName;
	}
	public String getFileDateOfCreation() {
		return FileDateOfCreation;
	}
	public void setFileDateOfCreation(String fileDateOfCreation) {
		FileDateOfCreation = fileDateOfCreation;
	}
	public String getIDFile() {
		return IDFile;
	}
	public void setIDFile(String iDFile) {
		IDFile = iDFile;
	}
	
}

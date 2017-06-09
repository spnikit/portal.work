package sheff.rjd.ws.handlers;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.apache.xmlbeans.XmlOptions;
import org.apache.xmlbeans.XmlValidationError;

import portal.iit.object.AddressRFAct;
import portal.iit.object.CorrectionSfObjectByKorSf;
import portal.iit.object.InfoPaymentDocuments;
import portal.iit.object.UPDSfAddressTypeDTO;
import portal.iit.object.UPDSfAdrInfTypeDTO;
import portal.iit.object.UPDSfAdrRFTypeDTO;
import portal.iit.object.UPDSfFIOTypeDTO;
import portal.iit.object.UPDSfFlDTO;
import portal.iit.object.UPDSfGruzOtDTO;
import portal.iit.object.UPDSfGruzPoluchDTO;
import portal.iit.object.UPDSfKontaktTypeDTO;
import portal.iit.object.UPDSfMemberTypeDTO;
import portal.iit.object.UPDSfObject;
import portal.iit.object.UPDSfPodpisantDTO;
import portal.iit.object.UPDSfSumAkcizTypeDTO;
import portal.iit.object.UPDSfSumNDSTypeDTO;
import portal.iit.object.UPDSfSvIPTypeDTO;
import portal.iit.object.UPDSfSvPokypDTO;
import portal.iit.object.UPDSfSvTdDTO;
import portal.iit.object.UPDSfSvedTovDTO;
import portal.iit.object.UPDSfTableSchFaktDTO;
import portal.iit.object.UPDSfTextInfTypeDTO;
import portal.iit.object.UPDSfVsegoOplDTO;
import portal.iit.object.UPDSfYlDTO;

import ru.aisa.rgd.utils.Base64;
import ru.aisa.rgd.ws.exeption.InternalException;
import ru.aisa.rgd.ws.utility.DataBinder;
import ru.aisa.rgd.ws.utility.RowHandler;
import ru.aisa.sftorgact.ВремяТип;
import ru.aisa.sftorgact.ДатаТип;
import ru.aisa.sftorgact.ДатаТип.Factory;
import ru.aisa.sftorgact.ФайлDocument;
import rzd.util.XmlGenByDocIdResponseDocument.XmlGenByDocIdResponse;
import sheff.rjd.gvcservice.DocXmlGeneratorEndpoint;
import ru.aisa.sftorgact.ФайлDocument.Файл.Документ.Подписант.ОблПолн;
import ru.aisa.sftorgact.ФайлDocument.Файл.Документ.Подписант.Статус;
import ru.aisa.sftorgact.ФайлDocument.Файл.Документ.ТаблСчФакт.СведТов.НалСт;
import ru.aisa.sftorgact.ФайлDocument.Файл.Документ.Функция;

public class UPDSfXmlHandler implements FormHandler {
	static Logger log = Logger.getLogger(UPDSfXmlHandler.class);

	public Object handle(Object... params) throws Exception {
		
		final DataBinder binder = (DataBinder) params[0];
		final XmlGenByDocIdResponse resp = (XmlGenByDocIdResponse) params[1];
		final String param = (String) params[2]; 
		final String docid = (String) params[3]; 

		final String funEnum = "СЧФ";
		
		final UPDSfObject sf = new UPDSfObject();
		sf.setFunctionEnum(funEnum);
		
		sf.setIdPol(binder.getValue("idoper") + binder.getValue("cabinetIdRecv"));
		sf.setIdOtpr(binder.getValue("idoper") + binder.getValue("cabinetIdSell"));

		sf.setNumberSf(binder.getValue("num_schet"));

		sf.setDateSf(binder.getValue("date_ot"));
		final CorrectionSfObjectByKorSf currectionSf = new CorrectionSfObjectByKorSf();
		if (!binder.getValueIfTagExists("num_isprav").equals("")) {
			currectionSf.setDateCorrection(binder.getValueIfTagExists("date_ot_isprav"));
			currectionSf.setNumberCorrection(Integer.parseInt(binder.getValueIfTagExists("num_isprav")));
		}
		sf.setCorrectionSfObj(currectionSf);
		// ---------------- Налог
		sf.setCodeOKB(binder.getValue("valyuta"));

		final UPDSfVsegoOplDTO vsegoOplDTO = new UPDSfVsegoOplDTO();
		final UPDSfSumNDSTypeDTO sumNDSTypeDTO = new UPDSfSumNDSTypeDTO();
		final UPDSfTableSchFaktDTO tableSchFaktDTO = new UPDSfTableSchFaktDTO();
		

		BigDecimal stTovUchNalVsego = new BigDecimal(binder.getValue("vsego_stoimost_s_nalog"));
		vsegoOplDTO.setStTovUchNalVsego(stTovUchNalVsego);
		if(!binder.getValue("vsego_stoimost_bez_nalog").equals("")){
		BigDecimal stTovBezNDSVsego = new BigDecimal(binder.getValue("vsego_stoimost_bez_nalog"));
		vsegoOplDTO.setStTovBezNDSVsego(stTovBezNDSVsego);
		}
		if(!binder.getValue("vsego_nalog").equals("без НДС")){
			BigDecimal sumNal = new BigDecimal(binder.getValue("vsego_nalog"));
			sumNDSTypeDTO.setSumNal(sumNal);
		}else{
			sumNDSTypeDTO.setSumNal(BigDecimal.ZERO);	
		}
		vsegoOplDTO.setSumNDSType(sumNDSTypeDTO);
		tableSchFaktDTO.setVsegoOpl(vsegoOplDTO);
		sf.setTableSchFakt(tableSchFaktDTO);


		// ---------------- Продавец
		final UPDSfMemberTypeDTO memberTypeDTO = new UPDSfMemberTypeDTO();
		memberTypeDTO.setSvULYchNameCompany(binder.getValue("prodavec"));
		
		final String[] innKpp = binder.getValue("inn_prodavca").split("/");
		memberTypeDTO.setSvULYchINNYL(innKpp[0]);
		if (innKpp.length > 1) {
			memberTypeDTO.setSvULYchKPP(innKpp[1]);
		}
		if (innKpp[0].length() == 10) {
			sf.setIfForIProd((int) 0);
		} else {
			sf.setIfForIProd((int) 1);
		}
		final UPDSfKontaktTypeDTO kontakt = new UPDSfKontaktTypeDTO();
		final UPDSfAdrInfTypeDTO adrInfoType = new UPDSfAdrInfTypeDTO();
		final UPDSfAdrRFTypeDTO addressSeller = new UPDSfAdrRFTypeDTO();
		final UPDSfAddressTypeDTO addressTypeDTO = new UPDSfAddressTypeDTO();

		binder.handleTable("SellAddr", "row", new RowHandler<UPDSfAdrRFTypeDTO>() {
			public void handleRow(DataBinder b, int rowNum, UPDSfAdrRFTypeDTO adr) throws InternalException {
				fillAddressRF(adr, b);
			}
		}, addressSeller);
		
		binder.handleTable("SellAddrIn", "row", new RowHandler<UPDSfAdrInfTypeDTO>(){
			public void handleRow(DataBinder b, int rowNum, UPDSfAdrInfTypeDTO adr) throws InternalException{
				fillAddressInf(adr, b);
			}
		}, adrInfoType);
		addressTypeDTO.setUpdSfAdrInfTypeDTO(adrInfoType);
		addressTypeDTO.setUpdSfAdrRFTypeDTO(addressSeller);
		
		memberTypeDTO.setUpdSfAddressTypeDTO(addressTypeDTO);
		memberTypeDTO.setUpdSfKontaktTypeDTO(kontakt);

		final UPDSfFIOTypeDTO fio = new UPDSfFIOTypeDTO();
		final UPDSfSvIPTypeDTO sfSvIP = new UPDSfSvIPTypeDTO();
		sfSvIP.setUpdSfFIOTypeDTO(fio);
		memberTypeDTO.setUpdSfSvIPTypeDTO(sfSvIP);
		sf.setMemberTypeAtribut(memberTypeDTO);

		// ---------------- Покупатель
		final UPDSfMemberTypeDTO memberTypeDTOBayer = new UPDSfMemberTypeDTO();
		memberTypeDTOBayer.setSvULYchNameCompany(binder.getValue("pokypatel"));
		final String[] innKppForPok = binder.getValue("inn_pokypatel").split("/");
		final UPDSfSvPokypDTO sfSvPokyp = new UPDSfSvPokypDTO();
		memberTypeDTOBayer.setSvULYchINNYL(innKppForPok[0]);
		if (innKppForPok.length > 1) {
			memberTypeDTOBayer.setSvULYchKPP(innKppForPok[1]);
		}

		if (innKppForPok[0].length() == 10) {
			sf.setIfForIPok((int) 0);
		} else {
			sf.setIfForIPok((int) 1);
		}
		final UPDSfKontaktTypeDTO kontaktPokyp = new UPDSfKontaktTypeDTO();
		final UPDSfAdrInfTypeDTO adrInfoTypePokyp = new UPDSfAdrInfTypeDTO();
		final UPDSfAdrRFTypeDTO addressBuyer = new UPDSfAdrRFTypeDTO();
		final UPDSfAddressTypeDTO addressTypeDTOBayer = new UPDSfAddressTypeDTO();
		binder.handleTable("CustAddr", "row", new RowHandler<UPDSfAdrRFTypeDTO>() {
			public void handleRow(DataBinder b, int rowNum, UPDSfAdrRFTypeDTO adr) throws InternalException {
				fillAddressRF(adr, b);
			}
		}, addressBuyer);

		binder.handleTable("CustAddrIn", "row", new RowHandler<UPDSfAdrInfTypeDTO>(){
			public void handleRow(DataBinder b, int rowNum, UPDSfAdrInfTypeDTO adr) throws InternalException{
				fillAddressInf(adr, b);
			}
		}, adrInfoTypePokyp);
		
		addressTypeDTOBayer.setUpdSfAdrInfTypeDTO(adrInfoTypePokyp);
		addressTypeDTOBayer.setUpdSfAdrRFTypeDTO(addressBuyer);
		
		memberTypeDTOBayer.setUpdSfAddressTypeDTO(addressTypeDTOBayer);
		memberTypeDTOBayer.setUpdSfKontaktTypeDTO(kontaktPokyp);
		
		
		final UPDSfFIOTypeDTO fioBayer = new UPDSfFIOTypeDTO();
		final UPDSfSvIPTypeDTO sfSvIPBayer = new UPDSfSvIPTypeDTO();
		sfSvIPBayer.setUpdSfFIOTypeDTO(fioBayer);
		memberTypeDTOBayer.setUpdSfSvIPTypeDTO(sfSvIPBayer);
		sfSvPokyp.setSvPokyp(memberTypeDTOBayer);
		sf.setSvPokypDTO(sfSvPokyp);

		// ---------------- Отправитель
		
		final String cargoShipper = binder.getValue("gryzootprav").trim();
		final UPDSfGruzOtDTO sfGruzOt = new UPDSfGruzOtDTO();
		if(!cargoShipper.isEmpty()){
		final UPDSfMemberTypeDTO memberTypeDTOShiper = new UPDSfMemberTypeDTO();
		final String organizationName = cargoShipper.split(",")[0];
		if (organizationName != null) {
			memberTypeDTOShiper.setSvULYchNameCompany(organizationName.trim());
		}
		final UPDSfKontaktTypeDTO kontaktOtpr = new UPDSfKontaktTypeDTO();
		final UPDSfAdrInfTypeDTO adrInfoTypeOtpr = new UPDSfAdrInfTypeDTO();
		final UPDSfAdrRFTypeDTO addressShipper = new UPDSfAdrRFTypeDTO();
		final UPDSfAddressTypeDTO addressTypeDTOShipper = new UPDSfAddressTypeDTO();
		binder.handleTable("SendAddr", "row", new RowHandler<UPDSfAdrRFTypeDTO>() {
			public void handleRow(DataBinder b, int rowNum, UPDSfAdrRFTypeDTO adr) throws InternalException {
				fillAddressRF(adr, b);
			}
		}, addressShipper);
		
		binder.handleTable("SendAddrIn", "row", new RowHandler<UPDSfAdrInfTypeDTO>(){
			public void handleRow(DataBinder b, int rowNum, UPDSfAdrInfTypeDTO adr) throws InternalException{
				fillAddressInf(adr, b);
			}
		}, adrInfoTypeOtpr);
		
		addressTypeDTOShipper.setUpdSfAdrInfTypeDTO(adrInfoTypeOtpr);
		addressTypeDTOShipper.setUpdSfAdrRFTypeDTO(addressShipper);
		
		memberTypeDTOShiper.setUpdSfAddressTypeDTO(addressTypeDTOShipper);
		memberTypeDTOShiper.setUpdSfKontaktTypeDTO(kontaktOtpr);

		final UPDSfFIOTypeDTO fioOtpr = new UPDSfFIOTypeDTO();
		final UPDSfSvIPTypeDTO sfSvIPOtpr = new UPDSfSvIPTypeDTO();
		sfSvIPOtpr.setUpdSfFIOTypeDTO(fioOtpr);
		memberTypeDTOShiper.setUpdSfSvIPTypeDTO(sfSvIPOtpr);
		sfGruzOt.setGruzOt(memberTypeDTOShiper);
		}
		sf.setGruzOtDTO(sfGruzOt);
		// ---------------- Получатель
		
		final String cargoRcv = binder.getValue("gryzopolych").trim();
		final UPDSfGruzPoluchDTO gruzPoluch = new UPDSfGruzPoluchDTO();
		if(!cargoRcv.isEmpty()){
		final UPDSfMemberTypeDTO memberTypeDTORvc = new UPDSfMemberTypeDTO();
		final String organizationNameForGruzPoluch = cargoRcv.split(",")[0];
		if (organizationNameForGruzPoluch != null) {
			memberTypeDTORvc.setSvULYchNameCompany(organizationNameForGruzPoluch.trim());
		}
		final UPDSfKontaktTypeDTO kontaktPoluch = new UPDSfKontaktTypeDTO();
		final UPDSfAdrInfTypeDTO adrInfoTypePoluch = new UPDSfAdrInfTypeDTO();
		final UPDSfAdrRFTypeDTO addressRcv = new UPDSfAdrRFTypeDTO();
		final UPDSfAddressTypeDTO addressTypeDTORcv = new UPDSfAddressTypeDTO();
		binder.handleTable("RecvAddr", "row", new RowHandler<UPDSfAdrRFTypeDTO>() {
			public void handleRow(DataBinder b, int rowNum, UPDSfAdrRFTypeDTO adr) throws InternalException {
				fillAddressRF(adr, b);
			}
		}, addressRcv);
		
		binder.handleTable("RecvAddrIn", "row", new RowHandler<UPDSfAdrInfTypeDTO>(){
			public void handleRow(DataBinder b, int rowNum, UPDSfAdrInfTypeDTO adr) throws InternalException{
				fillAddressInf(adr, b);
			}
		}, adrInfoTypePoluch);
		
		addressTypeDTORcv.setUpdSfAdrInfTypeDTO(adrInfoTypePoluch);
		addressTypeDTORcv.setUpdSfAdrRFTypeDTO(addressRcv);
		
		memberTypeDTORvc.setUpdSfAddressTypeDTO(addressTypeDTORcv);
		memberTypeDTORvc.setUpdSfKontaktTypeDTO(kontaktPoluch);
		

		final UPDSfFIOTypeDTO fioPoluch = new UPDSfFIOTypeDTO();
		final UPDSfSvIPTypeDTO sfSvIPPoluch = new UPDSfSvIPTypeDTO();
		sfSvIPPoluch.setUpdSfFIOTypeDTO(fioPoluch);
		memberTypeDTORvc.setUpdSfSvIPTypeDTO(sfSvIPPoluch);
		gruzPoluch.setGruzPoluch(memberTypeDTORvc);
		}
		sf.setGruzPolichDTO(gruzPoluch);
		// ---------------- Оператор документооборота

		sf.setNameOrganization(binder.getValue("nameoper"));
		sf.setIdEdo(binder.getValue("idoper"));
		sf.setInnEntity(binder.getValue("innoper"));
		SimpleDateFormat regDate = new SimpleDateFormat("dd.MM.yyyy");
		SimpleDateFormat regVremya = new SimpleDateFormat("HH.mm.ss");
		Date vremya = new Date();
		sf.setDataInfPr(regDate.format(vremya));
		sf.setVremInfPr(regVremya.format(vremya));
		sf.setOsnDoverOrgSost(" ");
		sf.setNaimEconSybSost(binder.getValue("prodavec"));
		
		// --------------- Сведения таблицы счета фактуры ( СведТов, ВсегоОпл )
		final List<UPDSfSvTdDTO> listSfSvTdDTO = new ArrayList<UPDSfSvTdDTO>();
		final List<UPDSfSvedTovDTO> listSfSvedTov = new ArrayList<UPDSfSvedTovDTO>();
		final List<UPDSfTextInfTypeDTO> listSfText = new ArrayList<UPDSfTextInfTypeDTO>();
		binder.handleTable("tabel", "row", new RowHandler<Object>() {
			public void handleRow(DataBinder b, int rowNum, Object obj) throws InternalException {
				final UPDSfSvedTovDTO sfSvedTov = new UPDSfSvedTovDTO();
				sfSvedTov.setNumStr(rowNum + 1);
				sfSvedTov.setNameTov(b.getValue("name_tovar"));
				if(!b.getValue("kolvo").equals("")){
				BigDecimal kolVo = new BigDecimal(b.getValue("kolvo"));
				sfSvedTov.setKolTov(kolVo);
				}
				if(!b.getValue("cena").equals("")){
				BigDecimal cena = new BigDecimal(b.getValue("cena"));
				sfSvedTov.setCenaTov(cena);
				}
				if(!b.getValue("stoimoct_bez_nalog").equals("")){
				BigDecimal bezNalog = new BigDecimal(b.getValue("stoimoct_bez_nalog"));
				sfSvedTov.setStTovBezNDS(bezNalog);
				}
				// Налоговую ставку можно исправлять ибо есть варик брать из
				// <asd>

				String taxRate = b.getValue("nalog_stavka");
				if (!"без НДС".equalsIgnoreCase(taxRate) && !taxRate.contains("/") && !taxRate.contains("%")) {
					taxRate += "%";
				}

				sfSvedTov.setNalSt(НалСт.Enum.forString(taxRate));
				final UPDSfSumNDSTypeDTO sfSumNDSTypeDTO = new UPDSfSumNDSTypeDTO();
				if(!b.getValue("sum_nalog").equals("без НДС")){
					BigDecimal sumNal = new BigDecimal(b.getValue("sum_nalog"));
					sfSumNDSTypeDTO.setSumNal(sumNal);
				}else{
					sfSumNDSTypeDTO.setSumNal(BigDecimal.ZERO);	
				}
				sfSvedTov.setSumNDSType(sfSumNDSTypeDTO);
				
				BigDecimal sumNalog = new BigDecimal(b.getValue("stoimoct_s_nalog"));
				sfSvedTov.setStTovUchNal(sumNalog);
				sfSvedTov.setOkeiTov(b.getValue("kod_ed_izmer"));
				final UPDSfSumAkcizTypeDTO sfAkciz = new UPDSfSumAkcizTypeDTO();
				if (!b.getValue("akciz").equals("без акциза")) {
					BigDecimal sumAkciz = new BigDecimal(b.getValue("akciz"));
					sfAkciz.setSumAkciz(sumAkciz);
				} else {
					sfAkciz.setSumAkciz(BigDecimal.ZERO);
				}
				sfSvedTov.setSumAkcizType(sfAkciz);
				// -----------------------декларация
				final UPDSfSvTdDTO sfSvTdDTO = new UPDSfSvTdDTO();
				sfSvTdDTO.setKodProish(b.getValueIfTagExists("kod_strana"));
				sfSvTdDTO.setNumTD(b.getValueIfTagExists("num_deklar"));
				listSfSvTdDTO.add(sfSvTdDTO);

				final UPDSfTextInfTypeDTO updSfText = new UPDSfTextInfTypeDTO();
				listSfText.add(updSfText);
				sfSvedTov.setTextInfType(listSfText);
				
				sfSvedTov.setSvTd(listSfSvTdDTO);
				listSfSvedTov.add(sfSvedTov);
			}
		}, null);
		
		sf.setSvedTovDTO(listSfSvedTov);
		
		final List<UPDSfTextInfTypeDTO> infType = new ArrayList<UPDSfTextInfTypeDTO>();
		sf.setTextInfType(infType);

		final List<InfoPaymentDocuments> listInfPay = new ArrayList<InfoPaymentDocuments>();
		binder.handleTable("table1", "row", new RowHandler<Object>() {
			public void handleRow(DataBinder b, int rowNum, Object obj) throws InternalException {
				final InfoPaymentDocuments infPay = new InfoPaymentDocuments();
				infPay.setDate(b.getValue("k_dok_date"));
				infPay.setNumber(b.getValue("k_doc_num"));
				listInfPay.add(infPay);
			}
		}, null);
		sf.setPrd(listInfPay);
		
		//Подписанты
		
		final List<UPDSfPodpisantDTO> podpisantList = new ArrayList<UPDSfPodpisantDTO>();
		final UPDSfPodpisantDTO podisant = new UPDSfPodpisantDTO();
		
		final UPDSfFIOTypeDTO fioYlDTO = new UPDSfFIOTypeDTO();
		fioYlDTO.setName(param.split(";")[0].split(" ")[1]);
		fioYlDTO.setSureName(param.split(";")[0].split(" ")[0]);
		
		final UPDSfSvIPTypeDTO svIpDTO = new UPDSfSvIPTypeDTO();
		final UPDSfFlDTO flDTO = new UPDSfFlDTO();
		final UPDSfYlDTO ylDTO = new UPDSfYlDTO();
		
		if(param.split(";").length > 1){
		ylDTO.setDoljn(param.split(";")[1]);
		}
		ylDTO.setInnYL(innKpp[0]);
		ylDTO.setNameOrg(binder.getValue("prodavec"));
		ylDTO.setFio(fioYlDTO);
		
		podisant.setFl(flDTO);
		podisant.setIp(svIpDTO);
		podisant.setYl(ylDTO);
		podisant.setStatus(Статус.Enum.forInt(1));
		podisant.setOblPoln(ОблПолн.Enum.forInt(1));
		podisant.setOsnPoln(" ");
		
		podpisantList.add(podisant);
		
		sf.setPodpisantDTO(podpisantList);
		
		String xml = sf.Generate();
		// Подробная валидация.
		final XmlOptions options = new XmlOptions();
		final List<XmlValidationError> errorList = new ArrayList<XmlValidationError>();

		// Валидация полученного XML.
		final boolean valid = ФайлDocument.Factory.parse(xml).validate(options.setErrorListener(errorList));
		if (!valid) {
			xml = xml.replaceAll(DocXmlGeneratorEndpoint.REGEXP, "");
			log.debug("Данный XML был сгенерирован для документа (СФ) id: " + docid + " {" + xml + "}");
			throw new Exception("XML не прошел валидацию. {" + errorList + "}");
		}

		// Убираем неймспейсы
		xml = xml.replaceAll(DocXmlGeneratorEndpoint.REGEXP, "");

		final String str = Base64.encodeBytes(xml.getBytes("windows-1251"), Base64.DONT_BREAK_LINES);
		
		resp.setXmlString(str);

		return resp;

	}
	private void fillAddressInf (UPDSfAdrInfTypeDTO adr, DataBinder b) throws InternalException{
		adr.setKodeCountry(b.getValue("kod"));
		adr.setAddressText(b.getValue("text"));
	}
	
	private void fillAddressRF(UPDSfAdrRFTypeDTO adr, DataBinder b) throws InternalException {
		adr.setIndex(b.getValue("ind"));
		adr.setKodeRegion(b.getValue("kod"));
		adr.setArea(b.getValue("raion"));
		adr.setLocality(b.getValue("punkt"));
		adr.setCity(b.getValue("town"));
		adr.setStreet(b.getValue("street"));
		adr.setStreetNumber(b.getValue("house"));
		adr.setHousing(b.getValue("korp"));
		adr.setApartment(b.getValue("flat"));
	}

	public int getType() {
		return FormHandler.XML_GENERATOR;
	}

}

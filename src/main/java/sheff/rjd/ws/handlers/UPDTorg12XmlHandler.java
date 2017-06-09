package sheff.rjd.ws.handlers;

import java.util.ArrayList;
import java.util.List;

import portal.iit.object.CorrectionSfObjectByKorSf;
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
import portal.iit.object.UPDSfSvIPTypeDTO;
import portal.iit.object.UPDSfSvPokypDTO;
import portal.iit.object.UPDSfYlDTO;
import ru.aisa.rgd.ws.exeption.InternalException;
import ru.aisa.rgd.ws.utility.DataBinder;
import ru.aisa.rgd.ws.utility.RowHandler;
import ru.aisa.sftorgact.ФайлDocument.Файл.Документ.Подписант.ОблПолн;
import ru.aisa.sftorgact.ФайлDocument.Файл.Документ.Подписант.Статус;
import rzd.util.XmlGenByDocIdResponseDocument;

public class UPDTorg12XmlHandler implements FormHandler
{

	public Object handle(Object... params) throws Exception {
		
		final DataBinder binder = (DataBinder) params[0];
		final XmlGenByDocIdResponseDocument.XmlGenByDocIdResponse resp = (XmlGenByDocIdResponseDocument.XmlGenByDocIdResponse) params[1];
		final String param = (String) params[2]; // Параметры
		final String[] massiv = param.split(";");
		final String fioParam = massiv[0]; // ФИО
		final String post = massiv[1]; // Должность
		final String sign = massiv[2]; // Подпись
		final String docId = (String) params[3]; // DocId
		
		final String funEnum = "ДОП";
		
		final UPDSfObject sf = new UPDSfObject();
		sf.setFunctionEnum(funEnum);
		
		sf.setIdPol(binder.getValue("idoper") + binder.getValue("cabinetIdRecv"));
		sf.setIdOtpr(binder.getValue("idoper") + binder.getValue("cabinetIdSell"));

		sf.setNumberSf(binder.getValue("num_schet"));

		sf.setDateSf(binder.getValue("date_ot"));
		if (!binder.getValueIfTagExists("num_isprav").equals("")) {
			final CorrectionSfObjectByKorSf currectionSf = new CorrectionSfObjectByKorSf();
			currectionSf.setDateCorrection(binder.getValueIfTagExists("date_ot_isprav"));
			currectionSf.setNumberCorrection(Integer.parseInt(binder.getValueIfTagExists("num_isprav")));
			sf.setCorrectionSfObj(currectionSf);
		}
		
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

		if (innKpp[0].length() == 10) {
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
		
		final String cargoShipper = binder.getValue("gryzootprav");
		if(!cargoShipper.isEmpty()){
		final UPDSfGruzOtDTO sfGruzOt = new UPDSfGruzOtDTO();
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
		sf.setGruzOtDTO(sfGruzOt);
		}
		// ---------------- Получатель
		
		final String cargoRcv = binder.getValue("gryzopolych");
		if(!cargoRcv.isEmpty()){
		final String organizationNameForGruzPoluch = cargoRcv.split(",")[0];
		final UPDSfMemberTypeDTO memberTypeDTORvc = new UPDSfMemberTypeDTO();
		final UPDSfGruzPoluchDTO gruzPoluch = new UPDSfGruzPoluchDTO();
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
		sf.setGruzPolichDTO(gruzPoluch);
		}
		
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
		
		//TODO: допилить Torg12 продавца. Тут осталось СвПродПер допилить, и теги нужные расставить для СвСчФакт
		
		return null;
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

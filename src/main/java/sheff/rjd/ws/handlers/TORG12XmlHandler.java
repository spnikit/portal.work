package sheff.rjd.ws.handlers;

import portal.iit.object.*;
import sheff.rgd.ws.Controllers.TOR.NumberOfWords;
import sheff.rjd.ws.handlers.FormHandler;
import ru.aisa.rgd.utils.Base64;
import ru.aisa.rgd.ws.exeption.InternalException;
import ru.aisa.rgd.ws.utility.DataBinder;
import ru.aisa.rgd.ws.utility.RowHandler;
import ru.aisa.torgpart1.*;
import rzd.util.XmlGenByDocIdResponseDocument;
import sheff.rjd.gvcservice.DocXmlGeneratorEndpoint;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.xmlbeans.XmlOptions;
import org.apache.xmlbeans.XmlValidationError;
import org.codehaus.plexus.util.StringUtils;

/**
 * Created by xblx on 05.05.15.
 *
 */
public class TORG12XmlHandler implements FormHandler
{
	private static final SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
	private static final SimpleDateFormat time = new SimpleDateFormat("HH.mm.ss");

	public Object handle(Object... params) throws Exception
	{

		final DataBinder binder = (DataBinder) params[0];
		final XmlGenByDocIdResponseDocument.XmlGenByDocIdResponse resp = (XmlGenByDocIdResponseDocument.XmlGenByDocIdResponse) params[1];
		final String param = (String) params[2]; // Параметры
		final String[] massiv = param.split(";");
		final String fioParam = massiv[0]; // ФИО
		final String post = massiv[1]; // Должность
		final String sign = massiv[2]; // Подпись
		final String docId = (String) params[3]; // DocId

		if(sign.equals("1"))
		{
			
			
			Torg12part1Object torg = new Torg12part1Object();


			torg.setIdBuyer(binder.getValue("idoper") + binder.getValue("cabinetIdRecv"));  //Тег <cabinetIdRecv> не заполняется
			torg.setIdSender(binder.getValue("idoper") + binder.getValue("cabinetIdSell")); //Тег <cabinetIdSell> не заполняется
			
			String dateDoc = sdf.format(new Date());
			String timeDoc = time.format(new Date());

			torg.setDateDoc(dateDoc);
			torg.setTimeDoc(timeDoc);
			torg.setDatePackingList(binder.getValue("P_14"));
			torg.setNumberPackingList(binder.getValue("P_13"));
			//Грузоотправитель
			//Начало
			PartyType pShipper = new PartyType();
			Entity shipperEntity = new Entity();

			shipperEntity.setNameOrganization(binder.getValue("P_1"));
			final AddressRFAct addressShipper = new AddressRFAct();
			binder.handleTable("GruzOtprAddr", "row", new RowHandler<AddressRFAct>()
			{
				public void handleRow(DataBinder b, int rowNum, AddressRFAct adr) throws InternalException
				{
					fillAddressRF(adr, b);
				}
			}, addressShipper);
			pShipper.setAddressRF(addressShipper);
			pShipper.setTelephone(binder.getValue("P_1b"));
			pShipper.setFax(binder.getValue("P_1v"));
			pShipper.setAccountNumber(binder.getValue("P_1g"));
			pShipper.setNameBank(binder.getValue("P_1d"));
			pShipper.setBik(binder.getValue("P_1e"));
//			pShipper.setAccountNumber(binder.getValue("P_1j"));
			pShipper.setCorrespondentAccount(binder.getValue("P_1j"));
			shipperEntity.setInn(binder.getValue("P_1z"));
			shipperEntity.setKpp(binder.getValue("P_1i"));
			pShipper.setOkpo(binder.getValue("P_2"));

			pShipper.setEntity(shipperEntity);
			torg.setShipper(pShipper);
			//Конец

			//Грузополучатель
			//Начало
			PartyType pConsignee = new PartyType();
			Entity entConsignee = new Entity();

			entConsignee.setNameOrganization(binder.getValue("P_4"));
			final AddressRFAct addressConsignee = new AddressRFAct();
			binder.handleTable("GruzPolAddr", "row", new RowHandler<AddressRFAct>()
			{
				public void handleRow(DataBinder b, int rowNum, AddressRFAct adr) throws InternalException
				{
					fillAddressRF(adr, b);
				}
			}, addressConsignee);
			pConsignee.setAddressRF(addressConsignee);
			pConsignee.setTelephone(binder.getValue("P_4b"));
			pConsignee.setFax(binder.getValue("P_4v"));
			pConsignee.setEntity(entConsignee);
			torg.setConsignee(pConsignee);
			//Конец

			//Поставщик
			//Начало
			PartyType pContractor = new PartyType();
			Entity entContractor = new Entity();

			entContractor.setNameOrganization(binder.getValue("P_5"));
			final AddressRFAct addressContractor = new AddressRFAct();
			binder.handleTable("PostAddr", "row", new RowHandler<AddressRFAct>()
			{
				public void handleRow(DataBinder b, int rowNum, AddressRFAct adr) throws InternalException
				{
					fillAddressRF(adr, b);
				}
			}, addressContractor);
			pContractor.setAddressRF(addressContractor);
			pContractor.setTelephone(binder.getValue("P_5b"));
			pContractor.setFax(binder.getValue("P_5v"));
			pContractor.setAccountNumber(binder.getValue("P_5g"));
			pContractor.setNameBank(binder.getValue("P_5d"));
			pContractor.setBik(binder.getValue("P_5e"));
//			pContractor.setAccountNumber(binder.getValue("P_5j"));
			pContractor.setCorrespondentAccount(binder.getValue("P_5j"));
			entContractor.setInn(binder.getValue("P_5z"));
			entContractor.setKpp(binder.getValue("P_5i"));
			pContractor.setOkpo(binder.getValue("P_5_1a"));

			pContractor.setEntity(entContractor);
			torg.setContractor(pContractor);

			//Первый подписант - юр. лицо
			final Entity signerEntity1 = new Entity();
			signerEntity1.setNameOrganization(entContractor.getNameOrganization());
			signerEntity1.setInn(entContractor.getInn());

			final FIO fio = FIO.parse(fioParam);
			signerEntity1.setFio(fio);
			signerEntity1.setPost(post);
			torg.setEntity(signerEntity1);
			//Конец

			//Покупатель
			//Начало
			PartyType pPayer = new PartyType();
			Entity entPayer = new Entity();

			entPayer.setNameOrganization(binder.getValue("P_6"));
			final AddressRFAct addressPayer = new AddressRFAct();
			binder.handleTable("BuyerAddr", "row", new RowHandler<AddressRFAct>()
			{
				public void handleRow(DataBinder b, int rowNum, AddressRFAct adr) throws InternalException
				{
					fillAddressRF(adr, b);
				}
			}, addressPayer);
			pPayer.setAddressRF(addressPayer);
			entPayer.setKpp(binder.getValue("P_6b"));
			entPayer.setInn(binder.getValue("P_6v"));
			pPayer.setAccountNumber(binder.getValue("P_6g"));
			pPayer.setNameBank(binder.getValue("P_6d"));
			pPayer.setBik(binder.getValue("P_6e"));
//			pPayer.setAccountNumber(binder.getValue("P_6j"));
			pPayer.setCorrespondentAccount(binder.getValue("P_6j"));
			pPayer.setOkpo(binder.getValue("P_6_1a"));

			pPayer.setEntity(entPayer);
			torg.setPayer(pPayer);
			//Конец

			//Основание
			//Начало
			Base base = new Base();
			base.setName(binder.getValue("P_7"));
			base.setNumber(binder.getValue("P_8"));
			base.setDate(binder.getValue("P_9"));

			torg.setBase(base);
			//Конец

			//Таблица torg_tab
			//Начало
			final List<InfoGoods> tableInfoGoods = new ArrayList<InfoGoods>();

			binder.handleTable("table1", "row", new RowHandler<Object>()
			{
				public void handleRow(DataBinder b, int rowNum, Object obj) throws InternalException
				{
					InfoGoods row = new InfoGoods();
					row.setNumberGoods(b.getLong("P_15"));
					row.setNameGoods(b.getValue("P_16.1"));
					row.setCharacteristicGoods(b.getValue("P_16.2"));
					row.setCodeGoods(b.getValue("P_17"));
					row.setNameUnit(b.getValue("P_18"));
					row.setOkeiGoods(b.getValue("P_19"));
					row.setQuantity(b.getBigDecimal("P_24"));
					row.setPrice(b.getBigDecimal("P_25"));
					row.setPriceWithoutNds(b.getBigDecimal("P_26"));
					row.setNds(b.getBigDecimal("P_27"));
					row.setSumNds(b.getBigDecimal("P_28"));
					row.setPriceWithNds(b.getBigDecimal("P_29"));

					tableInfoGoods.add(row);
				}

			}, null);

			torg.setTableInfoGoods(tableInfoGoods);
			//Конец

			//Итого
			//Начало
			TotalInvoice totalInvoice = new TotalInvoice();
			totalInvoice.setQuantity(binder.getBigDecimal("P_32"));
			totalInvoice.setPriceWithoutNds(binder.getBigDecimal("P_33"));
			totalInvoice.setSumNds(binder.getBigDecimal("P_34"));
			totalInvoice.setPriceWithNds(binder.getBigDecimal("P_35"));
			torg.setTotalInvoice(totalInvoice);
			//Конец

			//Всего
			//Начало
			OverviewLading overviewLading = new OverviewLading();
			overviewLading.setNumberSerialInWords(binder.getValue("P_46"));
			torg.setOverviewLading(overviewLading);
			//Конец
			//Оператор
			//Начало
			torg.setNameOrganization(binder.getValue("nameoper"));
			torg.setIdEdo(binder.getValue("idoper"));
			torg.setInn(binder.getValue("innoper"));
			//Конец

			//Отпуск груза
			//Начало
			InfoReleaseCargo infoReleaseCargo = new InfoReleaseCargo();

			
			
			OfficerType officerTypeAllow = new OfficerType();
			if(!StringUtils.isBlank(binder.getValue("P_50a"))){
				officerTypeAllow.setPost(binder.getValue("P_50a"));
			}else{
				officerTypeAllow.setPost("-");
			}
			if(!StringUtils.isBlank(binder.getValue("P_50v")) && binder.getValue("P_50v").split("[\\u00A0\\s]+").length>1){
				officerTypeAllow.setFio(FIO.parse(binder.getValue("P_50v")));
			}else{
				FIO oficerFio = new FIO();
				oficerFio.setName("-");
				oficerFio.setSurname("-");
				oficerFio.setMiddlename("-");
				officerTypeAllow.setFio(oficerFio);
			}
			//officerTypeAllow.setPost(post);
			//officerTypeAllow.setFio(fio);
			infoReleaseCargo.setVacation(officerTypeAllow);
				
			NumberOfWords now = new NumberOfWords(binder.getValue("P_35"));
			String totalAmountInWords = now.num2str();
			char[] c = totalAmountInWords.toCharArray();
			c[0] = Character.toUpperCase(c[0]);
			totalAmountInWords = new String(c);
			infoReleaseCargo.setTotalAmountInWords(totalAmountInWords);//+binder.getValue("P_51_2") 
	
			String totalAmount = binder.getValue("P_35");//binder.getValue("P_51_1") + "." + binder.getValue("P_51_2");
			BigDecimal totalAmountBigDec = new BigDecimal(totalAmount);
			infoReleaseCargo.setTotalAmount(totalAmountBigDec);

			OfficerType officerTypeMade = new OfficerType();
//			officerTypeMade.setPost(binder.getValue("P_52a"));
//			officerTypeMade.setFio(FIO.parse(binder.getValue("P_52v")));
			officerTypeMade.setPost(post);
			officerTypeMade.setFio(fio);
			infoReleaseCargo.setVacationMade(officerTypeMade);

			OfficerType officerTypeChief = new OfficerType();
			if(!StringUtils.isBlank(binder.getValue("P_54v")) && binder.getValue("P_54v").split("[\\u00A0\\s]+").length>1){
				officerTypeChief.setFio(FIO.parse(binder.getValue("P_54v")));
			}else{
				FIO chiefFio = new FIO();
				chiefFio.setName("-");
				chiefFio.setSurname("-");
				chiefFio.setMiddlename("-");
				officerTypeChief.setFio(chiefFio);
			}
//			officerTypeChief.setFio(fio);
			officerTypeChief.setPost("-");;
			infoReleaseCargo.setChief(officerTypeChief);

			torg.setInfoReleaseCargo(infoReleaseCargo);
			//Конец

			String generate = torg.Generate();
//			System.out.println(generate);
			// Подробная валидация.
			final XmlOptions options = new XmlOptions();
			final List<XmlValidationError> errorList = new ArrayList<XmlValidationError>();

			// Валидация полученного XML.
			final boolean valid = ФайлDocument.Factory.parse(generate).validate(options.setErrorListener(errorList));

			// Убираем неймспейсы
			generate = generate.replaceAll(DocXmlGeneratorEndpoint.REGEXP, "");

			if(!valid)
			{
				log.debug("Данный XML был сгенерирован для документа (ТОРГ-12) id: " + docId + " ,номер подписи: " + sign +  " {"+generate+"}");
				throw new Exception("XML не прошел валидацию. {" + errorList + "}");
			}

			final String str = Base64.encodeBytes(generate.getBytes("windows-1251"), Base64.DONT_BREAK_LINES);

			resp.setXmlString(str);
			return  resp;
		}
		else
		if(sign.equals("2"))
		{
			Torg12part2Object torg2 = new Torg12part2Object();

			torg2.setIdBuyer(binder.getValue("idoper") + binder.getValue("cabinetIdRecv"));  //Тег <cabinetIdRecv> не заполняется
			torg2.setIdSender(binder.getValue("idoper") + binder.getValue("cabinetIdSell"));   // Тег <cabinetIdSell> не заполняется

			torg2.setFileIdentifierConsignmentNoteTitleOfSeller(binder.getValue("IdFileTN"));
			torg2.setDateFormationDocumentWaybillNoteTitleOfSeller(binder.getValue("DateDocTN"));
			torg2.setTimeFormationDocumentWaybillNoteTitleOfSeller(binder.getValue("TimeDocTN"));

			String dateDoc = sdf.format(new Date());
			String timeDoc = time.format(new Date());

			torg2.setDateDoc(dateDoc);
			torg2.setTimeDoc(timeDoc);

			//Начало
			torg2.setNameOrganization(binder.getValue("nameoper"));
			torg2.setIdEdo(binder.getValue("idoper"));
			torg2.setInn(binder.getValue("innoper"));
			//Конец

			torg2.setNumberLadingTitleSeller(binder.getValue("P_13"));
			torg2.setDatePreparationConsignmentNoteTitleSeller(binder.getValue("P_14"));

			//Груз получил + Доверенность
			//Начало
			GetLoad getLoad = new GetLoad();
			ProxyExecutive proxyExecutive = new ProxyExecutive();
			ProxyWhoIssued proxyWhoIssued = new ProxyWhoIssued(); //Кем выдана доверенность
			if(!binder.getValue("P_55v").isEmpty())
			{
				OfficerType georgiaReceived = new OfficerType();
				georgiaReceived.setPost(binder.getValue("P_55a"));
				georgiaReceived.setFio(FIO.parse(binder.getValue("P_55v")));
				getLoad.setGeorgiaReceived(georgiaReceived);
				getLoad.setDateReceived(binder.getValue("P_56"));
			}
			if(!binder.getValue("P_53g").isEmpty())
			{
				ProxyIssued proxyIssued = new ProxyIssued(); //Кому выдана доверенность
				proxyIssued.setFio(FIO.parse(binder.getValue("P_53g")));
				proxyExecutive.setProxyIssued(proxyIssued);
			}
			proxyExecutive.setDateProxy(binder.getValue("P_53b"));
			proxyWhoIssued.setNameOrganization(binder.getValue("P_53v"));
			proxyExecutive.setProxyWhoIssued(proxyWhoIssued);

			torg2.setGetLoad(getLoad);
			//Конец

			//Подписант второй - юр.лицо
			//Начало
			final Entity signerEntity2 = new Entity();
			signerEntity2.setNameOrganization(binder.getValue("P_6"));
			signerEntity2.setInn(binder.getValue("P_6v"));

			final FIO fio = FIO.parse(fioParam);
			signerEntity2.setFio(fio);
			signerEntity2.setPost(post);
			torg2.setEntity(signerEntity2);
			//Конец

			String generate2 = torg2.Generate();

			// Подробная валидация.
			final XmlOptions options = new XmlOptions();
			final List<XmlValidationError> errorList = new ArrayList<XmlValidationError>();

			// Валидация полученного XML.
			final boolean valid = ru.aisa.torgpart2.ФайлDocument.Factory.parse(generate2).validate(options.setErrorListener(errorList));

			// Убираем неймспейсы
			generate2 = generate2.replaceAll(DocXmlGeneratorEndpoint.REGEXP, "");

			if(!valid)
			{
				log.debug("Данный XML был сгенерирован для документа (ТОРГ-12) id: " + docId + " ,номер подписи: " + sign +  " {"+generate2+"}");
				throw new Exception("XML не прошел валидацию. {" + errorList + "}");
			}

			final String str = Base64.encodeBytes(generate2.getBytes("windows-1251"), Base64.DONT_BREAK_LINES);

			resp.setXmlString(str);
		}
		return null;
	}

	private void fillAddressRF(AddressRFAct adr, DataBinder b) throws InternalException
	{
		adr.setIndexRf(b.getValue("ind"));
		adr.setRegionRf(b.getValue("kod"));
		adr.setNeighborhoodRf(b.getValue("raion"));
		adr.setLocalityRf(b.getValue("punkt"));
		adr.setCityRf(b.getValue("town"));
		adr.setStreetRf(b.getValue("street"));
		adr.setHouseRf(b.getValue("house"));
		adr.setHousingRf(b.getValue("korp"));
		adr.setOficeRf(b.getValue("flat"));
	}

	public int getType()
	{
		return FormHandler.XML_GENERATOR;
	}
	
}

package sheff.rjd.ws.handlers;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.xmlbeans.XmlOptions;
import org.apache.xmlbeans.XmlValidationError;

import portal.iit.object.AddressRFAct;
import portal.iit.object.Base;
import portal.iit.object.Entity;
import portal.iit.object.FIO;
import portal.iit.object.InfoGoods;
import portal.iit.object.PartyType;
import portal.iit.object.Signer;
import portal.iit.object.Torg12part1ObjectNew;
import portal.iit.object.Torg12part2ObjectNew;
import ru.aisa.rgd.utils.Base64;
import ru.aisa.rgd.ws.exeption.InternalException;
import ru.aisa.rgd.ws.utility.DataBinder;
import ru.aisa.rgd.ws.utility.RowHandler;
import ru.aisa.torgpart1New.ФайлDocument;
import rzd.util.XmlGenByDocIdResponseDocument;
import sheff.rjd.gvcservice.DocXmlGeneratorEndpoint;

public class TORG12XmlHandlerNew implements FormHandler{

	private static final SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
	private static final SimpleDateFormat time = new SimpleDateFormat("HH.mm.ss");
	private static final SimpleDateFormat sdf2 = new SimpleDateFormat("dd-MM-yyyy");

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
		final FIO fio = FIO.parse(fioParam);

		if(sign.equals("1")){
			
			Torg12part1ObjectNew torg = new Torg12part1ObjectNew();

			//СвУчДокОбор
			torg.setIdCustomer(binder.getValue("idoper") + binder.getValue("cabinetIdRecv"));  //Тег <cabinetIdRecv> не заполняется
			torg.setIdSender(binder.getValue("idoper") + binder.getValue("cabinetIdSell")); //Тег <cabinetIdSell> не заполняется


			torg.setNameOrganization(binder.getValue("nameoper"));
			torg.setIdEdo(binder.getValue("idoper"));
			torg.setInn(binder.getValue("innoper"));


			//Документ
			String dateDoc = sdf.format(new Date());
			String timeDoc = time.format(new Date());
			/*Date actDate = sdf2.parse(binder.getValue("P_8"));
			String actDateFinal = sdf.format(actDate);*/

			torg.setDateDocSeller(dateDoc);
			torg.setTimeDocSeller(timeDoc);

			torg.setNameEconomicSubject(binder.getValue("P_1"));

			torg.setNameDocFactEconomicLife("Документ о передаче товара при торговых операциях");// Взял как в ФПУ26
			torg.setNameDocOrganization("Документ о передаче результатов работ (Документ об оказании услуг)");// Взял как в ФПУ26

			torg.setDateDocPt(binder.getValue("P_14"));//actDateFinal как в ФПУ26
			torg.setNumberDocPt(binder.getValue("P_13"));//binder.getValue("P_7") как в ФПУ26

			torg.setCodeOkb("643");
			torg.setNameOkb("Российский рубль");//не обязательный параметр (ИмяОКБ) в Торг12, но обязательный в ФПУ26 так же или нет заполнять?

			//Грузоотправитель
			//Начало
			PartyType pShipper = new PartyType();
			pShipper.setStructuralSubdivision(binder.getValue("P_1"));
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
			pConsignee.setStructuralSubdivision(binder.getValue("P_4"));
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
			entConsignee.setInn(binder.getValue("P_5z"));
			pConsignee.setEntity(entConsignee);
			torg.setConsignee(pConsignee);
			//Конец



			PartyType seller = new PartyType();

			seller.setStructuralSubdivision(binder.getValue("P_4"));

			//Покупатель
			Entity sellerEntity = new Entity();
			sellerEntity.setNameOrganization(binder.getValue("P_6"));
			sellerEntity.setInn(binder.getValue("P_6v"));
			seller.setEntity(sellerEntity);
			sellerEntity.setFio(fio);
			sellerEntity.setPost(post);
			torg.setSeller(seller);

			//Продавец
			PartyType buyer = new PartyType();
			Entity buyerEntity = new Entity();
			buyerEntity.setNameOrganization(binder.getValue("P_5"));
			buyerEntity.setInn(binder.getValue("P_5z"));
			buyer.setEntity(buyerEntity);
			torg.setBuyer(buyer);

			//Основание
			List<Base> baseList = new ArrayList<Base>();
			Base base = new Base();
			base.setName(binder.getValue("P_7"));
			baseList.add(base);
			torg.setBaseList(baseList);

			//Не заполняю необязательные теги Перевозчик ИдГосКон ВидОперации ИнфПолФХЖ1

			final List<InfoGoods> tableInfoGoods = new ArrayList<InfoGoods>();

			binder.handleTable("table1", "row", new RowHandler<Object>()
					{
				public void handleRow(DataBinder b, int rowNum, Object obj) throws InternalException
				{
					InfoGoods row = new InfoGoods();
					row.setNumberGoods(b.getLong("P_15"));
					//row.setNameGoods(b.getValue("P_16.1"));//не обязательное
					//row.setCharacteristicGoods(b.getValue("P_16.2"));//не обязательное
					//row.setCodeGoods(b.getValue("P_17"));//не обязательное
					row.setNameUnit(b.getValue("P_18"));
					row.setOkeiGoods(b.getValue("P_19"));
					row.setQuantity(b.getBigDecimal("P_24"));
					//row.setPrice(b.getBigDecimal("P_25"));//не обязательное
					//row.setPriceWithoutNds(b.getBigDecimal("P_26"));//не обязательное
					//row.setNds(b.getBigDecimal("P_27"));//не обязательное
					//row.setSumNds(b.getBigDecimal("P_28"));//не обязательное
					row.setPriceWithNds(b.getBigDecimal("P_29"));

					tableInfoGoods.add(row);
				}

					}, null);

			torg.setInfoGoodsList(tableInfoGoods);

			torg.setContentOperation("Перечисленные в документе ценности переданы");

			//подписант

			List<Signer> signersList = new ArrayList<Signer>();

			Signer signer = new Signer();
			signer.setScopePowers(3);
			signer.setStatus(1);
			signer.setUnderAuthoritySignatory("Должностные обязанности");
			signer.setUnderAuthorityOrganization("Доверенность "+ binder.getValue("P_8") +" от "+binder.getValue("P_9") );

			Entity entitySigner = new Entity();
			entitySigner.setFio(fio);
			entitySigner.setInn(binder.getValue("P_5z"));
			entitySigner.setNameOrganization(binder.getValue("P_1"));
			entitySigner.setPost(post);

			signer.setEntity(entitySigner);
			signersList.add(signer);
			torg.setSignersList(signersList);

			String generate = torg.Generate();

			// Подробная валидация.
			final XmlOptions options = new XmlOptions();
			final List<XmlValidationError> errorList = new ArrayList<XmlValidationError>();

			// Валидация полученного XML.
			final boolean valid = ФайлDocument.Factory.parse(generate).validate(options.setErrorListener(errorList));

			// Убираем неймспейсы
			generate = generate.replaceAll(DocXmlGeneratorEndpoint.REGEXP, "");
			
//			System.out.println(generate);
			
			if(!valid)
			{
				log.debug("Данный XML был сгенерирован для документа (ТОРГ-12) id: " + docId + " ,номер подписи: " + sign +  " {"+generate+"}");
				throw new Exception("XML не прошел валидацию. {" + errorList + "}");
			}

			final String str = Base64.encodeBytes(generate.getBytes("windows-1251"), Base64.DONT_BREAK_LINES);

			resp.setXmlString(str);

			return  resp;

		}else if(sign.equals("2")){

			Torg12part2ObjectNew torg2 = new Torg12part2ObjectNew();

			//СвУчДокОбор
			torg2.setIdCustomer(binder.getValue("idoper") + binder.getValue("cabinetIdRecv"));  //Тег <cabinetIdRecv> не заполняется
			torg2.setIdSender(binder.getValue("idoper") + binder.getValue("cabinetIdSell")); //Тег <cabinetIdSell> не заполняется


			torg2.setNameOrganization(binder.getValue("nameoper"));
			torg2.setIdEdo(binder.getValue("idoper"));
			torg2.setInn(binder.getValue("innoper"));

			//Документ
			torg2.setIdInfoDocSeller(binder.getValue("IdFileTN"));
			torg2.setDateInfoDocSeller(binder.getValue("DateDocTN"));
			torg2.setTimeInfoDocSeller(binder.getValue("TimeDocTN"));

			String dateDoc = sdf.format(new Date());
			String timeDoc = time.format(new Date());

			torg2.setDateDocBayer(dateDoc);
			torg2.setTimeDocBayer(timeDoc);
			torg2.setNameEconomicSubject(binder.getValue("P_4"));

			torg2.setIdInfoDocSeller(binder.getValue("documentId"));
			List<String> electronicSignatureList = new ArrayList<String>();
			electronicSignatureList.add(binder.getValue("text1"));
			torg2.setElectronicSignatureList(electronicSignatureList);

			torg2.setNameDocSeller("Товарная накладная");
			torg2.setDateDocPt(binder.getValue("P_14"));

			torg2.setContentOperation("Перечисленные в документе ценности приняты без претензий");

			//final FIO fio = FIO.parse(fioParam);
			List<Signer> signersList = new ArrayList<Signer>();
			Signer signer = new Signer();
			signer.setScopePowers(3);
			signer.setStatus(1);
			signer.setUnderAuthoritySignatory("Должностные обязанности");

			Entity entitySigner = new Entity();
			entitySigner.setFio(fio);
			entitySigner.setInn(binder.getValue("P_6v"));
			entitySigner.setNameOrganization(binder.getValue("P_6"));
			entitySigner.setPost(post);

			signer.setEntity(entitySigner);
			signersList.add(signer);
			torg2.setSignersList(signersList);

			String generate2 = torg2.Generate();

			// Подробная валидация.
			final XmlOptions options = new XmlOptions();
			final List<XmlValidationError> errorList = new ArrayList<XmlValidationError>();

			// Валидация полученного XML.
			final boolean valid = ru.aisa.torgpart2New.ФайлDocument.Factory.parse(generate2).validate(options.setErrorListener(errorList));

			// Убираем неймспейсы
			generate2 = generate2.replaceAll(DocXmlGeneratorEndpoint.REGEXP, "");
			
			if(!valid)
			{
				log.debug("Данный XML был сгенерирован для документа (ТОРГ-12) id: " + docId + " ,номер подписи: " + sign +  " {"+generate2+"}");
				throw new Exception("XML не прошел валидацию. {" + errorList + "}");
			}

			final String str = Base64.encodeBytes(generate2.getBytes("windows-1251"), Base64.DONT_BREAK_LINES);

			resp.setXmlString(str);
		
			return resp;

		}
		return null;
	}

	private void fillAddressRF(AddressRFAct adr, DataBinder b) throws InternalException{
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

	public int getType(){
		return FormHandler.XML_GENERATOR;
	}

}

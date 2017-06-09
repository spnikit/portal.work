package sheff.rjd.ws.handlers;

import portal.iit.object.*;
import sheff.rjd.ws.handlers.FormHandler;
import ru.aisa.rgd.utils.Base64;
import ru.aisa.rgd.ws.exeption.InternalException;
import ru.aisa.rgd.ws.utility.DataBinder;
import ru.aisa.rgd.ws.utility.RowHandler;
import ru.aisa.sfact.ФайлDocument;
import rzd.util.XmlGenByDocIdResponseDocument.XmlGenByDocIdResponse;
import sheff.rjd.gvcservice.DocXmlGeneratorEndpoint;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.apache.xmlbeans.XmlOptions;
import org.apache.xmlbeans.XmlValidationError;

/**
 * Created by xblx on 05.05.15.
 *
 */
public class SFXmlHandler implements FormHandler
{
	/**
	 * @param params : DataBinder, XmlGenByDocIdResponseDocument.XmlGenByDocIdResponse, String params
	 * @throws Exception
	 */
	static Logger log	= Logger.getLogger(SFXmlHandler.class);
	
	public Object handle(Object... params) throws Exception
	{
		final DataBinder binder = (DataBinder) params[0];
		final XmlGenByDocIdResponse resp = (XmlGenByDocIdResponse) params[1];
		final String param = (String) params[2]; // ФИО
		final String docid = (String) params[3];
		
		final SfObject sf = new SfObject();
		
		
		final String idBuy = binder.getValue("idoper") + binder.getValue("cabinetIdRecv");
		final String idSell = binder.getValue("idoper") + binder.getValue("cabinetIdSell");

		sf.setIdBuyer(idBuy);
		sf.setIdSender(idSell);

		sf.setNumberSf(binder.getValue("num_schet")); //TODO: надо что-то придумать со слешами.
		sf.setDateSf(binder.getValue("date_ot"));

		if(!binder.getValueIfTagExists("num_isprav").isEmpty())
		{
			final CorrectionSfObjectByKorSf csf = new CorrectionSfObjectByKorSf();
			csf.setDateCorrection(binder.getValue("date_ot_isprav"));
			csf.setNumberCorrection(binder.getInt("num_isprav"));
			sf.setCorrectionSf(csf);
		}

		// ---------------- Налог ----------------------------------------------------------------

		sf.setCodeOKB(binder.getValue("valyuta"));

		sf.setSummnalog(binder.getValue("vsego_nalog"));
		sf.setSummproductPriceWhithNalog(binder.getValue("vsego_stoimost_s_nalog"));
		sf.setSummproductPriceWhithoutNalog(binder.getValue("vsego_stoimost_bez_nalog"));

		// ---------------- Продавец ----------------------------------------------------------------
		{
			final PartyType pSeller = new PartyType();
			final Entity entitySeller = new Entity();

			entitySeller.setNameOrganization(binder.getValue("prodavec"));

			final String[] innKpp = binder.getValue("inn_prodavca").split("/");
			entitySeller.setInn(innKpp[0]);
			if(innKpp.length > 1)
				entitySeller.setKpp(innKpp[1]);

			final AddressRFAct addressSeller = new AddressRFAct();
			binder.handleTable("SellAddr", "row", new RowHandler<AddressRFAct>()
			{
				public void handleRow(DataBinder b, int rowNum, AddressRFAct adr) throws InternalException
				{
					fillAddressRF(adr, b);
				}
			}, addressSeller);

			pSeller.setAddressRF(addressSeller);
			pSeller.setEntity(entitySeller);
			sf.setSeller(pSeller);

			// Юридическое лицо подписанта идентично продавцу.
			final Entity ulEntity = new Entity();
			ulEntity.setNameOrganization(entitySeller.getNameOrganization());
			ulEntity.setInn(entitySeller.getInn());

			final FIO fio = FIO.parse(param);
			ulEntity.setFio(fio);
			sf.setEntity(ulEntity);
		}

		// ---------------- Покупатель ----------------------------------------------------------------
		{
			final PartyType pBuyer = new PartyType();
			final Entity entityBuyer = new Entity();

			entityBuyer.setNameOrganization(binder.getValue("pokypatel"));

			final String[] innKpp = binder.getValue("inn_pokypatel").split("/");
			entityBuyer.setInn(innKpp[0]);
			if(innKpp.length > 1)
				entityBuyer.setKpp(innKpp[1]);

			final AddressRFAct addressBuyer = new AddressRFAct();
			binder.handleTable("CustAddr", "row", new RowHandler<AddressRFAct>()
			{
				public void handleRow(DataBinder b, int rowNum, AddressRFAct adr) throws InternalException
				{
					fillAddressRF(adr, b);
				}
			}, addressBuyer);

			pBuyer.setAddressRF(addressBuyer);
			pBuyer.setEntity(entityBuyer);
			sf.setBuyer(pBuyer);
		}

		// ---------------- Отправитель ----------------------------------------------------------------
		{
			final String cargoShipper = binder.getValue("gryzootprav");
			if(!cargoShipper.isEmpty())
			{
				final PartyType pShipper = new PartyType();
				final Entity entityShipper = new Entity();

				final String organizationName = cargoShipper.split(",")[0];
				if(organizationName != null)
					entityShipper.setNameOrganization(organizationName.trim());

				final AddressRFAct addressShipper = new AddressRFAct();
				binder.handleTable("SendAddr", "row", new RowHandler<AddressRFAct>()
				{
					public void handleRow(DataBinder b, int rowNum, AddressRFAct adr) throws InternalException
					{
						fillAddressRF(adr, b);
					}
				}, addressShipper);

				pShipper.setAddressRF(addressShipper);
				pShipper.setEntity(entityShipper);
				sf.setShipper(pShipper);
				
				
			}
		}

		// ---------------- Получатель ----------------------------------------------------------------
		{
			final String cargoRcv = binder.getValue("gryzopolych");
			if(!cargoRcv.isEmpty())
			{
				final PartyType pRcv = new PartyType();
				final Entity entityRcv = new Entity();

				final String organizationName = cargoRcv.split(",")[0];
				if(organizationName != null)
					entityRcv.setNameOrganization(organizationName.trim());

				final AddressRFAct addressRcv = new AddressRFAct();
				binder.handleTable("RecvAddr", "row", new RowHandler<AddressRFAct>()
				{
					public void handleRow(DataBinder b, int rowNum, AddressRFAct adr) throws InternalException
					{
						fillAddressRF(adr, b);
					}
				}, addressRcv);

				pRcv.setAddressRF(addressRcv);
				pRcv.setEntity(entityRcv);
				sf.setConsignee(pRcv);
			}
		}

		// ---------------- Оператор документооборота -------------------------------------------------------------

		sf.setNameOrganization(binder.getValue("nameoper"));
		sf.setIdEdo(binder.getValue("idoper"));
		sf.setInnEntity(binder.getValue("innoper"));

		// ---------------- Табличка с данными платежей -------------------------------------------------------------

		final List<TableObj> table = new ArrayList<TableObj>();
		binder.handleTable("tabel", "row", new RowHandler<Object>()
		{
			public void handleRow(DataBinder b, int rowNum, Object obj) throws InternalException
			{
				final TableObj row = new TableObj();
				row.setProductName(b.getValue("name_tovar"));
				row.setProductCount(b.getValue("kolvo"));
				row.setProductPrice(b.getValue("cena"));
				row.setProductPriceWhithoutNalog(b.getValue("stoimoct_bez_nalog"));

				String taxRate = b.getValue("nalog_stavka");
				if (!"без НДС".equalsIgnoreCase(taxRate) && !taxRate.contains("/") && !taxRate.contains("%"))
					taxRate += "%";

				row.setTaxRate(taxRate);
				row.setNalog(b.getValue("sum_nalog"));
				row.setProductPriceWhithNalog(b.getValue("stoimoct_s_nalog"));
				row.setProductUnits(b.getValue("kod_ed_izmer"));
				row.setExcise(b.getValue("akciz"));
				// необязательные поля.
				// ------------------------------------------------------------------------------------------------

				// декларация.
				final String kod_strana = b.getValueIfTagExists("kod_strana");
				final String num_deklar = b.getValueIfTagExists("num_deklar");
				if(!kod_strana.isEmpty() && !num_deklar.isEmpty())
				{
					final List<InfoCustomsDeclaration> declarations = new ArrayList<InfoCustomsDeclaration>();
					final InfoCustomsDeclaration declaration = new InfoCustomsDeclaration();

					declaration.setCode(kod_strana);
					declaration.setNumber(num_deklar);

					declarations.add(declaration);
					row.setInfoCustomsDeclaration(declarations);
 				}
				// ------------------------------------------------------------------------------------------------
				table.add(row);
			}
		}, null);

		sf.setTableList(table);

		// -------------------------------------------------------------------------------------------
		
		final List<InfoPaymentDocuments> ipd = new ArrayList<InfoPaymentDocuments>();
		binder.handleTable("table1", "row", new RowHandler<Object>()
		{
			public void handleRow(DataBinder b, int rowNum, Object obj) throws InternalException
			{
				final String docNum = b.getValue("k_doc_num");
				final String docDate = b.getValue("k_dok_date");
				if(!docNum.isEmpty() && !docDate.isEmpty())
				{
					final InfoPaymentDocuments row = new InfoPaymentDocuments();
					row.setNumber(docNum);
					row.setDate(docDate);
					ipd.add(row);
				}
			}
		}, null);

		// Добавляем, только если что-то заполнилось.
		if(!ipd.isEmpty())
			sf.setPrd(ipd);
		
		// -------------------------------------------------------------------------------------------
		
		String xml = sf.Generate();
//		System.out.println(xml);

		// Подробная валидация.
				final XmlOptions options = new XmlOptions();
				final List<XmlValidationError> errorList = new ArrayList<XmlValidationError>();

				// Валидация полученного XML.
				final boolean valid = ФайлDocument.Factory.parse(xml).validate(options.setErrorListener(errorList));
		if(!valid){
			log.debug( "Данный XML был сгенерирован для документа (СФ) id: " + docid + " {"+xml+"}");
			throw new Exception("XML не прошел валидацию. {" + errorList + "}");
		}
		
		// Убираем неймспейсы
		xml = xml.replaceAll(DocXmlGeneratorEndpoint.REGEXP, "");
		
		final String str = Base64.encodeBytes(xml.getBytes("windows-1251"), Base64.DONT_BREAK_LINES);
		
		resp.setXmlString(str);
		return resp;
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

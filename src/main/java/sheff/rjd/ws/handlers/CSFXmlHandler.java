package sheff.rjd.ws.handlers;

import portal.iit.object.*;
import ru.aisa.korsfact.ФайлDocument;
import sheff.rjd.ws.handlers.FormHandler;
import ru.aisa.rgd.utils.Base64;
import ru.aisa.rgd.ws.exeption.InternalException;
import ru.aisa.rgd.ws.utility.DataBinder;
import ru.aisa.rgd.ws.utility.RowHandler;
import rzd.util.XmlGenByDocIdResponseDocument.XmlGenByDocIdResponse;
import sheff.rjd.gvcservice.DocXmlGeneratorEndpoint;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

/**
 * Created by xblx on 05.05.15.
 *
 */
public class CSFXmlHandler implements FormHandler
{
	static Logger log	= Logger.getLogger(CSFXmlHandler.class);
	
	public Object handle(Object... params) throws Exception
	{
		final DataBinder binder = (DataBinder) params[0];
		final XmlGenByDocIdResponse resp = (XmlGenByDocIdResponse) params[1];
		final String param = (String) params[2]; // ФИО
		final String docid = (String) params[3]; // ФИО
		final CorrectSfObject csf = new CorrectSfObject();

		final String idBuy = binder.getValue("idoper") + binder.getValue("cabinetIdRecv");
		final String idSell = binder.getValue("idoper") + binder.getValue("cabinetIdSell");

		csf.setIdBuyer(idBuy);
		csf.setIdSender(idSell);

		// КОРРЕКТИРОВОЧНЫЙ СЧЕТ-ФАКТУРА №
		// korrek_sf_nomer
		// korrek_sf_ot
		csf.setNumberKorSf(binder.getValue("korrek_sf_nomer"));
		csf.setDateKorSf(binder.getValue("korrek_sf_ot"));

		// ИСПРАВЛЕНИЕ КОРРЕКТИРОВОЧНОГО СЧЕТА-ФАКТУРЫ
		if(!binder.getValueIfTagExists("isprav_sf_nomer").isEmpty())
		{
			final CorrectionSfObjectByKorSf correction = new CorrectionSfObjectByKorSf();
			correction.setNumberCorrection(binder.getInt("isprav_sf_nomer")); //TODO: надо что-то придумать со слешами.
			correction.setDateCorrection(binder.getValue("isprav_sf_ot"));
			csf.setCorrectionKorSf(correction);
		}

		// ------------------- Информация по счетам-фактурам -----------------------------------------------------------

		final List<SfObjectByKorSf> sfsInfo = new ArrayList<SfObjectByKorSf>(1);

		// к СЧЕТУ-ФАКТУРЕ №
		// kschety_faktyre_nomer
		// kschety_faktyre_ot

		final SfObjectByKorSf sfInfo = new SfObjectByKorSf();
		sfInfo.setNumberSf(binder.getValue("kschety_faktyre_nomer"));  //TODO: надо что-то придумать со слешами.
		sfInfo.setDateSf(binder.getValue("kschety_faktyre_ot"));

		// с учетом исправления №
		// sychetom_isprav_nomer
		// sychetom_isprav_ot

		// Существует корректировка счета-фактуры
		if(!binder.getValueIfTagExists("sychetom_isprav_nomer").isEmpty())
		{
			final CorrectionSfObjectByKorSf sfCorrectionInfo = new CorrectionSfObjectByKorSf();
			sfCorrectionInfo.setNumberCorrection(binder.getInt("sychetom_isprav_nomer"));
			sfCorrectionInfo.setDateCorrection(binder.getValue("sychetom_isprav_ot"));

			final List<CorrectionSfObjectByKorSf> sfCorrectionsInfo = new ArrayList<CorrectionSfObjectByKorSf>(1);
			sfCorrectionsInfo.add(sfCorrectionInfo);
			sfInfo.setCorrection(sfCorrectionsInfo);
		}
		sfsInfo.add(sfInfo);
		csf.setCorrectionSfList(sfsInfo);

		// -------------------------------------------------------------------------------------------------------------

		// ----------- ПРОДАВЕЦ ----------------------------------------------------------------------------------------
		{
			final PartyType seller = new PartyType();
			final Entity entity = new Entity();

			entity.setNameOrganization(binder.getValue("prodavec"));

			final String[] innKpp = binder.getValue("inn_prodovca").split("/");
			entity.setInn(innKpp[0]);
			if(innKpp.length > 1)
				entity.setKpp(innKpp[1]);

			final AddressRFAct sellerAddress = new AddressRFAct();
			binder.handleTable("SellAddr", "row", new RowHandler<AddressRFAct>()
			{
				public void handleRow(DataBinder b, int rowNum, AddressRFAct obj) throws InternalException
				{
					fillAddressRF(obj, b);
				}
			}, sellerAddress);

			seller.setAddressRF(sellerAddress);
			seller.setEntity(entity);
			csf.setSeller(seller);

			// Юридическое лицо подписанта идентично продавцу.
			final Entity ulEntity = new Entity();
			ulEntity.setNameOrganization(entity.getNameOrganization());
			ulEntity.setInn(entity.getInn());

			final FIO fio = FIO.parse(param);
			ulEntity.setFio(fio);
			csf.setEntity(ulEntity);
		}

		// ------------ ПОКУПАТЕЛЬ -------------------------------------------------------------------------------------
		{
			final PartyType buyer = new PartyType();
			final Entity entity = new Entity();

			entity.setNameOrganization(binder.getValue("pokypatel"));

			final String[] innKpp = binder.getValue("inn_pokypatelya").split("/");
			entity.setInn(innKpp[0]);
			if(innKpp.length > 1)
				entity.setKpp(innKpp[1]);

			final AddressRFAct buyerAddress = new AddressRFAct();
			binder.handleTable("CustAddr", "row", new RowHandler<AddressRFAct>()
			{
				public void handleRow(DataBinder b, int rowNum, AddressRFAct obj) throws InternalException
				{
					fillAddressRF(obj, b);
				}
			}, buyerAddress);

			buyer.setAddressRF(buyerAddress);
			buyer.setEntity(entity);
			csf.setBuyer(buyer);
		}

		// -------------------------------------------------------------------------------------------------------------

		csf.setCodeOKB(binder.getValue("kod_valuta"));

		// ---------------- Оператор документооборота ------------------------------------------------------------------

		csf.setNameOrganization(binder.getValue("nameoper"));
		csf.setIdEdo(binder.getValue("idoper"));
		csf.setInnEntity(binder.getValue("innoper"));

		// ---------------- Табличка с данными платежей -------------------------------------------------------------

		final String taxRateType = binder.getValue("P_C");
		final List<TableObj> table = new ArrayList<TableObj>();
		binder.handleTable("table1", "row", new RowHandler<Object>()
		{
			public void handleRow(DataBinder b, int rowNum, Object obj) throws InternalException
			{
				final TableObj row = new TableObj();
				row.setProductName(b.getValue("naim_tovara"));

				final PriceGoods priceWithoutTax = new PriceGoods(b.getValue("stoim_tovara"), b.getValue("stoim_tovara2"));
				row.setProductPriceWithoutNalogObj(priceWithoutTax);

				final PriceGoods priceWithTax = new PriceGoods(b.getValue("stoim_vsego"), b.getValue("stoim_vsego2"));
				row.setProductPriceWithNalogObj(priceWithTax);

				row.setProductCount(b.getValue("kol_obem"));
				row.setProductCountAfter(b.getValue("kol_obem2"));

				row.setProductUnits(b.getValue("kod"));
				row.setProductUnitsAfter(b.getValue("kod2"));

				row.setProductPrice(b.getValue("cena_tarif"));
				row.setProductPriceAfter(b.getValue("cena_tarif2"));

				row.setExcise(b.getValue("vtom_chisle"));
				row.setExciseAfter(b.getValue("vtom_chisle2"));

				{
					String taxRate = b.getValue("nalog_stavka");
					if (!"без НДС".equalsIgnoreCase(taxRate)
							&& !taxRate.contains("/")
							&& !taxRate.contains("%"))
						taxRate += "%";

					row.setTaxRate(taxRate);
				}
				{
					String taxRateAfter = b.getValue("nalog_stavka2");
					if (!"без НДС".equalsIgnoreCase(taxRateAfter)
							&& !taxRateAfter.contains("/")
							&& !taxRateAfter.contains("%"))
						taxRateAfter += "%";

					row.setTaxRateAfter(taxRateAfter);
				}

				row.setNalog(b.getValue("summ_naloga"));
				row.setNalogAfter(b.getValue("summ_naloga2"));

				table.add(row);
			}
		}, null);
		csf.setTableList(table);

		// -------------------------------------------------------------------------------------------
		String xml = csf.Generate();
//		System.out.println(xml);
		// Валидация полученного XML.
		final boolean valid = ФайлDocument.Factory.parse(xml).validate();
		if(!valid){
			log.debug( "Данный XML был сгенерирован для документа (КСФ) id: " + docid + " {"+xml+"}");
			throw new Exception("XML не прошел валидацию.");
		}
			

		// Убираем неймспейсы
		xml = xml.replaceAll(DocXmlGeneratorEndpoint.REGEXP, "");

		final String str = Base64.encodeBytes(xml.getBytes("windows-1251"), Base64.DONT_BREAK_LINES);

		resp.setXmlString(str);

		return resp;
	}

	public int getType()
	{
		return FormHandler.XML_GENERATOR;
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
}

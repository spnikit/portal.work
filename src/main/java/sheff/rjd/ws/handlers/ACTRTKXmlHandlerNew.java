package sheff.rjd.ws.handlers;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Level;
import org.apache.xmlbeans.XmlOptions;
import org.apache.xmlbeans.XmlValidationError;
import org.codehaus.plexus.util.StringUtils;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import portal.iit.object.ActObjectNew;
import portal.iit.object.AddressINOAct;
import portal.iit.object.Base;
import portal.iit.object.DescriptionWorks;
import portal.iit.object.Entity;
import portal.iit.object.FIO;
import portal.iit.object.PartyType;
import portal.iit.object.Signer;
import portal.iit.object.WorkObj;
import ru.aisa.rgd.utils.Base64;
import ru.aisa.rgd.ws.exeption.InternalException;
import ru.aisa.rgd.ws.utility.DataBinder;
import ru.aisa.rgd.ws.utility.RowHandler;
import ru.aisa.rjd.utility.TypeConverter;
import rzd.util.XmlGenByDocIdResponseDocument;
import sheff.rjd.gvcservice.DocXmlGeneratorEndpoint;


public class ACTRTKXmlHandlerNew implements FormHandler
{

	private static final SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
	private static final SimpleDateFormat time = new SimpleDateFormat("HH.mm.ss");
	private NamedParameterJdbcTemplate npjt;

	public NamedParameterJdbcTemplate getNpjt() {
		return npjt;
	}

	public void setNpjt(NamedParameterJdbcTemplate npjt) {
		this.npjt = npjt;
	}

	public Object handle(Object... params) throws Exception
	{
		
		final DataBinder binder = (DataBinder) params[0];
		final XmlGenByDocIdResponseDocument.XmlGenByDocIdResponse resp = (XmlGenByDocIdResponseDocument.XmlGenByDocIdResponse) params[1];
		final String param = (String) params[2]; // Параметры
		final String[] massiv = param.split(";");
		final String fioParam = massiv[0]; // ФИО
		final String post = massiv[1]; // Должность
		final String dateSign = massiv[2]; //Дата подписи
		final String sign = massiv[3]; // Подпись
		final String docId = (String) params[3]; // DocId
		
		Map<String, Object> paramMap = new HashMap<String, Object>();
		
		if (sign.equals("1")) {
			
			
			
			ActObjectNew actObject = new ActObjectNew();
			final FIO fio = FIO.parse(fioParam);

			actObject.setIdRecipient(binder.getValue("idoper") + binder.getValue("cabinetIdRecv"));
			actObject.setIdSeller(binder.getValue("idoper") + binder.getValue("cabinetIdSell"));
			
			//Оператор
			//Начало
			actObject.setNameOrganizationSeller(binder.getValue("nameoper"));
			actObject.setIdEdoSeller(binder.getValue("idoper"));
			actObject.setInnEntitySeller(binder.getValue("innoper"));
			//Конец
			
			String dateDoc = sdf.format(new Date());//переделали пока что так
			String timeDoc = time.format(new Date());//переделали пока что так
			
			
			
			actObject.setDateDocExecutor(dateDoc);
			actObject.setTimeDocExecutor(timeDoc);
			
			actObject.setNameEconomicSubject(binder.getValue("P_6"));
			
			
			actObject.setFactEconomicLife("Документ о передаче результатов работ (Документ об оказании услуг)");
			actObject.setNameSender("Акт о передаче результатов работ (Акт об оказании услуг)");
			actObject.setNumberDocPru(binder.getValue("P_2"));
			actObject.setDateDocPru(binder.getValue("P_49"));
			
			actObject.setCodeOkb("643");
			actObject.setNameOkb("Российский рубль");
			
			actObject.setTitleSubstanceTransaction(binder.getValue("P_48"));
			
			String okpoExecutor = null;
			String okpoCustomer = null;
			try{
				paramMap.put("innExecutor", binder.getValue("P_31"));
				paramMap.put("kppExecutor", binder.getValue("P_32"));
				paramMap.put("innCustomer", binder.getValue("P_33"));
				paramMap.put("kppCustomer", binder.getValue("P_34"));
				okpoExecutor = (String) getNpjt().queryForObject("select okpo_kod from snt.pred where trim(inn) in :innExecutor and trim(kpp) in :kppExecutor" , paramMap, String.class);
				okpoCustomer = (String) getNpjt().queryForObject("select okpo_kod from snt.pred where trim(inn) in :innCustomer and trim(kpp) in :kppCustomer" , paramMap, String.class);
				okpoExecutor = StringUtils.leftPad(okpoExecutor, 8, "0");
				okpoCustomer = StringUtils.leftPad(okpoCustomer, 8, "0");
			}catch(Exception e){
				log.error(TypeConverter.exceptionToString(e));
			}
			
			PartyType executor = new PartyType();
			
			executor.setOkpo(okpoExecutor);
			//executor.setStructuralSubdivision(binder.getValue("P_9"));
			
			Entity entityExecutor = new Entity();
			
			entityExecutor.setNameOrganization(binder.getValue("P_6"));
			entityExecutor.setInn(binder.getValue("P_31"));
			entityExecutor.setKpp(binder.getValue("P_32"));
			
			AddressINOAct addressInoExecutor = new AddressINOAct();
			addressInoExecutor.setCodeCountryIno("643");
			addressInoExecutor.setAddressTextIno("-");
				
			executor.setEntity(entityExecutor);
			executor.setAddresINO(addressInoExecutor);
			actObject.setExecutor(executor);
			
			PartyType customer = new PartyType();
			
			customer.setOkpo(okpoCustomer);
			
			Entity entityCustomer = new Entity();

			entityCustomer.setNameOrganization(binder.getValue("P_8"));
			entityCustomer.setInn(binder.getValue("P_33"));
			entityCustomer.setKpp(binder.getValue("P_34"));
			
			AddressINOAct addressInoCustomer = new AddressINOAct();
			addressInoCustomer.setCodeCountryIno("643");
			addressInoCustomer.setAddressTextIno("-");
				
			customer.setEntity(entityCustomer);
			customer.setAddresINO(addressInoCustomer);
			actObject.setCustomer(customer);
			
			List<Base> baseList = new ArrayList<Base>();
			
			Base base = new Base();
			base.setName("-");
			//base.setNumber(binder.getValue("P_50"));
			//base.setDate(binder.getValue("P_49"));
			base.setInfo(binder.getValue("P_9"));
			baseList.add(base);
			actObject.setBaseList(baseList);
			
			
			final List<WorkObj> tableWorkObj = new ArrayList<WorkObj>();
			binder.handleTable("table", "row", new RowHandler<Object>()
					{
						public void handleRow(DataBinder b, int rowNum, Object obj)
						{
							WorkObj rowWrkObj = new WorkObj();
							rowWrkObj.setNumber(String.valueOf(rowNum + 1));
							try {
								if (!b.getValue("P_11").isEmpty()) {
									rowWrkObj.setNameWorks(b.getValue("P_11"));
								}

								rowWrkObj.setNameUnits(b.getValue("P_13"));
								rowWrkObj.setOkei("796");
								if (!b.getValue("P_14").isEmpty()) {
									rowWrkObj.setPrice(b.getBigDecimal("P_14"));
								}
								if (!b.getValue("P_12").isEmpty()) {
									rowWrkObj.setCount(b.getBigDecimal("P_12"));
								}
								if (!b.getValue("P_41").isEmpty()) {
									rowWrkObj.setSumWithoutNds(b.getBigDecimal("P_41"));
								}
								if (!b.getValue("P_15").isEmpty()) {
									rowWrkObj.setTaxRate(b.getValue("P_15")+"%");
								}

								if (!b.getValue("P_16").isEmpty()) {
									rowWrkObj.setSumNds(b.getBigDecimal("P_16"));
								}
								if (!b.getValue("P_17").isEmpty()) {
									rowWrkObj.setSumWithtNds(b.getBigDecimal("P_17"));
								}
							} catch (InternalException e) {
								log.error(TypeConverter.exceptionToString(e));
							}

							tableWorkObj.add(rowWrkObj);
						}
					}, null);
			
			
			List<DescriptionWorks> descriptionWorks = new ArrayList<DescriptionWorks>();
			DescriptionWorks rowDescriptionWorks = new DescriptionWorks();
			rowDescriptionWorks.setListWorksObj(tableWorkObj);
			if (!binder.getValue("P_49").isEmpty()) {
				rowDescriptionWorks.setStartWorks(binder.getValue("P_49"));
			}
			if (!binder.getValue("P_49").isEmpty()) {
				rowDescriptionWorks.setEndWorks(binder.getValue("P_49"));
			}
			if (!binder.getValue("P_18").isEmpty()) {
				rowDescriptionWorks.setSumWithoutNdsInTotal(binder.getBigDecimal("P_18"));
			}
			if (!binder.getValue("P_20").isEmpty()) {
				rowDescriptionWorks.setSumNdsInTotal(binder.getBigDecimal("P_19"));
			}
			rowDescriptionWorks.setSumWithNdsInTotal(binder.getBigDecimal("P_20"));
			descriptionWorks.add(rowDescriptionWorks);
			
			actObject.setDescriptionWorks(descriptionWorks);
			
			actObject.setContentsOperation("Результаты работ переданы (услуги оказаны)");
			
			List<Signer> signersList = new ArrayList<Signer>();
			Signer signer = new Signer();
			signer.setScopePowers(3);
			signer.setStatus(1);
			signer.setUnderAuthoritySignatory("Должностные обязанности");
			signer.setUnderAuthorityOrganization("Доверенность "+ binder.getValue("P_42") +" от "+binder.getValue("P_49") );
			Entity entitySigner = new Entity();
			
			entitySigner.setFio(fio);
			entitySigner.setInn(binder.getValue("P_31"));
			entitySigner.setNameOrganization(binder.getValue("P_6"));
			entitySigner.setPost(post);
			
			signer.setEntity(entitySigner);
			signersList.add(signer);
			actObject.setSignersList(signersList);

			String generate = actObject.Generate();
			// Подробная валидация.
			final XmlOptions options = new XmlOptions();
			final List<XmlValidationError> errorList = new ArrayList<XmlValidationError>();

			// Валидация полученного XML.
			final boolean valid = ru.aisa.actpart1New.ФайлDocument.Factory.parse(generate).validate(options.setErrorListener(errorList));

			// Убираем неймспейсы
			generate = generate.replaceAll(DocXmlGeneratorEndpoint.REGEXP, "");

			log.log(Level.DEBUG, "Данный XML был сгенерирован для документа (Акт РТК) id: " + docId + " ,номер подписи: " + sign + " {" + generate + "}");

			if (!valid)
			{
				throw new Exception("XML не прошел валидацию. {" + errorList + "}");
			}

			final String str = Base64.encodeBytes(generate.getBytes("windows-1251"), Base64.DONT_BREAK_LINES);
		
			resp.setXmlString(str);
			
			return resp;
		} 
		return null;
	}

	public int getType()
	{
		return FormHandler.XML_GENERATOR;
	}
}

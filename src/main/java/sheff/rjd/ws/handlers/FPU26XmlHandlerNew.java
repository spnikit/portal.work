package sheff.rjd.ws.handlers;

import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.xmlbeans.XmlOptions;
import org.apache.xmlbeans.XmlValidationError;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.transaction.support.TransactionTemplate;

import portal.iit.object.Act2ObjectNew;
import portal.iit.object.ActObjectNew;
import portal.iit.object.AddressINOAct;
import portal.iit.object.Base;
import portal.iit.object.DescriptionWorks;
import portal.iit.object.Entity;
import portal.iit.object.FIO;
import portal.iit.object.PartyType;
import portal.iit.object.Signer;
import portal.iit.object.WorkObj;
import ru.aisa.actpart2New.ФайлDocument;
import ru.aisa.rgd.utils.Base64;
import ru.aisa.rgd.ws.dao.JdbcServiceFacade;
import ru.aisa.rgd.ws.dao.ServiceFacade;
import ru.aisa.rgd.ws.exeption.InternalException;
import ru.aisa.rgd.ws.utility.DataBinder;
import ru.aisa.rgd.ws.utility.RowHandler;
import ru.aisa.rgd.ws.utility.TypeConverter;
import rzd.util.XmlGenByDocIdResponseDocument;
import sheff.rjd.gvcservice.DocXmlGeneratorEndpoint;

public class FPU26XmlHandlerNew implements FormHandler{
	
	private static final SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
	private static final SimpleDateFormat time = new SimpleDateFormat("HH.mm.ss");
	private static final SimpleDateFormat sdf2 = new SimpleDateFormat("dd-MM-yyyy");
	private NamedParameterJdbcTemplate npjt;
	private static final String getCrdateSql = "select crdate from snt.docstore where id = :docid";
	private static final String getCrtimeSql = "select crtime from snt.docstore where id = :docid";
	
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
		paramMap.put("docid", Long.parseLong(docId));
		Date portalDate = null;
		
		
		try{
			 portalDate =(Date) getNpjt().queryForObject(getCrdateSql, paramMap, Date.class);
		}catch(Exception e){
			log.error("DOCID = "+  Long.parseLong(docId));
			log.error(TypeConverter.exceptionToString(e));
		}
		Time portalTime = null;
		try{
			portalTime =(Time) getNpjt().queryForObject(getCrtimeSql, paramMap, Time.class);
		}catch(Exception e){
			log.error("DOCID = "+  Long.parseLong(docId));
			log.error(TypeConverter.exceptionToString(e));
		}
			String dateDocCustomer = sdf.format(portalDate);
			String timeDocCustomer = time.format(portalTime);
			
			if (sign.equals("1")){
				ActObjectNew actObjectNew = new ActObjectNew();
				
				actObjectNew.setIdRecipient(binder.getValue("idoper") + binder.getValue("cabinetIdRecv"));
				actObjectNew.setIdSeller(binder.getValue("idoper") + binder.getValue("cabinetIdSell"));
				
				
				actObjectNew.setNameOrganizationSeller(binder.getValue("nameoper"));
				actObjectNew.setIdEdoSeller(binder.getValue("idoper"));
				actObjectNew.setInnEntitySeller(binder.getValue("innoper"));
				
				
				/*Date dateDocDate = sdf2.parse(binder.getValue("dateDocAct"));
				String dateDoc = sdf.format(dateDocDate);
				Date timeDocDate = sdf2.parse(binder.getValue("timeDocAct"));
				String timeDoc = time.format(timeDocDate);*/
				String dateDoc = sdf.format(new Date());//переделали пока что так
				String timeDoc = time.format(new Date());//переделали пока что так
				Date actDate = sdf2.parse(binder.getValue("P_8"));
				String actDateFinal = sdf.format(actDate);
				actObjectNew.setDateDocExecutor(dateDoc);
				actObjectNew.setTimeDocExecutor(timeDoc);
				
				actObjectNew.setNameEconomicSubject(binder.getValue("P_4"));
				
				actObjectNew.setFactEconomicLife("Документ о передаче результатов работ (Документ об оказании услуг)");
				actObjectNew.setNameSender("Акт о передаче результатов работ (Акт об оказании услуг)");
				actObjectNew.setNumberDocPru(binder.getValue("P_7"));
				actObjectNew.setDateDocPru(actDateFinal);
				
				actObjectNew.setCodeOkb("643");
				actObjectNew.setNameOkb("Российский рубль");
				
				actObjectNew.setTitleSubstanceTransaction("Мы, нижеподписавшиеся, представитель ИСПОЛНИТЕЛЯ "
						+ "с одной стороны и представитель ЗАКАЗЧИКА с другой стороны, "
						+ "составили настоящий акт в том, что ИСПОЛНИТЕЛЬ выполнил, "
						+ "а ЗАКАЗЧИК принял следующие работы (услуги)");
				
				PartyType executor = new PartyType();
				
				executor.setOkpo(binder.getValue("P_5"));
				executor.setStructuralSubdivision(binder.getValue("P_4_1"));
				
				Entity entityExecutor = new Entity();
				
				entityExecutor.setNameOrganization(binder.getValue("P_4"));
				entityExecutor.setInn(binder.getValue("innIspoln"));
				entityExecutor.setKpp(binder.getValue("kppIspoln"));
				
				AddressINOAct addressInoExecutor = new AddressINOAct();
				addressInoExecutor.setCodeCountryIno("643");
				addressInoExecutor.setAddressTextIno(binder.getValue("P_4b"));
				

				
				executor.setEntity(entityExecutor);
				executor.setAddresINO(addressInoExecutor);
				actObjectNew.setExecutor(executor);
				
				PartyType customer = new PartyType();
				
				customer.setOkpo(binder.getValue("P_2"));
				
				Entity entityCustomer = new Entity();
				
				entityCustomer.setNameOrganization(binder.getValue("P_1"));
				entityCustomer.setInn(binder.getValue("P_39"));
				entityCustomer.setKpp(binder.getValue("P_40"));
				
				AddressINOAct addressInoCustomer = new AddressINOAct();
				addressInoCustomer.setCodeCountryIno("643");
				addressInoCustomer.setAddressTextIno(binder.getValue("P_1b"));
					
				customer.setEntity(entityCustomer);
				customer.setAddresINO(addressInoCustomer);
				actObjectNew.setCustomer(customer);
				
				List<Base> baseList = new ArrayList<Base>();
				
				Base base = new Base();
				base.setName("Договор");
				base.setNumber(binder.getValue("P_25b"));
				base.setDate(binder.getValue("P_25a"));
				base.setInfo(binder.getValue("P_25"));
				baseList.add(base);
				actObjectNew.setBaseList(baseList);
				
				final List<WorkObj> tableWorkObj = new ArrayList<WorkObj>();
				binder.handleTable("table1", "row", new RowHandler<Object>()
						{
							public void handleRow(DataBinder b, int rowNum, Object obj)
							{
								WorkObj rowWrkObj = new WorkObj();
								rowWrkObj.setNumber(String.valueOf(rowNum + 1));
								try {
									if (!b.getValue("P_13").isEmpty()) {
										rowWrkObj.setNameWorks(b.getValue("P_13"));
									}

									rowWrkObj.setNameUnits("шт.");
									rowWrkObj.setOkei("796");
									if (!b.getValue("P_16").isEmpty()) {
										rowWrkObj.setPrice(b.getBigDecimal("P_16"));
									}
									if (!b.getValue("P_15").isEmpty()) {
										rowWrkObj.setCount(b.getBigDecimal("P_15"));
									}
									if (!b.getValue("P_17").isEmpty()) {
										rowWrkObj.setSumWithoutNds(b.getBigDecimal("P_17"));
									}

									rowWrkObj.setTaxRate("18%");

									if (!b.getValue("P_18").isEmpty()) {
										rowWrkObj.setSumNds(b.getBigDecimal("P_18"));
									}
									if (!b.getValue("P_19").isEmpty()) {
										rowWrkObj.setSumWithtNds(b.getBigDecimal("P_19"));
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
				if (!binder.getValue("P_26").isEmpty()) {
					rowDescriptionWorks.setStartWorks(binder.getValue("P_26"));
				}
				if (!binder.getValue("P_27").isEmpty()) {
					rowDescriptionWorks.setEndWorks(binder.getValue("P_27"));
				}
				if (!binder.getValue("P_22").isEmpty()) {
					rowDescriptionWorks.setSumWithoutNdsInTotal(binder.getBigDecimal("P_22"));
				}
				if (!binder.getValue("P_23").isEmpty()) {
					rowDescriptionWorks.setSumNdsInTotal(binder.getBigDecimal("P_23"));
				}
				rowDescriptionWorks.setSumWithNdsInTotal(binder.getBigDecimal("P_24"));
				descriptionWorks.add(rowDescriptionWorks);
				
				actObjectNew.setDescriptionWorks(descriptionWorks);
				
				actObjectNew.setContentsOperation("Результаты работ переданы (услуги оказаны)");
				
				final FIO fio = FIO.parse(fioParam);
				List<Signer> signersList = new ArrayList<Signer>();
				Signer signer = new Signer();
				signer.setScopePowers(3);
				signer.setStatus(1);
				signer.setUnderAuthoritySignatory("Должностные обязанности");
				signer.setUnderAuthorityOrganization("Доверенность "+ binder.getValue("P_10v") +" от "+binder.getValue("P_10g") );
				Entity entitySigner = new Entity();
				
				entitySigner.setFio(fio);
				entitySigner.setInn(binder.getValue("innIspoln"));
				entitySigner.setNameOrganization(binder.getValue("P_4"));
				entitySigner.setPost(post);
				
				signer.setEntity(entitySigner);
				signersList.add(signer);
				actObjectNew.setSignersList(signersList);
				
				String generate = actObjectNew.Generate();
				// Подробная валидация.
				final XmlOptions options = new XmlOptions();
				final List<XmlValidationError> errorList = new ArrayList<XmlValidationError>();
	
				// Валидация полученного XML.
				final boolean valid = ru.aisa.actpart1New.ФайлDocument.Factory.parse(generate).validate(options.setErrorListener(errorList));
	
				// Убираем неймспейсы
				generate = generate.replaceAll(DocXmlGeneratorEndpoint.REGEXP, "");
	
				log.debug("Данный XML был сгенерирован для документа (ФПУ-26) id: " + docId + " ,номер подписи: " + sign + " {" + generate + "}");
	
				if (!valid)
				{
					throw new Exception("XML не прошел валидацию. {" + errorList + "}");
				}
	
				final String str = Base64.encodeBytes(generate.getBytes("windows-1251"), Base64.DONT_BREAK_LINES);
				resp.setXmlString(str);
				return resp;
				
				
			}else if (sign.equals("2")){
				Act2ObjectNew act2ObjectNew = new Act2ObjectNew();
				final FIO fio = FIO.parse(fioParam);
				/*Date dateDocDate = sdf2.parse(binder.getValue("dateDocAct"));
				String dateDoc = sdf.format(dateDocDate);
				Date timeDocDate = sdf2.parse(binder.getValue("timeDocAct"));
				String timeDoc = time.format(timeDocDate);*/
				String dateDoc = binder.getValue("dateDocAct");
				String timeDoc = binder.getValue("timeDocAct");
				Date actDate = sdf2.parse(binder.getValue("P_8"));
				String actDateFinal = sdf.format(actDate);
				
				act2ObjectNew.setIdRecipient(binder.getValue("idoper") + binder.getValue("cabinetIdRecv"));
				act2ObjectNew.setIdSender(binder.getValue("idoper") + binder.getValue("cabinetIdSell"));
				
				act2ObjectNew.setNameOrganizationSeller(binder.getValue("nameoper"));
				act2ObjectNew.setIdEdoSeller(binder.getValue("idoper"));
				act2ObjectNew.setInnEntitySeller(binder.getValue("innoper"));
				
				/*String dateDocCustomer = sdf.format(new Date());
				String timeDocCustomer = time.format(new Date());*/
				act2ObjectNew.setDateDocCustomer(dateDocCustomer);
				act2ObjectNew.setTimeDocCustomer(timeDocCustomer);
				act2ObjectNew.setNameEconomicSubject(binder.getValue("P_1"));
				
				act2ObjectNew.setIdInfoDocExecutor(binder.getValue("idFileAct"));
				act2ObjectNew.setDateInfoDocExecutor(dateDoc);
				act2ObjectNew.setTimeInfoDocExecutor(timeDoc);
				
				List<String> electronicSignatureList = new ArrayList<String>();
				electronicSignatureList.add(binder.getValue("sign1"));
				act2ObjectNew.setElectronicSignatureList(electronicSignatureList);
				
				act2ObjectNew.setNameDocIdentificationExecutor("Акт о передаче результатов работ (Акт об оказании услуг)");
				act2ObjectNew.setNumberDocPruExecutor(binder.getValue("P_7"));
				act2ObjectNew.setDateDocPruExecutor(actDateFinal);
				act2ObjectNew.setContentOperation("Результаты работ (оказанных услуг) приняты без претензий");
				
				List<Signer> signersList = new ArrayList<Signer>();
				Signer signer = new Signer();
				signer.setScopePowers(3);
				signer.setStatus(1);
				signer.setUnderAuthoritySignatory("Должностные обязанности");
				signer.setUnderAuthorityOrganization("Доверенность "+ binder.getValue("P_11v") +" от "+binder.getValue("P_11g") );
				Entity entitySigner = new Entity();
				
				entitySigner.setFio(fio);
				entitySigner.setInn(binder.getValue("P_39"));
				entitySigner.setNameOrganization(binder.getValue("P_1"));
				entitySigner.setPost(post);
				
				signer.setEntity(entitySigner);
				signersList.add(signer);
				act2ObjectNew.setSignersList(signersList);
				String generate2 = act2ObjectNew.Generate();
				// Подробная валидация.
				final XmlOptions options = new XmlOptions();
				final List<XmlValidationError> errorList = new ArrayList<XmlValidationError>();
	
				// Валидация полученного XML.
				final boolean valid = ФайлDocument.Factory.parse(generate2).validate(options.setErrorListener(errorList));
	
				// Убираем неймспейсы
				generate2 = generate2.replaceAll(DocXmlGeneratorEndpoint.REGEXP, "");
	
				log.debug("Данный XML был сгенерирован для документа (ФПУ-26) id: " + docId + " ,номер подписи: " + sign + " {" + generate2 + "}");
	
				if (!valid){
					throw new Exception("XML не прошел валидацию. {" + errorList + "}");
				}
				
				final String str = Base64.encodeBytes(generate2.getBytes("windows-1251"), Base64.DONT_BREAK_LINES);

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

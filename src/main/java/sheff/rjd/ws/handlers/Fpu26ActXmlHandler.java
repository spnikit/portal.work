package sheff.rjd.ws.handlers;

import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Level;
import org.apache.xmlbeans.XmlOptions;
import org.apache.xmlbeans.XmlValidationError;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import portal.iit.object.Act2ObjectNew;
import portal.iit.object.Entity;
import portal.iit.object.FIO;
import portal.iit.object.Signer;
import ru.aisa.actpart2New.ФайлDocument;
import ru.aisa.rgd.utils.Base64;
import ru.aisa.rgd.ws.utility.DataBinder;
import ru.aisa.rgd.ws.utility.TypeConverter;
import rzd.util.XmlGenByDocIdResponseDocument;
import sheff.rjd.gvcservice.DocXmlGeneratorEndpoint;

public class Fpu26ActXmlHandler implements FormHandler {

	private static final SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
	private static final SimpleDateFormat time = new SimpleDateFormat("HH.mm.ss");
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
			log.error(TypeConverter.exceptionToString(e));
		}
		Time portalTime = null;
		try{
			portalTime = (Time) getNpjt().queryForObject(getCrtimeSql, paramMap, Time.class);
		}catch(Exception e){
			log.error(TypeConverter.exceptionToString(e));
		}

		String dateDocCustomer = sdf.format(portalDate);
		String timeDocCustomer = time.format(portalTime);

		if (sign.equals("2")){
			Act2ObjectNew act2Object = new Act2ObjectNew();
			final FIO fio = FIO.parse(fioParam);


			act2Object.setIdRecipient(binder.getValue("idoper") + binder.getValue("cabinetIdRecv"));
			act2Object.setIdSender(binder.getValue("idoper") + binder.getValue("cabinetIdSell"));

			//Оператор
			//Начало
			act2Object.setNameOrganizationSeller(binder.getValue("nameoper"));
			act2Object.setIdEdoSeller(binder.getValue("idoper"));
			act2Object.setInnEntitySeller(binder.getValue("innoper"));
			//Конец

			act2Object.setDateDocCustomer(dateDocCustomer);
			act2Object.setTimeDocCustomer(timeDocCustomer);
			act2Object.setNameEconomicSubject(binder.getValue("org_zakaz"));

			act2Object.setIdInfoDocExecutor(binder.getValue("idFileAct"));
			act2Object.setDateInfoDocExecutor(binder.getValue("dateDocAct"));
			act2Object.setTimeInfoDocExecutor(binder.getValue("timeDocAct"));

			List<String> electronicSignatureList = new ArrayList<String>();
			electronicSignatureList.add(binder.getValue("sign1"));
			act2Object.setElectronicSignatureList(electronicSignatureList);

			act2Object.setNameDocIdentificationExecutor("Акт о передаче результатов работ (Акт об оказании услуг)");
			act2Object.setNumberDocPruExecutor(binder.getValue("num_akt"));
			act2Object.setDateDocPruExecutor(binder.getValue("date_akt"));
			act2Object.setContentOperation("Результаты работ (оказанных услуг) приняты без претензий");

			List<Signer> signersList = new ArrayList<Signer>();
			Signer signer = new Signer();
			signer.setScopePowers(3);
			signer.setStatus(1);
			signer.setUnderAuthoritySignatory("Должностные обязанности");
			signer.setUnderAuthorityOrganization(binder.getValue("na_osnov_isp") );
			Entity entitySigner = new Entity();

			entitySigner.setFio(fio);
			entitySigner.setInn(binder.getValue("innZakaz"));
			entitySigner.setNameOrganization(binder.getValue("org_zakaz"));
			entitySigner.setPost(post);

			signer.setEntity(entitySigner);
			signersList.add(signer);
			act2Object.setSignersList(signersList);

			String generate2 = act2Object.Generate();
			// Подробная валидация.
			final XmlOptions options = new XmlOptions();
			final List<XmlValidationError> errorList = new ArrayList<XmlValidationError>();

			// Валидация полученного XML.
			final boolean valid = ФайлDocument.Factory.parse(generate2).validate(options.setErrorListener(errorList));

			// Убираем неймспейсы
			generate2 = generate2.replaceAll(DocXmlGeneratorEndpoint.REGEXP, "");

			log.log(Level.DEBUG, "Данный XML был сгенерирован для документа (ФПУ-26) id: " + docId + " ,номер подписи: " + sign + " {" + generate2 + "}");

			if (!valid)
			{
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

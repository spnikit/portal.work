package sheff.rjd.ws.handlers;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Level;
import org.apache.xmlbeans.XmlOptions;
import org.apache.xmlbeans.XmlValidationError;

import portal.iit.object.Act2Object;
import portal.iit.object.ActObject;
import portal.iit.object.DescriptionWorks;
import portal.iit.object.Entity;
import portal.iit.object.FIO;
import portal.iit.object.ProxyExecutive;
import portal.iit.object.ProxyIssued;
import portal.iit.object.ProxyWhoIssued;
import portal.iit.object.SignatureArtist;
import portal.iit.object.WorkObj;
import ru.aisa.actpart2.ФайлDocument;
import ru.aisa.rgd.utils.Base64;
import ru.aisa.rgd.ws.exeption.InternalException;
import ru.aisa.rgd.ws.utility.DataBinder;
import ru.aisa.rgd.ws.utility.RowHandler;
import rzd.util.XmlGenByDocIdResponseDocument;
import sheff.rjd.gvcservice.DocXmlGeneratorEndpoint;

/**
 * Created by alexa on 11.09.15.
 */
public class FPU26XmlHandler implements FormHandler
{

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
		final String dateSign = massiv[2]; //Дата подписи
		final String sign = massiv[3]; // Подпись
		final String docId = (String) params[3]; // DocId

		if (sign.equals("1"))
		{
			ActObject actObject = new ActObject();

			actObject.setIdBuyer(binder.getValue("idoper") + binder.getValue("cabinetIdRecv"));
			actObject.setIdSeller(binder.getValue("idoper") + binder.getValue("cabinetIdSell"));

			actObject.setTitle(binder.getValue("T1"));

			String dateDoc = sdf.format(new Date());
			String timeDoc = time.format(new Date());
			actObject.setDateDoc(dateDoc);
			actObject.setTimeDoc(timeDoc);
			actObject.setDateSignatureExecutive(dateSign);

			//СвФЛ
			//Начало
			final FIO fio = FIO.parse(fioParam);
			actObject.setInfoNameIndividual("-");
			actObject.setInfoSurnameIndividual("-");
			actObject.setInfoMiddlenameIndividual("-");
			//Конец

			//Оператор
			//Начало
			actObject.setNameOrganizationSeller(binder.getValue("nameoper"));
			actObject.setIdEdoSeller(binder.getValue("idoper"));
			actObject.setInnEntitySeller(binder.getValue("innoper"));
			//Конец

			Date actDate = sdf2.parse(binder.getValue("P_8"));
			String actDateFinal = sdf.format(actDate);
			actObject.setTimeAct(actDateFinal);

			actObject.setNumberAct(binder.getValue("P_7"));

			//Исполнитель
			//Начало
			actObject.setPerformerOkpo(binder.getValue("P_5"));
			actObject.setInfoNameOrganization(binder.getValue("P_4"));
			actObject.setInfoInnEntity(binder.getValue("innIspoln"));
			actObject.setInfoKppEntity(binder.getValue("kppIspoln"));
			actObject.setAddressText(binder.getValue("P_4b"));
			//Конец

			//Описание работ
			//Начало
			final List<WorkObj> tableWorkObj = new ArrayList<WorkObj>();

			binder.handleTable("table1", "row", new RowHandler<Object>()
			{
				public void handleRow(DataBinder b, int rowNum, Object obj) throws InternalException
				{
					WorkObj rowWrkObj = new WorkObj();
					rowWrkObj.setNameUnits("шт");
					rowWrkObj.setNumber(String.valueOf(rowNum + 1));
					rowWrkObj.setNameWorks(b.getValue("P_13"));
					rowWrkObj.setOkei("796");
					if (!b.getValue("P_15").isEmpty())
						rowWrkObj.setCount(b.getBigDecimal("P_15"));
					if (!b.getValue("P_16").isEmpty())
						rowWrkObj.setPrice(b.getBigDecimal("P_16"));
					rowWrkObj.setSumWithoutNds(b.getBigDecimal("P_17"));
					rowWrkObj.setSumNds(b.getBigDecimal("P_18"));
					rowWrkObj.setSumWithtNds(b.getBigDecimal("P_19"));

					tableWorkObj.add(rowWrkObj);
				}

			}, null);

			List<DescriptionWorks> descriptionWorks = new ArrayList<DescriptionWorks>();

			DescriptionWorks rowDescriptionWorks = new DescriptionWorks();
			rowDescriptionWorks.setListWorksObj(tableWorkObj);
			rowDescriptionWorks.setSumWithoutNdsInTotal(binder.getBigDecimal("P_22"));
			rowDescriptionWorks.setSumNdsInTotal(binder.getBigDecimal("P_23"));
			rowDescriptionWorks.setSumWithNdsInTotal(binder.getBigDecimal("P_24"));
			rowDescriptionWorks.setStartWorks(binder.getValue("P_26"));
			rowDescriptionWorks.setEndWorks(binder.getValue("P_27"));

			descriptionWorks.add(rowDescriptionWorks);

			actObject.setDescriptionWorks(descriptionWorks);
			//Конец


			if (binder.getValue("P_10v").isEmpty())
			{
				//ПодписьИсполн  Начало
				SignatureArtist signatureArtist = new SignatureArtist();
				signatureArtist.setFio(fio);
				signatureArtist.setPost(post);
				actObject.setSignatureArtist(signatureArtist);
				//ПодписьИсполн  Конец
			} else
			{
				//Доверенность
				//Начало
				ProxyExecutive proxyExecutiveP = new ProxyExecutive();

				ProxyIssued proxyIssued = new ProxyIssued();
				proxyIssued.setFio(fio);
				proxyIssued.setPost(post);
				ProxyWhoIssued proxyWhoIssued = new ProxyWhoIssued();
				FIO whoIssuedIspolnFio = new FIO();
				whoIssuedIspolnFio.setName(binder.getValue("NIssuedDovIspoln"));
				whoIssuedIspolnFio.setSurname(binder.getValue("SNIssuedDovIspoln"));
				whoIssuedIspolnFio.setMiddlename(binder.getValue("MNIssuedDovIspoln"));
				proxyWhoIssued.setPost(binder.getValue("DoljIssuedDovIspoln"));
				proxyWhoIssued.setNameOrganization(binder.getValue("NaimOrgIssuedDovIspoln"));
				proxyWhoIssued.setInfo(binder.getValue("DopInfoDovIspoln"));
				proxyWhoIssued.setFio(whoIssuedIspolnFio);

				proxyExecutiveP.setNumberProxy(binder.getValue("P_10v"));
				proxyExecutiveP.setDateProxy(binder.getValue("P_10g"));
				proxyExecutiveP.setProxyIssued(proxyIssued);
				proxyExecutiveP.setProxyWhoIssued(proxyWhoIssued);

				actObject.setProxyExecutive(proxyExecutiveP);
				//Конец
			}

			//Подписант первый - юр. лицо
			//Начало
			final Entity signerEntity1 = new Entity();
			signerEntity1.setNameOrganization(actObject.getInfoNameOrganization());

			signerEntity1.setFio(fio);
			signerEntity1.setPost(post);
			signerEntity1.setInn(actObject.getInfoInnEntity());
			actObject.setEntity(signerEntity1);
			//Конец

			String generate = actObject.Generate();
			// Подробная валидация.
			final XmlOptions options = new XmlOptions();
			final List<XmlValidationError> errorList = new ArrayList<XmlValidationError>();

			// Валидация полученного XML.
			final boolean valid = ru.aisa.actpart1.ФайлDocument.Factory.parse(generate).validate(options.setErrorListener(errorList));

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
		} else if (sign.equals("2"))
		{
			Act2Object act2Object = new Act2Object();
			final FIO fio = FIO.parse(fioParam);

			act2Object.setIdBuyer(binder.getValue("idoper") + binder.getValue("cabinetIdRecv"));
			act2Object.setIdSender(binder.getValue("idoper") + binder.getValue("cabinetIdSell"));

			String dateDoc = sdf.format(new Date());
			String timeDoc = time.format(new Date());
			act2Object.setDateDoc(dateDoc);
			act2Object.setTimeDoc(timeDoc);
			act2Object.setDateSignatureActCustomer(dateSign);


			//Оператор
			//Начало
			act2Object.setNameOrganization(binder.getValue("nameoper"));
			act2Object.setIdEdo(binder.getValue("idoper"));
			act2Object.setInn(binder.getValue("innoper"));

			//Конец

			act2Object.setIdFileActImplementation(binder.getValue("idFileAct"));
			act2Object.setDateDocActImplementation(binder.getValue("dateDocAct"));
			act2Object.setTimeDocActImplementation(binder.getValue("timeDocAct"));

			act2Object.setNumberAct(binder.getValue("P_7"));

			Date actDate = sdf2.parse(binder.getValue("P_8"));
			String actDateFinal = sdf.format(actDate);
			act2Object.setDateAct(actDateFinal);


			if (binder.getValue("P_11v").isEmpty())
			{
				//ПодписьИсполн  Начало
				SignatureArtist signatureArtist = new SignatureArtist();
				signatureArtist.setFio(fio);
				signatureArtist.setPost(post);
				act2Object.setSignatureArtist(signatureArtist);
				//ПодписьИсполн  Конец
			} else
			{
				//Доверенность
				//Начало
				ProxyExecutive proxyExecutive = new ProxyExecutive();
				ProxyIssued proxyIssued = new ProxyIssued();
				ProxyWhoIssued proxyWhoIssued = new ProxyWhoIssued();
				FIO customerWhoIssuedFio = new FIO();

				customerWhoIssuedFio.setName(binder.getValue("NIssuedDovBuyer"));
				customerWhoIssuedFio.setMiddlename(binder.getValue("MNIssuedDovBuyer"));
				customerWhoIssuedFio.setSurname(binder.getValue("SNIssuedDovBuyer"));

				proxyIssued.setFio(fio);
				proxyIssued.setPost(post);

				proxyWhoIssued.setPost(binder.getValue("DoljIssuedDovBuyer"));
				proxyWhoIssued.setInfo(binder.getValue("DopInfoDovBuyer"));
				proxyWhoIssued.setNameOrganization(binder.getValue("NaimOrgIssuedDovBuyer"));
				proxyWhoIssued.setFio(customerWhoIssuedFio);

				proxyExecutive.setProxyIssued(proxyIssued);
				proxyExecutive.setProxyWhoIssued(proxyWhoIssued);
				proxyExecutive.setNumberProxy(binder.getValue("P_11v"));
				proxyExecutive.setDateProxy(binder.getValue("P_11g"));
				act2Object.setProxyExecutive(proxyExecutive);
				//Конец
			}

			//Подписант второй - юр. лицо
			//Начало
			final Entity signerEntity2 = new Entity();
			signerEntity2.setNameOrganization(binder.getValue("P_1"));
			signerEntity2.setInn(binder.getValue("P_39"));

			signerEntity2.setFio(fio);
			signerEntity2.setPost(post);
			act2Object.setEntity(signerEntity2);
			//Конец

			String generate2 = act2Object.Generate();
			// Подробная валидация.
			final XmlOptions options = new XmlOptions();
			final List<XmlValidationError> errorList = new ArrayList<XmlValidationError>();

			// Валидация полученного XML.
			final boolean valid = ФайлDocument.Factory.parse(generate2).validate(options.setErrorListener(errorList));

			// Убираем неймспейсы
			generate2 = generate2.replaceAll(DocXmlGeneratorEndpoint.REGEXP, "");

			log.debug("Данный XML был сгенерирован для документа (ФПУ-26) id: " + docId + " ,номер подписи: " + sign + " {" + generate2 + "}");

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

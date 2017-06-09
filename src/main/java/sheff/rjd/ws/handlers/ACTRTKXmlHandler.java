package sheff.rjd.ws.handlers;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.xmlbeans.XmlOptions;
import org.apache.xmlbeans.XmlValidationError;

import portal.iit.object.ActObject;
import portal.iit.object.DescriptionWorks;
import portal.iit.object.Entity;
import portal.iit.object.FIO;
import portal.iit.object.SignatureArtist;
import portal.iit.object.WorkObj;
import ru.aisa.rgd.utils.Base64;
import ru.aisa.rgd.ws.exeption.InternalException;
import ru.aisa.rgd.ws.utility.DataBinder;
import ru.aisa.rgd.ws.utility.RowHandler;
import rzd.util.XmlGenByDocIdResponseDocument;
import sheff.rjd.gvcservice.DocXmlGeneratorEndpoint;


public class ACTRTKXmlHandler implements FormHandler
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

		if (sign.equals("1")) {
			ActObject actObject = new ActObject();

			actObject.setIdBuyer(binder.getValue("idoper") + binder.getValue("cabinetIdRecv"));
			actObject.setIdSeller(binder.getValue("idoper") + binder.getValue("cabinetIdSell"));

//			actObject.setIdBuyer("2JH001");
//			actObject.setIdSeller("2JH011");
			
			actObject.setNameOrganizationSeller(binder.getValue("nameoper"));
			actObject.setIdEdoSeller(binder.getValue("idoper"));
			actObject.setInnEntitySeller(binder.getValue("innoper"));
			
//			actObject.setNameOrganizationSeller("ООО \"ИИТ\"");
//			actObject.setIdEdoSeller("2JH");
//			actObject.setInnEntitySeller("7708713259");
			actObject.setTitle("Мы, нижеподписавшиеся, представитель ИСПОЛНИТЕЛЯ,"
					+ " с одной стороны и представитель ЗАКАЗЧИКА с другой стороны, "
					+ "составили настоящий акт в том, что ИСПОЛНИТЕЛЬ выполнил, а "
					+ "ЗАКАЗЧИК принял следующие работы (услуги)");

			String dateDoc = sdf.format(new Date());
			String timeDoc = time.format(new Date());
			actObject.setDateDoc(dateDoc);
			actObject.setTimeDoc(timeDoc);
			actObject.setDateSignatureExecutive(dateSign);
			actObject.setTimeAct(binder.getValue("P_49"));
			final List<WorkObj> tableWorkObj = new ArrayList<WorkObj>();
			
			binder.handleTable("table", "row", new RowHandler<Object>()
			{
				public void handleRow(DataBinder b, int rowNum, Object obj) throws InternalException
				{
					WorkObj rowWrkObj = new WorkObj();
					rowWrkObj.setNameUnits(b.getValue("P_13"));
					rowWrkObj.setNumber(String.valueOf(rowNum + 1));
					rowWrkObj.setNameWorks(b.getValue("P_11"));
					if (!b.getValue("P_12").isEmpty()) {
						rowWrkObj.setCount(b.getBigDecimal("P_12"));
					}
					if (!b.getValue("P_14").isEmpty()) {
						rowWrkObj.setPrice(b.getBigDecimal("P_14")); 
					}
					if (!b.getValue("P_16").isEmpty()) {
                        if(b.getValue("P_16").matches("[0-9]{0,10}\\.[0-9]{2}")){
                        	rowWrkObj.setSumNds(b.getBigDecimal("P_16"));
                        }
					}
					if (!b.getValue("P_17").isEmpty()) {
						rowWrkObj.setSumWithtNds(b.getBigDecimal("P_17"));
					}
					tableWorkObj.add(rowWrkObj);
				}
			}, null);
			List<DescriptionWorks> descriptionWorks = new ArrayList<DescriptionWorks>();
			DescriptionWorks rowDescriptionWorks = new DescriptionWorks();
			rowDescriptionWorks.setListWorksObj(tableWorkObj);
			rowDescriptionWorks.setSumWithoutNdsInTotal(binder.getBigDecimal("P_18"));
			rowDescriptionWorks.setSumWithNdsInTotal(binder.getBigDecimal("P_19"));
			rowDescriptionWorks.setSumNdsInTotal(binder.getBigDecimal("P_20"));
			descriptionWorks.add(rowDescriptionWorks);
			actObject.setDescriptionWorks(descriptionWorks);
			//СвФЛ
			//Начало
			final FIO fio = FIO.parse(fioParam);
			actObject.setInfoNameIndividual("-");
			actObject.setInfoSurnameIndividual("-");
			actObject.setInfoMiddlenameIndividual("-");
//			//Конец

//			Date actDate = sdf2.parse(binder.getValue("P_8"));
//			String actDateFinal = sdf.format(actDate);
//			actObject.setTimeAct(actDateFinal);
//
			actObject.setNumberAct(binder.getValue("P_2"));
//
//			//Исполнитель
//			//Начало
			actObject.setInfoNameOrganization(binder.getValue("P_6"));
			actObject.setInfoInnEntity(binder.getValue("P_31"));
			actObject.setInfoKppEntity(binder.getValue("P_32"));
//			//Конец
//
		
			//Подписант первый - юр. лицо
			//Начало
			final Entity signerEntity1 = new Entity();
			signerEntity1.setNameOrganization(actObject.getInfoNameOrganization());
			signerEntity1.setFio(fio);
			signerEntity1.setPost(post);
			signerEntity1.setInn(actObject.getInfoInnEntity());
			actObject.setEntity(signerEntity1);
			
			final SignatureArtist signartist = new SignatureArtist();
			signartist.setFio(fio);
			signartist.setPost(post);
			actObject.setSignatureArtist(signartist);
			
			//Конец
		    String generate = actObject.Generate();  
//			// Подробная валидация.
			final XmlOptions options = new XmlOptions();
			final List<XmlValidationError> errorList = new ArrayList<XmlValidationError>();

			// Валидация полученного XML.
			final boolean valid = ru.aisa.actpart1.ФайлDocument.Factory.parse(generate).validate(options.setErrorListener(errorList));

			// Убираем неймспейсы
			generate = generate.replaceAll(DocXmlGeneratorEndpoint.REGEXP, "");

			log.debug("Данный XML был сгенерирован для документа (Акт РТК) id: " + docId + " ,номер подписи: " + sign + " {" + generate + "}");

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

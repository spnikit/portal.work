package ru.aisa.rgd.ws.client;

import java.text.SimpleDateFormat;
import java.util.List;

import org.apache.log4j.Logger;

import ru.aisa.rgd.ws.documents.ETDForm;
import ru.aisa.rgd.ws.utility.SimpleXmlParser;
import rzd8888.gvc.etd.was.etd.sign.SignRequestDocument;
import rzd8888.gvc.etd.was.etd.sign.Signature;
import sheff.rjd.dispatcher.Dispatcher;

/**
 * @author anton
 *
 */
public class ETDStandartSignification extends SignificationTemplate {

	private String formType; //Тип формы
	private String formNumber; //Номер, подтип
	private int allSignatures; //Общее количество подписей в форме, сведетельствующее
	//о завершении ее оформления
	
	//Форматирование даты и времени
	private static SimpleDateFormat dateFormater = new SimpleDateFormat("dd.MM.yyyy");
	private static SimpleDateFormat timeFormater = new SimpleDateFormat("HH:mm");
	private static Logger log = Logger.getLogger(Dispatcher.class);
	
	public int getAllSignatures() {
		return allSignatures;
	}

	public void setAllSignatures(int allSignatures) {
		this.allSignatures = allSignatures;
	}

	public String getFormNumber() {
		return formNumber;
	}

	public void setFormNumber(String formNumber) {
		this.formNumber = formNumber;
	}

	public String getFormType() {
		return formType;
	}

	public void setFormType(String formType) {
		this.formType = formType;
	}

	
	/**
	 * Если в уведомлении используется дополнительный элемент EXT, то метод необходимо переопределить.
	 * @param xml Данные формы
	 * @return Cодержимое элемента EXT уведомления
	 * @default null
	 */
	public String composeExt(String xml, Long docid){
		return null;
	}
	
	/**
	 * Если в уведомлении используется  элемент NO, то метод необходимо переопределить.
	 * @param xml Данные формы
	 * @return Какой-то номер документа )
	 * @default null
	 */
	public Integer composeNo(String xml){
		return null;
	}
	
	/**
	 * По умолчанию получает флаг созданный DataBinder.setStageFlag()
	 * @param xml Данные формы
	 * @return Очередь оформления документа
	 */
	public int getStage(String xml)
	{
		return SimpleXmlParser.getStageFlag(xml);
	}
	/**
	 * По умолчанию получает флаг созданный DataBinder.setStageFlag()
	 * @param docId идентификатор документа
	 * @param xml Данные формы
	 * @param signsNumber номер подписи
	 * @param allSignatures количество подписей
	 * @param docurl url документа
	 * @param predkod предприятие приписки документа
	 * @param url адрес отправки
	 * @return Очередь оформления документа
	 */
	
	public void execute(long docId, String xml, int signsNumber,int allSignatures, String docurl, Integer predid, String url, int trynumber)	throws Exception 
	{
		if (getDispatch().equalsIgnoreCase("yes"))
		{
			String urls = getFacade().getUrlByDocumentId(predid);
			if (url.length() <= 2)
			{
				throw new RuntimeException("Form " + getFormname() + " dispatcher hasn't URL");
			}
			else
			{
				log.debug("Sending notification for from " + getFormname() + " docId " + docId +" on url " + url);
				SignRequestDocument requestDocument = SignRequestDocument.Factory.newInstance();
				SignRequestDocument.SignRequest req =  requestDocument.addNewSignRequest();
				
				//URL системы в которую необходимо отправить сообщение
				req.setPredurl(urls);
				//Идентификатор документа в системе АС ЭТД.
				req.setDocid(docId);
				//Признак окончания оформления документа.
				req.setFinished(isFinished(xml, signsNumber,allSignatures) ? 1 : 0);
				//Номер наименования формы
				req.setFormnum(getFormNumber());
				//Наименование хозяйства
				req.setFormtype(getFormType());
				//URL документа
				req.setUrl(docurl);
				//kod предприятия
				req.setPredid(predid);	
				//SignRequest / number	Номер документа
				Integer no = composeNo(xml);
				if (no != null){
					req.setNumber(no);
				}
				
				if (getStage(xml) == ETDForm.STAGE_SERVICE)
				{
					ru.aisa.rgd.ws.domain.Signature signature = getFacade().getDocumentLastSignature(docId);
					Signature reqSignature = req.addNewSignature();
					reqSignature.setFio(signature.getFio());
					reqSignature.setFioid(signature.getPersId());
					reqSignature.setSigndate(dateFormater.format(signature.getDate()));
					reqSignature.setSigntime(timeFormater.format(signature.getDate()));
					reqSignature.setSignnum(signsNumber);
				}
				else //if (getFacade().isDocumentFinallySigned(docId))
				{
					List<ru.aisa.rgd.ws.domain.Signature> signatures = getFacade().getDocumentSignatures(docId);
					for (ru.aisa.rgd.ws.domain.Signature signature : signatures)
					{
						Signature reqSignature = req.addNewSignature();
						reqSignature.setFio(signature.getFio());
						reqSignature.setFioid(signature.getPersId());
						reqSignature.setSigndate(dateFormater.format(signature.getDate()));
						reqSignature.setSigntime(timeFormater.format(signature.getDate()));
						reqSignature.setSignnum(signature.getOrder());
					}
				}
//				else
//				{
//					logger.debug("Notification didn't send becouse document not finally signed");
//					return;
//				}
				
				String ext = composeExt(xml, docId);
				if (ext != null){
					req.setExttext(ext);
				}
				
				Dispatcher.WstSend(getWst(), url, docId, requestDocument, "trynumber = "+trynumber);
			}
		}
	}

	@Override
	public boolean isFinished(String xml, int signsNumber,int allSignatures)
	{
		return (signsNumber == allSignatures);
	}	
	
	
	
	public void executeUS(long docId, String xml, int signsNumber,int allSignatures, String docurl, Integer predkod)	throws Exception 
	{
		//nothing
	}
	
	@Override
	public void execute(long docId, String xml, int signsNumber, String docurl, Integer predid, String url, int trynumber) throws Exception {
		execute(docId, xml, signsNumber, getAllSignatures(), docurl, predid, url, trynumber);
	}

	@Override
	public void executeUS(long docId, String xml, int signsNumber,
			String docurl, Integer predkod) throws Exception {
		executeUS(docId, xml, signsNumber, getAllSignatures(), docurl, predkod);
		
	}
}

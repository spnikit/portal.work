package sheff.rjd.gvcservice;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.oxm.Marshaller;
import org.springframework.ws.server.endpoint.AbstractMarshallingPayloadEndpoint;

import ru.aisa.rgd.ws.dao.ServiceFacade;
import ru.aisa.rgd.ws.documents.ETDForm;
import ru.aisa.rgd.ws.domain.Document;
import ru.aisa.rgd.ws.utility.DataBinder;
import ru.aisa.rgd.ws.utility.TypeConverter;
import rzd.util.XmlGenByDocIdResponseDocument;
import sheff.rjd.ws.handlers.ACTRTKXmlHandler;
import sheff.rjd.ws.handlers.CSFXmlHandler;
import sheff.rjd.ws.handlers.FPU26XmlHandler;
import sheff.rjd.ws.handlers.FPU26XmlHandlerNew;
import sheff.rjd.ws.handlers.FormHandler;
import sheff.rjd.ws.handlers.Fpu26ActXmlHandler;
import sheff.rjd.ws.handlers.SFXmlHandler;
import sheff.rjd.ws.handlers.TORG12XmlHandler;
import sheff.rjd.ws.handlers.TORG12XmlHandlerNew;
import sheff.rjd.ws.handlers.UPDSfXmlHandler;
import sheff.rjd.ws.handlers.UPDTorg12XmlHandler;

/**
 * Created by xblx on 28.04.15.
 *
 */
public class DocXmlGeneratorEndpoint extends AbstractMarshallingPayloadEndpoint
{
	public static final String REGEXP="xmlns=\".*\"";
	private static final int OK = 0;
	private static final int ERROR = 1;
	private static final Logger logger = Logger.getLogger(DocXmlGeneratorEndpoint.class);
	//private static final Map<String, Class<? extends FormHandler>> handlerMap;
	private static Map<String, FormHandler> handlerMap;
	
	
	
	private ServiceFacade facade;
	public void setFacade(ServiceFacade facade)
	{
		this.facade = facade;
	}

// Поменял параметры DocXmlGeneratorEndpoint теперь в sfHandler по новому образцу, раньше был класс SFXmlHandler.
	
	public DocXmlGeneratorEndpoint(Marshaller marshaller, UPDSfXmlHandler sfHandler, 
								CSFXmlHandler csfHandler, FPU26XmlHandler fpu26Handler, 
								ACTRTKXmlHandler actrtcHandler, TORG12XmlHandler torg12Handler, Fpu26ActXmlHandler fpu26actHandler)
	{
		super(marshaller);
		handlerMap = new HashMap<String, FormHandler>();
		
		//ТОР
//		handlerMap.put("Счет-фактура", sfHandler);
		handlerMap.put("Счет-фактура", sfHandler);
		handlerMap.put("Счет-фактура ЦСС", sfHandler);
		handlerMap.put("Счет-фактура РТК", sfHandler);
		handlerMap.put("Корректировочный счет-фактура", csfHandler);
		handlerMap.put("Корректировочный счет-фактура РТК", csfHandler);
//		handlerMap.put("ТОРГ-12", new UPDTorg12XmlHandler());
		handlerMap.put("ТОРГ-12", torg12Handler);
		handlerMap.put("ФПУ-26", fpu26Handler);
		handlerMap.put("Акт РТК", actrtcHandler);
		handlerMap.put("ФПУ-26 АСР", fpu26actHandler);
		
	}

	public DocXmlGeneratorEndpoint()
	{
		super();
	}
	
	
	
	/*static 
	{
		handlerMap = new HashMap<String, Class<? extends FormHandler>>();
		
		//ТОР
		handlerMap.put("Счет-фактура", SFXmlHandler.class);
		handlerMap.put("Счет-фактура ЦСС", SFXmlHandler.class);
		handlerMap.put("Счет-фактура РТК", SFXmlHandler.class);
		handlerMap.put("Корректировочный счет-фактура", CSFXmlHandler.class);
		handlerMap.put("Корректировочный счет-фактура РТК", CSFXmlHandler.class);
		handlerMap.put("ТОРГ-12", TORG12XmlHandler.class);
		handlerMap.put("ФПУ-26", FPU26XmlHandlerNew.class);
		handlerMap.put("Акт РТК", ACTRTKXmlHandler.class);
		
	}*/
	
	//Создаем объект обработчика
	private static FormHandler getHandler(String type)
	{
		type = type.trim();
		FormHandler handlerObject = null;
		
		logger.debug("Get handler for type: <" + type + ">");
		
		if (handlerMap.containsKey(type))
		{
			/*try 
			{*/
				//handlerObject = handlerMap.get(type).newInstance();
				handlerObject = handlerMap.get(type);
				//logger.debug("Resived handler: <" + handlerMap.get(type).getName() + ">");
				logger.debug("Resived handler: <" + handlerMap.get(type).getClass() + ">");
			/*} 
			catch (InstantiationException e) 
			{
				logger.error(TypeConverter.exceptionToString(e));
			}
			catch (IllegalAccessException e) 
			{
				logger.error(TypeConverter.exceptionToString(e));
			}*/
		}
		else
		{
			logger.debug("Handler didn't set");
		}
		return handlerObject;
	}
	@Override
	protected Object invokeInternal(Object o) throws Exception
	{
//		System.out.println(o);
		final DataBinder rBinder = new DataBinder(o.toString());
 
		final String params = rBinder.getValue("params");
		final long docId = Long.parseLong(rBinder.getValue("documentId"));
		final String docidStr = rBinder.getValue("documentId");
		String portalIdStr = "";
		long portalId = -1;
//		try{
//		portalId = Long.parseLong(rBinder.getValue("portalId"));
//		portalIdStr = rBinder.getValue("portalId");
//			
//		}catch (Exception e){
//			logger.error("No tag portalId in request for XML");
//		}
//		Document doc = null;
//		if (portalIdStr.length()>0){
//			doc = facade.getDocumentById(portalId);
//		}
//		else {
//			doc = facade.getDocumentDao().getByEtdId(docId);
//		}
		Document doc = facade.getDocumentById(docId);
		ETDForm form = ETDForm.createFromArchive(doc.getBlDoc());
		final DataBinder binder = form.getBinder();
		final XmlGenByDocIdResponseDocument respDoc = XmlGenByDocIdResponseDocument.Factory.newInstance();
		final XmlGenByDocIdResponseDocument.XmlGenByDocIdResponse resp = respDoc.addNewXmlGenByDocIdResponse();

		try{
			FormHandler handler = getHandler(doc.getType());
			Object result  = handler.handle(binder, resp, params, docidStr);
			resp.setCode(OK);
			resp.setMessage("Сгенерированный XML прошел валидацию. Ошибок нет.");
		} catch (Exception e){
			logger.error(e.getMessage(), e);
			resp.setCode(ERROR);
			resp.setMessage(e.getMessage() + " " + Calendar.getInstance().getTime());
			resp.setXmlString(null);
		}
		
		
		return respDoc;
	}
}

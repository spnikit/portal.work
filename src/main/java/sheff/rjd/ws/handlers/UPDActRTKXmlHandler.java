package sheff.rjd.ws.handlers;

import ru.aisa.rgd.ws.utility.DataBinder;
import rzd.util.XmlGenByDocIdResponseDocument;

public class UPDActRTKXmlHandler implements FormHandler
{

	public Object handle(Object... params) throws Exception {
		
		final DataBinder binder = (DataBinder) params[0];
		final XmlGenByDocIdResponseDocument.XmlGenByDocIdResponse resp = (XmlGenByDocIdResponseDocument.XmlGenByDocIdResponse) params[1];
		final String param = (String) params[2]; // Параметры
		final String[] massiv = param.split(";");
		final String fioParam = massiv[0]; // ФИО
		final String post = massiv[1]; // Должность
		final String sign = massiv[2]; // Подпись
		final String docId = (String) params[3]; // DocId
		return null;
	}

	public int getType() {
		return FormHandler.XML_GENERATOR;
	}

}

package sheff.rgd.ws.Abstract;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import javax.xml.transform.TransformerException;

import org.w3c.dom.DOMException;

import ru.aisa.rgd.etd.objects.ETDDocumentData;
import ru.aisa.rgd.ws.exeption.InternalException;
import ru.aisa.rgd.ws.exeption.ServiceException;
import sheff.rjd.services.syncutils.SyncObj;

public interface Action {
	
	public void setParentform(String parentform);

	public String getParentform();
	
	public ETDDocumentData EditBeforeOpen(String docName, ETDDocumentData doc) throws UnsupportedEncodingException, ServiceException, IOException, DOMException, InternalException, TransformerException;
	
	public void doAfterSign(String docName, String docdata, int predid, int signNumber, long id, String certserial, int WrkId);

	public void doAfterSync(String formname, SyncObj syncobj, String sql, int signum) throws ServiceException, IOException;
	
	public void doAfterSave(String docName, String docdata, int predid, int signNumber, String CertID, long id,int WrkId) throws Exception;
	
	
}

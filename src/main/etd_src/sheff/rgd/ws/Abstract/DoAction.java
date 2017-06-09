package sheff.rgd.ws.Abstract;


import java.io.IOException;
import java.io.UnsupportedEncodingException;

import javax.xml.transform.TransformerException;

import org.w3c.dom.DOMException;

import ru.aisa.rgd.etd.objects.ETDDocumentData;
import ru.aisa.rgd.ws.exeption.InternalException;
import ru.aisa.rgd.ws.exeption.ServiceException;
import sheff.rjd.services.syncutils.SyncObj;


public class DoAction {
	
    private Action[] docArray;
	//private final Logger log = Logger.getLogger(getClass());
	
	public void setDocArray(Action[] docArray) {
		this.docArray = docArray;
	}

	public Action[] getDocArray() {
		return docArray;
	}	

	
	
	public ETDDocumentData EditBeforeOpen(String docName, ETDDocumentData doc) throws UnsupportedEncodingException, ServiceException, IOException, DOMException, InternalException, TransformerException{
		{
			for(Action action:docArray){
				if(docName.equals(action.getParentform())){
					doc = action.EditBeforeOpen(docName,doc);
					break;
					}
				}		 
		}
		return doc; 
	}
	
	public void doAfterSign(String docName, String docdata, int predid, int signNumber, long id, String certserial ,int WrkId) throws Exception 
	{
		for(Action action:docArray){
			if(docName.equals(action.getParentform())){
				action.doAfterSign(docName, docdata, predid, signNumber, id, certserial, WrkId);
				break;
				}
			}		 
	} 
	
	/**
	 * @param docName - Имя документа
	 * @param docdata - String от BLDOC
	 * @param predid - ИД предприятия
	 * @param signNumber - количество подписей
	 * @param CertID - номер сертификата (x16)
	 * @param id - ИД документа на Портале
	 * @param WrkId - ИД должности
	 * @throws Exception
	 */
	public void doAfterSave(String docName, String docdata, int predid, int signNumber, String CertID, long id, int WrkId) throws Exception 
	{
		for(Action action:docArray){
			if(docName.equals(action.getParentform())){
				action.doAfterSave(docName, docdata, predid, signNumber, CertID, id,WrkId);
				break;
				}
			}		
	}
	
	public void doAfterSync(String formname, SyncObj syncobj, String sql, int signum) throws Exception 
	{
		for(Action action:docArray){
			if(formname.equals(action.getParentform())){
				action.doAfterSync(formname, syncobj, sql, signum);
				break;
				}
			}		 
	} 
	
	
}

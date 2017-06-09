package sheff.rjd.services.syncutils;

import java.io.PrintWriter;
import java.io.StringWriter;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import ru.aisa.etdportal.contenthandler.ContentHandlerFactory;
import ru.aisa.etdportal.vagonhandler.VagnumHandlerFactory;
import ru.aisa.rgd.ws.utility.TypeConverter;
import sheff.rjd.ws.OCO.TORLists;

public class GetContent {
	private String[] vagnum;
	private static Log log = LogFactory
			.getLog(GetContent.class);
	
	
	public SyncObj getContent(SyncParamObject obj, String formname, SyncObj sync, String formblob){
		
		getContentval(obj, formname, sync, formblob);
		getvagnum(formname, sync, formblob);
		return sync;
	}
	
	
	private SyncObj getContentval(SyncParamObject obj, String formname, SyncObj sync, String formblob){
		
		if (ContentHandlerFactory.handlerExist(formname))
			obj.setContent(ContentHandlerFactory.handleForm(
					formname, formblob));

		if (formname.equals(TORLists.Package)
				&& obj.getContent().contains("zzz")) {
			String[] split = obj.getContent().split("zzz");
			if (!" , , ".equals(split[0]))
				obj.setContent(split[0]);
			else
				obj.setContent(obj.getId_pak());
			obj.setDogovor(split[1]);
		}

		sync.setDogovor(obj.getDogovor());
		if (!formname.equals(TORLists.SF)) {
			if (obj.getContent().length() > 0) {

				sync.setContent(obj.getContent());
			} else {

				sync.setContent(obj.getId_pak());
			}
		}
		
		
		return sync;
	}
	
	
	private SyncObj getvagnum(String formname, SyncObj syncobj, String formblob){
		
		try {
			if (VagnumHandlerFactory.handlerExist(formname)) {

				vagnum = VagnumHandlerFactory.handleForm(formname,
						formblob);

				if (vagnum[0].length() > 0) {

					syncobj.setVagnum(vagnum[0]);
				} else {

					syncobj.setVagnum(null);
				}

				if (!vagnum[1].equals("nain")) {

					syncobj.setRepdate(vagnum[1]);

				} else {

					syncobj.setRepdate(null);
				}
			} else {

				syncobj.setVagnum(null);
				syncobj.setRepdate(null);
			}
		} catch (Exception e) {
			log.error(TypeConverter.exceptionToString(e)+" for document with etdid = "+syncobj.getEtdid());
			syncobj.setVagnum(null);
			syncobj.setRepdate(null);
		}
		
		
		return syncobj;
		
	}
}

package sheff.rgd.ws.Controllers.PPS;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;

import org.apache.log4j.Logger;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import ru.aisa.rgd.etd.dao.ETDSyncServiceFacade;
import ru.aisa.rgd.etd.objects.ETDDocumentData;
import ru.aisa.rgd.ws.documents.ETDForm;
import ru.aisa.rgd.ws.exeption.InternalException;
import ru.aisa.rgd.ws.exeption.ServiceException;
import ru.aisa.rgd.ws.utility.DataBinder;
import ru.aisa.rgd.ws.utility.TypeConverter;
import sheff.rgd.ws.Abstract.AbstractAction;
import sheff.rjd.services.syncutils.SyncObj;

public class MOController extends AbstractAction{

	private String parentform;
	private ETDSyncServiceFacade etdsyncfacade;
	private NamedParameterJdbcTemplate npjt;
	protected final Logger	log	= Logger.getLogger(getClass());
	public NamedParameterJdbcTemplate getNpjt() {
		return npjt;
	}

	public void setNpjt(NamedParameterJdbcTemplate npjt) {
		this.npjt = npjt;
	}

	public String getParentform() {
		return parentform;
	}

	public ETDSyncServiceFacade getEtdsyncfacade() {
		return etdsyncfacade;
	}

	public void setEtdsyncfacade(ETDSyncServiceFacade etdsyncfacade) {
		this.etdsyncfacade = etdsyncfacade;
	}

	public void setParentform(String parentform) {
		this.parentform = parentform;
	}


	@Override
	public void doAfterSign(String docName, String docdata, int predid, int signNumber, long id, String certserial, int WrkId) {
		
	}

	@Override
	public void doAfterSave(String docName, String docdata, int predid, int signNumber, String CertID, long id, int WrkId) throws Exception {
		
		
	}

	@Override
	public void doAfterSync(String formname, SyncObj syncobj, String sql, int signum) {
		
		ETDForm form = null;
		try {
			form = ETDForm.createFromArchive(syncobj.getBldoc());
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ServiceException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		DataBinder b = form.getBinder();
		
		long ppsid = 0;
		
		
		try {
			ppsid = Long.parseLong(b.getValue("id_PPS"));
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InternalException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		HashMap<String, Object> pp = new HashMap<String, Object>();
		pp.put("ppsid", ppsid);
		
		int predid = npjt.queryForInt("select predid from snt.docstore where etdid = :ppsid", pp);
		syncobj.setPredid(predid);
		
		
		
		
		
		
		
//		etdsyncfacade.updateDSF(syncobj);
		
		
		StringBuffer content = new StringBuffer();
		try{
//			System.out.println("content");
		content.append(b.getValue("P_1"));
		content.append(", ");
		content.append(b.getValue("P_12"));
		content.append(", ");
		content.append("поезд ");
		content.append(b.getValue("P_4"));
		content.append(" отказано ");
		content.append(b.getValue("P_16"));
		
		
//		pp.put("id", syncobj.getDocid());
//		pp.put("content", content.toString());
		syncobj.setContent(content.toString());
//		pp.put("typeid", syncobj.getTypeid());
//		npjt.update("update snt.docstore set nwrkid = 1, visible = 0, opisanie = :content where id =:id", pp); 

		} catch (Exception e){
			log.error(TypeConverter.exceptionToString(e));
		}
		syncobj.setSignlvl(0);
		etdsyncfacade.insertDocstore(sql, etdsyncfacade.getWorkerWithorderNull(syncobj));
		
	}
	public ETDDocumentData EditBeforeOpen(String docName, ETDDocumentData doc) {
		// TODO Auto-generated method stub
		return doc;
	}
}

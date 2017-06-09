package sheff.rgd.ws.Controllers.TOR;

import java.io.IOException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

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
import sheff.rjd.services.transoil.TransService;
import sheff.rjd.ws.OCO.AfterSign.SendToEtd;

public class VU23Controller extends AbstractAction{

	private String parentform;
	 private SendToEtd sendtoetd;
	 protected final Logger log = Logger.getLogger(getClass());
	 private TransService sendtotransoil;  
	 private ETDSyncServiceFacade etdsyncfacade;
	 private NamedParameterJdbcTemplate npjt;
	 private Integer syncsignatures;
	 public String getParentform() {
		return parentform;
	 }

	public void setParentform(String parentname) {
		this.parentform = parentname;	
	}
	 
	 public SendToEtd getSendtoetd() {
	     return sendtoetd;
	 }

	 public void setSendtoetd(SendToEtd sendtoetd) {
	     this.sendtoetd = sendtoetd;
	 }
	 public TransService getSendtotransoil() {
	 	return sendtotransoil;
	 }

	 public void setSendtotransoil(TransService sendtotransoil) {
	 	this.sendtotransoil = sendtotransoil;
	 }
	 
	 public ETDSyncServiceFacade getEtdsyncfacade() {
			return etdsyncfacade;
	}

	public void setEtdsyncfacade(ETDSyncServiceFacade etdsyncfacade) {
			this.etdsyncfacade = etdsyncfacade;
	}
	
	public NamedParameterJdbcTemplate getNpjt() {
		return npjt;
	}

	public void setNpjt(NamedParameterJdbcTemplate npjt) {
		this.npjt = npjt;
	}

	public Integer getSyncsignatures() {
		return syncsignatures;
	}

	public void setSyncsignatures(Integer syncsignatures) {
		this.syncsignatures = syncsignatures;
	}

	@Override
	public void doAfterSign(String docName, String docdata, int predid, int signNumber, long id, String certserial, int WrkId) {
		
	}

	@Override
	public void doAfterSave(String docName, String docdata, int predid, int signNumber, String CertID, long id, int WrkId) throws Exception {		
		try{
			  sendtoetd.FakeSign(docName, docdata, predid, signNumber, CertID, id, WrkId);
			  sendtoetd.PackUpdate(id);
			    } catch (Exception e){
			    	log.error(TypeConverter.exceptionToString(e));
			    };
			    sendtotransoil.SendBlobToTransoil(id, docName, predid);
	}

	@Override
	public void doAfterSync(String formname, SyncObj syncobj, String sql, int signum) {
		try {
			
			byte[] blob = syncobj.getBldoc();
			ETDForm form = ETDForm.createFromArchive(blob);
			DataBinder binder = form.getBinder();
			binder.setRootElement("data");
			if(syncobj.getVu36_etdid() > 0) {
//				System.out.println("syncobj.getVu36_etdid() " + syncobj.getVu36_etdid());
				String countDoc = "select count(0) from snt.docstore where etdid = :etdid";
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("etdid", syncobj.getEtdid());
				int numberDoc = npjt.queryForInt(countDoc, map);
				
				Long etdIdVU36 = syncobj.getVu36_etdid();
				String selectPredid = "select predid from snt.docstore where etdid = :etdidVU36";
				map.put("etdidVU36", etdIdVU36);
				int predid = npjt.queryForInt(selectPredid, map);
				
				if(numberDoc > 0) {
					insertInMarsh(syncobj.getEtdid(), predid);
				} else {
					syncobj.setSignlvl(syncsignatures);
					etdsyncfacade.insertDocstore(sql, etdsyncfacade.getWorkerWithorderNull(syncobj));
					etdsyncfacade.updateDSF(syncobj);
					
					//заполнение для отчета пгк п.1 тз
					String vagon = binder.getValue("wagon_number");
					String date = binder.getValue("signer1_date") + " " + binder.getValue("signer1_time");
					SimpleDateFormat myDateFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm");
					Date DT_VU23 = myDateFormat.parse(date); 
					myDateFormat.applyPattern("yyyy-MM-dd HH:mm:ss");
		            DT_VU23 = myDateFormat.parse(myDateFormat.format(DT_VU23));
					String cleymo = binder.getValue("f47");
					map.put("vagon", vagon);
					map.put("DT_VU23", DT_VU23);
					map.put("cleymo", cleymo);
					map.put("predid", syncobj.getPredid());
					String sqlInsert = "INSERT INTO snt.PGKREPORT (vagon, DT_VU23, Kleymo, predid) "
							+ "Values (:vagon, :DT_VU23, :cleymo, :predid)";
					npjt.update(sqlInsert, map);
					
					insertInMarsh(syncobj.getEtdid(), predid);
				}
				
				npjt.update("update snt.docstore set visible = 1 where etdid = :etdid", map);
			} else {
				syncobj.setSignlvl(syncsignatures);
				etdsyncfacade.insertDocstore(sql, etdsyncfacade.getWorkerWithorderNull(syncobj));
				etdsyncfacade.updateDSF(syncobj);
				
				//заполнение для отчета пгк п.1
				String vagon = binder.getValue("wagon_number");
				String date = binder.getValue("signer1_date") + " " + binder.getValue("signer1_time");
				SimpleDateFormat myDateFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm");
				Date DT_VU23 = myDateFormat.parse(date); 
				myDateFormat.applyPattern("yyyy-MM-dd HH:mm:ss");
	            DT_VU23 = myDateFormat.parse(myDateFormat.format(DT_VU23));
				String cleymo = binder.getValue("f47");
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("vagon", vagon);
				map.put("DT_VU23", DT_VU23);
				map.put("cleymo", cleymo);
				map.put("predid", syncobj.getPredid());
				String sqlInsert = "INSERT INTO snt.PGKREPORT (vagon, DT_VU23, Kleymo, predid) "
						+ "Values (:vagon, :DT_VU23, :cleymo, :predid)";
				npjt.update(sqlInsert, map);
				
	
				if(checkVagnum(vagon)) {
					String selectPredid = "select id from snt.pred where okpo_kod = :okpo_kod";
					map.put("okpo_kod", 78970343);
					int predid = npjt.queryForInt(selectPredid, map);
	            	insertInMarsh(syncobj.getEtdid(), predid);
	        	}
			}
		} catch (Exception e) {
//			e.printStackTrace();
			log.error(TypeConverter.exceptionToString(e));
		} 
	}
	public ETDDocumentData EditBeforeOpen(String docName, ETDDocumentData doc) {
		// TODO Auto-generated method stub
		return doc;
	}
	
	
	private boolean checkVagnum(String vagnum) {
		boolean result = false;
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("vagnum", vagnum);
		String count = "select count(0) from snt.vrk_vagnums "
				+ " where vagnum = :vagnum";
		if(npjt.queryForInt(count, map) > 0) {
			result = true;
		}
		return result;
	}

	
	private void insertInVRK2Table(String vagnum) {
		String insert = "insert into snt.vrk_vagnums (vagnum) "
				+ " values(:vagnum)";
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("vagnum", vagnum);
		npjt.update(insert, map);
	}
	
	private void insertInMarsh(Long etdid, int predid){
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("predid", predid);
		map.put("id", etdid);
		int docid = getNpjt().queryForInt("select id from snt.docstore where etdid =:id", map);
		map.put("docid", docid);
		getNpjt().update("insert into snt.marsh (docid, predid) "
				+ " values(:docid, :predid)", map);
	}
	
}

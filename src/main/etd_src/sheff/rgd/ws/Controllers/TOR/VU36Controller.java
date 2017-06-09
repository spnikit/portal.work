package sheff.rgd.ws.Controllers.TOR;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.text.html.ListView;

import org.apache.log4j.Logger;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.w3c.dom.NodeList;

import ru.aisa.rgd.etd.dao.ETDSyncServiceFacade;
import ru.aisa.rgd.etd.objects.ETDDocumentData;
import ru.aisa.rgd.ws.documents.ETDForm;
import ru.aisa.rgd.ws.exeption.ServiceException;
import ru.aisa.rgd.ws.utility.DataBinder;
import ru.aisa.rgd.ws.utility.TypeConverter;
import sheff.rgd.ws.Abstract.AbstractAction;
import sheff.rjd.services.syncutils.SendToVRKService;
import sheff.rjd.services.syncutils.SyncObj;
import sheff.rjd.services.transoil.TransService;
import sheff.rjd.ws.OCO.AfterEdit.FakeSignature;
import sheff.rjd.ws.OCO.AfterSign.SendToEtd;

public class VU36Controller extends AbstractAction{
	
	private final static String transoil = "ООО «Трансойл»";
	 private String parentform;
	 private SendToEtd sendtoetd;
	 protected final Logger log = Logger.getLogger(getClass());
	 private TransService sendtotransoil;  
	 private ETDSyncServiceFacade etdsyncfacade;
	 private NamedParameterJdbcTemplate npjt;
	 private FakeSignature fakesignature;
	 private SendToVRKService signvrkservice;
	 
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

	public FakeSignature getFakesignature() {
		return fakesignature;
	}

	public void setFakesignature(FakeSignature fakesignature) {
		this.fakesignature = fakesignature;
	}

	public SendToVRKService getSignvrkservice() {
		return signvrkservice;
	}

	public void setSignvrkservice(SendToVRKService signvrkservice) {
		this.signvrkservice = signvrkservice;
	}

	@Override
	public void doAfterSign(String docName, String docdata, int predid, int signNumber, long id, String certserial, int WrkId) {
		
		
		Map<String, Object> hm1 = new HashMap<String, Object>();
		hm1.put("id", id);
		int drop = getNpjt()
				.queryForInt(
						"select CASE when dropid is null then 0 else 1 end from snt.docstore where id = :id",
						hm1);
		try{
		boolean newdoc = false;
		if (signNumber==1){
			newdoc=true;
			sendtoetd.SendToEtdMessage(id, docdata, parentform,
					signNumber, drop, newdoc);
		}
		
		signvrkservice.SendSign(id, signNumber, drop, predid, "", parentform);
		
	}catch (Exception e){
		log.error(TypeConverter.exceptionToString(e));
	}
	}

	@Override
	public void doAfterSave(String docName, String docdata, int predid, int signNumber, String CertID, long id, int WrkId) throws Exception {		
		
		
		if (signNumber==2){
		
		try{
			
			sendtoetd.FakeSign(docName, docdata, predid, signNumber, CertID, id, WrkId);
			sendtoetd.PackUpdate(id);
			    } catch (Exception e){
			    	log.error(TypeConverter.exceptionToString(e));
			    };
		
			    sendtotransoil.SendBlobToTransoilVU36(id, docName, predid);
	}
	}
	@Override
	public void doAfterSync(String formname, SyncObj syncobj, String sql, int signum) throws ServiceException, IOException {
		Map<String, Object> hm1 = new HashMap<String, Object>();
		hm1.put("id", syncobj.getEtdid());
		
		
		int drop = syncobj.getDrop();
		
		if (drop==1){
			HashMap<String, Object> pp = new HashMap<String, Object>();
			pp.put("id", syncobj.getEtdid());
			int predid = getNpjt().queryForInt("select predid from snt.docstore where etdid =:id ", pp);
			int docid = getNpjt().queryForInt("select id from snt.docstore where etdid =:id", pp);
			signvrkservice.SendSign(docid, syncobj.getSignlvl(), drop, predid, syncobj.getMess1354(), parentform);
		}
	
//		System.out.println(syncobj.isUpdate());
//		System.out.println(syncobj.isEtdSecondVU36());
		
		
		if (syncobj.getSignlvl()==2&&!syncobj.isUpdate()&&!syncobj.isEtdSecondVU36()){
		etdsyncfacade.getWorkerWithorderNull(syncobj);
		etdsyncfacade.insertDocstore(sql, syncobj);
		etdsyncfacade.updateDSF(syncobj);

		etdsyncfacade.getWorkerWithorder(syncobj);
		etdsyncfacade.updateDSF(syncobj);
		
		ETDForm form  = ETDForm.createFromArchive(syncobj.getBldoc());
		DataBinder db = form.getBinder();
		
		HashMap<String, Object> pp = new HashMap<String, Object>();
		pp.put("name", transoil);
		
		ArrayList<String> okpo_array = db.getValuesAsArrayList("kod_okpo");
		String okpo = String.valueOf(npjt.queryForInt("select okpo_kod from snt.pred where name =:name", pp));
		if (okpo_array.contains(okpo)){
			sendtotransoil.SendtoTransoil(syncobj.getEtdid(), parentform, syncobj.getDocdata(), new String(syncobj.getBldoc(), "UTF-8"));
		}
		List<String> listNumVag = new ArrayList<String>();
		try {
			//отчет п.2
			db.setRootElement("data");
			String data_priem = db.getValue("data_priem");
			String[] arr = data_priem.split("\\.");
			arr[2] = "20" + arr[2]; 
			data_priem = arr[2] + "-" + arr[1] + "-" + arr[0];
			String[] array = db.getValuesAsArray("nomer_vagona_new_8");
            for( String s: array) {
                listNumVag.add(s);
			}

	        String select = "select count(0) from snt.PGKREPORT where vagon = :vagon "
	        		+ "and rep_date = :rep_date with ur";
	        if(listNumVag.size() > 0) {
	        	SimpleDateFormat myDateFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
	        	Date DT_VU36 = myDateFormat.parse(db.getValue("P_7_8"));
			    myDateFormat.applyPattern("yyyy-MM-dd HH:mm:ss");
			    DT_VU36 = myDateFormat.parse(myDateFormat.format(DT_VU36));
	        	pp.put("DT_VU36", DT_VU36);
	        	pp.put("predid", syncobj.getPredid());
	        	for(int i = 0; i < listNumVag.size(); i++) {
	        		pp.put("vagon", listNumVag.get(i));
	        		pp.put("rep_date", data_priem);
	        		Integer number = npjt.queryForInt(select, pp);
	        		if(number > 0) {
	        			Object isNull = npjt.queryForObject("select DT_VU23 from snt.PGKREPORT where "
	        					+ "vagon = :vagon and rep_date = :rep_date with ur", pp, Object.class);
	        			Object DT_create_pak_isNull = npjt.queryForObject("select DT_create_pak "
        						+ "from snt.PGKREPORT where "
	        					+ "vagon = :vagon and rep_date = :rep_date  with ur", pp, Object.class);
	        			if(isNull == null) {	
	        				if(DT_create_pak_isNull != null) {
		        				npjt.update("update snt.PGKREPORT set DT_VU36 = :DT_VU36, predid = :predid, "
		        						+ "wait_create_pak = TIMESTAMPDIFF(8,CHAR(DT_create_pak - CAST(:DT_VU36 AS TIMESTAMP))) "
		        						+ "where vagon = :vagon and rep_date = :rep_date", pp);
	        				} else {
	        					npjt.update("update snt.PGKREPORT set DT_VU36 = :DT_VU36, predid = :predid, "
	            						+ "wait_repair = TIMESTAMPDIFF(8,CHAR(CAST(:DT_VU36 AS TIMESTAMP) - DT_VU23)) "
	            						+ "where vagon = :vagon and rep_date = :rep_date", pp);
	        				}
	        			} else {
	        				if(DT_create_pak_isNull != null) {
		        				npjt.update("update snt.PGKREPORT set DT_VU36 = :DT_VU36, predid = :predid, "
		        						+ "wait_repair = TIMESTAMPDIFF(8,CHAR(CAST(:DT_VU36 AS TIMESTAMP) - DT_VU23)), "
		        						+ "wait_create_pak = TIMESTAMPDIFF(8,CHAR(DT_create_pak - CAST(:DT_VU36 AS TIMESTAMP))) "
		        						+ "where vagon = :vagon and rep_date = :rep_date", pp);
	        			   } else {
	        				   npjt.update("update snt.PGKREPORT set DT_VU36 = :DT_VU36, predid = :predid, "
	           						+ "wait_repair = TIMESTAMPDIFF(8,CHAR(CAST(:DT_VU36 AS TIMESTAMP) - DT_VU23)) "
	           						+ "where vagon = :vagon and rep_date = :rep_date", pp);
	        			   }
	        			}
	        		} else {
	        			number = npjt.queryForInt("select count(0) from snt.PGKREPORT "
	        					+ "where vagon = :vagon and rep_date is null and DT_VU23 = "
	        	        		+ "(select max(DT_VU23) from snt.PGKREPORT where vagon = :vagon "
	        	        		+ "and rep_date is null) with ur", pp); 
	        			if(number > 0) {
	        				npjt.update("update snt.PGKREPORT set DT_VU36 = :DT_VU36, rep_date = :rep_date, "
	        						+ "wait_repair = TIMESTAMPDIFF(8,CHAR(CAST(:DT_VU36 AS TIMESTAMP) - DT_VU23)), predid = :predid "
	        						+ "where vagon = :vagon and DT_VU23 = "
	        	        		+ "(select max(DT_VU23) from snt.PGKREPORT where vagon = :vagon and rep_date is null)", pp);
	        			} else {
	        				String sqlInsert = "INSERT INTO snt.PGKREPORT (vagon, rep_date, DT_VU36, Kleymo, predid)"
	        						+ " Values (:vagon, :rep_date, :DT_VU36, :Kleymo, :predid)";
	        				pp.put("Kleymo", db.getValue("kod_predpr"));
	        				npjt.update(sqlInsert, pp);
	        			}
	        		}
	        	}
	        }
	        
			} catch(Exception e) {
				log.error(TypeConverter.exceptionToString(e));
//				e.printStackTrace();
			}
		
			try {
		        boolean flag = false;
		        for(int i = 0; i < listNumVag.size(); i++) {
					if(checkVagnum(listNumVag.get(i))) {
						flag = true;
		        	}
		        }
		        if(flag) {
		        	String selectPredid = "select id from snt.pred where okpo_kod = :okpo_kod";
					hm1.put("okpo_kod", 78970343);
					int predid = npjt.queryForInt(selectPredid, hm1);
		        	insertInMarsh(syncobj.getEtdid(), predid);
		        }
			} catch(Exception e) {
//				e.printStackTrace();
				log.error(TypeConverter.exceptionToString(e));
			}
		
		}
		
		if (syncobj.getMess1354().length()>0){
//			send mess1354
			HashMap<String, Object> pp = new HashMap<String, Object>();
			pp.put("id", syncobj.getDocid());
			int predid = getNpjt().queryForInt("select predid from snt.docstore where id =:id ", pp);
			signvrkservice.SendSign(syncobj.getDocid(), syncobj.getSignlvl(), drop, predid, syncobj.getMess1354(), parentform);
		}
		
		if (syncobj.getMess4624().length()>0){
//			send mess4624
		}
		
		if (syncobj.getSignlvl()==2&&syncobj.isEtdSecondVU36()){
			HashMap<String, Object> pp = new HashMap<String, Object>();
			pp.put("typeid", syncobj.getTypeid());
			pp.put("order", 5);
			pp.put("id", syncobj.getDocid());
			int wrkid = getNpjt().queryForInt("select wrkid from snt.doctypeflow where order = :order and dtid = :typeid ", pp);
			int predid = getNpjt().queryForInt("select predid from snt.docstore where id =:id ", pp);
			fakesignature.fakesign(syncobj.getDocid(), 0, wrkid, predid, syncobj.getBldoc(), syncobj.getDocdata());
			signvrkservice.SendSign(syncobj.getDocid(), syncobj.getSignlvl(), drop, predid, "", parentform);
		}
		
		if (syncobj.getSignlvl()==3){

			HashMap<String, Object> pp = new HashMap<String, Object>();
			pp.put("typeid", syncobj.getTypeid());
			pp.put("order", 6);
			pp.put("id", syncobj.getDocid());
			int wrkid = getNpjt().queryForInt("select wrkid from snt.doctypeflow where order = :order and dtid = :typeid ", pp);
			int predid = getNpjt().queryForInt("select predid from snt.docstore where id =:id ", pp);
			fakesignature.fakesign(syncobj.getDocid(), 0, wrkid, predid, syncobj.getBldoc(), syncobj.getDocdata());
			signvrkservice.SendSign(syncobj.getDocid(), syncobj.getSignlvl(), drop, predid, "", parentform);
			
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

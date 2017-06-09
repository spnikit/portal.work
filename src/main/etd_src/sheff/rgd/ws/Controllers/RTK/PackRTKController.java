package sheff.rgd.ws.Controllers.RTK;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.w3c.dom.NodeList;

import ru.aisa.rgd.etd.dao.ETDSyncServiceFacade;
import ru.aisa.rgd.etd.objects.ETDDocumentData;
import ru.aisa.rgd.ws.dao.ServiceFacade;
import ru.aisa.rgd.ws.documents.ETDForm;
import ru.aisa.rgd.ws.domain.Document;
import ru.aisa.rgd.ws.exeption.InternalException;
import ru.aisa.rgd.ws.exeption.ServiceException;
import ru.aisa.rgd.ws.utility.DataBinder;
import ru.aisa.rgd.ws.utility.RowHandler;
import ru.aisa.rgd.ws.utility.TypeConverter;
import sheff.rgd.ws.Abstract.AbstractAction;
import sheff.rjd.services.syncutils.SendToRTKService;
import sheff.rjd.services.syncutils.SyncObj;
import sheff.rjd.ws.OCO.AfterEdit.FakeSignature;
import sheff.rjd.ws.OCO.AfterSign.SendToEtd;

public class PackRTKController extends AbstractAction{

	private String parentform;
	private NamedParameterJdbcTemplate npjt;
	private ETDSyncServiceFacade etdsyncfacade;
	private FakeSignature fakesignature;
	private SendToEtd sendtoetd;
	private SendToRTKService sendsign;  
	private ServiceFacade facade;
	
	private List<String> listFileName = new ArrayList<String>();
	private Map<String, Long> mapIdDoc = new HashMap<String, Long>();
	
	protected final Logger	log	= Logger.getLogger(getClass());
	
	public SendToEtd getSendtoetd() {
	    return sendtoetd;
	} 
	
	public SendToRTKService getSendsign() {
		return sendsign;
	}
	
	public void setSendsign(SendToRTKService sendsign) {
		this.sendsign = sendsign;
	}
	
	public void setSendtoetd(SendToEtd sendtoetd) {
	    this.sendtoetd = sendtoetd;
	}
	
	public NamedParameterJdbcTemplate getNpjt() {
		return npjt;
	}
	public void setNpjt(NamedParameterJdbcTemplate npjt) {
		this.npjt = npjt;
	}
	
	public ETDSyncServiceFacade getEtdsyncfacade() {
		return etdsyncfacade;
	}
	public void setEtdsyncfacade(ETDSyncServiceFacade etdsyncfacade) {
		this.etdsyncfacade = etdsyncfacade;
	}
	
	public void setParentform(String parentname) {
		this.parentform = parentname;
	}

	public FakeSignature getFakesignature() {
		return fakesignature;
	}

	public void setFakesignature(FakeSignature fakesignature) {
		this.fakesignature = fakesignature;
	}

	public String getParentform() {
		return parentform;
	}
	
	public ServiceFacade getFacade() {
		return facade;
	}

	public void setFacade(ServiceFacade facade) {
		this.facade = facade;
	}
	
	@Override
	public void doAfterSign(String docName, String docdata, int predid, int signNumber, long id, String certserial, int WrkId) {
		Map<String, Object> hm1 = new HashMap<String, Object>();
		hm1.put("id", id);
		int drop = getNpjt().queryForInt("select CASE when dropid is null then 0 else 1 end from snt.docstore where id = :id", hm1);
		String id_pak = getNpjt().queryForObject("select id_pak from snt.docstore where id = :id", hm1, String.class);
		
		if(drop==1||signNumber==1){
		
		try {
	 
			if (drop==0){
				sendtoetd.SendToEtdMessage(id, docdata, parentform, signNumber, drop, true);
				hm1.put("status", "Подписан РТК");
			}
	   
			if (drop==1){
				hm1.put("status", "Отклонен РТК");
			}
			getNpjt().update("update snt.docstore set reqnum =:status where id =:id ", hm1);
//	   sendsign.SendSign(id, signNumber, drop, predid);
			AddInReport(id, predid, signNumber, drop);
	    } catch (Exception e) {
	    	log.error(TypeConverter.exceptionToString(e));
	    } 
	
		}
	}

	@Override
	public void doAfterSave(String docName, String docdata, int predid, int signNumber, String CertID, long id, int WrkId) throws Exception {
	    try{
	    ETDForm form=ETDForm.createFromArchive(docdata.getBytes("UTF-8"));
	   	DataBinder kinder=form.getBinder();
	   	listFileName.clear();
	   	mapIdDoc.clear();
		kinder.handleTable("table3", "row3", new RowHandler<Object>() {
			public void handleRow(DataBinder kinder, int rowNum, Object obj) throws InternalException
			{  
				if(kinder.getValue("id_kard").length() == 0 && kinder.getValue("file_name").length()!=0) {
				listFileName.add(kinder.getValue("file_name"));
				}
			}
		}, null);
		kinder.resetRootElement();
	    kinder.setRootElement("page");
   	    NodeList list = kinder.getNodes("data");
   	    Map<String, String> mapFileContent = new HashMap<String, String>();
   	    boolean flag = false;
   	    String tempFilename = null;
   	    for(int i = 0; i<list.getLength(); i++) {
   	    	NodeList listData = list.item(i).getChildNodes();
   	    	for(int j = 0; j < listData.getLength(); j++) {
   	    		if(listData.item(j).getNodeName().equals("filename")) {
   	    			for(int k = 0; k < listFileName.size(); k++) {
   	    				if(listData.item(j).getTextContent().equals(listFileName.get(k))) {
   	   	    				flag = true;
   		    				tempFilename = listData.item(j).getTextContent();
   		    				break;	
   	   	    			}
   	    			}
	    			
   	    		}
   	    		if(listData.item(j).getNodeName().equals("mimedata") && flag) {
   	    			mapFileContent.put(tempFilename,listData.item(j).getTextContent());
   	    			flag = false;
   	    		}
   	    	}  	
   	    }
   	  kinder.resetRootElement();
   	  
     createCardDocRTK(mapFileContent, id, predid);
     
    if(!mapIdDoc.isEmpty()) {
 	kinder.handleTable("table3", "row3", new RowHandler<Object>() {
		public void handleRow(DataBinder kinder, int rowNum, Object obj) throws InternalException
		{  
			for (Map.Entry<String, Long> entry : mapIdDoc.entrySet()) {
				if(kinder.getValue("file_name").equals(entry.getKey())) {
					kinder.setNodeValue("id_kard", entry.getValue());
				}
			}			
		}
	}, null);
    }	
	   	 if (kinder.getValue("P_4").length()>0)
	   	 {
	   	    HashMap<String, Object> pp = new HashMap<String, Object>();
	   	 
	   	    pp.put("ID", id);
	   		pp.put("name", kinder.getValue("P_4"));	
	   	    npjt.update("update snt.docstore set rem_pred = :name where id = :ID", pp);	
	   	    
	   	}
	   	 
		 kinder.resetRootElement();
	     kinder.setRootElement("data");
	     String[] arrayId = kinder.getValue("list_id_for_remove").split(":");
	     if(arrayId.length > 0){
		     for(String str: arrayId) {
		    	 if(str.equals("")) {
		    		 break;
		    	 }
		    	 facade.deleteDocument(Long.parseLong(str));
		     }
		     kinder.getNode("list_id_for_remove").setTextContent("");
	     }
	   	 HashMap<String, Object> pp = new HashMap<String, Object>();
	   	 pp.put("BLDOC", form.encodeToArchiv());
		 pp.put("DOCDATA", form.transform("data"));
		 pp.put("ID", id);
	   	 facade.getNpjt().update("update snt.docstore set bldoc =:BLDOC, docdata =:DOCDATA where id=:ID", pp);
	    } catch (Exception e){
	    	log.error(TypeConverter.exceptionToString(e));
	    };
		
	}

	@Override
	public void doAfterSync(String formname, SyncObj obj, String sql, int signum) {
		Map<String, Object> pp = new HashMap<String, Object>();
		pp.put("etdid", obj.getEtdid());
		try { 
			Long id = getNpjt().queryForLong("select id from snt.docstore where etdid = :etdid", pp);
			pp.put("id", id);
			int predid = getNpjt().queryForInt("select predid from snt.docstore where id =:id ", pp);
			if (signum==2){
				//TODO Добавить проверку для ТФС
				int flag = 0;
				Document doc = facade.getDocumentById(id);

				try {
					ETDForm form = ETDForm.createFromArchive(doc.getBlDoc());
					DataBinder db= form.getBinder();
					flag = Integer.parseInt(db.getValue("flag"));
				} catch (Exception e) {
					log.error(TypeConverter.exceptionToString(e));
				}

				if (flag==0) {
					pp.put("status", "Подписан ДИ");
					getNpjt().update("update snt.docstore set reqnum = :status where id =:id ", pp);
				} else if (flag==1) {
					pp.put("typeid", obj.getTypeid());
					pp.put("order", 2);
					pp.put("status", "Подписан ДИ");
					int wrkid = getNpjt().queryForInt("select wrkid from snt.doctypeflow where order = :order and dtid = :typeid ", pp);
					fakesignature.fakesign(id, 0, wrkid, predid, obj.getBldoc(), obj.getDocdata());
					getNpjt().update("update snt.docstore set reqnum = :status where id =:id ", pp);
				}
			}
			if (signum==3){
				pp.put("typeid", obj.getTypeid());
				pp.put("order", 2);
				pp.put("status", "Подписан Заказчиком");
				int wrkid = getNpjt().queryForInt("select wrkid from snt.doctypeflow where order = :order and dtid = :typeid ", pp);
				fakesignature.fakesign(id, 0, wrkid, predid, obj.getBldoc(), obj.getDocdata());
				getNpjt().update("update snt.docstore set reqnum = :status where id =:id ", pp);
			}
			int drop = getNpjt().queryForInt("select CASE when dropid is null then 0 else 1 end from snt.docstore where id = :id", pp);
			AddInReport(id, predid, signum, drop);
		} catch (Exception e) {
			log.error(TypeConverter.exceptionToString(e));
		}
	}
	
	private void createCardDocRTK(Map<String, String> map, long id_packDoc, int predId) {
		try {
		String selectPackageId = "select id_pak from snt.docstore where id = :id";
	   	for (Map.Entry<String, String> entry : map.entrySet()) {
	    	ETDForm form = new ETDForm(facade.getDocumentTemplate("Карточка документ РТК"));
	        DataBinder binder = form.getBinder();
	        binder.createElement("data", "page");
	        binder.setRootElement("page");
	        binder.getElement("data").setAttribute("sid", "data1");
	        binder.createElement("mimetype", "data");
	        String[] filename = entry.getKey().split("\\.");
	        binder.getElement("mimetype").setTextContent("application/"+filename[1]);
	        binder.createElement("filename", "data");
	        binder.getElement("filename").setTextContent(entry.getKey());
	        binder.createElement("datagroup", "data");
	        binder.getLastNode("datagroup").appendChild(binder.createElement("datagroupref"));
	        binder.getLastNode("datagroupref").setTextContent("EncloseImage");
	        binder.createElement("mimedata", "data");
	        binder.getElement("mimedata").setAttribute("encoding", "base64-gzip");
	        binder.getElement("mimedata").setTextContent(entry.getValue());
	        HashMap<String, Object> pp = new HashMap<String, Object>();
	        pp.put("id", id_packDoc);
	        Document document  = new Document();
			document.setBlDoc(form.encodeToArchiv());
			document.setDocData(form.transform("data"));
			String packageId = (String)npjt.queryForObject(selectPackageId, pp, Object.class);
			pp.put("id_pak", packageId);
			document.setPredId(predId);
			document.setSignLvl(0);
			document.setType("Карточка документ РТК");
			Long id = facade.getNextDocumentId();
			document.setId(id);	
			facade.getDocumentDao().save(document);
			pp.put("filename", filename[0]);
			pp.put("docid", id);
			npjt.update("update snt.docstore set opisanie = :filename where id =:docid", pp);
		    facade.getNpjt()
			.update("update snt.docstore set id_pak = :id_pak, visible=0 where id =:docid",
					pp);
		    mapIdDoc.put(entry.getKey(), id);
	    	}
	    } catch(Exception e) {
	    	log.error(TypeConverter.exceptionToString(e));
	    }
	}
	public ETDDocumentData EditBeforeOpen(String docName, ETDDocumentData doc) {
		// TODO Auto-generated method stub
		return doc;
	}
	
	private void AddInReport(long id, int predid, int signCount, int drop) throws InternalException {
		try {
			String selectBldocActRTK = "select  bldoc from snt.docstore where id_pak = :id_pak and  "
					+ " typeid = (select id from snt.doctype where rtrim(name) = 'Акт РТК')";
			String selectCountDoc = "select count(0) from snt.RTKREPORT where docid = :docid";
			String update = "update snt.rtkreport set #filter where docid = :docid";
			String selectLdate = "select ldate, ltime from snt.docstore where id = :docid";
			Map<String, Object> pp = new HashMap<String, Object>();
			pp.put("docid", id);
	        String id_pak = getNpjt().queryForObject("select id_pak from snt.docstore where id = :docid", pp, String.class);
			pp.put("id_pak", id_pak);
		 	byte[] blob = (byte[])npjt.queryForObject(selectBldocActRTK, pp, byte[].class);
			ETDForm form = ETDForm.createFromArchive(blob);
			DataBinder binder = form.getBinder();   
		
			int numberRecord = getNpjt().queryForInt(selectCountDoc, pp);
			String no = binder.getValue("P_2");
			String P_49 = binder.getValue("P_49");
			SimpleDateFormat myDateFormat = new SimpleDateFormat("dd.MM.yyyy");
			Date createDateDoc = myDateFormat.parse(P_49);
			String summ = binder.getValue("P_19");
			if (binder.getInt("P_1")==2){
				summ="-"+summ;
			}
//			System.out.println(summ);
			String pfmCode = binder.getValue("P_28");
			int correctPfm = PfmCnsiCodeConvertionTable.correctPfmCode(pfmCode);
			pp.put("etdid", correctPfm);
			String shortDescrDepartment = npjt.queryForObject("select rtrim(name) from snt.pfmcodes "
					+ " where etdid = :etdid", pp, String.class);
			String fio = null;
			String P_40 = binder.getValue("P_40");
			if(!P_40.isEmpty()) {
				fio = P_40;
			}
			pp.put("fio", fio);
			pp.put("no", no);
			pp.put("crdate", createDateDoc);
			pp.put("summ", summ);
			pp.put("department", shortDescrDepartment);
			pp.put("predid", predid);
			String filter = new String();
			if(drop == 1) {
				Date dropdate = npjt.queryForObject("select droptime from snt.docstore where id = :docid",
						pp, Date.class);
				pp.put("dropdate", dropdate);
				if(numberRecord > 0) {
					filter = "datedrop = :dropdate";
					update = update.replace("#filter", filter);
					npjt.update(update, pp);
				} else {
					npjt.update("INSERT INTO snt.RTKREPORT (docid, no, crdate, fio, summ, short_descr_dep, Datedrop, predid) " + 
							" Values (:docid, :no, :crdate, :fio, :summ, :department, :dropdate, :predid)", pp);
				}
			} else {
				SqlRowSet rs = npjt.queryForRowSet(selectLdate, pp);
	    		Date ldate = null;
	    		Time ltime = null;
	    		while(rs.next()) {
	    			ldate = rs.getDate("ldate");
	    			ltime = rs.getTime("ltime");
	    		}       	
	    		myDateFormat.applyPattern("yyyy-MM-dd");
	            String ldt = myDateFormat.format(ldate);
	            myDateFormat.applyPattern("yyyy-MM-dd HH:mm:ss");
	        	Date dateSign = myDateFormat.parse(ldt + " " + ltime);
	        	
				if(numberRecord > 0) {
					if(signCount == 1) {
						filter = "date_Sign_RTK = :dateSignRTK";
						update = update.replace("#filter", filter);
						pp.put("dateSignRTK", myDateFormat.format(dateSign));
						log.debug(pp.get("dateSignRTK"));
					} else if(signCount == 2) {
						filter = "date_Sign_DI = :dateSignDI";
						update = update.replace("#filter", filter);
						pp.put("dateSignDI", myDateFormat.format(new Date()));
						log.debug(pp.get("dateSignDI"));
					} else if(signCount == 3) {
						filter = "date_Sign_CBS = :dateSignCBS";
						update = update.replace("#filter", filter);
						pp.put("dateSignCBS", myDateFormat.format(dateSign));
						log.debug(pp.get("dateSignCBS"));
					}
					npjt.update(update, pp);
				} else {
					pp.put("dateSignRTK", dateSign);
					npjt.update("INSERT INTO snt.RTKREPORT (docid, no, crdate, fio, summ, short_descr_dep, date_Sign_RTK, predid) " + 
							" Values (:docid, :no, :crdate, :fio, :summ, :department, :dateSignRTK, :predid)", pp);
				}
			}
		} catch(Exception e) {
			log.error(TypeConverter.exceptionToString(e));
		}

	}
}

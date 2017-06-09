package sheff.rgd.ws.Controllers.ASR;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import ru.aisa.rgd.etd.dao.ETDSyncServiceFacade;
import ru.aisa.rgd.etd.objects.ETDDocumentData;
import ru.aisa.rgd.ws.documents.ETDForm;
import ru.aisa.rgd.ws.exeption.ServiceException;
import ru.aisa.rgd.ws.utility.DataBinder;
import ru.aisa.rgd.ws.utility.TypeConverter;
import sheff.rgd.ws.Abstract.AbstractAction;
import sheff.rgd.ws.Abstract.DoAction;
import sheff.rjd.services.syncutils.SyncObj;
//import sheff.rjd.ws.OCO.AfterSave.AfterSave;
import sheff.rjd.ws.OCO.AfterSign.DropList;
import sheff.rjd.ws.OCO.AfterSign.SendToEtd;

public class FPU26ASRController extends AbstractAction{

	private String parentform;
	private ETDSyncServiceFacade etdsyncfacade;
	private NamedParameterJdbcTemplate npjt;
	private SendToEtd sendtoetd;
	private DropList droplist;
	private DoAction action;
    protected final Logger	log	= Logger.getLogger(getClass());
	
	public DropList getDroplist() {
		return droplist;
	}

	public void setDroplist(DropList droplist) {
		this.droplist = droplist;
	}

	public NamedParameterJdbcTemplate getNpjt() {
		return npjt;
	}

	public void setNpjt(NamedParameterJdbcTemplate npjt) {
		this.npjt = npjt;
	}

   
    public SendToEtd getSendtoetd() {
        return sendtoetd;
    }

    public void setSendtoetd(SendToEtd sendtoetd) {
        this.sendtoetd = sendtoetd;
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
	
	public void setFormControllers (DoAction action){
		this.action = action;
		}

	public DoAction getFormControllers(){
		return action;
	}


	@Override
	public void doAfterSign(String docName, String docdata, int predid, int signNumber, long id, String certserial, int WrkId) {
		Map<String, Object> hm1 = new HashMap<String, Object>();
		hm1.put("id", id);
		int drop = getNpjt().queryForInt("select CASE when dropid is null then 0 else 1 end from snt.docstore where id = :id", hm1);
		
		if(signNumber==2||drop==1){
			
			try{
			
				Map<String, Object> dl = new HashMap<String, Object>();
				if (drop ==1){
					try{
						dl = droplist.getDropList(id);
									
					}catch(Exception e){
						log.error(TypeConverter.exceptionToString(e));
					}
				}
				
					String id_pak = (String) getNpjt().queryForObject("select id_pak from snt.docstore where id = :id", hm1, String.class);
				 hm1.put("id_pak", id_pak);		
				
				 
				 //ОПИСАНИЕ
				 int countsf = getNpjt().queryForInt("select count(0) from snt.docstore where typeid in " +
				 		"(select id from snt.doctype where name like '%Счет-фактура%') and id_pak = :id_pak", hm1);
				 
				 
				 if (countsf==0)
				 {
				 String content = (String) getNpjt().queryForObject("select rtrim(opisanie) from snt.docstore where id = :id", hm1, String.class);
				 
				 hm1.put("content", content+" - Счет-фактура не сформирована");
				 getNpjt().update("update snt.docstore set opisanie = :content where id = :id", hm1);
				 				 
				 }
				 
				 List<Map<String, Object>> ids = getNpjt().queryForList("select id from snt.docstore where id_pak = :id_pak" +
				 		" and id <>:id and signlvl is not null and typeid not in (select id from snt.doctype where name like '%Счет-фактура%')", hm1);
				
				 for (int i=0;i<ids.size(); i++){
					 try{
						
						final long docid = Long.valueOf(String.valueOf(ids.get(i).get("ID")));
						
						Map pp = new HashMap<String, Object>();
						pp.put("docid", docid);
								
						List<Map<String, Object>> l = npjt.queryForList("select typeid, (select rtrim(name) docname from snt.doctype "+
	"where id = ds.typeid), "+
	"(select count(0) signumber from snt.docstoreflow where docid = :docid) "+
	" from snt.docstore ds where id =:docid ", pp);
						
						
						byte[] bldoc = (byte[])npjt.queryForObject("select bldoc from snt.docstore where id = :docid", pp, byte[].class);
						String docdata1 = new String (bldoc, "UTF-8");
						
							String docname1 = l.get(0).get("DOCNAME").toString();
							int signumber1 = Integer.valueOf(l.get(0).get("SIGNUMBER").toString());
							
							if (drop==0)
							action.doAfterSave(docname1, docdata1, predid, signumber1, certserial, docid, WrkId);
							else if (drop==1)
						{
									try{
								
								dl.put("DOCID", docid);
								
								droplist.DropDocs(dl);
								
							sendtoetd.SendToEtdMessage(docid, docdata1, docname1, signNumber, drop,false);
							} catch(Exception e){
								
								log.error(TypeConverter.exceptionToString(e));
							}
								
						}
					 }catch(Exception e){
							
						 log.error(TypeConverter.exceptionToString(e));
						}
						
						
						
				 }
				
				
				try {
				sendtoetd.SendToEtdMessage(id, docdata, parentform, signNumber, drop,false);
			    } catch (Exception e) {
			    	log.error(TypeConverter.exceptionToString(e));
			
			    } 
			}catch (ServiceException e)
			{
				//e.printStackTrace(System.out);
				log.error("Service Error:");
				log.error("code:" + e.getError().getCode() + " message : " + e.getError().getMessage());
			}
			catch (Exception e)
			{
				log.error(TypeConverter.exceptionToString(e));
			}
		}
	}

	@Override
	public void doAfterSave(String docName, String docdata, int predid, int signNumber, String CertID, long id, int WrkId) throws Exception {
		
		
	}

	@Override
	public void doAfterSync(String formname, SyncObj syncobj, String sql, int signum) {
		etdsyncfacade.insertDocstore(sql, etdsyncfacade.getWorkerWithorderNull(syncobj));
		etdsyncfacade.updateDSF(syncobj);
		
	}


	public ETDDocumentData EditBeforeOpen(String docName, ETDDocumentData doc) {
		try{
			ETDForm form = ETDForm.createFromArchive(doc.getBlDoc());
			DataBinder b= form.getBinder();
			b.setNodeValue("documentId", String.valueOf(doc.getId()));
			doc.setBlDoc(form.encodeToArchiv());
			}catch (Exception e){
				log.error("Error in EditBeforeOpen: "+TypeConverter.exceptionToString(e));
			}
		return doc;
	}

}

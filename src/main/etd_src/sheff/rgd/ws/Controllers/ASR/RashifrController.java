package sheff.rgd.ws.Controllers.ASR;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import ru.aisa.rgd.etd.dao.ETDSyncServiceFacade;
import ru.aisa.rgd.etd.objects.ETDDocumentData;
import ru.aisa.rgd.ws.exeption.ServiceException;
import ru.aisa.rgd.ws.utility.TypeConverter;
import sheff.rgd.ws.Abstract.AbstractAction;
import sheff.rjd.services.syncutils.SyncObj;
import sheff.rjd.ws.OCO.AfterSign.DropList;
import sheff.rjd.ws.OCO.AfterSign.SendToEtd;

public class RashifrController extends AbstractAction{

	private String parentform;
	private ETDSyncServiceFacade etdsyncfacade;
    private SendToEtd sendtoetd;
	private DropList droplist;
	private NamedParameterJdbcTemplate npjt;
	
    public NamedParameterJdbcTemplate getNpjt() {
		return npjt;
	}

	public void setNpjt(NamedParameterJdbcTemplate npjt) {
		this.npjt = npjt;
	}

	public DropList getDroplist() {
		return droplist;
	}

	public void setDroplist(DropList droplist) {
		this.droplist = droplist;
	}

	protected final Logger	log	= Logger.getLogger(getClass());
   
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


	@Override
	public void doAfterSign(String docName, String docdata, int predid, int signNumber, long id, String certserial, int WrkId) {
		Map hm1 = new HashMap();
		hm1.put("id", id);
		int drop = getNpjt().queryForInt("select CASE when dropid is null then 0 else 1 end from snt.docstore where id = :id", hm1);
		
		if(drop==1){
			
			try{
			
				Map dl = new HashMap();
				if (drop ==1){
					try{
						dl = droplist.getDropList(id);
						}catch(Exception e){
							log.error(TypeConverter.exceptionToString(e));
					}
				}
				
			
				String id_pak = (String) getNpjt().queryForObject("select id_pak from snt.docstore where id = :id", hm1, String.class);
				 hm1.put("id_pak", id_pak);		
				
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
							
							if (drop==1)
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
				//e.printStackTrace(System.out);
				log.error(TypeConverter.exceptionToString(e));
				//SNMPSender.sendMessage(e);
			}
		}
	}

	@Override
	public void doAfterSave(String docName, String docdata, int predid, int signNumber, String CertID, long id, int WrkId) throws Exception {		
		    try{
		    	sendtoetd.FakeSign(docName, docdata, predid, signNumber, CertID, id, WrkId);
		    } catch (Exception e){
		    	log.error(TypeConverter.exceptionToString(e));
		    };
		    try{
		    sendtoetd.SendToEtdMessage(id, docdata, parentform, signNumber, 0,false);
		    }catch (Exception e){
		    	log.error(TypeConverter.exceptionToString(e));
	    }
		
	}

	@Override
	public void doAfterSync(String formname, SyncObj syncobj, String sql, int signum) {
		etdsyncfacade.insertDocstore(sql, etdsyncfacade.getWorkerWithorderNull(syncobj));
		etdsyncfacade.updateDSF(syncobj);
	}

	public ETDDocumentData EditBeforeOpen(String docName, ETDDocumentData doc) {
		// TODO Auto-generated method stub
		return doc;
	}

}
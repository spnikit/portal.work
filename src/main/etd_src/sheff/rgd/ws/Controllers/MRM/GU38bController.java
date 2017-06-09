package sheff.rgd.ws.Controllers.MRM;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import ru.aisa.rgd.etd.dao.ETDSyncServiceFacade;
import ru.aisa.rgd.etd.objects.ETDDocumentData;
import ru.aisa.rgd.ws.documents.ETDForm;
import ru.aisa.rgd.ws.utility.DataBinder;
import ru.aisa.rgd.ws.utility.TypeConverter;
import sheff.rgd.ws.Abstract.AbstractAction;
import sheff.rjd.services.syncutils.SendToRTKService;
import sheff.rjd.services.syncutils.SyncObj;

public class GU38bController extends AbstractAction{

	private String parentform;
	private ETDSyncServiceFacade etdsyncfacade;
   private NamedParameterJdbcTemplate npjt;
	private SendToRTKService signservice;
	
	
	
	protected final Logger	log	= Logger.getLogger(getClass());
	
    public SendToRTKService getSignservice() {
		return signservice;
	}

	public void setSignservice(SendToRTKService signservice) {
		this.signservice = signservice;
	}

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
	public void doAfterSign(String docName, String docdata, int predid, int signNumber, long id, String certserial, int WrkId) {}

	@Override
	public void doAfterSave(String docName, String docdata, int predid, int signNumber, String CertID, long id, int WrkId) throws Exception {}

	@Override
	public void doAfterSync(String formname, SyncObj obj, String sql, int signum) {
		
		ETDForm form = null;
		DataBinder b = null;
		try {
			form = ETDForm.createFromArchive(obj.getBldoc());
			b = form.getBinder();
		} catch (Exception e) {
			log.error(TypeConverter.exceptionToString(e));
		} 
		
		
		if (obj.getSignlvl()==0){
			try{
				 StringBuffer no = new StringBuffer();
				  no.append(b.getValue("P_5"));
				  obj.setDogovor(no.toString());
				  StringBuffer content = new StringBuffer();
				  content.append(b.getValue("P_13a"));
				  content.append(", ");
				  content.append("-");
				  content.append(", ");
				  content.append(b.getValue("P_16a"));
				  content.append(", ");
				  content.append(b.getValue("P_3"));
				  content.append(", ");
				  content.append(b.getValue("P_4"));
				  content.append(", ");
				  content.append(b.getValue("P_6"));
				  content.append(" ");
				  content.append(b.getValuesAsArray("P_25.1").length);
					content.append(" вагона(ов)");
				  obj.setContent(content.toString());
				}catch (Exception e){
					log.error(TypeConverter.exceptionToString(e));
				}
			 try{
				 etdsyncfacade.insertDocstore(sql, etdsyncfacade.getWorkerWithorderNull(obj));
				 String inn = b.getValue("P_32b");
					String kpp = b.getValue("P_32v");
				 int predid = etdsyncfacade.getpredIdByINNKPP(inn, kpp);
				 Long etdid = obj.getEtdid();
				 HashMap<String, Object> pp = new HashMap<String, Object>();	 
					pp.put("etdid", etdid);
					pp.put("predid", predid);
				 npjt.update("insert into snt.marsh(docid, predid) values (:etdid,:predid)", pp);
			 }  catch (Exception e){
					log.error(TypeConverter.exceptionToString(e));
				}		
			 
			
		}
		
	if (obj.getSignlvl()==1){
		String inn1="";
		String kpp1="";
		String inn2="";
		String kpp2="";
		
			try{
			inn1 = b.getValue("P_18b");
			kpp1 = b.getValue("P_18v");
			} catch (Exception e){
				StringWriter outError = new StringWriter();
				PrintWriter errorWriter = new PrintWriter(outError);
				e.printStackTrace(errorWriter);
				log.error("не найдено тега P_18 в форме "+outError.toString());
//				e.printStackTrace();
			}
			
			try{
			 inn2 = b.getValue("P_19b");
			 kpp2 = b.getValue("P_19v");
			} catch (Exception e){
				StringWriter outError = new StringWriter();
				PrintWriter errorWriter = new PrintWriter(outError);
				e.printStackTrace(errorWriter);
				log.error("не найдено тега P_19 в форме "+outError.toString());
//				e.printStackTrace();
			}
			
			
			int predid1 = -1;
			int predid2 = -1;
			
			
			
			if (inn1.length()>2&&kpp1.length()>2)
				predid1 = etdsyncfacade.getpredIdByINNKPP(inn1, kpp1);
			if (inn2.length()>2&&kpp2.length()>2)
				predid2 = etdsyncfacade.getpredIdByINNKPP(inn2, kpp2);
			
			
			List<Object[]>aa = new ArrayList<Object[]>();
			
			if (predid1>-1)
			 aa.add(new Object[]{obj.getEtdid(), predid1});
			
			if (predid2>-1)
				 aa.add(new Object[]{obj.getEtdid(), predid2});
			
			
			HashMap<String, Object> pp = new HashMap<String, Object>();
			pp.put("docdata", obj.getDocdata());
			pp.put("bldoc", obj.getBldoc());
			pp.put("etdid", obj.getEtdid());
			pp.put("signlvl", obj.getSignlvl());
			npjt.update("update snt.docstore set docdata =:docdata, bldoc = :bldoc, signlvl =:signlvl where etdid =:etdid", pp);
			
			
			try{
				int i=0;
				while (i<aa.size()){
					
				
				npjt.update("merge into snt.marsh M Using (Values ("+aa.get(i)[0]+", "+aa.get(i)[1]+")) as NEW (DOCID, PREDID) on m.docid = new.docid and m.predid = new.predid "+
	" WHEN NOT MATCHED THEN insert (docid, predid) values (new.docid, new.predid)", new HashMap());
				
				i++;
				}

			} catch (Exception e){
				log.error(TypeConverter.exceptionToString(e));
			}		
		}
		
		
		
	if (obj.getSignlvl()>1){
		
		HashMap<String, Object> pp = new HashMap<String, Object>();
		pp.put("docdata", obj.getDocdata());
		pp.put("bldoc", obj.getBldoc());
		pp.put("etdid", obj.getEtdid());
		pp.put("signlvl", obj.getSignlvl());
		npjt.update("update snt.docstore set docdata =:docdata, bldoc = :bldoc, signlvl =:signlvl where etdid =:etdid", pp);
		
		
		
		
		
		try {
			if (b.getValue("P_6").equals("000"))
			{
			if (b.getValue("P_28").length()>2&&b.getValue("P_31").length()>2)
				npjt.update("update snt.docstore set signlvl = null where etdid =:etdid", pp);
			}
			else if (!b.getValue("P_6").equals("000")){
				if (b.getValue("P_28").length()>2&&b.getValue("P_31").length()>2&&b.getValue("P_34.3").length()>2)
					npjt.update("update snt.docstore set signlvl = null where etdid =:etdid", pp);
			}
		} catch (Exception e) {
			log.error(TypeConverter.exceptionToString(e));
		} 
	
	}


}

	public ETDDocumentData EditBeforeOpen(String docName, ETDDocumentData doc) {
		// TODO Auto-generated method stub
		return doc;
	}

}

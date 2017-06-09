package sheff.rjd.services.syncutils;

import java.sql.Types;
import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.jdbc.core.SqlOutParameter;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcTemplate;

import ru.aisa.rgd.etd.dao.ETDSyncServiceFacade;
import sheff.rgd.ws.Abstract.DoAction;
import sheff.rjd.mappers.WrkflowMapper;
import sheff.rjd.utils.MyStoredProc;
import sheff.rjd.ws.OCO.TORLists;

public class SignatureProcessor {

    private static Log log = LogFactory
    .getLog(SignatureProcessor.class);
  
    private ETDSyncServiceFacade etdsyncfacade;
    private WrkflowMapper wrkmap;

    private NamedParameterJdbcTemplate npjt;
    private DoAction formControllers;
public DataSource getDs() {
        return ds;
    }

    public void setDs(DataSource ds) {
        this.ds = ds;
    }

    private DataSource ds;
	private static final String updtblob = "update snt.docstore set bldoc = :bldoc, docdata = :docdata where id =:id";
	private static final String updt = "insert into snt.packages (id_pak, vagnum, etdid) values (?,?,?)";
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

   
    public WrkflowMapper getWrkmap() {
        return wrkmap;
    }

    public void setWrkmap(WrkflowMapper wrkmap) {
        this.wrkmap = wrkmap;
    }


	public VchdeObj getVchde(int mark){
		if (mark>-1){
			
			return etdsyncfacade.getvchde(mark);
			
		}
		else return new VchdeObj();
    	  	
    }
    
    
    
    
	public DoAction getFormControllers() {
		return formControllers;
	}

	public void setFormControllers(DoAction formControllers) {
		this.formControllers = formControllers;
	}

	public int getPredId(String inn, String kpp, int okpo, int mark){
    	int pred =-1;
    	int count =-1;
    	
    	if (inn.length()>0&&kpp.length()>0&&mark>-1){
    		count = etdsyncfacade.getCountpredIdByINNKPPMARK(inn, kpp, mark);
    		if (count==1){
    		return etdsyncfacade.getpredIdByINNKPPMARK(inn, kpp, mark);
    		}
    		else {
    			return etdsyncfacade.getpredIdByINNKPP(inn, kpp);
    		}
    	}
    	else if (okpo>0&&mark>-1){

    		count = etdsyncfacade.getCountpredIdByIOKPOMARK(okpo, mark);
    		if (count==1){
    		return etdsyncfacade.getpredIdByIOKPOMARK(okpo, mark);
    		}
    		else {
    			return etdsyncfacade.getpredIdByIOKPO(okpo);
    		}
    	
    	}
    	
    	else if (inn.length()>0&&kpp.length()>0&&mark==-1)
    		return etdsyncfacade.getpredIdByINNKPP(inn, kpp);
    	
    	else if (okpo>0&&mark==-1)
    		return etdsyncfacade.getpredIdByIOKPO(okpo);
    	
    	else return -1;
    }
    
    
    
    public SyncObj marsh(SyncObj syncobj, String sql, int signum, String formname, long etddocid) throws Exception{
    	if(formControllers != null)
        {
    		formControllers.doAfterSync(formname, syncobj, sql, signum);
        }
    	
    	return syncobj;
    }
    
     
    public long getDocid(boolean isUpdate, Long etddocid, String formname){
    	long docid = -1;
    	if (!isUpdate) {
//    		System.out.println(etddocid);
    		
		if (!etdsyncfacade.getCountPortalDoc(etddocid.toString())){
			docid = etdsyncfacade.getNextDocid();
		}
		//Проверка на документы по пакету
		else if (TORLists.packlist.contains(formname)||formname.equals(TORLists.Package)||formname.equals(TORLists.FPu26)
				||formname.equals(TORLists.SF)||formname.equals(TORLists.GU45)||formname.equals(TORLists.GU23)
				||formname.equals("СДГ")||formname.equals("ГУ-2в")){
		
			docid = 0;
		}
		else docid = etdsyncfacade.getIdbyEtdid(etddocid);

		} else {
			docid = etdsyncfacade.getIdbyEtdid(etddocid);
		}
    	return docid;
    }
    


private void fakesign(long docid, int userid, int wrkid, int predid, byte[] blob, String docdata){
	callSignProcedure(docid, userid, wrkid, predid, new byte[1]);
	
	HashMap<String, Object> pp = new HashMap<String, Object>();
	pp.put("id", docid);
	pp.put("bldoc", blob);
	pp.put("docdata", docdata);
	try{
	getNpjt().update(updtblob, pp);
	} catch (Exception e){
		e.printStackTrace();
	}
}



private Map callSignProcedure(long id,int userid,int wrkid, int predid, byte[] tsp){	
MyStoredProc sproc = new MyStoredProc(getDs());
sproc.setSql("SNT.SignDoc");
sproc.declareParameter(new SqlOutParameter("out", Types.CHAR));
sproc.declareParameter(new SqlParameter("docid", Types.BIGINT));
sproc.declareParameter(new SqlParameter("userid", Types.INTEGER));
sproc.declareParameter(new SqlParameter("wrkid", Types.INTEGER));
sproc.declareParameter(new SqlParameter("predid", Types.INTEGER));
sproc.declareParameter(new SqlParameter("ts", Types.BLOB));
sproc.declareParameter(new SqlOutParameter("timestamp", Types.CHAR));
sproc.compile();
Map input = new HashMap();
input.put("docid", id);
input.put("userid", userid);
input.put("wrkid", wrkid);
input.put("predid", predid);
input.put("ts", tsp);
return sproc.execute(input);
}
}

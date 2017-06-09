package sheff.rgd.ws.Controllers.PPS;

import java.sql.Types;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.log4j.Logger;
import org.springframework.jdbc.core.SqlOutParameter;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import ru.aisa.rgd.etd.dao.ETDSyncServiceFacade;
import ru.aisa.rgd.etd.objects.ETDDocumentData;
import ru.aisa.rgd.ws.dao.ServiceFacade;
import ru.aisa.rgd.ws.documents.ETDForm;
import ru.aisa.rgd.ws.domain.Requisites;
import ru.aisa.rgd.ws.exeption.InternalException;
import ru.aisa.rgd.ws.utility.DataBinder;
import ru.aisa.rgd.ws.utility.RowHandler;
import ru.aisa.rgd.ws.utility.TypeConverter;
import sheff.rgd.ws.Abstract.AbstractAction;
import sheff.rjd.services.syncutils.SyncObj;
import sheff.rjd.services.syncutils.db2DateGenerator;
import sheff.rjd.utils.MyStoredProc;
import sheff.rjd.ws.OCO.AfterEdit.FakeSignature;
import sheff.rjd.ws.OCO.AfterSign.SendToEtd;

public class ZPPSController extends AbstractAction{

    protected final Logger	log	= Logger.getLogger(getClass());
	private NamedParameterJdbcTemplate npjt;
	private String parentform;
	private DataSource ds;
	private ServiceFacade facade;
	private ETDSyncServiceFacade etdsyncfacade;
	private FakeSignature fakesignature;
	private SendToEtd sendtoetd;
	
	public String getParentform() {
		return parentform;

	}

	public FakeSignature getFakesignature() {
		return fakesignature;
	}

	public void setFakesignature(FakeSignature fakesignature) {
		this.fakesignature = fakesignature;
	}

	public void setParentform(String parentname) {
		this.parentform = parentname;

	}

	public NamedParameterJdbcTemplate getNpjt() {
		return npjt;
	}

	public void setNpjt(NamedParameterJdbcTemplate npjt) {
		this.npjt = npjt;
	}

	public DataSource getDs() {
		return ds;
	}

	public void setDs(DataSource ds) {
		this.ds = ds;
	}

	public ServiceFacade getFacade() {
		return facade;
	}

	public void setFacade(ServiceFacade facade) {
		this.facade = facade;
	}
	
	public ETDSyncServiceFacade getEtdsyncfacade() {
		return etdsyncfacade;
	}
	
	public void setEtdsyncfacade(ETDSyncServiceFacade etdsyncfacade) {
		this.etdsyncfacade = etdsyncfacade;
	}
	
	public SendToEtd getSendtoetd() {
	    return sendtoetd;
	}
	public void setSendtoetd(SendToEtd sendtoetd) {
	    this.sendtoetd = sendtoetd;
	}

	private Map callNumProcedure(int predid, int typeid, int code) {
		MyStoredProc sproc = new MyStoredProc(getDs());
		sproc.setSql("SNT.GETDOC_M_NUM_ZPPS");
		sproc.declareParameter(new SqlOutParameter("out", Types.CHAR));
		sproc.declareParameter(new SqlParameter("PredId", Types.INTEGER));
		sproc.declareParameter(new SqlParameter("sFormID", Types.INTEGER));
		sproc.declareParameter(new SqlParameter("Code", Types.INTEGER));
		sproc.declareParameter(new SqlOutParameter("DocNum", Types.INTEGER));
		sproc.compile();
		Map input = new HashMap();
		input.put("PredId", predid);
		input.put("sFormID", typeid);
		input.put("Code", code);
		return sproc.execute(input);
	}


	@Override
	public void doAfterSign(String docName, String docdata, int predid, int signNumber, long id, String certserial, int WrkId) {

	}

	@Override
	public void doAfterSave(String docName, String docdata, int predid, int signNumber, String CertID, long id, int WrkId) throws Exception {
		try {
			ETDForm form = ETDForm.createFromArchive(docdata.getBytes("UTF-8"));
			DataBinder binder = form.getBinder();
			binder.setRootElement("data");
			final Map<String, Object> map = new HashMap<String, Object>();
			map.put("idClaim", id);
			map.put("predid", predid);
			map.put("okpo_customer", binder.getValue("P_20"));
		    map.put("performer", binder.getValue("P_4"));
		    
			SimpleDateFormat myDateFormat1 = new SimpleDateFormat();
			
			String P7  = binder.getValue("P_7");
			if(P7.contains("-")) {
				myDateFormat1.applyPattern("yyyy-MM-dd");
			} else {
				myDateFormat1.applyPattern("dd.MM.yyyy");
			}
	  	    Date P_7 = myDateFormat1.parse(P7);
	  	    
			String P8 = binder.getValue("P_8");
			if(P8.contains("-")) {
				myDateFormat1.applyPattern("yyyy-MM-dd");
			} else {
				myDateFormat1.applyPattern("dd.MM.yyyy");
			}
	  	    Date P_8 = myDateFormat1.parse(P8);

			map.put("date_doc", P_8);
			map.put("date_doc_1", P_8);
			binder.handleTable("table1", "row", new RowHandler<Object>() {
				public void handleRow(DataBinder kinder, int rowNum, Object obj) throws InternalException
				{
					map.put("vagnum", kinder.getValue("P_9"));
					map.put("name_prod_from", kinder.getValue("P_10"));
					map.put("name_prod_to", kinder.getValue("P_11"));
					map.put("num_operation", kinder.getValue("P_12"));
	                int p = getNpjt().queryForInt("select case when id_app_treatment = :idClaim "
	                		+ "then 1 else 2 end p from snt.ppsreport "
	                		+ " where id = (select id from snt.ppsreport where vagnum = :vagnum "
	                		+ " and predid in (select id from snt.pred where headid = :predid "
	                		+ " or id = :predid))", map);
	                if(p == 1) {
	                	String update = "update snt.ppsreport set vagnum =:vagnum, name_prod_from = :name_prod_from, "
								+ "name_prod_to = :name_prod_to, num_operation = :num_operation,"
								+ "okpo_customer = :okpo_customer, date_doc = :date_doc, performer = :performer "
								+ "where id = (select id from snt.ppsreport where vagnum = :vagnum "
								+ "and predid in (select id from snt.pred where headid = :predid or id = :predid))";
						getNpjt().update(update, map);
	                } else if(p == 2) {
	                	String update = "update snt.ppsreport set vagnum =:vagnum, name_prod_from_1= :name_prod_from, "
								+ "name_prod_to_1 = :name_prod_to, num_operation_1 = :num_operation,"
								+ "okpo_customer = :okpo_customer, date_doc_1 = :date_doc_1, performer = :performer "
								+ "where id = (select id from snt.ppsreport where vagnum = :vagnum "
								+ "and predid in (select id from snt.pred where headid = :predid or id = :predid))";
						getNpjt().update(update, map);
	                }

				}
			}, null);

		} catch (Exception e) {
//			e.printStackTrace();
			log.error(TypeConverter.exceptionToString(e));
		}

		
	}

	@Override
	public void doAfterSync(String formname, SyncObj obj, String sql, int signum) {

		
	}
	public ETDDocumentData EditBeforeOpen(String docName, ETDDocumentData doc) {
		// TODO Auto-generated method stub
		return doc;
	}
	
}

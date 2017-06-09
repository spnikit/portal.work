package sheff.rgd.ws.Controllers.PPS;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.sql.Types;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;
import javax.xml.transform.TransformerException;

import org.apache.log4j.Logger;
import org.springframework.jdbc.core.SqlOutParameter;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import ru.aisa.rgd.etd.dao.ETDSyncServiceFacade;
import ru.aisa.rgd.etd.objects.ETDDocumentData;
import ru.aisa.rgd.ws.dao.ServiceFacade;
import ru.aisa.rgd.ws.documents.ETDForm;
import ru.aisa.rgd.ws.exeption.InternalException;
import ru.aisa.rgd.ws.exeption.ServiceException;
import ru.aisa.rgd.ws.utility.DataBinder;
import ru.aisa.rgd.ws.utility.RowHandler;
import ru.aisa.rgd.ws.utility.TypeConverter;
import sheff.rgd.ws.Abstract.AbstractAction;
import sheff.rjd.services.syncutils.SyncObj;
import sheff.rjd.utils.MyStoredProc;
import sheff.rjd.ws.OCO.AfterEdit.FakeSignature;

public class VU19Controller extends AbstractAction{

    protected final Logger	log	= Logger.getLogger(getClass());
	private NamedParameterJdbcTemplate npjt;
	private String parentform;
	private DataSource ds;
	private ServiceFacade facade;
	private ETDSyncServiceFacade etdsyncfacade;
	private FakeSignature fakesignature;
	
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
	
	
	private Map callNumProcedure(int predid) {
		MyStoredProc sproc = new MyStoredProc(getDs());
		sproc.setSql("SNT.GETDOC_M_NUM_PPS");
		sproc.declareParameter(new SqlOutParameter("out", Types.CHAR));
		sproc.declareParameter(new SqlParameter("PredId", Types.INTEGER));
		sproc.declareParameter(new SqlOutParameter("DocNum", Types.INTEGER));
		sproc.compile();
		Map input = new HashMap();
		input.put("PredId", predid);
		return sproc.execute(input);
	}

	@Override
	public void doAfterSign(String docName, String docdata, int predid, int signNumber, long id, String certserial, int WrkId) {
		try {
			if(signNumber == 1) {
				ETDForm form = ETDForm.createFromArchive(docdata.getBytes("UTF-8"));
				DataBinder binder = form.getBinder();
				String selectDateSign = "select ldate from snt.docstore where id = :id";
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("id", id);
		
				String no = callNumProcedure(predid).get("DocNum").toString();
				binder.setNodeValue("vu_number", no);
				Date dateSign = getNpjt().queryForObject(selectDateSign, map, Date.class);
				SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
				binder.setNodeValue("dat_formirov", sdf.format(dateSign));
				map.put("bldoc", form.encodeToArchiv());
				map.put("docdata", form.transform("data"));
				map.put("reqdate", dateSign);
				map.put("no", no);
				getNpjt().update("update snt.docstore set no = :no, bldoc = :bldoc,"
						+ " docdata = :docdata, reqdate = :reqdate where id = :id", map);

			} else if(signNumber == 3) {
				ETDForm form = ETDForm.createFromArchive(docdata.getBytes("UTF-8"));
				DataBinder binder = form.getBinder();
				Long idClaim = Long.parseLong(binder.getValue("P_9"));
				
				updateClaim(idClaim, binder);
				
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("id", id);
				SqlRowSet rs = getNpjt().queryForRowSet("select id, type_act, type_act_1,"
						+ " case WHEN id_act_workability is not null THEN 1 end p1, " 
						+ " case WHEN id_act_workability_1 is not null THEN 2 end p2 "
						+ " from snt.ppsreport where id_act_workability = :id "
						+ " or id_act_workability_1 = :id", map);
				int priznak = 0;
				while(rs.next()) {
					
					if(rs.getInt("p1") != 0) {
						priznak = 1;
					}
					if(rs.getInt("p2") != 0) {
						priznak = 2;
					}
					
					map.put("docid", rs.getInt("id"));
					map.put("num_act", binder.getValue("vu_number"));
					SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
					map.put("date_act", sdf.parse(binder.getValue("dat_formirov")));
					if(rs.getInt("type_act") != 0 && rs.getInt("type_act") != 3) {
						getNpjt().update("update snt.ppsreport set num_act = :num_act, "
								+ "date_act = :date_act "
								+ "where id = :docid", map);
					}
					if(rs.getInt("type_act_1") != 0 && rs.getInt("type_act_1") != 3) {
						getNpjt().update("update snt.ppsreport set num_act_1 = :num_act, "
								+ "date_act_1 = :date_act where id = :docid", map);
					}
				}
				
				if(priznak == 1) {
					int count = getNpjt().queryForInt("select count(0) from snt.ppsreport "
							+ "where id = :docid and id_gu45_ub is not null "
							+ " and id_app_treatment is not null", map);
					if(count > 0) {
						map.put("complete", 1);
						getNpjt().update("update snt.ppsreport set complete = :complete "
								+ " where id = :docid",map);
					} 
				} else if(priznak == 2) {
					int count = getNpjt().queryForInt("select count(0) from snt.ppsreport"
							+ " where id = :docid "
							+ " and id_gu45_ub is not null"
							+ " and id_app_treatment_1 is not null", map);
					if(count > 0) {
						map.put("complete", 1);
						getNpjt().update("update snt.ppsreport set complete = :complete  "
								+ " where id = :docid", map);
					} 

				}
			}
		} catch(Exception e) {
			log.error(TypeConverter.exceptionToString(e));
			e.printStackTrace();
		}
	}

	@Override
	public void doAfterSave(String docName, String docdata, int predid, int signNumber, String CertID, long id, int WrkId) throws Exception {}

	@Override
	public void doAfterSync(String formname, SyncObj obj, String sql, int signum) {
		
	}
	public ETDDocumentData EditBeforeOpen(String docName, ETDDocumentData doc) {
		try {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("id", doc.getWrkid());
			String wrkName = getNpjt().queryForObject("select rtrim(name) from snt.wrkname "
					+ "where id = :id", map, String.class);
			byte[] blob = doc.getBlDoc();
			ETDForm form = ETDForm.createFromArchive(blob);
			DataBinder binder = form.getBinder();
			binder.setNodeValue("user_role", wrkName);
			doc.setBlDoc(form.encodeToArchiv());
		} catch (Exception e) {
			log.error(TypeConverter.exceptionToString(e));
		}
		return doc;
	}
	
	private void updateClaim(Long idClaim, final DataBinder binder) throws UnsupportedEncodingException, ServiceException, IOException, InternalException, TransformerException {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("idClaim", idClaim);
		byte[] blob = (byte[])npjt.queryForObject("select bldoc from snt.docstore "
				+ "where id = :idClaim", map, byte[].class);
		ETDForm formClaim = ETDForm.createFromArchive(blob);
		final DataBinder binderClaim = formClaim.getBinder();
		final String vagnum = binder.getValue("num_cisterni");
		final String P_14InClaim = "ВУ-19 " + binder.getValue("vu_number") + " " + binder.getValue("dat_formirov");
		binderClaim.setRootElement("data");
		
		
		int countCompleteVag1 = binderClaim.getInt("P_21");
		binderClaim.handleTable("table1", "row", new RowHandler<Object>() {
			public void handleRow(DataBinder kinder, int rowNum, Object obj) throws InternalException
			{   
				if(kinder.getValue("P_9").equals(vagnum)) {
					kinder.setNodeValue("P_14", P_14InClaim);
				}

			}
		}, null);
		String[] mass =  binderClaim.getValuesAsArray("P_14");
		int countCompleteVag = 0;
		for(String s: mass) {
			if(s.length() > 0) {
				countCompleteVag++;
			}
		}
		binderClaim.setNodeValue("P_21", countCompleteVag);
        //проверка на перевод в архив и в документы в работе
		int countCompleteVag2 = binderClaim.getInt("P_21");
		boolean toDocumentInWork = false;
		if(countCompleteVag2 > countCompleteVag1) {
			toDocumentInWork = true;
		}
        String opisanie  = countCompleteVag2 + " из " +
        		binderClaim.getValue("P_19") + " обработано";
        blob = formClaim.encodeToArchiv();
        String docdata = formClaim.transform("data");
        map.put("bldoc", blob);
        map.put("docdata", docdata);
        map.put("opisanie", opisanie);
        if(countCompleteVag2 == binderClaim.getInt("P_19")) {
        	getNpjt().update("update snt.docstore set bldoc = :bldoc, docdata = :docdata,"
        			+ " opisanie = :opisanie, signlvl = null where id = :idClaim", map);
        } else if(toDocumentInWork){
			getNpjt().update("update snt.docstore set bldoc = :bldoc, docdata = :docdata, "
					+ " opisanie = :opisanie, signlvl = 1 where id = :idClaim", map);
        } else {
			getNpjt().update("update snt.docstore set bldoc = :bldoc, docdata = :docdata, "
					+ " opisanie = :opisanie where id = :idClaim", map);
        }
	}
}
package sheff.rgd.ws.Controllers.PPS;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.print.attribute.HashAttributeSet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import javax.xml.transform.TransformerException;

import org.apache.log4j.Logger;
import org.apache.tools.ant.types.CommandlineJava.SysProperties;
import org.json.JSONException;
import org.springframework.jdbc.InvalidResultSetAccessException;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.transaction.support.TransactionTemplate;
import org.springframework.web.servlet.ModelAndView;
import org.w3c.dom.DOMException;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.aisa.htmlgenerator.utils.Table;

import ru.aisa.etdadmin.controllers.AbstractMultipartController;
import ru.aisa.rgd.etd.dao.ETDDocumentDao;
import ru.aisa.rgd.etd.dao.ETDUserDao;
import ru.aisa.rgd.ws.dao.ServiceFacade;
import ru.aisa.rgd.ws.documents.ETDForm;
import ru.aisa.rgd.ws.domain.Document;
import ru.aisa.rgd.ws.exeption.InternalException;
import ru.aisa.rgd.ws.exeption.ServiceException;
import ru.aisa.rgd.ws.utility.DataBinder;
import ru.aisa.rgd.ws.utility.RowHandler;
import ru.aisa.rgd.ws.utility.TypeConverter;

public class VUGen  {
	
    protected final Logger	log	= Logger.getLogger(getClass());
	private ServiceFacade facade;
	private DataSource ds;
	private NamedParameterJdbcTemplate npjt;
	private TransactionTemplate transT;
	
	public DataSource getDs() {
		return ds;
	}

	public void setDs(DataSource ds) {
		this.ds = ds;
	}

	public NamedParameterJdbcTemplate getNpjt() {
		return npjt;
	}

	public void setNpjt(NamedParameterJdbcTemplate npjt) {
		this.npjt = npjt;
	}
	
	
	public ServiceFacade getFacade() {
		return facade;
	}

	public void setFacade(ServiceFacade facade) {
		this.facade = facade;
	}
	
	public TransactionTemplate getTransT() {
		return transT;
	}
	public void setTransT(TransactionTemplate transT) {
		this.transT = transT;
	}


	
	public VUGen() throws JSONException{
		super();
	}
	    
	
	private int generateVU20a(List<VU20a> vu20aList) throws Exception {
		
		int countCreateVU20a = 0;
		for(VU20a object: vu20aList) {
//			System.out.println("gen vu20a");
			
			String okpo_perf = object.getOkpo_perf();
			String perf = object.getPerf();
			String nameProdTo = object.getNameProdTo();
	  	    
			ETDForm form = new ETDForm(facade.getDocumentTemplate("ВУ-20а"));
			DataBinder binder = form.getBinder();
			
			binder.setNodeValue("stName",perf);
			binder.setNodeValue("stCode", okpo_perf);
			binder.setNodeValue("nProductName", nameProdTo);
			
			List<RowTable1> list  = object.getList();
			
			binder.setRootElement("table1");
			Node templateRowForTabl1 = binder.getNode("row");
			binder.resetRootElement();
			binder.setRootElement("table2");
			Node templateRowForTabl2 = binder.getNode("row");
			binder.resetRootElement();
			binder.setRootElement("data");
			int rownum = 0;
			List<String> vagnumList = new ArrayList<String>();
			for(int i = 0; i < list.size(); i++) {
				RowTable1 r = list.get(i);
				vagnumList.add(r.getVagnum());
				if(i == 0) {
					binder.setRootElement("table1");
					binder.setNodeValue("vagNum", r.getVagnum());
					binder.setNodeValue("rowNum", ++rownum);
					binder.setNodeValue("sProductName", r.getNameProdFrom());
					binder.setNodeValue("operNum", r.getNumOperation());
					binder.setNodeValue("P_22", r.getIdClaim());
					binder.setNodeValue("P_23", r.getNumClaim());
					binder.setNodeValue("P_24", r.getDateClaim());
				} else if(i == 1) {
					binder.resetRootElement();
					binder.setRootElement("table2");
					binder.setNodeValue("vagNum", r.getVagnum());
					binder.setNodeValue("rowNum", rownum);
					binder.setNodeValue("sProductName", r.getNameProdFrom());
					binder.setNodeValue("operNum", r.getNumOperation());
					binder.setNodeValue("P_22", r.getIdClaim());
					binder.setNodeValue("P_23", r.getNumClaim());
					binder.setNodeValue("P_24", r.getDateClaim());
					binder.resetRootElement();
				} else if(i % 2 == 0) {
					binder.setRootElement("data");
					Node tempRow = templateRowForTabl1.cloneNode(true);
					Node table = binder.getNode("table1");
					binder.setRootElement("table1");
					table.appendChild(tempRow);
					binder.getLastNode("vagNum").setTextContent(r.getVagnum());
					binder.getLastNode("rowNum").setTextContent(String.valueOf(++rownum));
					binder.getLastNode("sProductName").setTextContent(r.getNameProdFrom());
					binder.getLastNode("operNum").setTextContent(r.getNumOperation());
					binder.getLastNode("P_22").setTextContent(String.valueOf(r.getIdClaim()));
					binder.getLastNode("P_23").setTextContent(r.getNumClaim());
					binder.getLastNode("P_24").setTextContent(r.getDateClaim());
					binder.resetRootElement();
				} else if(i % 2 == 1) {
					binder.setRootElement("data");
					Node tempRow = templateRowForTabl2.cloneNode(true);
					Node table = binder.getNode("table2");
					binder.setRootElement("table2");
					table.appendChild(tempRow);
					binder.getLastNode("vagNum").setTextContent(r.getVagnum());
					binder.getLastNode("rowNum").setTextContent(String.valueOf(rownum));
					binder.getLastNode("sProductName").setTextContent(r.getNameProdFrom());
					binder.getLastNode("operNum").setTextContent(r.getNumOperation());
					binder.getLastNode("P_22").setTextContent(String.valueOf(r.getIdClaim()));
					binder.getLastNode("P_23").setTextContent(r.getNumClaim());
					binder.getLastNode("P_24").setTextContent(r.getDateClaim());
					binder.resetRootElement();
				}
			}	
			binder.setRootElement("data");
			binder.setNodeValue("P_25",  object.getCustomer());
			binder.setNodeValue("P_25_1", object.getOkpo_customer());
//			System.out.println(form.transform("data"));
			
			String opisanie  = binder.getValuesAsArray("rowNum").length + 
					 " цс под " + binder.getValue("nProductName"); 
		Document document = new Document();
		Long id = facade.getDocumentDao().getNextId();
		document.setBlDoc(form.encodeToArchiv());
		document.setDocData(form.transform("data"));
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("okpo_kod", object.getOkpo_perf());
		int predId = getNpjt().queryForInt("select id from snt.pred where "
				+ "okpo_kod = :okpo_kod and headid is null", map);
		document.setPredId(predId);
		document.setSignLvl(0);
		document.setType("ВУ-20а");
		document.setId(id);
		facade.insertDocumentWithDocid(document);
		countCreateVU20a++;
		
		map.put("id_act_workability", id);
		map.put("opisanie", opisanie);
		map.put("rem_pred", object.getPerf());
		getNpjt().update("update snt.docstore set opisanie = :opisanie, rem_pred = :rem_pred "
				+ " where id = :id_act_workability", map) ;
		
		if(object.getIdList1().size() > 0) {
			map.put("idList", object.getIdList1());
			getNpjt().update("update snt.ppsreport set id_act_workability = :id_act_workability "
					+ "where id in (:idList)", map);
		}
		if(object.getIdList2().size() > 0) {
			map.put("idList", object.getIdList2());
			getNpjt().update("update snt.ppsreport set id_act_workability_1 = :id_act_workability "
					+ "where id in (:idList)", map);
		}
		map.put("okpo_kod_customer", object.getOkpo_customer());
		int predIdCustomer = getNpjt().queryForInt("select id from snt.pred where "
				+ "okpo_kod = :okpo_kod_customer and headid is null", map);
		map.put("predId", predIdCustomer);
        getNpjt().update("insert into snt.marsh (docid, predid) values(:id_act_workability, :predId)", map);
        
		for(String vagnum: vagnumList) {
			map.put("vagnum", vagnum);
			map.put("predid", predId);
			Long idRecordPPS = getNpjt().queryForLong("select id from snt.ppsreport "
					+ "where vagnum = :vagnum and predid = :predid", map);
			setP16FieldInClaim(idRecordPPS, vagnum, id);
		}
	    log.debug("gen vu20a " + form.transform("data"));
		}
		return countCreateVU20a;
	}
	
	
	public int generateVU20(List<VU20> vu20List) throws Exception {
		int countCreateVU20 = 0;
		for(VU20 object: vu20List) {
			//System.out.println("gen vu20");

			ETDForm form = new ETDForm(facade.getDocumentTemplate("ВУ-20"));
			DataBinder binder = form.getBinder();
			
			binder.setNodeValue("esr1", object.getPerf());
			binder.setNodeValue("kodesr1", object.getOkpo_perf());
			binder.setNodeValue("productIn", object.getNameProdFrom());
			binder.setNodeValue("productOut", object.getNameProdTo());
			binder.setNodeValue("oper", object.getNumOperation());
			binder.setNodeValue("cist", object.getVagnum());
			binder.setNodeValue("P_16", object.getIdClaim());
			binder.setNodeValue("P_17", object.getNumClaim());
			binder.setNodeValue("P_18", object.getDateClaim());
			binder.setNodeValue("P_19", object.getCustomer());
			binder.setNodeValue("P_19_1", object.getOkpo_customer());
		
			Document document = new Document();
			Long id = facade.getDocumentDao().getNextId();
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("okpo_kod", object.getOkpo_perf());
			map.put("id", object.getId());
			map.put("id_act_workability", id);
			int predId = getNpjt().queryForInt("select id from snt.pred where "
					+ "okpo_kod = :okpo_kod and headid is null", map);
			document.setPredId(predId);
			document.setSignLvl(0);
			document.setType("ВУ-20");
			document.setId(id);
			document.setBlDoc(form.encodeToArchiv());
			document.setDocData(form.transform("data"));
			facade.insertDocumentWithDocid(document);
			countCreateVU20++;
			map.put("opisanie", object.getVagnum() + " " + object.getNameProdTo());
			map.put("rem_pred", object.getPerf());
			getNpjt().update("update snt.docstore set opisanie = :opisanie, rem_pred = :rem_pred"
					+ " where id = :id_act_workability", map) ;
			if(object.getPriznak() == 1) {
				getNpjt().update("update snt.ppsreport set  id_act_workability = :id_act_workability where id = :id",map);
			} else if(object.getPriznak() == 2) {
			    getNpjt().update("update snt.ppsreport set id_act_workability_1 = :id_act_workability where id = :id",map);
			}
			map.put("okpo_kod_customer", object.getOkpo_customer());
			int predIdCustomer = getNpjt().queryForInt("select id from snt.pred where "
					+ "okpo_kod = :okpo_kod_customer and headid is null", map);
			map.put("predId", predIdCustomer);
			getNpjt().update("insert into snt.marsh (docid, predid) values(:id_act_workability, :predId)", map);
//			System.out.println(form.transform("data"));
			setP16FieldInClaim(object.getId(), binder.getValue("cist"), id);
		    log.debug("gen vu20 with id = " + id +" " + form.transform("data"));
			}
			return countCreateVU20;
	}

	public boolean generateGU2b(int predid) {
//		System.out.println("generate GU2b");
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("predid", predid);
		String select = "select id, vagnum, code_station, okpo_customer, performer from snt.ppsreport "
				+ " where idGu2b is null and complete is null "
				+ " and (num_act is not null or num_act_1 is not null)"
				+ " and predid in "
				+ "(select id from snt.pred where headid = :predid or id = :predid)"; 
		String okpo_code = getNpjt().queryForObject("select okpo_kod from snt.pred"
				+ " where id = :predid", map, String.class);
		boolean isCreate = false;
		try {
			ETDForm form = new ETDForm(facade.getDocumentTemplate("ГУ-2б"));
			DataBinder binder = form.getBinder();
			SqlRowSet rs = getNpjt().queryForRowSet(select, map);
			
			binder.setRootElement("table1");
			Node templateRowForTabl1 = binder.getNode("row");
			binder.resetRootElement();
			binder.setRootElement("table4");
			Node templateRowForTabl4 = binder.getNode("row");
			binder.resetRootElement();
			int countRecord = 0;
			List<Long> listId = new ArrayList<Long>(); 
			while(rs.next()) {
				listId.add(rs.getLong("id"));
				if(rs.isFirst()) {
					binder.setNodeValue("P_1a", rs.getString("code_station"));
					binder.setNodeValue("P_4", rs.getString("performer"));
					binder.setNodeValue("P_9_2", rs.getString("vagnum"));
//					binder.setNodeValue("P_41", rs.getString("okpo_customer"));
					binder.setNodeValue("P_9_2_1", rs.getString("okpo_customer"));
				} else {
					Node tempRow = templateRowForTabl1.cloneNode(true);
					Node table = binder.getNode("table1");
					table.appendChild(tempRow);
					binder.getLastNode("P_9_2").setTextContent(rs.getString("vagnum"));
					binder.getLastNode("P_9_2_1").setTextContent( rs.getString("okpo_customer"));
					tempRow = templateRowForTabl4.cloneNode(true);
					table = binder.getNode("table4");
					table.appendChild(tempRow);
//					binder.getLastNode("P_41").setNodeValue(rs.getString("okpo_customer"));
				}
					binder.setNodeValue("P_4a", okpo_code); 
					
					countRecord++;
			}	
            if(countRecord > 0) {
            	isCreate = true;
//            	System.out.println(form.transform("data"));
    			Document document = new Document();
    			Long id = facade.getDocumentDao().getNextId();
    			document.setBlDoc(form.encodeToArchiv());
    			document.setDocData(form.transform("data"));
    			document.setPredId(predid);
    			document.setSignLvl(0);
    			document.setType("ГУ-2б");
    			document.setId(id);
    			facade.insertDocumentWithDocid(document);
    			map.put("idgu2b", id);
    			map.put("listId", listId);
    			getNpjt().update("update snt.ppsreport set idgu2b = :idgu2b where id in (:listId)", map);
//    			map.put("id", rs.getInt("id"));
//    			map.put("num_act", binder.getValue("P_1"));
//    			getNpjt().update("update snt.ppsreport set num_act = :num_act where id = :id",map);
    		    log.debug("gen gu2b " + form.transform("data"));
            }
			
		} catch (Exception e) {
			log.error(TypeConverter.exceptionToString(e));
			isCreate = false;
//			e.printStackTrace();
		}
		return isCreate;
	
	}
	
private int generateVU19(List<VU20> vu19List) throws Exception {
		int countCreateVU19 = 0;
		for(VU20 object: vu19List) {
//			System.out.println("gen vu19");
			
			ETDForm form = new ETDForm(facade.getDocumentTemplate("ВУ-19"));
			DataBinder binder = form.getBinder();
			
			binder.setNodeValue("num_cisterni", object.getVagnum());
			binder.setNodeValue("rzd_pred_name", object.getPerf());
			binder.setNodeValue("rzd_pred_id", object.getOkpo_perf());
			binder.setNodeValue("P_9", object.getIdClaim());
			binder.setNodeValue("P_10", object.getNumClaim());
			binder.setNodeValue("P_11", object.getDateClaim());
			binder.setNodeValue("P_12", object.getCustomer());
			binder.setNodeValue("P_12_1", object.getOkpo_customer());
			
			Document document = new Document();
			Long id = facade.getDocumentDao().getNextId();
			document.setBlDoc(form.encodeToArchiv());
			document.setDocData(form.transform("data"));
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("okpo_kod", object.getOkpo_perf());
			int predId = getNpjt().queryForInt("select id from snt.pred where "
					+ "okpo_kod = :okpo_kod and headid is null", map);
			document.setPredId(predId);
			document.setSignLvl(0);
			document.setType("ВУ-19");
			document.setId(id);
			facade.insertDocumentWithDocid(document);
			countCreateVU19++;
			
			map.put("id", object.getId());
			map.put("opisanie", object.getVagnum());
			map.put("id_act_workability", id);
			map.put("rem_pred", object.getPerf());
			getNpjt().update("update snt.docstore set opisanie = :opisanie, rem_pred = :rem_pred"
					+ " where id = :id_act_workability", map) ;
			if(object.getPriznak() == 1) {
				getNpjt().update("update snt.ppsreport set  id_act_workability = :id_act_workability where id = :id",map);
			} else if(object.getPriznak() == 2) {
			    getNpjt().update("update snt.ppsreport set id_act_workability_1 = :id_act_workability where id = :id",map);
			}
			map.put("okpo_kod_customer", object.getOkpo_customer());
			int predIdCustomer = getNpjt().queryForInt("select id from snt.pred where "
					+ "okpo_kod = :okpo_kod_customer and headid is null", map);
			map.put("predId", predIdCustomer);
			getNpjt().update("insert into snt.marsh (docid, predid) values(:id_act_workability, :predId)", map);
			setP16FieldInClaim(object.getId(), binder.getValue("num_cisterni"), id);
			log.debug("gen vu19 " + form.transform("data"));
//			System.out.println(form.transform("data"));
		}
		return countCreateVU19;
	}
	public int generateVU(int predid, List<Long> listId) {
		int countVu = 0;
		try {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("predid", predid);
		map.put("listId", listId);
		
		String selectForVU20a = " with main as ( select id, vagnum, NAME_PROD_FROM, OKPO_CUSTOMER, APP_NUM, " + 
				 "               NUM_OPERATION, CUSTOMER, ID_APP_TREATMENT, DATE_DOC, NAME_PROD_TO," + 
				 "               OKPO_PERFORMER, PERFORMER, 1 p " + 
				 "               from snt.ppsreport where " + 
				 "                                   TYPE_ACT = 1 and id_act_workability is null" + 
				 "                             and predid in (select id from snt.pred where headid = :predid or id = :predid) and complete is null " + 
				 "                             and id_gu45_pod is not null" + 
				 "                                    union  " + 
				 "               select id, vagnum, NAME_PROD_FROM_1 as NAME_PROD_FROM, OKPO_CUSTOMER, APP_NUM_1 as APP_NUM,  " + 
				 "                          NUM_OPERATION_1 as NUM_OPERATION, CUSTOMER, ID_APP_TREATMENT_1 as ID_APP_TREATMENT, DATE_DOC_1 as DATE_DOC, " + 
				 "                          NAME_PROD_TO_1 as NAME_PROD_TO, OKPO_PERFORMER, PERFORMER, 2 p  " + 
				 "                          from snt.ppsreport where " + 
				 "                                         id_act_workability_1 is null and TYPE_ACT_1 = 1 " + 
				 "                 and predid in (select id from snt.pred where headid = :predid or id = :predid) and complete is null " + 
				 "                   and id_gu45_pod is not null) " + 
				 "                  select * from main where  NAME_PROD_TO in (select NAME_PROD_TO FROM main " + 
				 "                        GROUP BY NAME_PROD_TO, okpo_customer HAVING COUNT(*) > 1) and id in (:listId) order by name_prod_to";
        
     
        
        SqlRowSet rsForVU20a = getNpjt().queryForRowSet(selectForVU20a, map);
        String Okpo_customer = "";
        String customer = "";
        String Name_prod_to = "";
    	VU20a vu20a = new VU20a();
    	List<RowTable1> list = new ArrayList<RowTable1>();
    	List<VU20a> vu20aList = new ArrayList<VU20a>();
    	List<Long> idList1 = new ArrayList<Long>();
    	List<Long> idList2 = new ArrayList<Long>();
        while(rsForVU20a.next()) {
        	if(rsForVU20a.isFirst()) {
        		Okpo_customer = rsForVU20a.getString("OKPO_CUSTOMER");
        	    Name_prod_to = rsForVU20a.getString(10);
        	    customer = rsForVU20a.getString("CUSTOMER");
        	    if(rsForVU20a.getInt("p") == 1) {
        	        idList1.add(rsForVU20a.getLong("id"));
        	    } else if(rsForVU20a.getInt("p") == 2) {
            	    idList2.add(rsForVU20a.getLong("id"));
        	    }
        	
        	}
//        		System.out.println(rsForVU20a.getString("id"));
        		if(!(rsForVU20a.getString(10).equals(Name_prod_to) &&
        				rsForVU20a.getString("Okpo_customer").equals(Okpo_customer))) {
        			
        			vu20aList.add(vu20a);
        			vu20a = new VU20a();
        			list = new ArrayList<RowTable1>();
        			idList1 = new ArrayList<Long>();
        			idList2 = new ArrayList<Long>();
        			Name_prod_to= rsForVU20a.getString(10);
        			Okpo_customer = rsForVU20a.getString("Okpo_customer");
        			
        			RowTable1 row = new RowTable1();
        			row.setNumClaim(rsForVU20a.getString(5));
        			row.setNumOperation(rsForVU20a.getString(6));
        			row.setNameProdFrom(rsForVU20a.getString(3));
        			row.setIdClaim(rsForVU20a.getInt(8));
        			row.setVagnum(rsForVU20a.getString("VAGNUM"));
					Date dateClaim = rsForVU20a.getDate(9);
					SimpleDateFormat myDateFormat1 = new SimpleDateFormat("dd.MM.yyyy");
			  	    String date = myDateFormat1.format(dateClaim);
        			row.setDateClaim(date);
        			list.add(row);
        			 if(rsForVU20a.getInt("p") == 1) {
             	        idList1.add(rsForVU20a.getLong("id"));
             	    } else if(rsForVU20a.getInt("p") == 2) {
                 	    idList2.add(rsForVU20a.getLong("id"));
             	    }
        			vu20a.setNameProdTo(Name_prod_to);
        			vu20a.setPerf(rsForVU20a.getString("PERFORMER"));
        			vu20a.setOkpo_perf(rsForVU20a.getString("OKPO_PERFORMER"));
        			vu20a.setList(list);
        			vu20a.setIdList1(idList1);
        			vu20a.setIdList2(idList2);
        			vu20a.setCustomer(customer);
        			vu20a.setOkpo_customer(Okpo_customer);
        			if(rsForVU20a.isLast()) {
        				vu20aList.add(vu20a);
        			}
        		}  else {
	        		Okpo_customer = rsForVU20a.getString("OKPO_CUSTOMER");
	        	    Name_prod_to = rsForVU20a.getString(10);
	    			RowTable1 row = new RowTable1();
	    			row.setNumClaim(rsForVU20a.getString(5));
	    			row.setNumOperation(rsForVU20a.getString(6));
	    			row.setNameProdFrom(rsForVU20a.getString(3));
	    			row.setIdClaim(rsForVU20a.getInt(8));
	    			row.setVagnum(rsForVU20a.getString("VAGNUM"));
					Date dateClaim = rsForVU20a.getDate(9);
					SimpleDateFormat myDateFormat1 = new SimpleDateFormat("dd.MM.yyyy");
			  	    String date = myDateFormat1.format(dateClaim);
	    			row.setDateClaim(date);
	    			list.add(row);
	    			 if(rsForVU20a.getInt("p") == 1) {
	         	        idList1.add(rsForVU20a.getLong("id"));
	         	    } else if(rsForVU20a.getInt("p") == 2) {
	             	    idList2.add(rsForVU20a.getLong("id"));
	         	    }
	    			vu20a.setNameProdTo(Name_prod_to);
	    			vu20a.setPerf(rsForVU20a.getString("PERFORMER"));
	    			vu20a.setOkpo_perf(rsForVU20a.getString("OKPO_PERFORMER"));
	    			vu20a.setList(list);
	    			vu20a.setIdList1(idList1);
	    			vu20a.setIdList2(idList2);
        			vu20a.setCustomer(customer);
        			vu20a.setOkpo_customer(Okpo_customer);
        			if(rsForVU20a.isLast()) {
        				vu20aList.add(vu20a);
        			}
	    	
	    		}
     
        }
        
        countVu = generateVU20a(vu20aList);
        
        String selectForVU20 = "select id, vagnum, NAME_PROD_FROM, OKPO_CUSTOMER, APP_NUM, "
        		+ "NUM_OPERATION, CUSTOMER, ID_APP_TREATMENT, DATE_DOC, NAME_PROD_TO, "
        		+ "OKPO_PERFORMER, PERFORMER, case WHEN TYPE_ACT is not null THEN 1 end p "
        		+ "from snt.ppsreport where NAME_PROD_TO in " + 
        		"                  (select NAME_PROD_TO FROM snt.ppsreport where  " + 
        		"                      TYPE_ACT = 1 and id_act_workability is null and ID_GU45_POD is not null and predid in " + 
        		"						(select id from snt.pred where headid = :predid or id = :predid)" +
        		"                  GROUP BY NAME_PROD_TO HAVING COUNT(*) = 1) " + 
        		" and TYPE_ACT = 1 and id_act_workability is null and ID_GU45_POD is not null and predid in" +
        		"						(select id from snt.pred where headid = :predid or id = :predid) and complete is null "
        		+ "and id in (:listId)" +
        		" union " +
        		
		        " select id, vagnum, NAME_PROD_FROM_1, OKPO_CUSTOMER, APP_NUM_1," + 
		        "        		 NUM_OPERATION_1, CUSTOMER, ID_APP_TREATMENT_1, DATE_DOC_1, NAME_PROD_TO_1, " + 
		        "        		 OKPO_PERFORMER, PERFORMER,  case WHEN TYPE_ACT_1 is not null THEN 2 end p " + 
		        "        		 from snt.ppsreport where NAME_PROD_TO_1 in   " + 
		        "        		                    (select NAME_PROD_TO_1 FROM snt.ppsreport where   " + 
		        "        		                      TYPE_ACT_1 = 1 and id_act_workability_1 is null and ID_GU45_POD is not null and predid in " + 
		        "						(select id from snt.pred where headid = :predid or id = :predid)" +
		        "        		                  GROUP BY NAME_PROD_TO_1 HAVING COUNT(*) = 1) " + 
		        " and TYPE_ACT_1 = 1 and id_act_workability_1 is null and ID_GU45_POD is not null and predid in " + 
		        "		    (select id from snt.pred where headid = :predid or id = :predid) and complete is null"
		        + " and id in (:listId)";
        
        
        
        SqlRowSet rsForVU20 = getNpjt().queryForRowSet(selectForVU20, map);
    	List<VU20> vu20List = new ArrayList<VU20>();
        while(rsForVU20.next()) {
        	VU20 vu20 = new VU20();
        	vu20.setPriznak(rsForVU20.getInt("p"));
			vu20.setId(rsForVU20.getLong("id"));
			vu20.setNumClaim(rsForVU20.getString(5));
			vu20.setNumOperation(rsForVU20.getString(6));
			vu20.setCustomer(rsForVU20.getString("CUSTOMER"));
			vu20.setNameProdFrom(rsForVU20.getString(3));
			vu20.setIdClaim(rsForVU20.getInt(8));
			vu20.setOkpo_customer(rsForVU20.getString("OKPO_CUSTOMER"));
			vu20.setVagnum(rsForVU20.getString("VAGNUM"));
			Date dateClaim = rsForVU20.getDate(9);
			SimpleDateFormat myDateFormat1 = new SimpleDateFormat("dd.MM.yyyy");
	  	    String date = myDateFormat1.format(dateClaim);
	  	    vu20.setDateClaim(date);
			vu20.setNameProdTo(rsForVU20.getString(10));
			vu20.setPerf(rsForVU20.getString("PERFORMER"));
			vu20.setOkpo_perf(rsForVU20.getString("OKPO_PERFORMER"));
			vu20List.add(vu20);
        }
        countVu = countVu + generateVU20(vu20List);
        
        String selectForVU19 = " select id, vagnum, NAME_PROD_FROM, OKPO_CUSTOMER, APP_NUM, " + 
        		"        		 NUM_OPERATION, CUSTOMER, ID_APP_TREATMENT, DATE_DOC, NAME_PROD_TO, " + 
        		"        		 OKPO_PERFORMER, PERFORMER, case WHEN TYPE_ACT is not null THEN 1 end p" + 
        		"        		 from snt.ppsreport where " + 
        		"        		 TYPE_ACT = 2 and id_act_workability is null and ID_GU45_POD is not null and predid in  " + 
        		"						(select id from snt.pred where headid = :predid or id = :predid) and complete is null "
        		+ " and id in (:listId) " +
        		"        	 union all" + 
        		"           select id, vagnum, NAME_PROD_FROM_1, OKPO_CUSTOMER, APP_NUM_1, " + 
        		"                         NUM_OPERATION_1, CUSTOMER, ID_APP_TREATMENT_1, DATE_DOC_1, NAME_PROD_TO_1, " + 
        		"                         OKPO_PERFORMER, PERFORMER, case WHEN TYPE_ACT_1 is not null THEN 2 end p" + 
        		"                         from snt.ppsreport where " + 
        		"                         TYPE_ACT_1 = 2 and id_act_workability_1 is null and ID_GU45_POD is not null and predid in" +
        		"						(select id from snt.pred where headid = :predid or id = :predid) and complete is null "
        		+ " and id in (:listId) ";
      
        SqlRowSet rsForVU19 = getNpjt().queryForRowSet(selectForVU19, map);
    	List<VU20> vu19List = new ArrayList<VU20>();
        while(rsForVU19.next()) {
           	VU20 vu19 = new VU20();
        	vu19.setPriznak(rsForVU19.getInt("p"));
			vu19.setId(rsForVU19.getLong("id"));
			vu19.setNumClaim(rsForVU19.getString(5));
			vu19.setNumOperation(rsForVU19.getString(6));
			vu19.setCustomer(rsForVU19.getString("CUSTOMER"));
			vu19.setNameProdFrom(rsForVU19.getString(3));
			vu19.setIdClaim(rsForVU19.getInt(8));
			vu19.setOkpo_customer(rsForVU19.getString("OKPO_CUSTOMER"));
		
			vu19.setVagnum(rsForVU19.getString("VAGNUM"));
			Date dateClaim = rsForVU19.getDate(9);
			SimpleDateFormat myDateFormat1 = new SimpleDateFormat("dd.MM.yyyy");
	  	    String date = myDateFormat1.format(dateClaim);
	  	    vu19.setDateClaim(date);
			vu19.setNameProdTo(rsForVU19.getString(10));
			vu19.setPerf(rsForVU19.getString("PERFORMER"));
			vu19.setOkpo_perf(rsForVU19.getString("OKPO_PERFORMER"));
			vu19List.add(vu19);
        }
        countVu = countVu + generateVU19(vu19List);

		} catch(Exception e) {
			log.error(TypeConverter.exceptionToString(e));
//			e.printStackTrace();
		}
		return countVu;
	}
	
    private void setP16FieldInClaim(final Long idReport,final String vagnum, final Long idDoc) throws UnsupportedEncodingException, ServiceException, IOException, TransformerException, InternalException {
    	Map<String, Object> map = new HashMap<String, Object>();
    	map.put("id", idReport);
    	Long idClaim = null;
    	
        SqlRowSet rs= getNpjt().queryForRowSet("select id_app_treatment, id_app_treatment_1, name_prod_from_1, reason, "
        		+ " num_operation_1, name_prod_to_1, id_act_workability, id_act_workability_1, name_prod_from, name_prod_to, num_operation from snt.ppsreport where id = :id", map);
        String name_prod_from_1 = null;
        String name_prod_to_1 = null;
        String num_operation_1 = null;
        String reason = null;
        while(rs.next()) {
        	if(rs.getObject("id_act_workability") != null &&
        			rs.getObject("id_app_treatment_1") != null) {
        		idClaim = rs.getLong("id_app_treatment_1");
        	   	name_prod_from_1 = rs.getString("name_prod_from");
            	name_prod_to_1 = rs.getString("name_prod_to");
            	num_operation_1 = rs.getString("num_operation");
        	} else if(rs.getObject("id_act_workability_1") != null) {
	      		idClaim = rs.getLong("id_app_treatment");
	    	   	name_prod_from_1 = rs.getString("name_prod_from_1");
	        	name_prod_to_1 = rs.getString("name_prod_to_1");
	        	num_operation_1 = rs.getString("num_operation_1");

        	} else  {
	      		idClaim = rs.getLong("id_app_treatment");
	    	   	name_prod_from_1 = "";
	        	name_prod_to_1 = "";
	        	num_operation_1 = "";
        	} 
        	reason = rs.getString("reason");
        }
//        System.out.println(idClaim);
    	map.put("idClaim", idClaim);
		byte[] blob = (byte[])getNpjt().queryForObject("select bldoc from snt.docstore "
				+ "where id = :idClaim", map, byte[].class);
		ETDForm formClaim = ETDForm.createFromArchive(blob);
		final DataBinder binderClaim = formClaim.getBinder();
		
        final String name_prod_from_11 = name_prod_from_1;
        final String name_prod_to_11 = name_prod_to_1;
        final String num_operation_11 = num_operation_1;
        final String reason1 = reason;
		binderClaim.setRootElement("data");
		binderClaim.handleTable("table1", "row", new RowHandler<Object>() {
			public void handleRow(DataBinder kinder, int rowNum, Object obj) throws InternalException
			{   
				if(kinder.getValue("P_9").equals(vagnum)) {
					kinder.setNodeValue("P_16", idDoc.toString());
					kinder.setNodeValue("P_22", reason1);
					kinder.setNodeValue("P_23", name_prod_from_11);
					kinder.setNodeValue("P_24", name_prod_to_11);
					kinder.setNodeValue("P_25", num_operation_11);		
				}
			}
		}, null);
		
        blob = formClaim.encodeToArchiv();
		String docdata = formClaim.transform("data");
		map.put("bldoc", blob);
		map.put("docdata", docdata);
		getNpjt().update("update snt.docstore set bldoc = :bldoc, docdata = :docdata"
				+ " where id = :idClaim", map);
    }

}

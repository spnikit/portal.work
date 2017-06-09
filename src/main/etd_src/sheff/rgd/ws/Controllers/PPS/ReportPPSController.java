package sheff.rgd.ws.Controllers.PPS;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.sql.Blob;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import javax.xml.transform.TransformerException;

import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcTemplate;
import org.springframework.jdbc.support.rowset.ResultSetWrappingSqlRowSet;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.support.TransactionTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;

import com.aisa.htmlgenerator.utils.Table;

import ru.aisa.etdadmin.controllers.AbstractMultipartController;
import ru.aisa.etdportal.controllers.GenerateHTMLController;
import ru.aisa.rgd.etd.dao.ETDSyncServiceFacade;
import ru.aisa.rgd.ws.dao.ServiceFacade;
import ru.aisa.rgd.ws.documents.ETDForm;
import ru.aisa.rgd.ws.exeption.InternalException;
import ru.aisa.rgd.ws.exeption.ServiceException;
import ru.aisa.rgd.ws.utility.DataBinder;
import ru.aisa.rgd.ws.utility.RowHandler;
import ru.aisa.rgd.ws.utility.TypeConverter;
import sheff.rjd.ws.OCO.AfterEdit.FakeSignature;


public class ReportPPSController extends AbstractController { 

	private static Logger log = Logger.getLogger(GenerateHTMLController.class);
	private NamedParameterJdbcTemplate npjt;
	private DataSource ds;
    private SimpleJdbcTemplate sjt;
	private VUGen vugenerator;
	private ServiceFacade facade;
	private TransactionTemplate transT;
	
	public TransactionTemplate getTransT() {
		return transT;
	}
	public void setTransT(TransactionTemplate transT) {
		this.transT = transT;
	}
	public ServiceFacade getFacade() {
		return facade;
	}

	public void setFacade(ServiceFacade facade) {
		this.facade = facade;
	}
	
	public VUGen getVugenerator() {
		return vugenerator;
	}

	public void setVugenerator(VUGen vugenerator) {
		this.vugenerator = vugenerator;
	}

	public SimpleJdbcTemplate getSjt() {
		return sjt;
	}

	public void setSjt(SimpleJdbcTemplate sjt) {
		this.sjt = sjt;
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

	
	public ReportPPSController() throws JSONException {
		super();
	}
	
	private List<String> parseClaim(ETDForm form) throws InternalException {
		DataBinder binder = form.getBinder();
		binder.setRootElement("data");
		final List<String> vagnumList = new ArrayList<String>();
		binder.handleTable("table1", "row", new RowHandler<Object>() {
			public void handleRow(DataBinder kinder, int rowNum, Object obj) throws InternalException
			{   
				vagnumList.add(kinder.getValue("P_9"));
			}
		}, null);
		return vagnumList;
	}
	
	public String generateHTML(String html, Map<String, Object> map) throws IOException, ServiceException, SQLException, TransformerException, InternalException {
		 Document document = Jsoup.parse(html);
		 
		 String selectEditClaims = " select id, bldoc from snt.docstore where typeid = "
				 + "(select id from snt.doctype where rtrim(name) = 'Заявка ППС') "
				 + "and predid in (select id from snt.pred where id = :predid or headid = :predid) "
				 + "and inuseid is not null and signlvl = 0";
		 ResultSetWrappingSqlRowSet srs =  (ResultSetWrappingSqlRowSet)getNpjt().queryForRowSet(selectEditClaims, map);
		 ResultSet resultSet = srs.getResultSet();
		 List<String> vagnumList = new ArrayList<String>();
		 while(resultSet.next()) {
			 Blob blob = resultSet.getBlob("bldoc");
			 ETDForm form = ETDForm.createFromArchive(blob.getBytes(1, (int)blob.length()));
//			 System.out.println(form.transform("data"));
			 List<String> list = parseClaim(form);
			 vagnumList.addAll(list);
		 }
		 

		 String select = "select id, vagnum, reason, name_prod_from, name_prod_to,  name_prod_from_1, name_prod_to_1, type_act, type_act_1,"
				 + " num_operation, num_operation_1, app_num, app_num_1, date_doc, date_doc_1, "
				 + "num_act, num_act_1, date_act, date_act_1, num_gu45_pod, num_gu45_pod_1, "
				 + "gu45_pod_date, gu45_pod_date_1, num_gu45_ub, num_gu45_ub_1, date_ub_gu45, "
				 + "date_ub_gu45_1, num_gu2b, id_app_treatment, id_app_treatment_1, id_act_workability,"
				 + " id_act_workability_1, id_gu45_pod, id_gu45_pod_1, id_gu45_ub, id_gu45_ub_1,"
				 + " note"
				 + " from snt.ppsreport where (predid = :predid or okpo_customer = "
				 + "(select cast(cast(okpo_kod  AS CHAR(100)) as varchar(100)) from snt.pred where id = :predid)) "
				 + " and complete is null "
				 + " order by gu45_pod_date asc, date_doc asc, date_act asc, vagnum asc";

		 String predName = getNpjt().queryForObject("select rtrim(name) from snt.pred where id = :predid", map, String.class);
		 Element perfromer = document.getElementById("performer");
		 perfromer.text(predName);
		 Element cloneParentRow1 = null;
		 Element cloneParentRow2 = null;
		 Element cloneParentRow3 = null;
		 Element parentRow1 = null;
		 Element parentRow2 = null;
		 Element parentRow3 = null;
		 for(int i = 0; i < document.getElementsByAttribute("name").size(); i++) {
			 Element elemWithAttrName  = document.getElementsByAttribute("name").get(i);	
			 String valueAttr = elemWithAttrName.attr("name");
			 if(valueAttr.equals("p1")) {
				 cloneParentRow1 = elemWithAttrName.parent().parent().clone();
				 parentRow1 = elemWithAttrName.parent().parent();
			 }
			 if(valueAttr.equals("p1_1")) {
				 cloneParentRow2 = elemWithAttrName.parent().parent().clone();
				 parentRow2 = elemWithAttrName.parent().parent();
			 }
			 if(valueAttr.equals("nomer")) {
				 cloneParentRow3 = elemWithAttrName.parent().parent().clone();
				 parentRow3 = elemWithAttrName.parent().parent();
			 }
		 }
		 
		    SqlRowSet rs = getNpjt().queryForRowSet(select, map);
			int num_record = 0;
			while(rs.next()) {
				if(num_record == 0) {
					num_record++;
					
					 for(int i = 0; i < parentRow1.getElementsByAttribute("name").size(); i++) {
						 Element elemWithAttrName  = parentRow1.getElementsByAttribute("name").get(i);	
						 String valueAttr = elemWithAttrName.attr("name");

						/* if(valueAttr.equals("id")) {
							 elemWithAttrName.text(rs.getString("id"));		 
						 }*/
						 if(valueAttr.equals("id")) {
							 if(rs.getString("id") != null) {
								 elemWithAttrName.getElementsByTag("textarea")
								 	.get(0).text(rs.getString("id"));	
							 }
						 }
						 if(valueAttr.equals("p1")) {
							 elemWithAttrName.text(rs.getString("vagnum"));
						 }
						 if(valueAttr.equals("p2")) {
							 if(rs.getString("name_prod_from") != null) {
								 elemWithAttrName.text(rs.getString("name_prod_from"));
							 }
						 }
						 if(valueAttr.equals("p3")) {
							 if(rs.getString("name_prod_to") != null) {
								 elemWithAttrName.text(rs.getString("name_prod_to"));
							 }
						 }
						 if(valueAttr.equals("p4")) {
							 if(rs.getString("num_operation") != null) {
								 elemWithAttrName.text(rs.getString("num_operation"));
							 }
						 }
						 if(valueAttr.equals("p5")) {
							 if(rs.getString("app_num") != null) {
								 elemWithAttrName.text(rs.getString("app_num"));
							 }
						 }
						 if(valueAttr.equals("p6")) {
							 if(rs.getString("date_doc") != null) {
								 String date_doc = convertDateToString(rs.getDate("date_doc"));
								 elemWithAttrName.text(date_doc);
							 }
						 }
						 if(valueAttr.equals("p7")) {
							 if(rs.getString("type_act") != null) {
								 if(rs.getString("type_act").equals("2")) {
									elemWithAttrName.getElementsByAttributeValue("value", "VU-19").get(0).attr("selected", "selected");
								 }
								 if(rs.getString("type_act").equals("3")) {
										elemWithAttrName.getElementsByAttributeValue("value", "bez-obr").get(0).attr("selected", "selected");
								 }
							 }
							
						 }
						 if(valueAttr.equals("p8")) {
							 if(rs.getString("num_act") != null) {
								 elemWithAttrName.text(rs.getString("num_act"));
							 }
						 }
						 if(valueAttr.equals("p9")) {
							 if(rs.getString("date_act") != null) {
								 String date_act = convertDateToString(rs.getDate("date_act"));
								 elemWithAttrName.text(date_act);
							 }
						 }
						 if(valueAttr.equals("p10")) {
							 if(rs.getString("num_gu45_pod") != null) {
								 elemWithAttrName.text(rs.getString("num_gu45_pod"));
							 }
						 }
						 if(valueAttr.equals("p11")) {
							 if(rs.getString("gu45_pod_date") != null) {
								 Date date = rs.getDate("gu45_pod_date");
								 SimpleDateFormat smplDateFormat = new SimpleDateFormat("dd.MM");
								 String gu45_pod_date = smplDateFormat.format(date);
								 elemWithAttrName.text(gu45_pod_date);
							 }
						 }
						 if(valueAttr.equals("p12")) {
							 if(rs.getString("num_gu45_ub") != null) {
								 elemWithAttrName.text(rs.getString("num_gu45_ub"));
							 }
						 }
						 if(valueAttr.equals("p13")) {
							 if(rs.getString("date_ub_gu45") != null) {
								 String date_ub_gu45 = rs.getString("date_ub_gu45");
								 elemWithAttrName.text(date_ub_gu45);
							 }
						 }
						 if(valueAttr.equals("p14")) {
							 if(rs.getString("num_gu2b") != null) {
								 elemWithAttrName.text(rs.getString("num_gu2b"));
							 }
						 } 
						 if(valueAttr.equals("p15")) {
							 elemWithAttrName.text(rs.getString("id_app_treatment"));
						 }
						 if(valueAttr.equals("p16")) {
							 if(rs.getString("id_act_workability") != null) {
								 elemWithAttrName.text(rs.getString("id_act_workability"));
							 }
						 }
						 if(valueAttr.equals("p17")) {
							 if(rs.getString("id_gu45_pod") != null) {
								 elemWithAttrName.text(rs.getString("id_gu45_pod"));
							 }
						 }
						 if(valueAttr.equals("p19")) {
							 if(rs.getString("note") != null) {
								 elemWithAttrName.getElementsByTag("textarea")
								 	.get(0).text(rs.getString("note"));	
							 }
						 }
					
						 if(valueAttr.equals("lock")) {
							 String vagnum =  rs.getString("vagnum");
							 if(vagnumList.contains(vagnum)) {
								 elemWithAttrName.text("1");
							 } 
						 }
					 }
					 
					 for(int i = 0; i < parentRow2.getElementsByAttribute("name").size(); i++) {
						 Element elemWithAttrName  = parentRow2.getElementsByAttribute("name").get(i);	
						 String valueAttr = elemWithAttrName.attr("name");

						 if(valueAttr.equals("id1")) {
							 elemWithAttrName.text(rs.getString("id"));
						 }
						 if(valueAttr.equals("p1_1")) {
							 if(rs.getString("reason") != null) {
								 elemWithAttrName.getElementsByTag("textarea")
								 	.get(0).text(rs.getString("reason"));	
							 }
						 }
						 if(valueAttr.equals("p2_1")) {
							 if(rs.getString("name_prod_from_1") != null) {
								 elemWithAttrName.text(rs.getString("name_prod_from_1"));
							 } 
						 }

						 if(valueAttr.equals("p3_1")) {
							 if(rs.getString("name_prod_to_1") != null) {
								 elemWithAttrName.text(rs.getString("name_prod_to_1"));
							 }
						 }
						 if(valueAttr.equals("p4_1")) {
							 if(rs.getString("num_operation_1") != null) {
								 elemWithAttrName.text(rs.getString("num_operation_1"));
							 }
						 }
						 if(valueAttr.equals("p5_1")) {
							 if(rs.getString("app_num_1") != null) {
								 elemWithAttrName.text(rs.getString("app_num_1"));
							 }
						 }
						 if(valueAttr.equals("p6_1")) {
							 if(rs.getString("date_doc_1") != null) {
								 String date_doc = convertDateToString(rs.getDate("date_doc_1"));
								 elemWithAttrName.text(date_doc);
							 }
						 }
						 if(valueAttr.equals("p7_1")) {
							 
							 if(rs.getString("type_act_1") != null) {
								 if(rs.getString("type_act_1").equals("2")) {
									elemWithAttrName.getElementsByAttributeValue("value", "VU-19").get(0).attr("selected", "selected");
								 }
								 if(rs.getString("type_act_1").equals("3")) {
										elemWithAttrName.getElementsByAttributeValue("value", "bez-obr").get(0).attr("selected", "selected");
								 }
							 }
						 }
						 if(valueAttr.equals("p8_1")) {
							 if(rs.getString("num_act_1") != null) {
								 elemWithAttrName.text(rs.getString("num_act_1"));
							 }
						 }
						 if(valueAttr.equals("p9_1")) {
							 if(rs.getString("date_act_1") != null) {
								 String date_act = convertDateToString(rs.getDate("date_act_1"));
								 elemWithAttrName.text(date_act);
							 }
						 }
						 if(valueAttr.equals("p10_1")) {
							 if(rs.getString("num_gu45_pod_1") != null) {
								 elemWithAttrName.text(rs.getString("num_gu45_pod_1"));
							 }
						 }
						 if(valueAttr.equals("p11_1")) {
							 if(rs.getString("gu45_pod_date_1") != null) {
								 Date date = rs.getDate("gu45_pod_date_1");
								 SimpleDateFormat smplDateFormat = new SimpleDateFormat("dd.MM");
								 String gu45_pod_date = smplDateFormat.format(date);
								 elemWithAttrName.text(gu45_pod_date);
							 }
						 }
						 if(valueAttr.equals("p12_1")) {
							 if(rs.getString("num_gu45_ub_1") != null) {
								 elemWithAttrName.text(rs.getString("num_gu45_ub_1"));
							 }
						 }
						 if(valueAttr.equals("p13_1")) {
							 if(rs.getString("date_ub_gu45_1") != null) {
								 String date_ub_gu45 = rs.getString("date_ub_gu45_1");
								 elemWithAttrName.text(date_ub_gu45);
							 }
						 }
						 if(valueAttr.equals("p15_1")) {
							 if(rs.getString("id_app_treatment_1") != null) {
								 elemWithAttrName.text(rs.getString("id_app_treatment_1"));
							 }
						 }
						 if(valueAttr.equals("p16_1")) {
							 if(rs.getString("id_act_workability_1") != null) {
								 elemWithAttrName.text(rs.getString("id_act_workability_1"));
							 }
						 }
						 if(valueAttr.equals("p17_1")) {
							 if(rs.getString("id_gu45_pod") != null) {
								 elemWithAttrName.text(rs.getString("id_gu45_pod"));
							 }
						 }
						 if(valueAttr.equals("lock1")) {
							 String vagnum =  rs.getString("vagnum");
							 if(vagnumList.contains(vagnum)) {
								 elemWithAttrName.text("1");
							 } 
						 }
					 }
					 
					 for(int i = 0; i < parentRow3.getElementsByAttribute("name").size(); i++) {
						 Element elemWithAttrName  = parentRow3.getElementsByAttribute("name").get(i);	
						 String valueAttr = elemWithAttrName.attr("name");
						 if(valueAttr.equals("nomer")) {
							 elemWithAttrName.text(String.valueOf(num_record));
						 }
					 }
				} else {
					 num_record++;
					
					 Element cloneParentRow = cloneParentRow3.clone();
					
					 for(int i = 0; i < cloneParentRow.getElementsByAttribute("name").size(); i++) {
						 Element elemWithAttrName  = cloneParentRow.getElementsByAttribute("name").get(i);	
						 String valueAttr = elemWithAttrName.attr("name");
						 if(valueAttr.equals("nomer")) {
							 elemWithAttrName.text(String.valueOf(num_record));
						 }
					 }
					Element table = parentRow1.parent();
				    table.appendChild(cloneParentRow);

					cloneParentRow = cloneParentRow1.clone();
					Set<String> setClassNames = new HashSet<String>();
					setClassNames.add("row" + num_record);
					cloneParentRow.classNames(setClassNames);
					 for(int i = 0; i < cloneParentRow.getElementsByAttribute("name").size(); i++) {
						 Element elemWithAttrName  = cloneParentRow.getElementsByAttribute("name").get(i);	
						 String valueAttr = elemWithAttrName.attr("name");
						 
						/* if(valueAttr.equals("id")) {
							 elemWithAttrName.text(rs.getString("id"));
						 }*/
						 if(valueAttr.equals("id")) {
							 if(rs.getString("id") != null) {
								 elemWithAttrName.getElementsByTag("textarea")
								 	.get(0).text(rs.getString("id"));	
							 }
						 }
						 if(valueAttr.equals("p1")) {
							 if(rs.getString("vagnum") != null) {
								 elemWithAttrName.text(rs.getString("vagnum"));
							 }
						 }
						 
						 if(valueAttr.equals("p2")) {
							 if(rs.getString("name_prod_from") != null) {
								 elemWithAttrName.text(rs.getString("name_prod_from"));
							 }
						 }
						 if(valueAttr.equals("p3")) {
							 if(rs.getString("name_prod_to") != null) {
								 elemWithAttrName.text(rs.getString("name_prod_to"));
							 }
						 }
						 if(valueAttr.equals("p4")) {
							 if(rs.getString("num_operation") != null) {
								 elemWithAttrName.text(rs.getString("num_operation"));
							 }
						 }
						 if(valueAttr.equals("p5")) {
							 if(rs.getString("app_num") != null) {
								 elemWithAttrName.text(rs.getString("app_num"));
							 }
						 }
						 if(valueAttr.equals("p6")) {
							 if(rs.getString("date_doc") != null) {
								 String date_doc = convertDateToString(rs.getDate("date_doc"));
								 elemWithAttrName.text(date_doc);
							 }
						 }
						 if(valueAttr.equals("p7")) {
							 if(rs.getString("type_act") != null) {
								 if(rs.getString("type_act").equals("2")) {
									elemWithAttrName.getElementsByAttributeValue("value", "VU-19").get(0).attr("selected", "selected");
								 }
								 if(rs.getString("type_act").equals("3")) {
										elemWithAttrName.getElementsByAttributeValue("value", "bez-obr").get(0).attr("selected", "selected");
								 }
							 }
							
						 }
						 if(valueAttr.equals("p8")) {
							 if(rs.getString("num_act") != null) {
								 elemWithAttrName.text(rs.getString("num_act"));
							 }
						 }
						 if(valueAttr.equals("p9")) {
							 if(rs.getString("date_act") != null) {
								 String date_act = convertDateToString(rs.getDate("date_act"));
								 elemWithAttrName.text(date_act);
							 }
						 }
						 if(valueAttr.equals("p10")) {
							 if(rs.getString("num_gu45_pod") != null) {
								 elemWithAttrName.text(rs.getString("num_gu45_pod"));
							 }
						 }
						 if(valueAttr.equals("p11")) {
							 if(rs.getString("gu45_pod_date") != null) {
								 Date date = rs.getDate("gu45_pod_date");
								 SimpleDateFormat smplDateFormat = new SimpleDateFormat("dd.MM");
								 String gu45_pod_date = smplDateFormat.format(date);
								 elemWithAttrName.text(gu45_pod_date);
							 }
						 }
						 if(valueAttr.equals("p12")) {
							 if(rs.getString("num_gu45_ub") != null) {
								 elemWithAttrName.text(rs.getString("num_gu45_ub"));
							 }
						 }
						 if(valueAttr.equals("p13")) {
							 if(rs.getString("date_ub_gu45") != null) {
								 String date_ub_gu45 = rs.getString("date_ub_gu45");
								 elemWithAttrName.text(date_ub_gu45);
							 }
						 }
						 if(valueAttr.equals("p14")) {
							 if(rs.getString("num_gu2b") != null) {
								 elemWithAttrName.text(rs.getString("num_gu2b"));
							 }
						 }
						 if(valueAttr.equals("p15")) {
							 if(rs.getString("id_app_treatment") != null) {
								 elemWithAttrName.text(rs.getString("id_app_treatment"));
							 }
						 }
						 if(valueAttr.equals("p16")) {
							 if(rs.getString("id_act_workability") != null) {
								 elemWithAttrName.text(rs.getString("id_act_workability"));
							 }
						 }
						 if(valueAttr.equals("p17")) {
							 if(rs.getString("id_gu45_pod") != null) {
								 elemWithAttrName.text(rs.getString("id_gu45_pod"));
							 }
						 }
						 if(valueAttr.equals("p19")) {
							 if(rs.getString("note") != null) {
								 elemWithAttrName.getElementsByTag("textarea")
								 	.get(0).text(rs.getString("note"));	
							 }
						 }
						 if(valueAttr.equals("lock")) {
							 String vagnum =  rs.getString("vagnum");
							 if(vagnumList.contains(vagnum)) {
								 elemWithAttrName.text("1");
							 } 
						 }
					 }
					 table.appendChild(cloneParentRow);
	
					 cloneParentRow = cloneParentRow2.clone();
					 cloneParentRow.classNames(setClassNames);
					 for(int i = 0; i < cloneParentRow.getElementsByAttribute("name").size(); i++) {
						 Element elemWithAttrName  = cloneParentRow.getElementsByAttribute("name").get(i);	
						 String valueAttr = elemWithAttrName.attr("name");
						 
						 if(valueAttr.equals("id1")) {
							 elemWithAttrName.text(rs.getString("id"));
						 }
						 if(valueAttr.equals("p1_1")) {
							 if(rs.getString("reason") != null) {
								 elemWithAttrName.getElementsByTag("textarea")
								 	.get(0).text(rs.getString("reason"));	
							 }
						 }
						 if(valueAttr.equals("p2_1")) {
							 if(rs.getString("name_prod_from_1") != null) {
								 elemWithAttrName.text(rs.getString("name_prod_from_1"));
							 } 
						 }

						 if(valueAttr.equals("p3_1")) {
							 if(rs.getString("name_prod_to_1") != null) {
								 elemWithAttrName.text(rs.getString("name_prod_to_1"));
							 }
						 }
						 if(valueAttr.equals("p4_1")) {
							 if(rs.getString("num_operation_1") != null) {
								 elemWithAttrName.text(rs.getString("num_operation_1"));
							 }
						 }
						 if(valueAttr.equals("p5_1")) {
							 if(rs.getString("app_num_1") != null) {
								 elemWithAttrName.text(rs.getString("app_num_1"));
							 }
						 }
						 if(valueAttr.equals("p6_1")) {
							 if(rs.getString("date_doc_1") != null) {
								 String date_doc = convertDateToString(rs.getDate("date_doc_1"));
								 elemWithAttrName.text(date_doc);
							 }
						 }
						 if(valueAttr.equals("p7_1")) {
							 if(rs.getString("type_act_1") != null) {
								 if(rs.getString("type_act_1").equals("2")) {
									elemWithAttrName.getElementsByAttributeValue("value", "VU-19").get(0).attr("selected", "selected");
								 }
								 if(rs.getString("type_act_1").equals("3")) {
										elemWithAttrName.getElementsByAttributeValue("value", "bez-obr").get(0).attr("selected", "selected");
								 }
							 }
						 }
						 if(valueAttr.equals("p8_1")) {
							 if(rs.getString("num_act_1") != null) {
								 elemWithAttrName.text(rs.getString("num_act_1"));
							 }
						 }
						 if(valueAttr.equals("p9_1")) {
							 if(rs.getString("date_act_1") != null) {
								 String date_act = convertDateToString(rs.getDate("date_act_1"));
								 elemWithAttrName.text(date_act);
							 }
						 }
						 if(valueAttr.equals("p10_1")) {
							 if(rs.getString("num_gu45_pod_1") != null) {
								 elemWithAttrName.text(rs.getString("num_gu45_pod_1"));
							 }
						 }
						 if(valueAttr.equals("p11_1")) {
							 if(rs.getString("gu45_pod_date_1") != null) {
								 Date date = rs.getDate("gu45_pod_date_1");
								 SimpleDateFormat smplDateFormat = new SimpleDateFormat("dd.MM");
								 String gu45_pod_date = smplDateFormat.format(date);
								 elemWithAttrName.text(gu45_pod_date);
							 }
						 }
						 if(valueAttr.equals("p12_1")) {
							 if(rs.getString("num_gu45_ub_1") != null) {
								 elemWithAttrName.text(rs.getString("num_gu45_ub_1"));
							 }
						 }
						 if(valueAttr.equals("p13_1")) {
							 if(rs.getString("date_ub_gu45_1") != null) {
								 String date_ub_gu45 = rs.getString("date_ub_gu45_1");
								 elemWithAttrName.text(date_ub_gu45);
							 }
						 }
						 
						 if(valueAttr.equals("p15_1")) {
							 if(rs.getString("id_app_treatment_1") != null) {
								 elemWithAttrName.text(rs.getString("id_app_treatment_1"));
							 }
						 }
						 if(valueAttr.equals("p16_1")) {
							 if(rs.getString("id_act_workability_1") != null) {
								 elemWithAttrName.text(rs.getString("id_act_workability_1"));
							 }
						 }
						 if(valueAttr.equals("p17_1")) {
							 if(rs.getString("id_gu45_pod") != null) {
								 elemWithAttrName.text(rs.getString("id_gu45_pod"));
							 }
						 }
						 if(valueAttr.equals("lock1")) {
							 String vagnum =  rs.getString("vagnum");
							 if(vagnumList.contains(vagnum)) {
								 elemWithAttrName.text("1");
							 } 
						 }
					 }
					 table.appendChild(cloneParentRow);	
				}

			}
			

		return document.toString();
	}
	
	private String convertDateToString(Date date) {
		SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
        return sdf.format(date);
	}

	@Override
	protected ModelAndView handleRequestInternal(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		try {

			Integer predid = Integer.parseInt(request.getParameter("predid"));
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("predid", predid);
			if(request.getParameter("action").equals("checkOpenReportForClaim")) {
				int count = getNpjt().queryForInt("select count(0) from snt.docstore where "
	        			+ "typeid = (select id from snt.doctype where name = 'Отчет обработки вагонов-цистерн') "
	        			+ " and predid = :predid and inuseid is not null", map);
		   		JSONObject json = new JSONObject();
		   		json.append("countOpenReport", count);
		   		response.getWriter().write(json.toString());
			}

	        if(request.getParameter("action").equals("checkOnEdit")) { 
	        	int readonly = 0;
	    		map.put("CERTSERIAL", new BigInteger(request.getParameter("cert"),16).toString());
	        	SqlRowSet rs1 = getNpjt().queryForRowSet("select id, rtrim(cast(rtrim(fname) || ' ' || rtrim(mname) || ' ' || rtrim(lname) as char(240))) username "
	        			+ " from snt.personall where CERTSERIAL = :CERTSERIAL", map);
	        	int userid = 0;
	        	String username = null;
	        	while(rs1.next()) {
	        		userid = rs1.getInt("id");
	        		username = new String(rs1.getString("username").getBytes(),"UTF-8");
	        	}
	        	
	        	map.put("inuseid", userid);
	        	Integer countReport = getNpjt().queryForInt("select count(0) from snt.docstore where "
	        			+ "typeid = (select id from snt.doctype where name = 'Отчет обработки вагонов-цистерн') "
	        			+ " and predid = :predid", map);
	        	if(countReport > 0) {
	        		SqlRowSet rs = getNpjt().queryForRowSet("select id, inuseid, droptime from snt.docstore where " + 
	        				" typeid = (select id from snt.doctype " + 
	        				 " where name = 'Отчет обработки вагонов-цистерн') " + 
	        				"  and predid = :predid", map);
	        		Long id = null;
	        		Long useId = null;
	        		Date date = null;
	        		while(rs.next()) {
	        			id = rs.getLong("id");
	        			useId = rs.getLong("inuseid");
	        			date = rs.getTimestamp("droptime");
	        		}
	        		if(useId > 0) {
	        			if(date != null) {
	        				if(new Date().getTime() - date.getTime() < 1800000) {
	    	        			readonly = 1;
	    	        			map.put("userId", useId);  
	    	        			username = getNpjt().queryForObject("select rtrim(cast(rtrim(fname) || ' ' || rtrim(mname) || ' ' || rtrim(lname) as char(240))) username " + 
	    	        					" from snt.personall where id = :userId", map, String.class);
	        				} else {
	    	           			map.put("id", id);
	    	            		map.put("droptime", new Date());
	    	            		getNpjt().update("update snt.docstore set inuseid = :inuseid, droptime = :droptime where id = :id", map);
	        				}
	        			}
	        		} else {
	            		map.put("id", id);
	            		map.put("droptime", new Date());
	            		getNpjt().update("update snt.docstore set inuseid = :inuseid, droptime = :droptime where id = :id", map);
	        		}

	        	} else if(countReport == 0) {
	        		ru.aisa.rgd.ws.domain.Document document = new ru.aisa.rgd.ws.domain.Document();
	        		Long id = facade.getDocumentDao().getNextId();
	        		byte[] bl = {1,2,3};
	    			document.setBlDoc(bl);
	    			document.setDocData("<data></data>");
	    			document.setPredId(predid);
	    			document.setSignLvl(0);
	    			Date date = new Date();
	    			document.setDropTime(date.getTime());
	    			document.setType("Отчет обработки вагонов-цистерн");
	    			document.setId(id);
	    			facade.insertDocumentWithDocid(document);
	    			map.put("id", id);
	        		getNpjt().update("update snt.docstore set inuseid = :inuseid where id = :id", map);
	        	}
//	        	System.out.println(username);
		   		JSONObject json = new JSONObject();
		   		json.append("isEdit", readonly);
		   		json.append("username", username);
		   		response.setCharacterEncoding("UTF-8");
		   		response.getWriter().write(json.toString());
	     }
	        
        if(request.getParameter("action").equals("0")) {

	   		byte[] bl = getNpjt().queryForObject("select html_template from snt.doctype where "
	   				+ " rtrim(name) = 'Отчет обработки вагонов-цистерн'", map, byte[].class);
	   		String html = generateHTML(new String(bl), map);

	   		response.setCharacterEncoding("UTF-8");
	   		response.setContentType("text/html; charset=UTF-8");
	   		response.getWriter().print(html);

        } else if(request.getParameter("action").equals("1")) {
        	String[] arrayParams = request.getParameter("params1").split(";");
        	final List<Report> listReport = new ArrayList<Report>();
        	List<Long> idList = new ArrayList<Long>();
        	for(String s: arrayParams) {
        		Report report = new Report();
        		String[] arr = s.split(",");
        		String id = arr[2];
        		String type_act = arr[0];
        		String type_act_1 = arr[3];
        		
        		if(type_act.equals("VU-20")) {
        			report.setSelectCodeName1("1");
        			type_act  = "1";
        		}
        		if(type_act.equals("VU-19")) {
        			report.setSelectCodeName1("2");
        			type_act  = "2";
        		}
        		if(type_act.equals("bez-obr")) {
        			report.setSelectCodeName1("3");
        			type_act  = "3";
        		}
        		

        		if(type_act_1.equals("VU-20")) {
        			report.setSelectCodeName2("1");
        			type_act_1  = "1";
        		}
        		if(type_act_1.equals("VU-19")) {
        			report.setSelectCodeName2("2");
        			type_act_1  = "2";
        		}
        		if(type_act_1.equals("bez-obr")) {
        			report.setSelectCodeName2("3");
        			type_act_1  = "3";
        		}
        		
        		report.setId(id);
        		report.setReason(arr[4]);
				report.setNote(arr[1]);
        		listReport.add(report);
        		idList.add(Long.parseLong(id));
        	}
        
        	final String update = "update snt.ppsreport set type_act = :type_act, "
        			+ "type_act_1 = :type_act_1, note = :note, reason = :reason "
        			+ " where id = :id";
        	
//        	final String update = "update snt.ppsreport set type_act = :type_act "
//        			+ " where id = :id";

        	//sjt.batchUpdate(update, listPair);
        	for(Report pair: listReport) {
        		map.put("id", Long.parseLong(pair.getId()));
        		map.put("type_act", pair.getSelectCodeName1());
        		map.put("type_act_1", pair.getSelectCodeName2());
         		map.put("note", pair.getNote());
         		map.put("reason", pair.getReason());
        		getNpjt().update(update, map);
        	}
        	int numberVu = vugenerator.generateVU(predid, idList);

    		JSONObject json = new JSONObject();
	   		json.append("message", "Создан(но) " + numberVu + " документ(ов) ВУ.");
	   		response.setCharacterEncoding("UTF-8");
	   		response.getWriter().write(json.toString());
     
        } else if(request.getParameter("action").equals("2")) {
        	String[] arrayParams = request.getParameter("params1").split(";");
        	final List<Report> listReport = new ArrayList<Report>();
        	List<Long> idList = new ArrayList<Long>();
        	for(String s: arrayParams) {
        		Report report = new Report();
        		String[] arr = s.split(",");
        		String id = arr[2];
        		String type_act = arr[0];
        		String type_act_1 = arr[3];
        		
        		if(type_act.equals("VU-20")) {
        			report.setSelectCodeName1("1");
        			type_act  = "1";
        		}
        		if(type_act.equals("VU-19")) {
        			report.setSelectCodeName1("2");
        			type_act  = "2";
        		}
        		if(type_act.equals("bez-obr")) {
        			report.setSelectCodeName1("3");
        			type_act  = "3";
        		}
        		

        		if(type_act_1.equals("VU-20")) {
        			report.setSelectCodeName2("1");
        			type_act_1  = "1";
        		}
        		if(type_act_1.equals("VU-19")) {
        			report.setSelectCodeName2("2");
        			type_act_1  = "2";
        		}
        		if(type_act_1.equals("bez-obr")) {
        			report.setSelectCodeName2("3");
        			type_act_1  = "3";
        		}
        		
        		report.setId(id);
        		report.setReason(arr[4]);
				report.setNote(arr[1]);
        		listReport.add(report);
        		
        		idList.add(Long.parseLong(id));
        	}
        	final String update = "update snt.ppsreport set type_act = :type_act, "
        			+ "type_act_1 = :type_act_1, note = :note, reason = :reason "
        			+ " where id = :id";
        	
        	for(Report pair: listReport) {
        		map.put("id", Long.parseLong(pair.getId()));
        		map.put("type_act", pair.getSelectCodeName1());
        		map.put("type_act_1", pair.getSelectCodeName2());
         		map.put("note", pair.getNote());
         		map.put("reason", pair.getReason());
        		getNpjt().update(update, map);
        	}
        	boolean isCreate = vugenerator.generateGU2b(predid);
        	response.setCharacterEncoding("UTF-8");
        	if(isCreate) {
        		JSONObject json = new JSONObject();
		   		json.append("message", "Документ ГУ-2б создан.");
		   		response.getWriter().write(json.toString());
        	} else {
        		JSONObject json = new JSONObject();
		   		json.append("message", "Документ ГУ-2б не создан.");
		   		response.getWriter().write(json.toString());
        	}
        	
        } else if(request.getParameter("action").equals("close")) {
        	map.put("CERTSERIAL", new BigInteger(request.getParameter("cert"),16).toString());
        	Integer idUser1 = getNpjt().queryForInt("select id from snt.personall "
        			+ " where CERTSERIAL = :CERTSERIAL", map);
        	Integer idUser2 = getNpjt().queryForInt("select inuseid from snt.docstore where "
        			+ "typeid = (select id from snt.doctype where name = 'Отчет обработки вагонов-цистерн') "
        			+ " and predid = :predid", map);
        	if(idUser1.equals(idUser2)) {
	        	getNpjt().update("update snt.docstore set inuseid = null, droptime = null "
	        			+ " where typeid = (select id from snt.doctype " + 
	        			" where name = 'Отчет обработки вагонов-цистерн') " + 
	        			" and predid = :predid", map);
        	}
        }
		
		} catch(Exception e) {
			log.error(TypeConverter.exceptionToString(e));
//			e.printStackTrace();
		}
		return null;
	}
    private class Report {
    	private String id;
        private String selectCodeName1;
        private String selectCodeName2;
        private String note;
        private String reason;
        
		public String getId() {
			return id;
		}
		public void setId(String id) {
			this.id = id;
		}
		public String getSelectCodeName1() {
			return selectCodeName1;
		}
		public void setSelectCodeName1(String selectCodeName1) {
			this.selectCodeName1 = selectCodeName1;
		}
		public String getSelectCodeName2() {
			return selectCodeName2;
		}
		public void setSelectCodeName2(String selectCodeName2) {
			this.selectCodeName2 = selectCodeName2;
		}
		public String getNote() {
			return note;
		}
		public void setNote(String note) {
			this.note = note;
		}
		public String getReason() {
			return reason;
		}
		public void setReason(String reason) {
			this.reason = reason;
		}
    }
    
   
   

}

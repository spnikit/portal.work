package sheff.rgd.ws.Controllers.PPS;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.sql.Types;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;
import javax.xml.transform.TransformerException;

import org.apache.log4j.Logger;
import org.springframework.jdbc.core.SqlOutParameter;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.w3c.dom.Element;
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

public class VU20aController extends AbstractAction{

    protected final Logger  log    = Logger.getLogger(getClass());
       private NamedParameterJdbcTemplate npjt;
       private String parentform;
       private DataSource ds;
       private ServiceFacade facade;
       private ETDSyncServiceFacade etdsyncfacade;
       private FakeSignature fakesignature;
       private VUGen vugenerator;
       
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
       
       public VUGen getVugenerator() {
              return vugenerator;
       }

       public void setVugenerator(VUGen vugenerator) {
              this.vugenerator = vugenerator;
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
                            String[] vagnumArray = binder.getValuesAsArray("vagNum");
                            Date dateSign = getNpjt().queryForObject(selectDateSign, map, Date.class);
                            for(String vagnum: vagnumArray) {
                                   map.put("pred_id", predid);
                                   map.put("vagnum", vagnum);
                                   
                                   SqlRowSet rs = getNpjt().queryForRowSet("select code_station, id, "
                                                 + " case WHEN id_act_workability is not null THEN 1 end p1, "
                                                 + " case WHEN id_act_workability_1 is not null THEN 2 end p2 "
                                                 + " from snt.ppsreport "
                                                 + "where predid = :pred_id and vagnum = :vagnum and complete is null", map);
                                   String st_code = null;
                                   Long idPPS = null;
                                   int priznak = 0;
                                   while(rs.next()) {
                                          idPPS = rs.getLong("id");
                                          if(rs.getInt("p1") != 0) {
                                                 priznak = 1;
                                          }
                                          if(rs.getInt("p2") != 0) {
                                                 priznak = 2;
                                          }
                                   }
                                   
                                   map.put("st_kod", st_code);
                                   binder.setNodeValue("vu_number", no);
                                   
                                   map.put("id_pps", idPPS);
                                   map.put("date_act", dateSign);
                                   map.put("num_act", no);
                                   
                                   
                                   
                                   SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
                                   binder.setNodeValue("signDate", sdf.format(dateSign));
                                   map.put("no", no);
                                   map.put("reqdate", dateSign);
                            }
                            map.put("bldoc", form.encodeToArchiv());
                            map.put("docdata", form.transform("data"));
                            map.put("opisanie", getContent(binder));


                            getNpjt().update("update snt.docstore set bldoc = :bldoc,"
                                          + " docdata = :docdata, no = :no, reqdate = :reqdate where id = :id", map);
                            
                     } else if(signNumber == 2) {
                            final Map<String, Object> map = new HashMap<String, Object>();
                            map.put("id", id);
                            SqlRowSet rs1 = getNpjt().queryForRowSet("select id_app_treatment, id_app_treatment_1, "
                                          + " id_act_workability, id_act_workability_1, "
                                          + " id, vagnum, type_act, type_act_1, "
                                          + " case WHEN id_act_workability is not null THEN 1 end p1,"
                                          + " case WHEN id_act_workability_1 is not null THEN 2 end p2 "
                                          + "from snt.ppsreport where id_act_workability = :id or"
                                          + " id_act_workability_1 = :id", map);
                            Long idClaim = null;
                            ETDForm form = ETDForm.createFromArchive(docdata.getBytes("UTF-8"));
                            final DataBinder binder = form.getBinder();
                            int priznak = 0;
                            while(rs1.next()) {
                                   
                                   if(rs1.getInt("p1") != 0) {
                                          priznak = 1;
                                   }
                                   if(rs1.getInt("p2") != 0) {
                                          priznak = 2;
                                   }
                                   
                                   if(rs1.getObject("id_act_workability") != null) {
                                          idClaim = rs1.getLong("id_app_treatment");
                                   } else if(rs1.getObject("id_act_workability_1") != null) {
                                          idClaim = rs1.getLong("id_app_treatment_1");
                                          updateClaim(rs1.getLong("id_app_treatment"), binder, map);
                                   }
//                                   System.out.println("id_claim " + idClaim ); 
                                   updateClaim(idClaim, binder,  map);
                                   
                                   /*map.put("id", id);
                                   SqlRowSet rs = getNpjt().queryForRowSet("select id, vagnum, type_act, type_act_1 "
                                                 + "from snt.ppsreport where id_act_workability = :id "
                                                 + "or id_act_workability_1 = :id", map);*/
                                   SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
                                   final String nProductName = binder.getValue("nProductName");
                                   final String vuNumber =  binder.getValue("vu_number");

//                                 while(rs.next()) {
                                          String signDate = binder.getValue("signDate");
                                          final Date date = sdf.parse(signDate);
                                          final String vagnum = rs1.getString("vagnum");
                                          final long idRecord = rs1.getLong("id");
                                          if(rs1.getInt("type_act") != 0 && rs1.getInt("type_act") != 3) {
                                                 binder.handleTable("table1", "row", new RowHandler<Object>() {
                                                        public void handleRow(DataBinder kinder, int rowNum, Object obj) throws InternalException
                                                        {   
                                                               if(kinder.getValue("vagNum").equals(vagnum)) {
                                                                      map.put("docid", idRecord);
                                                                      map.put("name_prod_from", binder.getValue("sProductName"));
                                                                      map.put("name_prod_to", nProductName);
                                                                      map.put("num_operation", binder.getValue("operNum"));
                                                                      map.put("num_act", vuNumber);
                                                                      map.put("date_act", date);
                                                                      getNpjt().update("update snt.ppsreport set name_prod_from = :name_prod_from, "
                                                                                    + "name_prod_to = :name_prod_to, num_operation = :num_operation, "
                                                                                    + "num_act = :num_act, date_act = :date_act "
                                                                                    + "where id = :docid ", map);
                                                               }
                                   
                                                        }
                                                 }, null);
                                                 
                                                 binder.handleTable("table2", "row", new RowHandler<Object>() {
                                                        public void handleRow(DataBinder kinder, int rowNum, Object obj) throws InternalException
                                                        {   
                                                               if(kinder.getValue("vagNum").equals(vagnum)) {
                                                                      map.put("docid", idRecord);
                                                                      map.put("name_prod_from", binder.getValue("sProductName"));
                                                                      map.put("name_prod_to", nProductName);
                                                                      map.put("num_operation", binder.getValue("operNum"));
                                                                      map.put("num_act", vuNumber);
                                                                      map.put("date_act", date);
                                                                      getNpjt().update("update snt.ppsreport set name_prod_from = :name_prod_from, "
                                                                                    + "name_prod_to = :name_prod_to, num_operation = :num_operation, "
                                                                                    + "num_act = :num_act, date_act = :date_act "
                                                                                    + "where id = :docid ", map);
                                                               }
                                   
                                                        }
                                                 }, null);
                                          } 
                                          
                                          if(rs1.getInt("type_act_1") != 0 && rs1.getInt("type_act_1") != 3) {
                                                 
                                                 binder.handleTable("table1", "row", new RowHandler<Object>() {
                                                        public void handleRow(DataBinder kinder, int rowNum, Object obj) throws InternalException
                                                        {   
                                                               if(kinder.getValue("vagNum").equals(vagnum)) {
                                                                      map.put("docid", idRecord);
                                                                      map.put("name_prod_from", binder.getValue("sProductName"));
                                                                      map.put("name_prod_to", nProductName);
                                                                      map.put("num_operation", binder.getValue("operNum"));
                                                                      map.put("num_act", vuNumber);
                                                                      map.put("date_act", date);
       
                                                                      getNpjt().update("update snt.ppsreport set name_prod_from_1 = :name_prod_from, "
                                                               + "name_prod_to_1 = :name_prod_to, num_operation_1 = :num_operation, "
                                                               + "num_act_1 = :num_act, date_act_1 = :date_act  "
                                                               + "where id = :docid", map);
                                                               }
                                   
                                                        }
                                                 }, null);
                                                 
                                                 binder.handleTable("table2", "row", new RowHandler<Object>() {
                                                        public void handleRow(DataBinder kinder, int rowNum, Object obj) throws InternalException
                                                        {   
                                                               if(kinder.getValue("vagNum").equals(vagnum)) {
                                                                      map.put("docid", idRecord);
                                                                      map.put("name_prod_from", binder.getValue("sProductName"));
                                                                      map.put("name_prod_to", nProductName);
                                                                      map.put("num_operation", binder.getValue("operNum"));
                                                                      map.put("num_act", vuNumber);
                                                                      map.put("date_act", date);
       
                                                                      getNpjt().update("update snt.ppsreport set name_prod_from_1 = :name_prod_from, "
                                                               + "name_prod_to_1 = :name_prod_to, num_operation_1 = :num_operation, "
                                                               + "num_act_1 = :num_act, date_act_1 = :date_act  "
                                                               + "where id = :docid", map);
                                                               }
                                   
                                                        }
                                                 }, null);
                                          }
                                          
                                          map.put("id_pps", idRecord);
                                          if(priznak == 1) {
                                                 int count = getNpjt().queryForInt("select count(0) from snt.ppsreport "
                                                               + " where id = :id_pps "
                                                               + " and id_gu45_ub is not null "
                                                               + " and id_app_treatment is not null", map);
                                                 if(count > 0) {
                                                        map.put("complete", 1);
                                                        getNpjt().update("update snt.ppsreport set complete = :complete "
                                                                      + " where id = :id_pps",map);
                                                 } 
                                          } else if(priznak == 2) {
                                                 int count = getNpjt().queryForInt("select count(0) from snt.ppsreport "
                                                               + " where id = :id_pps "
                                                               + " and id_gu45_ub is not null"
                                                               + " and id_app_treatment_1 is not null", map);
                                                 if(count > 0) {
                                                        map.put("complete", 1);
                                                        getNpjt().update("update snt.ppsreport set complete = :complete "
                                                                      + " where id = :id_pps", map);
                                                 } 

                                          }
                                          
//                     }
                                   
                                   
                            map.put("opisanie", getContent(binder));
                            getNpjt().update("update snt.docstore set opisanie = :opisanie where id = :id", map);
                     }
                     }
              } catch(Exception e) {
                     log.error(TypeConverter.exceptionToString(e));
//                     e.printStackTrace();
              }
       }

       @Override
       public void doAfterSave(String docName, String docdata, int predid, int signNumber, String CertID, long id, int WrkId) throws Exception {
              ETDForm form = ETDForm.createFromArchive(docdata.getBytes("UTF-8"));
              DataBinder binder = form.getBinder();
              //System.out.println("docdata " + docdata);
              int countCist = binder.getValuesAsArray("vagNum").length;
              if(binder.getValue("genVu20").length() > 0) {
                     final String p_25 = binder.getValue("P_25");
                     final String p_25_1 = binder.getValue("P_25_1");
                     final String nProductName = binder.getValue("nProductName");
                     final String stName = binder.getValue("stName");
                     final String stCode = binder.getValue("stCode");
                     final List<VU20> listVU20 = new ArrayList<VU20>();
                     final String select = "select id, case WHEN TYPE_ACT = 1 THEN 1 end p1, "
                                   + "case WHEN TYPE_ACT_1 = 1 THEN 2 end p2 from snt.ppsreport "
                            + " where (id_act_workability = :id "
                            + "or id_act_workability_1 = :id) and vagnum = :vagnum";
                     final Map<String, Object> map = new HashMap<String, Object>();
                     map.put("id", id);
                     final Element table1 = binder.getElement("table1");
              binder.handleTable("table1", "row", new RowHandler<Object>() {
                            public void handleRow(DataBinder kinder, int rowNum, Object obj) throws InternalException
                            {   
                                   if(kinder.getValue("P_10_1").equals("true")) {
                                          map.put("vagnum", kinder.getValue("vagNum"));
                                          Long id = null;
                                          Integer p1 = null;
                                          Integer p2 = null;
                                          SqlRowSet rs = getNpjt().queryForRowSet(select, map);
                                          while(rs.next()) {
                                                 id = rs.getLong("id");
                                                 p1 = rs.getInt("p1");
                                                 p2 = rs.getInt("p2");
                                          }
                                          VU20 vu20 = new VU20();
                                          if(p1 != 0) {
                                        	  vu20.setPriznak(1);
                                          }

                                          if(p2 != 0) {
                                        	  vu20.setPriznak(2);
                                          }
                                          vu20.setId(id);
                                          vu20.setCustomer(p_25);
                                          vu20.setOkpo_customer(p_25_1);
                                          vu20.setIdClaim(Integer.parseInt(kinder.getValue("P_22")));
                                          vu20.setDateClaim(kinder.getValue("P_24"));
                                          vu20.setNameProdFrom(kinder.getValue("sProductName"));
                                          vu20.setNameProdTo(nProductName);
                                          vu20.setOkpo_perf(stCode);
                                          vu20.setPerf(stName);
                                          vu20.setVagnum(kinder.getValue("vagNum"));
                                          vu20.setNumClaim(kinder.getValue("P_23"));
                                          vu20.setNumOperation(kinder.getValue("operNum"));
                                          listVU20.add(vu20);
                                   
                                          int numberRow = table1.getElementsByTagName("row").getLength();
                                          if(numberRow > 1) {
                                                 table1.removeChild(kinder.getRootEl());
                                          } else {
                                                 kinder.setNodeValue("P_10_1", "");
                                                 kinder.setNodeValue("P_22", "");
                                                 kinder.setNodeValue("P_23", "");
                                                 kinder.setNodeValue("P_24", "");
                                                 kinder.setNodeValue("vagNum", "");
                                                 kinder.setNodeValue("operNum", "");
                                                 kinder.setNodeValue("sProductName", "");
                                                 kinder.setNodeValue("rowNum", "");
                                          }
                                   }
                            }
                     }, null);
              
              final Element table2 = binder.getElement("table2");
              binder.handleTable("table2", "row", new RowHandler<Object>() {
                     public void handleRow(DataBinder kinder, int rowNum, Object obj) throws InternalException
                     {

                            if(kinder.getValue("P_15_1").equals("true")) {
                                   map.put("vagnum", kinder.getValue("vagNum"));
                                   Long id = null;
                                          Integer p1 = null;
                                          Integer p2 = null;
                                          SqlRowSet rs = getNpjt().queryForRowSet(select, map);
                                          while(rs.next()) {
                                                 id = rs.getLong("id");
                                                 p1 = rs.getInt("p1");
                                                 p2 = rs.getInt("p2");
                                          }
                                          VU20 vu20 = new VU20();
                                          if(p1 != 0) {
                                                 vu20.setPriznak(1);
                                          }
                                          
                                          if(p2 != 0) {
                                                 vu20.setPriznak(2);
                                          }
                                          vu20.setId(id);
                                          vu20.setCustomer(p_25);
                                          vu20.setOkpo_customer(p_25_1);
                                          vu20.setIdClaim(Integer.parseInt(kinder.getValue("P_22")));
                                          vu20.setDateClaim(kinder.getValue("P_24"));
                                          vu20.setNameProdFrom(kinder.getValue("sProductName"));
                                          vu20.setNameProdTo(nProductName);
                                          vu20.setOkpo_perf(stCode);
                                          vu20.setPerf(stName);
                                          vu20.setVagnum(kinder.getValue("vagNum"));
                                          vu20.setNumClaim(kinder.getValue("P_23"));
                                          vu20.setNumOperation(kinder.getValue("operNum"));
                                          listVU20.add(vu20);
                                   
                                   int numberRow = table2.getElementsByTagName("row").getLength();
                                          if(numberRow > 1) {
                                                 table2.removeChild(kinder.getRootEl());
                                          } else {
                                                 kinder.setNodeValue("P_15_1", "");
                                                 kinder.setNodeValue("P_22", "");
                                                 kinder.setNodeValue("P_23", "");
                                                 kinder.setNodeValue("P_24", "");
                                                 kinder.setNodeValue("vagNum", "");
                                                 kinder.setNodeValue("operNum", "");
                                                 kinder.setNodeValue("sProductName", "");
                                                 kinder.setNodeValue("rowNum", "");
                                          }
                            }
              
                     }
              }, null);
              countCist = countCist - listVU20.size();
                     vugenerator.generateVU20(listVU20);
                     binder.setNodeValue("genVu20", "");
                     String opisanie = countCist + " цс под " + binder.getValue("nProductName");
                     map.put("opisanie", opisanie);
                     map.put("bldoc", form.encodeToArchiv());
                     map.put("docdata", form.transform("data"));
                     getNpjt().update("update snt.docstore set bldoc = :bldoc, docdata = :docdata, opisanie = :opisanie "
                                   + " where id = :id", map);
              }
                     
       }

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
//                   e.printStackTrace();
              }
              return doc;
       }
       
       private String getContent(DataBinder binder) throws InternalException {
              int number = binder.getValuesAsArray("row").length;
              String p_7 = binder.getValue("nProductName");
              String content = number + " цс под " + p_7;
              return content;
       }
       
       private void updateClaim(Long idClaim, DataBinder binder, final Map<String, Object> map) throws UnsupportedEncodingException, ServiceException, IOException, InternalException, TransformerException {
              String dateAct = binder.getValue("signDate");
              map.put("idClaim", idClaim);
              byte[] blob = (byte[])npjt.queryForObject("select bldoc from snt.docstore "
                            + "where id = :idClaim", map, byte[].class);
              ETDForm formClaim = ETDForm.createFromArchive(blob);
              final DataBinder binderClaim = formClaim.getBinder();
//                          binder.setRootElement("table1");
              String[] vagnumArray = binder.getValuesAsArray("vagNum");
              binder.resetRootElement();
              binderClaim.setRootElement("data");
              int countCompleteVag1 = binderClaim.getInt("P_21");
              for(final String vagnum: vagnumArray) {
                     final String P_14InClaim = "ВУ-20а " + binder.getValue("vu_number") +" " +  dateAct;
                     binderClaim.handleTable("table1", "row", new RowHandler<Object>() {
                            public void handleRow(DataBinder kinder, int rowNum, Object obj) throws InternalException
                            {   
                                   if(kinder.getValue("P_9").equals(vagnum)) {
                                          kinder.setNodeValue("P_14", P_14InClaim);
                                   }
       
                            }
                     }, null);
              }
        //проверка на перевод в архив и в документы в работе
              String[] mass =  binderClaim.getValuesAsArray("P_14");
              int countCompleteVag = 0;
              for(String s: mass) {
                     if(s.length() > 0) {
                            countCompleteVag++;
                     }
              }
              binderClaim.setNodeValue("P_21", countCompleteVag);
              
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
        if(countCompleteVag == binderClaim.getInt("P_19")) {
              getNpjt().update("update snt.docstore set bldoc = :bldoc, docdata = :docdata, opisanie = :opisanie,  "
                            + "signlvl = null where id = :idClaim", map);
        } else if(toDocumentInWork){
                     getNpjt().update("update snt.docstore set bldoc = :bldoc, docdata = :docdata, opisanie = :opisanie,  "
                                   + "signlvl = 1 where id = :idClaim", map);
        } else {
                     getNpjt().update("update snt.docstore set bldoc = :bldoc, docdata = :docdata, opisanie = :opisanie "
                                   + " where id = :idClaim", map);
        }
       }
}
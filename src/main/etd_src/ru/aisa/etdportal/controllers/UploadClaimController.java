package ru.aisa.etdportal.controllers;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.activemq.transport.tcp.ExceededMaximumConnectionsException;
import org.apache.commons.fileuploadnamesconf.FileItemIterator;
import org.apache.commons.fileuploadnamesconf.FileItemStream;
import org.apache.commons.fileuploadnamesconf.servlet.ServletFileUpload;
import org.apache.commons.fileuploadnamesconf.util.Streams;
import org.apache.commons.io.IOUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.web.servlet.ModelAndView;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import ru.aisa.etdadmin.controllers.AbstractMultipartController;
import ru.aisa.rgd.ws.dao.ServiceFacade;
import ru.aisa.rgd.ws.documents.ETDForm;
import ru.aisa.rgd.ws.domain.Document;
import ru.aisa.rgd.ws.exeption.InternalException;
import ru.aisa.rgd.ws.utility.DataBinder;
import ru.aisa.rgd.ws.utility.RowHandler;
import ru.aisa.rgd.ws.utility.TypeConverter;

public class UploadClaimController extends AbstractMultipartController {

  private ServiceFacade facade;
  
  public UploadClaimController() throws JSONException {
    super();
  }
  
  public ServiceFacade getFacade() {
    return facade;
  }

  public void setFacade(ServiceFacade facade) {
    this.facade = facade;
  }

  
  @Override
  protected JSONObject add(HashMap<String, String> requestParameters,
      HashMap<String, byte[]> requestFiles,HashMap<String, String> requestFileNames) throws JSONException,
      IOException {
    JSONObject response = new JSONObject();
    response.put(success, false); 
    String predid = requestParameters.get("predid");
    byte[] fileContent = requestFiles.get("fileExcel");
    InputStream myInputStream = new ByteArrayInputStream(fileContent);
    XSSFWorkbook workbook = new XSSFWorkbook(myInputStream);
    XSSFSheet sheet = workbook.getSheetAt(0);
    Iterator<Row> rowIterator = sheet.iterator();
    String num = "";
    String okpo = "";
    String nameWork = "";
    String nameFrom = "";
    String nameTo = "";
    String contract = "";
    String numContract = "";
    String dateContract = "";
    Date crDate = null;
    List<String> vagnumList = new ArrayList<String>();
    try {
      final List<ClaimObj> listClaimObjs = new ArrayList<ClaimObj>();
      while (rowIterator.hasNext()) {
        Row row = rowIterator.next();
        if(row.getRowNum() == 2 ) {
          if(row.getCell(3).getCellType() != Cell.CELL_TYPE_BLANK) {
            row.getCell(3).setCellType(Cell.CELL_TYPE_STRING);
            num = row.getCell(3).getStringCellValue();
            if(num.length() > 4) {
            	  response.put(description, "Номер должен быть не больше 4 знаков");
                  throw new Exception();
            }
          }  else {
            response.put(description, "Не проставлен номер");
            throw new Exception();
          }
          if(row.getCell(6).getCellType() != Cell.CELL_TYPE_BLANK) {
            row.getCell(6).setCellType(Cell.CELL_TYPE_STRING);
            okpo =  row.getCell(6).getStringCellValue();
            if(okpo.length() > 11) {
          	  response.put(description, "ОКПО должно быть не больше 11 знаков");
                throw new Exception();
            }
          } 
        }
        
        if(row.getRowNum() == 3) {
          if(row.getCell(2) != null) {
            row.getCell(2).setCellType(Cell.CELL_TYPE_STRING);
            nameFrom = row.getCell(2).getStringCellValue();
            if(nameFrom.length() > 60) {
          	  response.put(description, "Наименование заказчика должно быть не больше 60 знаков");
                throw new Exception();
            }
          }
          
        }
        
        if(row.getRowNum() == 4) {
          if(row.getCell(0) != null) {
            row.getCell(0).setCellType(Cell.CELL_TYPE_STRING);
            nameWork = row.getCell(0).getStringCellValue();
            if(nameWork.length() > 60) {
          	  response.put(description, "Наименование работ должно быть не больше 100 знаков");
                throw new Exception();
          }
          }
          if(row.getCell(4) != null) {
            row.getCell(4).setCellType(Cell.CELL_TYPE_STRING);
            nameTo = row.getCell(4).getStringCellValue();
            if(nameTo.length() > 60) {
            	  response.put(description, "Наименование исполнителя должно быть не больше 60 знаков");
                  throw new Exception();
            }
          }
        }
        
        if(row.getRowNum() == 5) {
          if(row.getCell(0) != null) {
            row.getCell(0).setCellType(Cell.CELL_TYPE_STRING);
            contract = row.getCell(0).getStringCellValue();
            if(contract.length() > 30) {
          	    response.put(description, "Название документа должно быть не больше 30 знаков");
                throw new Exception();
            }
          }
          if(row.getCell(2) != null) {
            row.getCell(2).setCellType(Cell.CELL_TYPE_STRING);
            numContract = row.getCell(2).getStringCellValue();
            if(numContract.length() > 15) {
          	  response.put(description, "Номер документа должен быть не больше 15 знаков");
                throw new Exception();
            }
          }
          if(row.getCell(3) != null) {
            row.getCell(3).setCellType(Cell.CELL_TYPE_STRING);
            dateContract = row.getCell(3).getStringCellValue();
            String date = dateContract.substring(3, 13); 
            Pattern regexp = Pattern.compile("((0[1-9])|([12]\\d)|(3[01]))\\.((0[1-9])|(1[0-2]))\\.\\d{4}");
            Matcher m = regexp.matcher(date);
            if(m.matches() == false) {
            	response.put(description, "Даты должны быть в формате ДД.ММ.ГГГГ");
         		throw new Exception();
            }
          }
        }
        
        if(row.getRowNum() == 6) {
          if(row.getCell(1).getCellType() != Cell.CELL_TYPE_BLANK) {
            row.getCell(1).setCellType(Cell.CELL_TYPE_NUMERIC);
            try {
            	crDate  = row.getCell(1).getDateCellValue();
            } catch(Exception e) {
            	response.put(description, "Даты должны быть в формате ДД.ММ.ГГГГ");
         		throw new Exception();
            }
          } else {
            response.put(description, "Нет даты");
            throw new Exception();
          }
        }
        
        if(row.getRowNum() > 10 ) {
          if(row.getCell(0) != null) {
            ClaimObj claimObj = new ClaimObj();
            for(int j = 1;j < 5; j++) {
              row.getCell(j).setCellType(Cell.CELL_TYPE_STRING);
            }
            if(!row.getCell(1).getStringCellValue().equals("")) {
            	String nomer = row.getCell(4).getStringCellValue();
            	if(!nomer.equals("0") && !nomer.equals("1") &&
            			!nomer.equals("2") && !nomer.equals("3") && !nomer.equals("")) {
            		response.put(description, "Номер операции должен быть от 0 до 3");
            		throw new Exception();
            	}
            	String name_prod1 = row.getCell(2).getStringCellValue();
            	if(name_prod1.length() > 30) {
            		response.put(description, "Наименование продукта должно содержать до 30 знаков");
            		throw new Exception();
            	}
            	String name_prod2 = row.getCell(3).getStringCellValue();
            	if(name_prod2.length() > 30) {
            		response.put(description, "Наименование продукта должно содержать до 30 знаков");
            		throw new Exception();
            	}
            	String vagnum = row.getCell(1).getStringCellValue();
             	if(vagnum.length() > 8) {
            		response.put(description, "номер вагона должен быть до 8 знаков");
            		throw new Exception();
            	}
            	vagnumList.add(vagnum);
            	claimObj.setP_9(vagnum);
            	claimObj.setP_10(name_prod1);
            	claimObj.setP_11(name_prod2);
            	claimObj.setP_12(nomer);

            	listClaimObjs.add(claimObj);

            }
            
          }
        }
      }
      
     

      ETDForm form = new ETDForm(facade.getDocumentTemplate("Заявка ППС"));
      DataBinder binder = form.getBinder();
      binder.setNodeValue("P_1", num);
      binder.setNodeValue("P_2", nameFrom);
      binder.setNodeValue("P_3", nameWork);
      binder.setNodeValue("P_4", nameTo);
      Map<String, Object> pp = new HashMap<String, Object>();
      pp.put("predid",  predid);
      String okpo_codePerf = getNpjt().queryForObject("select okpo_kod from snt.pred"
          + " where id = :predid", pp, String.class);
      binder.setNodeValue("P_4a", okpo_codePerf);
      binder.setNodeValue("P_5_6", contract + " " + numContract + " " + dateContract.substring(0, 2) );
      dateContract  = dateContract.substring(3, 13); 
      binder.setNodeValue("P_7", dateContract);
   
      
      SimpleDateFormat myDateFormat1 = new SimpleDateFormat("dd.MM.yyyy");
          String date = myDateFormat1.format(crDate);
      binder.setNodeValue("P_8", date);
        binder.setNodeValue("P_19", listClaimObjs.size());
        binder.setNodeValue("P_20", okpo);
      binder.setNodeValue("P_21", 0);
          binder.setRootElement("row");
          int rowNum = 1;
        for(int i = 0; i < listClaimObjs.size(); i++) {
        ClaimObj object = listClaimObjs.get(i);
        binder.resetRootElement();
          binder.setRootLastElement("row");
        if(listClaimObjs.size() != 1 && i != listClaimObjs.size() - 1) {
          binder.cloneNode(binder.getRootEl());
        }
        
          if(i == 0) {
            binder.setNodeValue("rowNum", rowNum);
            binder.setNodeValue("P_9", object.getP_9());
            binder.setNodeValue("P_10", object.getP_10());
            binder.setNodeValue("P_11", object.getP_11());
            binder.setNodeValue("P_12", object.getP_12());
          } else {
            binder.setNodeValue("rowNum", rowNum);
            binder.setNodeValue("P_9", object.getP_9());
            binder.setNodeValue("P_10", object.getP_10());
            binder.setNodeValue("P_11", object.getP_11());
            binder.setNodeValue("P_12", object.getP_12());
          }
          rowNum++;
      }
         
      Document document = new Document();
      Long id = facade.getDocumentDao().getNextId();
    
      StringBuilder stb = new StringBuilder();
      pp.put("vagnums", vagnumList);
      pp.put("predid", predid);
      SqlRowSet srs = getNpjt().queryForRowSet("select  VAGNUM "
          + "from snt.PPSREPORT where vagnum in (:vagnums) and predid in " 
          + "(select id from snt.pred where headid = :predid or id = :predid)"
          + " and NAME_PROD_FROM_1 is not null and complete is null", pp);
      while(srs.next()) {
        String vagnum = srs.getString("vagnum");
        if(stb.length() == 0) {
          stb.append(vagnum);
        } else {
          stb.append(", " + vagnum);
        }
      }
      if(stb.length() > 0) {
        response.put(description, "Заявка не загружена, так как уже существуют заявки "
            + "на вагоны:" + stb.toString());
        throw new Exception();
      }
      
      for(ClaimObj claimObject: listClaimObjs) {
        pp.put("vagnum", claimObject.getP_9());
        
        Integer count = getNpjt().queryForInt("select count(0) "
            + "from snt.PPSREPORT where vagnum = :vagnum "
            + " and complete is null and predid in " 
            + "(select id from snt.pred where headid = :predid or id = :predid)", pp);
        
        if(count > 0) {
          SqlRowSet rs = getNpjt().queryForRowSet("select ID, "
              + "ID_APP_TREATMENT, num_act, num_act_1, id_gu45_ub, id_gu45_pod from snt.PPSREPORT "
              + "where vagnum = :vagnum  and complete is null and predid in " 
              + "(select id from snt.pred where headid = :predid or id = :predid)", pp);
             
          while(rs.next()) {
            if(rs.getString("ID_APP_TREATMENT") != null) {
              
              pp.put("NAME_PROD_FROM_1", claimObject.getP_10());
              pp.put("NAME_PROD_TO_1", claimObject.getP_11());
              pp.put("NUM_OPERATION_1", claimObject.getP_12());
              pp.put("APP_NUM_1", num);
              pp.put("DATE_DOC_1", crDate);
              pp.put("ID_APP_TREATMENT_1", id);
              pp.put("CUSTOMER", nameFrom);
              pp.put("OKPO_CUSTOMER", okpo);
              pp.put("PERFORMER", nameTo);
              pp.put("OKPO_PERFORMER", okpo_codePerf);
              pp.put("id", rs.getLong("id"));
              
              int countRec = getNpjt().queryForInt("select count(0) from snt.ppsreport "
              		+ " where id_gu45_ub_1 is not null and "
              		+ " (type_act_1 = 3 or num_act_1 is not null) and id_gu45_pod is not null"
              		+ " and id = :id", pp);
              if(countRec > 0) {
            	  pp.put("complete", 1);
                  getNpjt().update("update snt.PPSREPORT set NAME_PROD_FROM_1 = :NAME_PROD_FROM_1, "
                          + "NAME_PROD_TO_1 = :NAME_PROD_TO_1, NUM_OPERATION_1 = :NUM_OPERATION_1, "
                          + "APP_NUM_1 = :APP_NUM_1, DATE_DOC_1 = :DATE_DOC_1, ID_APP_TREATMENT_1 = :ID_APP_TREATMENT_1, "
                          + "CUSTOMER = :CUSTOMER, OKPO_PERFORMER = :OKPO_PERFORMER, PERFORMER = :PERFORMER, "
                          + "OKPO_CUSTOMER = :OKPO_CUSTOMER, complete = :complete, NOTE = :CUSTOMER  where id = :id", pp);
              } else {
            	  getNpjt().update("update snt.PPSREPORT set NAME_PROD_FROM_1 = :NAME_PROD_FROM_1, "
                          + "NAME_PROD_TO_1 = :NAME_PROD_TO_1, NUM_OPERATION_1 = :NUM_OPERATION_1, "
                          + "APP_NUM_1 = :APP_NUM_1, DATE_DOC_1 = :DATE_DOC_1, ID_APP_TREATMENT_1 = :ID_APP_TREATMENT_1, "
                          + "CUSTOMER = :CUSTOMER, OKPO_PERFORMER = :OKPO_PERFORMER, "
                          + "PERFORMER = :PERFORMER, OKPO_CUSTOMER = :OKPO_CUSTOMER, NOTE = :CUSTOMER  where id = :id", pp);
              }
            } else if(rs.getString("ID_APP_TREATMENT") == null) {
              pp.put("NAME_PROD_FROM", claimObject.getP_10());
              pp.put("NAME_PROD_TO", claimObject.getP_11());
              pp.put("NUM_OPERATION", claimObject.getP_12());
              pp.put("APP_NUM", num);
              pp.put("DATE_DOC", crDate);
              pp.put("ID_APP_TREATMENT", id);
              pp.put("CUSTOMER", nameFrom);
              pp.put("OKPO_CUSTOMER", okpo);
              pp.put("PERFORMER", nameTo);
              pp.put("OKPO_PERFORMER", okpo_codePerf);
              pp.put("id", rs.getLong("id"));
              
              
              int countRec = getNpjt().queryForInt("select count(0) from snt.ppsreport "
                		+ " where id_gu45_ub is not null and "
                		+ " ((type_act = 3 and id_app_treatment_1 is null) or num_act is not null) "
                		+ " and id = :id  and id_gu45_pod is not null", pp);
              
              if(countRec > 0) {
            	  pp.put("complete", 1);
                  getNpjt().update("update snt.PPSREPORT set NAME_PROD_FROM = :NAME_PROD_FROM, "
                          + "NAME_PROD_TO = :NAME_PROD_TO, NUM_OPERATION = :NUM_OPERATION, "
                          + "APP_NUM = :APP_NUM, DATE_DOC = :DATE_DOC, ID_APP_TREATMENT = :ID_APP_TREATMENT, "
                          + "CUSTOMER = :CUSTOMER, OKPO_PERFORMER = :OKPO_PERFORMER,"
                          + " PERFORMER = :PERFORMER, OKPO_CUSTOMER = :OKPO_CUSTOMER, "
                          + " complete = :complete, NOTE = :CUSTOMER  where id = :id", pp);
              } else {
                  getNpjt().update("update snt.PPSREPORT set NAME_PROD_FROM = :NAME_PROD_FROM, "
                          + " NAME_PROD_TO = :NAME_PROD_TO, NUM_OPERATION = :NUM_OPERATION, "
                          + " APP_NUM = :APP_NUM, DATE_DOC = :DATE_DOC, ID_APP_TREATMENT = :ID_APP_TREATMENT, "
                          + " CUSTOMER = :CUSTOMER, OKPO_PERFORMER = :OKPO_PERFORMER, "
                          + " PERFORMER = :PERFORMER, OKPO_CUSTOMER = :OKPO_CUSTOMER, NOTE = :CUSTOMER "
                          + " where id = :id", pp);
              }
            } 
          }
        } else {
          String insert = "insert into snt.ppsreport(VAGNUM, NAME_PROD_FROM,"
              + " NAME_PROD_TO, NUM_OPERATION, APP_NUM, CUSTOMER, ID_APP_TREATMENT, "
              + "PREDID, OKPO_CUSTOMER, PERFORMER, OKPO_PERFORMER, DATE_DOC, NOTE) "
              + " values(:VAGNUM, :NAME_PROD_FROM, :NAME_PROD_TO, :NUM_OPERATION, :APP_NUM, "
              + ":CUSTOMER, :ID_APP_TREATMENT, :PREDID, :OKPO_CUSTOMER, :PERFORMER, "
              + ":OKPO_PERFORMER, :DATE_DOC, :CUSTOMER)";
          pp.put("VAGNUM", claimObject.getP_9());
          pp.put("NAME_PROD_FROM", claimObject.getP_10());
          pp.put("NAME_PROD_TO", claimObject.getP_11());
          pp.put("NUM_OPERATION", claimObject.getP_12());
          pp.put("APP_NUM", num);
          pp.put("CUSTOMER", nameFrom);
          pp.put("OKPO_CUSTOMER", okpo);
          pp.put("PERFORMER", nameTo);
          pp.put("DATE_DOC", crDate);
          pp.put("ID_APP_TREATMENT", id);
          pp.put("PREDID", predid);
          pp.put("OKPO_PERFORMER", okpo_codePerf);
          getNpjt().update(insert, pp);
        }
      }
      
      
      document.setBlDoc(form.encodeToArchiv());
      document.setDocData(form.transform("data"));
      Map<String, Object> map = new HashMap<String, Object>();
//      map.put("okpo_kod", okpo_perf);
//      int predId = getNpjt().queryForInt("select id from snt.pred where "
//          + "okpo_kod = :okpo_kod and headid is null", map);
      document.setPredId(Integer.parseInt(predid));
      document.setSignLvl(0);
      document.setNo(num);
      document.setType("Заявка ППС");
      document.setId(id);
      facade.insertDocumentWithDocid(document);
      binder.resetRootElement();
      map.put("opisanie", binder.getValue("P_21") + " из " + binder.getValue("P_19") + " обработано");
      map.put("reqdate", crDate);
      map.put("id", id);
      map.put("di", numContract + " " + dateContract);
      getNpjt().update("update snt.docstore set opisanie = :opisanie, reqdate = :reqdate, di = :di "
          + " where id = :id", map) ;
      map.put("okpo_kod", okpo);
      int predId = getNpjt().queryForInt("select id from snt.pred where "
          + "okpo_kod = :okpo_kod and headid is null", map);
      map.put("predId", predId);
        getNpjt().update("insert into snt.marsh (docid, predid) values(:id, :predId)", map);
//      System.out.println("id z_pps " + id);
      response.put(success, true);
      
    } catch(Exception e) {
//      e.printStackTrace();
      log.error(TypeConverter.exceptionToString(e));
      //response.put(description, "Неизвестная ошибка");
    }

    return response;
  }
  
  public static byte[] getBytes(InputStream is) throws IOException {
      ByteArrayOutputStream buffer = new ByteArrayOutputStream();

      int len;
      byte[] data = new byte[100000];
      while ((len = is.read(data, 0, data.length)) != -1) {
      buffer.write(data, 0, len);
      }

      buffer.flush();
      return buffer.toByteArray();
  }
  
  private class ClaimObj {
    
    private String num;
    private String okpo;
    private String nameWork;
    private String nameFrom;
    private String nameTo;
    private String contract;
    private String numContract;
    private String dateContract;
    private String p_9;
    private String p_10;
    private String p_11;
    private String p_12;
    
    public String getP_9() {
      return p_9;
    }
    public void setP_9(String p_9) {
      this.p_9 = p_9;
    }
    public String getP_10() {
      return p_10;
    }
    public void setP_10(String p_10) {
      this.p_10 = p_10;
    }
    public String getP_11() {
      return p_11;
    }
    public void setP_11(String p_11) {
      this.p_11 = p_11;
    }
    public String getP_12() {
      return p_12;
    }
    public void setP_12(String p_12) {
      this.p_12 = p_12;
    }
    public String getNum() {
      return num;
    }
    public void setNum(String num) {
      this.num = num;
    }
    public String getOkpo() {
      return okpo;
    }
    public void setOkpo(String okpo) {
      this.okpo = okpo;
    }
    public String getNameWork() {
      return nameWork;
    }
    public void setNameWork(String nameWork) {
      this.nameWork = nameWork;
    }
    public String getNameFrom() {
      return nameFrom;
    }
    public void setNameFrom(String nameFrom) {
      this.nameFrom = nameFrom;
    }
    public String getNameTo() {
      return nameTo;
    }
    public void setNameTo(String nameTo) {
      this.nameTo = nameTo;
    }
    public String getContract() {
      return contract;
    }
    public void setContract(String contract) {
      this.contract = contract;
    }
    public String getNumContract() {
      return numContract;
    }
    public void setNumContract(String numContract) {
      this.numContract = numContract;
    }
    public String getDateContract() {
      return dateContract;
    }
    public void setDateContract(String dateContract) {
      this.dateContract = dateContract;
    }
    
  }
}
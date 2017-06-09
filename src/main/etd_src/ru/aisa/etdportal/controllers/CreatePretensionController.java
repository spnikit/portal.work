package ru.aisa.etdportal.controllers;

import java.math.BigDecimal;
import java.sql.Blob;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.bouncycastle.asn1.isismtt.x509.ProfessionInfo;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.jdbc.core.simple.SimpleJdbcTemplate;
import org.springframework.jdbc.support.rowset.ResultSetWrappingSqlRowSet;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.transaction.support.TransactionTemplate;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import ru.aisa.rgd.ws.dao.ServiceFacade;
import ru.aisa.rgd.ws.documents.ETDForm;
import ru.aisa.rgd.ws.domain.Document;
import ru.aisa.rgd.ws.exeption.InternalException;
import ru.aisa.rgd.ws.utility.Attribute;
import ru.aisa.rgd.ws.utility.DataBinder;
import ru.aisa.rgd.ws.utility.RowHandler;
import ru.aisa.rgd.ws.utility.TypeConverter;
import sheff.rgd.ws.Controllers.TOR.NumberOfWords;

public class CreatePretensionController extends AbstractPortalSimpleController {

	private ServiceFacade facade;
	private TransactionTemplate transT;
	private String formname;
	private SimpleJdbcTemplate sjt;
	private static Logger log = Logger.getLogger(CreatePretensionController.class);
	
	public CreatePretensionController() throws JSONException {
		super();
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
	
	
	public String getFormname() {
		return formname;
	}

	public void setFormname(String formname) {
		this.formname = formname;
	}
	
	public SimpleJdbcTemplate getSjt() {
		return sjt;
	}

	public void setSjt(SimpleJdbcTemplate sjt) {
		this.sjt = sjt;
	}

	@Override
	public JSONArray get(HttpServletRequest request){
		final JSONArray response = new JSONArray();
		String reqnum = null;
		String id_pak = null;
		String onbaseid = null;
		if (request.getParameter("packId") != null) {
			HashMap<String, Object> pp = new HashMap<String, Object>();
			id_pak = request.getParameter("packId");
			int predid = Integer.parseInt(request.getParameter("predid"));
			pp.put("id_pak", id_pak);
			int count = getNpjt().queryForInt("select count(0) from snt.docstore where id_pak = :id_pak "
					+ "and typeid = (select id from snt.doctype where rtrim(name) = 'Претензия')", pp);
			JSONObject js = new JSONObject();
			try {
				if(count > 0) {
					js.put("message", "Претензия на данный ремонт ранее создавалась");
					response.put(js);
					reqnum = "Вторичный";
				} 
				
				if(!checkFormInPackage("ВУ-23_О", id_pak)) {
					js.put("message", "Отсутствует ВУ-23_О");
					response.put(js);
					return response;
				}
				if(!checkFormInPackage("РДВ", id_pak)) {
					js.put("message", "Отсутствует РДВ");
					response.put(js);
					return response;
				}
				if(count >= 0) {
					ETDForm formPretension = new ETDForm(facade.getDocumentTemplate("Претензия"));
				    DataBinder pretensionBinder = formPretension.getBinder();
				    
				    ETDForm packageForm = null;
				    ETDForm RDVForm = null;
				    ETDForm VU23Form = null;
				    ResultSetWrappingSqlRowSet srs =  (ResultSetWrappingSqlRowSet)getNpjt().
				    		queryForRowSet("select bldoc, (select rtrim(name) from snt.doctype where id = ds.typeid) name,"
				    				+ " onbaseid, id_pak "
				    				+ " from snt.docstore as ds " 
				    				+ " where etdid = :id_pak or "
				    				+ " (etdid in (select etdid from snt.packages where id_pak = :id_pak))", pp);
					ResultSet resultSet = srs.getResultSet();
					while(resultSet.next()) {
						 Blob blob = resultSet.getBlob("bldoc");
						 if(resultSet.getString("name").equals("Пакет документов")) {
							 packageForm = ETDForm.createFromArchive(blob.getBytes(1, (int)blob.length()));
							 id_pak = resultSet.getString("id_pak");
							 onbaseid = resultSet.getString("onbaseid");
						 } else if(resultSet.getString("name").equals("РДВ")) {
							 RDVForm = ETDForm.createFromArchive(blob.getBytes(1, (int)blob.length()));
						 } else if(resultSet.getString("name").equals("ВУ-23_О")) {
							 VU23Form = ETDForm.createFromArchive(blob.getBytes(1, (int)blob.length()));
						 }
					}
				    DataBinder packageBinder = packageForm.getBinder();
				    DataBinder rdvBinder = RDVForm.getBinder();
				    DataBinder vu23Binder = VU23Form.getBinder();
				    String P_38 = rdvBinder.getValue("P_38").trim();
				    pretensionBinder.setNodeValue("P_2", P_38);
				    pretensionBinder.setNodeValue("P_8", packageBinder.getValue("P_15"));
				    pretensionBinder.setNodeValue("P_23", packageBinder.getValue("P_15"));
				    pretensionBinder.setNodeValue("P_10", packageBinder.getValue("P_21"));
				    pretensionBinder.setNodeValue("P_11a", packageBinder.getValue("P_19"));
				    pretensionBinder.setNodeValue("P_12", packageBinder.getValue("P_15"));
				    pretensionBinder.setNodeValue("P_15", packageBinder.getValue("P_22"));
				    pretensionBinder.setNodeValue("P_17", P_38);
					NumberOfWords now = new NumberOfWords(P_38);
				    pretensionBinder.setNodeValue("P_18", now.num2str(false));
				    pretensionBinder.setNodeValue("P_24", P_38);
				    pretensionBinder.setNodeValue("P_25", pretensionBinder.getValue("P_18"));
				    pp.put("predid", predid);
					String predName = getNpjt().queryForObject("select rtrim(name) from snt.pred "
							+ " where id = :predid", pp, String.class);
					pretensionBinder.setNodeValue("P_30", predName);
					SqlRowSet rs = getNpjt().queryForRowSet("select bik, account, korraccount, bankname, "
							+ " inn, kpp from snt.pred_requisites where predid = :predid", pp);
					while(rs.next()) {
					    pretensionBinder.setNodeValue("P_31", rs.getString("inn"));
					    pretensionBinder.setNodeValue("P_32", rs.getString("kpp"));
					    pretensionBinder.setNodeValue("P_34", rs.getString("bik"));
					    pretensionBinder.setNodeValue("P_36", rs.getString("korraccount"));
					    pretensionBinder.setNodeValue("P_37", rs.getString("account"));
					    pretensionBinder.setNodeValue("P_38", rs.getString("bankname"));
					}
				    String defectName = vu23Binder.getValue("disrepair_item1_name");
				    if(vu23Binder.getValue("disrepair_item2_name").length() > 0) {
				    	defectName = defectName + "," + vu23Binder.getValue("disrepair_item2_name");
				    }	    
				    if(vu23Binder.getValue("disrepair_item3_name").length() > 0) {
				    	defectName = defectName + "," + vu23Binder.getValue("disrepair_item3_name");
				    }
				    pretensionBinder.setNodeValue("P_13", defectName);
				    
				    String codeDefect = vu23Binder.getValue("disrepair_item1_id");
				    if(!vu23Binder.getValue("disrepair_item2_id").equals("000")) {
				    	codeDefect = codeDefect + "," + vu23Binder.getValue("disrepair_item2_id");
				    }	    
				    if(!vu23Binder.getValue("disrepair_item3_id").equals("000")) {
				    	codeDefect = codeDefect + "," + vu23Binder.getValue("disrepair_item3_id");
				    }
				    pretensionBinder.setNodeValue("P_14", codeDefect);
				    pretensionBinder.setNodeValue("P_16", getNumWorksFromRDV(rdvBinder));
				    List<RDVWork> list = getListP23_P24FieldValue(rdvBinder);
					
				    pretensionBinder.setNodeValue("P_29", P_38);
				    pretensionBinder.setRootElement("table1");
				    pretensionBinder.setRootElement("row");
				    for(int i = 0; i < list.size(); i++) {
				    	RDVWork rdvWork = list.get(i);
				    	String numWork = "";
				    	if(rdvWork.getNumWork() != null) {
				    		numWork = rdvWork.getNumWork();
				    	}
				    	if(i == 0 ) {
				    		pretensionBinder.setNodeValue("P_26", numWork);
				    		pretensionBinder.setNodeValue("P_27", rdvWork.getNameWork());
				    		pretensionBinder.setNodeValue("P_28", rdvWork.getSumm());
				    	} else {
				    		pretensionBinder.resetRootElement();	
				    		pretensionBinder.setRootElement("table1");
				    		Node node = pretensionBinder.cloneNode("row");
				    		pretensionBinder.setRootElement((Element)node);
				    		pretensionBinder.setNodeValue("P_26", numWork);
				    		pretensionBinder.setNodeValue("P_27", rdvWork.getNameWork());
				    		pretensionBinder.setNodeValue("P_28", rdvWork.getSumm());
				    	}
				    }
					pretensionBinder.resetRootElement();	
				    pretensionBinder.setRootElement("data");
				    String capitalRepair = vu23Binder.getValue("last_major_repair_date");
				    String depoRepair = vu23Binder.getValue("last_depo_repair_date");
				    SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
				    
				    Date capitalRepairDate = null;
				    if(capitalRepair.length() > 0) {
				    	capitalRepairDate = sdf.parse(capitalRepair);
				    }
				    
				    Date depoRepairDate = null;
				    if(depoRepair.length() > 0) {
				    	depoRepairDate = sdf.parse(depoRepair);
					}
				    Date dateRepair = null;
				    String repairPredname = null;
				    String kleymo = null;
				    if(capitalRepairDate != null && depoRepairDate == null) {
				    	repairPredname = vu23Binder.getValue("last_major_repair_name");
			    		dateRepair = capitalRepairDate;
			    		kleymo = vu23Binder.getValue("last_major_repair_id");
				    } else if(capitalRepairDate == null && depoRepairDate != null) {
				    	repairPredname = vu23Binder.getValue("last_depo_repair_name");
			    		dateRepair = depoRepairDate;
			    		kleymo = vu23Binder.getValue("last_depo_repair_id");
				    } else if(capitalRepairDate == null && depoRepairDate == null) {
				    	js.put("message", "Отсутствует дата КР и ДР в ВУ-23_О");
						response.put(js);
						return response;
				    } else {
				    	int resultCompareDateRepair = capitalRepairDate.compareTo(depoRepairDate);

				    	if(resultCompareDateRepair == 1) {
				    		repairPredname = vu23Binder.getValue("last_major_repair_name");
				    		dateRepair = capitalRepairDate;
				    		kleymo = vu23Binder.getValue("last_major_repair_id");
				    	} else if(resultCompareDateRepair == -1) {
				    		repairPredname = vu23Binder.getValue("last_depo_repair_name");
				    		dateRepair = depoRepairDate;
				    		kleymo = vu23Binder.getValue("last_depo_repair_id");
				    	} else {
				    		repairPredname = vu23Binder.getValue("last_major_repair_name");
				    		dateRepair = capitalRepairDate;
				    		kleymo = vu23Binder.getValue("last_major_repair_id");
				    	}
				    }
				   
				    String dRepair = sdf.format(dateRepair);
				    pretensionBinder.setNodeValue("P_6", dRepair);
				    pretensionBinder.setNodeValue("P_11", packageBinder.getValue("P_18"));
				    pretensionBinder.setNodeValue("P_20", repairPredname.trim());
				    
				    Long repair = dateRepair.getTime();
				    pp.put("kleymo", kleymo);
					rs = getNpjt().queryForRowSet("select contr_name name, contract_date, "
							+ " num_contract, varranty_repair, refund, depo  from snt.vrkcontract "
							+ " where kleymo = :kleymo and predid in "
							+ "(select id from snt.pred where headid = :predid or id = :predid)", pp);
					List<ContractData> listContractData = new ArrayList<ContractData>();
				    int ch = 0;
					while(rs.next()) {
						ContractData contractData = new ContractData();
						contractData.setContractDate(rs.getDate("contract_date"));
						contractData.setContrname(rs.getString("name"));
						contractData.setNumContract(rs.getString("num_contract"));
						contractData.setRefund(rs.getString("refund"));
						contractData.setVarranty_repair(rs.getString("varranty_repair"));
						contractData.setTime(rs.getDate("contract_date").getTime());
						contractData.setDepo(rs.getString("depo"));
						listContractData.add(contractData); 
						ch++;
					}
					if(ch == 0) {
						js.put("message", "Клеймо депо не найдено в таблице договоров АО \"ПГК\"");
						response.put(js);
						return response;
					}
					Collections.sort(listContractData);
					ContractData contractData = listContractData.get(listContractData.size()-1);
					for(int i = 0; i < listContractData.size(); i++) {
						ContractData data = listContractData.get(i);
						if(i == 0) {
							if(repair - data.getTime() < 0) {
								contractData = data;
								break;
							} 
						} else {
							if(repair - listContractData.get(i-1).getTime() 
									< Math.abs(repair - data.getTime())) {
								contractData = listContractData.get(i-1);
								break;
							}
						}
					}
				    String dateVRK = sdf.format(contractData.getContractDate());
				    pretensionBinder.setNodeValue("P_3", dateVRK);
				    pretensionBinder.setNodeValue("P_4", contractData.getContrname());
				    pretensionBinder.setNodeValue("P_22", contractData.getContrname());
				    pretensionBinder.setNodeValue("P_5", contractData.getNumContract());
				    pretensionBinder.setNodeValue("P_9", contractData.getVarranty_repair());
				    pretensionBinder.setNodeValue("P_7", contractData.getDepo());
				    pretensionBinder.setNodeValue("P_21", contractData.getRefund());
				    
				    Document document = new Document();
				    Long id = facade.getNextDocumentId();
				    document.setId(id);
				    document.setSignLvl(0);
				    document.setType(formname);
					document.setPredId(predid);
					document.setBlDoc(formPretension.encodeToArchiv());
					document.setDocData(formPretension.transform("data"));
				    facade.insertDocumentWithDocid(document);
				    addInPackInfo(id, codeDefect);
				    pp.put("vagnum", pretensionBinder.getValue("P_8"));
				    pp.put("id_pak", id_pak);
				    pp.put("price", pretensionBinder.getValue("P_29"));
				    pp.put("repdate", pretensionBinder.getValue("P_15"));
				    pp.put("reqnum", reqnum);
				    pp.put("onbaseid", onbaseid);
				    pp.put("no", contractData.getNumContract());
				    pp.put("opisanie", contractData.getContrname());
				    pp.put("id", id);
				    getNpjt().update("update snt.docstore set vagnum = :vagnum, id_pak = :id_pak,"
				    		+ " price =:price, repdate = :repdate, reqnum = :reqnum, onbaseid = :onbaseid" 
				    		+ ", no = :no, opisanie = :opisanie "
							+ " where id = :id", pp);
				    
				}
			} catch (Exception e) {
				log.error(TypeConverter.exceptionToString(e));
				try {
					js.put("message", "Ошибка при создании претензии");
				} catch (JSONException e1) {
					log.error(TypeConverter.exceptionToString(e1));
				}
				response.put(js);
				return response;
			}
			
			response.put(js);
		}
		
		return response;
	}
	
	private boolean checkFormInPackage(String formname, String id_pak) {
		boolean result = false;
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("name", formname);
		map.put("id_pak", id_pak);
		
		int countForm = getNpjt().queryForInt("select count(0) from snt.packages where id_pak = :id_pak "
				+ " and etdid in (select etdid from snt.docstore where typeid = "
				+ "(select id from snt.doctype where rtrim(name) = :name))", map);
		if(countForm > 0) {
			result = true;
		}
		return result;
	}
	
	private String getNumWorksFromRDV(DataBinder binder) throws InternalException {
		List<RDVWork> list = getListP23_P24FieldValue(binder);
		StringBuilder stb = new StringBuilder();
		for(int i = 0; i< list.size(); i++) {
			RDVWork rdWork = list.get(i);
			if(rdWork.getNumWork()!= null && rdWork.getNumWork().length() > 0) {
				if(stb.length() > 0) {
					stb.append(",");
				}
				stb.append(rdWork.getNumWork());
			}
		}
		return stb.toString();
	}
	
	private List<RDVWork> getListP23_P24FieldValue(DataBinder binder) throws InternalException {
		
		final List<RDVWork> listRWorks = new ArrayList<CreatePretensionController.RDVWork>();
		
		binder.handleTable("table4", "row", new RowHandler<Object>() {
			public void handleRow(DataBinder kinder, int rowNum, Object obj) throws InternalException
			{
				if(kinder.getValue("P_23_1").length() > 0) {
					RDVWork rdvWork = new RDVWork();
					rdvWork.setNumWork(kinder.getValue("P_23_1").trim());
					rdvWork.setNameWork(kinder.getValue("P_24_1").trim());
					rdvWork.setSumm(kinder.getValue("P_27_1").trim());
					listRWorks.add(rdvWork);
				}
			}
		}, null);

		binder.handleTable("table5", "row", new RowHandler<Object>() {
			public void handleRow(DataBinder kinder, int rowNum, Object obj) throws InternalException
			{
				if(kinder.getValue("P_23_2").length() > 0) {
					RDVWork rdvWork = new RDVWork();
					rdvWork.setNumWork(kinder.getValue("P_23_2").trim());
					rdvWork.setNameWork(kinder.getValue("P_24_2").trim());
					rdvWork.setSumm(kinder.getValue("P_27_2").trim());
					listRWorks.add(rdvWork);
				}
			}
		}, null);

		binder.handleTable("table6", "row", new RowHandler<Object>() {
			public void handleRow(DataBinder kinder, int rowNum, Object obj) throws InternalException
			{
				if(kinder.getValue("P_23_3").length() > 0) {
					RDVWork rdvWork = new RDVWork();
					rdvWork.setNumWork(kinder.getValue("P_23_3").trim());
					rdvWork.setNameWork(kinder.getValue("P_24_3").trim());
					rdvWork.setSumm(kinder.getValue("P_27_3").trim());
					listRWorks.add(rdvWork);
				}
			}
		}, null);

		binder.handleTable("table7", "row", new RowHandler<Object>() {
			public void handleRow(DataBinder kinder, int rowNum, Object obj) throws InternalException
			{
				if(kinder.getValue("P_23_4").length() > 0) {
					RDVWork rdvWork = new RDVWork();
					rdvWork.setNumWork(kinder.getValue("P_23_4").trim());
					rdvWork.setNameWork(kinder.getValue("P_24_4").trim());
					rdvWork.setSumm(kinder.getValue("P_27_4").trim());
					listRWorks.add(rdvWork);
				}
			}
		}, null);

		binder.handleTable("table8", "row", new RowHandler<Object>() {
			public void handleRow(DataBinder kinder, int rowNum, Object obj) throws InternalException
			{
				if(kinder.getValue("P_23_5").length() > 0) {
					RDVWork rdvWork = new RDVWork();
					rdvWork.setNumWork(kinder.getValue("P_23_5").trim());
					rdvWork.setNameWork(kinder.getValue("P_24_5").trim());
					rdvWork.setSumm(kinder.getValue("P_27_5").trim());
					listRWorks.add(rdvWork);
				}
			}
		}, null);

		binder.handleTable("table9", "row", new RowHandler<Object>() {
			public void handleRow(DataBinder kinder, int rowNum, Object obj) throws InternalException
			{
				if(kinder.getValue("P_23_6").length() > 0) {
					RDVWork rdvWork = new RDVWork();
					rdvWork.setNumWork(kinder.getValue("P_23_6").trim());
					rdvWork.setNameWork(kinder.getValue("P_24_6").trim());
					rdvWork.setSumm(kinder.getValue("P_27_6").trim());
					listRWorks.add(rdvWork);
				}
			}
		}, null);

		binder.handleTable("table10", "row", new RowHandler<Object>() {
			public void handleRow(DataBinder kinder, int rowNum, Object obj) throws InternalException
			{
				if(kinder.getValue("P_23_7").length() > 0) {
					RDVWork rdvWork = new RDVWork();
					rdvWork.setNumWork(kinder.getValue("P_23_7").trim());
					rdvWork.setNameWork(kinder.getValue("P_24_7").trim());
					rdvWork.setSumm(kinder.getValue("P_27_7").trim());
					listRWorks.add(rdvWork);
				}
			}
		}, null);

		binder.handleTable("table11", "row", new RowHandler<Object>() {
			public void handleRow(DataBinder kinder, int rowNum, Object obj) throws InternalException
			{
				if(kinder.getValue("P_23_8").length() > 0) {
					RDVWork rdvWork = new RDVWork();
					rdvWork.setNumWork(kinder.getValue("P_23_8").trim());
					rdvWork.setNameWork(kinder.getValue("P_24_8").trim());
					rdvWork.setSumm(kinder.getValue("P_27_8").trim());
					listRWorks.add(rdvWork);
				}
			}
		}, null);

		binder.handleTable("table12", "row", new RowHandler<Object>() {
			public void handleRow(DataBinder kinder, int rowNum, Object obj) throws InternalException
			{
				if(kinder.getValue("P_23_9").length() > 0) {
					RDVWork rdvWork = new RDVWork();
					rdvWork.setNumWork(kinder.getValue("P_23_9").trim());
					rdvWork.setNameWork(kinder.getValue("P_24_9").trim());
					rdvWork.setSumm(kinder.getValue("P_27_9").trim());
					listRWorks.add(rdvWork);
				}
			}
		}, null);

		binder.handleTable("table13", "row", new RowHandler<Object>() {
			public void handleRow(DataBinder kinder, int rowNum, Object obj) throws InternalException
			{
				if(kinder.getValue("P_23_10").length() > 0) {
					RDVWork rdvWork = new RDVWork();
					rdvWork.setNumWork(kinder.getValue("P_23_10").trim());
					rdvWork.setNameWork(kinder.getValue("P_24_10").trim());
					rdvWork.setSumm(kinder.getValue("P_27_10").trim());
					listRWorks.add(rdvWork);
				}
			}
		}, null);
		
		binder.handleTable("table1", "row", new RowHandler<Object>() {
			public void handleRow(DataBinder kinder, int rowNum, Object obj) throws InternalException
			{
				if(kinder.getValue("P_30").length() > 0) {
					RDVWork rdvWork = new RDVWork();
					rdvWork.setNameWork(kinder.getValue("P_28").trim());
					rdvWork.setSumm(kinder.getValue("P_30").trim());
					listRWorks.add(rdvWork);
				}
			}
		}, null);

		return listRWorks;
	}
	
	private class RDVWork  {
		
		private String numWork;
		private String nameWork;
		private String summ;
		
		public String getNumWork() {
			return numWork;
		}
		public void setNumWork(String numWork) {
			this.numWork = numWork;
		}
		public String getNameWork() {
			return nameWork;
		}
		public void setNameWork(String nameWork) {
			this.nameWork = nameWork;
		}
		public String getSumm() {
			return summ;
		}
		public void setSumm(String summ) {
			this.summ = summ;
		}
		
	}
	
	private void addInPackInfo(Long id, String otc_code) {
		String updtpackinfo = "insert into snt.packinfo (ID, OBJECT_KOD, REFER_ID, CODE_TEXT, REFER_TEXT) values (?,?,?,?,?)";
		if (otc_code.length() > 0) {
			try {
				if (otc_code.contains(",")) {
					List<Object[]> otc_obj = new ArrayList<Object[]>();
					ArrayList<Integer> refer = new ArrayList<Integer>();
					String[] otc_code_arr = otc_code.split(",");
					StringBuffer otc_name_str = new StringBuffer();
					HashMap<String, Object> otc = new HashMap<String, Object>();
					for (int j = 0; j < otc_code_arr.length; j++) {
						otc.put("object_kod", Integer.parseInt(otc_code_arr[j]));
						otc_name_str
								.append((String) getNpjt()
										.queryForObject(
												"select rtrim(refer_name) from snt.dic_refer where refer_id =  (select refer from snt.dic_objects where object_kod = :object_kod)",
												otc, String.class));
						if ((j + 1) < otc_code_arr.length) {
							otc_name_str.append(",");
						}
						refer.add(getNpjt()
								.queryForInt(
										"select refer from snt.dic_objects where object_kod = :object_kod",
										otc));

					}

					for (int z = 0; z < otc_code_arr.length; z++) {
						otc_obj.add(new Object[] { id, Integer.parseInt(otc_code_arr[z]),
								refer.get(z), otc_code, otc_name_str.toString()});
					}

					sjt.batchUpdate(updtpackinfo, otc_obj);

				} else {
//					System.out.println(otc_code);
					 HashMap<String, Object> pp = new HashMap<String, Object>();
					 pp.put("docid", id);
					 pp.put("otc_code", Integer.parseInt(otc_code));
					 getNpjt().update("update snt.docstore set otc_code = :otc_code where id =:docid",
					 pp);
					
					
					try{
						List<Object[]> otc_obj = new ArrayList<Object[]>();
						ArrayList<Integer> refer = new ArrayList<Integer>();
						String otc_name_str = new String();
						HashMap<String, Object> otc = new HashMap<String, Object>();
						otc.put("object_kod", Integer.parseInt(otc_code));
						otc_name_str = (String) getNpjt()
								.queryForObject(
										"select rtrim(refer_name) from snt.dic_refer where refer_id =  (select refer from snt.dic_objects where object_kod = :object_kod)",
										otc, String.class);
						refer.add(getNpjt()
								.queryForInt(
										"select refer from snt.dic_objects where object_kod = :object_kod",
										otc));
						
						otc_obj.add(new Object[] {id, Integer.parseInt(otc_code),
								refer.get(0), otc_code.toString(), otc_name_str});
						
						sjt.batchUpdate(updtpackinfo, otc_obj);
					} catch (Exception e){
						log.error(TypeConverter.exceptionToString(e));
					}
				}
			} catch (Exception e) {
				log.error(TypeConverter.exceptionToString(e));
			}
		}
	}
	
private class ContractData implements Comparable<ContractData> {
		
		private String contrname;
		private Date contractDate;
		private String numContract;
		private String varranty_repair;
		private String refund;
		private Long time;
		private String depo;
		
		public String getContrname() {
			return contrname;
		}
		public void setContrname(String contrname) {
			this.contrname = contrname;
		}
		public Date getContractDate() {
			return contractDate;
		}
		public void setContractDate(Date contractDate) {
			this.contractDate = contractDate;
		}
		public String getNumContract() {
			return numContract;
		}
		public void setNumContract(String numContract) {
			this.numContract = numContract;
		}
		public String getVarranty_repair() {
			return varranty_repair;
		}
		public void setVarranty_repair(String varranty_repair) {
			this.varranty_repair = varranty_repair;
		}
		public String getRefund() {
			return refund;
		}
		public void setRefund(String refund) {
			this.refund = refund;
		}
		public Long getTime() {
			return time;
		}
		public void setTime(Long time) {
			this.time = time;
		}
		public String getDepo() {
			return depo;
		}
		public void setDepo(String depo) {
			this.depo = depo;
		}
		
		@Override
		public int compareTo(ContractData o) {
			long result = this.getTime() - o.getTime();
			if(result > 0) {
				return 1;
			} else if(result == 0) {
				return 0;
			} else {
				return -1;
			}

		}

	}
	
}
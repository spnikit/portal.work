package sheff.rgd.ws.Controllers.TOR;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.log4j.Logger;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import ru.aisa.rgd.etd.dao.ETDSyncServiceFacade;
import ru.aisa.rgd.etd.objects.ETDDocumentData;
import ru.aisa.rgd.ws.documents.ETDForm;
import ru.aisa.rgd.ws.exeption.InternalException;
import ru.aisa.rgd.ws.exeption.ServiceException;
import ru.aisa.rgd.ws.utility.DataBinder;
import ru.aisa.rgd.ws.utility.TypeConverter;
import sheff.rgd.ws.Abstract.AbstractAction;
import sheff.rjd.services.syncutils.SyncObj;
import sheff.rjd.services.transoil.TransService;
import sheff.rjd.ws.OCO.AfterSign.SendToEtd;

public class RDVController extends AbstractAction{

	private String parentform;
	private NamedParameterJdbcTemplate npjt;
	private SendToEtd sendtoetd;
	protected final Logger	log	= Logger.getLogger(getClass());
	private TransService sendtotransoil;  
	private ETDSyncServiceFacade etdsyncfacade;

	public ETDSyncServiceFacade getEtdsyncfacade() {
		return etdsyncfacade;
	}

	public void setEtdsyncfacade(ETDSyncServiceFacade etdsyncfacade) {
		this.etdsyncfacade = etdsyncfacade;
	}

	public TransService getSendtotransoil() {
		return sendtotransoil;
	}

	public void setSendtotransoil(TransService sendtotransoil) {
		this.sendtotransoil = sendtotransoil;
	}

	public NamedParameterJdbcTemplate getNpjt() {
		return npjt;
	}

	public SendToEtd getSendtoetd() {
		return sendtoetd;
	}

	public void setSendtoetd(SendToEtd sendtoetd) {
		this.sendtoetd = sendtoetd;
	}

	public void setNpjt(NamedParameterJdbcTemplate npjt) {
		this.npjt = npjt;
	}


	public void setParentform(String parentname) {
		this.parentform = parentname;
	}

	public String getParentform() {
		return parentform;
	}


	@Override
	public void doAfterSign(String docName, String docdata, int predid, int signNumber, long id, String certserial, int WrkId) {
		Map hm1 = new HashMap();
		hm1.put("id", id);
		int drop = getNpjt().queryForInt("select CASE when dropid is null then 0 else 1 end from snt.docstore where id = :id", hm1);
		if(drop==1||signNumber==3){

			try {
				sendtoetd.SendToEtdMessage(id, docdata, parentform, signNumber, drop,false);
				sendtotransoil.SendSigntoTransoil(id, signNumber, drop, predid);


				/*else if (drop ==1)
				sendtoetd.PackDrop(id, docName);*/
			} catch (Exception e) {
				log.error(TypeConverter.exceptionToString(e));

			} 
			if (drop==0)
				try {
					sendtoetd.PackUpdate(id);
				} catch (Exception e) {
					log.error(TypeConverter.exceptionToString(e));
				}
		}

	}

	@Override
	public void doAfterSave(String docName, String docdata, int predid, int signNumber, String CertID, long id, int WrkId) throws Exception {


	}

	@Override
	public void doAfterSync(String formname, SyncObj syncobj, String sql, int signum) {
		etdsyncfacade.getWorkerWithorderNull(syncobj);
		etdsyncfacade.insertDocstore(sql, syncobj);
		etdsyncfacade.updateDSF(syncobj);

		etdsyncfacade.getWorkerWithorder(syncobj);
		etdsyncfacade.updateDSF(syncobj);

		String GET_PRICE_CHECK_SQL = "select pricecheck from snt.pred where id =(select case when headid is null then id else headid end from snt.pred where id = :predid)";
		int predid = syncobj.getPredid();
		int pricecheck;
		Map <String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("predid", predid);	
		try{
			pricecheck = getNpjt().queryForInt(GET_PRICE_CHECK_SQL, paramMap);
		}catch(Exception e){
			
			System.out.println(e);
			pricecheck=0;
		}
	//	System.out.println("RdvController");
		if(pricecheck!=0){
			String INSERT_RDV_DATA = "INSERT INTO SNT.CHANGES (ID, TYPEID, ID_PAK, DIFF_PRICE_DATA, EMPTY_DATA, STATUS_COLOR, ETDID) VALUES (COALESCE((select max(id)+1 from SNT.CHANGES),0), :typeid, :id_pak, :diffDocData, :emptyData, :status_color, :etdid)";
			//String GET_COUNT_MX_BY_PACKAGE = "SELECT COUNT(*) FROM SNT.CHANGES WHERE ID_PAK = :id_pak and typeid = (select id from snt.doctype where name in ('МХ-1'))";
			String GET_STATUS_COLOR_MX_BY_PACKAGE = "SELECT STATUS_COLOR FROM SNT.CHANGES WHERE ID_PAK = :id_pak  and typeid in (select id from snt.doctype where name in ('МХ-1'))";
			String INSERT_COLOR_PACKAGE = "INSERT INTO SNT.COLORPACKAGE (ID, ID_PAK, COLOR) VALUES (COALESCE((select max(id)+1 from SNT.COLORPACKAGE),0), :id_pak, :color)";
			String GET_NAME_BY_CODE ="select service_name from snt.services where service_code in (:differentPrice)";
			String docdata = syncobj.getDocdata();
			String diffDocData = "";
			String emptyData = "";
			int statusColor = 0;
			List<List<String>> resultListInList = resultDocumentData(docdata, predid);
			if(!resultListInList.get(0).isEmpty()){
				MapSqlParameterSource parameters = new MapSqlParameterSource();
				parameters.addValue("differentPrice", resultListInList.get(0));
				List<String> nameByCode = new ArrayList<String>();
				try{
					nameByCode = getNpjt().queryForList(GET_NAME_BY_CODE, parameters, String.class);
				}catch(Exception e){
					log.error(TypeConverter.exceptionToString(e));
				}
				for(int i =0; i<nameByCode.size();i++){
					if(i!=nameByCode.size()-1){
						diffDocData+=nameByCode.get(i)+";";
					}else{
						diffDocData+=nameByCode.get(i);
					}
				}
			}
			if(!resultListInList.get(1).isEmpty()){
				for(int i =0; i<resultListInList.get(1).size();i++){
					if(i!=resultListInList.get(0).size()-1){
						emptyData+=resultListInList.get(1).get(i)+";";
					}else{
						emptyData+=resultListInList.get(1).get(i);
					}
				}
			}
			if(diffDocData!=""){
				statusColor = 2;
			}else if(emptyData!=""){
				statusColor = 4;
			}else{
				statusColor =1;
			}

			//Map<String, Object> paramMap = new HashMap<String, Object>();
			paramMap.put("typeid", syncobj.getTypeid());
			paramMap.put("id_pak", syncobj.getId_pak());
			paramMap.put("diffDocData", diffDocData);
			paramMap.put("emptyData", emptyData);
			paramMap.put("status_color", statusColor);
			paramMap.put("etdid", syncobj.getEtdid());

			getNpjt().update(INSERT_RDV_DATA, paramMap);
			paramMap.put("color", statusColor);
			getNpjt().update(INSERT_COLOR_PACKAGE, paramMap);
		}
	}

	private Map<String, Object> getReferenceData(String sql, int predid){
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("predid", predid);
		Map<String, Object> map = (Map) getNpjt().query(sql, paramMap,new ResultSetExtractor() {
			public Object extractData(ResultSet rs) throws SQLException {
				Map map = new HashMap();
				while (rs.next()) {
					String item = /*Integer.toString(*/rs.getString("service_code");//);
					Object price = rs.getDouble("services_price");
					map.put(item, price);
				}
				return map;
			};
		});

		return map;
	}
	private List<List<String>> resultDocumentData(String str, int predid){
		
		//TODO predid
		String GET_REFERENCE_DATA_RDV = "select s.service_code, s.services_price from snt.services as s where s.type_id = (select id from snt.doctype where name = 'РДВ') and pred_id = (select case when headid is null then id else headid end from snt.pred where id = :predid)";
		Map<String, Double> resultMap = new HashMap<String, Double>();
		List<List<String>> listAllListDifferent = null;	
		resultMap = parseDocData(str);
		Map<String, Object> referenceDataMap = getReferenceData(GET_REFERENCE_DATA_RDV, predid);
		listAllListDifferent = compareMap(referenceDataMap,resultMap);
		return listAllListDifferent;
	}
	private List<List<String>> compareMap(Map<String, Object> referenceDataMap, Map<String, Double> documentMap){
		List<List<String>> resultList = new ArrayList<List<String>>();
		List<String> differenPrice = new ArrayList<String>();
		List<String> emptyDirectory = new ArrayList<String>();
		for(String strDocument : documentMap.keySet()){
			if(referenceDataMap.get(strDocument)!=null){
				if(!referenceDataMap.get(strDocument).equals(documentMap.get(strDocument))){
					differenPrice.add(strDocument);
				}
			}else{
				emptyDirectory.add(strDocument);
			}
		}
		resultList.add(0,differenPrice);
		resultList.add(1,emptyDirectory);
		return resultList;
	}
	private Map<String, Double> parseDocData(String dataString/*Element eElement*/){
		Map<String, Double> resultMap = new HashMap<String, Double>();
		InputStream stream = new ByteArrayInputStream(dataString.getBytes());

		ETDForm form = null;
		try {
			form = new ETDForm(stream);
			DataBinder binder = form.getBinder();
			binder.setRootElement("data");
			for(int i = 4; i<14;i++){
				binder.setRootElement("table"+i);
				NodeList nodeList = binder.getNodes("row");
				for(int temp = 0; temp<nodeList.getLength(); temp++){
					Element el = (Element) nodeList.item(temp);
					binder.setRootElement(el);
					String rdvCode = binder.getValue("P_23_"+(i-3)).trim();
					String rdvPrice = binder.getValue("P_26_"+(i-3)).trim();
					if(!rdvCode.equals("")){
						try{
							//int code = Integer.parseInt(rdvCode);
							double price = Double.parseDouble(rdvPrice);
							resultMap.put(rdvCode,price);
						}catch(NumberFormatException e){
							log.error(TypeConverter.exceptionToString(e));
							//throw new IllegalArgumentException("string not parse double");
						}catch(NullPointerException e){
							log.error(TypeConverter.exceptionToString(e));
							//throw new NullPointerException("You gave null in parseDouble");
						}
					}	
				}
				binder.resetRootElement();
				binder.setRootElement("data");
			}
		} catch (ServiceException e1) {
			log.error(TypeConverter.exceptionToString(e1));
		} catch (IOException e1) {
			log.error(TypeConverter.exceptionToString(e1));
		} catch (InternalException e) {
			log.error(TypeConverter.exceptionToString(e));
		}
		return resultMap;

	}
	public ETDDocumentData EditBeforeOpen(String docName, ETDDocumentData doc) {
		// TODO Auto-generated method stub
		return doc;
	}

}

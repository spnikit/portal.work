package ru.aisa.etdportal.controllers;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.ParameterizedRowMapper;
import ru.aisa.rgd.ws.utility.TypeConverter;

public class GetListChangesController extends AbstractPortalSimpleController{

	static final private String GET_NAME_DOCUMENT_BY_PACKADGE_DOCS = "select trim(name) from snt.doctype where id in (select typeid from snt.docstore where id in (select id from snt.docstore where etdid in (select etdid from snt.packages where id_pak = :id_pak)))";
	static final private String GET_DATA_CHANGE_RDV = "select etdid, diff_price_data, empty_data from snt.changes where id_pak = :id_pak and typeid in (select id from snt.doctype where name in ('РДВ'))";
	static final private String GET_DATA_CHANGE_MX = "select etdid, diff_price_data, empty_data from snt.changes where id_pak = :id_pak and typeid in (select id from snt.doctype where name in ('МХ-1'))";
	public GetListChangesController() throws JSONException{
		super();
	}
	
	@Override
	public JSONArray get(HttpServletRequest request) {
		
		JSONArray jsonArray = new JSONArray();
		JSONObject jsonObj = new JSONObject();
		String idpak = request.getParameter("idpak");
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("id_pak",idpak);
		List<String> nameDocPackage = getNpjt().queryForList(GET_NAME_DOCUMENT_BY_PACKADGE_DOCS, paramMap, String.class);
		if(nameDocPackage.contains("РДВ")/* && nameDocPackage.contains("МХ-1")*/){
			DataObject dataRdv = null;
			try{
				dataRdv = getNpjt().queryForObject(GET_DATA_CHANGE_RDV, paramMap, new RowMapper<DataObject>() {	
					public DataObject mapRow(ResultSet rs, int rowNum) throws SQLException {
							DataObject dataObj = new DataObject();
							dataObj.setEtdid(rs.getInt("ETDID"));
							dataObj.setDifferentPrice(rs.getString("DIFF_PRICE_DATA"));
							dataObj.setEmptyData(rs.getString("EMPTY_DATA"));
							return dataObj;
						}
					});
			}catch(Exception e){
				jsonArray = null;
				log.error(TypeConverter.exceptionToString(e));
			}
			if(dataRdv!=null){
				String[] strArrayDiff;
				String[] strArrayEmpty;
				if(dataRdv.getDifferentPrice()!=null){
					strArrayDiff = dataRdv.getDifferentPrice().split(";");
				}else{
					strArrayDiff = new String[0];
				}
				if(dataRdv.getEmptyData()!=null){
					strArrayEmpty = dataRdv.getEmptyData().split(";");
				}else{
					strArrayEmpty = new String[0];
				}
				if(strArrayEmpty.length==1 && strArrayDiff.length==1 && strArrayDiff[0]=="" && strArrayEmpty[0]==""){
					jsonArray = null;
				}else{
					List<String> nameRdvList = new ArrayList<String>(Arrays.asList(strArrayDiff));
					List<String> codeRdvList = new ArrayList<String>(Arrays.asList(strArrayEmpty));	
	
					try {
						jsonObj.put("name", nameRdvList);
						jsonObj.put("code", codeRdvList);
						jsonObj.put("etdid", dataRdv.getEtdid());
					} catch (JSONException e) {
						log.error(TypeConverter.exceptionToString(e));
					}			
				}
				jsonArray.put(jsonObj);
			}

			JSONArray jsonArr = new JSONArray();
			JSONObject jsonMxObj = new JSONObject();
			List<DataObject> dataMxList = getNpjt().query(
					GET_DATA_CHANGE_MX, paramMap, new ParameterizedRowMapper<DataObject>() {
						public DataObject mapRow(ResultSet rs, int n)
								throws SQLException {
							DataObject obj = new DataObject();
							obj.setEtdid(rs.getInt("etdid"));
							obj.setDifferentPrice(rs.getString("DIFF_PRICE_DATA"));
							obj.setEmptyData(rs.getString("EMPTY_DATA"));
							return obj;
						}
					});

			if(dataMxList!=null){
				for(DataObject dataObject : dataMxList){
					if(dataObject!=null){
						String[] strArrayDiffMx;
						String[] strArrayEmptyMx;
						if(dataObject.getDifferentPrice()!=null){
							strArrayDiffMx = dataObject.getDifferentPrice().split(";");
						}else{
							strArrayDiffMx = new String[0];
						}
						if(dataObject.getEmptyData()!=null){
							strArrayEmptyMx = dataObject.getEmptyData().split(";");
						}else{
							strArrayEmptyMx = new String[0];
						}
						List<String> nameMxList = new ArrayList<String>(Arrays.asList(strArrayDiffMx));
						List<String> codeMxList = new ArrayList<String>(Arrays.asList(strArrayEmptyMx));				
						try {
							jsonMxObj.put("name", nameMxList);
							jsonMxObj.put("code", codeMxList);
							jsonMxObj.put("etdid", dataObject.getEtdid());
						} catch (JSONException e) {
							log.error(TypeConverter.exceptionToString(e));
						}
					}
					jsonArr.put(jsonMxObj);
				}
			}
			JSONObject mxObject = new JSONObject();
			try {
				mxObject.put("mxArray", jsonArr);
			} catch (JSONException e) {
				log.error(TypeConverter.exceptionToString(e));
			}
			jsonArray.put(mxObject);
		}
		 return jsonArray;
	 }
	
	class DataObject{
			int etdid;
			String differentPrice;
			String emptyData;
			public int getEtdid() {
				return etdid;
			}
			public void setEtdid(int etdid) {
				this.etdid = etdid;
			}
			public String getDifferentPrice() {
				return differentPrice;
			}
			public void setDifferentPrice(String differentPrice) {
				this.differentPrice = differentPrice;
			}
			public String getEmptyData() {
				return emptyData;
			}
			public void setEmptyData(String emptyData) {
				this.emptyData = emptyData;
			}
	}
}



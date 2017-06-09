package ru.aisa.etdportal.controllers;

import java.sql.Blob;
import java.sql.ResultSet;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.jdbc.support.rowset.ResultSetWrappingSqlRowSet;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import ru.aisa.rgd.ws.documents.ETDForm;
import ru.aisa.rgd.ws.utility.DataBinder;
import ru.aisa.rgd.ws.utility.TypeConverter;


public class CheckActTmcController  extends AbstractPortalSimpleController {

	private static Logger log = Logger.getLogger(CheckActTmcController.class);
	
	public CheckActTmcController() throws JSONException {
		super();
	}

	@Override
	public JSONArray get(HttpServletRequest request) {
		final JSONArray response = new JSONArray();
		HashMap<String, Object> map = new HashMap<String, Object>();
		String id = request.getParameter("id");
		String isSend = "0";

		map.put("id", id);
		try { 
			ETDForm packageForm = null;
			ETDForm MX3Form = null;
			Integer torgType = null;
			Integer signlvl = null;
			ResultSetWrappingSqlRowSet srs =  (ResultSetWrappingSqlRowSet)getNpjt().
					queryForRowSet("select bldoc, signlvl, (select rtrim(name) from snt.doctype where id = typeid) name, "
							+ " (select torgtype from snt.pred where id = predid) torgtype "
							+ " from snt.docstore where (etdid in ("
							+ "select etdid from snt.packages where id_pak = "
							+ "(select id_pak from snt.docstore where id = :id)) and  typeid = ("
							+ "select id from snt.doctype where rtrim(name) = 'МХ-3')) or "
							+ "  (typeid = (select id from snt.doctype "
							+ " where rtrim(name) = 'Пакет документов') "
							+ " and id_pak = (select id_pak from snt.docstore where id = :id))",map);
			ResultSet resultSet = srs.getResultSet();
			while(resultSet.next()) {
				Blob blob = resultSet.getBlob("bldoc");
				if(resultSet.getString("name").equals("Пакет документов")) {
					packageForm = ETDForm.createFromArchive(blob.getBytes(1, (int)blob.length()));
				}
				if(resultSet.getString("name").equals("МХ-3")) {
					MX3Form = ETDForm.createFromArchive(blob.getBytes(1, (int)blob.length()));
					signlvl = resultSet.getInt("signlvl");
					torgType = resultSet.getInt("torgtype");
				}

			}
			if(MX3Form != null) {
				DataBinder packageBinder = packageForm.getBinder();
				String P_23 = packageBinder.getValue("P_23");
				if(P_23.equals("02") && (signlvl.equals(2) || signlvl.equals(3)) 
						&& (torgType.equals(1) || torgType.equals(2) || torgType.equals(3))) {
					isSend = "1";
				}
				if(P_23.equals("06") && (signlvl.equals(2) || signlvl.equals(3))) {
					isSend = "1";
				}
			}
			JSONObject js = new JSONObject();
			js.put("isSend", isSend);
			response.put(js);
		} catch(Exception e) {
			log.error(TypeConverter.exceptionToString(e));
//			e.printStackTrace();
		}

		return response;
	}
}
		
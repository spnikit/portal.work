package ru.aisa.etdportal.controllers;


import java.sql.Types;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.sql.DataSource;
import org.json.JSONArray;
import org.json.JSONException;
import org.springframework.jdbc.core.SqlOutParameter;
import org.springframework.jdbc.core.SqlParameter;
import sheff.rjd.utils.MyStoredProc;


public class Num extends AbstractPortalSimpleController {

	public Num() throws JSONException {
		super();
	}

	 private DataSource ds;
	
	public DataSource getDs() {
		return ds;
	}

	public void setDs(DataSource ds) {
		this.ds = ds;
	}

	@Override
	public JSONArray get(HttpServletRequest request){
		
//		System.out.println("deop");
		final JSONArray response = new JSONArray();
		HashMap<String, Object> pp = new HashMap<String, Object>();
			
		Map output = callNumProcedure(18, 1);
		System.out.println(output.get("DocNum"));
		return response;
		
	}
	
	 private Map callNumProcedure(int predid, int typeid){	
			MyStoredProc sproc = new MyStoredProc(getDs());
			sproc.setSql("SNT.GETDOC_M_NUM");
			sproc.declareParameter(new SqlOutParameter("out", Types.CHAR));
			sproc.declareParameter(new SqlParameter("rw_station_id", Types.INTEGER));
			sproc.declareParameter(new SqlParameter("sFormID", Types.INTEGER));
			sproc.declareParameter(new SqlOutParameter("DocNum", Types.INTEGER));
			sproc.compile();
			Map input = new HashMap();
			input.put("rw_station_id", predid);
			input.put("sFormID", typeid);
			System.out.println(input);
			return sproc.execute(input);
		}
}
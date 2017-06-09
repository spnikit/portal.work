package ru.aisa.etdportal.controllers;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.json.JSONArray;
import org.json.JSONException;
import org.springframework.jdbc.core.simple.ParameterizedRowMapper;
import sheff.rjd.services.syncutils.SyncObj;
import ru.aisa.rgd.ws.documents.ETDForm;
import ru.aisa.rgd.ws.utility.DataBinder;

public class updtpack extends AbstractPortalSimpleController {

	public updtpack() throws JSONException {
		super();
	}

	private static final String sql  = "select bldoc, id_pak, vagnum from snt.docstore where typeid = (select id from snt.doctype where name ='Пакет документов') and visible =1 and crdate between '2013-10-01' and '2013-10-19' with ur";
	private static final String updt = "insert into snt.packages (id_pak, vagnum, etdid) values (?,?,?)";
	@Override
	public JSONArray get(HttpServletRequest request){
		
		final JSONArray response = new JSONArray();
				
			try{
			
				

@SuppressWarnings("unchecked")
List<SyncObj> list = getNpjt().query(sql, new HashMap(), new ParameterizedRowMapper<SyncObj>() {
	public SyncObj mapRow(ResultSet rs, int n) throws SQLException {
		SyncObj obj = new SyncObj();
		obj.setBldoc(rs.getBytes("bldoc"));
		obj.setId_pak(rs.getString("id_pak"));
		obj.setVagnum(rs.getString("vagnum"));
		return obj;
		
	}
});
				
List<Object[]> aa = new ArrayList<Object[]>();

 
				int i =0;
				while (i<list.size()){
				 ETDForm form=ETDForm.createFromArchive(list.get(i).getBldoc());
				 DataBinder kinder=form.getBinder();
				 
				 String[] etdid = kinder.getValuesAsArray("P_3a");
				 for(int j=0; j<etdid.length; j++){
					 aa.add(new Object[]{list.get(i).getId_pak(), list.get(i).getVagnum(), Long.valueOf(etdid[j])});
				 }
				  
				i++;
				}
				
				
				 int z = 0;
				 while (z<aa.size()){

				z++;
				 }
				 
				 
				 getSjt().batchUpdate(updt, aa);
				 response.put(true);
				
			} catch (Exception e) {
				
				e.printStackTrace();
				response.put(false);
				}
	
		
		return response;
	}
	
	
}
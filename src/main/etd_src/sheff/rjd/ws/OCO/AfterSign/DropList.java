package sheff.rjd.ws.OCO.AfterSign;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;


public class DropList {

	NamedParameterJdbcTemplate npjt;
	
	
	
	
	public NamedParameterJdbcTemplate getNpjt() {
		return npjt;
	}




	public void setNpjt(NamedParameterJdbcTemplate npjt) {
		this.npjt = npjt;
	}




	public Map getDropList(long docid){
	
		Map <String, Object> dl = new HashMap<String, Object>();
		dl.put("id", docid);
		
		
		try{
		
		List<Map<String, Object>> declinelist = npjt.queryForList("select droptxt, dropid, dropwrkid, droppredid from snt.docstore where id =:id", dl);
		
		dl.put("droptxt", declinelist.get(0).get("DROPTXT").toString());
		dl.put("dropid", Integer.valueOf(declinelist.get(0).get("DROPID").toString()));
		dl.put("dropwrkid", Integer.valueOf(declinelist.get(0).get("DROPWRKID").toString()));
		dl.put("droppredid", Integer.valueOf(declinelist.get(0).get("DROPPREDID").toString()));	
		} catch (Exception e){
			e.printStackTrace();
		}
		/*System.out.println(declinelist.get(0).get("DROPTXT").toString());
		System.out.println(Integer.valueOf(declinelist.get(0).get("DROPID").toString()));
		System.out.println(Integer.valueOf(declinelist.get(0).get("DROPWRKID").toString()));
		System.out.println(Integer.valueOf(declinelist.get(0).get("DROPPREDID").toString()));
		*/return dl;
		
	}
	
	
	public void DropDocs(Map dl){
		
		npjt.update("update snt.docstore set droptime = current timestamp, droptxt = :droptxt, " +
				"dropid = :dropid, " +
				"dropwrkid = :dropwrkid, droppredid = :droppredid  where id = :DOCID ", dl);
		
		
		
	}
	
	
}

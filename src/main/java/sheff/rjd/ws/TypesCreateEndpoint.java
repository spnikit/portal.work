package sheff.rjd.ws;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.log4j.Logger;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.ParameterizedRowMapper;
import org.springframework.oxm.Marshaller;
import org.springframework.ws.server.endpoint.AbstractMarshallingPayloadEndpoint;

import ru.aisa.edt.GetTypesDocument;
import ru.aisa.edt.GetTypesDocument.GetTypes.Type;

public class TypesCreateEndpoint extends AbstractMarshallingPayloadEndpoint {
	private NamedParameterJdbcTemplate npjt;
	//private String reports;
	private ArrayList<String> reports;
	
	 private static Logger	log	= Logger.getLogger(TypesCreateEndpoint.class);
    
	public TypesCreateEndpoint(Marshaller marshaller) {
		super(marshaller);
	}
	protected Object invokeInternal(Object obj) {
		try {
	    final GetTypesDocument td = (GetTypesDocument)obj;
		Map pp = new HashMap();
		pp.put("wrkid", td.getGetTypes().getWrkid());
		Type tmp = td.getGetTypes().addNewType();
		tmp.setId(-1);
		tmp.setTemplate(new byte[1]);
		tmp.setName("-");
		List result = getNpjt().query("SELECT id, rtrim(name) as name FROM letd.doctype where id in (select dtid from letd.doctypeacc where wrkid = :wrkid and cnew = 1) order by name",pp, new ParameterizedRowMapper<Object>() {

			public Object mapRow(ResultSet rs, int numrow) {
				try{
				Type tmp = td.getGetTypes().addNewType();
				tmp.setId(rs.getInt("id"));
				tmp.setName(rs.getString("name"));
				tmp.setTemplate(new byte[1]);
				}
				catch (Exception e) {
					StringWriter outError = new StringWriter();
					   PrintWriter errorWriter = new PrintWriter( outError );
					   e.printStackTrace(errorWriter);
					   log.error(outError.toString());
				}
				return null;
			}
			
		});
		tmp = td.getGetTypes().addNewType();
		tmp.setId(-2);
		tmp.setTemplate(new byte[1]);
		tmp.setName("-");
		pp.put("names", getReports());
		result = getNpjt().query("SELECT id, rtrim(name) as name FROM letd.doctype where id in (select dtid from letd.doctypeacc where wrkid = :wrkid and (cview = 1 OR cedit = 1 OR cnew = 1)) and name in ( :names ) order by name",pp, new ParameterizedRowMapper<Object>() {
		//result = getNpjt().query("SELECT id, rtrim(name) as name FROM letd.doctype where id in (select dtid from letd.doctypeacc where wrkid = :wrkid and (cview = 1 OR cedit = 1 OR cnew = 1)) and name in ( "+getReports()+" ) order by name",pp, new ParameterizedRowMapper<Object>() {

			public Object mapRow(ResultSet rs, int numrow) {
				try{
				Type tmp = td.getGetTypes().addNewType();
				tmp.setId(rs.getInt("id"));
				tmp.setName(rs.getString("name"));
				tmp.setTemplate(new byte[1]);
				}
				catch (Exception e) {
					StringWriter outError = new StringWriter();
					   PrintWriter errorWriter = new PrintWriter( outError );
					   e.printStackTrace(errorWriter);
					   log.error(outError.toString());
				}
				return null;
			}
			
		});
		return td;
		}
		catch (Exception e) {
			StringWriter outError = new StringWriter();
			   PrintWriter errorWriter = new PrintWriter( outError );
			   e.printStackTrace(errorWriter);
			   log.error(outError.toString());
			return null;
		}
	}
	public NamedParameterJdbcTemplate getNpjt() {
		return npjt;
	}
	public void setNpjt(NamedParameterJdbcTemplate npjt) {
		this.npjt = npjt;
	}
	public ArrayList<String> getReports() {
		return reports;
	}
	public void setReports(ArrayList<String> reports) {
		this.reports = reports;
	}
	
	

}

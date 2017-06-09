package sheff.rjd.ws.OCO;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.ParameterizedRowMapper;
import org.springframework.oxm.Marshaller;
import org.springframework.ws.server.endpoint.AbstractMarshallingPayloadEndpoint;

import ru.aisa.edt.GetTypesDocument;
import ru.aisa.edt.GetTypesDocument.GetTypes.Type;
import sheff.rjd.utils.SecurityManager;

public class TypesEndpointOCO extends AbstractMarshallingPayloadEndpoint {
	private NamedParameterJdbcTemplate npjt;
	private SecurityManager securityManager;
	 private static Logger	log	= Logger.getLogger(TypesEndpointOCO.class);
    
	 public SecurityManager getSecurityManager() {
			return securityManager;
		}



		public void setSecurityManager(SecurityManager securityManager) {
			this.securityManager = securityManager;
		}
	public TypesEndpointOCO(Marshaller marshaller) {
		super(marshaller);
	}
	
	protected Object invokeInternal(Object obj) {
	 
		try{
			System.out.println("typesendp");
			final GetTypesDocument td = (GetTypesDocument)obj;
			Map pp = new HashMap();
			pp.put("wrkid", td.getGetTypes().getWrkid());
			Type tmp = td.getGetTypes().addNewType();
			tmp.setId(-1);
			tmp.setName("-");
			List result = getNpjt().query("SELECT id, rtrim(name) as name FROM SNT.doctype where id in (select dtid from SNT.doctypeacc where wrkid = :wrkid and cnew = 1) and ptype = 0 order by name",pp, new ParameterizedRowMapper<Object>() {

				public Object mapRow(ResultSet rs, int numrow) {
					try{
					Type tmp = td.getGetTypes().addNewType();
					tmp.setId(rs.getInt("id"));
					tmp.setName(rs.getString("name"));
					
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
			//tmp.setTemplate(new byte[1]);
			tmp.setName("separator");
			result = getNpjt().query("SELECT id, rtrim(name) as name FROM SNT.doctype where id in (select dtid from SNT.doctypeacc where wrkid = :wrkid and (cview = 1 OR cedit = 1 OR cnew = 1)) and ptype = 1  order by name",pp, new ParameterizedRowMapper<Object>() {

				public Object mapRow(ResultSet rs, int numrow) {
					try{
					Type tmp = td.getGetTypes().addNewType();
					tmp.setId(-2);
					tmp.setName(rs.getString("name"));
				//	tmp.setTemplate(new byte[1]);
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
			//tmp.setTemplate(new byte[1]);
			tmp.setName("separator_types");
			result = getNpjt().query("SELECT id, rtrim(name) as name FROM SNT.doctype where id in (select dtid from SNT.doctypeacc where wrkid = :wrkid and (cview = 1 OR cedit = 1)) and ptype=0 order by name",pp, new ParameterizedRowMapper<Object>() {

				public Object mapRow(ResultSet rs, int numrow) {
					try{
					Type tmp = td.getGetTypes().addNewType();
					tmp.setId(rs.getInt("id"));
					tmp.setName(rs.getString("name"));
				//	tmp.setTemplate(new byte[1]);
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
			
		//System.out.println("td "+td);	
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
	
	

}

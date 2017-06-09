package ru.aisa.etdadmin.cnsimappers;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.log4j.Logger;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowCallbackHandler;

import ru.aisa.etdadmin.SqlForCnsi;



	
	
	public class ObjOsnInfRowMapper implements RowCallbackHandler {
		private JdbcTemplate jt;
		private static Logger log = Logger.getLogger(ObjOsnInfRowMapper.class);
		public ObjOsnInfRowMapper(JdbcTemplate npjtt){
			jt = npjtt;
		}
		public void processRow(ResultSet rs) throws SQLException {
			try{
				String tmp = SqlForCnsi.sqlnew[14]
				                               
				.replaceAll(":OBJ_OSN_ID", rs.getString("OBJ_OSN_ID"))
				.replaceAll(":V_OBJ_ID",(rs.getObject("V_OBJ_ID") == null ? " cast(null as int) " : rs.getString("V_OBJ_ID")))
				.replaceAll(":OKATO_ID", (rs.getObject("OKATO_ID") == null ? " cast(null as int) " : rs.getString("OKATO_ID")))
				.replaceAll(":DOP_RAZM", (rs.getString("DOP_RAZM") == null ? " " : (rs.getString("DOP_RAZM").trim().length()==0 ? " ":rs.getString("DOP_RAZM").trim())))
				.replaceAll(":VNAME", rs.getString("VNAME").trim())
				.replaceAll(":NAME", rs.getString("NAME").trim())
				.replaceAll(":OBJ_OSN_N", (rs.getObject("OSN_OBJ_N") == null ? " cast(null as int) " : rs.getString("OSN_OBJ_N")))
				.replaceAll(":OBJ_OSN_K", (rs.getObject("OSN_OBJ_K") == null ? " cast(null as int) " : rs.getString("OSN_OBJ_K")))
				.replaceAll(":ACTIV",  (rs.getString("COR_TIP").equals("D") ? "N" : "Y") ); 
				jt.update(tmp);
			
			}
			catch (Exception e) {
				StringWriter outError = new StringWriter();
				PrintWriter errorWriter = new PrintWriter(outError);
				e.printStackTrace(errorWriter);
				log.error("cnsisync "+rs.getString("OBJ_OSN_ID"));
				log.error("cnsisync "+outError.toString());
			}
		}
	}
	
	

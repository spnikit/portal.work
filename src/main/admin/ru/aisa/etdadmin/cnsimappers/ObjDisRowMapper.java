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




	
	
	public class ObjDisRowMapper implements RowCallbackHandler {
		private JdbcTemplate jt;
		private static Logger log = Logger.getLogger(ObjDisRowMapper.class);
		public ObjDisRowMapper(JdbcTemplate npjtt){
			jt = npjtt;
		}
		public void processRow(ResultSet rs) throws SQLException {
					try{
						String tmp = SqlForCnsi.sqlnew[17]
						.replaceAll(":PRED_ID", String.valueOf(rs.getInt("PRED_ID")+20000000))
						.replaceAll(":OBJ_OSN_ID", rs.getString("OBJ_OSN_ID"))
						.replaceAll(":PRIM", (rs.getString("PRIM") == null ? " " : (rs.getString("PRIM").trim().length()==0 ? " ":rs.getString("PRIM").trim())))
						.replaceAll(":ACTIV", (rs.getString("COR_TIP").equals("D") ? "N" : "Y") );
						
					 jt.update(tmp);
				
			}
			
			catch (Exception e) {
				StringWriter outError = new StringWriter();
				PrintWriter errorWriter = new PrintWriter(outError);
				e.printStackTrace(errorWriter);
				log.error("cnsisync "+outError.toString());
			}
		}
	}
	
	
	

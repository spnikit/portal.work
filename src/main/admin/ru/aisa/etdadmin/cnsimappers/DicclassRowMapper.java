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




	
	
	public class DicclassRowMapper implements RowCallbackHandler {
		private JdbcTemplate jt;
		private static Logger	log	= Logger.getLogger(DicclassRowMapper.class);
		private int rowcount = 0;
		public DicclassRowMapper(JdbcTemplate npjtt){
			jt = npjtt;
		}
		public void processRow(ResultSet rs) throws SQLException {
				try{
					String tmp = SqlForCnsi.sqlnew[18]
                    .replaceAll(":CLASS_ID", rs.getString("CLASS_ID"))
                    .replaceAll(":CLASS_NAME", (rs.getString("CLASS_NAME") == null ? " " : (rs.getString("CLASS_NAME").trim().length()==0 ? " ":rs.getString("CLASS_NAME").trim())))
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
	
	
	

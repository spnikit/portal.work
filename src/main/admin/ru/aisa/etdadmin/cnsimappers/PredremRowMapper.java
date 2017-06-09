package ru.aisa.etdadmin.cnsimappers;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.log4j.Logger;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowCallbackHandler;

import ru.aisa.etdadmin.SqlForCnsi;


	
	
	public class PredremRowMapper implements RowCallbackHandler {
		private JdbcTemplate jt;
		private static Logger log = Logger.getLogger(PredremRowMapper.class);
		
				
		
		public PredremRowMapper(JdbcTemplate npjtt){
			jt = npjtt;
			
		}
		public void processRow(ResultSet rs) throws SQLException {
			try{
						
			String tmp = SqlForCnsi.sqlnew[12]
			.replaceAll(":PRED_ID", String.valueOf(rs.getInt("PRED_ID")+20000000))
			.replaceAll(":UKP_KOD", rs.getString("UKP_KOD"))
			.replaceAll(":activ",  (rs.getString("COR_TIP").equals("D") ? "N" : "Y") );
			jt.update(tmp);
			}
			catch (Exception e) {
				StringWriter outError = new StringWriter();
				PrintWriter errorWriter = new PrintWriter(outError);
				e.printStackTrace(errorWriter);
				log.error("cnsisync "+rs.getInt("PRED_ID")+2000000);
				log.error("cnsisync "+outError.toString());
			}
			
		}
	}



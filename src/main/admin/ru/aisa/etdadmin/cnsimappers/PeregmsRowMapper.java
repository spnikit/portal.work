package ru.aisa.etdadmin.cnsimappers;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.log4j.Logger;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowCallbackHandler;

import ru.aisa.etdadmin.SqlForCnsi;




	
	public class PeregmsRowMapper implements RowCallbackHandler {
		private JdbcTemplate jt;
		
		private static Logger log = Logger.getLogger(PeregmsRowMapper.class);
		
		
		public PeregmsRowMapper(JdbcTemplate npjtt){
			jt = npjtt;
		}
		public void processRow(ResultSet rs) throws SQLException {
			try{		
				String tmp = SqlForCnsi.sqlnew[24]
				.replaceAll(":PEREG_MS_ID", rs.getString("PEREG_MS_ID"))
				.replaceAll(":UP_ID",  rs.getObject("UP_ID") == null ? " cast(null as int) " : rs.getString("UP_ID"))
				.replaceAll(":STAN1_ID",  rs.getObject("STAN1_ID") == null ? " cast(null as int) " : rs.getString("STAN1_ID"))
				.replaceAll(":STAN2_ID",  rs.getObject("STAN2_ID") == null ? " cast(null as int) " : rs.getString("STAN2_ID"))
				.replaceAll(":EXPL",  rs.getObject("EXPL") == null ? " cast(null as decimal) " : rs.getString("EXPL"))
				.replaceAll(":CHET",  rs.getObject("CHET") == null ? " cast(null as smallint) " : rs.getString("CHET"))
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
	
	
	

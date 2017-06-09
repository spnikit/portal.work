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


	
	
	public class DispuchmarshRowMapper implements RowCallbackHandler {
		private JdbcTemplate jt;
		private static Logger	log	= Logger.getLogger(DispuchmarshRowMapper.class);
		private int rowcount = 0;
		public DispuchmarshRowMapper(JdbcTemplate npjtt){
			jt = npjtt;
		}
		public void processRow(ResultSet rs) throws SQLException {
			try{
			//if (rowcount%10 == 0) log.warn(rowcount);
			if (!rs.getString("COR_TIP").equals("D")){
				String tmp = SqlForCnsi.sqlnew[10]	                         
				.replaceAll(":DISP_UCH_ID", rs.getString("DISP_UCH_ID"))			
				.replaceAll(":MARSH_NOM", rs.getString("MARSH_NOM"))			
				.replaceAll(":STAN_NOM", rs.getString("STAN_NOM"))		
				.replaceAll(":STAN_ID", rs.getString("STAN_ID"))			
				.replaceAll(":NODE_TYPE", rs.getString("NODE_TYPE").trim());			
				jt.update(tmp);
			}
			rowcount++;
			}
			catch (Exception e) {
				StringWriter outError = new StringWriter();
				PrintWriter errorWriter = new PrintWriter(outError);
				e.printStackTrace(errorWriter);
				log.error("cnsisync "+rs.getString("STAN_ID"));
				log.error("cnsisync "+outError.toString());
			}
		}
	}
	
	

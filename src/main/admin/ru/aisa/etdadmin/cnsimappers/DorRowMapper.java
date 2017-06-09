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


	
	
	public class DorRowMapper implements RowCallbackHandler {
		private JdbcTemplate jt;
		private static Logger	log	= Logger.getLogger(DorRowMapper.class);
		private int rowcount = 0;
		public DorRowMapper(JdbcTemplate npjtt){
			jt = npjtt;
		}
		public void processRow(ResultSet rs) throws SQLException {
			try{
			//if (rowcount%10 == 0) log.warn(rowcount);
			if (!rs.getString("COR_TIP").equals("D")){
				String tmp = SqlForCnsi.sqlnew[1].replaceAll(":DOR_KOD", rs.getString("DOR_KOD"))
				.replaceAll(":ADM_KOD", String.valueOf(rs.getInt("ADM_KOD")))
				.replaceAll(":NAME", rs.getString("NAME").trim())
				.replaceAll(":SNAME", rs.getString("SNAME").trim());
				jt.update(tmp);
			}
			rowcount++;
			}
			catch (Exception e) {
				StringWriter outError = new StringWriter();
				PrintWriter errorWriter = new PrintWriter(outError);
				e.printStackTrace(errorWriter);
				log.error("cnsisync "+rs.getString("DOR_KOD"));
				log.error("cnsisync "+outError.toString());
			}
		}
	}
	
	
	
	

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

	
	public class AdmjdRowMapper implements RowCallbackHandler {
		private JdbcTemplate jt;
		private static Logger	log	= Logger.getLogger(AdmjdRowMapper.class);
		private int rowcount = 0;
		
		public AdmjdRowMapper(JdbcTemplate npjtt){
			jt = npjtt;
		}
		public void processRow(ResultSet rs) throws SQLException {
			try{
			//if (rowcount%10 == 0) log.warn(rowcount);
			if (!rs.getString("COR_TIP").equals("D")){
				String tmp = SqlForCnsi.sqlnew[0].replaceAll(":ID", rs.getString("ADM_KOD").trim())
				.replaceAll(":COUNTY", rs.getString("STRAN_KOD").trim())
				.replaceAll(":NAME", rs.getString("NAME").trim())
				.replaceAll(":SNAMER", rs.getString("Sname_Rus").trim())
				.replaceAll(":SNAMEA", rs.getString("Sname_Lat").trim());
				jt.update(tmp);
			}
			rowcount++;
			}
			catch (Exception e) {
				StringWriter outError = new StringWriter();
				PrintWriter errorWriter = new PrintWriter(outError);
				e.printStackTrace(errorWriter);
				log.error("cnsisync "+rs.getString("ADM_KOD"));
				log.error("cnsisync "+outError.toString());

			}
		}
	}
	
	
	

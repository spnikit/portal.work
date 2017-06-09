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


	
	
	public class GruzRowMapper implements RowCallbackHandler {
		private JdbcTemplate jt;
		private static Logger log = Logger.getLogger(GruzRowMapper.class);
		private int rowcount = 0;
		public GruzRowMapper(JdbcTemplate npjtt){
			jt = npjtt;
		}
		public void processRow(ResultSet rs) throws SQLException {
			try{
			//if (rowcount%10 == 0) log.warn(rowcount);
			if (!rs.getString("COR_TIP").equals("D")){
				String tmp = SqlForCnsi.sqlnew[11]			
				.replaceAll(":GRUZ_ETSNG2_ID", rs.getString("GRUZ_ETSNG2_ID"))
				.replaceAll(":KOD", rs.getString("KOD").trim())
				.replaceAll(":vName", rs.getString("vName").trim())
				.replaceAll(":sName", (rs.getString("sName") == null ? " " : (rs.getString("sName").trim().length()==0 ? " ":rs.getString("sName").trim())))
				.replaceAll(":PR_ALF", (rs.getString("PR_ALF") == null ? "0" : (rs.getString("PR_ALF").trim().length()==0 ? "0":rs.getString("PR_ALF").trim())))
				.replaceAll(":iKOD_INT", rs.getString("KOD_INT"))
				.replaceAll(":TAR_CLASS", (rs.getObject("TAR_CLASS") == null ? " cast(null as smallint) " : rs.getString("TAR_CLASS")));		
				jt.update(tmp);
			}
			rowcount++;
			}
			catch (Exception e) {
				StringWriter outError = new StringWriter();
				PrintWriter errorWriter = new PrintWriter(outError);
				e.printStackTrace(errorWriter);
				log.error("cnsisync "+rs.getString("GRUZ_ETSNG2_ID"));
				log.error("cnsisync "+outError.toString());
			}
		}
	}
	
	

package ru.aisa.etdadmin.cnsimappers;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.log4j.Logger;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowCallbackHandler;

import ru.aisa.etdadmin.SqlForCnsi;


	
	
	public class ViddejRowMapper implements RowCallbackHandler {
		private JdbcTemplate jt;
		private static Logger log = Logger.getLogger(ViddejRowMapper.class);
		private int rowcount = 0;
		public ViddejRowMapper(JdbcTemplate npjtt){
			jt = npjtt;
		}
		public void processRow(ResultSet rs) throws SQLException {
			try{
		//	if (rowcount%10 == 0) log.warn(rowcount);
			if (!rs.getString("COR_TIP").equals("D")){
				String tmp = SqlForCnsi.sqlnew[8]			
				.replaceAll(":VD_ID", rs.getString("VD_ID"))
				.replaceAll(":VD_KOD", rs.getString("VD_KOD"))
				.replaceAll(":PVD_KOD", (rs.getObject("PVD_KOD") == null ? " cast(null as int) " : rs.getString("PVD_KOD")))
				.replaceAll(":Name", rs.getString("Name").trim())
				.replaceAll(":sName", (rs.getString("sName") == null ? " " : (rs.getString("sName").trim().length()==0 ? " ":rs.getString("sName").trim())));			
				jt.update(tmp);
			}
			rowcount++;
			}
			catch (Exception e) {
				StringWriter outError = new StringWriter();
				PrintWriter errorWriter = new PrintWriter(outError);
				e.printStackTrace(errorWriter);
				log.error("cnsisync "+rs.getString("VD_ID"));
				log.error("cnsisync "+outError.toString());
			}
		}
	}
	
	

package ru.aisa.etdadmin.cnsimappers;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.log4j.Logger;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowCallbackHandler;

import ru.aisa.etdadmin.SqlForCnsi;


	
	
	public class UpRowMapper implements RowCallbackHandler {
		private JdbcTemplate jt;
		private static Logger log = Logger.getLogger(UpRowMapper.class);	
		private int rowcount = 0;
		public UpRowMapper(JdbcTemplate npjtt){
			jt = npjtt;
		}
		public void processRow(ResultSet rs) throws SQLException {
			try{
		//	if (rowcount%10 == 0) log.warn(rowcount);
			if (!rs.getString("COR_TIP").equals("D")){
				String tmp = SqlForCnsi.sqlnew[4]
				.replaceAll(":up_id", rs.getString("up_id"))
				.replaceAll(":Mag_Kod", rs.getString("Mag_Kod"))   
				.replaceAll(":up_kod", rs.getString("up_kod"))   
				.replaceAll(":Name", rs.getString("Name").trim())   
				.replaceAll(":Stan1_ID", rs.getObject("Stan1_ID")==null ? " CAST(NULL AS INT) " : rs.getString("Stan1_ID")) 
				.replaceAll(":Stan2_ID", rs.getObject("Stan2_ID")==null ? " CAST(NULL AS INT) " : rs.getString("Stan2_ID"));			
				jt.update(tmp);
			}
			rowcount++;
			}
			catch (Exception e) {
				StringWriter outError = new StringWriter();
				PrintWriter errorWriter = new PrintWriter(outError);
				e.printStackTrace(errorWriter);
				log.error("cnsisync "+rs.getString("up_id"));
				log.error("cnsisync "+outError.toString());
			}
		}
	}
	
	

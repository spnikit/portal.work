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


	
	
	public class Gruz2RowMapper implements RowCallbackHandler {
		private JdbcTemplate jt;
		private static Logger log = Logger.getLogger(Gruz2RowMapper.class);
		private int rowcount = 0;
		public Gruz2RowMapper(JdbcTemplate npjtt){
			jt = npjtt;
		}
		public void processRow(ResultSet rs) throws SQLException {
			try{
			//if (rowcount%10 == 0) log.warn(rowcount);
			if (!rs.getString("COR_TIP").equals("D")  && rs.getInt("class_id") == 2157 ){
				String tmp = SqlForCnsi.sqlnew[11]			
				.replaceAll(":GRUZ_ETSNG2_ID", String.valueOf(rs.getInt("OBJECT_KOD")+1000000))
				.replaceAll(":KOD", rs.getString("OBJECT_KOD"))
				.replaceAll(":vName", rs.getString("OBJECT_VNAME").trim())
				.replaceAll(":sName", rs.getString("OBJECT_NAME").trim())
				.replaceAll(":PR_ALF", "0")
				.replaceAll(":iKOD_INT", rs.getString("OBJECT_KOD"))
				.replaceAll(":TAR_CLASS", "0");		
				jt.update(tmp);
			}
			rowcount++;
			}
			catch (Exception e) {
				StringWriter outError = new StringWriter();
				PrintWriter errorWriter = new PrintWriter(outError);
				e.printStackTrace(errorWriter);
				log.error("cnsisync "+rs.getString("OBJECT_KOD"));
				log.error("cnsisync "+outError.toString());
			}
		}
	}
	
	

package ru.aisa.etdadmin.cnsimappers;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.log4j.Logger;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowCallbackHandler;

import ru.aisa.etdadmin.SqlForCnsi;


	
	
	public class VertsvRowMapper implements RowCallbackHandler {
		private JdbcTemplate jt;
		private static Logger log = Logger.getLogger(VertsvRowMapper.class);
		public VertsvRowMapper(JdbcTemplate npjtt){
			jt = npjtt;
		}
		public void processRow(ResultSet rs) throws SQLException {
			try{
				String tmp = SqlForCnsi.sqlnew[13]
				.replaceAll(":V_ID", String.valueOf(rs.getInt("PRED_V_ID")+20000000))
				.replaceAll(":N_ID", String.valueOf(rs.getInt("PRED_N_ID")+20000000))				
				.replaceAll(":VERT_SV_ID", rs.getString("VERT_SV_ID"))
				.replaceAll(":Vid_Podch_Priz", rs.getString("Vid_Podch_Priz"))
				.replaceAll(":VERTSV_VID_ID", (rs.getObject("VERTSV_VID_ID") == null ? " cast(null as int) " : rs.getString("VERTSV_VID_ID")))
				.replaceAll(":activ",  (rs.getString("COR_TIP").equals("D") ? "N" : "Y") ); 			
				jt.update(tmp);
			
			}
			catch (Exception e) {
				StringWriter outError = new StringWriter();
				PrintWriter errorWriter = new PrintWriter(outError);
				e.printStackTrace(errorWriter);
				log.error("cnsisync "+rs.getString("PRED_V_ID"));
				log.error("cnsisync "+outError.toString());
			}
		}
	}
	
	

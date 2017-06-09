package ru.aisa.etdadmin.cnsimappers;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.log4j.Logger;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowCallbackHandler;

import ru.aisa.etdadmin.SqlForCnsi;




	
	public class StpprjRowMapper implements RowCallbackHandler {
		private JdbcTemplate jt;
		
		private static Logger log = Logger.getLogger(StpprjRowMapper.class);
		
		
		public StpprjRowMapper(JdbcTemplate npjtt){
			jt = npjtt;
		}
		public void processRow(ResultSet rs) throws SQLException {
			try{		
				String tmp = SqlForCnsi.sqlnew[26]
				.replaceAll(":Stp_Prj_Id", rs.getString("Stp_Prj_Id"))
				.replaceAll(":Kod", (rs.getString("Kod") == null ? "null" : (rs.getString("Kod").trim().length()==0 ? "null":"'"+rs.getString("Kod").trim()+"'")))
				.replaceAll(":SKodS", (rs.getString("KodS") == null ? "null" : (rs.getString("KodS").trim().length()==0 ? "null":"'"+rs.getString("KodS").trim()+"'")))
				.replaceAll(":Id_rel", rs.getObject("Id_rel") == null ? " cast(null as int) " : rs.getString("Id_rel"))
				.replaceAll(":Id_MK", rs.getObject("Id_MK") == null ? " cast(null as int) " : rs.getString("Id_MK"))
				.replaceAll(":LMM", rs.getObject("LMM") == null ? " cast(null as int) " : rs.getString("LMM"))
				.replaceAll(":V_BOK", rs.getObject("V_BOK") == null ? " cast(null as smallint) " : rs.getString("V_BOK"))
				.replaceAll(":V_PR", rs.getObject("V_PR") == null ? " cast(null as smallint) " : rs.getString("V_PR"))
				.replaceAll(":PRIM", (rs.getString("PRIM") == null ? "null" : (rs.getString("PRIM").trim().length()==0 ? "null":"'"+rs.getString("PRIM").trim()+"'")));
//				.replaceAll(":Cor_tip", (rs.getString("Cor_tip").equals("D") ? "'N'" : "'Y'") );
						log.debug("ST_PRJ"+tmp);		
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
	
	
	

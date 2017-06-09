package ru.aisa.etdadmin.cnsimappers;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.log4j.Logger;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowCallbackHandler;

import ru.aisa.etdadmin.SqlForCnsi;




	
	public class VagnumRowMapper implements RowCallbackHandler {
		private JdbcTemplate jt;
		
		private static Logger log = Logger.getLogger(VagnumRowMapper.class);
		
		
		public VagnumRowMapper(JdbcTemplate npjtt){
			jt = npjtt;
		}
		public void processRow(ResultSet rs) throws SQLException {
			try{		
				String tmp = SqlForCnsi.sqlnew[25]
				.replaceAll(":VAG_NUM_ID", rs.getString("VAG_NUM_ID"))
				.replaceAll(":NG3", (rs.getString("NG3") == null ? "null" : (rs.getString("NG3").trim().length()==0 ? "null":"'"+rs.getString("NG3").trim()+"'")))
				.replaceAll(":VG3", (rs.getString("VG3") == null ? "null" : (rs.getString("VG3").trim().length()==0 ? "null":"'"+rs.getString("VG3").trim()+"'")))
				.replaceAll(":NG4", (rs.getString("NG4") == null ? "null" : (rs.getString("NG4").trim().length()==0 ? "null":"'"+rs.getString("NG4").trim()+"'")))
				.replaceAll(":VG4", (rs.getString("VG4") == null ? "null" : (rs.getString("VG4").trim().length()==0 ? "null":"'"+rs.getString("VG4").trim()+"'")))
				.replaceAll(":NG5", (rs.getString("NG5") == null ? "null" : (rs.getString("NG5").trim().length()==0 ? "null":"'"+rs.getString("NG5").trim()+"'")))
				.replaceAll(":VG5", (rs.getString("VG5") == null ? "null" : (rs.getString("VG5").trim().length()==0 ? "null":"'"+rs.getString("VG5").trim()+"'")))
				.replaceAll(":NG6", (rs.getString("NG6") == null ? "null" : (rs.getString("NG6").trim().length()==0 ? "null":"'"+rs.getString("NG6").trim()+"'")))
				.replaceAll(":VG6", (rs.getString("VG6") == null ? "null" : (rs.getString("VG6").trim().length()==0 ? "null":"'"+rs.getString("VG6").trim()+"'")))
				.replaceAll(":NG7", (rs.getString("NG7") == null ? "null" : (rs.getString("NG7").trim().length()==0 ? "null":"'"+rs.getString("NG7").trim()+"'")))
				.replaceAll(":VG7", (rs.getString("VG7") == null ? "null" : (rs.getString("VG7").trim().length()==0 ? "null":"'"+rs.getString("VG7").trim()+"'")))
				.replaceAll(":KO", rs.getObject("KO") == null ? " cast(null as smallint) " : rs.getString("KO"))
				.replaceAll(":VT", rs.getObject("VT") == null ? " cast(null as double) " : rs.getString("VT"))
				.replaceAll(":UD", rs.getObject("UD") == null ? " cast(null as smallint) " : rs.getString("UD"))
				.replaceAll(":DV", rs.getObject("DV") == null ? " cast(null as int) " : rs.getString("DV"))
				.replaceAll(":PG", rs.getObject("PG") == null ? " cast(null as double) " : rs.getString("PG"))
				.replaceAll(":UTV", rs.getObject("UTV") == null ? " cast(null as smallint) " : rs.getString("UTV"))
				.replaceAll(":RV", rs.getObject("RV") == null ? " cast(null as smallint) " : rs.getString("RV"))
				.replaceAll(":PRDO", (rs.getString("PRDO") == null ? "null" : (rs.getString("PRDO").trim().length()==0 ? "null":"'"+rs.getString("PRDO").trim()+"'")))
				.replaceAll(":ACTIV", (rs.getString("COR_TIP").equals("D") ? "'N'" : "'Y'") );
				
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
	
	
	

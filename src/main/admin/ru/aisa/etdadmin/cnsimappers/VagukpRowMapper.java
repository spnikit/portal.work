package ru.aisa.etdadmin.cnsimappers;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import org.apache.log4j.Logger;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowCallbackHandler;

import ru.aisa.etdadmin.SqlForCnsi;




	
	public class VagukpRowMapper implements RowCallbackHandler {
		private JdbcTemplate jt;
		
		private static Logger log = Logger.getLogger(VagukpRowMapper.class);
		private int rowcount = 0;
		
		public VagukpRowMapper(JdbcTemplate npjtt){
			jt = npjtt;
		}
		public void processRow(ResultSet rs) throws SQLException {
			try{		
				String tmp = SqlForCnsi.sqlnew[22]
				.replaceAll(":VAG_UKP_ID", rs.getString("VAG_UKP_ID"))
				.replaceAll(":UKP_KOD", rs.getString("UKP_KOD"))
				.replaceAll(":STRAN_KOD", rs.getObject("STRAN_KOD") == null ? " cast(null as smallint) " : rs.getString("STRAN_KOD"))
				.replaceAll(":DOR_KOD", rs.getObject("DOR_KOD") == null ? "0" : rs.getString("DOR_KOD"))
				.replaceAll(":STAN_ID", rs.getObject("STAN_ID") == null ? " cast(null as int) " : rs.getString("STAN_ID"))
				.replaceAll(":TP", (rs.getString("TP") == null ? " " : (rs.getString("TP").trim().length()==0 ? " ":rs.getString("TP").trim())))
				.replaceAll(":NAME", (rs.getString("NAME") == null ? " " : (rs.getString("NAME").trim().length()==0 ? " ":process(rs.getString("NAME").trim()))))
				.replaceAll(":SNAME", (rs.getString("SNAME") == null ? " " : (rs.getString("SNAME").trim().length()==0 ? " ":process(rs.getString("SNAME").trim()))))
				.replaceAll(":PRED_ID", rs.getObject("PRED_ID") == null ? " cast(null as int) " : (rs.getString("PRED_ID").equals("0")?"null":String.valueOf(rs.getInt("PRED_ID")+20000000)))
				.replaceAll(":DR", (rs.getString("DR") == null ? " " : (rs.getString("DR").trim().length()==0 ? " ":rs.getString("DR").trim())))
				.replaceAll(":KR1", (rs.getString("KR") == null ? " " : (rs.getString("KR").trim().length()==0 ? " ":rs.getString("KR").trim())))
				.replaceAll(":POSTR", (rs.getString("POSTR") == null ? " " : (rs.getString("POSTR").trim().length()==0 ? " ":rs.getString("POSTR").trim())))
				.replaceAll(":KRP", (rs.getString("KRP") == null ? " " : (rs.getString("KRP").trim().length()==0 ? " ":rs.getString("KRP").trim())))
				.replaceAll(":DPS1", (rs.getString("DPS1") == null ? " " : (rs.getString("DPS1").trim().length()==0 ? " ":rs.getString("DPS1").trim())))
				.replaceAll(":DPS2", (rs.getString("DPS2") == null ? " " : (rs.getString("DPS2").trim().length()==0 ? " ":rs.getString("DPS2").trim())))
				.replaceAll(":DPS3", (rs.getString("DPS3") == null ? " " : (rs.getString("DPS3").trim().length()==0 ? " ":rs.getString("DPS3").trim())))
				.replaceAll(":TEK_REM", (rs.getString("TEK_REM") == null ? " " : (rs.getString("TEK_REM").trim().length()==0 ? " ":rs.getString("TEK_REM").trim())))
				.replaceAll(":OSI", (rs.getString("OSI") == null ? " " : (rs.getString("OSI").trim().length()==0 ? " ":rs.getString("OSI").trim())))
				.replaceAll(":AMA", (rs.getString("RAMA") == null ? " " : (rs.getString("RAMA").trim().length()==0 ? " ":rs.getString("RAMA").trim())))
				.replaceAll(":BALKA", (rs.getString("BALKA") == null ? " " : (rs.getString("BALKA").trim().length()==0 ? " ":rs.getString("BALKA").trim())))
				.replaceAll(":REM_KOL_PARI", (rs.getString("REM_KOL_PARI") == null ? " " : (rs.getString("REM_KOL_PARI").trim().length()==0 ? " ":rs.getString("REM_KOL_PARI").trim())))
				.replaceAll(":KOL_PARA", (rs.getString("KOL_PARA") == null ? " " : (rs.getString("KOL_PARA").trim().length()==0 ? " ":rs.getString("KOL_PARA").trim())))
				.replaceAll(":KOLESA", (rs.getString("KOLESA") == null ? " " : (rs.getString("KOLESA").trim().length()==0 ? " ":rs.getString("KOLESA").trim())))
				.replaceAll(":TORM_PROD", (rs.getString("TORM_PROD") == null ? " " : (rs.getString("TORM_PROD").trim().length()==0 ? " ":rs.getString("TORM_PROD").trim())))
				.replaceAll(":ACTIV",  (rs.getString("COR_TIP").equals("D") ? "N" : "Y") );				
				jt.update(tmp);
				}
				catch (Exception e) {
					StringWriter outError = new StringWriter();
					PrintWriter errorWriter = new PrintWriter(outError);
					e.printStackTrace(errorWriter);
					log.error("cnsisync "+outError.toString());
				}
		}
		
//		 replacing side quotes to usual and replacing long defis to usual  159,143 - for ZOS
		private String process(String str) {
			ArrayList a = new ArrayList();
			ArrayList b = new ArrayList();
			char[] qq = str.toCharArray();
			for (int i = 0; i < qq.length; i++) {  
				if (((int)qq[i])==159 || ((int)qq[i])==143) a.add(i);
			    if (((int)qq[i])==150) b.add(i);
			}
			for (int i = 0; i < a.size(); i++) {
				str = str.replace(str.charAt(Integer.valueOf(a.get(i).toString())), '\"');
			}
			for (int i = 0; i < b.size(); i++) {
				str = str.replace(str.charAt(Integer.valueOf(b.get(i).toString())), '-');
			}
			return str;
		}
		
		
	}
	
	
	

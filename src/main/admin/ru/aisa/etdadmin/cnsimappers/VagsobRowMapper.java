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




	
	public class VagsobRowMapper implements RowCallbackHandler {
		private JdbcTemplate jt;
		
		private static Logger log = Logger.getLogger(VagsobRowMapper.class);
		private int rowcount = 0;
		
		public VagsobRowMapper(JdbcTemplate npjtt){
			jt = npjtt;
		}
		public void processRow(ResultSet rs) throws SQLException {
			try{		
				String tmp = SqlForCnsi.sqlnew[23]
				.replaceAll(":VAG_SOB_ID", rs.getString("VAG_SOB_ID"))
				.replaceAll(":LOC_KOD", rs.getObject("LOC_KOD") == null ? " cast(null as int) " : rs.getString("LOC_KOD"))
				//.replaceAll(":NAME", (rs.getString("NAME") == null ? " " : (rs.getString("NAME").trim().length()==0 ? " ":process(rs.getString("NAME").trim()))))
				.replaceAll(":OKPO_KOD", rs.getObject("OKPO_KOD") == null ? " cast(null as int) " : rs.getString("OKPO_KOD"))
				.replaceAll(":SNAME", (rs.getString("SNAME") == null ? " " : (rs.getString("SNAME").trim().length()==0 ? " ":process(rs.getString("SNAME").trim()))))
				.replaceAll(":REG_NOM", (rs.getString("REG_NOM") == null ? " " : (rs.getString("REG_NOM").trim().length()==0 ? " ":rs.getString("REG_NOM").trim())))
				.replaceAll(":ADM_KOD", rs.getObject("ADM_KOD") == null ? " cast(null as smallint) " : rs.getString("ADM_KOD"))
				.replaceAll(":ACTIV", (rs.getString("COR_TIP").equals("D") ? "N" : "Y") );			
				jt.update(tmp);
				}
				catch (Exception e) {
					StringWriter outError = new StringWriter();
					PrintWriter errorWriter = new PrintWriter(outError);
					e.printStackTrace(errorWriter);
					log.error("cnsisync "+outError.toString());
				}
		}
		
//		 replacing side quotes to usual and replacing long defis to usual
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
	
	
	

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




	
	public class StanparkRowMapper implements RowCallbackHandler {
		private JdbcTemplate jt;
		
		private static Logger log = Logger.getLogger(StanparkRowMapper.class);
		
		
		public StanparkRowMapper(JdbcTemplate npjtt){
			jt = npjtt;
		}
		public void processRow(ResultSet rs) throws SQLException {
			try{		
				String tmp = SqlForCnsi.sqlnew[27]
				.replaceAll(":STAN_ID", rs.getString("STAN_ID"))
				.replaceAll(":STANPARK_ID", rs.getString("STANPARK_ID"))
				.replaceAll(":NAME", (rs.getString("NAME") == null ? "null" : (rs.getString("NAME").trim().length()==0 ? "null":"'"+process(rs.getString("NAME").trim())+"'")))
				.replaceAll(":SPC_PARK_ID", rs.getObject("SPC_PARK_ID") == null ? " cast(null as smallint) " : rs.getString("SPC_PARK_ID"))
				.replaceAll(":SNAME", (rs.getString("SNAME") == null ? "null" : (rs.getString("SNAME").trim().length()==0 ? "null":"'"+process(rs.getString("SNAME").trim())+"'")))
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
	
	
	

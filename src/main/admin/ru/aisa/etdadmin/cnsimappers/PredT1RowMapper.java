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



	
	
	public class PredT1RowMapper implements RowCallbackHandler {
		private JdbcTemplate jt;
		private static Logger log = Logger.getLogger(PredT1RowMapper.class);
		private int rowcount = 0;
		public PredT1RowMapper(JdbcTemplate npjtt){
			jt = npjtt;
		}
		public void processRow(ResultSet rs) throws SQLException {
			try{
			//if (rowcount%10 == 0) log.warn(rowcount);
				String tmp = SqlForCnsi.sqlnew[7]
				.replaceAll(":VAG_UKP_ID", String.valueOf(rs.getInt("VAG_UKP_ID")+10000000))
				.replaceAll(":Dor_Kod", rs.getObject("Dor_Kod") == null ? "0" : rs.getString("Dor_Kod"))
				.replaceAll(":Ukp_Kod", rs.getString("Ukp_Kod"))   
				.replaceAll(":sName", process(rs.getString("sName").trim()))
				.replaceAll(":Name", process(rs.getString("Name").trim()))
				.replaceAll(":stan_id", rs.getString("stan_id") == null ? " CAST( NULL AS INT) "  : rs.getString("stan_id"))
				.replaceAll(":activ",  (rs.getString("COR_TIP").equals("D") ? "N" : "Y") );
				jt.update(tmp);
			
			rowcount++;
			}
			catch (Exception e) {
				StringWriter outError = new StringWriter();
				PrintWriter errorWriter = new PrintWriter(outError);
				e.printStackTrace(errorWriter);
				log.error("cnsisync "+rs.getInt("VAG_UKP_ID")+10000000);
				log.error("cnsisync "+outError.toString());
			}
			
			
		}
		
		
		// replacing side quotes to usual and replacing long defis to usual
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
	
	
	

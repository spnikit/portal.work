package ru.aisa.etdadmin.cnsimappers;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.log4j.Logger;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowCallbackHandler;

import ru.aisa.etdadmin.SqlForCnsi;


	
	
	public class DispuchRowMapper implements RowCallbackHandler {
		private JdbcTemplate jt;
		private static Logger	log	= Logger.getLogger(DispuchRowMapper.class);
		private int rowcount = 0;
		public DispuchRowMapper(JdbcTemplate npjtt){
			jt = npjtt;
		}
		public void processRow(ResultSet rs) throws SQLException {
			try{
			//if (rowcount%10 == 0) log.warn(rowcount);
			if (!rs.getString("COR_TIP").equals("D")){
				String tmp = SqlForCnsi.sqlnew[9]			
				.replaceAll(":DISP_UCH_ID", rs.getString("DISP_UCH_ID"))
				.replaceAll(":Name", process(rs.getString("Name").trim()))
				.replaceAll(":vName", (rs.getString("vName") == null ? " " : (rs.getString("vName").trim().length()==0 ? " ": process(rs.getString("vName").trim()))))
				.replaceAll(":DOR_KOD", rs.getString("DOR_KOD"));			
				jt.update(tmp);
			}
			rowcount++;
			}
			catch (Exception e) {
				StringWriter outError = new StringWriter();
				PrintWriter errorWriter = new PrintWriter(outError);
				e.printStackTrace(errorWriter);
				log.error("cnsisync "+rs.getString("DISP_UCH_ID"));
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
	
	

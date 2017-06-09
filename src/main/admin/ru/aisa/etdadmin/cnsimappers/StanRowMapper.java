package ru.aisa.etdadmin.cnsimappers;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.log4j.Logger;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowCallbackHandler;

import ru.aisa.etdadmin.SqlForCnsi;
import ru.aisa.etdadmin.Utils;



	
	
	public class StanRowMapper implements RowCallbackHandler {
		private JdbcTemplate jt;
		private static Logger log = Logger.getLogger(StanRowMapper.class);
		private int rowcount = 0;
		public StanRowMapper(JdbcTemplate npjtt){
			jt = npjtt;
		}
		public void processRow(ResultSet rs) throws SQLException {
			try{
			//if (rowcount%10 == 0) log.warn(rowcount);
			if (!rs.getString("COR_TIP").equals("D")){
				String tmp = SqlForCnsi.sqlnew[2]
				.replaceAll(":Stan_ID", rs.getString("Stan_ID"))
				.replaceAll(":Dor_Kod", rs.getString("Dor_Kod"))
				//.replaceAll(":St_Kod", rs.getString("St_Kod").trim())   //add 6 digit here
				//.replaceAll(":St_Kod", " CAST ( RTRIM(CHAR("+rs.getString("St_Kod").trim()+")) || RTRIM(CHAR(LETD.KOD_KR("+rs.getString("St_Kod").trim()+",SMALLINT(5)))) AS INT) " )  
				.replaceAll(":St_Kod", rs.getString("St_Kod").trim()+Utils.get6Kod(Integer.valueOf(rs.getString("St_Kod").trim())))
				.replaceAll(":VName", rs.getString("VName").trim())
				.replaceAll(":Name", rs.getString("Name").trim());
				jt.update(tmp);
			}
			rowcount++;
			}
			catch (Exception e) {
				StringWriter outError = new StringWriter();
				PrintWriter errorWriter = new PrintWriter(outError);
				e.printStackTrace(errorWriter);
				log.error("cnsisync "+rs.getString("Stan_ID"));
				log.error("cnsisync "+outError.toString());
			}
			
			
			try{
											String tmp = SqlForCnsi.sqlnew[16]
				               				.replaceAll(":STAN_ID", rs.getString("STAN_ID"))
				               				.replaceAll(":DOR_KOD", rs.getObject("DOR_KOD") == null ? "null" : rs.getString("DOR_KOD"))
				               				.replaceAll(":PRED_ID", rs.getObject("PRED_ID") == null ? "null" : String.valueOf(rs.getInt("PRED_ID")+20000000))
				               				.replaceAll(":OKATO_ID", rs.getObject("OKATO_ID") == null ? "null" : rs.getString("OKATO_ID"))
				               				.replaceAll(":ST_KOD", rs.getObject("ST_KOD") == null ? "null" : rs.getString("ST_KOD"))
				               				.replaceAll(":VNAME", (rs.getString("VNAME") == null ? " " : (rs.getString("VNAME").trim().length()==0 ? " ":rs.getString("VNAME").trim())))
				               				.replaceAll(":NAME", (rs.getString("NAME") == null ? " " : (rs.getString("NAME").trim().length()==0 ? " ":rs.getString("NAME").trim())))
				               				.replaceAll(":STAN_TIP_ID", rs.getObject("STAN_TIP_ID") == null ? "null" : rs.getString("STAN_TIP_ID"))
				               				.replaceAll(":MNEM", (rs.getString("MNEM") == null ? " " : (rs.getString("MNEM").trim().length()==0 ? " ":rs.getString("MNEM").trim())))
				               				.replaceAll(":ACTIV", (rs.getString("COR_TIP").equals("D") ? "N" : "Y") );
				               				
				               				jt.update(tmp);
				
			}
			
			catch (Exception e) {
				StringWriter outError = new StringWriter();
				PrintWriter errorWriter = new PrintWriter(outError);
				e.printStackTrace(errorWriter);
				log.error("cnsisync "+rs.getString("Stan_ID"));
				log.error("cnsisync "+outError.toString());
			}
			
			
		}
	}
	
	
	

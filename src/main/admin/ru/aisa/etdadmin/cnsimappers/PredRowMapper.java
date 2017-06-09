package ru.aisa.etdadmin.cnsimappers;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.regex.Matcher;

import org.apache.log4j.Logger;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowCallbackHandler;

import ru.aisa.etdadmin.SqlForCnsi;


	
	
	public class PredRowMapper implements RowCallbackHandler {
		private JdbcTemplate jt;
		private static Logger log = Logger.getLogger(PredRowMapper.class);
		
		public PredRowMapper(JdbcTemplate npjtt){
			jt = npjtt;			
		}
		public void processRow(ResultSet rs) throws SQLException {
			
			try{					
				String tmp = SqlForCnsi.sqlnew[3]
				.replaceAll(":Pred_ID", String.valueOf(rs.getInt("Pred_ID")+20000000))
				.replaceAll(":Dor_Kod", rs.getObject("Dor_Kod") == null ? "0" : Matcher.quoteReplacement(rs.getString("Dor_Kod")))
				.replaceAll(":Otr_Kod", rs.getObject("Otr_Kod") == null ? " cast(null as int) " : rs.getString("Otr_Kod"))
				.replaceAll(":sName", (rs.getString("sName") == null ? " " : (rs.getString("sName").trim().length()==0 ? " ":Matcher.quoteReplacement(rs.getString("sName").trim()))))
				.replaceAll(":s2Name", (rs.getString("sName2") == null ? " " : (rs.getString("sName2").trim().length()==0 ? " ":Matcher.quoteReplacement(rs.getString("sName2").trim()))))
				.replaceAll(":Name", (rs.getString("Name") == null ? " " : (rs.getString("Name").trim().length()==0 ? " ":Matcher.quoteReplacement(rs.getString("Name").trim()))))
				.replaceAll(":self",  (rs.getString("MPS_PRIZ").equals("-1") ? "Y" : "N") )
				.replaceAll(":activ",  (rs.getString("COR_TIP").equals("D") ? "N" : "Y") )
				.replaceAll(":ZAV_TIP_ID", rs.getObject("ZAV_TIP_ID") == null ? " cast(null as int) " : rs.getString("ZAV_TIP_ID"))
				.replaceAll(":stprim", ( rs.getObject("STAN_M_ID") == null ? " CAST( NULL AS INT) "  :  rs.getString("STAN_M_ID")  ));
				jt.update(tmp);			
			}
			catch (Exception e) {
				StringWriter outError = new StringWriter();
				PrintWriter errorWriter = new PrintWriter(outError);
				e.printStackTrace(errorWriter);
				log.error("cnsisync "+rs.getInt("Pred_ID")+2000000);
				log.error("cnsisync "+outError.toString());
			}
			
			
			try{
				
											String tmp = SqlForCnsi.sqlnew[15]
											.replaceAll(":PRED_ID", String.valueOf(rs.getInt("PRED_ID")+20000000))
				               				.replaceAll(":DOR_KOD", rs.getObject("DOR_KOD") == null ? "null" : rs.getString("DOR_KOD"))
				               				.replaceAll(":GR_ID", rs.getObject("GR_ID") == null ? "null" : rs.getString("GR_ID"))
				               				.replaceAll(":VD_ID", rs.getObject("VD_ID") == null ? "null" : rs.getString("VD_ID"))
				               				.replaceAll(":OTR_KOD", rs.getObject("OTR_KOD") == null ? "null" : rs.getString("OTR_KOD"))
				               				.replaceAll(":MPS_PRIZ", rs.getString("MPS_PRIZ"))
				               				.replaceAll(":STRAN_KOD", rs.getObject("STRAN_KOD") == null ? "null" : rs.getString("STRAN_KOD"))
				               				.replaceAll(":OKATO_ID", rs.getObject("OKATO_ID") == null ? "null" : rs.getString("OKATO_ID"))
				               				.replaceAll(":OKPO_KOD", rs.getObject("OKPO_KOD") == null ? "null" : rs.getString("OKPO_KOD"))
				               				.replaceAll(":OKONH_KOD", rs.getObject("OKONH_KOD") == null ? "null" : rs.getString("OKONH_KOD"))
				               				.replaceAll(":VNAME", (rs.getString("VNAME") == null ? " " : (rs.getString("VNAME").trim().length()==0 ? " ":Matcher.quoteReplacement(rs.getString("VNAME").trim()))))
				               				.replaceAll(":NAME", (rs.getString("NAME") == null ? " " : (rs.getString("NAME").trim().length()==0 ? " ":Matcher.quoteReplacement(rs.getString("NAME").trim()))))
				               				.replaceAll(":SNAME", (rs.getString("SNAME") == null ? " " : (rs.getString("SNAME").trim().length()==0 ? " ":Matcher.quoteReplacement(rs.getString("SNAME").trim()))))
				               				.replaceAll(":S2NAME", (rs.getString("SNAME2") == null ? " " : (rs.getString("SNAME2").trim().length()==0 ? " ":Matcher.quoteReplacement(rs.getString("SNAME2").trim()))))
				               				.replaceAll(":KR", rs.getObject("KR") == null ? "null" : rs.getString("KR"))
				               				.replaceAll(":MESTO", (rs.getString("MESTO") == null ? " " : (rs.getString("MESTO").trim().length()==0 ? " ":Matcher.quoteReplacement(rs.getString("MESTO").trim()))))
				               				.replaceAll(":NOM", rs.getObject("NOM") == null ? "null" : rs.getString("NOM"))
				               				.replaceAll(":STAN_M_ID", rs.getObject("STAN_M_ID") == null ? "null" : rs.getString("STAN_M_ID"))
				               				.replaceAll(":ZAV_TIP_ID", rs.getObject("ZAV_TIP_ID") == null ? "null" : rs.getString("ZAV_TIP_ID"))
				               				.replaceAll(":ACTIV", (rs.getString("COR_TIP").equals("D") ? "N" : "Y") );
				               				
				               				
				               				
				               			jt.update(tmp);
				
			}
			catch (Exception e) {
				StringWriter outError = new StringWriter();
				PrintWriter errorWriter = new PrintWriter(outError);
				e.printStackTrace(errorWriter);
				log.error("cnsisync "+rs.getInt("Pred_ID"));
				log.error("cnsisync "+outError.toString());
			}
			
		}
	}
	
	

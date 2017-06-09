package ru.aisa.etdadmin.cnsimappers;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.log4j.Logger;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowCallbackHandler;

import ru.aisa.etdadmin.SqlForCnsi;




	
	
	public class VagRemVu22RowMapper implements RowCallbackHandler {
		private JdbcTemplate jt;
		private static Logger log = Logger.getLogger(VagRemVu22RowMapper.class);
		public VagRemVu22RowMapper(JdbcTemplate npjtt){
			jt = npjtt;
		}
		public void processRow(ResultSet rs) throws SQLException {
			try{
				String tmp = SqlForCnsi.sqlnew[20]
				.replaceAll(":Vag_Rem_VU22_Id", rs.getString("Vag_Rem_VU22_Id"))
				.replaceAll(":Vag_Rem_Kod", rs.getObject("Vag_Rem_Kod") == null ? " cast(null as int) " : rs.getString("Vag_Rem_Kod"))
				.replaceAll(":VNAME", (rs.getString("VNAME") == null ? " " : (rs.getString("VNAME").trim().length()==0 ? " ":rs.getString("VNAME").trim())))
				.replaceAll(":NAME", (rs.getString("NAME") == null ? " " : (rs.getString("NAME").trim().length()==0 ? " ":rs.getString("NAME").trim())))
				.replaceAll(":Presk_#_new", (rs.getString("Presk_#_new") == null ? " " : (rs.getString("Presk_#_new").trim().length()==0 ? " ":rs.getString("Presk_#_new").trim())))
				.replaceAll(":Presk_#_old", (rs.getString("Presk_#_old") == null ? " " : (rs.getString("Presk_#_old").trim().length()==0 ? " ":rs.getString("Presk_#_old").trim())))
				.replaceAll(":Uzl_ID", rs.getObject("Uzl_ID") == null ? " cast(null as int) " : rs.getString("Uzl_ID"))
				.replaceAll(":TR1", rs.getObject("TR1") == null ? " cast(null as smallint) " : rs.getString("TR1"))
				.replaceAll(":TR2", rs.getObject("TR2") == null ? " cast(null as smallint) " : rs.getString("TR2"))
				.replaceAll(":DR", rs.getObject("DR") == null ? " cast(null as smallint) " : rs.getString("DR"))
				.replaceAll(":KR", rs.getObject("KR") == null ? " cast(null as smallint) " : rs.getString("KR"))
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
	}
	
	
	

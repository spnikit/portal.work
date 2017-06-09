package ru.aisa.etdadmin.cnsimappers;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.log4j.Logger;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowCallbackHandler;

import ru.aisa.etdadmin.SqlForCnsi;




	
	
	public class VagRemRodRowMapper implements RowCallbackHandler {
		private JdbcTemplate jt;
		private static Logger log = Logger.getLogger(VagRemRodRowMapper.class);
		public VagRemRodRowMapper(JdbcTemplate npjtt){
			jt = npjtt;
		}
		public void processRow(ResultSet rs) throws SQLException {
			try{
				String tmp = SqlForCnsi.sqlnew[21]
				.replaceAll(":Vag_Rem_VU22_Id", rs.getString("Vag_Rem_VU22_Id"))
				.replaceAll(":Vag_Rod_ID", rs.getString("Vag_Rod_ID"))
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
	
	
	

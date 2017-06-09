package ru.aisa.etdadmin.cnsimappers;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.log4j.Logger;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowCallbackHandler;

import ru.aisa.etdadmin.SqlForCnsi;


	
	
	public class GidroRowMapper implements RowCallbackHandler {
		private JdbcTemplate jt;
		private int classid;
		private static Logger	log	= Logger.getLogger(GidroRowMapper.class);
		private int rowcount = 0;
		public GidroRowMapper(JdbcTemplate npjtt,int classid){
			jt = npjtt;
			this.classid = classid;
		}
		public void processRow(ResultSet rs) throws SQLException {
			try{
			//if (rowcount%10 == 0) log.warn(rowcount);
			if (!rs.getString("COR_TIP").equals("D") && rs.getInt("class_id") == classid ){
				String tmp = SqlForCnsi.sqlnew[5]
				.replaceAll(":object_id", rs.getString("object_id"))
				.replaceAll(":class_id", rs.getString("class_id"))   
				.replaceAll(":object_kod", rs.getString("object_kod"))   
				.replaceAll(":object_name", rs.getString("object_name").trim()) ;  	
				jt.update(tmp);
			}
			rowcount++;
			}
			catch (Exception e) {
				StringWriter outError = new StringWriter();
				PrintWriter errorWriter = new PrintWriter(outError);
				e.printStackTrace(errorWriter);
				log.error("cnsisync "+rs.getString("object_id"));
				log.error("cnsisync "+outError.toString());
			}
			
			
			try{
				
				
				String tmp = SqlForCnsi.sqlnew[19]
				.replaceAll(":OBJECT_ID", rs.getString("OBJECT_ID"))
				.replaceAll(":CLASS_ID", rs.getObject("CLASS_ID") == null ? "null" : rs.getString("CLASS_ID"))
				.replaceAll(":OBJECT_KOD", rs.getObject("OBJECT_KOD") == null ? "null" : rs.getString("OBJECT_KOD"))
				.replaceAll(":OBJECT__KODSTR", (rs.getString("OBJECT_KODSTR") == null ? " " : (rs.getString("OBJECT_KODSTR").trim().length()==0 ? " ":rs.getString("OBJECT_KODSTR").trim())))
				.replaceAll(":OBJECT_VNAME", (rs.getString("OBJECT_VNAME") == null ? " " : (rs.getString("OBJECT_VNAME").trim().length()==0 ? " ":rs.getString("OBJECT_VNAME").trim().replaceAll("'", ""))))
				.replaceAll(":OBJECT_NAME", (rs.getString("OBJECT_NAME") == null ? " " : (rs.getString("OBJECT_NAME").trim().length()==0 ? " ":rs.getString("OBJECT_NAME").trim().replaceAll("'", ""))))
				.replaceAll(":OBJECT_SNAME", (rs.getString("OBJECT_SNAME") == null ? " " : (rs.getString("OBJECT_SNAME").trim().length()==0 ? " ":rs.getString("OBJECT_SNAME").trim().replaceAll("'", ""))))
				.replaceAll(":REFER", rs.getObject("REFER") == null ? "null" : rs.getString("REFER"))
				.replaceAll(":S1", (rs.getString("S1") == null ? " " : (rs.getString("S1").trim().length()==0 ? " ":rs.getString("S1").trim())))
				.replaceAll(":I1", rs.getObject("I1") == null ? "null" : rs.getString("I1"))
				.replaceAll(":ACTIV", (rs.getString("COR_TIP").equals("D") ? "N" : "Y") );
				
				jt.update(tmp);
				
			}
				catch (Exception e) {
					StringWriter outError = new StringWriter();
					PrintWriter errorWriter = new PrintWriter(outError);
					e.printStackTrace(errorWriter);
					log.error("cnsisync "+rs.getString("object_id"));
					log.error("cnsisync "+outError.toString());
				}
		}
	}
	
	

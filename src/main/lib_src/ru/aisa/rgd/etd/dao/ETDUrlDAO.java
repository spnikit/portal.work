package ru.aisa.rgd.etd.dao;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.log4j.Logger;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcDaoSupport;


public class ETDUrlDAO extends NamedParameterJdbcDaoSupport {
private static Logger	log	= Logger.getLogger(ETDUrlDAO.class);
private String urltype = "SynchrETDURL";
private String urlNotiseToEtd = "EtdNoticeURL";

	public ETDUrlDAO(DataSource ds)
	{
		super();
		setDataSource(ds);
	}
	public String getURL(){
		String url = "";
		Map hm = new HashMap();
		hm.put("SERVICENAME", urltype);
		
		
		try{
			
		url = (String)getNamedParameterJdbcTemplate().queryForObject("select url from snt.urls where SERVICENAME =:SERVICENAME", hm, String.class);
			
			
		}catch(Exception e){
			StringWriter outError = new StringWriter();
		    PrintWriter errorWriter = new PrintWriter(outError);
		    e.printStackTrace(errorWriter);
		    logger.error(outError.toString());
		    e.printStackTrace();
		}
		
		return url;
		
	}
	
	public String getEtdNoticeUrl(){
		String url = "";
		Map hm = new HashMap();
		hm.put("SERVICENAME", urlNotiseToEtd);
		
		
		try{
			
		url = (String)getNamedParameterJdbcTemplate().queryForObject("select url from snt.urls where SERVICENAME =:SERVICENAME", hm, String.class);
			
			
		}catch(Exception e){
			StringWriter outError = new StringWriter();
		    PrintWriter errorWriter = new PrintWriter(outError);
		    e.printStackTrace(errorWriter);
		    logger.error(outError.toString());
		    e.printStackTrace();
		}
		
		return url;
	}
	
}

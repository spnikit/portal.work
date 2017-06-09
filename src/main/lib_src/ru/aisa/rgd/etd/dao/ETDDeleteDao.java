package ru.aisa.rgd.etd.dao;

import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.log4j.Logger;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcDaoSupport;



public class ETDDeleteDao extends NamedParameterJdbcDaoSupport {
	
	private static Logger	log	= Logger.getLogger(ETDDeleteDao.class);
	
	public ETDDeleteDao(DataSource ds)
	{
		super();
		setDataSource(ds);
	}
	
	static private final String DELETE_DOCUMENTS_BY_LASTSIGN = 
			"DELETE from LETD.DOCSTORE where " +
			"LDATE < current date - #period and " +
			"SIGNLVL IS NULL and " +
			"typeid = (SELECT ID from LETD.DOCTYPE where NAME = :NAME)";
	
	static private final String DELETE_DOCSIGNS_BY_LASTSIGN = 
		"DELETE from LETD.DOCSTOREFLOW where " +
		"DOCID IN (" +
				"SELECT ID from LETD.DOCSTORE where " +
				"LDATE < current date - #period and " +
				"SIGNLVL IS NULL and " +
				"typeid = (SELECT ID from LETD.DOCTYPE where NAME = :NAME)" +
				") ";

	
	//examples of period
	//  1 YEAR ; 5 MONTHS ; 1.5 года == 1 YEAR - 6 MONTHS (-, а не +);
	public void deleteDocsByLastsign(String period, String docname){
		logger.warn("deleting docs "+docname+" on "+period);
		Map pp = new HashMap();
		pp.put("NAME", docname);
		
		int dsf = getNamedParameterJdbcTemplate().update(DELETE_DOCSIGNS_BY_LASTSIGN.replaceAll("#period", period), pp);
		int ds = getNamedParameterJdbcTemplate().update(DELETE_DOCUMENTS_BY_LASTSIGN.replaceAll("#period", period), pp);
		
		logger.warn(docname+" rows affected = " +dsf+";"+ds);		
	}
	
	static private final String DELETE_DOCUMENTS_BY_CREATEDATE = 
		"DELETE from LETD.DOCSTORE where " +
		"CRDATE < current date - #period and " +
		"SIGNLVL = 0 and " +
		"typeid = (SELECT ID from LETD.DOCTYPE where NAME = :NAME)";
	
	public void deleteNotSignedDocsByCreateDate(String period, String docname){
		logger.warn("deleting not signed docs "+docname+" on "+period);
		Map pp = new HashMap();
		pp.put("NAME", docname);
		
		int ds = getNamedParameterJdbcTemplate().update(DELETE_DOCUMENTS_BY_CREATEDATE.replaceAll("#period", period), pp);
		
		logger.warn(docname+" rows affected = "+ds);
	}

	
	
}

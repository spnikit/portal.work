package ru.aisa.rgd.etd.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.log4j.Logger;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.SqlOutParameter;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcDaoSupport;
import org.springframework.jdbc.core.simple.ParameterizedRowMapper;
import org.springframework.jdbc.object.StoredProcedure;

import ru.aisa.rgd.etd.objects.ETDCounterPers;
import ru.aisa.rgd.etd.objects.ETDCounterparts;
import ru.aisa.rgd.etd.objects.ETDDocumentAccess;
import ru.aisa.rgd.etd.objects.ETDDocumentData;
import ru.aisa.rgd.etd.objects.ETDTemplate;

public class ETDCounterPersDao extends NamedParameterJdbcDaoSupport {
	
	private static Logger	log	= Logger.getLogger(ETDDocumentDao.class);

	public ETDCounterPersDao(DataSource ds) {
		super();
		setDataSource(ds);
	}
	
		
	
	
	static private final String GET_COUNTERPERS_VIEW_SQL = "select id, " +
			//"rtrim(fname) " +
	" rtrim(cast(rtrim(fname) || ' ' || rtrim(mname) || ' ' || rtrim(lname) as char(240) ccsid unicode)) "+
	"name from SNT.personall where id in (select distinct pid from snt.perswrk where predid = :predId )" ;
	//		"name from SNT.personall where id in " +
	//" (select relateId from SNT.PERSRelates where Id=:Id and predId =:predId and relatepredid=:predSnd)   ";
	
		
	public List<ETDCounterPers> getCounterPers(/*Integer Id, Integer predSnd, */Integer predId)
	{
		//System.out.println("getCounterpersForRole");
		//System.out.println("Id "+Id);
		//System.out.println("predId "+predId);
		@SuppressWarnings("unchecked")
		List<ETDCounterPers> result = getNamedParameterJdbcTemplate().query(GET_COUNTERPERS_VIEW_SQL, 
				//new MapSqlParameterSource("Id", Id).addValue("predId", predId).addValue("predSnd", predSnd),
				new MapSqlParameterSource("predId", predId),
				new ParameterizedRowMapper<ETDCounterPers>() 
				{
					public ETDCounterPers mapRow(ResultSet rs, int n) throws SQLException 
					{
						//System.out.println("rs.getInt(id) "+rs.getInt("id"));
						//System.out.println("rs.getString(name) "+rs.getString("name"));
						ETDCounterPers obj = new ETDCounterPers(rs.getInt("id"), rs.getString("name").trim());
						return obj;
					}
				});
		logger.debug(result.toString());
		result.add(0, new ETDCounterPers(-2, "Отправить всем"));
		return result;
	}	

}
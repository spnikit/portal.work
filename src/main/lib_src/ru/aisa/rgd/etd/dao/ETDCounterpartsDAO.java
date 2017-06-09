package ru.aisa.rgd.etd.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import javax.sql.DataSource;

import org.apache.log4j.Logger;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcDaoSupport;
import org.springframework.jdbc.core.simple.ParameterizedRowMapper;

import ru.aisa.rgd.etd.objects.ETDCounterparts;

public class ETDCounterpartsDAO extends NamedParameterJdbcDaoSupport {
	
	private static Logger	log	= Logger.getLogger(ETDDocumentDao.class);

	public ETDCounterpartsDAO(DataSource ds) {
		super();
		setDataSource(ds);
	}
	
		
	
	
	/*static private final String GET_COUNTERPARTS_VIEW_SQL = "select id, rtrim(name) name from LETD.wrkname where id in " +
	"(select wrkid from LETD.doctypeflow where wrkid<> :workId and parent in " +
	"(select order from LETD.doctypeflow where wrkid = :workId "+ 
"and parent is not null) )";*/
	
	static private final String GET_COUNTERPARTS_VIEW_SQL = "select id, rtrim(vname) name from SNT.pred where id in " +
	"(select relatePredId from SNT.predRelates where predId=:predId )";

	
	static private final String GET_COUNTERPARTS_VIEW_SQL_R2 = "select id, rtrim(name) name from SNT.wrkname where id in " +
					"(select wrkid from SNT.doctypeflow where wrkid<> :workId and order in " +
					"(select parent from SNT.doctypeflow where wrkid = :workId "+ 
			") )";
	
	public List<ETDCounterparts> getCounterpartsForRole(Integer predId)
	{
		//System.out.println("getCounterpartsForRole");
		//System.out.println("predId "+predId);
		MapSqlParameterSource maap = new MapSqlParameterSource();
		maap.addValue("predId", predId);		
		//System.out.println("maap "+maap);
		@SuppressWarnings("unchecked")
		List<ETDCounterparts> result = getNamedParameterJdbcTemplate().query(GET_COUNTERPARTS_VIEW_SQL, 
				maap
				,
				new ParameterizedRowMapper<ETDCounterparts>() 
				{
					public ETDCounterparts mapRow(ResultSet rs, int n) throws SQLException 
					{
						//System.out.println("rs.getInt(id) "+rs.getInt("id"));
						//System.out.println("rs.getString(name) "+rs.getString("name"));
						ETDCounterparts obj = new ETDCounterparts(rs.getInt("id"), rs.getString("name").trim());
						return obj;
					}
				});
		logger.debug(result.toString());
		return result;
	}	

}

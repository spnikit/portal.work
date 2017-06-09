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
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcDaoSupport;
import org.springframework.jdbc.core.simple.ParameterizedRowMapper;

import ru.aisa.rgd.etd.objects.ETDFlowTypes;
public class ETDFlowTypesDao extends NamedParameterJdbcDaoSupport {
	
	private static Logger	log	= Logger.getLogger(ETDDocumentDao.class);

	public ETDFlowTypesDao(DataSource ds) {
		super();
		setDataSource(ds);
	}
	
		
	
	
	static private final String GET_FLOWTYPES_VIEW_SQL = "select id, " +
			//"rtrim(fname) " +
	" rtrim(name) name from SNT.docgroups" ;
	//		"name from SNT.personall where id in " +
	//" (select relateId from SNT.PERSRelates where Id=:Id and predId =:predId and relatepredid=:predSnd)   ";
	
		
	public List<ETDFlowTypes> getFlowTypes()
	{
		//System.out.println("getCounterpersForRole");
		//System.out.println("Id "+Id);
		//System.out.println("predId "+predId);
		@SuppressWarnings("unchecked")
		List<ETDFlowTypes> result = getNamedParameterJdbcTemplate().query(GET_FLOWTYPES_VIEW_SQL, 
				//new MapSqlParameterSource("Id", Id).addValue("predId", predId).addValue("predSnd", predSnd),
				new MapSqlParameterSource(),
				new ParameterizedRowMapper<ETDFlowTypes>() 
				{
					public ETDFlowTypes mapRow(ResultSet rs, int n) throws SQLException 
					{
						ETDFlowTypes obj = new ETDFlowTypes(rs.getInt("id"), rs.getString("name").trim());
						return obj;
					}
				});
		logger.debug(result.toString());
		//result.add(0, new ETDFlowTypes(-2, "Отправить всем"));
		return result;
	}	

}
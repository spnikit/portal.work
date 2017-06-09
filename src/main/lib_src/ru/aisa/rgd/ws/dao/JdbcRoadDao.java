package ru.aisa.rgd.ws.dao;

import java.sql.ResultSet;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcDaoSupport;
import org.springframework.jdbc.core.simple.ParameterizedRowMapper;

import ru.aisa.rgd.ws.domain.Road;
import ru.aisa.rgd.ws.exeption.ServiceCode;
import ru.aisa.rgd.ws.exeption.ServiceError;
import ru.aisa.rgd.ws.exeption.ServiceException;

public class JdbcRoadDao extends NamedParameterJdbcDaoSupport implements
		RoadDao {

	public JdbcRoadDao(DataSource ds) 
	{
		super();
		setDataSource(ds);
	}
	
//	SQl statements ////////////////////////////////////////////////////////
	protected static final String
		SELECT_NAME_SQL = 
			"SELECT rtrim(name) FROM letd.dor WHERE id = :id FETCH FIRST 1 ROWS ONLY",
		
		SELECT_SHORT_NAME_SQL = 
			"SELECT rtrim(sname) FROM letd.dor WHERE id = :id FETCH FIRST 1 ROWS ONLY",

		SELECT_ROAD_SQL = 
				"SELECT id, admId, rtrim(name) as name, rtrim(sname) as sName FROM letd.dor WHERE id = :id FETCH FIRST 1 ROWS ONLY";
	

// ORM Mapping for Enterprise object /////////////////////////////////////////
	static class Mapper implements ParameterizedRowMapper<Road>
	{
		public Road mapRow(ResultSet rs, int n) throws SQLException 
		{
			Road r = new Road();
			r.setAdmId(rs.getInt("admId"));
			r.setId(rs.getInt("id"));
			r.setName(rs.getString("name"));
			r.setSName(rs.getString("sName"));
			return r;
		}
	}	

//	DAO methods ///////////////////////////////////////////////////////////////
	/* (non-Javadoc)
	 * @see ru.aisa.rgd.ws.dao.RoadDao#getName(int)
	 */
	public String getName(int id) throws ServiceException 
	{
		String name = null;
		try
		{
			name = (String) getNamedParameterJdbcTemplate().queryForObject(SELECT_NAME_IIT_SQL, 
					new MapSqlParameterSource("id", id), String.class);
		}
		catch(IncorrectResultSizeDataAccessException e)
		{
			throw new ServiceException(e, ServiceCode.ERR_ROADID_EMPTY, 
					"Road with id " + id + " does not exist");
		}
		return name;
	}

	/* (non-Javadoc)
	 * @see ru.aisa.rgd.ws.dao.RoadDao#getShortName(int)
	 */
	public String getShortName(int id) throws ServiceException 
	{
		String name = null;
		try
		{
			name = (String) getNamedParameterJdbcTemplate().queryForObject(SELECT_SHORT_NAME_SQL, 
					new MapSqlParameterSource("id", id), String.class);
		}
		catch(IncorrectResultSizeDataAccessException e)
		{
			throw new ServiceException(e, ServiceCode.ERR_ROADID_EMPTY, 
					"Road with id " + id + " does not exist");
		}
		return name;
	}

	/* (non-Javadoc)
	 * @see ru.aisa.rgd.ws.dao.RoadDao#getById(int)
	 */
	public Road getById(int id) throws ServiceException 
	{
		Road road = null;
		try
		{
			road = (Road) getNamedParameterJdbcTemplate().queryForObject(SELECT_ROAD_SQL, 
				new MapSqlParameterSource("id", id), new Mapper());
		}
		catch(IncorrectResultSizeDataAccessException e)
		{
			throw new ServiceException(e, ServiceCode.ERR_ROADID_EMPTY, 
					"Road with id " + id + " does not exist");
		}
		return road;
	}

	private final static String NAME_BY_ENT_ID = 
		"select rtrim(name) from letd.dor where id = " +
		"(select dorid from letd.pred where id = :PREDID)";
	
	private final static String SELECT_NAME_IIT_SQL = "SELECT rtrim(name) FROM snt.dor WHERE id = :id FETCH FIRST 1 ROWS ONLY";
	
	public String getNameByEnterpriseId(int enterpriseId) 
	{
		String name = null;
		try
		{
			name = (String) getNamedParameterJdbcTemplate().queryForObject(NAME_BY_ENT_ID, 
					new MapSqlParameterSource("PREDID", enterpriseId), String.class);
		}
		catch(IncorrectResultSizeDataAccessException e)
		{
			throw new ServiceException(e, ServiceError.ERR_ROADID_EMPTY);
		}
		return name;
	}
	

}

package ru.aisa.rgd.ws.dao;

import java.sql.ResultSet;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcDaoSupport;
import org.springframework.jdbc.core.simple.ParameterizedRowMapper;

import ru.aisa.rgd.ws.domain.River;
import ru.aisa.rgd.ws.exeption.ServiceCode;
import ru.aisa.rgd.ws.exeption.ServiceException;

//GIDRO Table
class RiverMapper implements ParameterizedRowMapper<River>
{
	public River mapRow(ResultSet rs, int n) throws SQLException 
	{
		River obj = new River();
		obj.setCode(rs.getInt("kod"));
		obj.setId(rs.getInt("id"));
		obj.setName(rs.getString("name"));
		obj.setObjClass(rs.getInt("objclass"));
		return obj;
	}
}

public class JdbcRiverDao extends NamedParameterJdbcDaoSupport implements RiverDao {

	public JdbcRiverDao(DataSource ds) {
		super();
		setDataSource(ds);
	}
	
	private static final String GET_BY_CODE_SQL =
		"SELECT id, objclass, kod, RTRIM(name) AS name FROM letd.gidro WHERE kod = :code";
	
	public River getByCode(int code) 
	{
		River obj = null;
		try
		{
			obj = (River) getNamedParameterJdbcTemplate().queryForObject(GET_BY_CODE_SQL, 
					new MapSqlParameterSource("code", code), new RiverMapper());
		}
		catch(IncorrectResultSizeDataAccessException e)
		{
			throw new ServiceException(e, ServiceCode.ERR_RIVER_EMPTY,
					"River with  code " + code + " does not exist");
		}
		return obj;
	}

	private static final String GET_BY_ID_SQL =
		"SELECT id, objclass, kod, RTRIM(name) AS name FROM letd.gidro WHERE id = :id";
	
	public River getById(int id) 
	{
		River obj = null;
		try
		{
			obj = (River) getNamedParameterJdbcTemplate().queryForObject(GET_BY_ID_SQL, 
					new MapSqlParameterSource("id", id), new RiverMapper());
		}
		catch(IncorrectResultSizeDataAccessException e)
		{
			throw new ServiceException(e, ServiceCode.ERR_RIVER_EMPTY,
					"River with  id " + id + " does not exist");
		}
		return obj;
	}

	private static final String GET_NAME_BY_CODE_SQL =
		"SELECT RTRIM(name) AS name FROM letd.gidro WHERE kod = :code";
	
	public String getNameByCode(int code) 
	{
		String name = null;
		try
		{
			name = (String)getNamedParameterJdbcTemplate().queryForObject(GET_NAME_BY_CODE_SQL, 
					new MapSqlParameterSource("code", code), String.class);
		}
		catch(IncorrectResultSizeDataAccessException e)
		{
			throw new ServiceException(e, ServiceCode.ERR_RIVER_EMPTY,
					"River with  code " + code + " does not exist");
		}
		return name;
	}

	private static final String GET_NAME_BY_ID_SQL =
		"SELECT RTRIM(name) AS name FROM letd.gidro WHERE id = :id";

	public String getNameById(int id) 
	{
		String name = null;
		try
		{
			name = (String)getNamedParameterJdbcTemplate().queryForObject(GET_NAME_BY_ID_SQL , 
					new MapSqlParameterSource("id", id), String.class);
		}
		catch(IncorrectResultSizeDataAccessException e)
		{
			throw new ServiceException(e, ServiceCode.ERR_RIVER_EMPTY,
					"River with  id " + id + " does not exist");
		}
		return name;
	}

}

package ru.aisa.rgd.ws.dao;

import java.sql.ResultSet;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcDaoSupport;
import org.springframework.jdbc.core.simple.ParameterizedRowMapper;

import ru.aisa.rgd.ws.domain.TerritorialObject;
import ru.aisa.rgd.ws.exeption.ServiceCode;
import ru.aisa.rgd.ws.exeption.ServiceException;

//OKATO Table
class TerritorialObjectMapper implements ParameterizedRowMapper<TerritorialObject>
{
	public TerritorialObject mapRow(ResultSet rs, int n) throws SQLException 
	{
		TerritorialObject obj = new TerritorialObject();
		obj.setCode(rs.getInt("OKATO_KOD"));
		obj.setId(rs.getInt("OKATO_ID"));
		obj.setName(rs.getString("NAME"));
		obj.setStranCode(rs.getInt("STRAN_KOD"));
		return obj;
	}
}

public class JdbsTerritorialObjectDao extends NamedParameterJdbcDaoSupport implements TerritorialObjectDao {
	
	public JdbsTerritorialObjectDao(DataSource ds)
	{
		super();
		setDataSource(ds);
	}

	private static final String GET_BY_CODE_SQL =
		"SELECT okato_id, stran_kod, okato_kod, RTRIM(name) AS name FROM letd.okato WHERE okato_kod = :code";
	
	public TerritorialObject getByCode(int code) 
	{
		TerritorialObject obj = null;
		try
		{
			obj = (TerritorialObject) getNamedParameterJdbcTemplate().queryForObject(GET_BY_CODE_SQL, 
					new MapSqlParameterSource("code", code), new TerritorialObjectMapper());
		}
		catch(IncorrectResultSizeDataAccessException e)
		{
			throw new ServiceException(e, ServiceCode.ERR_OKATO_EMPTY,
					"Terrirorial object with  code " + code + " does not exist");
		}
		return obj;
	}

	private static final String GET_BY_ID_SQL =
		"SELECT okato_id, stran_kod, okato_kod, RTRIM(name) AS name FROM letd.okato WHERE okato_id = :id";
	
	public TerritorialObject getById(int id) 
	{
		TerritorialObject obj = null;
		try
		{
			obj = (TerritorialObject) getNamedParameterJdbcTemplate().queryForObject(GET_BY_ID_SQL, 
					new MapSqlParameterSource("id", id), new TerritorialObjectMapper());
		}
		catch(IncorrectResultSizeDataAccessException e)
		{
			throw new ServiceException(e, ServiceCode.ERR_OKATO_EMPTY,
					"Terrirorial object with  id " + id + " does not exist");
		}
		return obj;
	}

	private static final String GET_NAME_BY_CODE_SQL =
		"SELECT RTRIM(name) AS name FROM letd.okato WHERE okato_kod = :code";
	
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
			throw new ServiceException(e, ServiceCode.ERR_OKATO_EMPTY,
					"Terrirorial object with  code " + code + " does not exist");
		}
		return name;
	}

	private static final String GET_NAME_BY_ID_SQL =
		"SELECT RTRIM(name) AS name FROM letd.okato WHERE okato_id = :id";

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
			throw new ServiceException(e, ServiceCode.ERR_OKATO_EMPTY,
					"Terrirorial object with  id " + id + " does not exist");
		}
		return name;
	}

}

package ru.aisa.rgd.ws.dao;

import java.sql.ResultSet;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcDaoSupport;
import org.springframework.jdbc.core.simple.ParameterizedRowMapper;

import ru.aisa.rgd.ws.domain.Up;
import ru.aisa.rgd.ws.exeption.ServiceCode;
import ru.aisa.rgd.ws.exeption.ServiceException;

public class JdbcUpDao extends NamedParameterJdbcDaoSupport implements
		UpDao {
	
	static class Mapper implements ParameterizedRowMapper<Up>
	{
		public Up mapRow(ResultSet rs, int n) throws SQLException 
		{
			Up e = new Up();

			e.setId(rs.getInt("id"));
			e.setCode(rs.getInt("code"));
			e.setMagcode(rs.getInt("magcode"));
			e.setStId1(rs.getInt("stid1"));
			e.setStId2(rs.getInt("stid2"));
			e.setName(rs.getString("name"));			
			return e;
		}
	}
		
	public JdbcUpDao(DataSource ds) 
	{
		super();
		setDataSource(ds);
	}

	static final String SELECT_BY_ID = 
		"SELECT id, code, magcode, stid1, stid2, RTRIM(name) as name" +
		" FROM letd.up WHERE id = :id";
	public Up getById(int id) throws ServiceException 
	{
		Up up = null;
		try
		{
			up = (Up) getNamedParameterJdbcTemplate().queryForObject(SELECT_BY_ID, 
				new MapSqlParameterSource("id", id), new Mapper());
		}
		catch(IncorrectResultSizeDataAccessException e)
		{
			throw new ServiceException(e, ServiceCode.ERR_UP_EMPTY,
					"Up with id " + id + "  does not exist");
		}
		return up;
	}
	
	static final String SELECT_BY_CODE = 
		"SELECT id, code, magcode, stid1, stid2, RTRIM(name) as name" +
		" FROM letd.up WHERE code = :code";
	public Up getByCode(int code) throws ServiceException 
	{
		Up up = null;
		try
		{
			up = (Up) getNamedParameterJdbcTemplate().queryForObject(SELECT_BY_CODE, 
				new MapSqlParameterSource("code", code), new Mapper());
		}
		catch(IncorrectResultSizeDataAccessException e)
		{
			throw new ServiceException(e, ServiceCode.ERR_UP_EMPTY,
					"Up with code " + code + "  does not exist");
		}
		return up;
	}

}

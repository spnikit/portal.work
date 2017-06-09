package ru.aisa.rgd.ws.dao;

import java.sql.ResultSet;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcDaoSupport;
import org.springframework.jdbc.core.simple.ParameterizedRowMapper;

import ru.aisa.rgd.ws.domain.WayPart;
import ru.aisa.rgd.ws.exeption.ServiceCode;
import ru.aisa.rgd.ws.exeption.ServiceException;

/**
 * Участок пути
 */
public class JdbcWayPartDao extends NamedParameterJdbcDaoSupport implements
		WayPartDao {
	
	class WayPartMapper implements ParameterizedRowMapper<WayPart>
	{
		public WayPart mapRow(ResultSet rs, int n) throws SQLException 
		{
			WayPart part = new WayPart();
			part.setCode(rs.getInt("code"));
			part.setId(rs.getInt("id"));
			part.setMagCode(rs.getInt("magCode"));
			part.setName(rs.getString("name"));
			part.setStId1(rs.getInt("stId1"));
			part.setStId2(rs.getInt("stId2"));
			return part;
		}

	}

	public JdbcWayPartDao(DataSource ds) 
	{
		super();
		setDataSource(ds);
	}

	private static final String GET_BY_CODE_SQL = 
		"SELECT * FROM letd.up WHERE code = :code";
	/* (non-Javadoc)
	 * @see ru.aisa.rgd.ws.dao.WayPartDao#getByCode(int)
	 */
	public WayPart getByCode(int code) throws ServiceException 
	{
		WayPart part = null;
		try
		{
			part = (WayPart)getNamedParameterJdbcTemplate().queryForObject(GET_BY_CODE_SQL, 
					new MapSqlParameterSource("code", code), new WayPartMapper());
		}
		catch (IncorrectResultSizeDataAccessException e)
		{
			throw new ServiceException(e, ServiceCode.ERR_WAYPART_EMPTY,
					"Way part with  code " + code + " does not exist");
		};
		return part;
	}

	private static final String GET_BY_ID_SQL = 
		"SELECT * FROM letd.up WHERE id = :id";
	/* (non-Javadoc)
	 * @see ru.aisa.rgd.ws.dao.WayPartDao#getById(int)
	 */
	public WayPart getById(int id) throws ServiceException 
	{
		WayPart part = null;
		try
		{
			part = (WayPart)getNamedParameterJdbcTemplate().queryForObject(GET_BY_ID_SQL, 
					new MapSqlParameterSource("id", id), new WayPartMapper());
		}
		catch (IncorrectResultSizeDataAccessException e)
		{
			throw new ServiceException(e, ServiceCode.ERR_WAYPART_EMPTY,
					"Way part with  id " + id + " does not exist");
		};
		return part;
		
	}

	private static final String GET_NAME_BY_CODE_SQL = 
		"SELECT rtrim(name) as name  FROM letd.up WHERE code = :code";
	/* (non-Javadoc)
	 * @see ru.aisa.rgd.ws.dao.WayPartDao#getNameByCode(int)
	 */
	public String getNameByCode(int code) throws ServiceException 
	{
		String name = null;
		try
		{
			name = (String) getNamedParameterJdbcTemplate().queryForObject(GET_NAME_BY_CODE_SQL, 
					new MapSqlParameterSource("code", code), String.class);
		}
		catch (IncorrectResultSizeDataAccessException e)
		{
			throw new ServiceException(e, ServiceCode.ERR_WAYPART_EMPTY,
					"Way part with  code " + code + " does not exist");
		};
		return name;
	}

}

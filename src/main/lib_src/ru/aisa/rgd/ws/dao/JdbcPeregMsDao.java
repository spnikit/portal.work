package ru.aisa.rgd.ws.dao;

import java.sql.ResultSet;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcDaoSupport;
import org.springframework.jdbc.core.simple.ParameterizedRowMapper;

import ru.aisa.rgd.ws.domain.PeregMs;
import ru.aisa.rgd.ws.exeption.ServiceCode;
import ru.aisa.rgd.ws.exeption.ServiceException;

public class JdbcPeregMsDao extends NamedParameterJdbcDaoSupport implements
		PeregMsDao {
	
	static class Mapper implements ParameterizedRowMapper<PeregMs>
	{
		public PeregMs mapRow(ResultSet rs, int n) throws SQLException 
		{
			PeregMs e = new PeregMs();

			e.setActiv(rs.getString("activ"));
			e.setPeregMsId(rs.getInt("pereg_ms_id"));
			e.setUpId(rs.getInt("up_id"));
			e.setStan1Id(rs.getInt("stan1_id"));
			e.setStan2Id(rs.getInt("stan2_id"));
			e.setExpl(rs.getDouble("expl"));
			e.setChet(rs.getInt("chet"));			
			return e;
		}
	}
		
	public JdbcPeregMsDao(DataSource ds) 
	{
		super();
		setDataSource(ds);
	}

	static final String SELECT_BY_ID = 
		"SELECT pereg_ms_id, up_id, stan1_id, stan2_id, expl, chet, RTRIM(activ) as activ" +
		" FROM letd.pereg_ms WHERE pereg_ms_id = :id AND activ = 'Y'";// FETCH FIRST 1 ROW ONLY";
	public PeregMs getById(int id) throws ServiceException 
	{
		PeregMs perMs = null;
		try
		{
			perMs = (PeregMs) getNamedParameterJdbcTemplate().queryForObject(SELECT_BY_ID, 
				new MapSqlParameterSource("id", id), new Mapper());
		}
		catch(IncorrectResultSizeDataAccessException e)
		{
			throw new ServiceException(e, ServiceCode.ERR_PEREG_MS_EMPTY,
					"Pereg_ms with id " + id + " does not exist");
		}
		return perMs;
	}

}

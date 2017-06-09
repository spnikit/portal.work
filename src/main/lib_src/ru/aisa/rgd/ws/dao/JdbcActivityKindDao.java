package ru.aisa.rgd.ws.dao;

import java.sql.ResultSet;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcDaoSupport;
import org.springframework.jdbc.core.simple.ParameterizedRowMapper;

import ru.aisa.rgd.ws.domain.ActivityKind;
import ru.aisa.rgd.ws.exeption.ServiceCode;
import ru.aisa.rgd.ws.exeption.ServiceException;
class ActivityKindMapper implements ParameterizedRowMapper<ActivityKind>
{
	public ActivityKind mapRow(ResultSet rs, int n) throws SQLException 
	{
		ActivityKind obj = new ActivityKind();
		obj.setVdId(rs.getInt("VD_ID"));
		obj.setVdKod(rs.getInt("VD_KOD"));
		obj.setPvdKod(rs.getInt("PVD_KOD"));
		obj.setName(rs.getString("NAME"));
		obj.setSName(rs.getString("SNAME"));
		return obj;
	}
}

public class JdbcActivityKindDao extends NamedParameterJdbcDaoSupport implements
		ActivityKindDao {
	
	public JdbcActivityKindDao(DataSource ds){
		super();
		setDataSource(ds);
	}

	private static final String GET_BY_ID_SQL = 
		"select VD_ID, VD_KOD, PVD_KOD, NAME, SNAME from LETD.VIDDEJ where VD_ID = :ID"; 
	
	public ActivityKind getById(int id) {
		ActivityKind obj = null;
		try
		{
			obj = (ActivityKind) getNamedParameterJdbcTemplate().queryForObject(GET_BY_ID_SQL, 
					new MapSqlParameterSource("ID", id), new ActivityKindMapper());
		}
		catch(IncorrectResultSizeDataAccessException e)
		{
			throw new ServiceException(e, ServiceCode.ERR_ACTIVITY_KIND_EMPTY,
					"ActivityKind with  id " + id + " does not exist");
		}
		return obj;
	}

	private static final String GET_NAME_BY_ID_SQL = 
		"SELECT RTRIM(name) AS name FROM LETD.VIDDEJ WHERE vd_id = :id";
	
	public String getNameById(int id) {
		String name = null;
		try
		{
			name = (String)getNamedParameterJdbcTemplate().queryForObject(GET_NAME_BY_ID_SQL , 
					new MapSqlParameterSource("id", id), String.class);
		}
		catch(IncorrectResultSizeDataAccessException e)
		{
			throw new ServiceException(e, ServiceCode.ERR_RIVER_EMPTY,
					"ActivityKind with  id " + id + " does not exist");
		}
		return name;		
	}

}

package ru.aisa.rgd.ws.dao;

import java.sql.ResultSet;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcDaoSupport;
import org.springframework.jdbc.core.simple.ParameterizedRowMapper;

import ru.aisa.rgd.ws.domain.Gruz;
import ru.aisa.rgd.ws.exeption.ServiceCode;
import ru.aisa.rgd.ws.exeption.ServiceException;

//GIDRO Table
class GruzMapper implements ParameterizedRowMapper<Gruz>
{
	public Gruz mapRow(ResultSet rs, int n) throws SQLException 
	{
		Gruz obj = new Gruz();
		obj.setGruzEtsng2Id(rs.getLong("gruz_etsng2_id"));
		obj.setKod(rs.getString("kod"));
		obj.setKodInt(rs.getInt("kod_int"));
		obj.setPrAlf(rs.getString("pr_alf").charAt(0));
		obj.setSname(rs.getString("sname"));
		obj.setTarClass(rs.getInt("tar_class"));
		obj.setVname(rs.getString("vname"));
		return obj;
	}
}

public class JdbcGruzDao extends NamedParameterJdbcDaoSupport implements GruzDao {

	public JdbcGruzDao(DataSource ds) {
		super();
		setDataSource(ds);
	}
	
	private static final String GET_BY_CODE_SQL =
		"SELECT GRUZ_ETSNG2_ID, KOD, KOD_INT, PR_ALF, SNAME, VNAME, TAR_CLASS  FROM letd.gruz WHERE KOD_INT = :code";
	
	public Gruz getByCode(int code) 
	{
		Gruz obj = null;
		try
		{
			obj = (Gruz) getNamedParameterJdbcTemplate().queryForObject(GET_BY_CODE_SQL, 
					new MapSqlParameterSource("code", code), new GruzMapper());
		}
		catch(IncorrectResultSizeDataAccessException e)
		{
			throw new ServiceException(e, ServiceCode.ERR_GRUZ_EMPTY,
					"Gruz with  code " + code + " does not exist");
		}
		return obj;
	}


	private static final String GET_NAME_BY_CODE_SQL =
		"SELECT RTRIM(VNAME) AS name FROM letd.gruz WHERE KOD_INT = :code";
	
	public String getVNameByCode(int code) 
	{
		String name = null;
		try
		{
			name = (String)getNamedParameterJdbcTemplate().queryForObject(GET_NAME_BY_CODE_SQL, 
					new MapSqlParameterSource("code", code), String.class);
		}
		catch(IncorrectResultSizeDataAccessException e)
		{
			throw new ServiceException(e, ServiceCode.ERR_GRUZ_EMPTY,
					"Gruz with  code " + code + " does not exist");
		}
		return name;
	}
	
	private static final String GET_SNAME_BY_CODE_SQL =
		"SELECT RTRIM(SNAME) AS name FROM letd.gruz WHERE KOD_INT = :code";
	
	public String getSNameByCode(int code) 
	{
		String name = null;
		try
		{
			name = (String)getNamedParameterJdbcTemplate().queryForObject(GET_SNAME_BY_CODE_SQL, 
					new MapSqlParameterSource("code", code), String.class);
		}
		catch(IncorrectResultSizeDataAccessException e)
		{
			throw new ServiceException(e, ServiceCode.ERR_GRUZ_EMPTY,
					"Gruz with  code " + code + " does not exist");
		}
		return name;
	}

}

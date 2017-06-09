package ru.aisa.rgd.ws.dao;

import java.sql.ResultSet;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcDaoSupport;
import org.springframework.jdbc.core.simple.ParameterizedRowMapper;

import ru.aisa.rgd.ws.domain.Vagsob;
import ru.aisa.rgd.ws.exeption.ServiceCode;
import ru.aisa.rgd.ws.exeption.ServiceException;

//VAG_SOB Table
class VagsobMapper implements ParameterizedRowMapper<Vagsob>
{
	public Vagsob mapRow(ResultSet rs, int n) throws SQLException 
	{
		Vagsob obj = new Vagsob();
		obj.setVagSobId(rs.getLong("vag_sob_id"));
		obj.setLocKod(rs.getLong("loc_kod"));
		obj.setOkpoKod(rs.getLong("okpo_kod"));
		obj.setSname(rs.getString("sname"));
		obj.setRegNom(rs.getString("reg_nom"));
		obj.setAdmKod(rs.getInt("adm_kod"));
		obj.setActiv(rs.getString("activ"));
		return obj;
	}
}

public class JdbcVagsobDao extends NamedParameterJdbcDaoSupport implements VagsobDao {

	public JdbcVagsobDao(DataSource ds) {
		super();
		setDataSource(ds);
	}
	
	private static final String GET_BY_LOC_CODE_SQL =
		"SELECT VAG_SOB_ID, LOC_KOD, OKPO_KOD, RTRIM(REG_NOM) as reg_nom, ADM_KOD, RTRIM(ACTIV) as activ, RTRIM(SNAME) AS sname FROM letd.vag_sob WHERE loc_kod = :code";
	//RTRIM(SNAME) AS name
	public Vagsob getByLocKod(int code)
	{
		Vagsob obj = null;
		try
		{
			obj = (Vagsob) getNamedParameterJdbcTemplate().queryForObject(GET_BY_LOC_CODE_SQL, 
					new MapSqlParameterSource("code", code), new VagsobMapper());
		}
		catch(IncorrectResultSizeDataAccessException e)
		{
			throw new ServiceException(e, ServiceCode.ERR_VAGSOB_EMPTY, 
					"Vagsob with  code " + code + " does not exist");
		}
		return obj;
	}


	private static final String GET_NAME_BY_CODE_SQL =
		"SELECT RTRIM(SNAME) AS name FROM letd.vag_sob WHERE LOC_KOD = :code";
	
	public String getSNameByLocKod(int code) 
	{
		String name = null;
		try
		{
			name = (String)getNamedParameterJdbcTemplate().queryForObject(GET_NAME_BY_CODE_SQL, 
					new MapSqlParameterSource("code", code), String.class);
		}
		catch(IncorrectResultSizeDataAccessException e)
		{
			throw new ServiceException(e, ServiceCode.ERR_VAGSOB_EMPTY,
					"Vagsob with  code " + code + " does not exist");
		}
		return name;
	}



}

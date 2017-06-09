package ru.aisa.rgd.ws.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcDaoSupport;
import org.springframework.jdbc.core.simple.ParameterizedRowMapper;

import ru.aisa.rgd.ws.domain.Persona;
import ru.aisa.rgd.ws.exeption.ServiceError;
import ru.aisa.rgd.ws.exeption.ServiceException;
import ru.aisa.rgd.ws.utility.TypeConverter;

public class JdbcPersonaDao extends NamedParameterJdbcDaoSupport implements
		PersonaDao {
	
	class PersonaMapper implements ParameterizedRowMapper<Persona>
	{
		public Persona mapRow(ResultSet rs, int n) throws SQLException 
		{
			Persona pers = new Persona();
			pers.setId(rs.getInt("id"));
			pers.setDepId(rs.getInt("depId"));
			pers.setWrkNow(rs.getInt("wrkNow"));
			pers.setFName(rs.getString("fName"));
			pers.setMName(rs.getString("mName"));
			pers.setLName(rs.getString("lName"));
			pers.setPublicKey(rs.getBlob("publicKey"));
			pers.setCertSerial(rs.getString("certSerial"));
			pers.setDuty(rs.getString("Duty"));
			return pers;
		}

	}

	public JdbcPersonaDao(DataSource ds) {
		super();
		setDataSource(ds);
	}

	private static final String SELECT_BY_ID_SQL = 
		"SELECT  id, depId, wrkNow, RTRIM(fName) as fName, RTRIM(mName) as mName, " +
		" RTRIM(lName) as lName, publicKey , certSerial, "+
		"case when (select title from snt.personall where id =:id) is null or (select title from snt.personall where id =:id)='' then "+
		"(select rtrim(name) from snt.wrkname where id = "+
		"(select wrkid from snt.perswrk where pid = :id fetch first row only)) "+
		"else (select title from snt.personall where id =:id) end as Duty " +
		"FROM snt.personall WHERE id = :id";
	
	
	private static final String	SELECT_BY_CERT_ID_SQL	= "SELECT  id, depId, wrkNow, RTRIM(fName) as fName, RTRIM(mName) as mName, "
			+ " RTRIM(lName) as lName, publicKey , certSerial FROM snt.personall WHERE certSerial = :certId";

	private static final String	SELECT_PRED_BY_INN_KPP_SQL	= "select id from snt.pred where " +
			"inn =:inn and kpp=:kpp and headid is null";
	
	private static final String SELECT_ID_BY_FIO = "SELECT id FROM snt.personall WHERE FNAME = :fName and MNAME = :mName and LNAME = :lName FETCH FIRST";
	
	private static final String SELECT_ID_FICT_BY_FIO = "SELECT id FROM snt.personall WHERE FNAME = :fName and MNAME = :mName and LNAME = :lName and FICT = 1";
	
	private static final String CREATE_NEW_FICT_USER = "INSERT INTO snt.personall (ID, DEPID, FNAME, MNAME, LNAME, FICT) " +
	"VALUES (((select MAX(id) from snt.personall) + 1), 1, :fName, :mName, :lName, 1)";
	
	private static final String SELECT_COUNT_ID_BY_FIO = "SELECT count(id) FROM snt.personall WHERE FNAME = :fName and MNAME = :mName and LNAME = :lName and FICT = 1";
	
	
	public Persona getById(int id) throws ServiceException 
	{
		Persona persona = null;
		try
		{
			persona = (Persona) getNamedParameterJdbcTemplate().queryForObject(SELECT_BY_ID_SQL, 
				new MapSqlParameterSource("id", id), new PersonaMapper());
		}
		catch (IncorrectResultSizeDataAccessException e)
		{
			throw new ServiceException("Invalid argument. ID", ServiceError.ERR_UNDEFINED);
		}
		return persona;
	}

	public Persona getByCertId(String cert) throws ServiceException
	{
	Persona persona = null;
	try
		{
//		System.out.println("1");
		Map<String, Object> map = new HashMap<String, Object>(1);
		map.put("certId", cert);
		
		persona = (Persona) getNamedParameterJdbcTemplate().queryForObject(
				SELECT_BY_CERT_ID_SQL, map, new PersonaMapper());
		}
	catch (IncorrectResultSizeDataAccessException e)
		{
		System.out.println("catch");
		throw new ServiceException("Invalid argument. ID",
				ServiceError.ERR_UNDEFINED);
		}
	return persona;
	}
	
	public int getByFIO (String fName, String mName, String lName) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("fName", fName);
		params.put("mName", mName);
		params.put("lName", lName);
		int persId = getNamedParameterJdbcTemplate().queryForInt(SELECT_ID_BY_FIO, params);
		return persId;
	}
	
	public int createFictUser (String fName, String mName, String lName) {
		Integer id = null;
		try{
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("fName", fName);
		params.put("mName", mName);
		params.put("lName", lName);
		id = getNamedParameterJdbcTemplate().update(CREATE_NEW_FICT_USER, params);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return id;
	}
	
	public int getFictCountByFIO (String fName, String mName, String lName) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("fName", fName);
		params.put("mName", mName);
		params.put("lName", lName);
		int count = getNamedParameterJdbcTemplate().queryForInt(SELECT_COUNT_ID_BY_FIO, params);
		return count;
	}
	
	public int getFictByFIO (String fName, String mName, String lName) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("fName", fName);
		params.put("mName", mName);
		params.put("lName", lName);
		int persId = getNamedParameterJdbcTemplate().queryForInt(SELECT_ID_FICT_BY_FIO, params);
		return persId;
	}
	
	public int getPredId(String inn, String kpp) throws ServiceException
	{
	int predid = -1;
	try
		{
//		System.out.println("1");
		Map<String, Object> map = new HashMap<String, Object>(1);
		map.put("inn", inn);
		map.put("kpp", kpp);
		predid = getNamedParameterJdbcTemplate().queryForInt(SELECT_PRED_BY_INN_KPP_SQL, map);
		}
	catch (IncorrectResultSizeDataAccessException e)
		{
//		System.out.println("catch");
		throw new ServiceException("Invalid argument. INN, KPP",
				ServiceError.ERR_UNDEFINED);
		}
	return predid;
	}
	
	
}

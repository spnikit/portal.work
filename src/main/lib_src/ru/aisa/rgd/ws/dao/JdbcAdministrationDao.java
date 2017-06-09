package ru.aisa.rgd.ws.dao;

import javax.sql.DataSource;

import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcDaoSupport;

import ru.aisa.rgd.ws.exeption.ServiceCode;
import ru.aisa.rgd.ws.exeption.ServiceException;

public class JdbcAdministrationDao extends NamedParameterJdbcDaoSupport implements AdministrationDao {

	public JdbcAdministrationDao(DataSource ds) 
	{
		super();
		setDataSource(ds);
	}
	
	
	static final private String GET_NAME_BY_ID =
		"select rtrim(name) as name from letd.admjd where id = :id";
	
	public String getNameById(int id)
	{
		String name = null;
		try
		{
			name =  (String) getNamedParameterJdbcTemplate().queryForObject(GET_NAME_BY_ID, new MapSqlParameterSource("id", id), String.class);
		}
		catch(IncorrectResultSizeDataAccessException e)
		{
			throw new ServiceException(e, ServiceCode.ERR_ADMINISTRATION_EMPTY,
					"Administraion with the id " + id + " does not exist");
		}
		return name;
	}

}

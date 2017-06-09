package ru.aisa.rgd.ws.dao;

import java.sql.ResultSet;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcDaoSupport;
import org.springframework.jdbc.core.simple.ParameterizedRowMapper;

import ru.aisa.rgd.ws.domain.Template;
import ru.aisa.rgd.ws.exeption.ServiceError;
import ru.aisa.rgd.ws.exeption.ServiceException;

public class JdbcTemplateDao extends NamedParameterJdbcDaoSupport implements
		TemplateDao {
	
	public JdbcTemplateDao(DataSource ds) {
		super();
		setDataSource(ds);
	}
		
	static class Mapper implements ParameterizedRowMapper<Template>
	{
		public Template mapRow(ResultSet rs, int n) throws SQLException 
		{
			Template t = new Template();
			t.setId(rs.getInt("id"));
			t.setAccCnt(rs.getInt("accCnt"));
			t.setFlowCnt(rs.getInt("flowCnt"));
			t.setName(rs.getString("name"));
			t.setPType(rs.getInt("pType"));
			t.setTemplate(rs.getBytes("template"));
			return t;
		}
	}
	private static final String
		SELECT_BY_NAME = "SELECT * FROM letd.doctype WHERE name = :name";
	
	

	/* (non-Javadoc)
	 * @see ru.aisa.rgd.ws.dao.TemplateDao#getByName(java.lang.String)
	 */
	public Template getByName(String name) 
	{
		Template t = null;
		try
		{
			t = (Template)getNamedParameterJdbcTemplate().queryForObject(SELECT_BY_NAME, 
				new MapSqlParameterSource("name", name), new Mapper());
		}
		catch (IncorrectResultSizeDataAccessException  e)
		{
			throw new ServiceException(e, ServiceError.ERR_TEMPLATE_EMPTY);
		}
		return t;
	}

}

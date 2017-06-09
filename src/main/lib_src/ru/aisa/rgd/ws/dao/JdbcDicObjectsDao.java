package ru.aisa.rgd.ws.dao;

import java.sql.ResultSet;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcDaoSupport;
import org.springframework.jdbc.core.simple.ParameterizedRowMapper;

import ru.aisa.rgd.ws.domain.DicObjects;
import ru.aisa.rgd.ws.exeption.ServiceCode;
import ru.aisa.rgd.ws.exeption.ServiceException;

public class JdbcDicObjectsDao extends NamedParameterJdbcDaoSupport implements
		DicObjectsDao {
	
	static class Mapper implements ParameterizedRowMapper<DicObjects>
	{
		public DicObjects mapRow(ResultSet rs, int n) throws SQLException 
		{
			DicObjects e = new DicObjects();

			e.setActiv(rs.getString("activ"));
			e.setClassId(rs.getInt("class_id"));
			e.setName(rs.getString("object_name"));
			e.setObjectId(rs.getInt("object_id"));
			e.setObjectKod(rs.getInt("object_kod"));
			e.setObjectKodstr(rs.getString("object_kodstr"));
			e.setRefer(rs.getInt("refer"));
			e.setSName(rs.getString("object_sname"));
			e.setVName(rs.getString("object_vname"));
			
			return e;
		}
	}
	

	public JdbcDicObjectsDao(DataSource ds) 
	{
		super();
		setDataSource(ds);
	}

	static final String SELECT_BY_CODE = 
		"SELECT object_id, class_id, object_kod, RTRIM(object_kodstr)as object_kodstr, RTRIM(object_vname) as object_vname, RTRIM(object_name) as object_name,"
		+ " RTRIM(object_sname) as object_sname, refer, RTRIM(activ) as activ" +
		" FROM letd.dic_objects WHERE object_kod = :code FETCH FIRST 1 ROW ONLY";
	
	static final String SELECT_BY_CODE_AND_CLASSID = 
		"SELECT object_id, class_id, object_kod, RTRIM(object_kodstr)as object_kodstr, RTRIM(object_vname) as object_vname, RTRIM(object_name) as object_name,"
		+ " RTRIM(object_sname) as object_sname, refer, RTRIM(activ) as activ" +
		" FROM letd.dic_objects WHERE object_kod = :code and class_id= :classid FETCH FIRST 1 ROW ONLY";
	
	public DicObjects getByCode(int code) throws ServiceException 
	{
		DicObjects dicOb = null;
		try
		{
			dicOb = (DicObjects) getNamedParameterJdbcTemplate().queryForObject(SELECT_BY_CODE, 
				new MapSqlParameterSource("code", code), new Mapper());
		}
		catch(IncorrectResultSizeDataAccessException e)
		{
			throw new ServiceException(e, ServiceCode.ERR_DIC_OBJECTS_EMPTY,
					"DicObjects with code " + code + "  does not exist");
		}
		return dicOb;
	}

	public DicObjects getByCodeAndClassID(int code, int classid) throws ServiceException 
	{
		DicObjects dicOb = null;
		MapSqlParameterSource msps = new MapSqlParameterSource();
		msps.addValue("code", code);
		msps.addValue("classid", classid);
		try
		{
			dicOb = (DicObjects) getNamedParameterJdbcTemplate().queryForObject(SELECT_BY_CODE_AND_CLASSID, 
				msps, new Mapper());
		}
		catch(IncorrectResultSizeDataAccessException e)
		{
			throw new ServiceException(e, ServiceCode.ERR_DIC_OBJECTS_EMPTY,
					"DicObjects with code " + Integer.toString(code) + " and class_id = "+Integer.toString(classid)+ "  does not exist");
		}
		return dicOb;
	}
	
}

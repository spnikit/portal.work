package ru.aisa.rgd.etd.dao;

import java.math.BigInteger;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;

import javax.sql.DataSource;

import org.apache.commons.logging.Log;
import org.apache.log4j.Logger;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcDaoSupport;
import org.springframework.jdbc.core.simple.ParameterizedRowMapper;

import ru.aisa.rgd.etd.objects.ETDUserInformation;
import ru.aisa.rgd.etd.objects.ETDWorkInformation;

public class ETDUserDao extends NamedParameterJdbcDaoSupport {
	
	protected  Logger	logger	= Logger.getLogger(getClass());

	public ETDUserDao(DataSource ds) {
		super();
		setDataSource(ds);
	}
	
	static private final String 
	GET_ID_NAME_DEP_AUTO_SQL = 
		"SELECT id,  RTRIM(fname) || ' ' || RTRIM(mname) || ' ' || RTRIM(lname) as name , " +
		"(SELECT RTRIM(name) FROM SNT.department WHERE id = ps.DEPID) AS dep, autosgn, issgn  from " +
		"SNT.Personall AS ps WHERE certserial = :certid with ur",
	GET_NEWS = 
		"select news from SNT.news with ur",
	GET_ISNEWS = 
		"select NREAD from SNT.personall where certserial=:certid with ur",
	GET_WORK_INFORMATION_SQL =
		"with a as (select wrkid, " +
		"(select rtrim(cast(name as char(200) ccsid unicode))  from " +
		"SNT.wrkname where id = wrkid) as wrkname, predid, " +
		"(select rtrim(cast(vname as char(120) ccsid unicode)) from " +
		"SNT.pred where id = predid) as predname, " +
		"case when (select headid from snt.pred where id = predid) is null "+	
		"then predid else (select headid from snt.pred where id = predid) end as headpredid, "+
		"case when (select headid from snt.pred where id = predid) is null "+
		"then (select rtrim(cast(vname as char(120) ccsid unicode)) from "+
		"SNT.pred where id = predid) else (select rtrim(cast(vname as char(120) ccsid unicode)) from "+
		"SNT.pred where id = (select headid from snt.pred where id = predid)) end as headpredname, "+
		"(select expdoc from "+
		"SNT.pred where id = predid) as expdoc, "+
		"(select pdfcheck from "+
		"SNT.pred where id = predid) as pdfcheck, "+
		"(select isself from SNT.pred where id = predid) as isself , " +
		"(select kod from SNT.pred where id = predid) as kod ,   " +
		"(select issm from SNT.wrkname where id = wrkid) as issm " +
		"from SNT.perswrk " +
		"where pid = :PID) " +
		"select wrkid,wrkname,predid,predname,lower(predname) as lpredname, headpredid, headpredname, " +
		"isself,kod,issm, expdoc, pdfcheck from a order by lpredname,wrkname with ur",

	GET_FORM_TYPES_SQL =
		"SELECT id, RTRIM(name) AS name FROM SNT.doctype WHERE id IN " +
		"(SELECT dtid FROM SNT.doctypeacc WHERE wrkid = :wrkid AND (cview = 1 OR cedit = 1)) " +
		"ORDER BY name with ur";

	
		
	
	public ETDUserInformation getUserInformation(String certificateId)
	{
		final ETDUserInformation user = new ETDUserInformation();
		
		getNamedParameterJdbcTemplate().query(GET_ID_NAME_DEP_AUTO_SQL, 
				new MapSqlParameterSource("certid", certificateId), new ParameterizedRowMapper<Object>(){
					public Object mapRow(ResultSet rs, int numrow) throws SQLException {
						user.setId(rs.getInt("id"));
						user.setName(rs.getString("name"));
						user.setDepartmentName(rs.getString("dep"));
						
						if (rs.getInt("AUTOSGN")==1)
						user.setAutosgn(true);
						else 
							user.setAutosgn(false);
						
						if (rs.getInt("ISSGN")==1)
							user.setIssgn(true);
							else 
								user.setIssgn(false);
						return null;
					}
		});
		
		getNamedParameterJdbcTemplate().query(GET_NEWS, 
				new HashMap(), new ParameterizedRowMapper<Object>(){
					public Object mapRow(ResultSet rs, int numrow) throws SQLException {
						user.setNews(rs.getString("news"));
						return null;
					}
		});
		
		
		getNamedParameterJdbcTemplate().query(GET_ISNEWS, 
				new MapSqlParameterSource("certid", certificateId), new ParameterizedRowMapper<Object>(){
					public Object mapRow(ResultSet rs, int numrow) throws SQLException {
						user.setIsNews(rs.getInt("NREAD"));
						return null;
					}
		});
		
		
		getNamedParameterJdbcTemplate().query(GET_WORK_INFORMATION_SQL, 
				new MapSqlParameterSource("PID", user.getId()), new ParameterizedRowMapper<Object>(){
					public Object mapRow(ResultSet rs, int numrow) throws SQLException 
					{
						ETDWorkInformation wi = user.addWorkInformation();
						
						wi.setPredId(rs.getInt("predid"));
						wi.setWorkId(rs.getInt("wrkid"));
						wi.setWorkName(rs.getString("wrkname"));
						wi.setFr(rs.getInt("issm"));
						
						//FIXME не забыть поменять на то что сверху
						wi.setPredName(rs.getString("predname"));
						wi.setPredRZD(rs.getString("isself"));
						//wi.setDorId(rs.getInt("dorid"));
						//wi.setDorName(rs.getString("dorname"));
						wi.setPredCode(rs.getInt("kod"));
						wi.setHeadpredId(rs.getInt("headpredid"));
						wi.setHeadpredName(rs.getString("headpredname"));
						wi.setExpdoc(rs.getBoolean("expdoc"));
						wi.setPdfcheck(rs.getBoolean("pdfcheck"));
						return null;
					}
		});
		
		//TODO Попытаться сделать нормально ))
		final List<ETDWorkInformation> l = user.getWorkInfo();
		for (int i = 0; i < l.size(); i++) {
			final int cur = i;
			boolean found = false;
			
			//check if already requested this role
			for (int j = 0; j < cur; j++) {
				if (l.get(j).getWorkId().equals(l.get(cur).getWorkId())){
					if (l.get(j).getAvailableForms().length()>2) l.get(cur).addAvailableForm(l.get(j).getAvailableForms().substring(2));
					found = true;
					break;
				}
			}
			
			if (!found)		
			getNamedParameterJdbcTemplate().query(GET_FORM_TYPES_SQL, 
					new MapSqlParameterSource("wrkid", l.get(cur).getWorkId()), new ParameterizedRowMapper<Object>(){
						public Object mapRow(ResultSet rs, int numrow) throws SQLException 
						{
							l.get(cur).addAvailableForm(rs.getString("name"));
							return null;
						}
				});
				
		}

		return user;
	}
	
	private static final String GET_ID_BY_CERTIFICATE =
		"select id from SNT.personall where CERTSERIAL = :CERTSERIAL with ur";
	
	private static final String GET_AUTOSGN_BY_CERTIFICATE =
			"select autosgn from SNT.personall where CERTSERIAL = :CERTSERIAL with ur";
		
	
	
	public int getIdByCertificateSerial(String certificateSerial)
	{
		String cert = new BigInteger(certificateSerial,16).toString();
		logger.debug("Loading user id with certificate serial " + cert);

		int id = getNamedParameterJdbcTemplate().queryForInt(GET_ID_BY_CERTIFICATE,
				new MapSqlParameterSource("CERTSERIAL", cert));
		
		logger.debug("Loaded id " + id);
		return id;
	}
}

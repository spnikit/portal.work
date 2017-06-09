package ru.aisa.rgd.ws.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcDaoSupport;
import org.springframework.jdbc.core.simple.ParameterizedRowMapper;

import ru.aisa.rgd.ws.domain.MRMvag;
import ru.aisa.rgd.ws.exeption.ServiceException;

public class JdbcMRMDao extends NamedParameterJdbcDaoSupport implements MRMDao{
	public static final String lastBulidDate = "16.07.2013 13:53:04";
	public JdbcMRMDao(DataSource ds) 
	{
		super();
		setDataSource(ds);
	}
	
	class MRMvagMapper implements ParameterizedRowMapper<MRMvag>
	{
	public MRMvag mapRow(ResultSet rs, int n) throws SQLException
		{
		MRMvag mrm = new MRMvag();
		mrm.setIdSes(rs.getInt("ID_SES"));
		mrm.setIdDev(rs.getString("ID_DEV"));
		mrm.setFio(rs.getString("FIO"));
		mrm.setTabelNum(rs.getString("TABEL_NUM"));
		mrm.setCardNum(rs.getString("CARD_NUM"));
		mrm.setPersId(rs.getInt("PERSID"));
		mrm.setCnsiDor(rs.getInt("CNSI_DOR"));
		mrm.setCnsiPred(rs.getInt("CNSI_PRED"));
		mrm.setOpendate(rs.getTimestamp("OPENDATE"));
		if (rs.getTimestamp("CLOSEDATE") == null)
			{
			mrm.setClosedate(null);
			}
		else
			{
			mrm.setClosedate(rs.getTimestamp("CLOSEDATE"));
			}
		return mrm;
		}
	
	}
	
	
	public static final int datatypeVag = 10010;
	
	public static final int datatypeEKasuiP = 10020;
	public static final int datatypeEKasuiNSI = 12000;
	public static final int datatypeEKasuiNSIByDor = 20000;
	
	
	//private static final String	SQL_GENERATE_ID_SES	= "select max(ID_SES)+1 from LETD.MRM_SESSIONS";
	private static final String	SQL_GENERATE_ID_SES	= "SELECT NEXT VALUE FOR snt.mrmsess_seq FROM SYSIBM.SYSDUMMY1";
	
	private static final String	INSERT_MRM	= "insert into snt.MRM_SESSIONS (ID_SES,ID_DEV,FIO,TABEL_NUM,CARD_NUM,PERSID,CNSI_DOR,CNSI_PRED,OPENDATE) " +
			"values(:id_ses,:id_dev,:fio,:tabel_num,:card_num,:persid,:cnsi_dor,:cnsi_pred,current timestamp)";
	
	private static final String GET_id_ses = "select id_ses from snt.mrm_sessions where id_ses=:id_ses";
	
	private static final String GET_ses = "select * from snt.mrm_sessions where id_ses=:id_ses";
	
	private static final String GET_CLOSEDATE = "select CLOSEDATE from snt.mrm_sessions where id_ses=:id_ses";
	
	private static final String CLOSE_SES = "update snt.mrm_sessions set closedate=current timestamp where id_ses=:id_ses";
	
	
	public int generateIdSes() throws Exception
	{
	return getNamedParameterJdbcTemplate().queryForInt(SQL_GENERATE_ID_SES,
			new HashMap<String, Object>(1));
	}
	
//	public void createNewSes(final int id_ses, final String id_dev, final String fio, final int tabel_num, 
//			final String card_num, final int persid, final int cnsi_dor, final int cnsi_pred, final String opendate) throws Exception
//	{
//		Map<String, Object> map = new HashMap<String, Object>();
//		map.put("id_ses", id_ses);
//		map.put("id_dev", id_dev);
//		map.put("fio", fio);
//		map.put("tabel_num", tabel_num);
//		map.put("card_num", card_num);
//		map.put("persid", persid);
//		map.put("cnsi_dor", cnsi_dor);
//		map.put("cnsi_pred", cnsi_pred);
//		Date date = new SimpleDateFormat("dd.MM.yyyy HH:mm").parse(opendate);
//		map.put("opendate", date);
//		getNamedParameterJdbcTemplate().update(INSERT_MRM, map);
//	}
	
	public void createNewSes(final MRMvag mrmvag){
		
		Map<String, Object> input = mrmvag.map();
		getNamedParameterJdbcTemplate().update(INSERT_MRM, input);
		getNamedParameterJdbcTemplate().queryForObject(
				"select opendate from snt.MRM_SESSIONS where id_ses = :id_ses", input,
				new ParameterizedRowMapper<Object>()
					{
						
						public Object mapRow(ResultSet rs, int n) throws SQLException
							{
							mrmvag.setOpendate(rs.getTimestamp("opendate"));
							return null;
							}
					});
	}
	
	

	public Timestamp getCloseDate(int id_ses) throws DataAccessException {
//		//проверка самой сессии
//		try{
//			getNamedParameterJdbcTemplate().queryForInt(GET_id_ses, new MapSqlParameterSource("id_ses", id_ses));
//		}catch (Exception e) {
//			throw new ServiceException(new Exception(),ServiceCode.ERR_UNDEFINED,
//					"Сессии с id_ses = "+id_ses+" не существует");
//		}
		MRMvag mrmvag = getSes(id_ses);
		//проверка сессии на закрытие		
		Timestamp cldate = mrmvag.getClosedate();
//		try{
//			cldate = getNamedParameterJdbcTemplate().queryForObject(GET_CLOSEDATE, 
//					new MapSqlParameterSource("id_ses", id_ses), String.class).toString();
//		}catch (Exception e) {
//			cldate = "";
//		}
		if (cldate != null) throw new ServiceException(new Exception(),-4,
				"Сессия с id_ses = "+id_ses+" закрыта");
		
		return cldate;
	}
	
	public MRMvag getSes(int id_ses) throws DataAccessException {
		//проверка самой сессии
//		int idses=0;
//		try{
//			id_ses = getNamedParameterJdbcTemplate().queryForInt(GET_id_ses, new MapSqlParameterSource("id_ses", id_ses));
//		}catch (Exception e) {
//			throw new ServiceException(new Exception(),ServiceCode.ERR_UNDEFINED,
//					"Сессии с id_ses = "+id_ses+" не существует");
//		}
		
		MRMvag mrmvag = null;
		try{
		mrmvag = (MRMvag) getNamedParameterJdbcTemplate().queryForObject(
				GET_ses, new MapSqlParameterSource("id_ses", id_ses),
				new MRMvagMapper());
		}catch (Exception e) {
			throw new ServiceException(new Exception(),-5,
					"Сессии с id_ses = "+id_ses+" не существует");
		}
		return mrmvag;
	}

	public void closeSes(final MRMvag mrmvag) throws Exception {
		
		getNamedParameterJdbcTemplate().update(CLOSE_SES, mrmvag.map());
		getNamedParameterJdbcTemplate().queryForObject(
				"select closedate from snt.MRM_SESSIONS where id_ses = :id_ses", mrmvag.map(),
				new ParameterizedRowMapper<Object>()
					{
						
						public Object mapRow(ResultSet rs, int n) throws SQLException
							{
							mrmvag.setClosedate(rs.getTimestamp("closedate"));
							return null;
							}
					});
		
	}


}


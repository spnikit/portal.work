package ru.aisa.rgd.ws.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.log4j.Logger;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.SqlOutParameter;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcDaoSupport;
import org.springframework.jdbc.core.simple.ParameterizedRowMapper;
import org.springframework.jdbc.object.StoredProcedure;
import org.springframework.transaction.support.TransactionTemplate;

import ru.aisa.rgd.ws.domain.Enterprise;
import ru.aisa.rgd.ws.exeption.ServiceCode;
import ru.aisa.rgd.ws.exeption.ServiceException;

public class JdbcEnterpriseDao extends NamedParameterJdbcDaoSupport implements
		EnterpriseDao {
	
	protected final Logger	log	= Logger.getLogger(getClass());
	
	private TransactionTemplate transactionTemplate;
	
	//	SQl statements ////////////////////////////////////////////////////////
	protected static final String
	SELECT_STATION_NAME = 
		"select RTRIM(vname) from LETD.PRED where type=:type and kod = :stationCode and activ = 'Y' with ur",
		
	SELECT_SHORT_NAME = 
		"select RTRIM(name) from LETD.PRED where type=:type and kod = :stationCode and activ = 'Y' with ur",
		
	SELECT_FULL_NAME = 
		"select  rtrim(vname) || ' ж.д. ' || rtrim(name)" +
		" from (select vname,dorid from letd.pred where kod = :stationCode and type = :type and activ = 'Y')" +
		" as a,letd.dor d where id = dorid  with ur",
		
	SELECT_ADDITIONAL_NAME = 
		"select RTRIM(oname) from LETD.PRED where type=:type and kod = :stationCode and activ = 'Y' with ur",
		
	
	
	SELECT_URL_BY_DOCID =
		"SELECT rtrim(url) FROM letd.pred WHERE id = " +
		"(SELECT predid FROM letd.docstore WHERE id = :docid) with ur";
	
	// DBRM StoredPocedure /////////////////////////////////////////////////////// 
	 static class Procedure extends StoredProcedure 
	 {
		private static final String 
			SPROC_NAME = "LETD.GetStId_Kod",
			PARAM_CODE = "rw_station_kod",
			PARAM_ID = "rw_station_id",
			PARAM_TYPE = "sType";
		
		public Procedure(DataSource dataSource) 
		{
			super(dataSource, SPROC_NAME);
			setFunction(false);
			
			declareParameter(new SqlParameter(PARAM_TYPE, Types.INTEGER));
			declareParameter(new SqlParameter(PARAM_CODE, Types.INTEGER));
			declareParameter(new SqlOutParameter(PARAM_ID, Types.INTEGER));
			
			compile();
		}
		
		public Integer execute(int code, int type) 
		{
			Map<String, Integer> input = new HashMap<String, Integer>(3);
			input.put(PARAM_CODE, code);
			input.put(PARAM_TYPE, type);
			
			Map output = super.execute(input);
			
			return (Integer)output.get(PARAM_ID);
		}
	}
	 
	// ORM Mapping for Enterprise object /////////////////////////////////////////
	static class Mapper implements ParameterizedRowMapper<Enterprise>
	{
		public Enterprise mapRow(ResultSet rs, int n) throws SQLException 
		{
			Enterprise e = new Enterprise();

			e.setCode(rs.getInt("kod"));
			e.setDorId(rs.getInt("dorId"));
			e.setId(rs.getInt("id"));
			e.setIsSelf(rs.getString("isSelf"));
			e.setName(rs.getString("name"));
			e.setStpRim(rs.getInt("stpRim"));
			e.setType(rs.getInt("type"));
			e.setUrl(rs.getString("url"));
			e.setVName(rs.getString("vName"));
			e.setZoneCnt(rs.getInt("zoneCnt")); 
			
			return e;
		}
	}
	
	// Constructor
	
	public JdbcEnterpriseDao(DataSource ds) 
	{
		super();
		setDataSource(ds);
	}

	public TransactionTemplate getTransactionTemplate() {
		return transactionTemplate;
	}

	public void setTransactionTemplate(TransactionTemplate transactionTemplate) {
		this.transactionTemplate = transactionTemplate;
	}

	/* (non-Javadoc)
	 * @see ru.aisa.rgd.ws.dao.EnterpriseDao#getName(int, int)
	 */
	public String getName(int type, int code) throws ServiceException
	{
		String name = null;
		try
		{
			HashMap<String, Integer> map = new HashMap<String, Integer>(2);
			map.put("stationCode",code);
			map.put("type", type);
			
			name = (String)getNamedParameterJdbcTemplate().queryForObject(SELECT_STATION_NAME, map, String.class);
		}
		catch(IncorrectResultSizeDataAccessException e)
		{
			throw new ServiceException(e, ServiceCode.ERR_STATIONID_EMPTY,
					"Enperprise with the code " + code + " and type " + type + " does not exist");
		}
		
		return name;
	}
	
	/* (non-Javadoc)
	 * @see ru.aisa.rgd.ws.dao.EnterpriseDao#getName(int, int)
	 */
	public String getAdditionalName(int type, int code) throws ServiceException
	{
		String name = null;
		try
		{
			HashMap<String, Integer> map = new HashMap<String, Integer>(2);
			map.put("stationCode",code);
			map.put("type", type);
			
			name = (String)getNamedParameterJdbcTemplate().queryForObject(SELECT_ADDITIONAL_NAME, map, String.class);
		}
		catch(IncorrectResultSizeDataAccessException e)
		{
			throw new ServiceException(e, ServiceCode.ERR_STATIONID_EMPTY,
					"Enperprise with the code " + code + " and type " + type + " does not exist");
		}
		
		return name;
	}
	
	/* (non-Javadoc)
	 * @see ru.aisa.rgd.ws.dao.EnterpriseDao#getId(int, int)
	 */
	public Integer getId(int type, int code) throws ServiceException 
	{
		Integer id = null;
	
		Procedure pr = new Procedure(getDataSource());
		id = pr.execute(code, type);
		if (id == null) throw new ServiceException(new EmptyResultDataAccessException(1), ServiceCode.ERR_STATIONID_EMPTY,
				"Enperprise with the code " + code + " and type " + type + " does not exist");
		
		return id;
	}

	/*
	 * (non-Javadoc)
	 * @see ru.aisa.rgd.ws.dao.EnterpriseDao#getFullName(int, int)
	 */
	public String getFullName(int type, int code) throws ServiceException 
	{
		String name = null;
		try
		{
			HashMap<String, Integer> map = new HashMap<String, Integer>(2);
			map.put("stationCode",code);
			map.put("type", type);
			
			name = (String)getNamedParameterJdbcTemplate().queryForObject(SELECT_FULL_NAME, map, String.class);
		}
		catch(IncorrectResultSizeDataAccessException e)
		{
			throw new ServiceException(e, ServiceCode.ERR_STATIONID_EMPTY,
					"Enperprise with the code " + code + " and type " + type + " does not exist");
		}
		
		return name;
	}

	private static final String SELECT_ENTERPRISE = 
		"SELECT id, type, dorId, kod, RTRIM(vName) as vName, RTRIM(name) as name, RTRIM(oName) as oName, " +
		"url, isSelf, stpRim,	zoneCnt " +
		"FROM letd.pred WHERE type=:type AND kod=:code AND activ = 'Y' FETCH FIRST 1 ROW ONLY with ur";
	/* (non-Javadoc)
	 * @see ru.aisa.rgd.ws.dao.EnterpriseDao#get(int, int)
	 */
	public Enterprise get(int type, int code) throws ServiceException 
	{
		Enterprise ent = null;
		try
		{
			ent = (Enterprise) getNamedParameterJdbcTemplate().queryForObject(SELECT_ENTERPRISE, 
				new MapSqlParameterSource("type", type).addValue("code", code), new Mapper());
		}
		catch(IncorrectResultSizeDataAccessException e)
		{
			throw new ServiceException(e, ServiceCode.ERR_STATIONID_EMPTY,
					"Enperprise with the code " + code + " and type " + type + " does not exist");
		}
		return ent;
	}

	/* (non-Javadoc)
	 * @see ru.aisa.rgd.ws.dao.EnterpriseDao#getUrlByDocumentId(int)
	 */
	public String getUrlByDocumentId(Integer predid) 
	{
//		String select_pID_by_docid = "select predid from letd.docstore where id=:docid with ur";
		String select_url = "select rtrim(url) from letd.pred where id=:predid with ur";
		Map <String, Object> map = new HashMap<String, Object>();
//		map.put("docid", documentId);
//		Integer predid = (Integer) getNamedParameterJdbcTemplate().queryForInt(select_pID_by_docid, map);
		map.put("predid", predid);
		String url = (String) getNamedParameterJdbcTemplate().queryForObject(
				select_url, map, String.class);
//		System.out.println("URLLLLL=  "+url);
		return url;
	}

	/* (non-Javadoc)
	 * @see ru.aisa.rgd.ws.dao.EnterpriseDao#getShortName(int, int)
	 */
	public String getShortName(int type, int code) throws ServiceException 
	{
		String name = null;
		try
		{
			name = (String)getNamedParameterJdbcTemplate().queryForObject(SELECT_SHORT_NAME, 
					new MapSqlParameterSource("stationCode",code).addValue("type", type), String.class);
		}
		catch(IncorrectResultSizeDataAccessException e)
		{
			throw new ServiceException(e, ServiceCode.ERR_STATIONID_EMPTY,
					"Enperprise with the code " + code + " and type " + type + " does not exist");
		}
		
		return name;
	}

	static final String SELECT_ENTERPRISE_BY_ID = 
		"SELECT id, type, dorId, kod, RTRIM(vName) as vName, RTRIM(name) as name, " +
		"url, isSelf, stpRim,	zoneCnt " +
		"FROM snt.pred WHERE id = :id with ur";
	
	
	public Enterprise getById(int id) throws ServiceException 
	{
		Enterprise ent = null;
		try
		{
			ent = (Enterprise) getNamedParameterJdbcTemplate().queryForObject(SELECT_ENTERPRISE_BY_ID, 
				new MapSqlParameterSource("id", id), new Mapper());
		}
		catch(IncorrectResultSizeDataAccessException e)
		{
			throw new ServiceException(e, ServiceCode.ERR_STATIONID_EMPTY,
					"Enperprise with  ID " + id + " does not exist");
		}
		return ent;
	}

	
	
	static final String SELECT_ENTERPRISE_BY_CODE = 
		"SELECT id, type, dorId, kod, RTRIM(vName) as vName, RTRIM(name) as name, RTRIM(oName) as oName, " +
		"url, isSelf, stpRim,	zoneCnt " +
		"FROM letd.pred WHERE kod = :code AND activ = 'Y' FETCH FIRST 1 ROW ONLY with ur";
	public Enterprise getByCode(int code) throws ServiceException 
	{
		Enterprise ent = null;
		try
		{
			ent = (Enterprise) getNamedParameterJdbcTemplate().queryForObject(SELECT_ENTERPRISE_BY_CODE, 
				new MapSqlParameterSource("code", code), new Mapper());
		}
		catch(IncorrectResultSizeDataAccessException e)
		{
			throw new ServiceException(e, ServiceCode.ERR_STATIONID_EMPTY,
					"Enperprise with code " + code + "  does not exist");
		}
		return ent;
	}

	static final String SELECT_STATION_NAME_BY_CODE = 
		"select RTRIM(vname) from LETD.PRED where kod = :code AND activ = 'Y' FETCH FIRST 1 ROW ONLY with ur";
	public String getNameByCode(int code) throws ServiceException 
	{
		String name = null;
		try
		{
			name = (String)getNamedParameterJdbcTemplate().queryForObject(SELECT_STATION_NAME_BY_CODE, 
					new MapSqlParameterSource("code",code), String.class);
		}
		catch(IncorrectResultSizeDataAccessException e)
		{
			throw new ServiceException(e, ServiceCode.ERR_STATIONID_EMPTY,
					"Enperprise with the code " + code + " does not exist");
		}
		
		return name;
	}
	
	static final String SELECT_NAME_BY_ID = 
		"select rtrim(vname) from letd.pred where id = :PREDID with ur";
	public String getNameById(int id) throws ServiceException 
	{
		String name = null;
		try
		{
			name = (String)getNamedParameterJdbcTemplate().queryForObject(SELECT_NAME_BY_ID, 
					new MapSqlParameterSource("PREDID",id), String.class);
		}
		catch(IncorrectResultSizeDataAccessException e)
		{
			throw new ServiceException(e, ServiceCode.ERR_STATIONID_EMPTY,
					"Enperprise with the id " + id + " does not exist");
		}
		
		return name;
	}

	static final String SELECT_NAME_BY_CODE_AND_ROAD_ID = 
		"select RTRIM(vname) from LETD.PRED where kod = :code AND dorid = :roadId AND activ = 'Y' FETCH FIRST 1 ROW ONLY with ur";
	public String getNameByCodeAndRoadId(int code, int roadId) throws ServiceException 
	{
		String name = null;
		try
		{
			name = (String)getNamedParameterJdbcTemplate().queryForObject(SELECT_NAME_BY_CODE_AND_ROAD_ID, 
					new MapSqlParameterSource("code",code).
					addValue("roadId", roadId), String.class);
		}
		catch(IncorrectResultSizeDataAccessException e)
		{
			throw new ServiceException(e, ServiceCode.ERR_STATIONID_EMPTY,
					"Enperprise with the code " + code + " and road id "+ roadId +" does not exist");
		}
		
		return name;
	}
	
	static final String SELECT_NAME_BY_CODE_AND_TYPES = 
		"select RTRIM(vname) from LETD.PRED where kod = :code AND type in (:types) AND activ = 'Y' FETCH FIRST 1 ROW ONLY with ur";
	public String getNameByCodeAndType(int code, List<Integer> types) throws ServiceException 
	{
		String name = null;
		try
		{
			name = (String)getNamedParameterJdbcTemplate().queryForObject(SELECT_NAME_BY_CODE_AND_TYPES, 
					new MapSqlParameterSource("code",code).
					addValue("types", types), String.class);
		}
		catch(IncorrectResultSizeDataAccessException e)
		{
			throw new ServiceException(e, ServiceCode.ERR_STATIONID_EMPTY,
					"Enperprise with the code " + code + " and type in "+ String.valueOf(types) +" does not exist");
		}
		
		return name;
	}
	
	
	static final String SELECT_OKPO_KOD_BY_OTR_KOD_FROM_PRED_CNSI = 
		"SELECT okpo_kod as okpo_kod FROM LETD.PRED_CNSI "+
		"where otr_kod = :code FETCH FIRST 1 ROW ONLY with ur";
	
	public int getOkpoKodByOtrKod (int code) throws ServiceException{
		int okpo_kod;
		try {
			okpo_kod=(Integer)getNamedParameterJdbcTemplate().queryForInt(SELECT_OKPO_KOD_BY_OTR_KOD_FROM_PRED_CNSI, 
					new MapSqlParameterSource("code", code)) ;
		}
		catch (IncorrectResultSizeDataAccessException e)
		{
			throw new ServiceException(e, ServiceCode.ERR_OKPO_CODE_EMPTY, 
					"Okpo_kod for enterprise with the otr_kod "+ code +" does not exist");
		}
		return okpo_kod;
	}
	
	static final String SELECT_ENTERPRISE_BY_OTR_KOD_FROM_PRED_CNSI = 
		"SELECT pred_id as id, dor_kod as dorId, otr_kod as kod, RTRIM(vName) as vName, RTRIM(name) as name, RTRIM(sName) as oName "+
		"FROM LETD.PRED_CNSI " +
		"where otr_kod = :code FETCH FIRST 1 ROW ONLY with ur";
	
	public Enterprise getByOtrKod(int code) throws ServiceException{
		Enterprise ent = null;
		try {
			ent = (Enterprise) getNamedParameterJdbcTemplate().queryForObject(SELECT_ENTERPRISE_BY_OTR_KOD_FROM_PRED_CNSI, 
					new MapSqlParameterSource("code", code), new ParameterizedRowMapper<Enterprise>() {
				
				public Enterprise mapRow (ResultSet rs, int arg1) throws SQLException {
					Enterprise e = new Enterprise();
					e.setName(rs.getString("name"));
					e.setId(rs.getInt("id"));
					e.setVName(rs.getString("vName"));
					
					return e;
				}
					});
		}
		catch (IncorrectResultSizeDataAccessException e) {
			throw new ServiceException(e, ServiceCode.ERR_STATION_CODE_EMPTY, 
					"Enperprise with the code "+ code +" does not exist");
		}
		return ent;
	}
	
	
	static final String SELECT_ENTERPRISE_BY_OKPO_CODE_FROM_PRED_CNSI = 
		"SELECT RTRIM(name) as name FROM LETD.PRED_CNSI " +
		"where okpo_kod = :code FETCH FIRST 1 ROW ONLY with ur";
	
	public Enterprise getByOkpoKod(int code) throws ServiceException{
		Enterprise ent = null;
		try
		{
			ent = (Enterprise) getNamedParameterJdbcTemplate().queryForObject(SELECT_ENTERPRISE_BY_OKPO_CODE_FROM_PRED_CNSI, 
				new MapSqlParameterSource("code", code), new ParameterizedRowMapper<Enterprise>(){

					public Enterprise mapRow(ResultSet rs, int arg1) throws SQLException {
						Enterprise e = new Enterprise();
					
						e.setName(rs.getString("name")); 
						
						return e;
					}
				
			});
		}
		catch(IncorrectResultSizeDataAccessException e)
		{
			throw new ServiceException(e, ServiceCode.ERR_STATION_OKPO_KOD_EMPTY,
					"Enperprise with the okpo_code " + code + " does not exist");
		}
		return ent;
	}
	
	public Enterprise getByIdFromPredCnsi(int code) throws ServiceException{
		Enterprise ent = null;
		try
		{
			
			Map <String, Object> pp = new HashMap<String, Object>();
			pp.put("code", code);
			ent = (Enterprise) getNamedParameterJdbcTemplate().queryForObject("SELECT RTRIM(name) as name FROM LETD.PRED_CNSI " +
					"where pred_id =:code FETCH FIRST 1 ROW ONLY with ur", 
				pp, new ParameterizedRowMapper<Enterprise>(){

					public Enterprise mapRow(ResultSet rs, int arg1) throws SQLException {
						Enterprise e = new Enterprise();
					
						e.setName(rs.getString("name")); 
						
						return e;
					}
				
			});
		}
		catch(IncorrectResultSizeDataAccessException e)
		{
			throw new ServiceException(e, ServiceCode.ERR_STATIONID_EMPTY,
					"Enperprise with ID " + code + " does not exist");
		}
		return ent;
	}

	static final String SELECT_BY_CODE_AND_ROAD_ID = 
		"SELECT id, type, dorId, kod, RTRIM(vName) as vName, RTRIM(name) as name, RTRIM(oName) as oName, " +
		"url, isSelf, stpRim,	zoneCnt " +
		"FROM letd.pred WHERE kod = :code AND dorid = :roadId AND activ = 'Y' FETCH FIRST 1 ROW ONLY with ur";
	public Enterprise getByCodeAndRoadId(int code, int roadId) throws ServiceException 
	{
		Enterprise ent = null;
		try
		{
			ent = (Enterprise) getNamedParameterJdbcTemplate().queryForObject(SELECT_BY_CODE_AND_ROAD_ID, 
					new MapSqlParameterSource("code", code).
					addValue("roadId", roadId), new Mapper());
		}
		catch(IncorrectResultSizeDataAccessException e)
		{
			throw new ServiceException(e, ServiceCode.ERR_STATIONID_EMPTY,
					"Enperprise with code " + code + " and road id "+ roadId +"  does not exist");
		}
		return ent;
	}
	
	static final String SELECT_BY_CODE_AND_TYPES = 
		"SELECT id, type, dorId, kod, RTRIM(vName) as vName, RTRIM(name) as name, RTRIM(oName) as oName, " +
		"url, isSelf, stpRim,	zoneCnt " +
		"FROM letd.pred WHERE kod = :code AND type in (:types) AND activ = 'Y' FETCH FIRST 1 ROW ONLY with ur";
	public Enterprise getByCodeAndType(int code, List<Integer> types) throws ServiceException 
	{
		Enterprise ent = null;
		try
		{
			ent = (Enterprise) getNamedParameterJdbcTemplate().queryForObject(SELECT_BY_CODE_AND_TYPES, 
					new MapSqlParameterSource("code", code).
					addValue("types", types), new Mapper());
		}
		catch(IncorrectResultSizeDataAccessException e)
		{
			throw new ServiceException(e, ServiceCode.ERR_STATIONID_EMPTY,
					"Enperprise with code " + code + " and type in "+ String.valueOf(types) +"  does not exist");
		}
		return ent;
	}
	
	static final String SELECT_SHOP_BY_ID = 
		"SELECT id, type, dorId, kod, RTRIM(vName) as vName, RTRIM(name) as name, RTRIM(oName) as oName, " +
		"url, isSelf, stpRim,	zoneCnt " +
		"FROM letd.pred WHERE id = :id with ur";
	public Enterprise getShopById(int id) throws ServiceException 
	{
		id = id + 20000000;
		Enterprise ent = null;
		try
		{
			ent = (Enterprise) getNamedParameterJdbcTemplate().queryForObject(SELECT_ENTERPRISE_BY_ID, 
				new MapSqlParameterSource("id", id), new Mapper());
		}
		catch(IncorrectResultSizeDataAccessException e)
		{
			throw new ServiceException(e, ServiceCode.ERR_STATIONID_EMPTY,
					"Enperprise with  ID " + id + " does not exist");
		}
		return ent;
	}
	
	public int getKleimoByKodStation(int kod) throws ServiceException 
	{
		int kleimo;
		try
		{
			kleimo=(Integer)getNamedParameterJdbcTemplate().queryForInt(" select distinct ukp_kod as kod from letd.pred_rem where ukp_kod " +
					" in (select kod from letd.pred where id " +
					" in (select predid from letd.predzone where stanid"+
					" in (select id from letd.pred where kod=:KOD))) with ur", 
				new MapSqlParameterSource("KOD", kod));
		}
		catch(IncorrectResultSizeDataAccessException e)
		{
			throw new ServiceException(e, ServiceCode.ERR_STATION_OKPO_KOD_EMPTY,
					"Kleimo for Station with kod " + kod + " does not exist");
		}
		return kleimo;
	}
	
	static final String SELECT_FROM_OBJ_OSN_INF = 
		"SELECT obj_osn_id, vname, name FROM LETD.OBJ_OSN_INF " +
		"where obj_osn_id = :ID with ur";
	
	public Enterprise getFromObjOsnInf(int obj_osn_id) throws ServiceException{
		Enterprise ent = null;
		try
		{
			ent = (Enterprise) getNamedParameterJdbcTemplate().queryForObject(SELECT_FROM_OBJ_OSN_INF, 
				new MapSqlParameterSource("ID", obj_osn_id), new ParameterizedRowMapper<Enterprise>(){

					public Enterprise mapRow(ResultSet rs, int arg1) throws SQLException {
						Enterprise e = new Enterprise();
						
						e.setId(rs.getInt("obj_osn_id"));
						e.setName(rs.getString("name"));
						e.setVName(rs.getString("vName")); 
						
						return e;
					}
				
			});
		}
		catch(IncorrectResultSizeDataAccessException e)
		{
			throw new ServiceException(e, ServiceCode.ERR_STATIONID_EMPTY,
					"Enperprise with  ID " + obj_osn_id + " in OBJ_OSN_INF does not exist");
		}
		return ent;
	}

	
	
	static final String SELECT_ENTERPRISE_BY_KLEIMO = 
			"SELECT id, type, dorId, kod, RTRIM(vName) as vName, RTRIM(name) as name, " +
			"url, isSelf, stpRim,	zoneCnt " +
			"FROM snt.pred WHERE kleimo = :kleimo with ur";
	@Override
	public Enterprise getEnterpriseByKleimo(int kleimo) throws ServiceException {
		Enterprise ent = null;
		try
		{
			ent = (Enterprise) getNamedParameterJdbcTemplate().queryForObject(SELECT_ENTERPRISE_BY_KLEIMO, 
				new MapSqlParameterSource("kleimo", kleimo), new Mapper());
		}
		catch(IncorrectResultSizeDataAccessException e)
		{
				throw new ServiceException(e, ServiceCode.ERR_STATIONID_EMPTY,
					"Enperprise with  kleimo" + kleimo + " does not exist");
		}
		return ent;
	}

}

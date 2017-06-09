package ru.aisa.rgd.etd.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.log4j.Logger;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.SqlOutParameter;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcDaoSupport;
import org.springframework.jdbc.core.simple.ParameterizedRowMapper;
import org.springframework.jdbc.object.StoredProcedure;

import ru.aisa.rgd.etd.objects.ETDCounterparts;
import ru.aisa.rgd.etd.objects.ETDDocumentAccess;
import ru.aisa.rgd.etd.objects.ETDDocumentData;
import ru.aisa.rgd.etd.objects.ETDTemplate;

public class ETDFormDao extends NamedParameterJdbcDaoSupport {
	
	private static Logger	log	= Logger.getLogger(ETDDocumentDao.class);

	public ETDFormDao(DataSource ds) {
		super();
		setDataSource(ds);
	}
	
	class LockDocumentProcedure extends StoredProcedure 
	{
		private static final String 
			SPROC_NAME = "snt.LockDoc",
			PARAM_DOCID = "docid",
			PARAM_USERID = "userid",
			PARAM_LOCKID = "lockid",
			PARAM_LOCKNAME = "lockname";
		
		
		public LockDocumentProcedure(DataSource dataSource) 
		{
			super(dataSource, SPROC_NAME);
			setFunction(false);
			declareParameter(new SqlParameter(PARAM_DOCID, Types.BIGINT));
			declareParameter(new SqlParameter(PARAM_USERID, Types.INTEGER));
			declareParameter(new SqlOutParameter(PARAM_LOCKID, Types.INTEGER));
			declareParameter(new SqlOutParameter(PARAM_LOCKNAME, Types.CHAR));
			compile();
		}
		
		public Map execute(long documentId, int userId) 
		{
			Map<String, Object> input = new HashMap<String, Object>(3);
			input.put(PARAM_DOCID, documentId);
			input.put(PARAM_USERID, userId);
			
			Map output = super.execute(input);
			
			return output;
		}
	}
	
	/*
	 * Список форм доступных для просмотра и редактирования в данной роли
	 */
	
	static private final String GET_VIEW_EDIT_SQL = 
		"SELECT id, RTRIM(name) AS name FROM SNT.doctype WHERE id IN " +
		" (SELECT dtid FROM SNT.doctypeacc WHERE wrkid = :workId AND (cview = 1 OR cedit = 1))" +
		" ORDER BY name with ur";
	
	public List<ETDTemplate> getViewEditTypesForRole(Integer workId)
	{
		
		@SuppressWarnings("unchecked")
		List<ETDTemplate> result = getNamedParameterJdbcTemplate().query(GET_VIEW_EDIT_SQL, 
				new MapSqlParameterSource("workId", workId),
				new ParameterizedRowMapper<ETDTemplate>() 
				{
					public ETDTemplate mapRow(ResultSet rs, int n) throws SQLException 
					{
						ETDTemplate obj = new ETDTemplate(rs.getInt("id"), rs.getString("name"), new byte[1]);
						return obj;
					}
				});
		logger.debug(result.toString());
		return result;
	}
	
	
	/*
	 * Список форм доступных для создания  в данной роли
	 */
	
	static private final String GET_CREATE_SQL = 
		"SELECT id, RTRIM(name) as name FROM SNT.doctype WHERE id IN " +
		" (SELECT dtid FROM SNT.doctypeacc WHERE wrkid = :workId AND cnew = 1) ORDER BY name with ur";
	
	public List<ETDTemplate> getCreateTypesForRole(Integer workId)
	{
		
		@SuppressWarnings("unchecked")
		List<ETDTemplate> result = getNamedParameterJdbcTemplate().query(GET_CREATE_SQL, 
				new MapSqlParameterSource("workId", workId),
				new ParameterizedRowMapper<ETDTemplate>() 
				{
					public ETDTemplate mapRow(ResultSet rs, int n) throws SQLException 
					{
						ETDTemplate obj = new ETDTemplate(rs.getInt("id"), rs.getString("name"), new byte[1]);
						return obj;
					}
				});
		logger.debug(result.toString());
		return result;
	}
	
	
	static private final String GET_MSG_SQL = 
		"SELECT id, RTRIM(name) as name FROM SNT.doctype WHERE NAME = 'Сообщение' and id IN " +
		" (SELECT dtid FROM SNT.doctypeacc WHERE wrkid = :workId AND cnew = 1) ORDER BY name with ur";
	
	public List<ETDTemplate> getCreateMSGForRole(Integer workId)
	{
		
		@SuppressWarnings("unchecked")
		List<ETDTemplate> result = getNamedParameterJdbcTemplate().query(GET_MSG_SQL, 
				new MapSqlParameterSource("workId", workId),
				new ParameterizedRowMapper<ETDTemplate>() 
				{
					public ETDTemplate mapRow(ResultSet rs, int n) throws SQLException 
					{
						ETDTemplate obj = new ETDTemplate(rs.getInt("id"), rs.getString("name"), new byte[1]);
						return obj;
					}
				});
		logger.debug(result.toString());
		return result;
	}
	
	/*
	 * Список типов отчетов доступных для просмотра  в данной роли
	 */
	
	static private final String GET_REPORTS_VIEW_SQL = 
		"SELECT id, RTRIM(name) AS name FROM SNT.doctype WHERE id IN " +
		" (SELECT dtid FROM SNT.doctypeacc WHERE wrkid = :workId AND (cview = 1 OR cedit = 1 OR cnew = 1)) " +
		//" AND name IN ( :reportNames ) ORDER BY name with ur";
		" ORDER BY name with ur";
	
	public List<ETDTemplate> getReportTypesForRole(Integer workId, List<String> reportNames)
	{
		@SuppressWarnings("unchecked")
		List<ETDTemplate> result = getNamedParameterJdbcTemplate().query(GET_REPORTS_VIEW_SQL, 
				new MapSqlParameterSource("workId", workId)
				.addValue("reportNames", reportNames),
				new ParameterizedRowMapper<ETDTemplate>() 
				{
					public ETDTemplate mapRow(ResultSet rs, int n) throws SQLException 
					{
						ETDTemplate obj = new ETDTemplate(rs.getInt("id"), rs.getString("name"), new byte[1]);
						return obj;
					}
				});
		logger.debug(result.toString());
		return result;
	}
	
	
	
	
	static private final String GET_TEMPLATE_BY_NAME_SQL = 
		"SELECT id, RTRIM(name) AS name, template FROM SNT.DocType WHERE name = :name with ur";
	
	public ETDTemplate getTemplateByName(String name)
	{
		logger.debug("Loading template with name " + name);
		ETDTemplate template = (ETDTemplate)getNamedParameterJdbcTemplate().queryForObject(GET_TEMPLATE_BY_NAME_SQL,
				new MapSqlParameterSource("name", name),
				new ParameterizedRowMapper<ETDTemplate>() 
				{
					public ETDTemplate mapRow(ResultSet rs, int n) throws SQLException 
					{
						ETDTemplate obj = new ETDTemplate(rs.getInt("id"), rs.getString("name"), rs.getBytes("template"));
						return obj;
					}
				});
		return template;
	}
	
			
	static private final String GET_TEMPLATE_BY_ID_SQL = 
		"SELECT id, RTRIM(name) AS name, template, is_hv FROM SNT.DocType WHERE id = :id with ur";
	
	public ETDTemplate getTemplateById(Integer id)
	{
		logger.debug("Loading template with id " + id);
		ETDTemplate template = (ETDTemplate)getNamedParameterJdbcTemplate().queryForObject(GET_TEMPLATE_BY_ID_SQL,
				new MapSqlParameterSource("id", id),
				new ParameterizedRowMapper<ETDTemplate>() 
				{
					public ETDTemplate mapRow(ResultSet rs, int n) throws SQLException 
					{
						ETDTemplate obj = new ETDTemplate(rs.getInt("id"), rs.getString("name"), rs.getBytes("template"), rs.getInt("is_hv"));
						return obj;
					}
				});
		return template;
	}
	

	
	static private final String GET_PAK_COUNT_BY_DOCID_SQL = 
		"SELECT count(id) cnt from snt.docstore where id_pak =" +
		" (select id_pak from snt.docstore where id = :id) and signlvl is not null and typeid in " +
		"(select id from snt.doctype where name in ('Перечень первичных документов', 'Реестр счетов-фактур', 'Сопроводительное письмо')) with ur";
	
	
	
	public Integer getPakCountByDocumentId(Long id)
	{
		//logger.debug("Loading template by document id " + id);
		Integer res = (Integer)getNamedParameterJdbcTemplate().queryForObject(GET_PAK_COUNT_BY_DOCID_SQL,
				new MapSqlParameterSource("id", id),
				new ParameterizedRowMapper<Integer>() 
				{
					public Integer mapRow(ResultSet rs, int n) throws SQLException 
					{
						Integer obj = rs.getInt("cnt");
						return obj;
					}
				});
		return res;
	}
	
	static private final String GET_SF_SIGN_BY_DOCID_SQL = 
		"SELECT sf_sign from snt.docstore where id = :id with ur";
	
	
	
	public Integer getSfSignByDocumnetId(Long id)
	{
		//logger.debug("Loading template by document id " + id);
		Integer res = (Integer)getNamedParameterJdbcTemplate().queryForObject(GET_SF_SIGN_BY_DOCID_SQL,
				new MapSqlParameterSource("id", id),
				new ParameterizedRowMapper<Integer>() 
				{
					public Integer mapRow(ResultSet rs, int n) throws SQLException 
					{
						Integer obj = rs.getInt("sf_sign");
						
						return obj;
					}
				});
		return res;
	}
	
	
	
	
	
	
	static private final String GET_TEMPLATE_BY_DOCID_SQL = 
		"SELECT id, RTRIM(name) AS name, template, is_hv FROM SNT.DocType WHERE id in (SELECT typeid FROM SNT.docstore WHERE id = :id) with ur";
	
	public ETDTemplate getTemplateByDocumentId(Long id)
	{
		logger.debug("Loading template by document id " + id);
		ETDTemplate template = (ETDTemplate)getNamedParameterJdbcTemplate().queryForObject(GET_TEMPLATE_BY_DOCID_SQL,
				new MapSqlParameterSource("id", id),
				new ParameterizedRowMapper<ETDTemplate>() 
				{
					public ETDTemplate mapRow(ResultSet rs, int n) throws SQLException 
					{
						ETDTemplate obj = new ETDTemplate(rs.getInt("id"), rs.getString("name"), rs.getBytes("template"), rs.getInt("is_hv"));
						return obj;
					}
				});
		return template;
	}
	
	static final private String GET_ACCESS_BY_ROLE_ID_SQL = 
		"SELECT cview, cedit, cnew FROM SNT.doctypeacc WHERE wrkid = :workId AND dtid = " +
		" (SELECT typeid FROM SNT.docstore WHERE id = :documentId) with ur";
		
	public ETDDocumentAccess getAccessForRoleAndId(int workId, long documentId)
	{
		logger.debug("Loading access form role " + workId + " and docId " + documentId);
		ETDDocumentAccess access = (ETDDocumentAccess)getNamedParameterJdbcTemplate().queryForObject(GET_ACCESS_BY_ROLE_ID_SQL,
				new MapSqlParameterSource("workId", workId).
				addValue("documentId", documentId),
				new ParameterizedRowMapper<ETDDocumentAccess>() 
				{
					public ETDDocumentAccess mapRow(ResultSet rs, int n) throws SQLException 
					{
						ETDDocumentAccess obj = new ETDDocumentAccess();
						obj.setCreate(rs.getInt("cnew"));
						obj.setDelete(0/*rs.getInt("cdel")*/);
						obj.setEdit(rs.getInt("cedit"));
						obj.setView(rs.getInt("cview"));
						obj.setCreateSource(0/*rs.getInt("cnewdata")*/);
						return obj;
					}
				});
		return access;
	}
	
	static private final String GET_DOCUMENT_BY_ID_SQL = 
		"SELECT ID, BLDOC, INUSEID, DROPID, DROPTXT," +
		"case when ds.dropid is null then null else (select rtrim(cast(rtrim(fname) || ' ' || rtrim(mname) || ' ' || rtrim(lname) as char(240) ccsid unicode)) from SNT.personall where id = ds.dropid) end as dropuser, visible, readid, " +
		"case when ds.signlvl is null then 1 else 0 end as signed, "+
		"case when ds.signlvl =0 then 1 else 0 end as new "+
		" FROM SNT.DOCSTORE ds WHERE id = :id with ur";
	
	public ETDDocumentData getDocumentDataById(Long id)
	{
		logger.debug("Loading document with id " + id); 
		ETDDocumentData data = (ETDDocumentData)getNamedParameterJdbcTemplate().queryForObject(GET_DOCUMENT_BY_ID_SQL,
				new MapSqlParameterSource("id", id),
				new ParameterizedRowMapper<ETDDocumentData>() 
				{
					public ETDDocumentData mapRow(ResultSet rs, int n) throws SQLException 
					{
					    
					//    System.out.println("getDocumentDataById");
					    
						ETDDocumentData obj = new ETDDocumentData();
						obj.setBlDoc(rs.getBytes("BLDOC"));
						obj.setDropId(rs.getObject("DROPID") == null? null : rs.getInt("DROPID"));
						obj.setDropTxt(rs.getString("DROPTXT"));
						obj.setInUseId(rs.getInt("INUSEID"));
						obj.setId(rs.getLong("ID"));
						obj.setDropName(rs.getString("dropuser"));
						obj.setVisible(rs.getInt("VISIBLE"));
						obj.setRead(rs.getInt("READID"));
						obj.setSigned(rs.getInt("signed")==1?true:false);
						obj.setNew(rs.getInt("new")==1?true:false);
						return obj;
					}
				});
		return data;
	}
	
	public Map lockDocument(long documentId, int userId)
	{
		logger.debug("Lock document with id " + documentId + " for user " + userId); 
		LockDocumentProcedure  pr = new LockDocumentProcedure(getDataSource());
		Map map = pr.execute(documentId, userId);
		return map;
	}
	
	static private final String GET_DORS = 
		"SELECT id, name from SNT.dor ORDER BY name with ur";
	
	public Map<Integer, String> getDors(){
		final Map<Integer, String> map = new LinkedHashMap<Integer, String>();
		getNamedParameterJdbcTemplate().query(GET_DORS, new HashMap(), new RowMapper(){

			public Object mapRow(ResultSet rs, int arg1) throws SQLException {
				map.put(rs.getInt("id"), rs.getString("name"));
				return null;
			}
			
		});
		return map;
	}
	
	static private final String GET_PREDS_BY_DORID = 
		"SELECT id, vname from SNT.pred WHERE dorid = :DORID and activ = 'Y' ORDER BY vname with ur";
	
	public Map<Integer, String> getPredsByDorid(int dorid){
		final Map<Integer, String> map = new LinkedHashMap<Integer, String>();
		getNamedParameterJdbcTemplate().query(GET_PREDS_BY_DORID, new MapSqlParameterSource("DORID", dorid), new RowMapper(){

			public Object mapRow(ResultSet rs, int arg1) throws SQLException {
				map.put(rs.getInt("id"), rs.getString("vname"));
				return null;
			}
			
		});		
		return map;
	}
	

}

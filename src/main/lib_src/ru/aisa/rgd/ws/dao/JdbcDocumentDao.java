package ru.aisa.rgd.ws.dao;

import java.io.InputStream;
import java.math.BigInteger;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.log4j.Logger;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.SqlOutParameter;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcDaoSupport;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.ParameterizedRowMapper;
import org.springframework.jdbc.object.StoredProcedure;
import org.springframework.transaction.support.TransactionTemplate;

import ru.aisa.rgd.ws.domain.Document;
import ru.aisa.rgd.ws.domain.SFinfo;
import ru.aisa.rgd.ws.domain.Signature;
import ru.aisa.rgd.ws.exeption.ServiceError;
import ru.aisa.rgd.ws.exeption.ServiceException;
import ru.aisa.rgd.ws.utility.TypeConverter;

class SignatureMapper implements ParameterizedRowMapper<Signature>
{
	public Signature mapRow(ResultSet rs, int n) throws SQLException 
	{
		Signature sign = new Signature();
		sign.setDate(rs.getTimestamp("dt"));
		sign.setDocId(rs.getLong("docId"));
		sign.setOrder(rs.getInt("order"));
		sign.setParent(rs.getInt("parent"));
		sign.setPersId(rs.getInt("persId"));
		sign.setWrkId(rs.getInt("wrkId"));
		sign.setFio(rs.getString("fio"));
		//FIXME Когда понадобится 
		sign.setExp("");
		return sign;
	}
}


class DocumentMapper implements ParameterizedRowMapper<Document>
{
	public Document mapRow(ResultSet rs, int n) throws SQLException 
	{
		Document doc = new Document();
		doc.setId(rs.getLong("ID"));
		doc.setPredId(rs.getInt("PREDID"));
		doc.setCrdate(rs.getDate("CRDATE"));
		doc.setCrtime(rs.getTime("CRTIME"));
		if(rs.getString("SIGNLVL")==null)
			doc.setSignLvl(null);
		else
			doc.setSignLvl(rs.getInt("SIGNLVL"));
		doc.setType(rs.getString("TYPE"));
		doc.setNo(rs.getString("NO"));
		doc.setDocData(rs.getString("DOCDATA"));
		doc.setBlDoc(rs.getBytes("BLDOC"));
		return doc;
	}

}

class ProtocolNumberProcedure extends StoredProcedure 
{
	private static final String 
		SPROC_NAME = "snt.GetDoc_Y_Num",
		PARAM_ID = "rw_station_id",
		PARAM_FORMID = "sFormID",
		PARAM_NUMBER = "number";
	
	public ProtocolNumberProcedure(DataSource dataSource) 
	{
		super(dataSource, SPROC_NAME);
		setFunction(false);
		declareParameter(new SqlParameter(PARAM_ID, Types.INTEGER));
		declareParameter(new SqlParameter(PARAM_FORMID, Types.INTEGER));
		declareParameter(new SqlOutParameter(PARAM_NUMBER, Types.INTEGER));
		compile();
	}
	
	public Integer execute(int stationId, int documentId) 
	{
		Map<String, Integer> input = new HashMap<String, Integer>(3);
		input.put(PARAM_ID, stationId);
		input.put(PARAM_FORMID, documentId);
		
		Map output = super.execute(input);
		
		return (Integer)output.get(PARAM_NUMBER);
	}
}

class SerialNumberForMonthProcedure extends StoredProcedure 
{
	/*RW_STATION_ID	INTEGER (4)	IN
	SFORMID	INTEGER (4)	IN
	DOCNUM	INTEGER (4)	OUT*/
	private static final String 
		SPROC_NAME = "snt.GetDoc_M_Num",
		PARAM_ID = "RW_STATION_ID",
		PARAM_FORMID = "SFORMID",
		PARAM_NUMBER = "DOCNUM";
	
	public SerialNumberForMonthProcedure(DataSource dataSource) 
	{
		super(dataSource, SPROC_NAME);
		setFunction(false);
		declareParameter(new SqlParameter(PARAM_ID, Types.INTEGER));
		declareParameter(new SqlParameter(PARAM_FORMID, Types.INTEGER));
		declareParameter(new SqlOutParameter(PARAM_NUMBER, Types.INTEGER));
		compile();
	}
	
	public Integer execute(int enterpriseId, int documentId) 
	{
		Map<String, Integer> input = new HashMap<String, Integer>(3);
		input.put(PARAM_ID, enterpriseId);
		input.put(PARAM_FORMID, documentId);
		
		Map output = super.execute(input);
		
		return (Integer)output.get(PARAM_NUMBER);
	}
}

class SerialNumberForYearProcedure extends StoredProcedure 
{
	/*RW_STATION_ID	INTEGER (4)	IN
	SFORMID	INTEGER (4)	IN
	DOCNUM	INTEGER (4)	OUT*/
	private static final String 
		SPROC_NAME = "snt.GetDoc_Y_Num",
		PARAM_ID = "RW_STATION_ID",
		PARAM_FORMID = "SFORMID",
		PARAM_NUMBER = "DOCNUM";
	
	public SerialNumberForYearProcedure(DataSource dataSource) 
	{
		super(dataSource, SPROC_NAME);
		setFunction(false);
		declareParameter(new SqlParameter(PARAM_ID, Types.INTEGER));
		declareParameter(new SqlParameter(PARAM_FORMID, Types.INTEGER));
		declareParameter(new SqlOutParameter(PARAM_NUMBER, Types.INTEGER));
		compile();
	}
	
	public Integer execute(int enterpriseId, int documentId) 
	{
		Map<String, Integer> input = new HashMap<String, Integer>(3);
		input.put(PARAM_ID, enterpriseId);
		input.put(PARAM_FORMID, documentId);
		
		Map output = super.execute(input);
		
		return (Integer)output.get(PARAM_NUMBER);
	}
}

public class JdbcDocumentDao extends NamedParameterJdbcDaoSupport implements
		DocumentDao {
	
	protected final Logger	log= Logger.getLogger(getClass());
	
	private TransactionTemplate transactionTemplate;
	
	private static final String 
	
	SELECT_TEMPLATE_SQL = 
			"SELECT template FROM snt.doctype WHERE name=:name",
			
	SELECT_DOCUMENT_SQL =
		"SELECT a.id, a.predId, a.signLvl, a.no, a.docData, a.blDoc, a.crdate, a.crtime, (SELECT RTRIM(name) FROM snt.doctype WHERE id = a.typeId) as type FROM snt.docstore as a WHERE id=:id",
		
		SELECT_DOCUMENT_BY_ETDID_SQL =
		"SELECT a.etdid id, a.predId, a.signLvl, a.no, a.docData, a.blDoc, a.crdate, a.crtime, (SELECT RTRIM(name) FROM snt.doctype WHERE id = a.typeId) as type FROM snt.docstore as a WHERE etdid=:id",			
		
	UPDATE_DATA_SQL =
		"UPDATE  snt.DocStore SET blDoc = :blDoc, docData = :docData WHERE id = :id ",
	
	INSERT_DOCSTORE_SQL = 
		"INSERT INTO snt.DocStore " +
//		"(ID,PREDID,TYPEID,NO, CRDATE,CRTIME,BLDOC, DOCDATA, SIGNLVL, PRED_CREATOR, SF_SIGN, VISIBLE) " +
		"(ID,PREDID,TYPEID,NO, CRDATE,CRTIME,BLDOC, DOCDATA, SIGNLVL, PRED_CREATOR, VISIBLE) " +
		"VALUES ( " +
		":id , " +
		":predId, " +
		"(SELECT id FROM snt.doctype WHERE name = :type )," +
		":no , " +
		"current date," +
		"current time, " +
		":blDoc ,  " +
		":docData, " +
		"(CASE  (SELECT flowcnt FROM snt.doctype WHERE name = :type )  WHEN 0 THEN null ELSE 0 END),  :predId " +
//		",(CASE when :type like 'Счет-фактура' then 0 else null end) " +
		",1) " ,
		
	UPDATE_DOCSTORE_SQL = 
		"UPDATE  snt.DocStore " +
		"SET " +
		"predId = :predId, " +
		"typeId = (select id FROM snt.doctype where name = :type), " +
		"no = :no, " +
		"blDoc = :blDoc, " +
		"docData = :docData, " +
		"signLvl = :signLvl " +
		"WHERE id = :id ",
	
	DELETE_DOCSTORE_SQL = 
			"DELETE  FROM snt.docstore WHERE id = :id",	
		
	SELECT_NEW_ID_SQL = 
		"select next value for snt.vu_seq from snt.wrkname fetch first row only",
		
	SELECT_NO_SQL = 
		"SELECT no FROM snt.docstore WHERE id = :docid",
	
	IS_FINALLY_SIGNED_SQL =
		"SELECT COUNT(0) FROM snt.docstore WHERE id = :id AND signlvl IS null",
	
	UPDATE_DOCUMENTFLOW_SQL = "insert into snt.docstoreflow (docid, order, wrkid, dt, persid, parent, exp, ts, predid, stage)"+
	    		"values (:id, 1, (select id from snt.wrkname where name = :name),current timestamp, " +
	    		"(select id from snt.personall where certserial = :certserial),0,null,null,:predid,0)",
	
	UPDATE_DOCSTORE_TO_ARCHIVE_SQL = "update snt.docstore set signlvl= null where id = :id",
	
	SELECT_PACKAGE_BY_ID = "select count(id) from snt.archivepackreport where id_pack = (select id_pak from snt.docstore where id=:id) and SF is null",
	
	UPDATE_PACKAGE_FOR_SF = "update snt.archivepackreport set SF = 1 where id_pack = (select id_pak from snt.docstore where id=:id)";
	
	
	
	
		
	
	
	public JdbcDocumentDao(DataSource ds)
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

	/**
	 * Реализация методов интерфейса 
	 */
	
	public Long getNextId() 
	{
		Long id = new Long(getNamedParameterJdbcTemplate().queryForLong(SELECT_NEW_ID_SQL, new HashMap(1)));
		log.debug("Recieve new document ID = " + id);
		return id;
	}

	public InputStream getTemplate(String name) throws DataAccessException, ServiceException 
	{
		InputStream stream = null;
		try
		{
			SqlParameterSource pp = new MapSqlParameterSource("name", name);
			
			stream =  (InputStream) getNamedParameterJdbcTemplate().queryForObject(SELECT_TEMPLATE_SQL, pp,
				new ParameterizedRowMapper<InputStream>(){
					public InputStream mapRow(ResultSet rs, int numrow) throws SQLException
					{
							return rs.getBinaryStream("template");
					}}					
			);
		}
		catch (IncorrectResultSizeDataAccessException  e)
		{
			throw new ServiceException(e, ServiceError.ERR_TEMPLATE_EMPTY);
		}

		return stream;
	}
	
	public Long getNumTu174(String datetime,String nom_tab)
	{	
		@SuppressWarnings("unchecked")
		Long docid =getNamedParameterJdbcTemplate().queryForLong("SELECT id from snt.docstore where "+
				"(abs(((days('"+(datetime.substring(6,10)+"-"+datetime.substring(3,5)+"-"+datetime.substring(0,2))+"')*24*60+"+datetime.substring(11,13)+"*60+"+datetime.substring(14)+")-(days(CRDATE)*24*60+hour(crtime)*60+minute(crtime))))<120)"+ 
				"and signlvl=0 and typeid in(select id from snt.doctype where name in ('ТУ-174')) "+ 
				"and dropid is null "+ 
				"and xmlexists('$c/data[tabel_nom=\""+nom_tab+"\"]' passing docdata as \"c\")"+
				"order by crdate,crtime desc fetch first row only",new HashMap());
		return docid;
	}
	
	public String getDropTime(Long docid)
	{	
		@SuppressWarnings("unchecked")
		HashMap<String, Long> map = new HashMap<String, Long>(2);
       	map.put("ID",docid);
       	String drop_time =(String)getNamedParameterJdbcTemplate().queryForObject("select droptime from snt.docstore where id = :ID",map, String.class);
		return drop_time;
	}
	
	public void save(final Document document) 
	{
		Map<String, Object> input = document.map();
		getNamedParameterJdbcTemplate().update(INSERT_DOCSTORE_SQL, input);	
		getNamedParameterJdbcTemplate().queryForObject("select crdate,crtime from snt.docstore where id = :id with ur", input, new ParameterizedRowMapper<Object>(){

			public Object mapRow(ResultSet rs, int n) throws SQLException {
				document.setCrdate(rs.getDate("crdate"));
				document.setCrtime(rs.getTime("crtime"));
				return null;
			}
		});
		
		if (document.getType().equals("Счет-фактура")){
			getNamedParameterJdbcTemplate().update("update snt.docstore set sf_sign =2, readid =1 where id=:id", input);
		}		
		if (document.getType().equals("ТОРГ-12")){
			getNamedParameterJdbcTemplate().update("update snt.docstore set readid =1 where id=:id", input);
		}	
		
	}

	public void update(final Document document) 
	{
		Map<String, Object> input = document.map();
		getNamedParameterJdbcTemplate().update(UPDATE_DOCSTORE_SQL, input); 
	}
	public void updatePredPrip(long docid,int predid) 
	{
		Map<String,Object> m=new HashMap<String,Object>();
	    m.put("id", docid);
	    m.put("predid", predid);
		getNamedParameterJdbcTemplate().update("update snt.docstore set predid = :predid where id =:id", m); 
	}
	
	public void moveToArchive(long docid,int predid,String certid,String rolename)
	{
		Map<String,Object> m=new HashMap<String,Object>();
	    m.put("id", docid);
	    m.put("predid", predid);
	    m.put("certserial", new BigInteger(certid, 16).toString());
	    m.put("name", rolename);
	    getNamedParameterJdbcTemplate().update(UPDATE_DOCUMENTFLOW_SQL, m);
	    getNamedParameterJdbcTemplate().update(UPDATE_DOCSTORE_TO_ARCHIVE_SQL, new MapSqlParameterSource().addValue("id", docid));
	}

	public void delete(long id) 
	{
		SqlParameterSource input = new MapSqlParameterSource("id", id); 
		getNamedParameterJdbcTemplate().update(DELETE_DOCSTORE_SQL, input);
	}

	
	public String getNumber(long id) 
	{
		String no = (String) getNamedParameterJdbcTemplate().queryForObject(SELECT_NO_SQL, new MapSqlParameterSource("docid", id), String.class);
		return no;
	}

	private static final String SELECT_SIGNATURES_SQL =
		"SELECT  a.*, (select rtrim(b.fname) || ' ' || rtrim(b.mname) || ' ' || rtrim(b.lname) " +
		"from snt.personall as b where b.id = a.persid) as fio	FROM snt.docstoreflow as a " +
		"WHERE a.docid = :docid ORDER BY a.order ASC with ur";

	@SuppressWarnings("unchecked")
	public List<Signature> getSignatures(long id) 
	{
		List<Signature> list =  getNamedParameterJdbcTemplate().query(SELECT_SIGNATURES_SQL, new MapSqlParameterSource("docid", id), new SignatureMapper());
		return list;
	}

	public Document getById(long id) throws DataAccessException 
	{
		Document doc = (Document) getNamedParameterJdbcTemplate().queryForObject(SELECT_DOCUMENT_SQL, new MapSqlParameterSource("id", id), new DocumentMapper());
		return doc;
	}

	public Document getByEtdId(long id) throws DataAccessException 
	{
		Document doc = (Document) getNamedParameterJdbcTemplate().queryForObject(SELECT_DOCUMENT_BY_ETDID_SQL, new MapSqlParameterSource("id", id), new DocumentMapper());
		return doc;
	}
	
	public void updateData(Document document) throws DataAccessException 
	{
		getNamedParameterJdbcTemplate().update(UPDATE_DATA_SQL, document.map());
	}

	
	private static final String INSERT_INTO_MARSH =
		"INSERT INTO snt.MARSH " +
		"(DOCID, PREDID, PERSID) " +
		"VALUES ( " +
		":id , " +
		":predid , " +
		":persid)";
	
	private static final String INSERT_INTO_MARSH_SINGLE =
		"INSERT INTO snt.MARSH " +
		"(DOCID, PREDID, PERSID) " +
		"VALUES ( " +
		":id , " +
		":predid , " +
		"NULL)";
	
	
	private static final String CONTRAG_LIST_SQL = "SELECT id, (SELECT rtrim(name) name FROM snt.doctype WHERE id =ds.typeid), id_pak,vagnum,signlvl, "
			+ "CASE WHEN dropid IS NULL THEN 0 ELSE 1 END AS DROP, groupsgn FROM snt.docstore ds";

	
	public void InsertIntoMarsh(long docid, int predid, int persid) throws DataAccessException 
	{
		Map<String,String> m=new HashMap<String,String>();
		m.put("id",String.valueOf(docid));
		m.put("predid",String.valueOf(predid));
		if(persid!=-1){
			m.put("persid",String.valueOf(persid));
			getNamedParameterJdbcTemplate().update(INSERT_INTO_MARSH, m);
		}
		
		//int t=getNamedParameterJdbcTemplate().queryForInt(
		//		"select count(*) from snt.marsh where docid=:id and predid=:predid", m);
		//if(t==0) 
		else getNamedParameterJdbcTemplate().update(INSERT_INTO_MARSH_SINGLE, m);
	}
	
	
	private static final String SELECT_LAST_SIGNATURE_SQL =
		"select timestamp(ldate, ltime) as dt, lpersid as persId, id as docId, LWRKID as wrkId, " +
		"0 as order, 0 as parent, " + 
		"(select rtrim(fname) || ' ' || rtrim(mname) || ' ' || rtrim(lname) from snt.personall where id = lpersid) as fio" +
		" from snt.docstore where id = :id with ur";
	/* (non-Javadoc)
	 * @see ru.aisa.rgd.ws.dao.DocumentDao#getLastSignature(int)
	 */
	public Signature getLastSignature(long id) 
	{
		Signature sign = (Signature) getNamedParameterJdbcTemplate().queryForObject(SELECT_LAST_SIGNATURE_SQL, 
				new MapSqlParameterSource("id", id), new SignatureMapper());
		return sign;
	}

	
	/* (non-Javadoc)
	 * @see ru.aisa.rgd.ws.dao.DocumentDao#isFinallySigned(int)
	 */
	public boolean isFinallySigned(long id) 
	{
		Integer c = (Integer) getNamedParameterJdbcTemplate().queryForObject(IS_FINALLY_SIGNED_SQL , new MapSqlParameterSource("id", id), Integer.class);
		
		return c == 0 ? false : true;
	}
	

	/* (non-Javadoc)
	 * @see ru.aisa.rgd.ws.dao.DocumentDao#getNextProtocolNumber(int, int)
	 */
	public Integer getNextProtocolNumber(int stationId, int templateId) throws ServiceException 
	{
		Integer num = null;
		try
		{
			ProtocolNumberProcedure  pr = new ProtocolNumberProcedure (getDataSource());
			num = pr.execute(stationId, templateId);
			if (num == null) throw new ServiceException(new EmptyResultDataAccessException(1), ServiceError.ERR_UNDEFINED);
		}
		catch (DataIntegrityViolationException e)
		{
			throw new  ServiceException("Failed procedure snt.GetDoc_Y_Num. Reason - Incorrect input parmetr", ServiceError.ERR_UNDEFINED);
		}
		return num;
	}

	/* (non-Javadoc)
	 * @see ru.aisa.rgd.ws.dao.DocumentDao#getSerialNumberForMonth(int, int)
	 */
	public Integer getSerialNumberForMonth(int enterpriseId, int templateId) throws ServiceException 
	{
		Integer num = null;
		try
		{
			SerialNumberForMonthProcedure  pr = new SerialNumberForMonthProcedure(getDataSource());
			num = pr.execute(enterpriseId, templateId);
			if (num == null) throw new ServiceException(new EmptyResultDataAccessException(1), ServiceError.ERR_UNDEFINED);
		}
		catch (DataIntegrityViolationException e)
		{
			throw new  ServiceException("Failed procedure snt.GetDoc_M_Num. Reason - Incorrect input parmetr", ServiceError.ERR_UNDEFINED);
		}
		return num;
	}
	
	public Integer getSerialNumberForYear(int enterpriseId, int templateId) throws ServiceException 
	{
		Integer num = null;
		try
		{
			SerialNumberForYearProcedure  pr = new SerialNumberForYearProcedure(getDataSource());
			num = pr.execute(enterpriseId, templateId);
			if (num == null) throw new ServiceException(new EmptyResultDataAccessException(1), ServiceError.ERR_UNDEFINED);
		}
		catch (DataIntegrityViolationException e)
		{
			throw new  ServiceException("Failed procedure snt.GetDoc_Y_Num. Reason - Incorrect input parmetr", ServiceError.ERR_UNDEFINED);
		}
		return num;
	}

	private static final String GET_DATA_FOR_MONTH =
		"select docdata from snt.docstore where" +
		" predid = :enterpriseId and " +
		" MONTH(ldate) = :month and" +
		" YEAR(ldate) = :year and" +
		" typeid = (select id from snt.doctype where name = :templateName ) and" +
		" dropid is null and" +
		" signlvl is null with ur";
	@SuppressWarnings("unchecked")
	public <T> List<T> getDataForMonth(int enterpriseId, int month, int year, String templateName, ParameterizedRowMapper<T> mapper)  throws ServiceException
	{
		List<T> list =  getNamedParameterJdbcTemplate().query(GET_DATA_FOR_MONTH, 
				new MapSqlParameterSource("enterpriseId", enterpriseId).
				addValue("month", month).
				addValue("year", year).
				addValue("templateName",templateName), 
				mapper);
		return list;
	}
	private static final String GET_DATA_FOR_YEAR =
		"select docdata from snt.docstore where" +
		" predid = :enterpriseId and " +
		" YEAR(ldate) = :year and" +
		" typeid = (select id from snt.doctype where name = :templateName ) and" +
		" dropid is null and" +
		" signlvl is null with ur";
	@SuppressWarnings("unchecked")
	public <T> List<T> getDataForYear(int enterpriseId, int year, String templateName, ParameterizedRowMapper<T> mapper)  throws ServiceException
	{
		List<T> list =  getNamedParameterJdbcTemplate().query(GET_DATA_FOR_YEAR, 
				new MapSqlParameterSource("enterpriseId", enterpriseId).
				addValue("year", year).
				addValue("templateName",templateName), 
				mapper);
		return list;
	}
	private static final String GET_DATA_FOR_3YEARS =
		"select docdata from snt.docstore where" +
		" predid = :enterpriseId and " +
		" YEAR(ldate) in (:year, :year-1, :year-2) and" +
		" typeid = (select id from snt.doctype where name = :templateName ) and" +
		" dropid is null and" +
		" signlvl is null with ur";
	@SuppressWarnings("unchecked")
	public <T> List<T> getDataFor3Years(int enterpriseId, int year, String templateName, ParameterizedRowMapper<T> mapper)  throws ServiceException
	{
		List<T> list =  getNamedParameterJdbcTemplate().query(GET_DATA_FOR_3YEARS, 
				new MapSqlParameterSource("enterpriseId", enterpriseId).
				addValue("year", year).
				addValue("templateName",templateName), 
				mapper);
		return list;
	}
	private static final String GET_REPORTS_DATA =
		"select docdata from snt.docstore where predid = :enterpriseId and crdate = :reportDate " +
		"and typeid = (select id from snt.doctype where name= :templateName) " +
		"and dropid is null and signlvl is null with ur";
	@SuppressWarnings("unchecked")
	public <T> List<T> getDataForDate(int enterpriseId, Date date, String templateName, ParameterizedRowMapper<T> mapper) throws ServiceException 
	{
		List<T> list =  getNamedParameterJdbcTemplate().query(GET_REPORTS_DATA, 
				new MapSqlParameterSource("enterpriseId", enterpriseId).
				addValue("reportDate", date).
				addValue("templateName",templateName), 
				mapper);
		return list;
	}
	
	private static final String GET_DATA_FOR_BEGIN_AND_END_DATE =
		"select docdata from snt.docstore " +
		"where predid = :enterpriseId " +
		"and (ldate between :beginDate and :endDate) " +
		"and typeid = (select id from snt.doctype where name = :templateName ) " +
		"and dropid is null " +
		"and signlvl is null with ur";
	@SuppressWarnings("unchecked")
	public <T> List<T> getDataForBeginAndEndDate(int enterpriseId, Date beginDate, Date endDate, String templateName, ParameterizedRowMapper<T> mapper)  throws ServiceException
	{
		List<T> list =  getNamedParameterJdbcTemplate().query(GET_DATA_FOR_BEGIN_AND_END_DATE, 
				new MapSqlParameterSource("enterpriseId", enterpriseId).
				addValue("beginDate", new SimpleDateFormat("dd.MM.yyyy").format(beginDate)).
				addValue("endDate", new SimpleDateFormat("dd.MM.yyyy").format(endDate)).
				addValue("templateName",templateName), 
				mapper);
		return list;
	}
	
	private static final String GET_DATA_FOR_BEGIN_AND_END_DATE_ALL =
		"select docdata from snt.docstore " +
		"where predid = :enterpriseId " +
		"and (crdate between :beginDate and :endDate) " +
		"and typeid = (select id from snt.doctype where name = :templateName ) " +
		"and dropid is null with ur ";
	@SuppressWarnings("unchecked")
	public <T> List<T> getDataForBeginAndEndDateAll(int enterpriseId, Date beginDate, Date endDate, String templateName, ParameterizedRowMapper<T> mapper)  throws ServiceException
	{
		List<T> list =  getNamedParameterJdbcTemplate().query(GET_DATA_FOR_BEGIN_AND_END_DATE_ALL, 
				new MapSqlParameterSource("enterpriseId", enterpriseId).
				addValue("beginDate", new SimpleDateFormat("dd.MM.yyyy").format(beginDate)).
				addValue("endDate", new SimpleDateFormat("dd.MM.yyyy").format(endDate)).
				addValue("templateName",templateName), 
				mapper);
		return list;
	}
	
	private static final String GET_DATA_FOR_BEGIN_AND_END_DATE_DUXX =
		"select docdata from snt.docstore " +
		"where (predid = :enterpriseId or xmlexists('$c/data[Id_stan=\":enterpriseId\"]'passing docdata as \"c\"))" +
		"and (crdate between :beginDate and :endDate) " +
		"and typeid = (select id from snt.doctype where name = :templateName ) " +
		"and dropid is null with ur ";
	@SuppressWarnings("unchecked")
	public <T> List<T> getDataForBeginAndEndDateDUXX(int enterpriseId, Date beginDate, Date endDate, String templateName, ParameterizedRowMapper<T> mapper)  throws ServiceException
	{
		List<T> list =  getNamedParameterJdbcTemplate().query(GET_DATA_FOR_BEGIN_AND_END_DATE_DUXX, 
				new MapSqlParameterSource("enterpriseId", enterpriseId).
				addValue("beginDate", new SimpleDateFormat("dd.MM.yyyy").format(beginDate)).
				addValue("endDate", new SimpleDateFormat("dd.MM.yyyy").format(endDate)).
				addValue("templateName",templateName), 
				mapper);
		return list;
	}

	private static String UPDATE_DROPDATA_SQL = "UPDATE  snt.DocStore SET DROPTIME = :dropTime, DROPTXT = :dropTxt WHERE id = :id ";
	private static String UPDATE_VISIBLE0_SQL = "UPDATE  snt.DocStore SET visible =0 WHERE id = :id ";
	public void updateDropData(Document document) {
		Map<String, Object> params = new HashMap<String, Object>(document.map());
		Long droptime = (Long)document.map().get("dropTime");
		params.put("dropTime", new Timestamp(droptime));
		getNamedParameterJdbcTemplate().update(UPDATE_DROPDATA_SQL, params);
		
	}
	
	public List<SFinfo> getSf(int predid){
		
		return null;
		
	}
	
	public Long getPackageById (long id) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("id", String.valueOf(id));
		String packageId = getNamedParameterJdbcTemplate().queryForObject(SELECT_PACKAGE_BY_ID, params, String.class);
		return Long.valueOf(packageId);
	}
	
	public void updatePackageForSF (long id) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("id", id);
		getNamedParameterJdbcTemplate().update(UPDATE_PACKAGE_FOR_SF, params);
	}

	@Override
	public void updateVisibleTo0(Document document) throws DataAccessException {
		Map<String, Object> params = new HashMap<String, Object>(document.map());
		getNamedParameterJdbcTemplate().update(UPDATE_VISIBLE0_SQL, params);
	}
	
	private static final String INSERT_XML_SIGN = "insert into SNT.ARCHIVEXMLSIGNS (DOCID, TYPEID, PREDID, XML, SIGN, ID_PACK) VALUES " +
	"(:docid, :typeid, :predid, :file, :sign, :id_pack)";

	@Override
	public void insertXMLandSign(byte[] file, byte[] sign, long docId, long typeId, long predId, String id_pack) throws DataAccessException {
		try {
		Map<String, Object> params = new HashMap<String, Object> ();
		params.put("docid", docId);
		params.put("typeid", typeId);
		params.put("predid", predId);
		params.put("file", file);
		params.put("sign", sign);
		params.put("id_pack", id_pack);
		getNamedParameterJdbcTemplate().update(INSERT_XML_SIGN,params);

		} catch (Exception e) {
			log.error(TypeConverter.exceptionToString(e));
		}
	}
	
	private static final String SELECT_TYPE_ID = "select TYPEID from SNT.DOCSTORE where ID =:docid";
	
	public long getTypeId (long docId) throws DataAccessException {
		Map<String, Object> params = new HashMap<String, Object> ();
		params.put("docid", docId);
		long typeid = getNamedParameterJdbcTemplate().queryForLong(SELECT_TYPE_ID, params);
		return typeid;
	}
	
	private static final String SELECT_ID_PACK = "select id_pak from snt.docstore where id =:docId";
	
	public String getId_pack (long docId) {
		Map<String, Object> params = new HashMap<String, Object> ();
		params.put("docId", docId);
		return getNamedParameterJdbcTemplate().queryForObject(SELECT_ID_PACK, params, String.class);
		
	}

	
}

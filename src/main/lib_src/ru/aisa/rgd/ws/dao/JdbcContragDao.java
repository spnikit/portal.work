package ru.aisa.rgd.ws.dao;

import java.io.UnsupportedEncodingException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcDaoSupport;
import org.springframework.jdbc.core.simple.ParameterizedRowMapper;

import ru.aisa.rgd.etd.objects.AcceptDoc;
import ru.aisa.rgd.etd.objects.ETDDocument;
import ru.aisa.rgd.ws.domain.BankRequisites;
import ru.aisa.rgd.ws.domain.Requisites;
import ru.aisa.rgd.ws.exeption.ServiceException;
import ru.iit.portal.contraginvoice.DockAcceptLinkResponseDocument.DockAcceptLinkResponse.AcceptedDocs;
import sheff.rjd.services.contraginvoice.DropObj;
import sheff.rjd.services.contraginvoice.PackdocObj;
import sheff.rjd.services.contraginvoice.PersonObj;
import sheff.rjd.services.syncutils.ContrAgDocStatus;

public class JdbcContragDao extends NamedParameterJdbcDaoSupport implements
		ContragDao {
	public JdbcContragDao(DataSource ds) {
		super();
		setDataSource(ds);
	}

	class Statuswrapper implements ParameterizedRowMapper<ETDDocument> {
		public ETDDocument mapRow(ResultSet rs, int n) throws SQLException {
			ETDDocument obj = new ETDDocument();
			obj.setId(rs.getLong("ID"));
			obj.setName(rs.getString("NAME"));

			obj.setIdPak(rs.getString("ID_PAK"));

			obj.setVagnum(rs.getString("VAGNUM"));
			obj.setStat(0);

			if (rs.getInt("DROP") == 0)
				obj.setDrop(false);
			else
				obj.setDrop(true);

			obj.setVisible(rs.getInt("VISIBLE"));
			obj.setSigned(rs.getInt("SIGNED"));
			obj.setCount(rs.getInt("PACK"));
			obj.setSfSign(rs.getInt("SF_SIGN"));
			return obj;
		}

	}
	class AcceptDocWrapper implements ParameterizedRowMapper<AcceptDoc> {
		public AcceptDoc mapRow(ResultSet rs, int n) throws SQLException {
			AcceptDoc obj = new AcceptDoc();
			obj.setDocid(Long.parseLong(rs.getString("idDoc")));
			obj.setGr(rs.getString("gr"));
			obj.setDoctype(rs.getString("type"));
			obj.setSign(rs.getString("sign"));
			obj.setVis(rs.getString("vis"));
			
			return obj;
		}

	}
	class RequisitesWrapper implements ParameterizedRowMapper<Requisites> {
		public Requisites mapRow(ResultSet rs, int n) throws SQLException {
			Requisites req = new Requisites();
			req.setInn(rs.getString("INN"));
			req.setKpp(rs.getString("KPP"));
			req.setOkpo(rs.getInt("OKPO"));
			return req;
		}

	}
	
	class BankRequisitesWrapper implements ParameterizedRowMapper<BankRequisites> {
		public BankRequisites mapRow(ResultSet rs, int n) throws SQLException {
			BankRequisites req = new BankRequisites();
			req.setBankname(rs.getString("BANKNAME"));
			req.setBik(rs.getString("BIK_STR"));
			req.setAccount(rs.getString("ACCOUNT"));
			req.setKorraccount(rs.getString("KORRACCOUNT"));
			return req;
		}

	}
	class PersWrapper implements ParameterizedRowMapper<PersonObj> {
		public PersonObj mapRow(ResultSet rs, int n) throws SQLException {
			PersonObj req = new PersonObj();
			req.setFio(rs.getString("FIO"));
			req.setPersid(rs.getInt("PID"));
			req.setWrkid(rs.getInt("WRKID"));
			req.setPredid(rs.getInt("PREDID"));
			return req;
		}

	}
	
	class DropWrapper implements ParameterizedRowMapper<DropObj> {
		public DropObj mapRow(ResultSet rs, int n) throws SQLException {
			DropObj req = new DropObj();
			req.setDate(rs.getDate("DDATE"));
			req.setTime(rs.getTime("DTIME"));
			req.setFio(rs.getString("FIO"));
			return req;
		}

	}
	
	private static final String CONTRAG_LIST_SQL = "SELECT etdid id, (SELECT rtrim(name) name FROM snt.doctype WHERE id =ds.typeid), id_pak,vagnum, "
			+ "case when signlvl is not null then 0 else 1 end as signed, groupsgn, visible, "
			+ "CASE WHEN dropid IS NULL THEN 0 ELSE 1 END AS DROP, case when groupsgn is null then -1 else groupsgn end as pack, "
			+ "case when droptime is not null then date(droptime) else ldate end as lastdate, "
			+ "case when sf_sign is null then -1 else sf_sign end as sf_sign "
			+ "FROM snt.docstore ds where crdate between :DATEAFTER and :DATEBEFORE and predid in "
			+ "(select id from snt.pred where inn=:inn and kpp =:kpp) ";

	private static final String ID_CHECK_SQL = "select rtrim(name) from snt.doctype where id = (select typeid from snt.docstore where etdid =:docid)";
	
	private static final String UPDATE_DOCSTORE_SQL = "update snt.docstore set droptime = CURRENT_TIMESTAMP, droptxt=:reason, "
			+ "(dropid,dropwrkid) = (select pid, wrkid from snt.perswrk "
			+ "where pid = (select id from snt.personall where certserial = :cert) FETCH FIRST 1 ROW ONLY), droppredid = (select predid from snt.docstore where etdid = :docid) "
			+ " where etdid = :docid";
	
	//private static final String PERSONALL_DATA_SQL = "select pid, wrkid, predid from snt.perswrk where pid = (select id from snt.personall where certserial=:cert)";
	
	private static final String COUNT_PRED_SQL = "select count(0) from snt.pred where inn =:inn and kpp =:kpp";

	private static final String COUNT_PRED_CERT_SQL = "select count(0) from snt.perswrk where pid = (select id from snt.personall where CERTSERIAL =:cert)"
			+ " and predid in (select id from snt.pred where inn =:inn and kpp =:kpp)";

	private static final String RIGHT_ID_SQL = "select count(0) from snt.docstore where etdid =:id and predid in (select id from snt.pred where inn =:inn and kpp=:kpp)";

	private static final String GET_DOCDATA_SQL = "select docdata from snt.docstore where id =:id";

	private static final String GET_DOCDATA_BY_ETDID_SQL = "select docdata from snt.docstore where etdid =:id";

	private static final String DOC_STATUS_SQL = "SELECT id, (SELECT rtrim(name) name FROM snt.doctype WHERE id =ds.typeid), id_pak,vagnum,"
			+ "case when signlvl is not null then 0 else 1 end as signed, groupsgn, visible, "
			+ "CASE WHEN dropid IS NULL THEN 0 ELSE 1 END AS DROP, case when groupsgn is null then -1 else groupsgn end as pack,"
			+ "case when sf_sign is null then -1 else sf_sign end as sf_sign "
			+ " FROM snt.docstore ds where etdid =:ID";

	private static final String ISARCHIVE_SQL = "select case when dropid is null and signlvl is null then 1 else 0 end from snt.docstore where etdid = :id";
	
	private static final String ISARCHIVEorDROPPED_SQL = "select case when dropid is not null or signlvl is null then 1 else 0 end from snt.docstore where etdid = :id";
	
	private static final String Invoices_filled_SQL = "select case when SF_GFSGN=1 then 1 else 0 end from snt.docstore where etdid = :id";

	private static final String LIST_ALL = " and typeid in (select id from snt.doctype where groupid = (select id from snt.docgroups where name = 'ТОР'))";

	private static final String LIST_TYPE = " and typeid in (select id from snt.doctype where name = :formname)";

	private static final String ISPACK_SQL = "select count(0) from snt.docstore where etdid =:etdid "
			+ "and typeid = (select id from snt.doctype where name = :packname) "
			+ "and predid in (select id from snt.pred where inn =:inn and kpp=:kpp)";

	private static final String PACK_LIST_SQL = "SELECT etdid id, (SELECT rtrim(name) name FROM snt.doctype WHERE id =ds.typeid), id_pak,vagnum, "
			+ "case when signlvl is not null then 0 else 1 end as signed, groupsgn, visible, "
			+ "CASE WHEN dropid IS NULL THEN 0 ELSE 1 END AS DROP, case when groupsgn is null then -1 else groupsgn end as pack, "
			+ "case when droptime is not null then date(droptime) else ldate end as lastdate, "
			+ "case when sf_sign is null then -1 else sf_sign end as sf_sign, bldoc, docdata "
			+ "FROM snt.docstore ds where etdid in (select distinct(etdid) from snt.packages where id_pak =:id_pak)";

	private static final String GET_REQUISITES_SQL = "select inn, kpp, (okpo_kod) okpo from snt.pred where id = :PREDID";
	
	private static final String GET_BANKREQUISITES_SQL = "select bankname, bik_str, account, korraccount from snt.pred_requisites where predid = "
			+ "(select case when headid is null then id else headid end from snt.pred where id = :PREDID)";

	private static final String GET_PERS_SQL = "select pid, wrkid, predid , "+
" (select rtrim(cast(rtrim(fname) || ' ' || rtrim(mname) || ' ' || rtrim(lname) as char(240) ccsid unicode)) "+
" from SNT.personall where id = ps.pid) fio from snt.perswrk ps where pid = (select id from snt.personall where certserial = :cert) fetch first row only";
	
	private static final String GET_DROP_INFO_SQL = "select date(droptime) ddate, time (droptime) dtime, "
			+ "(select rtrim(cast(rtrim(fname) || ' ' || rtrim(mname) || ' ' || rtrim(lname) as char(240) ccsid unicode)) from SNT.personall "
			+ "where id = ds.dropid) fio from snt.docstore ds where etdid = :etdid";
	private static final String PACK_NEW_SQL = "select case when groupsgn is null and signlvl is not null and dropid is null then 1 else 0 end from snt.docstore where etdid = :id";
	/******/
	private static final String ACCEPTED_DOCS = "select distinct (ds.ID) idDoc,rtrim(ttype.NAME) type, "+
	 " CASE WHEN ds.SIGNLVL is null then -1 ELSE ds.SIGNLVL END as sign, ds.VISIBLE vis, grooup.NAME gr, CASE WHEN ds.DROPID is null THEN 0 ELSE 1 END as dropid "+
	 " from SNT.DOCSTORE ds, SNT.PERSWRK pw, SNT.PERSONALL pa, SNT.PRED pr, SNT.DOCTYPE ttype, SNT.DOCGROUPS grooup "+ 
     " where ds.PREDID = pr.id and pw.PID = pa.ID  and ttype.GROUPID = grooup.ID" +" and ttype.ID = ds.TYPEID and "+
     " ds.ID is not null and ttype.NAME is not null  and ds.VISIBLE is not null and grooup.NAME is not null "+
     " and pw.PREDID = pr.ID and pa.CERTSERIAL =:cert_id and ds.ID_PAK =:pacid and pr.INN =:inn and pr.KPP =:kpp "; 
	
	
	/*****/
	private static final String PACK_EXIST = "select count(0) from SNT.DOCSTORE ds, SNT.PRED pred"+
" where ds.PREDID = pred.ID and pred.INN =:inn and pred.KPP =:kpp and ds.ID_PAK =:idpack";
	public int packExist(String idpack, String inn, String kpp) throws ServiceException{
		HashMap<String, Object> pp = new HashMap<String, Object>();
		pp.put("idpack", idpack);
		pp.put("inn", inn);
		pp.put("kpp", kpp);
		
		return getNamedParameterJdbcTemplate().queryForInt(PACK_EXIST, pp);
	}
	public List<AcceptDoc> getAcceptedDoc(String cert_id,String pacid, String inn, String kpp) throws ServiceException
	{		
		String sql = ACCEPTED_DOCS;
		List<AcceptDoc> rez = new ArrayList<AcceptDoc>();
		HashMap<String, Object> pm = new HashMap<String, Object>();
		pm.put("cert_id",cert_id );
		pm.put("pacid", pacid);
		pm.put("inn",inn);
		pm.put("kpp", kpp);
		try
		{			
			rez = getNamedParameterJdbcTemplate().query(sql, pm, new ParameterizedRowMapper<AcceptDoc>(){

					public AcceptDoc mapRow(ResultSet rs, int n) throws SQLException 
					{
						AcceptDoc obj = new AcceptDoc();
						long id=Long.parseLong(rs.getString("idDoc"));
						obj.setDocid(id);
						obj.setGr(rs.getString("gr"));
						obj.setDoctype(rs.getString("type"));
						obj.setSign(rs.getString("sign"));
						obj.setVis(rs.getString("vis"));
						obj.setDropId(rs.getString("dropid"));					
								
						return obj;
									}
				});
						
		}
			catch(ServiceException ex)
			{
				ex.printStackTrace();
				throw new ServiceException(new Exception(), -1,
						"Внутренняя ошибка сервиса");

			}
		
		return rez;
	}
				
					
	public List<ETDDocument> Contraglist(String inn, String kpp, String date1,
			String date2, String formname) throws ServiceException {
		HashMap<String, Object> pp = new HashMap<String, Object>();

		String sql;

		pp.put("inn", inn);
		pp.put("kpp", kpp);

		if (date1.length() > 0)
			pp.put("DATEAFTER", date1);
		else
			pp.put("DATEAFTER", "1970-01-01");
		if (date2.length() > 0)
			pp.put("DATEBEFORE", date2);
		else
			pp.put("DATEBEFORE", "2100-01-01");

		if (formname.length() > 2) {
			pp.put("formname", formname);
			sql = CONTRAG_LIST_SQL.concat(LIST_TYPE);
		} else
			sql = CONTRAG_LIST_SQL.concat(LIST_ALL);

		final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-mm-dd");

		List<ETDDocument> result = new ArrayList<ETDDocument>();

		try {
			result = getNamedParameterJdbcTemplate().query(sql, pp,
					new ParameterizedRowMapper<ETDDocument>() {
						public ETDDocument mapRow(ResultSet rs, int n)
								throws SQLException {

							ETDDocument obj = new ETDDocument();
							obj.setId(rs.getLong("ID"));
							obj.setName(rs.getString("NAME"));

							obj.setIdPak(rs.getString("ID_PAK"));

							obj.setVagnum(rs.getString("VAGNUM"));
							obj.setStat(0);

							if (rs.getInt("DROP") == 0)
								obj.setDrop(false);
							else
								obj.setDrop(true);

							obj.setVisible(rs.getInt("VISIBLE"));
							obj.setSigned(rs.getInt("SIGNED"));
							obj.setCount(rs.getInt("PACK"));
							obj.setLastDate(rs.getDate("lastdate"));
							obj.setSfSign(rs.getInt("SF_SIGN"));
							return obj;

						}
					});
		} 
		catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	
	
	public boolean docchecktype(long docid) throws ServiceException{
		Map<String, Object> pp = new HashMap<String, Object>();
		pp.put("docid",docid);
		String check = "";
		String DocCheckType = "Пакет документов";
		check = getNamedParameterJdbcTemplate().queryForObject(ID_CHECK_SQL, pp,String.class);
		check = check.trim();
		if(!check.equals(DocCheckType)) return false;
		return true;
	}
	
	
	public int docstoreadd(String reason,String cert, long docid) throws ServiceException{
	Map<String, Object> pp = new HashMap<String, Object>();
	pp.put("cert", cert);
	pp.put("reason", reason);
	pp.put("docid", docid);
	return getNamedParameterJdbcTemplate().update(UPDATE_DOCSTORE_SQL, pp);
	}
	
	
	public int predcount(String inn, String kpp) throws ServiceException {

		HashMap<String, Object> pp = new HashMap<String, Object>();
		pp.put("inn", inn);
		pp.put("kpp", kpp);

		return getNamedParameterJdbcTemplate().queryForInt(COUNT_PRED_SQL, pp);
	}

	
	public int regcount(String cert, String inn, String kpp)
			throws ServiceException {
		
		HashMap<String, Object> pp = new HashMap<String, Object>();
		pp.put("cert", cert);
		pp.put("inn", inn);
		pp.put("kpp", kpp);

		return getNamedParameterJdbcTemplate().queryForInt(COUNT_PRED_CERT_SQL,
				pp);
		}
	

	
	public int rightid(long docid, String inn, String kpp)
			throws ServiceException {
		HashMap<String, Object> pp = new HashMap<String, Object>();
		pp.put("id", docid);
		pp.put("inn", inn);
		pp.put("kpp", kpp);
		return getNamedParameterJdbcTemplate().queryForInt(RIGHT_ID_SQL, pp);
	}

	
	public String getDocdata(long docid) throws ServiceException {
		HashMap<String, Object> pp = new HashMap<String, Object>();
		pp.put("id", docid);

		return (String) getNamedParameterJdbcTemplate().queryForObject(
				GET_DOCDATA_SQL, pp, String.class);
	}

	
	public String getDocdatabyetdid(long docid) throws ServiceException {
		HashMap<String, Object> pp = new HashMap<String, Object>();
		pp.put("id", docid);

		return (String) getNamedParameterJdbcTemplate().queryForObject(
				GET_DOCDATA_BY_ETDID_SQL, pp, String.class);
	}

	
	public int getDocstatus(long docid) throws ServiceException {
		ETDDocument doc = null;
		try {
			doc = (ETDDocument) getNamedParameterJdbcTemplate().queryForObject(
					DOC_STATUS_SQL, new MapSqlParameterSource("ID", docid),
					new Statuswrapper());
		} catch (Exception e) {
			e.printStackTrace();
			throw new ServiceException(new Exception(), -1,
					"Внутренняя ошибка сервиса");

		}

		return ContrAgDocStatus.getstatus(doc);
	}

	
	public boolean checkifarchieve(long docid) throws ServiceException {
		Map<String, Object> pp = new HashMap<String, Object>();
		pp.put("id", docid);
		try {
			int isarchive = getNamedParameterJdbcTemplate().queryForInt(
					ISARCHIVE_SQL, pp);

			if (isarchive == 1)
				return true;
			else
				return false;
		} catch (Exception e) {
			throw new ServiceException(new Exception(), -1,
					"Внутренняя ошибка сервиса");
		}
	}

	
	public int rightidpak(String id_pak, String inn, String kpp)
			throws ServiceException {
		long etdid = Integer.parseInt(id_pak);
		HashMap<String, Object> pp = new HashMap<String, Object>();
		pp.put("etdid", etdid);
		pp.put("packname", "Пакет документов");
		pp.put("inn", inn);
		pp.put("kpp", kpp);
		try {

			int count = getNamedParameterJdbcTemplate().queryForInt(ISPACK_SQL,
					pp);
			return count;
		} catch (Exception e) {
			throw new ServiceException(new Exception(), -1,
					"Внутренняя ошибка сервиса");
		}
	}

	public List<PackdocObj> PackDoclist(String inn, String kpp, String id_pak)
			throws ServiceException {
		HashMap<String, Object> pp = new HashMap<String, Object>();

		pp.put("inn", inn);
		pp.put("kpp", kpp);
		pp.put("id_pak", id_pak);

		List<PackdocObj> result = new ArrayList<PackdocObj>();

		try {
			result = getNamedParameterJdbcTemplate().query(PACK_LIST_SQL, pp,
					new ParameterizedRowMapper<PackdocObj>() {
						public PackdocObj mapRow(ResultSet rs, int n)
								throws SQLException {

							PackdocObj obj = new PackdocObj();
							ETDDocument doc = new ETDDocument();
							try {
								obj.setBldoc(new String(rs.getBytes("bldoc"),
										"UTF-8"));

							} catch (UnsupportedEncodingException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							obj.setDocdata(rs.getString("docdata"));
							obj.setDocid(rs.getLong("id"));
							if (rs.getInt("DROP") == 0)
								doc.setDrop(false);
							else
								doc.setDrop(true);

							doc.setVisible(rs.getInt("VISIBLE"));
							doc.setSigned(rs.getInt("SIGNED"));
							doc.setCount(rs.getInt("PACK"));
							doc.setSfSign(rs.getInt("SF_SIGN"));
							doc.setName(rs.getString("NAME"));
							obj.setFormname(rs.getString("NAME"));
							obj.setStatus(ContrAgDocStatus.getstatus(doc));

							return obj;

						}
					});
		} catch (Exception e) {
			throw new ServiceException(new Exception(), -1,
					"Внутренняя ошибка сервиса");
		}
		return result;
	}

	public Requisites getRequisitesByPredId(int predid)
			throws IncorrectResultSizeDataAccessException {
		Requisites req = null;

		try {
			req = (Requisites) getNamedParameterJdbcTemplate().queryForObject(
					GET_REQUISITES_SQL,
					new MapSqlParameterSource("PREDID", predid),
					new RequisitesWrapper());
		} catch (Exception e) {
			e.printStackTrace();
			throw new ServiceException(new Exception(), -1,
					"Внутренняя ошибка сервиса");

		}

		return req;

	}

	public BankRequisites getBankRequisitesByPredId(int predid)
			throws IncorrectResultSizeDataAccessException {
		BankRequisites req = null;

		try {
			req = (BankRequisites) getNamedParameterJdbcTemplate().queryForObject(
					GET_BANKREQUISITES_SQL,
					new MapSqlParameterSource("PREDID", predid),
					new BankRequisitesWrapper());
		} catch (Exception e) {
			e.printStackTrace();
			throw new ServiceException(new Exception(), -1,
					"Внутренняя ошибка сервиса");

		}

		return req;

	}
	
	
	
	@Override
	public PersonObj getPersbyCert(String cert)
			throws IncorrectResultSizeDataAccessException {
		PersonObj pers = null;
		try {
			pers = (PersonObj) getNamedParameterJdbcTemplate().queryForObject(
					GET_PERS_SQL,
					new MapSqlParameterSource("cert", cert),
					new PersWrapper());
		} catch (Exception e) {
			e.printStackTrace();
			throw new ServiceException(new Exception(), -1,
					"Внутренняя ошибка сервиса");

		}
		
		return pers;
	}



	@Override
	public DropObj getDropInfo(long docid)
			throws IncorrectResultSizeDataAccessException {
		DropObj drop = null;
		
		try {
			drop = (DropObj) getNamedParameterJdbcTemplate().queryForObject(
					GET_DROP_INFO_SQL,
					new MapSqlParameterSource("etdid", docid),
					new DropWrapper());
		} catch (Exception e) {
			e.printStackTrace();
			throw new ServiceException(new Exception(), -1,
					"Внутренняя ошибка сервиса");

		}
		
		return drop;
	}



	@Override
	public boolean checkpacknew(long docid)
			throws IncorrectResultSizeDataAccessException {
		Map<String, Object> pp = new HashMap<String, Object>();
		pp.put("id", docid);
		try {
			int isnew = getNamedParameterJdbcTemplate().queryForInt(
					PACK_NEW_SQL, pp);

			if (isnew == 1)
				return true;
			else
				return false;
		} catch (Exception e) {
			throw new ServiceException(new Exception(), -1,
					"Внутренняя ошибка сервиса");
		}
	
	}

	public String getNameByEtdid(long etdid) throws ServiceException {
		HashMap<String, Object> pp = new HashMap<String, Object>();
		pp.put("docid", etdid);

try{
		return (String) getNamedParameterJdbcTemplate().queryForObject(
				ID_CHECK_SQL, pp, String.class);
} catch (Exception e){
	e.printStackTrace();
	return null;
}	
}
	@Override
	public boolean checkifarchieveordropped(long etdid) throws ServiceException {
		Map<String, Object> pp = new HashMap<String, Object>();
		pp.put("id", etdid);
		try {
			int isarchiveordropped = getNamedParameterJdbcTemplate().queryForInt(
					ISARCHIVEorDROPPED_SQL, pp);

			if (isarchiveordropped == 1)
				return true;
			else
				return false;
		} catch (Exception e) {
			throw new ServiceException(new Exception(), -1,
					"Внутренняя ошибка сервиса");
		}
	}
	@Override
	public boolean InvoicesFilled(long etdid) throws ServiceException {
		Map<String, Object> pp = new HashMap<String, Object>();
		pp.put("id", etdid);
		try {
			int invoices = getNamedParameterJdbcTemplate().queryForInt(
					Invoices_filled_SQL, pp);

			if (invoices == 1)
				return true;
			else
				return false;
		} catch (Exception e) {
			throw new ServiceException(new Exception(), -1,
					"Внутренняя ошибка сервиса");
		}
	}
	
}

package ru.aisa.rgd.etd.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.log4j.Logger;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcDaoSupport;
import org.springframework.jdbc.core.simple.ParameterizedRowMapper;

import ru.aisa.rgd.etd.objects.DiObject;
import ru.aisa.rgd.etd.objects.ETDDocument;
import ru.aisa.rgd.etd.objects.PredTypedObject;
import ru.aisa.rgd.etd.objects.RemObject;
import sheff.rjd.ws.OCO.TORLists;

public class ETDDocStatusDao extends NamedParameterJdbcDaoSupport {

	private static Logger	log = Logger.getLogger(ETDDocStatusDao.class);

	public ETDDocStatusDao(DataSource ds) {
		super();
		setDataSource(ds);
	}

	private static final String statussql = "select case when droptxt is null then '' else droptxt end from snt.docstore where id = :docid";

	private static final String sqlpackdoc = "select id , (select rtrim(name) name from snt.doctype where id = ds.typeid), opisanie, vagnum, repdate, "
			+ "(case when ds.signlvl is null then 1 else 0 end) as signed, stat,(select count(0) count from snt.docstoreflow where docid = ds.id), "
			+ "(case when ds.dropid is null then 0 else 1 end) as drop, visible "
			+ "from snt.docstore ds where "
			+ "(visible = 0 or visible =2) and "
			+ "id_pak = (select id_pak from snt.docstore where id = :DOCID)";

	private static final String sqlpackdocnew = "select id , (select rtrim(name) name from snt.doctype where id = ds.typeid), opisanie, vagnum, repdate, "
			+ "(case when ds.signlvl is null then 1 else 0 end) as signed, stat,(select count(0) count from snt.docstoreflow where docid = ds.id), "
			+ "(case when ds.dropid is null then 0 else 1 end) as drop, visible "
			+ "from snt.docstore ds where "
			+ "(visible = 0 or visible =2 or visible =3) and "
			+ "ds.etdid in (select etdid from snt.packages where id_pak = (select id_pak from snt.docstore where id =:DOCID))";
	
	private static final String sqlpackcss = "select id , (select rtrim(name) name from snt.doctype where id = ds.typeid), opisanie, vagnum, repdate, "
			+ "ds.signlvl  as signed, stat,(select count(0) count from snt.docstoreflow where docid = ds.id), "
			+ "(case when ds.dropid is null then 0 else 1 end) as drop, visible "
			+ "from snt.docstore ds where "
			+ "(visible = 0 or visible = 1 or visible =2 or visible =3) and "
			+ "ds.etdid in (select etdid from snt.packages where id_pak = (select id_pak from snt.docstore where id =:DOCID))";

	private static final String sqlpackrtk = "select id , (select rtrim(name) name from snt.doctype where id = ds.typeid), opisanie, "
			+ " (case when ds.signlvl =0 then 0 else 1 end) as signed, visible from snt.docstore ds where "
			+ " visible = 0 and  ds.id_pak = (select id_pak from snt.docstore where id =:DOCID)";

	private static final String sqlcountpreds = "select count(id) from snt.pred where id in (select id from snt.pred where id =:predid "
			+ " union select id from snt.pred where headid =:predid union select headid from snt.pred where id = :predid)";

	private static final String sqlforhead = "select  case when (select headid from snt.pred where id = :predid) is null then 0 "
			+ "else 1  end from snt.pred where id = :predid";

	private static final String sqldiforhead = "select id, rtrim(name) name from snt.dor where id in (select dorid from snt.rem_pred where kleim in "
			+ "(select distinct(kid) from snt.pred_fil where hid = :predid)) order by name";

	private static final String sqlremforhead = "select kleim id, rtrim(name) name from snt.rem_pred where kleim in (select distinct(kid) from snt.pred_fil where hid = :predid) ";

	private static final String sqldiforfil = "select id, rtrim(name) name from snt.dor where id in (select dorid from snt.rem_pred where kleim in "
			+ "(select distinct(kid) from snt.pred_fil where pid = :predid)) order by name";

	private static final String sqlremforfil = "select kleim id, rtrim(name) name from snt.rem_pred where kleim in (select distinct(kid) from snt.pred_fil where pid = :predid ) ";

	private static final String sqldiall = "select id, rtrim(name) name from snt.dor where admid = 20 order by name";

	private static final String sqlremall = "select kleim id, rtrim(name) name from snt.rem_pred ";

	private static final String sqlhistory = "select id , crdate, crtime, date(droptime) ddate, time(droptime) dtime, droptxt, id_pak from snt.docstore where vagnum = :vagnum "
			+ "and typeid = (select id from snt.doctype where name =:name) and dropid is not null and repdate =:repdate";

	private static final String sqlhistorynew = "select id , crdate, crtime, date(droptime) ddate, time(droptime) dtime, droptxt, id_pak from snt.docstore ds where ds.id in "
			+ " (select id from snt.docstore where reqnum = (select reqnum from snt.docstore where id = :docid) and id<>:docid "
			+ " union select id from snt.docstore where id_pak = (select reqnum from snt.docstore where id = :docid) and id<>:docid) and ds.typeid = "
			+ "(select id from snt.doctype where name =:name)";

	/*private static final String sqlperspred = "select id , "
			+ "(select rtrim(cast(rtrim(fname) || ' ' || rtrim(mname) || ' ' || rtrim(lname) as char(240) ccsid unicode)) name "
			+ "from SNT.personall where id  = ps.id) from snt.personall ps where id in (select distinct(pid) from snt.perswrk where predid in "
			+ "(select id from snt.pred where id = :predid union select id from snt.pred where headid = :predid)) and autosgn>0 order by name";*/
	
	private static final String sqlperspred = "select id, rtrim(cast(rtrim(fname) || ' ' || rtrim(mname) || ' ' || rtrim(lname) as char(240) ccsid unicode)) name from snt.personall where "
												+"id in (select distinct(ds.lpersid) from snt.docstore ds where predid in (select id from snt.pred where id = :predid union select id from snt.pred where headid = :predid)) or "
												+"id in (select distinct(ds.dropid) from snt.docstore ds where predid in (select id from snt.pred where id = :predid union select id from snt.pred where headid = :predid)) order by name";



	private static final String sqltypes = "select dtid id , (select rtrim(name) name from snt.doctype where id = dac.dtid) name from snt.doctypeacc dac where wrkid = :wrkid #types order by name";

	public String getStatusMess(long docid) {
		Map<String, Object> pp = new HashMap<String, Object>();
		pp.put("docid", docid);
		String mess = (String) getNamedParameterJdbcTemplate().queryForObject(
				statussql, pp, String.class);

		return mess;

	}

	public List<ETDDocument> getPackdocs(long docid) {
		Map<String, Object> pp = new HashMap<String, Object>();
		pp.put("DOCID", docid);

		List<ETDDocument> result = getNamedParameterJdbcTemplate().query(
				sqlpackdocnew, pp, new ParameterizedRowMapper<ETDDocument>() {
					public ETDDocument mapRow(ResultSet rs, int n)
							throws SQLException {
						ETDDocument obj = new ETDDocument();
						obj.setId(rs.getLong("ID"));
						obj.setName(rs.getString("NAME"));
						obj.setVagnum(rs.getString("VAGNUM"));
						obj.setStat(rs.getInt("STAT"));
						obj.setReqdate(rs.getString("REPDATE"));
						obj.setContent(rs.getString("OPISANIE"));
						obj.setCount(rs.getInt("COUNT"));
						obj.setSigned(rs.getInt("SIGNED"));

						if (rs.getInt("drop") == 1) {

							obj.setDrop(true);
						}
						obj.setVisible(rs.getInt("visible"));

						return obj;
					}
				});

		return result;

	}

	public List<ETDDocument> getPackRTK(long docid) {
		Map<String, Object> pp = new HashMap<String, Object>();
		pp.put("DOCID", docid);

		List<ETDDocument> result = getNamedParameterJdbcTemplate().query(
				sqlpackrtk, pp, new ParameterizedRowMapper<ETDDocument>() {
					public ETDDocument mapRow(ResultSet rs, int n)
							throws SQLException {
						ETDDocument obj = new ETDDocument();
						obj.setId(rs.getLong("ID"));
						obj.setName(rs.getString("NAME"));
						obj.setContent(rs.getString("OPISANIE"));
						obj.setSigned(rs.getInt("SIGNED"));

//						if (rs.getInt("drop") == 1) {
//
//							obj.setDrop(true);
//						}
						obj.setVisible(rs.getInt("visible"));

						return obj;
					}
				});

		return result;

	}
	
	public List<ETDDocument> getPackCSS(long docid) {
		Map<String, Object> pp = new HashMap<String, Object>();
		pp.put("DOCID", docid);

		List<ETDDocument> result = getNamedParameterJdbcTemplate().query(
				sqlpackcss, pp, new ParameterizedRowMapper<ETDDocument>() {
					public ETDDocument mapRow(ResultSet rs, int n)
							throws SQLException {
						ETDDocument obj = new ETDDocument();
						obj.setId(rs.getLong("ID"));
						obj.setName(rs.getString("NAME"));
						obj.setContent(rs.getString("OPISANIE"));
						obj.setSigned(rs.getInt("SIGNED"));
						obj.setVisible(rs.getInt("visible"));
						if (rs.getInt("drop") == 1) {
							obj.setDrop(true);
						}
						return obj;
					}
				});

		return result;

	}

	public int getHeadforPred(int predid) {
		HashMap<String, Object> pp = new HashMap<String, Object>();
		pp.put("predid", predid);
		int head = -1;
		int count = getNamedParameterJdbcTemplate().queryForInt(sqlcountpreds,
				pp);

		if (count > 1)
			head = getNamedParameterJdbcTemplate().queryForInt(sqlforhead, pp);

		return head;

	}

	public List<DiObject> getDiList(long predid, int head) {
		Map<String, Object> pp = new HashMap<String, Object>();
		String sql = "";

		switch (head) {
		case -1:
			sql = sqldiall;
			break;
		// case 0: sql = sqldiforhead;
		case 0:
			sql = sqldiall;
			break;
		case 1:
			sql = sqldiforfil;
			break;
		}

		pp.put("predid", predid);
		// System.out.println(sql);
		List<DiObject> result = getNamedParameterJdbcTemplate().query(sql, pp,
				new ParameterizedRowMapper<DiObject>() {
					public DiObject mapRow(ResultSet rs, int n)
							throws SQLException {
						DiObject obj = new DiObject();
						obj.setId(rs.getInt("ID"));
						obj.setName(rs.getString("NAME"));

						return obj;
					}
				});

		return result;

	}

	public List<RemObject> getREmList(long predid, int head) {
		Map<String, Object> pp = new HashMap<String, Object>();
		String sql = "";
		String order = " order by name";
		switch (head) {
		case -1:
			sql = sqlremall.concat(order);
			break;
		// case 0: sql = sqlremforhead.concat(order);
		case 0:
			sql = sqlremall.concat(order);
			break;
		case 1:
			sql = sqlremforfil.concat(order);
			break;
		}
		pp.put("predid", predid);
		// System.out.println(sql);
		List<RemObject> result = getNamedParameterJdbcTemplate().query(sql, pp,
				new ParameterizedRowMapper<RemObject>() {
					public RemObject mapRow(ResultSet rs, int n)
							throws SQLException {
						RemObject obj = new RemObject();
						obj.setId(rs.getInt("ID"));
						obj.setName(rs.getString("NAME"));
						return obj;
					}
				});

		return result;

	}

	public List<RemObject> getLoadRem(long predid, int head, String di) {
		Map<String, Object> pp = new HashMap<String, Object>();
		String sql = "";
		// System.out.println("head: "+head);
		String sqladd = " dorid in (" + di + ") order by name";

		switch (head) {
		case -1:
			sql = sqlremall.concat(" where ").concat(sqladd);
			break;
		// case 0: sql = sqlremforhead.concat(" and ").concat(sqladd);
		case 0:
			sql = sqlremall.concat(" where ").concat(sqladd);
			break;
		case 1:
			sql = sqlremforfil.concat(" and ").concat(sqladd);
			break;
		}
		pp.put("predid", predid);

		// System.out.println(sql);
		List<RemObject> result = getNamedParameterJdbcTemplate().query(sql, pp,
				new ParameterizedRowMapper<RemObject>() {
					public RemObject mapRow(ResultSet rs, int n)
							throws SQLException {
						RemObject obj = new RemObject();
						obj.setId(rs.getInt("ID"));
						obj.setName(rs.getString("NAME"));
						return obj;
					}
				});

		return result;

	}

	public List<ETDDocument> getHistory(String vagnum, String date, long docid) {

		Map<String, Object> pp = new HashMap<String, Object>();
		pp.put("vagnum", vagnum);
		pp.put("name", "Пакет документов");
		pp.put("repdate", date);
		pp.put("docid", docid);
		List<ETDDocument> result = getNamedParameterJdbcTemplate().query(
				sqlhistory, pp, new ParameterizedRowMapper<ETDDocument>() {
					public ETDDocument mapRow(ResultSet rs, int n)
							throws SQLException {
						ETDDocument obj = new ETDDocument();
						obj.setId(rs.getLong("ID"));
						obj.setCreateDate(rs.getDate("CRDATE"));
						obj.setCreateTime(rs.getTime("CRTIME"));
						obj.setLastDate(rs.getDate("DDATE"));
						obj.setLastTime(rs.getTime("DTIME"));
						obj.setDroptxt(rs.getString("DROPTXT"));
						obj.setName("Пакет документов");
						obj.setIdPak(rs.getString("ID_PAK"));
						return obj;
					}
				});

		return result;

	}

	public List<PredTypedObject> getPreds(int predid) {

		Map<String, Object> pp = new HashMap<String, Object>();
		pp.put("predid", predid);

		List<PredTypedObject> result = getNamedParameterJdbcTemplate().query(
				sqlperspred, pp, new ParameterizedRowMapper<PredTypedObject>() {
					public PredTypedObject mapRow(ResultSet rs, int n)
							throws SQLException {
						PredTypedObject obj = new PredTypedObject();
						obj.setId(rs.getInt("ID"));
						obj.setName(rs.getString("NAME"));
						return obj;
					}
				});

		return result;

	}
	public int getRole(int wrkid) {
		
		Map<String, Object> pp = new HashMap<String, Object>();
		pp.put("wrkid", wrkid);
		String select = "select wrk.ISSM from SNT.WRKNAME wrk where wrk.ID = :wrkid ";
		int role =  getNamedParameterJdbcTemplate().queryForInt(select, pp);
				

		return role;

	}
	private String sql  = "";
	
	private String typesTOR = " and dtid in (select id from snt.doctype where name in ('"+TORLists.Package+"','"+TORLists.SF+"','"+TORLists.TORG12+"'))";
	private String typesCSS = " and dtid in (select id from snt.doctype where name in ('"+TORLists.PackageCSS+"','"+TORLists.SFCSS+"'))";
	public List<PredTypedObject> getTypes(int wrkid) {
		Map<String, Object> pp = new HashMap<String, Object>();
		pp.put("wrkid", wrkid);
		int r = getRole(wrkid);
//		System.out.println("wrk id is "+wrkid);
		if((r == 1)||(r == 2)||(r == 3)||(r == 6)||(r ==13))//jira PORTAL-639
		{
			sql = sqltypes.replaceAll("#types", typesTOR);
		}
		else if (r==17){
			sql = sqltypes.replaceAll("#types", typesCSS);
		}
		else
		{
			sql = sqltypes.replaceAll("#types", "");
		}

//		System.out.println(sql);
		List<PredTypedObject> result = getNamedParameterJdbcTemplate().query(
					sql, pp, new ParameterizedRowMapper<PredTypedObject>() {
						public PredTypedObject mapRow(ResultSet rs, int n)
								throws SQLException {

							PredTypedObject obj = new PredTypedObject();
							
								if(!rs.getString("NAME").equals("null")){
								obj.setId(rs.getInt("ID"));
//								System.out.println("id "+rs.getInt("ID"));
								obj.setName(rs.getString("NAME"));
//								System.out.println("id "+rs.getString("NAME"));
								
								}
							
							return obj;

						}
					});
		
		
		
		return result;

	}
}

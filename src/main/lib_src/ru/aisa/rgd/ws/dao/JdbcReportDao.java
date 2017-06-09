package ru.aisa.rgd.ws.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.jdbc.core.namedparam.NamedParameterJdbcDaoSupport;
import org.springframework.jdbc.core.simple.ParameterizedRowMapper;

import ru.aisa.rgd.etd.objects.DiObject;
import ru.aisa.rgd.etd.objects.ETDDocument;
import ru.aisa.rgd.etd.objects.ReportData;
import ru.aisa.rgd.ws.exeption.ServiceException;

public class JdbcReportDao extends NamedParameterJdbcDaoSupport implements ReportDao{

	public JdbcReportDao(DataSource ds) {
		super();
		setDataSource(ds);
	}
	
	
	private static final String dors = "select id, rtrim(dname) name from snt.dor where admid= :ADMID and name not like '%Сахалинская%'"; 
	
	private static final String pack = "Пакет документов"; 
	private static final String getFormIdSql = "SELECT ID FROM SNT.DOCTYPE WHERE NAME =:FORMNAME";
	
	
	
	private static final String sftype0 = "select onbaseid,etdid,vagnum,repdate,typeid,signlvl,crdate from snt.docstore  "+
			"where typeid = (select id from snt.doctype where rtrim(name)='Счет-фактура') "+
			"and crdate between :DATEFROM AND :DATETO "+
			"and predid in (select id from snt.pred where id = :PREDID union select id from snt.pred where headid = :PREDID "+
			"union select id from snt.pred where headid = (select headid from snt.pred where id = :PREDID ) "+
			"union select headid from snt.pred where id = :PREDID )";
	
	private static final String sftype1 = "select onbaseid,etdid,vagnum,signlvl, id_pak, crdate "+
"from snt.docstore ds "+
"where typeid = (select id from snt.doctype where rtrim(name)='Счет-фактура') "+
"and id_pak in (select pakid from temp1_main)";
	
	private static final String newrep = "with temp1_main as( "+
"select vagnum,max(etdid) as idd #reqdate #idpak from snt.docstore "+
"where typeid = (select id from snt.doctype where rtrim(name)='Пакет документов') "+
"and #sortdate between :DATEFROM AND :DATETO " +
"and predid in (select id from snt.pred where id = :PREDID union select id from snt.pred where headid = :PREDID "+
"union select id from snt.pred where headid = (select headid from snt.pred where id = :PREDID ) "+
"union select headid from snt.pred where id = :PREDID ) "+
"group by vagnum  #reqdate) "+
", "+
"curr_t as ( "+
"select ds.onbaseid,count(1) current from temp1_main t,snt.docstore ds "+
"where idd=ds.etdid  "+
"and ds.signlvl>0 and ds.dropid is null "+
"group by ds.onbaseid "+
"order by ds.onbaseid desc) "+
", "+
"inw_t as ( "+
"select ds.onbaseid,count(1) working from temp1_main t,snt.docstore ds "+
"where idd=ds.etdid and "+
"ds.signlvl is null and ds.groupsgn = 0 and dropid is null "+
"group by ds.onbaseid "+
"order by ds.onbaseid desc "+
") "+
", "+
"decl_t as ( "+
"select ds.onbaseid,count(1) declined from temp1_main t,snt.docstore ds "+
"where idd=ds.etdid and "+
"ds.dropid is not null "+
"group by ds.onbaseid "+
"order by ds.onbaseid desc  "+
") "+
", "+
"arch_t as ( "+
"select ds.onbaseid,count(1) archive from temp1_main t,snt.docstore ds "+
"where idd=ds.etdid and "+
"ds.signlvl is null and ds.groupsgn = 1 and dropid is null "+
"group by ds.onbaseid "+
"order by ds.onbaseid desc  "+
") "+
", "+
"t2 as ( #sf) "+
", "+
"sf_t as ( "+
"select onbaseid,count(distinct(vagnum)) sfvag from t2 "+
"group by onbaseid "+
") "+
", "+
"vchde_t as ( "+
"select dorid,kleim,name from snt.rem_pred "+
") "+
",torek as (	SELECT KLEIMO, PREDID,SUM(RCOUNT) AS RCOUNT FROM SNT.TOREKREPORTS AS S WHERE "+
   " S.RDATE BETWEEN :DATEFROM AND :DATETO and predid in (select id from snt.pred where id = :PREDID union select id from snt.pred where headid = :PREDID "+
    		"union select id from snt.pred where headid = (select headid from snt.pred where id = :PREDID ) "+
    		"union select headid from snt.pred where id = :PREDID) "+
" GROUP BY KLEIMO, PREDID) "+
"select v.dorid,v.kleim,v.name, " +
" (coalesce(current,0)+coalesce(working,0)+coalesce(declined,0)+coalesce(archive,0)) summ, " +
" coalesce(current,0) current,coalesce(working,0)working,coalesce(declined,0)declined, "+
" coalesce(archive,0) archive,coalesce(sfvag,0) sfvag , "+
" COALESCE(RCOUNT,0) RCOUNT from vchde_t v "+
"left outer join curr_t c on v.kleim=c.onbaseid "+
"left outer join inw_t i on v.kleim=i.onbaseid "+
"left outer join decl_t d on v.kleim=d.onbaseid "+
"left outer join arch_t a on v.kleim=a.onbaseid "+
"left outer join sf_t s on v.kleim=s.onbaseid "+
"LEFT OUTER JOIN torek tr ON v.kleim=tr.KLEIMO "+
"order by v.dorid";
	
	
	
//	SELECT
//    KLEIMO,
//    PREDID,
//    SUM(RCOUNT) AS RCOUNT
//FROM
//    SNT.TOREKREPORTS AS S
//WHERE
//    S.RDATE BETWEEN '2014-05-15' AND '2014-05-20'
//GROUP BY
//    KLEIMO,
//    PREDID
	
	
	
	@Override
	public int getFormId(String formname) throws ServiceException {
		HashMap <String, Object> pp = new HashMap<String, Object>();
		pp.put("FORMNAME", formname);
		
		int id = getNamedParameterJdbcTemplate().queryForInt(getFormIdSql, pp);
		return id;
	}


	@Override
	public List<ReportData> getReportData(int predid, int type,  String dateot, String dateto)
			throws ServiceException {
		//System.out.println("getReportData");
		String sql="";
		
		if (type ==0){
			sql = newrep.replaceAll("#reqdate", "").replaceAll("#sortdate", "crdate").replaceAll("#sf", sftype0).replaceAll("#idpak", "");
		}
		
		else if (type==1){
			sql = newrep.replaceAll("#reqdate", ", reqdate").replaceAll("#sortdate", "reqdate").replaceAll("#sf", sftype1).replaceAll("#idpak", ",MAX(id_pak) AS pakid");
		}
		
		
		HashMap <String, Object> pp = new HashMap<String, Object>();
		pp.put("PREDID", predid);
		pp.put("DATEFROM", dateot);
		pp.put("DATETO", dateto);
		
		
		
		
		List<ReportData> result = getNamedParameterJdbcTemplate().query(sql, pp, 
				new ParameterizedRowMapper<ReportData>() 
				{
					public ReportData mapRow(ResultSet rs, int n) throws SQLException 
					{
						ReportData obj = new ReportData();
						obj.setRecieved(Integer.parseInt(rs.getString("CURRENT")));
						obj.setAccepted(Integer.parseInt(rs.getString("WORKING")));
						obj.setDeclined(Integer.parseInt(rs.getString("DECLINED")));
						obj.setSigned(Integer.parseInt(rs.getString("ARCHIVE")));
						obj.setSumm(Integer.parseInt(rs.getString("SUMM")));
						obj.setSf(Integer.parseInt(rs.getString("SFVAG")));
						obj.setDorid(rs.getInt("DORID"));
						obj.setMark(rs.getInt("KLEIM"));
						obj.setRepname(rs.getString("NAME"));
						obj.setSummtorek(rs.getInt("RCOUNT"));
						return obj;
					}
				});
		
		
		
		
		return result;
	}

	
	public List<DiObject> getDors() throws ServiceException{
		
		HashMap <String, Object> pp = new HashMap<String, Object>();
		pp.put("ADMID", 20);
		List<DiObject> result = getNamedParameterJdbcTemplate().query(dors, pp, 
				new ParameterizedRowMapper<DiObject>() 
				{
					public DiObject mapRow(ResultSet rs, int n) throws SQLException 
					{
						DiObject obj = new DiObject();
						obj.setId(rs.getInt("ID"));
						obj.setName(rs.getString("NAME"));
						return obj;
					}
				});
		
		return result;
		
	}
}

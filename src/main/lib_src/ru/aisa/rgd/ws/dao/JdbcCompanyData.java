package ru.aisa.rgd.ws.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcDaoSupport;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.ParameterizedRowMapper;

import ru.aisa.rgd.ws.exeption.ServiceException;
import sheff.rjd.services.billing.CompanyDataObject;
import sheff.rjd.services.billing.DocumentObject;

public class JdbcCompanyData extends NamedParameterJdbcDaoSupport implements CompanyData {

	public JdbcCompanyData( DataSource ds) {
		super();
		setDataSource(ds);
	}


		private static final String PRED_ID_BY_HEADID_SQL = "select id from SNT.Pred where headid is null";
		
		private static final String DATA_DOC_BY_DOCSTORE_AND_CRDATE = "select count(*) count, ds.etdid, rtrim(dt.name) name, pr.inn from snt.docstore ds, snt.pred pr, snt.perswrk pw, snt.doctype dt where month(ds.crdate) = :month and year(ds.crdate) = :year and ds.predid = :predid and ds.predid = pr.id and ds.predid = pw.predid and ds.typeid = dt.id group by ds.etdid, dt.name, pr.inn, pw.predid";
		
		private static final String DATA_DOC_BY_DOCSTORE_AND_LDATE = "select count(*) count, ds.etdid, rtrim(dt.name) name, pr.inn from snt.docstore ds, snt.pred pr, snt.perswrk pw, snt.doctype dt where month(ds.ldate) = :month and year(ds.ldate) = :year and ds.predid = :predid and signlvl is null and ds.predid = pr.id and ds.predid = pw.predid and ds.typeid = dt.id and typeid in (select id from snt.doctype where name in ('ФПУ-26','МХ-1','МХ-3','Акт приема передачи ТМЦ')) group by ds.etdid, dt.name, pr.inn, pw.predid";
			
		private static final String DATA_All_DOC = "select count(*) count, etdid, name, inn, signed from (select ds.etdid etdid, rtrim(dt.name) name, pr.inn inn,case when (month(ds.ldate) = :month and year(ds.ldate) = :year and signlvl is null and typeid in (select id from snt.doctype where name in('ФПУ-26', 'МХ-1', 'МХ-3', 'Акт приема передачи ТМЦ'))) then '1'  else '0' end as signed from snt.docstore ds, snt.pred pr, snt.perswrk pw, snt.doctype dt where month(ds.crdate) = :month and year(ds.crdate) = :year and ds.predid in (:predid) and ds.predid = pr.id and ds.predid = pw.predid and ds.typeid = dt.id) d group by etdid, name, inn, signed";
	

		public List<Long> getPredids(){
			Map<String, Object> param = new HashMap<String, Object>();
			
			return  getNamedParameterJdbcTemplate().queryForList(PRED_ID_BY_HEADID_SQL,param,Long.class);
		}
		
		
		public List<CompanyDataObject> getDataCompany(int month, int year, List<Long> predIds) {
	        final Map<String, CompanyDataObject> map = new HashMap<String, CompanyDataObject>();
	        Map<String, Object> param = new HashMap<String, Object>();
	        List<CompanyDataObject> result = new ArrayList<CompanyDataObject>();

	        param.put("month", month);
	        param.put("year", year);
	        param.put("predid", predIds);
	       
	       try {
	    	   result = getNamedParameterJdbcTemplate().query(DATA_All_DOC, param,
	                    new RowMapper<CompanyDataObject>() {
	                        public CompanyDataObject mapRow(ResultSet rs, int rowNum) throws SQLException {
	                            String inn = rs.getString("inn");
	                            CompanyDataObject companyDataObject = map.get(inn);
	                            if (companyDataObject == null) {
	                            	companyDataObject = new CompanyDataObject();
	                                companyDataObject.setInnCompany(inn);
	                                List<DocumentObject> all = new ArrayList<DocumentObject>();
	                                List<DocumentObject> sign = new ArrayList<DocumentObject>();
	                                companyDataObject.setAll(all);
	                                companyDataObject.setSign(sign);
	                                companyDataObject.setCountUserCompany(rs.getInt("count"));
	                                map.put(inn, companyDataObject);
	                            }
	                            DocumentObject document = new DocumentObject(rs.getInt("etdid"), rs.getString("name"));
	                            companyDataObject.getAll().add(document);
	                            if (rs.getInt("signed") == 1) {
	                                companyDataObject.getSign().add(document);
	                            }
	                            return null;
	                        }
	                    });
	        } catch (Exception e) {
	            throw new ServiceException(new Exception(), -1,
	                    "Внутренняя ошибка сервиса");
	        }
	        return new ArrayList<CompanyDataObject>(map.values());
	    }
		
		
}	
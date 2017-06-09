package zpss.rjd.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.log4j.Logger;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.ParameterizedRowMapper;

import sheff.rjd.dbobjects.DocObject;
import sheff.rjd.mappers.TemplateMapper;

public class TemplateDao {
	 private NamedParameterJdbcTemplate npjt;
	 
	 private static Logger	log	= Logger.getLogger(TemplateDao.class);
	 
		public NamedParameterJdbcTemplate getNpjt() {
			return npjt;
		}
		public void setNpjt(NamedParameterJdbcTemplate npjt) {
			this.npjt = npjt;
		}
		public DocObject GetTemplate (String Name){
			 Map<String, String> pp = new HashMap<String, String>();
			 pp.put("Name", Name);
		     List <DocObject> out=getNpjt().query("select template from SNT.DocType where name = :Name and ptype = 0 ",pp, new TemplateMapper());
			 return out.get(0);
			
		}
}

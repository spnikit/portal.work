package sheff.rjd.utils;

import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

public class Change_char {
	private NamedParameterJdbcTemplate npjt;
	public NamedParameterJdbcTemplate getNpjt() {
		return npjt;
	}

	public void setNpjt(NamedParameterJdbcTemplate npjt) {
		this.npjt = npjt;
	}
	
	
	
	public String  change (String sql,String bb, Map<String, Integer> pp ){
		List l = getNpjt().queryForList(sql,pp);
		String aa= new String();
		if (l.size()>0) {
		Map tmp = (Map)l.get(0);
		aa= tmp.get(bb).toString();
		aa.replace((char) 26, ' ');
		}
		return aa;
		
	}

}

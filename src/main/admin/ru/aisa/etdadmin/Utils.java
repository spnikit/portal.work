package ru.aisa.etdadmin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcTemplate;
import org.springframework.security.authentication.encoding.Md5PasswordEncoder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class Utils {
	
	public static String ROLE_ADMIN="ROLE_ADMIN";
	public static String ROLE_USER="ROLE_USER";
	public static String ROLE_READONLY="ROLE_READONLY";
	public static String ROLE_POWER_USER="ROLE_POWER_USER";
	public static String ROLE_ANONYMOUS="ROLE_ANONYMOUS";
	private static NamedParameterJdbcTemplate npjt;
	private static SimpleJdbcTemplate sjt;
	public static String code = " unicode ";
	public static String FORMS_SNT = "'форма','1','2'";
	//private static Logger log = Logger.getLogger(Utils.class);
	

	public static List<Object[]> getIdsOfPred(final int predId,final int userId,final int wrkId, final int dorId, boolean check){
		final List<Object[]> list = new  ArrayList<Object[]>();
		list.add(new Object[]{userId,wrkId,predId,dorId});
	/*	HashMap<String,Integer> parameterMap = new HashMap<String, Integer>();
		parameterMap.put("predid", predId);
		String sql_zone;
		String sql_prim;
		if(check){
			sql_zone = "select stanid from letd.predzone where predid = :predid and" +
					" not stanid in (select predid from letd.perswrk where pid = :pid and wrkid = :wrk and dorid = :dorid)";
			sql_prim = "select stprim from letd.pred where id = :predid and not stprim  is null and" +
					" not stprim in (select predid from letd.perswrk where pid = :pid and wrkid = :wrk and dorid = :dorid)";
			parameterMap.put("pid", userId);
			parameterMap.put("wrk", wrkId);
			parameterMap.put("dorid", dorId);
		}
		else{
			sql_zone = "select stanid from letd.predzone where predid = :predid";
			sql_prim = "select stprim from letd.pred where id = :predid and not stprim  is null"; 
		}
		npjt.query(sql_zone, parameterMap,
					new ParameterizedRowMapper<Object>() {
				public Object mapRow(ResultSet rs, int numrow) throws SQLException {
					list.add(new Object[]{userId,wrkId,rs.getInt("stanid"),dorId});
					return null;
				}});
		if(list.size()==0)
		npjt.query(sql_prim, parameterMap,
				new ParameterizedRowMapper<Object>() {
			public Object mapRow(ResultSet rs, int numrow) throws SQLException {
				list.add(new Object[]{userId,wrkId,rs.getInt("stprim"),dorId});
				return null;
			}});*/
		return list;
	}

	public static List<Object[]> getIdsOfPred(final int predId,final int userId,final int wrkId, final int dorId){
		return getIdsOfPred(predId, userId, wrkId, dorId, false);
	}

	public static int getDorIdForCurrentUser()
	{
		
			Authentication auth = SecurityContextHolder.getContext().getAuthentication();
			return  sjt.queryForInt("SELECT DORID FROM snt.DORUSER WHERE USERNAME = ? ", new Object[]{auth.getName()});
	}
	public void init() {
		if(npjt.queryForInt("select count(0) from snt.doruser where length(password)>=1",
				new HashMap())==0){
			Md5PasswordEncoder md5 = new Md5PasswordEncoder();
			npjt.update("delete from snt.doruser", new HashMap());
			HashMap<String,Comparable>hm=new HashMap<String, Comparable>();
			hm.put("ID", 1);
			hm.put("NAME", "admin");
			hm.put("PASSWORD", md5.encodePassword("password", null));
			hm.put("ROLE", ROLE_ADMIN);
			npjt.update("insert into snt.doruser (id,dorid,username,password,authority) values(:ID,(select min(id) from snt.dor),:NAME,:PASSWORD,:ROLE)", hm);
		}
	}
	public static int getIdForCurrentUser() 
	{	
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
			return sjt.queryForInt("SELECT ID FROM snt.DORUSER WHERE USERNAME = ? ", new Object[]{auth.getName()});
	}
	public static boolean isAdmin() {
		boolean role=false;
		
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		for( int i=0;i<auth.getAuthorities().size();i++)
			if(auth.getAuthorities().toArray()[i].equals(ROLE_ADMIN)||auth.getAuthorities().toArray()[i].equals(ROLE_READONLY))
				return true;
		return false;
		
	}
	public static String getAuth(){
		return SecurityContextHolder.getContext().getAuthentication().getAuthorities().toArray()[0].toString();
	}
	public static boolean isReadonly(){
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		for( int i=0;i<auth.getAuthorities().size();i++)
			if(auth.getAuthorities().toArray()[i].equals(ROLE_READONLY)||auth.getAuthorities().toArray()[i].equals(ROLE_POWER_USER))
				return true;
		return false;
	}
	public static String getUserName() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		return auth.getName();
	}
	public static NamedParameterJdbcTemplate getNpjt() {
		return npjt;
	}
	public void setNpjt(NamedParameterJdbcTemplate npjt) {
		Utils.npjt = npjt;
	}
	public static SimpleJdbcTemplate getSjt() {
		return sjt;
	}
	public void setSjt(SimpleJdbcTemplate sjt) {
		Utils.sjt = sjt;
	}
	
	
	public static String get6Kod(int station){
		if (station > 0) {
		int length = 5;
		int sumr = 0;
		int kr = 0;
		int ind = 1;
		
		String tmp = "";
		if (String.valueOf(station).length() == 5 ) tmp = "00000"+String.valueOf(station);
		else if (String.valueOf(station).length() == 4 ) tmp = "000000"+String.valueOf(station);
		else return "";
		
		String sym_kod = Utils.db2substr(tmp, 11-length, length);
		while (ind<=length) {
			sumr += ind * Integer.valueOf(Utils.db2substr(sym_kod,ind,1));
			ind += 1;			
		}
		kr = sumr - 11 * (sumr / 11);
		if (kr<10) return String.valueOf(kr);
		sumr = 0;
		kr = 0;
		ind = 1;
		while (ind<=length) {
			sumr += (ind+2) * Integer.valueOf(Utils.db2substr(sym_kod,ind,1));
			ind += 1;			
		}
		kr = sumr - 11 * (sumr / 11);
		if (kr==10) kr = 0;
		return String.valueOf(kr);
		}
		else return "";
		
	}
	
	private static String db2substr(String str, int start, int length){
		return str.substring(start-1, start+length-1);
	}

}

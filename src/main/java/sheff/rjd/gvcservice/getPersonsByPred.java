package sheff.rjd.gvcservice;


import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.log4j.Logger;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;

import ru.aisa.rgd.etd.extend.SNMPSender;
import sheff.rjd.utils.XMLUtil;

public class getPersonsByPred extends AbstractController {

	/*public static String sql = "select id, " +
			//"rtrim(fname) " +
	" rtrim(cast(rtrim(fname) || ' ' || rtrim(mname) || ' ' || rtrim(lname) as char(240) ccsid unicode)) "+
			"name from SNT.personall where id in " +
	" (select relateId from SNT.PERSRelates where Id=:Id and predId =:predId and relatepredid=:predSnd)   order by name";//"select OBJECT_KOD, OBJECT_NAME, OBJECT_VNAME, OBJECT_SNAME from letd.DIC_OBJECTS where class_id = :ID order by OBJECT_KOD"; //test
	*/
	public static String sql = "select id, " +
	//"rtrim(fname) " +
" rtrim(cast(rtrim(fname) || ' ' || rtrim(mname) || ' ' || rtrim(lname) as char(240) ccsid unicode)) "+
"name from SNT.personall where id in (select distinct pid from snt.perswrk where predid = :predId )" ;

	
	
	private NamedParameterJdbcTemplate npjt;
	private static Logger	log	= Logger.getLogger(getPersonsByPred.class);
	
	public NamedParameterJdbcTemplate getNpjt() {
		return npjt;
	}
	
	public void setNpjt(NamedParameterJdbcTemplate npjt) {
		this.npjt = npjt;
	}
	
	public getPersonsByPred(){		
	}

	
	protected ModelAndView handleRequestInternal(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		String top = "<data xmlns=\"\">";
		String middle = ""; 
		String bottom = "</data>";
		try{
			BufferedReader br = new BufferedReader(new InputStreamReader(request.getInputStream()));
			StringBuilder sb = new StringBuilder();
			String line = null;
			while ((line = br.readLine()) != null) {
			sb.append(line + "\n");
			}
			br.close();
			//int predId = Integer.valueOf(XMLUtil.parseXmlWithSax(sb.toString(), "predId"));			
						
			int predSnd = Integer.valueOf(XMLUtil.parseXmlWithSax(sb.toString(), "predSnd"));
			//int userId = Integer.valueOf(XMLUtil.parseXmlWithSax(sb.toString(), "userId"));
			
			Map<String, Object> pp = new HashMap<String, Object>();
			
			
				//pp.put("predId", predId);
				pp.put("predId", predSnd);
				//pp.put("Id", userId);
				@SuppressWarnings("unchecked")
				List<Map<String, Object>> list = npjt.query(sql, pp, new RowMapper(){
					public Object mapRow(ResultSet rs, int arg1) throws SQLException {
						Map<String, Object> map = new HashMap<String, Object>();
						map.put("id", rs.getInt("id"));
						map.put("name", rs.getString("name").trim());
						return map;
					}
				});	
				middle += "<el name=\'" +"Отправить всем" + "\'>" +"Отправить всем" +  "</el>";
				
				for (Map<String, Object> map : list){
					middle += "<el name=\'" +map.get("name") + "\'>" +map.get("name") +  "</el>";
					}
			
		}catch (Exception e) {
			middle = "<el name=\"\"></el>";
			StringWriter outError = new StringWriter();
			PrintWriter errorWriter = new PrintWriter( outError );
			e.printStackTrace(errorWriter);
			log.error(outError.toString());
			e.printStackTrace(System.out);
			SNMPSender.sendMessage(e);
		}
		
		if (middle.length()==0){
			middle = "<el name=\"\"></el>";
		}
		response.setCharacterEncoding("UTF-8");
		response.setContentType("text/xml");
		response.getWriter().print(top+middle+bottom);
		//System.out.println(response);
		return null;
		
		
	}
	
}
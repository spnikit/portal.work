package sheff.rjd.gvcservice;


import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;

import ru.aisa.rgd.etd.extend.SNMPSender;
import sheff.rjd.utils.XMLUtil;

public class getPredsForCombo extends AbstractController {


	public static String sql = "select id, rtrim(vname) name from SNT.pred where id in " +
	"(select relatePredId from SNT.predRelates where predId=:predId )";//"select OBJECT_KOD, OBJECT_NAME, OBJECT_VNAME, OBJECT_SNAME from letd.DIC_OBJECTS where class_id = :ID order by OBJECT_KOD"; //test
	
	private NamedParameterJdbcTemplate npjt;
	private static Logger	log	= Logger.getLogger(getPredsForCombo.class);
	
	public NamedParameterJdbcTemplate getNpjt() {
		return npjt;
	}
	
	public void setNpjt(NamedParameterJdbcTemplate npjt) {
		this.npjt = npjt;
	}
	
	public getPredsForCombo(){		
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
			
			int id = Integer.valueOf(XMLUtil.parseXmlWithSax(sb.toString(), "predId"));
			
			Map<String, Object> pp = new HashMap<String, Object>();
			
			
				pp.put("predId", id);
				@SuppressWarnings("unchecked")
				List<Map<String, Object>> list = npjt.query(sql, pp, new RowMapper(){
					public Object mapRow(ResultSet rs, int arg1) throws SQLException {
						Map<String, Object> map = new HashMap<String, Object>();
						map.put("id", rs.getInt("id"));
						map.put("name", rs.getString("name").trim());
						return map;
					}
				});	
				
				for (Map<String, Object> map : list){
					middle += "<el name=\'" +map.get("name")+ "\'"+" id=\'"+map.get("id")+ "\'" +">" +map.get("name") +  "</el>";
					}		
			
		}catch (Exception e) {
			middle = "<el name=\"\"></el>";
			StringWriter outError = new StringWriter();
			PrintWriter errorWriter = new PrintWriter( outError );
			e.printStackTrace(errorWriter);
			log.error(outError.toString());
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

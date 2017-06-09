package ru.aisa.etdadmin.controllers.NormalImpl;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.ServletContext;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.jdbc.core.simple.ParameterizedRowMapper;
import org.springframework.web.servlet.ModelAndView;

import ru.aisa.etdadmin.controllers.AbstractMultipartController;
import ru.aisa.rgd.ws.utility.TypeConverter;
import sheff.rjd.utils.AppCtxGetterUtils;

import org.apache.tools.zip.*;
import org.codehaus.plexus.util.StringUtils;
//import org.apache.commons.compress.archivers.zip.ZipArchiveOutputStream;




public class UploadProtocol extends AbstractMultipartController {
	
	private final static String HTML = ".html";
	private final static String fileName = "protocol.html";
	public UploadProtocol() throws JSONException{
	super();
	}
	
	@Override
	protected ModelAndView handleRequestInternal(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
				
		//String headidData = request.getParameter("gridData");
		String predid = request.getParameter("predid");
		String dateStart = request.getParameter("dateFrom");
		String dateEnd = request.getParameter("dateTo");
		DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		DateFormat formatter2 = new SimpleDateFormat("dd.MM.yyyy");
		Date dateFrom = Calendar.getInstance().getTime() ; 
		Date dateTo = Calendar.getInstance().getTime() ; 
		int count = 0;
		
		String sql = "SELECT ds.etdid,ds.vagnum,(select rtrim(name) predname from snt.pred where id = :predid) head, "
	    		+ "(select rtrim(name) predname from snt.pred where id = (select predid from snt.docstoreflow where docid = ds.id and order = 2)) filial, "
	    		+ "(SELECT DISTINCT(kname) FROM snt.pred_fil WHERE kid=ds.ONBASEID) AS kname2, "
	    		+ "(SELECT dname FROM snt.dor AS dor WHERE cast(cast(dor.id as char(200)) as varchar(200))=(SELECT g2.P_19 FROM snt.docstore AS ds2, xmltable('$c/data' passing docdata AS \"c\" columns P_19 VARCHAR(200) path 'P_19') AS g2 WHERE ds2.etdid=cast(ds.id_pak as INT))) AS DI, "
	    		+ "g.P_32a, g.P_32v, g.P_33a, g.P_33v FROM snt.docstore AS ds, xmltable('$c/data' passing docdata AS \"c\" columns P_32a VARCHAR(200) path 'P_32a', P_32v VARCHAR(200) path 'P_32v', P_33a VARCHAR(200) path 'P_33a', P_33v VARCHAR(200) path 'P_33v') AS g "
	    		+ "WHERE ds.typeid=(select id from snt.doctype where name =:typename) AND ds.crdate between :dateFrom and :dateTo AND ds.predid in (select id from snt.pred where id = :predid or headid = :predid) and signlvl is null";
			
		/*String sql =  "select ds.etdid, ds.vagnum, (select distinct(kname) "
				+ "from snt.pred_fil where kid=ds.ONBASEID) as kname2, g.P_32a, g.P_32v, g.P_33a, g.P_33v "
				+ "from snt.docstore as ds, xmltable('$c/data' passing docdata AS  \"c\" columns P_32a varchar(200) path 'P_32a', P_32v varchar(200) path 'P_32v', P_33a varchar(200) path 'P_33a', P_33v varchar(200) path 'P_33v') as g "
				+ "where ds.typeid=:predid and ds.crdate between :dateFrom and :dateTo and ds.predid= :predid";*/
		Map<String, Object> defaultParam = new HashMap<String, Object>();
		/*System.out.println(predid);
		System.out.println(dateStart);
		System.out.println(dateEnd);*/
		//System.out.println(predid);
		//predid = "6";
		if(predid!= null&&dateStart != null&&dateEnd != null){
			int valuePredid = Integer.parseInt(predid);
			try {
				dateFrom = formatter.parse(dateStart);
				dateTo = formatter.parse(dateEnd);
			} catch (ParseException e) {
				log.error(TypeConverter.exceptionToString(e));
			}
			defaultParam.put("predid", valuePredid);
			defaultParam.put("dateFrom", dateFrom);
			defaultParam.put("dateTo", dateTo);
		}else{
			defaultParam.put("predid", 0);
			defaultParam.put("dateFrom", null);
			defaultParam.put("dateTo", null);
		}
		defaultParam.put("typename", "ФПУ-26");
		final Map <Integer, List<String>> resultMap = new HashMap<Integer, List<String>>();
		getNpjt().query(sql, defaultParam,
				new ParameterizedRowMapper<Object>() {

			public Object mapRow(ResultSet rs, int numrow)
					throws SQLException {
			List<String> list = new ArrayList<String>();
			list.add(rs.getString("ETDID"));
			list.add(rs.getString("VAGNUM"));
			list.add(rs.getString("kname2"));
			list.add(rs.getString("P_32a"));
			list.add(rs.getString("P_32v"));
			list.add(rs.getString("P_33a"));
			list.add(rs.getString("P_33v"));
			list.add(rs.getString("DI"));
			list.add(rs.getString("head"));
			list.add(rs.getString("filial"));
			resultMap.put(numrow, list);
			return null;
			}
		});
	
			response.setHeader("Content-disposition", "attachment; filename=package.zip");
			response.setContentType("application/zip; charset=UTF-8");
			ServletOutputStream respout = response.getOutputStream();
			ZipOutputStream zout = new ZipOutputStream(respout);
			zout.setEncoding("CP866");			
			
			
			ServletContext context = request.getSession().getServletContext();
			String realPath = context.getRealPath("admin/protocol.html");
			File file = new File(realPath);
			
//			FileInputStream inFile = new FileInputStream(file);
//			byte[] str = new byte[inFile.available()];
//			inFile.read(str);
//			String text = new String(str);
//			inFile.close();
			for(Entry entry : resultMap.entrySet()){
				FileInputStream inFile = new FileInputStream(file);
				byte[] str = new byte[inFile.available()];
				inFile.read(str);
				String text = new String(str);
				inFile.close();
				
	        	List<String> list = new ArrayList<String>();
	        	list = (List<String>) entry.getValue();
	        	/*System.out.println(list.size());
	        	for(int k = 0; k<list.size();k++){
	        		System.out.println(k+"="+list.get(k));
	        	}*/
	        	ZipEntry ze = new ZipEntry(list.get(1) + "_" + count + HTML);
	        	if(list.get(1)==null){
	        		text = text.replace("VAGNUM", "Вагон не определен");
	        	}else{
	        		text = text.replace("VAGNUM", list.get(1));
	        	}
	        	if(StringUtils.isBlank(list.get(2))){
	        		text = text.replace("KNAME2", "-----");
	        	}else{
	        		text = text.replace("KNAME2", list.get(2));
	        	}
	        	if(StringUtils.isBlank(list.get(3))){
	        		text = text.replace("POS_RZD", "-----");
	        	}else{
	        		text = text.replace("POS_RZD", list.get(3));
	        	}
	        	if(StringUtils.isBlank(list.get(4))){
	        		text = text.replace("FIO_RZD", "-----");
	        	}else{
	        		text = text.replace("FIO_RZD", list.get(4));
	        	}
	        	if(StringUtils.isBlank(list.get(5))){
	        		text = text.replace("POS_PORTAL", "-----");
	        	}else{
	        		text = text.replace("POS_PORTAL", list.get(5));
	        	}
	        	if(StringUtils.isBlank(list.get(6))){
	        		text = text.replace("FIO_PORTAL", "-----");
	        	}else{
	        		text = text.replace("FIO_PORTAL", list.get(6));
	        	}
	        	if(StringUtils.isBlank(list.get(7))){
	        		text = text.replace("DI", "-----");
	        	}else{
	        		text = text.replace("DI", list.get(7));
	        	}
	        	if(StringUtils.isBlank(list.get(8))){
	        		text = text.replace("COMPANY", "-----");
	        	}else{
	        		text = text.replace("COMPANY", list.get(8));
	        	}
	        	if(StringUtils.isBlank(list.get(9))){
	        		text = text.replace("FIL", "-----");
	        	}else{
	        		text = text.replace("FIL", list.get(9));
	        	}
	        	
	        	Date date = new Date();
	        	
	        	String date1 = formatter2.format(date);
	        	text = text.replace("DATE", date1);
	        	
	        	
	        	
	        	
	        	byte[] data = text.getBytes();
	        	zout.putNextEntry(ze);
	        	zout.write(data);
				zout.closeEntry();
				count++;
	        	
			}
			zout.close();
			respout.flush();
			respout.close();		
		return null;
	}
	
}
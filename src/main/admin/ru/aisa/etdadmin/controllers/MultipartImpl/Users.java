package ru.aisa.etdadmin.controllers.MultipartImpl;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.codec.binary.Base64;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.jdbc.core.simple.ParameterizedRowMapper;

import ru.aisa.crypto.FieldType;
import ru.aisa.crypto.WhoType;
import ru.aisa.crypto.X509Parser;
import ru.aisa.crypto.X509ParserFactory;
import ru.aisa.crypto.utils.FIO;
import ru.aisa.crypto.utils.X509Utils;
import ru.aisa.etdadmin.Utils;
import ru.aisa.etdadmin.controllers.AbstractMultipartController;
import ru.aisa.rgd.ws.utility.TypeConverter;

public class Users extends AbstractMultipartController {

	public Users() throws JSONException {
		super();
	}
	private String serialFromFile;
	
	private String name;
		
	public String getName() {
		return name;
	}
	public void setName(String name) {
//	System.out.println(name);
		this.name = name;
	}
	
	
	
	private String checkFile(String fileName,byte[] fileContent) {
		String extension;
		try {
			extension = fileName.substring(fileName.lastIndexOf(".") + 1); // !
		} catch (Exception ex) {
			extension = null;
		}
		if (extension == null || !extension.equalsIgnoreCase("cer")) {
			return "Неправильное расширение файла!";
		}
		if (fileContent == null || fileContent.length == 0) {
			return "Отсутсвует файл сертификата";
		}
		//String certserial;
		try {
			InputStream is = new ByteArrayInputStream(fileContent);
			CertificateFactory cf = CertificateFactory.getInstance("X.509");
			Certificate cert = cf.generateCertificate(is);
			X509Certificate cert509 = (X509Certificate) cert;
			serialFromFile = cert509.getSerialNumber().toString();
			setName(cert509.getSubjectDN().getName().toString());
			is.close();
		} catch (Exception e) {
			return "Неправильный файл сертификата";
		}

		return null;
		
	}
	@Override
	protected JSONObject add(HashMap<String, String> requestParameters,
			HashMap<String, byte[]> requestFiles,HashMap<String, String> requestFileNames) throws JSONException,
			IOException {
		if(Utils.isReadonly()) return FORBIDDEN;
		serialFromFile = null;
		JSONObject response = new JSONObject();
		response.put(success, false);
		Integer auto;
		Integer sgn;
		//int id = Integer.parseInt(requestParameters.get("id"));
		String fname = requestParameters.get("fname");
		String mname = requestParameters.get("mname");
		String lname = requestParameters.get("lname");
		String title = requestParameters.get("title");
		if (requestParameters.get("auto") != null)
			auto = 1;
		else
			auto = 0;
			
		if (requestParameters.get("sgn") != null)
			sgn = 1;
		else
			sgn = 0;
		
		response.put(description,checkFile(requestFileNames.get("certfile"), requestFiles.get("certfile")));
		if (response.has(description)){
			return response;
			}
		String certserial = serialFromFile;	
//		System.out.println(certserial);
		byte[] fileContent = requestFiles.get("certfile");	
		
		
		
		
		if(fname.equalsIgnoreCase("Заполнить")){
			
			
			FIO fio = getNameFromCert(getName(), fileContent);
			title = getTitlefromCert(getName(), fileContent);
			fname = fio.getLastName();
			mname = fio.getFirstName();
			lname = fio.getMiddleName();
			response.put("name0", fname);
			response.put("name1", mname);
			response.put("name2", lname);
			response.put("title", title);
			
			
			response.put(success, true);
			return response;
		
		}
		//power
							
		
		int c = getSjt().queryForInt(
				"SELECT count(0) FROM SNT.PERSONALL WHERE CERTSERIAL = ? ",
				new Object[] { certserial });
		
		if (c > 0) {
			response.put(description,
					"Номер сертификата должен быть уникальным");
			return response;
			}
		
		
		int id = getSjt().queryForInt("SELECT MAX(ID)+1 FROM SNT.personall");
		getSjt()
				.update(
						"INSERT INTO SNT.personall( DEPID , FNAME , MNAME , LNAME , TITLE, CERTSERIAL , ID, AUTOSGN, ISSGN) "
						+ "VALUES ((select min(id) from SNT.department), ? , ? , ? , ? ,?, ?, ?, ?)",
						new Object[] {  fname, mname, lname, title, certserial,
								id, auto, sgn });
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.put("contentBlob", fileContent);
		parameterMap.put("id", id);
		getNpjt()
				.update(
						"UPDATE SNT.PERSONALL SET PUBLICKEY = :contentBlob WHERE ID = :id",
						parameterMap);
		int otdel= Integer.parseInt(requestParameters.get("otdel"));
		int pred = Integer.parseInt(requestParameters.get("pred"));
		getSjt()
		.update(
			"insert into SNT.perswrk (pid,wrkid,predid) values( ?,?,?)",
						new Object[] { id, otdel, pred });
		response.put(success, true);

//} else 
	{response.put(success, true);
	}
		return response;
	}

	@Override
	protected JSONObject delete(HttpServletRequest request)
			throws JSONException {{
		if(Utils.isReadonly()) return FORBIDDEN;
		JSONObject response = new JSONObject();
		response.put(success, false);
		HashMap<String, Integer> parameterMap = new HashMap<String, Integer>();
		parameterMap.put("id", Integer.parseInt(request.getParameter("id")));
		
		if (getSjt()
				.queryForInt(
						"select count(0) from SNT.docstore where lpersid = :id or inuseid = :id or dropid = :id ",
						parameterMap) != 0) {
			response.put(description,
					"Нельзя удалить пользователя, сохранявшего документы");
			return response;
		}//POWER
		if (getSjt()
				.queryForInt(
						"select count(0) from SNT.docstoreflow where persid = :id ",
						parameterMap) != 0) {
			response.put(description,
					"Нельзя удалить пользователя, подписывавшего документы");
			return response;
		}
		/*if(getSjt().queryForInt("select count(0) from SNT.perswrk where pid= :id ",
				parameterMap)!=0){
			response.put(description, "Нельзя удалить пользователя у которого существуют привязки к предприятиям.");
			return response;
		}*/
		if (Utils.getAuth().equals(Utils.ROLE_POWER_USER))
			{
			final ArrayList<Integer> out= new ArrayList();
			
			 HashMap dor=new HashMap();
			getNpjt().query("SELECT dorid FROM SNT.WRKNAME WHERE ID in " +
				"(SELECT WRKID FROM SNT.PERSWRK WHERE PID="+Integer.parseInt(request.getParameter("id"))+")",dor, new ParameterizedRowMapper<Object>() {
			public Object mapRow(ResultSet rs, int numrow){
		
		try {
			out.add(rs.getInt("dorid"));
			}
			catch (Exception e) {
				
			}
			return null;
			
			}});
			if (out.equals(Utils.getDorIdForCurrentUser())){
			
			getSjt().update("delete from SNT.perswrk where pid = :id", parameterMap);
			getSjt().update("delete FROM SNT.personall WHERE id = :id ",
					parameterMap);
			}
			response.put(success, true);
			return response;
			}

		
			
		
			else getSjt().update("delete from SNT.perswrk where pid = :id", parameterMap);
			getSjt().update("delete FROM SNT.personall WHERE id = :id ",
					parameterMap);
			response.put(success, true);
			return response;
			}

	}
	
	@Override
	protected JSONObject edit(HashMap<String, String> requestParameters,
			HashMap<String, byte[]> requestFiles,HashMap<String, String> requestFileNames) throws JSONException,
			IOException {
		if(Utils.isReadonly()) return FORBIDDEN;
		serialFromFile = null;
		final JSONObject response = new JSONObject();
		//response.put("dor", new JSONArray());
		response.put(success, false);
		int id = Integer.parseInt(requestParameters.get("id"));
		
		
		Integer auto;
		if (requestParameters.get("auto") != null)
			auto = 1;
		else
			auto = 0;
	
		Integer sgn;
		if (requestParameters.get("sgn") != null)
			sgn = 1;
		else
			sgn = 0;
		
		String certserial = requestParameters.get("certserial");
		byte[] fileContent = requestFiles.get("certfile");
		
		
		String fname = requestParameters.get("fname");
		String mname = requestParameters.get("mname");
		String lname = requestParameters.get("lname");
		String title = requestParameters.get("title");
		
		if(fileContent != null && fileContent.length != 0){
			response.put(description,checkFile(requestFileNames.get("certfile"),fileContent));	
		}
		if(response.has(description)){
			return response;
		}

		certserial = serialFromFile == null ? certserial : serialFromFile;

		if(fname.equalsIgnoreCase("Заполнить")){
			
			
			FIO fio = getNameFromCert(getName(), fileContent);
			 title = getTitlefromCert(getName(), fileContent);
			fname = fio.getLastName();
			mname = fio.getFirstName();
			lname = fio.getMiddleName();
			
			
			response.put("name0", fname);
			response.put("name1", mname);
			response.put("name2", lname);
			response.put("title", title);
			
			
			response.put(success, true);
			return response;
		
		}
		
		
		int c = getSjt()
				.queryForInt(
						"SELECT count(0) FROM SNT.PERSONALL WHERE CERTSERIAL = ? AND ID <> ? ",
						new Object[] { certserial, id });
				
		if (c > 0) {
			response.put(description,
					"Номер сертификата должен быть уникальным");
			return response;
		}
		
		//power
		
		if (Utils.getAuth().equals(Utils.ROLE_POWER_USER))
		{
		final ArrayList<Integer> out= new ArrayList();
		
		 HashMap dor=new HashMap();
		 getNpjt().query("SELECT dorid FROM SNT.WRKNAME WHERE ID in " +
					"(SELECT WRKID FROM SNT.PERSWRK WHERE PID="+id+")",dor, new ParameterizedRowMapper<Object>() {
				public Object mapRow(ResultSet rs, int numrow){
			
			try {
				out.add(rs.getInt("dorid"));
					}
				catch (Exception e) {
					
				}
				return null;
				
				}});
				if (out.contains(Utils.getDorIdForCurrentUser())){
					getSjt()
					.update(
							"UPDATE SNT.PERSONALL SET FNAME=? , MNAME=? , LNAME=? , TITLE = ?, CERTSERIAL=?, AUTOSGN=?, ISSGN=? WHERE ID=?",
							new Object[] {  fname, mname, lname, title, certserial,  auto, sgn,
									id });
			if (serialFromFile != null) {
				Map<String, Object> parameterMap = new HashMap<String, Object>();
				parameterMap.put("contentBlob", fileContent);
				parameterMap.put("id", id);
				getNpjt()
						.update(
								"UPDATE SNT.PERSONALL SET PUBLICKEY = :contentBlob, AUTOSGN =:autosgn WHERE ID = :id",
								parameterMap);}
					
					response.put(success, true);
				return response;
		}
		}		
		else  getSjt()
		.update(
				"UPDATE SNT.PERSONALL SET FNAME=? , MNAME=? , LNAME=? , TITLE = ?, CERTSERIAL=?, AUTOSGN=?, ISSGN=? WHERE ID=?",
				new Object[] {  fname, mname, lname, title, certserial, auto, sgn,
						id });
if (serialFromFile != null) {
	Map<String, Object> parameterMap = new HashMap<String, Object>();
	parameterMap.put("contentBlob", fileContent);
	parameterMap.put("id", id);
	parameterMap.put("autosgn", auto);
	getNpjt()
			.update(
					"UPDATE SNT.PERSONALL SET PUBLICKEY = :contentBlob, AUTOSGN =:autosgn WHERE ID = :id",
					parameterMap);
	}
		
		response.put(success, true);
	return response;
	}
	
	
	
	@Override
	protected JSONObject get(HttpServletRequest request) throws JSONException {
		final JSONObject response = new JSONObject();
		response.put("data", new JSONArray());
		final int start;
		final int limit;
		if (request.getParameter("start") != null)
			start = Integer.parseInt(request.getParameter("start"));
		else
			start = 0;
		if (request.getParameter("limit") != null)
			limit = Integer.parseInt(request.getParameter("limit"));
		else
			limit = 20;
		String sort = request.getParameter("sort");
//		System.out.println("1111111111" + sort);
		if (sort == null
				|| !(sort.equalsIgnoreCase("lname")
						|| sort.equalsIgnoreCase("mname")
						|| sort.equalsIgnoreCase("depname") || sort
						.equalsIgnoreCase("certserial") || sort.equalsIgnoreCase("auto")))
			sort = "fname";
		if(sort.equalsIgnoreCase("auto")) sort = "autosgn";
		String dir = request.getParameter("dir");
		if (dir == null || !dir.equalsIgnoreCase("desc"))
			dir = "asc";
		String id_sql = "select id ";
		String sql = "SELECT id,";
		if (sort.equalsIgnoreCase("fname")){
			sql = sql + "rtrim(cast(fname as char(100) ccsid " + Utils.code
					+ ")) as fname ,";
			id_sql=id_sql+", rtrim(cast(fname as char(100) ccsid " + Utils.code
					+ ")) as fname ";
		}else if (sort.equalsIgnoreCase("lname")){
			sql = sql + "rtrim(cast(lname as char(100) ccsid " + Utils.code
					+ ")) as lname ,";
			id_sql = id_sql + ", rtrim(cast(lname as char(100) ccsid " + Utils.code
					+ ")) as lname";
		}else if (sort.equalsIgnoreCase("mname")){
			sql = sql + "rtrim(cast(mname as char(100) ccsid " + Utils.code
					+ ")) as mname ,";
			id_sql = id_sql + ",rtrim(cast(mname as char(100) ccsid " + Utils.code
			+ ")) as mname ";
		}else if(sort.equalsIgnoreCase("depname")){
			sql = sql + "cast((select rtrim(name) from SNT.department where id = depid) as char (100) ccsid" +
			Utils.code+ " ) as depname ,";
			id_sql = id_sql + ", cast((select rtrim(name) from SNT.department where id = depid) as char (100) ccsid" +
					Utils.code+ " ) as depname";
		}/*else if(sort.equalsIgnoreCase("autosgn")){
			
		}*/
		
		 id_sql = id_sql + " FROM SNT.PERSONALL pp " ;
		 
		sql = sql
				+ "rtrim(fname) as fname_utf ,rtrim(lname) as lname_utf ,rtrim(mname) as mname_utf,depid,autosgn, issgn,(select rtrim(name) from SNT.department where id = depid) as depname_utf, title as title_utf, CERTSERIAL FROM SNT.PERSONALL ";
		HashMap<String, Comparable> parameterMap = new HashMap<String, Comparable>();
		if(request.getParameter("search") != null){
			id_sql = id_sql + " where upper(pp.fname) like :search ";
			parameterMap.put("search", "%" + request.getParameter("search") + "%");
			if(/*Utils.getAuth().equals(Utils.ROLE_POWER_USER)||*/Utils.getAuth().equals(Utils.ROLE_USER)){
				id_sql = id_sql + " and id in (select pid from SNT.perswrk where wrkid in (select id from SNT.wrkname where dorid = :dorid )) ";parameterMap.put("dorid", Utils.getDorIdForCurrentUser());
			}
			
		}else if(/*Utils.getAuth().equals(Utils.ROLE_POWER_USER)||*/Utils.getAuth().equals(Utils.ROLE_USER)){
			id_sql = id_sql + " where id in (select pid from SNT.perswrk where wrkid in (select id from SNT.wrkname where dorid = :dorid )) ";
			parameterMap.put("dorid", Utils.getDorIdForCurrentUser());
		}
		if(!(sort.equalsIgnoreCase("certserial")||sort.equalsIgnoreCase("autosgn"))){
			id_sql = "select id from (" + id_sql + ") as w ";
		}
		id_sql = id_sql + " order by " + sort + " " + dir;
		sql = sql
		+ " where id in (:idList) ORDER BY ";
				
		List result = getNpjt().queryForList(id_sql, parameterMap);
		response.put("count", result.size());
		List<Integer> idList = new ArrayList<Integer>();
		for (int i = start; i < Math.min(start + limit, result.size()); i++) {
			idList.add((Integer) ((HashMap) result.get(i)).get("ID"));
		}
		HashMap<String, List> idsMap = new HashMap<String, List>();
		idsMap.put("idList", idList);
		if(idList.size()==0)
			return response;
		getNpjt().query(sql + sort + " " + dir, idsMap,
				new ParameterizedRowMapper<Object>() {

					public Object mapRow(ResultSet rs, int numrow)
							throws SQLException {
						
						try {
							JSONObject js = new JSONObject();
							js.put("id", rs.getInt("ID"));
							js.put("fname", rs.getString("FNAME_UTF"));
							js.put("lname", rs.getString("LNAME_UTF"));
							js.put("mname", rs.getString("MNAME_UTF"));
							js.put("title", rs.getString("TITLE_UTF"));
							js.put("depname", rs.getString("DEPNAME_UTF"));
							js.put("depid", rs.getInt("DEPID"));
							js.put("certserial", rs
												.getString("CERTSERIAL"));
							
							
							js.put("auto", rs.getInt("AUTOSGN"));
							js.put("sgn", rs.getInt("ISSGN"));
							response.accumulate("data", js);
						} catch (JSONException e) {
						}
						return null;
					}
				});
		return response;
	}
	@Override
	protected void export(HttpServletRequest request, HttpServletResponse response) throws IOException {
		HashMap<String, String> hm = new HashMap<String, String>();
		PrintWriter out = response.getWriter();
		if(request.getParameter("certid")==null||request.getParameter("certid").length()<1){
			out.println("certid is not specified");
			return;
		}
		
		hm.put("CERTSERIAL", new String(Base64.decodeBase64(request.getParameter("certid").getBytes()),"UTF-8"));
		try{
			byte[] cert = (byte[]) getNpjt().queryForObject("select publickey from SNT.personall where certserial = :CERTSERIAL ", hm, byte[].class);
			response.setHeader("Content-disposition", "attachment; filename=file.cer");
			response.setContentType("application/x-x509-ca-cert");
			out.print(new String(cert,codepage));
		}catch (Exception e) {
			out.println(e.getLocalizedMessage());
		}
		
	}
	
	private FIO getNameFromCert (String certstring, byte[] filecontent){
		FIO fio = null;
		try{
		X509Parser p = X509ParserFactory.getParser(filecontent);
		fio = X509Utils.parseFIO(p);
		} catch (Exception e){
			log.error(TypeConverter.exceptionToString(e));
		}
//		if (certstring.contains("SURNAME")&&certstring.contains("GIVENNAME")){
//			try{
//				StringBuffer sb= new StringBuffer();
//				
//				System.out.println(certstring);
//			String surname = certstring.substring(getName().indexOf("SURNAME=")+8);
//			surname = surname.substring(0, surname.indexOf(","));
//			sb.append(surname);
//			
//			
//			
//			System.out.println(sb.toString());
//			
//			} catch (Exception e){
//				e.printStackTrace();
//				log.error(TypeConverter.exceptionToString(e));
//
//			}
//			
//		}
//		
//		else {
//	try{
//	System.out.println(certstring);	
		
//	String finame = certstring.substring(certstring.indexOf("CN=")+3); 
//	if (finame.indexOf(",")>-1)
//		finalname = finame.substring(0, finame.indexOf(",")).split(" ");
//	
//	else finalname = finame.split(" ");
//	
//	
//	}catch (Exception e){
//		e.printStackTrace();
//		log.error(TypeConverter.exceptionToString(e));
//		
//	}
	
	
	
//	} 
		return fio;
		
	}


private String getTitlefromCert (String certstring, byte[] filecontent){
	String title = null;
	try{
		X509Parser p = X509ParserFactory.getParser(filecontent);
		title = p.getValue(WhoType.SUBJECT,FieldType.T);
		} catch (Exception e){
			log.error(TypeConverter.exceptionToString(e));
		}

	return title;
}
}

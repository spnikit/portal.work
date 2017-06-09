package ru.aisa.etdadmin.controllers.MultipartImpl;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.codec.binary.Base64;
import org.apache.log4j.Logger;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.jdbc.core.simple.ParameterizedRowMapper;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import ru.aisa.etdadmin.controllers.AbstractMultipartController;
import sheff.rjd.utils.XMLUtil;



public class Package extends AbstractMultipartController {

	public Package() throws JSONException {
		super();
	}


	private static Logger	log	= Logger.getLogger(Package.class);
	
	@Override
	protected JSONObject add(HashMap<String, String> requestParameters,
			HashMap<String, byte[]> requestFiles,
			HashMap<String, String> requestFileNames) throws JSONException,
			IOException {
		//if(Utils.isReadonly()) return FORBIDDEN;
		JSONObject response = new JSONObject();
		response.put(success, false);
		String fileName = requestFileNames.get("packet");
		byte[] fileContent = requestFiles.get("packet");
		String extension;
		try{
			extension = fileName.substring(fileName.lastIndexOf(".") + 1);
		}catch (Exception e) {
			extension = null;
		}
		if(extension == null || !extension.equalsIgnoreCase("zip")){
			response.put(description, "Неправильное расширение файла, допустимое расширение - 'zip'.");
			return response;
		}
		if(fileContent == null || fileContent.length == 0){
			response.put(description, "Отсутсвует файл пакета.");
			return response;
		}
		byte[] formContent = null;
		ZipInputStream zipIn = new ZipInputStream(new ByteArrayInputStream(fileContent));
		ZipEntry entry;
		Document doc = null;
		try{
			while((entry = zipIn.getNextEntry()) != null)
			{
				if(entry.getName().equalsIgnoreCase("form.xfdl")){
					byte[] buf = new byte[1024];
					ByteArrayOutputStream bos = new ByteArrayOutputStream();
					int len;
					while ((len = zipIn.read(buf)) > 0) {
						bos.write(buf, 0, len);
					}
					formContent = bos.toByteArray();
					bos.close();
					zipIn.closeEntry();					
				}else if(entry.getName().equalsIgnoreCase("package.xml")){
						byte[] buf = new byte[1024];
						ByteArrayOutputStream bos = new ByteArrayOutputStream();
						int len;
						while ((len = zipIn.read(buf)) > 0) {
							bos.write(buf, 0, len);
						}
						doc = XMLUtil.getDOM(new ByteArrayInputStream(bos.toByteArray()));
						zipIn.closeEntry();
				}else{
						response.put(description, "Неправильное содержимое файла.");
						zipIn.closeEntry();
						zipIn.close();
						return response;
				}
			}
			zipIn.close();
		}catch (Exception e) {
			response.put(description, "Неверный формат файла.");
			return response;
		}
		if(doc==null || formContent == null||formContent.length==0){
			response.put(description, "Неправильное содержимое файла.");
			return response;
		}
		Element root = (Element) doc.getElementsByTagName("root").item(0);
		String formName = root.getElementsByTagName("name").item(0).getTextContent();
		Integer crtn = Integer.parseInt(root.getElementsByTagName("sm_crtn").item(0).getTextContent());
		Integer sgnf = Integer.parseInt(root.getElementsByTagName("sm_sgnf").item(0).getTextContent());
		Integer dcln = Integer.parseInt(root.getElementsByTagName("sm_dcln").item(0).getTextContent());
		boolean isRewritePacket = false;
//System.out.println(":: Started");
		if(getSjt().queryForInt("select count(0) from letd.doctype where name = ?", 
				new Object[]{formName})>0){
			//response.put(description, "Такая форма уже существует");
			//return response;
			isRewritePacket = true;
			log.debug(":: Rewrite packet activated...");
//System.out.println(":: Rewrite packet activated...");
		}
		/*
		 * insert file of form to database
		 */
		HashMap<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.put("contentBlob", formContent);
		parameterMap.put("name", formName);
		parameterMap.put("sm_crtn", crtn);
		parameterMap.put("sm_sgnf", sgnf);
		parameterMap.put("sm_dcln", dcln);
		
		int docid = -1;
		
		if(isRewritePacket){
			docid = getSjt().queryForInt("select id from letd.doctype where name = ?", 
					new Object[]{formName});
			log.debug(":: RewritePacket :: Getting template id :: Template '"+formName+"' id = "+docid);
//System.out.println(":: RewritePacket :: Getting template id :: Template '"+formName+"' id = "+docid);
			
			int res = getNpjt().update("UPDATE letd.doctype SET template = :contentBlob, sm_crtn = :sm_crtn, sm_sgnf = :sm_sgnf, sm_dcln = :sm_dcln WHERE id = "+docid+" AND name = '"+formName+"'", parameterMap);
			log.debug(":: Trying to update template '"+formName+"' id="+docid+"... :: Result = "+res);
//System.out.println(":: Trying to update template '"+formName+"' id="+docid+"... :: Result = "+res);
			
			res = getNpjt().update("DELETE FROM letd.doctypeacc WHERE dtid = "+docid, new HashMap());
			log.debug(":: Trying to delete old rights :: Docid = "+docid+" :: Result  = "+res);
//System.out.println(":: Trying to delete old rights :: Docid = "+docid+" :: Result  = "+res);
			
			res = getNpjt().update("DELETE FROM letd.doctypeflow WHERE dtid = "+docid, new HashMap());
			log.debug(":: Trying to delete old signatures :: Docid = "+docid+" :: Result = "+res);
//System.out.println(":: Trying to delete old signatures :: Docid = "+docid+" :: Result = "+res);
		}else{
//System.out.println(":: Else");
			getNpjt()
					.update(
							"INSERT INTO letd.DOCTYPE(TEMPLATE, ID , NAME, SM_SGNF, SM_CRTN, SM_DCLN) VALUES(:contentBlob, COALESCE((select max(id)+1 from letd.doctype),0), :name,:sm_crtn,:sm_sgnf,:sm_dcln)",
						parameterMap);
			docid = getSjt().queryForInt("select id from letd.doctype where name = ?", 
					new Object[]{formName});
		}
		Element groups = (Element) root.getElementsByTagName("groups").item(0);
		HashMap<Integer,Integer> groupIds = getGroups(groups);
		
		Element roles = (Element) root.getElementsByTagName("roles").item(0);
		HashMap<Integer,Integer> roleIds = getRoles(roles,groupIds);
		Element rights = (Element) root.getElementsByTagName("rights").item(0);
		List<Object[]> rightsList = new ArrayList<Object[]>();
		for(int i=0;i<rights.getElementsByTagName("right").getLength();i++){
			Element right = (Element)rights.getElementsByTagName("right").item(i);
			int cdel, cnewdata;
			try{
				cdel = Integer.parseInt(right.getTextContent().substring(3, 4));
			}catch (StringIndexOutOfBoundsException e) {
				cdel = 0;
				log.warn(formName+" has old package format");
			}
			try{
				cnewdata = Integer.parseInt(right.getTextContent().substring(4, 5));
			}catch (StringIndexOutOfBoundsException e) {
				cnewdata = 0;
				log.warn(formName+" has old package format (Create from data don't contain)");
			}
			rightsList.add(new Object[]{
					docid,
					roleIds.get(Integer.parseInt(right.getAttribute("r"))),
					Integer.parseInt(right.getTextContent().substring(0, 1)),
					Integer.parseInt(right.getTextContent().substring(1, 2)),
					Integer.parseInt(right.getTextContent().substring(2, 3)),
					cdel,
					cnewdata
					});
		}
		getSjt().batchUpdate("insert into letd.doctypeacc (dtid,wrkid,cview,cedit,cnew,cdel, cnewdata) values (?,?,?,?,?,?,?)", rightsList);
		Element signatures = (Element) root.getElementsByTagName("signatures").item(0);
		List<Object[]> signaturesList = new ArrayList<Object[]>();
		for(int i=0;i<signatures.getElementsByTagName("signature").getLength();i++){
			Element signature = (Element) signatures.getElementsByTagName("signature").item(i);
			String par = signature.getElementsByTagName("par").item(0).getTextContent();
			String exp = signature.getElementsByTagName("exp").item(0).getTextContent();
			String stage;
			String xmltag;
			String priz;
			try{
				stage = signature.getElementsByTagName("stage").item(0).getTextContent();
			}catch (NullPointerException e) {
				stage = "";
				log.warn(formName+" has old package format (stage not exists)");
			}
			try{
				xmltag = signature.getElementsByTagName("xmltag").item(0).getTextContent();
			}catch (NullPointerException e) {
				xmltag = "";
				log.warn(formName+" has old package format (xmltag not exists)");
			}
			try{
				priz = signature.getElementsByTagName("priz").item(0).getTextContent();
			}catch (NullPointerException e) {
				priz = "";
				log.warn(formName+" has old package format (priz not exists)");
			}
			signaturesList.add(new Object[]{
					docid,
					Integer.parseInt(signature.getElementsByTagName("order").item(0).getTextContent()),
					roleIds.get(Integer.parseInt(signature.getElementsByTagName("role").item(0).getTextContent())),
					par.length()>=1?Integer.parseInt(par):null,
					exp.length()>=1?exp:null,
					stage.length()>=1?Integer.parseInt(stage):0,
					xmltag.length()>=1?xmltag:null,
					priz.length()>=1?Integer.parseInt(priz):null});
		}
		getSjt().batchUpdate("insert into letd.doctypeflow (dtid,order,wrkid,parent,exp,stage,xmltag,priz) values (?,?,?,?,?,?,?,?)", signaturesList);
		//response.put(description, "Function not implemented yet");
		response.put(success, true);
		return response;
	}
	
	/**
	 * insert new groups to database and than select ids of all groups in package
	 */	
	private HashMap<Integer,Integer> getGroups(Element groups){
		//Element that contains id in database of each group in package
		//index of hashmap - id in package, value - id in database
		final HashMap<Integer,Integer> groupIds = new HashMap<Integer, Integer>();
		//Element that contains all group names in package
		List<String> groupNames = new ArrayList<String>();
		//Element that contains id in package of each group
		final HashMap<String,Integer> grNames = new HashMap<String,Integer>();
		for(int i = 0 ; i<groups.getElementsByTagName("group").getLength();i++){
			groupNames.add(groups.getElementsByTagName("group").item(i).getTextContent());
			grNames.put(
					groups.getElementsByTagName("group").item(i).getTextContent(),
					Integer.parseInt(((Element)groups.getElementsByTagName("group").item(i)).getAttribute("id")));
		}
		HashMap<String,List> pp = new HashMap<String, List>();
		pp.put("nameList", groupNames);
		getNpjt().query("select id,rtrim(name) as name from letd.department where name in ( :nameList) ", pp,
				new ParameterizedRowMapper<Object>() {

			public Object mapRow(ResultSet rs, int numrow)
					throws SQLException {
				groupIds.put(grNames.get(rs.getString("name"))
						, rs.getInt("id"));
				return null;
				}
			}
		);
		List<Object[]> gr = new ArrayList<Object[]>();
		boolean isNotChanges = true;
		for(int i = 0 ; i<groups.getElementsByTagName("group").getLength();i++){
				if(groupIds.get(Integer.parseInt(
						((Element) groups.getElementsByTagName("group").item(i)).getAttribute("id")
						))==null){
					isNotChanges = false;
					gr.add(new Object[]{groups.getElementsByTagName("group").item(i).getTextContent()});
				}
		}
		if(isNotChanges) return groupIds;
		getSjt().batchUpdate("insert into letd.department (id,name) values ((select max(id)+1 from letd.department),?)", gr);
		groupIds.clear();
		getNpjt().query("select id,rtrim(name) as name from letd.department where name in ( :nameList) ", pp,
				new ParameterizedRowMapper<Object>() {

			public Object mapRow(ResultSet rs, int numrow)
					throws SQLException {
				groupIds.put(grNames.get(rs.getString("name"))
						, rs.getInt("id"));
				return null;
				}
			}
		);

		return groupIds;
		
	}

	/**
	 * insert new roles to database and than select ids of all roles in package
	 */	
	private HashMap<Integer,Integer> getRoles(Element roles,HashMap<Integer,Integer> groupIds){
		//Element that contains id in database of each role in package
		//index of hashmap - id in package, value - id in database
		final HashMap<Integer,Integer> roleIds = new HashMap<Integer, Integer>();
		//Element that contains all group names in package
		List<String> roleNames = new ArrayList<String>();
		//Element that contains id in package of each group
		final HashMap<String,Integer> grNames = new HashMap<String,Integer>();
		for(int i = 0 ; i<roles.getElementsByTagName("role").getLength();i++){
			roleNames.add(roles.getElementsByTagName("role").item(i).getTextContent());
			grNames.put(
					roles.getElementsByTagName("role").item(i).getTextContent(),
					Integer.parseInt(((Element)roles.getElementsByTagName("role").item(i)).getAttribute("id")));
		}
		HashMap<String,List> pp = new HashMap<String, List>();
		pp.put("nameList", roleNames);
		getNpjt().query("select id,rtrim(name) as name from letd.wrkname where name in ( :nameList) ", pp,
				new ParameterizedRowMapper<Object>() {

			public Object mapRow(ResultSet rs, int numrow)
					throws SQLException {
				roleIds.put(grNames.get(rs.getString("name"))
						, rs.getInt("id"));
				return null;
				}
			}
		);
		List<Object[]> gr = new ArrayList<Object[]>();
		boolean isNotChanges = true;
		for(int i = 0 ; i<roles.getElementsByTagName("role").getLength();i++){
				if(roleIds.get(Integer.parseInt(
						((Element) roles.getElementsByTagName("role").item(i)).getAttribute("id")
						))==null){
					isNotChanges = false;
					gr.add(new Object[]{
							groupIds.get(Integer.parseInt(((Element)roles.getElementsByTagName("role").item(i)).getAttribute("g"))),
							roles.getElementsByTagName("role").item(i).getTextContent()});
				}
		}
		if(isNotChanges) return roleIds;
		getSjt().batchUpdate("insert into letd.wrkname (id,depid,name) values ((select max(id)+1 from letd.wrkname),?,?)", gr);
		roleIds.clear();
		getNpjt().query("select id,rtrim(name) as name from letd.wrkname where name in ( :nameList) ", pp,
				new ParameterizedRowMapper<Object>() {

			public Object mapRow(ResultSet rs, int numrow)
					throws SQLException {
				roleIds.put(grNames.get(rs.getString("name"))
						, rs.getInt("id"));
				return null;
				}
			}
		);

		return roleIds;
		
	}

	@Override
	protected void export(HttpServletRequest request, HttpServletResponse response) throws IOException {
		//response.getOutputStream().println("not implemented");
		HashMap<String, String> hm = new HashMap<String, String>();
		PrintWriter out = response.getWriter();
		if(request.getParameter("formname")==null||request.getParameter("formname").length()<1){
			out.println("formname is not specified");
			return;
		}
		String formname =new String(Base64.decodeBase64(request.getParameter("formname").getBytes()),"UTF-8"); 
		hm.put("NAME", formname);
		int formid;
		try{
			formid = getNpjt().queryForInt("select id from letd.doctype where name = :NAME ", hm);
		}catch (Exception e) {
			out.println("form not found");
			return;
		}
		final Document doc = XMLUtil.getDOM("<root/>");
		Element name = doc.createElement("name");
		name.setTextContent(formname);
		doc.getDocumentElement().appendChild(name);
		final 
		
		HashMap<String,Integer> hmm = new HashMap<String, Integer>();
		hmm.put("id", formid);
		try {
			getNpjt().query("select sm_crtn, sm_sgnf, sm_dcln from letd.doctype where id = :id with ur", hmm,
					new ParameterizedRowMapper<Object>() {
				public Object mapRow(ResultSet rs, int numrow)
				throws SQLException{
				
					int cr = rs.getInt("sm_crtn");
					int sg = rs.getInt("sm_sgnf");
					int dc = rs.getInt("sm_dcln");
//					System.out.println("KARAMBA!!!   "+cr+" "+sg+" "+dc);
					Element sm_crtn = doc.createElement("sm_crtn");
					Element sm_sgnf = doc.createElement("sm_sgnf");
					Element sm_dcln = doc.createElement("sm_dcln");
					sm_crtn.setTextContent(Integer.toString(cr));
					sm_sgnf.setTextContent(Integer.toString(sg));
					sm_dcln.setTextContent(Integer.toString(dc));
					doc.getDocumentElement().appendChild(sm_crtn);
					doc.getDocumentElement().appendChild(sm_sgnf);
					doc.getDocumentElement().appendChild(sm_dcln);
					return null;
				}
					});
			
		} catch (Exception e){
			StringWriter outError = new StringWriter();
			   PrintWriter errorWriter = new PrintWriter( outError );
			   e.printStackTrace(errorWriter);
			   log.error(outError.toString());
			out.println(e.getLocalizedMessage());
			return;
		}
		
		final Element signatures = doc.createElement("signatures");
		final Element roles = doc.createElement("roles");
		final Element groups = doc.createElement("groups");
		final Element rights = doc.createElement("rights");
		doc.getDocumentElement().appendChild(signatures);
		doc.getDocumentElement().appendChild(roles);
		doc.getDocumentElement().appendChild(groups);
		doc.getDocumentElement().appendChild(rights);
		final ArrayList<Integer> rroles = new ArrayList<Integer>();
		final ArrayList<Integer> ggroups = new ArrayList<Integer>();
		try{
		getNpjt().query("select order,wrkid,rtrim(char(parent)) as parent,rtrim(exp) as exp, stage, rtrim(xmltag) xmltag, priz from letd.doctypeflow where dtid = :id ", hmm,
				new ParameterizedRowMapper<Object>() {

			public Object mapRow(ResultSet rs, int numrow)
					throws SQLException {
				Element signature = doc.createElement("signature");
				signatures.appendChild(signature);
				Element order = doc.createElement("order");
				Element role = doc.createElement("role");
				Element par = doc.createElement("par");
				Element exp = doc.createElement("exp");
				Element stage = doc.createElement("stage");
				Element xmltag = doc.createElement("xmltag");
				Element priz = doc.createElement("priz");
				signature.appendChild(order);
				signature.appendChild(role);
				signature.appendChild(par);
				signature.appendChild(exp);
				signature.appendChild(stage);
				signature.appendChild(xmltag);
				signature.appendChild(priz);
				order.setTextContent(Integer.toString(rs.getInt("order")));
				int wrkid = rs.getInt("wrkid");
				role.setTextContent(Integer.toString(wrkid));
				par.setTextContent(rs.getString("parent"));
				exp.setTextContent(rs.getString("exp"));
				stage.setTextContent(rs.getString("stage"));
				xmltag.setTextContent(rs.getString("xmltag"));
				priz.setTextContent(rs.getString("priz"));
				if (!rroles.contains(wrkid)){
					rroles.add(wrkid);
				}
				return null;
				}
			}
		);
		getNpjt().query("select wrkid,cview,cedit,cnew,cdel,cnewdata from letd.doctypeacc where dtid = :id ", hmm,
				new ParameterizedRowMapper<Object>() {

			public Object mapRow(ResultSet rs, int numrow)
					throws SQLException {
				Element right = doc.createElement("right");
				rights.appendChild(right);
				
				int wrkid = rs.getInt("wrkid");
				right.setTextContent(""+rs.getInt("cview")+
						rs.getInt("cedit")+rs.getInt("cnew")+rs.getInt("cdel")+rs.getInt("cnewdata"));
				right.setAttribute("r", Integer.toString(wrkid));
				if (!rroles.contains(wrkid)){
					rroles.add(wrkid);
				}
				return null;
				}
			}
		);
		HashMap<String, ArrayList<Integer>>hm1 = new HashMap<String, ArrayList<Integer>>();
		hm1.put("list", rroles);
		getNpjt().query("select id,depid,rtrim(name) as name from letd.wrkname where id in (:list) ", hm1,
				new ParameterizedRowMapper<Object>() {

			public Object mapRow(ResultSet rs, int numrow)
					throws SQLException {
				Element role = doc.createElement("role");
				roles.appendChild(role);
				role.setTextContent(rs.getString("name"));
				role.setAttribute("id", Integer.toString(rs.getInt("id")));
				int group = rs.getInt("depid");
				role.setAttribute("g", Integer.toString(group));
				if (!ggroups.contains(group)){
					ggroups.add(group);
				}
				return null;
				}
			}
		);
		hm1.clear();
		hm1.put("list", ggroups);
		getNpjt().query("select id,rtrim(name) as name from letd.department where id in (:list) ", hm1,
				new ParameterizedRowMapper<Object>() {

			public Object mapRow(ResultSet rs, int numrow)
					throws SQLException {
				Element group = doc.createElement("group");
				groups.appendChild(group);
				group.setTextContent(rs.getString("name"));
				group.setAttribute("id", Integer.toString(rs.getInt("id")));
				return null;
				}
			}
		);
		}catch (Exception e) {
			 StringWriter outError = new StringWriter();
			   PrintWriter errorWriter = new PrintWriter( outError );
			   e.printStackTrace(errorWriter);
			   log.error(outError.toString());
			out.println(e.getLocalizedMessage());
			return;
		}
		try{
			ByteArrayOutputStream bs = new ByteArrayOutputStream();
			ZipOutputStream zs= new ZipOutputStream(bs);
			zs.putNextEntry(new ZipEntry("package.xml"));
			zs.write(XMLUtil.toXML(doc, null, new StringWriter()).getBytes("UTF-8"));
			zs.closeEntry();
			byte[] form = (byte[]) getNpjt().queryForObject("select template from letd.doctype where id = :id", hmm, byte[].class);
			zs.putNextEntry(new ZipEntry("form.xfdl"));
			zs.write(form);
			zs.closeEntry();
			zs.close();
			bs.close();
			response.setHeader("Content-disposition", "attachment; filename=package.zip");
			response.setContentType("application/zip");
			out.print(bs.toString(codepage));
			return;
		}catch (Exception e) {
			out.println(e.getLocalizedMessage());
			 StringWriter outError = new StringWriter();
			   PrintWriter errorWriter = new PrintWriter( outError );
			   e.printStackTrace(errorWriter);
			   log.error(outError.toString());
			   
			return;
		}
		
		
	}


}

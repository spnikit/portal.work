package ru.aisa.etdportal.controllers;

import java.io.ByteArrayInputStream;
import java.security.cert.X509Certificate;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathFactory;

import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipArchiveOutputStream;
import org.apache.log4j.Logger;
import org.json.JSONException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.ParameterizedRowMapper;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import ru.aisa.crypto.FieldType;
import ru.aisa.crypto.WhoType;
import ru.aisa.crypto.X509Parser;
import ru.aisa.crypto.X509ParserFactory;
import ru.aisa.etdadmin.controllers.AbstractMultipartController;
import ru.aisa.pdf.PDFMaker;
import ru.aisa.rgd.utils.Base64;
import ru.aisa.rgd.ws.documents.ETDForm;
import ru.aisa.rgd.ws.utility.TypeConverter;
import sheff.rjd.ws.OCO.TORLists;

import com.aisa.cms.CMSSignedData;
import com.aisa.htmlgenerator.AutoCompleteHTML;


public class ExportPDF extends AbstractMultipartController {

	
	private NamedParameterJdbcTemplate npjt;
	protected final Logger	log	= Logger.getLogger(getClass());
	
	private final String mainSelect = "select ds.id, ds.docdata, ds.vagnum, ds.id_pak, "
			+ "(select dt.html_template from snt.doctype dt where id = ds.typeid), "
			+ "(select rtrim(dt.name) name from snt.doctype dt where id = ds.typeid) "
			+ "from snt.docstore ds where etdid in "
			+ "(select etdid from snt.packages where id_pak in (:list))";

	private final String selectForDoc = "select ds.id, ds.docdata, ds.vagnum, ds.id_pak, "
			+ "(select dt.html_template from snt.doctype dt where id = ds.typeid), "
			+ "(select rtrim(dt.name) name from snt.doctype dt where id = ds.typeid) "
			+ "from snt.docstore ds where id in (:list)";
	
	private final String getBldoc = "select bldoc from snt.docstore where id = :id";
	private final String got = "export";
	public NamedParameterJdbcTemplate getNpjt() {
		return npjt;
	}

	public void setNpjt(NamedParameterJdbcTemplate npjt) {
		this.npjt = npjt;
	}

	public ExportPDF() throws JSONException {
		super();
	}
	protected void export(HttpServletRequest request, HttpServletResponse response) {
		try {
		request.setCharacterEncoding("UTF-8");
		
		response.setCharacterEncoding("UTF-8");
		response.setHeader("Content-disposition",
				"attachment; filename=PDFData.zip");
		response.setContentType("application/zip");
		
		String ids = request.getParameter("ids");
		String choice = request.getParameter("choice");
		String [] arrayStr = ids.split(";");
		String [] arrayChoice = choice.split(";");
		List<HtmlObject> objList = null;
		List<HtmlObject> objListForAll = null;
		
		Map<String, List<String>> param = new HashMap<String,List<String>>();
		List<String> list = new ArrayList<String>();
		List<String> listChoice = new ArrayList<String>();
		List<String> listDocData = new ArrayList<String>();
		
		for(int i = 0; i < arrayStr.length; i++) {
		listChoice.add(arrayChoice[i]);
		if(listChoice.get(i).equals("doc")){
		listDocData.add(arrayStr[i]);
		}else{
		list.add(arrayStr[i]);
		}
		}
		
		if(listChoice.contains("doc")){
		param.put("list", listDocData);
		objList = getNpjt().query(selectForDoc, param, new Mapper());
		}else if(listChoice.contains("pack")){
		param.put("list", list);
		objList = getNpjt().query(mainSelect, param, new Mapper());
		}
		
		if(listChoice.contains("doc") && listChoice.contains("pack")){
		if(listChoice.contains("pack")){
		param.put("list", list);
		objListForAll = getNpjt().query(mainSelect, param, new Mapper());
				}
		for(int q = 0;q < objListForAll.size(); q++){
			objList.add(objListForAll.get(q));
			}
		}
		ServletOutputStream respout = response.getOutputStream();
		
		final ZipArchiveOutputStream zipstream = new ZipArchiveOutputStream(respout);
		ZipArchiveEntry entry = null;
		String folderName;
		GetHtmlContent getHTML = new GetHtmlContent();
		getHTML.setNpjt(getNpjt());
		for(HtmlObject obj : objList) {
			String html = getHTML.getHTMLforPrintAndPDF(obj.getId(), obj.getDocData(), obj.getHtmlTemplate(), obj.getTypeName(), getBldoc, got);
			byte[] pdf = PDFMaker.generatePDFfromHTML(html);
			String idPak = "";
			String vagNum = "";
			if (obj.getId_pak() != null) {
				idPak = obj.getId_pak().trim();
			}
			if (obj.getVagnum() != null) {
				vagNum = obj.getVagnum().trim();
			}
		
			folderName = getFolderName(idPak, vagNum);
			entry = new ZipArchiveEntry (folderName + "/" + obj.getTypeName() + ".pdf");
			entry.setSize(pdf.length);
			zipstream.putArchiveEntry(entry);
			zipstream.write(pdf);
			zipstream.closeArchiveEntry();
		}

		zipstream.flush();		
		
		zipstream.close();

		respout.close();
		
		}catch (Exception e) {
			log.error(TypeConverter.exceptionToString(e));
		}
	}
	
	
	private String getFolderName(String id_pak, String vagnum) {
		StringBuilder strb = new StringBuilder();
		strb.append("vagon").append("_").append(vagnum).append("_");
		strb.append("ID").append("_").append(id_pak);
		return strb.toString();
	}
	
	class HtmlObject {
		private Long id;
		private String docData;
		private byte[] htmlTemplate;
		private String id_pak;
		private String vagnum;
		private String typeName;
		
		public Long getId() {
			return id;
		}
		public void setId(Long id) {
			this.id = id;
		}
		public String getDocData() {
			return docData;
		}
		public void setDocData(String docData) {
			this.docData = docData;
		}
		public byte[] getHtmlTemplate() {
			return htmlTemplate;
		}
		public void setHtmlTemplate(byte[] htmlTemplate) {
			this.htmlTemplate = htmlTemplate;
		}
		public String getId_pak() {
			return id_pak;
		}
		public void setId_pak(String id_pak) {
			this.id_pak = id_pak;
		}
		public String getVagnum() {
			return vagnum;
		}
		public void setVagnum(String vagnum) {
			this.vagnum = vagnum;
		}
		public String getTypeName() {
			return typeName;
		}
		public void setTypeName(String typeName) {
			this.typeName = typeName;
		}
		public HtmlObject(Long id, String docData, byte[] htmlTemplate, String id_pak, String vagnum, String typeName) {
			this.id = id;
			this.docData = docData;
			this.htmlTemplate = htmlTemplate;
			this.id_pak = id_pak;
			this.vagnum = vagnum;
			this.typeName = typeName;
		}
	}
	
	class Mapper implements ParameterizedRowMapper<HtmlObject> {
		public HtmlObject mapRow(ResultSet rs, int rowNum) throws SQLException {
			String docData = rs.getString("DOCDATA");
			byte[] HTML_Template = rs.getBytes("HTML_TEMPLATE");
			Long id = rs.getLong("ID");
			String id_pak = rs.getString("ID_PAK");
			String vagnum = rs.getString("VAGNUM");			
		
			String typeName = rs.getString("NAME").trim();
			String name = (String) TORLists.exportList.get(typeName);
			try{
				return new HtmlObject(id, docData, HTML_Template, id_pak, vagnum, name);
			} catch (Exception e) {
				log.error(TypeConverter.exceptionToString(e));
			}
			return null;
		}
	}


}

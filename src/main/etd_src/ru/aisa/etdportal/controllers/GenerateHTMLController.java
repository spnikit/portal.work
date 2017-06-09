package ru.aisa.etdportal.controllers;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathFactory;

import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import ru.aisa.crypto.FieldType;
import ru.aisa.crypto.WhoType;
import ru.aisa.crypto.X509Parser;
import ru.aisa.crypto.X509ParserFactory;

import java.io.ByteArrayInputStream;
import java.io.FileWriter;
import java.security.cert.X509Certificate;

import ru.aisa.etdadmin.controllers.AbstractMultipartController;
import ru.aisa.rgd.utils.Base64;
import ru.aisa.rgd.ws.documents.ETDForm;
import ru.aisa.rgd.ws.utility.DataBinder;
import ru.aisa.rgd.ws.utility.TypeConverter;
import sheff.rjd.ws.OCO.TORLists;

import com.aisa.htmlgenerator.AutoCompleteHTML;
import com.aisa.cms.CMSSignedData;

public class GenerateHTMLController extends AbstractMultipartController {
	
	private final String getNameFormByIdSql = "select rtrim(name) from snt.doctype where id = (select typeid from snt.docstore where id = :id)";
	private final String getBldoc = "select bldoc from snt.docstore where id = :id";
	
	private static Logger log = Logger.getLogger(GenerateHTMLController.class);
	
	public GenerateHTMLController() throws JSONException {
		super();
	}
	@Override
	protected JSONObject get(HttpServletRequest request) throws JSONException {
		JSONObject resultJSONj = new JSONObject();
		resultJSONj.put("data", new JSONArray());
		try {
			String[] idDocArray = request.getParameter("idDocuments").split(";");
			Map<String, Object> map = new HashMap<String, Object>();
			byte[] result = null;
			for(int i = 0; i < idDocArray.length; i++) { //Формирование массива HTML страниц
				Long id = Long.parseLong(idDocArray[i]); //извлекаем id из массива который к нас пришле с фронта
				map.put("id", id); 
				byte[] html_template = (byte[])getNpjt().queryForObject("select HTML_Template from snt.doctype"
						+ " where id = (select typeid from snt.docstore where id = :id)", map, byte[].class);
				String selectData = "select docdata from SNT.DOCSTORE where id = :id";
				String docData = getNpjt().queryForObject(selectData, map, String.class);
				String formName = getNpjt().queryForObject(getNameFormByIdSql, map, String.class);
				String got = "";
				GetHtmlContent getHTML = new GetHtmlContent();
				getHTML.setNpjt(getNpjt());
				String html = getHTML.getHTMLforPrintAndPDF(id,docData,html_template,(String)TORLists.exportList.get(formName),getBldoc, got);
					JSONObject objTemp = new JSONObject();
					objTemp.put("html", html);
					resultJSONj.accumulate("data", objTemp);
				}
			
	
		} catch(Exception e) {
			log.error(TypeConverter.exceptionToString(e));
		}

		return resultJSONj;
	}
}
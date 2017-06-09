package ru.aisa.etdportal.controllers;

import java.io.ByteArrayInputStream;
import java.security.cert.X509Certificate;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathFactory;

import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.aisa.cms.CMSSignedData;
import com.aisa.htmlgenerator.AutoCompleteHTML;

import ru.aisa.crypto.FieldType;
import ru.aisa.crypto.WhoType;
import ru.aisa.crypto.X509Parser;
import ru.aisa.crypto.X509ParserFactory;
import ru.aisa.rgd.utils.Base64;
import ru.aisa.rgd.ws.documents.ETDForm;
import ru.aisa.rgd.ws.utility.TypeConverter;

public class GetHtmlContent {
	protected final Logger log = Logger.getLogger(getClass());

	private NamedParameterJdbcTemplate npjt;
	
	protected String getHTMLforPrintAndPDF (Long docId, String docData, byte[] htmlTemplate, String typeName, String getBldoc, String got) {
		
		AutoCompleteHTML ach = new AutoCompleteHTML();
		String pre_html = null;
		try{
		if(htmlTemplate != null) {
			byte[] content = ach.generateHTML(docData.getBytes(), htmlTemplate, docId);
			Map<String, Long> map = new HashMap<String, Long>();
			map.put("id", docId);
		
			byte[] fpu26form = getNpjt().queryForObject(getBldoc, map, byte[].class);
			ETDForm form = ETDForm.createFromArchive(fpu26form); //Разархивируем лотус форму
			List<Object> elementInNoteList = new ArrayList<Object>();
			final DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
			final DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
			final org.w3c.dom.Document xmlDoc = documentBuilder.parse(new ByteArrayInputStream(form.toString().getBytes())); // Парсим извлеченню форму XML c помощью DocumentBuilder
			final XPathFactory xPathFactory = XPathFactory.newInstance();
			final XPathExpression nodeXPath = xPathFactory.newXPath().compile("//signature[./mimedata]");
			final NodeList nodeList = (NodeList) nodeXPath.evaluate(xmlDoc, XPathConstants.NODESET);

			for(int l = 0; l < nodeList.getLength(); l++){
				Node node = nodeList.item(l);
				Element element = (Element) node;
	
			
				elementInNoteList.add(element.getElementsByTagName("mimedata").item(0).getTextContent());
			} //Получаем массив base64 с данными подписантов.
			
			if("FPU-26".equals(typeName) || "MX-1".equals(typeName) || "MX-3".equals(typeName) || "Act priema-peredachi".equals(typeName) || 
					"RDV".equals(typeName) || "TORG-12".equals(typeName)){
				Map<String, Object> docdataMap = ach.getMapParseXml(docData.getBytes(), docId);
	
				SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy");
				Document document = Jsoup.parse(new String(content), "UTF-8");
				content = document.toString().getBytes();
				
				if(!elementInNoteList.isEmpty()){
					
					document.select("body").last().append("<div class=\"foot\">"
							+ "<table class=\"table_foot\">"
							+ "<tr>"
							+ "<td>Организация</td>"
							+ "<td>Подписант</td>"
							+ "<td>Сертификат</td>"
							+ "<td>Дата подписи</td>"
							+ "</tr>");

					for(int u = 0; u < elementInNoteList.size(); u++){
						CMSSignedData cmsNew = new CMSSignedData(Base64.decode(elementInNoteList.get(u).toString())); //Приводим base64 код к нормальному состоянию для извлечения оттуда подписей, даты и т.д
						X509Certificate[] certsNew = cmsNew.getCertificates(); // извлекаем сертификаты
						String signDocumentDate = "";
						if("FPU-26".equals(typeName)) {
							signDocumentDate = docdataMap.get("P_4" + (u+1) + "_serv").toString();
						} else if(("MX-1".equals(typeName) && u < 2) || "MX-3".equals(typeName)) {//&& u < 2 потому что в MX-1 не проставляется date3
							if("MX-3".equals(typeName) && u == 1) {
								signDocumentDate = docdataMap.get("date" + (u+3)).toString();
							} else if("MX-3".equals(typeName) && (u == 2 || u == 3 )) {
								signDocumentDate = docdataMap.get("date" + (u)).toString();
							} else {
								signDocumentDate = docdataMap.get("date" + (u+1)).toString();
							}	
						} else if("RDV".equals(typeName)) {
							if(u == 1){
								signDocumentDate = docdataMap.get("P_" + (u) + "_serv").toString();
							}else if (u == 2) {
								signDocumentDate = docdataMap.get("P_" + (u) + "_serv").toString();
					
							} else if(u == 0){
								signDocumentDate = docdataMap.get("P_" + (u+3) + "_serv").toString();
						}
						} else if ("TORG-12".equals(typeName)){
							String dateServ = "P_4" + (u+1) + "_serv";
							Boolean docdataMapBool = docdataMap.containsValue(dateServ);
							if(docdataMapBool != false){
								signDocumentDate = docdataMap.get(dateServ).toString();
							}
						}
						
					/*	//Уже 2022 год, надеюсь лотус научится передавать дату Акта приема передачи ТМЦ
						else if ("Act priema-peredachi".equals(typeName)){
						if(u == 0){
								signDocumentDate = docdataMap.get("date1" + (u) + "_serv").toString();
							}else if (u == 1) {
								signDocumentDate = docdataMap.get("date2" + (u) + "_serv").toString();
					
							}
						}*/
						
						for(int k = 0 ; k < certsNew.length; k++) {

							X509Parser parserNew = X509ParserFactory.getParser(certsNew[k]);
							if(parserNew.getValue(WhoType.SUBJECT, FieldType.SURNAME) != null) {
								
								String certserial = parserNew.getSerialNumber().toString(16);
								String htmlTable = null;

								if(k == elementInNoteList.size()-1) {
									
									
									htmlTable = "<tr><td>"+parserNew.getValue(WhoType.SUBJECT, FieldType.O)+"</td>"
											+ "<td>"+parserNew.getValue(WhoType.SUBJECT, FieldType.SURNAME) + " "
											+ parserNew.getValue(WhoType.SUBJECT, FieldType.GIVENNAME) + "<br />"
											+ parserNew.getValue(WhoType.SUBJECT, FieldType.T)+"</td>"
											+ "<td>"+certserial.toUpperCase() + "<br/>"
											+ "Срок действия сертификата " + "<br/>" 
											+ "c " + format.format(parserNew.getCertificate().getNotBefore()) + " по "
											+ format.format(parserNew.getCertificate().getNotAfter()) +"</td>"
											+ "<td>"+ signDocumentDate + "<br/>"
											+ "Документ подписан усиленной КЭП" +"</td>" 
											+ "</tr></table></div>";
									
									
								}else {
									
									htmlTable = "<tr><td>"+parserNew.getValue(WhoType.SUBJECT, FieldType.O)+"</td>"
											+ "<td>"+parserNew.getValue(WhoType.SUBJECT, FieldType.SURNAME) + " "
											+ parserNew.getValue(WhoType.SUBJECT, FieldType.GIVENNAME) + "<br />"
											+ parserNew.getValue(WhoType.SUBJECT, FieldType.T)+"</td>"
											+ "<td>"+certserial.toUpperCase() + "<br/>"
											+"Срок действия сертификата " + "<br/>"
											+ "c " + format.format(parserNew.getCertificate().getNotBefore()) + " по "
											+ format.format(parserNew.getCertificate().getNotAfter()) +"</td>"
											+ "<td>"+ signDocumentDate + "<br/>"
											+ "Документ подписан усиленной КЭП" +"</td>"; 
											
								}
								
								document.select("tbody").last().append(htmlTable);
							}
						}
					}
					content = document.toString().getBytes();
					
				
				}

			}
			pre_html = new String(content);
			if (got.equals("export")){
			pre_html = pre_html.replace("<!DOCTYPE html PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\">", "");
			pre_html = pre_html.replace("<input","<p").replace("/input>", "/p>");
			}
		}
		}catch (Exception e) {
			log.error(TypeConverter.exceptionToString(e));
		}
		return pre_html;
	}


	public NamedParameterJdbcTemplate getNpjt() {
		return npjt;
	}


	public void setNpjt(NamedParameterJdbcTemplate npjt) {
		this.npjt = npjt;
	}
	
}

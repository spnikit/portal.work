package ru.aisa.etdportal.controllers;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


import org.json.JSONException;

import org.springframework.web.servlet.ModelAndView;

import ru.aisa.etdadmin.controllers.AbstractMultipartController;
import ru.aisa.rgd.HTMLGenerator.AutoCompleteHTML;
import ru.aisa.rgd.utils.Base64;

public class TestHTMLGeneratorController extends AbstractMultipartController {

	public TestHTMLGeneratorController() throws JSONException {
		super();
	}
	
	protected ModelAndView handleRequestInternal(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		try {
			int idMX3 = 31560;
			int idMX1 = 31557;
			int idBrak = 14257;
			int idPak = 16113;
			int idvu41 =31755;
			int idrsx =31767;
			int idrsp = 31762;
			int idspravka = 23320;
			int idcarddoc = 31672;
			int idactTMC = 34641;
			int idrdv = 24224;
			int idfpu26 = 32632;
			int idvu36 = 34182;
			int idvu23 = 31758;
			int idvu23_auto1 = 32547;
			int idtorg12ASU0 = 33416;
			int idtorg12 = 34330;
			int idvu22 = 22425;
			int idksf = 35384;
			int idksfWithOneRoW = 25873;
			
			List<Integer> listId = new ArrayList<Integer>();
			List<byte[]> resultHTML = new ArrayList<byte[]>();
			listId.add(idMX3);
			listId.add(idMX1);
			listId.add(idBrak);
			listId.add(idPak);
			listId.add(idvu41);
			listId.add(idrsx);
			listId.add(idrsp);
			listId.add(idspravka);
			listId.add(idcarddoc);
			listId.add(idactTMC);
			listId.add(idrdv);
			listId.add(idfpu26);
			listId.add(idvu36);
			listId.add(idvu23);
			listId.add(idvu23_auto1);
			listId.add(idtorg12ASU0);
			listId.add(idtorg12);
			listId.add(idvu22);
			listId.add(idksf);
			listId.add(idksfWithOneRoW);
			
			
			String selectBlobHTML = "select columnname from snt.doctype where "
					+ "id = (select typeid from snt.docstore where id = :id)";
			Map<String, Object> map = new HashMap<String, Object>();
			
			for(Integer id: listId) {
				map.put("id", id);
				byte[] blobHTML = (byte[])getNpjt().queryForObject(selectBlobHTML, map, byte[].class);
				byte[] newBlobHTML = decodeFromArchiv(blobHTML);
				String selectData = "select docdata from SNT.DOCSTORE where id = :id";
			    String xml = getNpjt().queryForObject(selectData, map, String.class);
			    byte[] docdata = xml.getBytes();
			    AutoCompleteHTML ach = new AutoCompleteHTML();
			    byte[] result = ach.generateHTML(docdata, newBlobHTML);
				resultHTML.add(result);
			}
		} catch(Exception e) {
			System.out.println(e);
		}
		
		return null;
	}
	//вряд ли будет работать нормально для html
	static public byte[] decodeFromArchiv(byte[] gzip) throws UnsupportedEncodingException
	{
		String str = new String(gzip, "UTF-8");
		String zipStr = str.replaceFirst("application/vnd.xfdl;content-encoding=\"base64-gzip\"\n", "");
		byte[] data =  Base64.decode(zipStr, Base64.GZIP);
		return data;
	}

}

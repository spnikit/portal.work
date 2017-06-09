package ru.aisa.etdportal.controllers;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Array;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipArchiveOutputStream;
import org.apache.log4j.Logger;
import org.fusesource.hawtbuf.ByteArrayInputStream;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.ParameterizedRowMapper;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.web.servlet.ModelAndView;

import ru.aisa.etdadmin.controllers.AbstractMultipartController;
import ru.aisa.rgd.etd.objects.TORArchiveReportObject;
import ru.aisa.rgd.ws.utility.TypeConverter;
import sheff.rjd.gvcservice.GetNameINN;

public class ExportXMLArchive extends AbstractMultipartController {
	 private NamedParameterJdbcTemplate npjt;
		protected final Logger	log	= Logger.getLogger(getClass());
		protected final String checktype = "select name from snt.doctype where id = (select typeid from snt.docstore where id = :docid)";
		
		private final String prepareId = "select ds.id from snt.docstore as ds full outer join snt.doctype as dt "
				+ "on ds.typeid = dt.id where dt.name in ('Акт РТК', 'ФПУ-26', 'ФПУ-26 АСР') and "
				+ "ds.id_pak in (select dss.id_pak from snt.docstore as dss full outer join "
				+ "snt.doctype dtt on dss.typeid = dtt.id where dtt.name in "
				+ "('Пакет документов', 'Пакет документов РТК', 'Пакет документов ЦСС') and dss.id in "
				+ "(:list)) union select ds2.id from snt.docstore as ds2 full outer join snt.doctype as "
				+ "dt2 on ds2.typeid = dt2.id where dt2.name = 'ТОРГ-12' and ds2.id in (:list)";
		
		public NamedParameterJdbcTemplate getNpjt() {
			return npjt;
		}
		public void setNpjt(NamedParameterJdbcTemplate npjt) {
			this.npjt = npjt;
		}

		public ExportXMLArchive() throws JSONException {
			super();
		}

		protected JSONObject get(HttpServletRequest request) throws JSONException {
			String ids = request.getParameter("ids");
			List<Long> list = getListId(ids);

			HashMap <String, List<Long>> param = new HashMap<String, List<Long>>();
			param.put("list", list);
			
			JSONObject obj = new JSONObject();
			
			try{
			//Передаем все id для которых пытаемся выгрузить xml, проверяются только пакеты
	
			final String sql_old= "with temp as (select id_pak, (select count(etdid) cn "+
			"from snt.packages p where id_pak = ds.id_pak and etdid in (select etdid " +
			"from snt.docstore where etdid = p.etdid and typeid in " +
			"(select id from snt.doctype where name in ('ФПУ-26','Акт РТК')))) " +
			"from snt.docstore ds where id in (:list) and typeid in (select id from snt.doctype " +
			"where name in ('Пакет документов','Пакет документов РТК'))) select case " +
			"when (select count(distinct(cn)) from temp where temp.cn=0)=0 " +
			"then 0 else 1 end from temp fetch first row only";
			
			final String sql_new = "with temp as (select ds.id_pak as pack from snt.docstore ds "
			+ "full outer join snt.doctype dt on ds.typeid = dt.id where dt.name in "
			+ "('Пакет документов','Пакет документов РТК','Пакет документов ЦСС') and ds.id in (:list)) "
			+ "select case "
			+ "when (select count(distinct(pack)) from temp) = (select count(ds.id) from "
			+ "snt.docstore ds full outer join snt.doctype dt on ds.typeid = dt.id where dt.name "
			+ "in ('ФПУ-26','Акт РТК','ФПУ-26 АСР') and ds.id_pak in (select pack from temp)) "
			+ "then 0 else 1 end from temp fetch first row only";
			
			try{
			Integer count = getNpjt().queryForInt(sql_new, param);
			if (count != 0) {
				obj.put("count", false);
				return obj;
			} else {
				obj.put("count", true);
			}
			} catch (EmptyResultDataAccessException e) {
				obj.put("count", true);
			}
			
			//Заменяем id пакетов на id ФПУ-26/Акт РТК/ФПУ-26 АСР в списке айдишников
			List<Long> listId = replaceIds(param);
			
			param.put("list_id", listId);
			//Проверка наличия записей в таблице snt.archivexmlsigns
			final String main_check = "select case "
			+ "when (select count(distinct(docid)) from snt.archivexmlsigns where docid in (:list_id)) = 0 "
			+ "then 0 "
			+ "when (select count(distinct(docid)) from snt.archivexmlsigns where docid in (:list_id)) < "
			+ "(select count(id) from snt.docstore where id in (:list_id)) "
			+ "then 1 "
			+ "when (select count(distinct(docid)) from snt.archivexmlsigns where docid in (:list_id)) = "
			+ "(select count(id) from snt.docstore where id in (:list_id)) "
			+ "then 2 end from snt.archivexmlsigns fetch first row only";
								
			Long index = getNpjt().queryForLong(main_check, param);
			obj.put("index", index);
			} catch (Exception ex) {
				log.error(TypeConverter.exceptionToString(ex));
			}
			return obj;
						
		}
		protected void export(HttpServletRequest request, HttpServletResponse response) throws IOException{
			try {
			request.setCharacterEncoding("UTF-8");	

			String ids = request.getParameter("ids");
			List<Long> list = getListId(ids);

			HashMap<String, List<Long>> param = new HashMap<String, List<Long>>();
			param.put("list", list);
				
			List<Long> list_id = replaceIds(param);
			
			param.put("list_id", list_id);

			final String mainSelect_old = "select sx.docid, dt.name, sx.xml, sx.sign, sx.id_pack "
					+ "from snt.archivexmlsigns as sx full outer join snt.doctype as dt "
					+ "on sx.typeid = dt.id where sx.docid in "
					+ "(select id from snt.docstore where typeid in "
					+ "(select id from snt.doctype where name in ('ТОРГ-12', 'ФПУ-26 АСР')) "
					+ "and id in (:list) union select ds.id from snt.docstore as ds full outer "
					+ "join snt.doctype as dt on ds.typeid = dt.id where dt.name in "
					+ "('Акт РТК', 'ФПУ-26') and ds.id_pak in (select dss.id_pak from "
					+ "snt.docstore as dss full outer join snt.doctype as dtt on "
					+ "dss.typeid = dtt.id  where dss.id in (:list) and "
					+ "dtt.name in ('Пакет документов', 'Пакет документов РТК'))) order by docid";
			
			final String mainSelect = "select sx.docid, dt.name, sx.xml, sx.sign, sx.id_pack "
					+ "from snt.archivexmlsigns as sx full outer join snt.doctype as dt "
					+ "on sx.typeid = dt.id where sx.docid in (:list_id)";
			
			response.setCharacterEncoding("UTF-8");
			response.setHeader("Content-disposition",
					"attachment; filename=XMLsign.zip");
			response.setContentType("application/zip");

			ServletOutputStream respout = response.getOutputStream();

			final ZipArchiveOutputStream zipstream = new ZipArchiveOutputStream(respout);
			ZipArchiveEntry entry = null;
			byte[] xml = null;
			byte[] sign = null;
			String foldername;
			String filename;
			
			List<SignXml> docList = new ArrayList <SignXml>();
			
			docList = getNpjt().query(mainSelect, param, new Mapper());
			for(SignXml sx: docList) {

				//Создаем папки для выгрузки
				xml = sx.getXml();
				foldername = sx.getId_pack() + "_" + sx.getType_name();
				filename = getFileName(xml);
				entry = new ZipArchiveEntry (foldername + "/" + filename + ".xml");
				
				entry.setSize(xml.length);
				zipstream.putArchiveEntry(entry);
				zipstream.write(xml);
				zipstream.closeArchiveEntry();
				
				sign = sx.getSign();
				entry = new ZipArchiveEntry (foldername + "/" + filename + ".p7s");
				
				entry.setSize(sign.length);
				zipstream.putArchiveEntry(entry);
				zipstream.write(sign);
				zipstream.closeArchiveEntry();
						
			}
			zipstream.flush();		
			
			zipstream.close();

			respout.close();
			
			} catch (Exception e) {
				e.printStackTrace();
				log.error(TypeConverter.exceptionToString(e));
			}
		}
		
		private List<Long> replaceIds (HashMap <String, List<Long>> param) {
			return getNpjt().queryForList(prepareId, param, Long.class);
		}

		private String getFileName (byte[] file) throws UnsupportedEncodingException {

			DocumentBuilderFactory factory = DocumentBuilderFactory
					.newInstance();
			DocumentBuilder builder;
			String fileName = null;
			try {
				builder = factory.newDocumentBuilder();
				fileName = builder.parse(new ByteArrayInputStream(file)).getDocumentElement()
						.getAttributes().getNamedItem("ИдФайл").getTextContent();
				
			} catch (Exception e) {
				log.error(TypeConverter.exceptionToString(e));
			}
			return fileName;
		}
		
		private List<Long> getListId (String ids) {
			String [] arrayStr= ids.split(";");
			List<Long> list = new ArrayList<Long>();
			for(int i = 0; i < arrayStr.length; i++) {
				list.add(Long.valueOf(arrayStr[i]));
			}
			return list;
		}

		class SignXml
		{
			private byte[] xml;
			private byte[] sign;
			private Long docId;
			private String id_pack;
			private String type_name;
			
			public String getId_pack() {
				return id_pack;
			}

			public void setId_pack(String id_pack) {
				this.id_pack = id_pack;
			}

			public String getType_name() {
				return type_name;
			}

			public void setType_name(String type_name) {
				this.type_name = type_name;
			}

			public Long getDocId() {
				return docId;
			}

			public void setDocId(Long docId) {
				this.docId = docId;
			}

			public byte[] getXml() {
				return xml;
			}

			public void setXml(byte[] xml) {
				this.xml = xml;
			}

			public byte[] getSign() {
				return sign;
			}

			public void setSign(byte[] sign) {
				this.sign = sign;
			}

			SignXml(Long docId ,byte[] xml,byte[] sign, String type_name, String id_pack)
			{
				this.docId = docId;
				this.sign = sign;
				this.xml = xml;
				this.type_name = type_name;
				this.id_pack = id_pack;			
			}
		}
		
		
		class Mapper implements ParameterizedRowMapper<SignXml> {

			public SignXml mapRow(ResultSet rs, int rowNum) throws SQLException {
				
				Long docId = rs.getLong("docid");
				byte[] xml = rs.getBytes("xml");
				byte[] sign = rs.getBytes("sign");
				String id_pack = rs.getString("id_pack");
				String type_rus = rs.getString("name").trim();
				String type_eng = null;
				
				if (type_rus.equals("ФПУ-26")) type_eng = "FPU-26";
				if (type_rus.equals("ФПУ-26 АСР")) type_eng = "FPU-26 ASR";
				if (type_rus.equals("Акт РТК")) type_eng = "Akt RTK";
				if (type_rus.equals("ТОРГ-12")) type_eng = "TORG-12";
				
				try{
					return new SignXml(docId, xml, sign, type_eng, id_pack);

				} catch (Exception e) {
					log.error(TypeConverter.exceptionToString(e));
				}
				return null;
				
			}
		
		}
}

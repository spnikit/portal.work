package ru.aisa.etdportal.controllers;


import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipArchiveOutputStream;
import org.codehaus.plexus.util.StringUtils;
import org.fusesource.hawtbuf.ByteArrayInputStream;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.jdbc.core.simple.ParameterizedRowMapper;
import org.w3c.dom.Document;

import ru.aisa.etdadmin.controllers.AbstractMultipartController;
import ru.aisa.etdadmin.controllers.MultipartImpl.HeadidSave.HeadidDoc;
import ru.aisa.rgd.etd.objects.ETDDocument;
import ru.aisa.rgd.ws.utility.TypeConverter;
import sheff.rjd.ws.OCO.TORLists;


public class getXML extends AbstractMultipartController {

	public getXML() throws JSONException {
		super();
	}

	@SuppressWarnings("unchecked")
	@Override
	public void export(HttpServletRequest request, HttpServletResponse response)
			throws IOException {

		request.setCharacterEncoding("UTF-8");

		ArrayList<Object[]> List = new ArrayList<Object[]>();
		List<Integer> Lids = new ArrayList();
		int iter = 0;
		while (request.getParameter("id" + iter) != null) {
			//1 - флаг для пакетов
			if (request.getParameter("ispack" + iter).equals("1")){
				HashMap<String, Object> idpack = new HashMap<String, Object>();

				idpack.put("id", request.getParameter("id" + iter));
				idpack.put("sf", TORLists.SFRTK);
				idpack.put("corrsf", TORLists.CorrSFRTK);

				try{

					List sfids = getNpjt().queryForList("select id from snt.docstore where id_pak = (select id_pak from snt.docstore where id = :id) "
							+" and typeid in (select id from snt.doctype where name in (:sf,:corrsf))", idpack);

					for (int j=0;j<sfids.size();j++){
						
						Map aa = (HashMap) sfids.get(j);
						Lids.add(Integer.parseInt(aa.get("ID").toString()));
						
					}
				} catch (Exception e){
					log.error(TypeConverter.exceptionToString(e));
				} 
			}



			else {
				Lids.add(Integer.parseInt(request.getParameter("id" + iter)));
			}
			//			List.add(new Object[] { request.getParameter("id" + iter),
			//					request.getParameter("name" + iter),
			//					request.getParameter("content" + iter) });

			iter++;

		}

		// final String filename = List.get(0)[0]+"_"+"SF"+"_"+List.get(0)[2];
		HashMap in = new HashMap();
		in.put("DOCID", Lids);
		response.setCharacterEncoding("UTF-8");
		response.setHeader("Content-disposition",
				"attachment; filename=SFdata.zip");
		response.setContentType("application/zip");

		ServletOutputStream respout = response.getOutputStream();

		final ZipArchiveOutputStream zaos = new ZipArchiveOutputStream(respout);
		//		zaos.setEncoding("UTF-8");
		String sql = "select ID, SF_D1,SF_D2,SF_D3,SF_D4, SF_D5, SF_D6, SF_D8, "
				+" SF_S1,SF_S2,SF_S3,SF_S4, SF_S5, SF_S6, SF_S8,"
				+ "(select UV_D1 from snt.dfkorr where id = DFS.ID), "
				+ "(select UV_S1 from snt.dfkorr where id = DFS.ID), "
				+ " (select UV_D2 from snt.dfkorr where id = DFS.ID), "
				+ " (select UV_S2 from snt.dfkorr where id = DFS.ID), "
				+ " SF_FD, SF_FDS1, "
				+ " (select OPISANIE from SNT.DOCSTORE where id = DFS.ID), "
				+ " (select id_pak from snt.docstore where id =  DFS.ID) "
				+ " from snt.DFSIGNS DFS where id in (:DOCID) ";

		try{

			getNpjt().query(sql, in, new ParameterizedRowMapper<Object>() {

				public Object mapRow(ResultSet rs, int numrow) throws SQLException {
					JSONObject js = new JSONObject();
					try {

						DocumentBuilderFactory factory = DocumentBuilderFactory
								.newInstance();
						DocumentBuilder builder;
						builder = factory.newDocumentBuilder();

						//					Document sf = builder.parse(new ByteArrayInputStream(rs
						//							.getBytes("SF_D1")));
						String filename = null;
						if(StringUtils.isBlank(rs.getString("ID_PAK"))) {
							filename = String.valueOf(rs.getInt("ID")) + "_"
									+ rs.getString("OPISANIE");
						} else {
							filename = String.valueOf(rs.getString("ID_PAK")) + "_"
									+ rs.getString("OPISANIE");
						}
						ZipArchiveEntry entry = null;
						byte[] SF = null;
						byte[] SIGN = null;
						String name;
						SF = rs.getBytes("SF_FD");
						SIGN= rs.getBytes("SF_FDS1");

						if (SF!=null&&SIGN!=null&&SF.length>0&&SIGN.length>0){
							//					name = builder.parse(new ByteArrayInputStream(rs
							//							.getString("SF_FDS_XML").getBytes())).getDocumentElement()
							//							.getAttributes().getNamedItem("ИдФайл").getTextContent();
							name = builder.parse(new ByteArrayInputStream(rs
									.getBytes("SF_FD"))).getDocumentElement()
									.getAttributes().getNamedItem("ИдФайл").getTextContent();
							entry = new ZipArchiveEntry(filename
									+ "/"+name+".xml");
							//					byte[] SF = rs.getString("SF_FDS_XML").getBytes("UTF-8");
							entry.setSize(SF.length);
							zaos.putArchiveEntry(entry);
							zaos.write(SF);
							zaos.closeArchiveEntry();

							entry = new ZipArchiveEntry(filename
									+ "/"+name+".p7s");
							//						byte[] SF = rs.getString("SF_FDS_XML").getBytes("UTF-8");
							entry.setSize(SIGN.length);
							zaos.putArchiveEntry(entry);
							zaos.write(SIGN);
							zaos.closeArchiveEntry();

						}





						SF = rs.getBytes("SF_D1");
						SIGN = rs.getBytes("SF_S1");

						if (SF!=null&&SIGN!=null&&SF.length > 0&&SIGN.length>0) {
							name = builder.parse(new ByteArrayInputStream(rs
									.getBytes("SF_D1"))).getDocumentElement()
									.getAttributes().getNamedItem("ИдФайл").getTextContent();
							entry = new ZipArchiveEntry(filename + "/"+name+".xml");


							entry.setSize(SF.length);
							zaos.putArchiveEntry(entry);
							zaos.write(SF);
							zaos.closeArchiveEntry();

							entry = new ZipArchiveEntry(filename + "/"+name+".p7s");


							entry.setSize(SIGN.length);
							zaos.putArchiveEntry(entry);
							zaos.write(SIGN);
							zaos.closeArchiveEntry();	

						}


						SF = rs.getBytes("SF_D2");
						SIGN = rs.getBytes("SF_S2");


						if (SF!=null&&SIGN!=null&&SF.length > 0&&SIGN.length>0) {
							name = builder.parse(new ByteArrayInputStream(rs
									.getBytes("SF_D2"))).getDocumentElement()
									.getAttributes().getNamedItem("ИдФайл").getTextContent();
							entry = new ZipArchiveEntry(filename + "/"+name+".xml");


							entry.setSize(SF.length);
							zaos.putArchiveEntry(entry);
							zaos.write(SF);
							zaos.closeArchiveEntry();

							entry = new ZipArchiveEntry(filename + "/"+name+".p7s");


							entry.setSize(SIGN.length);
							zaos.putArchiveEntry(entry);
							zaos.write(SIGN);
							zaos.closeArchiveEntry();	

						}
						SF = rs.getBytes("SF_D3");
						SIGN = rs.getBytes("SF_S3");


						if (SF!=null&&SIGN!=null&&SF.length > 0&&SIGN.length>0) {
							name = builder.parse(new ByteArrayInputStream(rs
									.getBytes("SF_D3"))).getDocumentElement()
									.getAttributes().getNamedItem("ИдФайл").getTextContent();
							entry = new ZipArchiveEntry(filename + "/"+name+".xml");


							entry.setSize(SF.length);
							zaos.putArchiveEntry(entry);
							zaos.write(SF);
							zaos.closeArchiveEntry();

							entry = new ZipArchiveEntry(filename + "/"+name+".p7s");


							entry.setSize(SIGN.length);
							zaos.putArchiveEntry(entry);
							zaos.write(SIGN);
							zaos.closeArchiveEntry();	

						}

						SF = rs.getBytes("SF_D4");
						SIGN = rs.getBytes("SF_S4");


						if (SF!=null&&SIGN!=null&&SF.length > 0&&SIGN.length>0) {
							name = builder.parse(new ByteArrayInputStream(rs
									.getBytes("SF_D4"))).getDocumentElement()
									.getAttributes().getNamedItem("ИдФайл").getTextContent();
							entry = new ZipArchiveEntry(filename + "/"+name+".xml");


							entry.setSize(SF.length);
							zaos.putArchiveEntry(entry);
							zaos.write(SF);
							zaos.closeArchiveEntry();

							entry = new ZipArchiveEntry(filename + "/"+name+".p7s");


							entry.setSize(SIGN.length);
							zaos.putArchiveEntry(entry);
							zaos.write(SIGN);
							zaos.closeArchiveEntry();	

						}
						SF = rs.getBytes("SF_D5");
						SIGN = rs.getBytes("SF_S5");


						if (SF!=null&&SIGN!=null&&SF.length > 0&&SIGN.length>0) {
							name = builder.parse(new ByteArrayInputStream(rs
									.getBytes("SF_D5"))).getDocumentElement()
									.getAttributes().getNamedItem("ИдФайл").getTextContent();
							entry = new ZipArchiveEntry(filename + "/"+name+".xml");


							entry.setSize(SF.length);
							zaos.putArchiveEntry(entry);
							zaos.write(SF);
							zaos.closeArchiveEntry();

							entry = new ZipArchiveEntry(filename + "/"+name+".p7s");


							entry.setSize(SIGN.length);
							zaos.putArchiveEntry(entry);
							zaos.write(SIGN);
							zaos.closeArchiveEntry();	

						}
						SF = rs.getBytes("SF_D6");
						SIGN = rs.getBytes("SF_S6");


						if (SF!=null&&SIGN!=null&&SF.length > 0&&SIGN.length>0) {
							name = builder.parse(new ByteArrayInputStream(rs
									.getBytes("SF_D6"))).getDocumentElement()
									.getAttributes().getNamedItem("ИдФайл").getTextContent();
							entry = new ZipArchiveEntry(filename + "/"+name+".xml");


							entry.setSize(SF.length);
							zaos.putArchiveEntry(entry);
							zaos.write(SF);
							zaos.closeArchiveEntry();

							entry = new ZipArchiveEntry(filename + "/"+name+".p7s");


							entry.setSize(SIGN.length);
							zaos.putArchiveEntry(entry);
							zaos.write(SIGN);
							zaos.closeArchiveEntry();	

						}


						SF = rs.getBytes("SF_D8");
						SIGN = rs.getBytes("SF_S8");


						if (SF!=null&&SIGN!=null&&SF.length > 0&&SIGN.length>0) {
							name = builder.parse(new ByteArrayInputStream(rs
									.getBytes("SF_D8"))).getDocumentElement()
									.getAttributes().getNamedItem("ИдФайл").getTextContent();
							entry = new ZipArchiveEntry(filename + "/"+name+".xml");


							entry.setSize(SF.length);
							zaos.putArchiveEntry(entry);
							zaos.write(SF);
							zaos.closeArchiveEntry();

							entry = new ZipArchiveEntry(filename + "/"+name+".p7s");


							entry.setSize(SIGN.length);
							zaos.putArchiveEntry(entry);
							zaos.write(SIGN);
							zaos.closeArchiveEntry();	

						}

						SF = rs.getBytes("UV_D1");
						SIGN = rs.getBytes("UV_S1");

						if (SF!=null&&SIGN!=null&&SF.length > 0&&SIGN.length>0) {
							name = builder.parse(new ByteArrayInputStream(rs
									.getBytes("UV_D1"))).getDocumentElement()
									.getAttributes().getNamedItem("ИдФайл").getTextContent();
							entry = new ZipArchiveEntry(filename + "/"+name+".xml");


							entry.setSize(SF.length);
							zaos.putArchiveEntry(entry);
							zaos.write(SF);
							zaos.closeArchiveEntry();

							entry = new ZipArchiveEntry(filename + "/"+name+".p7s");


							entry.setSize(SIGN.length);
							zaos.putArchiveEntry(entry);
							zaos.write(SIGN);
							zaos.closeArchiveEntry();	

						}

						SF = rs.getBytes("UV_D2");
						SIGN = rs.getBytes("UV_S2");


						if (SF!=null&&SIGN!=null&&SF.length > 0&&SIGN.length>0) {
							name = builder.parse(new ByteArrayInputStream(rs
									.getBytes("UV_D2"))).getDocumentElement()
									.getAttributes().getNamedItem("ИдФайл").getTextContent();
							entry = new ZipArchiveEntry(filename + "/"+name+".xml");


							entry.setSize(SF.length);
							zaos.putArchiveEntry(entry);
							zaos.write(SF);
							zaos.closeArchiveEntry();

							entry = new ZipArchiveEntry(filename + "/"+name+".p7s");


							entry.setSize(SIGN.length);
							zaos.putArchiveEntry(entry);
							zaos.write(SIGN);
							zaos.closeArchiveEntry();	

						}

					} catch (Exception e) {
						log.error(TypeConverter.exceptionToString(e));

					}
					return null;
				}
			});
		}catch(Exception e){
			e.printStackTrace();
		}

		zaos.close();

		respout.close();

	}

	protected JSONObject get(HttpServletRequest request) throws JSONException{

		JSONObject response = new JSONObject();
		final JSONArray jsArray = new JSONArray();
		String ids = request.getParameter("idSf");
		String isPack = request.getParameter("isPack");
		String[] isPackArray = isPack.split(";");
		List<Integer> Lids = new ArrayList();
		
		if(!StringUtils.isBlank(ids)){
			String[] idsArray = ids.split(";");
			for(int i = 0, k = 0; i < idsArray.length && k < isPackArray.length; i++, k++ ){

				HashMap<String, Object> idpack = new HashMap<String, Object>();

				idpack.put("id", idsArray[i]);
				idpack.put("sf", TORLists.SFRTK);
				idpack.put("corrsf", TORLists.CorrSFRTK);

				List sfids = getNpjt().queryForList("select id from snt.docstore where id_pak = (select id_pak from snt.docstore where id = :id) "
						+" and typeid in (select id from snt.doctype where name in (:sf,:corrsf))", idpack);
			
				String idPack = getNpjt().queryForObject("SELECT distinct CASE WHEN id_pak is null THEN '' ELSE id_pak END FROM snt.docstore where id = :id", idpack, String.class);
	
				if(sfids.size()!=0 && isPackArray[k].equals("1")){

					for(int j = 0; j < sfids.size(); j++){

						Map aa = (HashMap) sfids.get(j);
						idpack.put("idSf", Integer.parseInt(aa.get("ID").toString()));


						int countSf = getNpjt().queryForInt("select count(*) from snt.DFSIGNS where id in (:idSf)", idpack);

						if(countSf != 0){
							JSONObject js = new JSONObject();
							js.put("iter", i);
							js.put("id", idsArray[i]);
							js.put("isIdPack", 1);
							js.put("isEmpty", 0);
							js.put("sfId", Integer.parseInt(aa.get("ID").toString()));
							js.put("containBase", 1);
							js.put("idPack", idPack);
							jsArray.put(js);
						}else{
							JSONObject js = new JSONObject();
							js.put("iter", i);
							js.put("id", idsArray[i]);
							js.put("isIdPack", 1);
							js.put("isEmpty", 0);
							js.put("sfId", Integer.parseInt(aa.get("ID").toString()));
							js.put("containBase", 0);
							js.put("idPack", idPack);
							jsArray.put(js);
						}
					}	
				}else if(sfids.size()==0 && !isPackArray[k].equals("1")){

					idpack.put("idSf", idsArray[i]);
					int countSf = getNpjt().queryForInt("select count(*) from snt.DFSIGNS where id in (:idSf)", idpack);
					if(countSf != 0){
						JSONObject js = new JSONObject();
						js.put("iter", i);
						js.put("id", idsArray[i]);
						js.put("isIdPack", 0);
						js.put("isEmpty", 0);
						js.put("sfId", idsArray[i]);
						js.put("containBase", 1);
						js.put("idPack", idPack);
						jsArray.put(js);
					}else{
						JSONObject js = new JSONObject();
						js.put("iter", i);
						js.put("id", idsArray[i]);
						js.put("isIdPack", 0);
						js.put("isEmpty", 0);
						js.put("sfId", idsArray[i]);
						js.put("containBase", 0);
						js.put("idPack", idPack);
						jsArray.put(js);
					}

				}else{

					JSONObject js = new JSONObject();
					js.put("iter", i);
					js.put("id", idsArray[i]);
					js.put("isIdPack", 1);
					js.put("isEmpty", 1);
					js.put("containBase", 0);
					js.put("idPack", idPack);
					jsArray.put(js);
				}
			}

		}else{
			jsArray.put(new JSONObject().put("isEmpty", 1));
		}
		response.put("data", jsArray);

		return response;
	}

}
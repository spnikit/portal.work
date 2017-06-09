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

import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipArchiveOutputStream;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.jdbc.core.simple.ParameterizedRowMapper;

import com.ibm.db2.jcc.c.ob;

import ru.aisa.etdadmin.controllers.AbstractMultipartController;
import ru.aisa.rgd.etd.objects.ETDDocument;
import ru.aisa.rgd.ws.utility.TypeConverter;
import sheff.rjd.ws.OCO.TORLists;

public class getDocumentRTK extends AbstractMultipartController {

	public getDocumentRTK() throws JSONException {
		super();
	}

	private static final String sqlpackdocnew = "select id ,bldoc, docdata, (select rtrim(name) name from snt.doctype where id = ds.typeid), opisanie, vagnum, repdate, "
			+ "(case when ds.signlvl is null then 1 else 0 end) as signed, stat,(select count(0) count from snt.docstoreflow where docid = ds.id), "
			+ "(case when ds.dropid is null then 0 else 1 end) as drop, visible, id_pak, etdid "
			+ "from snt.docstore ds where "
			+ "(visible = 0 or visible =2) and "
			+ "ds.id_pak = (select id_pak from snt.docstore where id =:DOCID)";

	
	@SuppressWarnings("unchecked")
	@Override
	public void export(HttpServletRequest request, HttpServletResponse response)
			throws IOException {

		request.setCharacterEncoding("UTF-8");
		response.setHeader("Content-disposition",
				"attachment; filename=DocData.zip");
		response.setContentType("application/zip; charset=UTF-8");

		ServletOutputStream respout = response.getOutputStream();
		final ZipArchiveOutputStream zaos = new ZipArchiveOutputStream(respout);
		
		int iter = 0;
		while (request.getParameter("id" + iter) != null)  {
			HashMap<String, Object> in = new HashMap<String, Object>();
			in.put("DOCID", request.getParameter("id" + iter));
			
			
			getNpjt().query(sqlpackdocnew, in, new ParameterizedRowMapper<Object>() {

				public Object mapRow(ResultSet rs, int numrow)
						throws SQLException {
				try {
						String package_name = "Package_"+rs.getString("id_pak")+"/";
						String formname = TORLists.exportList.get(rs.getString("name")).toString();
						String filename = formname + "_pack_"+ rs.getString("id_pak") + "_" + "ID_"+ String.valueOf(rs.getInt("ETDID"));

						ZipArchiveEntry entry = new ZipArchiveEntry(package_name + filename + "/" + formname+ ".xfdl");
						byte[] data = rs.getBytes("BLDOC");
						entry.setSize(data.length);
						zaos.putArchiveEntry(entry);
						zaos.write(data);
						zaos.closeArchiveEntry();

						data = rs.getString("DOCDATA").getBytes("UTF-8");
						if (data.length > 0) {
							entry = new ZipArchiveEntry(package_name + filename+ "/" + formname + ".xml");
						entry.setSize(data.length);
						zaos.putArchiveEntry(entry);
						zaos.write(data);
						zaos.closeArchiveEntry();

						}

					} catch (Exception e) {
						log.error(TypeConverter.exceptionToString(e));

					}
					return null;
				}
			});
iter++;
		}

		zaos.close();
		respout.close();

	}

	private String[] concatArray(String[] a, String[] b) {
		if (a == null)
			return b;
		if (b == null)
			return a;
		String[] r = new String[a.length + b.length];
		System.arraycopy(a, 0, r, 0, a.length);
		System.arraycopy(b, 0, r, a.length, b.length);
		return r;
	}
}
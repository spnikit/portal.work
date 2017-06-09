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
import sheff.rjd.ws.OCO.TORLists;

public class getDocument extends AbstractMultipartController {

	public getDocument() throws JSONException {
		super();
	}

	private static final String sqlpackdocnew = "select id , (select rtrim(name) name from snt.doctype where id = ds.typeid), opisanie, vagnum, repdate, "
			+ "(case when ds.signlvl is null then 1 else 0 end) as signed, stat,(select count(0) count from snt.docstoreflow where docid = ds.id), "
			+ "(case when ds.dropid is null then 0 else 1 end) as drop, visible "
			+ "from snt.docstore ds where "
			+ "(visible = 0 or visible =2) and "
			+ "ds.etdid in (select etdid from snt.packages where id_pak = (select id_pak from snt.docstore where id =:DOCID))";

	private static final String sqldocumentnew = "select id , (select rtrim(name) name from snt.doctype where id = ds.typeid), opisanie, vagnum, repdate, "
			+ "(case when ds.signlvl is null then 1 else 0 end) as signed, stat,(select count(0) count from snt.docstoreflow where docid = ds.id), "
			+ "(case when ds.dropid is null then 0 else 1 end) as drop, visible "
			+ "from snt.docstore ds where id=:DOCID";

	@SuppressWarnings("unchecked")
	@Override
	public void export(HttpServletRequest request, HttpServletResponse response)
			throws IOException {

		request.setCharacterEncoding("UTF-8");
		response.setHeader("Content-disposition",
				"attachment; filename=DocData.zip");
		response.setContentType("application/zip; charset=UTF-8");

		String sql = "select ETDID, BLDOC, vagnum,  docdata, (select rtrim(name) name from snt.doctype where id =ds.typeid), "
				+ "id_pak from snt.docstore ds where id in (:DOCID)";
		

		ServletOutputStream respout = response.getOutputStream();
		final ZipArchiveOutputStream zaos = new ZipArchiveOutputStream(respout);
		String groupsId = request.getParameter("groupId");
		String groupDoc = request.getParameter("groupDoc");
		String[] groupsIdMass = null;
		String[] groupDocMass = null;
		final int result;
		if (groupsId != null) {
			groupsIdMass = groupsId.split(";");
			result = groupsIdMass.length;
		} else {
			groupsIdMass = null;
			result = 0;
		}
		if (groupDoc != null) {
			groupDocMass = groupDoc.split(";");
		} else {
			groupDocMass = null;
		}
		String[] coupledArray = concatArray(groupsIdMass, groupDocMass);
		String finalsql = "";
		String name;

		for (int i = 0; i < coupledArray.length; i++) {
			final int k = i;
			final ArrayList<Object[]> List = new ArrayList<Object[]>();
			List<Long> Lids = new ArrayList();

			if (i < result) {
				finalsql = sqlpackdocnew;

				// name = "Package with id in "+coupledArray[i]+"/";
			} else {
				finalsql = sqldocumentnew;
				name = "";

			}
//			System.out.println(finalsql);
			Map id = new HashMap();
			id.put("DOCID", Long.parseLong(coupledArray[i]));
			JSONObject documents = new JSONObject();
			List<ETDDocument> list = getNpjt().query(finalsql, id,
					new ParameterizedRowMapper<ETDDocument>() {
						public ETDDocument mapRow(ResultSet rs, int n)
								throws SQLException {
							ETDDocument obj = new ETDDocument();
							obj.setId(rs.getLong("ID"));
							obj.setName(rs.getString("NAME"));
							obj.setVagnum(rs.getString("VAGNUM"));
							obj.setStat(rs.getInt("STAT"));
							obj.setReqdate(rs.getString("REPDATE"));
							obj.setContent(rs.getString("OPISANIE"));
							obj.setCount(rs.getInt("COUNT"));
							obj.setSigned(rs.getInt("SIGNED"));

							if (rs.getInt("drop") == 1) {

								obj.setDrop(true);
							}
							obj.setVisible(rs.getInt("visible"));
							return obj;
						}
					});
			
//			System.out.println(list.get(0));
			
			
			List<Long> vagnum = getNpjt().query(sql, id,
					new ParameterizedRowMapper<Long>() {
						public Long mapRow(ResultSet rs, int n)
								throws SQLException {
							Long vagnum = rs.getLong("VAGNUM");
							return vagnum;
						}
					});
			
//			System.out.println("vagnum =" + vagnum.get(0));
			
			
			for (ETDDocument obj : list) {

				if (obj.getId() != null) {
					Lids.add(obj.getId());
					List.add(new Object[] { obj.getId(), obj.getName() });

				}
			}
			String package_n = "";
			if (k < result) {
				package_n = "Package_vagon_" + vagnum.get(0) + "_" + "ID_"
						+ list.get(0).getContent() + "/";

			} else {
				package_n = "";
			}
			final String package_name = package_n;
			HashMap in = new HashMap();
			in.put("DOCID", Lids);

			// final String package_name = name;
			getNpjt().query(sql, in, new ParameterizedRowMapper<Object>() {

				public Object mapRow(ResultSet rs, int numrow)
						throws SQLException {
					JSONObject js = new JSONObject();
					try {
						
						System.out.println(rs.getString("name"));

						String formname = TORLists.exportList.get(
								rs.getString("name")).toString();
						
						System.out.println(formname);
						String filename = null;
						ZipArchiveEntry entry = null;
						if(rs.getString("name").equals("Претензия")) {
							filename = formname + "_vagon_"
									+ rs.getString("vagnum") + "_" + "ID_PAK_"
									+ String.valueOf(rs.getString("id_pak"));
							entry = new ZipArchiveEntry(
									package_name + filename + "/" + formname + "_" + "ID_PAK_"
											+ String.valueOf(rs.getString("id_pak")) + ".xfdl");
						} else {
							filename = formname + "_vagon_"
									+ rs.getString("vagnum") + "_" + "ID_"
									+ String.valueOf(rs.getInt("ETDID"));
							entry = new ZipArchiveEntry(
									package_name + filename + "/" + formname
											+ ".xfdl");
						}

						byte[] data = rs.getBytes("BLDOC");
						entry.setSize(data.length);
						zaos.putArchiveEntry(entry);
						zaos.write(data);
						zaos.closeArchiveEntry();

						data = rs.getString("DOCDATA").getBytes("UTF-8");
						if (data.length > 0) {
							if(rs.getString("name").equals("Претензия")) {
								entry = new ZipArchiveEntry(package_name + filename
										+ "/" + formname + "_" + "ID_PAK_"
												+ String.valueOf(rs.getString("id_pak")) + ".xml");
							} else {
								entry = new ZipArchiveEntry(package_name + filename
										+ "/" + formname + ".xml");
							}
							entry.setSize(data.length);
							zaos.putArchiveEntry(entry);
							zaos.write(data);
							zaos.closeArchiveEntry();

						}

					} catch (Exception e) {
						e.printStackTrace();

					}
					return null;
				}
			});

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
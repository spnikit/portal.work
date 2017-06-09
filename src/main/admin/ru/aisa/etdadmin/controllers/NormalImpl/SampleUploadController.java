package ru.aisa.etdadmin.controllers.NormalImpl;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.json.JSONException;
import org.springframework.jdbc.core.simple.ParameterizedRowMapper;
import org.springframework.web.servlet.ModelAndView;

import ru.aisa.etdadmin.controllers.AbstractMultipartController;


public class SampleUploadController extends AbstractMultipartController{
	
	public SampleUploadController() throws JSONException{
		super();
	}
	
	protected ModelAndView handleRequestInternal(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		String headerField = new String(request.getParameter("headerField").getBytes("ISO8859-1"),"UTF-8");
		String predid =  new String(request.getParameter("predid").getBytes("ISO8859-1"),"UTF-8").trim();
		String dateFrom =  new String(request.getParameter("dateFrom").getBytes("ISO8859-1"),"UTF-8").trim();
		String dateTo =  new String(request.getParameter("dateTo").getBytes("ISO8859-1"),"UTF-8").trim();
		
		String sql =  "select etdid, id_pak, vagnum, (select rtrim(name) name from snt.doctype where id = ds.typeid), "
				+ "(select rtrim(vname) vname from snt.pred where id = ds.predid),"
				+ "(select repdate from snt.docstore where id_pak = ds.id_pak and typeid = (select id from snt.doctype where name = 'Пакет документов')) "
				+ " from snt.docstore ds where signlvl is null "
				+ "and typeid in (select id from snt.doctype where name in ('ФПУ-26','МХ-1','МХ-3','Акт приема передачи ТМЦ'))"
				+ " and ldate between :dateFrom and :dateTo and predid in (select id from snt.pred where id = :predid or headid = :predid)";
		
		Map<String,Object> paramMap = new HashMap<String, Object>();
		final List<DataObject> dataObjectList = new ArrayList<DataObject>(); 
		
		paramMap.put("predid", predid);
		paramMap.put("dateFrom", dateFrom);
		paramMap.put("dateTo", dateTo);
		
		getNpjt().query(sql, paramMap,
				new ParameterizedRowMapper<Object>() {

			public Object mapRow(ResultSet rs, int numrow)
					throws SQLException {

					DataObject dataObject = new DataObject();
					dataObject.setEtdid(rs.getString("etdid"));
					dataObject.setId_pak(rs.getString("id_pak"));
					dataObject.setVagnum(rs.getString("vagnum"));
					dataObject.setNameDoc(rs.getString("name"));
					dataObject.setNameOrganization(rs.getString("vname"));
					dataObject.setRepDate(rs.getString("repdate"));
					dataObjectList.add(dataObject);
				
				return null;
			}
		});
		
		Map<Integer, Object[]> data = new HashMap<Integer, Object[]>();
		String[] allHeaders = headerField.split(";");

		data.put(1, allHeaders);

		for(int i = 0; i < dataObjectList.size();i++){
			DataObject obj = dataObjectList.get(i);
			Object[] mass = new Object[6];
			mass[0] = obj.getEtdid();
			mass[1] = obj.getId_pak();
			mass[2] = obj.getVagnum();
			mass[3] = obj.getRepDate();
			mass[4] = obj.getNameDoc();
			mass[5] = obj.getNameOrganization();
			data.put(i+2, mass);
		}
		HSSFWorkbook workbook = new HSSFWorkbook();
		HSSFSheet sheet = workbook.createSheet("Sample sheet");

		Font font = workbook.createFont();
		font.setBoldweight(Font.BOLDWEIGHT_BOLD);

		CellStyle style;
		style = workbook.createCellStyle();
		style.setAlignment(CellStyle.ALIGN_CENTER);

		CellStyle style1;
		style1 = workbook.createCellStyle();
		style1.setAlignment(CellStyle.ALIGN_CENTER);
		style1.setFont(font);

		Set<Integer> keyset = data.keySet();
		int rownum = 0;
		for (int key=1;key<=keyset.size(); key++){
		
			Row row = sheet.createRow(rownum++);

			Object [] objArr = data.get(key);
			int cellnum = 0;
			for (Object obj : objArr){
			
				Cell cell = row.createCell(cellnum++);              
				if(key == 1){
					cell.setCellStyle(style1);
				}else{
					cell.setCellStyle(style);
				}

				if(obj instanceof String)
					cell.setCellValue((String)obj);
				else if(obj instanceof Integer)
					cell.setCellValue((Integer)obj);
			}

		}
		
		for(int colNum = 0; colNum<sheet.getRow(0).getLastCellNum();colNum++){ 
			sheet.autoSizeColumn(colNum);
		}
		
		try {

			ByteArrayOutputStream outByteStream = new ByteArrayOutputStream();
			workbook.write(outByteStream);
			byte [] outArray = outByteStream.toByteArray();

			response.reset();
			response.setStatus(response.SC_OK);
			response.setContentType("application/vnd.ms-excel;charset=windows-1251");
			response.setHeader("Cache-Control", "max-age = 2592000,must-revalidate");
			response.setHeader("Pragma", "public");
			response.setDateHeader("Last-Modified", System.currentTimeMillis() ); 			 
			response.setContentLength(outArray.length);
			response.setHeader("Content-disposition", "attachment;filename=usersExcel.xls");  

			OutputStream outStream = response.getOutputStream();
			outStream.write(outArray);
			outStream.flush();
			outStream.close();


		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return null;
	}

	class DataObject{
		String etdid;
		String id_pak;
		String vagnum;
		String nameDoc;
		String nameOrganization;
		String repDate;
		public String getEtdid() {
			return etdid;
		}
		public void setEtdid(String etdid) {
			this.etdid = etdid;
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
		public String getNameDoc() {
			return nameDoc;
		}
		public void setNameDoc(String nameDoc) {
			this.nameDoc = nameDoc;
		}
		public String getNameOrganization() {
			return nameOrganization;
		}
		public void setNameOrganization(String nameOrganization) {
			this.nameOrganization = nameOrganization;
		}
		public String getRepDate() {
			return repDate;
		}
		public void setRepDate(String repDate) {
			this.repDate = repDate;
		}
	}
	
}

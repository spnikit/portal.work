package ru.aisa.etdportal.controllers;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.util.CellRangeAddress;
import org.json.JSONException;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.ParameterizedRowMapper;
import org.springframework.web.servlet.ModelAndView;

import ru.aisa.etdadmin.controllers.AbstractMultipartController;
import ru.aisa.rgd.etd.objects.TORArchiveReportObject;
import ru.aisa.rgd.ws.domain.Document;
import ru.aisa.rgd.ws.utility.TypeConverter;

public class ExportTORArchiveController extends AbstractMultipartController {
	
    private NamedParameterJdbcTemplate npjt;
	protected final Logger	log	= Logger.getLogger(getClass());
	
	public NamedParameterJdbcTemplate getNpjt() {
		return npjt;
	}
	public void setNpjt(NamedParameterJdbcTemplate npjt) {
		this.npjt = npjt;
	}

	public ExportTORArchiveController() throws JSONException {
		super();
	}
	
	protected ModelAndView handleRequestInternal(HttpServletRequest request,
			HttpServletResponse response) { 

		String dateFromPeriod = request.getParameter("dateFromPeriod");
		String dateToPeriod = request.getParameter("dateToPeriod");
		String di = request.getParameter("di");
		String vagon = request.getParameter("vagon");
		String packageDoc = request.getParameter("packageDoc");
		String predId = request.getParameter("predid");
		
		String SQL = composeSQL(dateFromPeriod, dateToPeriod, di, vagon, packageDoc, predId);
		HashMap<String, Object> params = composeParams(dateFromPeriod, dateToPeriod, di, vagon, packageDoc, predId);
		
		List<TORArchiveReportObject> TORReportList = getNpjt().query(SQL, params, new Mapper());
		
		try {
			makeExcelReport(TORReportList, response);
		} catch (IOException e) {
			log.error(TypeConverter.exceptionToString(e));
		}
		return null;
	}

	
	
	private String composeSQL (String dateFrom, String dateTo, String di, String vagon, String packageDoc, String predId) {
		
		String sql = "select name_di, name_pred, id_pack, vagon, id_act, repdate, signdate, servicename, " + 
		"cost, summ from snt.archivepackreport where repdate >= :dateFrom and repdate <= :dateTo and id_di in (:id_di) " +
		"and vagon =:vagon and id_pack =:packageDoc and SF is null and predid = :predid";

		if (di.equals("null")) {
			sql = sql.replace("and id_di in (:id_di) ", "");
		}
		if(vagon.equals("null")) {
			sql = sql.replace("and vagon =:vagon ","");
		}
		if(packageDoc.equals("null")) {
			sql = sql.replace("and id_pack =:packageDoc ","");
		}
		return sql;
	}
	
	private HashMap <String, Object> composeParams (String dateFrom, String dateTo, String di_id, String vagon_id, String packageDoc_id, String predId) {
		HashMap<String, Object> params = new HashMap<String, Object> ();
		SimpleDateFormat myDateFormat = new SimpleDateFormat("yyyy-MM-dd");
       	try {
			Date begin = myDateFormat.parse(dateFrom);
			Date end = myDateFormat.parse(dateTo);
		 	params.put("dateFrom", begin);
		 	params.put("dateTo", end);
		} catch (ParseException e) {
			log.error(TypeConverter.exceptionToString(e));
		 	params.put("dateFrom", null);
			params.put("dateTo", null);
		}
       	if (!di_id.equals("null")) {
       		String [] array = di_id.trim().split(",");
       		if (array.length == 1) {
       			params.put("id_di", Long.valueOf(array[0]));
       		}
       		else {
       			List<Long> list = new ArrayList<Long> ();
       			for(int i = 0; i < array.length; i++) {
       				list.add(Long.valueOf(array[i]));
       			}
       			params.put("id_di", list);
       		}
       	}
       	if (!vagon_id.equals("null")) {
       		Long vagon = Long.valueOf(vagon_id);
       		params.put("vagon", vagon);
       	}
       	if (!packageDoc_id.equals("null")) {
       		Long packageDoc = Long.valueOf(packageDoc_id);
       		params.put("packageDoc", packageDoc);
       	}
       	
       	params.put("predid", predId);
       	return params;
	}
	
	private void makeExcelReport (List<TORArchiveReportObject> reportList, HttpServletResponse response) throws IOException {
		HSSFWorkbook workbook = new HSSFWorkbook();
		HSSFSheet sheet = workbook.createSheet("Sample sheet");
		
		final String [] headers = {"№","Наименование ДИ", "Наименование ВЧДЭ","Номер подписанного/согласованного пакета",
				 "Номер вагона, к которому подписан/согласован пакет", "Номер Акта (ФПУ-26)", "Дата ремонта",
				 "Дата подписания пакета", "Наименование услуги", "Стоимость ремонта с НДС,руб","Сумма НДС,руб"};
		
		final String title = "Отчет по согласованным пакетам документов по ТОР, к которым не выставлены счета-фактуры на Портале ЭДО СПС";
		
		sheet.addMergedRegion(new CellRangeAddress(0,0,0,9));
		
		Font font = workbook.createFont();
		font.setBoldweight(Font.BOLDWEIGHT_BOLD);
		
		CellStyle style = workbook.createCellStyle();
  		style.setBorderBottom(HSSFCellStyle.BORDER_THIN);
  		style.setBorderTop(HSSFCellStyle.BORDER_THIN);
  		style.setBorderRight(HSSFCellStyle.BORDER_THIN);
  		style.setBorderLeft(HSSFCellStyle.BORDER_THIN); 
  		style.setAlignment(CellStyle.ALIGN_CENTER);
  		
		CellStyle fontStyle;
		fontStyle = workbook.createCellStyle();
		fontStyle.setFont(font);
		fontStyle.setAlignment(CellStyle.ALIGN_CENTER);
		
		CellStyle headStyle = workbook.createCellStyle();
		headStyle = workbook.createCellStyle();
		headStyle.setAlignment(CellStyle.ALIGN_CENTER);
		headStyle.setBorderRight(HSSFCellStyle.BORDER_MEDIUM);
		headStyle.setBorderLeft(HSSFCellStyle.BORDER_MEDIUM);
		headStyle.setBorderTop(HSSFCellStyle.BORDER_MEDIUM);
		headStyle.setBorderBottom(HSSFCellStyle.BORDER_MEDIUM);
		headStyle.setFont(font);
		
		Row mainHeader = sheet.createRow(0);
		Cell mainCell = mainHeader.createCell(0);
		mainCell.setCellStyle(fontStyle);
		mainCell.setCellValue(title);
//		sheet.autoSizeColumn(1, true);
		
		Row header = sheet.createRow(1);
		for(int i = 0; i < headers.length; i++){
			Cell cell = header.createCell(i);
			cell.setCellValue(headers[i]);
			cell.setCellStyle(headStyle);
		}	
		
		int i = 2;
		for(TORArchiveReportObject tor: reportList) {
			Row workRow = sheet.createRow(i);
			Cell workCell = workRow.createCell(0);
			workCell.setCellStyle(style);
			workCell.setCellValue(i-1);
			workCell = workRow.createCell(1);
			workCell.setCellStyle(style);
			workCell.setCellValue(tor.getName_di().trim());
			workCell = workRow.createCell(2);
			workCell.setCellStyle(style);
			workCell.setCellValue(tor.getName_pred().trim());
			workCell = workRow.createCell(3);
			workCell.setCellStyle(style);
			workCell.setCellValue(tor.getId_pack());
			workCell = workRow.createCell(4);
			workCell.setCellStyle(style);
			workCell.setCellValue(tor.getVagon());
			workCell = workRow.createCell(5);
			workCell.setCellStyle(style);
			workCell.setCellValue(tor.getId_act());
			workCell = workRow.createCell(6);
			workCell.setCellStyle(style);
			workCell.setCellValue(tor.getRepDate().toString());
			workCell = workRow.createCell(7);
			workCell.setCellStyle(style);
			workCell.setCellValue(tor.getSignDate().toString());
			workCell = workRow.createCell(8);
			workCell.setCellStyle(style);
			workCell.setCellValue(tor.getServiceName().trim());
			workCell = workRow.createCell(9);
			workCell.setCellStyle(style);
			if(Double.valueOf(tor.getCost()) != null){
			workCell.setCellValue(tor.getCost());
			}
			workCell = workRow.createCell(10);
			workCell.setCellStyle(style);
			if(Double.valueOf(tor.getSumm()) != null){
			workCell.setCellValue(tor.getSumm());
			}
			i++;
		}
		
		for(int colNum = 0; colNum < sheet.getRow(1).getLastCellNum();colNum++){ 
			sheet.autoSizeColumn(colNum);
		}
		

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
		response.setHeader("Content-disposition", "attachment;filename=torarchiveexcel.xls");  

		OutputStream outStream = response.getOutputStream();
		outStream.write(outArray);
		outStream.flush();
		outStream.close();
		
		
		

	}
	
	class Mapper implements ParameterizedRowMapper<TORArchiveReportObject> {
		public TORArchiveReportObject mapRow (ResultSet rs, int n) throws SQLException {
			TORArchiveReportObject report = new TORArchiveReportObject();
			report.setName_di(rs.getString("NAME_DI"));
			report.setName_pred(rs.getString("NAME_PRED"));
			report.setId_pack(rs.getString("ID_PACK"));
			report.setVagon(rs.getString("vagon"));
			report.setId_act(rs.getString("ID_ACT"));
			report.setRepDate(rs.getDate("REPDATE"));
			report.setSignDate(rs.getDate("SIGNDATE"));
			report.setServiceName(rs.getString("SERVICENAME"));
			report.setCost(rs.getDouble("COST"));
			report.setSumm(rs.getDouble("SUMM"));
			return report;
			
		}
	}
	
	

}

package ru.aisa.etdportal.controllers;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.util.CellRangeAddress;
import org.json.JSONException;
import org.springframework.jdbc.core.simple.ParameterizedRowMapper;
import org.springframework.web.servlet.ModelAndView;

import ru.aisa.etdadmin.controllers.AbstractMultipartController;
import ru.aisa.rgd.etd.objects.RtkReportObject;
import ru.aisa.rgd.ws.utility.TypeConverter;

public class RtkReportController extends AbstractMultipartController{
	
	public RtkReportController() throws JSONException{
		super();
	}
	
	protected ModelAndView handleRequestInternal(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
	    
	    String sql = " with tt as (select id, docid, no, crdate, summ, fio, " + 
	    		"		   short_descr_dep,date_Sign_RTK, date_Sign_DI, date_Sign_CBS, datedrop" + 
	    		"		     from snt.rtkreport  where crdate  >= :createDateBefore and crdate  <= :createDateAfter and " + 
	    		"		       predid  in (select id from snt.pred where headid = :predid or id = :predid))," + 
	    		"		t2 as (select  no, max(id) id, max(predid) predid, max(date_sign_rtk) dateSR from snt.rtkreport " +
	    		" where crdate  >= :createDateBefore and crdate  <= :createDateAfter and predid  in " +
                " (select id from snt.pred where headid = :predid or id = :predid) group by no)" + 
	    		"	         select tt.id, tt.no, tt.crdate, tt.fio, cast(tt.summ as decimal(9,2)) summ, tt.short_descr_dep, " + 
	    		"	         tt.date_sign_rtk, tt.date_sign_di, tt.date_sign_cbs, tt.datedrop from tt, t2 where tt.id = t2.id";
		Map<String, Object> pp = new HashMap<String, Object>();
		String dateFromPeriod = request.getParameter("dateFromPeriod");
		String dateToPeriod = request.getParameter("dateToPeriod");
		String dateFromSign = request.getParameter("dateFromSign");
		String dateToSign = request.getParameter("dateToSign");
		if(!dateFromSign.isEmpty() && !dateToSign.isEmpty()) {
			SimpleDateFormat myDateFormat = new SimpleDateFormat("yyyy-MM-dd");
	       	Date begin = myDateFormat.parse(dateFromSign);
	       	Date end = myDateFormat.parse(dateToSign);
	       	//прибавляем день к конечному дню периода, чтобы документы за этот день вошли в отчет
	       	Calendar c = Calendar.getInstance(); 
	       	c.setTime(end); 
	       	c.add(Calendar.DATE, 1);
	       	end = c.getTime();
			pp.put("dateFromSign", begin);
			pp.put("dateToSign", end);
			sql = sql + " and tt.date_sign_rtk > :dateFromSign and tt.date_sign_rtk < :dateToSign";
		}
		String[] temp = dateFromPeriod.split("-");
		String date1 = temp[2] + "." + temp[1] + "." + temp[0];
		temp = dateToPeriod.split("-");
		String date2 = temp[2] + "." + temp[1] + "." + temp[0];
		
		int predid = Integer.parseInt(request.getParameter("predid"));
		pp.put("predid", predid);
		Integer headIdPred =  getNpjt().queryForObject("select headid from snt.pred where id = :predid", pp, Integer.class);
		if(headIdPred != null) {
			predid = getNpjt().queryForInt("select headid from snt.pred where id = :predid", pp);
			pp.put("predid", headIdPred);
		}
		String title = "Отчет сверки Актов выполненных работ за период "+date1+" - "+date2;
		Map<Integer, Object[]> data = new HashMap<Integer, Object[]>();
		String[] allHeaders = {"№","Номер документа","Дата документа","ФИО","Сумма( руб.)","Подразделение","Дата подписи РТК","Дата подписи ДИ","Дата согласования ЦБС","Дата отклонения"};
		SimpleDateFormat myDateFormat = new SimpleDateFormat("yyyy-MM-dd");
       	Date begin = myDateFormat.parse(dateFromPeriod);
       	Date end = myDateFormat.parse(dateToPeriod);
		pp.put("createDateBefore", begin);
		pp.put("createDateAfter", end);
       
		List<RtkReportObject> rtkObjectList = getNpjt().query(
				sql, pp, new ParameterizedRowMapper<RtkReportObject>() {
					public RtkReportObject mapRow(ResultSet rs, int n)
							throws SQLException {
						SimpleDateFormat myDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
						String temp_date = null;
						RtkReportObject rtkObject = new RtkReportObject();
						if(rs.getTimestamp("date_sign_di") != null) {
							temp_date = myDateFormat.format(rs.getTimestamp("date_sign_di"));
						} else {
							temp_date = "";
						}
						rtkObject.setDateSignatureDi(temp_date);
						
						if(rs.getTimestamp("date_sign_rtk") != null) {
							temp_date = myDateFormat.format(rs.getTimestamp("date_sign_rtk"));
						} else {
							temp_date = "";
						}
						rtkObject.setDateSignatureRtk(temp_date);
						
						if(rs.getTimestamp("date_sign_cbs") != null) {
							temp_date = myDateFormat.format(rs.getTimestamp("date_sign_cbs"));
						} else {
							temp_date = "";
						}
						rtkObject.setDataMatchingCbs(temp_date);
						
						if(rs.getTimestamp("datedrop") != null) {
							temp_date = myDateFormat.format(rs.getTimestamp("datedrop"));
						} else {
							temp_date = "";
						}
						rtkObject.setDateRejection(temp_date);
						rtkObject.setSum(rs.getDouble("summ"));
						rtkObject.setSubdivision(rs.getString("short_descr_dep"));
						rtkObject.setFIO(rs.getString("fio"));
						rtkObject.setDocumentNumber(rs.getString("no"));
						rtkObject.setDocumentDate(rs.getString("crdate"));
						return rtkObject;
					}
				});
				
		data.put(1, allHeaders);
		
		HSSFWorkbook workbook = new HSSFWorkbook();
		HSSFSheet sheet = workbook.createSheet("Sample sheet");

		Font font = workbook.createFont();
		font.setBoldweight(Font.BOLDWEIGHT_BOLD);
		CellStyle style;
		style = workbook.createCellStyle();
		style.setAlignment(CellStyle.ALIGN_CENTER);

		CellStyle style1;
		style1 = workbook.createCellStyle();

		style1.setBorderRight((short)6);
		style1.setBorderLeft((short)6);
		style1.setBorderTop((short)6);
		style1.setBorderBottom((short)6);
		style1.setAlignment(CellStyle.ALIGN_CENTER);

		CellStyle style2;
		style2 = workbook.createCellStyle();
		style2.setAlignment(CellStyle.ALIGN_CENTER);
		style2.setBorderRight(HSSFCellStyle.BORDER_MEDIUM);
		style2.setBorderLeft(HSSFCellStyle.BORDER_MEDIUM);
		style2.setBorderTop(HSSFCellStyle.BORDER_MEDIUM);
		style2.setBorderBottom(HSSFCellStyle.BORDER_MEDIUM);
		
		CellStyle fontStyle;
		fontStyle = workbook.createCellStyle();
		fontStyle.setFont(font);
		fontStyle.setAlignment(CellStyle.ALIGN_CENTER);
		
  		CellStyle style5 = workbook.createCellStyle();
  		style5.setBorderBottom(HSSFCellStyle.BORDER_MEDIUM);
  		style5.setBorderTop(HSSFCellStyle.BORDER_MEDIUM);
  		style5.setBorderRight(HSSFCellStyle.BORDER_MEDIUM);
  		style5.setBorderLeft(HSSFCellStyle.BORDER_MEDIUM); 
  		style5.setAlignment(CellStyle.ALIGN_CENTER);

		sheet.addMergedRegion(new CellRangeAddress(0,0,0,9));
		sheet.addMergedRegion(new CellRangeAddress(1,1,0,9));

		Row header1 = sheet.createRow(0);
		Cell headerCell1 = header1.createCell(0);
		headerCell1.setCellValue(title);
		headerCell1.setCellStyle(fontStyle);		
		
		for(int i = 2; i < 10; i++){
			headerCell1 = header1.createCell(i);
			headerCell1.setCellStyle(style1);	
		}	

		Set<Integer> keyset = data.keySet();
		int rownum = 2;
		for (int key=1;key<=keyset.size(); key++){
		
			Row row = sheet.createRow(rownum++);

			Object [] objArr = data.get(key);
			int cellnum = 1;
			for (Object obj : objArr){
			
				Cell cell = row.createCell(cellnum++);              
				if(key == 1){
					cell.setCellStyle(style2);
				}else{
					cell.setCellStyle(style1);
				}

				if(obj instanceof String)
					cell.setCellValue((String)obj);
				else if(obj instanceof Integer)
					cell.setCellValue((Integer)obj);
			}

		}
		
		int i = rownum++;
        Integer count = 1;
        double sum = 0;
        double sumRTK = 0;
        double sumDI = 0;
        double sumCBS = 0;
        double sumDrop = 0;
		DecimalFormat formatter = new DecimalFormat("#0.00");
		for(RtkReportObject rtk: rtkObjectList) {
			
			sum = sum + rtk.getSum();
//			System.out.println(sum);
			if(!rtk.getDateSignatureDi().isEmpty()) {
				sumDI = sumDI + rtk.getSum();
			}
			if(!rtk.getDateSignatureRtk().isEmpty()) {
				sumRTK = sumRTK + rtk.getSum();
			}
			if(!rtk.getDateRejection().isEmpty()) {
				sumDrop = sumDrop + rtk.getSum();
			}
			if(!rtk.getDataMatchingCbs().isEmpty()) {
				sumCBS = sumCBS + rtk.getSum();
			}
			
			
			 Row tmpRow = sheet.createRow(i);
             Cell tmpCell = tmpRow.createCell(1);
             tmpCell.setCellValue(count);
             tmpCell.setCellStyle(style5);
             tmpCell = tmpRow.createCell(2);
             tmpCell.setCellValue(rtk.getDocumentNumber());
             tmpCell.setCellStyle(style5);
             tmpCell = tmpRow.createCell(3);
             tmpCell.setCellValue(rtk.getDocumentDate());
             tmpCell.setCellStyle(style5);
             tmpCell = tmpRow.createCell(4);
             tmpCell.setCellValue(rtk.getFIO());
             tmpCell.setCellStyle(style5);
             tmpCell = tmpRow.createCell(5);
             tmpCell.setCellValue(formatter.format(rtk.getSum()));
             tmpCell.setCellStyle(style5);
             tmpCell = tmpRow.createCell(6);
             tmpCell.setCellValue(rtk.getSubdivision());
             tmpCell.setCellStyle(style5);
             tmpCell = tmpRow.createCell(7);
             tmpCell.setCellValue(rtk.getDateSignatureRtk());
             tmpCell.setCellStyle(style5);
             tmpCell = tmpRow.createCell(8);
             tmpCell.setCellValue(rtk.getDateSignatureDi());
             tmpCell.setCellStyle(style5);
             tmpCell = tmpRow.createCell(9);
             tmpCell.setCellValue(rtk.getDataMatchingCbs());
             tmpCell.setCellStyle(style5);
             tmpCell = tmpRow.createCell(10);
             tmpCell.setCellValue(rtk.getDateRejection());
             tmpCell.setCellStyle(style5);
             i++;
             count++;
		}
		Row lastRow = sheet.createRow(i);
		Cell tmpCell = lastRow.createCell(4);
		tmpCell.setCellValue("Итого:");
		tmpCell.setCellStyle(style5);
		tmpCell = lastRow.createCell(5);
		tmpCell.setCellValue(formatter.format(sum));
		tmpCell.setCellStyle(style5);
		tmpCell = lastRow.createCell(7);
		tmpCell.setCellValue(formatter.format(sumRTK));
		tmpCell.setCellStyle(style5);
		tmpCell = lastRow.createCell(8);
		tmpCell.setCellValue(formatter.format(sumDI));
		tmpCell.setCellStyle(style5);
		tmpCell = lastRow.createCell(9);
		tmpCell.setCellValue(formatter.format(sumCBS));
		tmpCell.setCellStyle(style5);
		tmpCell = lastRow.createCell(10);
		tmpCell.setCellValue(formatter.format(sumDrop));
		tmpCell.setCellStyle(style5);
        
		for(int colNum = 0; colNum<sheet.getRow(2).getLastCellNum();colNum++){ 
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
			response.setHeader("Content-disposition", "attachment;filename=rtkexcel.xls");  

			OutputStream outStream = response.getOutputStream();
			outStream.write(outArray);
			outStream.flush();
			outStream.close();

		} catch (FileNotFoundException e) {
			log.error(TypeConverter.exceptionToString(e));
		} catch (IOException e) {
			log.error(TypeConverter.exceptionToString(e));
		}
		
		
		
		return null;
	}

}

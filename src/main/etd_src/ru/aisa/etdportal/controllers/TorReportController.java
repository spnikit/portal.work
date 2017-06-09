package ru.aisa.etdportal.controllers;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

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
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.web.servlet.ModelAndView;

import ru.aisa.etdadmin.controllers.AbstractMultipartController;
import ru.aisa.rgd.ws.utility.TypeConverter;

public class TorReportController extends AbstractMultipartController {

	public TorReportController() throws JSONException {
		super();
	}
	
	
	protected ModelAndView handleRequestInternal(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		
		String d1 = request.getParameter("dateFrom");
		String d2 = request.getParameter("dateTo");
		String[] temp = d1.split("-");
		String date1 = temp[2] + "." + temp[1] + "." + temp[0];
		temp = d2.split("-");
		String date2 = temp[2] + "." + temp[1] + "." + temp[0];
		try {
			
	    	HSSFWorkbook my_workbook = new HSSFWorkbook();
	        HSSFSheet sheet = my_workbook.createSheet("Отчет");
	         
	        Font font = my_workbook.createFont();
		  	font.setBoldweight(Font.BOLDWEIGHT_BOLD);
		    font.setFontHeightInPoints((short) 10); 
		    
		    Font font1 = my_workbook.createFont();
		  	font1.setBoldweight(Font.BOLDWEIGHT_BOLD);
		  	
		  	Font font2 = my_workbook.createFont();
		  	font2.setBoldweight(Font.BOLDWEIGHT_BOLD);
		  	font2.setFontHeightInPoints((short) 20); 
	         
	  		CellStyle style = my_workbook.createCellStyle();
	  		style.setBorderBottom(HSSFCellStyle.BORDER_MEDIUM);
	  		style.setBorderTop(HSSFCellStyle.BORDER_MEDIUM);
	  		style.setBorderRight(HSSFCellStyle.BORDER_MEDIUM);
	  		style.setBorderLeft(HSSFCellStyle.BORDER_MEDIUM); 
	  		style.setAlignment(CellStyle.ALIGN_CENTER);
	  		style.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
	  		//style.setFont(font1);
	        
	  		CellStyle style2 = my_workbook.createCellStyle();
	  		style2.setBorderBottom(HSSFCellStyle.BORDER_MEDIUM);
	  		style2.setBorderTop(HSSFCellStyle.BORDER_MEDIUM);
	  		style2.setBorderRight(HSSFCellStyle.BORDER_MEDIUM);
	  		style2.setBorderLeft(HSSFCellStyle.BORDER_MEDIUM);
	  		style2.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
	  		style2.setAlignment(CellStyle.ALIGN_CENTER);
	  		style2.setRotation((short) 90);
	  		style2.setFont(font);
	  		style2.setWrapText(true);
	  		
	  		CellStyle style3 = my_workbook.createCellStyle();
	  		style3.setBorderBottom(HSSFCellStyle.BORDER_MEDIUM);
	  		style3.setBorderLeft(HSSFCellStyle.BORDER_MEDIUM);
	  		
	  		CellStyle style4 = my_workbook.createCellStyle();
	  		style4.setBorderRight(HSSFCellStyle.BORDER_MEDIUM);
	  		
	  		CellStyle style5 = my_workbook.createCellStyle();
	  		style5.setBorderBottom(HSSFCellStyle.BORDER_MEDIUM);
	  		style5.setBorderTop(HSSFCellStyle.BORDER_MEDIUM);
	  		style5.setBorderRight(HSSFCellStyle.BORDER_MEDIUM);
	  		style5.setBorderLeft(HSSFCellStyle.BORDER_MEDIUM); 
	  		style5.setAlignment(CellStyle.ALIGN_CENTER);
	  		style5.setFont(font1);
	  		
	  		CellStyle style6 = my_workbook.createCellStyle();
	  		style6.setFont(font1);
	  		
	  		CellStyle style7 = my_workbook.createCellStyle();
	  		style7.setBorderBottom(HSSFCellStyle.BORDER_MEDIUM);
	  		style7.setBorderTop(HSSFCellStyle.BORDER_MEDIUM);
	  		style7.setBorderRight(HSSFCellStyle.BORDER_MEDIUM);
	  		style7.setBorderLeft(HSSFCellStyle.BORDER_MEDIUM); 
	  		style7.setAlignment(CellStyle.ALIGN_CENTER);
	  		style7.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
	  		style7.setFont(font1);
	  		style7.setWrapText(true);
	  		
	  		CellStyle styleForRow8 = my_workbook.createCellStyle();
	  		styleForRow8.setBorderLeft(HSSFCellStyle.BORDER_MEDIUM); 
	  		styleForRow8.setBorderRight(HSSFCellStyle.BORDER_MEDIUM);
	  		styleForRow8.setAlignment(CellStyle.ALIGN_CENTER);
	  		styleForRow8.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
	  		
	  		
	  		for(int i = 3; i < 35; i ++) {
	  			sheet.setColumnWidth(i, 7000);
	  		}
	  		
	  		sheet.setColumnWidth(18, 8000);
	         
	         //формируем шапку
	  		 Row row = sheet.createRow(0);
	  		 Cell cell1 = row.createCell(33);
	  		 cell1.setCellValue("Приложение 1. Аналитическая справка");
	  		
	         row = sheet.createRow(1);
	         Cell cell = row.createCell(1);
	         cell.setCellValue("Сводная форма отчета по работе в системе ЭДО СПС");
	         cell.setCellStyle(style6);
	         row = sheet.createRow(2);
	         cell = row.createCell(1);
	         cell.setCellValue("за период с " + date1 +" по " + date2);
	         cell.setCellStyle(style6);
	         row = sheet.createRow(5);
	         cell = row.createCell(1);
	         cell.setCellValue("Аналитическая справка");
	         
	         row = sheet.createRow(6);
	         row.setHeight((short)800);
	         cell = row.createCell(1);
	         cell.setCellValue("Плательщик");
	         cell.setCellStyle(style7);
	         
	         cell = row.createCell(2);
	         cell.setCellValue("Филиал");
	         cell.setCellStyle(style7);
	         
	         cell = row.createCell(3);
	         cell.setCellValue("ДИ");
	         cell.setCellStyle(style7);
	         
	         cell = row.createCell(4);
	         cell.setCellValue("Отремонтировано, ваг.");
	         cell.setCellStyle(style7);
	         
	         cell = row.createCell(5);
	         cell.setCellValue("Средний простой \n вагонов, час");
	         cell.setCellStyle(style7);
	            
	         cell = row.createCell(6);
	         cell.setCellValue("Подписан пакет документов в РЖД и \n отправлен на Портал");
	         cell.setCellStyle(style7);
	         
	         cell = row.createCell(7);
	         cell.setCellStyle(style7);
	         
	         cell = row.createCell(9);
	         cell.setCellStyle(style7);
	         
	         cell = row.createCell(11);
	         cell.setCellStyle(style7);
	         
	         cell = row.createCell(13);
	         cell.setCellStyle(style7);
	         
	         cell = row.createCell(15);
	         cell.setCellStyle(style7);
	         
	         for(int i = 23; i < 30; i++) {
	             cell = row.createCell(i);
		         cell.setCellStyle(style7);
	         }
	         
	         cell = row.createCell(32);
	         cell.setCellStyle(style7);
	         
	         Row row8 = sheet.createRow(8);
	         row8.setHeight((short)550);
	         
	         for(int i = 1; i < 35; i++) {
		        cell = row8.createCell(i);
		        cell.setCellStyle(styleForRow8);
		     }
	         
	         cell = row8.createCell(6);
	         cell.setCellValue("ваг.");
	         cell.setCellStyle(style7);
	         
	         cell = row8.createCell(7);
	         cell.setCellValue("% -т от выпуска");
	         cell.setCellStyle(style7);
	         
	         cell = row.createCell(8);
	         cell.setCellValue("Собственник открыл документы на Портале");
	         cell.setCellStyle(style7);
	         
	         cell = row8.createCell(8);
	         cell.setCellValue("ваг.");
	         cell.setCellStyle(style7);
	         
	         cell = row8.createCell(9);
	         cell.setCellValue("% -т от отправленных \nна Портал");
	         cell.setCellStyle(style7);
	         
	         cell = row.createCell(10);
	         cell.setCellValue("Собственник согласовал документы на\n"
	         		+ " Портале(пакет перешел в документы в работе)");
	         cell.setCellStyle(style7);
	         
	         cell = row8.createCell(10);
	         cell.setCellValue("ваг.");
	         cell.setCellStyle(style7);
	         
	         cell = row8.createCell(11);
	         cell.setCellValue("% -т от отправленных \nна Портал");
	         cell.setCellStyle(style7);
	         
	         cell = row.createCell(12);
	         cell.setCellValue("В работе по замечаниям");
	         cell.setCellStyle(style7);
	         
	         cell = row8.createCell(12);
	         cell.setCellValue("ваг.");
	         cell.setCellStyle(style7);
	         
	         cell = row8.createCell(13);
	         cell.setCellValue("% - т от отправленных \nна Портал");
	         cell.setCellStyle(style7);
	         
	         cell = row.createCell(14);
	         cell.setCellValue("Подписано");
	         cell.setCellStyle(style7);
	         
	         cell = row8.createCell(14);
	         cell.setCellValue("ваг.");
	         cell.setCellStyle(style7);
	         
	         cell = row8.createCell(15);
	         cell.setCellValue("% -т от отправленных \nна Портал");
	         cell.setCellStyle(style7);
	         
	         cell = row.createCell(16);
	         cell.setCellValue("Выставл. СФ");
	         cell.setCellStyle(style7);
	         
	         cell = row.createCell(17);
	         cell.setCellValue("Выставл. ТОРГ-12");
	         cell.setCellStyle(style7);
	         
	         cell = row.createCell(18);
	         cell.setCellValue("Среднее время по \n"
	         		+ "формированию полного \n"
	         		+ "пакета документов от РЖД, час.\n"
	         		+ "(от ВУ-36 до \nотправки на Портал)");
	         cell.setCellStyle(style7);
	         
	         cell = row.createCell(19);
	         cell.setCellValue("Среднее время по\n исправлению пакета");
	         cell.setCellStyle(style7);
	         
	         cell = row.createCell(20);
	         cell.setCellValue("Среднее время по \n"
	         		+ "оформлению пакета \n"
	         		+ "документов на Портале \n"
	         		+ "до подписания");
	         cell.setCellStyle(style7);
	         
	         cell = row.createCell(21);
	         cell.setCellValue("Среднее \n"
	         		+ " количество раз по \n "
	         		+ "отклонению пакета \n "
	         		+ "документов собственником");
	         cell.setCellStyle(style7);
	         
	        cell = row.createCell(22);
	        cell.setCellValue("Среднее количество подписанных документов");
	        cell.setCellStyle(style7);
	        
	        Row row7 = sheet.createRow(7);
	        
	        for(int i = 1; i < 35; i++) {
		        cell = row7.createCell(i);
		        cell.setCellStyle(styleForRow8);
	        }	
	        
	        cell = row7.createCell(22);
	        cell.setCellValue("с 1-го раза");
	        cell.setCellStyle(style7);
	        
	        cell = row7.createCell(24);
	        cell.setCellValue("со 2-го раза");
	        cell.setCellStyle(style7);
	        
	        cell = row7.createCell(26);
	        cell.setCellValue("с 3-го раза");
	        cell.setCellStyle(style7);
	        
	        cell = row7.createCell(28);
	        cell.setCellValue("более 3-х раз");
	        cell.setCellStyle(style7);
	        
	        cell = row8.createCell(22);
	        cell.setCellValue("кол-во ваг");
	        cell.setCellStyle(style7);
	        
	        cell = row8.createCell(23);
	        cell.setCellValue("%-т от подписанных \n на Портале");
	        cell.setCellStyle(style7);
	        
	        cell = row8.createCell(24);
	        cell.setCellValue("кол-во ваг");
	        cell.setCellStyle(style7);
	        
	        cell = row8.createCell(25);
	        cell.setCellValue("%-т от подписанных \n на Портале");
	        cell.setCellStyle(style7);
	        
	        cell = row8.createCell(26);
	        cell.setCellValue("кол-во ваг");
	        cell.setCellStyle(style7);
	        
	        cell = row8.createCell(27);
	        cell.setCellValue("%-т от подписанных \n на Портале");
	        cell.setCellStyle(style7);
	        
	        cell = row8.createCell(28);
	        cell.setCellValue("кол-во ваг");
	        cell.setCellStyle(style7);
	        
	        cell = row8.createCell(29);
	        cell.setCellValue("%-т от подписанных \n на Портале");
	        cell.setCellStyle(style7);
	        
	        cell = row.createCell(30);
	        cell.setCellValue("Средняя стоимость \n ремонта, руб.");
	        cell.setCellStyle(style7);
	        
	        cell = row.createCell(31);
	        cell.setCellValue("Количество эксплуатационных неисправностей");
	        cell.setCellStyle(style7);
	        
	        cell = row8.createCell(31);
	        cell.setCellValue("ваг.");
	        cell.setCellStyle(style7);
	        
	        cell = row8.createCell(32);
	        cell.setCellValue("% -т от отправленных \n на Портал");
	        cell.setCellStyle(style7);
	        
	        cell = row.createCell(33);
	        cell.setCellValue("Количество технологических неисправностей");
	        cell.setCellStyle(style7);
	        
	         cell = row.createCell(34);
		     cell.setCellStyle(style7);
	        
	        cell = row8.createCell(33);
	        cell.setCellValue("ваг.");
	        cell.setCellStyle(style7);
	        
	        cell = row8.createCell(34);
	        cell.setCellValue("% -т от отправленных \n на Портал");
	        cell.setCellStyle(style7);
	         
	         sheet.addMergedRegion(new CellRangeAddress(6, 8, 1, 1 ));
	         
	         sheet.addMergedRegion(new CellRangeAddress(6, 8, 2, 2 ));
	         
	         sheet.addMergedRegion(new CellRangeAddress(6, 8, 3, 3 ));
	         
	         sheet.addMergedRegion(new CellRangeAddress(6, 8, 4, 4 ));
	         
	         sheet.addMergedRegion(new CellRangeAddress(6, 8, 5, 5 ));
	         
	         sheet.addMergedRegion(new CellRangeAddress(6, 7, 6, 7 ));
	         
	         sheet.addMergedRegion(new CellRangeAddress(6, 7, 8, 9 ));
	         
	         sheet.addMergedRegion(new CellRangeAddress(6, 7, 10, 11 ));
	         
	         sheet.addMergedRegion(new CellRangeAddress(6, 7, 12, 13 ));
	         
             sheet.addMergedRegion(new CellRangeAddress(6, 7, 14, 15 ));
	         
	         sheet.addMergedRegion(new CellRangeAddress(6, 8, 16, 16 ));
	         
	         sheet.addMergedRegion(new CellRangeAddress(6, 8, 17, 17 ));
	         
	         sheet.addMergedRegion(new CellRangeAddress(6, 8, 18, 18 ));
	         
	         sheet.addMergedRegion(new CellRangeAddress(6, 8, 19, 19 ));
	         
	         sheet.addMergedRegion(new CellRangeAddress(6, 8, 20, 20 ));
	         
	         sheet.addMergedRegion(new CellRangeAddress(6, 8, 21, 21));
	         
	         sheet.addMergedRegion(new CellRangeAddress(6, 6, 22, 29 ));
	         
	         sheet.addMergedRegion(new CellRangeAddress(7, 7, 22, 23 ));
	         
	         sheet.addMergedRegion(new CellRangeAddress(7, 7, 24, 25 ));
	         
	         sheet.addMergedRegion(new CellRangeAddress(7, 7, 26, 27 ));
	         
	         sheet.addMergedRegion(new CellRangeAddress(7, 7, 28, 29));
	         
	         sheet.addMergedRegion(new CellRangeAddress(6, 8, 30, 30 ));
	         
	         sheet.addMergedRegion(new CellRangeAddress(6, 7, 31, 32 ));
	         
	         sheet.addMergedRegion(new CellRangeAddress(6, 7, 33, 34 ));
	         
	         cell = row.createCell(34);
		     cell.setCellStyle(style7);
	         
	         Row row9 =  sheet.createRow(9);
	         
	         //заполнение 9 строки excel
	         for(int i = 1; i < 10; i++) {
	         	Cell cl = row9.createCell(i);
	         	cl.setCellValue(i);
	         	cl.setCellStyle(style7);
	         }
	         Cell cl = row9.createCell(10);
	         cl.setCellValue("8a");
	         cl.setCellStyle(style7);
	         cl = row9.createCell(11);
	         cl.setCellValue("9a");
	         cl.setCellStyle(style7);
	         
	         for(int i = 12; i < 19; i++) {
		         cl = row9.createCell(i);
		         cl.setCellValue(i-2);
		         cl.setCellStyle(style7);
		     }
	         cl = row9.createCell(19);
	         cl.setCellValue("16a");
	         cl.setCellStyle(style7);
	         
	         for(int i = 20; i < 35; i++) {
		         cl = row9.createCell(i);
		         cl.setCellValue(i-3);
		         cl.setCellStyle(style7);
		     }

	        String select = "with main as (select distinct(kleim) kleim, \n" + 
	        		"        (select pid from snt.pred_fil where kid =rp.kleim and (hid = :hid or pid =:hid)),\n" + 
	        		"        (select rtrim(vname) vname from snt.pred \n" + 
	        		"        where id = (select pid from snt.pred_fil where kid =rp.kleim and (hid = :hid or pid = :hid))),\n" + 
	        		"        (select dorid from snt.rem_pred where kleim = rp.kleim),\n" + 
	        		"        (select sname from snt.dor where id = (select dorid from snt.rem_pred where kleim = rp.kleim))\n" + 
	        		"        from snt.rem_pred rp),\n" + 
	        		"       t2 as (select vagon, rep_date, kleim, count(0) cnt from snt.pgkreport p,main \n" + 
	        		"          where p.service_type = '02' and cast(rep_date as date) between :begin and :end\n" + 
	        		"               and p.kleymo = main.kleim and p.predid = main.pid \n" + 
	        		"               group by vagon, rep_date, kleim),\n" + 
	        		"       t3 as (select wait_repair, kleim from snt.pgkreport p, main \n" + 
	        		"                where p.service_type = '02' and p.kleymo = main.kleim and p.predid = main.pid and wait_repair is not null \n" + 
	        		"                and cast(rep_date as date) between :begin and :end),\n" + 
	        		"       DT_cr_pak as (select vagon, rep_date, kleim, count(0) cnt from snt.pgkreport p,main \n" + 
	        		"           where p.service_type = '02' and p.DT_create_pak is not null \n" + 
	        		"               and cast(rep_date as date) between :begin and :end\n" + 
	        		"               and p.kleymo = main.kleim and p.predid = main.pid \n" + 
	        		"               group by vagon, rep_date, kleim),         \n" + 
	        		"       DT_op_pak as (select vagon, rep_date, kleim, count(0) cnt from snt.pgkreport p,main \n" + 
	        		"           where p.service_type = '02' and p.DT_create_pak is not null and p.DT_open_pak is not null and p.DT_drop_pak is null \n" + 
	        		"               and p.DT_sign_pak is null and p.DT_ACCEPT_PAK is null \n" + 
	        		"               and cast(rep_date as date) between :begin and :end\n" + 
	        		"               and p.kleymo = main.kleim and p.predid = main.pid \n" + 
	        		"               group by vagon, rep_date, kleim),    \n" + 
	        		"     DT_acc_pak as (select vagon, rep_date, kleim, count(0) cnt from snt.pgkreport p,main \n" + 
	        		"           where p.service_type = '02' and p.DT_ACCEPT_PAK is not null and p.DT_drop_pak is null and p.DT_sign_pak is null \n" + 
	        		"               and cast(rep_date as date) between :begin and :end\n" + 
	        		"               and p.kleymo = main.kleim and p.predid = main.pid \n" + 
	        		"               group by vagon, rep_date, kleim), \n" + 
	        		"      DT_dr_pak as (select vagon, rep_date, kleim, count(0) cnt from snt.pgkreport p,main \n" + 
	        		"           where p.service_type = '02' and p.DT_drop_pak is not null\n" + 
	        		"               and cast(rep_date as date) between :begin and :end\n" + 
	        		"               and p.kleymo = main.kleim and p.predid = main.pid \n" + 
	        		"               group by vagon, rep_date, kleim),  \n" + 
	        		"      DT_sg_pak as (select vagon, rep_date, kleim, count(0) cnt from snt.pgkreport p,main \n" + 
	        		"           where p.service_type = '02' and p.DT_sign_pak is not null\n" + 
	        		"               and cast(rep_date as date) between :begin and :end\n" + 
	        		"               and p.kleymo = main.kleim and p.predid = main.pid \n" + 
	        		"               group by vagon, rep_date, kleim),  \n" + 
	        		"      DT_cr_sf as (select vagon, rep_date, kleim, count(0) cnt from snt.pgkreport p,main \n" + 
	        		"           where p.service_type = '02' and p.DT_create_sf is not null \n" + 
	        		"               and cast(rep_date as date) between :begin and :end\n" + 
	        		"               and p.kleymo = main.kleim and p.predid = main.pid \n" + 
	        		"               group by vagon, rep_date, kleim),  \n" + 
	        		"      DT_cr_torg12 as (select vagon, rep_date, kleim, count(0) cnt from snt.pgkreport p,main \n" + 
	        		"           where p.service_type = '02' and p.DT_create_torg12 is not null\n" + 
	        		"               and cast(rep_date as date) between :begin and :end\n" + 
	        		"               and p.kleymo = main.kleim and p.predid = main.pid \n" + 
	        		"               group by vagon, rep_date, kleim), \n" + 
	        		"      wait_cr_pak as (select wait_create_pak, kleim from snt.pgkreport p, main \n" + 
	        		"                where p.service_type = '02' and p.kleymo = main.kleim and p.predid = main.pid \n" + 
	        		"                and wait_create_pak is not null\n" + 
	        		"                and cast(rep_date as date) between :begin and :end),\n" + 
	        		"      wait_corr_pak as (select WAIT_CORRECT_PAK, kleim from snt.pgkreport p, main \n" + 
	        		"                where p.service_type = '02' and p.kleymo = main.kleim and p.predid = main.pid \n" + 
	        		"                and WAIT_CORRECT_PAK is not null\n" + 
	        		"                and cast(rep_date as date) between :begin and :end),\n" + 
	        		"      wait_sg_pak as (select wait_sign_pak, kleim from snt.pgkreport p, main \n" + 
	        		"                where p.service_type = '02' and p.kleymo = main.kleim and p.predid = main.pid \n" + 
	        		"                and wait_sign_pak is not null\n" + 
	        		"                and cast(rep_date as date) between :begin and :end), \n" + 
	        		"    avg_drop_pak as (select vagon, rep_date, max(count_drop) * 1.00 cd, kleim from snt.pgkreport p, main \n" + 
	        		"                where p.service_type = '02' and p.kleymo = main.kleim and p.predid = main.pid \n" + 
	        		"                and count_drop is not null\n" + 
	        		"                and cast(rep_date as date) between :begin and :end\n" + 
	        		"                group by vagon, rep_date, kleim), \n" + 
	        		"      DT_sg_pak_iter0 as (select vagon, rep_date, kleim, count(0) cnt from snt.pgkreport p,main \n" + 
	        		"          where p.service_type = '02' and DT_sign_pak is not null\n" + 
	        		"               and cast(rep_date as date) between :begin and :end\n" + 
	        		"               and p.kleymo = main.kleim and count_drop = 0 and p.predid = main.pid \n" + 
	        		"                  group by vagon, rep_date, kleim),  \n" + 
	        		"      DT_sg_pak_iter1 as (select vagon, rep_date, kleim, count(0) cnt from snt.pgkreport p,main \n" + 
	        		"          where p.service_type = '02' and DT_sign_pak is not null\n" + 
	        		"               and cast(rep_date as date) between :begin and :end\n" + 
	        		"               and p.kleymo = main.kleim and count_drop = 1 and p.predid = main.pid \n" + 
	        		"                  group by vagon, rep_date, kleim),  \n" + 
	        		"      DT_sg_pak_iter2 as (select vagon, rep_date, kleim, count(0) cnt from snt.pgkreport p,main \n" + 
	        		"         where p.service_type = '02' and DT_sign_pak is not null\n" + 
	        		"               and cast(rep_date as date) between :begin and :end\n" + 
	        		"               and p.kleymo = main.kleim and count_drop = 2 and p.predid = main.pid \n" + 
	        		"                  group by vagon, rep_date, kleim),    \n" + 
	        		"      DT_sg_pak_iter_more_2 as (select vagon, rep_date, kleim, count(0) cnt from snt.pgkreport p,main \n" + 
	        		"         where p.service_type = '02' and DT_sign_pak is not null\n" + 
	        		"               and cast(rep_date as date) between :begin and :end\n" + 
	        		"               and p.kleymo = main.kleim and count_drop > 2 and p.predid = main.pid \n" + 
	        		"               group by vagon, rep_date, kleim),         \n" + 
	        		"      avg_price as (select vagon, rep_date, max(price) price, kleim from snt.pgkreport p, main \n" + 
	        		"                where p.service_type = '02' and p.kleymo = main.kleim and p.predid = main.pid  and price is not null\n" + 
	        		"                and cast(rep_date as date) between :begin and :end\n" + 
	        		"                group by vagon, rep_date, kleim), \n" + 
	        		"      avg_count as (\n" + 
	        		"        select kleim, vagon, rep_date, count(0) coun from snt.pgkreport p, main \n" + 
	        		"               where p.service_type = '02' and p.kleymo = main.kleim and p.predid = main.pid  and price is not null\n" + 
	        		"               and cast(rep_date as date) between :begin and :end\n" + 
	        		"               group by kleim, vagon, rep_date),\n" + 
	        		"      defect_1 as (select vagon, rep_date, kleim, count(0) cnt from snt.pgkreport p,main \n" + 
	        		"         		where p.service_type = '02' and DT_sign_pak is not null\n" + 
	        		"               and cast(rep_date as date) between :begin and :end\n" + 
	        		"               and p.kleymo = main.kleim and defect = 1 and p.predid = main.pid \n" + 
	        		"               group by vagon, rep_date, kleim),     \n" + 
	        		"      defect_0 as (select vagon, rep_date, kleim, count(0) cnt from snt.pgkreport p,main \n" + 
	        		"         		where p.service_type = '02' and DT_sign_pak is not null\n" + 
	        		"               and cast(rep_date as date) between :begin and :end \n" + 
	        		"               and p.kleymo = main.kleim and defect = 0 and p.predid = main.pid \n" + 
	        		"                  group by vagon, rep_date, kleim),\n" + 
	        		"       avg_count_cr_pak as (\n" + 
	        		"        select kleim, count(0) count_cr_pak from snt.pgkreport p, main \n" + 
	        		"                where p.service_type = '02' and p.kleymo = main.kleim and p.predid = main.pid  and wait_create_pak is not null\n" + 
	        		"               and cast(rep_date as date) between :begin and :end\n" + 
	        		"               group by kleim\n" + 
	        		"             ),\n" + 
	        		"       avg_count_wait_rep as (\n" + 
	        		"        select kleim, count(0) count_wait_rep from snt.pgkreport p, main \n" + 
	        		"                where p.service_type = '02' and p.kleymo = main.kleim and p.predid = main.pid  and wait_repair is not null \n" + 
	        		"               and cast(rep_date as date) between :begin and :end\n" + 
	        		"               group by kleim\n" + 
	        		"             ),    \n" + 
	        		"       avg_count_wait_corr as (\n" + 
	        		"        select kleim, count(0) count_wait_corr from snt.pgkreport p, main \n" + 
	        		"                where p.service_type = '02' and p.kleymo = main.kleim and p.predid = main.pid  and WAIT_CORRECT_PAK is not null\n" + 
	        		"               and cast(rep_date as date) between :begin and :end\n" + 
	        		"               group by kleim\n" + 
	        		"             ),  \n" + 
	        		"       avg_count_wait_sign as (\n" + 
	        		"        select kleim, count(0) count_wait_sign from snt.pgkreport p, main \n" + 
	        		"                where p.service_type = '02' and p.kleymo = main.kleim and p.predid = main.pid  and wait_sign_pak is not null\n" + 
	        		"               and cast(rep_date as date) between :begin and :end\n" + 
	        		"               group by kleim\n" + 
	        		"             ),    \n" + 
	        		"       avg_count_drop_pak as (\n" + 
	        		"                select kleim, vagon, rep_date, count(0) count_drop_pak from snt.pgkreport p, main \n" + 
	        		"                        where p.service_type = '02' and p.kleymo = main.kleim and p.predid = main.pid  and count_drop is not null\n" + 
	        		"                       and cast(rep_date as date) between :begin and :end \n" + 
	        		"                       group by kleim, vagon, rep_date\n" + 
	        		"                     ),           \n" + 
	        		"       t4 as (select kleim, count(0) p4 from t2 group by t2.kleim),\n" + 
	        		"       t5 as (select kleim, sum(wait_repair) p5 from t3 group by t3.kleim),\n" + 
	        		"       t6 as (select kleim, count(0) p6 from DT_cr_pak group by DT_cr_pak.kleim),\n" + 
	        		"       t8 as (select kleim, count(0) p8 from DT_op_pak group by DT_op_pak.kleim),\n" + 
	        		"       t8a as (select kleim, count(0) p8a from DT_acc_pak group by DT_acc_pak.kleim),\n" + 
	        		"       t10 as (select kleim, count(0) p10 from DT_dr_pak group by DT_dr_pak.kleim),\n" + 
	        		"       t12 as (select kleim, count(0) p12 from DT_sg_pak group by DT_sg_pak.kleim),\n" + 
	        		"       t14 as (select kleim, count(0) p14 from DT_cr_sf group by DT_cr_sf.kleim),\n" + 
	        		"       t15 as (select kleim, count(0) p15 from DT_cr_torg12 group by DT_cr_torg12.kleim),\n" + 
	        		"       t16 as (select kleim, sum(wait_create_pak) p16 from wait_cr_pak group by wait_cr_pak.kleim),\n" + 
	        		"       t16a as (select kleim, sum(WAIT_CORRECT_PAK) p16a from wait_corr_pak group by wait_corr_pak.kleim),\n" + 
	        		"       t17 as (select kleim, sum(wait_sign_pak) p17 from wait_sg_pak group by wait_sg_pak.kleim),\n" + 
	        		"       t18 as (select kleim, cast(sum(cd) as decimal(9,2))p18 from avg_drop_pak group by avg_drop_pak.kleim),\n" + 
	        		"       t19 as (select kleim, count(0) p19 from DT_sg_pak_iter0 group by DT_sg_pak_iter0.kleim),\n" + 
	        		"       t21 as (select kleim, count(0) p21 from DT_sg_pak_iter1 group by DT_sg_pak_iter1.kleim),\n" + 
	        		"       t23 as (select kleim, count(0) p23 from DT_sg_pak_iter2 group by DT_sg_pak_iter2.kleim),\n" + 
	        		"       t25 as (select kleim, count(0) p25 from DT_sg_pak_iter_more_2 group by DT_sg_pak_iter_more_2.kleim),\n" + 
	        		"       t27 as (select kleim, cast(sum(cast(price as decimal(9,2))) as decimal(9,2))  p27 from avg_price group by avg_price.kleim),\n" + 
	        		"       t28 as (select kleim, count(0) p28 from defect_1 group by defect_1.kleim),\n" + 
	        		"       t30 as (select kleim, count(0) p30 from defect_0 group by defect_0.kleim),\n" + 
	        		"       t32 as (select kleim, count(coun) p32 from avg_count group by avg_count.kleim),\n" + 
	        		"       t33 as (select kleim, count_cr_pak p33 from avg_count_cr_pak),\n" + 
	        		"       t34 as (select kleim, count_wait_rep p34 from avg_count_wait_rep),\n" + 
	        		"       t35 as (select kleim, count_wait_corr p35 from avg_count_wait_corr),\n" + 
	        		"       t36 as (select kleim, count_wait_sign p36 from avg_count_wait_sign),\n" + 
	        		"       t37 as (select kleim, count(count_drop_pak) p37 from avg_count_drop_pak group by avg_count_drop_pak.kleim)\n" + 
	        		"      select  vname, dorid, dname, p4, cast((p5*1.00)/p4 as decimal(9,2)) p5, p6,     \n" + 
	        		"      case \n" + 
	        		"      WHEN p4 is null THEN 0 \n" + 
	        		"      WHEN p4 = 0 THEN 0 \n" + 
	        		"      else cast((p6*100.00)/p4 as decimal(9,2))\n" + 
	        		"      end p7, \n" + 
	        		"      p8, \n" + 
	        		"      p8a, \n" + 
	        		"      case p6\n" + 
	        		"      WHEN 0 THEN 0 \n" + 
	        		"      else cast((p8*100.00)/p6 as decimal(9,2))\n" + 
	        		"      end p9,\n" + 
	        		"      case p6\n" + 
	        		"      WHEN 0 THEN 0 \n" + 
	        		"      else cast((p8a*100.00)/p6 as decimal(9,2))\n" + 
	        		"      end p9a,\n" + 
	        		"      p10,\n" + 
	        		"      case p6\n" + 
	        		"      WHEN 0 THEN 0 \n" + 
	        		"      else cast((p10*100.00)/p6 as decimal(9,2))\n" + 
	        		"      end p11, \n" + 
	        		"      p12, \n" + 
	        		"      case p6\n" + 
	        		"      WHEN 0 THEN 0 \n" + 
	        		"      else cast((p12*100.00)/p6 as decimal(9,2))\n" + 
	        		"      end p13,\n" + 
	        		"      p14, p15, cast((p16*1.00)/p33 as decimal(9,1)) p16, \n" + 
	        		"      cast((p16a*1.0)/p35 as decimal(9,1)) p16a, cast((p17*1.0)/p36 as decimal(9,1)) p17, \n" + 
	        		"      cast((p18*0.1)/p37 as decimal(9,1)) p18, p19, \n" + 
	        		"      case p12\n" + 
	        		"      WHEN 0 THEN 0 \n" + 
	        		"      else cast((p19*100.0)/p12 as decimal(9,1))\n" + 
	        		"      end p20, \n" + 
	        		"      p21, \n" + 
	        		"      case p12\n" + 
	        		"      WHEN 0 THEN 0 \n" + 
	        		"      else cast((p21*100.0)/p12 as decimal(9,1))\n" + 
	        		"      end p22,\n" + 
	        		"      p23, \n" + 
	        		"      case p12\n" + 
	        		"      WHEN 0 THEN 0 \n" + 
	        		"      else cast((p23*100.0)/p12 as decimal(9,1))\n" + 
	        		"      end p24,\n" + 
	        		"      p25, \n" + 
	        		"      case p12\n" + 
	        		"      WHEN 0 THEN 0 \n" + 
	        		"      else cast((p25*100.0)/p12 as decimal(9,1))\n" + 
	        		"      end p26,\n" + 
	        		"      p27/p32 p27, p28,\n" + 
	        		"      case p6\n" + 
	        		"      WHEN 0 THEN 0 \n" + 
	        		"      else cast((p28*100.00)/p6 as decimal(9,2))\n" + 
	        		"      end p29,\n" + 
	        		"      p30,\n" + 
	        		"      case p6\n" + 
	        		"      WHEN 0 THEN 0 \n" + 
	        		"      else cast((p30*100.00)/p6 as decimal(9,2))\n" + 
	        		"      end p31,\n" + 
	        		"      p32, p34, p36, p37, p33\n" + 
	        		"      from (           \n" + 
	        		"              select  main.vname, dorid, rtrim(main.sname) dname, sum(p4) p4, sum(p5) p5, sum(p6) p6, sum(p8) p8, sum(p8a) p8a, sum(p10) p10, \n" + 
	        		"              sum(p12) p12, sum(p14) p14, sum(p15) p15, sum(p16) p16, sum(p16a) p16a, \n" + 
	        		"              sum(p17) p17, sum(p18) p18, sum(p19) p19, sum(p21) p21, sum(p23) p23,\n" + 
	        		"              sum(p25) p25, sum(p27) p27, sum(p28) p28, sum(p30) p30, sum(p32) p32, sum(p33) p33, sum(p34) p34,\n" + 
	        		"              sum(p35) p35, sum(p36) p36, sum(p37) p37\n" + 
	        		"              from main \n" + 
	        		"                        left outer join t4 a1 on main.kleim = a1.kleim \n" + 
	        		"                        left outer join t5 a2 on main.kleim = a2.kleim\n" + 
	        		"                        left outer join t6 a3 on main.kleim = a3.kleim\n" + 
	        		"                        left outer join t8 a4 on main.kleim = a4.kleim\n" + 
	        		"                        left outer join t8a a8a on main.kleim = a8a.kleim\n" + 
	        		"                        left outer join t10 a5 on main.kleim = a5.kleim\n" + 
	        		"                        left outer join t12 a6 on main.kleim = a6.kleim\n" + 
	        		"                        left outer join t14 a7 on main.kleim = a7.kleim\n" + 
	        		"                        left outer join t15 a8 on main.kleim = a8.kleim\n" + 
	        		"                        left outer join t16 a9 on main.kleim = a9.kleim\n" + 
	        		"                        left outer join t16a a16a on main.kleim = a16a.kleim\n" + 
	        		"                        left outer join t17 a10 on main.kleim = a10.kleim\n" + 
	        		"                        left outer join t18 a18 on main.kleim = a18.kleim\n" + 
	        		"                        left outer join t19 a11 on main.kleim = a11.kleim\n" + 
	        		"                        left outer join t21 a12 on main.kleim = a12.kleim\n" + 
	        		"                        left outer join t23 a13 on main.kleim = a13.kleim\n" + 
	        		"                        left outer join t25 a14 on main.kleim = a14.kleim\n" + 
	        		"                        left outer join t27 a15 on main.kleim = a15.kleim  \n" + 
	        		"                        left outer join t28 a16 on main.kleim = a16.kleim\n" + 
	        		"                        left outer join t30 a17 on main.kleim = a17.kleim \n" + 
	        		"                        left outer join t32 a180 on main.kleim = a180.kleim \n" + 
	        		"                        left outer join t33 a181 on main.kleim = a181.kleim \n" + 
	        		"                        left outer join t34 a182 on main.kleim = a182.kleim \n" + 
	        		"                        left outer join t35 a183 on main.kleim = a183.kleim \n" + 
	        		"                        left outer join t36 a184 on main.kleim = a184.kleim \n" + 
	        		"                        left outer join t37 a185 on main.kleim = a185.kleim \n" + 
	        		"                        group by main.vname, dorid, main.sname\n" + 
	        		"                      ) asd order by dorid with ur";
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("id", Integer.parseInt(request.getParameter("predid")));
			Integer headIdPred =  getNpjt().queryForObject("select headid from snt.pred where id = :id", map, Integer.class);
			String predName;
        	int headId;
			if(headIdPred == null) {
				headId = Integer.parseInt(request.getParameter("predid"));
				predName = getNpjt().queryForObject("select name from snt.pred where id = :id", map, String.class);
			} else {
				headId = getNpjt().queryForInt("select headid from snt.pred where id = :id", map);
				map.put("headid", headIdPred);
				predName = getNpjt().queryForObject("select name from snt.pred where id = :headid", map, String.class);
			}
			SimpleDateFormat myDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        	Date begin = myDateFormat.parse(d1);
        	Date end = myDateFormat.parse(d2);
        	map.put("begin", begin);
        	map.put("end", end);
        	map.put("hid", headId);
        	map.put("pid", headId);
	        SqlRowSet rs = getNpjt().queryForRowSet(select, map); 
	        int sumP4 = 0;
	        double sumP5 = 0;
	        int sumP6 = 0;
	        double sumP7 = 0;
	        int sumP8 = 0;
	        int sumP8a = 0;
	        double sumP9 = 0;
	        double sumP9a = 0;
	        int sumP10 = 0;
	        double sumP11 = 0;
	        int sumP12 = 0;
	        double sumP13 = 0;
	        int sumP14 = 0;
	        int sumP15 = 0;
	        double sumP16 = 0;
	        double sumP16a = 0;
	        double sumP17 = 0;
	        double sumP18 = 0;
	        int sumP19 = 0;
	        double sumP20 = 0;
	        int sumP21 = 0;
	        double sumP22 = 0;
	        int sumP23 = 0;
	        double sumP24 = 0;
	        int sumP25 = 0;
	        double sumP26 = 0;
	        double sumP27 = 0;
	        int sumP28 = 0;
	        double sumP29 = 0;
	        int sumP30 = 0;
	        double sumP31 = 0;
	        
	        int i = 10;
	        int count = 0;
			while(rs.next()) {
              Row tmpRow = sheet.createRow(i);
              Cell tmpCell = tmpRow.createCell(1);
              tmpCell.setCellStyle(style);
              tmpCell = tmpRow.createCell(2);
              tmpCell.setCellValue(rs.getString("vname"));
              tmpCell.setCellStyle(style);
              tmpCell = tmpRow.createCell(3);
              tmpCell.setCellValue(rs.getString("dname"));
              tmpCell.setCellStyle(style);
              tmpCell = tmpRow.createCell(4);
              sumP4 = sumP4 + rs.getInt("p4");
              tmpCell.setCellValue(rs.getInt("p4"));
              tmpCell.setCellStyle(style);
              tmpCell = tmpRow.createCell(5);
              sumP5 = sumP5 + rs.getDouble("p5");
              tmpCell.setCellValue(rs.getDouble("p5"));
              tmpCell.setCellStyle(style);
              sumP6 = sumP6 + rs.getInt("p6");
              tmpCell = tmpRow.createCell(6);
              tmpCell.setCellValue(rs.getInt("p6"));
              tmpCell.setCellStyle(style);
              sumP7 = sumP7 + rs.getDouble("p7");
              tmpCell = tmpRow.createCell(7);
              tmpCell.setCellValue(rs.getDouble("p7"));
              tmpCell.setCellStyle(style);
              tmpCell = tmpRow.createCell(8);
              sumP8 = sumP8 + rs.getInt("p8");
              tmpCell.setCellValue(rs.getInt("p8"));
              tmpCell.setCellStyle(style);
              sumP9 = sumP9 + rs.getDouble("p9");
              tmpCell = tmpRow.createCell(9);
              tmpCell.setCellValue(rs.getDouble("p9"));
              tmpCell.setCellStyle(style);
              tmpCell = tmpRow.createCell(10);
              sumP8a = sumP8a + rs.getInt("p8a");
              tmpCell.setCellValue(rs.getInt("p8a"));
              tmpCell.setCellStyle(style);
              sumP9a = sumP9a + rs.getDouble("p9a");
              tmpCell = tmpRow.createCell(11);
              tmpCell.setCellValue(rs.getDouble("p9a"));
              tmpCell.setCellStyle(style);
              sumP10 = sumP10 + rs.getInt("p10");
              tmpCell = tmpRow.createCell(12);
              tmpCell.setCellValue(rs.getInt("p10"));
              tmpCell.setCellStyle(style);
              tmpCell = tmpRow.createCell(13);
              sumP11 = sumP11 + rs.getDouble("p11");
              tmpCell.setCellValue(rs.getDouble("p11"));
              tmpCell.setCellStyle(style);
              tmpCell = tmpRow.createCell(14);
              sumP12 = sumP12 + rs.getInt("p12");
              tmpCell.setCellValue(rs.getInt("p12"));
              tmpCell.setCellStyle(style);
              tmpCell = tmpRow.createCell(15);
              sumP13 = sumP13 + rs.getDouble("p13");
              tmpCell.setCellValue(rs.getDouble("p13"));
              tmpCell.setCellStyle(style);
              tmpCell = tmpRow.createCell(16);
              sumP14 = sumP14 + rs.getInt("p14");
              tmpCell.setCellValue(rs.getInt("p14"));
              tmpCell.setCellStyle(style);
              tmpCell = tmpRow.createCell(17);
              sumP15 = sumP15 + rs.getInt("p15");
              tmpCell.setCellValue(rs.getInt("p15"));
              tmpCell.setCellStyle(style);
              tmpCell = tmpRow.createCell(18);
              sumP16 = sumP16 + rs.getDouble("p16");
              tmpCell.setCellValue(rs.getDouble("p16"));
              tmpCell.setCellStyle(style);
              tmpCell = tmpRow.createCell(19);
              sumP16a = sumP16a + rs.getDouble("p16a");
              tmpCell.setCellValue(rs.getDouble("p16a"));
              tmpCell.setCellStyle(style);
              tmpCell = tmpRow.createCell(20);
              sumP17 = sumP17 + rs.getDouble("p17");
              tmpCell.setCellValue(rs.getDouble("p17"));
              tmpCell.setCellStyle(style);
              tmpCell = tmpRow.createCell(21);
              sumP18 = sumP18 + rs.getDouble("p18");
              tmpCell.setCellValue(rs.getDouble("p18"));
              tmpCell.setCellStyle(style);
              tmpCell = tmpRow.createCell(22);
              sumP19 = sumP19 + rs.getInt("p19");
              tmpCell.setCellValue(rs.getInt("p19"));
              tmpCell.setCellStyle(style);
              tmpCell = tmpRow.createCell(23);
              sumP20 = sumP20 + rs.getDouble("p20");
              tmpCell.setCellValue(rs.getDouble("p20"));
              tmpCell.setCellStyle(style);
              tmpCell = tmpRow.createCell(24);
              sumP21 = sumP21 + rs.getInt("p21");
              tmpCell.setCellValue(rs.getInt("p21"));
              tmpCell.setCellStyle(style);
              tmpCell = tmpRow.createCell(25);
              sumP22 = sumP22 + rs.getDouble("p22");
              tmpCell.setCellValue(rs.getDouble("p22"));
              tmpCell.setCellStyle(style);
              tmpCell = tmpRow.createCell(26);
              sumP23 = sumP23 + rs.getInt("p23");
              tmpCell.setCellValue(rs.getInt("p23"));
              tmpCell.setCellStyle(style);
              tmpCell = tmpRow.createCell(27);
              sumP24 = sumP24 + rs.getDouble("p24");
              tmpCell.setCellValue(rs.getDouble("p24"));
              tmpCell.setCellStyle(style);
              tmpCell = tmpRow.createCell(28);
              sumP25 = sumP25 + rs.getInt("p25");
              tmpCell.setCellValue(rs.getInt("p25"));
              tmpCell.setCellStyle(style);
              tmpCell = tmpRow.createCell(29);
              sumP26 = sumP26 + rs.getDouble("p26");
              tmpCell.setCellValue(rs.getDouble("p26"));
              tmpCell.setCellStyle(style);
              tmpCell = tmpRow.createCell(30);
              sumP27 = sumP27 + rs.getDouble("p27");
              tmpCell.setCellValue(rs.getDouble("p27"));
              tmpCell.setCellStyle(style);
              tmpCell = tmpRow.createCell(31);
              sumP28 = sumP28 + rs.getInt("p28");
              tmpCell.setCellValue(rs.getInt("p28"));
              tmpCell.setCellStyle(style);
              tmpCell = tmpRow.createCell(32);
              sumP29 = sumP29 + rs.getDouble("p29");
              tmpCell.setCellValue(rs.getDouble("p29"));
              tmpCell.setCellStyle(style);
              tmpCell = tmpRow.createCell(33);
              sumP30 = sumP30 + rs.getInt("p30");
              tmpCell.setCellValue(rs.getInt("p30"));
              tmpCell.setCellStyle(style);
              tmpCell = tmpRow.createCell(34);
              sumP31 = sumP31 + rs.getDouble("p31");
              tmpCell.setCellValue(rs.getDouble("p31"));
              tmpCell.setCellStyle(style);
              i++;
              count++;
			}
			Row lastRow = sheet.createRow(i);
			for(int k = 2; k < 35; k++) {
				lastRow.createCell(k).setCellStyle(style);
			}
			//заполнение сумм в таблицу
			Cell tmp = lastRow.getCell(3);
			tmp.setCellValue("Итоговое значение");
			tmp.setCellStyle(style5);
			
			tmp = lastRow.getCell(4);
			tmp.setCellStyle(style5);
			tmp.setCellValue(sumP4);
			
			tmp = lastRow.getCell(5);
			tmp.setCellStyle(style5);
			tmp.setCellValue(Math.round(sumP5/count));
			
			tmp = lastRow.getCell(6);
			tmp.setCellStyle(style5);
			tmp.setCellValue(sumP6);
			
			tmp = lastRow.getCell(7);
			tmp.setCellStyle(style5);
			tmp.setCellValue(Math.round(sumP7/count));
			
			tmp = lastRow.getCell(8);
			tmp.setCellStyle(style5);
			tmp.setCellValue(sumP8);
			
			tmp = lastRow.getCell(9);
			tmp.setCellStyle(style5);
			tmp.setCellValue(Math.round(sumP9/count));
			
			tmp = lastRow.getCell(10);
			tmp.setCellStyle(style5);
			tmp.setCellValue(sumP8a);
			
			tmp = lastRow.getCell(11);
			tmp.setCellStyle(style5);
			tmp.setCellValue(Math.round(sumP9a/count));
			
			tmp = lastRow.getCell(12);
			tmp.setCellStyle(style5);
			tmp.setCellValue(sumP10);
			
			tmp = lastRow.getCell(13);
			tmp.setCellStyle(style5);
			tmp.setCellValue(Math.round(sumP11/count));
			
			tmp = lastRow.getCell(14);
			tmp.setCellStyle(style5);
			tmp.setCellValue(sumP12);
			
			tmp = lastRow.getCell(15);
			tmp.setCellStyle(style5);
			tmp.setCellValue(Math.round(sumP13/count));
			
			tmp = lastRow.getCell(16);
			tmp.setCellStyle(style5);
			tmp.setCellValue(sumP14);
			
			tmp = lastRow.getCell(17);
			tmp.setCellStyle(style5);
			tmp.setCellValue(sumP15);
			
			tmp = lastRow.getCell(18);
			tmp.setCellStyle(style5);
			tmp.setCellValue(roundDouble(sumP16/count, 1));
			
			tmp = lastRow.getCell(19);
			tmp.setCellStyle(style5);
			tmp.setCellValue(roundDouble(sumP16a/count, 1));
			
			tmp = lastRow.getCell(20);
			tmp.setCellStyle(style5);
			tmp.setCellValue(roundDouble(sumP17/count, 1));
			
			tmp = lastRow.getCell(21);
			tmp.setCellStyle(style5);
			tmp.setCellValue(roundDouble(sumP18/count, 1));
			
			tmp = lastRow.getCell(22);
			tmp.setCellStyle(style5);
			tmp.setCellValue(sumP19);
			
			tmp = lastRow.getCell(23);
			tmp.setCellStyle(style5);
			tmp.setCellValue(roundDouble(sumP20/count, 1));
			
			tmp = lastRow.getCell(24);
			tmp.setCellStyle(style5);
			tmp.setCellValue(sumP21);
			
			tmp = lastRow.getCell(25);
			tmp.setCellStyle(style5);
			tmp.setCellValue(roundDouble(sumP22/count, 1));
			
			tmp = lastRow.getCell(26);
			tmp.setCellStyle(style5);
			tmp.setCellValue(sumP23);
			
			tmp = lastRow.getCell(27);
			tmp.setCellStyle(style5);
			tmp.setCellValue(roundDouble(sumP24/count, 1));
			
			tmp = lastRow.getCell(28);
			tmp.setCellStyle(style5);
			tmp.setCellValue(sumP25);
			
			tmp = lastRow.getCell(29);
			tmp.setCellStyle(style5);
			tmp.setCellValue(roundDouble(sumP26/count, 1));
			
			tmp = lastRow.getCell(30);
			tmp.setCellStyle(style5);
			tmp.setCellValue(roundDouble(sumP27/count, 2));
			
			tmp = lastRow.getCell(31);
			tmp.setCellStyle(style5);
			tmp.setCellValue(sumP28);
			
			tmp = lastRow.getCell(32);
			tmp.setCellStyle(style5);
			tmp.setCellValue(Math.round(sumP29/count));
			
			tmp = lastRow.getCell(33);
			tmp.setCellStyle(style5);
			tmp.setCellValue(sumP30);
			
			tmp = lastRow.getCell(34);
			tmp.setCellStyle(style5);
			tmp.setCellValue(Math.round(sumP31/count));
			
			Row row10 = sheet.getRow(10);
			sheet.addMergedRegion(new CellRangeAddress(10, i, 1, 1 ));
			Cell cell10_1 = row10.createCell(1);
			cell10_1.setCellValue(predName.trim());
			cell10_1.setCellStyle(style2);
			lastRow.createCell(1).setCellStyle(style3);
			sheet.setColumnWidth(1, 4000);
			sheet.setColumnWidth(2, 9000);
	        
			ByteArrayOutputStream outByteStream = new ByteArrayOutputStream();
			my_workbook.write(outByteStream);
			byte [] outArray = outByteStream.toByteArray();

			response.reset();
			response.setStatus(response.SC_OK);
			response.setContentType("application/vnd.ms-excel;charset=windows-1251");
			response.setHeader("Cache-Control", "max-age = 2592000,must-revalidate");
			response.setHeader("Pragma", "public");
			response.setDateHeader("Last-Modified", System.currentTimeMillis() ); 			 
			response.setContentLength(outArray.length);
			response.setHeader("Content-disposition", "attachment;filename=tor_report.xls");  

			OutputStream outStream = response.getOutputStream();
			outStream.write(outArray);
			outStream.flush();
			outStream.close();

		} catch(Exception e) {
			log.error(TypeConverter.exceptionToString(e));
		}
		
		return null;
	}
	
	private double roundDouble(double d, int number) {
		BigDecimal bd = new BigDecimal(d);
		bd = bd.setScale(number, RoundingMode.HALF_UP);
		double result = bd.doubleValue();
		return result;
	}

}

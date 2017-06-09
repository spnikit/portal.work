package ru.aisa.etdportal.controllers;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.GregorianCalendar;
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
import ru.aisa.rgd.etd.objects.SfSpsObject;


public class SfSpsExcel  extends AbstractMultipartController {

	public SfSpsExcel() throws JSONException {
		super();
	}

	public static String formString(List<String> list){
		String resultString = "";
		for(int i = 0; i < list.size(); i++){
			if(i == list.size()-1){
				resultString+=list.get(i);
			}else{
				resultString+=list.get(i)+", ";
			}
		}

		return resultString;
	}

	public static String dateStringPrint(String str){
		String stringPrint = "";
		String[] mass = str.split("-");
		stringPrint = mass[2]+"."+mass[1]+"."+mass[0];
		return stringPrint;	
	}

	public static List<Integer> arrayToList(String str){
		List<Integer> list = new ArrayList<Integer>();
		String[] responseIdMass = str.split(","); 
		Integer[] responseIdMassInt = new Integer[responseIdMass.length];

		for(int i =0; i < responseIdMass.length; i++){
			responseIdMassInt[i] = Integer.parseInt(responseIdMass[i]);
		}
		list = Arrays.asList(responseIdMassInt);
		return list;
	}

	protected ModelAndView handleRequestInternal(HttpServletRequest request,
			HttpServletResponse response) throws Exception {


		String sql  = "select ID,PREDID,SELLER,INNSELLER,SFNUMBER,SF_DATE,SUMMWITHNDS,SUMMNDS,NDSVALUE,RECIEVE_DATE, VAGNUM,SFTYPE,"
				+ "(select rtrim(rtrim(fname) || ' ' || rtrim(mname) || ' ' || rtrim(lname)) RESPONSEID from snt.personall where id  = sfrep.responseid ) as RESPONSEID,INCOME_DATE, "
				+" (select case when dropid is null then 0 else 1 end dropped from snt.docstore where id = sfrep.id), "
				+" (select case when signlvl is null then 1 else 0 end signed from snt.docstore where id = sfrep.id) "
				+ " from SNT.SF_REPORTS sfrep where predid in (select id from snt.pred where id = :predid or headid = :predid) and INCOME_DATE BETWEEN :datebefore AND :dateafter #resp #sell with ur";


		String sqlResponseId = "select rtrim(rtrim(fname) || ' ' || rtrim(mname) || ' ' || rtrim(lname)) RESPONSEID from snt.personall where id in (:responseId) ";
		String sqlSeller = "SELECT rtrim(NAME) NAME from SNT.DOR WHERE ID IN (:seller)";

		HashMap<String, Object> pp = new HashMap<String, Object>();		
		String dateBefore = request.getParameter("dateBefore");
		String dateAfter = request.getParameter("dateAfter");
		String name = new String(request.getParameter("name").getBytes("ISO8859-1"),"UTF-8");
		String rolename = new String(request.getParameter("rolename").getBytes("ISO8859-1"),"UTF-8");
		String predid = request.getParameter("predid");
		String seller = request.getParameter("seller");
		String responseId =request.getParameter("responseId");
		String sellerName = "";
		String responseIdName = "";		
		String addresp = "AND RESPONSEID in (:responseId) ";
		String addsell = "AND sellerid in (:seller)"; 
		List<Integer> responseIdList = new ArrayList<Integer>();
		List<Integer> sellerList = new ArrayList<Integer>();
		double summWithNdsAll = 0;
		double summNdsAll = 0;
		
		SimpleDateFormat df = new SimpleDateFormat("dd.MM.yyyy");
		Calendar cal = new GregorianCalendar();
		String nowDate = df.format(cal.getTime());
		
		if(seller.equals("") && !responseId.equals("")){
			responseIdList = arrayToList(responseId);
			sql = sql.replaceAll("#resp", addresp).replaceAll("#sell", "");
		}else if(!seller.equals("") && responseId.equals("")){
			sellerList = arrayToList(seller);
			sql = sql.replaceAll("#sell", addsell).replaceAll("#resp", "");
		}else if(!seller.equals("") && !responseId.equals("")){
			responseIdList = arrayToList(responseId);
			sellerList = arrayToList(seller);
			sql = sql.replaceAll("#resp", addresp).replaceAll("#sell", addsell);
		}else{
			sql = sql.replaceAll("#resp", "").replaceAll("#sell", "");
		}
		String dateBeforePrint = dateStringPrint(dateBefore);
		String dateAfterPrint = dateStringPrint(dateAfter);
		String title = "\"Отчет по полученным счет-фактурам из ЭДО СПС по ТОР за период с "+dateBeforePrint+" по "+dateAfterPrint+"\"";

		pp.put("datebefore", dateBefore);
		pp.put("dateafter", dateAfter);
		pp.put("predid", Integer.parseInt(predid));
		pp.put("responseId",responseIdList);
		pp.put("seller", sellerList);

		if(!seller.equals("")){
			List<String> sellerNameList =getNpjt().query(
					sqlSeller, pp, new ParameterizedRowMapper<String>() {
						public String mapRow(ResultSet rs, int n)
								throws SQLException {
							String str = "";
							str = rs.getString("NAME");
							return str;
						}
					});
			sellerName = formString(sellerNameList);
		}
		
		if(!responseId.equals("")){
			List<String> responseIdNameList =getNpjt().query(
					sqlResponseId, pp, new ParameterizedRowMapper<String>() {
						public String mapRow(ResultSet rs, int n)
								throws SQLException {
							String str = "";
							str = rs.getString("RESPONSEID");
							return str;
						}
					});
			responseIdName = formString(responseIdNameList);
		}

		List<SfSpsObject> list =getNpjt().query(
				sql, pp, new ParameterizedRowMapper<SfSpsObject>() {
					public SfSpsObject mapRow(ResultSet rs, int n)
							throws SQLException {
						SfSpsObject obj = new SfSpsObject();
						obj.setId(rs.getString("ID"));
						obj.setPredid(rs.getString("PREDID"));
						obj.setSeller(rs.getString("SELLER"));
						obj.setInnSeller(rs.getString("INNSELLER"));
						obj.setSfNumber(rs.getString("SFNUMBER"));
						obj.setSfDate(rs.getString("SF_DATE"));
						obj.setSummWithNds(rs.getString("SUMMWITHNDS"));
						obj.setSummNds(rs.getString("SUMMNDS"));
						obj.setNdsValue(rs.getString("NDSVALUE"));
						obj.setRecieveDate(rs.getString("RECIEVE_DATE"));
						obj.setVagNum(rs.getString("VAGNUM"));
						obj.setSfType(rs.getString("SFTYPE"));
						obj.setResponseId(rs.getString("RESPONSEID"));
						obj.setIncomeDate(rs.getString("INCOME_DATE"));

						if(rs.getInt("signed")==1&&rs.getInt("dropped")==0){
							obj.setStatus("Подлежит оформлению");
						}else{
							obj.setStatus("Не подлежит оформлению");
						}
						
						if (rs.getInt("signed")==0&&rs.getInt("dropped")==0){
							obj.setPending("на рассмотрении");
						} else if (rs.getInt("signed")==0&&rs.getInt("dropped")==1){
							obj.setPending("отклоненные");
						}else if (rs.getInt("signed")==1&&rs.getInt("dropped")==0){
							obj.setPending("согласованные");
						}



						return obj;
					}
				});

		Map<Integer, Object[]> data = new HashMap<Integer, Object[]>();
		String[] allHeaders = {"№ п/п","Продавец","ИНН Продавца","№ счет-фактуры","Дата счета фактуры","Сумма(в т.ч. НДС), руб.","Сумма НДС, руб.","Ставка НДС","Дата получения","Номер вагона","Вид счет-фактуры","Ответственный", "Стадия рассмотрения" ,"Статус СФ"};

		data.put(1, allHeaders);

		for(int i = 0; i < list.size();i++){
			SfSpsObject obj = list.get(i);
			Object[] mass = new Object[14];
			mass[0] = i+1;
			mass[1] = obj.getSeller();
			mass[2] = obj.getInnSeller();
			mass[3] = obj.getSfNumber();
			mass[4] = obj.getSfDate();
			mass[5] = obj.getSummWithNds();
			mass[6] = obj.getSummNds();
			mass[7] = obj.getNdsValue();
			mass[8] = obj.getRecieveDate();
			mass[9] = obj.getVagNum();
			mass[10] = obj.getSfType();
			mass[11] = obj.getResponseId();
			mass[12] = obj.getPending();
			mass[13] = obj.getStatus();
			data.put(i+2, mass);
			summWithNdsAll += Double.parseDouble(obj.getSummWithNds());
			summNdsAll += Double.parseDouble(obj.getSummNds());
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
		fontStyle.setBorderRight(HSSFCellStyle.BORDER_MEDIUM);
		fontStyle.setBorderLeft(HSSFCellStyle.BORDER_MEDIUM);
		fontStyle.setBorderTop(HSSFCellStyle.BORDER_MEDIUM);
		fontStyle.setBorderBottom(HSSFCellStyle.BORDER_MEDIUM);

		sheet.addMergedRegion(new CellRangeAddress(data.size()+5,data.size()+5,0,11));
		sheet.addMergedRegion(new CellRangeAddress(0,0,1,11));
		sheet.addMergedRegion(new CellRangeAddress(1,1,1,11));
		sheet.addMergedRegion(new CellRangeAddress(2,2,1,11));
		sheet.addMergedRegion(new CellRangeAddress(3,3,0,11));

		Row header1 = sheet.createRow(0);
		Cell headerCell1 = header1.createCell(0);
		headerCell1.setCellValue("Наименование: ");
		headerCell1.setCellStyle(style1);
		headerCell1 = header1.createCell(1);
		headerCell1.setCellValue(title);
		headerCell1.setCellStyle(fontStyle);
		Row header2 = sheet.createRow(1);
		Cell headerCell2 = header2.createCell(0);
		headerCell2.setCellValue("Продавец: ");
		headerCell2.setCellStyle(style1);

		headerCell2 = header2.createCell(1);
		headerCell2.setCellValue(sellerName);
		headerCell2.setCellStyle(style1);

		Row header3 = sheet.createRow(2);
		Cell headerCell3 = header3.createCell(0);
		headerCell3.setCellValue("Ответственный: ");
		headerCell3.setCellStyle(style1);

		headerCell3 = header3.createCell(1);
		headerCell3.setCellValue(responseIdName);
		headerCell3.setCellStyle(style1);

		for(int i = 2; i < 12; i++){
			headerCell1 = header1.createCell(i);
			headerCell1.setCellStyle(style1);

			headerCell2 = header2.createCell(i);
			headerCell2.setCellStyle(style1);

			headerCell3 = header3.createCell(i);
			headerCell3.setCellStyle(style1);
		}	 

		Row header = sheet.createRow(data.size()+6);
		Cell headerCell4 = header.createCell(0);
		headerCell4.setCellValue("Должность");

		headerCell4 = header.createCell(1);
		headerCell4.setCellValue(rolename);

		headerCell4 = header.createCell(2);
		headerCell4.setCellValue("ФИО");

		headerCell4 = header.createCell(3);
		headerCell4.setCellValue(name);

		headerCell4 = header.createCell(4);
		headerCell4.setCellValue("Дата");

		headerCell4 = header.createCell(5);
		headerCell4.setCellValue(nowDate);

		Row header5 = sheet.createRow(data.size()+4);
		Cell headerCell5 = header5.createCell(4);
		headerCell5.setCellValue("Итого:");
		headerCell5.setCellStyle(style1);
		
		headerCell5 = header5.createCell(5);
		headerCell5.setCellValue(summWithNdsAll);
		headerCell5.setCellStyle(style1);
		
		headerCell5 = header5.createCell(6);
		headerCell5.setCellValue(summNdsAll);
		headerCell5.setCellStyle(style1);
		
		Set<Integer> keyset = data.keySet();
		int rownum = 4;
		for (int key=1;key<=keyset.size(); key++){
		
			Row row = sheet.createRow(rownum++);

			Object [] objArr = data.get(key);
			int cellnum = 0;
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

		for(int colNum = 0; colNum<sheet.getRow(4).getLastCellNum();colNum++){ 
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
			response.setHeader("Content-disposition", "attachment;filename=sfexcel.xls");  

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
}

package sheff.rjd.controllers;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.sql.Blob;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;

import javax.activation.DataSource;
import javax.servlet.Filter;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.ParameterizedRowMapper;
import org.springframework.web.servlet.ModelAndView;

import com.sun.java.swing.plaf.windows.resources.windows;

import ru.aisa.edt.DocumentsTableRequestDocument;
import ru.aisa.edt.DocumentsTableRequestDocument.DocumentsTableRequest;
import ru.aisa.etdadmin.controllers.AbstractMultipartController;
import ru.aisa.rgd.etd.dao.ETDDocumentDao;
import ru.aisa.rgd.etd.objects.ETDDocument;
import ru.aisa.rgd.etd.objects.ETDDocumentFilter;
import ru.aisa.rgd.utils.Base64;



public class AploadedToExcel extends AbstractMultipartController {

	public AploadedToExcel() throws JSONException {
		super();
	}

	public String[] compareDate(String before, String after){

		String[] massDate = new String[2];
		String[] parseAfter = new String[3];
		String[] parseBefore = new String[3];
		parseAfter = after.split("-");
		parseBefore = before.split("-");

		//		if(Integer.parseInt(parseAfter[0])==2000 && Integer.parseInt(parseBefore[0])==2100){
		//			DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		//			Calendar rightNow = Calendar.getInstance();
		//			rightNow.setTimeInMillis(System.currentTimeMillis());
		//			Date afterDate = rightNow.getTime();
		//			rightNow.add(Calendar.DAY_OF_MONTH, -31);
		//			Date beforeDate =  rightNow.getTime();	
		//			parseAfter = dateFormat.format(afterDate).split("-");
		//			parseBefore = dateFormat.format(beforeDate).split("-");
		//			after = dateFormat.format(afterDate);
		//			before = dateFormat.format(beforeDate);
		//	
		//		}

		if(Integer.parseInt(parseBefore[0])<=Integer.parseInt(parseAfter[0])&&Integer.parseInt(parseBefore[1])<=Integer.parseInt(parseAfter[1])&&Integer.parseInt(parseBefore[2])<=Integer.parseInt(parseAfter[2])){
			massDate[0] = before;
			massDate[1] = after;
		}else{
			massDate[0] = after;
			massDate[1] = before;
		}

		return massDate;
	}

	public Map<String, Object> conformity(ETDDocument etd){

		Map<String, Object> conformity = new HashMap<String, Object>();
		conformity.put("Номер вагона", etd.getVagnum());
		conformity.put("Тип документа", etd.getName());
		conformity.put("Дата ремонта", etd.getReqdate());
		conformity.put("Описание", etd.getNumber());
		conformity.put("Договор", etd.getDognum());
		conformity.put("ДИ", etd.getDi());
		conformity.put("ВЧДЭ", etd.getRem_pred());
		conformity.put("Пакет", etd.getIdPak());
		conformity.put("Дата поступления", etd.getCreateDate().toString()+" "+etd.getCreateTime().toString());
		conformity.put("Дата создания", etd.getCreateDate().toString()+" "+etd.getCreateTime().toString());
		conformity.put("Текущий исполнитель", etd.getLastSigner());
		Date etdLastDate = etd.getLastDate();
		if(etdLastDate == null) {
			conformity.put("Дата последней подписи", "");
		} else {
			conformity.put("Дата последней подписи", etdLastDate.toString() + " " + etd.getLastTime().toString());
		}
		try{
			conformity.put("Стоимость ремонта", Double.parseDouble(etd.getPrice()));
		}catch(NullPointerException e){
			conformity.put("Стоимость ремонта", "");
		}catch(NumberFormatException e){
			conformity.put("Стоимость ремонта", etd.getPrice());
		}
		/*if(etd.getPrice()!=null && !etd.getPrice().equals("")){
			conformity.put("Стоимость ремонта", Double.parseDouble(etd.getPrice()));
		}else{
			conformity.put("Стоимость ремонта", etd.getPrice());
		}*/
		conformity.put("Статус", etd.getReqnum());
		conformity.put("Наименование услуги", etd.getServicetype());
		conformity.put("Код неисправности", etd.getOtcname());
		conformity.put("Вид неисправности", etd.getOtctype());
		conformity.put("Вид СФ", etd.getSftype());
		conformity.put("Причина отклонения", etd.getDroptxt());
		conformity.put("Номер", etd.getDognum());
		conformity.put("Краткое содержание", etd.getNumber());
		conformity.put("ID", etd.getEtdid());

		return conformity;
	}

	protected ModelAndView handleRequestInternal(HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		Map<String,Object> conformity = new HashMap<String, Object>();
		String params =  request.getParameter("params");	
		String headerField = new String(request.getParameter("headerField").getBytes("ISO8859-1"),"UTF-8");
		//		System.out.println(headerField);
		String shiftParams = request.getParameter("shiftParams");
		String predid = "";
		String workid = "";
		String fr = "";
		String before = "";
		String after = "";
		String indexInset = "";
		String[] allHeaders = null;
		String[] parameters = null;
		String sort = request.getParameter("sort");
		String dir = request.getParameter("dir");
		//System.out.println("sort = " + sort);
		//System.out.println("dir = " + dir);



		allHeaders = headerField.split(";");
		parameters = params.split(";");
		predid = parameters[0];
		workid = parameters[1];
		fr = parameters[2];
		before = parameters[3];
		after = parameters[4];
		indexInset = parameters[5];

		String[] date =compareDate(before, after);
		before = date[1];
		after = date[0];

		DocumentsTableRequestDocument requestDocument = DocumentsTableRequestDocument.Factory.newInstance();
		DocumentsTableRequest request1 = requestDocument.addNewDocumentsTableRequest();

		ru.aisa.edt.DocumentsTableRequestDocument.DocumentsTableRequest.Filter filter = request1.addNewFilter();
		filter.setFuncRole(Integer.parseInt(fr));
		filter.setPredId(Integer.parseInt(predid));
		filter.setWorkerId(Integer.parseInt(workid));
		filter.setDateBefore(before);
		filter.setDateAfter(after);
		filter.setShift(shiftParams);
		filter.setSort(sort);
		filter.setDir(dir);




		if(after == null || before == null ){
			filter.setDateEqual(before+","+after);
		}else{
			filter.setDateEqual("null");
		}

		List<Long> idS = new ArrayList<Long>();
		List<ETDDocument> result = new ArrayList<ETDDocument>();

		Map<Integer, Object[]> data = new HashMap<Integer, Object[]>();
		data.put(1, allHeaders);

		ETDDocumentFilter pm = new ETDDocumentFilter(filter);


		long start = System.currentTimeMillis();

		if(Integer.parseInt(indexInset)==1) {
			
			if(!"13".equals(fr)) {
				idS = getDocumentDao().getActiveDocumentIds(pm);
				//		System.out.println("active2 "+idS);
				filter.setStart(0);
				filter.setLimit(idS.size());
				pm = new ETDDocumentFilter(filter);
				result = getDocumentDao().getActiveDocuments(pm, idS);



				for(int i = 0; i < result.size();i++){

					Object[] massEl = new Object[allHeaders.length];
					ETDDocument etd = null;
					etd = result.get(i);
					conformity = conformity(etd);

					for(int j = 0; j< allHeaders.length; j++){
						massEl[j] = conformity.get(allHeaders[j]);
					}

					data.put(i+2, massEl);

				}
			} else {
				idS = getDocumentDao().getArchiveDocumentIds(pm);
				//		System.out.println("idssize "+idS.size());
				filter.setStart(0);
				filter.setLimit(idS.size());
				pm = new ETDDocumentFilter(filter);
				result = getDocumentDao().getArchiveDocuments(pm, idS);
				//		System.out.println("1: "+(System.currentTimeMillis()-start));
				for(int i = 0; i < result.size();i++){
					Object[] massEl = new Object[allHeaders.length];			
					ETDDocument etd = null;
					etd = result.get(i);
					conformity = conformity(etd);

					for(int j = 0; j< allHeaders.length; j++){
						massEl[j] = conformity.get(allHeaders[j]);
					}

					data.put(i+2, massEl);
				}
			}
		}else if(Integer.parseInt(indexInset)==2){
			idS = getDocumentDao().getDroppedDocumentIds(pm);
			filter.setStart(0);
			filter.setLimit(idS.size());
			pm = new ETDDocumentFilter(filter);
			result = getDocumentDao().getInworkDocuments(pm, idS);

			for(int i = 0; i < result.size();i++){

				Object[] massEl = new Object[allHeaders.length];
				ETDDocument etd = null;
				etd = result.get(i);
				conformity = conformity(etd);

				for(int j = 0; j< allHeaders.length; j++){
					massEl[j] = conformity.get(allHeaders[j]);
				}

				data.put(i+2, massEl);
			}

		}else if(Integer.parseInt(indexInset)==3){
			idS = getDocumentDao().getDeclinedDocumentIds(pm);
			filter.setStart(0);
			filter.setLimit(idS.size());
			pm = new ETDDocumentFilter(filter);
			result = getDocumentDao().getDroppedDocuments(pm, idS);

			for(int i = 0; i < result.size();i++){
				//			System.out.println(result.get(i).getDroptxt());
				Object[] massEl = new Object[allHeaders.length];
				ETDDocument etd = null;
				etd = result.get(i);
				conformity = conformity(etd);

				for(int j = 0; j< allHeaders.length; j++){
					massEl[j] = conformity.get(allHeaders[j]);
				}

				data.put(i+2, massEl);
			}

		}else{

			idS = getDocumentDao().getArchiveDocumentIds(pm);
			//		System.out.println("idssize "+idS.size());
			filter.setStart(0);
			filter.setLimit(idS.size());
			pm = new ETDDocumentFilter(filter);
			result = getDocumentDao().getArchiveDocuments(pm, idS);
			//		System.out.println("1: "+(System.currentTimeMillis()-start));
			for(int i = 0; i < result.size();i++){
				Object[] massEl = new Object[allHeaders.length];			
				ETDDocument etd = null;
				etd = result.get(i);
				conformity = conformity(etd);

				for(int j = 0; j< allHeaders.length; j++){
					massEl[j] = conformity.get(allHeaders[j]);
				}

				data.put(i+2, massEl);
			}
			//		System.out.println("2: "+(System.currentTimeMillis()-start));
		}

		//		System.out.println("3");

		HSSFWorkbook workbook = new HSSFWorkbook();
		HSSFSheet sheet = workbook.createSheet("Sample sheet");




		CellStyle style;
		HSSFFont font = workbook.createFont();
		font.setFontName(HSSFFont.FONT_ARIAL);

		style = workbook.createCellStyle();
		style.setAlignment(CellStyle.ALIGN_CENTER);
		style.setFont(font);







		Set<Integer> keyset = data.keySet();
		int rownum = 0;
		for (int key = 1; key<=keyset.size();key++) {
			Row row = sheet.createRow(rownum++);


			Object [] objArr = data.get(key);
			int cellnum = 0;
			for (Object obj : objArr) {
				Cell cell = row.createCell(cellnum++);
				if(key == 1){
					cell.setCellStyle(style);
				}
				if(obj instanceof Date){
					cell.setCellValue((Date)obj);	        
				}else if(obj instanceof Boolean){
					cell.setCellValue((Boolean)obj);
				}else if(obj instanceof String){
					cell.setCellValue((String)obj);
				}else if(obj instanceof Double){
					cell.setCellValue((Double)obj);
				}else if(obj instanceof Integer){
					cell.setCellValue((Integer)obj);
				}else if(obj instanceof Long){
					cell.setCellValue((Long)obj);
				}

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
			response.setHeader("Content-disposition", "attachment;filename=served.xls");  

			OutputStream outStream = response.getOutputStream();
			outStream.write(outArray);
			outStream.flush();
			outStream.close();

			//		    System.out.println("Excel written successfully..");

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return null;
	}

}

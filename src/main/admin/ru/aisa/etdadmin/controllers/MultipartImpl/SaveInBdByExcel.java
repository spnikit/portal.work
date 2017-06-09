package ru.aisa.etdadmin.controllers.MultipartImpl;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.jdbc.core.simple.ParameterizedRowMapper;
import ru.aisa.etdadmin.Utils;
import ru.aisa.etdadmin.controllers.AbstractMultipartController;
import ru.aisa.rgd.ws.utility.TypeConverter;

public class SaveInBdByExcel extends AbstractMultipartController {

	public SaveInBdByExcel() throws JSONException {
		super();
	}
	@Override
	protected JSONObject add(HashMap<String, String> requestParameters,
			HashMap<String, byte[]> requestFiles,HashMap<String, String> requestFileNames) throws JSONException,
			IOException {
		//if(Utils.isReadonly()) return FORBIDDEN;
		JSONObject response = new JSONObject();
		response.put(success, false); 
		byte[] fileContent = requestFiles.get("formfile");
		String sql = "MERGE INTO snt.services AS srv USING TABLE(VALUES(CAST (COALESCE((select max(id)+1 from SNT.SERVICES),0) AS INTEGER), CAST (? AS INTEGER), CAST ((select id from snt.doctype where name ='РДВ') AS INTEGER), CAST (? AS VARCHAR(20)),CAST (? AS VARCHAR(1000)), CAST (? AS DECIMAL(11,2))))" 
		+"s(id, pred_id, type_id, service_code, service_name, services_price) " 
		+"ON srv.pred_id = s.pred_id and srv.service_code = s.service_code "
		+"WHEN MATCHED THEN "+
		"update set service_name = s.service_name, services_price = s.services_price "
		+"WHEN NOT MATCHED THEN "
		+"insert (id, pred_id, type_id, service_code, service_name, services_price) values (s.id, s.pred_id, s.type_id, s.service_code, s.service_name, s.services_price)";
		String GET_PRED_ID = "select id from SNT.pred where type <> 1 and vname = :name";
		if (fileContent != null && fileContent.length > 0) {
			ArrayList<Object[]> objectList = new ArrayList<Object[]>();
			int predId = 0;
			String predName = "";
			InputStream myInputStream = new ByteArrayInputStream(fileContent);
			XSSFWorkbook workbook = new XSSFWorkbook(myInputStream);
			XSSFSheet sheet = workbook.getSheetAt(0);
			Iterator<Row> rowIterator = sheet.iterator();
			while (rowIterator.hasNext())
			{
				Row row = rowIterator.next();
				if(row.getRowNum()==1){
					Map<String, Object> paramMap = new HashMap<String, Object>();
					row.getCell(0).setCellType(Cell.CELL_TYPE_STRING);
					paramMap.put("name", row.getCell(0).getStringCellValue());
					predId = getNpjt().queryForInt(GET_PRED_ID, paramMap);
				}
				//getNpjt().update(sql, paramMap)
				if(row.getRowNum()!=0){
					if(row.getCell(0) == null || row.getCell(0).getCellType() == Cell.CELL_TYPE_BLANK){
						break;
					}else if(row.getCell(1) == null || row.getCell(1).getCellType() == Cell.CELL_TYPE_BLANK){
						break;
					}else if(row.getCell(2) == null || row.getCell(2).getCellType() == Cell.CELL_TYPE_BLANK){
						break;
					}else if(row.getCell(3) == null || row.getCell(3).getCellType() == Cell.CELL_TYPE_BLANK){	
						break;
					}
					row.getCell(0).setCellType(Cell.CELL_TYPE_STRING);
					row.getCell(1).setCellType(Cell.CELL_TYPE_STRING);
					row.getCell(2).setCellType(Cell.CELL_TYPE_STRING);
					row.getCell(3).setCellType(Cell.CELL_TYPE_STRING);
					objectList.add(new Object[]{predId,row.getCell(1).getStringCellValue().trim(),row.getCell(2).getStringCellValue().trim(),Double.parseDouble(row.getCell(3).getStringCellValue().trim())});
				}

			}
			myInputStream.close();
			try{
				getSjt().batchUpdate(sql, objectList);
			}catch(Exception e){
				log.error(TypeConverter.exceptionToString(e));
			}
			response.put(success, true);
		} else {
			response.put(description, "Нет файла");
		}

		return response;
	}
}


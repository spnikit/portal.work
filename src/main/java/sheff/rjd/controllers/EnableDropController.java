package sheff.rjd.controllers;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.json.JSONException;
import org.springframework.web.servlet.ModelAndView;

import ru.aisa.edt.DocumentsTableRequestDocument;
import ru.aisa.edt.DocumentsTableRequestDocument.DocumentsTableRequest;
import ru.aisa.etdadmin.controllers.AbstractMultipartController;
import ru.aisa.rgd.etd.objects.ETDDocument;
import ru.aisa.rgd.etd.objects.ETDDocumentFilter;

public class EnableDropController extends AbstractMultipartController {

	public EnableDropController() throws JSONException {
		super();

	}

	public String[] compareDate(String before, String after) {

		String[] massDate = new String[2];
		String[] parseAfter = new String[3];
		String[] parseBefore = new String[3];
		parseAfter = after.split("-");
		parseBefore = before.split("-");

		if (Integer.parseInt(parseBefore[0]) <= Integer.parseInt(parseAfter[0])
				&& Integer.parseInt(parseBefore[1]) <= Integer
						.parseInt(parseAfter[1])
				&& Integer.parseInt(parseBefore[2]) <= Integer
						.parseInt(parseAfter[2])) {
			massDate[0] = before;
			massDate[1] = after;
		} else {
			massDate[0] = after;
			massDate[1] = before;
		}

		return massDate;
	}

	/*public Map<String, Object> conformity(ETDDocument etd) {

		Map<String, Object> conformity = new HashMap<String, Object>();
		conformity.put("Номер вагона", etd.getVagnum());
		conformity.put("Тип документа", etd.getName());
		conformity.put("Дата ремонта", etd.getReqdate());
		conformity.put("Описание", etd.getNumber());
		conformity.put("Договор", etd.getDognum());
		conformity.put("ДИ", etd.getDi());
		conformity.put("ВЧДЭ", etd.getRem_pred());
		conformity.put("Пакет", etd.getIdPak());
		conformity.put("Дата поступления", etd.getCreateDate().toString() + " "
				+ etd.getCreateTime().toString());
		conformity.put("Текущий исполнитель", etd.getLastSigner());
		Date etdLastDate = etd.getLastDate();
		if (etdLastDate == null)
			etdLastDate = new Date();
		conformity.put("Дата последней подписи", etdLastDate.toString());
		conformity.put("Стоимость ремонта", etd.getPrice());
		conformity.put("Статус", etd.getReqnum());
		conformity.put("Наименование услуги", etd.getServicetype());
		conformity.put("Код неисправности", etd.getOtcname());
		conformity.put("Вид неисправности", etd.getOtctype());
		conformity.put("Вид СФ", etd.getSftype());

		return conformity;
	}*/

	protected ModelAndView handleRequestInternal(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		boolean enable = false;
		String shparams = request.getParameter("shiftParams");
		String params = request.getParameter("params");
		
		String[] splitted = params.split(";");

		String predid = splitted[0];
		String workid = splitted[1];
		String fr = splitted[2];
		String before = splitted[3];
		String after = splitted[4];
		String indexTab = splitted[5];
		
		String[] date = compareDate(before, after);
		before = date[1];
		after = date[0];
		String sort = request.getParameter("sort");
		String dir = request.getParameter("dir");
		String docId = request.getParameter("docId");
		long destId = Long.parseLong(docId);

		DocumentsTableRequestDocument requestDocument = DocumentsTableRequestDocument.Factory
				.newInstance();
		DocumentsTableRequest request1 = requestDocument
				.addNewDocumentsTableRequest();

		ru.aisa.edt.DocumentsTableRequestDocument.DocumentsTableRequest.Filter filter = request1
				.addNewFilter();
		filter.setFuncRole(Integer.parseInt(fr));
		filter.setPredId(Integer.parseInt(predid));
		filter.setWorkerId(Integer.parseInt(workid));
		filter.setDateBefore(before);
		filter.setDateAfter(after);
		filter.setShift(shparams);
		filter.setSort(sort);
		filter.setDir(dir);


		if (after == null || before == null) {
			filter.setDateEqual(before + "," + after);
		} else {
			filter.setDateEqual("null");
		}

		List<Long> idS = new ArrayList<Long>();
		List<ETDDocument> result = new ArrayList<ETDDocument>();

		Map<Integer, Object[]> data = new HashMap<Integer, Object[]>();

		ETDDocumentFilter pm = new ETDDocumentFilter(filter);
		long start = System.currentTimeMillis();
		if (Integer.parseInt(indexTab) == 1) {

			idS = getDocumentDao().getActiveDocumentIds(pm);

			for (int i = 0; i < idS.size(); i++) {
				
				if (idS.get(i) == destId) {
					enable = true;
					break;
				}
			}
			
			/*filter.setStart(0);
			filter.setLimit(idS.size());
			pm = new ETDDocumentFilter(filter);
			result = getDocumentDao().getActiveDocuments(pm, idS);*/

		}
		if (Integer.parseInt(indexTab) == 2) {
			idS = getDocumentDao().getDroppedDocumentIds(pm); 
			for (int i = 0; i < idS.size(); i++) {
				
				if (idS.get(i) == destId) {
					enable = true;
					break;
				}
				/*filter.setStart(0);
				filter.setLimit(idS.size());
				pm = new ETDDocumentFilter(filter);*/
				//result = getDocumentDao().getInworkDocuments(pm, idS);

			}

		}
		try {
			String outStr = String.valueOf(enable);
			byte[] outArray = outStr.getBytes();

			response.reset();

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
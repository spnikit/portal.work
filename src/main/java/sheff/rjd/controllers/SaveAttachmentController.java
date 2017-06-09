package sheff.rjd.controllers;

import java.io.File;
import java.io.FileInputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;

import ru.aisa.rgd.utils.Base64;

public class SaveAttachmentController extends AbstractController {
//	private NamedParameterJdbcTemplate npjt;
//
//	public void setNpjt(NamedParameterJdbcTemplate npjt) {
//		this.npjt = npjt;
//	}
//
//	public NamedParameterJdbcTemplate getNpjt() {
//		return npjt;
//	}

	public SaveAttachmentController() {
	}

	protected ModelAndView handleRequestInternal(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		response.setCharacterEncoding("UTF-8");
		response.setContentType("text/html; charset=UTF-8");
		request.setCharacterEncoding("UTF-8");
		
		String fname = request.getParameter("fname");
		String type = request.getParameter("type");
		String filePath = "C:/aisa/temp_save_docs/tempattach";
		File file = new File(filePath);
//		FileInputStream fis = new FileInputStream(file);
//		byte[] b = new byte[(int)file.length()];
//		fis.read(b);
//		fis.close();
		byte[] b = Base64.decodeFromFile(filePath);
		file.delete();
		
		//response.setContentType("application/x-download");
		response.setContentType(type);
		response.setHeader("Content-disposition", "attachment; filename="+fname);
		response.getOutputStream().write(b);
		return null;
	}

}

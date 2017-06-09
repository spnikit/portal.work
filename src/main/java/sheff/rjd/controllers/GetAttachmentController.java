package sheff.rjd.controllers;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.sql.Blob;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;

import ru.aisa.rgd.utils.Base64;

public class GetAttachmentController extends AbstractController {
	private NamedParameterJdbcTemplate npjt;

	public void setNpjt(NamedParameterJdbcTemplate npjt) {
		this.npjt = npjt;
	}

	public NamedParameterJdbcTemplate getNpjt() {
		return npjt;
	}

	public GetAttachmentController() {
	}

	protected ModelAndView handleRequestInternal(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		response.setCharacterEncoding("UTF-8");
		response.setContentType("text/html; charset=UTF-8");
		request.setCharacterEncoding("UTF-8");
		
		String fname = request.getParameter("fname");
		int isLoad = Integer.valueOf(request.getParameter("isload"));
		if (isLoad==1){
			String docid = "-1";
			boolean quick = false;
			if (request.getCookies() != null) {
				Cookie ck;
				for (int loopIndex = 0; loopIndex < request.getCookies().length; loopIndex++) {
					ck = request.getCookies()[loopIndex];
					if (ck.getName().equals("quickcookie"))
						quick = true;
				}
				for (int loopIndex = 0; loopIndex < request.getCookies().length; loopIndex++) {
					ck = request.getCookies()[loopIndex];
					if (quick) {
						if (ck.getName().equals("quickdocid"))
							docid = ck.getValue();
					} else {
						if (ck.getName().equals("docid"))
							docid = ck.getValue();
					}
	
				}
			}
			
			String idx = fname.replace("data", "");
			Map p = new HashMap();
			p.put("docid", Integer.parseInt(docid));
			p.put("idx", Integer.parseInt(idx));
			String sql = "SELECT TEMPLATE FROM SNT.ENCLOSEDDOCS WHERE DOCID = :docid AND ID = :idx";
			String sqlfn = "SELECT FNAME FROM SNT.ENCLOSEDDOCS WHERE DOCID = :docid AND ID = :idx";
			
			try{
				Blob blob = (Blob)npjt.queryForObject(sql, p, Blob.class);
				String zipfile = new String(blob.getBytes(1, (int)blob.length()));
				String filename = (String) npjt.queryForObject(sqlfn, p, String.class);
				byte[] file = Base64.decode(zipfile, Base64.GZIP);
				
				response.setContentType("application/x-download");
				response.setHeader("Content-disposition", "attachment; filename="+filename);
				response.getOutputStream().write(file);
			} catch (Exception ex){
				ex.printStackTrace();
			}
		} else {
			File t = new File("C://aisa//temp_save_docs//"+fname);
			if (t.exists()){
				String filename = request.getParameter("name");
				FileInputStream is = new FileInputStream(t);
				byte[] file = new byte[(int)t.length()];
				is.read(file);
				is.close();
				response.setContentType("application/x-download");
				response.setHeader("Content-disposition", "attachment; filename="+filename);
				response.getOutputStream().write(file);
			}
		}
		
		return null;
	}

}

package sheff.rjd.controllers;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.jdbc.SQLWarningException;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;

public class RemoveAttachmentController extends AbstractController {
	private NamedParameterJdbcTemplate npjt;

	public void setNpjt(NamedParameterJdbcTemplate npjt) {
		this.npjt = npjt;
	}

	public NamedParameterJdbcTemplate getNpjt() {
		return npjt;
	}

	public RemoveAttachmentController() {
	}

	protected ModelAndView handleRequestInternal(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		response.setCharacterEncoding("UTF-8");
		response.setContentType("text/html; charset=UTF-8");
		request.setCharacterEncoding("UTF-8");

		String ret = "0";
		int act = Integer.parseInt(request.getParameter("act"));

		if (act == 0) {
			String fname = request.getParameter("fname");
			File t = new File("C://aisa//temp_save_docs//" + fname);
			t.delete();
			ret = "1";
		} else if (act == 1) {
			String fname = request.getParameter("fname");
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
			if (docid.compareToIgnoreCase("-1") != 0 && fname.length()>0) {
				Map p = new HashMap();
				p.put("docid", Integer.parseInt(docid));
				String[] filenames = fname.substring(0, fname.length()-3).split(":::");
				try {
					for (int i=0;i<filenames.length;i++){
						String idx = filenames[i].replace("data", "");
						p.put("idx", Integer.parseInt(idx));
						npjt.update("DELETE FROM SNT.ENCLOSEDDOCS WHERE DOCID = :docid AND ID = :idx",p);
					} 
					ret = "1";
				} catch (Exception e){
					e.printStackTrace();
				}
			}
		} else if (act == 2) {
			File test = new File("C:\\aisa\\temp_save_docs");
			if (test.exists()) {
				File[] files = test.listFiles();
				for (int i=0;i<files.length;i++){
					files[i].delete();
				}
			}
		}

		response.getWriter().write(ret);
		return null;
	}
}

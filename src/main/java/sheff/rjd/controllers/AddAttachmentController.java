package sheff.rjd.controllers;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Iterator;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;

import sheff.rjd.utils.Base64;

public class AddAttachmentController extends AbstractController {
	
	public AddAttachmentController(){}
	
	protected ModelAndView handleRequestInternal(HttpServletRequest request, HttpServletResponse response) throws Exception {
		response.setCharacterEncoding("UTF-8");
		response.setContentType("text/html; charset=UTF-8");
		request.setCharacterEncoding("UTF-8");
		
		MultipartHttpServletRequest msr = (MultipartHttpServletRequest) request;
		String num = msr.getParameter("num");
		Iterator<String> iterator = (Iterator<String>) msr.getFileNames();
		String ret = new String();
		while (iterator.hasNext()){
			try {
				MultipartFile f = msr.getFile(iterator.next());
				String fname = "data"+num;
				String name = f.getOriginalFilename();
				String type = f.getContentType();
				//String code = Base64.encodeBytes(f.getBytes(), Base64.GZIP);
				
				JSONObject o = new JSONObject();
				o.put("name", name);
				o.put("type", type);
				//o.put("code", code);
				o.put("fname", fname);
				
				File t = new File("C://aisa//temp_save_docs");
				if (!t.exists()){
					t.mkdir();
				}
				
			    FileOutputStream fos =  new FileOutputStream(new File("C://aisa//temp_save_docs//" + fname ));
			    fos.write(f.getBytes());
				fos.flush();
				fos.close();
				ret = o.toString();
			} catch (Exception e) {
				e.printStackTrace();
				ret = "";
			}
		}
		
		response.getWriter().write(ret);
		return null;
	}
	
}

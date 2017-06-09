package ru.aisa.etdadmin.controllers.excel;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class Export2Excel extends HttpServlet  {
	
	public Export2Excel(){
		super();
	}
	
	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}
	
	private final static int DEFAULT_COLUMN_WIDTH = 50;
	private final static String DEFAULT_FILENAME = "newdocument";
	
	private static Map<String, String> mpFileNames;
	static{
		mpFileNames = new HashMap<String, String>();
		mpFileNames.put("Группы", "groups");
		mpFileNames.put("Формы", "forms");
		mpFileNames.put("Предприятия", "preds");
		mpFileNames.put("Роли", "roles");
		mpFileNames.put("Права для форм", "form_rules");
		mpFileNames.put("Подписи", "signs");
		mpFileNames.put("URLs", "urls");
		mpFileNames.put("Пользователи", "users");
	}
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		ServletOutputStream out;
		String req = request.getParameter("params");
		String [] params = req.split("#@#");
		if(params.length>=3){
			String group = params[0];
			String [] columns = params[1].split("#!#");
			String [] data = params[2].split("#!#");
			
			int tableWidth = 0;
			int colWidth = 0;
			if(columns.length>0){
				response.setContentType("text/html");
				response.setCharacterEncoding("UTF-8");
				String filename = mpFileNames.get(group);
				if(filename==null) filename = DEFAULT_FILENAME;
				response.addHeader("Content-Disposition", "attachment; filename="+filename+".xls");
				
				out = response.getOutputStream();
				out.println("<html><head>");
				out.println("<meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\"></meta>");
				out.println("</head><body>");
				out.println("<br><br>");
				out.println("<h1>"+group+"<h1>");
				out.println("<br><br>");
				out.println("<table border = 1>");
				out.println("<tr>");
				
				String colHeader = "";
				int colSize = 0;
				String [] tmpHeaderArray = new String[2];
				int[] colSizeArray = new int[columns.length];
				for(int i=0; i<columns.length; i++){
					tmpHeaderArray = columns[i].split(":::");
					colHeader = tmpHeaderArray[0];
					try{
						colSize = Integer.parseInt(tmpHeaderArray[1]);
					}
					catch(NumberFormatException e){
						colSize = DEFAULT_COLUMN_WIDTH;
					}
					colSizeArray[i] = colSize;
					out.println("<td width="+colSize+" bgcolor=#000000><center><font color=#FFFFFF><b>"+colHeader+"</b></font><center></td>");
				}
				out.println("</tr>");
				
				
				String color = "";
				boolean whiteColor = false;
				for(int z=0; z<data.length; z++){
					whiteColor = !whiteColor;
					if(whiteColor) color = "#FFFFFF";
					else color = "#D9DADF";
					out.println("<tr bgcolor = "+color+">");
					String [] tmpDataArray = data[z].split(":::");
					for(int i=0; i<tmpDataArray.length; i++){
						out.println("<td>"+tmpDataArray[i]+"</td>");
					}
					out.println("</tr>");
				}
				
				out.println("</table>");
				out.println("</body></html>");
				out.close();
			}
			
		}
//		out.write("<tr>");
//		out.write("<td>121314</td>");
//		out.write("<td>qqqqq</td>");
//		out.write("</tr>");
//		out.write("<tr>");
//		out.write("<td>ja html</td>");
//		out.write("<td>zzzzzz</td>");
//		out.write("</tr>");
//		out.write("</table>");
		
	}
}

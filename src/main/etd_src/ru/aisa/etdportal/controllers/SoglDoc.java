package ru.aisa.etdportal.controllers;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.json.JSONArray;
import org.json.JSONException;

import com.aisa.portal.invoice.operator.obj.OperatorObject;

import ru.aisa.rgd.ws.documents.ETDForm;
import ru.aisa.rgd.ws.utility.DataBinder;
import ru.aisa.rgd.ws.utility.TypeConverter;


public class SoglDoc extends AbstractPortalSimpleController {
private OperatorObject oper;
	
	public OperatorObject getOper() {
		return oper;
	}

	public void setOper(OperatorObject oper) {
		this.oper = oper;
	}
	public SoglDoc() throws JSONException {
		super();
	}

	private static final String sql  = "update snt.docstore set visible = 2 where id =:id";
	private static final String sqltorg  = "update snt.docstore set readid = 1 where id =:id";
	private static final String namesql  = "select rtrim(name) from snt.doctype where id = (select typeid from snt.docstore where id =:id)";
	

	@Override
	public JSONArray get(HttpServletRequest request){
		
//		System.out.println("deop");
		final JSONArray response = new JSONArray();
		HashMap<String, Object> pp = new HashMap<String, Object>();
		if (request.getParameter("docid")!=null){
	
			try{
				pp.put("id",Integer.parseInt(request.getParameter("docid")));
				String formname = (String) getNpjt().queryForObject(namesql, pp, String.class);
				
				if (formname.equals("ТОРГ-12")||formname.contains("ФПУ-26 ППС")){
					getNpjt().update(sqltorg, pp);
					if (formname.contains("ФПУ-26 ППС")){
						pp.put("reqnum", "Согласовано");
						getNpjt().update("update snt.docstore set reqnum =:reqnum where id =:id", pp);
					
					try{
						 ETDForm form = null;
						 byte[] fpublob = getNpjt().queryForObject("select bldoc from snt.docstore where id =:id", pp, byte[].class);
						 form = ETDForm.createFromArchive(fpublob);
							DataBinder kinder = form.getBinder();
						long etdreestrid = Long.parseLong(kinder.getValue("P_46"));
						pp.put("etdreestrid", etdreestrid);
						getNpjt().update("update snt.docstore set reqnum =:reqnum where etdid =:etdreestrid", pp);
					}catch (Exception e){
						log.error(TypeConverter.exceptionToString(e));
					}
					
					
					
					}
					
//					if (formname.equals("ТОРГ-12")){
//						 ETDForm form = null;
//						 byte[] torgblob = getNpjt().queryForObject("select bldoc from snt.docstore where id =:id", pp, byte[].class);
//						 form = ETDForm.createFromArchive(torgblob);
//							DataBinder kinder = form.getBinder();
//						 if (kinder.getValue("P_13").length()==0)
//						 {
//						   System.out.println("savetorg");
//						     int torg_seq= getNpjt().queryForInt("select next value for SNT.TORG_SEQ from SNT.wrkname fetch first row only", pp);
//						   
//						     kinder.setNodeValue("P_13", torg_seq);
//						     kinder.setNodeValue("nameoper", oper.getNameUrLic());
//						     kinder.setNodeValue("innoper", oper.getInn());
//						     kinder.setNodeValue("idoper", oper.getId());  
//						     pp.put("NO", torg_seq);
//						     pp.put("BLDOC", form.encodeToArchiv());
//						     pp.put("DOCDATA", form.transform("data"));
//						     getNpjt().update("update snt.docstore set OPISANIE = :NO, BLDOC = :BLDOC, docdata = :DOCDATA where id = :id", pp);	    
//						}
//					}
					
				}
			
				else {
		getNpjt().update(sql, pp);
				}
		response.put(true);
		
			} catch (Exception e) {
				
				e.printStackTrace();
				response.put(true);
				}
//		System.out.println(response);
		}
		return response;
		
	}
	
	
}
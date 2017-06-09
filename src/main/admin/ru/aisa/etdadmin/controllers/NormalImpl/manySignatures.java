package ru.aisa.etdadmin.controllers.NormalImpl;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.ParameterizedRowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcTemplate;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;

import ru.aisa.etdadmin.Utils;
import ru.aisa.etdadmin.controllers.AbstractNormalController;

public class manySignatures extends AbstractNormalController {

	public manySignatures() throws JSONException {
		super();
	}


	@Override
	protected JSONObject add(HttpServletRequest request)
			throws JSONException {
		if(Utils.isReadonly()) return FORBIDDEN;
		final JSONObject response = new JSONObject();
		getTransT();
		final ArrayList<Integer> IdsWithoutNull = new ArrayList<Integer>();
		final ArrayList<Integer> IdsFrirstOtdel = new ArrayList<Integer>();
		final String form= request.getParameter("forms");
		final String[]  first=request.getParameter("otd_first").split(",");
		final String  firstOtdel=request.getParameter("otd_first");
		final String  last=request.getParameter("otd_last");
		final String [] forms=request.getParameter("forms").split(",");
		
//		System.out.println("forms: " +form);
//		System.out.println("first: "+ firstOtdel);
//		System.out.println("last: "+last);	
		
		getTransT().execute(new TransactionCallbackWithoutResult() {
		protected void doInTransactionWithoutResult(TransactionStatus status) {
			
			try{
					getNpjt().query("select id from SNT.doctype as dt where dt.id in ("+form+") and " +
				" dt.id not in ( select dtid from SNT.doctypeflow where dtid in ("+form+") and order=0 )", new HashMap(), new ParameterizedRowMapper<Object>() {

					public Object mapRow(ResultSet rs, int numrow)
							throws SQLException {
						IdsWithoutNull.add(rs.getInt("id"));
						return null;
					}
				});
		ArrayList<Object[]> aa = new ArrayList<Object[]>();
	if (IdsWithoutNull.size()>0)
		{	for (int iter=0;iter<IdsWithoutNull.size();iter++){
			
				aa.add(new Object[]{IdsWithoutNull.get(iter),0,first[0]});
			}
		
		getSjt().batchUpdate("INSERT INTO SNT.DOCTYPEFLOW (DTID,ORDER,WRKID,PARENT) values(?,?,?,null)", aa);
		}
	
	       getNpjt().query("select wrkid from SNT.doctypeflow as Wrkid where wrkid in ("+firstOtdel+") and " +
			" wrkid not in ( select wrkid from SNT.doctypeflow where dtid in ("+form+") and Wrkid in ("+firstOtdel+") and parent is not null) group by wrkid ", new HashMap(), new ParameterizedRowMapper<Object>() {

				public Object mapRow(ResultSet rs, int numrow)
						throws SQLException {
					IdsFrirstOtdel.add(rs.getInt("Wrkid"));
					return null;
				}
			});
		aa = new ArrayList<Object[]>();
	
			for (int iter=0;iter<forms.length;iter++){
				
				for (int i=0;i<IdsFrirstOtdel.size();i++){
				aa.add(new Object[]{Integer.parseInt(forms[iter]),Integer.parseInt(forms[iter]),IdsFrirstOtdel.get(i)});
				}
				}
			
		for (int i=0; i<aa.size();i++){
			getNpjt().update("INSERT INTO SNT.DOCTYPEFLOW (DTID,ORDER,WRKID,PARENT) values("+aa.get(i)[0]+"," +
				"(select max(ORDER)+1 from SNT.DOCTYPEFLOW where dtid="+aa.get(i)[1]+"),"+aa.get(i)[2]+"," +
						"(select case when (select max(order) from snt.doctypeflow where dtid = "+aa.get(i)[1]+" and wrkid = "+aa.get(i)[2]+") is null then 0"+ 
" else (select max(order) from snt.doctypeflow where dtid = "+aa.get(i)[1]+" and wrkid = "+aa.get(i)[2]+") end" +
" from snt.doctypeflow fetch first row only))", 
						new HashMap());
		}
	/*	getSjt().batchUpdate("INSERT INTO SNT.DOCTYPEFLOW (DTID,ORDER,WRKID,PARENT) values(?," +
				"(select max(ORDER)+1 from SNT.DOCTYPEFLOW where dtid=?),?,0)", aa);*/
		aa = new ArrayList<Object[]>();
		int iter=0;
		for (int i1=0;i1<forms.length;i1++){
			for (int i=0;i<first.length;i++){
					aa.add(new Object[]{Integer.parseInt(forms[i1]),Integer.parseInt(forms[i1]),last,Integer.parseInt(forms[i1]),first[i]});		
			}
		}
		for (int i=0; i<aa.size();i++){
			getNpjt().update("INSERT INTO SNT.DOCTYPEFLOW (DTID,ORDER,WRKID,PARENT) values("+aa.get(i)[0]+"," +
				"(select max(ORDER)+1 from SNT.DOCTYPEFLOW where dtid="+aa.get(i)[1]+"),"+aa.get(i)[2]+"," +
				"(select max(order) from SNT.doctypeflow where dtid="+aa.get(i)[3]+" and wrkid="+aa.get(i)[4]+"))", new HashMap());
		}
		
		/*getSjt().batchUpdate("INSERT INTO SNT.DOCTYPEFLOW (DTID,ORDER,WRKID,PARENT) values(?," +
				"(select max(ORDER)+1 from SNT.DOCTYPEFLOW where dtid=?),?,(select max(order) from SNT.doctypeflow where dtid=? and wrkid=?))", aa);
	*/
		
	//удаляем текущие права для отдела на выбранные документы
		/*
	ArrayList<Object[]> deleteList = new ArrayList<Object[]>();
	for(int i = 0; i<first.length; i++)
	{
		deleteList.add(new Object[]{last, forms[i]});
	}	
	getSjt().batchUpdate("delete from snt.doctypeacc where wrkid = ? " +
			"and dtid = ?", deleteList);
		
	//прописываем новые права для отдела на выбранные документы	
	Integer cnew = new Integer(0);
	Integer cview = new Integer(0);
	Integer cedit = new Integer(0);
	Integer funcrole = getSjt().queryForInt("select issm from snt.wrkname " +
			"where id = ? ", new Object[]{Integer.parseInt(last)});
	switch(funcrole){
	case 1:
	case 7:
		cnew = 1; cview = 1; cedit = 1;
		break;
	case 2:
	case 10:
	case 5:
	case 11:
		cview = 1; cedit = 1;
		break;
	case 3:
		cview = 1;
		break;
	}
		ArrayList<Object[]> rightslist = new ArrayList<Object[]>();
		for(int i = 0; i<first.length; i++)
		{
			rightslist.add(new Object[]{forms[i], last, cview, cedit, cnew});
		}
		getSjt().batchUpdate("insert into snt.doctypeacc (dtid, wrkid, cview, cedit, " +
				"cnew) values (?, ?, ?, ?,?)", rightslist);
		
		*/
		
		response.put(success, true);
			} catch(Exception e){
				   status.setRollbackOnly();
				   StringWriter outError = new StringWriter();
				   PrintWriter errorWriter = new PrintWriter( outError );
				   e.printStackTrace(errorWriter);
				   log.error(outError.toString());
				   try {
					response.put(success, false);
				} catch (JSONException e1) {
					
				}
			}
			
			
			
			}});

		return response;
		
		
	}

}

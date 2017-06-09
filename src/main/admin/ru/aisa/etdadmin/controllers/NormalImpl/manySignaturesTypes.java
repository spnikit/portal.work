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

public class manySignaturesTypes extends AbstractNormalController {

	public manySignaturesTypes() throws JSONException {
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
		final String rec= request.getParameter("rec");		
		final Integer  docdoctype=Integer.parseInt(request.getParameter("docdoctype"));		
		
		final List forms = getNpjt().queryForList("Select id, name as name from snt.doctype where doctypeid = "+docdoctype+" order by name ", new HashMap());
		final List sends = getNpjt().queryForList("Select id, name as name from snt.wrkname where doctypeid = "+docdoctype+" and issm = 1 order by name ", new HashMap());
		final List<Integer> idFormList = new ArrayList<Integer>();
		final List<Integer> idSendList = new ArrayList<Integer>();		
		for (int i = 0; i < forms.size(); i++) {
			idFormList.add((Integer) ((HashMap) forms.get(i)).get("ID"));
		}
		for (int i = 0; i < sends.size(); i++) {
			idSendList.add((Integer) ((HashMap) sends.get(i)).get("ID"));
		}
		final HashMap<String, List> idsFormMap = new HashMap<String, List>();
		idsFormMap.put("idFormList", idFormList);
		HashMap<String, List> idsSendMap = new HashMap<String, List>();
		idsFormMap.put("idSendList", idSendList);
		getTransT().execute(new TransactionCallbackWithoutResult() {
		protected void doInTransactionWithoutResult(TransactionStatus status) {
			
			try{
					getNpjt().query("select id from SNT.doctype as dt where dt.id in (:idFormList) and " +
				" dt.id not in ( select dtid from SNT.doctypeflow where dtid in (:idFormList) and order=0 )", idsFormMap, new ParameterizedRowMapper<Object>() {

					public Object mapRow(ResultSet rs, int numrow)
							throws SQLException {
						IdsWithoutNull.add(rs.getInt("id"));
						return null;
					}
				});
					
		ArrayList<Object[]> aa = new ArrayList<Object[]>();
	if (IdsWithoutNull.size()>0)
		{	for (int iter=0;iter<IdsWithoutNull.size();iter++){
				aa.add(new Object[]{IdsWithoutNull.get(iter),0,idSendList.get(0)});
			}
		
		getSjt().batchUpdate("INSERT INTO SNT.DOCTYPEFLOW (DTID,ORDER,WRKID,PARENT) values(?,?,?,null)", aa);
		}
	
	
	
	//цикл по количеству форм
	
	for (int f=0; f<idFormList.size(); f++){
	
	
	       getNpjt().query("select wrkid from SNT.doctypeflow as Wrkid where wrkid in (:idSendList) and " +
			" wrkid not in ( select wrkid from SNT.doctypeflow where dtid in (:idFormList) and Wrkid in (:idSendList) and parent is not null) group by wrkid ", idsFormMap, new ParameterizedRowMapper<Object>() {

				public Object mapRow(ResultSet rs, int numrow)
						throws SQLException {
					IdsFrirstOtdel.add(rs.getInt("Wrkid"));
					return null;
				}
			});
		aa = new ArrayList<Object[]>();
	
		//	for (int iter=0;iter<idFormList.size();iter++){
				
				for (int i=0;i<IdsFrirstOtdel.size();i++){
					aa.add(new Object[]{idFormList.get(f),idFormList.get(f),IdsFrirstOtdel.get(i)});
				
				}
			//	}
		//	System.out.println(aa.size());
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
		//for (int i1=0;i1<idFormList.size();i1++){
			for (int i=0;i<idSendList.size();i++){
					aa.add(new Object[]{idFormList.get(f),idFormList.get(f),rec,idFormList.get(f),idSendList.get(i)});		
			}
	//	}
		for (int i=0; i<aa.size();i++){
			  getNpjt().update("INSERT INTO SNT.DOCTYPEFLOW (DTID,ORDER,WRKID,PARENT) values("+aa.get(i)[0]+"," +
				"(select max(ORDER)+1 from SNT.DOCTYPEFLOW where dtid="+aa.get(i)[1]+"),"+aa.get(i)[2]+"," +
				"(select max(order) from SNT.doctypeflow where dtid="+aa.get(i)[3]+" and wrkid="+aa.get(i)[4]+"))", new HashMap());
		}
		
		/*getSjt().batchUpdate("INSERT INTO SNT.DOCTYPEFLOW (DTID,ORDER,WRKID,PARENT) values(?," +
				"(select max(ORDER)+1 from SNT.DOCTYPEFLOW where dtid=?),?,(select max(order) from SNT.doctypeflow where dtid=? and wrkid=?))", aa);
	*/
		
	//удаляем текущие права для отдела на выбранные документы
	}
	ArrayList<Object[]> deleteList = new ArrayList<Object[]>();
	for(int i = 0; i<idSendList.size(); i++)
	{
		deleteList.add(new Object[]{rec, idFormList.get(i)});
	}	
	getSjt().batchUpdate("delete from snt.doctypeacc where wrkid = ? " +
			"and dtid = ?", deleteList);
		
	int size = sends.size()+1;
	
		Object[] wrkIds = new Object[size];
	
	wrkIds[0] = Integer.parseInt(rec);
	for (int i=1; i<idSendList.size()+1;i++){
		wrkIds[i] = idSendList.get(i-1);
	}
	ArrayList<Object[]> rightslist = new ArrayList<Object[]>();
	
	for (int fr = 0; fr<idFormList.size(); fr++) 
	
	{
		
		for (int wrk=0; wrk<wrkIds.length; wrk++){
		
	//прописываем новые права для отдела на выбранные документы	
	Integer cnew = new Integer(0);
	Integer cview = new Integer(0);
	Integer cedit = new Integer(0);
	Integer funcrole = getSjt().queryForInt("select issm from snt.wrkname " +
			"where id = ? ", new Object[]{wrkIds[wrk]});
	switch(funcrole){
	case 1:
	cnew = 1; cview = 1; cedit = 1;
		break;
	case 2:
		cview = 1; cedit = 1;
		break;
	
	}
	rightslist.add(new Object[]{idFormList.get(fr), wrkIds[wrk], cview, cedit, cnew});
		}
		
	}
	
	
		getSjt().batchUpdate("insert into snt.doctypeacc (dtid, wrkid, cview, cedit, " +
				"cnew) values (?, ?, ?, ?,?)", rightslist);
		
		
		
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
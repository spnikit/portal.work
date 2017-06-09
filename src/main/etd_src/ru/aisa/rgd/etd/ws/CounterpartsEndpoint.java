/*package ru.aisa.rgd.etd.ws;


import java.io.PrintWriter;
import java.io.StringWriter;
import java.math.BigInteger;

import java.sql.ResultSet;
import java.sql.SQLException;

import java.sql.Types;

import java.util.HashMap;

import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.springframework.jdbc.core.SqlOutParameter;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.ParameterizedRowMapper;
import org.springframework.oxm.Marshaller;
import org.springframework.ws.server.endpoint.AbstractMarshallingPayloadEndpoint;


import ru.aisa.edt.CounterpartData;
import ru.aisa.edt.CounterpartsDocument;
import ru.aisa.edt.GetDocDocument;
import ru.aisa.edt.CounterpartsDocument.Counterparts;
import ru.aisa.edt.impl.CounterpartsDocumentImpl.CounterpartsImpl;

import sheff.rjd.dbobjects.DocObject;
import sheff.rjd.utils.MyStoredProc;
import sheff.rjd.utils.SecurityManager;
import zpss.rjd.dao.GetDocDao;

public class CounterpartsEndpoint extends AbstractMarshallingPayloadEndpoint {
    private SecurityManager securityManager;
	private NamedParameterJdbcTemplate npjt;
	public NamedParameterJdbcTemplate getNpjt() {
		return npjt;
	}


	public void setNpjt(NamedParameterJdbcTemplate npjt) {
		this.npjt = npjt;
	}
    private static Log	log	= Logger.getLogger(CounterpartsEndpoint.class);

	public SecurityManager getSecurityManager() {
		return securityManager;
	}

	public void setSecurityManager(SecurityManager securityManager) {
		this.securityManager = securityManager;
	}

	public CounterpartsEndpoint(Marshaller marshaller) {
		super(marshaller);
	}

	protected Object invokeInternal(Object obj) {
	
		final CounterpartsDocument gd = (CounterpartsDocument) obj;
		final Counterparts cp = gd.getCounterparts();
		
		int wrkid = cp.getWrkid();
		HashMap m = new HashMap();
		m.put("WRKID", wrkid);
		String sql = "select issm from LETD.wrkname where id = :WRKID";
		
		int fr = getNpjt().queryForInt(sql, m);
		
		if(fr == 0){
			sql ="select id, rtrim(name) name from LETD.wrkname where id in " +
			"(select wrkid from LETD.doctypeflow where wrkid<> :WRKID and parent in " +
			"(select order from LETD.doctypeflow where wrkid = :WRKID "+ 
"and parent is not null) )";
			getNpjt().query(sql, m, new ParameterizedRowMapper(){

				public Object mapRow(ResultSet rs, int arg1)
						throws SQLException {
					// TODO Auto-generated method stub
					CounterpartData cpd = cp.addNewData();
					cpd.setId(rs.getInt("id"));
					cpd.setName(rs.getString("name"));
										
					return null;
				}
				
			});
		}
		else if(fr == 1){
			sql ="select id, rtrim(name) name from LETD.wrkname where id in " +
					"(select wrkid from LETD.doctypeflow where wrkid<> :WRKID and order in " +
					"(select parent from LETD.doctypeflow where wrkid = :WRKID "+ 
			") )";
						getNpjt().query(sql, m, new ParameterizedRowMapper(){

							public Object mapRow(ResultSet rs, int arg1)
									throws SQLException {
								// TODO Auto-generated method stub
								CounterpartData cpd = cp.addNewData();
								cpd.setId(rs.getInt("id"));
								cpd.setName(rs.getString("name"));
													
								return null;
							}
							
						});
					}
		
		
		
		return gd;
	}



}
*/


package ru.aisa.rgd.etd.ws;

import java.util.ArrayList;
import java.util.List;

import org.springframework.oxm.Marshaller;

import ru.aisa.edt.CounterpartsDocument;
import ru.aisa.edt.CounterpartsResponseDocument;
import ru.aisa.edt.FormTypesRequestDocument;
import ru.aisa.edt.FormTypesResponseDocument;
import ru.aisa.edt.Signature;
import ru.aisa.edt.CounterpartsDocument.Counterparts;
import ru.aisa.edt.CounterpartsResponseDocument.CounterpartsResponse;
import ru.aisa.edt.FormTypesRequestDocument.FormTypesRequest;
import ru.aisa.edt.FormTypesResponseDocument.FormTypesResponse;
import ru.aisa.edt.FormTypesResponseDocument.FormTypesResponse.Type;
import ru.aisa.rgd.etd.dao.ETDFacade;
import ru.aisa.rgd.etd.objects.ETDCounterparts;
import ru.aisa.rgd.etd.objects.ETDTemplate;

public class CounterpartsEndpoint extends ETDAbstractSecurityEndoint<CounterpartsDocument, CounterpartsResponseDocument> {

	//Список форм отчетов
	private ArrayList<String> reportNames;
	
	public CounterpartsEndpoint(Marshaller marshaller) {
		super(marshaller);
	}

	@Override
	protected CounterpartsResponseDocument composeInvalidSecurityResponce(Signature signature) 
	{
		
		CounterpartsResponseDocument responseDocument = CounterpartsResponseDocument.Factory.newInstance();
		CounterpartsResponse response = responseDocument.addNewCounterpartsResponse();
		response.addNewSecurity().setLogonstatus("false");
		return responseDocument;
	}

	@Override
	protected CounterpartsDocument convertRequest(Object obj) 
	{
		
		CounterpartsDocument requestDocument = (CounterpartsDocument) obj;
		return requestDocument;
	}

	@Override
	protected Signature getSecurity(CounterpartsDocument requestDocument) 
	{
		
		Signature s = requestDocument.getCounterparts().getSecurity();
		return s;
	}

	@Override
	protected CounterpartsResponseDocument processRequest(CounterpartsDocument requestDocument, Signature signature, ETDFacade facade) throws Exception 
	{
		
		Counterparts request = requestDocument.getCounterparts();
		
		CounterpartsResponseDocument responseDocument = CounterpartsResponseDocument.Factory.newInstance();
		CounterpartsResponse response = responseDocument.addNewCounterpartsResponse();
		
		response.setSecurity(signature);

		
		List<ETDCounterparts> reportList = facade.getCounterpartsForRole(request.getPredid());
		
		for (ETDCounterparts templ : reportList)
		{
			ru.aisa.edt.CounterpartsResponseDocument.CounterpartsResponse.Type type = response.addNewType();
			type.setId(templ.getId());
			type.setName(templ.getName());
		}
		
		return responseDocument;
	}

	public ArrayList<String> getReportNames() {
		return reportNames;
	}

	public void setReportNames(ArrayList<String> reportNames) {
		this.reportNames = reportNames;
	}
	


}
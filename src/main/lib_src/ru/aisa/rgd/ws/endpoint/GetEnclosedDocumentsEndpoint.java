package ru.aisa.rgd.ws.endpoint;


import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.log4j.Logger;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.ParameterizedRowMapper;
import org.springframework.oxm.Marshaller;

import ru.aisa.edt.EnclosedDocDocument;
import ru.aisa.edt.EnclosedDocDocument.EnclosedDoc;
import ru.aisa.edt.Signature;
import ru.aisa.rgd.etd.dao.ETDFacade;
import ru.aisa.rgd.etd.ws.ETDAbstractSecurityEndoint;

public class GetEnclosedDocumentsEndpoint extends ETDAbstractSecurityEndoint<EnclosedDocDocument, EnclosedDocDocument> {//extends AbstractMarshallingPayloadEndpoint {
	private NamedParameterJdbcTemplate npjt;
    private DataSource ds;


    private static Logger	log	= Logger.getLogger(GetEnclosedDocumentsEndpoint.class);
 


	public DataSource getDs() {
		return ds;
	}

	public void setDs(DataSource ds) {
		this.ds = ds;
	}



	public GetEnclosedDocumentsEndpoint(Marshaller marshaller) {
		super(marshaller);
	}
	
	
	
	@Override
	protected EnclosedDocDocument composeInvalidSecurityResponce(Signature signature) 
	{
		EnclosedDocDocument responseDocument = EnclosedDocDocument.Factory.newInstance();
		EnclosedDoc response = responseDocument.addNewEnclosedDoc();
		
		response.addNewSecurity().setLogonstatus("false");
		return responseDocument;
	}

	@Override
	protected EnclosedDocDocument convertRequest(Object obj) {
		EnclosedDocDocument requestDocument = (EnclosedDocDocument) obj;
		return requestDocument;
	}

	@Override
	protected Signature getSecurity(EnclosedDocDocument requestDocument) 
	{
		Signature s = requestDocument.getEnclosedDoc().getSecurity();
		return s;
	}
	

	protected EnclosedDocDocument processRequest(EnclosedDocDocument requestDocument, Signature signature, ETDFacade facade) throws Exception{ //invokeInternal(Object obj) {
		final EnclosedDocDocument gd = (EnclosedDocDocument) requestDocument;
		final EnclosedDocDocument out=EnclosedDocDocument.Factory.newInstance();
		out.addNewEnclosedDoc();
//		System.out.println(gd);
		try{
			final Map pp = new HashMap();
			pp.put("DOCID", gd.getEnclosedDoc().getDocId());
			
			if (gd.getEnclosedDoc().getRequest().equals("getencdoc")){
				pp.put("ID", gd.getEnclosedDoc().getDocumentNameArray(0).replaceAll("data",""));
				 gd.getEnclosedDoc().removeDocumentName(0);
			    
					List result = getNpjt().query("select id, template from SNT.ENCLOSEDDOCS where DOCID=:DOCID and ID=:ID ",pp, new ParameterizedRowMapper<Object>() {

						public Object mapRow(ResultSet rs, int numrow) throws SQLException {
							out.getEnclosedDoc().addDocumentBl(new String(rs.getBytes("template")));
							out.getEnclosedDoc().addDocumentName("data"+rs.getString("id"));
							return null;
						} });
					if (out.getEnclosedDoc().getDocumentNameArray().length==0){
						out.getEnclosedDoc().setResponse("no_encdoc");
						
						return out;
					}
					out.getEnclosedDoc().setResponse("ok");
				return out;
				
			}
			
			
			////////////////////////////////////////////
			if (gd.getEnclosedDoc().getRequest().equals("update")){
		        List result = getNpjt().query("select DISTINCT id, fname, datagroup from SNT.encloseddocs where DOCID=:DOCID order by id",pp, new ParameterizedRowMapper<Object>() {
					
					public Object mapRow(ResultSet rs, int numrow) throws SQLException {
					
					out.getEnclosedDoc().addDocumentName("data"+rs.getString("id"));
					out.getEnclosedDoc().addDocumentFname(rs.getString("fname"));
					out.getEnclosedDoc().addDocumentDataGroup(rs.getString("datagroup"));
						return null;
					} });
				if (out.getEnclosedDoc().getDocumentNameArray().length==0|out.getEnclosedDoc().getDocumentDataGroupArray().length==0|out.getEnclosedDoc().getDocumentFnameArray().length==0){
					out.getEnclosedDoc().setResponse("no_doc");
					
					return out;
				}
				out.getEnclosedDoc().setResponse("ok");
			return out;
			
		}
		}
		catch (Exception e) {
			out.getEnclosedDoc().setResponse("error");
			 StringWriter outError = new StringWriter();
			   PrintWriter errorWriter = new PrintWriter( outError );
			   e.printStackTrace(errorWriter);
			   log.error(outError.toString());
		}
		return out;
	}


	public NamedParameterJdbcTemplate getNpjt() {
		return npjt;
	}


	public void setNpjt(NamedParameterJdbcTemplate npjt) {
		this.npjt = npjt;
	}

}

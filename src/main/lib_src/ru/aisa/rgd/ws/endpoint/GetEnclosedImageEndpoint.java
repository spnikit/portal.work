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

import ru.aisa.edt.ImageDocDocument;
import ru.aisa.edt.ImageDocDocument.ImageDoc;
import ru.aisa.edt.Signature;
import ru.aisa.rgd.etd.dao.ETDFacade;
import ru.aisa.rgd.etd.ws.ETDAbstractSecurityEndoint;

public class GetEnclosedImageEndpoint extends ETDAbstractSecurityEndoint<ImageDocDocument, ImageDocDocument> {//extends AbstractMarshallingPayloadEndpoint {
	/**
	 * @uml.property  name="npjt"
	 * @uml.associationEnd  
	 */
	private NamedParameterJdbcTemplate npjt;

    /**
     * @uml.property  name="ds"
     * @uml.associationEnd  
     */
    private DataSource ds;


    private static Logger	log= Logger.getLogger(GetEnclosedImageEndpoint.class);
 


	/**
	 * @return
	 * @uml.property  name="ds"
	 */
	public DataSource getDs() {
		return ds;
	}

	/**
	 * @param ds
	 * @uml.property  name="ds"
	 */
	public void setDs(DataSource ds) {
		this.ds = ds;
	}



	public GetEnclosedImageEndpoint(Marshaller marshaller) {
		super(marshaller);
	}
	
	
	
	@Override
	protected ImageDocDocument composeInvalidSecurityResponce(Signature signature) 
	{
		ImageDocDocument responseDocument = ImageDocDocument.Factory.newInstance();
		ImageDoc response = responseDocument.addNewImageDoc();
		
		response.addNewSecurity().setLogonstatus("false");
		return responseDocument;
	}

	@Override
	protected ImageDocDocument convertRequest(Object obj) {
		ImageDocDocument requestDocument = (ImageDocDocument) obj;
		return requestDocument;
	}

	@Override
	protected Signature getSecurity(ImageDocDocument requestDocument) 
	{
		Signature s = requestDocument.getImageDoc().getSecurity();
		return s;
	}
	

	protected ImageDocDocument processRequest(ImageDocDocument requestDocument, Signature signature, ETDFacade facade) throws Exception{//invokeInternal(Object obj) {
		
	
		final ImageDocDocument gd = (ImageDocDocument) requestDocument;
		final ImageDocDocument out=ImageDocDocument.Factory.newInstance();
		out.addNewImageDoc(); 
		System.out.println("enclimg");
		try{
		    
		   
			final Map pp = new HashMap();
			pp.put("DOCID", gd.getImageDoc().getDocId());
			
			//pp.put("DOCID", 39875);
			
			
			if (gd.getImageDoc().getRequest().equals("getimg")){
				pp.put("IMAGEID", gd.getImageDoc().getImageIdArray(0));
				
				
		       	gd.getImageDoc().removeImageId(0);
					List result = getNpjt().query("select imageid, template from SNT.images where DOCID=:DOCID and imageid=:IMAGEID",pp, new ParameterizedRowMapper<Object>() {

						public Object mapRow(ResultSet rs, int numrow) throws SQLException {
							out.getImageDoc().addImageBl(new String(rs.getBytes("template")));
							out.getImageDoc().addImageId(rs.getInt("imageid"));
							
							return null;
							
							
						} });
					if (out.getImageDoc().getImageIdArray().length==0){
						out.getImageDoc().setResponse("no_image");
						return out;
					}
					out.getImageDoc().setResponse("ok");
				return out;
				
			}
			if (gd.getImageDoc().getRequest().equals("update")){
			       	gd.getImageDoc().removeImageId(0);
					List result = getNpjt().query("select imageid from SNT.images where DOCID=:DOCID order by imageid",pp, new ParameterizedRowMapper<Object>() {

						public Object mapRow(ResultSet rs, int numrow) throws SQLException {
							out.getImageDoc().addImageId(rs.getInt("imageid"));
							return null;
						} });
					if (out.getImageDoc().getImageIdArray().length==0){
						out.getImageDoc().setResponse("no_image");
						return out;
					}
					out.getImageDoc().setResponse("ok");
				return out;
				
			}
	
			
		}
		catch (Exception e) {
			   out.getImageDoc().setResponse("error");
			   StringWriter outError = new StringWriter();
			   PrintWriter errorWriter = new PrintWriter( outError );
			   e.printStackTrace(errorWriter);
			   log.error(outError.toString());
		}
		return out;
	}


	/**
	 * @return
	 * @uml.property  name="npjt"
	 */
	public NamedParameterJdbcTemplate getNpjt() {
		return npjt;
	}


	/**
	 * @param npjt
	 * @uml.property  name="npjt"
	 */
	public void setNpjt(NamedParameterJdbcTemplate npjt) {
		this.npjt = npjt;
	}

}

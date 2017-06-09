package sheff.rjd.gvcservice;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.log4j.Logger;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.ParameterizedRowMapper;
import org.springframework.oxm.Marshaller;
import org.springframework.ws.server.endpoint.AbstractMarshallingPayloadEndpoint;



import rzd.util.EgrpoRequestDocument;
import rzd.util.EgrpoResponseDocument;


public class GetEgrpo extends AbstractMarshallingPayloadEndpoint {
    private NamedParameterJdbcTemplate npjt;
    private static Logger	log	= Logger.getLogger(GetEgrpo.class);
	public  GetEgrpo(Marshaller marshaller) {
		super(marshaller);
	}
	
	public NamedParameterJdbcTemplate getNpjt() {
		return npjt;
	}

	public void setNpjt(NamedParameterJdbcTemplate npjt) {
		this.npjt = npjt;
	}

	protected Object invokeInternal(final Object inn)  {
		
	    
		EgrpoRequestDocument doc = (EgrpoRequestDocument) inn;
		String inn1 = doc.getEgrpoRequest().getInn();
		String kpp = doc.getEgrpoRequest().getKpp();
		EgrpoResponseDocument resp = EgrpoResponseDocument.Factory.newInstance();
		resp.addNewEgrpoResponse();		
		
		
		try
		{	
		    
		    Map pp = new HashMap();
		    
		    pp.put("inn", inn1);
		    pp.put("kpp", kpp);
		  // List l = npjt.queryForList("select rtrim(name) name, adr, tel, fax from snt.egrpo where inn = :inn and kpp=:kpp", pp);
		  
		   List l = npjt.queryForList("select rtrim(name1) as name, rtrim(pstlz) || ', ' || rtrim(Ort01) || ', ' || rtrim(Stras)"+
		  " as adr, rtrim(Telf1) as tel,rtrim(Telfx) as fax from snt.contragents_load_all where Stcd1 = :inn and Telbx=:kpp",pp);
		   if (l.size()>0)
		   { 
		       
		       Map egrpo = (HashMap) l.get(0);
		 
		 //  sb.append(egrpo.get("NAME")+", "+egrpo.get("ADR")+", "+egrpo.get("TEL")+", "+egrpo.get("FAX"));
		   resp.getEgrpoResponse().setName(egrpo.get("NAME").toString());   
		   resp.getEgrpoResponse().setAdr(egrpo.get("ADR").toString());
		   resp.getEgrpoResponse().setTel(egrpo.get("TEL").toString());
		   resp.getEgrpoResponse().setFax(egrpo.get("FAX").toString());
		  
		   
		   System.out.println(resp);
		   }
		   
		   else {
		       resp.getEgrpoResponse().setAdr("");		
		  resp.getEgrpoResponse().setName("");	
		  resp.getEgrpoResponse().setFax("");
		  resp.getEgrpoResponse().setTel("");
		  }
		}
	
		catch (Exception e) {
			StringWriter outError = new StringWriter();
			PrintWriter errorWriter = new PrintWriter( outError );
			e.printStackTrace(errorWriter);
			log.error(outError.toString());
			//datetime = "error";
			 resp.getEgrpoResponse().setAdr("");		
			  resp.getEgrpoResponse().setName("");	
			  resp.getEgrpoResponse().setFax("");
			  resp.getEgrpoResponse().setTel("");
			}
	
		return resp;		
	}
}


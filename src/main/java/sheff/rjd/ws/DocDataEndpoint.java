package sheff.rjd.ws;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.log4j.Logger;
import org.apache.xmlbeans.XmlException;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.ParameterizedRowMapper;
import org.springframework.oxm.Marshaller;
import org.springframework.ws.server.endpoint.AbstractMarshallingPayloadEndpoint;

import ru.aisa.rgd.etd.extend.SNMPSender;
import rzd8888.gvc.etd.was.etd.docdata.DocDataRequestDocument;
import rzd8888.gvc.etd.was.etd.docdata.DocDataResponseDocument;
import rzd8888.gvc.etd.was.etd.docdata.XMLData;

public class DocDataEndpoint extends AbstractMarshallingPayloadEndpoint {
	 private NamedParameterJdbcTemplate npjt;
	 private static Logger log = Logger.getLogger(DocDataEndpoint.class);
	 
	 public DocDataEndpoint(Marshaller marshaller) {
			super(marshaller);
		};
	
		private boolean checkPass(String login,String password){
			//System.out.println(login + " " + password);
			//System.out.println(login == "etd-docdata" && password == "megapassword");
			return login.equals("etd-docdata") && password.equals("\\qR10D+V");
		}
	@Override
	protected Object invokeInternal(Object arg0) throws Exception {
		DocDataRequestDocument dd = (DocDataRequestDocument) arg0;
		final DocDataResponseDocument dr = DocDataResponseDocument.Factory.newInstance();
		dr.addNewDocDataResponse();
		HashMap<String,Long> hm = new HashMap<String, Long>(1);
		String login = dd.getDocDataRequest().getLogin();
		String password = dd.getDocDataRequest().getPassword();
		if(!checkPass(login, password)){
			dr.getDocDataResponse().setCode(3);
			dr.getDocDataResponse().setDescription("login and password incorrect");
			return dr;
		}
		hm.put("DOCID", dd.getDocDataRequest().getDocid());
		String sql = "select case when signlvl is null then 1 "+
			" else 0  end as signlvl,docdata,typeid," +
			" (select name from letd.doctype b where b.id = a.typeid) " +
			" from letd.docstore a where id = :DOCID";
		try{
			if(getNpjt().query(sql, hm, new ParameterizedRowMapper<Object>() {

			public Object mapRow(ResultSet rs, int numrow) throws SQLException {	
				dr.getDocDataResponse().setDocname(rs.getString("NAME"));
				dr.getDocDataResponse().setDoctype(rs.getInt("TYPEID"));
				dr.getDocDataResponse().setIsFinished(rs.getInt("SIGNLVL") == 1);
				XMLData nt;
				try {
					nt = XMLData.Factory.parse(rs.getString("DOCDATA"));
					dr.getDocDataResponse().setDocdata(nt);
				} catch (XmlException e) {
					StringWriter outError = new StringWriter();
					PrintWriter errorWriter = new PrintWriter(outError);
					e.printStackTrace(errorWriter);
					log.error(outError.toString());
				}
				return null;
			}
		}).size()==1) {
				dr.getDocDataResponse().setCode(0);
				dr.getDocDataResponse().setDescription("success");
			}else{
				dr.getDocDataResponse().setCode(1);
				dr.getDocDataResponse().setDescription("Документ не существует");
			}
		}catch (Exception e) {
			StringWriter outError = new StringWriter();
			PrintWriter errorWriter = new PrintWriter(outError);
			e.printStackTrace(errorWriter);
			log.error(outError.toString());
			SNMPSender.sendMessage(e);
			dr.getDocDataResponse().setCode(2);
			dr.getDocDataResponse().setDescription(e.getMessage());
		}
		return dr;
	}
	public NamedParameterJdbcTemplate getNpjt() {
		return npjt;
	}
	public void setNpjt(NamedParameterJdbcTemplate npjt) {
		this.npjt = npjt;
	}

}

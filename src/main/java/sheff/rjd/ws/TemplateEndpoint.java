package sheff.rjd.ws;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.ResultSet;
import java.sql.SQLException;
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

import ru.aisa.edt.GetTemplateDocument;
import ru.aisa.edt.GetTypesDocument;
import ru.aisa.edt.GetTypesDocument.GetTypes;
import ru.aisa.edt.GetTypesDocument.GetTypes.Type;

public class TemplateEndpoint extends AbstractMarshallingPayloadEndpoint {
	private NamedParameterJdbcTemplate npjt;
	 private static Logger	log	= Logger.getLogger(TemplateEndpoint.class);

	public NamedParameterJdbcTemplate getNpjt() {
		return npjt;
	}
	public void setNpjt(NamedParameterJdbcTemplate npjt) {
		this.npjt = npjt;
	}
	public TemplateEndpoint(Marshaller marshaller) {
		super(marshaller);
	}
	protected Object invokeInternal(Object obj) {
		try{
		GetTemplateDocument trd = (GetTemplateDocument )obj;
		String template = trd.getGetTemplate().getName();
		GetTypesDocument tr = GetTypesDocument.Factory.newInstance();
		final GetTypes trr= tr.addNewGetTypes();
		trr.setWrkid(-1);
		Map pp = new HashMap();
		pp.put("TEMPLATE", template);
		List result = getNpjt().query("select id, rtrim(name) as name,template from letd.DocType where name = :TEMPLATE ",pp, new ParameterizedRowMapper<Object>() {

			public Object mapRow(ResultSet rs, int numrow) throws SQLException {
				Type tmp = trr.addNewType();
				tmp.setId(rs.getInt("id"));
				tmp.setName(rs.getString("name"));
				tmp.setTemplate(rs.getBytes("template"));
				return null;
			}
			
		});
		//System.out.println(trr.getTypeArray(0).getTemplate().toString());
		trr.getTypeArray(0).setTemplate(
				//templ.prepareTemplate(trr.getTypeArray(0).getName(),
						trr.getTypeArray(0).getTemplate());
						//,trd.getGetTemplate().getPredid()));
		return tr;
		}
		catch (Exception e) {
			StringWriter outError = new StringWriter();
			   PrintWriter errorWriter = new PrintWriter( outError );
			   e.printStackTrace(errorWriter);
			   log.error(outError.toString());
			return null;
		}
	
	}

}

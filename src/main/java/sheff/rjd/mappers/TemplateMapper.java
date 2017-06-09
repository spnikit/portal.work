package sheff.rjd.mappers;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.log4j.Logger;
import org.springframework.jdbc.core.simple.ParameterizedRowMapper;

import sheff.rjd.dbobjects.DocObject;
import sheff.rjd.utils.Base64;


public class TemplateMapper  implements ParameterizedRowMapper<DocObject> {
	private static Logger log = Logger.getLogger(TemplateMapper.class);
	public DocObject mapRow(ResultSet rs, int arg1) throws SQLException {
		try{
			DocObject out=new DocObject();
			//out.setDoc(rs.getBytes("template"));
			byte[] tt=rs.getBytes("template");
			out.setDoc(Base64.encodeBytes(tt, Base64.GZIP));
			return out;
		}
		catch (Exception e) {
			 StringWriter outError = new StringWriter();
			   PrintWriter errorWriter = new PrintWriter( outError );
			   e.printStackTrace(errorWriter);
			   log.error(outError.toString());
		}
		return null;
	}
}

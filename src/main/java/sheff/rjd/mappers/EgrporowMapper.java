package sheff.rjd.mappers;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.log4j.Logger;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcTemplate;

import rzd8888.gvc.etd.was.etd.etdegrposync.Table;
import sheff.rjd.mappers.SqlSync;



public class EgrporowMapper {
    private SimpleJdbcTemplate jt;
    private static Logger log = Logger.getLogger(EgrporowMapper.class);
    
    public EgrporowMapper(SimpleJdbcTemplate sjt){
	jt = sjt;
    }
    public void processRow(List<Table> a) throws SQLException {
        try{
           
           for (int i =0; i<a.size(); i++){
           
            String tmp = SqlSync.sqlnew[0]
            .replaceAll("#OKPO_KOD",String.valueOf(a.get(i).getOkpo()))
            .replaceAll("#NAME", a.get(i).getName())
             .replaceAll("#INN", a.get(i).getInn())
            .replaceAll("#ADR", a.get(i).getAdr())
         .replaceAll("#TEL", a.get(i).getTel())
         .replaceAll("#EMAIL", a.get(i).getEmail())
         .replaceAll("#FAX", a.get(i).getFax())
         .replaceAll("#KPP", a.get(i).getKpp());
         
            jt.update(tmp);
           }
            
        } catch ( Exception e ) {
            StringWriter outError = new StringWriter();
            PrintWriter errorWriter = new PrintWriter(outError);
            e.printStackTrace(errorWriter);
            log.error("egrposync " + outError.toString());

        }
    }
    
}
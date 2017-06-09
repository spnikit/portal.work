package sheff.rjd.gvcservice;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.log4j.Logger;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.oxm.Marshaller;
import org.springframework.ws.server.endpoint.AbstractMarshallingPayloadEndpoint;

import rzd.util.InnRequestDocument;
import rzd.util.InnResponseDocument;
import rzd.util.InnResponseDocument.InnResponse;

public class GetNameINN extends AbstractMarshallingPayloadEndpoint {
    private NamedParameterJdbcTemplate npjt;
    private static Logger log = Logger.getLogger(GetNameINN.class);

    public GetNameINN(Marshaller marshaller) {
	super(marshaller);
    }

    public NamedParameterJdbcTemplate getNpjt() {
	return npjt;
    }

    public void setNpjt(NamedParameterJdbcTemplate npjt) {
	this.npjt = npjt;
    }

    protected Object invokeInternal(final Object inn) {

	InnRequestDocument doc = (InnRequestDocument) inn;

	InnResponseDocument resp = InnResponseDocument.Factory.newInstance();
	InnResponse respresp = resp.addNewInnResponse();
	try {

	    HashMap pp = new HashMap();

	    pp.put("okpo", Integer.parseInt(doc.getInnRequest().getOkpo()));

	   /* List l = npjt
		    .queryForList(
			    "select rtrim(name)name,  inn from snt.egrpo where okpo_kod = :okpo",
			    pp);*/
	    List l = npjt
			    .queryForList("select rtrim(name1) as name, rtrim(Stcd1) as inn, telbx from snt.contragents_load_all where Stcd2 = :okpo",pp);
	    if (l.size() > 0) {

		Map egrpo = (HashMap) l.get(0);
		respresp.setInn(egrpo.get("INN").toString());
		respresp.setName(egrpo.get("NAME").toString());

	    }

	    else {
		respresp.setInn("");
		respresp.setName("");
	    }
	}

	catch (Exception e) {
	    StringWriter outError = new StringWriter();
	    PrintWriter errorWriter = new PrintWriter(outError);
	    e.printStackTrace(errorWriter);
	    log.error(outError.toString());
	    // datetime = "error";
	    respresp.setInn("");
	    respresp.setName("");
	}

	return resp;
    }
}

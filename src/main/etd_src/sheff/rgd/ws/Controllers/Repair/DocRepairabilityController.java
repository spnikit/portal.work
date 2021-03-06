package sheff.rgd.ws.Controllers.Repair;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;

import javax.xml.transform.TransformerException;

import org.apache.log4j.Logger;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.w3c.dom.DOMException;

import ru.aisa.rgd.etd.dao.ETDSyncServiceFacade;
import ru.aisa.rgd.etd.objects.ETDDocumentData;
import ru.aisa.rgd.ws.exeption.InternalException;
import ru.aisa.rgd.ws.exeption.ServiceException;
import sheff.rgd.ws.Abstract.AbstractAction;
import sheff.rjd.services.repair.TOREK_DocumentObject;
import sheff.rjd.services.syncutils.SendToVRKService;
import sheff.rjd.services.syncutils.SyncObj;

public class DocRepairabilityController extends AbstractAction{
	
	private String parentform;
	private ETDSyncServiceFacade etdsyncfacade;
	private NamedParameterJdbcTemplate npjt;
	private SendToVRKService signvrkservice;

	protected final Logger	log	= Logger.getLogger(getClass());

	public NamedParameterJdbcTemplate getNpjt() {
		return npjt;
	}

	public void setNpjt(NamedParameterJdbcTemplate npjt) {
		this.npjt = npjt;
	}

	public String getParentform() {
		return parentform;
	}

	public ETDSyncServiceFacade getEtdsyncfacade() {
		return etdsyncfacade;
	}

	public void setEtdsyncfacade(ETDSyncServiceFacade etdsyncfacade) {
		this.etdsyncfacade = etdsyncfacade;
	}

	public void setParentform(String parentform) {
		this.parentform = parentform;
	}
	
	public SendToVRKService getSignvrkservice() {
		return signvrkservice;
	}

	public void setSignvrkservice(SendToVRKService signvrkservice) {
		this.signvrkservice = signvrkservice;
	}
	
	
	@Override
	public void doAfterSign(String docName, String docdata, int predid, int signNumber, long id, String certserial, int WrkId) {
		
		Map<String, Object> hm1 = new HashMap<String, Object>();
		hm1.put("id", id);
		hm1.put("certserial", new BigInteger(certserial,16).toString());
		int drop = getNpjt().queryForInt("select CASE when dropid is null then 0 else 1 end from snt.docstore where id = :id", hm1);

		if (drop==1){
			try{
				final Map<String, Comparable> pp = new HashMap<String, Comparable>();
				String getOpisanieByIdSql = "select opisanie from snt.docstore where id = :id";
				String getFioByCertSerialSql = "select rtrim(cast(rtrim(fname) || ' ' || rtrim(mname) || ' ' || rtrim(lname) as char(240) ccsid unicode)) as name "
												+"from SNT.personall where trim(CERTSERIAL) = :certserial";
				String getDateByIdSql = "select dt from snt.vrkids where docid = :id";
				String getTorekIdByIdSql = "select torekid from snt.vrkids where docid = :id";
				pp.put("DOCID", id);
				pp.put("content", "Комплект отклонен");
				String opisanie = getNpjt().queryForObject(getOpisanieByIdSql, hm1, String.class);
				npjt.update("update snt.vrkids set dt = current timestamp where docid =:DOCID", pp);
				npjt.update("update snt.docstore set opisanie =:content where id =:DOCID", pp);

				String droptxt = getNpjt().queryForObject("select droptxt from snt.docstore where id = :DOCID", pp, String.class);
				String fio = getNpjt().queryForObject(getFioByCertSerialSql, hm1, String.class);
				String date = getNpjt().queryForObject(getDateByIdSql, hm1, String.class);
				String torekId = getNpjt().queryForObject(getTorekIdByIdSql, hm1, String.class);
				
				if( ("Комплект ремонтопригодности".equals(docName) && "Исходящий комплект".equals(opisanie))) {
					TOREK_DocumentObject torekObject = new TOREK_DocumentObject(id, date, fio, droptxt, docName);

					signvrkservice.SendDocs(3, torekObject);
					
				}

			} catch (Exception e){
				e.printStackTrace();
			}
		}


	}

	@Override
	public void doAfterSave(String docName, String docdata, int predid, int signNumber, String CertID, long id, int WrkId) throws Exception {}

	@Override
	public void doAfterSync(String formname, SyncObj syncobj, String sql, int signum) {}

	

	public ETDDocumentData EditBeforeOpen(String docName, ETDDocumentData doc) {
		// TODO Auto-generated method stub
		return doc;
	}

}

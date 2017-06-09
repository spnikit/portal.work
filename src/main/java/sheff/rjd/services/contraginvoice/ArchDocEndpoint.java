package sheff.rjd.services.contraginvoice;

import java.math.BigInteger;

import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.oxm.Marshaller;

import ru.aisa.rgd.ws.dao.ServiceFacade;
import ru.aisa.rgd.ws.documents.StandartResponseWrapper;
import ru.aisa.rgd.ws.domain.Document;
import ru.aisa.rgd.ws.endpoint.ETDAbstractEndpoint;
import ru.aisa.rgd.ws.endpoint.ResponseAdapter;
import ru.aisa.rgd.ws.exeption.ServiceCode;
import ru.aisa.rgd.ws.exeption.ServiceError;
import ru.aisa.rgd.ws.exeption.ServiceException;
import ru.iit.portal.contraginvoice.ArchDocRequestDocument;
import ru.iit.portal.contraginvoice.ArchDocRequestDocument.ArchDocRequest;
import ru.iit.portal.contraginvoice.ArchDocResponseDocument;
import ru.iit.portal.contraginvoice.ArchDocResponseDocument.ArchDocResponse;
public class ArchDocEndpoint extends ETDAbstractEndpoint<ArchDocwrapper>{
	private NamedParameterJdbcTemplate npjt;
	  

    public NamedParameterJdbcTemplate getNpjt() {
	return npjt;
    }

    public void setNpjt(NamedParameterJdbcTemplate npjt) {
	this.npjt = npjt;
    }

    
    public ArchDocEndpoint(Marshaller marshaller){
    	super(marshaller);
    }
    
	
	@Override
	protected ResponseAdapter<ArchDocwrapper> processRequest(Object arg, ServiceFacade facade)
			throws Exception {
	
		ArchDocRequestDocument requestdoc = (ArchDocRequestDocument)arg;
		ArchDocRequest req = requestdoc.getArchDocRequest();
		String inn = req.getInn();
		String kpp = req.getKpp();
		String cert = new BigInteger(req.getCertSn().replaceAll(" ", ""), 16).toString();
		Long docid = req.getDocid();
	
		
		if (facade.getContragDao().regcount(cert, inn ,kpp)==0)
			throw new ServiceException(new Exception(), ServiceError.ERR_CHECK_PRED_BY_CERT);

		if (facade.getContragDao().rightid(docid, inn, kpp)==0)
			throw new ServiceException(new Exception(), ServiceError.ERR_WRONG_DOCID);
		
		if (!facade.getContragDao().checkifarchieve(docid))
			throw new ServiceException(new Exception(), ServiceError.ERR_NOTARCHIVE);
		
		
		Document doc = facade.getDocumentDao().getByEtdId(docid);
		
//		System.out.println(doc.getType());
		
		ArchDocwrapper wrapper = new ArchDocwrapper();
		wrapper.setBldoc(new String(doc.getBlDoc(), "UTF-8"));
		wrapper.setDescription("ok");
		wrapper.setCode(ServiceCode.ERR_OK);
		wrapper.setDocumentId(docid);
		wrapper.setDocstatus(facade.getContragDao().getDocstatus(docid));
		ResponseAdapter<ArchDocwrapper> adapter = new ResponseAdapter<ArchDocwrapper>(
				true, wrapper, ServiceError.ERR_OK);
		return adapter;
	}

	@Override
	protected Object composeResponce(ResponseAdapter<ArchDocwrapper> adapter) {
		ArchDocResponseDocument respdoc = ArchDocResponseDocument.Factory.newInstance();
		ArchDocResponse resp = respdoc.addNewArchDocResponse();
		
		if (adapter.isStatus()){
			resp.setBldoc(adapter.getResponse().getBldoc());
			resp.setCode(adapter.getResponse().getCode());
			resp.setDescription(adapter.getResponse().getDescription());
			resp.setDocid(adapter.getResponse().getDocumentId());
			resp.setStatus(adapter.getResponse().getDocstatus());
			
		} else {
			resp.setCode(adapter.getError().getCode());
			resp.setDescription(adapter.getError().getMessage());
			resp.setDocid(-1);
			resp.setStatus(-1);
		}
		
		
		return respdoc;
	}



}
class ArchDocwrapper extends StandartResponseWrapper{
	String bldoc;
	int docstatus;
	public String getBldoc() {
		return bldoc;
	}

	public void setBldoc(String bldoc) {
		this.bldoc = bldoc;
	}

	public int getDocstatus() {
		return docstatus;
	}

	public void setDocstatus(int docstatus) {
		this.docstatus = docstatus;
	}

}

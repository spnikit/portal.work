package sheff.rjd.services.contraginvoice;

import java.math.BigInteger;

import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.oxm.Marshaller;

import ru.aisa.rgd.ws.dao.ServiceFacade;
import ru.aisa.rgd.ws.documents.StandartResponseWrapper;
import ru.aisa.rgd.ws.domain.Document;
import ru.aisa.rgd.ws.endpoint.ContragAbstractEndpoint;
import ru.aisa.rgd.ws.endpoint.ResponseAdapter;
import ru.aisa.rgd.ws.exeption.ServiceCode;
import ru.aisa.rgd.ws.exeption.ServiceError;
import ru.aisa.rgd.ws.exeption.ServiceException;
import ru.aisa.rgd.ws.utility.TypeConverter;
import ru.iit.portal.contraginvoice.DocDataRequestDocument;
import ru.iit.portal.contraginvoice.DocDataRequestDocument.DocDataRequest;
import ru.iit.portal.contraginvoice.DocDataResponseDocument;
import ru.iit.portal.contraginvoice.DocDataResponseDocument.DocDataResponse;



public class DocDataEndpoint extends
	ContragAbstractEndpoint<DocDataWrapper> {

       private NamedParameterJdbcTemplate npjt;

  
	public NamedParameterJdbcTemplate getNpjt() {
	return npjt;
    }

    public void setNpjt(NamedParameterJdbcTemplate npjt) {
	this.npjt = npjt;
    }

    public DocDataEndpoint(Marshaller marshaller) {
	super(marshaller);
    }

   
  
	@Override
	protected ResponseAdapter<DocDataWrapper> processRequest(Object arg, ServiceFacade facade)
			throws Exception {

		DocDataRequestDocument requestdoc = (DocDataRequestDocument) arg;
    	DocDataRequest request = requestdoc.getDocDataRequest();
   		String inn = request.getInn();
		String kpp = request.getKpp();
		String cert = "";
		try{
			 cert = new BigInteger(request.getCertSn().replaceAll(" ", ""), 16).toString();
		} catch (Exception e){
			log.error(TypeConverter.exceptionToString(e));
		}
		Long docid = request.getDocid();
	
		
		if (facade.getContragDao().regcount(cert, inn ,kpp)==0)
			throw new ServiceException(new Exception(), ServiceError.ERR_CHECK_PRED_BY_CERT);
		
		if (facade.getContragDao().rightid(docid, inn, kpp)==0)
			throw new ServiceException(new Exception(), ServiceError.ERR_WRONG_DOCID);
		
		
		
		Document doc = null;
		
		try{
			
			
			doc = facade.getDocumentDao().getByEtdId(docid);
			
		} catch (Exception e){
			throw new ServiceException(e, ServiceError.ERR_DOCDATA);
		}
		
		
       
		DocDataWrapper wrapper = new DocDataWrapper();
		wrapper.setDescription("ok");
		wrapper.setCode(ServiceCode.ERR_OK);
		wrapper.setDocumentId(docid);
		wrapper.setDocdata(doc.getDocData());
		wrapper.setBldoc(new String(doc.getBlDoc(), "UTF-8"));
		wrapper.setDocstatus(facade.getContragDao().getDocstatus(docid));
		ResponseAdapter<DocDataWrapper> adapter = new ResponseAdapter<DocDataWrapper>(
				true, wrapper, ServiceError.ERR_OK);
		return adapter;

	}

	@Override
	protected Object composeResponce(ResponseAdapter<DocDataWrapper> adapter) {
		DocDataResponseDocument responsedoc = DocDataResponseDocument.Factory.newInstance();
		DocDataResponse response = responsedoc.addNewDocDataResponse();
		if (adapter.isStatus()){
			response.setCode(adapter.getResponse().getCode());
			response.setDescription(adapter.getResponse().getDescription());
			response.setDocdata(adapter.getResponse().getDocdata());
			response.setBldoc(adapter.getResponse().getBldoc());
			response.setDocid(adapter.getResponse().getDocumentId());
			response.setStatus(adapter.getResponse().getDocstatus());
		}
		
		else {
			response.setCode(adapter.getError().getCode());
			response.setDescription(adapter.getError().getMessage());
			response.setDocid(-1);
			response.setStatus(-1);
		}
		
		
		
		return responsedoc;
	}

}
class DocDataWrapper extends StandartResponseWrapper{
	String bldoc;
	String docdata;
	int docstatus;	
	public String getDocdata() {
		return docdata;
	}

	public void setDocdata(String docdata) {
		this.docdata = docdata;
	}

	public int getDocstatus() {
		return docstatus;
	}

	public void setDocstatus(int docstatus) {
		this.docstatus = docstatus;
	}

	public String getBldoc() {
		return bldoc;
	}

	public void setBldoc(String bldoc) {
		this.bldoc = bldoc;
	}
	
}


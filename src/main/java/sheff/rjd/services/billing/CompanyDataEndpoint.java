package sheff.rjd.services.billing;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.oxm.Marshaller;

import ru.aisa.rgd.ws.dao.ServiceFacade;
import ru.aisa.rgd.ws.documents.StandartResponseWrapper;
import ru.aisa.rgd.ws.endpoint.ContragAbstractEndpoint;
import ru.aisa.rgd.ws.endpoint.ResponseAdapter;
import ru.aisa.rgd.ws.exeption.ServiceCode;
import ru.aisa.rgd.ws.exeption.ServiceError;
import ru.aisa.rgd.ws.exeption.ServiceException;
import ru.iit.billing.companyData.CompanyDataRequestDocument;
import ru.iit.billing.companyData.CompanyDataResponseDocument;
import ru.iit.billing.companyData.CompanyDataType;
import ru.iit.billing.companyData.DocPortal;
import ru.iit.billing.companyData.DocType;
import ru.iit.billing.companyData.RequestCompanyData;
import ru.iit.billing.companyData.ResponseCompanyData;


public class CompanyDataEndpoint extends
ContragAbstractEndpoint<CompanyDataWrapper> {

	private static Log log = LogFactory
		    .getLog(CompanyDataWrapper.class);

	    private NamedParameterJdbcTemplate npjt;
	  
	    public NamedParameterJdbcTemplate getNpjt() {
		return npjt;
	    }

	    public void setNpjt(NamedParameterJdbcTemplate npjt) {
		this.npjt = npjt;
	    }
		
	public CompanyDataEndpoint(Marshaller marshaller) {
		super(marshaller);
	}

	@Override
	protected ResponseAdapter<CompanyDataWrapper> processRequest(
			Object arg, ServiceFacade facade) throws Exception {
		
		CompanyDataRequestDocument requestdoc = (CompanyDataRequestDocument) arg;
		RequestCompanyData request = requestdoc.getCompanyDataRequest();
		Integer month = request.getMonth();
		Integer year = request.getYear();
		List<Long> predIds = new ArrayList<Long>();
		predIds =  facade.getCompanyData().getPredids();
		List<CompanyDataObject> result = new ArrayList<CompanyDataObject>();
	
		
		try{
			
			result = facade.getCompanyData().getDataCompany( month, year, predIds);
		
	} catch (Exception e){
		throw new ServiceException(e, ServiceError.ERR_UNDEFINED);
	}
		CompanyDataWrapper wrapper = new CompanyDataWrapper();
		wrapper.setDescription("ok");
		wrapper.setCode(ServiceCode.ERR_OK);
		wrapper.setResult(result);
		ResponseAdapter<CompanyDataWrapper> adapter = new ResponseAdapter<CompanyDataWrapper>(
				true, wrapper, ServiceError.ERR_OK);
		
		return adapter;
	}

	private DocPortal convert(List<DocumentObject> fign){
		
		DocPortal result = DocPortal.Factory.newInstance();
		DocType[] dt = new DocType[fign.size()];
		
		for (int i = 0; i < dt.length; i++) {
		dt[i] = DocType.Factory.newInstance();
		dt[i].setId(fign.get(i).getId());
		dt[i].setName(fign.get(i).getName());
		}
		result.setDocArray(dt);
		return result;
		}
	
	@Override
	protected Object composeResponce(
			ResponseAdapter<CompanyDataWrapper> adapter) {

		List<CompanyDataObject> list = adapter.getResponse().getResult();
		
		CompanyDataResponseDocument cdr = CompanyDataResponseDocument.Factory.newInstance();

		ResponseCompanyData rcp = ResponseCompanyData.Factory.newInstance();
		CompanyDataType[] cdt = new CompanyDataType[list.size()];
		
		for (int i = 0; i < list.size(); i++) {
		cdt[i] = CompanyDataType.Factory.newInstance();
		cdt[i].setInnCompany(list.get(i).getInnCompany());
		cdt[i].setCountUserCompany(list.get(i).getCountUserCompany());
		DocPortal dp = convert(list.get(i).getSign());
		DocPortal dp1 = convert(list.get(i).getAll());
		cdt[i].setNumberSignedDocPortal(dp);
		cdt[i].setNumberDocPortal(dp1);
		}

		rcp.setContragentArray(cdt);
		cdr.setCompanyDataResponse(rcp);
		return cdr;

	}
}

class CompanyDataWrapper extends StandartResponseWrapper{
	
	private List<CompanyDataObject> result;

	public List<CompanyDataObject> getResult() {
		return result;
	}

	public void setResult(List<CompanyDataObject> result) {
		this.result = result;
	}
}
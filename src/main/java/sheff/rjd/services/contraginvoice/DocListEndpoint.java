package sheff.rjd.services.contraginvoice;

import java.math.BigInteger;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.oxm.Marshaller;

import ru.aisa.rgd.etd.objects.ETDDocument;
import ru.aisa.rgd.ws.dao.ServiceFacade;
import ru.aisa.rgd.ws.documents.StandartResponseWrapper;
import ru.aisa.rgd.ws.endpoint.ContragAbstractEndpoint;
import ru.aisa.rgd.ws.endpoint.ResponseAdapter;
import ru.aisa.rgd.ws.exeption.ServiceCode;
import ru.aisa.rgd.ws.exeption.ServiceError;
import ru.aisa.rgd.ws.exeption.ServiceException;
import ru.iit.portal.contraginvoice.DocListRequestDocument;
import ru.iit.portal.contraginvoice.DocListRequestDocument.DocListRequest;
import ru.iit.portal.contraginvoice.DocListResponseDocument;
import ru.iit.portal.contraginvoice.DocListResponseDocument.DocListResponse;
import ru.iit.portal.contraginvoice.DocListResponseDocument.DocListResponse.Tabledocs;
import sheff.rjd.services.syncutils.ContrAgDocStatus;
import sheff.rjd.services.syncutils.db2DateGenerator;
import sheff.rjd.utils.DocListDateComparator;

public class DocListEndpoint extends ContragAbstractEndpoint<DocListWrapper> {

	private static Logger log = Logger.getLogger(DocListEndpoint.class);

	private NamedParameterJdbcTemplate npjt;

	public NamedParameterJdbcTemplate getNpjt() {
		return npjt;
	}

	public void setNpjt(NamedParameterJdbcTemplate npjt) {
		this.npjt = npjt;
	}

	public DocListEndpoint(Marshaller marshaller) {
		super(marshaller);
	}

	@Override
	protected ResponseAdapter<DocListWrapper> processRequest(Object arg,
			ServiceFacade facade) throws Exception {

		DocListRequestDocument requestdoc = (DocListRequestDocument) arg;
		DocListRequest request = requestdoc.getDocListRequest();
		db2DateGenerator dategen = new db2DateGenerator();
		String inn = request.getInn();
		String kpp = request.getKpp();
		String cert = new BigInteger(request.getCertSn().replaceAll(" ", ""), 16).toString();
		
		String date1 = "";
		String date2 = "";
		Date dateizmafter = null;
		Date dateizmbefore = null;
		String formname = "";
		if (request.isSetDateafter())
			date1 = dategen.Date(request.getDateafter());

		if (request.isSetDatebefore())
			date2 = dategen.Date(request.getDatebefore());

		
		if (request.isSetDateizmafter())
		dateizmafter = dategen.shiftDate(request.getDateizmafter(), false);
		if (request.isSetDateizmbefore())
			dateizmbefore = dategen.shiftDate(request.getDateizmbefore(), true);
		if (request.isSetFormaname())
			formname = request.getFormaname();
				
		if (facade.getContragDao().regcount(cert, inn ,kpp)==0)
			throw new ServiceException(new Exception(), ServiceError.ERR_CHECK_PRED_BY_CERT);

			
		
//			if (facade.getContragDao().predcount(inn, kpp)==0) 
//				throw new ServiceException(new Exception(), ServiceError.ERR_CHECK_PRED);
			
		
		
		
		
		
		List<ETDDocument> docs = facade.getContragDao().Contraglist(
				inn, kpp, date1, date2, formname);

		
		DocListDateComparator comp = new DocListDateComparator();
		
		
		DocListWrapper wrapper = new DocListWrapper();
		wrapper.setDescription("ok");
		wrapper.setCode(ServiceCode.ERR_OK);
		wrapper.setDoclist(comp.resp(dateizmafter, dateizmbefore, docs));
		ResponseAdapter<DocListWrapper> adapter = new ResponseAdapter<DocListWrapper>(
				true, wrapper, ServiceError.ERR_OK);
		return adapter;

	}

	@Override
	protected Object composeResponce(ResponseAdapter<DocListWrapper> adapter) {
		DocListResponseDocument respdoc = DocListResponseDocument.Factory
				.newInstance();
		DocListResponse response = respdoc.addNewDocListResponse();

		if (adapter.isStatus()) {
			response.setCode(adapter.getResponse().getCode());
			response.setDescription(adapter.getResponse().getDescription());
			for (ETDDocument doc : adapter.getResponse().getDoclist()) {
			
								
				
				Tabledocs table = response.addNewTabledocs();
				table.setDocid(doc.getId());
				// System.out.println(doc.getIdPak().length());
				if (doc.getIdPak() != null) {
					if (doc.getIdPak().length() > 0)
						table.setIdPak(doc.getIdPak());
				}
//				ContrAgDocStatus status = new ContrAgDocStatus();
				table.setStatus(ContrAgDocStatus.getstatus(doc));
				table.setType(doc.getName());
				if (doc.getVagnum() != null)
					table.setVagnum(doc.getVagnum());
			}

		}

		else {
			response.setCode(adapter.getError().getCode());
			response.setDescription(adapter.getError().getMessage());
		}
		// System.out.println(respdoc);
		return respdoc;
	}



}

class DocListWrapper extends StandartResponseWrapper {

	List<ETDDocument> doclist;

	public List<ETDDocument> getDoclist() {
		return doclist;
	}

	public void setDoclist(List<ETDDocument> doclist) {
		this.doclist = doclist;
	}

	

}

package sheff.rjd.services.contraginvoice;

import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.HashMap;

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
import ru.iit.portal.contraginvoice.DocDeclineRequestDocument;
import ru.iit.portal.contraginvoice.DocDeclineRequestDocument.DocDeclineRequest;
import ru.iit.portal.contraginvoice.DocDeclineResponseDocument;
import ru.iit.portal.contraginvoice.DocDeclineResponseDocument.DocDeclineResponse;
import sheff.rjd.services.transoil.TransService;
import sheff.rjd.ws.OCO.AfterSign.SendToEtd;

public class DeclineDocEndpoint extends
		ContragAbstractEndpoint<DeclineDocWrapper> {

	private static Log log = LogFactory.getLog(DeclineDocEndpoint.class);

	private NamedParameterJdbcTemplate npjt;
	private SendToEtd sendtoetd;
	private TransService sendtotransoil;

	public NamedParameterJdbcTemplate getNpjt() {
		return npjt;
	}

	public void setNpjt(NamedParameterJdbcTemplate npjt) {
		this.npjt = npjt;
	}

	public SendToEtd getSendtoetd() {
		return sendtoetd;
	}

	public void setSendtoetd(SendToEtd sendtoetd) {
		this.sendtoetd = sendtoetd;
	}

	public TransService getSendtotransoil() {
		return sendtotransoil;
	}

	public void setSendtotransoil(TransService sendtotransoil) {
		this.sendtotransoil = sendtotransoil;
	}

	public DeclineDocEndpoint(Marshaller marshaller) {
		super(marshaller);
	}

	@Override
	protected ResponseAdapter<DeclineDocWrapper> processRequest(Object arg,
			ServiceFacade facade) throws Exception {
		DocDeclineRequestDocument requestdoc = (DocDeclineRequestDocument) arg;
		DocDeclineRequest request = requestdoc.getDocDeclineRequest();
		String reason = request.getReason();
		String cert = new BigInteger(request.getCertSn().replaceAll(" ", ""),
				16).toString();
		long docid = request.getDocid();
		String inn = request.getInn();
		String kpp = request.getKpp();

		if (facade.getContragDao().regcount(cert, inn, kpp) == 0)
			throw new ServiceException(new Exception(),
					ServiceError.ERR_CHECK_PRED_BY_CERT);

		if (facade.getContragDao().rightidpak(String.valueOf(docid), inn, kpp) == 0)
			throw new ServiceException(new Exception(),
					ServiceError.ERR_CHECK_DOCSTORE);

		// if(!facade.getContragDao().docchecktype(docid))
		// throw new ServiceException(new Exception(),
		// ServiceError.ERR_CHECK_DOCSTORE);

		if (!facade.getContragDao().checkpacknew(docid))
			throw new ServiceException(new Exception(),
					ServiceError.ERR_PACK_NEW);

		facade.getContragDao().docstoreadd(reason, cert, docid);

		String datetime = "";
		DropObj dropobj = null;

		try {
			dropobj = facade.getContragDao().getDropInfo(docid);

			SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy");

			datetime = format.format(dropobj.getDate()).toString().concat(" ")
					.concat(dropobj.getTime().toString());
		} catch (Exception e) {
			e.printStackTrace();
		}

		HashMap<String, Object> pp = new HashMap<String, Object>();
		pp.put("etdid", docid);
		long portalid = npjt.queryForLong(
				"select id from snt.docstore where etdid = :etdid", pp);
		int predid = npjt.queryForInt(
				"select predid from snt.docstore where etdid = :etdid", pp);

		
		
		
		
		
		sendtoetd.SendToEtdMessage(portalid, new String(facade.getDocumentDao().getById(portalid).getBlDoc(), "UTF-8"), "Пакет документов", 1, 1, false);
		
		
		sendtotransoil.SendSigntoTransoil(portalid, 1, 1, predid);

		
		
		
		DeclineDocWrapper wrapper = new DeclineDocWrapper();
		wrapper.setDescription("ok");
		wrapper.setCode(ServiceCode.ERR_OK);
		wrapper.setDocumentId(docid);
		wrapper.setFio(dropobj.getFio());
		wrapper.setDatetime(datetime);
		ResponseAdapter<DeclineDocWrapper> adapter = new ResponseAdapter<DeclineDocWrapper>(
				true, wrapper, ServiceError.ERR_OK);
		return adapter;
	}

	@Override
	protected Object composeResponce(ResponseAdapter<DeclineDocWrapper> adapter) {
		DocDeclineResponseDocument respdoc = DocDeclineResponseDocument.Factory
				.newInstance();
		DocDeclineResponse response = respdoc.addNewDocDeclineResponse();

		if (adapter.isStatus()) {
			response.setCode(adapter.getResponse().getCode());
			response.setDescription(adapter.getResponse().getDescription());
			response.setDocid(adapter.getResponse().getDocumentId());
			response.setFio(adapter.getResponse().getFio());
			response.setDroptime(adapter.getResponse().getDatetime());
		} else {
			response.setCode(adapter.getError().getCode());
			response.setDescription(adapter.getError().getMessage());
			response.setDocid(-1);
		}

		return respdoc;
	}

}

class DeclineDocWrapper extends StandartResponseWrapper {
	String fio;
	String datetime;

	public String getFio() {
		return fio;
	}

	public void setFio(String fio) {
		this.fio = fio;
	}

	public String getDatetime() {
		return datetime;
	}

	public void setDatetime(String datetime) {
		this.datetime = datetime;
	}

}
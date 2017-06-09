package sheff.rjd.services.contraginvoice;

import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.oxm.Marshaller;

import ru.aisa.rgd.ws.dao.ServiceFacade;
import ru.aisa.rgd.ws.documents.StandartResponseWrapper;
import ru.aisa.rgd.ws.endpoint.ContragAbstractEndpoint;
import ru.aisa.rgd.ws.endpoint.ResponseAdapter;
import ru.aisa.rgd.ws.exeption.ServiceCode;
import ru.aisa.rgd.ws.exeption.ServiceError;
import ru.aisa.rgd.ws.exeption.ServiceException;
import ru.iit.portal.contraginvoice.PackOpenDateRequestDocument;
import ru.iit.portal.contraginvoice.PackOpenDateRequestDocument.PackOpenDateRequest;
import ru.iit.portal.contraginvoice.PackOpenDateResponseDocument;
import ru.iit.portal.contraginvoice.PackOpenDateResponseDocument.PackOpenDateResponse;

public class PackOpenDateEndpoint extends ContragAbstractEndpoint<PackOpenDateDTO> {
	private final String sqlForPGKREPORT = "select dt_open_pak from snt.pgkreport where id_pak =:id_pak";
	private NamedParameterJdbcTemplate npjt;

	public NamedParameterJdbcTemplate getNpjt() {
		return npjt;
	}

	public void setNpjt(NamedParameterJdbcTemplate npjt) {
		this.npjt = npjt;
	}

	protected PackOpenDateEndpoint(Marshaller marshaller) {
		super(marshaller);
	}

	@Override
	protected ResponseAdapter<PackOpenDateDTO> processRequest(Object arg, ServiceFacade facade) throws Exception {
		/* HttpServletResponse error = null; */
		PackOpenDateDTO dateResponse;
		Date timeFirstOpen;
		Map<String, Object> pr = new HashMap<String, Object>();
		String id_pak = "";
		String inn = "";
		String kpp = "";
		String cert = "";
		try {
			PackOpenDateRequestDocument dateDoc = (PackOpenDateRequestDocument) arg;
			PackOpenDateRequest data = dateDoc.getPackOpenDateRequest();
			id_pak = data.getIdPak();
			inn = data.getInn();
			kpp = data.getKpp();
			cert = new BigInteger(data.getCertSn().replaceAll(" ", ""), 16).toString();
		} catch (Exception q) {
			throw new ServiceException(new Exception(), ServiceError.ERR_UNDEFINED);
		}
			if (facade.getContragDao().regcount(cert, inn, kpp) == 0)
				throw new ServiceException(new Exception(),
						ServiceError.ERR_CHECK_PRED_BY_CERT);
			
			if (facade.getContragDao().rightidpak(id_pak, inn, kpp)!=1)
				throw new ServiceException(new Exception(), ServiceError.ERR_WRONG_IDPAK);
			
			pr.put("id_pak", id_pak);

		try {
			timeFirstOpen = npjt.queryForObject(sqlForPGKREPORT, pr, Date.class);
		} catch (Exception w) {
			throw new ServiceException(new Exception(), ServiceError.ERR_NON_DATA);
		}
		try {
			SimpleDateFormat time = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
			dateResponse = new PackOpenDateDTO();
			dateResponse.setCode(ServiceCode.ERR_OK);
			dateResponse.setDescription("Запрос успешно обработан");
			dateResponse.setDate(time.format(timeFirstOpen));
		} catch (Exception r) {
			throw new ServiceException(new Exception(), ServiceError.ERR_UNDEFINED);
		}
		ResponseAdapter<PackOpenDateDTO> forResponce = new ResponseAdapter<PackOpenDateDTO>(true, dateResponse,
				ServiceError.OK_WHILE_SAVING_TOREK);
		return forResponce;

	}

	@Override
	protected Object composeResponce(ResponseAdapter<PackOpenDateDTO> adapter) {
		PackOpenDateResponseDocument respDate = PackOpenDateResponseDocument.Factory.newInstance();
		PackOpenDateResponse resDate = respDate.addNewPackOpenDateResponse();

		if (adapter.isStatus()) {
			resDate.setOpenpack(adapter.getResponse().getDate());
			resDate.setCode(adapter.getResponse().getCode());
			resDate.setDescription(adapter.getResponse().getDescription());
		} else {
			resDate.setCode(adapter.getError().getCode());
			resDate.setDescription(adapter.getError().getMessage());
		}
		return respDate;
	}

}

class PackOpenDateDTO extends StandartResponseWrapper {
	private String date;

	public String getDate() {
		return date;
	}

	public void setDate(String l) {

		date = l;
	}

}

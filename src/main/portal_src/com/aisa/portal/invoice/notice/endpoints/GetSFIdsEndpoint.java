package com.aisa.portal.invoice.notice.endpoints;

import java.util.List;

import org.springframework.oxm.Marshaller;

import ru.aisa.rgd.ws.domain.SFinfo;
import ru.aisa.rgd.ws.endpoint.ResponseAdapter;
import ru.aisa.rgd.ws.exeption.ServiceCode;
import ru.aisa.rgd.ws.exeption.ServiceError;
import ru.iit.portal8888.portal.GetSFIdsRequestDocument;
import ru.iit.portal8888.portal.GetSFIdsRequestDocument.GetSFIdsRequest;
import ru.iit.portal8888.portal.GetSFIdsResponseDocument;
import ru.iit.portal8888.portal.GetSFIdsResponseDocument.GetSFIdsResponse;
import ru.iit.portal8888.portal.SF;

import com.aisa.portal.invoice.integration.facade.PortalSFFacade;

public class GetSFIdsEndpoint extends Abstract<SFWrapper> {

	protected GetSFIdsEndpoint(Marshaller marshaller) {
		super(marshaller);
		// TODO Auto-generated constructor stub
	}

	@Override
	ResponseAdapter<SFWrapper> processRequestInvoice(Object arg,
			PortalSFFacade facade) {
		GetSFIdsRequestDocument request = (GetSFIdsRequestDocument) arg;
		GetSFIdsRequest req = request.getGetSFIdsRequest();

		int predid = req.getPredId();
		int wrkid = req.getPersonId();
//		System.out.println(predid);
//		System.out.println(wrkid);
		SFWrapper wrapper = new SFWrapper();

		List<SFinfo> sflist = facade.getIntegrationDao().getSF(predid, wrkid);

		
		wrapper.setSflist(sflist);
		wrapper.setCode(ServiceCode.ERR_OK);

		return new ResponseAdapter<SFWrapper>(true, wrapper,
				ServiceError.ERR_OK);
	}

	@Override
	Object composeResponceInvoice(ResponseAdapter<SFWrapper> adapter) {

		GetSFIdsResponseDocument responsedoc = GetSFIdsResponseDocument.Factory
				.newInstance();
		GetSFIdsResponse response = responsedoc.addNewGetSFIdsResponse();

		if (adapter.isStatus()) {

			for (SFinfo sf : adapter.getResponse().getSflist()) {
				SF s = response.addNewIdsList();
				s.setContent(sf.getContent());
				s.setId(sf.getDocid());
			}
			response.setCode(adapter.getResponse().getCode());
		}

		else {

			response.setCode(adapter.getError().getCode());

		}
//		System.out.println(responsedoc);
		return responsedoc;
	}

}

class SFWrapper extends NoticeWrapper {

	List<SFinfo> sflist;

	public List<SFinfo> getSflist() {
		return sflist;
	}

	public void setSflist(List<SFinfo> sflist) {
		this.sflist = sflist;
	}

}

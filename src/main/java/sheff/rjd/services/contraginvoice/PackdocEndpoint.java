package sheff.rjd.services.contraginvoice;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.oxm.Marshaller;

import ru.aisa.rgd.ws.dao.ServiceFacade;
import ru.aisa.rgd.ws.documents.StandartResponseWrapper;
import ru.aisa.rgd.ws.endpoint.ContragAbstractEndpoint;
import ru.aisa.rgd.ws.endpoint.ResponseAdapter;
import ru.aisa.rgd.ws.exeption.ServiceCode;
import ru.aisa.rgd.ws.exeption.ServiceError;
import ru.aisa.rgd.ws.exeption.ServiceException;
import ru.iit.portal.contraginvoice.DocData;
import ru.iit.portal.contraginvoice.PackdocRequestDocument;
import ru.iit.portal.contraginvoice.PackdocRequestDocument.PackdocRequest;
import ru.iit.portal.contraginvoice.PackdocResponseDocument;
import ru.iit.portal.contraginvoice.PackdocResponseDocument.PackdocResponse;


public class PackdocEndpoint extends
	ContragAbstractEndpoint<PackdocWrapper> {

       private NamedParameterJdbcTemplate npjt;

  
	public NamedParameterJdbcTemplate getNpjt() {
	return npjt;
    }

    public void setNpjt(NamedParameterJdbcTemplate npjt) {
	this.npjt = npjt;
    }

    public PackdocEndpoint(Marshaller marshaller) {
	super(marshaller);
    }

   
  
	@Override
	protected ResponseAdapter<PackdocWrapper> processRequest(Object arg, ServiceFacade facade)
			throws Exception {
		PackdocRequestDocument requestdoc = (PackdocRequestDocument) arg;
		PackdocRequest request = requestdoc.getPackdocRequest();
		String inn = request.getInn();
		String kpp = request.getKpp();
		String cert = new BigInteger(request.getCertSn().replaceAll(" ", ""), 16).toString();
		String id_pak = request.getIdPak();
	
		
		if (facade.getContragDao().regcount(cert, inn ,kpp)==0)
			throw new ServiceException(new Exception(), ServiceError.ERR_CHECK_PRED_BY_CERT);

		if (facade.getContragDao().rightidpak(id_pak, inn, kpp)!=1)
			throw new ServiceException(new Exception(), ServiceError.ERR_WRONG_IDPAK);
	
		
		List<PackdocObj> list = new ArrayList<PackdocObj>();
		
		
		try{
				list = facade.getContragDao().PackDoclist(inn, kpp, id_pak);
//			System.out.println(list.size());
			
		} catch (Exception e){
			throw new ServiceException(e, ServiceError.ERR_UNDEFINED);
		}
		
		int packstatus = facade.getContragDao().getDocstatus(Integer.parseInt(id_pak));
		
		
		
		
		

		PackdocWrapper wrapper = new PackdocWrapper();
		wrapper.setDescription("ok");
		wrapper.setCode(ServiceCode.ERR_OK);
		wrapper.setList(list);
		wrapper.setPackstatus(packstatus);
		ResponseAdapter<PackdocWrapper> adapter = new ResponseAdapter<PackdocWrapper>(
				true, wrapper, ServiceError.ERR_OK);
		return adapter;

	}

	@Override
	protected Object composeResponce(ResponseAdapter<PackdocWrapper> adapter) {

		PackdocResponseDocument responsedoc = PackdocResponseDocument.Factory.newInstance();
		PackdocResponse response = responsedoc.addNewPackdocResponse();
		if (adapter.isStatus()){
			response.setCode(adapter.getResponse().getCode());
			response.setDescription(adapter.getResponse().getDescription());
			response.setPackstatus(adapter.getResponse().getPackstatus());
			for (PackdocObj obj : adapter.getResponse().getList()){
				DocData doc = response.addNewDocument();
				doc.setBldoc(obj.getBldoc());
				doc.setDocdata(obj.getDocdata().toString());
				doc.setDocid(obj.getDocid());
				doc.setFormtype(obj.getFormname());
				doc.setStatus(obj.getStatus());
				
			}
			
			
						
		}
		
		else {
			response.setCode(adapter.getError().getCode());
			response.setDescription(adapter.getError().getMessage());
		
		}
		
		
//		System.out.println(responsedoc);
		return responsedoc;
	}

}
class PackdocWrapper extends StandartResponseWrapper{
	List<PackdocObj> list;
	int packstatus;
	public List<PackdocObj> getList() {
		return list;
	}
	public void setList(List<PackdocObj> list) {
		this.list = list;
	}
	public int getPackstatus() {
		return packstatus;
	}
	public void setPackstatus(int packstatus) {
		this.packstatus = packstatus;
	}
	
}


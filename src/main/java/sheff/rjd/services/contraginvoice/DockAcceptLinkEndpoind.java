package sheff.rjd.services.contraginvoice;
import java.math.BigInteger;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.oxm.Marshaller;

import ru.aisa.rgd.etd.objects.AcceptDoc;
import ru.aisa.rgd.ws.dao.ServiceFacade;
import ru.aisa.rgd.ws.documents.StandartResponseWrapper;
import ru.aisa.rgd.ws.domain.Document;
import ru.aisa.rgd.ws.endpoint.ContragAbstractEndpoint;
import ru.aisa.rgd.ws.endpoint.ResponseAdapter;
import ru.aisa.rgd.ws.exeption.ServiceCode;
import ru.aisa.rgd.ws.exeption.ServiceError;
import ru.aisa.rgd.ws.exeption.ServiceException;
import ru.iit.portal.contraginvoice.DockAcceptLinkRequestDocument;
import ru.iit.portal.contraginvoice.DockAcceptLinkRequestDocument.DockAcceptLinkRequest;
import ru.iit.portal.contraginvoice.DockAcceptLinkResponseDocument;
import ru.iit.portal.contraginvoice.DockAcceptLinkResponseDocument.DockAcceptLinkResponse;
import ru.iit.portal.contraginvoice.DockAcceptLinkResponseDocument.DockAcceptLinkResponse.AcceptedDocs;

public class DockAcceptLinkEndpoind extends
		ContragAbstractEndpoint<DocAcceptListWrapper> {


private static Logger log = Logger.getLogger(DockAcceptLinkEndpoind.class);
	private NamedParameterJdbcTemplate npjt;

	public NamedParameterJdbcTemplate getNpjt() {
		return npjt;
	}

	public void setNpjt(NamedParameterJdbcTemplate npjt) {
		this.npjt = npjt;
	}

	public DockAcceptLinkEndpoind(Marshaller marshaller)
	{
		super(marshaller);
	}


	@Override
	
	protected ResponseAdapter<DocAcceptListWrapper> processRequest(Object arg,
			ServiceFacade facade) throws Exception {
		DockAcceptLinkRequestDocument reqDoc = (DockAcceptLinkRequestDocument) arg;
		DockAcceptLinkRequest request = reqDoc.getDockAcceptLinkRequest();
		
		String inn = request.getInn();
		String kpp = request.getKpp();
//		String cert = request.getCertSn();
		String packId = request.getIdPak();
		List<AcceptDoc>	doki;
		String cert = "";
		try{
			 cert = new BigInteger(request.getCertSn().replaceAll(" ", ""), 16).toString();
		} catch (Exception e){
			e.printStackTrace();
		}
		DocAcceptListWrapper wrapper = new DocAcceptListWrapper();
		
	
		try{
			 cert = new BigInteger(request.getCertSn().replaceAll(" ", ""), 16).toString();
		} catch (Exception e){
			e.printStackTrace();
		}
		
	//check cert
		if(facade.getContragDao().regcount(cert, inn ,kpp)==0){
			wrapper.setDescription("Error cert");
			throw new ServiceException(new Exception(), ServiceError.ERR_CHECK_PRED_BY_CERT);
			
		}
		//check pack
		if(facade.getContragDao().packExist(packId, inn, kpp)==0){
			wrapper.setDescription("Error pack");
			throw new ServiceException(new Exception(), ServiceError.ERR_WRONG_IDPAK);
		}
		
		try{
		
			doki = facade.getContragDao().getAcceptedDoc(cert, packId, inn, kpp);
		
		}
		
			catch (Exception e){
				throw new ServiceException(e, ServiceError.ERR_UNDEFINED);
		}
		
		wrapper.setDocList(doki);
		wrapper.setCode(ServiceCode.ERR_OK);
	
		ResponseAdapter<DocAcceptListWrapper> adapter = new ResponseAdapter<DocAcceptListWrapper>(
				true,wrapper,ServiceError.ERR_OK);
		return adapter;

	}

	@Override
	
	protected Object composeResponce(ResponseAdapter<DocAcceptListWrapper> adapter) {
	
		DockAcceptLinkResponseDocument respdoc = DockAcceptLinkResponseDocument.Factory.newInstance();
		DockAcceptLinkResponse response = respdoc.addNewDockAcceptLinkResponse();
		
		int s = adapter.getError().getCode();
		
		if(s==0){
			
			for(AcceptDoc doc:adapter.getResponse().getDocList())
			{
				AcceptedDocs table = response.addNewAcceptedDocs();
				long id = doc.getDocid();
				table.setDocid(id);
				String docType = doc.getDoctype();
				table.setFormtype(docType);
				String group = doc.getGr();
				String vis = doc.getVis();
				String sign = doc.getSign();
				String dropid = doc.getDropId();
				int code = doc.getCode();				
				Document d = new Document();
				d.setId(id);
				table.setCode(code);
						
			
				if( ((group.equals("ТОР")) || ( group.equals("Связь") )) && (vis.equals("2")))
				{
				
					table.setStat("согласован");/*Прямая ссылка на документ – выдается только в случае, если документ согласован.*/
					if(!docType.contains("Пакет документов"))/* для пакета документов не возвращается*/
					{
						table.setLink(d.createUrl());
					}
				}
				
				else if((sign.equals("-1") || (sign.equals("")) ))
					{
						table.setStat("подписан");
					}
				else if(sign.equals("0"))
					{
						table.setStat("не подписан");
					}
				else
					{
						table.setStat("не согласован");
					}
				
				if(!dropid.equals("0"))/*eсли документ был отклонен передавать для него прямую ссылку.*/
				{
					if(!docType.contains("Пакет документов"))/* для пакета документов не возвращается*/
					{
						table.setLink(d.createUrl());
					}
					
				}
				
				/* Для РДВ ссылка на документ выдается всегда */
				if(docType.equals("РДВ"))
				{
					table.setLink(d.createUrl());
					
				}
				
			}
		}
		else
		{
			
			AcceptedDocs errorDoc = response.addNewAcceptedDocs();
			errorDoc.setCode(adapter.getError().getCode());
			errorDoc.setDocid(-1);
			errorDoc.setFormtype("не определено");
			errorDoc.setStat("Error status");
			errorDoc.setDescription(adapter.getError().getMessage());
		}
	
		return respdoc;
	}

	
}
class DocAcceptListWrapper extends StandartResponseWrapper{
	
	List<AcceptDoc> doclist;
	
	public List<AcceptDoc> getDocList(){
		return doclist;
	}
	
	public void setDocList(List<AcceptDoc> l){
		
		doclist = l;
		}
	}

	






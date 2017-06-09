package sheff.rjd.services.repair;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.oxm.Marshaller;
import org.springframework.security.crypto.codec.Base64;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;
import org.springframework.ws.client.core.WebServiceTemplate;
import org.w3c.dom.Document;

import com.ibm.db2.jcc.c.ob;

import ru.aisa.rgd.ws.dao.ServiceFacade;
import ru.aisa.rgd.ws.documents.StandartResponseWrapper;
import ru.aisa.rgd.ws.endpoint.ETDAbstractEndpoint;
import ru.aisa.rgd.ws.endpoint.ResponseAdapter;
import ru.aisa.rgd.ws.exeption.ServiceError;
import ru.aisa.rgd.ws.exeption.ServiceException;
import ru.aisa.rgd.ws.utility.TypeConverter;
import ru.iit.repair.torek.SENDTIDDATA;
import ru.iit.repair.torek.TSENDRequestDocument;
import ru.iit.repair.torek.TSENDRequestDocument.TSENDRequest;
import ru.iit.repair.torek.TSENDResponseDocument;
import ru.iit.repair.torek.TSENDResponseDocument.TSENDResponse;
import sheff.rjd.services.syncutils.SendToVRKService;
import sheff.rjd.utils.TOREK_WRK_DateAppender;
import sheff.rjd.utils.XMLUtil;

public class T_SEND_Endpoint extends
		ETDAbstractEndpoint<T_SendWrapper> {

	private static Logger log = Logger.getLogger(T_SEND_Endpoint.class);
	private ServiceFacade facade;
	private NamedParameterJdbcTemplate npjt;
	private  WebServiceTemplate wst;
	private TransactionTemplate transTemplate;
	private SendToVRKService signvrkservice;
	public ServiceFacade getFacade() {
		return facade;
	}

	public void setFacade(ServiceFacade facade) {
		this.facade = facade;
	}

	public NamedParameterJdbcTemplate getNpjt() {
		return npjt;
	}

	public void setNpjt(NamedParameterJdbcTemplate npjt) {
		this.npjt = npjt;
	}

	public WebServiceTemplate getWst() {
		return wst;
	}

	public void setWst(WebServiceTemplate wst) {
		this.wst = wst;
	}

	public TransactionTemplate getTransTemplate() {
		return transTemplate;
	}

	public void setTransTemplate(TransactionTemplate transTemplate) {
		this.transTemplate = transTemplate;
	}

	public SendToVRKService getSignvrkservice() {
		return signvrkservice;
	}

	public void setSignvrkservice(SendToVRKService signvrkservice) {
		this.signvrkservice = signvrkservice;
	}

	public T_SEND_Endpoint(Marshaller marshaller) {
		super(marshaller);
	}

	@Override
	protected ResponseAdapter<T_SendWrapper> processRequest(
			Object arg, ServiceFacade facade) throws Exception {

		final TSENDRequestDocument requestDocument = (TSENDRequestDocument) arg;
		final TSENDRequest request = requestDocument.getTSENDRequest();
		System.out.println(request);
		T_SendWrapper wrapper = new T_SendWrapper();
		//Тип запроса 
		//1-Исходящий пакет
		//2-подписанный пакет
		//3-отклонение
		int type_pack = Integer.parseInt(request.getPRPACKAGE());
		int mark;
		
		//------------------Общая обработка документов на вход
//		String torpackid = request.getTorPackID();
//		String firstpack="";
//		if (request.isSetCorrPackID()){
//			firstpack = request.getCorrPackID();
//		}
		
		if (!request.isSetRecieverDepoID()){
			throw new ServiceException(new Exception(), ServiceError.ERR_WRONG_MARK);
		}
		else {
			mark = Integer.parseInt(request.getRecieverDepoID());
		}
				
		int i =0;
		
		List<TOREK_DocumentObject> docs = new ArrayList<TOREK_DocumentObject>();
		while (i<request.getPACKAGEArray().length){
			int j=0;
			List<SignInfoObject> signs = new ArrayList<SignInfoObject>();
			
			//Если пакет исохдящий, то берем все подписи 
			if (type_pack==1){
			while (j<request.getPACKAGEArray(i).getEDSTABArray().length){
				SignInfoObject sign = new SignInfoObject(request.getPACKAGEArray(i).getEDSTABArray(j).getSIGNNUM(), 
						Base64.decode(request.getPACKAGEArray(i).getEDSTABArray(j).getEDS().getBytes("UTF-8")));
				signs.add(sign);
				j++;
			}
			}
			
			//Если нет, то отсеиваем существующие
			else if (type_pack==2){
				HashMap<String, Object> pp = new HashMap<String, Object>();
				long docid = Long.valueOf(request.getPACKAGEArray(i).getDOCID());
				pp.put("docid", docid);
				int realsignnum = npjt.queryForInt("select max(sign) from snt.vrkdocflow where docid =:docid", pp);
				while (j<request.getPACKAGEArray(i).getEDSTABArray().length){
					if (request.getPACKAGEArray(i).getEDSTABArray(j).getSIGNNUM()>realsignnum){
					SignInfoObject sign = new SignInfoObject(request.getPACKAGEArray(i).getEDSTABArray(j).getSIGNNUM(), 
							Base64.decode(request.getPACKAGEArray(i).getEDSTABArray(j).getEDS().getBytes("UTF-8")));
					signs.add(sign);
					}
					j++;
				}
			}
			
			
			TOREK_DocumentObject obj = new TOREK_DocumentObject(request.getPACKAGEArray(i).getDOCID(),
					request.getTorPackID(),
					request.getPACKAGEArray(i).getDOCSPEC(), 
					request.getPACKAGEArray(i).getDOCTYPE(),
					request.getPACKAGEArray(i).getDOCSTATUS(), signs);
			
			if (request.getPACKAGEArray(i).getDOCSTATUS()!=1){
			obj.setBinary(Base64.decode(request.getPACKAGEArray(i).getCONTENT().getBytes("UTF-8")));
			}
			
			else {
				try {
					String[] content = (new String(Base64.decode(request.getPACKAGEArray(i).getCONTENT().getBytes("UTF-8"))).replaceAll("<", "").replaceAll(">", "")).split("h");
					obj.setFio(content[0]);
					obj.setReason(content[3]);
				} catch(Exception e) {
					throw new ServiceException(new Exception(), ServiceError.ERR_WRONG_CONTENT);
				}
			}
			
			if (request.getPACKAGEArray(i).isSetFILENAME()){
				obj.setIsscan(true);
				obj.setFilename(request.getPACKAGEArray(i).getFILENAME());
			}
			docs.add(obj);
			i++;
		}
		
		
		//--------------------------------------Конец заполнения
		switch (type_pack){
		case 1:
			System.out.println("case 1");
		
		
		try{
		docs = HandleNewPackage(docs, mark);
		
		
		wrapper.setDescription(ServiceError.OK_WHILE_SAVING_TOREK.getMessage());
		wrapper.setCode(ServiceError.OK_WHILE_SAVING_TOREK.getCode());
		
		} catch (Exception e){
		log.error(TypeConverter.exceptionToString(e));
		wrapper.setDescription(ServiceError.ERR_WHILE_SAVING_TOREK.getMessage());
		wrapper.setCode(ServiceError.ERR_WHILE_SAVING_TOREK.getCode());
		
//		throw new ServiceException(e, ServiceError.ERR_OK);
		}
		
		break;
		
		case 2:
			System.out.println("case 2");
			try{
				docs = HandleDocsForSign(docs, mark);

				
					}catch (Exception e){
						log.error(TypeConverter.exceptionToString(e));
						wrapper.setDescription(ServiceError.ERR_WHILE_SAVING_TOREK.getMessage());
						wrapper.setCode(ServiceError.ERR_WHILE_SAVING_TOREK.getCode());
					}
		break;
		
		case 3:
			System.out.println("case 3");
			try{
				docs = HandleDocsForSign(docs, mark);

			}catch (Exception e){
				log.error(TypeConverter.exceptionToString(e));
				wrapper.setDescription(ServiceError.ERR_WHILE_SAVING_TOREK.getMessage());
				wrapper.setCode(ServiceError.ERR_WHILE_SAVING_TOREK.getCode());
			}
			break;
		
		}
		responsedata[] response = new responsedata[docs.size()];
		for (int z=0;z<docs.size();z++){
			response[z] = new responsedata();
			response[z].setDocid(docs.get(z).getTorekid());
			response[z].setOperdocid(String.valueOf(docs.get(z).getPortalid()));
			}
		wrapper.setDocdata(response);
		
		ResponseAdapter<T_SendWrapper> adapter = new ResponseAdapter<T_SendWrapper>(
				true, wrapper, ServiceError.OK_WHILE_SAVING_TOREK);
		return adapter;

	}

	@Override
	protected Object composeResponce(
			ResponseAdapter<T_SendWrapper> adapter) {
		TSENDResponseDocument responsedoc = TSENDResponseDocument.Factory.newInstance();
		TSENDResponse response = responsedoc.addNewTSENDResponse();

		if (adapter.isStatus()) {
			response.setCode(String.valueOf(adapter.getResponse().getCode()));
			response.setDescription(adapter.getResponse().getDescription());
			
			for (int i =0; i< adapter.getResponse().getDocdata().length; i++){
			SENDTIDDATA data = response.addNewIDDATA();
			data.setDOCID(adapter.getResponse().getDocdata()[i].getDocid());
			data.setOperDocID(adapter.getResponse().getDocdata()[i].getOperdocid());
			}
			
		} else {
			response.setCode(String.valueOf(adapter.getError().getCode()));
			response.setDescription(adapter.getError().getMessage());
			
		}

		return responsedoc;
	}
	
	private List<TOREK_DocumentObject> HandleNewPackage(final List<TOREK_DocumentObject> docs, final int mark){
		//при получении из ТОР ЭК C04
		
		
		getTransTemplate().execute(new TransactionCallbackWithoutResult() {

			@Override
			protected void doInTransactionWithoutResult(TransactionStatus status) {
				try{
					
					for (int i =0; i<docs.size();i++){
					HashMap<String, Object> pp = new HashMap<String, Object>();
					long portalid = -1;
					switch(docs.get(i).getDocstatus()){
					case 0:
					pp.put("torekid", docs.get(i).getTorekid());
					pp.put("packid", docs.get(i).getPackid());
					npjt.update("update snt.vrkids set packid =:packid where torekid =:torekid ", pp);
					portalid = npjt.queryForLong("select docid from snt.vrkids where torekid =:torekid", pp);
					
					break;
					case 3:
						
						//FIXME
						pp.put("mark", mark);
						int predid = npjt.queryForInt("select id from snt.pred where kleimo = :mark", pp);
						pp.put("formname", docs.get(i).getFormname());
						int typeid = npjt.queryForInt("select id from snt.doctype where name = :formname", pp);
						
//						System.out.println(typeid);
						
						portalid = facade.getNextDocumentId();
						//Запись в таблицы
						pp.put("docid", portalid);
						pp.put("torekid", docs.get(i).getTorekid());
						pp.put("packid", docs.get(i).getPackid());
						pp.put("predid", predid);
						pp.put("typeid", typeid);
						pp.put("docdata", new String(docs.get(i).getBinary(), "UTF-8"));
						pp.put("bldoc", docs.get(i).getBinary());
						pp.put("typedoc", docs.get(i).getDoctype());
//						pp.put("filename", docs.get(i).getFilename());
//						System.out.println(docs.get(i).getPortalid()+" "+docs.get(i).getTorekid()+" "+docs.get(i).getPackid()+" "+predid);
						npjt.update("insert into snt.docstore (id, predid , typeid, no, bldoc,  docdata, crdate, crtime, id_pak, signlvl) "
								+ "values (:docid, :predid, :typeid, :typedoc, :bldoc, :docdata, current date, current time, :packid, 0)", pp);
						npjt.update("insert into snt.vrkids (docid, torekid, packid) values (:docid, :torekid, :packid)", pp);
						
						if (docs.get(i).isscan){
							pp.put("filename", docs.get(i).getFilename());
							npjt.update("update snt.docstore set opisanie =:filename where id =:docid", pp);
						}
						
						for (int j=0; j<docs.get(i).getSigns().size();j++){
							pp.put("sign", docs.get(i).getSigns().get(j).getSign_num());
							pp.put("binary", docs.get(i).getSigns().get(j).getBinary());
							npjt.update("insert into snt.vrkdocflow (docid, sign, dt, binary, predid) values (:docid, :sign, current timestamp, :binary, :predid)", pp);
							npjt.update("update snt.docstore set signlvl = :sign where id = :docid", pp);
						}
						
						if (npjt.queryForInt("select count(0) from snt.doctypeflow where dtid = "+
								" (select typeid from snt.docstore where id=:docid) and parent is not null and order>:sign ", pp)==0){
							npjt.update("update snt.docstore set signlvl = null where id = :docid", pp);
						}
						
						if (docs.get(i).getDocspec().equals("C04")){
							pp.put("content", "Входящий комплект");
							npjt.update("update snt.docstore set opisanie =:content, signlvl =99 where id =:docid", pp);
												
						}
						
						
					break;
					}
						
					docs.get(i).setPortalid(portalid);	
					if (docs.get(i).getDocstatus()!=0)
					signvrkservice.SendDocs(1, docs.get(i));
					
				}
					
					
					
					
				} catch (Exception e){
//					e.printStackTrace();
					log.error(TypeConverter.exceptionToString(e));
					status.setRollbackOnly();
				}
				
			}
			
		});
		return docs;
	} 
	
	
	private List<TOREK_DocumentObject> HandleDocsForSign(final List<TOREK_DocumentObject> docs, final int mark){

		//при получении из ТОР ЭК пакетов C05 и C06
				
		getTransTemplate().execute(new TransactionCallbackWithoutResult() {

			@Override
			protected void doInTransactionWithoutResult(TransactionStatus status) {
				try{
					HashMap<String, Object> pp = new HashMap<String, Object>();
					pp.put("mark", mark);
					int predid = npjt.queryForInt("select id from snt.pred where kleimo = :mark", pp);
					for (int i=0;i<docs.size();i++){
						
					switch(docs.get(i).getDocstatus()){
					case 0:
						docs.get(i).setPortalid(Long.valueOf(docs.get(i).getTorekid()));
						break;
					case 2:
						
						for (int j=0;j<docs.get(i).getSigns().size();j++){
							pp.put("sign", docs.get(i).getSigns().get(j).getSign_num());
							pp.put("binary", docs.get(i).getSigns().get(j).getBinary());
							pp.put("docid", docs.get(i).getTorekid());
							pp.put("predid", predid);
							npjt.update("insert into snt.vrkdocflow (docid, sign, dt, binary, predid) values (:docid, :sign, current timestamp, :binary, :predid)", pp);
							npjt.update("update snt.docstore set signlvl = :sign where id = :docid", pp);
						}
						if (npjt.queryForInt("select count(0) from snt.doctypeflow where dtid = "+
								" (select typeid from snt.docstore where id=:docid) and parent is not null and order>:sign ", pp)==0){
							npjt.update("update snt.docstore set signlvl = null where id = :docid", pp);
						}
						docs.get(i).setPortalid(Long.valueOf(docs.get(i).getTorekid()));
						signvrkservice.SendDocs(1, docs.get(i));
					break;
					case 1:
					pp.put("reason", docs.get(i).getReason());
					pp.put("droppredid", predid);
					pp.put("DOCID", docs.get(i).getTorekid());
					pp.put("name", "ТОРЭК");
					pp.put("opisanie", "Комплект отклонен");
					getNpjt()
							.update("update snt.docstore set droptime = CURRENT TIMESTAMP,"
									+ "droptxt = :reason,"
									+ "dropid = 0, "
									+ "droppredid = :droppredid, "
									+ "opisanie = :opisanie, "
									+ "dropwrkid = (select id from snt.wrkname where name =:name fetch first row only) where id = :DOCID",
									pp);
					
					String datetime = facade.getNpjt().queryForObject("select (cast(date(droptime) as varchar(20)) || ' ' || cast(time(droptime) as varchar(20))) "
							+ "as char from snt.docstore where id = :DOCID ", pp, String.class);
					
					TOREK_WRK_DateAppender dateadapter = new TOREK_WRK_DateAppender();
					String signdatetime = dateadapter.tovrk(datetime);
					
					docs.get(i).setPortalid(Long.valueOf(docs.get(i).getTorekid()));
					docs.get(i).setDroptime(signdatetime);
					signvrkservice.SendDocs(2, docs.get(i));
					break;
					}
					
					
				}
										
				} catch (Exception e) {
//					e.printStackTrace();
					log.error(TypeConverter.exceptionToString(e));
					status.setRollbackOnly();
				}
				
			}
			
		});
		return docs;
	
	}
	
	
}
		class responsedata{
			String docid;
			String operdocid;
			public String getDocid() {
				return docid;
			}
			public void setDocid(String docid) {
				this.docid = docid;
			}
			public String getOperdocid() {
				return operdocid;
			}
			public void setOperdocid(String operdocid) {
				this.operdocid = operdocid;
			}
			
		}
		
		class T_SendWrapper extends StandartResponseWrapper
		{
			private responsedata[]	docdata;

			public responsedata[] getDocdata() {
				return docdata;
			}

			public void setDocdata(responsedata[] docdata) {
				this.docdata = docdata;
			}

			
			
			
		}

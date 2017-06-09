package sheff.rjd.services.repair;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;

import org.apache.log4j.Logger;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.oxm.Marshaller;
import org.springframework.security.crypto.codec.Base64;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;
import org.springframework.ws.client.core.WebServiceTemplate;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.ibm.db2.jcc.c.ob;

import ru.aisa.rgd.ws.dao.ServiceFacade;
import ru.aisa.rgd.ws.documents.StandartResponseWrapper;
import ru.aisa.rgd.ws.endpoint.ETDAbstractEndpoint;
import ru.aisa.rgd.ws.endpoint.ResponseAdapter;
import ru.aisa.rgd.ws.exeption.ServiceError;
import ru.aisa.rgd.ws.utility.TypeConverter;
import ru.warecs.iit.sendToPortal.RequestType;
import ru.warecs.iit.sendToPortal.ResponseType;
import ru.warecs.iit.sendToPortal.SendPortalRequestDocument;
import ru.warecs.iit.sendToPortal.SendPortalResponseDocument;
import sheff.rjd.utils.XMLUtil;


public class Send_to_portalEndpoint extends
		ETDAbstractEndpoint<StandartResponseWrapper> {

	private static Logger log = Logger.getLogger(Send_to_portalEndpoint.class);
	private ServiceFacade facade;
	private NamedParameterJdbcTemplate npjt;
	private  WebServiceTemplate wst;
	private TransactionTemplate transTemplate;
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

	public Send_to_portalEndpoint(Marshaller marshaller) {
		super(marshaller);
	}

	private static final String C04 = "Комплект на пересылку в ремонт";
	@Override
	protected ResponseAdapter<StandartResponseWrapper> processRequest(
			Object arg, ServiceFacade facade) throws Exception {

		final SendPortalRequestDocument requestDocument = (SendPortalRequestDocument) arg;
		final RequestType request = requestDocument.getSendPortalRequest();
		StandartResponseWrapper wrapper = new StandartResponseWrapper();
		try{
		String wareksid = String.valueOf(request.getDocid());
		byte[] xml = Base64.decode(request.getXml().getBytes("UTF-8"));
		String type = String.valueOf(request.getType());
		int mark = request.getMark();
		TOREK_DocumentObject object = new TOREK_DocumentObject(type, "UNKNOWN", xml, wareksid, mark);
		object = HandleDocs(object);
		
		if (object.getPortalid()>-1){
		wrapper.setDescription("ok");
		wrapper.setCode(0);
		}
		else {
			wrapper.setDescription("Ошибка при обработке документа");
			wrapper.setCode(-1);
		}
		wrapper.setDocumentId(object.getPortalid());
		} catch (Exception e){
			e.printStackTrace();
			wrapper.setDescription("error");
			wrapper.setCode(-1);
			wrapper.setDocumentId(-1);
		}
		
		ResponseAdapter<StandartResponseWrapper> adapter = new ResponseAdapter<StandartResponseWrapper>(
				true, wrapper, ServiceError.ERR_OK);
		return adapter;

	}

	@Override
	protected Object composeResponce(
			ResponseAdapter<StandartResponseWrapper> adapter) {
		SendPortalResponseDocument responsedoc = SendPortalResponseDocument.Factory.newInstance();
		ResponseType response = responsedoc.addNewSendPortalResponse();

		if (adapter.isStatus()) {
			response.setCode(adapter.getResponse().getCode());
			response.setDescription(adapter.getResponse().getDescription());
			response.setDocid(adapter.getResponse().getDocumentId());

		} else {
			response.setCode(adapter.getError().getCode());
			response.setDescription(adapter.getError().getMessage());
			response.setDocid(Long.MIN_VALUE);
		}

		return responsedoc;
	}

	private TOREK_DocumentObject HandleDocs(final TOREK_DocumentObject object){
		getTransTemplate().execute(new TransactionCallbackWithoutResult() {

			@Override
			protected void doInTransactionWithoutResult(TransactionStatus status) {
				try{
					HashMap<String, Object> pp = new HashMap<String, Object>();
					//FIXME
					pp.put("mark", object.getMark());
					int predid = npjt.queryForInt("select id from snt.pred where kleimo = :mark", pp);
					pp.put("formname", object.getFormname());
					int typeid = npjt.queryForInt("select id from snt.doctype where name = :formname", pp);
//					System.out.println(typeid);
					long portalid = facade.getNextDocumentId();
//					Запись в таблицы
					pp.put("docid", portalid);
					pp.put("wrkid", object.getWareksid());
//					pp.put("torekid", docs.get(i).getTorekid());
//					pp.put("packid", docs.get(i).getPackid());
					
					pp.put("predid", predid);
					pp.put("typeid", typeid);
					
					//Проверка на XML
					try{
						Document ddoc = XMLUtil.getDOM(new String(object.getBinary(), "UTF-8"));
						ddoc.getFirstChild();
						pp.put("docdata", new String(object.getBinary(), "UTF-8"));
						object.setDoctype("E");
					}catch(Exception e){
//						e.printStackTrace();
						pp.put("docdata", null);
						object.setDoctype("S");
					} 
					pp.put("no", object.getDoctype());
					pp.put("bldoc", object.getBinary());
//					System.out.println(new String(object.getBinary(), "UTF-8"));
					npjt.update("insert into snt.docstore (id, predid , no, typeid, bldoc,  docdata, crdate, crtime, signlvl) "
							+ "values (:docid, :predid, :no, :typeid, :bldoc, :docdata, current date, current time, 0)", pp);
					npjt.update("insert into snt.vrkids (docid, wareksid) values (:docid, :wrkid)", pp);
					
					if (object.getDocspec().equals("C04")||object.getDocspec().equals("C05")||object.getDocspec().equals("C06")){
						setPackDocs(object, portalid);
						pp.put("content", "Исходящий комплект");
						npjt.update("update snt.docstore set opisanie =:content where id =:docid", pp);
					}
				
					object.setPortalid(portalid);
					
				} catch (Exception e){
					e.printStackTrace();
					log.error(TypeConverter.exceptionToString(e));
					status.setRollbackOnly();
				}
				
			}
			
		});
		
		return object;
		
	}
	
	private TOREK_DocumentObject AddToArray(TOREK_DocumentObject object){
		Document ddoc;
		try
		{
			ddoc = XMLUtil.getDOM(new String(object.getBinary(), "UTF-8"));
	
			Element el =(Element)ddoc.getElementsByTagName("docid_arr").item(0);
			
			Element z = ddoc.createElement("docid");
			z.setTextContent("test");
			el.appendChild(z);
			object.setBinary(XMLUtil.getFormattedString(ddoc).getBytes("UTF-8"));
			
		}
		
		catch (UnsupportedEncodingException e)
		{
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		return object;
	}
	
	private void setPackDocs(TOREK_DocumentObject object, long portalid){

		Document ddoc;
		try
		{
			ddoc = XMLUtil.getDOM(new String(object.getBinary(), "UTF-8"));
			NodeList nl = ddoc.getElementsByTagName("docid");
			String torpackid = ddoc.getElementsByTagName("torPackId").item(0).getTextContent();
			HashMap<String, Object> pp = new HashMap<String, Object>();
			//Проставляем packid для документов внутри пакета
			for (int i=0;i<nl.getLength();i++){
//			System.out.println(nl.item(i).getTextContent());
			pp.put("docid", Long.valueOf(nl.item(i).getTextContent()));
			pp.put("packid", portalid);
			final String packname = (String)npjt.queryForObject("select rtrim(name) from snt.doctype where id = (select typeid from snt.docstore where id = :docid)", pp, String.class);
			
			npjt.update("update snt.vrkids set packid =:packid where docid =:docid", pp);
			if (!packname.equals(C04)){
			npjt.update("update snt.docstore set id_pak =:packid where id =:docid", pp);
			}
			}
			//Проставляем packid для самого пакета
			pp.put("docid", portalid);
			pp.put("packid", portalid);
			npjt.update("update snt.vrkids set packid =:packid where docid =:docid", pp);
			npjt.update("update snt.docstore set id_pak =:packid where id =:docid", pp);
			
			//если заполнен тег torpackid с ид первичного C04
			if (object.getDocspec().equals("C04")&&torpackid.length()>0){
			pp.put("torpackid", torpackid);
			npjt.update("update snt.docstore set torpackid =:torpackid where id =:docid", pp);
			}
		}
		
		catch (Exception e)
		{
			log.error("Ошибка обработки документов внутри пакета. "+TypeConverter.exceptionToString(e));
//			e.printStackTrace();
			throw new RuntimeException(e);
//			throw new ServiceException(new Exception(), ServiceError.ERR_ACTTMC_NOTARCHIVE);
		}
	
	}
}

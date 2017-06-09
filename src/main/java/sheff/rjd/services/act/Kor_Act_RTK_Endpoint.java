package sheff.rjd.services.act;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.transform.TransformerException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.log4j.Logger;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.oxm.Marshaller;
import org.w3c.dom.DOMException;

import ru.aisa.rgd.ws.dao.ServiceFacade;
import ru.aisa.rgd.ws.domain.Document;
import ru.iit.korACTRTK.korActRTKEndpoint.FormCreateRequest;
import ru.iit.korACTRTK.korActRTKEndpoint.FormCreateRequestDocument;
import ru.iit.korACTRTK.korActRTKEndpoint.FormCreateRequest.Table1.Row;
import ru.iit.korACTRTK.korActRTKEndpoint.FormCreateResponse;
import ru.iit.korACTRTK.korActRTKEndpoint.FormCreateResponseDocument;
import ru.aisa.rgd.ws.documents.ETDForm;
import ru.aisa.rgd.ws.documents.StandartResponseWrapper;
import ru.aisa.rgd.ws.endpoint.ETDAbstractEndpoint;
import ru.aisa.rgd.ws.endpoint.ResponseAdapter;
import ru.aisa.rgd.ws.exeption.InternalException;
import ru.aisa.rgd.ws.exeption.ServiceError;
import ru.aisa.rgd.ws.exeption.ServiceException;
import ru.aisa.rgd.ws.utility.DataBinder;
import ru.aisa.rgd.ws.utility.RowFiller;
import ru.aisa.rgd.ws.utility.TypeConverter;
import sheff.rjd.services.act.PFMList;


public class Kor_Act_RTK_Endpoint extends ETDAbstractEndpoint<KorActRTKWrapper> {

	private static Logger log = Logger.getLogger(ActRTK_Endpoint.class);
	private String formname;
	private ServiceFacade facade;
	private NamedParameterJdbcTemplate npjt;  
	String selectINN = "select count(0) from SNT.PRED where INN = :INN and headid is null";
    String selectKPP = "select count(0) from SNT.PRED where KPP = :KPP and headid is null";

	 public String getFormname() {
		return formname;
	}

	public void setFormname(String formname) {
		this.formname = formname;
	}
	
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

	public Kor_Act_RTK_Endpoint(Marshaller marshaller) {
		super(marshaller);
	}

	@Override
	protected ResponseAdapter<KorActRTKWrapper> processRequest(Object arg,
			ServiceFacade facade) throws Exception {
   
		FormCreateRequestDocument requestDocument = (FormCreateRequestDocument) arg;
		FormCreateRequest request = requestDocument.getFormCreateRequest();

		Document document = null;
		ETDForm form = null;
		int ent1 = 0;
		Long id = null;
		document = new Document();
		form = new ETDForm(facade.getDocumentTemplate(formname));
		DataBinder binder = form.getBinder();
		binder.setRootElement("data");
	    binder.setNodeValue("P_1", request.getP1());
		binder.setNodeValue("P_2", request.getP2());
		binder.setNodeValue("P_3", request.getP3());
		binder.setNodeValue("P_4", request.getP4());
		binder.setNodeValue("P_5", request.getP5());
		binder.setNodeValue("P_6", request.getP6());
		binder.setNodeValue("P_7", request.getP7());
		binder.setNodeValue("P_8", request.getP8());
		binder.setNodeValue("P_9", request.getP9());
		
		Map<String, Object> resultMap = new HashMap<String, Object>();
		
		resultMap.put("INN", request.getP10());
		Integer ColINN = npjt.queryForInt(selectINN, resultMap);
		if(ColINN!=0){
			binder.setNodeValue("P_10", request.getP10());
		}
		else{
		   throw new ServiceException(new Exception(), ServiceError.ERR_WRONG_INN);    
		}
			
		  	   
		resultMap.put("KPP", request.getP11());
		Integer ColKPP = npjt.queryForInt(selectKPP, resultMap);
		if(ColKPP!=0){
			binder.setNodeValue("P_11", request.getP11());
		}
		else{
			throw new ServiceException(new Exception(), ServiceError.ERR_WRONG_KPP);    	    
		}

		binder.setNodeValue("P_10", request.getP10());
		binder.setNodeValue("P_11", request.getP11());
		binder.setNodeValue("P_12", request.getP12());
		binder.setNodeValue("P_13", request.getP13());
		binder.setNodeValue("P_14", request.getP14());
		binder.setNodeValue("P_15", request.getP15());
		binder.setNodeValue("P_16", request.getP16());
		binder.setNodeValue("P_17", request.getP17());
		binder.setNodeValue("P_18", request.getP18());
		

		binder.fillTable(request.getTable1().getRowArray(),
				new RowFiller<FormCreateRequest.Table1.Row, Object>() {

					public void fillRow(DataBinder b, Row rowContent,
							int numRow, Object options) throws DOMException,InternalException {
						b.setNodeValue("P_19", rowContent.getP19());
						b.setNodeValue("P_20", rowContent.getP20());
						b.setNodeValue("P_20_1", rowContent.getP201());
						b.setNodeValue("P_21", rowContent.getP21());
						b.setNodeValue("P_22", rowContent.getP22());
						b.setNodeValue("P_23", rowContent.getP23());
						b.setNodeValue("P_24", rowContent.getP24());
						b.setNodeValue("P_25", rowContent.getP25());
						b.setNodeValue("P_26", rowContent.getP26());
						b.setNodeValue("P_21_1", rowContent.getP211());
						b.setNodeValue("P_22_1", rowContent.getP221());
						b.setNodeValue("P_23_1", rowContent.getP231());
						b.setNodeValue("P_24_1", rowContent.getP241());
						b.setNodeValue("P_25_1", rowContent.getP251());
						b.setNodeValue("P_26_1", rowContent.getP261());
						b.setNodeValue("P_25_2", rowContent.getP252());
						b.setNodeValue("P_26_2", rowContent.getP262());
						b.setNodeValue("P_25_3", rowContent.getP253());
						b.setNodeValue("P_26_3", rowContent.getP263());
						b.setNodeValue("P_25_4", rowContent.getP254());
						b.setNodeValue("P_26_4", rowContent.getP264());
						b.setNodeValue("P_25_5", rowContent.getP255());
						b.setNodeValue("P_26_5", rowContent.getP265());
						
						float A = Float.parseFloat(rowContent.getP23());
						float B = Float.parseFloat(rowContent.getP26());
						float C = Float.parseFloat(rowContent.getP25());
						float D = rowContent.getP24().equalsIgnoreCase("Без НДС") ? 0 : 
							rowContent.getP24().contains("%") ? Float.parseFloat(rowContent.getP24().replace("%", "")) : Float.parseFloat(rowContent.getP24());
						
							float AD = A * (100 + D)/100;
							float AC = (A + C);
							
							String ads = String.valueOf(AD);
							String[] adsa = ads.split("\\.");
							if(adsa[1].length() > 2){
								ads = adsa[0] + "." + adsa[1].substring(0, 2);
							}
							String acs = String.valueOf(AC);
							String[] acsa = acs.split("\\.");
							if(acsa[1].length() > 2){
								acs = acsa[0] + "." + acsa[1].substring(0, 2);
							}
							
							AD = Float.parseFloat(ads);
							AC = Float.parseFloat(acs);
							
						 if(AD != B | AC != B){
						 throw new ServiceException(new Exception(), new ServiceError(99, "Ошибка проверки НДС"));
						 }
					}
				}, "table1", "row");

		binder.setNodeValue("P_27", request.getP27());
		binder.setNodeValue("P_28", request.getP28());
		binder.setNodeValue("P_29", request.getP29());
		binder.setNodeValue("P_30", request.getP30());
		binder.setNodeValue("P_31", request.getP31());
		binder.setNodeValue("P_32", request.getP32());
		Long pfmcode = null;
		try{
		pfmcode = Long.valueOf((20000000+PFMList.pfmcode.get(request.getP37())));
		} catch(Exception e){
			log.error(TypeConverter.exceptionToString(e));
		throw new ServiceException(new Exception(), new ServiceError(98,
				"Не существует такого кода ПФМ"));
		}
	
		binder.setNodeValue("P_37", pfmcode);
         
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("inn", request.getP10());//инн и кпп исполнителя
		map.put("kpp", request.getP11());
		
		ent1 = facade
				.getNpjt()
				.queryForInt(
						"select id from snt.pred where inn = :inn and kpp = :kpp and headid is null",
						map);
		

		String packid = GeneratePackage(ent1,pfmcode,request.getP1());


//		binder.setNodeValue("P_38", packid);
		binder.setNodeValue("package", packid);
		binder.setNodeValue("predId", pfmcode);
//		binder.setNodeValue("package", packid);
//		binder.setNodeValue("predId", pfmcode);


		Long actid = facade.getDocumentDao().getNextId();
		binder.setNodeValue("P_39", actid);
		binder.setNodeValue("P_40", request.getP40());
		if(request.isSetP41()){
		binder.setNodeValue("P_41", request.getP41());
		}
		document.setBlDoc(form.encodeToArchiv());
		document.setDocData(form.transform("data"));
		document.setPredId(ent1);
		document.setSignLvl(0);
		document.setType(formname);
		document.setId(actid);
		facade.insertDocumentWithDocid(document);
		//System.out.println(document.toString());
		
		HashMap<String, Object> pp = new HashMap<String, Object>();
		pp.put("docid", actid);
		pp.put("packdocid", packid);
		facade.getNpjt()
				.update("update snt.docstore set id_pak = :packdocid, visible = 0 where id =:docid",
						pp);
       
		KorActRTKWrapper wrapper = new KorActRTKWrapper();
		wrapper.setDescription(document.createUrl());
		wrapper.setCode(0);
		wrapper.setDocumentId(actid);
		wrapper.setPackid(packid);
		
		ResponseAdapter<KorActRTKWrapper> adapter = new ResponseAdapter<KorActRTKWrapper>(
				true, wrapper, ServiceError.ERR_OK);
		

		return adapter;

	}
 
	@Override
	protected Object composeResponce(ResponseAdapter<KorActRTKWrapper> adapter) {
		FormCreateResponseDocument responsedoc = FormCreateResponseDocument.Factory
				.newInstance();
		FormCreateResponse response = responsedoc.addNewFormCreateResponse();

		if (adapter.isStatus()) {
			response.setCode(adapter.getResponse().getCode());
			response.setDescription(adapter.getResponse().getDescription());
			response.setDocid(adapter.getResponse().getDocumentId());
			response.setPackageid(adapter.getResponse().getPackid());
		} else {
			response.setCode(adapter.getError().getCode());
			response.setDescription(adapter.getError().getMessage());
			response.setDocid(Long.MIN_VALUE);
		}

		return responsedoc;
	}

	@SuppressWarnings("unused")
	private String GeneratePackage(int ent1, Long pfmcode,  String dogovor)
			throws ServiceException, IOException, TransformerException,
			DOMException, InternalException {

		try {
			Document document = new Document();
			ETDForm form = new ETDForm(
					facade.getDocumentTemplate("Пакет документов РТК"));
			DataBinder db = form.getBinder();
			long packdocid = facade.getDocumentDao().getNextId();
			String packid = "P_" + packdocid;

			db.setNodeValue("P_8", packid);
			db.setNodeValue("P_9", pfmcode);
//			db.setRootElement("table1");
			db.setRootElement("row");
			db.setNodeValue("P_1", 1);
			db.setNodeValue("P_2", formname);

//			db.setRootElement("table2");
			db.setRootElement("row2");
			db.setNodeValue("P_2", formname);
			
			db.setRootElement("data");
			db.setNodeValue("package", packid);
			db.setNodeValue("predId", pfmcode);
			document.setId(packdocid);
			document.setBlDoc(form.encodeToArchiv());
			document.setDocData(form.transform("data"));
			document.setPredId(ent1);
			document.setSignLvl(0);
			document.setType("Пакет документов РТК");
             
		
//			System.out.println(form.transform("data"));
			
			
			facade.insertDocumentWithDocid(document);
			HashMap<String, Object> pp = new HashMap<String, Object>();
			pp.put("docid", packdocid);
			pp.put("packid", packid);
		//	pp.put("content", content);
			pp.put("no", dogovor);
			facade.getNpjt()
					.update("update snt.docstore set id_pak = :packid, no =:no where id =:docid",
							pp);

			return packid;

		} catch (Exception e) {

			log.error(TypeConverter.exceptionToString(e));
			throw new ServiceException(new Exception(), new ServiceError(-1,
					"Не удалось создать Пакет документов РТК"));
		}

	}

}

class KorActRTKWrapper extends StandartResponseWrapper {
	String packid;

	public String getPackid() {
		return packid;
	}

	public void setPackid(String packid) {
		this.packid = packid;
	}

}

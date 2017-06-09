package sheff.rjd.services.act;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.HashMap;

import javax.xml.transform.TransformerException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.log4j.Logger;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.oxm.Marshaller;
import org.w3c.dom.DOMException;

import ru.aisa.rgd.ws.dao.ServiceFacade;
import ru.aisa.rgd.ws.documents.ETDForm;
import ru.aisa.rgd.ws.documents.StandartResponseWrapper;
import ru.aisa.rgd.ws.domain.Document;
import ru.aisa.rgd.ws.endpoint.ETDAbstractEndpoint;
import ru.aisa.rgd.ws.endpoint.ResponseAdapter;
import ru.aisa.rgd.ws.exeption.InternalException;
import ru.aisa.rgd.ws.exeption.ServiceError;
import ru.aisa.rgd.ws.exeption.ServiceException;
import ru.aisa.rgd.ws.utility.DataBinder;
import ru.aisa.rgd.ws.utility.RowFiller;
import ru.aisa.rgd.ws.utility.TypeConverter;
import ru.iit.act.rtk.FormCreateRequest;
import ru.iit.act.rtk.FormCreateRequest.Table1.Row;
import ru.iit.act.rtk.FormCreateRequestDocument;
import ru.iit.act.rtk.FormCreateResponse;
import ru.iit.act.rtk.FormCreateResponseDocument;
import ru.iit.act.rtk.Nalsumm;


public class ActRTK_Endpoint extends ETDAbstractEndpoint<ActRTKWrapper> {

	private static Logger log = Logger.getLogger(ActRTK_Endpoint.class);

	public ServiceFacade getFacade() {
		return facade;
	}

	public void setFacade(ServiceFacade facade) {
		this.facade = facade;
	}

	private ServiceFacade facade;

	public ActRTK_Endpoint(Marshaller marshaller) {
		super(marshaller);
	}

	DecimalFormat df = new DecimalFormat("##########.##");
	
	@Override
	protected ResponseAdapter<ActRTKWrapper> processRequest(Object arg,
			ServiceFacade facade) throws Exception {

		FormCreateRequestDocument requestDocument = (FormCreateRequestDocument) arg;
		FormCreateRequest request = requestDocument.getFormCreateRequest();

		Document document = null;
		ETDForm form = null;
		int ent1 = 0;
		Long id = null;
		document = new Document();
		form = new ETDForm(facade.getDocumentTemplate("Акт РТК"));
		DataBinder binder = form.getBinder();

		int p1 = request.getP1();
		binder.setNodeValue("P_1", p1);
		binder.setNodeValue("P_2", request.getP2());
		binder.setNodeValue("P_3", request.getP3());
		binder.setNodeValue("P_4", request.getP4());
		binder.setNodeValue("P_5", request.getP5());
		binder.setNodeValue("P_6", request.getP6());
		binder.setNodeValue("P_7", request.getP7());
		binder.setNodeValue("P_8", request.getP8());
		binder.setNodeValue("P_9", request.getP9());
		binder.setRootElement("data");

		binder.fillTable(request.getTable1().getRowArray(),
				new RowFiller<FormCreateRequest.Table1.Row, Object>() {

					public void fillRow(DataBinder b, Row rowContent,
							int numRow, Object options) throws DOMException,
							InternalException {
						b.setNodeValue("P_10", rowContent.getP10());
						b.setNodeValue("P_11", rowContent.getP11());
						b.setNodeValue("P_12", rowContent.getP12());
						b.setNodeValue("P_13", rowContent.getP13());
						b.setNodeValue("P_14", rowContent.getP14());
						b.setNodeValue("P_15", rowContent.getP15());
						b.setNodeValue("P_16", rowContent.getP16());
						b.setNodeValue("P_17", rowContent.getP17());
						b.setNodeValue("P_37", rowContent.getP37());
						b.setNodeValue("P_38", rowContent.getP38());
						b.setNodeValue("P_41", rowContent.getP41());
						
						if (rowContent.getP37()==0&&rowContent.isSetP39()&&rowContent.getP39().length()>0
								||rowContent.getP37()==1&&!rowContent.isSetP39()
								||rowContent.getP37()==1&&rowContent.isSetP39()&&rowContent.getP39().length()==0){
							throw new ServiceException(new Exception(), new
									 ServiceError(
									 -2, "Неверное заполнение запроса"));
									 
						}
						if (rowContent.isSetP39()){
							b.setNodeValue("P_39", rowContent.getP39());
						}
						
						/*				
						А=14_поле	Стоимость товаров (работ, услуг), имущественных прав без налога
						В=17_поле	Стоимость товаров (работ, услуг), имущественных прав с налогом
						С=16_поле	Сумма налога, предъявляемая покупателю	18 рублей	16
						D=15_поле	Налоговая ставка
						E=41 поле   Цена за ед.
						F=12 поле   Количество
						 */
						float A = Float.parseFloat(rowContent.getP14());
						float B = Float.parseFloat(rowContent.getP17());
						float C =  rowContent.getP16().equalsIgnoreCase("без НДС")||rowContent.getP16().equalsIgnoreCase("-")? 0 : Float.parseFloat(rowContent.getP16());
						float D = rowContent.getP15().equalsIgnoreCase("без НДС") ? 0 : 
							rowContent.getP15().contains("%") ? Integer.parseInt(rowContent.getP15().replace("%", "")) : Integer.parseInt(rowContent.getP15());
						float E = Float.parseFloat(rowContent.getP41());
						int F = Integer.parseInt(rowContent.getP12());
							float AD = A * (100 + D)/100;
							float AC = (A + C);
							float EF = E*F;
							AD = Float.parseFloat((new BigDecimal(AD).setScale(2, BigDecimal.ROUND_HALF_UP).toString()));
							AC = Float.parseFloat((new BigDecimal(AC).setScale(2, BigDecimal.ROUND_HALF_UP)).toString());
							EF = Float.parseFloat((new BigDecimal(EF).setScale(2, BigDecimal.ROUND_HALF_UP)).toString());
							
						/*	float delta = Float.parseFloat("0.01");
														
							float ADdeltaup = Float.parseFloat((new BigDecimal(AD+delta).setScale(2, BigDecimal.ROUND_HALF_UP).toString()));
							float ADdeltadown = Float.parseFloat((new BigDecimal(AD-delta).setScale(2, BigDecimal.ROUND_HALF_UP).toString()));
														
							float ACdeltaup = Float.parseFloat((new BigDecimal(AC+delta).setScale(2, BigDecimal.ROUND_HALF_UP).toString()));
							float ACdeltadown = Float.parseFloat((new BigDecimal(AC-delta).setScale(2, BigDecimal.ROUND_HALF_UP).toString()));
							
							float EFdeltaup = Float.parseFloat((new BigDecimal(EF+delta).setScale(2, BigDecimal.ROUND_HALF_UP).toString()));
							float EFdeltadown = Float.parseFloat((new BigDecimal(EF-delta).setScale(2, BigDecimal.ROUND_HALF_UP).toString()));
							
							
						 if(EF != A && EFdeltaup != A && EFdeltadown != A) {
							 throw new ServiceException(new Exception(), new
									 ServiceError(99, "Ошибка проверки НДС"));
							 
						 }
						 if(AD != B && ADdeltaup!=B && ADdeltadown!=B && AC!= B && ACdeltaup!= B 
								 && ACdeltadown!= B){
							 throw new ServiceException(new Exception(), new
							 ServiceError(99, "Ошибка проверки НДС"));
						 }*/
					}
				}, "table", "row");

		binder.setNodeValue("P_18", request.getP18());
		binder.setNodeValue("P_19", request.getP19());
		binder.setNodeValue("P_20", request.getP20());
		binder.setNodeValue("P_21", request.getP21());
		binder.setNodeValue("P_22", request.getP22());
		binder.setNodeValue("P_23", request.getP23());
		Long pfmcode = null;
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("pfmcode", request.getP28());
		try {
			Integer code = facade.getNpjt().queryForInt("select etdid from snt.pfmcodes "
					+ " where pfmcode = :pfmcode", map);
			if (Long.valueOf(code) < 40000000) {
				pfmcode = Long.valueOf((20000000+code));
			} else {
				pfmcode = Long.valueOf(code);
			}
		} catch(Exception e) {
			log.error(TypeConverter.exceptionToString(e));
			throw new ServiceException(new Exception(), new ServiceError(100,
					"Не существует такого кода ПФМ"));
		}
		
		//TFS
		
		int flag =0;
		try{
			flag = facade.getNpjt().queryForInt("select isfilial from snt.pfmcodes where pfmcode =:pfmcode", map);
			binder.setNodeValue("flag", flag);
		} catch (Exception e){
			log.error(TypeConverter.exceptionToString(e));
			throw new ServiceException(new Exception(), new ServiceError(100,
					"Не существует такого кода ПФМ"));
		}
		//
		
		binder.setNodeValue("P_28", pfmcode);
		binder.setNodeValue("P_31", request.getP31());
		binder.setNodeValue("P_32", request.getP32());
		binder.setNodeValue("P_33", request.getP33());
		binder.setNodeValue("P_34", request.getP34());
		binder.setNodeValue("P_35", request.getP35());
		binder.setNodeValue("P_36", request.getP36());
		
		if (request.isSetP40()){
			try{
				binder.setNodeValue("P_40", request.getP40());
			} catch (Exception e){
				log.error("No tag P_40 in Act RTK "+TypeConverter.exceptionToString(e));
			}
		}
		if (p1 == 2 && (!request.isSetP42() || !request.isSetP43() 
				|| !request.isSetP44() || !request.isSetP45()
				|| !request.isSetP47())) {
			 throw new ServiceException(new Exception(), new
					 ServiceError(101, "Не заполнены обязательные поля 42-45,47 когда <P_1>2</P_1>"));
		}
		if(request.isSetP42()) {
			binder.setNodeValue("P_42", request.getP42());
		}
		if(request.isSetP43()) {
			binder.setNodeValue("P_43", request.getP43());
		}
		if(request.isSetP44()) {
			binder.setNodeValue("P_44", request.getP44());
		}
		if(request.isSetP45()) {
			binder.setNodeValue("P_45", request.getP45());
		}
		binder.setNodeValue("P_46", request.getP46());
		if(request.isSetP47()) {
			binder.setNodeValue("P_47", request.getP47());
		}
		
		binder.setNodeValue("P_48", request.getP48());
		binder.setNodeValue("P_49", request.getP49());

		map.put("inn", request.getP31());
		map.put("kpp", request.getP32());

		SqlRowSet rs = facade
				.getNpjt()
				.queryForRowSet("select id, contr_code from snt.pred where inn = :inn and kpp = :kpp and headid is null", map);
		
		String cabinetIdSell = "";
		while(rs.next()) {
			ent1 = rs.getInt("id");
			cabinetIdSell = rs.getString("contr_code");
		}
		String packid = GeneratePackage(ent1,pfmcode, request.getP9(), request.getP2(), flag);
		
		
		//FOR XML
		
		try{
			binder.setNodeValue("P_50", request.getP50());
		binder.setLastNodeValue("cabinetIdSell", cabinetIdSell);
		binder.setLastNodeValue("cabinetIdRecv", "001");
		binder.setLastNodeValue("nameoper", "ООО \"ИИТ\"");
		binder.setLastNodeValue("innoper", "7708713259");
		binder.setLastNodeValue("idoper", "2JH");
		} catch (Exception e){
			log.error(
				TypeConverter.exceptionToString(e));
		}
		
		//
		binder.setNodeValue("P_29", packid);
		binder.setNodeValue("package", packid);
		binder.setNodeValue("predId", pfmcode);
		
		Long actid = facade.getDocumentDao().getNextId();
		document.setBlDoc(form.encodeToArchiv());
		document.setDocData(form.transform("data"));
		document.setPredId(ent1);
		document.setSignLvl(0);
		document.setType("Акт РТК");
		document.setId(actid);
		facade.insertDocumentWithDocid(document);

		HashMap<String, Object> pp = new HashMap<String, Object>();
		pp.put("docid", actid);
		pp.put("packdocid", packid);
		facade.getNpjt()
				.update("update snt.docstore set id_pak = :packdocid, visible = 0 where id =:docid",
						pp);

		ActRTKWrapper wrapper = new ActRTKWrapper();
		wrapper.setDescription(document.createUrl());
		wrapper.setCode(0);
		wrapper.setDocumentId(actid);
		wrapper.setPackid(packid);
		ResponseAdapter<ActRTKWrapper> adapter = new ResponseAdapter<ActRTKWrapper>(
				true, wrapper, ServiceError.ERR_OK);
		return adapter;

	}

	@Override
	protected Object composeResponce(ResponseAdapter<ActRTKWrapper> adapter) {
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
			response.setPackageid("-1");
		}

		return responsedoc;
	}

	@SuppressWarnings("unused")
	private String GeneratePackage(int ent1, Long pfmcode, String content,  String dogovor, int flag)
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
			//TFS
			try{
			db.setNodeValue("flag", flag);
			}catch (Exception e){
				log.error(TypeConverter.exceptionToString(e));
			}
//			db.setRootElement("table1");
			db.setRootElement("row");
			db.setNodeValue("P_1", 1);
			db.setNodeValue("P_2", "Акт РТК");

//			db.setRootElement("table2");
			db.setRootElement("row2");
			db.setNodeValue("P_2", "Акт РТК");
			
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
			pp.put("content", content);
			pp.put("no", dogovor);
			facade.getNpjt()
					.update("update snt.docstore set id_pak = :packid, opisanie =:content, no =:no where id =:docid",
							pp);

			return packid;

		} catch (Exception e) {

			log.error(TypeConverter.exceptionToString(e));
			throw new ServiceException(new Exception(), new ServiceError(-1,
					"Не удалось создать Пакет документов РТК"));
		}

	}

}

class ActRTKWrapper extends StandartResponseWrapper {
	String packid;

	public String getPackid() {
		return packid;
	}

	public void setPackid(String packid) {
		this.packid = packid;
	}

}

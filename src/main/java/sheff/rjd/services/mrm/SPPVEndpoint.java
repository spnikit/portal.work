package sheff.rjd.services.mrm;

import java.util.HashMap;

import javax.sql.DataSource;

import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
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
import ru.iit.sppvEndpoint.FormCreateRequest;
import ru.iit.sppvEndpoint.FormCreateRequestDocument;
import ru.iit.sppvEndpoint.FormCreateRequest.Table1.Row;
import ru.iit.sppvEndpoint.FormCreateRequest.Table1.Row.Table2.Row2;
//import ru.iit.xsd.utilTypes.ximport.standartResponsesV10.FormCreateResponse;
//import ru.iit.xsd.utilTypes.ximport.standartResponsesV10.FormCreateResponseDocument;
import ru.iit.sppvEndpoint.FormCreateResponse;
import ru.iit.sppvEndpoint.FormCreateResponseDocument;

public class SPPVEndpoint extends ETDAbstractEndpoint<StandartResponseWrapper>{
	private NamedParameterJdbcTemplate npjt;  
	private DataSource ds;
	private String formname;
	static String selectStation = "select count(0) from SNT.STAN where st_kod = :st_kod";
	
	protected SPPVEndpoint(Marshaller marshaller) {
		super(marshaller); 
	}

	public DataSource getDs() {
		return ds;
	}

	public void setDs(DataSource ds) {
		this.ds = ds;
	}
	
	public NamedParameterJdbcTemplate getNpjt() {
		return npjt;
	}


	public void setNpjt(NamedParameterJdbcTemplate npjt) {
		this.npjt = npjt;
	}
	
	public String getFormname() {
		return formname;
	}

	public void setFormname(String formname) {
		this.formname = formname;
	}

	@Override
	protected ResponseAdapter<StandartResponseWrapper> processRequest(Object arg, ServiceFacade facade)
			throws Exception {
		
		FormCreateRequestDocument requestdoc = (FormCreateRequestDocument) arg;
		FormCreateRequest req = requestdoc.getFormCreateRequest();
		Document document = new Document();
		ETDForm form = new ETDForm(facade.getDocumentTemplate(formname));
		DataBinder binder = form.getBinder();
		Long docId = null;
		boolean reWriteDoc = req.isSetDocId();
		String selectOKPO = "select count(0) from SNT.PRED where okpo_kod = :okpo_kod and headid is null";
		String selectINN = "select count(0) from SNT.PRED where INN = :INN and headid is null";
	    String selectKPP = "select count(0) from SNT.PRED where KPP = :KPP and headid is null";
	    String selectClientName = "select rtrim(vname) from snt.pred where okpo_kod = :okpo_kod";
	    String selectNameStation = "select rtrim(vname) from SNT.STAN where st_kod = :st_kod";
	    
		if (reWriteDoc) {
			docId = req.getDocId();
			document = facade.getDocumentById(docId);
			if (document.getDropTime() != null) {
				throw new ServiceException(new Exception(), new ServiceError(
						9, "Документ имеет подпись или отклонен, перезаписать данные нельзя"));
			}
			if (document.getSignLvl() == null || document.getSignLvl() != 0) {
				throw new ServiceException(new Exception(), new ServiceError(
						9, "Документ имеет подпись или отклонен, перезаписать данные нельзя"));
			}
			if(!document.getType().equals(formname)){
				throw new ServiceException(new Exception(), ServiceError.ERR_WRONG_TYPEFORM); 
			}
			
		    } else {
			docId = facade.getNextDocumentId();
			document.setId(docId);	
		}
		binder.setRootElement("data");
        HashMap<String, Object> resultMap = new HashMap<String, Object>();
        try{
        resultMap.put("okpo_kod", Integer.parseInt(req.getOKPO()));
	    Integer ColOKPO = npjt.queryForInt(selectOKPO, resultMap);
	    if(ColOKPO!=0){
	    binder.setNodeValue("P_1a",req.getOKPO());
		}
		else{
		    throw new ServiceException(new Exception(), ServiceError.ERR_WRONG_CODE_OKPO);    
	    }
		}catch(Exception e)
		{
			log.error(TypeConverter.exceptionToString(e));
		}
        
        String name = (String)npjt.queryForObject(selectClientName, resultMap, Object.class); 
	    try{
	   
	   
	    binder.setNodeValue("P_1", name);
	    }catch(Exception e)
	    {
	    	log.error(TypeConverter.exceptionToString(e));
	    }
	    if (req.isSetINN()){	   
		    resultMap.put("INN", req.getINN());
		    Integer ColINN = npjt.queryForInt(selectINN, resultMap);
			if(ColINN!=0){
			binder.setNodeValue("P_1b",req.getINN());
			}
		    else{
		    	throw new ServiceException(new Exception(), ServiceError.ERR_WRONG_INN);    
			 }
		}
	    if (req.isSetKPP()){		   
	    	resultMap.put("KPP", req.getKPP());
			Integer ColKPP = npjt.queryForInt(selectKPP, resultMap);
			if(ColKPP!=0){
			binder.setNodeValue("P_1v", req.getKPP());	
			}
			else{
				throw new ServiceException(new Exception(), ServiceError.ERR_WRONG_KPP);    	    
			 }
			}
	    
	    if (req.getCodeECPstation().length()==6){
	    	 resultMap.put("st_kod", Integer.parseInt(req.getCodeECPstation().substring(0, 5)));	
	    }
	    
	    else resultMap.put("st_kod", Integer.parseInt(req.getCodeECPstation()));
	    
	    Integer colStan = npjt.queryForInt(selectStation, resultMap);
	   
	    String nameStation ="";
	    if(colStan!=0){
	    	binder.setNodeValue("P_4a", req.getCodeECPstation());
	    	nameStation =(String)npjt.queryForObject(selectNameStation, resultMap, Object.class);
	    	binder.setNodeValue("P_4", nameStation);
	    }
	    else{
	    	throw new ServiceException(new Exception(), ServiceError.ERR_WRONG_CODESTATION);
	    }
	    
        int predId = npjt.queryForInt("select id from snt.pred where okpo_kod = :okpo_kod and headid is null", resultMap);
	   

		
		binder.fillTable(req.getTable1().getRowArray(), new RowFiller<FormCreateRequest.Table1.Row, Object>() {
			public void fillRow(DataBinder b, Row rowContent, int numRow,Object options) throws DOMException, InternalException {
			String rowNum = (numRow + 1) < 10 ? "0" + String.valueOf(numRow + 1) : String.valueOf(numRow + 1);
			    b.setNodeValue("P_9_1", rowNum);
			    b.setNodeValue("P_9_2", rowContent.getColVagPodach());
			    if(rowContent.isSetDateWork()){
				    b.setNodeValue("P_9_7_1", rowContent.getDateWork());
			    }
			    if(rowContent.isSetTimeWork()){
				    b.setNodeValue("P_9_7_2", rowContent.getTimeWork());
			    }
				if(rowContent.isSetDateEnd()){
					b.setNodeValue("P_9_8_1", rowContent.getDateEnd());
				}
				if(rowContent.isSetTimeEnd()){
					b.setNodeValue("P_9_8_2", rowContent.getTimeEnd());
				}
				
				if(rowContent.getTable2().getRow2Array().length != 0){
					b.fillTable(rowContent.getTable2().getRow2Array(), new RowFiller<FormCreateRequest.Table1.Row.Table2.Row2, Object>() {
						
						HashMap<String, Object> resultMap = new HashMap<String, Object>();
						
						public void fillRow(DataBinder b, Row2 rowContent,
								int numRow, Object options) throws DOMException, InternalException {
							
							
						    String rowNum = (numRow + 1) < 10 ? "0" + String.valueOf(numRow + 1) : String.valueOf(numRow + 1);
							b.setNodeValue("P_9_2_1", rowNum);
							b.setNodeValue("P_9_2_2", rowContent.getNumVag());
							if(rowContent.isSetGruzName()){
								b.setNodeValue("P_9_2_3", rowContent.getGruzName());
							}
							if(rowContent.isSetNumTrain()){
								b.setNodeValue("P_9_2_4", rowContent.getNumTrain());
							}
							
							if(rowContent.isSetCodeECPstationFormTrain()){
								 if (rowContent.getCodeECPstationFormTrain().length()==6){
							    	 resultMap.put("st_kod", Integer.parseInt(rowContent.getCodeECPstationFormTrain().substring(0, 5)));	
							    }
								 else resultMap.put("st_kod", Integer.parseInt(rowContent.getCodeECPstationFormTrain()));
							    Integer colStan = npjt.queryForInt(selectStation, resultMap);
							    if(colStan!=0){
								b.setNodeValue("P_9_2_5", rowContent.getCodeECPstationFormTrain());
							    }
							    else{
							    	throw new ServiceException(new Exception(), ServiceError.ERR_WRONG_CODESTATION);
							    }
							}
							
							if(rowContent.isSetNumSostava()){
								b.setNodeValue("P_9_2_6", rowContent.getNumSostava());
							}
							if(rowContent.isSetCodeECPstationDestTrain()){
								 if (rowContent.getCodeECPstationDestTrain().length()==6){
							    	 resultMap.put("st_kod", Integer.parseInt(rowContent.getCodeECPstationDestTrain().substring(0, 5)));	
							    }
								 else resultMap.put("st_kod", Integer.parseInt(rowContent.getCodeECPstationDestTrain()));
							    Integer colStan = npjt.queryForInt(selectStation, resultMap);
							    if(colStan!=0){
								b.setNodeValue("P_9_2_7", rowContent.getCodeECPstationFormTrain());
							
							    }
							    else{
							    	throw new ServiceException(new Exception(), ServiceError.ERR_WRONG_CODESTATION);
							    }
							}
							if(rowContent.isSetCodeECPstationStartTrain()){
								 if (rowContent.getCodeECPstationStartTrain().length()==6){
							    	 resultMap.put("st_kod", Integer.parseInt(rowContent.getCodeECPstationStartTrain().substring(0, 5)));	
							    }
								 else resultMap.put("st_kod", Integer.parseInt(rowContent.getCodeECPstationStartTrain()));
							    Integer colStan = npjt.queryForInt(selectStation, resultMap);
							    if(colStan!=0){
								b.setNodeValue("P_9_2_8", rowContent.getCodeECPstationFormTrain());
							    }
							    else{
							    	throw new ServiceException(new Exception(), ServiceError.ERR_WRONG_CODESTATION);
							    }
							}
						}
					}, "table2","row");
				}
				if(rowContent.isSetPark()){
					b.setNodeValue("P_9_3", rowContent.getPark());
				}		
				if(rowContent.isSetRoad()){
					b.setNodeValue("P_9_4", rowContent.getRoad());
				}
				if(rowContent.isSetGrFront()){
					b.setNodeValue("P_9_5", rowContent.getGrFront());
				}		
				if(rowContent.isSetRoadPod()){
					b.setNodeValue("P_9_6", rowContent.getRoadPod());
				}
				if(rowContent.isSetPrimech()){
					b.setNodeValue("P_9_9", rowContent.getPrimech());
				}		
			}
		}, "table1", "row");
	
		
		try{
		if (req.isSetOKPOop()){
	    resultMap.put("okpo_kod", Integer.parseInt(req.getOKPOop()));
	    Integer ColOKPOop = npjt.queryForInt(selectOKPO, resultMap);
	    if(ColOKPOop!=0){
		binder.setNodeValue("P_13a",req.getOKPOop());
	    }
	    else{
	    	throw new ServiceException(new Exception(), ServiceError.ERR_WRONG_CODE_OKPO_OP);    
		 }
		}
		}
		catch(Exception e)
		{
			log.error(TypeConverter.exceptionToString(e));
		}
	    try{
		if (req.isSetINNop()){
		resultMap.put("INN", req.getINNop());
	    Integer ColINNop = npjt.queryForInt(selectINN, resultMap);
		if(ColINNop!=0){
		binder.setNodeValue("P_13b",req.getINNop());
		}
	    else{
	    	throw new ServiceException(new Exception(), ServiceError.ERR_WRONG_INN_OP);    
		 }
		}
	    }catch(Exception e)
	    { 
	    	log.error(TypeConverter.exceptionToString(e));
	    }
	    try{
		if (req.isSetKPPop()){	
		resultMap.put("KPP", req.getKPPop());
		Integer ColKPPop = npjt.queryForInt(selectKPP, resultMap);
		if(ColKPPop!=0){
		binder.setNodeValue("P_13v", req.getKPPop());	
		}
		else{
			throw new ServiceException(new Exception(), ServiceError.ERR_WRONG_KPP_OP);    	    
		 }
		}
	    }catch(Exception e)
	    {
	    	log.error(TypeConverter.exceptionToString(e));

	    }
	    document.setSignLvl(0);
	    document.setType(formname);
		document.setPredId(predId);
		document.setBlDoc(form.encodeToArchiv());
		document.setDocData(form.transform("data"));
//		System.out.println(document.toString());
		try{
		if (reWriteDoc) {
		facade.getDocumentDao().update(document);
		} 
		else { 
    	facade.getDocumentDao().save(document);
		}
		}catch(Exception e)
		{
			log.error(TypeConverter.exceptionToString(e));

		}
		try{
			StringBuffer content = new StringBuffer();
			content.append(name);
			content.append(", ");
			content.append(nameStation);
			content.append(", ");
			content.append(binder.getValuesAsArray("P_9_1").length);
			content.append(" подач");
			
			HashMap<String, Object> pp = new HashMap<String, Object>();
			pp.put("id", document.getId());
			pp.put("content", content.toString());
			getNpjt().update("update snt.docstore set opisanie = :content where id =:id", pp);
		}catch(Exception e){
			log.error(TypeConverter.exceptionToString(e));
		}
		StandartResponseWrapper wrapper = new StandartResponseWrapper();
		wrapper.setDescription("ok");
		wrapper.setCode(0);
		wrapper.setDocumentId(document.getId());
		ResponseAdapter<StandartResponseWrapper> adapter = new ResponseAdapter<StandartResponseWrapper>(
				true, wrapper, ServiceError.ERR_OK);
		
		
		return adapter;
	}

	

	@Override
	protected Object composeResponce(ResponseAdapter<StandartResponseWrapper> adapter) {
		FormCreateResponseDocument responsedoc = FormCreateResponseDocument.Factory.newInstance();
		FormCreateResponse response = responsedoc.addNewFormCreateResponse();

		if (adapter.isStatus()) {
			response.setCode(adapter.getResponse().getCode());
			response.setDescription(adapter.getResponse().getDescription());
			response.setDocid(adapter.getResponse().getDocumentId());
		} else {
			response.setCode(adapter.getError().getCode());
			response.setDescription(adapter.getError().getMessage());
			response.setDocid(-1);
		}

		return responsedoc;
	}
}

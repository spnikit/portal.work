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
import ru.iit.zuvEndPoint.FormCreateRequest;
import ru.iit.zuvEndPoint.FormCreateRequestDocument;
import ru.iit.zuvEndPoint.FormCreateRequest.Table1.Row;
//import ru.iit.xsd.utilTypes.ximport.standartResponsesV10.FormCreateResponse;
//import ru.iit.xsd.utilTypes.ximport.standartResponsesV10.FormCreateResponseDocument;
import ru.iit.zuvEndPoint.FormCreateResponse;
import ru.iit.zuvEndPoint.FormCreateResponseDocument;

public class ZUVEndpoint extends ETDAbstractEndpoint<StandartResponseWrapper>{
    
	private NamedParameterJdbcTemplate npjt;  
	private DataSource ds;
	private String formname;
	protected ZUVEndpoint(Marshaller marshaller) {
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
	    String selectClientName = "select rtrim(vname) from SNT.PRED where okpo_kod = :okpo_kod and headid is null";
	    String selectStation = "select count(0) from SNT.STAN where st_kod = :st_kod";
	    String selectNameStation = "select rtrim(vname) from SNT.STAN where st_kod = :st_kod";;
	    
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
			System.out.println(TypeConverter.exceptionToString(e));
		}
        String name = "";
	    try{
	    name = (String)npjt.queryForObject(selectClientName, resultMap, Object.class); 
	    binder.setNodeValue("P_1", name);
	    }catch(Exception e)
	    {
	    	System.out.println(TypeConverter.exceptionToString(e));
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
	    if(colStan!=0){
	    	binder.setNodeValue("P_4a", req.getCodeECPstation());
	    	String nameStation =(String)npjt.queryForObject(selectNameStation, resultMap, Object.class);
	    	binder.setNodeValue("P_4", nameStation);
	    }
	    else{
	    	throw new ServiceException(new Exception(), ServiceError.ERR_WRONG_CODESTATION);
	    }
	    
        int predId = npjt.queryForInt("select id from snt.pred where okpo_kod = :okpo_kod and headid is null", resultMap);
	   
        if(req.isSetDatePribLok()){
        binder.setNodeValue("P_7_1", req.getDatePribLok());
        }
        if(req.isSetTimePribLok()){
        binder.setNodeValue("P_7_2", req.getTimePribLok());
        }
        binder.setNodeValue("P_7_3", req.getDatePribLok());
        binder.setNodeValue("P_7_4", req.getTimePribLok());
		if(req.isSetDateEnd()){
		binder.setNodeValue("P_8_1", req.getDateEnd());
		}
		if(req.isSetTimeEnd()){
		binder.setNodeValue("P_8_2", req.getTimeEnd());
		}
		binder.setNodeValue("P_8_3", req.getDateEnd());
		binder.setNodeValue("P_8_4", req.getTimeEnd());

		try{
		binder.fillTable(req.getTable1().getRowArray(), new RowFiller<FormCreateRequest.Table1.Row, Object>() {
			public void fillRow(DataBinder b, Row rowContent, int numRow,Object options) throws DOMException, InternalException {
			String rowNum = (numRow + 1) < 10 ? "0" + String.valueOf(numRow + 1) : String.valueOf(numRow + 1);
			  b.setNodeValue("P_9_1", rowNum);
			    
				b.setNodeValue("P_9_2", rowContent.getNumVag());
				
				if(rowContent.isSetGrFront()){
					b.setNodeValue("P_9_5", rowContent.getGrFront());
				}
				if(rowContent.isSetRoad()){
					b.setNodeValue("P_9_6", rowContent.getRoad());
				}
				
				if(rowContent.isSetPrimech()){
					b.setNodeValue("P_9_8", rowContent.getPrimech());
				}		
			}
		}, "table1", "row");
		}catch(Exception e)
		{
			System.out.println(TypeConverter.exceptionToString(e));
		}
		
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
			System.out.println(TypeConverter.exceptionToString(e));
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
	    	System.out.println(TypeConverter.exceptionToString(e));
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
	    	System.out.println(TypeConverter.exceptionToString(e));

	    }
	    document.setSignLvl(0);
	    document.setType(formname);
		document.setPredId(predId);
		document.setBlDoc(form.encodeToArchiv());
		document.setDocData(form.transform("data"));
	//	System.out.println(document.toString());
		try{
		if (reWriteDoc) {
		facade.getDocumentDao().update(document);
		} 
		else {
    	facade.getDocumentDao().save(document);
		}
		}catch(Exception e)
		{
	    	System.out.println(TypeConverter.exceptionToString(e));

		}
		try{
			StringBuffer content = new StringBuffer();
			content.append(name);
			content.append(", ");
			if(req.isSetDatePribLok()){
				content.append(req.getDatePribLok());
				content.append(", ");
			}
			if(req.isSetTimePribLok()){
				content.append(req.getTimePribLok());
				content.append(", ");
			}
			if(req.isSetDateEnd()){
				content.append(req.getDateEnd());
				content.append(", ");
			}
			if(req.isSetTimeEnd()){
				content.append(req.getTimeEnd());
				content.append(", ");
			}
			content.append(binder.getValuesAsArray("P_9_1").length);
			content.append(" вагона(ов)");
			
			HashMap<String, Object> pp = new HashMap<String, Object>();
			pp.put("id", document.getId());
			pp.put("content", content.toString());
			getNpjt().update("update snt.docstore set opisanie = :content where id =:id", pp);
			}catch(Exception e){
				System.err.println(e);
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
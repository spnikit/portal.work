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
import ru.iit.gu2B.gu2BEndpoint.FormCreateRequest;
import ru.iit.gu2B.gu2BEndpoint.FormCreateRequestDocument;
import ru.iit.gu2B.gu2BEndpoint.FormCreateRequest.Table1.Row1;
import ru.iit.gu2B.gu2BEndpoint.FormCreateRequest.Table1.Row1.Table11.Row11;
import ru.iit.gu2B.gu2BEndpoint.FormCreateRequest.Table2;
import ru.iit.gu2B.gu2BEndpoint.FormCreateRequest.Table2.Table21.Row21;
//import ru.iit.xsd.utilTypes.ximport.standartResponsesV10.FormCreateResponse;
//import ru.iit.xsd.utilTypes.ximport.standartResponsesV10.FormCreateResponseDocument;
import ru.iit.gu2B.gu2BEndpoint.FormCreateResponse;
import ru.iit.gu2B.gu2BEndpoint.FormCreateResponseDocument;

public class Gu2bEndpoint extends ETDAbstractEndpoint<StandartResponseWrapper>{
    
	private NamedParameterJdbcTemplate npjt;  
	private DataSource ds;
	private String formname;
	protected Gu2bEndpoint(Marshaller marshaller) {
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
		FormCreateRequestDocument reqDoc = (FormCreateRequestDocument)arg;
		FormCreateRequest request = reqDoc.getFormCreateRequest();
//		System.out.println(reqDoc);
		Document document = new Document();
		ETDForm form = new ETDForm(facade.getDocumentTemplate(formname));
		DataBinder binder = form.getBinder();
		Long docId = null;
		Boolean rewriteDoc = request.isSetDocId();
		String selectOKPO = "select count(0) from SNT.PRED where okpo_kod = :okpo_kod and headid is null";
		String selectINN = "select count(0) from SNT.PRED where INN = :INN and headid is null";
	    String selectKPP = "select count(0) from SNT.PRED where KPP = :KPP and headid is null";
	    String selectClientName = "select rtrim(vname) from SNT.PRED where okpo_kod = :okpo_kod and headid is null";
	    String selectStation = "select count(0) from SNT.STAN where st_kod = :st_kod";
	    String selectNameStation = "select rtrim(vname) from SNT.STAN where st_kod = :st_kod";
	    
		if(rewriteDoc){
			docId = request.getDocId();
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
			
			binder.clearElements("P_1a", "P_4a","P_4b","P_4v","P_5","P_6","P_7");
			binder.clearTable41("table1", "table2", "row");
	        binder.clearTable41("table4", "table5", "row");
		    } else {
			docId = facade.getNextDocumentId();
			document.setId(docId);	
		}
		
		binder.setRootElement("data");
		binder.setNodeValue("sposob", "1");
		HashMap<String, Object> resultMap = new HashMap<String, Object>();
//		if (request.getCodeECPstation().length()==5){
//	    	 resultMap.put("st_kod", Integer.parseInt(("0"+request.getCodeECPstation()).substring(0, 5)));	
//	    }
//		 if (request.getCodeECPstation().length()==6){
//	    	 resultMap.put("st_kod", Integer.parseInt(request.getCodeECPstation().substring(0, 5)));	
//	    }
//		else resultMap.put("st_kod", Integer.parseInt(request.getCodeECPstation()));
		 
		String codestr="";
		 if (request.getCodeECPstation().length()==5){
			 codestr = "0"+request.getCodeECPstation();
		 }else {
			 codestr = request.getCodeECPstation();
		 }
		 
		 resultMap.put("st_kod", Integer.parseInt(codestr.substring(0, 5)));
		
		 
		Integer colStan = npjt.queryForInt(selectStation, resultMap);
	    if(colStan!=0){
	    	binder.setNodeValue("P_1a", codestr);
	    	String nameStation =(String)npjt.queryForObject(selectNameStation, resultMap, Object.class);
	    	binder.setNodeValue("P_1", nameStation);
	    }
	    else{
	    	throw new ServiceException(new Exception(), ServiceError.ERR_WRONG_CODESTATION);
	    }
		resultMap.put("okpo_kod", Integer.parseInt(request.getOKPO()));
		Integer ColOKPO = npjt.queryForInt(selectOKPO, resultMap);
		if(ColOKPO!=0){
		  binder.setNodeValue("P_4a",request.getOKPO());
		}
		else{
	      throw new ServiceException(new Exception(), ServiceError.ERR_WRONG_CODE_OKPO);    
		}
		
		// resultMap.put("okpo_kod", Integer.parseInt(request.getOKPO()));//?
		 String name = (String)npjt.queryForObject(selectClientName, resultMap, Object.class); 
		 binder.setNodeValue("P_4", name);
		
	    if (request.isSetINN()){	   
		   resultMap.put("INN", request.getINN());
		   Integer ColINN = npjt.queryForInt(selectINN, resultMap);
		   if(ColINN!=0){
			  binder.setNodeValue("P_4b",request.getINN());
		   }
		   else{
			  throw new ServiceException(new Exception(), ServiceError.ERR_WRONG_INN);    
		   }
	    }
	    if (request.isSetKPP()){		   
	    	resultMap.put("KPP", request.getKPP());
			Integer ColKPP = npjt.queryForInt(selectKPP, resultMap);
			if(ColKPP!=0){
			 binder.setNodeValue("P_4v", request.getKPP());	
			}
			else{
			 throw new ServiceException(new Exception(), ServiceError.ERR_WRONG_KPP);    	    
			 }
			}
        int predId = npjt.queryForInt("select id from snt.pred where okpo_kod = :okpo_kod and headid is null", resultMap);
		
        binder.setNodeValue("P_5", request.getPlacePod()); 
        if(request.isSetGrFront())
        	binder.setNodeValue("P_6", request.getGrFront());
        if(request.isSetRoad())
        	binder.setNodeValue("P_7", request.getRoad());
        
        
        binder.prettyfillTable(request.getTable1().getRow1Array(), new RowFiller<FormCreateRequest.Table1.Row1, Object>() {
			public void fillRow(DataBinder b, Row1 rowContent, int numRow,Object options) throws DOMException, InternalException {
			String rowNum = (numRow + 1) < 10 ? "0" + String.valueOf(numRow + 1) : String.valueOf(numRow + 1);
				  b.setNodeValue("P_9_1", rowNum);	
				  b.setNodeValue("P_9_2", rowContent.getNumVag());
				  if(rowContent.isSetOKPOsobstv())
					  b.setNodeValue("P_9_2_1", rowContent.getOKPOsobstv());
				  if(rowContent.isSetOperation())
					  b.setNodeValue("P_9_3", rowContent.getOperation());
				  if(rowContent.getTable11().getRow11Array().length != 0){
						b.fillTable(rowContent.getTable11().getRow11Array(), new RowFiller<FormCreateRequest.Table1.Row1.Table11.Row11, Object>() {
							public void fillRow(DataBinder b, Row11 rowContent,
									int numRow, Object options) throws DOMException, InternalException {
						      if(rowContent.isSetNumCont())
						    	  b.setNodeValue("P_9_4_1",rowContent.getNumCont());
						      if(rowContent.isSetTypeCont())
						    	  b.setNodeValue("P_9_4_2", rowContent.getTypeCont());
							}
					}, "table2","row");
			   }
			}
        }, "table1", "row", true);
        if(request.getTable2Array().length > 0){
        binder.prettyfillTable(request.getTable2Array(), new RowFiller<FormCreateRequest.Table2, Object>(){
        	public void fillRow(DataBinder b, Table2 rowContent, int numRow,Object options) throws DOMException, InternalException {
        	String rowNum = (numRow + 1) < 10 ? "0" + String.valueOf(numRow + 1) : String.valueOf(numRow + 1);
			    b.setNodeValue("P_40", rowNum);	
        		if(rowContent.isSetNumVag())
        		   b.setNodeValue("P_41", rowContent.getNumVag());
        		if(rowContent.getTable21().getRow21Array().length!=0){
        			b.fillTable(rowContent.getTable21().getRow21Array(), new RowFiller<FormCreateRequest.Table2.Table21.Row21, Object>() {
        				public void fillRow(DataBinder b, Row21 rowContent,
								int numRow, Object options) throws DOMException, InternalException {
        					if(rowContent.isSetTypeZPU())
        						b.setNodeValue("P_42", rowContent.getTypeZPU());
        					if(rowContent.isSetMarkOnZPU())
        						b.setNodeValue("P_43", rowContent.getMarkOnZPU());
        				}
        			}, "table5", "row");
        		}
        	}
         }, "table4", "row", true);
        }
        document.setSignLvl(0);
	    document.setType(formname);
		document.setPredId(predId);
		document.setBlDoc(form.encodeToArchiv());
		document.setDocData(form.transform("data"));
		System.out.println(form.transform("data"));
		try{
		if (rewriteDoc) {
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
			content.append(request.getPlacePod());
			content.append(", ");
			content.append(request.getGrFront());
			content.append(", ");
			content.append(request.getRoad());
			content.append(", ");
			content.append(binder.getValuesAsArray("P_9_1").length-1);
			content.append(" вагона(ов)");
			
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
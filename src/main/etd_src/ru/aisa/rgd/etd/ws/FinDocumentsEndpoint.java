package ru.aisa.rgd.etd.ws;
import java.text.SimpleDateFormat;
import java.util.List;

import org.springframework.oxm.Marshaller;

import ru.aisa.edt.DocumentsTableRequestDocument;
import ru.aisa.edt.DocumentsTableResponseDocument;
import ru.aisa.edt.Signature;
import ru.aisa.edt.DocumentsTableRequestDocument.DocumentsTableRequest;
import ru.aisa.edt.DocumentsTableResponseDocument.DocumentsTableResponse;
import ru.aisa.edt.DocumentsTableResponseDocument.DocumentsTableResponse.Data;
import ru.aisa.edt.DocumentsTableResponseDocument.DocumentsTableResponse.Data.Doc;
import ru.aisa.rgd.etd.dao.ETDFacade;
import ru.aisa.rgd.etd.extend.ShortContentCreator;
import ru.aisa.rgd.etd.objects.ETDDocument;
import ru.aisa.rgd.etd.objects.ETDDocumentFilter;

public class FinDocumentsEndpoint
		extends
		ETDAbstractSecurityEndoint<DocumentsTableRequestDocument, DocumentsTableResponseDocument> {
	
//	Форматирование даты и времени
	private SimpleDateFormat dateFormater;
	private SimpleDateFormat timeFormater;
	
//	Вещь, создающая контент для столбца "Краткое содержание"
	private ShortContentCreator shortContentCreator;

	public FinDocumentsEndpoint(Marshaller marshaller) {
		super(marshaller);
	}

	@Override
	protected DocumentsTableResponseDocument composeInvalidSecurityResponce(Signature signature) 
	{
		DocumentsTableResponseDocument responseDocument = DocumentsTableResponseDocument.Factory.newInstance();
		DocumentsTableResponse response = responseDocument.addNewDocumentsTableResponse();
		response.addNewData();
		response.addNewSecurity().setLogonstatus("false");
		return responseDocument;
	}

	@Override
	protected DocumentsTableRequestDocument convertRequest(Object obj) {
		DocumentsTableRequestDocument requestDocument = (DocumentsTableRequestDocument) obj;
		return requestDocument;
	}

	@Override
	protected Signature getSecurity(DocumentsTableRequestDocument requestDocument) 
	{
		Signature s = requestDocument.getDocumentsTableRequest().getSecurity();
		return s;
	}

	@Override
	protected DocumentsTableResponseDocument processRequest(DocumentsTableRequestDocument requestDocument, Signature signature, ETDFacade facade) throws Exception 
	{
//		System.out.println("fin");
		
		
DocumentsTableRequest request = requestDocument.getDocumentsTableRequest();
		
		String sort = request.getFilter().getSort();
		String sortDir = request.getFilter().getDir();
		DocumentsTableResponseDocument responseDocument = DocumentsTableResponseDocument.Factory.newInstance();
		DocumentsTableResponse response = responseDocument.addNewDocumentsTableResponse();
		
		response.setSecurity(signature);

		
		ETDDocumentFilter filter = new ETDDocumentFilter(request.isSetFilter() ? request.getFilter() : request.addNewFilter());
		
		List<Long> ids = facade.getFinDocumentIDs(filter);
		List<ETDDocument> documents = facade.getFinDocuments(filter, ids);
		int kolvo_doc=ids.size();
		
		Data data = response.addNewData();
		for (ETDDocument doc : documents)
		{
			//if(CheckDocsActive.check(doc,facade,filter)){
				Doc xml = data.addNewDoc();
				xml.setCDel(doc.getCDel().toString());
				xml.setCCreateSource(String.valueOf(doc.getCCreateSourse()));
				xml.setName(doc.getName());
				xml.setNumber(doc.getNumber());
				xml.setCreator(doc.getCreator());
				xml.setId(doc.getId());
				xml.setCreateDate(getDateFormater().format(doc.getCreateDate()).concat(" ").concat(getTimeFormater().format(doc.getCreateTime())));
				if (doc.getLastDate() != null && doc.getLastTime() != null)
				{
					xml.setLastDate(getDateFormater().format(doc.getLastDate())+ " " + getTimeFormater().format(doc.getLastTime()));
					xml.setLastSigner(doc.getLastSigner());
				}
				else
				{
					xml.setLastDate("");
					xml.setLastSigner("");
				};
				if (doc.getSfSign()>-1)
					xml.setSfSign(doc.getSfSign());
//				xml.setShort(shortContentCreator.createValue(doc.getName(), doc.getData()));
				xml.setIdpak(doc.getIdPak());
				xml.setDognum(doc.getDognum());
				xml.setVagnum(doc.getVagnum());
				
				xml.setReqDate(doc.getReqdate());
				xml.setStatus(doc.getStat());
					xml.setDi(doc.getDi());
			
					xml.setRemPred(doc.getRem_pred());
					if (doc.getReqnum().length()>1)
						xml.setPackstatus(doc.getReqnum());

						xml.setServicetype(doc.getServicetype());
						if (doc.getPrice()!=null){
							xml.setPrice(doc.getPrice());
						}			
						if (doc.getOtcname()!=null){
							xml.setOtcName(doc.getOtcname());
						}	
						if (doc.getOtctype()!=null){
							xml.setOtcType(doc.getOtctype());
						}	
						if (doc.getSftype()!=null){
							xml.setSfType(doc.getSftype());
						}	
//						System.out.println(doc.getDroptxt());
						
						xml.setDroptxt(doc.getDroptxt());
						
						if(doc.getEtdid()!= null) {
							xml.setEtdid(doc.getEtdid());
						}
						if(doc.getNumberSf()!=null){
							xml.setNumberSf(doc.getNumberSf());
						}
						if(doc.getNumberFpu()!=null){
							xml.setNumberFpu(doc.getNumberFpu());
						}
		}
		response.setTotalCount(kolvo_doc);
				
		if(sort!=null && (sort.equalsIgnoreCase("short")/* || sort.equalsIgnoreCase("number"))*/)){
			DocumentSorter sorter = new DocumentSorter();
			Doc[] dc = data.getDocArray();
			
			if (sort.equalsIgnoreCase("short"))	dc = sorter.sortByShortContent(dc, sortDir);
			//else if(sort.equalsIgnoreCase("number")) dc = sorter.sortByDocumentNumber(dc, sortDir);
			
			int from = request.getFilter().getStart();
			int to = request.getFilter().getLimit();
			int size = dc.length;
			if(to>size) to = size;	
//			dc = Arrays.copyOfRange(dc, from, to);
//			data.setDocArray(dc);
			Doc[] tempDoc = new Doc[to-from];
			for(int i=from; i<to; i++) tempDoc[i-from]=dc[i];
			data.setDocArray(tempDoc);
			response.setData(data);
		}
		
		return responseDocument;
	}

	public SimpleDateFormat getDateFormater() {
		return dateFormater;
	}

	public void setDateFormater(SimpleDateFormat dateFormater) {
		this.dateFormater = dateFormater;
	}

	public SimpleDateFormat getTimeFormater() {
		return timeFormater;
	}

	public void setTimeFormater(SimpleDateFormat timeFormater) {
		this.timeFormater = timeFormater;
	}

	public ShortContentCreator getShortContentCreator() {
		return shortContentCreator;
	}

	public void setShortContentCreator(ShortContentCreator shortContentCreator) {
		this.shortContentCreator = shortContentCreator;
	}

}

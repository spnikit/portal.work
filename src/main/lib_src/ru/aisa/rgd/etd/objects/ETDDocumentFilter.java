package ru.aisa.rgd.etd.objects;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import ru.aisa.edt.DocumentsTableRequestDocument.DocumentsTableRequest.Filter;
import ru.aisa.rgd.ws.utility.TypeConverter;



public class ETDDocumentFilter {
	private String dir;
	private String sort;
	private String sort2;
	private Integer start;
	private Integer limit;
	private String dateBefore;
	private String dateAfter;
	private String dateEqual;
	private boolean status;
	
	private String shift;
	
	private Integer formType;
	private Integer workerId;
	private Integer predId;
	private Integer persId;
	private List<Long> idList;
	private boolean isSystemWorker;
	
	private Integer name_filter;
	private Integer wrk_filter;
	private Integer pred_filter;
	private Integer page;
	
	private Integer funcRole;
	private List<String> datesList;
	private static Map<String, String> sortMapper;
	static
	{
		sortMapper = new HashMap<String, String>(10);
		sortMapper.put("number", "ds.No");
		sortMapper.put("creator", "predname");		
		sortMapper.put("createDate", "createDate");
		sortMapper.put("lastDate", "lastDate"/*"ds.ldate"*/);
		sortMapper.put("id", "ds.id");
		sortMapper.put("lastSigner", "lastSigner");
		sortMapper.put("name", "name");
		sortMapper.put("short", "short");
		sortMapper.put("vagnum", "vagnum");
		sortMapper.put("reqdate", "reqdate");
		sortMapper.put("dognum", "no");
		sortMapper.put("content", "content");
		sortMapper.put("di", "di");
		sortMapper.put("rem_pred", "rem_pred");
		sortMapper.put("idpak", "idpak");
		sortMapper.put("idpak", "idpak");
		sortMapper.put("price", "price");
		sortMapper.put("otcname", "otc_name");
		sortMapper.put("otctype", "otc_type");
		sortMapper.put("sftype", "sf_type");	
		sortMapper.put("etdid", "etdid");	
		sortMapper.put("servicetype", "servicecode");	
		sortMapper.put("packst", "reqnum");	
	}
	
	private static Logger	log	= Logger.getLogger(ETDDocumentFilter.class);
	
//	//TODO Delete default constructor
//	public ETDDocumentFilter()
//	{
//	
//	}
	
	/**
	 * Парсит параметры фильтра запроса и, если они не определены, задает значения
	 * по умолчанию.
	 * @param filter
	 */
	public ETDDocumentFilter(Filter filter)
	{
		
	  
	    dateAfter = (filter.isSetDateAfter() ? filter.getDateAfter() : null);
		dateBefore = (filter.isSetDateBefore() ? filter.getDateBefore() : null);
		dateEqual = (filter.isSetDateEqual() ? filter.getDateEqual() : null);
		formType = (filter.isSetFormType() ?  filter.getFormType() : null);
		limit = (filter.isSetLimit() ? filter.getLimit() : 20);
		predId = (filter.isSetPredId() ?  filter.getPredId() : -1);
		persId = (filter.isSetPersId() ?  filter.getPersId() : -1);
		//System.out.println("dir "+filter.isSetDir()+" "+filter.getDir() );
		dir = (filter.isSetDir() ? filter.getDir() : "ASC");
		//System.out.println("dir "+dir );
		shift = (filter.isSetShift() ? filter.getShift() : null);
		
		String s = filter.getSort();
		if (sortMapper.containsKey(s)) {
			sort = sortMapper.get(s);
			if (sort.equalsIgnoreCase("createDate")) {
				sort2 = "createTime";
			} else {
				sort2 = null;
			}
		} else {
			sort = "vagnum";
			sort2 = "CrDate";
		}

		if(s.equals("packst")) {
			status = true;
		} else {
			status = false;
		}
		
		start = (filter.isSetStart() ? filter.getStart() : 1);
		workerId = (filter.isSetWorkerId() ? filter.getWorkerId() : -1);
		funcRole = (filter.isSetFuncRole() ? filter.getFuncRole() : -1);
		wrk_filter = (filter.isSetWrkFilter() ? filter.getWrkFilter() : -1);
		pred_filter = (filter.isSetPredFilter() ? filter.getPredFilter() : -1);
		name_filter = (filter.isSetNameFilter() ? filter.getNameFilter() : -1);
		page = (filter.isSetPage() ? filter.getPage() : -1);
		
		idList = (filter.sizeOfIdListArray() > 0 ? TypeConverter.arrayToList(filter.getIdListArray()) : null);
		isSystemWorker = (filter.isSetSystemWorker() ? filter.getSystemWorker() : false);
		log.debug(this.toString());
	}

	public String getDir() {
		return dir;
	}

	public void setDir(String dir) {
		this.dir = dir;
	}
	
	public String getShift() {
		return shift;
	}

	public void setShift(String shift) {
		this.shift = shift;
	}
	
	public Integer getFuncRole() {
		return funcRole;
	}

	public void setFuncRole(Integer funcRole) {
		this.funcRole = funcRole;
	}

	public Integer getLimit() {
		return limit;
	}

	public void setLimit(int limit) {
		this.limit = limit;
	}

	public String getSort() {
		return sort;
	}

	public void setSort(String sort) {
		this.sort = sort;
	}

	public Integer getStart() {
		return start;
	}

	public void setStart(int start) {
		this.start = start;
	}

	public String getDateAfter() {
		return dateAfter;
	}

	public void setDateAfter(String dateAfter) {
		this.dateAfter = dateAfter;
	}

	public String getDateBefore() {
		return dateBefore;
	}

	public void setDateBefore(String dateBefore) {
		this.dateBefore = dateBefore;
	}

	public Integer getFormType() {
		return formType;
	}

	public void setFormType(int formType) {
		this.formType = formType;
	}

	public Integer getPredId() {
		return predId;
	}

	public void setPredId(Integer predId) {
		this.predId = predId;
	}

	public Integer getPersId() {
		return persId;
	}

	public void setPersId(Integer persId) {
		this.persId = persId;
	}
	
	public Integer getWrkFilter() {
		return wrk_filter;
	}

	public void setWrkFilter(Integer wrk_filter) {
		this.wrk_filter = wrk_filter;
	}
	
	public Integer getNameFilter() {
		return name_filter;
	}

	public void setNameFilter(Integer name_filter) {
		this.name_filter = name_filter;
	}
	
	public Integer getPredFilter() {
		return pred_filter;
	}

	public void setPredFilter(Integer pred_filter) {
		this.pred_filter = pred_filter;
	}
	
	public Integer getPage() {
		return page;
	}

	public void setPage(Integer page) {
		this.page = page;
	}
	
	public Integer getWorkerId() {
		return workerId;
	}

	public void setWorkerId(Integer workerId) {
		this.workerId = workerId;
	}

	public void setFormType(Integer formType) {
		this.formType = formType;
	}

	public void setLimit(Integer limit) {
		this.limit = limit;
	}

	public void setStart(Integer start) {
		this.start = start;
	}
	public List<Long> getIdList() {
		return idList;
	}

	public void setIdList(List<Long> idList) {
		this.idList = idList;
	}
	
	public List<String> getDatesList() {
		return datesList;
	}

	public void setDatesList(List<String> datesList) {
		this.datesList = datesList;
	}

	@Override
	public String toString() 
	{
		String NL = "; "; //Character.LINE_SEPARATOR;
		StringBuffer buff = new StringBuffer();
		try
		{
			Field[] fields = getClass().getDeclaredFields();
	
			for(Field f : fields)
			{
				buff.append(f.getName() + " = " + f.get(this) + NL);
			}
		}
		catch(Exception e){}
		return buff.toString();
	}

	public String getDateEqual() {
		return dateEqual;
	}

	public void setDateEqual(String dateEqual) {
		this.dateEqual = dateEqual;
	}

	public String getSort2() {
		return sort2;
	}

	public void setSort2(String sort2) {
		this.sort2 = sort2;
	}

	public void setSystemWorker(boolean isSystemWorker) {
		this.isSystemWorker = isSystemWorker;
	}

	public boolean isSystemWorker() {
		return isSystemWorker;
	}

	public boolean isStatus() {
		return status;
	}

	public void setStatus(boolean status) {
		this.status = status;
	}
	
}

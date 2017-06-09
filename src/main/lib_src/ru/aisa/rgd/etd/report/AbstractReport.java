package ru.aisa.rgd.etd.report;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import ru.aisa.rgd.etd.extend.SNMPSender;
import ru.aisa.rgd.ws.dao.ServiceFacade;
import ru.aisa.rgd.ws.documents.ETDForm;
import ru.aisa.rgd.ws.domain.Template;
import ru.aisa.rgd.ws.exeption.ServiceException;
import ru.aisa.rgd.ws.utility.TypeConverter;

/**
 * @author anton
 *
 * @param <T> Тип объектов, возвращаемых методом getData, обычно String - данные из базы
 * @param <E> Тип объектов, возаращаемый parseDate, для простых отчетов Map<String, String>
 */
public abstract class AbstractReport<T, E> implements IReport{
	
	//Название шаблона отчета
	private String reportTemplateName;
	//Название шаблона формы
	private String formTemplateName;
	//По какому предприятию
	private int enterpriseId;
	//Дата начала периода формирования отчета
	private Date reportBeginDate;
	//Дата конца периода формирования отчета
	private Date reportEndDate;
	//ID роли
	private int wrkid;
	//ID дороги
	private int dorId;
	

	protected Logger	log	= Logger.getLogger(getClass());
	
	public AbstractReport() {}
	
	/*public AbstractReport(String reportTemplateName, String formTemplateName, int enterpriseId, Date reportBeginDate, Date reportEndDate) {
		this.reportTemplateName = reportTemplateName;
		this.formTemplateName = formTemplateName;
		this.enterpriseId = enterpriseId;
		this.reportBeginDate = reportBeginDate;
		this.reportEndDate = reportEndDate;
	}*/
	
	//Достаем данные для составления отчета
	public abstract List<T> getData(ServiceFacade facade, String formTemplateName, int enterpriseId, Date reportFromDate, Date reportToDate) throws Exception;
			
	//Парсим данные из форм defult Map<String, String>
	public abstract E parseData(List<T> rawData) throws Exception;
	
	public abstract ETDForm createReport(E data , ETDForm reportForm, ServiceFacade facade, int enterpriseId, Date reportBeginDate, Date reportEndDate) throws Exception;
	
	//Достает форму отчета
	private ETDForm getReportForm(ServiceFacade facade) throws ServiceException, IOException
	{
		Template template = facade.getTemplateByName(reportTemplateName);
		ETDForm form = new ETDForm(template.getTemplate());
		return form;
	}
	
	//Формирование отчета за месяц
	protected String checkMonth(Date begin, Date end){
		String result = "";
		GregorianCalendar beginGrCal = new GregorianCalendar();
		beginGrCal.setTime(begin);
		GregorianCalendar endGrCal = new GregorianCalendar();
		endGrCal.setTime(end);
		GregorianCalendar currentGrCal = new GregorianCalendar();
		currentGrCal.setTime(new Date());
		if(!(beginGrCal.get(Calendar.YEAR)==endGrCal.get(Calendar.YEAR) 
				&& beginGrCal.get(Calendar.MONTH)==endGrCal.get(Calendar.MONTH) 
				&& beginGrCal.get(Calendar.DAY_OF_MONTH)==beginGrCal.getMinimum(Calendar.DAY_OF_MONTH)				 
				&& (endGrCal.get(Calendar.DAY_OF_MONTH)==beginGrCal.getActualMaximum(Calendar.DAY_OF_MONTH)
					||(endGrCal.get(Calendar.YEAR)==currentGrCal.get(Calendar.YEAR) 
							&& endGrCal.get(Calendar.MONTH)==currentGrCal.get(Calendar.MONTH)
							&& endGrCal.get(Calendar.DAY_OF_MONTH)==currentGrCal.get(Calendar.DAY_OF_MONTH))))){
				result = "Данный отчет формируется только за месяц";
			
		}
		return result;
	}
	
	protected String checkDates(Date beginDate, Date endDate){
		return "";
	}
	
	public Map<String, String> getForm(ServiceFacade facade)
	{
		Map<String, String> map = new HashMap<String, String>();
		String report = "";
		String error = "";
		try
		{
			if(checkDates(reportBeginDate, reportEndDate).length()>0){
				error = checkDates(reportBeginDate, reportEndDate);				
			}else{
				List<T> rawData = getData(facade, formTemplateName, enterpriseId, reportBeginDate, reportEndDate);
				E data = parseData(rawData);
				ETDForm form = getReportForm(facade);
				ETDForm reportDocument = createReport(data, form, facade, enterpriseId, reportBeginDate, reportEndDate);
				report = reportDocument.transform();
			}
		}
		catch(ServiceException e)
		{
			log.error("Error > code : " + e.getError().getCode() + " message:" + e.getError().getMessage());
			report = "";
		}
		catch(Exception e)
		{
			log.error(TypeConverter.exceptionToString(e));
//			SNMPSender.sendMessage(e);
			report = "";
		}
		map.put("report", report);
		map.put("error", error);
		return map;
	}

	public int getEnterpriseId() {
		return enterpriseId;
	}

	public void setEnterpriseId(int enterpriseId) {
		this.enterpriseId = enterpriseId;
	}

	public String getFormTemplateName() {
		return formTemplateName;
	}

	public void setFormTemplateName(String formTemplateName) {
		this.formTemplateName = formTemplateName;
	}

	public Date getReportBeginDate() {
		return reportBeginDate;
	}

	public void setReportBeginDate(Date reportFromDate) {
		this.reportBeginDate = reportFromDate;
	}

	public void setReportEndDate(Date reportToDate) {
		this.reportEndDate = reportToDate;
	}

	public Date getReportEndDate() {
		return reportEndDate;
	}

	public String getReportTemplateName() {
		return reportTemplateName;
	}

	public void setReportTemplateName(String reportTemplateName) {
		this.reportTemplateName = reportTemplateName;
	}
	
	public int getWrkid() {
		return wrkid;
	}

	public void setWrkid(int wrkid) {
		this.wrkid = wrkid;
	}
	
	public int getDorId() {
		return dorId;
	}

	public void setDorId(int dorId) {
		this.dorId = dorId;
	}
}

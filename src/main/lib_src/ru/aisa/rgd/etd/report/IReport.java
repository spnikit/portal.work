package ru.aisa.rgd.etd.report;

import java.util.Date;
import java.util.Map;

import ru.aisa.rgd.ws.dao.ServiceFacade;

public interface IReport 
{
	public Map<String, String> getForm(ServiceFacade facade);
	public int getEnterpriseId();
	public void setEnterpriseId(int enterpriseId);
	public String getFormTemplateName();
	public void setFormTemplateName(String formTemplateName);
	public Date getReportBeginDate();
	public void setReportBeginDate(Date reportDate);
	public Date getReportEndDate();
	public void setReportEndDate(Date reportDate);
	public String getReportTemplateName();
	public void setReportTemplateName(String reportTemplateName);

}

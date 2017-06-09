package ru.aisa.rgd.ws.dao;

import java.sql.SQLException;
import java.util.List;

import ru.aisa.rgd.etd.objects.DiObject;
import ru.aisa.rgd.etd.objects.ReportData;
import ru.aisa.rgd.ws.domain.Enterprise;
import ru.aisa.rgd.ws.exeption.ServiceException;

public interface ReportDao {
	
	int getFormId (String formname) throws ServiceException;
	List<ReportData> getReportData (int predid, int type, String dateot, String dateto)  throws ServiceException;
	List<DiObject> getDors()  throws ServiceException;
	
}

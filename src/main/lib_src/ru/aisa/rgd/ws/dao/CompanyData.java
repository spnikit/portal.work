package ru.aisa.rgd.ws.dao;

import java.util.List;

import ru.aisa.rgd.ws.exeption.ServiceException;
import sheff.rjd.services.billing.CompanyDataObject;

public interface CompanyData {

		public List<Long> getPredids() throws ServiceException;
		/*public List<CompanyDataObject> getAllCompanyDoc(List<Integer> predIds, int month, int year) throws ServiceException;
		public List<CompanyDataObject> getSignedCompanyDoc(List<Integer> predIds, int month, int year) throws ServiceException;*/
	public List<CompanyDataObject> getDataCompany(int month, int year, List<Long> predIds) throws ServiceException;
}

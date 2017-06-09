package ru.aisa.rgd.ws.dao;

import java.util.List;

import org.springframework.dao.IncorrectResultSizeDataAccessException;

import ru.aisa.rgd.etd.objects.AcceptDoc;
import ru.aisa.rgd.etd.objects.ETDDocument;
import ru.aisa.rgd.ws.domain.BankRequisites;
import ru.aisa.rgd.ws.domain.Requisites;
import ru.aisa.rgd.ws.exeption.ServiceException;
import sheff.rjd.services.contraginvoice.DropObj;
import sheff.rjd.services.contraginvoice.PackdocObj;
import sheff.rjd.services.contraginvoice.PersonObj;

public interface ContragDao {
	public List<ETDDocument> Contraglist(String inn, String kpp, String date1,
			String date2, String formname) throws ServiceException;

	public int predcount(String inn, String kpp) throws ServiceException;

	
	public int docstoreadd(String cert, String reason,long docid) throws ServiceException;
	
	public boolean docchecktype(long docid) throws ServiceException;
	
	public int regcount(String cert, String inn, String kpp)
			throws ServiceException;

	public int rightid(long docid, String inn, String kpp) throws ServiceException;

	public String getDocdata(long docid) throws ServiceException;
	
	public String getDocdatabyetdid(long docid) throws ServiceException;
	
	public int getDocstatus(long docid) throws ServiceException;
	
	public boolean checkifarchieve(long docid) throws ServiceException;
	
	public boolean checkifarchieveordropped(long etdid) throws ServiceException;
	
	public int rightidpak(String id_pak, String inn, String kpp) throws ServiceException;
	
	public List<PackdocObj> PackDoclist(String inn, String kpp, String id_pak) throws ServiceException;
	
	public Requisites getRequisitesByPredId(int predid) throws IncorrectResultSizeDataAccessException;
	
	public BankRequisites getBankRequisitesByPredId(int predid) throws IncorrectResultSizeDataAccessException;
	
	public PersonObj getPersbyCert(String cert) throws IncorrectResultSizeDataAccessException;
	
	public DropObj getDropInfo(long docid) throws IncorrectResultSizeDataAccessException;
	
	public List<AcceptDoc> getAcceptedDoc(String cert_id,String pacid, String inn, String kpp) throws ServiceException;
	public boolean checkpacknew(long docid) throws IncorrectResultSizeDataAccessException;
	public int packExist(String idpack, String inn, String kpp) throws ServiceException;
	public String getNameByEtdid(long etdid) throws ServiceException;
	public boolean InvoicesFilled(long etdid) throws ServiceException;
//	public String getBldoc(long docid) throws ServiceException;
}

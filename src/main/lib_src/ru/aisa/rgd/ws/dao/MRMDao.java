package ru.aisa.rgd.ws.dao;

import java.sql.Timestamp;
import java.util.Map;

import org.springframework.dao.DataAccessException;

import ru.aisa.rgd.ws.domain.MRMvag;

public interface MRMDao
	{
	public static final String	lastBulidDate	= "11.06.2013 17:32:49";
	
	public int generateIdSes() throws Exception;
	
	// public void createNewSes(final int id_ses, final String id_dev, final
	// String fio, final int tabel_num,
	// final String card_num, final int persid, final int cnsi_dor, final int
	// cnsi_pred, final String opendate) throws Exception;
	
	public void createNewSes(final MRMvag mrmvag) throws Exception;
	
	public Timestamp getCloseDate(final int id_ses) throws DataAccessException;
	
	public MRMvag getSes(final int id_ses) throws DataAccessException;
	
	public void closeSes(final MRMvag mrmvag) throws Exception;
	}

package ru.aisa.rgd.ws.dao;

import ru.aisa.rgd.ws.domain.PeregMs;
import ru.aisa.rgd.ws.exeption.ServiceException;

public interface PeregMsDao {	
	
	PeregMs getById(int id) throws ServiceException;

}

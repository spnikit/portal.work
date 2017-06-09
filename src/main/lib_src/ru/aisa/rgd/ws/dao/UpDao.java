package ru.aisa.rgd.ws.dao;

import ru.aisa.rgd.ws.domain.Up;
import ru.aisa.rgd.ws.exeption.ServiceException;

public interface UpDao {	
	
	Up getById(int id) throws ServiceException;
	Up getByCode(int code) throws ServiceException;

}

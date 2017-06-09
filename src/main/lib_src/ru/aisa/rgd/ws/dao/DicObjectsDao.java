package ru.aisa.rgd.ws.dao;

import ru.aisa.rgd.ws.domain.DicObjects;
import ru.aisa.rgd.ws.exeption.ServiceException;

public interface DicObjectsDao {	
	
	DicObjects getByCode(int code) throws ServiceException;
	DicObjects getByCodeAndClassID(int code, int classid) throws ServiceException;

}

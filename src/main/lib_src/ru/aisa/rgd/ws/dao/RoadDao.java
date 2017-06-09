package ru.aisa.rgd.ws.dao;

import ru.aisa.rgd.ws.domain.Road;
import ru.aisa.rgd.ws.exeption.ServiceException;

public interface RoadDao {
	
	/**
	 * @param id Идентификатор ЖД в системе
	 * @return Наименование ЖД
	 * @throws ServiceException Если дороги с таким ID не существует
	 */
	String getName(int id) throws ServiceException;
	
	/**
	 * @param id Идентификатор ЖД в системе
	 * @return Краткое наименование ЖД
	 * @throws ServiceException  Если дороги с таким ID не существует
	 */
	String getShortName(int id) throws ServiceException;
	
	/**
	 * @param id Идентификатор ЖД в системе
	 * @return Объект ЖД
	 * @throws ServiceException  Если дороги с таким ID не существует
	 */
	Road getById(int id) throws ServiceException;
	
	String getNameByEnterpriseId(int enterpriseId);
	

}

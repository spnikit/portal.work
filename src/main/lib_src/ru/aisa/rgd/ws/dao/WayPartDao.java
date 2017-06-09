package ru.aisa.rgd.ws.dao;

import ru.aisa.rgd.ws.domain.WayPart;
import ru.aisa.rgd.ws.exeption.ServiceException;

public interface WayPartDao {
	
	/**
	 * @param code Код участка пути
	 * @return Наименовани участка пути
	 * @throws ServiceException Если объект с данным кодом отсутствует в базе
	 */
	String getNameByCode(int code) throws ServiceException;
	/**
	 * @param id ИД участка пути
	 * @return Объект участка пути
	 * @throws ServiceException Если объект с данным ИД отсутствует в базе
	 */
	WayPart getById(int id)  throws ServiceException;
	WayPart getByCode(int code) throws ServiceException;

}

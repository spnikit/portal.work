package ru.aisa.rgd.ws.dao;

import java.util.List;

import ru.aisa.rgd.ws.domain.Enterprise;
import ru.aisa.rgd.ws.exeption.ServiceException;

public interface EnterpriseDao {
	

	/**
	 * @param type Типа предприятия
	 * @param code Код предприятия в системе
	 * @return Наименование предприятия
	 * @throws ServiceException Если нет соответсвтующего предприятия 
	 */
	String getName(int type, int code) throws ServiceException;

	
	/**
	 * @param type Типа предприятия
	 * @param code Код предприятия в системе
	 * @return Идентификатор предприятия
	 * @throws ServiceException Если нет соответсвтующего предприятия
	 */
	Integer getId(int type, int code) throws ServiceException ;
	
	/**
	 * @param type Типа предприятия
	 * @param code Код предприятия в системе
	 * @return Наименование предприятия и ж.д.
	 * @throws ServiceException Если нет соответсвтующего предприятия
	 */
	String getFullName(int type, int code) throws ServiceException;
	
	/**
	 * @param type Типа предприятия
	 * @param code Код предприятия в системе
	 * @return Дополнительное наименование предприятия 
	 * @throws ServiceException Если нет соответсвтующего предприятия
	 */
	String getAdditionalName(int type, int code) throws ServiceException;
	
	/**
	 * @param type Типа предприятия
	 * @param code Код предприятия в системе
	 * @return Краткое наименование предприятия
	 * @throws ServiceException 
	 */
	String getShortName(int type, int code) throws ServiceException;
	
	/**
	 * @param type Типа предприятия
	 * @param code Код предприятия в системе
	 * @return Информация о предприятии
	 * @throws ServiceException Если нет соответсвтующего предприятия
	 */
	Enterprise get(int type, int code) throws ServiceException;
	
	Enterprise getById(int id) throws ServiceException;
	
	Enterprise getEnterpriseByKleimo(int kleimo) throws ServiceException;
	
	Enterprise getByCode(int code) throws ServiceException;
	
	String getNameByCode(int code) throws ServiceException;
	
	String getUrlByDocumentId(Integer predid);
	
	String getNameById(int id) throws ServiceException;
	
	String getNameByCodeAndRoadId(int code, int roadId) throws ServiceException;
	
	Enterprise getByCodeAndRoadId(int code, int roadId) throws ServiceException;
	
	String getNameByCodeAndType(int code, List<Integer> types) throws ServiceException;
	
	Enterprise getByCodeAndType(int code, List<Integer> types) throws ServiceException; 
	
	Enterprise getShopById(int id) throws ServiceException;
	
	Enterprise getByOtrKod(int code) throws ServiceException;
	
	Enterprise getByOkpoKod(int code) throws ServiceException;
	
	int getOkpoKodByOtrKod(int code) throws ServiceException;
	
	Enterprise getFromObjOsnInf(int obj_osn_id) throws ServiceException;

	Enterprise getByIdFromPredCnsi(int code)throws ServiceException;
}

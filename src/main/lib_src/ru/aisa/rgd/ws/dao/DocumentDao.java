package ru.aisa.rgd.ws.dao;

import java.io.InputStream;
import java.util.Date;
import java.util.List;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.simple.ParameterizedRowMapper;

import ru.aisa.rgd.etd.objects.ETDDocument;
import ru.aisa.rgd.ws.domain.Document;
import ru.aisa.rgd.ws.domain.SFinfo;
import ru.aisa.rgd.ws.domain.Signature;
import ru.aisa.rgd.ws.exeption.ServiceException;
import sheff.rjd.services.syncutils.SyncObj;


public interface DocumentDao {

	Document getById(long id)  throws DataAccessException;
	
	Document getByEtdId(long etdid)  throws DataAccessException;
	
	void updateData(Document document) throws DataAccessException;
	
	void updatePredPrip(long docid,int predid) throws DataAccessException;
	
	InputStream getTemplate(String name) throws DataAccessException, ServiceException;
	
	Long getNextId() throws DataAccessException;
	
	void save(Document document) throws DataAccessException;
	
	Long getNumTu174(String datetime,String nom_tab) throws DataAccessException;
	
	String getDropTime (Long docid) throws DataAccessException;
	
	void delete(long id) throws DataAccessException;
	
	//void InsertIntoMarsh(long docid, int predid) throws DataAccessException;
	
	void InsertIntoMarsh(long docid, int predid, int persid) throws DataAccessException;
	
	void update(Document document) throws DataAccessException;
	
	void updateVisibleTo0(Document document) throws DataAccessException;
	
	Long getPackageById (long id);
	
	void insertXMLandSign (byte[] file, byte[] sign, long docId, long typeId, long predId, String id_pack) throws DataAccessException;
	
	long getTypeId (long docId) throws DataAccessException;
	
	String getId_pack (long id);
	
	void updatePackageForSF (long id);

	void moveToArchive(long docid,int predid,String certid,String rolename);
	/**
	 * @param id Идентификатор документа
	 * @return Номер документа (В базе как строка)
	 */
	String getNumber(long id);
	
	/**
	 * @param id Идентификатор документа
	 * @return Список просавленных подписей, отсортированных по ORDER в возрастающем порядке
	 */
	List<Signature> getSignatures(long id);
	
	/**
	 * @param id Идентификатор документа
	 * @return Последняя подпись
	 */
	Signature getLastSignature(long id);
	
	/**
	 * @param id Идентификатор документа
	 * @return true Если в кокументе проставлены все подписи
	 */
	boolean isFinallySigned(long id);
	
	
	/**
	 * @param stationId  Идентификатор документа
	 * @param templateId Идентификатор шаблона
	 * @return
	 * @throws ServiceException Если процедура БД вернула NULL
	 */
	Integer getNextProtocolNumber(int stationId, int templateId) throws ServiceException;
	
	/**
	 * Порядковый номер документа с начала месяца по данному предприятию
	 * @param enterpriseId
	 * @param templateId
	 * @return
	 * @throws ServiceException
	 */
	Integer getSerialNumberForMonth(int enterpriseId, int templateId) throws ServiceException;
	
	Integer getSerialNumberForYear(int enterpriseId, int templateId) throws ServiceException;
	
	<T> List<T> getDataForMonth(int enterpriseId, int month, int year, String templateName, ParameterizedRowMapper<T> mapper)  throws ServiceException;
	
	<T> List<T> getDataForDate(int enterpriseId, Date date, String templateName, ParameterizedRowMapper<T> mapper)  throws ServiceException;
	
	<T> List<T> getDataForYear(int enterpriseId, int year, String templateName, ParameterizedRowMapper<T> mapper)  throws ServiceException;
	
	<T> List<T> getDataForBeginAndEndDate(int enterpriseId, Date beginDate, Date endDate, String templateName, ParameterizedRowMapper<T> mapper)  throws ServiceException;
	
	<T> List<T> getDataForBeginAndEndDateAll(int enterpriseId, Date beginDate, Date endDate, String templateName, ParameterizedRowMapper<T> mapper)  throws ServiceException;
	
	<T> List<T> getDataForBeginAndEndDateDUXX(int enterpriseId, Date beginDate, Date endDate, String templateName, ParameterizedRowMapper<T> mapper)  throws ServiceException;
	
	<T> List<T> getDataFor3Years(int enterpriseId, int year, String templateName, ParameterizedRowMapper<T> mapper)  throws ServiceException;
	void updateDropData(Document document);
	
}

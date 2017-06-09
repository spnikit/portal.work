package ru.aisa.rgd.ws.dao;

import java.io.InputStream;
import java.util.List;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.ParameterizedRowMapper;
import org.springframework.transaction.support.TransactionCallback;


import ru.aisa.rgd.ws.domain.ActivityKind;
import ru.aisa.rgd.ws.domain.DicObjects;
import ru.aisa.rgd.ws.domain.Document;
import ru.aisa.rgd.ws.domain.Enterprise;
import ru.aisa.rgd.ws.domain.Gruz;
import ru.aisa.rgd.ws.domain.PeregMs;
import ru.aisa.rgd.ws.domain.Persona;
import ru.aisa.rgd.ws.domain.River;
import ru.aisa.rgd.ws.domain.Road;
import ru.aisa.rgd.ws.domain.Signature;
import ru.aisa.rgd.ws.domain.Template;
import ru.aisa.rgd.ws.domain.TerritorialObject;
import ru.aisa.rgd.ws.domain.Up;
import ru.aisa.rgd.ws.domain.Vagsob;
import ru.aisa.rgd.ws.domain.WayPart;
import ru.aisa.rgd.ws.exeption.ServiceException;
import sheff.rjd.dispatcher.Dispatcher;

public interface ServiceFacade {

	Object 	executeInTransaction(TransactionCallback action);
	
//	DocumentDao implementation ////////////////////////////////////////////////////
	InputStream getDocumentTemplate(String name) throws ServiceException;
	Long getNextDocumentId();
	Long insertDocument(Document document);
	Long insertDocumentWithDocid(Document document);
	void deleteDocument(long id);
	void updateDocument(Document document);
	void updateVisibleTo0(Document document);
	void updatePredPrip(long docid,int predid);
	void updateDocumentFlow(long docid,int predid,String certid,String rolename);
	String getDocumentNumber(long id);
	List<Signature> getDocumentSignatures(long id);
	Document getDocumentById(long id);
	void updateDocumentData(Document document);
	void InsertIntoMarsh(long docid, int predid);
	void InsertIntoMarsh(long docid, int predid, int persid);
	Signature getDocumentLastSignature(long id);
	boolean isDocumentFinallySigned(long id);
	Integer getDocumentNextProtocolNumber(int stationId, int templateId) throws ServiceException;
	Integer getDocumentSerialNumberForMonth(int enterpriseId, int templateId) throws ServiceException;
	public <T> List<T> getDocumentDataForMonth(int enterpriseId, int month,  int year, String templateName, ParameterizedRowMapper<T> mapper)  throws ServiceException;
	
	
//	EnterpriseDao implementation //////////////////////////////////////////////////
	String getEnterpriseAdditionalName(int type, int code) throws ServiceException;
	String getEnterpriseName(int type, int code) throws ServiceException;
	Integer getEnterpriseId(int type, int code) throws ServiceException;
	String getEnterpriseFullName(int type, int code) throws ServiceException;
	String getEnterpriseShortName(int type, int code) throws ServiceException ;
	Enterprise getEnterprise(int type, int code) throws ServiceException;
	String getUrlByDocumentId(Integer predid);
	Enterprise getEnterpriseById(int id) throws ServiceException ;
	Enterprise getByOkpoKod(int code) throws ServiceException ;
	Enterprise getByOtrKod(int code) throws ServiceException;
	int getOkpoKodByOtrKod(int code) throws ServiceException; 
	Enterprise getEnterpriseByCode(int code) throws ServiceException;
	Enterprise getEnterpriseByKleimo(int kleimo) throws ServiceException;
	String getEnterpriseNameByCode(int code) throws ServiceException;
	String getEnterpriseNameByCodeAndType(int code, List<Integer> types) throws ServiceException;
	Enterprise getEnterpriseByCodeAndType(int code, List<Integer> types) throws ServiceException;
	Enterprise getShopById(int id) throws ServiceException;
	Enterprise getByIdFromPredCnsi(int code) throws ServiceException;
	
// TemplateDao implementation /////////////////////////////////////////////////////
	Template getTemplateByName(String name);
	
// RoadDao implementation //////////////////////////////////////////////////////////
	Road getRoadById(int id) throws ServiceException;
	String getRoadName(int id) throws ServiceException;
	String getRoadShortName(int id) throws ServiceException;
	
//	PersonaDao implementation //////////////////////////////////////////////////////////
	Persona getPersonaById(int id) throws ServiceException;
	
//	WayPartDao implementation //////////////////////////////////////////////////////////
	String getWayPartNameByCode(int code) throws ServiceException;
	WayPart getWayPartById(int id)  throws ServiceException;
	WayPart getWayPartByCode(int code) throws ServiceException;
	ReportDao getReportDao();
	RoadDao getRoadDao();
	EnterpriseDao getEnterpriseDao();
	TemplateDao getTemplateDao();
	WayPartDao getWayPartDao();
	PersonaDao getPersonaDao();
	DocumentDao getDocumentDao();
	
	NamedParameterJdbcTemplate getNpjt();
//Administration dao ////////////////////////////////////////////////////////////////
	String getAdministrationNameById(int id);
	
//Territorial objects dao //////////////////////////////////////////////////////////
	TerritorialObject getTerritorialObjectById(int id);
	TerritorialObject getTerritorialObjectByCode(int code);
	String getTerritorialObjectNameById(int id);
	String getTerritorialObjectNameByCode(int code);

//River Dao //////////////////////////////////////////////////////////////////////
	River getRiverById(int id);
	River getRiverByCode(int id);
	String getRiverNameById(int id);
	String getRiverNameByCode(int code);
//	Gruz Dao //////////////////////////////////////////////////////////////////////
	Gruz getGruzByCode(int id);
	String getGruzVNameByCode(int id);
	String getGruzSNameByCode(int id);
//	Vagsob Dao //////////////////////////////////////////////////////////////////////
	Vagsob getByLocKod(int code);
	String getSNameByLocKod(int code);
//ActivityKind Dao ///////////////////////////////////////////////////////////////
	ActivityKind getActivityKindById(int id);
	String getActivityKindNameById(int id);
//DicObjects Dao
	DicObjects getDicObjectsByCode(int code) throws ServiceException;
	DicObjects getDicObjectsByCodeAndClassID(int code, int classid) throws ServiceException;
//	PeregMs Dao
	PeregMs getPeregMsById(int id) throws ServiceException;
//	Up Dao
	Up getUpById(int id) throws ServiceException;
	Up getUpByCode(int code) throws ServiceException;
//	ETDForm Dao
	Long getNumTu174(String datetime,String nom_tab) throws ServiceException;
// 
	String getDropTime (Long docid) throws DataAccessException;
//
	Dispatcher getDispatcher();
	void setDispatcher(Dispatcher dispatcher);
	
	MRMDao getMRMDao();
	RoleDao getRoleDao();
	ContragDao getContragDao();
	//my
	CompanyData getCompanyData();
	
}
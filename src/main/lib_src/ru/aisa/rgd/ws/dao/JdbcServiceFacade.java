package ru.aisa.rgd.ws.dao;

import java.io.InputStream;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.ParameterizedRowMapper;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;

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

public class JdbcServiceFacade implements ServiceFacade {
	
	

	public JdbcServiceFacade() {
		super();
	}

	

	protected final Logger	log	= Logger.getLogger(getClass());
	
	private TransactionTemplate transactionTemplate;
	
	private DocumentDao documentDao;
	
	private EnterpriseDao enterpriseDao;
	
	private TemplateDao templateDao;
	
	private RoadDao roadDao;
	
	private PersonaDao personaDao;
	
	private WayPartDao wayPartDao;
	
	private AdministrationDao administrationDao;
	
	private TerritorialObjectDao territorialObjectDao;
	
	private RiverDao riverDao;
	
	private GruzDao gruzDao;
	
	private VagsobDao vagsobDao;
	
	private ActivityKindDao activityKindDao;
	
	private NamedParameterJdbcTemplate npjt;
	
	private DicObjectsDao dicObjectsDao;
	
	private PeregMsDao peregMsDao;
	
	private UpDao upDao;
	
	private Dispatcher dispatcher;
	
	private ReportDao reportDao;
	
	
	

	private RoleDao											roleDao;

	private MRMDao MRMDao;
	
	
	private ContragDao ContragDao;
	
	private CompanyData CompanyData;

	//-------------------------------------------------------------------------
	// Setter methods for dependency injection
	//-------------------------------------------------------------------------
	public MRMDao getMRMDao() {
		return MRMDao;
	}

	public ContragDao getContragDao() {
		return ContragDao;
	}


	public void setContragDao(ContragDao contragDao) {
		ContragDao = contragDao;
	}
	
	
	//my
	public CompanyData getCompanyData() {
		return CompanyData;
	}


	public void setCompanyData(CompanyData companyData) {
		CompanyData = companyData;
	}

	public TransactionTemplate getTransactionTemplate() {
		if (transactionTemplate == null) throw new IllegalStateException("TransactionTemplate hasn't been initialized");
		return transactionTemplate;
	}

	public void setTransactionTemplate(TransactionTemplate transactionTemplate) {
		this.transactionTemplate = transactionTemplate;
	}
	
	public DocumentDao getDocumentDao() {
		if (documentDao == null) throw new IllegalStateException("DocumentDao hasn't been initialized");
		return documentDao;
	}

	public void setDocumentDao(DocumentDao documentDao) {
		this.documentDao = documentDao;
	}

	public ReportDao getReportDao() {
		if (reportDao == null) throw new IllegalStateException("reportDao hasn't been initialized");
		return reportDao;
	}

	public void setReportDao(ReportDao reportDao) {
		this.reportDao = reportDao;
	}
	public EnterpriseDao getEnterpriseDao() {
		if (enterpriseDao == null) throw new IllegalStateException("EnterpriseDao hasn't been initialized");
		return enterpriseDao;
	}

	public void setEnterpriseDao(EnterpriseDao enterpriseDao) {
		this.enterpriseDao = enterpriseDao;
	}
	
	public TemplateDao getTemplateDao() {
		if (templateDao == null) throw new IllegalStateException("TemplateDao hasn't been initialized");
		return templateDao;
	}

	public void setTemplateDao(TemplateDao templateDao) {
		this.templateDao = templateDao;
	}
	
	public RoadDao getRoadDao() {
		if (roadDao == null) throw new IllegalStateException("RoadDao hasn't been initialized");
		return roadDao;
	}
	
	public WayPartDao getWayPartDao() {
		if (wayPartDao == null) throw new IllegalStateException("WayPartDao hasn't been initialized");
		return wayPartDao;
	}

	public void setWayPartDao(WayPartDao wayPartDao) {
		this.wayPartDao = wayPartDao;
	}

	public void setRoadDao(RoadDao roadDao) {
		this.roadDao = roadDao;
	}

	public PersonaDao getPersonaDao() {
		return personaDao;
	}

	public void setPersonaDao(PersonaDao personaDao) {
		this.personaDao = personaDao;
	}
	
	public AdministrationDao getAdministrationDao() {
		if (administrationDao == null) throw new IllegalStateException("AdministrationDao hasn't been initialized");
		return administrationDao;
	}

	public void setAdministrationDao(AdministrationDao administrationDao) {
		this.administrationDao = administrationDao;
	}
	
	public TerritorialObjectDao getTerritorialObjectDao() {
		if (territorialObjectDao == null) throw new IllegalStateException("TerritorialObjectDao hasn't been initialized");
		return territorialObjectDao;
	}

	public void setTerritorialObjectDao(TerritorialObjectDao territorialObjectDao) {
		this.territorialObjectDao = territorialObjectDao;
	}
	
	public RiverDao getRiverDao() {
		if (riverDao == null) throw new IllegalStateException("RiverDao hasn't been initialized");
		return riverDao;
	}

	public void setRiverDao(RiverDao riverDao) {
		this.riverDao = riverDao;
	}
	
	public GruzDao getGruzDao() {
		if (gruzDao == null) throw new IllegalStateException("GruzDao hasn't been initialized");
		return gruzDao;
	}

	public void setGruzDao(GruzDao gruzDao) {
		this.gruzDao = gruzDao;
	}
	
	public VagsobDao getVagsobDao() {
		if (vagsobDao == null) throw new IllegalStateException("VagsobDao hasn't been initialized");
		return vagsobDao;
	}

	public void setVagsobDao(VagsobDao vagsobDao) {
		this.vagsobDao = vagsobDao;
	}
	
	public void setActivityKindDao(ActivityKindDao activityKindDao) {
		this.activityKindDao = activityKindDao;
	}
	
	public ActivityKindDao getActivityKindDao() {
		return activityKindDao;
	}
	
	public DicObjectsDao getDicObjectsDao() {
		if (dicObjectsDao == null) throw new IllegalStateException("DicObjectsDao hasn't been initialized");
		return dicObjectsDao;
	}

	public void setDicObjectsDao(DicObjectsDao dicObjectsDao) {
		this.dicObjectsDao = dicObjectsDao;
	}
	
	public PeregMsDao getPeregMsDao() {
		if (peregMsDao == null) throw new IllegalStateException("PeregMsDao hasn't been initialized");
		return peregMsDao;
	}

	public void setPeregMsDao(PeregMsDao peregMsDao) {
		this.peregMsDao = peregMsDao;
	}
	
	public UpDao getUpDao() {
		if (upDao == null) throw new IllegalStateException("UpDao hasn't been initialized");
		return upDao;
	}

	public void setUpDao(UpDao upDao) {
		this.upDao = upDao;
	}
	
	//-------------------------------------------------------------------------
	// Operation methods, implementing the Facade interface
	//-------------------------------------------------------------------------


	public void deleteDocument(long id) {
		getDocumentDao().delete(id);
	}

	public Long getNumTu174(String datetime,String nom_tab) {
		return getDocumentDao().getNumTu174(datetime,nom_tab);
	}
	public String getDropTime (Long docid) {
		return getDocumentDao().getDropTime(docid);
	}

	public InputStream getDocumentTemplate(String name)	throws ServiceException{
		return getDocumentDao().getTemplate(name);
	}

	public Long getNextDocumentId() {
		return getDocumentDao().getNextId();
	}
	
	public Long insertDocument(Document document) {
		Long id = getDocumentDao().getNextId();
		document.setId(id);
		getDocumentDao().save(document);
		log.debug("Document id = " + id);
		return id;
	}
	
	public Long insertDocumentWithDocid(Document document) {
//		Long id = getDocumentDao().getNextId();
//		document.setId(id);
		getDocumentDao().save(document);
		log.debug("Document id = " + document.getId());
		return document.getId();
	}
	
	
	public void updateDocument(Document document) {
		getDocumentDao().update(document);
	}
	public void updatePredPrip(long docid,int predid) {
		getDocumentDao().updatePredPrip(docid, predid);
	}
	public void updateDocumentFlow(long docid,int predid,String certid,String rolename){
		getDocumentDao().moveToArchive(docid,predid,certid,rolename);
	}
	
	public String getDocumentNumber(long id) {
		return getDocumentDao().getNumber(id);
	}
	
	public Document getDocumentById(long id) 
	{
		return getDocumentDao().getById(id);
	}

	public void updateDocumentData(Document document) 
	{
		getDocumentDao().updateData(document);		
	}

	public void InsertIntoMarsh(long docid, int predid) 
	{
		getDocumentDao().InsertIntoMarsh(docid, predid, -1);		
	}
	
	public void InsertIntoMarsh(long docid, int predid, int persid) 
	{
		getDocumentDao().InsertIntoMarsh(docid, predid, persid);		
	}
	
	public List<Signature> getDocumentSignatures(long id) {
		return getDocumentDao().getSignatures(id);
	}
	
	public Signature getDocumentLastSignature(long id) {
		return getDocumentDao().getLastSignature(id);
	}

	public Integer getDocumentNextProtocolNumber(int stationId, int templateId) throws ServiceException {
		return getDocumentDao().getNextProtocolNumber(stationId, templateId);
	}
	
	public Integer getDocumentSerialNumberForMonth(int enterpriseId, int templateId) throws ServiceException 
	{
		return getDocumentDao().getSerialNumberForMonth(enterpriseId, templateId);
	}

	public boolean isDocumentFinallySigned(long id) {
		return getDocumentDao().isFinallySigned(id);
	}
	
	
	public Integer getEnterpriseId(int type, int code) throws ServiceException {
		return getEnterpriseDao().getId(type, code);
	}

	public String getEnterpriseName(int type, int code) throws ServiceException{
		return getEnterpriseDao().getName(type, code);
	}
	public int getOkpoKodByOtrKod(int code) throws ServiceException{
		return getEnterpriseDao().getOkpoKodByOtrKod(code);
	}

	public String getEnterpriseFullName(int type, int code) throws ServiceException {
		return getEnterpriseDao().getFullName(type, code);
	}
	
	public String getEnterpriseAdditionalName(int type, int code) throws ServiceException {
		return getEnterpriseDao().getAdditionalName(type, code);
	}
	
	public String getEnterpriseShortName(int type, int code) throws ServiceException {
		return getEnterpriseDao().getShortName(type, code);
	}

	public Enterprise getEnterprise(int type, int code) throws ServiceException {
		return getEnterpriseDao().get(type, code);
	}

	public String getUrlByDocumentId(Integer predid) {
		return getEnterpriseDao().getUrlByDocumentId(predid);
	}
	
	public String getEnterpriseNameByCodeAndType(int code, List<Integer> types) throws ServiceException{
		return getEnterpriseDao().getNameByCodeAndType(code, types);
	}
	
	public Enterprise getEnterpriseByCodeAndType(int code, List<Integer> types) throws ServiceException{
		return getEnterpriseDao().getByCodeAndType(code, types);
	}

	public Enterprise getShopById(int id) throws ServiceException {
		return getEnterpriseDao().getShopById(id);
	}
	
	public Enterprise getByOtrKod(int code) throws ServiceException {
		return getEnterpriseDao().getByOtrKod(code);
	}
	
	public Enterprise getByOkpoKod(int code) throws ServiceException {
		return getEnterpriseDao().getByOkpoKod(code);
	}

	public Template getTemplateByName(String name) {
		return getTemplateDao().getByName(name);
	}

	public Road getRoadById(int id) throws ServiceException {
		return getRoadDao().getById(id);
	}

	public String getRoadName(int id) throws ServiceException {
		return getRoadDao().getName(id);
	}

	public String getRoadShortName(int id) throws ServiceException {
		return getRoadDao().getShortName(id);
	}

	
	public Persona getPersonaById(int id) throws ServiceException 
	{
		return getPersonaDao().getById(id);
	}
	
	public WayPart getWayPartByCode(int code) throws ServiceException {
		return getWayPartDao().getByCode(code);
	}

	public WayPart getWayPartById(int id) throws ServiceException {
		return getWayPartDao().getById(id);
	}

	public String getWayPartNameByCode(int code) throws ServiceException {
		return getWayPartDao().getNameByCode(code);
	}

	public Object executeInTransaction(TransactionCallback action) {
		return transactionTemplate.execute(action);
	}

	public Enterprise getEnterpriseById(int id) throws ServiceException {
		return getEnterpriseDao().getById(id);
	}

	public <T> List<T> getDocumentDataForMonth(int enterpriseId, int month, int year, String templateName, ParameterizedRowMapper<T> mapper) throws ServiceException 
	{
		return getDocumentDao().getDataForMonth(enterpriseId, month, year, templateName, mapper);
	}

	public Enterprise getEnterpriseByCode(int code) throws ServiceException {
		return getEnterpriseDao().getByCode(code);
	}

	public Enterprise getEnterpriseByKleimo(int kleimo) throws ServiceException {
		// TODO Auto-generated method stub
		return getEnterpriseDao().getEnterpriseByKleimo(kleimo);
	}
	
	
	public String getEnterpriseNameByCode(int code) throws ServiceException {
		return getEnterpriseDao().getNameByCode(code);
	}
	
	public Enterprise getByIdFromPredCnsi(int code) throws ServiceException {
		return getEnterpriseDao().getByIdFromPredCnsi(code);
	}

	public NamedParameterJdbcTemplate getNpjt() {
		if (npjt == null) throw new IllegalStateException("NPJT hasn't been initialized");
		return npjt;
	}

	public void setNpjt(NamedParameterJdbcTemplate npjt) {
		this.npjt = npjt;
	}

	//	Administration	/////////////////////////////////////////////////////////////////////////
	
	public String getAdministrationNameById(int id){
		return getAdministrationDao().getNameById(id);
	}

	//Territorial objects ////////////////////////////////////////////////////////
	public TerritorialObject getTerritorialObjectByCode(int code) {
		return getTerritorialObjectDao().getByCode(code);
	}

	public TerritorialObject getTerritorialObjectById(int id) {
		return getTerritorialObjectDao().getById(id);
	}

	public String getTerritorialObjectNameByCode(int code) {
		return getTerritorialObjectDao().getNameByCode(code);
	}

	public String getTerritorialObjectNameById(int id) {
		return getTerritorialObjectDao().getNameById(id);
	}

//	River objects ////////////////////////////////////////////////////////
	public River getRiverByCode(int id) {
		return getRiverDao().getByCode(id);
	}

	public River getRiverById(int id) {
		return getRiverDao().getById(id);
	}

	public String getRiverNameByCode(int code) {
		return getRiverDao().getNameByCode(code);
	}

	public String getRiverNameById(int id) {
		return getRiverDao().getNameById(id);
	}

	public ActivityKind getActivityKindById(int id) {
		return getActivityKindDao().getById(id);
	}

	public String getActivityKindNameById(int id) {
		return getActivityKindDao().getNameById(id);
	}

//	Gruz objects ////////////////////////////////////////////////////////
	public Gruz getGruzByCode(int id) {
		return getGruzDao().getByCode(id);
	}
	
	public String getGruzVNameByCode(int id) {
		return getGruzDao().getVNameByCode(id);
	}
	
	public String getGruzSNameByCode(int id) {
		return getGruzDao().getSNameByCode(id);
	}
//	Vagsob objects ////////////////////////////////////////////////////////
	public Vagsob getByLocKod(int code) {
		return getVagsobDao().getByLocKod(code);
	}
	
	public String getSNameByLocKod(int code) {
		return getVagsobDao().getSNameByLocKod(code);
	}
	
//	DicObjects	
	public DicObjects getDicObjectsByCode(int code) throws ServiceException {
		return getDicObjectsDao().getByCode(code);
	}
	
	public DicObjects getDicObjectsByCodeAndClassID(int code, int classid) throws ServiceException {
		return getDicObjectsDao().getByCodeAndClassID(code, classid);
	}
//	PeregMs	
	public PeregMs getPeregMsById(int id) throws ServiceException {
		return getPeregMsDao().getById(id);
	}	
//	Up	
	public Up getUpById(int id) throws ServiceException {
		return getUpDao().getById(id);
	}
	public Up getUpByCode(int code) throws ServiceException {
		return getUpDao().getByCode(code);
	}
	public Dispatcher getDispatcher()
	{
		return dispatcher;
	}
	public void setDispatcher(Dispatcher dispatcher)
	{
		this.dispatcher = dispatcher; 
	}

	public RoleDao getRoleDao() {
		return roleDao;
	}

	public void setRoleDao(RoleDao roleDao) {
		this.roleDao = roleDao;
	}

	public void setMRMDao(MRMDao mRMDao) {
		MRMDao = mRMDao;
	}

	@Override
	public void updateVisibleTo0(Document document) {
		getDocumentDao().updateVisibleTo0(document);
		
	}

	

	
}

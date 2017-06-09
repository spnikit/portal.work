package ru.aisa.rgd.etd.dao;

import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import ru.aisa.rgd.etd.objects.ETDCounterPers;
import ru.aisa.rgd.etd.objects.ETDCounterparts;
import ru.aisa.rgd.etd.objects.ETDDocument;
import ru.aisa.rgd.etd.objects.ETDDocumentAccess;
import ru.aisa.rgd.etd.objects.ETDDocumentData;
import ru.aisa.rgd.etd.objects.ETDDocumentFilter;
import ru.aisa.rgd.etd.objects.ETDTemplate;
import ru.aisa.rgd.etd.objects.ETDUserInformation;
import ru.aisa.rgd.etd.objects.ETDFlowTypes;
import ru.aisa.rgd.ws.dao.EnterpriseDao;

public class ETDFacade {
	private ETDDocumentDao documentDao;
	private ETDUserDao userDao;
	private ETDFormDao formDao;
	private EnterpriseDao enterpriseDao;
	private ETDCounterpartsDAO counterpartsDao;
	private ETDCounterPersDao counterPersDao;
	private ETDSyncServiceDAO etdsyncdao;
	private ETDSFNoticeDao etdsfnoticedao;
	private ETDDocStatusDao etdDocStatusdao;
	
	public ETDDocStatusDao getEtdDocStatusdao() {
		return etdDocStatusdao;
	}

	public void setEtdDocStatusdao(ETDDocStatusDao etdDocStatusdao) {
		this.etdDocStatusdao = etdDocStatusdao;
	}

	public ETDSFNoticeDao getEtdsfnoticedao() {
		return etdsfnoticedao;
	}

	public void setEtdsfnoticedao(ETDSFNoticeDao etdsfnoticedao) {
		this.etdsfnoticedao = etdsfnoticedao;
	}

	public ETDFlowTypesDao getFlowTypesDao() {
		return flowTypesDao;
	}

	public void setFlowTypesDao(ETDFlowTypesDao flowTypesDao) {
		this.flowTypesDao = flowTypesDao;
	}

	//private ETDPersonalDao personalDao;
	private ETDDeleteDao deleteDao;
	private ETDFlowTypesDao flowTypesDao;
	
	
	private NamedParameterJdbcTemplate npjt;
		    
	    
	    public ETDSyncServiceDAO getETDSyncServiceDAO() {
		if (etdsyncdao == null) throw new IllegalStateException("EtdsyncserviceDaoDao hasn't been initialized");
		return etdsyncdao;
	}

	public void setETDSyncServiceDAO(ETDSyncServiceDAO etdsyncdao) {
		this.etdsyncdao = etdsyncdao;
	}
	    
	
	
	public ETDCounterPersDao getCounterPersDao() {
		if (counterPersDao == null) throw new IllegalStateException("EnterpriseDao hasn't been initialized");
		return counterPersDao;
	}

	public void setCounterPersDao(ETDCounterPersDao counterPersDao) {
		this.counterPersDao = counterPersDao;
	}
	
	public ETDCounterpartsDAO getCounterpartsDao() {
		if (counterpartsDao == null) throw new IllegalStateException("EnterpriseDao hasn't been initialized");
		return counterpartsDao;
	}

	public void setCounterpartsDao(ETDCounterpartsDAO counterpartsDao) {
		this.counterpartsDao = counterpartsDao;
	}
	
	public EnterpriseDao getEnterpriseDao() {
		if (enterpriseDao == null) throw new IllegalStateException("EnterpriseDao hasn't been initialized");
		return enterpriseDao;
	}

	public void setEnterpriseDao(EnterpriseDao enterpriseDao) {
		this.enterpriseDao = enterpriseDao;
	}
	
	public ETDDeleteDao getDeleteDao() {
		if (deleteDao == null) throw new IllegalStateException("DeleteDao hasn't been initialized");
		return deleteDao;
	}
	public void setDeleteDao(ETDDeleteDao deleteDao) {
		this.deleteDao = deleteDao;
	}
	public ETDFormDao getFormDao() {
		if (formDao == null) throw new IllegalStateException("FormDao hasn't been initialized");
		return formDao;
	}
	public void setFormDao(ETDFormDao formDao) {
		this.formDao = formDao;
	}
	public ETDDocumentDao getDocumentDao() {
		if (documentDao == null) throw new IllegalStateException("DocumentDao hasn't been initialized");
		return documentDao;
	}
	public void setDocumentDao(ETDDocumentDao documentDao) {
		this.documentDao = documentDao;
	}
	public ETDUserDao getUserDao() {
		if (userDao == null) throw new IllegalStateException("UserDao hasn't been initialized");
		return userDao;
	}
	public void setUserDao(ETDUserDao userDao) {
		this.userDao = userDao;
	}
	
	/*
	public ETDPersonalDao getPersonalDao() {
		if (personalDao == null) throw new IllegalStateException("personalDao hasn't been initialized");
		return personalDao;
	}
	public void setPersonalDao(ETDPersonalDao personalDao) {
		this.personalDao = personalDao;
	}*/
	/**
	 * UserDao
	 */
	
	public ETDUserInformation getUserInformation(String certificateId){
		return getUserDao().getUserInformation(certificateId);
	}
	public int getUserIdByCertificateSerial(String certificateSerial){
		return getUserDao().getIdByCertificateSerial(certificateSerial);
	}
	
	/**
	 * DocumentDao
	 */
	public List<Long> getActiveDocumentIDs(ETDDocumentFilter filter){
		//System.out.println("filter "+filter);
		//System.out.println("documentDao.getActiveDocumentIds(filter) "+documentDao.getActiveDocumentIds(filter));
		return documentDao.getActiveDocumentIds(filter);
	}
	
	
	public List<Long> getFinDocumentIDs(ETDDocumentFilter filter){
		//System.out.println("filter "+filter);
		//System.out.println("documentDao.getActiveDocumentIds(filter) "+documentDao.getActiveDocumentIds(filter));
		return documentDao.getDeclinedDocumentIds(filter);
	}
	
	
	
	public List<Long> getArchiveDocumentIDs(ETDDocumentFilter filter){
		//System.out.println("filter "+filter);
		//System.out.println("documentDao.getActiveDocumentIds(filter) "+documentDao.getActiveDocumentIds(filter));
		
		return documentDao.getArchiveDocumentIds(filter);
	}
	
	public List<Long> getDroppedDocumentIDs(ETDDocumentFilter filter){
		//System.out.println("filter "+filter);
		//System.out.println("documentDao.getActiveDocumentIds(filter) "+documentDao.getActiveDocumentIds(filter));
		
		return documentDao.getDroppedDocumentIds(filter);
	}
	
	
	public List<ETDDocument> getActiveDocuments(ETDDocumentFilter filter, List<Long> ids){
		//System.out.println("filter ids "+filter+" "+ids);
		//System.out.println("documentDao.getActiveDocuments(filter, ids) "+documentDao.getActiveDocuments(filter, ids));
		return documentDao.getActiveDocuments(filter, ids);
	}
	
	public List<ETDDocument> getRoughDocuments(ETDDocumentFilter filter, List<Long> ids){
		//System.out.println("filter ids "+filter+" "+ids);
		//System.out.println("documentDao.getActiveDocuments(filter, ids) "+documentDao.getActiveDocuments(filter, ids));
		return documentDao.getRoughDocuments(filter, ids);
	}
	
	public List<ETDDocument> getFinDocuments(ETDDocumentFilter filter, List<Long> ids){
		//System.out.println("filter ids "+filter+" "+ids);
		//System.out.println("documentDao.getActiveDocuments(filter, ids) "+documentDao.getActiveDocuments(filter, ids));
		return documentDao.getFinDocuments(filter, ids);
	}
	
	public List<ETDDocument> getMessageDocuments(ETDDocumentFilter filter, List<Long> ids){
		//System.out.println("filter ids "+filter+" "+ids);
		//System.out.println("documentDao.getActiveDocuments(filter, ids) "+documentDao.getActiveDocuments(filter, ids));
		return documentDao.getMessageDocuments(filter, ids);
	}
	
	public List<ETDDocument> getArchiveDocuments(ETDDocumentFilter filter, List<Long> ids){
		//System.out.println("filter ids "+filter+" "+ids);
		//System.out.println("documentDao.getActiveDocuments(filter, ids) "+documentDao.getActiveDocuments(filter, ids));
		
		return documentDao.getArchiveDocuments(filter, ids);
	}
	
	public List<ETDDocument> getDroppedDocuments(ETDDocumentFilter filter, List<Long> ids){
		//System.out.println("filter ids "+filter+" "+ids);
		//System.out.println("documentDao.getActiveDocuments(filter, ids) "+documentDao.getActiveDocuments(filter, ids));
		
		return documentDao.getDroppedDocuments(filter, ids);
	}
	
	public List<ETDDocument> getInworkDocuments(ETDDocumentFilter filter, List<Long> ids){
		return documentDao.getInworkDocuments(filter, ids);
	}
	
	/**
	 * FormDao
	 */
	public List<ETDTemplate> getViewEditTypesForRole(Integer workId){
		return getFormDao().getViewEditTypesForRole(workId);
	}
	public List<ETDTemplate> getCreateTypesForRole(Integer workId){
		return getFormDao().getCreateTypesForRole(workId);
	}
	public List<ETDTemplate> getCreateMSGForRole(Integer workId){
		return getFormDao().getCreateMSGForRole(workId);
	}
	public List<ETDCounterparts> getCounterpartsForRole(Integer predId){
		//System.out.println("ciunterpartsforrole");
		return getCounterpartsDao().getCounterpartsForRole(predId);
		
	}
	public List<ETDCounterPers> getCounterPers(/*Integer Id, Integer predSnd, */Integer predId){
		//System.out.println("counterPers");
		return getCounterPersDao().getCounterPers(/*Id, predSnd, */predId);
		
	}
	
	public List<ETDFlowTypes> getFlowTypes(){
		//System.out.println("counterPers");
		return getFlowTypesDao().getFlowTypes();
		
	}
	
	public List<ETDTemplate> getReportTypesForRole(Integer workId, List<String> reportNames){
		//System.out.println("getReportTypesForRole");
		//System.out.println("reportNames "+reportNames);
		return getFormDao().getReportTypesForRole(workId, reportNames);
		
	}
	public ETDTemplate getTemplateByName(String name){
		return getFormDao().getTemplateByName(name);
	}
	public ETDTemplate getTemplateById(Integer id){
		return getFormDao().getTemplateById(id);
	}
	public ETDTemplate getTemplateByDocumentId(Long id){
		return getFormDao().getTemplateByDocumentId(id);
	}
	
	public Integer getPakCountByDocumentId(Long id){
		return getFormDao().getPakCountByDocumentId(id);
	}
	public Integer getSfSignByDocid(Long id){
		return getFormDao().getSfSignByDocumnetId(id);
	}
	
	public ETDDocumentData getDocumentDataById(Long id){
		return getFormDao().getDocumentDataById(id);
	}
	public Map lockDocument(long documentId, int userId){
		return getFormDao().lockDocument(documentId, userId);
	}
	public ETDDocumentAccess getDocumentAccessByRoleAndId(int workId, long documentId){
		return getFormDao().getAccessForRoleAndId(workId, documentId);
	}
	public Map<Integer, String> getDors(){
		return getFormDao().getDors();
	}
	public Map<Integer, String> getPredsByDorid(int dorid){
		return getFormDao().getPredsByDorid(dorid);
	}
	
	/**
	 * PersonalDao
	 */
	
	/**
	 * DeleteDao
	 */	
	public void deleteByLastsign(String period, String docname){
		getDeleteDao().deleteDocsByLastsign(period, docname);
	}
	
	public void deleteNotSignedDocsByCreateDate(String period, String docname){
		getDeleteDao().deleteNotSignedDocsByCreateDate(period, docname);
	}
	
	public void setNpjt(NamedParameterJdbcTemplate npjt){
		this.npjt=npjt;
	}

	public NamedParameterJdbcTemplate getNpjt(){
		return npjt;
	}
	/**
	 * SyncDao
	 */
	
}

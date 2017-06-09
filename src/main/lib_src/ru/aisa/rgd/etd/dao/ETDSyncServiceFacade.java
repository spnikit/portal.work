package ru.aisa.rgd.etd.dao;

import sheff.rjd.services.syncutils.SyncObj;
import sheff.rjd.services.syncutils.VchdeObj;
import sheff.rjd.services.transoil.TransDocForSendObj;

public class ETDSyncServiceFacade {
	// private NamedParameterJdbcTemplate npjt;
	private ETDSyncServiceDAO etdsyncDao;

	public ETDSyncServiceDAO getEtdsyncDao() {
		if (etdsyncDao == null)
			throw new IllegalStateException(
					"EtdsyncserviceDaoDao hasn't been initialized");

		return etdsyncDao;
	}

	public void setEtdsyncDao(ETDSyncServiceDAO etdsyncDao) {
		this.etdsyncDao = etdsyncDao;
	}

	public int getTypeid(SyncObj obj) {

		return getEtdsyncDao().getTypeid(obj);

	}

	public VchdeObj getvchde(int kleim) {
		return getEtdsyncDao().getVchde(kleim);
	}

	public SyncObj getWorkerWithorderNull(SyncObj obj) {
		return getEtdsyncDao().getWorkerWithorderNull(obj);
	}

	public SyncObj getWorkerWithorder(SyncObj obj) {
		return getEtdsyncDao().getWorkerWithorder(obj);
	}

	public String getCabinetIdByPred(int predid) {
		return getEtdsyncDao().getCabinetIdByPred(predid);
	}

	public int getpredIdByINNKPP(String inn, String kpp) {
		return getEtdsyncDao().getpredIdByINNKPP(inn, kpp);
	}

	public int getpredIdByINNKPPMARK(String inn, String kpp, int mark) {
		return getEtdsyncDao().getpredIdByINNKPPMARK(inn, kpp, mark);
	}

	public int getCountpredIdByINNKPPMARK(String inn, String kpp, int mark) {
		return getEtdsyncDao().getCountpredIdByINNKPPMARK(inn, kpp, mark);
	}

	public int getpredIdByIOKPO(int okpo) {
		return getEtdsyncDao().getpredIdByOKPO(okpo);
	}

	public int getpredIdByIOKPOMARK(int okpo, int mark) {
		return getEtdsyncDao().getpredIdByOKPOMARK(okpo, mark);
	}

	public int getCountpredIdByIOKPOMARK(int okpo, int mark) {
		return getEtdsyncDao().getCountpredIdByOKPOMARK(okpo, mark);
	}

	public String getNamebyPredid(int predid) {
		return getEtdsyncDao().getNamebyPredid(predid);
	}

	public int getNextDocid() {

		return getEtdsyncDao().getNextDocid();

	}

	public void updateDSF(SyncObj obj) {
		getEtdsyncDao().updateDSF(obj);
	}
	
	public void insertDocstore(String sqlinsert, SyncObj obj) {
		getEtdsyncDao().insertDocstore(sqlinsert, obj);
	}

	public long getCountFuact(SyncObj obj) {
		return getEtdsyncDao().getCountFpuact(obj);
	}

	public void updateFpuact(SyncObj obj) {
		getEtdsyncDao().UpdateFpuact(obj);
	}

	public String getFlowType(int typeid) {
		return getEtdsyncDao().getTypeName(typeid);
	}

	public long getIdbyEtdid(long etdid) {
		return getEtdsyncDao().getIdByEtdid(etdid);
	}

	public int getMarkCount(int mark) {
		return getEtdsyncDao().getMarkCount(mark);
	}

	public void setLastPak(String vagnum, long etdid, String repdate) {
		getEtdsyncDao().setLastPack(vagnum, etdid, repdate);
	}

	public SyncObj getFpuCredentials(long docid, SyncObj syncobj) {
		return getEtdsyncDao().getFpuCredentials(docid, syncobj);
	}

	public int getPredMaker(String name) {
		return getEtdsyncDao().getPredMaker(name);
	}

	public TransDocForSendObj getTransDocument(String etdid){
		return getEtdsyncDao().getTransDocument(etdid);
		
	}
	
	public boolean getCountPortalDoc(String etdid){
		return getEtdsyncDao().getCountPortalDoc(etdid);
	}
	
	
}

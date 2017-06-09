package sheff.rgd.ws.Controllers.MRM;

import java.util.HashMap;

import javax.sql.DataSource;

import org.apache.log4j.Logger;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import ru.aisa.rgd.etd.dao.ETDSyncServiceFacade;
import ru.aisa.rgd.etd.objects.ETDDocumentData;
import ru.aisa.rgd.ws.documents.ETDForm;
import ru.aisa.rgd.ws.utility.DataBinder;
import ru.aisa.rgd.ws.utility.TypeConverter;
import sheff.rgd.ws.Abstract.AbstractAction;
import sheff.rjd.services.syncutils.SyncObj;
import sheff.rjd.ws.OCO.AfterEdit.FakeSignature;
import sheff.rjd.ws.OCO.AfterSign.SendToEtd;

public class RNL2Controller extends AbstractAction {

	private String parentform;
	private NamedParameterJdbcTemplate npjt;
	private SendToEtd sendtoetd;
	private DataSource ds;
	private ETDSyncServiceFacade etdsyncfacade;
	private FakeSignature fakesignature;
	protected final Logger log = Logger.getLogger(getClass());

	public NamedParameterJdbcTemplate getNpjt() {
		return npjt;
	}

	public SendToEtd getSendtoetd() {
		return sendtoetd;
	}

	public void setSendtoetd(SendToEtd sendtoetd) {
		this.sendtoetd = sendtoetd;
	}

	public void setNpjt(NamedParameterJdbcTemplate npjt) {
		this.npjt = npjt;
	}

	public void setParentform(String parentname) {
		this.parentform = parentname;
	}

	public String getParentform() {
		return parentform;
	}

	public DataSource getDs() {
		return ds;
	}

	public void setDs(DataSource ds) {
		this.ds = ds;
	}

	public ETDSyncServiceFacade getEtdsyncfacade() {
		return etdsyncfacade;
	}

	public void setEtdsyncfacade(ETDSyncServiceFacade etdsyncfacade) {
		this.etdsyncfacade = etdsyncfacade;
	}

	public FakeSignature getFakesignature() {
		return fakesignature;
	}

	public void setFakesignature(FakeSignature fakesignature) {
		this.fakesignature = fakesignature;
	}

	public void doAfterSign(String docName, String docdata, int predid,
			int signNumber, long id, String certserial, int WrkId) {

		try {
			ETDForm form = ETDForm.createFromArchive(docdata.getBytes("UTF-8"));
			DataBinder binder = form.getBinder();

			if (signNumber == 1) {
				boolean first = false;
				boolean second = false;

				if (binder.getValue("P_7a").length() == 0
						&& binder.getValue("P_7b").length() == 0
						&& binder.getValue("P_7v").length() == 0) {

				} else {
					first = true;
				}

				if (binder.getValue("P_8a").length() == 0
						&& binder.getValue("P_8b").length() == 0
						&& binder.getValue("P_8v").length() == 0) {

				} else {
					second = true;
				}

				if (first && second) {

					int predcreator = -1;

					if (binder.getValue("P_8a").length() > 0) {
						predcreator = etdsyncfacade.getpredIdByIOKPO(Integer
								.parseInt(binder.getValue("P_8a")));
					}

					else if (binder.getValue("P_8b").length() > 0
							&& binder.getValue("P_8v").length() > 0) {
						predcreator = etdsyncfacade.getpredIdByINNKPP(
								binder.getValue("P_8b"),
								binder.getValue("P_8v"));

					}

					HashMap<String, Object> pp = new HashMap<String, Object>();
					pp.put("id", id);
					pp.put("predcreator", predcreator);
					npjt.update(
							"update snt.docstore set pred_creator = :predcreator where id=:id",
							pp);

				}

				else if (first && !second) {
					HashMap<String, Object> pp = new HashMap<String, Object>();
					pp.put("id", id);
					npjt.update(
							"update snt.docstore set signlvl = null where id=:id",
							pp);
				}
			}

		} catch (Exception e) {
			log.error(TypeConverter.exceptionToString(e));
			e.printStackTrace();
		}

		try {
			sendtoetd.SendToEtdMessage(id, docdata, parentform, signNumber, 0,
					false);
		} catch (Exception e) {
			log.error(TypeConverter.exceptionToString(e));
			e.printStackTrace();
		}

	}

	public void doAfterSave(String docName, String docdata, int predid,
			int signNumber, String CertID, long id, int WrkId) throws Exception {

	}

	public void doAfterSync(String formname, SyncObj syncobj, String sql,
			int signum) {
		int countpv = -1;

		try {
			ETDForm form = ETDForm.createFromArchive(syncobj.getBldoc());
			DataBinder binder = form.getBinder();

			syncobj.setId_pak(binder.getValue("IdRnl"));
			countpv = Integer.parseInt(binder.getValue("priznak"));

			if (binder.getValue("P_8a").length() == 0
					&& binder.getValue("P_8b").length() == 0
					&& binder.getValue("P_8v").length() == 0) {

			}

			if (binder.getValue("P_7a").length() == 0
					&& binder.getValue("P_7b").length() == 0
					&& binder.getValue("P_7v").length() == 0) {

				int predid = syncobj.getPredcreator();
				int predcreator = syncobj.getPredid();

				syncobj.setPredid(predid);
				syncobj.setPredcreator(predcreator);
			}

			etdsyncfacade.insertDocstore(sql,
					etdsyncfacade.getWorkerWithorderNull(syncobj));

		} catch (Exception e) {
			log.error(TypeConverter.exceptionToString(e));
		}

		if (countpv == 2) {
			HashMap<String, Object> pp = new HashMap<String, Object>();
			pp.put("docid", syncobj.getDocid());
			npjt.update("update snt.docstore set visible = 0 where id =:docid",
					pp);
		}
	}

	public ETDDocumentData EditBeforeOpen(String docName, ETDDocumentData doc) {
		// TODO Auto-generated method stub
		return doc;
	}

}
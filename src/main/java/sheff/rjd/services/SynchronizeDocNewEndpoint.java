package sheff.rjd.services;

import java.util.HashMap;

import org.apache.log4j.Logger;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.oxm.Marshaller;

import ru.aisa.rgd.etd.dao.ETDSyncServiceFacade;
import ru.aisa.rgd.ws.dao.ServiceFacade;
import ru.aisa.rgd.ws.documents.ETDForm;
import ru.aisa.rgd.ws.documents.SynchronizeDocResponseWrapper;
import ru.aisa.rgd.ws.endpoint.AbstractSyncEndpoint;
import ru.aisa.rgd.ws.endpoint.SyncResponseAdapter;
import ru.aisa.rgd.ws.exeption.SyncServiceError;
import ru.aisa.rgd.ws.exeption.SyncServiceException;
import ru.aisa.rgd.ws.utility.DataBinder;
import ru.aisa.rgd.ws.utility.TypeConverter;
import rzd8888.gvc.etd.was.etd.synchronizeXfdlDoc.Action;
import rzd8888.gvc.etd.was.etd.synchronizeXfdlDoc.EncodingBlob;
import rzd8888.gvc.etd.was.etd.synchronizeXfdlDoc.NameSystem;
import rzd8888.gvc.etd.was.etd.synchronizeXfdlDoc.ReservedNamesOfParams;
import rzd8888.gvc.etd.was.etd.synchronizeXfdlDoc.SynchronizeXfdlDocRequest;
import rzd8888.gvc.etd.was.etd.synchronizeXfdlDoc.SynchronizeXfdlDocRequestDocument;
import rzd8888.gvc.etd.was.etd.synchronizeXfdlDoc.SynchronizeXfdlDocResponse;
import rzd8888.gvc.etd.was.etd.synchronizeXfdlDoc.SynchronizeXfdlDocResponseDocument;
import sheff.rjd.services.syncutils.GetContent;
import sheff.rjd.services.syncutils.GetSyncParams;
import sheff.rjd.services.syncutils.SendToRTKService;
import sheff.rjd.services.syncutils.SignatureProcessor;
import sheff.rjd.services.syncutils.SyncObj;
import sheff.rjd.services.syncutils.SyncParamObject;
import sheff.rjd.ws.OCO.TORLists;

public class SynchronizeDocNewEndpoint extends
		AbstractSyncEndpoint<SynchronizeDocResponseWrapper> {

	protected SynchronizeDocNewEndpoint(Marshaller marshaller) {
		super(marshaller);
		// TODO Auto-generated constructor stub
	}

	// private static Log log = LogFactory
	// .getLog(SynchronizeDocNewEndpoint.class);

	private static Logger log = Logger
			.getLogger(SynchronizeDocNewEndpoint.class);

	private NamedParameterJdbcTemplate npjt;
	private ETDSyncServiceFacade etdsyncfacade;
	private SignatureProcessor signdealer;
	private SendToRTKService signservice;

	public NamedParameterJdbcTemplate getNpjt() {
		return npjt;
	}

	public void setNpjt(NamedParameterJdbcTemplate npjt) {
		this.npjt = npjt;
	}

	public ETDSyncServiceFacade getEtdsyncfacade() {
		return etdsyncfacade;
	}

	public void setEtdsyncfacade(ETDSyncServiceFacade etdsyncfacade) {
		this.etdsyncfacade = etdsyncfacade;
	}

	public SignatureProcessor getSigndealer() {
		return signdealer;
	}

	public void setSigndealer(SignatureProcessor signdealer) {
		this.signdealer = signdealer;
	}

	public SendToRTKService getSignservice() {
		return signservice;
	}

	public void setSignservice(SendToRTKService signservice) {
		this.signservice = signservice;
	}

	@Override
	protected SyncResponseAdapter<SynchronizeDocResponseWrapper> processRequest(
			Object arg, ServiceFacade facade) throws Exception {

		SynchronizeXfdlDocRequestDocument doc = (SynchronizeXfdlDocRequestDocument) arg;
		SynchronizeXfdlDocRequest sync = doc.getSynchronizeXfdlDocRequest();

		String sql = " insert into SNT.DocStore (ID,PREDID, TYPEID,CRDATE,CRTIME,BLDOC,NWRKID,LWRKID,LDATE,LTIME,LPERSID,INUSEID,DOCDATA, SIGNLVL,MADEID,  datedoc, PRED_CREATOR, PERSID, ETDID, id_pak, vagnum, OPISANIE, NO,REPDATE, sf_sign,  VISIBLE, ONBASEID) "
				+ " values ( :globalid, :PREDID, :TYPEID  ,current_date,current_time, :BLDOC  , null , :LWRKID,NULL,NULL ,NULL,NULL, :DOCDATA, "
				+ " :SIGNLVL, NULL"
				+ ",  current_date, :PREDCREATOR, -1, :ETDID, :id_pak, :vagnum, :content, :dognum, :repdate #sf,   :visible, :mark)";

		log.warn("starting endpont for etdid " + sync.getEtdDocId());

		SyncObj syncobj = new SyncObj();
		SyncParamObject spo = new SyncParamObject();

		boolean update = false;
		long countfpuact = -1;
		spo.setIsinn(ReservedNamesOfParams.PORTAL_INN.toString());
		spo.setIsokpo(ReservedNamesOfParams.PORTAL_OKPO.toString());
		spo.setIskpp(ReservedNamesOfParams.PORTAL_KPP.toString());
		spo.setIsdecline(ReservedNamesOfParams.DECLINE_REASON.toString());

		// if (sync.isSetSignatureTable()){
		//
		// }

		spo.setEtddocid(sync.getEtdDocId());
		String formname = sync.getFormName();

		String formblob = sync.getBlobDoc().getStringValue();

		if (sync.getBlobDoc().getEncoding().equals(EncodingBlob.BASE_64_GZIP))
			formblob = formblob
					+ "application/vnd.xfdl;content-encoding=\"base64-gzip\"\n";

		HashMap<String, Object> pp = new HashMap<String, Object>();
		pp.put("checkdoctype", formname);

		if (getNpjt().queryForInt(
				"select count(0) from snt.doctype where name =:checkdoctype",
				pp) != 1) {
			throw new SyncServiceException(new Exception(),
					SyncServiceError.ERR_NO_FLOW_FORDOC, spo.getEtddocid());
		}

		if (sync.isSetParams()) {
			GetSyncParams param = new GetSyncParams();
			spo = param.getparams(spo, sync.getParams());

		}

		// SIGNSAVE
		if (sync.getAction().equals(Action.SIMPLE_SAVE)) {
			if (spo.getInn().length() == 0 && spo.getKpp().length() == 0
					&& spo.getOkpo() == -1 || spo.getNo_sign().equals("true")) {
				update = true;
				syncobj.setUpdate(true);
			}
			if(spo.getNo_sign().equals("true")) {
				syncobj.setNo_sign(true);
			}
			syncobj.setMess1354(spo.getMess1354());
			syncobj.setId_pak(spo.getId_pak());
			syncobj.setEtdSecondVU36(spo.isEtdSecondVU36());
			if (sync.getSrcSystem().equals(NameSystem.ASETD)) {

				try {

					spo.setPredmake(etdsyncfacade.getPredMaker("ОАО «РЖД»"));

				} catch (Exception e) {
					throw new SyncServiceException(new Exception(),
							SyncServiceError.ERR_NO_RZD, spo.getEtddocid());

				}
			}
			try {

				spo.setPred(signdealer.getPredId(spo.getInn(), spo.getKpp(),
						spo.getOkpo(), spo.getMark()));
				// System.out.println(spo.getPred());
			} catch (Exception e) {
				throw new SyncServiceException(new Exception(),
						SyncServiceError.ERR_ERROR_PREDID, spo.getEtddocid());
			}
			try {
				if (spo.getMark() > -1) {

					if (etdsyncfacade.getMarkCount(spo.getMark()) == 1)
						syncobj.setMark(spo.getMark());
				}
			} catch (Exception e) {
				log.error(TypeConverter.exceptionToString(e));
			}
			if (spo.getPred() == -1 && !update) {

				throw new SyncServiceException(new Exception(),
						SyncServiceError.ERR_ERROR_PREDID, spo.getEtddocid());
			}

			ETDForm form = ETDForm
					.createFromArchive(formblob.getBytes("UTF-8"));
			DataBinder b = form.getBinder();
			if (form.toString().indexOf("cntPred") > -1) {
				if (b.getValue("cntPred").length() > 0)
					try {
						b.setNodeValue("cntPred",
								etdsyncfacade.getNamebyPredid(spo.getPred()));
					} catch (Exception e) {
						throw new SyncServiceException(new Exception(),
								SyncServiceError.ERR_NO_OKPO, spo.getEtddocid());
					}
			}

			syncobj.setBldoc(form.encodeToArchiv());

			syncobj.setDocdata(form.transform("data"));
			syncobj.setDoctype(formname);

			int typeid = etdsyncfacade.getTypeid(syncobj);
			syncobj.setTypeid(typeid);

			syncobj.setPredid(spo.getPred());

			syncobj.setPredcreator(spo.getPredmake());
			long docid = -1;

			// Костыль для ВУ-36
			int sgnlvl = sync.getCountSignatures();
			if (formname.equals(TORLists.VU36) && sgnlvl == 3
					|| formname.equals(TORLists.SF) && sgnlvl == 2) {
				update = true;
			}

			syncobj.setEtdid(spo.getEtddocid());

			docid = signdealer.getDocid(update, spo.getEtddocid(),
					syncobj.getDoctype());

			if (docid == -1) {
				throw new SyncServiceException(new Exception(),
						SyncServiceError.ERR_NO_DOCID_FOR_FORM,
						spo.getEtddocid());
			}

			else if (docid == 0) {
				throw new SyncServiceException(new Exception(),
						SyncServiceError.ERR_OK, spo.getEtddocid());
			}

			else
				syncobj.setDocid(docid);

			GetContent gc = new GetContent();
			gc.getContent(spo, formname, syncobj, formblob);

			if (formname.equals(TORLists.SF)
					|| formname.equals(TORLists.CorrSF)
					|| formname.equals(TORLists.SFCSS)) {

				sql = sql.replaceAll("#sf", ",0");

				try {
					countfpuact = etdsyncfacade.getCountFuact(syncobj);

					if (countfpuact != -1) {

						syncobj.setFpuactid(countfpuact);
						etdsyncfacade.updateFpuact(syncobj);
					}

				} catch (Exception e) {
					log.error(TypeConverter.exceptionToString(e));
				}
				try {

					long fpudocid = Long.parseLong(b.getValue("fpuDocId"));
					etdsyncfacade.getFpuCredentials(fpudocid, syncobj);

				} catch (Exception e) {
					log.error("no fpudocid in SF: "
							+ TypeConverter.exceptionToString(e));

				}

			}

			else {
				sql = sql.replaceAll("#sf", ",null");
			}

			String docflowtype = etdsyncfacade.getFlowType(typeid);
			if (docflowtype.equals("ТОР") || docflowtype.equals("Связь")) {

				if (TORLists.packlist.contains(formname)) {
					syncobj.setVisible(0);
				}

				else if (formname.equals(TORLists.FPu26)) {
					try {
						if (b.getValue("priz_pered").length() > 0
								&& b.getValue("priz_pered").equals("2")) {
							syncobj.setVisible(1);
						} else
							syncobj.setVisible(0);
					} catch (Exception e) {

						log.error("В форме ФПУ-26 c etdid "
								+ syncobj.getEtdid() + " нет тега priz_pred "
								+ TypeConverter.exceptionToString(e));

						syncobj.setVisible(1);
					}
				}

				else {

					syncobj.setVisible(1);
				}
			} else {

				syncobj.setVisible(1);
			}

			// Docflow work

			syncobj.setSignlvl(sgnlvl);
			syncobj.setDrop(0);
			syncobj.setVu36_etdid(spo.getVu36_etdid());
			signdealer.marsh(syncobj, sql, sgnlvl, formname, spo.getEtddocid());

			// transoil sending

			try {
				if (spo.getPred() != -1) {

					if (formname.equals("ГУ-45") || formname.equals("ГУ-23")
							|| formname.equals("СДГ")) {

						if (etdsyncfacade.getNamebyPredid(spo.getPred())
								.indexOf("Усть-Луга Ойл") > -1) {

							signservice.SendXML(spo.getEtddocid(), formname,
									syncobj.getDocdata(), formblob);
						}
					}

				}

			} catch (Exception e) {
				log.error("Ошибка при отправке документов на трансойл: "
						+ TypeConverter.exceptionToString(e));

			}

		}

		if (sync.getAction().equals(Action.DECLINING)) {

			try {

				if (sync.getSrcSystem().equals(NameSystem.ASETD)) {
					spo.setPredmake(etdsyncfacade.getPredMaker("ОАО «РЖД»"));
				}

				String reason = spo.getDecline();

				pp.put("reason", reason);
				pp.put("droppredid", spo.getPredmake());
				pp.put("ETDID", sync.getEtdDocId());
				pp.put("BLOB", formblob.getBytes("UTF-8"));
				getNpjt()
						.update("update snt.docstore set droptime = CURRENT TIMESTAMP,"
								+ "droptxt = :reason,"
								+ "dropid = 0, "
								+ "droppredid = (select predid from snt.docstore where etdid = :ETDID), "
								+ "bldoc =:BLOB, "
								+ "dropwrkid = (select id from snt.wrkname where issm=4 fetch first row only) where etdid = :ETDID",
								pp);

				if (sync.getFormName().equals("Пакет документов РТК")) {
					ETDForm form = ETDForm.createFromArchive(formblob
							.getBytes("UTF-8"));
					DataBinder b = form.getBinder();

					String status = "";

					if (b.getValue("reasonType").length() == 0) {
						status = "Отклонен РЖД";
					} else if (b.getValue("reasonType").equals("correction")) {
						status = "Запрошена корректировка";
					} else if (b.getValue("reasonType").equals("clarification")) {
						status = "Запрошено уточнение";
					}

					pp.put("status", status);
					getNpjt()
							.update("update snt.docstore set reqnum =:status where etdid=:ETDID",
									pp);
				}


				if (formname.equals("ТОРГ-12") || formname.equals("ВУ-36М_О")
						|| formname.equals("ЗПВ") || formname.equals("ЗУВ")
						|| formname.equals("Пакет документов РТК") || formname.equals("ГУ-45")
						|| formname.equals("ГУ-23")) {
					ETDForm form = ETDForm.createFromArchive(formblob.getBytes("UTF-8"));
					syncobj.setEtdid(spo.getEtddocid());
					syncobj.setDropreason(spo.getDecline());
					syncobj.setPredid(spo.getPredmake());
					syncobj.setBldoc(form.encodeToArchiv());
					syncobj.setDocdata(form.transform("data"));
					syncobj.setSignlvl(sync.getCountSignatures());
					syncobj.setDrop(1);
					signdealer.marsh(syncobj, sql, syncobj.getSignlvl(),
							formname, syncobj.getEtdid());
				}

			} catch (Exception e) {
				throw new SyncServiceException(e, -1, "Error occured "
						+ e.getMessage(), spo.getEtddocid());
			}

		}

		if (sync.getAction().equals(Action.UPDATE)) {
			if (sync.getSrcSystem().equals(NameSystem.ASETD)) {
				spo.setPredmake(etdsyncfacade.getPredMaker("ОАО «РЖД»"));
				pp.put("ETDID", sync.getEtdDocId());
				ETDForm form = ETDForm.createFromArchive(formblob
						.getBytes("UTF-8"));
				DataBinder b = form.getBinder();
				if (form.toString().indexOf("cntPred") > -1)
					try {
						b.setNodeValue("cntPred",
								etdsyncfacade.getNamebyPredid(spo.getPred()));
					} catch (Exception e) {
						throw new SyncServiceException(new Exception(),
								SyncServiceError.ERR_NO_OKPO, spo.getEtddocid());
					}
				pp.put("BLDOC", form.encodeToArchiv());
				pp.put("DOCDATA", form.transform("data"));
				getNpjt()
						.update("update snt.docstore set bldoc =:BLDOC, docdata =:DOCDATA where etdid =:ETDID",
								pp);

				if (formname.equals("РНЛ") && sync.getCountSignatures() == 2) {
					getNpjt()
							.update("update snt.docstore set signlvl = null where etdid =:ETDID",
									pp);
				}

				if (formname.equals("Сообщения АСОУП")) {
					getNpjt()
							.update("update snt.docstore set ldate = current_date, ltime = current_time where etdid =:ETDID",
									pp);
					syncobj.setEtdid(spo.getEtddocid());
					syncobj.setPredid(spo.getPredmake());
					syncobj.setBldoc(form.encodeToArchiv());
					syncobj.setDocdata(form.transform("data"));

					signdealer.marsh(syncobj, sql, sync.getCountSignatures(),
							formname, syncobj.getEtdid());
				}
				
				if(formname.equals("ВУ-23_О") && spo.getVu36_etdid() > 0) {
					syncobj.setVu36_etdid(spo.getVu36_etdid());
					syncobj.setEtdid(spo.getEtddocid());
					syncobj.setPredid(spo.getPredmake());
					syncobj.setBldoc(form.encodeToArchiv());
					syncobj.setDocdata(form.transform("data"));

					signdealer.marsh(syncobj, sql, sync.getCountSignatures(),
							formname, syncobj.getEtdid());
				}

			}
		}

		SynchronizeDocResponseWrapper wrapper = new SynchronizeDocResponseWrapper();
		wrapper.setDescription("ok");
		wrapper.setCode(0);
		wrapper.setEtddocid(sync.getEtdDocId());
		wrapper.setUpdate(false);
		SyncResponseAdapter<SynchronizeDocResponseWrapper> adapter = new SyncResponseAdapter<SynchronizeDocResponseWrapper>(
				true, wrapper, SyncServiceError.ERR_OK, spo.getEtddocid());
		return adapter;
	}

	@Override
	protected Object composeResponce(
			SyncResponseAdapter<SynchronizeDocResponseWrapper> adapter) {
		SynchronizeXfdlDocResponseDocument responsedoc = SynchronizeXfdlDocResponseDocument.Factory
				.newInstance();
		SynchronizeXfdlDocResponse response = responsedoc
				.addNewSynchronizeXfdlDocResponse();

		if (adapter.isStatus()) {
			response.setCode(adapter.getResponse().getCode());
			response.setDescription(adapter.getResponse().getDescription());
			response.setEtdDocId(adapter.getResponse().getEtddocid());
			response.setIsUpdateForm(adapter.getResponse().isUpdate());
		} else {
			response.setCode(adapter.getError().getCode());
			response.setDescription(adapter.getError().getMessage());
			response.setEtdDocId(adapter.getEtdid());
			response.setIsUpdateForm(false);
		}
		log.warn("returning response for etdid " + adapter.getEtdid());
		return responsedoc;
	}

}

package sheff.rjd.services.contraginvoice;

import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

import org.apache.log4j.Logger;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.oxm.Marshaller;

import ru.aisa.rgd.ws.dao.ServiceFacade;
import ru.aisa.rgd.ws.documents.ETDForm;
import ru.aisa.rgd.ws.documents.StandartResponseWrapper;
import ru.aisa.rgd.ws.endpoint.ContragAbstractEndpoint;
import ru.aisa.rgd.ws.endpoint.ResponseAdapter;
import ru.aisa.rgd.ws.exeption.ServiceCode;
import ru.aisa.rgd.ws.exeption.ServiceError;
import ru.aisa.rgd.ws.exeption.ServiceException;
import ru.aisa.rgd.ws.utility.DataBinder;
import ru.aisa.rgd.ws.utility.TypeConverter;
import ru.iit.portal.contraginvoice.PackAcceptRequestDocument;
import ru.iit.portal.contraginvoice.PackAcceptRequestDocument.PackAcceptRequest;
import ru.iit.portal.contraginvoice.PackAcceptResponseDocument;
import ru.iit.portal.contraginvoice.PackAcceptResponseDocument.PackAcceptResponse;
import sheff.rgd.ws.Abstract.DoAction;
import sheff.rjd.ws.OCO.AfterSign.SendToEtd;

public class PackAcceptEndpoint extends
		ContragAbstractEndpoint<PackAcceptWrapper> {

	private static Logger log = Logger.getLogger(PackAcceptEndpoint.class);
	private NamedParameterJdbcTemplate npjt;

	public NamedParameterJdbcTemplate getNpjt() {
		return npjt;
	}

	public void setNpjt(NamedParameterJdbcTemplate npjt) {
		this.npjt = npjt;
	}

	private SendToEtd sendtoetd;
	private DoAction formControllers;
	
public DoAction getFormControllers() {
		return formControllers;
	}

	public void setFormControllers(DoAction formControllers) {
		this.formControllers = formControllers;
	}

	public SendToEtd getSendtoetd() {
		return sendtoetd;
	}

	public void setSendtoetd(SendToEtd sendtoetd) {
		this.sendtoetd = sendtoetd;
	}

	public PackAcceptEndpoint(Marshaller marshaller) {
		super(marshaller);
	}

	private static final String blobsql = "select bldoc from snt.docstore where id =:id";
	private static String SF = "Счет-фактура";
	private static String Package = "Пакет документов";
	
	@Override
	protected ResponseAdapter<PackAcceptWrapper> processRequest(Object arg,
			ServiceFacade facade) throws Exception {
		PackAcceptRequestDocument requestdoc = (PackAcceptRequestDocument) arg;
		PackAcceptRequest request = requestdoc.getPackAcceptRequest();
		String cert = new BigInteger(request.getCertSn().replaceAll("[\\u00A0\\s]+", ""),16).toString();
		long etdid = request.getDocid();
		String inn = request.getInn();
		String kpp = request.getKpp();

		if (facade.getContragDao().regcount(cert, inn, kpp) == 0)
			throw new ServiceException(new Exception(),
					ServiceError.ERR_CHECK_PRED_BY_CERT);

		if (facade.getContragDao().rightid(etdid, inn, kpp)==0)
			throw new ServiceException(new Exception(),
					ServiceError.ERR_WRONG_DOCID);
		
		
		
		String formname = facade.getContragDao().getNameByEtdid(etdid);
		PersonObj pers = facade.getContragDao().getPersbyCert(cert);
		
		System.out.println(formname);
		
		if (formname.equals(Package)){
			if (facade.getContragDao().rightidpak(String.valueOf(etdid), inn, kpp) == 0)
				throw new ServiceException(new Exception(),
						ServiceError.ERR_CHECK_DOCSTORE);

			if (!facade.getContragDao().checkpacknew(etdid))
				throw new ServiceException(new Exception(),
						ServiceError.ERR_PACK_NEW);

			String datetime;
			HashMap<String, Object> pp = new HashMap<String, Object>();

			Date data = new Date();
			SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
			datetime = formatter.format(data);
			pp.put("etdid", etdid);
			
			
			AcceptObject PackObject = getAcceptObject(etdid, Package, request.getCertSn().replaceAll("[\\u00A0\\s]+", ""));

			
			ETDForm form = ETDForm.createFromArchive(PackObject.getBlob());

			DataBinder kinder = form.getBinder();

			try {

					kinder.setNodeValue("blocksign2", 1);
					kinder.setNodeValue("time_serv2", datetime);
					kinder.setNodeValue("FIO", pers.getFio());
				} catch (Exception e) {
					log.error(TypeConverter.exceptionToString(e));
				}

				
				try {

					sendtoetd.fakesignpack(PackObject.getPortalid(), pers.getPersid(), PackObject.getWrkid(),
							PackObject.getPredid(), form.encodeToArchiv(),
							form.transform("data"));

					formControllers.doAfterSign(Package,
							new String(form.encodeToArchiv(), "UTF-8"), PackObject.getPredid(), 2, PackObject.getPortalid(), PackObject.getCertx16(), PackObject.getWrkid());

					
					getNpjt().update("update snt.docstore set visible =3 where id_pak = (select id_pak from snt.docstore where etdid=:etdid) and etdid!=:etdid and signlvl is not null and visible =0", pp);
					
					
					
				} catch (Exception e) {
				log.error(TypeConverter.exceptionToString(e));
					throw new ServiceException(new Exception(),
							ServiceError.ERR_UNDEFINED);

				}

			
		}
		
		else if (formname.equals(SF)){
			if (facade.getContragDao().checkifarchieveordropped(etdid))
				throw new ServiceException(new Exception(),
						ServiceError.ERR_ARCHIVE_SF);
			
			if (!facade.getContragDao().InvoicesFilled(etdid))
				throw new ServiceException(new Exception(),
						ServiceError.ERR_NOTWORKING_SF);
			try{

				AcceptObject SFObject = getAcceptObject(etdid, SF, request.getCertSn().replaceAll(" ", ""));
				
				
			formControllers.doAfterSave(SF, new String(SFObject.getBlob(), "UTF-8"), SFObject.getPredid(), 1, SFObject.getCertx16(), SFObject.getPortalid(), SFObject.getWrkid());
			} catch (Exception e){
				log.error(TypeConverter.exceptionToString(e));
				throw new ServiceException(new Exception(),
						ServiceError.ERR_UNDEFINED);
			}
		}
		
		else throw new ServiceException(new Exception(),
				ServiceError.ERR_UNDEFINED_DOCTYPE);
		
		
		
		

		PackAcceptWrapper wrapper = new PackAcceptWrapper();
		wrapper.setDescription("ok");
		wrapper.setCode(ServiceCode.ERR_OK);
		wrapper.setDocumentId(etdid);
		wrapper.setFio(pers.getFio());

		ResponseAdapter<PackAcceptWrapper> adapter = new ResponseAdapter<PackAcceptWrapper>(
				true, wrapper, ServiceError.ERR_OK);
		return adapter;
	}

	@Override
	protected Object composeResponce(ResponseAdapter<PackAcceptWrapper> adapter) {
		PackAcceptResponseDocument respdoc = PackAcceptResponseDocument.Factory
				.newInstance();
		PackAcceptResponse response = respdoc.addNewPackAcceptResponse();

		if (adapter.isStatus()) {
			response.setCode(adapter.getResponse().getCode());
			response.setDescription(adapter.getResponse().getDescription());
			response.setDocid(adapter.getResponse().getDocumentId());
			response.setFio(adapter.getResponse().getFio());

		} else {
			response.setCode(adapter.getError().getCode());
			response.setDescription(adapter.getError().getMessage());
			response.setDocid(-1);
		}

		return respdoc;
	}

	private AcceptObject getAcceptObject(long etdid, String doctype, String cert){
		HashMap<String, Object> pp = new HashMap<String, Object>();
		pp.put("etdid", etdid);
		long portalid = facade.getNpjt().queryForLong(
				"select id from snt.docstore where etdid = :etdid", pp);
		pp.put("id", portalid);
		byte[] blob = (byte[]) facade.getNpjt().queryForObject(blobsql, pp,
				byte[].class);
		int docpredid = getNpjt().queryForInt("select predid from snt.docstore where etdid = :etdid", pp);
		pp.put("DOCTYPE", SF);
		int truewrk = facade
				.getNpjt()
				.queryForInt(
						"select wrkid from snt.doctypeflow where wrkid in (select id from snt.wrkname where issm=1) and dtid in "
								+ "(select id FROM SNT.doctype where name = :DOCTYPE)  fetch first row only",
						pp);
		AcceptObject accobj = new AcceptObject(etdid, portalid, blob, docpredid, truewrk, doctype, cert);
		return accobj;
	}

}

class PackAcceptWrapper extends StandartResponseWrapper {
	String fio;

	public String getFio() {
		return fio;
	}

	public void setFio(String fio) {
		this.fio = fio;
	}

}

class AcceptObject{
	long etdid;
	long portalid;
	byte[] blob;
	int predid;
	int wrkid;
	String doctype;
	String certx16;
	public AcceptObject(long etdid, long portalid, byte[] blob, int predid,
			int wrkid, String doctype, String certx16) {
		super();
		this.etdid = etdid;
		this.portalid = portalid;
		this.blob = blob;
		this.predid = predid;
		this.wrkid = wrkid;
		this.doctype = doctype;
		this.certx16 = certx16;
	}
	public long getEtdid() {
		return etdid;
	}
	public void setEtdid(long etdid) {
		this.etdid = etdid;
	}
	public long getPortalid() {
		return portalid;
	}
	public void setPortalid(long portalid) {
		this.portalid = portalid;
	}
	public byte[] getBlob() {
		return blob;
	}
	public void setBlob(byte[] blob) {
		this.blob = blob;
	}
	public int getPredid() {
		return predid;
	}
	public void setPredid(int predid) {
		this.predid = predid;
	}
	public int getWrkid() {
		return wrkid;
	}
	public void setWrkid(int wrkid) {
		this.wrkid = wrkid;
	}
	public String getDoctype() {
		return doctype;
	}
	public void setDoctype(String doctype) {
		this.doctype = doctype;
	}
	public String getCertx16() {
		return certx16;
	}
	public void setCertx16(String certx16) {
		this.certx16 = certx16;
	}
	
}


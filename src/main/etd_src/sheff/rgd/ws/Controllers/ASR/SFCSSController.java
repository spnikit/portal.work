package sheff.rgd.ws.Controllers.ASR;

import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import ru.aisa.crypto.FieldType;
import ru.aisa.crypto.WhoType;
import ru.aisa.crypto.X509Parser;
import ru.aisa.crypto.X509ParserFactory;
import ru.aisa.mail.EmailHelper;
import ru.aisa.rgd.etd.dao.ETDSyncServiceFacade;
import ru.aisa.rgd.etd.objects.ETDDocumentData;
import ru.aisa.rgd.ws.documents.ETDForm;
import ru.aisa.rgd.ws.utility.DataBinder;
import ru.aisa.rgd.ws.utility.TypeConverter;
import sheff.rgd.ws.Abstract.AbstractAction;
import sheff.rjd.services.syncutils.SyncObj;
import sheff.rjd.services.transoil.TransService;
import sheff.rjd.utils.Base64;
import sheff.rjd.ws.OCO.AfterEdit.FakeSignature;
import sheff.rjd.ws.OCO.AfterSign.SendToEtd;

import com.aisa.portal.invoice.ttk.SellerSignedInvoceToTTK;

public class SFCSSController extends AbstractAction {

	protected final Logger log = Logger.getLogger(getClass());
	private NamedParameterJdbcTemplate npjt;
	private SendToEtd sendtoetd;
	private TransService sendtotransoil;
	private SellerSignedInvoceToTTK ssiobj;
	private String parentform;
	private FakeSignature fakesignature;
	private EmailHelper mail;
	public NamedParameterJdbcTemplate getNpjt() {
		return npjt;
	}

	public void setNpjt(NamedParameterJdbcTemplate npjt) {
		this.npjt = npjt;
	}

	public SellerSignedInvoceToTTK getSsiobj() {
		return ssiobj;
	}

	public void setSsiobj(SellerSignedInvoceToTTK ssiobj) {
		this.ssiobj = ssiobj;
	}

	private ETDSyncServiceFacade etdsyncfacade;

	public ETDSyncServiceFacade getEtdsyncfacade() {
		return etdsyncfacade;
	}

	public void setEtdsyncfacade(ETDSyncServiceFacade etdsyncfacade) {
		this.etdsyncfacade = etdsyncfacade;
	}

	public SendToEtd getSendtoetd() {
		return sendtoetd;
	}

	public String getParentform() {
		return parentform;
	}

	public void setParentform(String parentform) {
		this.parentform = parentform;
	}

	public void setSendtoetd(SendToEtd sendtoetd) {
		this.sendtoetd = sendtoetd;
	}

	public TransService getSendtotransoil() {
		return sendtotransoil;
	}

	public void setSendtotransoil(TransService sendtotransoil) {
		this.sendtotransoil = sendtotransoil;
	}

	
	public FakeSignature getFakesignature() {
		return fakesignature;
	}

	public void setFakesignature(FakeSignature fakesignature) {
		this.fakesignature = fakesignature;
	}
	
	public EmailHelper getMail() {
		return mail;
	}

	public void setMail(EmailHelper mail) {
		this.mail = mail;
	}

	@Override
	public void doAfterSign(String docName, String docdata, int predid,
			int signNumber, long id, String certserial, int WrkId) {
		Map<String, Object> hm1 = new HashMap<String, Object>();
		hm1.put("id", id);
		int drop = getNpjt()
				.queryForInt(
						"select CASE when dropid is null then 0 else 1 end from snt.docstore where id = :id",
						hm1);

		if (drop == 1) {
			try {
				npjt.update("update snt.sf_reports set responseid = (select dropid from snt.docstore where id =:id) where id =:id", hm1);
				sendtoetd.SendToEtdMessage(id, docdata, parentform,
						signNumber, drop, false);
				sendtotransoil.SendSigntoTransoil(id, signNumber, drop,
						predid);
				if (drop==1){
					if (!isUtochnenie(id))
						SendEmailToIIT(id, certserial);
					
				}
			} catch (Exception e) {
				log.error(TypeConverter.exceptionToString(e));
			}
		}


	}

	@Override
	public void doAfterSave(String docName, String docdata, int predid,
			int signNumber, String CertID, long id, int WrkId) throws Exception {
		try {
			sendtoetd.FakeSign(docName, docdata, predid, signNumber, CertID,
					id, WrkId);
		} catch (Exception e) {
			log.error(TypeConverter.exceptionToString(e));
		}

		sendtoetd.SendToEtdMessage(id, docdata, parentform, signNumber, 0,
				false);
		sendtotransoil.SendSigntoTransoil(id, signNumber, 0, predid);
//		sendtotransoil.SendSFNotice(id,0);
	}

	@Override
	public void doAfterSync(String formname, SyncObj syncobj, String sql,
			int signum) {
		if (syncobj.getSignlvl()==1) {
			try {
			ETDForm form = ETDForm.createFromArchive(syncobj.getBldoc());
			DataBinder b = form.getBinder();
			try {
				Calendar cal = Calendar.getInstance();
				SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
				b.setNodeValue("recieve_date", sdf.format(cal.getTime()));
				syncobj.setBldoc(form.encodeToArchiv());
				syncobj.setDocdata(form.transform("data"));
			} catch (Exception e) {
				log.error(TypeConverter.exceptionToString(e));
			}
			try {
				long ppsid = 0;
				ppsid = Long.parseLong(b.getValue("fpuDocId"));
				HashMap<String, Object> pp = new HashMap<String, Object>();
				pp.put("ppsid", ppsid);

				int predid = npjt.queryForInt(
						"select predid from snt.docstore where etdid = :ppsid",
						pp);
				syncobj.setPredid(predid);
			} catch (Exception e) {
				e.printStackTrace();
				log.error("no tag id_PPS found "
						+ TypeConverter.exceptionToString(e));
			}
			syncobj.setContent(b.getValue("id_pak") + ", " + b.getValue("opisanie"));
			etdsyncfacade.insertDocstore(sql,
					etdsyncfacade.getWorkerWithorderNull(syncobj));
			etdsyncfacade.updateDSF(syncobj);

			if (syncobj.getId_pak().length() > 2) {
				try {
					HashMap<String, Object> pp = new HashMap<String, Object>();
					pp.put("docid", syncobj.getDocid());
					pp.put("typename", "Пакет документов ЦСС");
					long packid = npjt
							.queryForLong(
									"select id from snt.docstore where "
											+ "id_pak = (select id_pak from snt.docstore where id =:docid) and typeid = (select id from snt.doctype where name =:typename)",
									pp);
					pp.put("packid", packid);
					npjt.update(
							"update snt.docstore set repdate =(select repdate from snt.docstore where id =:packid), "
									+ "reqdate = (select reqdate from snt.docstore where id =:packid) where id =:docid",
							pp);
				} catch (Exception e) {
					log.error(TypeConverter.exceptionToString(e));
				}
			}
			
			try {
				if (b.getValue("num_isprav").length() == 0
						&& b.getValue("date_ot_isprav").length() == 0) {
					syncobj.setSf_type(2);
				} else {
					syncobj.setSf_type(0);
				}

				HashMap<String, Object> sfmap = new HashMap<String, Object>();
				sfmap.put("id", syncobj.getDocid());
				
				sfmap.put("sftypeint", syncobj.getSf_type());
				sfmap.put("sf_gfsgn", 0);
				npjt.update("update snt.docstore set sf_type = :sftypeint, sf_gfsgn =:sf_gfsgn where id =:id", sfmap);
			} catch (Exception e) {
				log.error(TypeConverter.exceptionToString(e));
			}

			String bcabinetid = etdsyncfacade.getCabinetIdByPred(syncobj
					.getPredid());
			String scabinetid = etdsyncfacade.getCabinetIdByPred(etdsyncfacade
					.getPredMaker("ОАО «РЖД»"));
			byte[] xml_text = Base64.decode(b.getValue("xml_text"));
			byte[] xml_sign = Base64.decode(b.getValue("xml_sign"));

			ssiobj.processSF(bcabinetid, scabinetid, syncobj.getDocid(),
					xml_text, xml_sign, false);

			// Отправка в Трансойл
			if (etdsyncfacade.getNamebyPredid(syncobj.getPredid()).indexOf(
					"ООО «Трансойл»") > -1) {
				sendtotransoil.SendtoTransoil(syncobj.getEtdid(), parentform,
						syncobj.getDocdata(), new String(syncobj.getBldoc(),
								"UTF-8"));
			}
			
			
		} catch (Exception e) {
			log.error(TypeConverter.exceptionToString(e));
		}
	}

	if (syncobj.getSignlvl()==2) {
		HashMap<String, Object> pp = new HashMap<String, Object>();
		pp.put("typeid", syncobj.getTypeid());
		pp.put("order", 4);
		pp.put("id", syncobj.getDocid());
		int wrkid = getNpjt().queryForInt("select wrkid from snt.doctypeflow where order = :order and dtid = :typeid ", pp);
		int predid = getNpjt().queryForInt("select predid from snt.docstore where id =:id ", pp);
		fakesignature.fakesign(syncobj.getDocid(), 0, wrkid, predid, syncobj.getBldoc(), syncobj.getDocdata());
	}

	}
	private String getEmailfromCert (String certstring){
		HashMap<String, Object> pp = new HashMap<String, Object>();
		pp.put("certserial", new BigInteger(certstring, 16).toString());
//		pp.put("certserial", "514672198863319208206685");
		byte[] filecontent = (byte[])npjt.queryForObject("select publickey from snt.personall where certserial =:certserial", pp, byte[].class);
		
		String email = null;
		try{
			X509Parser p = X509ParserFactory.getParser(filecontent);
			email = p.getValue(WhoType.SUBJECT,FieldType.E);
			} catch (Exception e){
				log.error(TypeConverter.exceptionToString(e));
			}

		return email;
	}
	
	private void SendEmailToIIT(long id, String certserial){
		HashMap<String, Object> pp = new HashMap<String, Object>();
		pp.put("id", id);
		String email = getEmailfromCert(certserial);
		String declinereason = npjt.queryForObject("select droptxt from snt.docstore where id=:id", pp, String.class);
		String predname = (String) npjt.queryForObject("select rtrim(vname) from snt.pred where id = (select predid from snt.docstore where id =:id)",pp, String.class);
		int etdid = getNpjt().queryForInt("select etdid from snt.docstore where id = :id", pp);
		 String mes="<div style='background-color: #dddddd'><b>Информация о СФ</b></div>"+
				 	"<br>"+
	                "<div>"+
	                "<div>ID документа: "+id+"</div>"+
	                "<br>"+
	                "<div>ID документа в АС ЭТД: "+etdid+"</div>"+
	                "<br>"+
	                "<div>Контрагент: "+predname+"</div>"+
	                "<br>"+
	                "<div>E-mail отклонившего: "+email+"</div>"+
	                "<br>"+
	                "<div>Причина отклонения: "+declinereason+"</div>"+
	                "</div>"+
	                "</div>";
         
	
   
		try {

			mail.sendEmail("Техническое отклонение",  mes);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			log.error(TypeConverter.exceptionToString(e));
		}
		
		
	
		
	}
	private boolean isUtochnenie(long docid) throws InterruptedException{
		Thread.sleep(3000);
		HashMap<String, Object> pp = new HashMap<String, Object>();
		pp.put("id", docid);
		if (npjt.queryForInt("select count(0) from snt.dfkorr where id =:id", pp)>0) return true;
		else return false;
	}

	public ETDDocumentData EditBeforeOpen(String docName, ETDDocumentData doc) {
		// TODO Auto-generated method stub
		return doc;
	}
}

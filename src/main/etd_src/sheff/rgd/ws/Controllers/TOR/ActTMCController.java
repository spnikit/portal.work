package sheff.rgd.ws.Controllers.TOR;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.transform.TransformerException;

import org.apache.log4j.Logger;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.w3c.dom.DOMException;
import org.w3c.dom.Element;

import com.aisa.portal.invoice.operator.obj.OperatorObject;

import ru.aisa.rgd.etd.dao.ETDSyncServiceFacade;
import ru.aisa.rgd.etd.objects.ETDDocumentData;
import ru.aisa.rgd.ws.dao.ServiceFacade;
import ru.aisa.rgd.ws.documents.ETDForm;
import ru.aisa.rgd.ws.domain.BankRequisites;
import ru.aisa.rgd.ws.domain.Document;
import ru.aisa.rgd.ws.exeption.InternalException;
import ru.aisa.rgd.ws.exeption.ServiceException;
import ru.aisa.rgd.ws.utility.DataBinder;
import ru.aisa.rgd.ws.utility.RowFiller;
import ru.aisa.rgd.ws.utility.RowHandler;
import ru.aisa.rgd.ws.utility.TypeConverter;
import sheff.rgd.ws.Abstract.AbstractAction;
import sheff.rjd.services.syncutils.SyncObj;
import sheff.rjd.services.transoil.TransService;
import sheff.rjd.ws.OCO.AfterSign.SendToEtd;
import sheff.rjd.ws.OCO.AfterSign.TOR.NumberOfWords;


public class ActTMCController extends AbstractAction{

	private static DecimalFormat nf = new DecimalFormat("##############.00");
	private static NumberFormat nfi = new DecimalFormat("##############");
	
	private String parentform;
	private NamedParameterJdbcTemplate npjt;
	private SendToEtd sendtoetd;
	private TransService sendtotransoil;  
	private ServiceFacade facade;
	private ETDSyncServiceFacade etdsyncfacade;
	private OperatorObject oper;
	public ETDSyncServiceFacade getEtdsyncfacade() {
		return etdsyncfacade;
	}

	public void setEtdsyncfacade(ETDSyncServiceFacade etdsyncfacade) {
		this.etdsyncfacade = etdsyncfacade;
	}
	 
	public NamedParameterJdbcTemplate getNpjt() {
		return npjt;
	}
	public TransService getSendtotransoil() {
		return sendtotransoil;
	}
	public void setSendtotransoil(TransService sendtotransoil) {
		this.sendtotransoil = sendtotransoil;
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
	
	public ServiceFacade getFacade() {
		return facade;
	}
	public void setFacade(ServiceFacade facade) {
		this.facade = facade;
	}

	protected final Logger	log	= Logger.getLogger(getClass());
	
	public void setParentform(String parentname) {
		this.parentform = parentname;
	}

	public String getParentform() {
		return parentform;
	}
	
	public OperatorObject getOper() {
		return oper;
	}

	public void setOper(OperatorObject oper) {
		this.oper = oper;
	}

	private class Addres {

		public String ind;
		public String kod;
		public String raion;
		public String punkt;
		public String town;
		public String street;
		public String house;
		public String korp;
		public String flat;

		public Addres(String[] str) {
			this.ind = str[1].trim();
			this.kod = str[0].trim();
			this.raion = null;
			this.punkt = null;
			this.town = str[2].trim();
			this.street = str[3].trim();
			this.house = str[4].trim();
			this.korp = str[5].trim();
			this.flat = str[6].trim();
		}

	}

	private class AddresList {
		public List<Addres> rowList = new ArrayList<Addres>();

	}

@Override
public void doAfterSign(String docName, String docdata, int predid, int signNumber,  long id, String certserial, int WrkId) {
	Map<String, Object> hm1 = new HashMap<String, Object>();
	hm1.put("id", id);
	hm1.put("predid", predid);
	int drop = getNpjt()
			.queryForInt(
					"select CASE when dropid is null then 0 else 1 end from snt.docstore where id = :id",
					hm1);

	if (signNumber == 2 || drop == 1) {

		try {
			sendtoetd.SendToEtdMessage(id, docdata, parentform, signNumber,
					drop, false);

			sendtotransoil.SendSigntoTransoil(id, signNumber, drop, predid);
		} catch (Exception e) {
			log.error(TypeConverter.exceptionToString(e));

		}

		if (drop == 0)
			try {
				
				sendtoetd.PackUpdate(id);
				
				
				int typetorg  = getNpjt().queryForInt("select torgtype from snt.pred where id = "
						+ "(select case when headid is null then id else headid end from snt.pred where id = :predid)", hm1);
				
				if (typetorg!=4)
				
				crtorg(docdata, predid, signNumber, id, typetorg);
				
				
				
				
			} catch (Exception e) {
				log.error(TypeConverter.exceptionToString(e));
			}

		if (drop == 1)
			sendtoetd.updateVisible(id);

	}
}

private void crtorg(String docdata, int predId, int signNumber, long actid, int typetorg)
		throws InternalException {

	try {

		ETDForm actform = ETDForm.createFromArchive(docdata
				.getBytes("UTF-8"));

		final DataBinder actbinder = actform.getBinder();

		ETDForm torgform = new ETDForm(
				facade.getDocumentTemplate("ТОРГ-12"));
		DataBinder torgbinder = torgform.getBinder();
		long docid = facade.getNextDocumentId();
		HashMap<String, Object> pp = new HashMap<String, Object>();
		try {

			try{
				torgbinder.setNodeValue("documentId", docid);
				}catch (Exception e){
					log.error(TypeConverter.exceptionToString(e));
				}
			
			
			DecimalFormatSymbols custom = new DecimalFormatSymbols();
			custom.setDecimalSeparator('.');
			nf.setDecimalFormatSymbols(custom);
			nfi.setRoundingMode(RoundingMode.DOWN);

			torgbinder.setNodeValue("flag_ASU", 0);
			torgbinder.setNodeValue("P_1", actbinder.getValue("P_3"));
			torgbinder.setNodeValue("P_1z", actbinder.getValue("P_24"));
			torgbinder.setNodeValue("P_1i", actbinder.getValue("P_25"));
			torgbinder.setNodeValue("P_1a", actbinder.getValue("P_31"));
			torgbinder.setNodeValue("P_1i", actbinder.getValue("P_25"));
			torgbinder.setNodeValue("P_1b", actbinder.getValue("P_32"));
			torgbinder.setNodeValue("P_1v", actbinder.getValue("P_33"));
			torgbinder.setNodeValue("P_2", actbinder.getValue("P_23"));
			torgbinder.setNodeValue("P_4", actbinder.getValue("P_6"));
			torgbinder.setNodeValue("P_4a", actbinder.getValue("P_28"));
			torgbinder.setNodeValue("P_4b", actbinder.getValue("P_29"));
			torgbinder.setNodeValue("P_4v", actbinder.getValue("P_30"));
			torgbinder.setNodeValue("P_5", actbinder.getValue("P_59"));
			torgbinder.setNodeValue("P_5z", actbinder.getValue("P_60"));
			torgbinder.setNodeValue("P_5i", actbinder.getValue("P_61"));
			torgbinder.setNodeValue("P_5a", actbinder.getValue("P_62"));
			torgbinder.setNodeValue("P_5b", actbinder.getValue("P_63"));
			torgbinder.setNodeValue("P_5v", actbinder.getValue("P_64"));
			
			String addressDataString =  actbinder.getValue("P_34");
			String account = "";
			String bankName = "";
			String bik = "";
			String corrAccount = "";
			String[] strArray =  addressDataString.split(",");
			for(int i = 0; i < strArray.length; i++){

				String[] finalStrArray = strArray[i].split(":");
				if(finalStrArray.length==2){
					String left = finalStrArray[0].trim();
					String right = finalStrArray[1].trim();
					if(left.equals("БАНК")){
						bankName = right;
					}
					if(left.equals("р/с")){
						account = right;
					}
					if(left.equals("БИК")){
						bik = right;
					}
					if(left.equals("к/с")){
						corrAccount = right;
					}
				}
				
			}	
//			String predname = etdsyncfacade.getNamebyPredid(predId);
			
			try{
				BankRequisites req = new BankRequisites();
				req = facade.getContragDao().getBankRequisitesByPredId(predId);
				torgbinder.setNodeValue("P_1g", req.getAccount());
				torgbinder.setNodeValue("P_1d", req.getBankname());
				torgbinder.setNodeValue("P_1j", req.getKorraccount());
				torgbinder.setNodeValue("P_1e", req.getBik());
				
				torgbinder.setNodeValue("P_5g", req.getAccount());
				torgbinder.setNodeValue("P_5d", req.getBankname());
				torgbinder.setNodeValue("P_5j", req.getKorraccount());
				torgbinder.setNodeValue("P_5e", req.getBik());
				
				
			} catch (Exception e){
				log.error(TypeConverter.exceptionToString(e));
				torgbinder.setNodeValue("P_5g", actbinder.getValue("P_65"));
				torgbinder.setNodeValue("P_5d", actbinder.getValue("P_66"));
				torgbinder.setNodeValue("P_5e", actbinder.getValue("P_67"));
				torgbinder.setNodeValue("P_5j", actbinder.getValue("P_68"));
			}

			torgbinder.setNodeValue("P_5_1a", actbinder.getValue("P_23"));
			torgbinder.setNodeValue("P_6", actbinder.getValue("P_34"));
			torgbinder.setNodeValue("P_6v", actbinder.getValue("P_35"));
			torgbinder.setNodeValue("P_6b", actbinder.getValue("P_36"));
			torgbinder.setNodeValue("P_6a", actbinder.getValue("P_37"));
			//torgbinder.setNodeValue("P_6g", actbinder.getValue("P_39"));
			//new
			torgbinder.setNodeValue("P_6g", account);
			torgbinder.setNodeValue("P_6d", bankName);
			torgbinder.setNodeValue("P_6e", bik);
			torgbinder.setNodeValue("P_6j", corrAccount);
			//new end
			torgbinder.setNodeValue("P_6_1a", actbinder.getValue("P_42"));
			torgbinder.setNodeValue("P_7", actbinder.getValue("P_43"));
			torgbinder.setNodeValue("P_8", actbinder.getValue("P_9"));
			torgbinder.setNodeValue("P_9", actbinder.getValue("P_10"));
			torgbinder.setNodeValue("P_14", actbinder.getValue("P_2"));

			Table bt = new Table();
			final Element e = actbinder.getElement("data");
			actbinder.handleTable("table1", "row", new RowHandler<Table>() {
				public void handleRow(DataBinder b, int rowNum, Table bt)
						throws InternalException {
					TableEntry t = new TableEntry();
					/* P_15 */t.number = b.getValue("P_14");
					/* P_16_1 и P_16_2 */
					t.denominationP161 = b.getValue("P_15");
					t.denominationP162 = b.getValue("P_16") + " "
							+ b.getValue("P_17a") + " "
							+ b.getValue("P_18");

					try {
						/* P_17 */t.kod = actbinder.getValue(e, "P_45");
						/* P_18 */t.name_ed_iz = actbinder.getValue(e,
								"P_46");
						/* P_19 */t.kod_OKEI = actbinder
								.getValue(e, "P_47");
						/* P_24 */t.amount = Float.parseFloat(actbinder
								.getValue(e, "P_48"));
					} catch (InternalException e) {
						e.printStackTrace();
					}

					/* P_26 */t.sumExNDS = Float.parseFloat(b
							.getValue("P_19"));
					/* P_25 */t.price = Float.parseFloat(b.getValue("P_19"));

					bt.rowList.add(t);
				}

			}, bt);

			TableEntry[] tea = bt.rowList.toArray(new TableEntry[bt.rowList
					.size()]);
			torgbinder.setRootElement("data");
			torgbinder.fillTable(tea, new RowFiller<TableEntry, Object>() {
				public void fillRow(DataBinder b, TableEntry te,
						int numRow, Object opt) throws DOMException,
						InternalException {
					b.setNodeValue("P_15", te.number);
					b.setNodeValue("P_16.1", te.denominationP161);
					b.setNodeValue("P_16.2", te.denominationP162);
					b.setNodeValue("P_16", te.denominationP161 + " "
							+ te.denominationP162);

					b.setNodeValue("P_17", te.kod);
					b.setNodeValue("P_18", te.name_ed_iz);
					b.setNodeValue("P_19", te.kod_OKEI);
					b.setNodeValue("P_24", nf.format(te.amount));
					b.setNodeValue("P_25", nf.format(te.price));
					b.setNodeValue("P_26", nf.format(te.sumExNDS));

					b.setNodeValue("P_27", "18");
					b.setNodeValue("P_28",
							nf.format(te.sumExNDS * 18 / 100));
					b.setNodeValue("P_29", nf.format(te.sumExNDS * 1.18));
				}
			}, "table1", "row");

			String strnub = null;
			float[] Sum = new float[2];
			for (TableEntry te : tea) {
				Sum[0] += te.amount;
				Sum[1] += te.sumExNDS;
				strnub = te.number;
			}

			torgbinder.setNodeValue("P_32", nf.format(Sum[0]));

			torgbinder.setNodeValue("P_33", nf.format(Sum[1]));
			torgbinder.setNodeValue("P_34", nf.format(Float
					.parseFloat(torgbinder.getValue("P_33")) * 0.18));
			torgbinder.setNodeValue("P_35", nf.format(Float
					.parseFloat(torgbinder.getValue("P_33")) * 1.18));

			torgbinder.setNodeValue("P_38", torgbinder.getValue("P_32"));
			torgbinder.setNodeValue("P_39", torgbinder.getValue("P_33"));
			torgbinder.setNodeValue("P_40", nf.format(Float
					.parseFloat(torgbinder.getValue("P_39")) * 0.18));
			torgbinder.setNodeValue("P_41", nf.format(Float
					.parseFloat(torgbinder.getValue("P_39")) * 1.18));

			torgbinder.setNodeValue("P_44", torgbinder.getValue("P_14"));

			NumberOfWords nownumber = new NumberOfWords(strnub);
			String[] str = nownumber.num2str().split(" руб");

			torgbinder.setNodeValue("P_46", str[0]);

//			берем не весь тег P_41 а только целую часть
			String rub = getRub(torgbinder.getValue("P_41"));
			NumberOfWords now = new NumberOfWords(nfi.format(Float.parseFloat(torgbinder.getValue("P_41"))));
			String kop = getKoppeki(torgbinder.getValue("P_41"));
			String finalOnlyRubInWords = now.num2str().substring(0, now.num2str().indexOf("00 коп.")-1);
			
			torgbinder.setNodeValue("P_51", toUpperChar0(finalOnlyRubInWords));
			torgbinder.setNodeValue("P_51_1", nfi.format(Float.parseFloat(torgbinder.getValue("P_41"))));
			torgbinder.setNodeValue("P_51_2", kop);

			torgbinder.setNodeValue("P_50a", torgbinder.getValue("P_52a"));
			torgbinder.setNodeValue("P_50v", torgbinder.getValue("P_52a"));

			torgbinder.setNodeValue("P_53a", actbinder.getValue("P_49"));
			torgbinder.setNodeValue("P_53b", actbinder.getValue("P_50"));
			torgbinder.setNodeValue("P_53v", actbinder.getValue("P_51"));
			torgbinder.setNodeValue("P_53g", actbinder.getValue("P_52"));
			torgbinder.setNodeValue("P_56", torgbinder.getValue("P_14"));
			torgbinder.setNodeValue("P_58", actbinder.getValue("P_11"));
			torgbinder.setNodeValue("P_59", actbinder.getValue("P_27"));

			torgbinder.setNodeValue("P_63", actbinder.getValue("P_38"));
			torgbinder.setNodeValue("P_64", actbinder.getValue("P_40"));
			torgbinder.setNodeValue("P_65", actbinder.getValue("P_41"));
			torgbinder.setNodeValue("P_66", actbinder.getValue("P_69"));
			torgbinder.setNodeValue("P_69", actbinder.getValue("P_56"));

			
			
			try{
				pp.put("innsell", torgbinder.getValue("P_5z"));
				pp.put("kppsell", torgbinder.getValue("P_5i"));
				pp.put("buyname", "ОАО «РЖД»");
				String cabinetIdSell = (String) getNpjt()
						.queryForObject(
								"Select contr_code from snt.pred where inn = :innsell and kpp =:kppsell and headid is null",
								pp, String.class);
				String cabinetIdRecv = (String) getNpjt()
						.queryForObject(
								"Select contr_code from snt.pred where name=:buyname and type = 1",
								pp, String.class);
				
				
				torgbinder.setNodeValue("cabinetIdSell", cabinetIdSell);
				torgbinder.setNodeValue("cabinetIdRecv", cabinetIdRecv);
				torgbinder.setNodeValue("nameoper", oper.getNameUrLic());
				torgbinder.setNodeValue("innoper", oper.getInn());
				torgbinder.setNodeValue("idoper", oper.getId());
				}catch (Exception ex){
					log.error(TypeConverter.exceptionToString(ex));
				}
			
			try{
			FillTableAddres(torgbinder, torgbinder.getValue("P_1a"),
					"GruzOtprAddr", "row");
			} catch (Exception ex){
				ex.printStackTrace();
			}
			try{
			FillTableAddres(torgbinder, torgbinder.getValue("P_4a"),
					"GruzPolAddr", "row");
		} catch (Exception ex){
			ex.printStackTrace();
		}
			try{
			FillTableAddres(torgbinder, torgbinder.getValue("P_5a"),
					"PostAddr", "row");
	} catch (Exception ex){
		ex.printStackTrace();
	}
			try{
			FillTableAddres(torgbinder, torgbinder.getValue("P_6a"),
					"BuyerAddr", "row");
} catch (Exception ex){
	ex.printStackTrace();
}
			
		} catch (Exception e) {
			log.error(TypeConverter.exceptionToString(e));
		}
		Document document = new Document();

		document.setBlDoc(torgform.encodeToArchiv());
		document.setDocData(torgform.transform("data"));
		document.setSignLvl(0);
		document.setPredId(predId);
		document.setType("ТОРГ-12");
		document.setId(docid);
//		long docid = facade.insertDocument(document);
		facade.insertDocumentWithDocid(document);
		
		pp.put("docid", docid);
		pp.put("actid", actid);

		pp.put("vagnum", torgbinder.getValue("P_58"));
		// код услуги
		pp.put("di", "08");
		pp.put("dogovor",
				torgbinder.getValue("P_8") + " от "
						+ torgbinder.getValue("P_9"));
		pp.put("onbaseid", Integer.parseInt(torgbinder.getValue("P_59")));
		pp.put("repdate", torgbinder.getValue("P_44"));

//		if (etdsyncfacade.getNamebyPredid(predId).equals("ЗАО «Спецэнерготранс»")){
//			pp.put("visible",1);
//		}
//		else {
//			pp.put("visible",0);
//		}
		
		int visible =-1;
		int readid = -1;
		
		switch(typetorg) {
		case 1: visible =1;
				readid=-1;
				break;
		case 2: visible =0;
				readid=-1;
				break;
		case 3: visible =1;
				readid=-1;
				break;
		}
		
		pp.put("visible", visible);
		pp.put("readid", readid);
		
		facade.getNpjt()
				.update("update snt.docstore set visible=:visible, readid = :readid, "
						+ "id_pak = (select id_pak from snt.docstore where id = :actid), vagnum = :vagnum, di =:di, no =:dogovor, onbaseid = :onbaseid, repdate =:repdate  where id = :docid",
						pp);

	} catch (ServiceException e) {
		log.error(TypeConverter.exceptionToString(e));
		e.printStackTrace();
	} catch (UnsupportedEncodingException e) {
		log.error(TypeConverter.exceptionToString(e));
		e.printStackTrace();
	} catch (IOException e) {
		log.error(TypeConverter.exceptionToString(e));
		e.printStackTrace();
	} catch (DOMException e) {
		log.error(TypeConverter.exceptionToString(e));
		e.printStackTrace();
	} catch (TransformerException e) {
		log.error(TypeConverter.exceptionToString(e));
		e.printStackTrace();
	}
	
}	
	

private class Table {
	public List<TableEntry> rowList = new ArrayList<TableEntry>();

}

private class TableEntry {
	public String number;
	public String denominationP161;
	public String denominationP162;
	public String kod;
	public String name_ed_iz;
	public String kod_OKEI;
	public float amount;
	public float price;
	public float sumExNDS;
	public long rate;
	public float sumNDS;
	public float sumWithNDS;

	@Override
	public String toString() {
		return "TableEntry [number=" + number + ", denomination="
				+ denominationP161 + " " + denominationP162 + ", kod="
				+ kod + ", name_ed_iz=" + name_ed_iz + ", kod_OKEI="
				+ kod_OKEI + ", amount=" + amount + ", price=" + price
				+ ", sumExNDS=" + sumExNDS + ", rate=" + rate + ", sumNDS="
				+ sumNDS + ", sumWithNDS=" + sumWithNDS + "]";
	}

}

private String getKoppeki(String s) {
	int i = s.indexOf(".");
	// String str = s.substring(i+1);
	return s.substring(i + 1);
}

private String getRub(String s) {
	int i = s.indexOf(".");
	// String str = s.substring(i+1);
	return s.substring(0, i);
}

public String toUpperChar0(String str) {
	char[] c = str.toCharArray();
	c[0] = Character.toUpperCase(c[0]);
	return new String(c);
}

public void FillTableAddres(DataBinder binder, String addres,
		String nameTable, String nameRow) throws InternalException {
	binder.fillTable(splitAddres(addres, binder),
			new RowFiller<Addres, Object>() {

				@Override
				public void fillRow(DataBinder b, Addres row, int numRow,
						Object options) throws DOMException,
						InternalException {
//					System.out.println(row.ind);
//					System.out.println(row.kod);
//					System.out.println(row.raion);
//					System.out.println(row.punkt);
//					System.out.println(row.town);
//					System.out.println(row.street);
//					System.out.println(row.house);
//					System.out.println(row.korp);
//					System.out.println(row.flat);
					b.setNodeValue("ind", row.ind);
					b.setNodeValue("kod", row.kod);
					b.setNodeValue("raion", row.raion);
					b.setNodeValue("punkt", row.punkt);
					b.setNodeValue("town", row.town);
					b.setNodeValue("street", row.street);
					b.setNodeValue("house", row.house);
					b.setNodeValue("korp", row.korp);
					b.setNodeValue("flat", row.flat);

				}

			}, nameTable, nameRow);
}

public Addres[] splitAddres(String str, DataBinder binderform)
		throws InternalException {

//	String[] s = str.split(",", 7);
//	System.out.println(str);
	String[] s = str.split(",");
	if (binderform.getValue("P_1a").equals(str))
		s[0] = binderform.getValue("P_64");
	if (binderform.getValue("P_4a").equals(str))
		s[0] = binderform.getValue("P_65");
	if (binderform.getValue("P_5a").equals(str))
		s[0] = binderform.getValue("P_66");
	if (binderform.getValue("P_6a").equals(str))
		s[0] = binderform.getValue("P_63");
	AddresList addl = new AddresList();
	addl.rowList.add(new Addres(s));
//	System.out.println(addl);
	Addres[] addarr = addl.rowList.toArray(new Addres[addl.rowList.size()]);
	return addarr;
}
	
	@Override
	public void doAfterSave(String docName, String docdata, int predid, int signNumber, String CertID, long id, int WrkId) throws Exception {

	}
	
	@Override
	public void doAfterSync(String formname, SyncObj syncobj, String sql, int signum) {
		etdsyncfacade.insertDocstore(sql, etdsyncfacade.getWorkerWithorderNull(syncobj));
		etdsyncfacade.updateDSF(syncobj);
	}
	public ETDDocumentData EditBeforeOpen(String docName, ETDDocumentData doc) {
		// TODO Auto-generated method stub
		return doc;
	}
}

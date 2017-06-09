package sheff.rjd.services;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.oxm.Marshaller;

import com.aisa.portal.invoice.operator.obj.OperatorObject;

import ru.aisa.rgd.ws.dao.ServiceFacade;
import ru.aisa.rgd.ws.documents.ETDForm;
import ru.aisa.rgd.ws.documents.StandartResponseWrapper;
import ru.aisa.rgd.ws.domain.Document;
import ru.aisa.rgd.ws.endpoint.ETDAbstractEndpoint;
import ru.aisa.rgd.ws.endpoint.ResponseAdapter;
import ru.aisa.rgd.ws.exeption.ServiceError;
import ru.aisa.rgd.ws.exeption.ServiceException;
import ru.aisa.rgd.ws.utility.DataBinder;
import ru.aisa.rgd.ws.utility.DataRowMapper;
import ru.aisa.rgd.ws.utility.TypeConverter;
import ru.iit.portal.torg12.AddrIn;
import ru.iit.portal.torg12.AddrRf;
import ru.iit.portal.torg12.Table;
import ru.iit.portal.torg12.Torg12RequestDocument;
import ru.iit.portal.torg12.Torg12RequestDocument.Torg12Request;
import ru.iit.portal.torg12.Torg12RequestDocument.Torg12Request.BuyerInf;
import ru.iit.portal.torg12.Torg12ResponseDocument;
import ru.iit.portal.torg12.Torg12ResponseDocument.Torg12Response;
import ru.iit.portal.torg12.Varantyinfo;

public class TORG12Endpoint extends ETDAbstractEndpoint<StandartResponseWrapper> {

	private NamedParameterJdbcTemplate npjt;
	private String formname;
	private OperatorObject oper;

	public NamedParameterJdbcTemplate getNpjt() {
		return npjt;
	}

	public void setNpjt(NamedParameterJdbcTemplate npjt) {
		this.npjt = npjt;
	}

	public String getFormname() {
		return formname;
	}

	public void setFormname(String formname) {
		this.formname = formname;
	}

	public OperatorObject getOper() {
		return oper;
	}

	public void setOper(OperatorObject oper) {
		this.oper = oper;
	}

	protected TORG12Endpoint(Marshaller marshaller) {
		super(marshaller);
		// TODO Auto-generated constructor stub
	}

	public static HashMap<String, String> months = new HashMap<String, String>();
	static {
		months.put("01", "января");
		months.put("02", "февраля");
		months.put("03", "марта");
		months.put("04", "апреля");
		months.put("05", "мая");
		months.put("06", "июня");
		months.put("07", "июля");
		months.put("08", "августа");
		months.put("09", "сентября");
		months.put("10", "октября");
		months.put("11", "ноября");
		months.put("12", "декабря");
	}

	@Override
	protected ResponseAdapter<StandartResponseWrapper> processRequest(Object arg, ServiceFacade facade)
			throws Exception {
		Torg12RequestDocument requestdoc = (Torg12RequestDocument) arg;
		Torg12Request req = requestdoc.getTorg12Request();

		ETDForm form = null;
		Document document = null;
		DataBinder binder;
		Long docid = null;

		HashMap<String, Object> hm = new HashMap<String, Object>();
		hm.put("actid", req.getTmcid());

		int count = getNpjt().queryForInt("select count(0) from snt.docstore where etdid = :actid", hm);

		if (count == 0) {
			throw new ServiceException(new Exception(), ServiceError.ERR_WRONG_DOCID);
		}

		int dotorg = getNpjt().queryForInt(
				"select case when (select signlvl from snt.docstore where etdid = :actid) is null then 1 else 0 end from snt.docstore where etdid = :actid",
				hm);

		if (dotorg == 0)
			throw new ServiceException(new Exception(), ServiceError.ERR_ACTTMC_NOTARCHIVE);

		try {
			document = new Document();
			form = new ETDForm(facade.getDocumentTemplate(formname));
			binder = form.getBinder();
			binder.setRootElement("data");

			binder.setNodeValue("flag_ASU", 1);

			// Gruzotpr
			// binder.setNodeValue("P_1a",
			// req.getGruzOtprInf().getGruzOtprName());
			binder.setNodeValue("P_1", req.getGruzOtprInf().getGruzOtprName());
			if (req.getGruzOtprInf().isSetGruzOtprTel())
				binder.setNodeValue("P_1b", req.getGruzOtprInf().getGruzOtprTel());
			if (req.getGruzOtprInf().isSetGruzOtprFax())
				binder.setNodeValue("P_1v", req.getGruzOtprInf().getGruzOtprFax());
			if (req.getGruzOtprInf().isSetGruzOtprBankname())
				binder.setNodeValue("P_1d", req.getGruzOtprInf().getGruzOtprBankname());
			if (req.getGruzOtprInf().isSetGruzOtprSchet())
				binder.setNodeValue("P_1g", req.getGruzOtprInf().getGruzOtprSchet());
			if (req.getGruzOtprInf().isSetGruzOtprKorschet())
				binder.setNodeValue("P_1j", req.getGruzOtprInf().getGruzOtprKorschet());
			if (req.getGruzOtprInf().isSetGruzOtprBik())
				binder.setNodeValue("P_1e", req.getGruzOtprInf().getGruzOtprBik());
			if (req.getGruzOtprInf().isSetGruzOtprInn())
				binder.setNodeValue("P_1z", req.getGruzOtprInf().getGruzOtprInn());
			if (req.getGruzOtprInf().isSetGruzOtprKpp())
				binder.setNodeValue("P_1i", req.getGruzOtprInf().getGruzOtprKpp());
			if (req.getGruzOtprInf().isSetGruzOtprOkpo())
				binder.setNodeValue("P_2", req.getGruzOtprInf().getGruzOtprOkpo());
			if (req.getGruzOtprInf().isSetGruzOtprOkdp())
				binder.setNodeValue("P_3", req.getGruzOtprInf().getGruzOtprOkdp());

			if (req.getGruzOtprInf().getGruzOtprAddress().isSetARf()) {
				ArrayList<AddrRf> l1 = new ArrayList<AddrRf>();
				l1.add(req.getGruzOtprInf().getGruzOtprAddress().getARf());
				binder.fillTable(l1, new DataRowMapper<AddrRf>() {

					public Map<String, Object> mapRow(AddrRf row) {
						Map<String, Object> map = new HashMap<String, Object>();
						map.put("kod", row.getKod());
						map.put("ind", row.getInd());
						map.put("raion", row.getRaion());
						map.put("punkt", row.getPunkt());
						map.put("town", row.getTown());
						map.put("street", row.getStreet());
						map.put("house", row.getHouse());
						map.put("korp", row.getKorp());
						map.put("flat", row.getFlat());
						return map;
					}

				}, "GruzOtprAddr", "row");

				StringBuffer sb = new StringBuffer();
				sb.append("Россия, ");
				if (req.getGruzOtprInf().getGruzOtprAddress().getARf().isSetInd()) {
					sb.append(req.getGruzOtprInf().getGruzOtprAddress().getARf().getInd());
					sb.append(", ");
				}
				// if
				// (req.getGruzOtprInf().getGruzOtprAddress().getARf().isSetKod()){
				// sb.append(req.getGruzOtprInf().getGruzOtprAddress().getARf().getKod());
				// sb.append(", ");
				// }

				if (req.getGruzOtprInf().getGruzOtprAddress().getARf().isSetRaion()) {
					sb.append(req.getGruzOtprInf().getGruzOtprAddress().getARf().getRaion());
					sb.append(", ");
				}
				if (req.getGruzOtprInf().getGruzOtprAddress().getARf().isSetPunkt()) {
					sb.append(req.getGruzOtprInf().getGruzOtprAddress().getARf().getPunkt());
					sb.append(", ");
				}
				if (req.getGruzOtprInf().getGruzOtprAddress().getARf().isSetTown()) {
					sb.append(req.getGruzOtprInf().getGruzOtprAddress().getARf().getTown());
					sb.append(", ");
				}
				if (req.getGruzOtprInf().getGruzOtprAddress().getARf().isSetStreet()) {
					sb.append(req.getGruzOtprInf().getGruzOtprAddress().getARf().getStreet());
					sb.append(", ");
				}
				if (req.getGruzOtprInf().getGruzOtprAddress().getARf().isSetHouse()) {
					sb.append(req.getGruzOtprInf().getGruzOtprAddress().getARf().getHouse());
					sb.append(", ");
				}
				if (req.getGruzOtprInf().getGruzOtprAddress().getARf().isSetKorp()) {
					sb.append(req.getGruzOtprInf().getGruzOtprAddress().getARf().getKorp());
					sb.append(", ");
				}
				if (req.getGruzOtprInf().getGruzOtprAddress().getARf().isSetFlat()) {
					sb.append(req.getGruzOtprInf().getGruzOtprAddress().getARf().getFlat());
					sb.append(", ");
				}

				// binder.setNodeValue(
				// "P_1b",
				// sb.toString().length() > 1
				// && sb.toString().charAt(
				// sb.toString().length() - 2) == ',' ? sb
				// .toString().substring(0,
				// sb.toString().length() - 2) : sb
				// .toString());
				binder.setNodeValue("P_1a",
						sb.toString().length() > 1 && sb.toString().charAt(sb.toString().length() - 2) == ','
								? sb.toString().substring(0, sb.toString().length() - 2) : sb.toString());
			} else {
				ArrayList<AddrIn> l1 = new ArrayList<AddrIn>();
				l1.add(req.getGruzOtprInf().getGruzOtprAddress().getAIn());
				binder.fillTable(l1, new DataRowMapper<AddrIn>() {

					public Map<String, Object> mapRow(AddrIn row) {
						Map<String, Object> map = new HashMap<String, Object>();

						map.put("kod", row.getKod());
						map.put("text", row.getText());

						return map;
					}

				}, "GruzOtprAddrIn", "row");

			}

			// Gruzopoluch
			binder.setNodeValue("P_4", req.getGruzPolInf().getGruzPolName());
			if (req.getGruzPolInf().isSetGruzPolTel())
				binder.setNodeValue("P_4b", req.getGruzPolInf().getGruzPolTel());

			if (req.getGruzPolInf().isSetGruzPolFax())
				binder.setNodeValue("P_4v", req.getGruzPolInf().getGruzPolFax());

			if(req.getGruzPolInf().isSetGruzPolOkpo())
				binder.setNodeValue("P_4_1a", req.getGruzPolInf().getGruzPolOkpo());
			
			if (req.getGruzPolInf().getGruzPolAddress().isSetARf()) {
				ArrayList<AddrRf> l1 = new ArrayList<AddrRf>();
				l1.add(req.getGruzPolInf().getGruzPolAddress().getARf());
				binder.fillTable(l1, new DataRowMapper<AddrRf>() {

					public Map<String, Object> mapRow(AddrRf row) {
						Map<String, Object> map = new HashMap<String, Object>();

						map.put("kod", row.getKod());
						map.put("ind", row.getInd());
						map.put("raion", row.getRaion());
						map.put("punkt", row.getPunkt());
						map.put("town", row.getTown());
						map.put("street", row.getStreet());
						map.put("house", row.getHouse());
						map.put("korp", row.getKorp());
						map.put("flat", row.getFlat());

						return map;
					}

				}, "GruzPolAddr", "row");

				StringBuffer sb = new StringBuffer();

				sb.append("Россия, ");

				if (req.getGruzPolInf().getGruzPolAddress().getARf().isSetInd()) {
					sb.append(req.getGruzPolInf().getGruzPolAddress().getARf().getInd());
					sb.append(", ");
				}
				// if
				// (req.getGruzPolInf().getGruzPolAddress().getARf().isSetKod()){
				// sb.append(req.getGruzPolInf().getGruzPolAddress().getARf().getKod());
				// sb.append(", ");
				// }

				if (req.getGruzPolInf().getGruzPolAddress().getARf().isSetRaion()) {
					sb.append(req.getGruzPolInf().getGruzPolAddress().getARf().getRaion());
					sb.append(", ");
				}
				if (req.getGruzPolInf().getGruzPolAddress().getARf().isSetPunkt()) {
					sb.append(req.getGruzPolInf().getGruzPolAddress().getARf().getPunkt());
					sb.append(", ");
				}
				if (req.getGruzPolInf().getGruzPolAddress().getARf().isSetTown()) {
					sb.append(req.getGruzPolInf().getGruzPolAddress().getARf().getTown());
					sb.append(", ");
				}
				if (req.getGruzPolInf().getGruzPolAddress().getARf().isSetStreet()) {
					sb.append(req.getGruzPolInf().getGruzPolAddress().getARf().getStreet());
					sb.append(", ");
				}
				if (req.getGruzPolInf().getGruzPolAddress().getARf().isSetHouse()) {
					sb.append(req.getGruzPolInf().getGruzPolAddress().getARf().getHouse());
					sb.append(", ");
				}
				if (req.getGruzPolInf().getGruzPolAddress().getARf().isSetKorp()) {
					sb.append(req.getGruzPolInf().getGruzPolAddress().getARf().getKorp());
					sb.append(", ");
				}
				if (req.getGruzPolInf().getGruzPolAddress().getARf().isSetFlat()) {
					sb.append(req.getGruzPolInf().getGruzPolAddress().getARf().getFlat());
					sb.append(", ");
				}

				binder.setNodeValue("P_4a",
						sb.toString().length() > 1 && sb.toString().charAt(sb.toString().length() - 2) == ','
								? sb.toString().substring(0, sb.toString().length() - 2) : sb.toString());

			} else {
				ArrayList<AddrIn> l1 = new ArrayList<AddrIn>();
				l1.add(req.getGruzPolInf().getGruzPolAddress().getAIn());
				binder.fillTable(l1, new DataRowMapper<AddrIn>() {

					public Map<String, Object> mapRow(AddrIn row) {
						Map<String, Object> map = new HashMap<String, Object>();

						map.put("kod", row.getKod());
						map.put("text", row.getText());

						return map;
					}

				}, "GruzPolAddrIn", "row");

			}

			// Post
			binder.setNodeValue("P_5", req.getPostInf().getPostName());
			binder.setNodeValue("P_5z", req.getPostInf().getPostInn());
			binder.setNodeValue("P_5i", req.getPostInf().getPostKpp());
			if (req.getPostInf().isSetPostTel())
				binder.setNodeValue("P_5b", req.getPostInf().getPostTel());

			if (req.getPostInf().isSetPostFax())
				binder.setNodeValue("P_5v", req.getPostInf().getPostFax());

			binder.setNodeValue("P_5g", req.getPostInf().getPostSchet());
			binder.setNodeValue("P_5d", req.getPostInf().getPostBankname());
			binder.setNodeValue("P_5e", req.getPostInf().getPostBik());
			binder.setNodeValue("P_5j", req.getPostInf().getPostKorschet());
			binder.setNodeValue("P_5_1a", req.getPostInf().getPostOkpo());

			if (req.getPostInf().getPostAddress().isSetARf()) {
				ArrayList<AddrRf> l1 = new ArrayList<AddrRf>();
				l1.add(req.getPostInf().getPostAddress().getARf());
				binder.fillTable(l1, new DataRowMapper<AddrRf>() {

					public Map<String, Object> mapRow(AddrRf row) {
						Map<String, Object> map = new HashMap<String, Object>();

						map.put("kod", row.getKod());
						map.put("ind", row.getInd());
						map.put("raion", row.getRaion());
						map.put("punkt", row.getPunkt());
						map.put("town", row.getTown());
						map.put("street", row.getStreet());
						map.put("house", row.getHouse());
						map.put("korp", row.getKorp());
						map.put("flat", row.getFlat());

						return map;
					}

				}, "PostAddr", "row");

				StringBuffer sb = new StringBuffer();
				sb.append("Россия, ");
				if (req.getPostInf().getPostAddress().getARf().isSetInd()) {
					sb.append(req.getPostInf().getPostAddress().getARf().getInd());
					sb.append(", ");
				}
				// if (req.getPostInf().getPostAddress().getARf().isSetKod()){
				// sb.append(req.getPostInf().getPostAddress().getARf().getKod());
				// sb.append(", ");
				// }

				if (req.getPostInf().getPostAddress().getARf().isSetRaion()) {
					sb.append(req.getPostInf().getPostAddress().getARf().getRaion());
					sb.append(", ");
				}
				if (req.getPostInf().getPostAddress().getARf().isSetPunkt()) {
					sb.append(req.getPostInf().getPostAddress().getARf().getPunkt());
					sb.append(", ");
				}
				if (req.getPostInf().getPostAddress().getARf().isSetTown()) {
					sb.append(req.getPostInf().getPostAddress().getARf().getTown());
					sb.append(", ");
				}
				if (req.getPostInf().getPostAddress().getARf().isSetStreet()) {
					sb.append(req.getPostInf().getPostAddress().getARf().getStreet());
					sb.append(", ");
				}
				if (req.getPostInf().getPostAddress().getARf().isSetHouse()) {
					sb.append(req.getPostInf().getPostAddress().getARf().getHouse());
					sb.append(", ");
				}
				if (req.getPostInf().getPostAddress().getARf().isSetKorp()) {
					sb.append(req.getPostInf().getPostAddress().getARf().getKorp());
					sb.append(", ");
				}
				if (req.getPostInf().getPostAddress().getARf().isSetFlat()) {
					sb.append(req.getPostInf().getPostAddress().getARf().getFlat());
					sb.append(", ");
				}

				binder.setNodeValue("P_5a",
						sb.toString().length() > 1 && sb.toString().charAt(sb.toString().length() - 2) == ','
								? sb.toString().substring(0, sb.toString().length() - 2) : sb.toString());
			} else {
				ArrayList<AddrIn> l1 = new ArrayList<AddrIn>();
				l1.add(req.getPostInf().getPostAddress().getAIn());
				binder.fillTable(l1, new DataRowMapper<AddrIn>() {

					public Map<String, Object> mapRow(AddrIn row) {
						Map<String, Object> map = new HashMap<String, Object>();

						map.put("kod", row.getKod());
						map.put("text", row.getText());

						return map;
					}

				}, "PostAddrIn", "row");

			}

			// Buyer
			binder.setNodeValue("P_6", req.getBuyerInf().getBuyerName());
			// binder.setNodeValue("P_6a", req.getBuyerInf().getBuyerInn());
			binder.setNodeValue("P_6v", req.getBuyerInf().getBuyerInn());
			binder.setNodeValue("P_6b", req.getBuyerInf().getBuyerKpp());
			binder.setNodeValue("P_6g", req.getBuyerInf().getBuyerSchet());
			binder.setNodeValue("P_6d", req.getBuyerInf().getBuyerBankname());
			binder.setNodeValue("P_6e", req.getBuyerInf().getBuyerBik());
			binder.setNodeValue("P_6j", req.getBuyerInf().getBuyerKorschet());
			binder.setNodeValue("P_6_1a", req.getBuyerInf().getBuyerOkpo());

			if (req.getBuyerInf().getBuyerAddress().isSetARf()) {
				ArrayList<AddrRf> l1 = new ArrayList<AddrRf>();
				l1.add(req.getBuyerInf().getBuyerAddress().getARf());
				binder.fillTable(l1, new DataRowMapper<AddrRf>() {

					public Map<String, Object> mapRow(AddrRf row) {
						Map<String, Object> map = new HashMap<String, Object>();

						map.put("kod", row.getKod());
						map.put("ind", row.getInd());
						map.put("raion", row.getRaion());
						map.put("punkt", row.getPunkt());
						map.put("town", row.getTown());
						map.put("street", row.getStreet());
						map.put("house", row.getHouse());
						map.put("korp", row.getKorp());
						map.put("flat", row.getFlat());

						return map;
					}

				}, "BuyerAddr", "row");
				StringBuffer sb = new StringBuffer();
				sb.append("Россия, ");
				if (req.getBuyerInf().getBuyerAddress().getARf().isSetInd()) {
					sb.append(req.getBuyerInf().getBuyerAddress().getARf().getInd());
					sb.append(", ");
				}
				// if (req.getBuyerInf().getBuyerAddress().getARf().isSetKod()){
				// sb.append(req.getBuyerInf().getBuyerAddress().getARf().getKod());
				// sb.append(", ");
				// }
				if (req.getBuyerInf().getBuyerAddress().getARf().isSetRaion()) {
					sb.append(req.getBuyerInf().getBuyerAddress().getARf().getRaion());
					sb.append(", ");
				}
				if (req.getBuyerInf().getBuyerAddress().getARf().isSetPunkt()) {
					sb.append(req.getBuyerInf().getBuyerAddress().getARf().getPunkt());
					sb.append(", ");
				}
				if (req.getBuyerInf().getBuyerAddress().getARf().isSetTown()) {
					sb.append(req.getBuyerInf().getBuyerAddress().getARf().getTown());
					sb.append(", ");
				}
				if (req.getBuyerInf().getBuyerAddress().getARf().isSetStreet()) {
					sb.append(req.getBuyerInf().getBuyerAddress().getARf().getStreet());
					sb.append(", ");
				}
				if (req.getBuyerInf().getBuyerAddress().getARf().isSetHouse()) {
					sb.append(req.getBuyerInf().getBuyerAddress().getARf().getHouse());
					sb.append(", ");
				}
				if (req.getBuyerInf().getBuyerAddress().getARf().isSetKorp()) {
					sb.append(req.getBuyerInf().getBuyerAddress().getARf().getKorp());
					sb.append(", ");
				}
				if (req.getBuyerInf().getBuyerAddress().getARf().isSetFlat()) {
					sb.append(req.getBuyerInf().getBuyerAddress().getARf().getFlat());
					sb.append(", ");
				}

				binder.setNodeValue("P_6a",
						sb.toString().length() > 1 && sb.toString().charAt(sb.toString().length() - 2) == ','
								? sb.toString().substring(0, sb.toString().length() - 2) : sb.toString());

			} else {
				ArrayList<AddrIn> l1 = new ArrayList<AddrIn>();
				l1.add(req.getBuyerInf().getBuyerAddress().getAIn());
				binder.fillTable(l1, new DataRowMapper<AddrIn>() {

					public Map<String, Object> mapRow(AddrIn row) {
						Map<String, Object> map = new HashMap<String, Object>();

						map.put("kod", row.getKod());
						map.put("text", row.getText());

						return map;
					}

				}, "BuyerAddrIn", "row");

			}

			binder.setNodeValue("P_7", req.getReason());
			binder.setNodeValue("P_8", req.getReasonnumber());
			binder.setNodeValue("P_9", req.getReasondate());
			binder.setNodeValue("P_13", req.getDocnumber());
			binder.setNodeValue("P_14", req.getDocdate());

			// Заполнение таблицы ТН
			binder.fillTable(TypeConverter.arrayToArrayList(req.getTorgTabArray()), new DataRowMapper<Table>() {

				public Map<String, Object> mapRow(Table row) {
					Map<String, Object> map = new HashMap<String, Object>();

					map.put("P_15", row.getNumber());
					map.put("P_16", row.getGoods());
					map.put("P_16.1", row.getGoodsname());
					map.put("P_16.2", row.getGoodschar());
					if (row.isSetGoodscode())
						map.put("P_17", row.getGoodscode());
					map.put("P_18", row.getEizmname());
					map.put("P_19", row.getEizmcode());
					map.put("P_24", row.getKolvo());
					map.put("P_25", row.getEdprice());
					map.put("P_26", row.getNonends());
					map.put("P_27", row.getNds());
					map.put("P_28", row.getNdssumm());
					map.put("P_29", row.getWithnds());
					return map;
				}

			}, "table1", "row");

			// Itogo
			// binder.setNodeValue("P_30", req.getItogotab().getItogoKolvo());
			// binder.setNodeValue("P_31", req.getItogotab().getItogoMass());
			binder.setNodeValue("P_32", req.getItogotab().getItogoKolvomass());
			binder.setNodeValue("P_33", req.getItogotab().getItogoBeznds());
			binder.setNodeValue("P_34", req.getItogotab().getNdssumm());
			binder.setNodeValue("P_35", req.getItogotab().getWithnds());

			// Vsego

			// binder.setNodeValue("P_36", req.getVsegotab().getKolvoall());
			// binder.setNodeValue("P_37", req.getVsegotab().getMassall());
			binder.setNodeValue("P_38", req.getVsegotab().getKolvomassall());
			binder.setNodeValue("P_39", req.getVsegotab().getBezndsall());
			binder.setNodeValue("P_40", req.getVsegotab().getNdssummall());
			binder.setNodeValue("P_41", req.getVsegotab().getWithndsall());

			binder.setNodeValue("P_44", req.getDatesell());

			try {
				String[] datebuy = req.getDatesell().toString().split("\\.");

				binder.setNodeValue("P_44d", datebuy[0]);
				binder.setNodeValue("P_44m", months.get(datebuy[1]));
				binder.setNodeValue("P_44ms", months.get(datebuy[1]));
				binder.setNodeValue("P_44y", datebuy[2]);
			} catch (Exception e) {
				e.printStackTrace();
			}

			binder.setNodeValue("P_46", req.getNumberwrite());
			binder.setNodeValue("P_51", req.getVsegootpwrite());
			binder.setNodeValue("P_51_1", req.getVsegootp());
			binder.setNodeValue("P_51_2", req.getVsegootpkop());

			// Varanty

			// if (req.isSetVarantyinfo()) {
			//
			// if (req.getVarantyinfo().isSetVarantyNumber()) {
			// binder.setNodeValue("P_53a", req.getVarantyinfo()
			// .getVarantyNumber());
			// binder.setNodeValue("varanty_number", req.getVarantyinfo()
			// .getVarantyNumber());
			// }
			// if (req.getVarantyinfo().isSetVarantyDate()) {
			// binder.setNodeValue("P_53b",
			// req.getVarantyinfo().getVarantyDate());
			// try {
			// String[] varantydate = req.getVarantyinfo()
			// .getVarantyDate().toString().split("\\.");
			// binder.setNodeValue("P_53bd", varantydate[0]);
			// binder.setNodeValue("P_53bm",
			// months.get(varantydate[1]));
			// binder.setNodeValue("P_53by", varantydate[2]);
			// } catch (Exception e) {
			// e.printStackTrace();
			// }
			// binder.setNodeValue("varanty_date", req.getVarantyinfo()
			// .getVarantyDate());
			// }
			//
			// if (req.getVarantyinfo().isSetGivenby()) {
			// StringBuffer varantyby = new StringBuffer();
			// if (req.getVarantyinfo().getGivenby().isSetOrgname()) {
			// binder.setNodeValue("orgname", req.getVarantyinfo()
			// .getGivenby().getOrgname());
			// varantyby.append(req.getVarantyinfo().getGivenby()
			// .getOrgname());
			// varantyby.append(", ");
			//
			// }
			// if (req.getVarantyinfo().getGivenby().isSetDopsvby()) {
			// binder.setNodeValue("dopsvby", req.getVarantyinfo()
			// .getGivenby().getDopsvby());
			// varantyby.append(req.getVarantyinfo().getGivenby()
			// .getDopsvby());
			// varantyby.append(", ");
			// }
			//
			// if (req.getVarantyinfo().getGivenby().isSetDoljby()) {
			// binder.setNodeValue("doljby", req.getVarantyinfo()
			// .getGivenby().getDoljby());
			// varantyby.append(req.getVarantyinfo().getGivenby()
			// .getDoljby());
			// varantyby.append(", ");
			// }
			//
			// if (req.getVarantyinfo().getGivenby().isSetFioby()) {
			// StringBuffer fioby = new StringBuffer();
			//
			// varantyby.append(req.getVarantyinfo().getGivenby()
			// .getFioby().getFname());
			// varantyby.append(" ");
			// varantyby.append(req.getVarantyinfo().getGivenby()
			// .getFioby().getName());
			//
			// fioby.append(req.getVarantyinfo().getGivenby()
			// .getFioby().getFname()
			// + " "
			// + req.getVarantyinfo().getGivenby().getFioby()
			// .getName());
			//
			// if (req.getVarantyinfo().getGivenby().getFioby()
			// .isSetSname()) {
			// varantyby.append(" ");
			// varantyby.append(req.getVarantyinfo().getGivenby()
			// .getFioby().getSname());
			// fioby.append(" "
			// + req.getVarantyinfo().getGivenby()
			// .getFioby().getSname());
			// }
			//
			// binder.setNodeValue("fioby", fioby.toString());
			//
			// }
			//
			// binder.setNodeValue("P_53v", varantyby.toString());
			//
			// }
			//
			// if (req.getVarantyinfo().isSetGivento()) {
			// StringBuffer varantyto = new StringBuffer();
			// if (req.getVarantyinfo().getGivento().isSetDopsvto()) {
			// varantyto.append(req.getVarantyinfo().getGivento()
			// .getDopsvto());
			// varantyto.append(", ");
			//
			// binder.setNodeValue("orgname", req.getVarantyinfo()
			// .getGivenby().getOrgname());
			// }
			// if (req.getVarantyinfo().getGivento().isSetDoljto()) {
			// varantyto.append(req.getVarantyinfo().getGivento()
			// .getDoljto());
			// varantyto.append(", ");
			// }
			//
			// if (req.getVarantyinfo().getGivento().isSetFioto()) {
			// StringBuffer fioto = new StringBuffer();
			//
			// varantyto.append(req.getVarantyinfo().getGivento()
			// .getFioto().getFname());
			// varantyto.append(" ");
			// varantyto.append(req.getVarantyinfo().getGivento()
			// .getFioto().getName());
			// fioto.append(req.getVarantyinfo().getGivento()
			// .getFioto().getFname()
			// + " "
			// + req.getVarantyinfo().getGivento().getFioto()
			// .getName());
			// if (req.getVarantyinfo().getGivento().getFioto()
			// .isSetSname()) {
			// varantyto.append(" ");
			// varantyto.append(req.getVarantyinfo().getGivento()
			// .getFioto().getSname());
			// fioto.append(" "
			// + req.getVarantyinfo().getGivento()
			// .getFioto().getSname());
			// }
			//
			// binder.setNodeValue("fioto", fioto.toString());
			//
			// }
			//
			// binder.setNodeValue("P_53g", varantyto.toString());
			//
			// }
			//
			// }
			//
			// binder.setNodeValue("P_56", req.getDatebuy());
			//
			// try {
			// String[] datebuy = req.getDatebuy().toString().split("\\.");
			// binder.setNodeValue("P_56d", datebuy[0]);
			// binder.setNodeValue("P_56m", months.get(datebuy[1]));
			// binder.setNodeValue("P_56ms", months.get(datebuy[1]));
			// binder.setNodeValue("P_56y", datebuy[2]);
			// } catch (Exception e) {
			// e.printStackTrace();
			// }

			binder.setNodeValue("P_56", req.getDocdate());

			binder.setNodeValue("P_58", req.getVagonnumber());
			binder.setNodeValue("P_59", req.getKleim());
			binder.setNodeValue("P_69", req.getTmcid());

			docid = facade.getNextDocumentId();
			try {
				binder.setNodeValue("documentId", docid);
			} catch (Exception e) {
				log.error(TypeConverter.exceptionToString(e));
			}
			Map<String, Object> map = new HashMap<String, Object>();
			try {
				map.put("innsell", binder.getValue("P_5z"));
				map.put("kppsell", binder.getValue("P_5i"));
				map.put("buyname", "ОАО «РЖД»");
				String cabinetIdSell = (String) getNpjt().queryForObject(
						"Select contr_code from snt.pred where inn = :innsell and kpp =:kppsell and headid is null",
						map, String.class);
				String cabinetIdRecv = (String) getNpjt().queryForObject(
						"Select contr_code from snt.pred where name=:buyname and type = 1", map, String.class);

				binder.setNodeValue("cabinetIdSell", cabinetIdSell);
				binder.setNodeValue("cabinetIdRecv", cabinetIdRecv);
				binder.setNodeValue("nameoper", oper.getNameUrLic());
				binder.setNodeValue("innoper", oper.getInn());
				binder.setNodeValue("idoper", oper.getId());
			} catch (Exception e) {
				log.error(TypeConverter.exceptionToString(e));
			}

			map.put("etdid", req.getTmcid());

			int ent1 = 0;

			ent1 = getNpjt().queryForInt("select predid from snt.docstore where etdid = :etdid fetch first row only",
					map);

			document.setBlDoc(form.encodeToArchiv());
			document.setDocData(form.transform("data"));
			document.setSignLvl(0);
			document.setPredId(ent1);
			document.setType(formname);
			document.setId(docid);
			// docid = facade.insertDocument(document);
			facade.insertDocumentWithDocid(document);

			map.put("ID", docid);

			// System.out.println(document.getDocData());

			map.put("vagnum", binder.getValue("P_58"));
			// код услуги
			map.put("di", "08");
			map.put("dogovor", binder.getValue("P_8") + " от " + binder.getValue("P_9"));
			map.put("onbaseid", Integer.parseInt(binder.getValue("P_59")));
			map.put("repdate", binder.getValue("P_44"));
			map.put("opisanie", binder.getValue("P_13"));
			npjt.update(
					"update snt.docstore set readid = 1, di=:di, vagnum =:vagnum, no =:dogovor, onbaseid =:onbaseid, "
							+ "id_pak = (select id_pak from snt.docstore where etdid =:etdid), repdate = :repdate, opisanie=:opisanie where id =:ID ",
					map);

		} catch (Exception e) {
			StringWriter outError = new StringWriter();
			PrintWriter errorWriter = new PrintWriter(outError);
			e.printStackTrace(errorWriter);
			log.error(outError.toString());
			System.out.println(outError.toString());
			throw new ServiceException(e, -1, "Error occured " + e.getMessage());
		}

		StandartResponseWrapper wrapper = new StandartResponseWrapper();
		wrapper.setDescription("ok");
		wrapper.setCode(0);
		wrapper.setDocumentId(docid);
		ResponseAdapter<StandartResponseWrapper> adapter = new ResponseAdapter<StandartResponseWrapper>(true, wrapper,
				ServiceError.ERR_OK);
		return adapter;
	}

	@Override
	protected Object composeResponce(ResponseAdapter<StandartResponseWrapper> adapter) {
		Torg12ResponseDocument responsedoc = Torg12ResponseDocument.Factory.newInstance();
		Torg12Response response = responsedoc.addNewTorg12Response();

		if (adapter.isStatus()) {
			response.setDescription(adapter.getResponse().getDescription());
			response.setCode(adapter.getResponse().getCode());
			response.setDocid(adapter.getResponse().getDocumentId());
		} else {
			response.setCode(adapter.getError().getCode());
			response.setDescription(adapter.getError().getMessage());
			response.setDocid(-1);
		}

		return responsedoc;
	}

}

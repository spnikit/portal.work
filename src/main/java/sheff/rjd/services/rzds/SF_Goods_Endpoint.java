package sheff.rjd.services.rzds;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.oxm.Marshaller;

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
import ru.iit.act.rtk.FormCreateResponse;
import ru.iit.act.rtk.FormCreateResponseDocument;
import ru.iit.rzds.sfg.AddrIn;
import ru.iit.rzds.sfg.AddrRf;
import ru.iit.rzds.sfg.SFRequestDocument;
import ru.iit.rzds.sfg.SFRequestDocument.SFRequest;
import ru.iit.rzds.sfg.Table1;
import ru.iit.rzds.sfg.Table2;

import com.aisa.portal.invoice.integration.security.Manager;
import com.aisa.portal.invoice.operator.obj.OperatorObject;


public class SF_Goods_Endpoint extends ETDAbstractEndpoint<StandartResponseWrapper> {

	private static Logger log = Logger.getLogger(SF_Goods_Endpoint.class);

	private Manager securitymanager;
	   private ServiceFacade facade;
	   OperatorObject oper;
	   private NamedParameterJdbcTemplate npjt;
	   private String formname;
	 public Manager getSecuritymanager() {
		return securitymanager;
	}


	public void setSecuritymanager(Manager securitymanager) {
		this.securitymanager = securitymanager;
	}


	public ServiceFacade getFacade() {
		return facade;
	}


	public void setFacade(ServiceFacade facade) {
		this.facade = facade;
	}


	public OperatorObject getOper() {
		return oper;
	}


	public void setOper(OperatorObject oper) {
		this.oper = oper;
	}


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


	public SF_Goods_Endpoint(Marshaller marshaller) {
		super(marshaller);
	}

	
	@Override
	protected ResponseAdapter<StandartResponseWrapper> processRequest(Object arg,
			ServiceFacade facade) throws Exception {


		
		final SFRequestDocument requestDocument = (SFRequestDocument) arg;
		final SFRequest request = requestDocument.getSFRequest();
		ETDForm form = null;
		Document document = null;
		DataBinder binder;
		Long id = null;
		
		//Проверка на существование СФ РТК
		
		
		HashMap<String, Object> sfmap = new HashMap<String, Object>();
		sfmap.put("id_pak", request.getIdPak());
		sfmap.put("type", formname);
//		sfmap.put("actid", request.getActId());
		
				
		
		document = new Document();
		form = new ETDForm(facade.getDocumentTemplate(formname));
		binder = form.getBinder();
			
		binder.setRootElement("data");
			
		
			HashMap<String, Object> map = new HashMap<String, Object>();

			binder.setNodeValue("num_schet", request.getSFInf().getNumber());
			binder.setNodeValue("date_ot", request.getSFInf().getDate());
			if (request.isSetCorrInf())
				binder.setNodeValue("num_isprav", request.getCorrInf()
						.getCorrectNumber());
			if (request.isSetCorrInf())
				binder.setNodeValue("date_ot_isprav", request.getCorrInf()
						.getCorrectDate());
			binder.setNodeValue("prodavec", request.getSellInf().getSellerName());
			
			String addr = "";
			if (request.getSellInf().getSellerAddress().isSetARf())
				{
				if (request.getSellInf().getSellerAddress().getARf().isSetInd())
					addr += request.getSellInf().getSellerAddress().getARf().getInd();
				// else addr+="- , ";
				// addr+=request.getSellInf().getSellerAddress().getARf().getKod()+" , ";
				if (request.getSellInf().getSellerAddress().getARf().isSetRaion())
					addr += ", "
							+ request.getSellInf().getSellerAddress().getARf().getRaion();
				// else addr+="- , ";
				if (request.getSellInf().getSellerAddress().getARf().isSetPunkt())
					addr += ", "
							+ request.getSellInf().getSellerAddress().getARf().getPunkt();
				// else addr+="- , ";
				if (request.getSellInf().getSellerAddress().getARf().isSetTown())
					addr += ", "
							+ request.getSellInf().getSellerAddress().getARf().getTown();
				// else addr+="- , ";
				if (request.getSellInf().getSellerAddress().getARf().isSetStreet())
					addr += ", "
							+ request.getSellInf().getSellerAddress().getARf().getStreet();
				// else addr+="- , ";
				if (request.getSellInf().getSellerAddress().getARf().isSetHouse())
					addr += ", "
							+ request.getSellInf().getSellerAddress().getARf().getHouse();
				// else addr+="- , ";
				if (request.getSellInf().getSellerAddress().getARf().isSetKorp())
					addr += ", "
							+ request.getSellInf().getSellerAddress().getARf().getKorp();
				// else addr+="- , ";
				if (request.getSellInf().getSellerAddress().getARf().isSetFlat())
					addr += ", "
							+ request.getSellInf().getSellerAddress().getARf().getFlat();
				// else addr+="-";
				}
			else
				{
				// addr+=request.getSellInf().getSellerAddress().getAIn().getKod()+" , ";
				addr += request.getSellInf().getSellerAddress().getAIn().getText();
				}
			
			binder.setNodeValue(
					"adres_prodavca",
					addr.length() > 1 && addr.charAt(addr.length() - 1) == ',' ? addr
							.substring(0, addr.length() - 2) : addr);
			
			if (request.getSellInf().isSetSellerInnkpp())
			
				{
				binder.setNodeValue("inn_prodavca", request.getSellInf()
						.getSellerInnkpp());
				
				String[] innkppsell = request.getSellInf().getSellerInnkpp().split("/");
				String[] innkppbuy = request.getCustInf().getCustomerInnkpp().split("/");
				
				try
					{
					map.put("innsell", innkppsell[0]);
					map.put("kppsell", innkppsell[1]);
					map.put("innbuy", innkppbuy[0]);
					map.put("kppbuy", innkppbuy[1]);
					String cabinetId = (String) npjt.queryForObject(
							"Select contr_code from snt.pred where inn = :innsell and kpp =:kppsell and headid is null", map,
							String.class);
					String cabinetIdContr = (String) npjt.queryForObject(
									"Select contr_code from snt.pred where inn = :innbuy and kpp =:kppbuy and headid is null",
									map, String.class);
					
					
					
					binder.setNodeValue("cabinetIdSell", cabinetId);
					binder.setNodeValue("cabinetIdRecv", cabinetIdContr);
				
					binder.setNodeValue("nameoper", oper.getNameUrLic());
					binder.setNodeValue("innoper", oper.getInn());
					binder.setNodeValue("idoper", oper.getId());
					
					
					}
				catch (Exception e)
					{
					e.printStackTrace();
					log.error("No cabinet for contrcode = " + request.getOkpoCodeBuy()
							+ " found in db");
					
					}
				
				}
			
			if (request.isSetSender())
				{
				addr = "" + request.getSender().getName() + " , ";
				if (request.getSender().getAddress().isSetARf())
					{
					if (request.getSender().getAddress().getARf().isSetInd())
						addr += request.getSender().getAddress().getARf().getInd();
					// else addr+="- , ";
					// addr+=request.getSender().getAddress().getARf().getKod()+" , ";
					if (request.getSender().getAddress().getARf().isSetRaion())
						addr += ", " + request.getSender().getAddress().getARf().getRaion();
					// else addr+="- , ";
					if (request.getSender().getAddress().getARf().isSetPunkt())
						addr += ", " + request.getSender().getAddress().getARf().getPunkt();
					// else addr+="- , ";
					if (request.getSender().getAddress().getARf().isSetTown())
						addr += ", " + request.getSender().getAddress().getARf().getTown();
					// else addr+="- , ";
					if (request.getSender().getAddress().getARf().isSetStreet())
						addr += ", "
								+ request.getSender().getAddress().getARf().getStreet();
					// else addr+="- , ";
					if (request.getSender().getAddress().getARf().isSetHouse())
						addr += ", " + request.getSender().getAddress().getARf().getHouse();
					// else addr+="- , ";
					if (request.getSender().getAddress().getARf().isSetKorp())
						addr += ", " + request.getSender().getAddress().getARf().getKorp();
					// else addr+="- , ";
					if (request.getSender().getAddress().getARf().isSetFlat())
						addr += ", " + request.getSender().getAddress().getARf().getFlat();
					// else addr+="-";
					}
				else
					{
					// addr+=request.getSender().getAddress().getAIn().getKod()+" , ";
					addr += request.getSender().getAddress().getAIn().getText();
					}
				binder.setNodeValue(
						"gryzootprav",
						addr.length() > 1 && addr.charAt(addr.length() - 1) == ',' ? addr
								.substring(0, addr.length() - 2) : addr);
				}
			
			if (request.isSetReciever())
				{
				addr = "" + request.getReciever().getName() + " , ";
				if (request.getReciever().getAddress().isSetARf())
					{
					if (request.getReciever().getAddress().getARf().isSetInd())
						addr += request.getReciever().getAddress().getARf().getInd();
					// else addr+="- , ";
					// addr+=request.getReciever().getAddress().getARf().getKod()+" , ";
					if (request.getReciever().getAddress().getARf().isSetRaion())
						addr += ", "
								+ request.getReciever().getAddress().getARf().getRaion();
					// else addr+="- , ";
					if (request.getReciever().getAddress().getARf().isSetPunkt())
						addr += ", "
								+ request.getReciever().getAddress().getARf().getPunkt();
					// else addr+="- , ";
					if (request.getReciever().getAddress().getARf().isSetTown())
						addr += ", "
								+ request.getReciever().getAddress().getARf().getTown();
					// else addr+="- , ";
					if (request.getReciever().getAddress().getARf().isSetStreet())
						addr += ", "
								+ request.getReciever().getAddress().getARf().getStreet();
					// else addr+="- , ";
					if (request.getReciever().getAddress().getARf().isSetHouse())
						addr += ", "
								+ request.getReciever().getAddress().getARf().getHouse();
					// else addr+="- , ";
					if (request.getReciever().getAddress().getARf().isSetKorp())
						addr += ", "
								+ request.getReciever().getAddress().getARf().getKorp();
					// else addr+="- , ";
					if (request.getReciever().getAddress().getARf().isSetFlat())
						addr += ", "
								+ request.getReciever().getAddress().getARf().getFlat();
					// else addr+="-";
					}
				else
					{
					// addr+=request.getReciever().getAddress().getAIn().getKod()+" , ";
					addr += request.getReciever().getAddress().getAIn().getText();
					}
				binder.setNodeValue(
						"gryzopolych",
						addr.length() > 1 && addr.charAt(addr.length() - 1) == ',' ? addr
								.substring(0, addr.length() - 2) : addr);
				}
			binder.setNodeValue("pokypatel", request.getCustInf().getCustomerName());
			
			addr = "";
			if (request.getCustInf().getCustomerAddress().isSetARf())
				{
				if (request.getCustInf().getCustomerAddress().getARf().isSetInd())
					addr += request.getCustInf().getCustomerAddress().getARf().getInd();
				// else addr+="- , ";
				// addr+=request.getCustInf().getCustomerAddress().getARf().getKod()+" , ";
				if (request.getCustInf().getCustomerAddress().getARf().isSetRaion())
					addr += ", "
							+ request.getCustInf().getCustomerAddress().getARf().getRaion();
				// else addr+="- , ";
				if (request.getCustInf().getCustomerAddress().getARf().isSetPunkt())
					addr += ", "
							+ request.getCustInf().getCustomerAddress().getARf().getPunkt();
				// else addr+="- , ";
				if (request.getCustInf().getCustomerAddress().getARf().isSetTown())
					addr += ", "
							+ request.getCustInf().getCustomerAddress().getARf().getTown();
				// else addr+="- , ";
				if (request.getCustInf().getCustomerAddress().getARf().isSetStreet())
					addr += ", "
							+ request.getCustInf().getCustomerAddress().getARf().getStreet();
				// else addr+="- , ";
				if (request.getCustInf().getCustomerAddress().getARf().isSetHouse())
					addr += ", "
							+ request.getCustInf().getCustomerAddress().getARf().getHouse();
				// else addr+="- , ";
				if (request.getCustInf().getCustomerAddress().getARf().isSetKorp())
					addr += ", "
							+ request.getCustInf().getCustomerAddress().getARf().getKorp();
				// else addr+="- , ";
				if (request.getCustInf().getCustomerAddress().getARf().isSetFlat())
					addr += ", "
							+ request.getCustInf().getCustomerAddress().getARf().getFlat();
				// else addr+="-";
				}
			else
				{
				// addr+=request.getCustInf().getCustomerAddress().getAIn().getKod()+" , ";
				addr += request.getCustInf().getCustomerAddress().getAIn().getText();
				}
			binder.setNodeValue(
					"adres_pokypatel",
					addr.length() > 1 && addr.charAt(addr.length() - 1) == ',' ? addr
							.substring(0, addr.length() - 2) : addr);
			if (request.getCustInf().isSetCustomerInnkpp())
				binder.setNodeValue("inn_pokypatel", request.getCustInf()
						.getCustomerInnkpp());
			
			binder.setNodeValue("valyuta", request.getCurrency());
			if (request.isSetCurrencyName()){
			binder.setNodeValue("currency_name", request.getCurrencyName());
			}
				
			binder.setNodeValue("vsego_stoimost_bez_nalog", request.getItogoPrice());
			binder.setNodeValue("vsego_nalog", request.getItogoNalSumm());
			binder.setNodeValue("vsego_stoimost_s_nalog", request.getItogoPriceNal());
			
			if (request.getSellInf().getSellerAddress().isSetARf())
				{
				ArrayList<AddrRf> l1 = new ArrayList<AddrRf>();
				l1.add(request.getSellInf().getSellerAddress().getARf());
				binder.fillTable(l1, new DataRowMapper<AddrRf>()
					{
						
						public Map<String, Object> mapRow(AddrRf row)
							{
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
						
					}, "SellAddr", "row");
				}
			else
				{
				ArrayList<AddrIn> l1 = new ArrayList<AddrIn>();
				l1.add(request.getSellInf().getSellerAddress().getAIn());
				binder.fillTable(l1, new DataRowMapper<AddrIn>()
					{
						
						public Map<String, Object> mapRow(AddrIn row)
							{
							Map<String, Object> map = new HashMap<String, Object>();
							
							map.put("kod", row.getKod());
							map.put("text", row.getText());
							
							return map;
							}
						
					}, "SellAddrIn", "row");
				
				}
			
			if (request.getCustInf().getCustomerAddress().isSetARf())
				{
				ArrayList<AddrRf> l1 = new ArrayList<AddrRf>();
				l1.add(request.getCustInf().getCustomerAddress().getARf());
				binder.fillTable(l1, new DataRowMapper<AddrRf>()
					{
						
						public Map<String, Object> mapRow(AddrRf row)
							{
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
						
					}, "CustAddr", "row");
				}
			else
				{
				ArrayList<AddrIn> l1 = new ArrayList<AddrIn>();
				l1.add(request.getCustInf().getCustomerAddress().getAIn());
				binder.fillTable(l1, new DataRowMapper<AddrIn>()
					{
						
						public Map<String, Object> mapRow(AddrIn row)
							{
							Map<String, Object> map = new HashMap<String, Object>();
							
							map.put("kod", row.getKod());
							map.put("text", row.getText());
							
							return map;
							}
						
					}, "CustAddrIn", "row");
				
				}
			
			if (request.isSetSender())
				{
				if (request.getSender().getAddress().isSetARf())
					{
					ArrayList<AddrRf> l1 = new ArrayList<AddrRf>();
					l1.add(request.getSender().getAddress().getARf());
					binder.fillTable(l1, new DataRowMapper<AddrRf>()
						{
							
							public Map<String, Object> mapRow(AddrRf row)
								{
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
							
						}, "SendAddr", "row");
					}
				else
					{
					ArrayList<AddrIn> l1 = new ArrayList<AddrIn>();
					l1.add(request.getSender().getAddress().getAIn());
					binder.fillTable(l1, new DataRowMapper<AddrIn>()
						{
							
							public Map<String, Object> mapRow(AddrIn row)
								{
								Map<String, Object> map = new HashMap<String, Object>();
								
								map.put("kod", row.getKod());
								map.put("text", row.getText());
								
								return map;
								}
							
						}, "SendAddrIn", "row");
					
					}
				}
			
			if (request.isSetReciever())
				{
				if (request.getReciever().getAddress().isSetARf())
					{
					ArrayList<AddrRf> l1 = new ArrayList<AddrRf>();
					l1.add(request.getReciever().getAddress().getARf());
					binder.fillTable(l1, new DataRowMapper<AddrRf>()
						{
							
							public Map<String, Object> mapRow(AddrRf row)
								{
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
							
						}, "RecvAddr", "row");
					}
				else
					{
					ArrayList<AddrIn> l1 = new ArrayList<AddrIn>();
					l1.add(request.getReciever().getAddress().getAIn());
					binder.fillTable(l1, new DataRowMapper<AddrIn>()
						{
							
							public Map<String, Object> mapRow(AddrIn row)
								{
								Map<String, Object> map = new HashMap<String, Object>();
								
								map.put("kod", row.getKod());
								map.put("text", row.getText());
								
								return map;
								}
							
						}, "RecvAddrIn", "row");
					
					}
				}

			binder.fillTable(TypeConverter.arrayToArrayList(request.getTabArray()),
					new DataRowMapper<Table1>()
						{
							
							public Map<String, Object> mapRow(Table1 row)
								{
								Map<String, Object> map = new HashMap<String, Object>();
								
								map.put("name_tovar", row.getGoodsName());
								map.put("kod_ed_izmer", row.getUnitKod());
								map.put("us_obozn", row.getUnitSign());
								map.put("kolvo", row.getKol());
								map.put("cena", row.getUnitPrice());
								map.put("stoimoct_bez_nalog", row.getPrice());
								map.put("akciz", row.getExciseSumm());
								map.put("nalog_stavka", row.getNalRate());
								map.put("sum_nalog", row.getNalSumm());
								map.put("stoimoct_s_nalog", row.getPriceNal());
								map.put("kod_strana", row.getCountryKod());
								map.put("sname_strana", row.getCountryName());
								map.put("num_deklar", row.getDecNum());
								map.put("P_38", row.getType());
								
								float A = Float.parseFloat(row.getPrice().toString());
								float B = Float.parseFloat(row.getPriceNal().toString());
//								float C = Float.parseFloat(row.getNalSumm());
								float D = row.getNalRate().equalsIgnoreCase("без НДС") ? 0 : 
									row.getNalRate().contains("%") ? Integer.parseInt(row.getNalRate().replace("%", "")) : Integer.parseInt(row.getNalRate());
									float C = row.getNalSumm().equalsIgnoreCase("без НДС") ? 0 : Float.parseFloat(row.getNalSumm());
										
									float AD = A * (100 + D)/100;
									float AC = (A + C);
									
									AD = Float.parseFloat((new BigDecimal(AD).setScale(2, BigDecimal.ROUND_HALF_UP).toString()));
									AC = Float.parseFloat((new BigDecimal(AC).setScale(2, BigDecimal.ROUND_HALF_UP)).toString());

									float delta = Float.parseFloat("0.01");
																
									float ADdeltaup = Float.parseFloat((new BigDecimal(AD+delta).setScale(2, BigDecimal.ROUND_HALF_UP).toString()));
									float ADdeltadown = Float.parseFloat((new BigDecimal(AD-delta).setScale(2, BigDecimal.ROUND_HALF_UP).toString()));
																
									float ACdeltaup = Float.parseFloat((new BigDecimal(AC+delta).setScale(2, BigDecimal.ROUND_HALF_UP).toString()));
									float ACdeltadown = Float.parseFloat((new BigDecimal(AC-delta).setScale(2, BigDecimal.ROUND_HALF_UP).toString()));
									
									
								 if(AD != B&&ADdeltaup!=B&&ADdeltadown!=B&&AC!= B&&ACdeltaup!= B&&ACdeltadown!= B){
								 throw new ServiceException(new Exception(), new
								 ServiceError(
								 99, "Ошибка проверки НДС"));
								 }
								
								 else return map;
								 
								 
								}
							
						}, "tabel", "row");
			binder.fillTable(
					TypeConverter.arrayToArrayList(request.getDocTabArray()),
					new DataRowMapper<Table2>()
						{
							public Map<String, Object> mapRow(Table2 row)
								{
								Map<String, Object> map = new HashMap<String, Object>();
								
								map.put("k_doc_num", row.getDoc());
								map.put("k_dok_date", row.getDocDate());
								
								return map;
								}
							
						}, "table1", "row");
			
			
			binder.setNodeValue("P_39a", request.getDogpostnumber());
			binder.setNodeValue("P_39b", request.getDogpostdate());
			binder.setNodeValue("P_40", request.getDogpostspec());
			binder.setNodeValue("P_41", request.getDateotgr());
			binder.setNodeValue("P_42a", request.getActtorgnumber());
			binder.setNodeValue("P_42b", request.getActtorgdate());
			binder.setNodeValue("P_43", request.getZdinvoice());
			binder.setNodeValue("P_44", request.getVagons());
			
			int ent1;
			HashMap<String, Object> pp = new HashMap<String, Object>();
			pp.put("okpo_sell", request.getOkpoCodeSell());
			ent1 = facade.getNpjt().queryForInt("select id from snt.pred where okpo_kod=:okpo_sell", pp);
			
//			Long pfmcode = null;
			
//			System.out.println("pfm: "+request.getPfmcode());
			
			binder.setNodeValue("id_paketa", request.getIdPak());
//			binder.setNodeValue("idakta", request.getActId());
			binder.setNodeValue("primechanie", request.getPrim());
//			binder.setNodeValue("nomer_bileta", request.getTicketnumber());
//			binder.setNodeValue("pfm", pfmcode);
			
			binder.setNodeValue("package", request.getIdPak());
//			binder.setNodeValue("predId", pfmcode);
			
			
			document.setBlDoc(form.encodeToArchiv());
			document.setDocData(form.transform("data"));
			
			document.setPredId(ent1);
			
			document.setSignLvl(0);
			document.setType(formname);
//			Long id = facade.insertDocument(document);
			id = facade.getDocumentDao().getNextId();
			document.setId(id);
			facade.insertDocumentWithDocid(document);
			
		
			pp.put("docid", id);
			pp.put("packid", request.getIdPak());
			pp.put("content", request.getSFInf().getNumber());
			facade.getNpjt()
					.update("update snt.docstore set id_pak = :packid, opisanie =:content where id =:docid",
							pp);
			
			

		StandartResponseWrapper wrapper = new StandartResponseWrapper();
		wrapper.setDescription("ok");
		wrapper.setCode(0);
		wrapper.setDocumentId(id);
		ResponseAdapter<StandartResponseWrapper> adapter = new ResponseAdapter<StandartResponseWrapper>(
				true, wrapper, ServiceError.ERR_OK);
		return adapter;

	}

	@Override
	protected Object composeResponce(ResponseAdapter<StandartResponseWrapper> adapter) {
		FormCreateResponseDocument responsedoc = FormCreateResponseDocument.Factory
				.newInstance();
		FormCreateResponse response = responsedoc.addNewFormCreateResponse();

		if (adapter.isStatus()) {
			response.setCode(adapter.getResponse().getCode());
			response.setDescription(adapter.getResponse().getDescription());
			response.setDocid(adapter.getResponse().getDocumentId());
			
		} else {
			response.setCode(adapter.getError().getCode());
			response.setDescription(adapter.getError().getMessage());
			response.setDocid(Long.MIN_VALUE);
		}

		return responsedoc;
	}

	
}



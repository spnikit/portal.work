package sheff.rjd.services.act;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.xml.transform.TransformerException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.log4j.Logger;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.oxm.Marshaller;
import org.springframework.ws.client.core.WebServiceTemplate;
import org.w3c.dom.DOMException;

import ru.aisa.rgd.ws.dao.ServiceFacade;
import ru.aisa.rgd.ws.documents.ETDForm;
import ru.aisa.rgd.ws.documents.StandartResponseWrapper;
import ru.aisa.rgd.ws.domain.Document;
import ru.aisa.rgd.ws.endpoint.ETDAbstractEndpoint;
import ru.aisa.rgd.ws.endpoint.ResponseAdapter;
import ru.aisa.rgd.ws.exeption.InternalException;
import ru.aisa.rgd.ws.exeption.ServiceError;
import ru.aisa.rgd.ws.exeption.ServiceException;
import ru.aisa.rgd.ws.utility.DataBinder;
import ru.aisa.rgd.ws.utility.DataRowMapper;
import ru.aisa.rgd.ws.utility.RowFiller;
import ru.aisa.rgd.ws.utility.TypeConverter;
import ru.iit.act.rtk.FormCreateResponse;
import ru.iit.act.rtk.FormCreateResponseDocument;
import rzd8888.gvc.etd.was.etd.sfrtk.AddrIn;
import rzd8888.gvc.etd.was.etd.sfrtk.AddrRf;
import rzd8888.gvc.etd.was.etd.sfrtk.SFRequestDocument;
import rzd8888.gvc.etd.was.etd.sfrtk.SFRequestDocument.SFRequest;
import rzd8888.gvc.etd.was.etd.sfrtk.SFResponseDocument;
import rzd8888.gvc.etd.was.etd.sfrtk.Table1;
import rzd8888.gvc.etd.was.etd.sfrtk.Table2;

import com.aisa.portal.invoice.integration.security.Manager;
import com.aisa.portal.invoice.operator.obj.OperatorObject;


public class SF_RTK_New_Endpoint extends ETDAbstractEndpoint<StandartResponseWrapper> {

	private static Logger log = Logger.getLogger(SF_RTK_New_Endpoint.class);

	private Manager securitymanager;
	   private ServiceFacade facade;
	   OperatorObject oper;
	   private NamedParameterJdbcTemplate npjt;
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


	public SF_RTK_New_Endpoint(Marshaller marshaller) {
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
		sfmap.put("type", "Счет-фактура РТК");
		sfmap.put("acttype", "Акт РТК");
		sfmap.put("korracttype", "Корректировочный Акт РТК");
		sfmap.put("actid", request.getActId());
		sfmap.put("packtype", "Пакет документов РТК");
		
		int count = facade.getNpjt().queryForInt("select count(id) from snt.docstore where id_pak =:id_pak and typeid = (select id from snt.doctype where name =:type)", sfmap);
		
		if (count>0){
			
			throw new ServiceException(new Exception(), new
					 ServiceError(
					 -2, "В этом пакете уже есть Счет-фактура"));
					 }
		
		
		if (facade.getNpjt().queryForInt("select count(id) from snt.docstore where id =:actid and typeid in "
				+ "(select id from snt.doctype where (name =:acttype) or (name= :korracttype))", sfmap)==0){
			throw new ServiceException(new Exception(), new
					 ServiceError(
					 -2, "Не существует Акта РТК с таким ID документа"));
					 }
		
		
		if (facade.getNpjt().queryForInt("select count(id) from snt.docstore where id_pak =:id_pak and visible = 1 and typeid = (select id from snt.doctype where name =:packtype)", sfmap)==0){
			throw new ServiceException(new Exception(), new
					 ServiceError(
					 -2, "Не существует Пакета документов РТК с таким ID пакета"));
					 }
		
		
		
		
		document = new Document();
		form = new ETDForm(facade.getDocumentTemplate("Счет-фактура РТК"));
		binder = form.getBinder();
			
		binder.setRootElement("data");
			
		
			HashMap<String, Object> map = new HashMap<String, Object>();
//			
//			map.put("predkod", request.getPredKod());
//			
//			int ent1 = 0;
//			
//			ent1 = getNpjt()
//					.queryForInt(
//							
//							
//							"select id from snt.pred where okpo_kod = :predkod and headid is null",
//							map);
//			map.put("code", request.getPredKod());
//			map.put("contr_code", request.getOkpoCode());
//			
//			String p1 = null;
//			binder.setNodeValue("cntPred", (String) getNpjt().queryForObject("select vname from snt.pred where okpo_kod = :contr_code", map, String.class));
			
			
			
			
//			binder.setNodeValue("pole", "");
//			binder.setNodeValue("id_pak", request.getIdPak());
			binder.setNodeValue("num_schet", request.getSFInf().getNumber());
			binder.setNodeValue("date_ot", request.getSFInf().getDate());
			if (request.isSetCorrInf())
				binder.setNodeValue("num_isprav", request.getCorrInf()
						.getCorrectNumber());
			if (request.isSetCorrInf())
				binder.setNodeValue("date_ot_isprav", request.getCorrInf()
						.getCorrectDate());
			
//			if (request.isSetCorrInf())
//				binder.setNodeValue("date_ot_isprav", request.getCorrInf()
//						.getCorrectNumber());
//			if (request.isSetCorrInf())
//				binder.setNodeValue("num_isprav", request.getCorrInf()
//						.getCorrectDate());
			
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
					log.error("No cabinet for contrcode = " + request.getOkpoCode()
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
			if (request.isSetGo())
				binder.setNodeValue("go", request.getGo());
			if (request.isSetGp())
				binder.setNodeValue("gp", request.getGp());
			
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
								
//								map.put("name_tovar", row.getGoodsName()+" "+(row.getType()==1?"Авиабилет":"прочая услуга"));
								map.put("name_tovar", row.getGoodsName());
								map.put("pb", row.getType());
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
								
								
								float A = Float.parseFloat(row.getPrice().toString());
								float B = Float.parseFloat(row.getPriceNal().toString());
//								float C = Float.parseFloat(row.getNalSumm());
								float D = row.getNalRate().equalsIgnoreCase("без НДС") ? 0 : 
									row.getNalRate().contains("%") ? Integer.parseInt(row.getNalRate().replace("%", "")) : Integer.parseInt(row.getNalRate());
									float C = row.getNalSumm().equalsIgnoreCase("без НДС") ? 0 :row.getNalSumm().equalsIgnoreCase("-") ? 0 : Float.parseFloat(row.getNalSumm());
										
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
			binder.fillTable(request.getDocTabArray(), new RowFiller<Table2, Object>()
					{
						public void fillRow(DataBinder b, Table2 row, int numRow, Object options) throws DOMException, InternalException
						{
							b.setNodeValue("k_doc_num", row.getDoc());
							b.setNodeValue("k_dok_date", row.getDocDate());
						}
					},"table1","row");
			
			
			int ent1;
			HashMap<String, Object> pp = new HashMap<String, Object>();
			pp.put("actid", request.getActId());
			ent1 = facade.getNpjt().queryForInt("select predid from snt.docstore where id=:actid", pp);
			map.put("pfmcode", request.getPfmcode());
			Long pfmcode = null;
			
//			System.out.println("pfm: "+request.getPfmcode());
			try {
				Integer code = facade.getNpjt().queryForInt("select etdid from snt.pfmcodes "
						+ " where pfmcode = :pfmcode", map);
				if (Long.valueOf(code) < 40000000) {
					pfmcode = Long.valueOf(20000000 + code);
				} else {
					pfmcode = Long.valueOf(code);
				}
			} catch(Exception e) {
				log.error(TypeConverter.exceptionToString(e));
				throw new ServiceException(new Exception(), new ServiceError(99,
						"Не существует такого кода ПФМ"));
			}
			
			binder.setNodeValue("id_paketa", request.getIdPak());
			binder.setNodeValue("idakta", request.getActId());
			binder.setNodeValue("primechanie", request.getPrim());
			binder.setNodeValue("nomer_bileta", request.getTicketnumber());
			binder.setNodeValue("pfm", pfmcode);
			
			binder.setNodeValue("package", request.getIdPak());
			//binder.setNodeValue("predId", pfmcode);
			
			//new
			setFlagAndPredid(binder, pfmcode);
			//end new
			
			AddToPackage(request.getIdPak());	
			
			document.setPredId(ent1);
			
			document.setSignLvl(0);
			document.setType("Счет-фактура РТК");
//			Long id = facade.insertDocument(document);
			id = facade.getDocumentDao().getNextId();
			document.setId(id);
			try{
				binder.setNodeValue("documentId", id);
				} catch (Exception e){
					log.error("no tag documentId in SF "+TypeConverter.exceptionToString(e));
				}
			document.setBlDoc(form.encodeToArchiv());
			document.setDocData(form.transform("data"));
			facade.insertDocumentWithDocid(document);
			
		
			pp.put("docid", id);
			pp.put("packid", request.getIdPak());
			facade.getNpjt()
					.update("update snt.docstore set id_pak = :packid, visible=0 where id =:docid",
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

	private void AddToPackage(String id_pak) throws UnsupportedEncodingException, ServiceException, IOException, TransformerException{
		HashMap<String, Object> pp = new HashMap<String, Object>();
    	pp.put("id_pak", id_pak);
    	byte[] blob = (byte[])facade.getNpjt().queryForObject("select bldoc from snt.docstore where id_pak =:id_pak and visible = 1", pp, byte[].class);
		 ETDForm form=ETDForm.createFromArchive(blob);
		
		 DataBinder b=form.getBinder();
		
		 try{
		
			int no =  b.getValuesAsArray("P_2").length;

			b.setRootElement("data");
			b.cloneNode("row");
			b.setRootLastElement("row");
			b.setNodeValue("P_1", no+1);
			b.setNodeValue("P_2", "Счет-фактура РТК");
			 
			b.setRootElement("data");
			b.cloneNode("row2");
			b.setRootLastElement("row2");
			b.setNodeValue("P_2", "Счет-фактура РТК");
			 
		 } catch (Exception e){
			 log.error(TypeConverter.exceptionToString(e));
		 }
		 
//		 System.out.println(form.transform("data"));
		 
		 
    	try{
		 pp.put("BLDOC", form.encodeToArchiv());
		 pp.put("DOCDATA", form.transform("data"));
		 pp.put("ID", id_pak.replaceAll("P_", ""));
		 
		 facade.getNpjt().update("update snt.docstore set bldoc =:BLDOC, docdata =:DOCDATA where id=:ID", pp);
    	} catch (Exception e){
    		log.error(TypeConverter.exceptionToString(e));
    		throw new ServiceException(e, -1, "Ошибка при обнолвении пакета документов РТК "
					+ e.getMessage());
    	}
		 
    	
    }	
	
	private void setFlagAndPredid(DataBinder binder, Long pfmcode) throws InternalException, ServiceException, IOException{
		String actId = binder.getValue("idakta");
		Document document = facade.getDocumentById(Long.parseLong(actId));
		byte[] blob = document.getBlDoc();
		ETDForm form = ETDForm.createFromArchive(blob);
		DataBinder binderAct = form.getBinder();
		String flag = binderAct.getValue("flag");
		binder.setNodeValue("flag", flag);
		if(Integer.parseInt(flag)==1){
			binder.setNodeValue("predId2", binderAct.getValue("predId"));
			binder.setNodeValue("predId", pfmcode);
		}else if(Integer.parseInt(flag)==0){
			binder.setNodeValue("predId", binderAct.getValue("predId"));
		}
	}
}



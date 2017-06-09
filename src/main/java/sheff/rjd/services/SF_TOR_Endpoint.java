package sheff.rjd.services;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.oxm.Marshaller;
import org.springframework.ws.client.core.WebServiceTemplate;
import org.springframework.ws.server.endpoint.AbstractMarshallingPayloadEndpoint;
import org.w3c.dom.DOMException;

import ru.aisa.rgd.ws.dao.ServiceFacade;
import ru.aisa.rgd.ws.documents.ETDForm;
import ru.aisa.rgd.ws.domain.Document;
import ru.aisa.rgd.ws.exeption.InternalException;
import ru.aisa.rgd.ws.exeption.ServiceException;
import ru.aisa.rgd.ws.utility.DataBinder;
import ru.aisa.rgd.ws.utility.DataRowMapper;
import ru.aisa.rgd.ws.utility.RowFiller;
import ru.aisa.rgd.ws.utility.TypeConverter;
import ru.iit.portal.sf.AddrIn;
import ru.iit.portal.sf.AddrRf;
import ru.iit.portal.sf.SFRequestDocument;
import ru.iit.portal.sf.SFRequestDocument.SFRequest;
import ru.iit.portal.sf.SFResponseDocument;
import ru.iit.portal.sf.Table1;
import ru.iit.portal.sf.Table2;

import com.aisa.portal.invoice.integration.security.Manager;
import com.aisa.portal.invoice.operator.obj.OperatorObject;
public class SF_TOR_Endpoint extends
	AbstractMarshallingPayloadEndpoint {

    private static Log log = LogFactory
	    .getLog(SF_TOR_Endpoint.class);

    private NamedParameterJdbcTemplate npjt;
   private  WebServiceTemplate wst;
   public ServiceFacade getFacade() {
	return facade;
}

public void setFacade(ServiceFacade facade) {
	this.facade = facade;
}
private Manager securitymanager;
   private ServiceFacade facade;
   OperatorObject oper;
   public Manager getSecuritymanager() {
		return securitymanager;
	}

	public WebServiceTemplate getWst() {
	return wst;
}

public void setWst(WebServiceTemplate wst) {
	this.wst = wst;
}

	public void setSecuritymanager(Manager securitymanager) {
		this.securitymanager = securitymanager;
	}

	public NamedParameterJdbcTemplate getNpjt() {
	return npjt;
    }

    public void setNpjt(NamedParameterJdbcTemplate npjt) {
	this.npjt = npjt;
    }

    
    public OperatorObject getOper() {
		return oper;
	}

	public void setOper(OperatorObject oper) {
		this.oper = oper;
	}

	public SF_TOR_Endpoint(Marshaller marshaller) {
	super(marshaller);
    }
    private String urltype = "GetNoticeURL";
   
    
    String sql = " insert into SNT.DocStore (ID,PREDID,TYPEID,CRDATE,CRTIME,BLDOC,NWRKID,LWRKID,LDATE,LTIME,LPERSID,INUSEID,DOCDATA, SIGNLVL,MADEID,  datedoc, PRED_CREATOR, PERSID, ETDID, id_pak, OPISANIE, sf_sign, vagnum, VISIBLE) "
		    + " values ( :globalid, :PREDID, :TYPEID  ,current_date,current_time, :BLDOC  , null , :LWRKID,NULL,NULL ,NULL,NULL, :DOCDATA, "
		    + " :SIGNLVL, NULL"
		    + ",  current_date, :PREDCREATOR, -1, :ETDID, :id_pak, :content #sf #vagnum #visible)";
    
    
    @SuppressWarnings("unchecked")
	protected Object invokeInternal(Object obj) {
	
		final SFRequestDocument requestDocument = (SFRequestDocument) obj;
		final SFRequest request = requestDocument.getSFRequest();
		SFResponseDocument response = SFResponseDocument.Factory.newInstance();
		ETDForm form = null;
		Document document = null;
		DataBinder binder;
		
		try
			{
			document = new Document();
		form = new ETDForm(facade.getDocumentTemplate("Счет-фактура"));
		binder = form.getBinder();
			
		binder.setRootElement("data");
			
			Map map = new HashMap();
			
			map.put("predkod", request.getPredKod());
			
			int ent1 = 0;
			
			ent1 = getNpjt()
					.queryForInt(
							
							
							"select id from snt.pred where okpo_kod = :predkod and headid is null",
							map);
			map.put("code", request.getPredKod());
			map.put("contr_code", request.getOkpoCode());
			
			String p1 = null;
			try{
			binder.setNodeValue("cntPred", (String) getNpjt().queryForObject("select vname from snt.pred where okpo_kod = :contr_code", map, String.class));
			} catch (Exception e){
				e.printStackTrace();
			}
			
			
			
			binder.setNodeValue("pole", "");
			binder.setNodeValue("id_pak", request.getIdPak());
			binder.setNodeValue("num_schet", request.getSFInf().getNumber());
			binder.setNodeValue("date_ot", request.getSFInf().getDate());
			if (request.isSetCorrInf())
			{
			
			// TODO поменяно местами
			
			binder.setNodeValue("num_isprav", request.getCorrInf().getCorrectDate());
			
			binder.setNodeValue("date_ot_isprav", request.getCorrInf()
					.getCorrectNumber());
			}
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
//					System.out.println(innkppsell[0]);
//					System.out.println(innkppsell[1]);
					map.put("innbuy", innkppbuy[0]);
					map.put("kppbuy", innkppbuy[1]);
//					System.out.println(innkppbuy[0]);
//					System.out.println(innkppbuy[1]);
					String cabinetId = (String) getNpjt().queryForObject(
							"Select contr_code from snt.pred where inn = :innsell and kpp =:kppsell and headid is null", map,
							String.class);
					String cabinetIdContr = (String) getNpjt().queryForObject(
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
								
								return map;
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
			
			
			if (request.isSetTorgetdid()){
			
			try{
				binder.setNodeValue("id_torg12_atd", request.getTorgetdid());
			}
			catch (Exception e)
			{
				log.error("No tag id_torg12_atd in SF " +TypeConverter.exceptionToString(e));
				
				}
			}
			
			if (request.isSetSfType()){
			try{
				binder.setNodeValue("vid_sf", request.getSfType());
			}
			catch (Exception e)
			{
				log.error("No tag vid_sf in SF " +TypeConverter.exceptionToString(e));
				
				}
			}
			
			
			
			
			
			document.setBlDoc(form.encodeToArchiv());
			document.setDocData(form.transform("data"));
			
			document.setPredId(ent1);
			
			document.setSignLvl(0);
			document.setType("Счет-фактура");
			Long id = facade.insertDocument(document);
			
			
			
			response.addNewSFResponse();
			response.getSFResponse().setCode(0);
			response.getSFResponse().setDescription("ok");
			response.getSFResponse().setDocid(id);
			
			
			return response;
			}
		catch (Exception e)
			{
			StringWriter outError = new StringWriter();
			PrintWriter errorWriter = new PrintWriter(outError);
			e.printStackTrace(errorWriter);
			log.error(outError.toString());
			System.out.println(outError.toString());
			throw new ServiceException(e, -1, "Error occured "
					+ e.getMessage());
			}
		
		}

}


package sheff.rjd.services.act;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.transform.TransformerException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.log4j.Logger;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.oxm.Marshaller;
import org.springframework.ws.client.core.WebServiceTemplate;
import org.springframework.ws.server.endpoint.AbstractMarshallingPayloadEndpoint;
import org.w3c.dom.DOMException;

import ru.aisa.rgd.ws.dao.ServiceFacade;
import ru.aisa.rgd.ws.domain.Document;

import com.aisa.portal.invoice.integration.facade.PortalSFFacade;
import com.aisa.portal.invoice.integration.security.Manager;
import com.aisa.portal.invoice.operator.obj.OperatorObject;

import ru.aisa.rgd.ws.documents.ETDForm;
import ru.aisa.rgd.ws.documents.StandartResponseWrapper;
import ru.aisa.rgd.ws.endpoint.ETDAbstractEndpoint;
import ru.aisa.rgd.ws.endpoint.ResponseAdapter;
import ru.aisa.rgd.ws.exeption.InternalException;
import ru.aisa.rgd.ws.exeption.ServiceError;
import ru.aisa.rgd.ws.exeption.ServiceException;
import ru.aisa.rgd.ws.utility.DataBinder;
import ru.aisa.rgd.ws.utility.DataRowMapper;
import ru.aisa.rgd.ws.utility.RowFiller;
import ru.aisa.rgd.ws.utility.TypeConverter;
//import rzd8888.gvc.etd.was.etd.korsfrtk.*;
import ru.aisa.rgd.ws.dao.ServiceFacade;
import ru.iit.korSFRTK.korSFRTKEndpoint.FormCreateRequest;
import ru.iit.korSFRTK.korSFRTKEndpoint.FormCreateResponse;
import ru.iit.korSFRTK.korSFRTKEndpoint.FormCreateRequestDocument;
import ru.iit.korSFRTK.korSFRTKEndpoint.PersonInfo;
import ru.iit.korSFRTK.korSFRTKEndpoint.FormCreateResponseDocument;
import ru.iit.korSFRTK.korSFRTKEndpoint.Table;
import ru.iit.korSFRTK.korSFRTKEndpoint.CodeAndNameNational;
import ru.iit.korSFRTK.korSFRTKEndpoint.PriceProduct;
import ru.iit.korSFRTK.korSFRTKEndpoint.GoodsVolume;
import ru.iit.korSFRTK.korSFRTKEndpoint.WholePrice;
import ru.iit.korSFRTK.korSFRTKEndpoint.ExciseSum;
import ru.iit.korSFRTK.korSFRTKEndpoint.TaxSum;
import ru.iit.korSFRTK.korSFRTKEndpoint.TaxRate;
import ru.iit.korSFRTK.korSFRTKEndpoint.WholePriceWithTax;
import ru.iit.korSFRTK.korSFRTKEndpoint.Addr;
import ru.iit.korSFRTK.korSFRTKEndpoint.AddrIn;
import ru.iit.korSFRTK.korSFRTKEndpoint.AddrRf;

public class Kor_SF_RTK_Endpoint extends
		ETDAbstractEndpoint<StandartResponseWrapper> {

	private static Logger log = Logger.getLogger(Kor_SF_RTK_Endpoint.class);
	private NamedParameterJdbcTemplate npjt;
	private WebServiceTemplate wst;
	private Manager securitymanager;
	private ServiceFacade facade;
	OperatorObject oper;
	private String formname;
	private String selectCountDoc = "select count(0) from snt.docstore where id = :id";

	protected Kor_SF_RTK_Endpoint(Marshaller marshaller) {
		super(marshaller);
	}

	public String getFormname() {
		return formname;
	}

	public void setFormname(String formname) {
		this.formname = formname;
	}

	public ServiceFacade getFacade() {
		return facade;
	}

	public void setFacade(ServiceFacade facade) {
		this.facade = facade;
	}

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

	@Override
	protected ResponseAdapter<StandartResponseWrapper> processRequest(
			Object arg, ServiceFacade facade) throws Exception {
		FormCreateRequestDocument formCreateRequestDocument = (FormCreateRequestDocument) arg;
		FormCreateRequest request = formCreateRequestDocument
				.getFormCreateRequest();
		FormCreateResponseDocument response = FormCreateResponseDocument.Factory
				.newInstance();

		ETDForm form = null;
		Document document = new Document();
		DataBinder dataBinder;
		boolean reWriteDoc = request.isSetDocId();
		Long id = null;

		// Проверка на существование СФ РТК

		if (reWriteDoc) {
			id = request.getDocId();
			document = facade.getDocumentById(id);

			if (document.getDropTime() != null) {
				throw new ServiceException(
						new Exception(),
						new ServiceError(9,
								"Документ имеет подпись или отклонен, перезаписать данные нельзя"));
			}
			if (document.getSignLvl() == null || document.getSignLvl() != 0) {
				throw new ServiceException(
						new Exception(),
						new ServiceError(9,
								"Документ имеет подпись или отклонен, перезаписать данные нельзя"));
			}
			if (!document.getType().equals(formname)) {
				throw new ServiceException(new Exception(),
						ServiceError.ERR_WRONG_TYPEFORM);
			}
		}

		else {
			HashMap<String, Object> sfmap = new HashMap<String, Object>();
			sfmap.put("id_pak", request.getIdPak());
			sfmap.put("type", formname);

			int count = facade
					.getNpjt()
					.queryForInt(
							"select count(id) from snt.docstore where id_pak =:id_pak and typeid = (select id from snt.doctype where name =:type)",
							sfmap);

			if (count > 0) {
				StandartResponseWrapper wrapper = new StandartResponseWrapper();
				wrapper.setDescription("В этом пакете уже есть корректировочный счет-фактура");
				wrapper.setCode(-2);
				wrapper.setDocumentId(Long.MIN_VALUE);
				ResponseAdapter<StandartResponseWrapper> adapter = new ResponseAdapter<StandartResponseWrapper>(
						true, wrapper, ServiceError.ERR_OK);
				return adapter;
			}

			id = facade.getNextDocumentId();
			document.setId(id);
		}

		try {
//			System.out.println("1");
			form = new ETDForm(facade.getDocumentTemplate(formname));
			dataBinder = form.getBinder();
			dataBinder.setRootElement("data");

			// Map map = new HashMap();

			// first part, string 1
			dataBinder.setNodeValue("korrek_sf_nomer", request
					.getSFCorrectionInfo().getNumber());
			dataBinder.setNodeValue("korrek_sf_ot", request
					.getSFCorrectionInfo().getDate());

			// first part, string 1a
			if (request.isSetSFCorrectionInfoCorrect()) {
				dataBinder.setNodeValue("isprav_sf_nomer", request
						.getSFCorrectionInfoCorrect().getNumber());
				dataBinder.setNodeValue("isprav_sf_ot", request
						.getSFCorrectionInfoCorrect().getDate());
			}
			// first part, string 1b
			// first
			dataBinder.setNodeValue("kschety_faktyre_nomer", request
					.getSFInfo().getNumber());
			dataBinder.setNodeValue("kschety_faktyre_ot", request.getSFInfo()
					.getDate());
			// second
			if (request.isSetSFInfoCorrect()) {
				dataBinder.setNodeValue("sychetom_isprav_nomer", request
						.getSFInfoCorrect().getNumber());
				dataBinder.setNodeValue("sychetom_isprav_ot", request
						.getSFInfoCorrect().getDate());
			}

			// first part, string 2,2a,2b
			PersonInfo personInfo = request.getSellerInfo();
			dataBinder.setNodeValue("prodavec", personInfo.getName());

			String adres_prodovca = setAddres(dataBinder, personInfo,
					"SellAddrIn", "SellAddr", "adres_prodovca_type");
			dataBinder.setNodeValue("adres_prodovca", adres_prodovca);
			dataBinder.setNodeValue("inn_prodovca", personInfo.getInn() + "/"
					+ personInfo.getKpp());
			// dataBinder.setNodeValue("kpp_prodovca", personInfo.getKpp());

			// first part, string 3,3a,3b
			personInfo = request.getCustomerInfo();
			dataBinder.setNodeValue("pokypatel", personInfo.getName());
			String adres_pocypatelya = setAddres(dataBinder, personInfo,
					"CustAddrIn", "CustAddr", "adres_pocypatelya_type");
			dataBinder.setNodeValue("adres_pocypatelya", adres_pocypatelya);
			dataBinder.setNodeValue("inn_pokypatelya", personInfo.getInn()
					+ "/" + personInfo.getKpp());
			// dataBinder.setNodeValue("kpp_pokypatelya", personInfo.getKpp());

			// first part, string 4
			dataBinder.setNodeValue("valuta", request.getCurrencyName());
			dataBinder.setNodeValue("kod_valuta", request.getCurrency());
		} catch (Exception e) {
			System.out.println(e);
			log.error(TypeConverter.exceptionToString(e));
			throw new ServiceException(e, -1, "Error occured " + e.getMessage());
		}
//		System.out.println("2");
		// second part - table1
		dataBinder.fillTable(request.getTableArray(),
				new RowFiller<Table, Object>() {

					public void fillRow(DataBinder b, Table rowContent,
							int numRow, Object options) throws DOMException,
							InternalException {

						float A = 0;
						float B = 0;
						float C = 0;
						float D = 0;

						// string - 1
						b.setNodeValue("naim_tovara",
								rowContent.getNameSuppliedGoods());
						// string - 2,2a
//						System.out.println("2.1");
						if (rowContent.isSetUnitProduct()) {
							if (rowContent.getUnitProduct().isSetBeforeChange()) {
								CodeAndNameNational codeAndNameNational = rowContent
										.getUnitProduct().getBeforeChange();
								b.setNodeValue("kod",
										codeAndNameNational.getCode());
								b.setNodeValue("yslovn_oboz",
										codeAndNameNational.getName());
							} else {
								b.setNodeValue("kod", "-");
								b.setNodeValue("yslovn_oboz", "-");
							}
							if (rowContent.getUnitProduct().isSetAfterChange()) {
								CodeAndNameNational codeAndNameNational = rowContent
										.getUnitProduct().getAfterChange();
								b.setNodeValue("kod2",
										codeAndNameNational.getCode());
								b.setNodeValue("yslovn_oboz2",
										codeAndNameNational.getName());
							} else {
								b.setNodeValue("kod2", "-");
								b.setNodeValue("yslovn_oboz2", "-");
							}
						} else {
							b.setNodeValue("kod", "-");
							b.setNodeValue("yslovn_oboz", "-");
							b.setNodeValue("kod2", "-");
							b.setNodeValue("yslovn_oboz2", "-");
						}
						// string - 3
//						System.out.println("2.2");
						if (rowContent.isSetGoodsVolume()) {
							GoodsVolume goodsVolume = rowContent
									.getGoodsVolume();
							if (goodsVolume.isSetBeforeChange()) {
								b.setNodeValue("kol_obem",
										goodsVolume.getBeforeChange());
							} else {
								b.setNodeValue("kol_obem", "-");
							}
							if (goodsVolume.isSetAfterChange()) {
								b.setNodeValue("kol_obem2",
										goodsVolume.getAfterChange());
							} else {
								b.setNodeValue("kol_obem2", "-");
							}
						} else {
							b.setNodeValue("kol_obem", "-");
							b.setNodeValue("kol_obem2", "-");
						}
//						System.out.println("2.4");
						// string - 4
						if (rowContent.isSetPriceProduct()) {
							PriceProduct priceProduct = rowContent
									.getPriceProduct();
							if (priceProduct.isSetBeforeChange()) {
								b.setNodeValue("cena_tarif",
										priceProduct.getBeforeChange());
							} else {
								b.setNodeValue("cena_tarif", "-");
							}
							if (priceProduct.isSetAfterChange()) {
								b.setNodeValue("cena_tarif2",
										priceProduct.getAfterChange());
							} else {
								b.setNodeValue("cena_tarif2", "-");
							}

						} else {
							b.setNodeValue("cena_tarif", "-");
							b.setNodeValue("cena_tarif2", "-");
						}

						b.setNodeValue("priznak_yslygi",
								rowContent.getSigService());

						if (rowContent.isSetInfPolStr()) {
							b.setNodeValue("infPolStr",
									rowContent.getInfPolStr());
						}
//						System.out.println("2.5");
						// string - 5
						if (rowContent.isSetWholePrice()) {
							WholePrice wholePrice = rowContent.getWholePrice();
							if (wholePrice.isSetBeforeChange()) {
								b.setNodeValue("stoim_tovara",
										wholePrice.getBeforeChange());
							} else {
								b.setNodeValue("stoim_tovara", "-");
							}
							if (wholePrice.isSetAfterChange()) {
								b.setNodeValue("stoim_tovara2",
										wholePrice.getAfterChange());// 30
								A = Float.parseFloat(wholePrice
										.getAfterChange());
							} else {
								b.setNodeValue("stoim_tovara2", "-");
							}
							if (wholePrice.isSetIncrease()) {
								b.setNodeValue("stoim_tovara3",
										wholePrice.getIncrease());
							} else {
								b.setNodeValue("stoim_tovara3", "-");
							}
							if (wholePrice.isSetReduction()) {
								b.setNodeValue("stoim_tovara4",
										wholePrice.getReduction());
							} else {
								b.setNodeValue("stoim_tovara4", "-");
							}

						} else {
							b.setNodeValue("stoim_tovara", "-");
							b.setNodeValue("stoim_tovara2", "-");
							b.setNodeValue("stoim_tovara3", "-");
							b.setNodeValue("stoim_tovara4", "-");
						}
//						System.out.println("2.6");
						// string - 6
						ExciseSum exciseSum = rowContent.getExciseSum();
						b.setNodeValue("vtom_chisle",
								exciseSum.getBeforeChange());

						b.setNodeValue("vtom_chisle2",
								exciseSum.getAfterChange());

						if (exciseSum.isSetIncrease()) {
							b.setNodeValue("vtom_chisle3",
									exciseSum.getIncrease());
							b.setNodeValue("vtom_chisle4", "-");
						} else {
							b.setNodeValue("vtom_chisle3", "-");
							b.setNodeValue("vtom_chisle4",
									exciseSum.getReduction());
						}
//						System.out.println("2.7");
						// string - 7
						TaxRate taxRate = rowContent.getTaxRate();

						b.setNodeValue("nalog_stavka",
								taxRate.getBeforeChange());

						b.setNodeValue("nalog_stavka2",
								taxRate.getAfterChange()); // 32

						D = taxRate.getAfterChange()
								.equalsIgnoreCase("без НДС") ? 0 : taxRate
								.getAfterChange().contains("%") ? Float
								.parseFloat(taxRate.getAfterChange().replace(
										"%", "")) : Float.parseFloat(taxRate
								.getAfterChange());
//								System.out.println("2.8");
						// string - 8
						if (rowContent.isSetTaxSum()) {
							TaxSum taxSum = rowContent.getTaxSum();
							if (taxSum.isSetBeforeChange()) {
								b.setNodeValue("summ_naloga",
										taxSum.getBeforeChange());
							} else {
								b.setNodeValue("summ_naloga", "без НДС");
							}
							if (taxSum.isSetAfterChange()) {
								b.setNodeValue("summ_naloga2",
										taxSum.getAfterChange());// 33
								if (!taxSum.getAfterChange().equals("без НДС")){
								C = Float.parseFloat(taxSum.getAfterChange());
								}else {
									C=0;
								}
							} else {
								b.setNodeValue("summ_naloga2", "без НДС");
							}
							if (taxSum.isSetIncrease()) {
								b.setNodeValue("summ_naloga3",
										taxSum.getIncrease());
							} else {
								b.setNodeValue("summ_naloga3", "-");
							}
							if (taxSum.isSetReduction()) {
								b.setNodeValue("summ_naloga4",
										taxSum.getReduction());
							} else {
								b.setNodeValue("summ_naloga4", "-");
							}

						} else {
							b.setNodeValue("summ_naloga", "без НДС");
							b.setNodeValue("summ_naloga2", "без НДС");
							b.setNodeValue("summ_naloga3", "-");
							b.setNodeValue("summ_naloga4", "-");
						}
//						System.out.println("2.9");
						// string - 9
						if (rowContent.isSetWholePriceWithTax()) {
							WholePriceWithTax wholePriceWithTax = rowContent
									.getWholePriceWithTax();
							b.setNodeValue("stoim_vsego",
									wholePriceWithTax.getBeforeChange());
							b.setNodeValue("stoim_vsego2",
									wholePriceWithTax.getAfterChange());// 34
							B = Float.parseFloat(wholePriceWithTax
									.getAfterChange());
							if (wholePriceWithTax.isSetIncrease()) {
								b.setNodeValue("stoim_vsego3",
										wholePriceWithTax.getIncrease());
							} else {
								b.setNodeValue("stoim_vsego3", "-");
							}
							if (wholePriceWithTax.isSetReduction()) {
								b.setNodeValue("stoim_vsego4",
										wholePriceWithTax.getReduction());
							} else {
								b.setNodeValue("stoim_vsego4", "-");
							}

						} else {
							b.setNodeValue("stoim_vsego", "-");
							b.setNodeValue("stoim_vsego2", "-");
							b.setNodeValue("stoim_vsego3", "-");
							b.setNodeValue("stoim_vsego4", "-");
						}
						float AD = A * (100 + D) / 100;
						float AC = (A + C);

						String ads = String.valueOf(AD);
						String[] adsa = ads.split("\\.");
						if (adsa[1].length() > 2) {
							ads = adsa[0] + "." + adsa[1].substring(0, 2);
						}
						String acs = String.valueOf(AC);
						String[] acsa = acs.split("\\.");
						if (acsa[1].length() > 2) {
							acs = acsa[0] + "." + acsa[1].substring(0, 2);
						}

						AD = Float.parseFloat(ads);
						AC = Float.parseFloat(acs);

						if (AD != B | AC != B) {
							throw new ServiceException(new Exception(),
									new ServiceError(99, "Ошибка проверки НДС"));
						}

					}
				}, "table1", "row");
		// if(request.isSetInfPolStr()){
		// dataBinder.setNodeValue("primechnie", request.getInfPolStr());
		// }
		
//		System.out.println("5");
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("id", request.getIdActRTK());
		Integer colIdRTK = npjt.queryForInt(selectCountDoc, map);
		if (colIdRTK != 0) {
			dataBinder.setLastNodeValue("id_ranee_peredov_akt",
					request.getIdActRTK());
		} else {
			throw new ServiceException(new Exception(), new ServiceError(102,
					"Не существует акта РТК с таким номером"));
		}
		map.put("id", request.getIdSFRTK());
		Integer colIdSFRTK = npjt.queryForInt(selectCountDoc, map);
		if (colIdSFRTK != 0) {
			dataBinder.setLastNodeValue("id_ranee_peredov_sf",
					request.getIdSFRTK());
		} else {
			throw new ServiceException(new Exception(), new ServiceError(103,
					"Не существует счет-фактуры РТК с таким номером"));
		}
//		System.out.println("3");
		// if (!request.isSetPredKod()){
		try {
			ETDForm sfform = ETDForm.createFromArchive(facade.getDocumentDao()
					.getById(request.getIdSFRTK()).getBlDoc());
			DataBinder sfbinder = sfform.getBinder();

			dataBinder.setNodeValue("cabinetIdSell",
					sfbinder.getValue("cabinetIdSell"));
			dataBinder.setNodeValue("cabinetIdRecv",
					sfbinder.getValue("cabinetIdRecv"));
			dataBinder.setNodeValue("nameoper", sfbinder.getValue("nameoper"));
			dataBinder.setNodeValue("innoper", sfbinder.getValue("innoper"));
			dataBinder.setNodeValue("idoper", sfbinder.getValue("idoper"));

		} catch (Exception e) {
			log.error(TypeConverter.exceptionToString(e));
			e.printStackTrace();
		}

		// }
		//
		// else {
		//
		// }

		// third part, all Increase and Reduction rows
		if (request.isSetAllIncWholePrice()) {
			dataBinder.setNodeValue("vsego_yvelich_summtovara",
					request.getAllIncWholePrice());
		}
		// if(request.isSetAllReductionStringsWholePrice()){
		if (request.isSetAllRedWholePrice()) {
			dataBinder.setNodeValue("vsego_ymenshenie_summtovara",
					request.getAllRedWholePrice());
		}
		dataBinder.setNodeValue("vsego_yvelich_summnaloga",
				request.getAllIncTaxSum());
		dataBinder.setNodeValue("vsego_ymenshenie_summnaloga",
				request.getAllRedTaxSum());
		dataBinder.setNodeValue("vsego_yvelich_stoimtovara",
				request.getAllIncWholePriceWithTax());
		dataBinder.setNodeValue("vsego_ymenshenie_stoimtovara",
				request.getAllRedWholePriceWithTax());

		// fourth part, id_pak, cabinetIdSell-cabinetIdRecv,
		// nameoper-innoper-idoper
		// id_pak
		// dataBinder.setNodeValue("id_pak", request.getIdPak());

		dataBinder.setNodeValue("package", request.getIdPak());
		AddToPackage(request.getIdPak());
		// int ent1;
		HashMap<String, Object> pp = new HashMap<String, Object>();
		pp.put("id_pack", request.getIdPak());
		List<Integer> ent1 = facade.getNpjt().queryForList(
				"select predid from snt.docstore where id_pak=:id_pack", pp,
				Integer.class);
		Long pfmcode = null;
		// System.out.println("pfm: "+request.getPFMCode());
//		System.out.println("4");
		try {
			pfmcode = Long.valueOf((20000000 + PFMList.pfmcode.get(request
					.getPFMCode())));
		} catch (Exception e) {
			log.error(TypeConverter.exceptionToString(e));
			throw new ServiceException(new Exception(), new ServiceError(99,
					"Не существует такого кода ПФМ"));
		}
		dataBinder.setNodeValue("pfm", request.getPFMCode());
		dataBinder.setNodeValue("predId", pfmcode);
		dataBinder.setNodeValue("num_bileta", request.getNumTicket());
		// dataBinder.setNodeValue("primechnie", request.getInfPolStr());

		// dataBinder.setNodeValue("", request.getPFMCode());
		document.setBlDoc(form.encodeToArchiv());
		document.setDocData(form.transform("data"));

		document.setPredId((Integer) ent1.get(0));

		document.setSignLvl(0);
		document.setType(formname);
		// Long id = facade.insertDocument(document);
		// id = facade.getDocumentDao().getNextId();

		dataBinder.setNodeValue("id_ksf", id);
		dataBinder.setNodeValue("documentId", id);
		
		document.setBlDoc(form.encodeToArchiv());
		document.setDocData(form.transform("data"));
		// document.setId(id);
		// System.out.println(document.getDocData());
		facade.getDocumentDao().save(document);

		pp.put("docid", id);
		// pp.put("packid", request.getIdPak());
		facade.getNpjt()
				.update("update snt.docstore set id_pak = :id_pack, visible=0 where id =:docid",
						pp);

		// System.out.println(document.toString());

		StandartResponseWrapper wrapper = new StandartResponseWrapper();
		wrapper.setDescription("ok");
		wrapper.setCode(0);
		wrapper.setDocumentId(document.getId());
		ResponseAdapter<StandartResponseWrapper> adapter = new ResponseAdapter<StandartResponseWrapper>(
				true, wrapper, ServiceError.ERR_OK);

		return adapter;
	}

	@Override
	protected Object composeResponce(
			ResponseAdapter<StandartResponseWrapper> adapter) {
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
			response.setDocid(-1);
		}

		return responsedoc;
	}

	public String setAddres(DataBinder dataBinder, PersonInfo personInfo,
			String addrIn, String addrRf, String type) throws Exception {
		String typeAddr = "";
		StringBuilder stringBuilder = new StringBuilder();
		Addr addr = personInfo.getAddress();
		if (addr.isSetAIn()) {
			AddrIn in = addr.getAIn();
			stringBuilder.append(in.getKod()).append(", ").append(in.getText());

			dataBinder.fillTable(new AddrIn[] { in },
					new RowFiller<AddrIn, Object>() {

						public void fillRow(DataBinder b, AddrIn rowContent,
								int numRow, Object options) {
							try {
								b.setNodeValue("kod", rowContent.getKod());
								b.setNodeValue("text", rowContent.getText());
							} catch (Exception e) {
								e.printStackTrace();
							}

						}
					}, addrIn, "row");
			typeAddr = "in";
		} else if (addr.isSetARf()) {
			// System.out.println("arf");
			AddrRf rf = addr.getARf();
			if (rf.isSetInd()) {
				stringBuilder.append(rf.getInd()).append(", ");
			}
			stringBuilder.append(rf.getKod()).append(", ");
			if (rf.isSetRaion()) {
				stringBuilder.append(rf.getRaion()).append(", ");
			}
			if (rf.isSetPunkt()) {
				stringBuilder.append(rf.getPunkt()).append(", ");
			}
			if (rf.isSetTown()) {
				stringBuilder.append(rf.getTown()).append(", ");
			}
			if (rf.isSetStreet()) {
				stringBuilder.append(rf.getStreet()).append(", ");
			}
			if (rf.isSetHouse()) {
				stringBuilder.append(rf.getHouse()).append(", ");
			}
			if (rf.isSetKorp()) {
				stringBuilder.append(rf.getKorp()).append(", ");
			}
			if (rf.isSetFlat()) {
				stringBuilder.append(rf.getFlat()).append(", ");
			}
			dataBinder.fillTable(new AddrRf[] { rf },
					new RowFiller<AddrRf, Object>() {

						public void fillRow(DataBinder dataBinder,
								AddrRf rowContent, int numRow, Object options)
								throws DOMException, InternalException {

							dataBinder.setNodeValue("kod", rowContent.getKod());
							if (rowContent.isSetInd()) {
								dataBinder.setNodeValue("ind",
										rowContent.getInd());
							}
							if (rowContent.isSetRaion()) {
								dataBinder.setNodeValue("raion",
										rowContent.getRaion());
							}
							if (rowContent.isSetPunkt()) {
								dataBinder.setNodeValue("punkt",
										rowContent.getPunkt());
							}
							if (rowContent.isSetTown()) {
								dataBinder.setNodeValue("town",
										rowContent.getTown());
							}
							if (rowContent.isSetStreet()) {
								dataBinder.setNodeValue("street",
										rowContent.getStreet());
							}
							if (rowContent.isSetHouse()) {
								dataBinder.setNodeValue("house",
										rowContent.getHouse());
							}
							if (rowContent.isSetKorp()) {
								dataBinder.setNodeValue("korp",
										rowContent.getKorp());
							}
							if (rowContent.isSetFlat()) {
								dataBinder.setNodeValue("flat",
										rowContent.getFlat());
							}

						}
					}, addrRf, "row");
			typeAddr = "rf";
			dataBinder.setNodeValue(type, typeAddr);
		}

		// dataBinder.setNodeValue(type, typeAddr);
		return stringBuilder.toString();
	}

	public String getAddress(Addr addr) {
		StringBuilder stringBuilder = new StringBuilder();

		if (addr.isSetARf()) {
			AddrRf rf = addr.getARf();
			if (rf.isSetInd())
				stringBuilder.append(rf.getInd()).append(", ");
			if (rf.isSetRaion())
				stringBuilder.append(rf.getRaion()).append(", ");
			if (rf.isSetPunkt())
				stringBuilder.append(rf.getPunkt()).append(", ");
			if (rf.isSetTown())
				stringBuilder.append(rf.getTown()).append(", ");
			if (rf.isSetStreet())
				stringBuilder.append(rf.getStreet()).append(", ");
			if (rf.isSetHouse())
				stringBuilder.append(rf.getHouse()).append(", ");
			if (rf.isSetKorp())
				stringBuilder.append(rf.getKorp()).append(", ");
			if (rf.isSetFlat())
				stringBuilder.append(rf.getFlat()).append(", ");

		} else {
			AddrIn in = addr.getAIn();
			stringBuilder.append(in.getKod()).append(", ").append(in.getText());
		}

		return stringBuilder.toString();
	}

	private void AddToPackage(String id_pak)
			throws UnsupportedEncodingException, ServiceException, IOException,
			TransformerException {
		HashMap<String, Object> pp = new HashMap<String, Object>();
		pp.put("id_pak", id_pak);
		byte[] blob = (byte[]) facade
				.getNpjt()
				.queryForObject(
						"select bldoc from snt.docstore where id_pak =:id_pak and visible = 1",
						pp, byte[].class);
		ETDForm form = ETDForm.createFromArchive(blob);

		DataBinder b = form.getBinder();

		try {

			int no = b.getValuesAsArray("P_2").length;

			b.setRootElement("data");
			b.cloneNode("row");
			b.setRootLastElement("row");
			b.setNodeValue("P_1", no + 1);
			b.setNodeValue("P_2", formname);

			b.setRootElement("data");
			b.cloneNode("row2");
			b.setRootLastElement("row2");
			b.setNodeValue("P_2", formname);

		} catch (Exception e) {
			log.error(TypeConverter.exceptionToString(e));
		}

		// System.out.println(form.transform("data"));

		try {
			pp.put("BLDOC", form.encodeToArchiv());
			pp.put("DOCDATA", form.transform("data"));
			pp.put("ID", id_pak.replaceAll("P_", ""));

			facade.getNpjt()
					.update("update snt.docstore set bldoc =:BLDOC, docdata =:DOCDATA where id=:ID",
							pp);
		} catch (Exception e) {
			log.error(TypeConverter.exceptionToString(e));
			throw new ServiceException(e, -1,
					"Ошибка при обнолвении пакета документов РТК "
							+ e.getMessage());
		}

	}
}

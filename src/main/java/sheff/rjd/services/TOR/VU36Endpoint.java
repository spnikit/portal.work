package sheff.rjd.services.TOR;

import java.util.HashMap;
import java.util.Map;

import org.springframework.oxm.Marshaller;

import ru.aisa.rgd.ws.dao.ServiceFacade;
import ru.aisa.rgd.ws.documents.ETDForm;
import ru.aisa.rgd.ws.documents.StandartResponseWrapper;
import ru.aisa.rgd.ws.domain.Document;
import ru.aisa.rgd.ws.domain.Enterprise;
import ru.aisa.rgd.ws.endpoint.ETDAbstractEndpoint;
import ru.aisa.rgd.ws.endpoint.ResponseAdapter;
import ru.aisa.rgd.ws.exeption.ServiceCode;
import ru.aisa.rgd.ws.exeption.ServiceError;
import ru.aisa.rgd.ws.exeption.ServiceException;
import ru.aisa.rgd.ws.utility.DataBinder;
import ru.aisa.rgd.ws.utility.DataRowMapper;
import ru.aisa.rgd.ws.utility.TypeConverter;
import ru.iit.iit.vu36.ReportType;
import ru.iit.iit.vu36.VU36DocRequestDocument;
import ru.iit.iit.vu36.VU36DocResponseDocument;
import ru.iit.iit.vu36.VU36Type;
import ru.iit.iit.vu36.VagonType;


public class VU36Endpoint extends ETDAbstractEndpoint<StandartResponseWrapper>
	{
	public static final String	lastBulidDate	= "20.09.2013 18:25:28";
	private static final String	_9006					= "9006";
	
	private static class Mapper1 implements DataRowMapper<VagonType>
		{
		
		public Map<String, Object> mapRow(VagonType obj)
			{
			Map<String, Object> hash = new HashMap<String, Object>();
			
			String id = obj.getVagonId();
			if (id.length() < 8)
				{
				while (id.length() < 8)
					{
					id = "0" + id;
					}
				}
			hash.put("nomer_vagona_new", id);
			
			String idOld = "";
			if (obj.isSetVagonIdOld())
				{
				idOld = Integer.toString(obj.getVagonIdOld());
				
				if (idOld.length() < 8)
					{
					while (idOld.length() < 8)
						{
						idOld = "0" + idOld;
						}
					}
				}
			
			hash.put("nomer_vagona_old", idOld);
			hash.put("kod_sobstv",
					Integer.toString((Integer.parseInt(obj.getSobstvKod(), 10))));
			hash.put("data_nach_remonta", obj.getRepairBeginDate());
			hash.put("vrem_nach_remonta", obj.getRepairBeginTime());
			boolean is9006 = false;
			
			if (obj.isSetModernization1())
				{
				String mod = obj.getModernization1();
				if (_9006.equals(mod))
					{
					is9006 = true;
					}
				hash.put("kod_modern_1", mod);
				
				}
			if (obj.isSetModernization2())
				{
				String mod = obj.getModernization2();
				if (_9006.equals(mod))
					{
					if (is9006)
						throw new ServiceException(new Exception(), -1,
								"Допустим только один код '9006'");
					is9006 = true;
					}
				hash.put("kod_modern_2", mod);
				}
			if (obj.isSetModernization3())
				{
				String mod = obj.getModernization3();
				if (_9006.equals(mod))
					{
					if (is9006)
						throw new ServiceException(new Exception(), -1,
								"Допустим только один код '9006'");
					is9006 = true;
					}
				hash.put("kod_modern_3", mod);
				}
			if (obj.isSetModernization4())
				{
				String mod = obj.getModernization4();
				if (_9006.equals(mod))
					{
					if (is9006)
						throw new ServiceException(new Exception(), -1,
								"Допустим только один код '9006'");
					is9006 = true;
					}
				hash.put("kod_modern_4", mod);
				}
			if (obj.isSetModernization5())
				{
				String mod = obj.getModernization5();
				if (_9006.equals(mod))
					{
					if (is9006)
						throw new ServiceException(new Exception(), -1,
								"Допустим только один код '9006'");
					is9006 = true;
					}
				hash.put("kod_modern_5", mod);
				}
			if (obj.isSetModernization6())
				{
				String mod = obj.getModernization6();
				if (_9006.equals(mod))
					{
					if (is9006)
						throw new ServiceException(new Exception(), -1,
								"Допустим только один код '9006'");
					is9006 = true;
					}
				hash.put("kod_modern_6", mod);
				}
			if (obj.isSetModernization7())
				{
				String mod = obj.getModernization7();
				if (_9006.equals(mod))
					{
					if (is9006)
						throw new ServiceException(new Exception(), -1,
								"Допустим только один код '9006'");
					is9006 = true;
					}
				hash.put("kod_modern_7", mod);
				}
			if (obj.isSetModernization8())
				{
				String mod = obj.getModernization8();
				if (_9006.equals(mod))
					{
					if (is9006)
						throw new ServiceException(new Exception(), -1,
								"Допустим только один код '9006'");
					is9006 = true;
					}
				hash.put("kod_modern_8", mod);
				}
			if (obj.isSetModernization9())
				{
				String mod = obj.getModernization9();
				if (_9006.equals(mod))
					{
					if (is9006)
						throw new ServiceException(new Exception(), -1,
								"Допустим только один код '9006'");
					is9006 = true;
					}
				hash.put("kod_modern_9", mod);
				}
			if (obj.isSetModernization10())
				{
				String mod = obj.getModernization10();
				if (_9006.equals(mod))
					{
					if (is9006)
						throw new ServiceException(new Exception(), -1,
								"Допустим только один код '9006'");
					is9006 = true;
					}
				hash.put("kod_modern_10", mod);
				}
			if (obj.isSetMess4624()){
				String mess4624 = obj.getMess4624();
				hash.put("P_4624", mess4624);
			}
			
			return hash;
			}
		}
	
	public VU36Endpoint(Marshaller marshaller)
		{
		super(marshaller);
		}
	
	@Override
	protected Object composeResponce(
			ResponseAdapter<StandartResponseWrapper> adapter)
		{
		VU36DocResponseDocument responseDocument = VU36DocResponseDocument.Factory
				.newInstance();
		ReportType response = responseDocument.addNewVU36DocResponse();
		if (adapter.isStatus())
			{
			response.setCode(adapter.getResponse().getCode());
			response.setDocid(adapter.getResponse().getDocumentId());
			response.setDescription(adapter.getResponse().getDescription());
			}
		else
			{
			response.setCode(adapter.getError().getCode());
			response.setDocid(Integer.MIN_VALUE);
			response.setDescription(adapter.getError().getMessage());
			}
		return responseDocument;
		}


	
	@Override
	protected ResponseAdapter<StandartResponseWrapper> processRequest(Object arg,
			ServiceFacade facade) throws Exception
		{
		StandartResponseWrapper wrapper = null;
		final VU36DocRequestDocument requestDocument = (VU36DocRequestDocument) arg;
		
		final VU36Type req = requestDocument.getVU36DocRequest();
		
		// на самом деле там передается код
		int codeStan = Integer.parseInt(requestDocument.getVU36DocRequest()
				.getStationId());
		
		String templatename = "ВУ-36М_О";
		Map<String, Object> pp = new HashMap<String, Object>();

		ETDForm form = new ETDForm(facade.getDocumentTemplate(templatename));
		DataBinder binder = form.getBinder();
		
		
		final Integer crtdBySrvs = 1;
		
		// посленяя версия определения ent:
		
		//st_kod esr kod - last simbol
		int st_kod = Integer.parseInt(requestDocument.getVU36DocRequest().getStationId().substring(0,5));
		
		pp.put("st_kod", st_kod);
		
		String st_name="";
		try{
		st_name = facade.getNpjt().queryForObject("select name from snt.stan where st_kod = :st_kod",pp, String.class);
		} catch (Exception e){
			throw new ServiceException(new Exception(), ServiceError.ERR_STATIONID_EMPTY);
		}
		
		// привязка не по клейму:
		// Enterprise ent = facade.getEnterpriseDao()
		// .getByCode(req.getStationId());
		String roadName = facade.getRoadName(req.getAdministration());
		
		//клеймо из pred
		Enterprise pred = facade.getEnterpriseByKleimo(req.getRepairPredKod());

		String isRZD = "";
		binder.setRootElement("data");
		binder.mergeElement(ETDForm.STAGE_FLAG, ETDForm.STAGE_SERVICE);
		binder.setNodeValue("auto", crtdBySrvs);
		binder.setNodeValue("jd_kod", req.getAdministration());
		binder.setNodeValue("naim_jd", roadName);
		
		String tmpStr = new String((new Integer(codeStan)).toString());
		for (int i = tmpStr.length(); i < 6; i++)
			{
			tmpStr = "0" + tmpStr;
			}
		
		binder.setNodeValue("kod_stan", tmpStr);
		binder.setNodeValue("naim_stanc", st_name);
		binder.setNodeValue("kod_priem", req.getRepairKod());
		binder.setNodeValue("rzd_pred_id", pred.getCode());
		binder.setNodeValue("rzd_dor_id", pred.getDorId());
		binder.setNodeValue("kod_predpr", req.getRepairPredKod());
		if (req.isSetP29())
			{
			binder.setNodeValue("P_29", req.getP29());
			}
		else
			binder.setNodeValue("P_29", "0");
		
		// binder.setNodeValue("vu10_number", number);FIXME
		
		String repairname = null;
		switch (Integer.parseInt(req.getRepairKod()))
			{
			case 0:
				repairname = "замена номера вагона без производства ремонта";
				break;
			case 1:
				repairname = "деповской ремонт вагонов (ДР)";
				break;
			case 2:
				repairname = "капитальный ремонт вагонов (КР)";
				break;
			case 3:
				repairname = "текущий ремонт порожних вагонов при подготовке под погрузку (ТР-1)";
				break;
			case 4:
				repairname = "текущий ремонт вагонов с отцепкой от составов (ТР-2)";
				break;
			case 6:
				repairname = "техническое обслуживание с диагностированием (ТОД)";
				break;
			case 8:
				repairname = "исключение вагона из инвентарного парка";
				break;
			case 9:
				repairname = "разбраковка";
				break;
			default:
				throw new ServiceException(new Exception(), ServiceCode.ERR_UNDEFINED,
						"Невозможно получить название вида ремонта");
			}
		binder.setNodeValue("priem_vagonov", repairname);
		
		// binder.setNodeValue("kod_predpr", req.getRepairPredKod());
		if (pred.getIsSelf().equals("Y"))
			isRZD = "1";
		else
			isRZD = "0";
		binder.setNodeValue("is2RZD", isRZD);
		
		binder.setNodeValue("naim_pred", pred.getName());
		
		binder.fillTable(TypeConverter.arrayToArrayList(req.getVagonArray()),
				new Mapper1(), "table1", "row");
		
		Document document = new Document();
			// последняя версия привязки:
			document.setPredId(pred.getId());
		
			
			
		document.setBlDoc(form.encodeToArchiv());
		document.setDocData(form.transform("data"));
		// document.setNo(String.valueOf(number));FIXME
		document.setSignLvl(0);
		document.setType(templatename);
		Long id = facade.insertDocument(document);
		
//		sendtoetd.SendToEtdMessage(id, new String(document.getBlDoc(), "UTF-8"), templatename, 0, 0, true);	
		
		
		wrapper = new StandartResponseWrapper();
		wrapper.setDocumentId(id);
		wrapper.setCode(ServiceError.ERR_OK.getCode());
		if (getNeedUrl().equals("0"))
			{
			wrapper.setDescription(ServiceError.ERR_OK.getMessage());
			}
		else
			{
			wrapper.setDescription(document.createUrl());
			}

		ResponseAdapter<StandartResponseWrapper> adapter = new ResponseAdapter<StandartResponseWrapper>(
				true, wrapper, ServiceError.ERR_OK);
		return adapter;
		}
	}

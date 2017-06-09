package ru.aisa.rgd.etd.ws;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.oxm.Marshaller;

import ru.aisa.edt.GetReportDocument;
import ru.aisa.edt.GetReportDocument.GetReport;
import ru.aisa.edt.Signature;
import ru.aisa.rgd.etd.dao.ETDFacade;
import ru.aisa.rgd.etd.report.AbstractReport;
import ru.aisa.rgd.ws.exeption.InternalException;



public class ReportEndpoint extends
		ETDAbstractSecurityEndoint<GetReportDocument, GetReportDocument> {
	
	static final Map<String, Class<? extends AbstractReport>> dispather;
	static final Map<String, String> mapper;
	static final Logger	log	= Logger.getLogger(ReportEndpoint.class);
	static
	{
		dispather = new HashMap<String, Class<? extends AbstractReport>>();
		mapper = new HashMap<String, String>();
	}

	//TODO Потом убрать и все первести на DAO
	private NamedParameterJdbcTemplate npjt;

	protected ReportEndpoint(Marshaller marshaller) {
		super(marshaller);
	}

	@Override
	protected GetReportDocument composeInvalidSecurityResponce(Signature signature) 
	{
		GetReportDocument responseDocument = GetReportDocument.Factory.newInstance();
		GetReport response = responseDocument.addNewGetReport();
		response.setForm("0");
		response.setPredid(-1);
		response.addNewSecurity();
		response.getSecurity().setLogonstatus("false");
		response.getSecurity().setSign(new byte[1]);
		response.getSecurity().setCertid("0");
		response.getSecurity().setUsername("0");
		return responseDocument;
	}

	@Override
	protected GetReportDocument convertRequest(Object obj) 
	{
		GetReportDocument responseDocument = (GetReportDocument) obj;
		return responseDocument;
	}

	@Override
	protected Signature getSecurity(GetReportDocument requestDocument) 
	{
		Signature s = requestDocument.getGetReport().getSecurity();
		return s;
	}

	@Override
	protected GetReportDocument processRequest(GetReportDocument requestDocument, Signature signature, ETDFacade facade) throws Exception 
	{
		GetReport request = requestDocument.getGetReport();
		String formName = request.getForm();
		int enterpriseId = request.getPredid();
		SimpleDateFormat df = new SimpleDateFormat("dd.MM.yyyy");
		Date beginDate = df.parse(request.getDate1());
		Date endDate = df.parse(request.getDate2());
		int wrkid = request.getWrkid();
		int dorId = request.getDorId();
		
		String report="";
		String error="";
		
		Class<? extends AbstractReport> clazz = dispather.get(formName);
		String formTemplateName = mapper.get(formName);
		if (clazz == null)
			throw new InternalException("Отчет формы " + formName + " не создан");
		else if (formTemplateName == null)
		{
			throw new InternalException("Название формы для отчета " + formName + " не создано");
		}
		else
		{
			logger.debug("Получен класс отчета " + clazz.toString());
			logger.debug("Получено название формы " + formTemplateName);
			//Создаем объект репортера
			AbstractReport reporter = clazz.newInstance();
			//пД предприятия, по которому создается отчет
			reporter.setEnterpriseId(enterpriseId);
			//Название формы документа
			reporter.setFormTemplateName(formTemplateName);
			//Дата начала отчета
			reporter.setReportBeginDate(beginDate);
			//Дата конца отчета
			reporter.setReportEndDate(endDate);
			//Название формы отчета
			reporter.setReportTemplateName(formName);
			//ID роли
			reporter.setWrkid(wrkid);
			reporter.setDorId(dorId);
			
			Map<String, String> map = reporter.getForm(getSrvFacade()); 
			
			report = map.get("report");
			error = map.get("error");
		}		
		
		if (error.length()>0){
			request.setPredid(-3);
			request.setForm(error);
		}
		else if (report.length()>1) 
		{
			request.setForm(report);
		}
		else
		{
			request.setPredid(-1);
			throw new InternalException("При составлении отчета произошла ошибка");
		}
		return requestDocument;
	}

	public NamedParameterJdbcTemplate getNpjt() {
		return npjt;
	}

	public void setNpjt(NamedParameterJdbcTemplate npjt) {
		this.npjt = npjt;
	}

}

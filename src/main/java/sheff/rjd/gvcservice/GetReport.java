package sheff.rjd.gvcservice;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.DataFormatException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.log4j.Logger;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.oxm.Marshaller;
import org.springframework.ws.server.endpoint.AbstractMarshallingPayloadEndpoint;

import ru.aisa.rgd.etd.objects.DiObject;
import ru.aisa.rgd.etd.objects.ReportData;
import ru.aisa.rgd.ws.dao.ServiceFacade;
import rzd.util.ReportRequestDocument;
import rzd.util.ReportResponseDocument;
import rzd.util.ReportResponseDocument.ReportResponse;
import rzd.util.ReportResponseDocument.ReportResponse.Table1;
import rzd.util.ReportResponseDocument.ReportResponse.Table1.Dor.Preds;
import rzd.util.ReportResponseDocument.ReportResponse.Table1.Dor.Preds.Row;


import rzd.util.ReportResponseDocument.ReportResponse.Table1.Dor;
public class GetReport extends AbstractMarshallingPayloadEndpoint {
    private NamedParameterJdbcTemplate npjt;
    private static Logger	log	= Logger.getLogger(GetReport.class);
	public  GetReport(Marshaller marshaller) {
		super(marshaller);
	}
	
	public NamedParameterJdbcTemplate getNpjt() {
		return npjt;
	}

	public void setNpjt(NamedParameterJdbcTemplate npjt) {
		this.npjt = npjt;
	}
private ServiceFacade facade;
	
	public ServiceFacade getFacade() {
	return facade;
}

public void setFacade(ServiceFacade facade) {
	this.facade = facade;
}

SimpleDateFormat db2format;


	public SimpleDateFormat getDb2format() {
	return db2format;
}

public void setDb2format(SimpleDateFormat db2format) {
	this.db2format = db2format;
}
	
	
	protected Object invokeInternal(final Object inn) throws ParseException  {
//		long start = System.currentTimeMillis();
		ReportRequestDocument req = (ReportRequestDocument) inn;
		SimpleDateFormat myDateFormat = new SimpleDateFormat("dd.MM.yyyy");
		SimpleDateFormat myDateFormat2 = new SimpleDateFormat("dd.MM.yyyy");
	 int acc = 0;
	 int inwork = 0;
	 int decl = 0;
	 int sign = 0;
	 int sf = 0;
	 int summ = 0;
	int summtorek = 0;
		Date date1 = null;
		Date date2 = null;
		int predid = req.getReportRequest().getPredid();
		int type = req.getReportRequest().getTypeid();
		//String formname = types.get(type);
		
		String formdateot = "";
		String formdateto = "";
		
		if (req.getReportRequest().isSetDateot()&&req.getReportRequest().getDateot().length()>2){
			try {
				date1 = myDateFormat.parse(req.getReportRequest().getDateot());
				myDateFormat.applyPattern("yyyy-MM-dd");
				formdateot = myDateFormat.format(date1);
			
			} catch (ParseException e) {
			
				e.printStackTrace();
			}
			
		}
		
		else formdateot ="1970-01-01";
		
		
		if (req.getReportRequest().isSetDateto()&&req.getReportRequest().getDateto().length()>2){
			try {
				myDateFormat.applyPattern("dd.MM.yyyy");
				date2 = myDateFormat.parse(req.getReportRequest().getDateto());
				myDateFormat.applyPattern("yyyy-MM-dd");
				formdateto = myDateFormat.format(date2);
			
			} catch (ParseException e) {
			
				e.printStackTrace();
			}
		}
		else formdateto = getDb2format().format(new Date().getTime()).toString();
		
		ReportResponseDocument resp = ReportResponseDocument.Factory.newInstance();
		ReportResponse rr = resp.addNewReportResponse(); 
		
		Date maxdate = new Date();
		String maxdatefrombase = (String)facade.getNpjt().queryForObject("select max(rdate)+1 day from snt.torekreports", new HashMap(), String.class);
		maxdate = myDateFormat.parse(maxdatefrombase);
		
		
		rr.setP3(myDateFormat2.format(maxdate)+ " 02:00");
		List<DiObject> dors = facade.getReportDao().getDors(); 
		List<ReportData> repdata = new ArrayList<ReportData>();
		
		try{
		repdata = facade.getReportDao().getReportData(predid, type, formdateot, formdateto);
		} catch (Exception e){
			e.printStackTrace();
		}
		int i =0;
		Table1 table = rr.addNewTable1();
		while (i<dors.size()){
			 int acc1 = 0;
			 int inwork1 = 0;
			 int decl1 = 0;
			 int sign1 = 0;
			 int sf1 = 0;
			 int summ1 = 0; 
			 int summtorek1 = 0;
			Dor dor = table.addNewDor();
			dor.setDorname("Всего по "+dors.get(i).getName());
			int count = 0;
			for (int j=0; j< repdata.size(); j++){
				
				Preds pred = dor.addNewPreds();
				if (repdata.get(j).getDorid()==dors.get(i).getId()){
					count = count+1;
					dor.setFlag(true);
					Row row = pred.addNewRow();
					row.setAccepted(repdata.get(j).getAccepted());
					row.setDeclined(repdata.get(j).getDeclined());
					row.setRecieved(repdata.get(j).getRecieved());
					row.setRemname(repdata.get(j).getRepname());
					row.setSigned(repdata.get(j).getSigned());
					row.setSumm(repdata.get(j).getSumm());
					row.setSf(repdata.get(j).getSf());
					row.setNumber(count);
					row.setP3Preds(repdata.get(j).getSummtorek());
					//new
					acc1 = acc1+ repdata.get(j).getRecieved();
					//inwork
					inwork1 = inwork1+repdata.get(j).getAccepted();
					//declined
					decl1 = decl1+repdata.get(j).getDeclined();
					//signed
					sign1 = sign1+repdata.get(j).getSigned();
					//summ
					summ1 = summ1+repdata.get(j).getSumm();
					//
					sf1 = sf1+repdata.get(j).getSf();
					
					summtorek1= summtorek1+repdata.get(j).getSummtorek();
				}
				
				
				
			}
			dor.setAccepted(inwork1);
			dor.setDeclined(decl1);
			dor.setSigned(sign1);
			dor.setRecieved(acc1);
			dor.setSumm(summ1);
			dor.setSf(sf1);
			dor.setP3Dor(summtorek1);
			inwork = inwork+inwork1;
			acc = acc+ acc1;
			decl = decl+decl1;
			sign = sign+sign1;
			sf = sf+sf1;
			summ = summ+summ1;
			summtorek = summtorek+summtorek1;
			i++;
			
		}
		
		Dor dor = table.addNewDor();
		dor.setDorname("Всего по всем ДИ");
		dor.setAccepted(inwork);
		dor.setDeclined(decl);
		dor.setFlag(false);
		dor.setRecieved(acc);
		dor.setSigned(sign);
		dor.setSumm(summ);
		dor.setSf(sf);
		dor.setP3Dor(summtorek);
	//System.out.println(resp);
	//System.out.println(System.currentTimeMillis()-start);
		return resp;		
	}
}


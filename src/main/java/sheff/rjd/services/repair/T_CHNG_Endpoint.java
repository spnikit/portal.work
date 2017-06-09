package sheff.rjd.services.repair;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.ParameterizedRowMapper;
import org.springframework.oxm.Marshaller;

import ru.aisa.rgd.etd.objects.ETDDocument;
import ru.aisa.rgd.ws.dao.ServiceFacade;
import ru.aisa.rgd.ws.documents.StandartResponseWrapper;
import ru.aisa.rgd.ws.endpoint.ETDAbstractEndpoint;
import ru.aisa.rgd.ws.endpoint.ResponseAdapter;
import ru.aisa.rgd.ws.exeption.ServiceError;
import ru.iit.repair.torek.TCHNGRequestDocument;
import ru.iit.repair.torek.TCHNGRequestDocument.TCHNGRequest;
import ru.iit.repair.torek.TCHNGResponseDocument;
import ru.iit.repair.torek.TCHNGResponseDocument.TCHNGResponse;
import ru.iit.repair.torek.CHNGTIDDATA;
import sheff.rjd.utils.TOREK_WRK_DateAppender;
import sheff.rjd.ws.OCO.ServiceTypes;

public class T_CHNG_Endpoint extends
		ETDAbstractEndpoint<T_ChngWrapper> {

	private static Logger log = Logger.getLogger(T_CHNG_Endpoint.class);
	private ServiceFacade facade;
	private NamedParameterJdbcTemplate npjt;
	public ServiceFacade getFacade() {
		return facade;
	}

	public void setFacade(ServiceFacade facade) {
		this.facade = facade;
	}

	public NamedParameterJdbcTemplate getNpjt() {
		return npjt;
	}

	public void setNpjt(NamedParameterJdbcTemplate npjt) {
		this.npjt = npjt;
	}
	
	private static HashMap<Integer, String> statustext = new HashMap<Integer, String>();
	static{
		statustext.put(1, "Отклонение входящего");
		statustext.put(2, "Подпись входящего");
		statustext.put(3, "Создан исходящий");
	}
	private static final String sql = "select docid, torekid, packid, dt, "+
" case when (select dropid from snt.docstore where id = vrk.docid ) is null then 2 else 1 end as code "+
" from snt.vrkids vrk where docid in (select id from snt.docstore where typeid in "+
" (select id from snt.doctype where name in ('Комплект на пересылку в ремонт')) "+
" and id in (select docid from snt.vrkids where dt >:dt)) "+
" union "+
" select docid, torekid, packid, dt, 3 as code from snt.vrkids vrk where docid in (select id from snt.docstore where typeid in "+
" (select id from snt.doctype where name in ('Комплект ремонтопригодности', 'Комплект завершение ремонта')) "+
" and id in (select docid from snt.vrkids where dt >:dt))";
	
	public T_CHNG_Endpoint(Marshaller marshaller) {
		super(marshaller);
	}

	@Override
	protected ResponseAdapter<T_ChngWrapper> processRequest(
			Object arg, ServiceFacade facade) throws Exception {

		final TCHNGRequestDocument requestDocument = (TCHNGRequestDocument) arg;
		final TCHNGRequest request = requestDocument.getTCHNGRequest();

		T_ChngWrapper wrapper = new T_ChngWrapper();
		TOREK_WRK_DateAppender dateadapter = new TOREK_WRK_DateAppender();
//		System.out.println(chngdate.fromtorek(request.getTimestamp()));
		String chdate = dateadapter.fromtorek(request.getTimestamp());
		
		HashMap<String, Object> pp = new HashMap<String, Object>();
		pp.put("dt", chdate);
		List<responsechdata> result = npjt.query(
				sql, pp, new ParameterizedRowMapper<responsechdata>() {
					public responsechdata mapRow(ResultSet rs, int n)
							throws SQLException {
						responsechdata obj = new responsechdata();
						obj.setCode(rs.getInt("CODE"));
						obj.setDate(rs.getString("DT"));
						obj.setDescription(statustext.get(obj.getCode()));
						if (obj.getCode()==1)
						obj.setDocid(rs.getString("TOREKID"));
						obj.setOperdocid(rs.getString("DOCID"));
						return obj;
					}
				});
		
		responsechdata[] response = new responsechdata[result.size()];
		for (int z=0;z<result.size();z++){
//			signvrkservice.SendDocs(1, docs.get(z));
			response[z] = new responsechdata();
			if (!"".equals(result.get(z).getDocid()))
			response[z].setDocid(result.get(z).getDocid());
			response[z].setOperdocid(result.get(z).getOperdocid());
			response[z].setDate(dateadapter.totorek(result.get(z).getDate()));
			response[z].setCode(result.get(z).getCode());
			response[z].setDescription(result.get(z).getDescription());
			}
		
		wrapper.setDocdata(response);
		
		ResponseAdapter<T_ChngWrapper> adapter = new ResponseAdapter<T_ChngWrapper>(
				true, wrapper, ServiceError.ERR_OK);
		return adapter;

	}

	@Override
	protected Object composeResponce(
			ResponseAdapter<T_ChngWrapper> adapter) {
		TCHNGResponseDocument responsedoc = TCHNGResponseDocument.Factory.newInstance();
		TCHNGResponse response = responsedoc.addNewTCHNGResponse();
		
		if (adapter.getResponse().getDocdata().length>0){
			for (int i=0; i<adapter.getResponse().getDocdata().length; i++){
			CHNGTIDDATA data = response.addNewIDDATA();
			data.setCode(String.valueOf(adapter.getResponse().getDocdata()[i].getCode()));
			data.setDescription(adapter.getResponse().getDocdata()[i].getDescription());
			if (!"".equals(adapter.getResponse().getDocdata()[i].getDocid()))
			data.setDOCID(adapter.getResponse().getDocdata()[i].getDocid());
			data.setOperDocID(adapter.getResponse().getDocdata()[i].getOperdocid());
			data.setTimestamp(adapter.getResponse().getDocdata()[i].getDate());
		}
		}
		
		return responsedoc;
	}
	
	
}
		class responsechdata{
			int code;
			String description;
			String docid = "";
			String operdocid;
			String date;
			public int getCode() {
				return code;
			}
			public void setCode(int code) {
				this.code = code;
			}
			public String getDescription() {
				return description;
			}
			public void setDescription(String description) {
				this.description = description;
			}
			public String getDocid() {
				return docid;
			}
			public void setDocid(String docid) {
				this.docid = docid;
			}
			public String getOperdocid() {
				return operdocid;
			}
			public void setOperdocid(String operdocid) {
				this.operdocid = operdocid;
			}
			public String getDate() {
				return date;
			}
			public void setDate(String date) {
				this.date = date;
			}
			
			
		}
		
		class T_ChngWrapper extends StandartResponseWrapper
		{
			private responsechdata[]	docdata;

			public responsechdata[] getDocdata() {
				return docdata;
			}

			public void setDocdata(responsechdata[] docdata) {
				this.docdata = docdata;
			}

			
			
			
		}

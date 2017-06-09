package sheff.rjd.services.repair;

import java.io.UnsupportedEncodingException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import org.apache.log4j.Logger;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.oxm.Marshaller;
import org.springframework.security.crypto.codec.Base64;

import ru.aisa.rgd.ws.dao.ServiceFacade;
import ru.aisa.rgd.ws.documents.StandartResponseWrapper;
import ru.aisa.rgd.ws.endpoint.ETDAbstractEndpoint;
import ru.aisa.rgd.ws.endpoint.ResponseAdapter;
import ru.aisa.rgd.ws.exeption.ServiceError;
import ru.aisa.rgd.ws.utility.TypeConverter;
import ru.iit.repair.torek.PACKTPROW;
import ru.iit.repair.torek.TPACKRequestDocument;
import ru.iit.repair.torek.TPACKRequestDocument.TPACKRequest;
import ru.iit.repair.torek.TPACKResponseDocument;
import ru.iit.repair.torek.TPACKResponseDocument.TPACKResponse;
import ru.iit.repair.torek.PACKTEDSTAB;
public class T_PACK_Endpoint extends
		ETDAbstractEndpoint<T_PackWrapper> {

	private static Logger log = Logger.getLogger(T_PACK_Endpoint.class);
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
	private static final String sqlforC04 = "select id, (select torekid from snt.vrkids where docid = ds.id), "+
    " case when dropid is null then 2 else 1 end as code from snt.docstore ds where id = :id";
	
	private static final String sqlforElse = "select id, (select torekid from snt.vrkids where docid = ds.id), "+
		    " 3 as code from snt.docstore ds where id = :id";
	
	private static HashMap<Integer, String> statustext = new HashMap<Integer, String>();
	static{
		statustext.put(0, "Статус не изменен");
		statustext.put(1, "Отклонение входящего");
		statustext.put(2, "Подпись входящего");
		statustext.put(3, "Создан исходящий");
	}
	private static final String C04 = "Комплект на пересылку в ремонт";
	private static final String B01 = "Акт приема-передачи КП в ремонт";
	private static final String B02 = "Акт приема-передачи ЗЧ в ремонт";
	public T_PACK_Endpoint(Marshaller marshaller) {
		super(marshaller);
	}

	@Override
	protected ResponseAdapter<T_PackWrapper> processRequest(
			Object arg, ServiceFacade facade) throws Exception {

		final TPACKRequestDocument requestDocument = (TPACKRequestDocument) arg;
		final TPACKRequest request = requestDocument.getTPACKRequest();

		T_PackWrapper wrapper = new T_PackWrapper();
		long docid = Long.valueOf(request.getTorPackID());
		HashMap<String, Object> pp = new HashMap<String, Object>();
		pp.put("id", docid);
		final String packname = (String)npjt.queryForObject("select rtrim(name) from snt.doctype where id = (select typeid from snt.docstore where id = :id)", pp, String.class);
		
		
		//Инф-я по самому пакету
		String sqlforpack;
		
		
		//Определяем входящий или исходящий пакет по torekid
		final int income = npjt.queryForInt("select case when torekid is null then 0 else 1 end from snt.vrkids where docid = :id", pp);
		
		if (packname.equals(C04) /*income ==1*/){
			
			sqlforpack =sqlforC04;
				
		}else {
			sqlforpack = sqlforElse;
		}
			packdata pack = npjt.queryForObject(sqlforpack, pp, new RowMapper<packdata>() {

				public packdata mapRow(ResultSet rs, int rowNum) throws SQLException {
					packdata pack = new packdata();
					pack.setCode(rs.getInt("CODE"));
					pack.setCodestr(String.valueOf(pack.getCode()));
					pack.setDescription(statustext.get(pack.getCode()));
					
					if (packname.equals(C04)/*&&income==1*/)
					
					pack.setTorpackid(pack.getCode()==1?rs.getString("TOREKID"):rs.getString("ID"));
					else pack.setTorpackid(rs.getString("ID"));
					return pack;
				}
			});
			

			wrapper.setPack(pack);
		
			
			
		//Сбор данных по документам
		List<TOREK_DocumentObject> documentlist = new ArrayList<TOREK_DocumentObject>();
		
		List<Map<String, Object>> docids = npjt.queryForList("select docid from snt.vrkids where packid = (select packid from snt.vrkids where docid = :id) ", pp);
		try{
		for (int i=0;i<docids.size();i++){
			
			//по каждому документу
//			final TOREK_DocumentObject object = new TOREK_DocumentObject();
			
			Long packdoc = Long.valueOf(docids.get(i).get("DOCID").toString());
//			object.setPortalid(packdoc);
			pp.put("packdoc", packdoc);
			
			int ischangetime = npjt.queryForInt("select case when dt is null then 0 else 1 end from snt.vrkids where docid = :packdoc ", pp);
			
			//определяем код документа
			//для все документов внутри С04 статус будет 0, 1 или 2
			//для документов внутри С05 и С06 статус будет 0 или 3
			//Бред? а то!
			int code;

			if (ischangetime==1){
				if (packname.equals(C04)){
						code = npjt.queryForInt("select case when dropid is null then 2 else 1 end as code from snt.docstore ds where id = :id", pp);
				} else {
					code = 3;
				}
			} else {
				code = 0;
			}
			
			//Получаем количество подписей
			int countsgns = npjt.queryForInt("select count(distinct(sign)) from snt.vrkdocflow where docid =:packdoc", pp);
			List<SignInfoObject> signlist= new ArrayList<SignInfoObject>();
			
			//Имя шаблона
			String formname = (String)npjt.queryForObject("select rtrim(name) from snt.doctype where id = (select typeid from snt.docstore where id=:packdoc)", pp, String.class);
			
			
			for (int j=0;j<countsgns;j++){
			//по каждой подписи берем информацию
			int signnumber = j+1;
			pp.put("countsgn", signnumber);
			byte[] sign = (byte[])npjt.queryForObject("select binary from snt.vrkdocflow where docid =:packdoc and sign = :countsgn fetch first row only", pp, byte[].class);
			
			if (income==0&&ischangetime==1){
				if (formname.equals(B01)||formname.equals(B02)||formname.equals(C04)){
					signnumber++;
				}
			}
			
			SignInfoObject signinfo = new SignInfoObject(signnumber, sign);
			signlist.add(signinfo);
			}
			
			//берем время изменения дабы понять менялся документ аль нет
			
			
			
					
			
			
//			else if (ischangetime==1&&income==0){
//			code=3;
//			}else {
//				code = npjt.queryForInt("select case when dropid is null then 2 else 1 end as code from snt.docstore ds where id = :id", pp);
//			}
			
			
			
			
			
			//Тип документа
			String doctype = (String)npjt.queryForObject("select rtrim(no) from snt.docstore where id=:packdoc", pp, String.class);
			//XML в байтах
			byte[]xml  = (byte[])npjt.queryForObject("select bldoc from snt.docstore where id=:packdoc", pp, byte[].class);
			
			String torekid;
			if (income==1){
				torekid = npjt.queryForObject("select torekid from snt.vrkids where docid = :packdoc", pp, String.class);
			}else {
				torekid = packdoc.toString();
			}
			//Определяем причину отклонения и генерим ее в нужном формате
			String declinereason="";
			if (code==1){
			declinereason = GetDeclineReason(packdoc);
			}
			final TOREK_DocumentObject object = new TOREK_DocumentObject(formname, doctype, xml, signlist, torekid, code, declinereason);
//			object.setDocstatus(code);
			documentlist.add(object);
		}
		}catch (Exception e){
			log.error(TypeConverter.exceptionToString(e));
		}
		wrapper.setDocs(documentlist);
		
		ResponseAdapter<T_PackWrapper> adapter = new ResponseAdapter<T_PackWrapper>(
				true, wrapper, ServiceError.ERR_OK);
		return adapter;

	}

	@Override
	protected Object composeResponce(
			ResponseAdapter<T_PackWrapper> adapter) {
		TPACKResponseDocument responsedoc = TPACKResponseDocument.Factory.newInstance();
		TPACKResponse response = responsedoc.addNewTPACKResponse();
		
		response.setCode(adapter.getResponse().getPack().getCodestr());
		response.setDescription(adapter.getResponse().getPack().getDescription());
		response.setTorPackID(adapter.getResponse().getPack().getTorpackid());
		try{
		if (adapter.getResponse().getDocs().size()>0){
			for (int i=0; i<adapter.getResponse().getDocs().size(); i++){
			PACKTPROW row = response.addNewPACKAGE();
			row.setCONTENT(new String(Base64.encode(adapter.getResponse().getDocs().get(i).getBinary()),"UTF-8"));
			row.setDOCID(adapter.getResponse().getDocs().get(i).getTorekid());
			row.setDOCSPEC(adapter.getResponse().getDocs().get(i).getDocspec());
			row.setDOCSTATUS(adapter.getResponse().getDocs().get(i).getDocstatus());
			row.setDOCTYPE(adapter.getResponse().getDocs().get(i).getDoctype());
			if (adapter.getResponse().getDocs().get(i).getReason().length()>0){
				row.setREJECTREASON(adapter.getResponse().getDocs().get(i).getReason());
			}
			
			for (int j=0; j<adapter.getResponse().getDocs().get(i).getSigns().size(); j++){
			PACKTEDSTAB sign = row.addNewEDSTAB();
			sign.setSIGNNUM(adapter.getResponse().getDocs().get(i).getSigns().get(j).getSign_num());
			sign.setEDS(new String(Base64.encode(adapter.getResponse().getDocs().get(i).getSigns().get(j).getBinary()), "UTF-8"));
			}
			}
		}
		}catch (Exception e){
		e.printStackTrace();	
		log.error(TypeConverter.exceptionToString(e));
		}
		
		return responsedoc;
	}
	
	private String GetDeclineReason(Long docid) throws UnsupportedEncodingException{
		HashMap<String, Object> pp = new HashMap<String, Object>();
		pp.put("DOCID", docid);
		final StringBuffer sb = new StringBuffer();
		String sql  = "select id, (select rtrim(cast(rtrim(fname) || ' ' || rtrim(mname) || ' ' || rtrim(lname) as char(240) ccsid unicode)) "+
 " from SNT.personall where id = ds.dropid) fio, "+
" case when (select title from snt.personall where id =ds.dropid) is null or (select title from snt.personall where id =ds.dropid)='' then "+
" (select rtrim(name) from snt.wrkname where id = ds.dropwrkid) "+
" else (select title from snt.personall where id =ds.dropid) end as Duty, "+
" date(droptime) dropdate, droptxt "+
" from snt.docstore ds where id = :DOCID";
		npjt.queryForObject(sql, pp, new RowMapper<Object>() {

			public packdata mapRow(ResultSet rs, int rowNum) throws SQLException {
//				sb.append("<"+rs.getString("FIO")+">");
//				sb.append("h");
//				sb.append("<"+rs.getString("DUTY")+">");
//				sb.append("h");
//				SimpleDateFormat sdf = new SimpleDateFormat();
//				sdf.applyPattern("yyyyMMdd");
//				sb.append("<"+sdf.format(rs.getDate("DROPDATE"))+">");
//				sb.append("h");
//				sb.append("<"+rs.getString("DROPTXT")+">");
				
				sb.append(rs.getString("FIO"));
				sb.append("h");
				sb.append(rs.getString("DUTY"));
				sb.append("h");
				SimpleDateFormat sdf = new SimpleDateFormat();
				sdf.applyPattern("yyyyMMdd");
				sb.append(sdf.format(rs.getDate("DROPDATE")));
				sb.append("h");
				sb.append(rs.getString("DROPTXT"));
				return null;
			}
		});
		
		return new String(sb.toString().getBytes("UTF-8"));
	}
}
		
		class packdata{
			String codestr;
			String description;
			String torpackid;
			int code;
			public String getCodestr() {
				return codestr;
			}
			public void setCodestr(String codestr) {
				this.codestr = codestr;
			}
			public String getDescription() {
				return description;
			}
			public void setDescription(String description) {
				this.description = description;
			}
			public String getTorpackid() {
				return torpackid;
			}
			public void setTorpackid(String torpackid) {
				this.torpackid = torpackid;
			}
			public int getCode() {
				return code;
			}
			public void setCode(int code) {
				this.code = code;
			}
			
			
		}
		class T_PackWrapper extends StandartResponseWrapper
		{
			private packdata pack;
			
			List<TOREK_DocumentObject> docs;

			

			public List<TOREK_DocumentObject> getDocs() {
				return docs;
			}

			public void setDocs(List<TOREK_DocumentObject> docs) {
				this.docs = docs;
			}

			public packdata getPack() {
				return pack;
			}

			public void setPack(packdata pack) {
				this.pack = pack;
			}

			
			
			
		}
		
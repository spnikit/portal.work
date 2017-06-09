package sheff.rjd.services.transoil;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.oxm.Marshaller;
import org.springframework.ws.server.endpoint.AbstractMarshallingPayloadEndpoint;

import ru.iit.portal.statusDoc.StatusDocRequestDocument;
import ru.iit.portal.statusDoc.StatusDocRequestDocument.StatusDocRequest;
import ru.iit.portal.statusDoc.StatusDocResponseDocument;
import ru.iit.portal.statusDoc.StatusDocResponseDocument.StatusDocResponse;

public class StatusDocEndpoint extends
	AbstractMarshallingPayloadEndpoint {

    private static Log log = LogFactory
	    .getLog(StatusDocEndpoint.class);

  
    private static final String sfstatus = "select case when dropid is null then 1 else 0 end from snt.docstore where etdid = :ETDID";
    private static final String packstatus = "select case when (signlvl is not null and dropid is null) or (signlvl is null and dropid is null and groupsgn = 0) "
    		+ "then 1 else 0 end from snt.docstore where etdid = :ETDID";
    private NamedParameterJdbcTemplate npjt;
  

    public NamedParameterJdbcTemplate getNpjt() {
	return npjt;
    }

    public void setNpjt(NamedParameterJdbcTemplate npjt) {
	this.npjt = npjt;
    }

    public StatusDocEndpoint(Marshaller marshaller) {
	super(marshaller);
    }

   
    protected Object invokeInternal(Object obj) {
		
   
    	
    	StatusDocRequestDocument requestdoc = (StatusDocRequestDocument) obj;
    	StatusDocRequest request = requestdoc.getStatusDocRequest();
    	StatusDocResponseDocument respdoc = StatusDocResponseDocument.Factory.newInstance();
    	StatusDocResponse resp = respdoc.addNewStatusDocResponse();
    	String reason = "";
    	String status="";
    	int statusid=-1;
    	int count;
    	long etdid = request.getEtddocid();
    	if (request.isSetCode()&&!request.isNilCode()){
    	statusid = request.getCode();
    	
    	}
    	if (request.isSetStatus()&&!request.isNilStatus()){
    		status=request.getStatus();
    		
    	}
    	if (request.isSetReason()&&!request.isNilReason()){
    		reason = request.getReason();
    	}
    	
    	
    	Map<String, Object> pp = new HashMap<String, Object>();
    	pp.put("ETDID", etdid);
    	pp.put("packname", "Пакет документов");
    	pp.put("sfname", "Счет-фактура");
    	
    	
    	count = npjt.queryForInt("select count(id) from snt.docstore where etdid = :ETDID and typeid in "
    			+ "(select id from snt.doctype where name like '%Пакет документов%' or name like '%Счет-фактура%' "
    			+ " or rtrim(name) in ('ГУ-45', 'ГУ-2б', 'ГУ-2в', 'ГУ-23'))", pp);
    	//System.out.println(count);
    	
    	if (request.isSetCode()&&!request.isNilCode()&&statusid<0&&statusid>4){
    		resp.setCode(-3);
    		resp.setDescription("Код статуса может принимать значения только от 0 до 4");
    		resp.setEtddocid(etdid);
    	}
    	
    	else if (count>0){
    		try{
    			
    	String typename = (String) getNpjt().queryForObject("select rtrim(name) from snt.doctype where id = (select typeid from snt.docstore where etdid = :ETDID)", pp, String.class);
    	
    	int marshdoc = -1;		
    			
    	if (typename.contains("Счет-фактура")){
    		marshdoc = getNpjt().queryForInt(sfstatus, pp);
    	} else {
    		marshdoc = getNpjt().queryForInt(packstatus, pp);
    	}
    	
    		if (marshdoc ==1)	{
    	String content = (String) npjt.queryForObject("select opisanie from snt.docstore where etdid = :ETDID", pp, String.class);

    	
    	
    	if (request.isSetCode()){
    		pp.put("stat", statusid);
    		npjt.update("update snt.docstore set stat = :stat where etdid = :ETDID", pp);
    	}
    	if (request.isSetStatus()){
    		if (content.indexOf(" Статус заказчика")>-1){
    			content= content.substring(0, content.indexOf(" Статус заказчика"));
    		}
    		if(status.length()>0){
    		pp.put("content", content+" Статус заказчика: "+status);
    		}else {
    			pp.put("content", content);
    		}
    		pp.put("droptxt", status.concat("&&"));
    		npjt.update("update snt.docstore set opisanie = :content,droptxt = :droptxt where etdid = :ETDID", pp);
    		
    	}
    	if (!"".equals(reason)&&request.isSetReason()){
    		pp.put("droptxt", status.concat("&&").concat(reason));
    		npjt.update("update snt.docstore set droptxt = :droptxt where etdid = :ETDID", pp);
    		
    	} else if (request.isSetReason()){
    			npjt.update("update snt.docstore set droptxt = null where etdid = :ETDID", pp);
    	}
    	
//    	npjt.update("update snt.docstore set droptxt = :droptxt, opisanie = :content, stat = :stat where etdid = :ETDID", pp);
    	
    	
    	
    	resp.setCode(0);
    	resp.setDescription("ok");
    	resp.setEtddocid(etdid);
    	} else if (marshdoc==0){
    		resp.setCode(-4);
    		resp.setDescription("Документ находится в архиве или отклонен");
    		resp.setEtddocid(etdid);
    	}
    	} catch (Exception e) {
    		resp.setCode(-2);
    		resp.setDescription("Ошибка в БД при обновлении статуса");
    		resp.setEtddocid(etdid);
		}
    	
    	} else {
    		resp.setCode(-1);
    		resp.setDescription("В системе отсутствует документ с таким ID");
    		resp.setEtddocid(etdid);
    	}
    	
    	return respdoc;
    }

}



package ru.aisa.transoildocumentsender;

import org.apache.log4j.Logger;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.ParameterizedRowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcTemplate;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import sheff.rjd.services.transoil.TransService;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class TransoilDocumentSender {
    private static Logger log = Logger.getLogger(TransoilDocumentSender.class);

    private final String sql = "select ds.id, ds.typeid, ds.etdid, trim(dt.name) as formname,pack.id_pak, ds.docdata, ds.bldoc from snt.docstore ds "
    							+"join snt.pred p on (ds.predid = p.id) "
    							+"join snt.packages pack on (pack.etdid= ds.etdid) "
    							+"join snt.doctype dt on dt.id = ds.typeid "
    							//+"where p.vname = 'ООО «Трансойл»' "
    							//+"and pack.id_pak in (select id_pak from snt.docstore where typeid = (select id from snt.doctype where name = 'Пакет документов')) and "
    							+"where pack.send = 0 FETCH FIRST 30 ROWS ONLY";
    private final String updateTransoilTableSql = "update snt.packages set send = 1 where etdid = :etdid and id_pak = :id_pak";
    private TransService trans;
    private ThreadPoolTaskExecutor taskExecutor;
    //private Timer timer;
   // private static long PERIOD = 1 * 60 * 1000;
   // private static long DELAY = 1 * 60 * 1000;
    private NamedParameterJdbcTemplate npjt;
    private SimpleJdbcTemplate sjt;
    private List<Object[]> dataUpadateQueryList = new ArrayList<Object[]>();
    
    public void initTask() {
        log.debug("Init transOil task");
        //timer.schedule(new TimerTask() {
        taskExecutor.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    log.debug("Start send files");
//                    System.out.println("dadaad");
                    sendFiles();
                    log.debug("End send files");
                } catch (Exception e) {
                    log.error("Cannot execute transoil task", e);
                }
            }
        }/*, DELAY, PERIOD*/);
    }

    void sendFiles() {
    	Map<String, Object> paramMap = new HashMap<String, Object>();
        List<DataObject> list = getNpjt().query(
                sql, paramMap, new ParameterizedRowMapper<DataObject>() {
                    public DataObject mapRow(ResultSet rs, int n)
                            throws SQLException {
                        DataObject obj = new DataObject();
                        obj.setEtdid(rs.getLong("etdid"));
                        obj.setIdPack(rs.getString("id_pak"));
                        obj.setDocdata(rs.getString("docdata"));
                        obj.setBldoc(new String(rs.getBytes("bldoc")));
                        obj.setFormname(rs.getString("formname"));
                        return obj;
                    }
                });
     
        for (DataObject dataObject : list) {
            sendFile(dataObject);
        }
//        if(!dataUpadateQueryList.isEmpty()){
//        	getSjt().batchUpdate(updateTransoilTableSql, dataUpadateQueryList);
//        }
    }

    private void sendFile(final DataObject dataObject) {
    	final Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("etdid", dataObject.getEtdid());
        paramMap.put("id_pak", dataObject.getIdPack());
    	taskExecutor.execute(new Runnable() {
            public void run() {
                    boolean isSend = trans.SendtoTransoil(dataObject.getEtdid(), dataObject.getFormname(), dataObject.getDocdata(), dataObject.getBldoc());
                    log.debug("File sended " + dataObject.getEtdid());
                    if(isSend){
//                    	dataUpadateQueryList.add(new Object[]{dataObject.getEtdid(),dataObject.getIdPack()});
//                        getSjt().update(updateTransoilTableSql, new Object[]{dataObject.getEtdid(),dataObject.getIdPack()});
                        npjt.update(updateTransoilTableSql, paramMap);
                    	
                    }else{
                    	log.error("Cannot send file " + dataObject.getEtdid());
                    }
             
            }
        });
    	
    }

    public void setTrans(TransService trans) {
        this.trans = trans;
    }

   /* public void setTimer(Timer timer) {
        this.timer = timer;
    }*/

    public void setTaskExecutor(ThreadPoolTaskExecutor taskExecutor) {
        this.taskExecutor = taskExecutor;
    }

    public void setNpjt(NamedParameterJdbcTemplate npjt) {
        this.npjt = npjt;
    }
    
    public NamedParameterJdbcTemplate getNpjt() {
		return npjt;
	}

	public SimpleJdbcTemplate getSjt() {
		return sjt;
	}

	public void setSjt(SimpleJdbcTemplate sjt) {
		this.sjt = sjt;
	}
	

	class DataObject {
        private long etdid;
        private String docdata;
        private String bldoc;
        private String formname;
        private String idPack;
        
        public long getEtdid() {
            return etdid;
        }

        public void setEtdid(long etdid) {
            this.etdid = etdid;
        }

        public String getDocdata() {
            return docdata;
        }

        public void setDocdata(String docdata) {
            this.docdata = docdata;
        }

        public String getBldoc() {
            return bldoc;
        }

        public void setBldoc(String bldoc) {
            this.bldoc = bldoc;
        }

        public String getFormname() {
            return formname;
        }

		public String getIdPack() {
			return idPack;
		}

		public void setIdPack(String idPack) {
			this.idPack = idPack;
		}

		public void setFormname(String formname) {
			this.formname = formname;
		}
        
    }

}
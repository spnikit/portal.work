package sheff.rjd.services.etd;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.log4j.Logger;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.ParameterizedRowMapper;
import org.springframework.ws.client.core.WebServiceTemplate;

import com.aisa.portal.invoice.integration.facade.PortalSFFacade;
import com.aisa.portal.invoice.notice.NoticeObject;
import com.aisa.portal.invoice.notice.Uvedparser;

import ru.aisa.rgd.etd.dao.ETDUrlDAO;
import ru.aisa.rgd.ws.domain.SFinvoice;
import ru.aisa.rgd.ws.utility.TypeConverter;
import ru.iit.sFxmltotrans.SFXMLtype;
import ru.iit.sFxmltotrans.SFxmlDocument;
import ru.iit.sFxmltotrans.SFxmlRequest;
import ru.iit.sFxmltotrans.XMLtype;
import ru.iit.sFxmltotrans.SFxmlDocument.SFxml;
import ru.iit.sFxmltotrans.SFxmlRequest.Xmltable;
import rzd8888.gvc.etd.was.etd.recievenotice.RecieveNoticeRequestDocument;
import rzd8888.gvc.etd.was.etd.recievenotice.RecieveNoticeRequestDocument.RecieveNoticeRequest;
import sheff.rjd.utils.Base64;

public class TransService {

	private NamedParameterJdbcTemplate npjt;
	private WebServiceTemplate wst;
	private boolean send;
	private static Logger log = Logger.getLogger(TransService.class);
	private ETDUrlDAO etdurldao;
	private PortalSFFacade portalSFFacade;
	
	
	public PortalSFFacade getPortalSFFacade() {
		return portalSFFacade;
	}
	public void setPortalSFFacade(PortalSFFacade portalSFFacade) {
		this.portalSFFacade = portalSFFacade;
	}
	public NamedParameterJdbcTemplate getNpjt() {
		return npjt;
	}
	public void setNpjt(NamedParameterJdbcTemplate npjt) {
		this.npjt = npjt;
	}
	public WebServiceTemplate getWst() {
		return wst;
	}
	public void setWst(WebServiceTemplate wst) {
		this.wst = wst;
	}
	public boolean isSend() {
		return send;
	}
	public void setSend(boolean send) {
		this.send = send;
	}
	public ETDUrlDAO getEtdurldao() {
		return etdurldao;
	}
	public void setEtdurldao(ETDUrlDAO etdurldao) {
		this.etdurldao = etdurldao;
	}
	public void SendSFNotice(long docid, int number, boolean isTrueTypeSend){
		HashMap<String, Object> pred = new HashMap<String, Object>();
		pred.put("docid", docid);
		String SFreqvisits  = "select onbaseid, (select rtrim(name) vchde from snt.rem_pred where kleim = ds.onbaseid), "+
				"(select rtrim(name) di from snt.dor where id = (select dorid from snt.rem_pred where kleim = ds.onbaseid)), "+
				"vagnum, id_pak as pack_id from snt.docstore ds where id= :docid fetch first row only";

		String getDocumentTypeSql = "select trim(dt.name) from snt.docstore ds, snt.doctype dt where dt.id = ds.typeid and  ds.id = :docid";
		String DocumentType = npjt.queryForObject(getDocumentTypeSql, pred, String.class);
		
		//		System.out.println(isSend());
		//		System.out.println(predname.indexOf(trns));
		if (isSend()){

			try{

				Long etdid = npjt.queryForLong("select etdid from snt.docstore where id =:docid", pred);
				//		SFinvoice req = getinvoicebyid(docid);
				SFinvoice req = getexactinvoicebyid(docid, number);
				if(DocumentType.equals("Счет-фактура РТК") && req.getType().equals("SNC") && isTrueTypeSend){
					RecieveNoticeRequestDocument xmldoc = RecieveNoticeRequestDocument.Factory.newInstance();
					RecieveNoticeRequest xmlreq  = xmldoc.addNewRecieveNoticeRequest();
					xmlreq.setXml(new String(req.getSf_d1(),"windows-1251" ));
					xmlreq.setConfirmid(req.getSf_vs1());
					xmlreq.setEtdid(etdid);
					xmlreq.setXtype(req.getType());
					xmlreq.setSign(req.getSf_s1());
					boolean send =true;
					Object r = null;

					try {
						if (send){
							//				System.out.println(sfdoc);
							r = wst.marshalSendAndReceive(etdurldao.getEtdNoticeUrl(), xmldoc);
						}
						//			System.out.println(r.toString());

					} catch (Exception e) {
						log.error(TypeConverter.exceptionToString(e));
					}
				}else if(DocumentType.equals("Счет-фактура РТК") && (req.getType().equals("BC") ||  req.getType().equals("BI") || req.getType().equals("SC"))&& !isTrueTypeSend){
							RecieveNoticeRequestDocument xmldoc = RecieveNoticeRequestDocument.Factory.newInstance();
							RecieveNoticeRequest xmlreq  = xmldoc.addNewRecieveNoticeRequest();
							xmlreq.setXml(new String(req.getSf_d1(),"windows-1251" ));
							xmlreq.setConfirmid(req.getSf_vs1());
							xmlreq.setEtdid(etdid);
							xmlreq.setXtype(req.getType());
							xmlreq.setSign(req.getSf_s1());
							boolean send =true;
							Object r = null;
	
							try {
								if (send){
									//				System.out.println(sfdoc);
									r = wst.marshalSendAndReceive(etdurldao.getEtdNoticeUrl(), xmldoc);
								}
								//			System.out.println(r.toString());
	
							} catch (Exception e) {
								log.error(TypeConverter.exceptionToString(e));
							}
						
				}else if((DocumentType.equals("Счет-фактура") ||  DocumentType.equals("Счет-фактура ЦСС")) && !req.getType().equals("notSend") && isTrueTypeSend){
					//if(!req.getType().equals("notSend")){
					
						RecieveNoticeRequestDocument xmldoc = RecieveNoticeRequestDocument.Factory.newInstance();
						RecieveNoticeRequest xmlreq  = xmldoc.addNewRecieveNoticeRequest();
						xmlreq.setXml(new String(req.getSf_d1(),"windows-1251" ));
						xmlreq.setConfirmid(req.getSf_vs1());
						xmlreq.setEtdid(etdid);
						xmlreq.setXtype(req.getType());
						xmlreq.setSign(req.getSf_s1());
						boolean send =true;
						Object r = null;

						try {
							if (send){
								//				System.out.println(sfdoc);
								r = wst.marshalSendAndReceive(etdurldao.getEtdNoticeUrl(), xmldoc);
							}
							//			System.out.println(r.toString());

						} catch (Exception e) {
							log.error(TypeConverter.exceptionToString(e));
						}
					
				}

				

			} catch (Exception e){
				log.error(TypeConverter.exceptionToString(e));
			}
		}
	}
	
	private SFinvoice getexactinvoicebyid(long docid, int number)
			throws IncorrectResultSizeDataAccessException {
		String xmlname="";
		String signname="";
		String tablename="";
		String guidName = "";
		String type ="";
		switch (number){
			case 0:	
				xmlname = "SF_D1";
				signname = "SF_S1";
				tablename = "DFSIGNS";
				guidName = "SF_VS1";
				type = "SC";
			break;
			case 1:	
				xmlname = "SF_D1";
				signname = "SF_S1";
				tablename = "DFSIGNS";
				guidName = "SF_VS1";
				type = "SC";
				break;
			case 2:	
				xmlname = "SF_D2";
				signname = "SF_S2";
				tablename = "DFSIGNS";
				guidName = "SF_VS2";
				type = "SNC";
				break;
			case 3:	
				xmlname = "SF_D3";
				signname = "SF_S3";
				tablename = "DFSIGNS";
				guidName = "SF_VS3";
				type = "BI";
				break;
			case 4:	
				xmlname = "SF_D4";
				signname = "SF_S4";
				tablename = "DFSIGNS";
				guidName = "SF_VS4";
				type = "BNI";
				break;
			case 5:	
				xmlname = "SF_D5";
				signname = "SF_S5";
				tablename = "DFSIGNS";
				guidName = "SF_VS5";
				type = "BNIC";
				break;
			case 6:	
				xmlname = "SF_D6";
				signname = "SF_S6";
				tablename = "DFSIGNS";
				guidName = "SF_VS6";
				type = "BC";
				break;
			case 7:	
				xmlname = "SF_D7";
				signname = "SF_S7";
				tablename = "DFSIGNS";
				guidName = "SF_VS7";
				type = "notSend";
				break;
			case 8:	
				xmlname = "SF_D8";
				signname = "SF_S8";
				tablename = "DFSIGNS";
				guidName = "SF_VS8";
				type = "BNC";
				break;
			case 9:	
				xmlname = "UV_D1";
				signname = "UV_S1";
				tablename = "DFKORR";
				guidName = "UV_VS1";
				type = "BNCUV";
				break;
			case 10:	
				xmlname = "UV_D2";
				signname = "UV_S2";
				tablename = "DFKORR";
				guidName = "UV_VS2";
				type = "SNCUV";
				break;
		}
		String sfsql = "";
		if(!guidName.equals("UV_VS1")&&!guidName.equals("UV_VS2")){
			sfsql = "select (select "+xmlname+" from snt."+tablename+" where id = DFS.ID) SF_D1,"
				+ "(select "+signname+" from snt."+tablename+" where id = DFS.ID) SF_S1,"
				+ "(select "+guidName+" from snt."+tablename+" where id = DFS.ID) SF_VS1 "
				+ "from snt.DFSIGNS DFS where id = :DOCID ";
		}else{
			sfsql = "select (select "+xmlname+" from snt.dfkorr where id = dfk.id) SF_D1, "
					+ "(select "+signname+" from snt.dfkorr where id = dfk.id) SF_S1, "
					+ "(select "+guidName+" from snt.dfkorr where id = dfk.id) SF_VS1 "
					+ "from snt.dfkorr dfk where id = :DOCID";
		}
		SFinvoice req = null;

		try {
			req = (SFinvoice) getNpjt().queryForObject(
					sfsql,
					new MapSqlParameterSource("DOCID", docid),
					new SFWrapper());
			req.setType(type);
//			System.out.println(req.getSf_d3());
			
		} catch (Exception e) {
			e.printStackTrace();
			log.error(TypeConverter.exceptionToString(e));

		}

		return req;

	}
	
	private class SFWrapper implements ParameterizedRowMapper<SFinvoice> {
		public SFinvoice mapRow(ResultSet rs, int n) throws SQLException {
			SFinvoice sf = new SFinvoice();
			sf.setSf_d1(rs.getBytes("SF_D1"));
//			sf.setSf_d2(rs.getBytes("SF_D2"));
//			sf.setSf_d3(rs.getBytes("SF_D3"));
//			sf.setSf_d4(rs.getBytes("SF_D4"));
//			sf.setSf_d5(rs.getBytes("SF_D5"));
//			sf.setSf_d6(rs.getBytes("SF_D6"));
//			sf.setSf_d7(rs.getBytes("SF_D7"));
//			sf.setSf_d8(rs.getBytes("SF_D8"));
			sf.setSf_s1(rs.getBytes("SF_S1"));
//			sf.setSf_s2(rs.getBytes("SF_S2"));
//			sf.setSf_s3(rs.getBytes("SF_S3"));
//			sf.setSf_s4(rs.getBytes("SF_S4"));
//			sf.setSf_s5(rs.getBytes("SF_S5"));
//			sf.setSf_s6(rs.getBytes("SF_S6"));
//			sf.setSf_s7(rs.getBytes("SF_S7"));
//			sf.setSf_s8(rs.getBytes("SF_S8"));
//			sf.setUv_d1(rs.getBytes("UV_D1"));
//			sf.setUv_d2(rs.getBytes("UV_D2"));
//			sf.setUv_s1(rs.getBytes("UV_S1"));
//			sf.setUv_s2(rs.getBytes("UV_S2"));
			sf.setSf_vs1(rs.getString("SF_VS1"));
			//sf.setSf_fd(rs.getBytes("SF_FD"));
			//sf.setSf_fds1(rs.getBytes("SF_FDS1"));
			//sf.setDocdata(rs.getString("DOCDATA"));
			return sf;
		}

	}
}

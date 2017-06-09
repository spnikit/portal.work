package com.aisa.portal.invoice.integration.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;

import javax.sql.DataSource;

import org.apache.log4j.Logger;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcDaoSupport;
import org.springframework.jdbc.core.simple.ParameterizedRowMapper;

import ru.aisa.rgd.ws.domain.SFinfo;
import ru.aisa.rgd.ws.exeption.ServiceException;
import ru.aisa.rgd.ws.utility.TypeConverter;

import com.aisa.portal.invoice.integration.dao.mappers.DocumentMapper;
import com.aisa.portal.invoice.integration.dao.mappers.StageDocumentMapper;
import com.aisa.portal.invoice.integration.endpoints.objects.DocumentsObj;

public class IntegrationDao extends NamedParameterJdbcDaoSupport {
	protected final Logger log = Logger.getLogger(getClass());

	public IntegrationDao(DataSource ds) {
		super();
		setDataSource(ds);
	}

	private String getCabinetId = "select CABINET_ID from snt.pred where id=:PREDID fetch first row only";
	private String isSeller = "select count(0) from snt.DFSIGNS   where SELLER=:CABINETID and ID=:DOCID";
	private String iskorrsf = "select rtrim(name) name from snt.doctype where id = (select typeid from snt.docstore where ID=:DOCID)";
	private String isdropped = "select case when dropid is null then 0 else 1 end from snt.docstore where ID =:DOCID";
private String isSellerbyDocid = "select count(0) from snt.DFSIGNS   where SELLER=(select CABINET_ID from snt.pred where id = "
			+ " (select predid from snt.docstore where id = :DOCID) fetch first row only) and ID=:DOCID";

	private String getInvoiceId = "select vid from snt.DFSIGNS where ID=:DOCID ";
	private String getInvoiceStage = "select status from snt.DFSIGNS where ID=:DOCID ";
	private String getSendforuv1 = "select case when uv_vs1 is null then 0 else 1 end from snt.dfkorr where id = :DOCID";
	private String getSendforuv2 = "select case when uv_vs2 is null then 0 else 1 end from snt.dfkorr where id = :DOCID";
	private String getInvoiceAndSgn = "select SF_FD AS XML,SF_FDS1 AS SGN from snt.DFSIGNS where ID=:DOCID";
	private String getBuyerInvoiceConfirmationAndSgn = "select sf_d3 AS XML,sf_s3 AS SGN from snt.DFSIGNS where ID=:DOCID";
	private String getBuyerConfirmationAndSgn = "select sf_d6 AS XML,sf_s6 AS SGN from snt.DFSIGNS where ID=:DOCID";
	private String getSellerInvoiceConfirmationAndSgn = "select sf_d1 AS XML,sf_s1 AS SGN from snt.DFSIGNS where ID=:DOCID";
	private String getBuyerUvedNotice = "select uv_d1 AS XML,uv_s1 AS SGN from snt.DFKORR where ID=:DOCID";
	
	private String ins0 = "INSERT INTO snt.DFSIGNS (status,SF_FD,SF_FDS1, id, seller, buyer, date, SF_FDS_XML) values (:STATUS, :XML, :SGN,:ID, :seller, :buyer, current timestamp,:DS_XML)  ";
	private String upins0 = "update snt.DFSIGNS set VID=:GUID where id=:ID";

	private String updt0 = "INSERT INTO snt.DFSIGNS (status,SF_FD,SF_FDS1,vid,id, seller, buyer, date, SF_FDS_XML) values (:STATUS, :XML, :SGN,:GUID,:ID, :seller, :buyer, current timestamp,:DS_XML)  ";
	private String updt0_1 = "INSERT INTO snt.DFFLOW (id) values ( :ID )  ";

	private String updt1 = "update snt.DFSIGNS set status=:STATUS, sf_d1= :XML, sf_s1=:SGN, sf_vs1=:GUID where id=:ID ";
	private String updt2 = "update snt.DFSIGNS set status=:STATUS, sf_d2= :XML, sf_s2=:SGN, sf_vs2=:GUID where id=:ID ";
	private String updt3 = "update snt.DFSIGNS set status=:STATUS, sf_d3= :XML, sf_s3=:SGN, sf_vs3=:GUID where id=:ID ";
	private String updt4 = "update snt.DFSIGNS set status=:STATUS, sf_d4= :XML, sf_s4=:SGN, sf_vs4=:GUID where id=:ID ";
	private String updt5 = "update snt.DFSIGNS set status=:STATUS, sf_d5= :XML, sf_s5=:SGN, sf_vs5=:GUID where id=:ID ";
	private String updt6 = "update snt.DFSIGNS set status=:STATUS, sf_d6= :XML, sf_s6=:SGN, sf_vs6=:GUID where id=:ID ";
	private String updt7 = "update snt.DFSIGNS set status=:STATUS, sf_d7= :XML, sf_s7=:SGN, sf_vs7=:GUID where id=:ID ";
	private String updt8 = "update snt.DFSIGNS set status=:STATUS, sf_d8= :XML, sf_s8=:SGN, sf_vs8=:GUID where id=:ID ";

	private String updt1_1 = "update snt.DFFLOW set SF_FVS1=1 where id=:ID ";
	private String updt2_1 = "update snt.DFFLOW set SF_FVS2=1 where id=:ID ";
	private String updt3_1 = "update snt.DFFLOW set SF_FVS3=1 where id=:ID ";
	private String updt4_1 = "update snt.DFFLOW set SF_FVS4=1 where id=:ID ";
	private String updt5_1 = "update snt.DFFLOW set SF_FVS5=1 where id=:ID ";
	private String updt6_1 = "update snt.DFFLOW set SF_FVS6=1 where id=:ID ";
	private String updt7_1 = "update snt.DFFLOW set SF_FVS7=1 where id=:ID ";
	private String updt8_1 = "update snt.DFFLOW set SF_FVS8=1 where id=:ID ";
	
	private String check_1_inv = "select case when SF_D1 is not null and SF_S1 is not null and SF_VS1 is not null then 1 else 0 end from snt.dfsigns where id = :ID";
	private String check_2_inv = "select case when SF_D2 is not null and SF_S2 is not null and SF_VS2 is not null then 1 else 0 end from snt.dfsigns where id = :ID";
	private String check_3_inv = "select case when SF_D3 is not null and SF_S3 is not null and SF_VS3 is not null then 1 else 0 end from snt.dfsigns where id = :ID";
	private String check_4_inv = "select case when SF_D4 is not null and SF_S4 is not null and SF_VS4 is not null then 1 else 0 end from snt.dfsigns where id = :ID";
	private String check_5_inv = "select case when SF_D5 is not null and SF_S5 is not null and SF_VS5 is not null then 1 else 0 end from snt.dfsigns where id = :ID";
	private String check_6_inv = "select case when SF_D6 is not null and SF_S6 is not null and SF_VS6 is not null then 1 else 0 end from snt.dfsigns where id = :ID";
	private String check_7_inv = "select case when SF_D7 is not null and SF_S7 is not null and SF_VS7 is not null then 1 else 0 end from snt.dfsigns where id = :ID";
	private String check_8_inv = "select case when SF_D8 is not null and SF_S8 is not null and SF_VS8 is not null then 1 else 0 end from snt.dfsigns where id = :ID";
	
	
	
	private String updtdoc = "update snt.docstore set signlvl = null where id=:ID";
	private String updtsfgfsgn = "update snt.docstore set sf_gfsgn = 1 where id =:ID";

	private String selectCount = "select count(0) from snt.DFSIGNS where  id=:ID ";

	private String selectStatus = "select status from snt.DFSIGNS where  id=:ID ";

	private String selectStageMap = "select SF_FVS1,SF_FVS2,SF_FVS3,SF_FVS4,SF_FVS5,SF_FVS6,SF_FVS7,SF_FVS8, SF_FULL from snt.DFFLOW where  id=:DOCID ";
	private String selectDropStatus = "select status from snt.SFKORR where  id=:DOCID ";

	// for etd notice
	private String selectetddocid = "select etdid from snt.docstore where id = :ID";
	private String selectPortalid = "select id from snt.docstore where etdid = :ETDID";
	private String selectetdnoticeurl = "select URL from SNT.URLS where SERVICENAME = :SERVICENAME";
	private String selectsfxml = "select SF_FDS_XML from snt.dfsigns where id = :DOCID";

	private String insertkorr = "insert into snt.DFKORR (ID, VID, STATUS, DATE, UV_D1) values (:DOCID, :GUID, :STATUS, current timestamp, :XML)";
	private String updt_gfsgn_for_korr = "update snt.docstore set sf_gfsgn = 0 where id = :DOCID";
	private String updt_korr1 = "update snt.dfkorr set status =:STATUS, UV_D1 = :XML, UV_S1 = :SGN, UV_VS1 = :GUID where id = :DOCID";
	private String updt_korr2 = "update snt.dfkorr set UV_D2 = :xml, status =:STATUS, UV_S2 = :SGN, UV_VS2 = :GUID where id = :DOCID";
	private String check_9_inv = "select case when UV_D1 is not null and UV_S1 is not null and UV_VS1 is not null then 1 else 0 end from snt.dfkorr where id = :DOCID";
	private String check_10_inv = "select case when UV_D2 is not null and UV_S2 is not null and UV_VS2 is not null then 1 else 0 end from snt.dfkorr where id = :DOCID";
	
	private String updt_korr1_withoutguid = "update snt.dfkorr set status =:STATUS, UV_D1 = :XML, UV_S1 = :SGN where id = :DOCID";
	private String updt_korr2_withoutguid = "update snt.dfkorr set UV_D2 = :xml, status =:STATUS, UV_S2 = :SGN where id = :DOCID";
	private String updt_guid_for_korr1 = "update snt.dfkorr set UV_VS1 = :GUID where id = :DOCID";
	private String updt_guid_for_korr2 = "update snt.dfkorr set UV_VS2 = :GUID where id = :DOCID";
	private String getkorr = "select UV_D1 from snt.dfkorr where id =:DOCID";
	private String SELECT_SFINFO_FOR_SIGN = "select id, case when opisanie is not null then rtrim(opisanie) else '' end as content from snt.docstore ds where "
			+ " sf_gfsgn =0 and (predid in (select id from snt.pred where id = :predid or headid =:predid) or pred_creator = :predid) and "
			+ " ds.typeid in (select dtid from SNT.doctypeacc where wrkid = :workerId and (cview = 1 or cedit=1)  ) order by crdate asc";
//	private String SELECT_SFINFO_FOR_SIGN = "select id, case when opisanie is not null then rtrim(opisanie) else '' end as content from snt.docstore ds where id = 67334";
	private String getdfkorrstatus = "select status from snt.dfkorr where id=:DOCID";
	private String isExternal = "select count(0) from snt.pred pred "+
			"where pred.CABINET_ID in (select seller from snt.dfsigns where id =:DOCID "+
			"union select buyer from snt.dfsigns where id =:DOCID) "+
			"and type = 1";
	@SuppressWarnings("unchecked")
	public boolean IsSellerServerSGN(long docid) {

		HashMap<String, Object> in = new HashMap<String, Object>();
		in.put("DOCID", docid);
		int count = getNamedParameterJdbcTemplate().queryForInt(
				isSellerbyDocid, in);
		if (count > 0)
			return false;
		else
			return true;
	}

	public boolean IsBuyerServerSGN(long docid) {

		HashMap<String, Object> in = new HashMap<String, Object>();
		in.put("DOCID", docid);
		int count = getNamedParameterJdbcTemplate().queryForInt(
				isSellerbyDocid, in);
		if (count > 0)
			return true;
		else
			return false;
	}
	public int isExterna(long docid) {
		HashMap<String,Object> in = new HashMap<String, Object>();
		in.put("DOCID", docid);
		return getNamedParameterJdbcTemplate().queryForInt(isExternal, in);
	}
	public void updtgfsgn(long docid) {
		HashMap<String, Object> in = new HashMap<String, Object>();
		in.put("ID", docid);
		getNamedParameterJdbcTemplate().update(updtsfgfsgn, in);
	}

	public String GetInvoiceId(long docid) {

		HashMap in = new HashMap();
		in.put("DOCID", docid);
		String invoiceId = (String) getNamedParameterJdbcTemplate()
				.queryForObject(getInvoiceId, in, String.class);
		return invoiceId;

	}
	public int GetKorrStat(long docid) {
		HashMap<String,Object> in = new HashMap<String, Object>();
		in.put("DOCID", docid);
		int stat = (int) getNamedParameterJdbcTemplate().queryForInt(getdfkorrstatus, in);
		return stat;
	}
	public boolean updateBuyerNoticeConfirmation(long docid, int status,
			String guid, byte[] xml, byte[] sgn) {

		HashMap in = new HashMap();
		in.put("ID", docid);
		if (GetCountForID(in) == 1) {

			int statusLocal = GetStatusForID(in);
			if (statusLocal > 6)
				in.put("STATUS", status);
			else
				in.put("STATUS", statusLocal);

			in.put("STATUS", status);
			in.put("XML", xml);
			in.put("GUID", guid);
			in.put("SGN", sgn);
			if (getNamedParameterJdbcTemplate().queryForInt(check_8_inv, in)==0){
				getNamedParameterJdbcTemplate().update(updt8, in);
				getNamedParameterJdbcTemplate().update(updt8_1, in);
				return true;
				} else 
					return false;
		} else
			return false;

	}

	public boolean updateBuyerNoticeInvoice(long docid, int status,
			String guid, byte[] xml, byte[] sgn) {

		HashMap in = new HashMap();
		in.put("ID", docid);
		if (GetCountForID(in) == 1) {

			int statusLocal = GetStatusForID(in);
			// if (statusLocal==3)
			if (statusLocal == 4)
				in.put("STATUS", status);
			else
				in.put("STATUS", statusLocal);

			in.put("STATUS", status);
			in.put("XML", xml);
			in.put("GUID", guid);
			in.put("SGN", sgn);
			if (getNamedParameterJdbcTemplate().queryForInt(check_4_inv, in)==0){
				getNamedParameterJdbcTemplate().update(updt4, in);
				getNamedParameterJdbcTemplate().update(updt4_1, in);
				return true;
				} else 
					return false;
		} else
			return false;

	}

	public boolean updateBuyerNoticeInvoiceConfirmation(long docid, int status,
			String guid, byte[] xml, byte[] sgn) {

		HashMap in = new HashMap();
		in.put("ID", docid);
		if (GetCountForID(in) == 1) {

			int statusLocal = GetStatusForID(in);
			// if (statusLocal==4)
			if (statusLocal == 3)
				in.put("STATUS", status);
			else
				in.put("STATUS", statusLocal);

			in.put("STATUS", status);
			in.put("XML", xml);
			in.put("GUID", guid);
			in.put("SGN", sgn);
			if (getNamedParameterJdbcTemplate().queryForInt(check_5_inv, in)==0){
				getNamedParameterJdbcTemplate().update(updt5, in);
				getNamedParameterJdbcTemplate().update(updt5_1, in);
				return true;
				} else 
					return false;
		} else
			return false;

	}

	public boolean updateSignedInvoceGuid(long docid, String guid) {
		HashMap in = new HashMap();
		in.put("ID", docid);
		in.put("GUID", guid);
		getNamedParameterJdbcTemplate().update(upins0, in);
		return true;
	}

	public boolean insertSellerSignedInvoceNoGuid(long docid, int status,
			byte[] xml, byte[] sgn, String seller, String buyer) {

		HashMap in = new HashMap();
		in.put("ID", docid);

		if (GetCountForID(in) == 0) {

			in.put("seller", seller);
			in.put("buyer", buyer);
			in.put("STATUS", status);
			in.put("XML", xml);
			in.put("SGN", sgn);
			in.put("DS_XML", xml);
			getNamedParameterJdbcTemplate().update(ins0, in);
			getNamedParameterJdbcTemplate().update(updt0_1, in);
			return true;
		} else
			return false;

	}

	public boolean insertSellerSignedInvoce(long docid, int status,
			String guid, byte[] xml, byte[] sgn, String seller, String buyer) {

		HashMap in = new HashMap();
		in.put("ID", docid);

		if (GetCountForID(in) == 0) {

			in.put("seller", seller);
			in.put("buyer", buyer);
			in.put("STATUS", status);
			in.put("XML", xml);
			in.put("GUID", guid);
			in.put("SGN", sgn);
			in.put("DS_XML", xml);
			getNamedParameterJdbcTemplate().update(updt0, in);
			getNamedParameterJdbcTemplate().update(updt0_1, in);
			return true;
		} else
			return false;

	}

	public boolean updateBuyerInvoice(long docid, int status, String guid,
			byte[] xml, byte[] sgn) {

		HashMap in = new HashMap();
		in.put("ID", docid);

		if (GetCountForID(in) == 1) {

			int statusLocal = GetStatusForID(in);
			if (statusLocal < 3)
				in.put("STATUS", status);
			else
				in.put("STATUS", statusLocal);

			in.put("STATUS", status);
			in.put("XML", xml);
			in.put("GUID", guid);
			in.put("SGN", sgn);
			if (getNamedParameterJdbcTemplate().queryForInt(check_3_inv, in)==0){
				getNamedParameterJdbcTemplate().update(updt3, in);
				getNamedParameterJdbcTemplate().update(updt3_1, in);
				return true;
				} else 
					return false;
		} else
			return false;

	}

	public boolean updateSellerNoticeInvoice(long docid, int status,
			String guid, byte[] xml, byte[] sgn) {

		HashMap in = new HashMap();
		in.put("ID", docid);
		if (GetCountForID(in) == 1) {

			int statusLocal = GetStatusForID(in);
			if (statusLocal == 6)
				in.put("STATUS", status);
			else
				in.put("STATUS", statusLocal);

			in.put("STATUS", status);
			in.put("XML", xml);
			in.put("GUID", guid);
			in.put("SGN", sgn);
			if (getNamedParameterJdbcTemplate().queryForInt(check_7_inv, in)==0){
				getNamedParameterJdbcTemplate().update(updt7, in);
				getNamedParameterJdbcTemplate().update(updt7_1, in);
				return true;
				} else 
					return false;
		} else
			return false;

	}

	public boolean updateBuyerConfirmation(long docid, int status, String guid,
			byte[] xml, byte[] sgn) {

		HashMap in = new HashMap();
		in.put("ID", docid);
		if (GetCountForID(in) == 1) {

			int statusLocal = GetStatusForID(in);
			if (statusLocal == 5)
				in.put("STATUS", status);
			else
				in.put("STATUS", statusLocal);

			in.put("STATUS", status);
			in.put("XML", xml);
			in.put("GUID", guid);
			in.put("SGN", sgn);
			if (getNamedParameterJdbcTemplate().queryForInt(check_6_inv, in)==0){
				getNamedParameterJdbcTemplate().update(updt6, in);
				getNamedParameterJdbcTemplate().update(updt6_1, in);
				return true;
				} else 
					return false;
		} else
			return false;

	}

	public boolean updateSellerNoticeConfirmation(long docid, int status,
			String guid, byte[] xml, byte[] sgn) {

		HashMap in = new HashMap();
		in.put("ID", docid);

		if (GetCountForID(in) == 1) {

			int statusLocal = GetStatusForID(in);
			if (statusLocal == 1)
				in.put("STATUS", status);
			else
				in.put("STATUS", statusLocal);

			in.put("XML", xml);
			in.put("GUID", guid);
			in.put("SGN", sgn);
			
			if (getNamedParameterJdbcTemplate().queryForInt(check_2_inv, in)==0){
				getNamedParameterJdbcTemplate().update(updt2, in);
				getNamedParameterJdbcTemplate().update(updt2_1, in);
				return true;
				} else 
					return false;
		} else
			return false;

	}

	public boolean updateSellerConfirmation(long docid, int status,
			String guid, byte[] xml, byte[] sgn) {

		HashMap in = new HashMap();
		in.put("ID", docid);

		if (GetCountForID(in) == 1) {

			int statusLocal = GetStatusForID(in);
			if (statusLocal == 0)
				in.put("STATUS", status);
			else
				in.put("STATUS", statusLocal);

			in.put("XML", xml);
			in.put("GUID", guid);
			in.put("SGN", sgn);
			
			if (getNamedParameterJdbcTemplate().queryForInt(check_1_inv, in)==0){
			getNamedParameterJdbcTemplate().update(updt1, in);
			getNamedParameterJdbcTemplate().update(updt1_1, in);
			return true;
			} else 
				return false;
			} else
			return false;

	}

	public void insertDFKorr(long docid, String guid, int status, byte[] xml) {
		HashMap in = new HashMap();
		in.put("DOCID", docid);
		in.put("GUID", guid);
		in.put("STATUS", status);
		in.put("XML", xml);
		getNamedParameterJdbcTemplate().update(insertkorr, in);

	}
	public void updtGfsgnForKorr(long docid) {
		HashMap in = new HashMap();
		in.put("DOCID", docid);
		getNamedParameterJdbcTemplate().update(updt_gfsgn_for_korr, in);

	}
	
	public boolean updtdfkorr1WithGUID(long docid, String guid, int status, byte[] xml, byte[] sgn) {
		HashMap<String, Object> in = new HashMap<String, Object>();
		in.put("DOCID", docid);
		in.put("GUID", guid);
		in.put("STATUS", status);
		in.put("XML", xml);
		in.put("SGN", sgn);
		if(getNamedParameterJdbcTemplate().queryForInt(check_9_inv, in)==0){
			getNamedParameterJdbcTemplate().update(updt_korr1, in);
			return true;
		}else{
			return false;
		}
	}
	public boolean updtdfkorr1WithoutGUID(long docid, String guid, int status, byte[] xml, byte[] sgn) {
		HashMap<String, Object> in = new HashMap<String, Object>();
		in.put("DOCID", docid);
		in.put("GUID", guid);
		in.put("STATUS", status);
		in.put("XML", xml);
		in.put("SGN", sgn);
		if(getNamedParameterJdbcTemplate().queryForInt(check_9_inv, in)==0){
			getNamedParameterJdbcTemplate().update(updt_korr1_withoutguid, in);
			return true;
		}else{
			return false;
		}
		

	}
	public boolean updtdfkorr2WithGUID(long docid, String guid, int status, byte[] xml,
			byte[] sgn) {
		HashMap<String, Object> in = new HashMap<String, Object>();
		in.put("DOCID", docid);
		in.put("GUID", guid);
		in.put("STATUS", status);
		in.put("xml", xml);
		in.put("SGN", sgn);
		if(getNamedParameterJdbcTemplate().queryForInt(check_10_inv, in)==0){
			getNamedParameterJdbcTemplate().update(updt_korr2, in);
			return true;
		}else{
			return false;
		}
		
	}
	public boolean updtdfkorr2WithoutGUID(long docid, String guid, int status, byte[] xml,
			byte[] sgn) {
		HashMap<String, Object> in = new HashMap<String, Object>();
		in.put("DOCID", docid);
		in.put("GUID", guid);
		in.put("STATUS", status);
		in.put("xml", xml);
		in.put("SGN", sgn);
		if(getNamedParameterJdbcTemplate().queryForInt(check_10_inv, in)==0){
			getNamedParameterJdbcTemplate().update(updt_korr2_withoutguid, in);
			return true;
		}else{
			return false;
		}
		

	}
	public void updtGUIDforkorr1(long docid, String guid) {
		HashMap<String, Object> in = new HashMap<String, Object>();
		in.put("DOCID", docid);
		in.put("GUID", guid);
		getNamedParameterJdbcTemplate().update(updt_guid_for_korr1, in);

	}
	public void updtGUIDforkorr2(long docid, String guid) {
		HashMap<String, Object> in = new HashMap<String, Object>();
		in.put("DOCID", docid);
		in.put("GUID", guid);
		getNamedParameterJdbcTemplate().update(updt_guid_for_korr2, in);

	}
	public int GetCountForID(HashMap in) {
		// System.out.println(
		// getNamedParameterJdbcTemplate().queryForInt(selectCount, in));
		return getNamedParameterJdbcTemplate().queryForInt(selectCount, in);
	}

	public int GetStatusForID(HashMap in) {

		return getNamedParameterJdbcTemplate().queryForInt(selectStatus, in);
	}
	
	public int GetDropStatusForID(long docid) {
		HashMap in = new HashMap();
		in.put("DOCID", docid);
		return getNamedParameterJdbcTemplate().queryForInt(selectDropStatus, in);
	}
	
	public DocumentsObj GetStatusMapForID(long docid) {
		HashMap in = new HashMap();
		in.put("DOCID", docid);
		List<DocumentsObj> document = getNamedParameterJdbcTemplate().query(
				selectStageMap, in, new StageDocumentMapper());
		boolean chk = document.get(0).CheckIsFullSigned();
		if (chk && document.get(0).getSF_FULL() == 0) {

		}
		return document.get(0);
	}

	public String GetCabinetId(int predid) {
		HashMap in = new HashMap();
		in.put("PREDID", predid);
		return (String) getNamedParameterJdbcTemplate().queryForObject(
				getCabinetId, in, String.class);
	}

	public boolean IsSeller(String cabinetid, long docid) {
		HashMap in = new HashMap();
		in.put("CABINETID", cabinetid);
		in.put("DOCID", docid);
		int count = getNamedParameterJdbcTemplate().queryForInt(isSeller, in);
		if (count > 0)
			return true;
		else
			return false;

	}

	public boolean IsKorrSF(long docid) {
		String sfname = "";
		HashMap in = new HashMap();
		in.put("DOCID", docid);
		sfname = getNamedParameterJdbcTemplate().queryForObject(iskorrsf, in,
				String.class);
		if (sfname.contains("Корр"))
			return true;
		else
			return false;

	}

	public boolean IsDropped(long docid) {
		HashMap in = new HashMap();
		in.put("DOCID", docid);
		int dropped = getNamedParameterJdbcTemplate().queryForInt(isdropped, in);
		if (dropped==1)
			return true;
		else
			return false;

	}
	
	public int GetInvoiceStage(long docid) {
		HashMap in = new HashMap();
		in.put("DOCID", docid);
		int stage = getNamedParameterJdbcTemplate().queryForInt(
				getInvoiceStage, in);
		return stage;
	}

	public boolean GetIsSendForKorr(long docid, int Stage) {
		HashMap in = new HashMap();
		in.put("DOCID", docid);
		boolean isSend = false;
		
		if (Stage==1){
			int stage = getNamedParameterJdbcTemplate().queryForInt(
					getSendforuv1, in);
			
			if (stage==0) isSend=true;
			
		}
		if (Stage==2){
			int stage = getNamedParameterJdbcTemplate().queryForInt(
					getSendforuv2, in);
			if (stage==0) isSend=true;
		}
		return isSend;
	}
	
	
	public DocumentsObj GetInvoice(long docid) {
		HashMap in = new HashMap();
		in.put("DOCID", docid);
		List<DocumentsObj> document = getNamedParameterJdbcTemplate().query(
				getInvoiceAndSgn, in, new DocumentMapper());
		return document.get(0);
	}

	public DocumentsObj GetBuyerInvoiceConfirmation(long docid) {
		HashMap in = new HashMap();
		in.put("DOCID", docid);
		List<DocumentsObj> document = getNamedParameterJdbcTemplate().query(
				getBuyerInvoiceConfirmationAndSgn, in, new DocumentMapper());
		return document.get(0);
	}

	public DocumentsObj GetBuyerConfirmation(long docid) {
		// TODO Auto-generated method stub
		HashMap in = new HashMap();
		in.put("DOCID", docid);
		List<DocumentsObj> document = getNamedParameterJdbcTemplate().query(
				getBuyerConfirmationAndSgn, in, new DocumentMapper());
		return document.get(0);
	}

	public DocumentsObj GetSellerConfirmation(long docid) {
		// TODO Auto-generated method stub
		HashMap in = new HashMap();
		in.put("DOCID", docid);
		List<DocumentsObj> document = getNamedParameterJdbcTemplate().query(
				getSellerInvoiceConfirmationAndSgn, in, new DocumentMapper());
		return document.get(0);
	}
	public DocumentsObj GetBuyerUvedNotice(long docid) {
		// TODO Auto-generated method stub
		HashMap in = new HashMap();
		in.put("DOCID", docid);
		List<DocumentsObj> document = getNamedParameterJdbcTemplate().query(
				getBuyerUvedNotice, in, new DocumentMapper());
		return document.get(0);
	}
	public long GetEtdDocid(long id) {
		HashMap in = new HashMap();
		in.put("ID", id);
		long etdid = getNamedParameterJdbcTemplate().queryForInt(
				selectetddocid, in);
		return etdid;
	}

	public long GetPortalid(long etdid) {
		HashMap in = new HashMap();
		in.put("ETDID", etdid);
		long id = getNamedParameterJdbcTemplate().queryForInt(selectPortalid,
				in);
		return id;
	}

	public String GetEtdNoticeURL(String urtype) {
		HashMap in = new HashMap();
		in.put("SERVICENAME", urtype);
		String url = (String) getNamedParameterJdbcTemplate().queryForObject(
				selectetdnoticeurl, in, String.class);
		return url;
	}

	public String GetSF(long docid) {
		HashMap in = new HashMap();
		in.put("DOCID", docid);
		String xml = (String) getNamedParameterJdbcTemplate().queryForObject(
				selectsfxml, in, String.class);
		return xml;
	}

	public List<SFinfo> getSF(int predid, int wrkid) throws ServiceException {
		List<SFinfo> result = null;
		HashMap<String, Object> pp = new HashMap<String, Object>();
		pp.put("predid", predid);
		pp.put("workerId", wrkid);
		// pp.put("name", "Счет-фактура РТК");
		try {
			result = getNamedParameterJdbcTemplate().query(
					SELECT_SFINFO_FOR_SIGN, pp,
					new ParameterizedRowMapper<SFinfo>() {

						public SFinfo mapRow(ResultSet rs, int n)
								throws SQLException {
							SFinfo sf = new SFinfo();
							sf.setContent(rs.getString("content"));
							sf.setDocid(rs.getLong("id"));
							// System.out.println(sf.getDocid());
							return sf;

						}
					});
		} catch (Exception e) {
			log.error(TypeConverter.exceptionToString(e));
		}
		return result;
	}

}

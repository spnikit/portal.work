package ru.aisa.rgd.etd.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import javax.sql.DataSource;

import org.apache.log4j.Logger;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcDaoSupport;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.ParameterizedRowMapper;

import ru.aisa.rgd.etd.objects.ETDDocument;
import ru.aisa.rgd.etd.objects.ETDDocumentFilter;
import ru.aisa.rgd.ws.utility.TypeConverter;
import sheff.rjd.ws.OCO.ServiceTypes;

public class ETDDocumentDao extends NamedParameterJdbcDaoSupport {

	private static Logger log = Logger.getLogger(ETDDocumentDao.class);

	public ETDDocumentDao(DataSource ds) {
		super();
		setDataSource(ds);
	}

	SimpleDateFormat db2dateformat;

	public SimpleDateFormat getDb2dateformat() {
		return db2dateformat;
	}

	public void setDb2dateformat(SimpleDateFormat db2dateformat) {
		this.db2dateformat = db2dateformat;
	}

	SimpleDateFormat stringdateformat;

	public SimpleDateFormat getStringdateformat() {
		return stringdateformat;
	}

	public void setStringdateformat(SimpleDateFormat stringdateformat) {
		this.stringdateformat = stringdateformat;
	}

	private static final String filialsqlflow = "(select id from snt.pred where id =:predId union select headid "
			+ "from snt.pred where id =:predId union select id from snt.pred where headid =:predId) ";

	private static final String filialsqlstore = "(select id from snt.pred where id =:predId or headid =:predId)  ";

	private static final String filialsqlarchivestore = "(select id from snt.pred where headid = :predId "
			+ " union select id from snt.pred where headid = (select headid from snt.pred where id = :predId) "
			+ " union select headid from snt.pred where id = :predId or headid = :predId)  ";

	private static final String uprt = " AND ds.nwrkid = CASE WHEN ds.nwrkid IS NOT NULL  THEN  (SELECT  ORDER FROM  snt.docacceptflow   WHERE dtid = ds.typeid "
			+ " AND order = (select parid from snt.wrkname where id = :workerId))  ELSE null END ";

	private static final String uprt2 = "AND ds.nwrkid = CASE WHEN ds.nwrkid IS NULL  THEN (SELECT parent FROM  snt.docacceptflow  "
			+ "where dtid = ds.typeid and wrkid = :workerId)  ELSE ds.nwrkid END";

	static private final String GET_ACTIVE_DOCUMENT_IDS_SQL_NEW =

	" select distinct ds.id as id, "
			+ "no, "
			+ " ( select rtrim(cast(name as char(120) ccsid unicode )) from SNT.DocType as dt where ds.typeid = dt.id ) as name,"
			+ " ds.CrDate as createDate,"
			+ "ds.opisanie as content, "
			+ "( select rtrim(vname) from SNT.PRED as prd where ds.pred_creator = prd.id  ) as predname ,"
			+ " ds.CrTime as createTime, "
			+ "ds.id_pak as idpak,"
			+ "ds.vagnum vagnum, ds.repdate repdate, "
			+ "case when ds.onbaseid is not null then (select rtrim(name) from snt.dor where id = "
			+ "(select distinct(dorid) from snt.rem_pred where kleim = ds.onbaseid)) end as di, "
			+ "case when ds.onbaseid is not null then (select rtrim(name) from snt.rem_pred "
			+ "where kleim = ds.onbaseid) end as rem_pred, "
			+ "ds.reqnum reqnum, "
			+ "ds.di servicecode, "
			+ " ds.reqdate reqdate, "
			+ " (select rtrim(cast(rtrim(fname) || ' ' || rtrim(mname) || ' ' || rtrim(lname) as char(240) ccsid unicode)) from SNT.personall where id = ds.LPERSID) as lastSigner, "
			+ "ds.ldate as lastDate, "
			+ "cast(ds.price as decimal(9,2)) price, "
			+ "ds.sf_type sf_type, "
			+ "ds.numsf, "
			+ "ds.numfpu, "
			+ " (select code_text from snt.packinfo where id = ds.id fetch first row only) otc_name, "
			+ " (select REFER_TEXT from snt.packinfo where id = ds.id fetch first row only) otc_type "
			+ "from SNT.DocStore as ds, SNT.doctypeflow as df "
			+ " where "
			+ " ds.typeid in (select dtid from SNT.doctypeacc where wrkid = :workerId and (cview = 1 or cedit=1)  )"
			+ " and ds.dropid is null  "
			+ "  and ds.visible =1 "
			+ " and ds.typeid = df.dtid "
			+ " and ds.SignLvl = df.Parent "
			+ "and ds.SignLvl >=0 "
			// + " and df.WrkID = :workerId "
			+ " and ds.predid in " + filialsqlstore + " and ds.ldate is null "
			+ " #filt #period " + "#pers #sort #dir #2sort #2dir with ur";

	static private final String GET_INWORK_DOCUMENTS_IDS_SQL_NEW = " select distinct ds.id as id ,no, "
			+ " ( select rtrim(cast(name as char(120) ccsid unicode )) from SNT.DocType as dt where ds.typeid = dt.id ) as name,"
			+ " ds.CrDate as createDate,"
			+ "ds.opisanie as content, "
			+ "( select rtrim(vname) from SNT.PRED as prd where ds.pred_creator = prd.id  ) as predname , "
			+ " ds.CrTime as createTime , "
			+ "ds.id_pak as idpak,"
			+ "ds.vagnum vagnum, ds.repdate repdate, "
			+ "ds.di servicecode, "
			+ "case when ds.onbaseid is not null then (select rtrim(name) from snt.dor where id = "
			+ "(select distinct(dorid) from snt.rem_pred where kleim = ds.onbaseid)) end as di, "
			+ "case when ds.onbaseid is not null then (select rtrim(name) from snt.rem_pred "
			+ "where kleim = ds.onbaseid) end as rem_pred, "
			+ " ds.reqdate reqdate, "
			+ " case when ds.etdid is null or ds.typeid  = (select id from snt.doctype where rtrim(name) = 'ГУ-2б') "
			+ " then ds.id else ds.etdid end as etdid, "
			+ " (select rtrim(cast(rtrim(fname) || ' ' || rtrim(mname) || ' ' || rtrim(lname) as char(240) ccsid unicode)) from SNT.personall where id = ds.LPERSID) as lastSigner, "
			+ "ds.ldate as lastDate, "
			+ "cast(ds.price as decimal(9,2)) price, "
			+ "ds.sf_type sf_type, "
			+ "ds.numsf, "
			+ "ds.numfpu, "
			+ " (select code_text from snt.packinfo where id = ds.id fetch first row only) otc_name, "
			+ " (select REFER_TEXT from snt.packinfo where id = ds.id fetch first row only) otc_type "
			+ "from SNT.DocStore as ds, SNT.doctypeflow as df  "
			+ " where "
			+ "ds.id in (select dstf.docid from SNT.docstoreflow dstf where dstf.predid in "
			+ filialsqlflow
			// + " and dstf.wrkid=:workerId"
			+ " ) "
			+ " and ds.typeid in (select dtid from SNT.doctypeacc where wrkid = :workerId and (cview = 1 or cedit=1) ) "
			+ " and ds.dropid is null "
			+ "and ds.predid in "
			+ filialsqlstore
			+ " and ds.typeid in (select dtid from SNT.doctypeacc where wrkid = :workerId and (cview = 1 or cedit=1)  ) "
			+ " and (ds.dropid is null  and ds.signlvl >0 or ds.GROUPSGN =0 and ds.dropid is null ) "
			+ "and ds.visible =1 #filt #period " + "#pers  #marsh with ur";

	static private final String GET_DECLINED_DOCUMENTS_IDS_SQL_NEW = " select distinct ds.id as id ,no, "
			+ " ( select rtrim(cast(name as char(120) ccsid unicode )) from SNT.DocType as dt where ds.typeid = dt.id ) as name,"
			+ " ds.CrDate as createDate,"
			+ "ds.opisanie as content, "
			+ "( select rtrim(vname) from SNT.PRED as prd where ds.pred_creator = prd.id  ) as predname ,"
			+ " ds.CrTime as createTime , "
			+ "ds.id_pak as idpak,"
			+ "ds.vagnum vagnum, ds.repdate repdate, "
			+ "ds.di servicecode, "
			+ "case when ds.onbaseid is not null then (select rtrim(name) from snt.dor where id = "
			+ "(select distinct(dorid) from snt.rem_pred where kleim = ds.onbaseid)) end as di, "
			+ "case when ds.onbaseid is not null then (select rtrim(name) from snt.rem_pred "
			+ "where kleim = ds.onbaseid) end as rem_pred, "
			+ "ds.reqnum reqnum, "
			+ "(select rtrim(cast(rtrim(fname) || ' ' || rtrim(mname) || ' ' || rtrim(lname) as char(240) ccsid unicode)) from SNT.personall where id = ds.DROPID) "
			+ " as lastSigner, "
			+ " ds.reqdate reqdate, "
			+ "date(ds.droptime) as lastDate, "
			+ "cast(ds.price as decimal(9,2)) price, "
			+ "ds.sf_type sf_type, "
			+ "ds.numsf, "
			+ "ds.numfpu, "
			+ " (select code_text from snt.packinfo where id = ds.id fetch first row only) otc_name, "
			+ " (select REFER_TEXT from snt.packinfo where id = ds.id fetch first row only) otc_type "
			+ "from SNT.DocStore as ds, SNT.doctypeflow as df  "
			+ "where ds.typeid in (select dtid from SNT.doctypeacc where wrkid = :workerId and (cview = 1 or cedit=1)  )"
			+ " and ds.typeid = df.dtid "
			+ " and ds.predid in "
			+ filialsqlstore
			// + "  and ds.typeid = df.dtid "
			+ " and ds.dropid is not null "
			+ "and ds.DROPPREDID in "
			+ filialsqlflow
			// + " and dropwrkid = :workerId "
			+ " and ds.visible =1 " + "#filt #period #pers #marsh with ur";

	static private final String GET_ARCHIVE_DOCUMENTS_IDS_SQL_NEW = " select distinct ds.id as id ,no, "
			+ " ( select rtrim(cast(name as char(120) ccsid unicode )) from SNT.DocType as dt where ds.typeid = dt.id ) as name,"
			+ " ds.CrDate as createDate,"
			+ "ds.opisanie as content, "
			+ "( select rtrim(vname) from SNT.PRED as prd where ds.pred_creator = prd.id  ) as predname ,"
			+ " ds.CrTime as createTime , "
			+ "ds.id_pak as idpak,"
			+ "ds.vagnum vagnum, ds.repdate repdate, "
			+ "ds.di servicecode, "
			+ "case when ds.onbaseid is not null then (select rtrim(name) from snt.dor where id = "
			+ "(select distinct(dorid) from snt.rem_pred where kleim = ds.onbaseid)) end as di, "
			+ "case when ds.onbaseid is not null then (select rtrim(name) from snt.rem_pred "
			+ "where kleim = ds.onbaseid) end as rem_pred, "
			+ "ds.reqnum reqnum, "
			+ " ds.reqdate reqdate, "
			+ " (select rtrim(cast(rtrim(fname) || ' ' || rtrim(mname) || ' ' || rtrim(lname) as char(240) ccsid unicode)) from SNT.personall where id = ds.LPERSID) as lastSigner, "
			+ "ds.ldate as lastDate, "
			+ "cast(ds.price as decimal(9,2)) price, "
			+ "ds.sf_type sf_type, "
			+ "ds.numsf, "
			+ "ds.numfpu, "
			+ " (select code_text from snt.packinfo where id = ds.id fetch first row only) otc_name, "
			+ " (select REFER_TEXT from snt.packinfo where id = ds.id fetch first row only) otc_type "

			+ "from SNT.DocStore as ds, SNT.doctypeflow as df  "
			+ " where ds.typeid = df.dtid"
			+ " and ds.dropid is null  "
			+ " and ds.SignLvl is NULL "
			+ " and (select dsf.predid from snt.docstoreflow dsf where "
			+ "dsf.docid = ds.id "
			+ "and dsf.order = (select max(order) from snt.docstoreflow where docid = ds.id)) in "
			+ filialsqlflow
			+ " and ds.predid in "
			+ filialsqlstore
			+ " and ds.typeid in (select dtid from SNT.doctypeacc where wrkid = :workerId "
			+ "and (cview = 1 or cedit=1) ) and ds.visible =1 "
			+ "and (ds.groupsgn is null or ds.groupsgn =1) #filt #period #pers #marsh with ur";

	static private final String GET_ACTIVE_DOCUMENT_IDS_SQL_NEW_ROLE2 =

	" select distinct ds.id as id, "
			+ "no, "
			+ " ( select rtrim(cast(name as char(120) ccsid unicode )) from SNT.DocType as dt where ds.typeid = dt.id ) as name,"
			+ " ds.CrDate as createDate,"
			+ "ds.opisanie as content, "
			+ "( select rtrim(vname) from SNT.PRED as prd where ds.pred_creator = prd.id  ) as predname ,"
			+ " ds.CrTime as createTime, "
			+ "ds.id_pak as idpak,"
			+ "ds.vagnum vagnum, ds.repdate repdate, "
			+ "ds.di servicecode, "
			+ "case when ds.onbaseid is not null then (select rtrim(name) from snt.dor where id = "
			+ "(select distinct(dorid) from snt.rem_pred where kleim = ds.onbaseid)) end as di, "
			+ "case when ds.onbaseid is not null then (select rtrim(name) from snt.rem_pred "
			+ "where kleim = ds.onbaseid) end as rem_pred, "
			+ "ds.reqnum reqnum, "
			+ " ds.reqdate reqdate, "
			+ " (select rtrim(cast(rtrim(fname) || ' ' || rtrim(mname) || ' ' || rtrim(lname) as char(240) ccsid unicode)) from SNT.personall where id = ds.LPERSID) as lastSigner, "
			+ "ds.ldate as lastDate, "
			+ "cast(ds.price as decimal(9,2)) price, "
			+ "ds.sf_type sf_type, "
			+ "ds.numsf, "
			+ "ds.numfpu, "
			+ " (select code_text from snt.packinfo where id = ds.id fetch first row only) otc_name, "
			+ " (select REFER_TEXT from snt.packinfo where id = ds.id fetch first row only) otc_type "

			+ "from SNT.DocStore as ds, SNT.doctypeflow as df "
			+ " where "
			+ " ds.typeid in (select dtid from SNT.doctypeacc where wrkid = :workerId and (cview = 1 or cedit=1)  )"
			+ " and ds.dropid is null  "
			+ "  and ds.visible =1  "
			+ " and ds.ldate is null "
			+ " and ds.typeid = df.dtid"
			+ " and ds.SignLvl = df.Parent and ds.SignLvl >=0 and groupsgn is null "
			+ " and ds.predid in " + filialsqlstore + " #filt #period "
			+ "#pers #sort #dir #2sort #2dir with ur";

	static private final String GET_INWORK_DOCUMENTS_IDS_SQL_NEW_ROLE2 = " select distinct ds.id as id ,no, "
			+ " ( select rtrim(cast(name as char(120) ccsid unicode )) from SNT.DocType as dt where ds.typeid = dt.id ) as name,"
			+ " ds.CrDate as createDate,"
			+ "ds.opisanie as content, "
			+ "( select rtrim(vname) from SNT.PRED as prd where ds.pred_creator = prd.id  ) as predname ,"
			+ " ds.CrTime as createTime , "
			+ "ds.id_pak as idpak,"
			+ "ds.vagnum vagnum, ds.repdate repdate, "
			+ "case when ds.onbaseid is not null then (select rtrim(name) from snt.dor where id = "
			+ "(select distinct(dorid) from snt.rem_pred where kleim = ds.onbaseid)) end as di, "
			+ "case when ds.onbaseid is not null then (select rtrim(name) from snt.rem_pred "
			+ "where kleim = ds.onbaseid) end as rem_pred, "
			+ "ds.reqnum reqnum, "
			+ "ds.di servicecode, "
			+ " ds.reqdate reqdate, "
			+ " (select rtrim(cast(rtrim(fname) || ' ' || rtrim(mname) || ' ' || rtrim(lname) as char(240) ccsid unicode)) from SNT.personall where id = ds.LPERSID) as lastSigner, "
			+ "ds.ldate as lastDate, "
			+ "cast(ds.price as decimal(9,2)) price, "
			+ "ds.sf_type sf_type, "
			+ "ds.numsf, "
			+ "ds.numfpu, "
			+ " (select code_text from snt.packinfo where id = ds.id fetch first row only) otc_name, "
			+ " (select REFER_TEXT from snt.packinfo where id = ds.id fetch first row only) otc_type "
			+ "from SNT.DocStore as ds, SNT.doctypeflow as df  "
			+ " where "
			+ "ds.id in (select dstf.docid from SNT.docstoreflow dstf where dstf.predid in "
			+ filialsqlflow
			+ " ) "
			+ " and ds.typeid in (select dtid from SNT.doctypeacc where wrkid = :workerId and (cview = 1 or cedit=1) ) "
			+ " and ds.dropid is null "
			+ "and ds.predid in "
			+ filialsqlstore
			+ " and (ds.dropid is null  and ds.signlvl >0 or ds.GROUPSGN =0 and ds.dropid is null ) "
			+ "and ds.visible =1 #filt #period "
			+ "#pers #sort #dir #2sort #2dir with ur";

	static private final String GET_DECLINED_DOCUMENTS_IDS_SQL_ROLE2 = " select distinct ds.id as id ,no, "
			+ " ( select rtrim(cast(name as char(120) ccsid unicode )) from SNT.DocType as dt where ds.typeid = dt.id ) as name,"
			+ " ds.CrDate as createDate,"
			+ "ds.opisanie as content, "
			+ "( select rtrim(vname) from SNT.PRED as prd where ds.pred_creator = prd.id  ) as predname ,"
			+ " ds.CrTime as createTime , "
			+ "ds.id_pak as idpak,"
			+ "ds.vagnum vagnum, ds.repdate repdate, "
			+ "ds.di servicecode, "
			+ "case when ds.onbaseid is not null then (select rtrim(name) from snt.dor where id = "
			+ "(select distinct(dorid) from snt.rem_pred where kleim = ds.onbaseid)) end as di, "
			+ "case when ds.onbaseid is not null then (select rtrim(name) from snt.rem_pred "
			+ "where kleim = ds.onbaseid) end as rem_pred, "
			+ "ds.reqnum reqnum, "
			+ " ds.reqdate reqdate, "
			+ "(select rtrim(cast(rtrim(fname) || ' ' || rtrim(mname) || ' ' || rtrim(lname) as char(240) ccsid unicode)) from SNT.personall where id = ds.DROPID) "
			+ " as lastSigner, "
			+ "date(ds.droptime) as lastDate, "
			+ "cast(ds.price as decimal(9,2)) price, "
			+ "ds.sf_type sf_type, "
			+ "ds.numsf, "
			+ "ds.numfpu, "
			+ " (select code_text from snt.packinfo where id = ds.id fetch first row only) otc_name, "
			+ " (select REFER_TEXT from snt.packinfo where id = ds.id fetch first row only) otc_type "

			+ "from SNT.DocStore as ds, SNT.doctypeflow as df  "
			+ "where ds.typeid in (select dtid from SNT.doctypeacc where wrkid = :workerId and (cview = 1 or cedit=1)  )"
			+ " and ds.dropid is not null  "
			+ " and ds.typeid = df.dtid "
			+ " and ds.predid in "
			+ filialsqlstore
			+ " and ds.dropid is not null   and ds.typeid = df.dtid "
			+ "and ds.DROPPREDID in "
			+ filialsqlflow
			+ " and ds.visible =1 "
			+ "#filt #period #pers #sort #dir #2sort #2dir with ur";

	static private final String GET_ARCHIVE_DOCUMENTS_IDS_SQL_ROLE2 = " select distinct ds.id as id ,no, "
			+ " ( select rtrim(cast(name as char(120) ccsid unicode )) from SNT.DocType as dt where ds.typeid = dt.id ) as name,"
			+ " ds.CrDate as createDate,"
			+ "ds.opisanie as content, "
			+ "( select rtrim(vname) from SNT.PRED as prd where ds.pred_creator = prd.id  ) as predname ,"
			+ " ds.CrTime as createTime , "
			+ "ds.id_pak as idpak,"
			+ "ds.vagnum vagnum, ds.repdate repdate, "
			+ "ds.di servicecode, "
			+ "case when ds.onbaseid is not null then (select rtrim(name) from snt.dor where id = "
			+ "(select distinct(dorid) from snt.rem_pred where kleim = ds.onbaseid)) end as di, "
			+ "case when ds.onbaseid is not null then (select rtrim(name) from snt.rem_pred "
			+ "where kleim = ds.onbaseid) end as rem_pred, "
			+ "ds.reqnum reqnum, "
			+ " ds.reqdate reqdate, "
			+ " (select rtrim(cast(rtrim(fname) || ' ' || rtrim(mname) || ' ' || rtrim(lname) as char(240) ccsid unicode)) from SNT.personall where id = ds.LPERSID) as lastSigner, "
			+ "ds.ldate as lastDate, "
			+ "cast(ds.price as decimal(9,2)) price, "
			+ "ds.sf_type sf_type, "
			+ "ds.numsf, "
			+ "ds.numfpu, "
			+ " (select code_text from snt.packinfo where id = ds.id fetch first row only) otc_name, "
			+ " (select REFER_TEXT from snt.packinfo where id = ds.id fetch first row only) otc_type "
			+ "from SNT.DocStore as ds, SNT.doctypeflow as df  "
			+ " where ds.typeid = df.dtid"
			+ " and ds.SignLvl is NULL "
			+ " and ds.dropid is NULL "
			+ " and (select dsf.predid from snt.docstoreflow dsf where dsf.docid = ds.id "
			+ "and dsf.order = (select max(order) from snt.docstoreflow where docid = ds.id)) in "
			+ filialsqlflow
			+ " and ds.predid in "
			+ filialsqlstore
			+ " and ds.typeid in (select distinct(dtid) from SNT.doctypeacc where wrkid = :workerId "
			+ "and (cview = 1 or cedit=1)) and ds.visible =1 "
			+ "and (ds.groupsgn is null or ds.groupsgn =1) #filt #period #pers #sort #dir #2sort #2dir with ur";

	static private final String GET_ARCHIVE_DOCUMENTS_IDS_SQL_ROLEARCHIVE = " select distinct ds.id as id ,no, "
			+ " ( select rtrim(cast(name as char(120) ccsid unicode )) from SNT.DocType as dt where ds.typeid = dt.id ) as name,"
			+ " ds.CrDate as createDate,"
			+ "ds.opisanie as content, "
			+ "( select rtrim(vname) from SNT.PRED as prd where ds.pred_creator = prd.id  ) as predname ,"
			+ " ds.CrTime as createTime , "
			+ "ds.id_pak as idpak,"
			+ "ds.vagnum vagnum, ds.repdate repdate, "
			+ "ds.di servicecode, "
			+ "case when ds.onbaseid is not null then (select rtrim(name) from snt.dor where id = "
			+ "(select distinct(dorid) from snt.rem_pred where kleim = ds.onbaseid)) end as di, "
			+ "case when ds.onbaseid is not null then (select rtrim(name) from snt.rem_pred "
			+ "where kleim = ds.onbaseid) end as rem_pred, "
			+ "ds.reqnum reqnum, "
			+ " ds.reqdate reqdate, "
			+ " (select rtrim(cast(rtrim(fname) || ' ' || rtrim(mname) || ' ' || rtrim(lname) as char(240) ccsid unicode)) from SNT.personall where id = ds.LPERSID) as lastSigner, "
			+ "ds.ldate as lastDate, "
			+ "cast(ds.price as decimal(9,2)) price, "
			+ "ds.sf_type sf_type, "
			+ "ds.numsf, "
			+ "ds.numfpu, "
			+ " (select code_text from snt.packinfo where id = ds.id fetch first row only) otc_name, "
			+ " (select REFER_TEXT from snt.packinfo where id = ds.id fetch first row only) otc_type "

			+ "from SNT.DocStore as ds, SNT.doctypeflow as df  "
			+ " where ds.typeid = df.dtid"
			+ " and ds.SignLvl is NULL "
			+ " and ds.dropid is NULL "
			+ " and (select dsf.predid from snt.docstoreflow dsf where dsf.docid = ds.id "
			+ "and dsf.order = (select max(order) from snt.docstoreflow where docid = ds.id)) in "
			+ filialsqlarchivestore
			+ " and ds.predid in "
			+ filialsqlarchivestore
			+ " and ds.typeid in (select dtid from SNT.doctypeacc where wrkid = :workerId "
			+ "and (cview = 1 or cedit=1) ) and ds.visible =1 "
			+ "and (ds.groupsgn is null or ds.groupsgn =1) #filt #period #pers #sort #dir #2sort #2dir with ur";

	static final private String GET_WRKID_SQL = "select name from SNT.wrkname where id=:workerId with ur";

	static final private String GET_MARSH_INWORK_DOCUMENTS_SQL = "union select distinct ds.id as id ,no, "
			+ "( select rtrim(cast(name as char(120) ccsid unicode )) from SNT.DocType as dt where ds.typeid = dt.id ) as name, "
			+ "ds.CrDate as createDate, "
			+ "ds.opisanie as content, "
			+ "( select rtrim(vname) from SNT.PRED as prd where ds.pred_creator = prd.id  ) as predname , "
			+ " ds.CrTime as createTime , "
			+ "ds.id_pak as idpak, "
			+ "ds.vagnum vagnum, ds.repdate repdate, "
			+ "ds.di servicecode, "
			+ "case when ds.onbaseid is not null then (select rtrim(name) from snt.dor where id = "
			+ "(select distinct(dorid) from snt.rem_pred where kleim = ds.onbaseid)) end as di, "
			+ "case when ds.onbaseid is not null then (select rtrim(name) from snt.rem_pred "
			+ "where kleim = ds.onbaseid) end as rem_pred, "
			+ " ds.reqdate reqdate, "
			+ " case when ds.etdid is null or ds.typeid  = (select id from snt.doctype where rtrim(name) = 'ГУ-2б') "
			+ " then ds.id else ds.etdid end as etdid, "
			+ " (select rtrim(cast(rtrim(fname) || ' ' || rtrim(mname) || ' ' || rtrim(lname) as char(240) ccsid unicode)) from SNT.personall where id = ds.LPERSID) as lastSigner, "
			+ "ds.ldate as lastDate "
			+ "from SNT.DocStore as ds, SNT.doctypeflow as df  "
			+ "where "
			+ "ds.id in (select id from snt.docstore where etdid in (select distinct(docid) from snt.marsh where predid in "
			+ filialsqlflow
			+ ")) "
			+ "and ds.dropid is null "
			+ "and ds.typeid in (select dtid from SNT.doctypeacc where wrkid = :workerId and (cview = 1 or cedit=1)  ) "
			+ "and  ds.signlvl >=0 and ds.visible =1 #filt #period "
			+ "#pers #sort #dir #2sort #2dir";

	static final private String GET_MARSH_DECLINED_DOCUMENTS_SQL = " union select distinct ds.id as id ,no, "
			+ " ( select rtrim(cast(name as char(120) ccsid unicode )) from SNT.DocType as dt where ds.typeid = dt.id ) as name,"
			+ " ds.CrDate as createDate,"
			+ "ds.opisanie as content, "
			+ "( select rtrim(vname) from SNT.PRED as prd where ds.pred_creator = prd.id  ) as predname ,"
			+ " ds.CrTime as createTime , "
			+ "ds.id_pak as idpak,"
			+ "ds.vagnum vagnum, ds.repdate repdate, "
			+ "ds.di servicecode, "
			+ "case when ds.onbaseid is not null then (select rtrim(name) from snt.dor where id = "
			+ "(select distinct(dorid) from snt.rem_pred where kleim = ds.onbaseid)) end as di, "
			+ "case when ds.onbaseid is not null then (select rtrim(name) from snt.rem_pred "
			+ "where kleim = ds.onbaseid) end as rem_pred, "
			+ "ds.reqnum reqnum, "
			+ "(select rtrim(cast(rtrim(fname) || ' ' || rtrim(mname) || ' ' || rtrim(lname) as char(240) ccsid unicode)) from SNT.personall where id = ds.DROPID) "
			+ " as lastSigner, "
			+ " ds.reqdate reqdate, "
			+ " case when ds.etdid is null or ds.typeid  = (select id from snt.doctype where rtrim(name) = 'ГУ-2б') "
			+ " then ds.id else ds.etdid end as etdid, "
			+ "date(ds.droptime) as lastDate "
			+ "from SNT.DocStore as ds, SNT.doctypeflow as df  "
			+ "where ds.id in (select id from snt.docstore where etdid in (select distinct(docid) from snt.marsh where predid in "
			+ filialsqlflow
			+ ")) "
			+ "and ds.typeid in (select dtid from SNT.doctypeacc where wrkid = :workerId and (cview = 1 or cedit=1)  )"
			+ " and ds.dropid is not null  "
			+ " and ds.typeid = df.dtid "
			+ " and ds.visible =1 #filt #period "
			+ "#pers #sort #dir #2sort #2dir";

	static final private String GET_MARSH_ARCHIVE_DOCUMENTS_SQL = " union select distinct ds.id as id ,no, "
			+ " ( select rtrim(cast(name as char(120) ccsid unicode )) from SNT.DocType as dt where ds.typeid = dt.id ) as name,"
			+ " ds.CrDate as createDate,"
			+ "ds.opisanie as content, "
			+ "( select rtrim(vname) from SNT.PRED as prd where ds.pred_creator = prd.id  ) as predname ,"
			+ " ds.CrTime as createTime , "
			+ "ds.id_pak as idpak,"
			+ "ds.vagnum vagnum, ds.repdate repdate, "
			+ "ds.di servicecode, "
			+ "case when ds.onbaseid is not null then (select rtrim(name) from snt.dor where id = "
			+ "(select distinct(dorid) from snt.rem_pred where kleim = ds.onbaseid)) end as di, "
			+ "case when ds.onbaseid is not null then (select rtrim(name) from snt.rem_pred "
			+ "where kleim = ds.onbaseid) end as rem_pred, "
			+ "ds.reqnum reqnum, "
			+ " ds.reqdate reqdate, "
			+ " case when ds.etdid is null or ds.typeid  = (select id from snt.doctype where rtrim(name) = 'ГУ-2б') "
			+ " then ds.id else ds.etdid end as etdid, "
			+ " (select rtrim(cast(rtrim(fname) || ' ' || rtrim(mname) || ' ' || rtrim(lname) as char(240) ccsid unicode)) from SNT.personall where id = ds.LPERSID) as lastSigner, "
			+ "ds.ldate as lastDate "
			+ "from SNT.DocStore as ds, SNT.doctypeflow as df  "
			+ "where ds.id in (select id from snt.docstore where etdid in (select distinct(docid) from snt.marsh where predid in "
			+ filialsqlflow
			+ ")) "
			+ " and ds.typeid = df.dtid"
			+ " and ds.dropid is null  "
			+ " and ds.SignLvl is NULL "
			+ " and ds.typeid in (select dtid from SNT.doctypeacc where wrkid = :workerId "
			+ "and (cview = 1 or cedit=1) ) and ds.visible =1 "
			+ "and (ds.groupsgn is null or ds.groupsgn =1) #filt #period "
			+ "#pers #sort #dir #2sort #2dir";

	static private final String GET_ACTIVE_DOCUMENT_IDS_SQL_PPS_ROLE2 =

			" select distinct ds.id as id, "
					+ "no, "
					+ " ( select rtrim(cast(name as char(120) ccsid unicode )) from SNT.DocType as dt where ds.typeid = dt.id ) as name,"
					+ " ds.CrDate as createDate,"
					+ "ds.opisanie as content, "
					+ "( select rtrim(vname) from SNT.PRED as prd where ds.pred_creator = prd.id  ) as predname ,"
					+ " ds.CrTime as createTime, "
					+ "ds.id_pak as idpak,"
					+ "(select rtrim(vname) from SNT.PRED as prd where prd.id = (select distinct(predid) from snt.marsh where docid = ds.id)) as rem_pred, "
					+ "ds.vagnum vagnum, ds.repdate repdate, "
					+ "case when ds.onbaseid is not null then (select rtrim(name) from snt.dor where id = "
					+ "(select distinct(dorid) from snt.pps_pred where code = ds.onbaseid)) end as di, "
//					+ "case when ds.onbaseid is not null then (select rtrim(name) from snt.pps_pred "
//					+ "where code = ds.onbaseid) end as rem_pred, "
					+ "ds.reqnum reqnum, "
					+ " ds.reqdate reqdate, "
					+ " (select rtrim(cast(rtrim(fname) || ' ' || rtrim(mname) || ' ' || rtrim(lname) as char(240) ccsid unicode)) from SNT.personall where id = ds.LPERSID) as lastSigner, "
					+ "ds.ldate as lastDate "
					+ "from SNT.DocStore as ds, SNT.doctypeflow as df "
					+ " where "
					+ " ds.typeid in (select dtid from SNT.doctypeacc where wrkid = :workerId and (cview = 1 or cedit=1)  )"
					+ " and ds.dropid is null  " + "  and ds.visible =1  "
					+ " and ds.typeid = df.dtid " + " and ds.SignLvl = df.Parent "// + " and ds.SignLvl = 0"//df.Parent "
					+ " and df.WrkID = :workerId "
					+ " and ds.predid in " + filialsqlstore
					+ " #filt #period " + "#pers #sort #dir #2sort #2dir with ur";
	
	static private final String GET_ACTIVE_DOCUMENT_IDS_SQL_PPS_ROLE3 =

			" select distinct ds.id as id, "
					+ "no, "
					+ " ( select rtrim(cast(name as char(120) ccsid unicode )) from SNT.DocType as dt where ds.typeid = dt.id ) as name,"
					+ " ds.CrDate as createDate,"
					+ "ds.opisanie as content, "
					+ "( select rtrim(vname) from SNT.PRED as prd where ds.pred_creator = prd.id  ) as predname ,"
					+ " ds.CrTime as createTime, "
					+ "ds.id_pak as idpak,"
					+ "ds.vagnum vagnum, ds.repdate repdate, "
					+ "case when ds.onbaseid is not null then (select rtrim(name) from snt.dor where id = "
					+ "(select distinct(dorid) from snt.pps_pred where code = ds.onbaseid)) end as di, "
					+ "case when ds.onbaseid is not null then (select rtrim(name) from snt.pps_pred "
					+ "where code = ds.onbaseid) end as rem_pred, "
					+ "ds.reqnum reqnum, "
					+ " ds.reqdate reqdate, "
					+ " (select rtrim(cast(rtrim(fname) || ' ' || rtrim(mname) || ' ' || rtrim(lname) as char(240) ccsid unicode)) from SNT.personall where id = ds.LPERSID) as lastSigner, "
					+ "ds.ldate as lastDate "
					+ "from SNT.DocStore as ds, SNT.doctypeflow as df "
					+ " where "
					+ " ds.typeid in (select dtid from SNT.doctypeacc where wrkid = :workerId and (cview = 1 or cedit=1)  )"
					+ " and ds.dropid is null  "
					+ "  and ds.visible =1  "
					+ "and ds.SignLvl =0 "

					+ " and ds.id in  (select distinct(docid) from snt.marsh where predid in "
					+ filialsqlflow
					+ ") "
					+  " #filt #period "
					+ "#pers #sort #dir #2sort #2dir with ur";
	
	static private final String GET_ACTIVE_DOCUMENT_IDS_SQL_PPS_ROLE4 =

			" select distinct ds.id as id, "
					+ "no, "
					+ " ( select rtrim(cast(name as char(120) ccsid unicode )) from SNT.DocType as dt where ds.typeid = dt.id ) as name,"
					+ " ds.CrDate as createDate,"
					+ "ds.opisanie as content, "
					+ "( select rtrim(vname) from SNT.PRED as prd where ds.pred_creator = prd.id  ) as predname ,"
					+ " ds.CrTime as createTime, "
					+ "ds.id_pak as idpak,"
					+ "(select rtrim(vname) from SNT.PRED as prd where prd.id = (select distinct(predid) from snt.marsh where docid = ds.id)) as rem_pred, "
					+ "ds.vagnum vagnum, ds.repdate repdate, "
					+ "case when ds.onbaseid is not null then (select rtrim(name) from snt.dor where id = "
					+ "(select distinct(dorid) from snt.pps_pred where code = ds.onbaseid)) end as di, "
//					+ "case when ds.onbaseid is not null then (select rtrim(name) from snt.pps_pred "
//					+ "where code = ds.onbaseid) end as rem_pred, "
					+ "ds.reqnum reqnum, "
					+ " ds.reqdate reqdate, "
					+ " (select rtrim(cast(rtrim(fname) || ' ' || rtrim(mname) || ' ' || rtrim(lname) as char(240) ccsid unicode)) from SNT.personall where id = ds.LPERSID) as lastSigner, "
					+ "ds.ldate as lastDate "
					+ "from SNT.DocStore as ds, SNT.doctypeflow as df "
					+ " where "
					+ " ds.typeid in (select dtid from SNT.doctypeacc where wrkid = :workerId and (cview = 1 or cedit=1)  )"
					+ " and ds.dropid is null  " + "  and ds.visible =1  "
					+ " and ds.typeid = df.dtid " + " and ds.SignLvl > 0"//df.Parent "
					+ " and df.WrkID = :workerId "
					+ " and ds.predid in " + filialsqlstore
					+ " #filt #period " + "#pers #sort #dir #2sort #2dir with ur";
	
	static private final String GET_INWORK_DOCUMENT_IDS_SQL_PPS_ROLE4 =

			" select distinct ds.id as id ,no, "
					+ " ( select rtrim(cast(name as char(120) ccsid unicode )) from SNT.DocType as dt where ds.typeid = dt.id ) as name,"
					+ " ds.CrDate as createDate,"
					+ "ds.opisanie as content, "
					+ "( select rtrim(vname) from SNT.PRED as prd where ds.pred_creator = prd.id  ) as predname , "
					+ " ds.CrTime as createTime , "
					+ "ds.id_pak as idpak,"
					+ "ds.vagnum vagnum, ds.repdate repdate, "
					+ "case when ds.onbaseid is not null then (select rtrim(name) from snt.dor where id = "
					+ "(select distinct(dorid) from snt.rem_pred where kleim = ds.onbaseid)) end as di, "
					+ "case when ds.onbaseid is not null then (select rtrim(name) from snt.rem_pred "
					+ "where kleim = ds.onbaseid) end as rem_pred, "
					+ " ds.reqdate reqdate, "
					+ " (select rtrim(cast(rtrim(fname) || ' ' || rtrim(mname) || ' ' || rtrim(lname) as char(240) ccsid unicode)) from SNT.personall where id = ds.LPERSID) as lastSigner, "
					+ "ds.ldate as lastDate "
					+ "from SNT.DocStore as ds, SNT.doctypeflow as df  "
					+ " where "
					+ " ds.typeid in (select dtid from SNT.doctypeacc where wrkid = :workerId and (cview = 1 or cedit=1) ) "
					+ " and ds.dropid is null "
					+ " and  df.WrkID =:workerId "
					+ " and ds.signlvl > 10 " //df.order)"
					+ " and ds.predid in "
					+ filialsqlstore
					+ " and ds.dropid is null "
					+ "and ds.visible =1 #filt #period "
					+ "#pers #sort #dir #2sort #2dir with ur";
	
	static private final String GET_ACTIVE_DOCUMENT_IDS_SQL_PPS_ROLE =

			" select distinct ds.id as id, "
					+ "no, "
					+ " ( select rtrim(cast(name as char(120) ccsid unicode )) from SNT.DocType as dt where ds.typeid = dt.id ) as name,"
					+ " ds.CrDate as createDate,"
					+ "ds.opisanie as content, "
					+ "( select rtrim(vname) from SNT.PRED as prd where ds.pred_creator = prd.id  ) as predname ,"
					+ " ds.CrTime as createTime, "
					+ "ds.id_pak as idpak,"
					+ "(select rtrim(vname) from SNT.PRED as prd where prd.id = (select distinct(predid) from snt.marsh where docid = ds.id)) as rem_pred, "
					+ "ds.vagnum vagnum, ds.repdate repdate, "
					+ "case when ds.onbaseid is not null then (select rtrim(name) from snt.dor where id = "
					+ "(select distinct(dorid) from snt.pps_pred where code = ds.onbaseid)) end as di, "
					+ "ds.reqnum reqnum, "
					+ " ds.reqdate reqdate, "
					+ " (select rtrim(cast(rtrim(fname) || ' ' || rtrim(mname) || ' ' || rtrim(lname) as char(240) ccsid unicode)) from SNT.personall where id = ds.LPERSID) as lastSigner, "
					+ "ds.ldate as lastDate "
					+ "from SNT.DocStore as ds, SNT.doctypeflow as df "
					+ " where "
					+ " ds.typeid in (select dtid from SNT.doctypeacc where wrkid = :workerId and (cview = 1 or cedit=1)  )"
					+ " and ds.dropid is null  " + "  and ds.visible =1  "
					+ " and ds.typeid = df.dtid " + " and ds.SignLvl = 0 " 
					+ " and ds.predid in " + filialsqlstore
					+ " #filt #period " + "#pers #sort #dir #2sort #2dir with ur";
	
	static private final String GET_INWORK_DOCUMENTS_IDS_SQL_PPS_ROLE = " select distinct ds.id as id ,no, "
			+ " ( select rtrim(cast(name as char(120) ccsid unicode )) from SNT.DocType as dt where ds.typeid = dt.id ) as name,"
			+ " ds.CrDate as createDate,"
			+ "ds.opisanie as content, "
			+ "( select rtrim(vname) from SNT.PRED as prd where ds.pred_creator = prd.id  ) as predname , "
			+ " ds.CrTime as createTime , "
			+ "ds.id_pak as idpak,"
			+ "ds.vagnum vagnum, ds.repdate repdate, "
			+ "case when ds.onbaseid is not null then (select rtrim(name) from snt.dor where id = "
			+ "(select distinct(dorid) from snt.rem_pred where kleim = ds.onbaseid)) end as di, "
			+ "case when ds.onbaseid is not null then (select rtrim(name) from snt.rem_pred "
			+ "where kleim = ds.onbaseid) end as rem_pred, "
			+ " ds.reqdate reqdate, "
			+ " (select rtrim(cast(rtrim(fname) || ' ' || rtrim(mname) || ' ' || rtrim(lname) as char(240) ccsid unicode)) from SNT.personall where id = ds.LPERSID) as lastSigner, "
			+ "ds.ldate as lastDate "
			+ "from SNT.DocStore as ds, SNT.doctypeflow as df  "
			+ " where "
			+ " ds.typeid in (select dtid from SNT.doctypeacc where wrkid = :workerId and (cview = 1 or cedit=1) ) "
			+ " and ds.dropid is null "
			+ " and  df.WrkID =:workerId "
			+ "and ds.predid in "
			+ filialsqlstore
			+ " and ds.dropid is null  and ds.signlvl >0 "
			+ "and ds.visible =1 #filt #period "
			+ "#pers #sort #dir #2sort #2dir with ur";
	
	static private final String GET_INWORK_DOCUMENT_IDS_SQL_PPS_ROLE3 =

			" select distinct ds.id as id, "
					+ "no, "
					+ " ( select rtrim(cast(name as char(120) ccsid unicode )) from SNT.DocType as dt where ds.typeid = dt.id ) as name,"
					+ " ds.CrDate as createDate,"
					+ "ds.opisanie as content, "
					+ "( select rtrim(vname) from SNT.PRED as prd where ds.pred_creator = prd.id  ) as predname ,"
					+ " ds.CrTime as createTime, "
					+ "ds.id_pak as idpak,"
					+ "ds.vagnum vagnum, ds.repdate repdate, "
					+ "case when ds.onbaseid is not null then (select rtrim(name) from snt.dor where id = "
					+ "(select distinct(dorid) from snt.pps_pred where code = ds.onbaseid)) end as di, "
					+ "case when ds.onbaseid is not null then (select rtrim(name) from snt.pps_pred "
					+ "where code = ds.onbaseid) end as rem_pred, "
					+ "ds.reqnum reqnum, "
					+ " ds.reqdate reqdate, "
					+ " (select rtrim(cast(rtrim(fname) || ' ' || rtrim(mname) || ' ' || rtrim(lname) as char(240) ccsid unicode)) from SNT.personall where id = ds.LPERSID) as lastSigner, "
					+ "ds.ldate as lastDate "
					+ "from SNT.DocStore as ds, SNT.doctypeflow as df "
					+ " where "
					+ " ds.typeid in (select dtid from SNT.doctypeacc where wrkid = :workerId and (cview = 1 or cedit=1)  )"
					+ " and ds.dropid is null  "
					+ "  and ds.visible =1  "
					+ "and ds.SignLvl > 0 "
					+ " and ds.id in  (select distinct(docid) from snt.marsh where predid in "
					+ filialsqlflow
					+ ") "
					+  " #filt #period "
					+ "#pers #sort #dir #2sort #2dir with ur";
	
	static private final String GET_ARHIVE_DOCUMENT_IDS_SQL_PPS_ROLE3 =

			" select distinct ds.id as id, "
					+ "no, "
					+ " ( select rtrim(cast(name as char(120) ccsid unicode )) from SNT.DocType as dt where ds.typeid = dt.id ) as name,"
					+ " ds.CrDate as createDate,"
					+ "ds.opisanie as content, "
					+ "( select rtrim(vname) from SNT.PRED as prd where ds.pred_creator = prd.id  ) as predname ,"
					+ " ds.CrTime as createTime, "
					+ "ds.id_pak as idpak,"
					+ "ds.vagnum vagnum, ds.repdate repdate, "
					+ "case when ds.onbaseid is not null then (select rtrim(name) from snt.dor where id = "
					+ "(select distinct(dorid) from snt.pps_pred where code = ds.onbaseid)) end as di, "
					+ "case when ds.onbaseid is not null then (select rtrim(name) from snt.pps_pred "
					+ "where code = ds.onbaseid) end as rem_pred, "
					+ "ds.reqnum reqnum, "
					+ " ds.reqdate reqdate, "
					+ " (select rtrim(cast(rtrim(fname) || ' ' || rtrim(mname) || ' ' || rtrim(lname) as char(240) ccsid unicode)) from SNT.personall where id = ds.LPERSID) as lastSigner, "
					+ "ds.ldate as lastDate "
					+ "from SNT.DocStore as ds, SNT.doctypeflow as df "
					+ " where "
					+ " ds.typeid in (select dtid from SNT.doctypeacc where wrkid = :workerId and (cview = 1 or cedit=1)  )"
					+ " and ds.dropid is null  "
					+ "  and ds.visible =1  "
					+ "and ds.SignLvl is null "
					+ " and ds.id in  (select distinct(docid) from snt.marsh where predid in "
					+ filialsqlflow
					+ ") "
					+  " #filt #period "
					+ "#pers #sort #dir #2sort #2dir with ur";

	static private final String GET_INWORK_DOCUMENTS_IDS_SQL_PPS = " select distinct ds.id as id ,no, "
			+ " ( select rtrim(cast(name as char(120) ccsid unicode )) from SNT.DocType as dt where ds.typeid = dt.id ) as name,"
			+ " ds.CrDate as createDate,"
			+ "ds.opisanie as content, "
			+ "( select rtrim(vname) from SNT.PRED as prd where ds.pred_creator = prd.id  ) as predname , "
			+ " ds.CrTime as createTime , "
			+ "ds.id_pak as idpak,"
			+ "ds.vagnum vagnum, ds.repdate repdate, "
			+ "case when ds.onbaseid is not null then (select rtrim(name) from snt.dor where id = "
			+ "(select distinct(dorid) from snt.rem_pred where kleim = ds.onbaseid)) end as di, "
			+ "case when ds.onbaseid is not null then (select rtrim(name) from snt.rem_pred "
			+ "where kleim = ds.onbaseid) end as rem_pred, "
			+ " ds.reqdate reqdate, "
			+ " (select rtrim(cast(rtrim(fname) || ' ' || rtrim(mname) || ' ' || rtrim(lname) as char(240) ccsid unicode)) from SNT.personall where id = ds.LPERSID) as lastSigner, "
			+ "ds.ldate as lastDate "
			+ "from SNT.DocStore as ds, SNT.doctypeflow as df  "
			+ " where "
		/*	+ "ds.id in (select dstf.docid from SNT.docstoreflow dstf where dstf.predid in "
			+ filialsqlflow
			+ " and dstf.wrkid=:workerId"
			+ " ) "
			+ " and */+ " ds.typeid in (select dtid from SNT.doctypeacc where wrkid = :workerId and (cview = 1 or cedit=1) ) "
			+ " and ds.dropid is null "
			+ " and  df.WrkID =:workerId and ds.signlvl = df.order "
			+ "and ds.predid in "
			+ filialsqlstore
			+ " and ds.dropid is null  and ds.signlvl >0 "
			+ "and ds.visible =1 #filt #period "
			+ "#pers #sort #dir #2sort #2dir with ur";

	static private final String GET_DECLINED_DOCUMENTS_IDS_SQL_PPS = " select distinct ds.id as id ,no, "
			+ " ( select rtrim(cast(name as char(120) ccsid unicode )) from SNT.DocType as dt where ds.typeid = dt.id ) as name,"
			+ " ds.CrDate as createDate,"
			+ "ds.opisanie as content, "
			+ "( select rtrim(vname) from SNT.PRED as prd where ds.pred_creator = prd.id  ) as predname ,"
			+ " ds.CrTime as createTime , "
			+ "ds.id_pak as idpak,"
			+ "ds.vagnum vagnum, ds.repdate repdate, "
			+ "case when ds.onbaseid is not null then (select rtrim(name) from snt.dor where id = "
			+ "(select distinct(dorid) from snt.rem_pred where kleim = ds.onbaseid)) end as di, "
			+ "case when ds.onbaseid is not null then (select rtrim(name) from snt.rem_pred "
			+ "where kleim = ds.onbaseid) end as rem_pred, "
			+ "ds.reqnum reqnum, "
			+ "(select rtrim(cast(rtrim(fname) || ' ' || rtrim(mname) || ' ' || rtrim(lname) as char(240) ccsid unicode)) from SNT.personall where id = ds.DROPID) "
			+ " as lastSigner, "
			+ " ds.reqdate reqdate, "
			+ "date(ds.droptime) as lastDate "
			+ "from SNT.DocStore as ds, SNT.doctypeflow as df  "
			+ "where ds.typeid in (select dtid from SNT.doctypeacc where wrkid = :workerId and (cview = 1 or cedit=1)  )"
			+ " and ds.dropid is not null  "
			+ " and ds.typeid = df.dtid "
			+ " and ds.predid in "
			+ filialsqlstore
			+ "  and ds.typeid = df.dtid "
			+ " and ds.dropid is not null "
			+ "and ds.DROPPREDID in "
			+ filialsqlflow
			// + " and dropwrkid = :workerId "
			+ " and ds.visible =1 "
			+ "#filt #period #pers #sort #dir #2sort #2dir with ur";

	static private final String GET_ARCHIVE_DOCUMENTS_IDS_SQL_PPS = " select distinct ds.id as id ,no, "
			+ " ( select rtrim(cast(name as char(120) ccsid unicode )) from SNT.DocType as dt where ds.typeid = dt.id ) as name,"
			+ " ds.CrDate as createDate,"
			+ "ds.opisanie as content, "
			+ "( select rtrim(vname) from SNT.PRED as prd where ds.pred_creator = prd.id  ) as predname ,"
			+ " ds.CrTime as createTime , "
			+ "ds.id_pak as idpak,"
			+ "ds.vagnum vagnum, ds.repdate repdate, "
			+ "case when ds.onbaseid is not null then (select rtrim(name) from snt.dor where id = "
			+ "(select distinct(dorid) from snt.rem_pred where kleim = ds.onbaseid)) end as di, "
			+ "case when ds.onbaseid is not null then (select rtrim(name) from snt.rem_pred "
			+ "where kleim = ds.onbaseid) end as rem_pred, "
			+ "ds.reqnum reqnum, "
			+ " ds.reqdate reqdate, "
			+ " (select rtrim(cast(rtrim(fname) || ' ' || rtrim(mname) || ' ' || rtrim(lname) as char(240) ccsid unicode)) from SNT.personall where id = ds.LPERSID) as lastSigner, "
			+ "ds.ldate as lastDate "
			+ "from SNT.DocStore as ds, SNT.doctypeflow as df  "
			+ " where ds.typeid = df.dtid"
			+ " and ds.dropid is null  "
			+ " and ds.SignLvl is NULL "
			+ " and ds.predid in "
			+ filialsqlstore

			+ " and ds.typeid in (select dtid from SNT.doctypeacc where wrkid = :workerId "
			+ "and (cview = 1 or cedit=1) ) "
			+ "and ds.visible =1 "
			+ " #filt #period #pers #sort #dir #2sort #2dir with ur";


	static private final String GET_ACTIVE_DOCUMENT_IDS_SQL_SOGL_MATLIC =

	" select distinct ds.id as id, "
			+ "no, "
			+ " ( select rtrim(cast(name as char(120) ccsid unicode )) from SNT.DocType as dt where ds.typeid = dt.id ) as name,"
			+ " ds.CrDate as createDate,"
			+ "ds.opisanie as content, "
			+ "( select rtrim(vname) from SNT.PRED as prd where ds.pred_creator = prd.id  ) as predname ,"
			+ " ds.CrTime as createTime, "
			+ "ds.id_pak as idpak,"
			+ "ds.vagnum vagnum, ds.repdate repdate, "
			+ "case when ds.onbaseid is not null then (select rtrim(name) from snt.dor where id = "
			+ "(select distinct(dorid) from snt.rem_pred where kleim = ds.onbaseid)) end as di, "
			+ "case when ds.onbaseid is not null then (select rtrim(name) from snt.rem_pred "
			+ "where kleim = ds.onbaseid) end as rem_pred, "
			+ "ds.reqnum reqnum, "
			+ " ds.reqdate reqdate, "
			+ " (select rtrim(cast(rtrim(fname) || ' ' || rtrim(mname) || ' ' || rtrim(lname) as char(240) ccsid unicode)) from SNT.personall where id = ds.LPERSID) as lastSigner, "
			+ "ds.ldate as lastDate "
			+ "from SNT.DocStore as ds, SNT.doctypeflow as df "
			+ " where "
			+ " ds.typeid in (select dtid from SNT.doctypeacc where wrkid = :workerId and (cview = 1 or cedit=1)  )"
			+ " and ds.dropid is null  " + "  and ds.visible =0 "
			+ "  and ds.readid =-1 " + " and ds.typeid = df.dtid "
			+ " and ds.SignLvl = df.Parent " + "and ds.SignLvl =0 "
			// + " and df.WrkID = :workerId "
			+ " and ds.predid in " + filialsqlstore + " #filt #period "
			+ "#pers #sort #dir #2sort #2dir with ur";

	static private final String GET_ACTIVE_DOCUMENT_IDS_SQL_RTK =

	" select distinct ds.id as id, "
			+ "no, "
			+ " ( select rtrim(cast(name as char(120) ccsid unicode )) from SNT.DocType as dt where ds.typeid = dt.id ) as name,"
			+ " ds.CrDate as createDate,"
			+ "ds.opisanie as content, "
			+ "( select rtrim(vname) from SNT.PRED as prd where ds.pred_creator = prd.id  ) as predname ,"
			+ " ds.CrTime as createTime, "
			+ "ds.id_pak as idpak,"
			+ "ds.vagnum vagnum, ds.repdate repdate, "
			+ "case when ds.onbaseid is not null then (select rtrim(name) from snt.dor where id = "
			+ "(select distinct(dorid) from snt.rem_pred where kleim = ds.onbaseid)) end as di, "
			+ "case when ds.onbaseid is not null then (select rtrim(name) from snt.rem_pred "
			+ "where kleim = ds.onbaseid) end as rem_pred, "
			+ "ds.reqnum reqnum, "
			+ " ds.reqdate reqdate, "
			+ " (select rtrim(cast(rtrim(fname) || ' ' || rtrim(mname) || ' ' || rtrim(lname) as char(240) ccsid unicode)) from SNT.personall where id = ds.LPERSID) as lastSigner, "
			+ "ds.ldate as lastDate "
			+ "from SNT.DocStore as ds, SNT.doctypeflow as df "
			+ " where "
			+ " ds.typeid in (select dtid from SNT.doctypeacc where wrkid = :workerId and (cview = 1 or cedit=1)  )"
			+ " and ds.dropid is null  " + "  and ds.visible =1 "
			+ " and ds.typeid = df.dtid " + " and ds.SignLvl = df.Parent "
			+ "and ds.SignLvl =0 " + " and df.WrkID = :workerId "
			+ " and ds.predid in " + filialsqlstore + " #filt #period "
			+ "#pers #sort #dir #2sort #2dir with ur";

	static private final String GET_INWORK_DOCUMENTS_IDS_SQL_RTK = " select distinct ds.id as id ,no, "
			+ " ( select rtrim(cast(name as char(120) ccsid unicode )) from SNT.DocType as dt where ds.typeid = dt.id ) as name,"
			+ " ds.CrDate as createDate,"
			+ "ds.opisanie as content, "
			+ "( select rtrim(vname) from SNT.PRED as prd where ds.pred_creator = prd.id  ) as predname , "
			+ " ds.CrTime as createTime , "
			+ "ds.id_pak as idpak,"
			+ "ds.vagnum vagnum, ds.repdate repdate, "
			+ "case when ds.onbaseid is not null then (select rtrim(name) from snt.dor where id = "
			+ "(select distinct(dorid) from snt.rem_pred where kleim = ds.onbaseid)) end as di, "
			+ "case when ds.onbaseid is not null then (select rtrim(name) from snt.rem_pred "
			+ "where kleim = ds.onbaseid) end as rem_pred, "
			+ " ds.reqdate reqdate, "
			+ " (select rtrim(cast(rtrim(fname) || ' ' || rtrim(mname) || ' ' || rtrim(lname) as char(240) ccsid unicode)) from SNT.personall where id = ds.LPERSID) as lastSigner, "
			+ "ds.ldate as lastDate "
//			+" case when ds.ldate is null then "
//			  +" (select date(dt) from snt.docstoreflow where docid = ds.id and predid = :predId) else ds.ldate end as lastDate "
			+ "from SNT.DocStore as ds, SNT.doctypeflow as df  "
			+ " where "
			+ "ds.id in (select dstf.docid from SNT.docstoreflow dstf where dstf.predid in "
			+ filialsqlflow
			+ " and dstf.wrkid=:workerId"
			+ " ) "
			+ " and ds.typeid in (select dtid from SNT.doctypeacc where wrkid = :workerId and (cview = 1 or cedit=1) ) "
//			+ " and ds.typeid in (select dtid from SNT.doctypeacc where wrkid = :workerId and (cview = 1 or cedit=1)  ) "
			+ " and ds.dropid is null  and ds.signlvl >0 "
			+ "and ds.visible =1 #filt #period #pers #marsh with ur";

	static private final String GET_DECLINED_DOCUMENTS_IDS_SQL_RTK = " select distinct ds.id as id ,no, "
			+ " ( select rtrim(cast(name as char(120) ccsid unicode )) from SNT.DocType as dt where ds.typeid = dt.id ) as name,"
			+ " ds.CrDate as createDate,"
			+ "ds.opisanie as content, "
			+ "( select rtrim(vname) from SNT.PRED as prd where ds.pred_creator = prd.id  ) as predname ,"
			+ " ds.CrTime as createTime , "
			+ "ds.id_pak as idpak,"
			+ "ds.vagnum vagnum, ds.repdate repdate, "
			+ "case when ds.onbaseid is not null then (select rtrim(name) from snt.dor where id = "
			+ "(select distinct(dorid) from snt.rem_pred where kleim = ds.onbaseid)) end as di, "
			+ "case when ds.onbaseid is not null then (select rtrim(name) from snt.rem_pred "
			+ "where kleim = ds.onbaseid) end as rem_pred, "
			+ "ds.reqnum reqnum, "
			+ "(select rtrim(cast(rtrim(fname) || ' ' || rtrim(mname) || ' ' || rtrim(lname) as char(240) ccsid unicode)) from SNT.personall where id = ds.DROPID) "
			+ " as lastSigner, "
			+ " ds.reqdate reqdate, "
			+ "date(ds.droptime) as lastDate, "
			+"droptxt "
			+ "from SNT.DocStore as ds, SNT.doctypeflow as df  "
			+ "where ds.typeid in (select dtid from SNT.doctypeacc where wrkid = :workerId and (cview = 1 or cedit=1)  )"
			+ " and ds.typeid = df.dtid "
			+ " and ds.predid in "
			+ filialsqlstore
			+ "  and ds.typeid = df.dtid "
			+ " and ds.dropid is not null "
			+ " and ds.visible =1 "
			+ "#filt #period #pers #marsh with ur";

	static private final String GET_ACTIVE_DOCUMENT_IDS_SQL_MRM =

	" select distinct ds.id as id, "
			+ "no, "
			+ " ( select rtrim(cast(name as char(120) ccsid unicode )) from SNT.DocType as dt where ds.typeid = dt.id ) as name,"
			+ " ds.CrDate as createDate,"
			+ "ds.opisanie as content, "
			+ "( select rtrim(vname) from SNT.PRED as prd where ds.pred_creator = prd.id  ) as predname ,"
			+ " ds.CrTime as createTime, "
			+ "ds.id_pak as idpak,"
			+ "ds.vagnum vagnum, ds.repdate repdate, "
			+ "case when ds.onbaseid is not null then (select rtrim(name) from snt.dor where id = "
			+ "(select distinct(dorid) from snt.rem_pred where kleim = ds.onbaseid)) end as di, "
			+ "case when ds.onbaseid is not null then (select rtrim(name) from snt.rem_pred "
			+ "where kleim = ds.onbaseid) end as rem_pred, "
			+ "ds.reqnum reqnum, "
			+ " case when ds.etdid is null or ds.typeid  = (select id from snt.doctype where rtrim(name) = 'ГУ-2б') "
			+ " then ds.id else ds.etdid end as etdid, "
			+ "ds.di servicecode, "
			+ " ds.reqdate reqdate, "
			+ " (select rtrim(cast(rtrim(fname) || ' ' || rtrim(mname) || ' ' || rtrim(lname) as char(240) ccsid unicode)) from SNT.personall where id = ds.LPERSID) as lastSigner, "
			+ "ds.ldate as lastDate "
			+ "from SNT.DocStore as ds, SNT.doctypeflow as df "
			+ " where "
			+ " ds.typeid in (select dtid from SNT.doctypeacc where wrkid = :workerId and (cview = 1 or cedit=1)  )"
			+ " and ds.dropid is null  "
			+ "  and ds.visible =1 "
			+ " and ds.typeid = df.dtid "
			+ " and ds.SignLvl = df.Parent "
			+ "and ds.SignLvl >=0 "
//			 + " and df.WrkID = :workerId "
			+ " and ds.predid in " + filialsqlstore 
			+ " and ds.ldate is null "
			+ " #filt #period " + "#pers #sort #dir #2sort #2dir with ur";

	static private final String GET_INWORK_DOCUMENTS_IDS_SQL_MRM = " select distinct ds.id as id ,no, "
			+ " ( select rtrim(cast(name as char(120) ccsid unicode )) from SNT.DocType as dt where ds.typeid = dt.id ) as name,"
			+ " ds.CrDate as createDate,"
			+ "ds.opisanie as content, "
			+ "( select rtrim(vname) from SNT.PRED as prd where ds.pred_creator = prd.id  ) as predname , "
			+ " ds.CrTime as createTime , "
			+ "ds.id_pak as idpak,"
			+ "ds.vagnum vagnum, ds.repdate repdate, "
			+ "ds.di servicecode, "
			+ "case when ds.onbaseid is not null then (select rtrim(name) from snt.dor where id = "
			+ "(select distinct(dorid) from snt.rem_pred where kleim = ds.onbaseid)) end as di, "
			+ "case when ds.onbaseid is not null then (select rtrim(name) from snt.rem_pred "
			+ "where kleim = ds.onbaseid) end as rem_pred, "
			+ " ds.reqdate reqdate, "
			+ " case when ds.etdid is null or ds.typeid  = (select id from snt.doctype where rtrim(name) = 'ГУ-2б') "
			+ " then ds.id else ds.etdid end as etdid, "
			+ " (select rtrim(cast(rtrim(fname) || ' ' || rtrim(mname) || ' ' || rtrim(lname) as char(240) ccsid unicode)) from SNT.personall where id = ds.LPERSID) as lastSigner, "
			+ "ds.ldate as lastDate "
			+ "from SNT.DocStore as ds, SNT.doctypeflow as df  "
			+ " where "
			+ "ds.id in (select dstf.docid from SNT.docstoreflow dstf where dstf.predid in "
			+ filialsqlflow
//			 + " and dstf.wrkid=:workerId"
			+ " ) "
			+ " and ds.typeid in (select dtid from SNT.doctypeacc where wrkid = :workerId and (cview = 1 or cedit=1) ) "
			+ " and ds.dropid is null "
			+ "and ds.predid in "
			+ filialsqlstore
			+ " and ds.typeid in (select dtid from SNT.doctypeacc where wrkid = :workerId and (cview = 1 or cedit=1)  ) "
			+ " and (ds.dropid is null  and ds.signlvl >0 or ds.GROUPSGN =0 and ds.dropid is null ) "
			+ " and  df.WrkID =:workerId and ds.signlvl = df.order "
			+ "and ds.visible =1 #filt #period #pers  #marsh with ur";

	static private final String GET_DECLINED_DOCUMENTS_IDS_SQL_MRM = " select distinct ds.id as id ,no, "
			+ " ( select rtrim(cast(name as char(120) ccsid unicode )) from SNT.DocType as dt where ds.typeid = dt.id ) as name,"
			+ " ds.CrDate as createDate,"
			+ "ds.opisanie as content, "
			+ "( select rtrim(vname) from SNT.PRED as prd where ds.pred_creator = prd.id  ) as predname ,"
			+ " ds.CrTime as createTime , "
			+ "ds.id_pak as idpak,"
			+ "ds.vagnum vagnum, ds.repdate repdate, "
			+ "ds.di servicecode, "
			+ "case when ds.onbaseid is not null then (select rtrim(name) from snt.dor where id = "
			+ "(select distinct(dorid) from snt.rem_pred where kleim = ds.onbaseid)) end as di, "
			+ "case when ds.onbaseid is not null then (select rtrim(name) from snt.rem_pred "
			+ "where kleim = ds.onbaseid) end as rem_pred, "
			+ "ds.reqnum reqnum, "
			+ "(select rtrim(cast(rtrim(fname) || ' ' || rtrim(mname) || ' ' || rtrim(lname) as char(240) ccsid unicode)) from SNT.personall where id = ds.DROPID) "
			+ " as lastSigner, "
			+ " ds.reqdate reqdate, "
			+ " case when ds.etdid is null or ds.typeid  = (select id from snt.doctype where rtrim(name) = 'ГУ-2б') "
			+ " then ds.id else ds.etdid end as etdid, "
			+ "date(ds.droptime) as lastDate "
			+ "from SNT.DocStore as ds, SNT.doctypeflow as df  "
			+ "where ds.typeid in (select dtid from SNT.doctypeacc where wrkid = :workerId and (cview = 1 or cedit=1)  )"
			+ " and ds.typeid = df.dtid "
			+ " and (ds.predid in "+filialsqlstore+" or ds.DROPPREDID in "+filialsqlflow+") "
			
			+ " and ds.dropid is not null "
			+ " and ds.visible =1 #filt #period #pers #marsh with ur";

	static private final String GET_ARCHIVE_DOCUMENTS_IDS_SQL_MRM = " select distinct ds.id as id ,no, "
			+ " ( select rtrim(cast(name as char(120) ccsid unicode )) from SNT.DocType as dt where ds.typeid = dt.id ) as name,"
			+ " ds.CrDate as createDate,"
			+ "ds.opisanie as content, "
			+ "( select rtrim(vname) from SNT.PRED as prd where ds.pred_creator = prd.id  ) as predname ,"
			+ " ds.CrTime as createTime , "
			+ "ds.id_pak as idpak,"
			+ "ds.vagnum vagnum, ds.repdate repdate, "
			+ "ds.di servicecode, "
			+ "case when ds.onbaseid is not null then (select rtrim(name) from snt.dor where id = "
			+ "(select distinct(dorid) from snt.rem_pred where kleim = ds.onbaseid)) end as di, "
			+ "case when ds.onbaseid is not null then (select rtrim(name) from snt.rem_pred "
			+ "where kleim = ds.onbaseid) end as rem_pred, "
			+ "ds.reqnum reqnum, "
			+ " ds.reqdate reqdate, "
			+ " case when ds.etdid is null or ds.typeid  = (select id from snt.doctype where rtrim(name) = 'ГУ-2б') "
			+ " then ds.id else ds.etdid end as etdid, "
			+ " (select rtrim(cast(rtrim(fname) || ' ' || rtrim(mname) || ' ' || rtrim(lname) as char(240) ccsid unicode)) from SNT.personall where id = ds.LPERSID) as lastSigner, "
			+ "ds.ldate as lastDate "
			+ "from SNT.DocStore as ds, SNT.doctypeflow as df  "
			+ " where ds.typeid = df.dtid"
			+ " and ds.dropid is null  "
			+ " and ds.SignLvl is NULL "
			// + " and (select dsf.predid from snt.docstoreflow dsf where "
			// + "dsf.docid = ds.id "
			// +
			// "and dsf.order = (select max(order) from snt.docstoreflow where docid = ds.id)) in "
			// + filialsqlflow
			+ " and ds.predid in "
			+ filialsqlstore
			+ " and ds.typeid in (select dtid from SNT.doctypeacc where wrkid = :workerId "
			+ "and (cview = 1 or cedit=1) ) and ds.visible =1 "
			+ "and (ds.groupsgn is null or ds.groupsgn =1) #filt #period #pers #marsh with ur";

	static private final String GET_ACTIVE_DOCUMENT_IDS_SQL_MRM_ROLE2 =

	" select distinct ds.id as id, "
			+ "no, "
			+ " ( select rtrim(cast(name as char(120) ccsid unicode )) from SNT.DocType as dt where ds.typeid = dt.id ) as name,"
			+ " ds.CrDate as createDate,"
			+ "ds.opisanie as content, "
			+ "( select rtrim(vname) from SNT.PRED as prd where ds.pred_creator = prd.id  ) as predname ,"
			+ " ds.CrTime as createTime, "
			+ "ds.id_pak as idpak,"
			+ "ds.vagnum vagnum, ds.repdate repdate, "
			+ "case when ds.onbaseid is not null then (select rtrim(name) from snt.dor where id = "
			+ "(select distinct(dorid) from snt.rem_pred where kleim = ds.onbaseid)) end as di, "
			+ "case when ds.onbaseid is not null then (select rtrim(name) from snt.rem_pred "
			+ "where kleim = ds.onbaseid) end as rem_pred, "
			+ "ds.reqnum reqnum, "
			+ "ds.di servicecode, "
			+ " ds.reqdate reqdate, "
			+ " case when ds.etdid is null or ds.typeid  = (select id from snt.doctype where rtrim(name) = 'ГУ-2б') "
			+ " then ds.id else ds.etdid end as etdid, "
			+ " (select rtrim(cast(rtrim(fname) || ' ' || rtrim(mname) || ' ' || rtrim(lname) as char(240) ccsid unicode)) from SNT.personall where id = ds.LPERSID) as lastSigner, "
			+ "ds.ldate as lastDate "
			+ "from SNT.DocStore as ds, SNT.doctypeflow as df "
			+ " where "
			+ " ds.typeid in (select dtid from SNT.doctypeacc where wrkid = :workerId and (cview = 1 or cedit=1)  )"
			+ " and ds.dropid is null  "
			+ "  and ds.visible =1 "
			+ " and ds.typeid = df.dtid "
			+ " and ds.SignLvl = df.Parent "
			+ "and ds.SignLvl >0 "
//			+ " and ds.lwrkid != :workerId "
			+ " and ds.pred_creator in "
			+ filialsqlstore
//			+ " and ds.nwrkid = (select wrkid from snt.docacceptflow where dtid = ds.typeid fetch first row only) "
			+ " #filt #period " + "#pers #sort #dir #2sort #2dir with ur";

	static private final String GET_INWORK_DOCUMENTS_IDS_SQL_MRM_ROLE2 = " select distinct ds.id as id ,no, "
			+ " ( select rtrim(cast(name as char(120) ccsid unicode )) from SNT.DocType as dt where ds.typeid = dt.id ) as name,"
			+ " ds.CrDate as createDate,"
			+ "ds.opisanie as content, "
			+ "( select rtrim(vname) from SNT.PRED as prd where ds.pred_creator = prd.id  ) as predname , "
			+ " ds.CrTime as createTime , "
			+ "ds.id_pak as idpak,"
			+ "ds.vagnum vagnum, ds.repdate repdate, "
			+ "ds.di servicecode, "
			+ "case when ds.onbaseid is not null then (select rtrim(name) from snt.dor where id = "
			+ "(select distinct(dorid) from snt.rem_pred where kleim = ds.onbaseid)) end as di, "
			+ "case when ds.onbaseid is not null then (select rtrim(name) from snt.rem_pred "
			+ "where kleim = ds.onbaseid) end as rem_pred, "
			+ " ds.reqdate reqdate, "
			+ " case when ds.etdid is null or ds.typeid  = (select id from snt.doctype where rtrim(name) = 'ГУ-2б') "
			+ " then ds.id else ds.etdid end as etdid, "
			+ " (select rtrim(cast(rtrim(fname) || ' ' || rtrim(mname) || ' ' || rtrim(lname) as char(240) ccsid unicode)) from SNT.personall where id = ds.LPERSID) as lastSigner, "
			+ "ds.ldate as lastDate "
			+ "from SNT.DocStore as ds, SNT.doctypeflow as df  "
			+ " where "
			+ "ds.id in (select dstf.docid from SNT.docstoreflow dstf where dstf.predid in "
			+ filialsqlflow
			+ " ) "
			+ " and "
			+ "ds.typeid in (select dtid from SNT.doctypeacc where wrkid = :workerId and (cview = 1 or cedit=1) ) "
			+ " and ds.dropid is null "
			+ "and ds.pred_creator in "
			+ filialsqlstore
			+ " and ds.typeid in (select dtid from SNT.doctypeacc where wrkid = :workerId and (cview = 1 or cedit=1)  ) "
//			+ " and ds.nwrkid = (select wrkid from snt.docacceptflow where dtid = ds.typeid fetch first row only) "
//			+ " and ds.lwrkid = :workerId "
			+ "and ds.visible =1 #filt #period " + "#pers  #marsh with ur";

	static private final String GET_DECLINED_DOCUMENTS_IDS_SQL_MRM_ROLE2 = " select distinct ds.id as id ,no, "
			+ " ( select rtrim(cast(name as char(120) ccsid unicode )) from SNT.DocType as dt where ds.typeid = dt.id ) as name,"
			+ " ds.CrDate as createDate,"
			+ "ds.opisanie as content, "
			+ "( select rtrim(vname) from SNT.PRED as prd where ds.pred_creator = prd.id  ) as predname ,"
			+ " ds.CrTime as createTime , "
			+ "ds.id_pak as idpak,"
			+ "ds.vagnum vagnum, ds.repdate repdate, "
			+ "ds.di servicecode, "
			+ "case when ds.onbaseid is not null then (select rtrim(name) from snt.dor where id = "
			+ "(select distinct(dorid) from snt.rem_pred where kleim = ds.onbaseid)) end as di, "
			+ "case when ds.onbaseid is not null then (select rtrim(name) from snt.rem_pred "
			+ "where kleim = ds.onbaseid) end as rem_pred, "
			+ "ds.reqnum reqnum, "
			+ "(select rtrim(cast(rtrim(fname) || ' ' || rtrim(mname) || ' ' || rtrim(lname) as char(240) ccsid unicode)) from SNT.personall where id = ds.DROPID) "
			+ " as lastSigner, "
			+ " ds.reqdate reqdate, "
			+ " case when ds.etdid is null or ds.typeid  = (select id from snt.doctype where rtrim(name) = 'ГУ-2б') "
			+ " then ds.id else ds.etdid end as etdid, "
			+ "date(ds.droptime) as lastDate "
			+ "from SNT.DocStore as ds, SNT.doctypeflow as df  "
			+ "where ds.typeid in (select dtid from SNT.doctypeacc where wrkid = :workerId and (cview = 1 or cedit=1)  )"
			+ " and ds.typeid = df.dtid "
			+ "and ds.pred_creator in "
			+ filialsqlstore
			+ " and ds.dropid is not null "
			+ " and ds.visible =1 " + " #filt #period #pers #sort #dir #2sort #2dir with ur";

	static private final String GET_ARCHIVE_DOCUMENTS_IDS_SQL_MRM_ROLE2 = " select distinct ds.id as id ,no, "
			+ " ( select rtrim(cast(name as char(120) ccsid unicode )) from SNT.DocType as dt where ds.typeid = dt.id ) as name,"
			+ " ds.CrDate as createDate,"
			+ "ds.opisanie as content, "
			+ "( select rtrim(vname) from SNT.PRED as prd where ds.pred_creator = prd.id  ) as predname ,"
			+ " ds.CrTime as createTime , "
			+ "ds.id_pak as idpak,"
			+ "ds.vagnum vagnum, ds.repdate repdate, "
			+ "ds.di servicecode, "
			+ "case when ds.onbaseid is not null then (select rtrim(name) from snt.dor where id = "
			+ "(select distinct(dorid) from snt.rem_pred where kleim = ds.onbaseid)) end as di, "
			+ "case when ds.onbaseid is not null then (select rtrim(name) from snt.rem_pred "
			+ "where kleim = ds.onbaseid) end as rem_pred, "
			+ "ds.reqnum reqnum, "
			+ " ds.reqdate reqdate, "
			+ " case when ds.etdid is null or ds.typeid  = (select id from snt.doctype where rtrim(name) = 'ГУ-2б') "
			+ " then ds.id else ds.etdid end as etdid, "
			+ " (select rtrim(cast(rtrim(fname) || ' ' || rtrim(mname) || ' ' || rtrim(lname) as char(240) ccsid unicode)) from SNT.personall where id = ds.LPERSID) as lastSigner, "
			+ "ds.ldate as lastDate "
			+ "from SNT.DocStore as ds, SNT.doctypeflow as df  "
			+ " where ds.typeid = df.dtid"
			+ " and ds.dropid is null  "
			+ " and ds.SignLvl is NULL "
			+ " and ds.pred_creator in "
			+ filialsqlstore
			+ " and ds.typeid in (select dtid from SNT.doctypeacc where wrkid = :workerId "
			+ "and (cview = 1 or cedit=1) ) and ds.visible =1 "
			+ "and (ds.groupsgn is null or ds.groupsgn =1) " + " #filt #period #pers #sort #dir #2sort #2dir with ur";

	static private final String GET_ACTIVE_DOCUMENT_IDS_SQL_MRM_ROLE3 =

	" select distinct ds.id as id, "
			+ "no, "
			+ " ( select rtrim(cast(name as char(120) ccsid unicode )) from SNT.DocType as dt where ds.typeid = dt.id ) as name,"
			+ " ds.CrDate as createDate,"
			+ "ds.opisanie as content, "
			+ "( select rtrim(vname) from SNT.PRED as prd where ds.pred_creator = prd.id  ) as predname ,"
			+ " ds.CrTime as createTime, "
			+ "ds.id_pak as idpak,"
			+ "ds.vagnum vagnum, ds.repdate repdate, "
			+ "case when ds.onbaseid is not null then (select rtrim(name) from snt.dor where id = "
			+ "(select distinct(dorid) from snt.rem_pred where kleim = ds.onbaseid)) end as di, "
			+ "case when ds.onbaseid is not null then (select rtrim(name) from snt.rem_pred "
			+ "where kleim = ds.onbaseid) end as rem_pred, "
			+ "ds.reqnum reqnum, "
			+ "ds.di servicecode, "
			+ " ds.reqdate reqdate, "
			+ " case when ds.etdid is null or ds.typeid  = (select id from snt.doctype where rtrim(name) = 'ГУ-2б') "
			+ " then ds.id else ds.etdid end as etdid, "
			+ " (select rtrim(cast(rtrim(fname) || ' ' || rtrim(mname) || ' ' || rtrim(lname) as char(240) ccsid unicode)) from SNT.personall where id = ds.LPERSID) as lastSigner, "
			+ "ds.ldate as lastDate "
			+ "from SNT.DocStore as ds, SNT.doctypeflow as df "
			+ " where "
			+ " ds.typeid in (select dtid from SNT.doctypeacc where wrkid = :workerId and (cview = 1 or cedit=1)  )"
			+ " and ds.dropid is null  " + "  and ds.visible =1 "
			+ " and ds.typeid = df.dtid " + " and ds.SignLvl = df.Parent "
			+ "and ds.SignLvl >=0 " + " and ds.pred_creator in "
			+ filialsqlstore + " #filt #period "
			+ "#pers #sort #dir #2sort #2dir with ur";

	static private final String GET_ARCHIVE_DOCUMENTS_IDS_SQL_MRM_ROLE3 = " select distinct ds.id as id ,no, "
			+ " ( select rtrim(cast(name as char(120) ccsid unicode )) from SNT.DocType as dt where ds.typeid = dt.id ) as name,"
			+ " ds.CrDate as createDate,"
			+ "ds.opisanie as content, "
			+ "( select rtrim(vname) from SNT.PRED as prd where ds.pred_creator = prd.id  ) as predname ,"
			+ " ds.CrTime as createTime , "
			+ "ds.id_pak as idpak,"
			+ "ds.vagnum vagnum, ds.repdate repdate, "
			+ "ds.di servicecode, "
			+ "case when ds.onbaseid is not null then (select rtrim(name) from snt.dor where id = "
			+ "(select distinct(dorid) from snt.rem_pred where kleim = ds.onbaseid)) end as di, "
			+ "case when ds.onbaseid is not null then (select rtrim(name) from snt.rem_pred "
			+ "where kleim = ds.onbaseid) end as rem_pred, "
			+ "ds.reqnum reqnum, "
			+ " ds.reqdate reqdate, "
			+ " case when ds.etdid is null or ds.typeid  = (select id from snt.doctype where rtrim(name) = 'ГУ-2б') "
			+ " then ds.id else ds.etdid end as etdid, "
			+ " (select rtrim(cast(rtrim(fname) || ' ' || rtrim(mname) || ' ' || rtrim(lname) as char(240) ccsid unicode)) from SNT.personall where id = ds.LPERSID) as lastSigner, "
			+ "ds.ldate as lastDate "
			+ "from SNT.DocStore as ds, SNT.doctypeflow as df  "
			+ " where ds.typeid = df.dtid"
			+ " and ds.dropid is null  "
			+ " and ds.SignLvl is NULL "
			+ " and ds.pred_creator in "
			+ filialsqlstore
			+ " and ds.typeid in (select dtid from SNT.doctypeacc where wrkid = :workerId "
			+ "and (cview = 1 or cedit=1) ) and ds.visible =1 "
			+ "and (ds.groupsgn is null or ds.groupsgn =1) " + " #filt #period #pers #sort #dir #2sort #2dir with ur";

	static private final String GET_DECLINED_DOCUMENTS_IDS_SQL_MRM_ROLE3 = " select distinct ds.id as id ,no, "
			+ " ( select rtrim(cast(name as char(120) ccsid unicode )) from SNT.DocType as dt where ds.typeid = dt.id ) as name,"
			+ " ds.CrDate as createDate,"
			+ "ds.opisanie as content, "
			+ "( select rtrim(vname) from SNT.PRED as prd where ds.pred_creator = prd.id  ) as predname ,"
			+ " ds.CrTime as createTime , "
			+ "ds.id_pak as idpak,"
			+ "ds.vagnum vagnum, ds.repdate repdate, "
			+ "ds.di servicecode, "
			+ "case when ds.onbaseid is not null then (select rtrim(name) from snt.dor where id = "
			+ "(select distinct(dorid) from snt.rem_pred where kleim = ds.onbaseid)) end as di, "
			+ "case when ds.onbaseid is not null then (select rtrim(name) from snt.rem_pred "
			+ "where kleim = ds.onbaseid) end as rem_pred, "
			+ "ds.reqnum reqnum, "
			+ "(select rtrim(cast(rtrim(fname) || ' ' || rtrim(mname) || ' ' || rtrim(lname) as char(240) ccsid unicode)) from SNT.personall where id = ds.DROPID) "
			+ " as lastSigner, "
			+ " ds.reqdate reqdate, "
			+ " case when ds.etdid is null or ds.typeid  = (select id from snt.doctype where rtrim(name) = 'ГУ-2б') "
			+ " then ds.id else ds.etdid end as etdid, "
			+ "date(ds.droptime) as lastDate "
			+ "from SNT.DocStore as ds, SNT.doctypeflow as df  "
			+ "where ds.typeid in (select dtid from SNT.doctypeacc where wrkid = :workerId and (cview = 1 or cedit=1)  )"
			+ " and ds.typeid = df.dtid "
			+ " and ds.pred_creator in "
			+ filialsqlstore
			// + "  and ds.typeid = df.dtid "
			+ " and ds.dropid is not null "
			// + " and dropwrkid = :workerId "
			+ " and ds.visible =1 " + " #filt #period #pers #sort #dir #2sort #2dir with ur";

	static final private String GET_DOCUMENTS_CONTENT_SQL = "select"
			+ " ds.id as id, no,"
			+ " ds.typeid as formType, "
			+ " CASE WHEN sf_sign is Null THEN -1 ELSE sf_sign END as sign, "
			+ "ds.opisanie as content, "
			+ " ( select rtrim(cast(name as char(120) ccsid unicode )) from SNT.DocType as dt where ds.typeid = dt.id ) as name,"
			+ "( select rtrim(vname) from SNT.PRED as prd where ds.pred_creator = prd.id  ) as predname ,"
			+ " ds.CrDate as createDate,"
			+ " ds.CrTime as createTime,"
			+ "ds.id_pak as idpak, "
			+ "ds.vagnum vagnum, ds.repdate repdate, ds.reqdate reqdate,"
			+ " ds.stat as stat, "
			+ "ds.reqnum reqnum, "
			+ "case when ds.onbaseid is not null then (select rtrim(name) from snt.dor where id = "
			+ "(select distinct(dorid) from snt.rem_pred where kleim = ds.onbaseid)) end as di, "
			+ "case when ds.onbaseid is not null then (select rtrim(name) from snt.rem_pred "
			+ "where kleim = ds.onbaseid) end as rem_pred, "
			+ "ds.torpackid, "
			+ "ds.di servicecode, "
			+ " case when ds.droptime is null then "
			+ "(select rtrim(cast(rtrim(fname) || ' ' || rtrim(mname) || ' ' || rtrim(lname) as char(240) ccsid unicode)) from SNT.personall where id = ds.LPERSID) "
			+ "else (select rtrim(cast(rtrim(fname) || ' ' || rtrim(mname) || ' ' || rtrim(lname) as char(240) ccsid unicode)) from SNT.personall where id = ds.DROPID) end "
			+ " as lastSigner,"
			+ " case when ds.droptime is null then "
			+ "ds.ldate "
			+ "else date(ds.droptime) end "
			+ "as lastDate,"
			+ " case when ds.droptime is null then "
			+ " ds.ltime "
			+ "else time(ds.droptime) end "
			+ "as lastTime, "
//			+"case "
//			+"when (ds.droptime is null and ds.ldate is null) then (select date(dt) from snt.docstoreflow where docid= ds.id) "
//			+"when (ds.droptime is not null) then date(droptime) "
//			+"else ds.ldate end as lastDate, "
//			+"case "
//			+"when (ds.droptime is null and ds.ltime is null) then (select time(dt) from snt.docstoreflow where docid= ds.id) "
//			+"when (ds.droptime is not null) then time(droptime) "
//			+"else ds.ltime end as lastTime, "
			+ "cast(ds.price as decimal(9,2)) price, "
			+ " case when ds.etdid is null or ds.typeid  = (select id from snt.doctype where rtrim(name) = 'ГУ-2б') "
			+ " then ds.id else ds.etdid end as etdid, "
			+ "ds.sf_type sf_type, "
			+ "ds.numsf, "
			+ "ds.numfpu, "
			+ " (select code_text from snt.packinfo where id = ds.id fetch first row only) otc_name, "
			+ " (select REFER_TEXT from snt.packinfo where id = ds.id fetch first row only) otc_type, "
			+" droptxt, "
			+ " ds.docData as docData from SNT.DocStore as ds"
			+ " where ds.id in (:idList) #sort #dir #2sort #2dir with ur";

	static private final String GET_ARCHIVE_DOCUMENTS_IDS_SQL_RZDS_ROLE1 = " select distinct ds.id as id ,no, "
			+ " ( select rtrim(cast(name as char(120) ccsid unicode )) from SNT.DocType as dt where ds.typeid = dt.id ) as name,"
			+ " ds.CrDate as createDate,"
			+ "ds.opisanie as content, "
			+ "( select rtrim(vname) from SNT.PRED as prd where ds.pred_creator = prd.id  ) as predname , "
			+ " ds.CrTime as createTime , "
			+ "ds.id_pak as idpak,"
			+ "ds.vagnum vagnum, ds.repdate repdate, "
			+ "case when ds.onbaseid is not null then (select rtrim(name) from snt.dor where id = "
			+ "(select distinct(dorid) from snt.rem_pred where kleim = ds.onbaseid)) end as di, "
			+ "case when ds.onbaseid is not null then (select rtrim(name) from snt.rem_pred "
			+ "where kleim = ds.onbaseid) end as rem_pred, "
			+ " ds.reqdate reqdate, "
			+ " (select rtrim(cast(rtrim(fname) || ' ' || rtrim(mname) || ' ' || rtrim(lname) as char(240) ccsid unicode)) from SNT.personall where id = ds.LPERSID) as lastSigner, "
			+ "ds.ldate as lastDate "
			+ "from SNT.DocStore as ds, SNT.doctypeflow as df  "
			+ " where "
			+ "ds.id in (select dstf.docid from SNT.docstoreflow dstf where dstf.predid in "
			+ filialsqlflow
			+ " and dstf.wrkid=:workerId"
			+ " ) "
			+ " and ds.typeid in (select dtid from SNT.doctypeacc where wrkid = :workerId and (cview = 1 or cedit=1) ) "

			+ " and ds.typeid in (select dtid from SNT.doctypeacc where wrkid = :workerId and (cview = 1 or cedit=1)  ) "
			+ " and ds.dropid is null  and ds.signlvl is null "
			+ "and ds.visible =1 #filt #period " + "#pers with ur";

	static private final String GET_DECLINED_DOCUMENTS_IDS_SQL_RZDS_ROLE1 = " select distinct ds.id as id ,no, "
			+ " ( select rtrim(cast(name as char(120) ccsid unicode )) from SNT.DocType as dt where ds.typeid = dt.id ) as name,"
			+ " ds.CrDate as createDate,"
			+ "ds.opisanie as content, "
			+ "( select rtrim(vname) from SNT.PRED as prd where ds.pred_creator = prd.id  ) as predname , "
			+ " ds.CrTime as createTime , "
			+ "ds.id_pak as idpak,"
			+ "ds.vagnum vagnum, ds.repdate repdate, "
			+ "case when ds.onbaseid is not null then (select rtrim(name) from snt.dor where id = "
			+ "(select distinct(dorid) from snt.rem_pred where kleim = ds.onbaseid)) end as di, "
			+ "case when ds.onbaseid is not null then (select rtrim(name) from snt.rem_pred "
			+ "where kleim = ds.onbaseid) end as rem_pred, "
			+ " ds.reqdate reqdate, "
			+ " (select rtrim(cast(rtrim(fname) || ' ' || rtrim(mname) || ' ' || rtrim(lname) as char(240) ccsid unicode)) from SNT.personall where id = ds.LPERSID) as lastSigner, "
			+ "ds.ldate as lastDate "
			+ "from SNT.DocStore as ds, SNT.doctypeflow as df  "
			+ " where "
			+ "ds.id in (select dstf.docid from SNT.docstoreflow dstf where dstf.predid in "
			+ filialsqlflow
			+ " and dstf.wrkid=:workerId"
			+ " ) "
			+ " and ds.typeid in (select dtid from SNT.doctypeacc where wrkid = :workerId and (cview = 1 or cedit=1) ) "

			+ " and ds.typeid in (select dtid from SNT.doctypeacc where wrkid = :workerId and (cview = 1 or cedit=1)  ) "
			+ " and (ds.dropid is not null  and ds.signlvl >0) or (ds.predid in "
			+ filialsqlstore
			+ " and ds.dropid is not null and ds.signlvl = 0) "
			+ "and ds.visible =1 #filt #period " + "#pers with ur";

	static private final String GET_ACTIVE_DOCUMENT_IDS_SQL_RZDS_ROLE2 =

	" select distinct ds.id as id, "
			+ "no, "
			+ " ( select rtrim(cast(name as char(120) ccsid unicode )) from SNT.DocType as dt where ds.typeid = dt.id ) as name,"
			+ " ds.CrDate as createDate,"
			+ "ds.opisanie as content, "
			+ "( select rtrim(vname) from SNT.PRED as prd where ds.pred_creator = prd.id  ) as predname ,"
			+ " ds.CrTime as createTime, "
			+ "ds.id_pak as idpak,"
			+ "ds.vagnum vagnum, ds.repdate repdate, "
			+ "case when ds.onbaseid is not null then (select rtrim(name) from snt.dor where id = "
			+ "(select distinct(dorid) from snt.rem_pred where kleim = ds.onbaseid)) end as di, "
			+ "case when ds.onbaseid is not null then (select rtrim(name) from snt.rem_pred "
			+ "where kleim = ds.onbaseid) end as rem_pred, "
			+ "ds.reqnum reqnum, "
			+ " ds.reqdate reqdate, "
			+ " (select rtrim(cast(rtrim(fname) || ' ' || rtrim(mname) || ' ' || rtrim(lname) as char(240) ccsid unicode)) from SNT.personall where id = ds.LPERSID) as lastSigner, "
			+ "ds.ldate as lastDate "
			+ "from SNT.DocStore as ds, SNT.doctypeflow as df "
			+ " where "
			+ " ds.typeid in (select dtid from SNT.doctypeacc where wrkid = :workerId and (cview = 1 or cedit=1)  )"
			+ " and ds.dropid is null  " + "  and ds.visible =1 "
			+ " and ds.typeid = df.dtid " + " and ds.SignLvl = df.Parent "
			+ "and ds.SignLvl >0 " + " and df.WrkID = :workerId "
			+ " and ds.predid in " + filialsqlstore + " #filt #period "
			+ "#pers #sort #dir #2sort #2dir with ur";

	static private final String GET_ACTIVE_DOCUMENT_IDS_VRKSIGN_SQL =

			" select distinct ds.id as id, "
					+ "no, "
					+ " ( select rtrim(cast(name as char(120) ccsid unicode )) from SNT.DocType as dt where ds.typeid = dt.id ) as name,"
					+ " ds.CrDate as createDate,"
					+ "ds.opisanie as content, "
					+ "( select rtrim(vname) from SNT.PRED as prd where ds.pred_creator = prd.id  ) as predname ,"
					+ " ds.CrTime as createTime, "
					+ "ds.id_pak as idpak,"
					+ "ds.vagnum vagnum, ds.repdate repdate, "
					+ "case when ds.onbaseid is not null then (select rtrim(name) from snt.dor where id = "
					+ "(select distinct(dorid) from snt.rem_pred where kleim = ds.onbaseid)) end as di, "
					+ "case when ds.onbaseid is not null then (select rtrim(name) from snt.rem_pred "
					+ "where kleim = ds.onbaseid) end as rem_pred, "
					+ "ds.reqnum reqnum, "
					+ "ds.di servicecode, "
					+ " ds.reqdate reqdate, "
					+ " (select rtrim(cast(rtrim(fname) || ' ' || rtrim(mname) || ' ' || rtrim(lname) as char(240) ccsid unicode)) from SNT.personall where id = ds.LPERSID) as lastSigner, "
					+ "ds.ldate as lastDate, "
					+ "cast(ds.price as decimal(9,2)) price, "
					+ "ds.sf_type sf_type, "
					+ "ds.numsf, "
					+ "ds.numfpu, "
					+ " (select code_text from snt.packinfo where id = ds.id fetch first row only) otc_name, "
					+ " (select REFER_TEXT from snt.packinfo where id = ds.id fetch first row only) otc_type "
					+ "from SNT.DocStore as ds, SNT.doctypeflow as df "
					+ " where "
					+ " ds.typeid in (select dtid from SNT.doctypeacc where wrkid = :workerId and (cview = 1 or cedit=1)  )"
					+ " and ds.dropid is null  "
//					+ "  and ds.visible =1 "
//					+ " and ds.typeid = df.dtid "
//					+ " and ds.SignLvl = df.Parent "
					+ "and ds.SignLvl >=0 "
					// + " and df.WrkID = :workerId "
					+ " and ds.predid in " + filialsqlstore + " "
					+ " #filt #period " + "#pers #sort #dir #2sort #2dir with ur";
	
	static private final String GET_DECLINED_DOCUMENT_IDS_VRKSIGN_SQL =

			" select distinct ds.id as id, "
					+ "no, "
					+ " ( select rtrim(cast(name as char(120) ccsid unicode )) from SNT.DocType as dt where ds.typeid = dt.id ) as name,"
					+ " ds.CrDate as createDate,"
					+ "ds.opisanie as content, "
					+ "( select rtrim(vname) from SNT.PRED as prd where ds.pred_creator = prd.id  ) as predname ,"
					+ " ds.CrTime as createTime, "
					+ "ds.id_pak as idpak,"
					+ "ds.vagnum vagnum, ds.repdate repdate, "
					+ "case when ds.onbaseid is not null then (select rtrim(name) from snt.dor where id = "
					+ "(select distinct(dorid) from snt.rem_pred where kleim = ds.onbaseid)) end as di, "
					+ "case when ds.onbaseid is not null then (select rtrim(name) from snt.rem_pred "
					+ "where kleim = ds.onbaseid) end as rem_pred, "
					+ "ds.reqnum reqnum, "
					+ "ds.di servicecode, "
					+ " ds.reqdate reqdate, "
					+ " (select rtrim(cast(rtrim(fname) || ' ' || rtrim(mname) || ' ' || rtrim(lname) as char(240) ccsid unicode)) from SNT.personall where id = ds.LPERSID) as lastSigner, "
					+ "ds.ldate as lastDate, "
					+ "cast(ds.price as decimal(9,2)) price, "
					+ "ds.sf_type sf_type, "
					+ "ds.numsf, "
					+ "ds.numfpu, "
					+ " (select code_text from snt.packinfo where id = ds.id fetch first row only) otc_name, "
					+ " (select REFER_TEXT from snt.packinfo where id = ds.id fetch first row only) otc_type "
					+ "from SNT.DocStore as ds, SNT.doctypeflow as df "
					+ " where "
					+ " ds.typeid in (select dtid from SNT.doctypeacc where wrkid = :workerId and (cview = 1 or cedit=1)  )"
					+ " and ds.dropid is not null  "
//					+ "  and ds.visible =1 "
//					+ " and ds.typeid = df.dtid "
//					+ " and ds.SignLvl = df.Parent "
//					+ "and ds.SignLvl >=0 "
					// + " and df.WrkID = :workerId "
					+ " and ds.predid in " + filialsqlstore + " "
					+ " #filt #period " + "#pers #sort #dir #2sort #2dir with ur";
	
	static private final String GET_ARCHIVE_DOCUMENT_IDS_VRKSIGN_SQL =

			" select distinct ds.id as id, "
					+ "no, "
					+ " ( select rtrim(cast(name as char(120) ccsid unicode )) from SNT.DocType as dt where ds.typeid = dt.id ) as name,"
					+ " ds.CrDate as createDate,"
					+ "ds.opisanie as content, "
					+ "( select rtrim(vname) from SNT.PRED as prd where ds.pred_creator = prd.id  ) as predname ,"
					+ " ds.CrTime as createTime, "
					+ "ds.id_pak as idpak,"
					+ "ds.vagnum vagnum, ds.repdate repdate, "
					+ "case when ds.onbaseid is not null then (select rtrim(name) from snt.dor where id = "
					+ "(select distinct(dorid) from snt.rem_pred where kleim = ds.onbaseid)) end as di, "
					+ "case when ds.onbaseid is not null then (select rtrim(name) from snt.rem_pred "
					+ "where kleim = ds.onbaseid) end as rem_pred, "
					+ "ds.reqnum reqnum, "
					+ "ds.di servicecode, "
					+ " ds.reqdate reqdate, "
					+ " (select rtrim(cast(rtrim(fname) || ' ' || rtrim(mname) || ' ' || rtrim(lname) as char(240) ccsid unicode)) from SNT.personall where id = ds.LPERSID) as lastSigner, "
					+ "ds.ldate as lastDate, "
					+ "cast(ds.price as decimal(9,2)) price, "
					+ "ds.sf_type sf_type, "
					+ "ds.numsf, "
					+ "ds.numfpu, "
					+ " (select code_text from snt.packinfo where id = ds.id fetch first row only) otc_name, "
					+ " (select REFER_TEXT from snt.packinfo where id = ds.id fetch first row only) otc_type "
					+ "from SNT.DocStore as ds, SNT.doctypeflow as df "
					+ " where "
					+ " ds.typeid in (select dtid from SNT.doctypeacc where wrkid = :workerId and (cview = 1 or cedit=1)  )"
					+ " and ds.dropid is null  "
//					+ "  and ds.visible =1 "
//					+ " and ds.typeid = df.dtid "
//					+ " and ds.SignLvl = df.Parent "
					+ "and ds.SignLvl is null "
					// + " and df.WrkID = :workerId "
					+ " and ds.predid in " + filialsqlstore + " "
					+ " #filt #period " + "#pers #sort #dir #2sort #2dir with ur";
	
	static final private String GET_DOCUMENTS_CONTENT_SQL_PPS = "select"
			+ " ds.id as id, no,"
			+ " ds.typeid as formType, "
			+ " CASE WHEN sf_sign is Null THEN -1 ELSE sf_sign END as sign, "
			+ "ds.opisanie as content, "
			+ " ( select rtrim(cast(name as char(120) ccsid unicode )) from SNT.DocType as dt where ds.typeid = dt.id ) as name,"
			+ "( select rtrim(vname) from SNT.PRED as prd where ds.pred_creator = prd.id  ) as predname ,"
			+ " ds.CrDate as createDate,"
			+ " ds.CrTime as createTime,"
			+ "ds.droptxt as droptxt,"
			+ "ds.id_pak as idpak, "
			+ "ds.vagnum vagnum, ds.repdate repdate, ds.reqdate reqdate,"
			+ " ds.stat as stat, "
			+ "ds.reqnum reqnum, "
			+ "ds.di servicecode, "
			+ "case when ds.onbaseid is not null then (select rtrim(name) from snt.dor where id = "
			+ "(select distinct(dorid) from snt.pps_pred where code = ds.onbaseid)) end as di, "
			+ "case when ds.onbaseid is not null then (select rtrim(name) from snt.pps_pred "
			+ "where code = ds.onbaseid) end as rem_pred, "
			+ " case when ds.droptime is null then "
			+ "(select rtrim(cast(rtrim(fname) || ' ' || rtrim(mname) || ' ' || rtrim(lname) as char(240) ccsid unicode)) from SNT.personall where id = ds.LPERSID) "
			+ "else (select rtrim(cast(rtrim(fname) || ' ' || rtrim(mname) || ' ' || rtrim(lname) as char(240) ccsid unicode)) from SNT.personall where id = ds.DROPID) end "
			+ " as lastSigner,"
			+ " case when ds.droptime is null then "
			+ "ds.ldate "
			+ "else date(ds.droptime) end "
			+ "as lastDate,"
			+ "  case when ds.droptime is null then "
			+ " ds.ltime "
			+ "else time(ds.droptime) end "
			+ "as lastTime, "
			+ "cast(ds.price as decimal(9,2)) price, "
			+ "ds.sf_type sf_type, "
			+ "ds.numsf, "
			+ "ds.numfpu, "
			+ " case when ds.otc_code is not null then "
			// +" (select rtrim(object_vname) from snt.dic_objects where object_kod = ds.otc_code) "
			+ " ds.otc_code "
			+ " else null end otc_name, "
			+ " case when ds.otc_code is not null then "
			+ " (select rtrim(refer_name) from snt.dic_refer where refer_id = (select refer from snt.dic_objects where object_kod = ds.otc_code)) "
			+ " else null end otc_type, " + " ds.docData as docData "
			+ " from SNT.DocStore as ds"
			+ " where ds.id in (:idList) #sort #dir #2sort #2dir with ur";
	
	static final private String GET_DOCUMENTS_CONTENT_SQL_PPS_2 = "select"
			+ " ds.id as id, no,"
			+ " ds.typeid as formType, "
			+ " CASE WHEN sf_sign is Null THEN -1 ELSE sf_sign END as sign, "
			+ "ds.opisanie as content, "
			+ " ( select rtrim(cast(name as char(120) ccsid unicode )) from SNT.DocType as dt where ds.typeid = dt.id ) as name,"
			+ "( select rtrim(vname) from SNT.PRED as prd where ds.pred_creator = prd.id  ) as predname ,"
			+ " ds.CrDate as createDate,"
			+ " ds.CrTime as createTime,"
			+ "ds.droptxt as droptxt,"
			+ "ds.id_pak as idpak, "
			+ "ds.vagnum vagnum, ds.repdate repdate, ds.reqdate reqdate,"
			+ " ds.stat as stat, "
			+ "ds.reqnum reqnum, "
			+ "ds.di servicecode, "
			+ "ds.di as di, "
			+ "(select rtrim(vname) from SNT.PRED as prd where prd.id = (select distinct(predid) from snt.marsh where docid = ds.id)) as rem_pred, "
			+ " case when ds.droptime is null then "
			+ "(select rtrim(cast(rtrim(fname) || ' ' || rtrim(mname) || ' ' || rtrim(lname) as char(240) ccsid unicode)) from SNT.personall where id = ds.LPERSID) "
			+ "else (select rtrim(cast(rtrim(fname) || ' ' || rtrim(mname) || ' ' || rtrim(lname) as char(240) ccsid unicode)) from SNT.personall where id = ds.DROPID) end "
			+ " as lastSigner,"
			+ " case when ds.droptime is null then "
			+ "ds.ldate "
			+ "else date(ds.droptime) end "
			+ "as lastDate,"
			+ "  case when ds.droptime is null then "
			+ " ds.ltime "
			+ "else time(ds.droptime) end "
			+ "as lastTime, "
			+ "cast(ds.price as decimal(9,2)) price, "
			+ "ds.sf_type sf_type, "
			+ " case when ds.etdid is null or ds.typeid  = (select id from snt.doctype where rtrim(name) = 'ГУ-2б') "
			+ " then ds.id else ds.etdid end as etdid, "
			+ "ds.numsf, "
			+ "ds.numfpu, "
			+ " case when ds.otc_code is not null then "
			// +" (select rtrim(object_vname) from snt.dic_objects where object_kod = ds.otc_code) "
			+ " ds.otc_code "
			+ " else null end otc_name, "
			+ " case when ds.otc_code is not null then "
			+ " (select rtrim(refer_name) from snt.dic_refer where refer_id = (select refer from snt.dic_objects where object_kod = ds.otc_code)) "
			+ " else null end otc_type, " + " ds.docData as docData "
			+ " from SNT.DocStore as ds"
			+ " where ds.id in (:idList) #sort #dir #2sort #2dir with ur";
	
	static final private String GET_DOCUMENTS_CONTENT_SQL_PPS_3 = "select"
			+ " ds.id as id, no,"
			+ " ds.typeid as formType, "
			+ " CASE WHEN sf_sign is Null THEN -1 ELSE sf_sign END as sign, "
			+ "ds.opisanie as content, "
			+ " ( select rtrim(cast(name as char(120) ccsid unicode )) from SNT.DocType as dt where ds.typeid = dt.id ) as name,"
			+ "( select rtrim(vname) from SNT.PRED as prd where ds.pred_creator = prd.id  ) as predname ,"
			+ " ds.CrDate as createDate,"
			+ " ds.CrTime as createTime,"
			+ "ds.droptxt as droptxt,"
			+ "ds.id_pak as idpak, "
			+ "ds.vagnum vagnum, ds.repdate repdate, ds.reqdate reqdate,"
			+ " ds.stat as stat, "
			+ "ds.reqnum reqnum, "
			+ "ds.di servicecode, "
			+ "case when ds.onbaseid is not null then (select rtrim(name) from snt.dor where id = "
			+ "(select distinct(dorid) from snt.pps_pred where code = ds.onbaseid)) end as di, "
			+ "ds.rem_pred as rem_pred, "
			+ " case when ds.droptime is null then "
			+ "(select rtrim(cast(rtrim(fname) || ' ' || rtrim(mname) || ' ' || rtrim(lname) as char(240) ccsid unicode)) from SNT.personall where id = ds.LPERSID) "
			+ "else (select rtrim(cast(rtrim(fname) || ' ' || rtrim(mname) || ' ' || rtrim(lname) as char(240) ccsid unicode)) from SNT.personall where id = ds.DROPID) end "
			+ " as lastSigner,"
			+ " case when ds.droptime is null then "
			+ "ds.ldate "
			+ "else date(ds.droptime) end "
			+ "as lastDate,"
			+ "  case when ds.droptime is null then "
			+ " ds.ltime "
			+ "else time(ds.droptime) end "
			+ "as lastTime, "
			+ "cast(ds.price as decimal(9,2)) price, "
			+ "ds.sf_type sf_type, "
			+ " case when ds.etdid is null or ds.typeid  = (select id from snt.doctype where rtrim(name) = 'ГУ-2б') "
			+ " then ds.id else ds.etdid end as etdid, "
			+ "ds.numsf, "
			+ "ds.numfpu, "
			+ " case when ds.otc_code is not null then "
			// +" (select rtrim(object_vname) from snt.dic_objects where object_kod = ds.otc_code) "
			+ " ds.otc_code "
			+ " else null end otc_name, "
			+ " case when ds.otc_code is not null then "
			+ " (select rtrim(refer_name) from snt.dic_refer where refer_id = (select refer from snt.dic_objects where object_kod = ds.otc_code)) "
			+ " else null end otc_type, " + " ds.docData as docData "
			+ " from SNT.DocStore as ds"
			+ " where ds.id in (:idList) #sort #dir #2sort #2dir with ur";
	
	static final private String GET_COLOR_PACKAGE = "select color from snt.colorpackage where id_pak = :id_pak";
	
	static private final String GET_INWORK_DOCUMENTS_IDS_SQL_VRK2 = " select distinct ds.id as id ,no, "
			+ " ( select rtrim(cast(name as char(120) ccsid unicode )) from SNT.DocType as dt where ds.typeid = dt.id ) as name,"
			+ " ds.CrDate as createDate,"
			+ "ds.opisanie as content, "
			+ "( select rtrim(vname) from SNT.PRED as prd where ds.pred_creator = prd.id  ) as predname , "
			+ " ds.CrTime as createTime , "
			+ "ds.id_pak as idpak,"
			+ "ds.vagnum vagnum, ds.repdate repdate, "
			+ "case when ds.onbaseid is not null then (select rtrim(name) from snt.dor where id = "
			+ "(select distinct(dorid) from snt.rem_pred where kleim = ds.onbaseid)) end as di, "
			+ "case when ds.onbaseid is not null then (select rtrim(name) from snt.rem_pred "
			+ "where kleim = ds.onbaseid) end as rem_pred, "
			+ " ds.reqdate reqdate, "
			+ " (select rtrim(cast(rtrim(fname) || ' ' || rtrim(mname) || ' ' || rtrim(lname) as char(240) ccsid unicode)) from SNT.personall where id = ds.LPERSID) as lastSigner, "
			+ "ds.ldate as lastDate "
			+ "from SNT.DocStore as ds, SNT.doctypeflow as df  "
			+ " where "
		/*	+ "ds.id in (select dstf.docid from SNT.docstoreflow dstf where dstf.predid in "
			+ filialsqlflow
			+ " and dstf.wrkid=:workerId"
			+ " ) and "*/
			+ "  ds.typeid in (select dtid from SNT.doctypeacc where wrkid = :workerId and (cview = 1 or cedit=1) ) "
			+ " and ds.dropid is null  and ds.signlvl >0 "
			+ " and ds.visible =0 "
			+ " and ds.id in (select docid from snt.marsh where predid in " + filialsqlstore +") "
			+ "#filt #period #pers #marsh with ur";
	
	static private final String GET_ARCHIVE_DOCUMENTS_IDS_SQL_VRK2 = " select distinct ds.id as id ,no, "
			+ " ( select rtrim(cast(name as char(120) ccsid unicode )) from SNT.DocType as dt where ds.typeid = dt.id ) as name,"
			+ " ds.CrDate as createDate,"
			+ "ds.opisanie as content, "
			+ "( select rtrim(vname) from SNT.PRED as prd where ds.pred_creator = prd.id  ) as predname ,"
			+ " ds.CrTime as createTime , "
			+ "ds.id_pak as idpak,"
			+ "ds.vagnum vagnum, ds.repdate repdate, "
			+ "ds.di servicecode, "
			+ "case when ds.onbaseid is not null then (select rtrim(name) from snt.dor where id = "
			+ "(select distinct(dorid) from snt.rem_pred where kleim = ds.onbaseid)) end as di, "
			+ "case when ds.onbaseid is not null then (select rtrim(name) from snt.rem_pred "
			+ "where kleim = ds.onbaseid) end as rem_pred, "
			+ "ds.reqnum reqnum, "
			+ " ds.reqdate reqdate, "
			+ " (select rtrim(cast(rtrim(fname) || ' ' || rtrim(mname) || ' ' || rtrim(lname) as char(240) ccsid unicode)) from SNT.personall where id = ds.LPERSID) as lastSigner, "
			+ "ds.ldate as lastDate "
			+ "from SNT.DocStore as ds, SNT.doctypeflow as df  "
			+ " where ds.typeid = df.dtid"
			+ " and ds.dropid is null  "
			+ " and ds.SignLvl is NULL "
		/*	+ " and ds.pred_creator in "
			+ filialsqlstore*/
			+ " and ds.typeid in (select dtid from SNT.doctypeacc where wrkid = :workerId "
			+ "and (cview = 1 or cedit=1) ) and ds.visible =0 "
			+ "and (ds.groupsgn is null or ds.groupsgn =1) "
			+ " and ds.id in (select docid from snt.marsh where predid in " + filialsqlstore +") "
			+ "#filt #period #pers with ur";
	
	static private final String GET_DECLINED_DOCUMENTS_IDS_SQL_VRK2 = " select distinct ds.id as id ,no, "
			+ " ( select rtrim(cast(name as char(120) ccsid unicode )) from SNT.DocType as dt where ds.typeid = dt.id ) as name,"
			+ " ds.CrDate as createDate,"
			+ "ds.opisanie as content, "
			+ "( select rtrim(vname) from SNT.PRED as prd where ds.pred_creator = prd.id  ) as predname ,"
			+ " ds.CrTime as createTime , "
			+ "ds.id_pak as idpak,"
			+ "ds.vagnum vagnum, ds.repdate repdate, "
			+ "ds.di servicecode, "
			+ "case when ds.onbaseid is not null then (select rtrim(name) from snt.dor where id = "
			+ "(select distinct(dorid) from snt.rem_pred where kleim = ds.onbaseid)) end as di, "
			+ "case when ds.onbaseid is not null then (select rtrim(name) from snt.rem_pred "
			+ "where kleim = ds.onbaseid) end as rem_pred, "
			+ "ds.reqnum reqnum, "
			+ "(select rtrim(cast(rtrim(fname) || ' ' || rtrim(mname) || ' ' || rtrim(lname) as char(240) ccsid unicode)) from SNT.personall where id = ds.DROPID) "
			+ " as lastSigner, "
			+ " ds.reqdate reqdate, "
			+ "date(ds.droptime) as lastDate "
			+ "from SNT.DocStore as ds, SNT.doctypeflow as df  "
			+ "where ds.typeid in (select dtid from SNT.doctypeacc where wrkid = :workerId and (cview = 1 or cedit=1)  )"
			+ " and ds.typeid = df.dtid "
		/*	+ " and ds.pred_creator in "
			+ filialsqlstore*/
			+ " and ds.dropid is not null "
			+ " and ds.id in (select docid from snt.marsh where predid in " + filialsqlstore +") "
			+ " and ds.visible =0 " + "#filt #period #pers with ur";
	
	static private final String GET_ACTIVE_DOCUMENT_IDS_SQL_VRK2 =

			" select distinct ds.id as id, "
					+ "no, "
					+ " ( select rtrim(cast(name as char(120) ccsid unicode )) from SNT.DocType as dt where ds.typeid = dt.id ) as name,"
					+ " ds.CrDate as createDate,"
					+ "ds.opisanie as content, "
					+ "( select rtrim(vname) from SNT.PRED as prd where ds.pred_creator = prd.id  ) as predname ,"
					+ " ds.CrTime as createTime, "
					+ "ds.id_pak as idpak,"
					+ "ds.vagnum vagnum, ds.repdate repdate, "
					+ "case when ds.onbaseid is not null then (select rtrim(name) from snt.dor where id = "
					+ "(select distinct(dorid) from snt.rem_pred where kleim = ds.onbaseid)) end as di, "
					+ "case when ds.onbaseid is not null then (select rtrim(name) from snt.rem_pred "
					+ "where kleim = ds.onbaseid) end as rem_pred, "
					+ "ds.reqnum reqnum, "
					+ "ds.di servicecode, "
					+ " ds.reqdate reqdate, "
					+ " (select rtrim(cast(rtrim(fname) || ' ' || rtrim(mname) || ' ' || rtrim(lname) as char(240) ccsid unicode)) from SNT.personall where id = ds.LPERSID) as lastSigner, "
					+ "ds.ldate as lastDate, "
					+ "cast(ds.price as decimal(9,2)) price, "
					+ "ds.sf_type sf_type, "
					+ "ds.numsf, "
					+ "ds.numfpu, "
					+ " (select code_text from snt.packinfo where id = ds.id fetch first row only) otc_name, "
					+ " (select REFER_TEXT from snt.packinfo where id = ds.id fetch first row only) otc_type "
					+ "from SNT.DocStore as ds, SNT.doctypeflow as df "
					+ " where "
					+ " ds.typeid in (select dtid from SNT.doctypeacc where wrkid = :workerId and (cview = 1 or cedit=1)  )"
					+ " and ds.dropid is null  "
					+ "  and ds.visible =0 "
					+ " and ds.typeid = df.dtid "
					+ " and ds.SignLvl = df.Parent "
					+ "and ds.SignLvl >=0 "
				/*	+ " and ds.predid in " + filialsqlstore */
					+ " and ds.ldate is null "
					+ " and ds.id in (select docid from snt.marsh where predid in " + filialsqlstore +") "
					+ " #filt #period " + "#pers #sort #dir #2sort #2dir with ur";
	
	static private final String GET_INWORK_DOCUMENTS_IDS_SQL_VRK1 = " select distinct ds.id as id ,no, "
			+ " ( select rtrim(cast(name as char(120) ccsid unicode )) from SNT.DocType as dt where ds.typeid = dt.id ) as name,"
			+ " ds.CrDate as createDate,"
			+ "ds.opisanie as content, "
			+ "( select rtrim(vname) from SNT.PRED as prd where ds.pred_creator = prd.id  ) as predname , "
			+ " ds.CrTime as createTime , "
			+ "ds.id_pak as idpak,"
			+ "ds.vagnum vagnum, ds.repdate repdate, "
			+ "case when ds.onbaseid is not null then (select rtrim(name) from snt.dor where id = "
			+ "(select distinct(dorid) from snt.rem_pred where kleim = ds.onbaseid)) end as di, "
			+ "case when ds.onbaseid is not null then (select rtrim(name) from snt.rem_pred "
			+ "where kleim = ds.onbaseid) end as rem_pred, "
			+ " ds.reqdate reqdate, "
			+ " (select rtrim(cast(rtrim(fname) || ' ' || rtrim(mname) || ' ' || rtrim(lname) as char(240) ccsid unicode)) from SNT.personall where id = ds.LPERSID) as lastSigner, "
			+ "ds.ldate as lastDate "
			+ "from SNT.DocStore as ds, SNT.doctypeflow as df  "
			+ " where "
			+ "(ds.id in (select dstf.docid from SNT.docstoreflow dstf where dstf.predid in "
			+ filialsqlflow
			+ " and dstf.wrkid=:workerId)"
			+ " and (ds.id in (select docid from snt.marsh where predid in " + filialsqlstore +") "
			+ " or ds.id not in (select docid from snt.marsh where predid in " + filialsqlstore +"))) "
			+ " and ds.typeid in (select dtid from SNT.doctypeacc where wrkid = :workerId and (cview = 1 or cedit=1) ) "
			+ " and ds.dropid is null  and ds.signlvl >0 "
			+ " and ds.visible =1 "
			+ "#filt #period #pers #marsh with ur";
	
	static private final String GET_ARCHIVE_DOCUMENTS_IDS_SQL_VRK1 = " select distinct ds.id as id ,no, "
			+ " ( select rtrim(cast(name as char(120) ccsid unicode )) from SNT.DocType as dt where ds.typeid = dt.id ) as name,"
			+ " ds.CrDate as createDate,"
			+ "ds.opisanie as content, "
			+ "( select rtrim(vname) from SNT.PRED as prd where ds.pred_creator = prd.id  ) as predname ,"
			+ " ds.CrTime as createTime , "
			+ "ds.id_pak as idpak,"
			+ "ds.vagnum vagnum, ds.repdate repdate, "
			+ "ds.di servicecode, "
			+ "case when ds.onbaseid is not null then (select rtrim(name) from snt.dor where id = "
			+ "(select distinct(dorid) from snt.rem_pred where kleim = ds.onbaseid)) end as di, "
			+ "case when ds.onbaseid is not null then (select rtrim(name) from snt.rem_pred "
			+ "where kleim = ds.onbaseid) end as rem_pred, "
			+ "ds.reqnum reqnum, "
			+ " ds.reqdate reqdate, "
			+ " (select rtrim(cast(rtrim(fname) || ' ' || rtrim(mname) || ' ' || rtrim(lname) as char(240) ccsid unicode)) from SNT.personall where id = ds.LPERSID) as lastSigner, "
			+ "ds.ldate as lastDate "
			+ "from SNT.DocStore as ds, SNT.doctypeflow as df  "
			+ " where ds.typeid = df.dtid"
			+ " and ds.dropid is null  "
			+ " and ds.SignLvl is NULL "
			+ " and (ds.pred_creator in "
			+ filialsqlstore 
			+ " or ds.id in (select docid from snt.marsh where predid in "+ filialsqlstore +"))"
			+ " and ds.typeid in (select dtid from SNT.doctypeacc where wrkid = :workerId "
			+ "and (cview = 1 or cedit=1) ) and ds.visible =1 "
			+ "and (ds.groupsgn is null or ds.groupsgn =1) "
			+ "#filt #period #pers with ur";
	
	static private final String GET_DECLINED_DOCUMENTS_IDS_SQL_VRK1 = " select distinct ds.id as id ,no, "
			+ " ( select rtrim(cast(name as char(120) ccsid unicode )) from SNT.DocType as dt where ds.typeid = dt.id ) as name,"
			+ " ds.CrDate as createDate,"
			+ "ds.opisanie as content, "
			+ "( select rtrim(vname) from SNT.PRED as prd where ds.pred_creator = prd.id  ) as predname ,"
			+ " ds.CrTime as createTime , "
			+ "ds.id_pak as idpak,"
			+ "ds.vagnum vagnum, ds.repdate repdate, "
			+ "ds.di servicecode, "
			+ "case when ds.onbaseid is not null then (select rtrim(name) from snt.dor where id = "
			+ "(select distinct(dorid) from snt.rem_pred where kleim = ds.onbaseid)) end as di, "
			+ "case when ds.onbaseid is not null then (select rtrim(name) from snt.rem_pred "
			+ "where kleim = ds.onbaseid) end as rem_pred, "
			+ "ds.reqnum reqnum, "
			+ "(select rtrim(cast(rtrim(fname) || ' ' || rtrim(mname) || ' ' || rtrim(lname) as char(240) ccsid unicode)) from SNT.personall where id = ds.DROPID) "
			+ " as lastSigner, "
			+ " ds.reqdate reqdate, "
			+ "date(ds.droptime) as lastDate "
			+ "from SNT.DocStore as ds, SNT.doctypeflow as df  "
			+ "where ds.typeid in (select dtid from SNT.doctypeacc where wrkid = :workerId and (cview = 1 or cedit=1)  )"
			+ " and ds.typeid = df.dtid "
			+ " and (ds.pred_creator in "
			+ filialsqlstore
			+ " or ds.id in (select docid from snt.marsh where predid in "+ filialsqlstore +"))"
			+ " and ds.dropid is not null "
			+ " and ds.visible =1 " + "#filt #period #pers with ur";
	
	static private final String GET_ACTIVE_DOCUMENT_IDS_SQL_VRK1 =

			" select distinct ds.id as id, "
					+ "no, "
					+ " ( select rtrim(cast(name as char(120) ccsid unicode )) from SNT.DocType as dt where ds.typeid = dt.id ) as name,"
					+ " ds.CrDate as createDate,"
					+ "ds.opisanie as content, "
					+ "( select rtrim(vname) from SNT.PRED as prd where ds.pred_creator = prd.id  ) as predname ,"
					+ " ds.CrTime as createTime, "
					+ "ds.id_pak as idpak,"
					+ "ds.vagnum vagnum, ds.repdate repdate, "
					+ "case when ds.onbaseid is not null then (select rtrim(name) from snt.dor where id = "
					+ "(select distinct(dorid) from snt.rem_pred where kleim = ds.onbaseid)) end as di, "
					+ "case when ds.onbaseid is not null then (select rtrim(name) from snt.rem_pred "
					+ "where kleim = ds.onbaseid) end as rem_pred, "
					+ "ds.reqnum reqnum, "
					+ "ds.di servicecode, "
					+ " ds.reqdate reqdate, "
					+ " (select rtrim(cast(rtrim(fname) || ' ' || rtrim(mname) || ' ' || rtrim(lname) as char(240) ccsid unicode)) from SNT.personall where id = ds.LPERSID) as lastSigner, "
					+ "ds.ldate as lastDate, "
					+ "cast(ds.price as decimal(9,2)) price, "
					+ "ds.sf_type sf_type, "
					+ "ds.numsf, "
					+ "ds.numfpu, "
					+ " (select code_text from snt.packinfo where id = ds.id fetch first row only) otc_name, "
					+ " (select REFER_TEXT from snt.packinfo where id = ds.id fetch first row only) otc_type "
					+ "from SNT.DocStore as ds, SNT.doctypeflow as df "
					+ " where "
					+ " ds.typeid in (select dtid from SNT.doctypeacc where wrkid = :workerId and (cview = 1 or cedit=1)  )"
					+ " and ds.dropid is null  "
					+ "  and ds.visible =1 "
					+ " and ds.typeid = df.dtid "
					+ " and ds.SignLvl = df.Parent "
					+ "and ds.SignLvl >=0 "
					+ " and (ds.predid in " + filialsqlstore 
					+ " or ds.id in (select docid from snt.marsh where predid in "+ filialsqlstore +")) "
					+ " and ds.ldate is null "
					+ " #filt #period " + "#pers #sort #dir #2sort #2dir with ur";
	
	private String[] parseArray(String reqnum) {

		String[] rqn = null;
		if (reqnum != null) {
			StringTokenizer st = new StringTokenizer(reqnum, "|");
			ArrayList tmp = new ArrayList();
			while (st.hasMoreTokens()) {
				tmp.add(st.nextToken());
			}
			rqn = new String[tmp.size()];
			for (int i = 0; i < rqn.length; i++) {
				rqn[i] = tmp.get(i).toString();

			}
		}
		return rqn;
	}

	@SuppressWarnings("unchecked")
	private List<Long> generateIDs(String sql, ETDDocumentFilter pm,
			boolean dropped) {
		if (sql.length() == 0)
			return new ArrayList();

		SqlParameterSource params = new BeanPropertySqlParameterSource(pm);
		if (logger.isDebugEnabled())
			logger.debug("Getting documents ids with params : " + pm.toString());

		// System.out.println(pm.getDateAfter());
		// System.out.println(pm.getDateBefore());
		// System.out.println("pm.getShift() "+pm.getShift());
		if (pm.getShift() != null && !pm.getShift().equals("null")) {
			// System.out.println(pm.getShift());
			String[] shift = parseArray(pm.getShift().toString());
			pm.getShift().split("|");
			String filt = "";

//			 for (int i = 0; i < shift.length; i++)
//			
//			 {
//			
//			 System.out.println(shift[i]);
//			 }

			if (!shift[0].equals("---") && !shift[0].equals("Все")) {
				// System.out.println(shift[0]);
				filt = filt
						+ " and (ds.typeid in (select id from snt.doctype where groupid = (select id from snt.docgroups where name='"
						+ shift[0]
						+ "')) or ds.typeid = (select id from snt.doctype where name like '%Счет-фактура%'))";

			}

			if (!shift[1].equals("---")) {

				if (!dropped)
					filt = filt + " and ds.ldate >= '" + shift[1] + "'";

				else if (dropped)
					filt = filt + " and date(ds.droptime) >= '" + shift[1]
							+ "'";

			}

			if (!shift[2].equals("---")) {

				if (!dropped)
					filt = filt + " and ds.ldate <= '" + shift[2] + "'";
				else if (dropped)
					filt = filt + " and date(ds.droptime) <= '" + shift[2]
							+ "'";

			}

			if (!shift[3].equals("---")) {

				filt = filt + " and ds.opisanie like '%" + shift[3] + "%'";

			}

			if (!shift[4].equals("---")) {

				filt = filt + " and no like '%" + shift[4] + "%'";
				// System.out.println(filt);
			}

			if (!shift[5].equals("---")) {

				filt = filt + " and ds.id_pak like '%" + shift[5] + "%'";
			}

			if (!shift[6].equals("---")) {

				filt = filt + " and ds.vagnum like '%" + shift[6] + "%'";
			}

			if (!shift[7].equals("---")) {
				filt = filt + " and ds.reqdate >= '" + shift[7] + "'";

			}

			if (!shift[13].equals("---")) {
				filt = filt + " and ds.reqdate <= '" + shift[13] + "'";

			}

			if (!shift[8].equals("---") && !shift[8].contains("-1")) {

				filt = filt
						+ " and (select dorid from snt.rem_pred where kleim = ds.onbaseid) in ("
						+ shift[8] + ") ";

			}
			if (!shift[9].equals("---") && !shift[9].contains("-1")) {
				filt = filt + " and ds.onbaseid in (" + shift[9] + ") ";

			}

			if (!shift[10].equals("---")) {
				filt = filt + " and ds.typeid in (" + shift[10] + ") ";

			}

			if (!shift[11].equals("---")) {
				if (!dropped) {
					filt = filt + " and ds.lpersid in (" + shift[11] + ") ";
				} else {
					filt = filt + " and ds.dropid in (" + shift[11] + ") ";
				}
			}

			if (!shift[12].equals("---")) {

				if (shift[12].equals("0"))
					filt = filt
							+ " and (ds.reqnum is null or ds.reqnum = '') and (ds.torpackid is null or ds.torpackid='') and ds.typeid in (select id from snt.doctype where name like 'Пакет документов%') ";

				else if (shift[12].equals("1"))
					filt = filt
							+ " and ds.reqnum is not null and ds.reqnum!='' and (ds.torpackid is null or ds.torpackid='') and ds.typeid in (select id from snt.doctype where name like 'Пакет документов%') ";
				else if (shift[12].equals("2"))
					filt = filt
							+ " and ds.torpackid is not null and ds.torpackid!='' and ds.typeid in (select id from snt.doctype where name like 'Пакет документов%') ";
			}
			if (!shift[14].equals("---")) {
				String[] servicecode = shift[14].split(",");
				StringBuilder stb = new StringBuilder();
				for(int i = 0; i < servicecode.length; i++) {
					if(i < servicecode.length-1) {
						stb = stb.append("'" + servicecode[i] + "',");  
					} else {
						stb = stb.append("'" + servicecode[i]  + "'");  
					}
				}
				filt = filt + " and ds.di in (" + stb.toString() + ") ";
			}
			if (!shift[15].equals("---")) {
				filt = filt + " and ds.stat in (" + shift[15] + ")";
			}

			if (!shift[16].equals("---")) {
				filt = filt + " and ds.sf_type in (" + shift[16] + ")";
			}
			if (!shift[17].equals("---")) {
				// filt = filt + " and ds.otc_code = "+shift[17]+"";
				filt = filt
						+ " and ds.id in (select distinct(id) from snt.packinfo where code_text like '%"
						+ shift[17] + "%') ";
			}
			if (!shift[18].equals("---")) {
				// filt = filt +
				// " and ds.otc_code in (select object_kod from snt.dic_objects where refer in ("+shift[18]+"))";
				filt = filt
						+ "and ds.id in (select distinct(id) from snt.packinfo where object_kod in (select object_kod from snt.dic_objects where refer in ("
						+ shift[18] + ")))";
			}
			if (shift.length > 20 && !shift[20].equals("---")) {
				filt = filt + " and ds.price like '%" + shift[20] + "%'";
			}
			if (!shift[19].equals("---")) {
				filt = filt
						+ "and (ds.etdid = " + shift[19] + " or ds.id = " + shift[19] + ")";

			}
			sql = sql.replace("#filt", filt);
		} else {

			sql = sql.replace("#filt", "  ");
		}

		if (pm.getPersId() != null)
			/*
			 * sql = sql.replace("#pers",
			 * " and (((ds.persId is NULL or ds.persId=-1) and ((select rtrim(cast(name as char(120) ccsid unicode )) from SNT.DocType as dt where ds.typeid = dt.id ) not like '%Документ%')) OR (((select rtrim(cast(name as char(120) ccsid unicode )) from SNT.DocType as dt where ds.typeid = dt.id ) like '%Документ%') and (((ds.persId is NOT NULL and ds.persId<>-1 and ds.persId<>-2) and ds.persId =:persId and pred_creator<>:predId) OR ((ds.persId is NOT NULL and ds.persId=-2) and :persId in (select pid from snt.perswrk where predid =:predId and wrkid =:workerId)  and pred_creator<>:predId) OR (ds.pers_creator=:persId and pred_creator =:predId))))"
			 * );
			 * 
			 * else
			 */

			sql = sql.replace("#pers", "  ");

		// if(pm.getSort().equals("short") || pm.getSort().equals("ds.No")) sql
		// = sql.replace("#dir", "").replace("#sort", "");

		if (pm.getSort().equals("ds.No")) {
			// System.out.println("numberdocs");
			sql = sql.replace("#sort", "ORDER BY content ").replace("#dir",
					pm.getDir());
		}
		
		else {
			sql = sql.replace("#dir", pm.getDir()).replace("#sort",
					"ORDER BY " + pm.getSort());
			sql = sql.replace("#select", ", " + pm.getSort());
		}
		if (pm.getSort2() != null) {
			sql = sql.replace("#2dir", pm.getDir()).replace("#2sort",
					", " + pm.getSort2());
		} else {
			sql = sql.replace("#2dir", "").replace("#2sort", "");
		}

		if (pm.getDateBefore() != null && pm.getDateBefore() != null) {

			if (!pm.getDateEqual().equals("null")) {
				List<String> datesList = new ArrayList<String>();

				try {
					String[] dates = pm.getDateEqual().split(",");
					int dateiter = 0;
					while (dateiter < dates.length) {
						datesList.add(dates[dateiter]);
						dateiter++;
					}
					pm.setDatesList(datesList);

					sql = sql.replace("#period",
							"and ds.CrDate in (:datesList) ");

				} catch (Exception e) {
					e.printStackTrace();
				}

				sql = sql.replace("#period", "");
			} else {

				sql = sql.replace("#period",
						"and ds.CrDate between :dateAfter and :dateBefore ");
			}

		} else {
			sql = sql.replace("#period", " ");
		}

		// Только заданные ИД, промежуток в 3 дня не учитываем
		if (pm.getIdList() != null && pm.getIdList().size() > 0) {

		} else {

			try {
				List idss = getNamedParameterJdbcTemplate().queryForList(sql,
						params);
				List<Long> ids = new ArrayList<Long>();
				for (int i = 0; i < idss.size(); i++) {
					// System.out.println( idss.get(i).getClass());
					ids.add(((Integer) ((Map) idss.get(i)).get("ID"))
							.longValue());
				}
				logger.debug("IDS SIZE 2" + ids.size());
				logger.debug("IDS SIZE 1" + ids.size());
				// System.out.println("IDS SIZE 1"+ids.size());

				if (logger.isDebugEnabled())
					logger.debug("IDs count : " + ids.size());
				return ids;
			} catch (Exception e) {
				e.printStackTrace(System.out);
			}

		}
		return null;

	}

	private List<ETDDocument> generateContent(final ETDDocumentFilter pm,
			final List<Long> ids) {
		String sql;
		if(pm.getFuncRole() == 7 || pm.getFuncRole() == 18 || pm.getFuncRole() == 8) {
			sql = GET_DOCUMENTS_CONTENT_SQL_PPS_2;
		} else if(pm.getFuncRole() == 19) {
			sql = GET_DOCUMENTS_CONTENT_SQL_PPS_3;
		} else{
			sql = GET_DOCUMENTS_CONTENT_SQL;
		}
		// Сортировка (обязательный параметр)
		// if(pm.getSort().equals("short") || pm.getSort().equals("ds.No")) sql
		// = sql.replace("#dir", "").replace("#sort", "");
		if (pm.getSort().equals("ds.No")) {
			// System.out.println("numberdocs");
			sql = sql.replace("#sort", "ORDER BY content ").replace("#dir",
					pm.getDir());
		}else if(pm.getSort().equals("vagnum")){
			sql = sql.replace("#dir", pm.getDir()).replace("#sort",
					"ORDER BY " + "LENGTH(vagnum), " +pm.getSort());
		}/*else if(pm.getSort().equals("price")){
			sql = sql.replace("#dir", pm.getDir()).replace("#sort",
					"ORDER BY " + "LENGTH(price), " +pm.getSort());
		}*/
		else
			sql = sql.replace("#dir", pm.getDir()).replace("#sort",
					"ORDER BY " + pm.getSort());

		if (pm.getSort2() != null) {

			sql = sql.replace("#2dir", pm.getDir()).replace("#2sort",
					", " + pm.getSort2());
		} else {
			sql = sql.replace("#2dir", "").replace("#2sort", "");
		}

		int start = pm.getStart();
		int end = pm.getLimit();
		int size;

		// Здесь никакой выборки в 20 документов, возвращаем полный список из
		// всех документов
		if (ids != null && ((size = ids.size()) > start)) {
			int fromIndex = start;
			int toIndex;
			if (end < size)
				toIndex = end;
			else
				toIndex = size;
			//List<Long> idList = ids.subList(fromIndex, toIndex);
			List<Long> idList = null;
			if(pm.isStatus() || pm.getSort().equals("servicecode")) {
				idList = ids;
			} else {
				idList = ids.subList(fromIndex, toIndex);
			}

			pm.setIdList(idList);
//			 System.out.println("idList "+idList);
			// System.out.println("sql "+sql);
			// List<Long> idList = ids.subList(fromIndex, toIndex);
			logger.debug("Loding documents data for ids : " + idList.size());
			// pm.setIdList(ids);

			SqlParameterSource params = new BeanPropertySqlParameterSource(pm);
			@SuppressWarnings("unchecked")
			List<ETDDocument> result = getNamedParameterJdbcTemplate().query(
					sql, params, new ParameterizedRowMapper<ETDDocument>() {
						public ETDDocument mapRow(ResultSet rs, int n)
								throws SQLException {
							ETDDocument obj = new ETDDocument();
							obj.setId(rs.getLong("id"));
							obj.setName(rs.getString("name"));
							obj.setNumber(rs.getString("content") != null ? rs
									.getString("content") : "");
							obj.setCreateDate(rs.getDate("createDate"));
							obj.setCreateTime(rs.getTime("createTime"));
							obj.setCreator(rs.getString("predname"));
							obj.setSfSign(rs.getInt("sign"));
							obj.setCDel(1);
							obj.setCCreateSourse(0);
							obj.setLastSigner(rs.getString("lastSigner"));
							obj.setLastDate(rs.getDate("lastDate"));
							obj.setLastTime(rs.getTime("lastTime"));
							obj.setData(rs.getString("docData"));
							obj.setIdPak(rs.getString("idpak"));
							obj.setDognum(rs.getString("no"));
							obj.setVagnum(rs.getString("vagnum"));
							obj.setReqdate(rs.getString("repdate"));
							obj.setStat(rs.getInt("stat"));
							obj.setRem_pred(rs.getString("rem_pred"));
							obj.setDi(rs.getString("di"));
							obj.setDroptxt(rs.getString("droptxt"));
							obj.setNumberSf(rs.getString("numsf"));
							obj.setNumberFpu(rs.getString("numfpu"));
							if (rs.getString("name").equals("Пакет документов")) {
//System.out.println("reqnum: "+rs.getString("reqnum"));
//System.out.println("torpackid: "+rs.getString("torpackid"));
								
								
								//TODO для проверки цен
								Map<String, Object> paramMap = new HashMap<String, Object>();
								paramMap.put("id_pak",rs.getString("idpak"));
								int colorPackage = 0;
								try{
									colorPackage = getNamedParameterJdbcTemplate().queryForInt(GET_COLOR_PACKAGE, paramMap);
								}catch(Exception e){
//									log.error(TypeConverter.exceptionToString(e));
								}
								obj.setColor(colorPackage);
								//TODO
								
								
								if (rs.getString("reqnum") != null	&& rs.getString("reqnum").equals("Отказ подписи ТОРГ-12")
										|| rs.getString("reqnum") != null&& rs.getString("reqnum").equals("Подписан ТОРГ-12")
										|| rs.getString("reqnum") != null&& rs.getString("reqnum").equals("Требует подписи ТОРГ-12")) {
									obj.setReqnum(rs.getString("reqnum"));
								}

								else if (rs.getString("torpackid") != null&& !"".equals(rs.getString("torpackid"))){
									obj.setReqnum("Дополнительный");
								}
								else if (rs.getString("reqnum") != null&& !"".equals(rs.getString("reqnum"))){
									obj.setReqnum("Вторичный");
								}
								else {
									obj.setReqnum("Первичный");
									}
//								System.out.println(obj.getReqnum());
							}
							else if (rs.getString("name").equals("Пакет документов ЦСС")) {
								if (rs.getString("reqnum") != null&& !"".equals(rs.getString("reqnum"))) {
									 obj.setReqnum("Вторичный"); 
								} else {
									obj.setReqnum("Первичный");
								}
							}

							else if (rs.getString("name").contains(
									"чет-фактура")) {
								if (rs.getInt("sign") == 1) {
									obj.setReqnum("Подлежит оформлению");
								} else {
									obj.setReqnum("Не подлежит оформлению");
								}

								if (rs.getInt("sf_type") == 0) {
									obj.setSftype("И");
								}
								if (rs.getInt("sf_type") == 1) {
									obj.setSftype("К");
								}
								if (rs.getInt("sf_type") == 2) {
									obj.setSftype("П");
								}
							}

							else if (rs.getString("name").equals("Заявка ППС")
									|| rs.getString("name").equals(
											"Пакет документов РТК")
									|| rs.getString("name").contains(
											"ФПУ-26 ППС")
									|| rs.getString("name").equals("Реестр ЦС")) {

								if (rs.getString("reqnum") != null
										&& !"".equals(rs.getString("reqnum")))
									obj.setReqnum(rs.getString("reqnum"));
								else
									obj.setReqnum("");
							}
							
							else if (rs.getString("name").equals("ТОРГ-12")&&rs.getString("reqnum") != null){
								obj.setReqnum(rs.getString("reqnum"));
							}
							else if (rs.getString("name").equals("Претензия")) {
								if (rs.getString("reqnum") != null && !"".equals(rs.getString("reqnum"))) {
									 obj.setReqnum("Вторичный"); 
								} else {
									obj.setReqnum("Первичный");
								}
							}
							else
								obj.setReqnum("");

							if (rs.getString("name").equals("ВУ-19")
									|| rs.getString("name").equals("ВУ-20")
									|| rs.getString("name").equals("ВУ-20а")
									|| rs.getString("name").equals("Заявка ППС")) {
								SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
								if(rs.getDate("reqdate")!= null) {
									obj.setReqdate(sdf.format(rs.getDate("reqdate")));
								} else {
			                        obj.setReqdate("");
								}
								if(rs.getString("reqnum")!= null) {
									obj.setReqnum(sdf.format(rs.getString("reqnum")));
								} else {
			                        obj.setReqnum("");
								}
								
     
//								obj.setDi("Куйбышевская");
//								obj.setOtcname("ВЧДЭ-13 ДЕМА КБШ");

							}

							if (rs.getString("servicecode") != null
									&& !"".equals(rs.getString("servicecode"))) {
								obj.setServicetype(ServiceTypes.servicetypes
										.get(rs.getString("servicecode")));
							} else {
								obj.setServicetype("");
							}
							if (rs.getString("price") != null)
								obj.setPrice(rs.getString("price"));
							if (rs.getString("otc_name") != null)
								obj.setOtcname(rs.getString("otc_name"));
							if (rs.getString("otc_type") != null)
								obj.setOtctype(rs.getString("otc_type"));
							obj.setEtdid(rs.getLong("etdid"));
							return obj;
						}
					});
			size = result.size();
			if(pm.isStatus()){
				if(pm.getDir().equals("ASC")) {
					Collections.sort(result, new Comparator<ETDDocument>() {
						@Override
						public int compare(final ETDDocument object1, final ETDDocument object2) {
							String reqnum1 = object1.getReqnum() == null ? "" : object1.getReqnum();
							String reqnum2 = object2.getReqnum() == null ? "" : object2.getReqnum();
							return reqnum1.compareTo(reqnum2);
						}
					});
				} else {
					Collections.sort(result, new Comparator<ETDDocument>() {
						@Override
						public int compare(final ETDDocument object1, final ETDDocument object2) {
							String reqnum1 = object1.getReqnum() == null ? "" : object1.getReqnum();
							String reqnum2 = object2.getReqnum() == null ? "" : object2.getReqnum();
							return reqnum2.compareTo(reqnum1);
						}
					});
				}
				return result.subList(fromIndex, toIndex);
			}
			
			if(pm.getSort().equals("servicecode")){
				if(pm.getDir().equals("ASC")) {
					Collections.sort(result, new Comparator<ETDDocument>() {
						@Override
						public int compare(final ETDDocument object1, final ETDDocument object2) {
							String servicetype1 = object1.getServicetype() == null ? "" : object1.getServicetype() ;
							String servicetype2 = object2.getServicetype() == null ? "" : object2.getServicetype();
							return servicetype1.compareTo(servicetype2);
						}
					});
				} else {
					Collections.sort(result, new Comparator<ETDDocument>() {
						@Override
						public int compare(final ETDDocument object1, final ETDDocument object2) {
							String servicetype1 = object1.getServicetype() == null ? "" : object1.getServicetype() ;
							String servicetype2 = object2.getServicetype() == null ? "" : object2.getServicetype();
							return servicetype2.compareTo(servicetype1);
						}
					});
				}
				return result.subList(fromIndex, toIndex);
			}
			return result;
		} else {
			return new ArrayList<ETDDocument>();
		}
	}

	public List<Long> getActiveDocumentIds(final ETDDocumentFilter pm) {
		String sql;
		String sql2;

		int fr = pm.getFuncRole();

		// if (pm.isSystemWorker()==true)
		if (fr == 2 || fr == 3 || fr == 6)
			sql = GET_ACTIVE_DOCUMENT_IDS_SQL_NEW_ROLE2;
		else if (fr == 7) {
			sql = GET_ACTIVE_DOCUMENT_IDS_SQL_PPS_ROLE;
		}
		else if (fr == 9 || fr == 15) {
			sql = GET_ACTIVE_DOCUMENT_IDS_SQL_RTK;
		} else if (fr == 10) {
			sql = GET_ACTIVE_DOCUMENT_IDS_SQL_SOGL_MATLIC;
		} else if (fr == 5) {
			sql = GET_ACTIVE_DOCUMENT_IDS_SQL_MRM;
		} else if (fr == 11) {
			sql = GET_ACTIVE_DOCUMENT_IDS_SQL_MRM_ROLE2;
		} else if (fr == 12) {
			sql = GET_ACTIVE_DOCUMENT_IDS_SQL_MRM_ROLE3;
		} else if (fr == 16) {
			sql = GET_ACTIVE_DOCUMENT_IDS_SQL_RZDS_ROLE2;
		}else if(fr == 18 || fr == 8) {
			sql = GET_ACTIVE_DOCUMENT_IDS_SQL_PPS_ROLE2;
		} else if(fr == 19) {
			sql = GET_ACTIVE_DOCUMENT_IDS_SQL_PPS_ROLE3;
		}
		else if (fr ==20){
			sql = GET_ACTIVE_DOCUMENT_IDS_VRKSIGN_SQL;
		} else if(fr == 21) {
			sql = GET_ACTIVE_DOCUMENT_IDS_SQL_VRK2;
		} else if(fr == 14) {
			sql = GET_ACTIVE_DOCUMENT_IDS_SQL_VRK1;
		}
		else {
			sql = GET_ACTIVE_DOCUMENT_IDS_SQL_NEW;
		}
		return generateIDs(sql, pm, false);

	}

	public List<Long> getArchiveDocumentIds(final ETDDocumentFilter pm) {
		String sql;
		String sql2;
		int fr = pm.getFuncRole();
		if (fr == 2 || fr == 3 || fr == 6)
			// if (pm.isSystemWorker()==true)
			sql = GET_ARCHIVE_DOCUMENTS_IDS_SQL_ROLE2;

		else if (fr == 5)
			sql = GET_ARCHIVE_DOCUMENTS_IDS_SQL_MRM.replaceAll("#marsh",
					GET_MARSH_ARCHIVE_DOCUMENTS_SQL);

		else if (fr == 7 || fr == 8) {
			sql = GET_ARCHIVE_DOCUMENTS_IDS_SQL_PPS;
		} else if (fr == 11) {
			sql = GET_ARCHIVE_DOCUMENTS_IDS_SQL_MRM_ROLE2;
		} else if (fr == 12) {
			sql = GET_ARCHIVE_DOCUMENTS_IDS_SQL_MRM_ROLE3;
		} else if (fr == 13) {
			sql = GET_ARCHIVE_DOCUMENTS_IDS_SQL_ROLEARCHIVE;
		}

		else if (fr == 15) {
			sql = GET_ARCHIVE_DOCUMENTS_IDS_SQL_RZDS_ROLE1;
		} else if (fr == 14) {
//			sql = GET_ARCHIVE_DOCUMENTS_IDS_SQL_MRM_ROLE3;
			sql = GET_ARCHIVE_DOCUMENTS_IDS_SQL_VRK1;
		}else if(fr == 19) {
			sql = GET_ARHIVE_DOCUMENT_IDS_SQL_PPS_ROLE3;
		}else if (fr ==20){
			sql = GET_ARCHIVE_DOCUMENT_IDS_VRKSIGN_SQL;
		}else if (fr ==21){
			sql = GET_ARCHIVE_DOCUMENTS_IDS_SQL_VRK2;
		} else
			sql = GET_ARCHIVE_DOCUMENTS_IDS_SQL_NEW.replaceAll("#marsh",
					"#sort #dir #2sort #2dir");
		return generateIDs(sql, pm, false);

	}

	public List<Long> getDeclinedDocumentIds(final ETDDocumentFilter pm) {

		String sql;
		String sql2;
		int fr = pm.getFuncRole();
		// if (pm.isSystemWorker()==true)
		if (fr == 2 || fr == 3 || fr == 6)
			sql = GET_DECLINED_DOCUMENTS_IDS_SQL_ROLE2;

		else if (fr == 5)
			sql = GET_DECLINED_DOCUMENTS_IDS_SQL_MRM.replaceAll("#marsh",
					GET_MARSH_DECLINED_DOCUMENTS_SQL);

		else if (fr == 7) {
			sql = GET_DECLINED_DOCUMENTS_IDS_SQL_PPS;
		} /* else if (fr == 8) {
		sql = GET_DECLINED_DOCUMENTS_IDS_SQL_PPS_ROLE2;
	}*/ else if (fr == 9) {
			sql = GET_DECLINED_DOCUMENTS_IDS_SQL_RTK.replaceAll("#marsh", "#sort #dir #2sort #2dir");
		} else if (fr == 11) {
			sql = GET_DECLINED_DOCUMENTS_IDS_SQL_MRM_ROLE2;
		} else if (fr == 12) {
			sql = GET_DECLINED_DOCUMENTS_IDS_SQL_MRM_ROLE3;
		} else if (fr == 15) {
			sql = GET_DECLINED_DOCUMENTS_IDS_SQL_RZDS_ROLE1;
		} else if (fr == 14) {
//			sql = GET_DECLINED_DOCUMENTS_IDS_SQL_MRM_ROLE3;
			sql = GET_DECLINED_DOCUMENTS_IDS_SQL_VRK1;
		}else if (fr ==20){
			sql = GET_DECLINED_DOCUMENT_IDS_VRKSIGN_SQL;
		}else if (fr ==21){
			sql = GET_DECLINED_DOCUMENTS_IDS_SQL_VRK2;
		} else
			sql = GET_DECLINED_DOCUMENTS_IDS_SQL_NEW.replaceAll("#marsh",
					"#sort #dir #2sort #2dir");

		return generateIDs(sql, pm, true);

	}

	public List<Long> getDroppedDocumentIds(final ETDDocumentFilter pm) {

		String sql;
		String sql2;
		int fr = pm.getFuncRole();

		// if (pm.isSystemWorker()==true)
		if (fr == 2 || fr == 3 || fr == 6)
			sql = GET_INWORK_DOCUMENTS_IDS_SQL_NEW_ROLE2;

		else if (fr == 5)
			sql = GET_INWORK_DOCUMENTS_IDS_SQL_MRM.replaceAll("#marsh",
					GET_MARSH_INWORK_DOCUMENTS_SQL);

		else if (fr == 11)
			sql = GET_INWORK_DOCUMENTS_IDS_SQL_MRM_ROLE2.replaceAll("#marsh",
					GET_MARSH_INWORK_DOCUMENTS_SQL);
		else if (fr == 18) {
			sql = GET_INWORK_DOCUMENTS_IDS_SQL_PPS;
		}
		else if (fr == 7) {
			sql = GET_INWORK_DOCUMENTS_IDS_SQL_PPS_ROLE;
		} else if(fr == 8) {
			sql = GET_INWORK_DOCUMENT_IDS_SQL_PPS_ROLE4;
		}else if (fr == 9 || fr == 15) {
			sql = GET_INWORK_DOCUMENTS_IDS_SQL_RTK.replaceAll("#marsh", "#sort #dir #2sort #2dir");	
		} else if(fr == 19) {
			sql = GET_INWORK_DOCUMENT_IDS_SQL_PPS_ROLE3;
		} else if (fr ==21){
			sql = GET_INWORK_DOCUMENTS_IDS_SQL_VRK2.replaceAll("#marsh", "#sort #dir #2sort #2dir");	
		} else if(fr == 14) {
			sql = GET_INWORK_DOCUMENTS_IDS_SQL_VRK1.replaceAll("#marsh", "#sort #dir #2sort #2dir");	
		} else
			sql = GET_INWORK_DOCUMENTS_IDS_SQL_NEW.replaceAll("#marsh",
					"#sort #dir #2sort #2dir");
		return generateIDs(sql, pm, false);

	}

	public List<ETDDocument> getMessageDocuments(final ETDDocumentFilter pm,
			final List<Long> ids) {
		return generateContent(pm, ids);
	}

	public List<ETDDocument> getActiveDocuments(final ETDDocumentFilter pm,
			final List<Long> ids) {
		return generateContent(pm, ids);
	}

	public List<ETDDocument> getRoughDocuments(final ETDDocumentFilter pm,
			final List<Long> ids) {
		return generateContent(pm, ids);
	}

	public List<ETDDocument> getFinDocuments(final ETDDocumentFilter pm,
			final List<Long> ids) {
		return generateContent(pm, ids);
	}

	public List<ETDDocument> getArchiveDocuments(final ETDDocumentFilter pm,
			final List<Long> ids) {
		return generateContent(pm, ids);
	}

	public List<ETDDocument> getDroppedDocuments(final ETDDocumentFilter pm,
			final List<Long> ids) {
		return generateContent(pm, ids);
	}

	public List<ETDDocument> getInworkDocuments(final ETDDocumentFilter pm,
			final List<Long> ids) {
		return generateContent(pm, ids);
	}

}

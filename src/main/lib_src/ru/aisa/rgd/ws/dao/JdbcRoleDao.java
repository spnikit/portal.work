package ru.aisa.rgd.ws.dao;

import java.util.HashMap;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.jdbc.core.namedparam.NamedParameterJdbcDaoSupport;

import ru.aisa.rgd.ws.exeption.InternalException;
//import sheff.rjd.dbobjects.DocTypeAccRecord;

public class JdbcRoleDao extends NamedParameterJdbcDaoSupport implements
		RoleDao
	{
	public static final String lastBulidDate = "09.08.2013 17:08:58";
	public static String				ROLE_NAME_PORTAL	= "ЭДО СПС";
	
	public JdbcRoleDao(DataSource ds)
		{
		super();
		setDataSource(ds);
		}
	
	private static String	SQL_QEURY_GET_WORK_ID_BY_NAME	= "select wrkn.ID from snt.WRKNAME as wrkn where wrkn.NAME =:name  with ur";
	
	public int getIdByName(String name) throws Exception
		{
		HashMap<String, Object> param = new HashMap<String, Object>(1);
		param.put("name", name);
		System.out.println(name);
		return getNamedParameterJdbcTemplate().queryForInt(
				SQL_QEURY_GET_WORK_ID_BY_NAME, param);
		}
	
	private static String	SQL_QEURY_GET_WORK_NAME_BY_ID	= "select rtrim(wrkn.name) as name from snt.WRKNAME as wrkn where wrkn.id =:wrkId  with ur";
	
	public String getNameById(int wrkId) throws Exception
		{
		HashMap<String, Object> param = new HashMap<String, Object>(1);
		param.put("wrkId", wrkId);
		String roleName = (String) getNamedParameterJdbcTemplate().queryForObject(
				SQL_QEURY_GET_WORK_NAME_BY_ID, param, String.class);
		if (roleName == null)
			{
			throw new InternalException("Роли с id = " + wrkId + " нет");
			}
		return roleName;
		}
	
	private static String	SQL_QEURY_GET_RIGHTS_BY_WKRID_TYPEID	= "SELECT dta.DTID 		AS dtid, "
																																	+ " dta.WRKID  		AS wrkid, "
																																	+ " dta.CVIEW  		AS cview, "
																																	+ " dta.cedit  		AS cedit, "
																																	+ " dta.CNEW   		AS cnew, "
																																	+ " dta.CDEL   		AS cdel, "
																																	+ " dta.CNEWDATA 	AS cnewdata"
																																	+ " FROM  snt.DOCTYPEACC AS dta "
																																	+ " WHERE   dta.WRKID=:wrkId AND dta.DTID=:docTypeId "
																																	+ " WITH  ur";
	
//	public DocTypeAccRecord getRights(int wrkId, int docTypeId) throws Exception
//		{
//		HashMap<String, Object> param = new HashMap<String, Object>(1);
//		param.put("wrkId", wrkId);
//		param.put("docTypeId", docTypeId);
//		
//		@SuppressWarnings("unchecked")
//		List<DocTypeAccRecord> list = (List<DocTypeAccRecord>) getNamedParameterJdbcTemplate()
//				.query(SQL_QEURY_GET_RIGHTS_BY_WKRID_TYPEID, param,
//						new DocTypeAccRecord.ImplRowMapper());
//		if (list.isEmpty())
//			return new DocTypeAccRecord(wrkId, docTypeId);
//		if (list.size() == 1)
//			return list.get(0);
//		else
//			throw new InternalException(
//					"Множество записей в letd.doctypeacc для wrkid = " + wrkId
//							+ " dtid = " + docTypeId);
//		}
	}

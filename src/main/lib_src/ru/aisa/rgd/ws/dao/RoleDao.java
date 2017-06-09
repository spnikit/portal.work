package ru.aisa.rgd.ws.dao;

//import sheff.rjd.dbobjects.DocTypeAccRecord;

public interface RoleDao
	{
	public static final String	lastBulidDate			= "20.05.2013 19:25:53";
	public static final String	ROLE_NAME_ASETD		= "АС ЭТД";
	public static final String	ROLE_NAME_PORTAL	= "ЭДО СПС";
	public static final String	ROLE_NAME_ASOUP		= "АСОУП";
	
	int getIdByName(String name) throws Exception;
	
	String getNameById(int name) throws Exception;
	
//	DocTypeAccRecord getRights(int wrkId, int docTypeId) throws Exception;
	}

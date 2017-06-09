package ru.aisa.rgd.etd.dao;

import java.io.UnsupportedEncodingException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
 
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import javax.sql.DataSource;

 

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.log4j.Logger;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcDaoSupport;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.ParameterizedRowMapper;

import ru.aisa.rgd.etd.objects.ETDDocument;
import ru.aisa.rgd.etd.objects.ETDDocumentFilter;
import ru.aisa.rgd.etd.objects.ETDSFNotice;


public class ETDSFNoticeDao extends NamedParameterJdbcDaoSupport {
	
	private static Logger	log	= Logger.getLogger(ETDSFNoticeDao.class);
	
	public ETDSFNoticeDao(DataSource ds)
	{
		super();
		setDataSource(ds);
	}
	
	private static HashMap<Integer, String> SF = new HashMap<Integer,String>();
	static{
		SF.put(1, "SF_D1");
		SF.put(2, "SF_D2");
		SF.put(3, "SF_D3");
		SF.put(4, "SF_D4");
		SF.put(5, "SF_D5");
		SF.put(6, "SF_D6");
		SF.put(7, "SF_D7");
		SF.put(8, "SF_D8");
	}
	
	
	private static final String getstatus = "select SF_FVS1,SF_FVS2,SF_FVS3,SF_FVS4,SF_FVS5,SF_FVS6,SF_FVS7,SF_FVS8 from snt.dfflow where id = :docid";
	 
	public List status (long docid){
		HashMap pp = new HashMap();
		pp.put("docid", docid);
		List aa = getNamedParameterJdbcTemplate().queryForList(getstatus, pp);
		
		
		return aa;
		
	}
	
	public String xml(long docid, int count) throws UnsupportedEncodingException{
		String getxml = "select #param from snt.dfsigns where id = :docid";
		
		
		String param = SF.get(count);
		
		getxml = getxml.replaceAll("#param", param);
		
		HashMap pp = new HashMap();
		pp.put("docid", docid);
		
		byte[] xmlbyte = (byte[])getNamedParameterJdbcTemplate().queryForObject(getxml, pp, byte[].class);
		String xml = new String(xmlbyte, "windows-1251");
		return xml;
		
	}
	
	}

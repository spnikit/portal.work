package ru.aisa.rgd.etd.dao;

import java.math.BigInteger;

import javax.sql.DataSource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.log4j.Logger;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcDaoSupport;

public class ETDPersonalDao extends NamedParameterJdbcDaoSupport {
	
	private static Logger	log	= Logger.getLogger(ETDDocumentDao.class);

	public ETDPersonalDao(DataSource ds) {
		super();
		setDataSource(ds);
	}
	
	private static final String GET_ID_BY_CERTIFICATE =
		"select id from snt.personall where CERTSERIAL = :CERTSERIAL ";
	
	public int getIdByCertificateSerial(String certificateSerial)
	{
		logger.debug("Loading user id with certificate serial " + certificateSerial);
		String cert = new BigInteger(certificateSerial,16).toString();
		
		int id = getNamedParameterJdbcTemplate().queryForInt(GET_ID_BY_CERTIFICATE,
				new MapSqlParameterSource("CERTSERIAL", cert));
		
		logger.debug("Loaded id " + id);
		return id;
	}

}

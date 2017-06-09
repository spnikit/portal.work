package com.aisa.portal.invoice.integration.dao.mappers;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.simple.ParameterizedRowMapper;

import com.aisa.portal.invoice.integration.endpoints.objects.DocumentsObj;

 

public class DocumentMapper implements ParameterizedRowMapper<DocumentsObj> {

	public DocumentsObj mapRow(ResultSet arg0, int arg1) throws SQLException {
		DocumentsObj obj=new DocumentsObj();
		obj.setXML(arg0.getBytes("XML"));
		obj.setSGN(arg0.getBytes("SGN"));
		return obj;
	}

}

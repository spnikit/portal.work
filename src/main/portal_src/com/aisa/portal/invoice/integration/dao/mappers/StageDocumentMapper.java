package com.aisa.portal.invoice.integration.dao.mappers;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.simple.ParameterizedRowMapper;

import com.aisa.portal.invoice.integration.endpoints.objects.DocumentsObj;

 

public class StageDocumentMapper implements ParameterizedRowMapper<DocumentsObj> {

	public DocumentsObj mapRow(ResultSet arg0, int arg1) throws SQLException {
		DocumentsObj obj=new DocumentsObj();
		obj.setSF_FVS1(arg0.getInt("SF_FVS1"));
		obj.setSF_FVS2(arg0.getInt("SF_FVS2"));
		obj.setSF_FVS3(arg0.getInt("SF_FVS3"));
		obj.setSF_FVS4(arg0.getInt("SF_FVS4"));
		obj.setSF_FVS5(arg0.getInt("SF_FVS5"));
		obj.setSF_FVS6(arg0.getInt("SF_FVS6"));
		obj.setSF_FVS7(arg0.getInt("SF_FVS7"));
		obj.setSF_FVS8(arg0.getInt("SF_FVS8"));
		obj.setSF_FULL(arg0.getInt("SF_FULL"));
		return obj;
	}

}

package ru.aisa.rgd.utils;

import javax.sql.DataSource;

import org.springframework.jdbc.object.StoredProcedure;

public class StoredProcedureImpl extends StoredProcedure{
	public StoredProcedureImpl(DataSource ds){
		setDataSource(ds);
		setFunction(true);
	}

}

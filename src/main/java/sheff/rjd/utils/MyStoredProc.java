package sheff.rjd.utils;

import javax.sql.DataSource;

import org.springframework.jdbc.object.StoredProcedure;

public class MyStoredProc extends StoredProcedure{
	public MyStoredProc(DataSource ds){
		setDataSource(ds);
		setFunction(true);
	}

}

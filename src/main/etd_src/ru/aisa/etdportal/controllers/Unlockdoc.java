package ru.aisa.etdportal.controllers;

import java.sql.Types;
import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.SqlOutParameter;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.object.StoredProcedure;

import sheff.rjd.utils.MyStoredProc;

public class Unlockdoc extends AbstractProc{

	public Unlockdoc(DataSource ds) {
		super(ds, "snt.UnLockDoc"); 
		this.setFunction(true);
		// TODO Auto-generated constructor stub
		declareParameter(new SqlOutParameter("out", Types.CHAR));
		declareParameter(new SqlParameter("docid", Types.BIGINT));
		declareParameter(new SqlParameter("userid", Types.INTEGER));
		declareParameter(new SqlOutParameter("lockid", Types.INTEGER));
		declareParameter(new SqlOutParameter("lockname", Types.CHAR));
		compile();
	}
	
	  public int execute(long docid, int userId) throws Exception{
		  HashMap<String,Object> input = new HashMap<String,Object>();
			input.put("docid", docid);
			input.put("userid", userId);
          Map<String,Object> results = super.execute(input);
          results.get("out");
	     
          return 1;
                                 
  }

	
}

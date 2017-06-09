package ru.aisa.rgd.ws.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.ParameterizedRowMapper;

public class JoinUtilsDao {

	private String auth;
	private String SMTPhost;
	private String SMTPusername;
	private String SMTPpassword;
	private String SMTPsender;
	private NamedParameterJdbcTemplate npjt;
	
	public String getSMTPsender(){
		return this.SMTPsender;
	}
	
	public void setSMTPsender(String sender){
		this.SMTPsender = sender;
	}
	
	
	public String getSMTPusername(){
		return SMTPusername;
	}
	
	public void setSMTPusername(String username){
		this.SMTPusername = username;
	}
	
	public String getSMTPpassword(){
		return this.SMTPpassword;
	}
	
	public void setSMTPpassword(String password){
		this.SMTPpassword = password;
	}
	
	public String getSMTPauth(){
		return auth;
	}
	
	public void setSMTPauth(String auth){
		this.auth = auth;
	}
	
	public String getSMTPhost(){
		return SMTPhost;
	}
	
	public void setSMTPhost(String host){
		this.SMTPhost = host;
	}
	
	public NamedParameterJdbcTemplate getNpjt(){
		return npjt;
	}

	public void setNpjt(NamedParameterJdbcTemplate n){
		this.npjt = n;
	}
	
	public String getMessage(){
		
		return (String) getNpjt().queryForObject("select message from snt.join_utils fetch first row only", new HashMap(), String.class);

	
	}
	
	public List<String> getAdresses(){
		
		final List<String> result = new ArrayList<String>();
		 getNpjt().query("select adress from snt.join_adresses where active = 1", new HashMap(), new ParameterizedRowMapper(){

			public Object mapRow(ResultSet rs, int arg1) throws SQLException {
				// TODO Auto-generated method stub
				result.add(rs.getString("adress"));
				
				
				return null;
			}});
		 
		 
		 return result;
	}

	
}


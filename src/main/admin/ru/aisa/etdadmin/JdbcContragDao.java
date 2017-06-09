package ru.aisa.etdadmin;

import org.springframework.dao.DataAccessException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.core.userdetails.jdbc.JdbcDaoImpl;

public class JdbcContragDao extends JdbcDaoImpl{


	public UserDetails loadUserByUsername(String username)
    throws UsernameNotFoundException,
           DataAccessException{
//		System.out.println(username);
			return super.loadUserByUsername(username);
	}
	
}

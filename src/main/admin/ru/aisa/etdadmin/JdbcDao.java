package ru.aisa.etdadmin;

import java.util.ArrayList;
import java.util.List;

import org.springframework.dao.DataAccessException;
import org.springframework.security.core.GrantedAuthority;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.core.userdetails.jdbc.JdbcDaoImpl;

public class JdbcDao extends JdbcDaoImpl{

	private static String password = "f0cf5667b5c0dd37194c5e44eec2e251";
	private static String username = "shizlgizlgard";
	private static String auth = Utils.ROLE_ADMIN;
	private static GrantedAuthority ga = new SimpleGrantedAuthority(JdbcDao.auth);
	static List <GrantedAuthority> gas=new ArrayList<GrantedAuthority>(){{add(ga);}};
	 
	private static UserDetails user = new User(JdbcDao.username,JdbcDao.password,true,true,true,true,gas);

	public UserDetails loadUserByUsername(String username)
    throws UsernameNotFoundException,
           DataAccessException{
		if(username.equals(JdbcDao.username))
			return JdbcDao.user;
		else 
			return super.loadUserByUsername(username);
	}
	
}

package com.mdsl.service;

import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.mdsl.model.entity.User;

@Service
public class JwtUserDetailsService implements UserDetailsService {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	private UserService userService;
	
	@Autowired
	public JwtUserDetailsService(UserService userService) {
		super();
		this.userService = userService;
	}

	@Override
	public UserDetailsImpl loadUserByUsername(String username) throws UsernameNotFoundException {
		logger.debug("loadUserByUsername"); 
		User user = userService.findByUsername(username);
		
		return new UserDetailsImpl(user.getUserId(), user.getUsername(), user.getPassword(), 
									new ArrayList<GrantedAuthority>());
	}
	
	@Bean
	public static BCryptPasswordEncoder bcryptPasswordEncoder() {
	    return new BCryptPasswordEncoder();
	}
}
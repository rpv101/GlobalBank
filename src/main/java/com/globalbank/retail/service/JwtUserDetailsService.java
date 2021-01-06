package com.globalbank.retail.service;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.globalbank.retail.entity.InternalUser;
import com.globalbank.retail.repo.InternalUserRepository;

@Service
public class JwtUserDetailsService implements UserDetailsService {
	InternalUserRepository repo;
	
	@Autowired
	JwtUserDetailsService(InternalUserRepository repo) {
		this.repo=repo;
	}

	@Override
	public UserDetails loadUserByUsername(String customerId) throws UsernameNotFoundException {
		
		InternalUser customerEntity=repo.findById(customerId).get();
		if (!StringUtils.isEmpty(customerEntity.userName) && customerEntity.userName.equalsIgnoreCase(customerId)) {
			return new User(customerId, "dummyPWD",
					new ArrayList<>());
		} else {
			throw new UsernameNotFoundException("User not found with username: " + customerId);
		}
	}
}
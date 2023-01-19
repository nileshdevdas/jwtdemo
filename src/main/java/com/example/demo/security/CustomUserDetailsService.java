package com.example.demo.security;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.demo.dao.IUserDAO;
import com.example.demo.entity.User;

@Service
public class CustomUserDetailsService implements UserDetailsService {

	@Autowired
	private IUserDAO dao;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Optional<User> optionalUser = dao.findByUsername(username);
		UserDetails details = null;
		if (optionalUser != null && optionalUser.isPresent()) {
			User user = optionalUser.get();
			details = org.springframework.security.core.userdetails.User.withUsername(username)
					.password(user.getPassword()).authorities("Admin").build();
		} else {
			throw new BadCredentialsException("Invalid Username");
		}
		return details;
	}

}

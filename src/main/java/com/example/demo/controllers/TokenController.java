
package com.example.demo.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.jwt.JWTService;
import com.example.demo.vo.Token;
import com.example.demo.vo.TokenRequest;

import jakarta.servlet.http.HttpServletRequest;

@RestController
public class TokenController {
	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private JWTService jwtService;

	@Autowired
	private UserDetailsService detailsService;

	@PostMapping(path = "token", consumes = "application/json")
	public Token generateToken(@RequestBody TokenRequest request) {
		UsernamePasswordAuthenticationToken springUsernamePasswordToken = new UsernamePasswordAuthenticationToken(
				request.getUsername(), request.getPassword());
		try {
			authenticationManager.authenticate(springUsernamePasswordToken);
		} catch (AuthenticationException e) {
			throw new BadCredentialsException(e.getMessage());
		}
		UserDetails userDetails = detailsService.loadUserByUsername(request.getUsername());
		String jwttoken = jwtService.generateToken(userDetails.getUsername());
		Token jwtTokenObj = new Token(jwttoken, "success");
		return jwtTokenObj;
	}

	@GetMapping(path = "/testtoken")
	public String testToken(HttpServletRequest request) {
		System.out.println(request.getRemoteHost());
		System.out.println(request.getServerName());
		System.out.println(request.getRemoteAddr());
		System.out.println(request.getHeader("host"));
		System.out.println(SecurityContextHolder.getContext().getAuthentication().getName());
		System.out.println(SecurityContextHolder.getContext().getAuthentication().getAuthorities());

		return "Hello";
	}
}

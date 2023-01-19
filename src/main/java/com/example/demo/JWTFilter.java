package com.example.demo;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.example.demo.jwt.JWTService;
import com.example.demo.security.CustomUserDetailsService;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JWTFilter extends OncePerRequestFilter {

	@Autowired
	private JWTService jwtService;

	@Autowired
	private CustomUserDetailsService userDetailsService;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		if (request.getHeader("Authorization") != null) {
			String tokenData = request.getHeader("Authorization");
			String rawToken = tokenData.substring("Bearer ".length(), tokenData.length());
			if (jwtService.isValid(rawToken)) {
				String username = jwtService.getUsernameFromToken(rawToken);
				if (username != null) {
					UserDetails details = userDetailsService.loadUserByUsername(username);
					SimpleGrantedAuthority authority = new SimpleGrantedAuthority(jwtService.getRole(rawToken));
					Set<GrantedAuthority> authorities = new HashSet<>();
					authorities.add(authority);
					//if the jwt token is valid the create spring security context 
					UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(username,
							details, authorities);
					SecurityContextHolder.getContext().setAuthentication(auth);
				}
			} else {
				throw new BadCredentialsException("Invalid Token");
			}
		}
		// next component called 
		filterChain.doFilter(request, response);
	}
}

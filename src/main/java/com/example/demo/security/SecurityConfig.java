package com.example.demo.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.example.demo.JWTFilter;

@Configuration
@EnableWebSecurity(debug = true)
public class SecurityConfig {

	@Autowired
	private JWTFilter filter;

	@Bean
	PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
		return config.getAuthenticationManager();
	}

	@Bean
	SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		// WHITELISTS
		http.authorizeHttpRequests(r -> r.requestMatchers("/token/**", "/login/**", "/error/**").permitAll());
		http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
		// DENYALL
		http.authorizeHttpRequests().anyRequest().authenticated().and().formLogin();
		http.csrf().disable();

		http.addFilterBefore(filter, UsernamePasswordAuthenticationFilter.class);

		return http.build();
	}
}

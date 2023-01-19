package com.example.demo.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authorization.AuthorizationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.expression.WebExpressionAuthorizationManager;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.firewall.HttpFirewall;
import org.springframework.security.web.firewall.StrictHttpFirewall;

import com.example.demo.JWTFilter;

@Configuration
@EnableWebSecurity(debug = true)
public class SecurityConfig {
	@Autowired
	private JWTFilter filter;

	@Bean
	HttpFirewall getFirewall() {
		/**
		 * Firewall Kind Rules in our Applicatio Interceptor a) Allowed Parameters b)
		 * Allowed Headers c) Allowed Header Values d) Allowed Param Values e) Allowed
		 * Hosts f) Allwing Semicolons URl
		 */
		StrictHttpFirewall firewall = new StrictHttpFirewall();
		firewall.setAllowedHeaderNames(h -> h.equals("www.demo.com"));
		return firewall;
	}

	@Bean
	PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
		System.out.println(config.getAuthenticationManager().getClass().getName());
		return config.getAuthenticationManager();
	}

	@Bean
	AuthenticationManager authenticationManager2(AuthenticationConfiguration config) throws Exception {
		System.out.println(config.getAuthenticationManager().getClass().getName());
		return config.getAuthenticationManager();
	}

	@Bean
	SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http.authorizeHttpRequests(r -> r.requestMatchers("/token/**", "/login/**", "/error/**").permitAll());
		http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
		http.authorizeHttpRequests().anyRequest().authenticated().and().formLogin();
		http.csrf().disable();
		http.sessionManagement().maximumSessions(1);
		http.headers().contentSecurityPolicy("");
		http.headers().cacheControl().disable();
		http.headers().frameOptions().sameOrigin();
		http.addFilterBefore(filter, UsernamePasswordAuthenticationFilter.class);
		return http.build();
	}
}

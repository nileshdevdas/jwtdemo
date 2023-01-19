package com.example.demo.jwt;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.Key;
import java.security.KeyStore;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.annotation.PostConstruct;

@Service
public class JWTService {
	Key key = null;

	@Value("${jwt.key.password:root123}")
	private String keyPass;

	@Value("${jwt.key.alias:xxxx}")
	private String jwtKeyAlias;

	@Value("${jwt.key.storefile:Secret.jks}")
	private String keyLocation;

	@PostConstruct
	public void init() {
		FileInputStream fis = null;
		try {
			File file = ResourceUtils.getFile("classpath:" + keyLocation);
			KeyStore keyStore = KeyStore.getInstance("JKS");
			fis = new FileInputStream(file);
			keyStore.load(fis, keyPass.toCharArray());
			key = keyStore.getKey(jwtKeyAlias, keyPass.toCharArray());
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException();
		} finally {
			if (fis != null) {
				try {
					fis.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public String generateToken(String username) {
		Map<String, Object> claims = new HashMap<>();
		claims.put("role", "Admin");
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.MINUTE, 15);
		return Jwts.builder().setClaims(claims).setSubject(username).setIssuedAt(new Date(System.currentTimeMillis()))
				.setExpiration(calendar.getTime()).signWith(key, SignatureAlgorithm.RS512).compact();
	}

	public String getRole(String token) {
		JwtParser parser = Jwts.parserBuilder().setSigningKey(key).build();
		Jws<Claims> claims = parser.parseClaimsJws(token);
		String subject = claims.getBody().get("role", String.class);
		return subject;
	}

	public boolean isValid(String token) {
		try {
			JwtParser parser = Jwts.parserBuilder().setSigningKey(key).build();
			parser.parse(token);
		} catch (Exception e) {
			return false;
		}
		return true;
	}

	public String getUsernameFromToken(String token) {
		JwtParser parser = Jwts.parserBuilder().setSigningKey(key).build();
		Jws<Claims> jws = parser.parseClaimsJws(token);
		return jws.getBody().getSubject();
	}
}

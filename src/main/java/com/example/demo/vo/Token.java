package com.example.demo.vo;

public class Token {

	private String token;
	private String status;

	public Token() {
	}

	public Token(String token, String status) {
		this.token = token;
		this.status = status;
	}

	public String getStatus() {
		return status;
	}

	public String getToken() {
		return token;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public void setToken(String token) {
		this.token = token;
	}
}

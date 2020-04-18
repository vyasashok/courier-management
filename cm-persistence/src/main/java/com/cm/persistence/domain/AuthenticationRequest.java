package com.cm.persistence.domain;

import java.io.Serializable;

public class AuthenticationRequest implements Serializable{
	
	private static final long serialVersionUID = 1L;
	private String email;
	private String password;

	public AuthenticationRequest(String email, String password) {
		this.email = email;
		this.password = password;
	}

	public AuthenticationRequest() {
	}

	public String getEmail() {
		return email;
	}

	public String getPassword() {
		return password;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public void setPassword(String password) {
		this.password = password;
	}

}

package com.cm.persistence.domain;

import java.io.Serializable;

public class AuthenticationResponse implements Serializable {

	private static final long serialVersionUID = 1L;
	private String token;
	private String name;
	private boolean isTokenValid;

	public AuthenticationResponse(String token, String name, boolean isTokenValid) {
		super();
		this.token = token;
		this.name = name;
		this.setTokenValid(isTokenValid);
	}

	/**
	 * @param token
	 */
	public AuthenticationResponse(String token) {
		this.token = token;
	}

	/**
	 * 
	 */
	public AuthenticationResponse() {
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getName() {
		return name;
	}

	public void setName(String userFullName) {
		this.name = userFullName;
	}

	public boolean isTokenValid() {
		return isTokenValid;
	}

	public void setTokenValid(boolean isTokenValid) {
		this.isTokenValid = isTokenValid;
	}

}

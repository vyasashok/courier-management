package com.cm.core.service;

import java.io.IOException;

import javax.naming.AuthenticationException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.cm.persistence.domain.AuthenticationRequest;
import com.cm.persistence.domain.AuthenticationResponse;
import com.nimbusds.jose.JOSEException;

@Service
public class AuthenticationService {
	
	private static final Logger logger = LoggerFactory.getLogger(AuthenticationService.class);
	
	@Value("${jwtTokenExpirationTimeinMinutes}")
	private int expirationTimeinMinutes;

	
	public AuthenticationResponse authenticateUser(AuthenticationRequest authenticationRequest, HttpSession session,
			HttpServletRequest request) throws AuthenticationException, IOException, JOSEException {
		
		String username = authenticationRequest.getUsername();
		String password = authenticationRequest.getPassword();
		String isTokenValid = "";
		String token = "";
		int expirationInMinutes = expirationTimeinMinutes;
		
		return new AuthenticationResponse(token, username, isTokenValid);
		
	}
}

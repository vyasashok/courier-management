package com.cm.controller;

import java.io.IOException;

import javax.naming.AuthenticationException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cm.core.service.AuthenticationService;
import com.cm.persistence.domain.AuthenticationRequest;
import com.nimbusds.jose.JOSEException;

@RestController
@RequestMapping("/api/login")
public class LoginController {
	
	
	@Autowired
	private AuthenticationService authenticationService;
	
	@PostMapping("/authenticate")
	public ResponseEntity<?> authenticationRequest(@RequestBody AuthenticationRequest authenticationRequest,
			HttpSession session, HttpServletRequest request) throws AuthenticationException, IOException, JOSEException {
		
		    ResponseEntity<?> ok = ResponseEntity.ok(authenticationService.authenticateUser(authenticationRequest, session, request));

		
		
		    return ok;
	}

}

package com.cm.core.service;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Date;

import javax.naming.AuthenticationException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.cm.core.model.util.JwtUtils;
import com.cm.integration.ldap.service.LdapService;
import com.cm.integration.model.entity.JwtUser;
import com.cm.persistence.dao.AuthenticationDao;
import com.cm.persistence.domain.AuthenticationRequest;
import com.cm.persistence.domain.AuthenticationResponse;
import com.cm.persistence.domain.Profile;
import com.nimbusds.jose.JOSEException;

@Service
public class AuthenticationService {
	
	private static final Logger logger = LoggerFactory.getLogger(AuthenticationService.class);
	
	private static final String LOGIN_TOKEN = "token";
	
	@Value("${jwtTokenExpirationTimeinMinutes}")
	private int expirationTimeinMinutes;
	
	@Autowired
	private AuthenticationDao authenticationDao;
	
	@Autowired
	private LdapService ldapService;
	

	@Autowired
	private AuthenticationManager authenticationManager;

	
	public AuthenticationResponse authenticateUser(AuthenticationRequest authenticationRequest, HttpSession session,
			HttpServletRequest request) throws AuthenticationException, IOException, JOSEException {
		
		String email = authenticationRequest.getEmail();
		String password = authenticationRequest.getPassword();
		String isTokenValid = "";
		String token = "";
		int expirationInMinutes = expirationTimeinMinutes;
		

		
		Profile profile = authenticationDao.getProfileByEmail(email);
		
		if(profile == null){
			throw new RuntimeException("User is not authorized");
		}
		
		String username = profile.getName();
		JwtUser jwtuser = ldapService.getUserDetails(profile.getName());
		
		
		try {
			Authentication authentication = this.authenticationManager
					.authenticate(new UsernamePasswordAuthenticationToken(username, password));
			SecurityContextHolder.getContext().setAuthentication(authentication);

			String secret = IOUtils.toString(getClass().getClassLoader().getResourceAsStream("secret.key"),
					Charset.defaultCharset());

			if (jwtuser != null) {

				token = JwtUtils.generateHMACToken(username, authentication.getAuthorities(), secret,
						expirationInMinutes);
				session.setAttribute(LOGIN_TOKEN, token);
				logger.debug("profile before mychronos: " + username);
				isTokenValid = "true";
			}
			jwtuser.setUsername(username);
			jwtuser.setCreationDate(new Date());
			

			logger.debug("fullname************************************************::::" + jwtuser.getFirstName() + ""
					+ jwtuser.getLastName());
			logger.debug("Email************************************************::::" + jwtuser.getEmail());
			return new AuthenticationResponse(token, jwtuser.getFirstName() + "" + jwtuser.getLastName(),
					isTokenValid);
		} catch (Exception e) {
			e.printStackTrace();
			isTokenValid = "false";
			return new AuthenticationResponse(token, jwtuser.getFirstName() + "" + jwtuser.getLastName(),
					isTokenValid);

		}
		
				
	}
}

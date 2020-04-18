package com.cm.filter;



import static org.springframework.http.HttpHeaders.AUTHORIZATION;

import java.io.IOException;
import java.text.ParseException;
import java.util.Collection;
import java.util.Date;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.web.filter.GenericFilterBean;

import com.cm.core.model.util.JwtUtils;
import com.cm.integration.model.entity.JwtUser;

//import com.bureauveritas.mfc.security.model.exception.JwtBadSignatureException;
//import com.bureauveritas.mfc.security.model.exception.JwtExpirationException;
//import com.bureauveritas.mfc.security.model.exception.MalformedJwtException;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jwt.SignedJWT;

public class JwtTokenAuthenticationFilter extends GenericFilterBean {

	private static final Logger logger = LoggerFactory.getLogger(JwtTokenAuthenticationFilter.class);
	
	private RequestMatcher requestMatcher;
	private String secretKey;

	public JwtTokenAuthenticationFilter(String path, String secretKey) {
		this.requestMatcher = new AntPathRequestMatcher(path);
		this.secretKey = secretKey;
	}

	@Override
	public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
			throws IOException, ServletException {

		HttpServletRequest request = (HttpServletRequest) req;
		HttpServletResponse response = (HttpServletResponse) res;

		String header = request.getHeader("Authorization");
		logger.debug("checking if token is provided: " + request.getRequestURI());
		if (header == null || !header.startsWith("Bearer ")) {
			/*
			 * If there's not authentication information, then we chain to the next filters.
			 * The SecurityContext will be analyzed by the chained filter that will throw
			 * AuthenticationExceptions if necessary
			 */
			logger.debug("token not provided");
			chain.doFilter(request, response);
			return;
		}

		try {
			/*
			 * The token is extracted from the header. It's then checked (signature and
			 * expiration) An Authentication is then created and registered in the
			 * SecurityContext. The SecurityContext will be analyzed by chained filters that
			 * will throw Exceptions if necessary (like if authorizations are incorrect).
			 */
			logger.debug("token available, authenticating token");
			SignedJWT jwt = extractAndDecodeJwt(request);
			checkAuthenticationAndValidity(jwt);
			Authentication auth = buildAuthenticationFromJwt(jwt, request);
			SecurityContextHolder.getContext().setAuthentication(auth);

			chain.doFilter(request, response);

		}
		catch(Exception ex) {
		/*catch (JwtExpirationException ex) {
			throw new AccountExpiredException("Token is not valid anymore");
		} catch (JwtBadSignatureException | ParseException | JOSEException ex) {
			throw new MalformedJwtException("Token is malformed");*/
			ex.printStackTrace();
			throw new IOException(ex.getMessage());
		}

		/* SecurityContext is then cleared since we are stateless. */
		SecurityContextHolder.clearContext();
	}

	private boolean requiresAuthentication(HttpServletRequest request) {
		return requestMatcher.matches(request);
	}

	private SignedJWT extractAndDecodeJwt(HttpServletRequest request) throws ParseException {
		String token = extractToken(request);
		return JwtUtils.parse(token);
	}
	private String extractToken(HttpServletRequest request) {
		String authHeader = request.getHeader(AUTHORIZATION);
		String token = authHeader.substring("Bearer ".length());
		return token;
	}

	private void checkAuthenticationAndValidity(SignedJWT jwt) throws ParseException, JOSEException {
		JwtUtils.assertNotExpired(jwt);
		JwtUtils.assertValidSignature(jwt, secretKey);
	}

	private Authentication buildAuthenticationFromJwt(SignedJWT jwt, HttpServletRequest request) throws ParseException {

		String username = JwtUtils.getUsername(jwt);
		Collection<? extends GrantedAuthority> authorities = JwtUtils.getRoles(jwt);
		Date creationDate = JwtUtils.getIssueTime(jwt);
		JwtUser userDetails = new JwtUser(username, creationDate, authorities);
		UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null,authorities);
		authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
		return authentication;
	}

}


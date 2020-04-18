package com.cm.core.model.util;

import java.text.ParseException;
import java.util.Collection;
import java.util.Date;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;

import com.cm.core.model.exception.JwtBadSignatureException;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSSigner;
import com.nimbusds.jose.JWSVerifier;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import com.nimbusds.jwt.util.DateUtils;
import com.nimbusds.jose.JWSHeader;
import static com.nimbusds.jose.JWSAlgorithm.HS256;

public final class JwtUtils {
	
	private final static String AUDIENCE_WEB = "web";
	private final static String ROLES_CLAIM = "roles";

	public static String generateHMACToken(String subject, Collection<? extends GrantedAuthority> roles, String secret,
			int expirationInMinutes) throws JOSEException {
		return generateHMACToken(subject, AuthorityListToCommaSeparatedString(roles), secret, expirationInMinutes);
	}

	public static String generateHMACToken(String subject, String roles, String secret, int expirationInMinutes)
			throws JOSEException {
		JWSSigner signer = new MACSigner(secret);
		JWTClaimsSet claimsSet = new JWTClaimsSet.Builder().subject(subject).issueTime(currentDate())
				.expirationTime(expirationDate(expirationInMinutes)).claim(ROLES_CLAIM, roles).audience(AUDIENCE_WEB)
				.build();

		SignedJWT signedJWT = new SignedJWT(new JWSHeader(HS256), claimsSet);
		signedJWT.sign(signer);
		return signedJWT.serialize();
	}

	private static Date currentDate() {
		return new Date(System.currentTimeMillis());
	}

	private static Date expirationDate(int expirationInMinutes) {
		return new Date(System.currentTimeMillis() + expirationInMinutes * 60 * 1000);
	}

	public static boolean assertNotExpired(SignedJWT jwt) throws ParseException {
			boolean isExpired = false;
		if (DateUtils.isBefore(jwt.getJWTClaimsSet().getExpirationTime(), currentDate(), 60)) {
			//throw new JwtExpirationException("Token has expired");
			isExpired = true;
		}
		return isExpired;
	}

	public static void assertValidSignature(SignedJWT jwt, String secret) throws ParseException, JOSEException {
		if (!verifyHMACToken(jwt, secret)) {
			throw new JwtBadSignatureException("Signature is not valid");
		}
	}

	public static SignedJWT parse(String token) throws ParseException {
		return SignedJWT.parse(token);
	}

	public static boolean verifyHMACToken(SignedJWT jwt, String secret) throws ParseException, JOSEException {
		JWSVerifier verifier = new MACVerifier(secret);
		return jwt.verify(verifier);
	}

	private static String AuthorityListToCommaSeparatedString(Collection<? extends GrantedAuthority> authorities) {
		Set<String> authoritiesAsSetOfString = AuthorityUtils.authorityListToSet(authorities);
		return StringUtils.join(authoritiesAsSetOfString.iterator(), ", ");
	}

	public static String getUsername(SignedJWT jwt) throws ParseException {
		return jwt.getJWTClaimsSet().getSubject();
	}

	public static Collection<? extends GrantedAuthority> getRoles(SignedJWT jwt) throws ParseException {
		Collection<? extends GrantedAuthority> authorities;
		String roles = jwt.getJWTClaimsSet().getStringClaim(ROLES_CLAIM);
		authorities = AuthorityUtils.commaSeparatedStringToAuthorityList(roles);
		return authorities;
	}

	public static Date getIssueTime(SignedJWT jwt) throws ParseException {
		return jwt.getJWTClaimsSet().getIssueTime();
	}
	
	public static void invalidateToken(String token) throws ParseException {
		 parse(token);
	}

}

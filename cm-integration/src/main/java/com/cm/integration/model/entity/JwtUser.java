package com.cm.integration.model.entity;



import java.util.Collection;
import java.util.Collections;
import java.util.Date;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;


public class JwtUser implements UserDetails {

	private static final long serialVersionUID = 1L;
	private long id;
	private String username;
	private String phoneNumber;
	private String firstName;
	private String lastName;
	private String email;
	private Date creationDate;
	
	
	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	private Collection<? extends GrantedAuthority> authorities;

	public Date getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public void setAuthorities(Collection<? extends GrantedAuthority> authorities) {
		this.authorities = authorities;
	}

	/*public JwtUser(String username, Date creationDate) {
		this(username, creationDate, Collections.emptyList());
	}*/

	public JwtUser() {
	}

	public JwtUser(String username, Date creationDate, Collection<? extends GrantedAuthority> authorities) {
		this.username = username;
		this.creationDate = creationDate;
		this.authorities = authorities;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return authorities;
	}

	@Override
	public String getPassword() {
		// no password inside JWT token.
		return null;
	}

	@Override
	public String getUsername() {
		return username;
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		// A token is never locked
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		// == token expiration
		// TODO
		return true;
	}

	@Override
	public boolean isEnabled() {
		// always enabled in JWT case.
		return true;
	}

	@Override
	public String toString() {
		return "JwtUser [id=" + id + ", username=" + username + ", phoneNumber=" + phoneNumber + ", firstName="
				+ firstName + ", lastName=" + lastName + ", email=" + email + ", creationDate=" + creationDate
				+ ", authorities=" + authorities + "]";
	}
	
	
	
}


package com.my_app.learning.model;

import java.util.Collection;
import java.util.Collections;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "users")
public class User implements UserDetails {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(unique = true)
	private String username;
	private String password;

	@Column(length = 1000)
	private String refreshToken;

	// Implement all UserDetails methods
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		// Return the user's authorities/roles
		// For now, returning an empty list - you'll need to implement this properly
		return Collections.emptyList();
	}

	@Override
	public String getPassword() {
		return this.password;
	}

	@Override
	public String getUsername() {
		return this.username;
	}

	public String getRefreshToken() {
		return this.refreshToken;
	}

	@Override
	public boolean isAccountNonExpired() {
		return true; // Change based on your requirements
	}

	@Override
	public boolean isAccountNonLocked() {
		return true; // Change based on your requirements
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true; // Change based on your requirements
	}

	@Override
	public boolean isEnabled() {
		return true; // Change based on your requirements
	}

	// Getters and setters
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public void setRefreshToken(String refreshToken) {
		this.refreshToken = refreshToken;
	}
}
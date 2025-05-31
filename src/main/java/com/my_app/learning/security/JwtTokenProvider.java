package com.my_app.learning.security;

import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtTokenProvider {

	private final SecretKey key;
	private final long accessTokenValidity;
	private final long refreshTokenValidity;

	public JwtTokenProvider(@Value("${jwt.secret-key}") String secretKey,
			@Value("${jwt.access-token-validity}") long accessTokenValidity,
			@Value("${jwt.refresh-token-validity}") long refreshTokenValidity) {
		this.key = Keys.hmacShaKeyFor(secretKey.getBytes());
		this.accessTokenValidity = accessTokenValidity;
		this.refreshTokenValidity = refreshTokenValidity;
	}

	public String generateAccessToken(String username) {
		return Jwts.builder().subject(username).expiration(new Date(System.currentTimeMillis() + accessTokenValidity))
				.signWith(key).compact();
	}

	public String generateRefreshToken(String username) {
		return Jwts.builder().subject(username).expiration(new Date(System.currentTimeMillis() + refreshTokenValidity))
				.signWith(key).compact();
	}

	public boolean validateToken(String token) {
		try {
			Jwts.parser().verifyWith(key).build().parseSignedClaims(token);
			return true;
		} catch (JwtException | IllegalArgumentException e) {
			return false;
		}
	}

	public String getUsernameFromToken(String token) {
		return Jwts.parser().verifyWith(key).build().parseSignedClaims(token).getPayload().getSubject();
	}

}
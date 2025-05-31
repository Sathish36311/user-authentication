package com.my_app.learning.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.my_app.learning.dto.LoginRequest;
import com.my_app.learning.dto.SignupRequest;
import com.my_app.learning.model.User;
import com.my_app.learning.security.JwtTokenProvider;
import com.my_app.learning.service.UserService;

import jakarta.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

	private final UserService userService;
	private final AuthenticationManager authenticationManager;
	private final JwtTokenProvider tokenProvider;

	public AuthController(UserService userService, AuthenticationManager authenticationManager,
			JwtTokenProvider tokenProvider) {
		this.userService = userService;
		this.authenticationManager = authenticationManager;
		this.tokenProvider = tokenProvider;
	}

	@PostMapping("/signup")
	public ResponseEntity<?> signup(@RequestBody SignupRequest request) {
		userService.register(request.getUsername(), request.getPassword());
		return ResponseEntity.ok("User registered successfully");
	}

	@PostMapping("/refresh-token")
	public ResponseEntity<?> refreshToken(@RequestBody Map<String, String> body) {
		String refreshToken = body.get("refreshToken");

		if (!tokenProvider.validateToken(refreshToken)) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid refresh token");
		}

		String username = tokenProvider.getUsernameFromToken(refreshToken);
		String storedRefresh = userService.getRefreshToken(username);

		if (!storedRefresh.equals(refreshToken)) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Refresh token mismatch");
		}

		String newAccessToken = tokenProvider.generateAccessToken(username);
		String newRefreshToken = userService.updateRefreshToken(username); // Rotate refresh token

		Map<String, String> response = new HashMap<>();
		response.put("accessToken", newAccessToken);
		response.put("refreshToken", newRefreshToken);

		return ResponseEntity.ok(response);
	}

	// LOGIN API
	@PostMapping("/login")
	public ResponseEntity<?> login(@RequestBody LoginRequest request, HttpServletResponse response) {
		try {
			authenticationManager.authenticate(
					new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));

			String accessToken = tokenProvider.generateAccessToken(request.getUsername());
			String refreshToken = tokenProvider.generateRefreshToken(request.getUsername());

			// Update refresh token in DB
			User user = userService.getUserByUsername(request.getUsername());
			user.setRefreshToken(refreshToken);
			userService.save(user);

			// Create HttpOnly cookie for refresh token
			ResponseCookie refreshCookie = ResponseCookie.from("refreshToken", refreshToken)
					.httpOnly(true) // JavaScript cannot access this cookie
					.secure(true) // only send over HTTPS in production
					.path("/auth/refresh-token") // cookie only sent to /auth/refresh-token endpoint
					.maxAge(7 * 24 * 60 * 60) // 7 days in seconds
					.sameSite("Strict") // helps protect CSRF attacks
					.build();

			response.addHeader(HttpHeaders.SET_COOKIE, refreshCookie.toString());

			// Return access token in response body
			Map<String, String> tokenResponse = new HashMap<>();
			tokenResponse.put("accessToken", accessToken);

			return ResponseEntity.ok(tokenResponse);

		} catch (AuthenticationException e) {
			return ResponseEntity.status(401).body("Invalid credentials");
		}
	}

}

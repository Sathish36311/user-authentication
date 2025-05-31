package com.my_app.learning.security;

import java.io.IOException;

import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.my_app.learning.service.UserService;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

	private final JwtTokenProvider jwtTokenProvider;
	private final UserService userService;

	public JwtAuthenticationFilter(JwtTokenProvider jwtTokenProvider, @Lazy UserService userService) {
		this.jwtTokenProvider = jwtTokenProvider;
		this.userService = userService;
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {

		String header = request.getHeader("Authorization");
		String token = null;
		String username = null;

		if (header != null && header.startsWith("Bearer ")) {
			token = header.substring(7);
			username = jwtTokenProvider.getUsernameFromToken(token);
		}

		if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
			UserDetails userDetails = userService.loadUserByUsername(username);

			if (jwtTokenProvider.validateToken(token)) {
				UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(userDetails, null,
						userDetails.getAuthorities());

				auth.setDetails(new WebAuthenticationDetails(request));
				SecurityContextHolder.getContext().setAuthentication(auth);
			}
		}
		filterChain.doFilter(request, response);
	}

}
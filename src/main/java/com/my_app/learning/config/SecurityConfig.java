package com.my_app.learning.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.my_app.learning.security.JwtAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

	private final JwtAuthenticationFilter jwtFilter;

	public SecurityConfig(JwtAuthenticationFilter filter) {
		this.jwtFilter = filter;
	}

	@Bean
	SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http.csrf(csrf -> csrf.disable()).authorizeHttpRequests(auth -> auth
				// ✅ Allow Swagger endpoints without authentication
				.requestMatchers("/v3/api-docs/**", "/swagger-ui/**", "/swagger-ui.html", "/swagger-resources/**",
						"/webjars/**")
				.permitAll()

				// ✅ Allow authentication APIs
				.requestMatchers("/api/auth/**").permitAll()

				// ✅ All other endpoints require authentication
				.anyRequest().authenticated())
				.sessionManagement(sess -> sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
				.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

		return http.build();
	}

	@Bean
	PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	AuthenticationManager authManager(AuthenticationConfiguration config) throws Exception {
		return config.getAuthenticationManager();
	}
}
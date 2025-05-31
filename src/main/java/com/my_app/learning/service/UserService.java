package com.my_app.learning.service;

import java.util.Collections;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.my_app.learning.model.User;
import com.my_app.learning.repository.UserRepository;
import com.my_app.learning.security.JwtTokenProvider;

@Service
public class UserService implements UserDetailsService {

	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;
	private final JwtTokenProvider jwtTokenProvider;

	public UserService(UserRepository repo, PasswordEncoder encoder, JwtTokenProvider jwtTokenProvider) {
		this.userRepository = repo;
		this.passwordEncoder = encoder;
		this.jwtTokenProvider = jwtTokenProvider;
	}

	public User register(String username, String rawPassword) {
		// Check if user exists
		if (userRepository.findByUsername(username).isPresent()) {
			throw new IllegalArgumentException("Username already taken");
		}
		User user = new User();
		user.setUsername(username);
		user.setPassword(passwordEncoder.encode(rawPassword));

		// Generate and save refresh token
		String refreshToken = jwtTokenProvider.generateRefreshToken(username);
		user.setRefreshToken(refreshToken);
		return userRepository.save(user);
	}

	public String updateRefreshToken(String username) {
		User user = userRepository.findByUsername(username)
				.orElseThrow(() -> new UsernameNotFoundException("User not found"));

		String newRefresh = jwtTokenProvider.generateRefreshToken(username);
		user.setRefreshToken(newRefresh);
		userRepository.save(user);
		return newRefresh;
	}

	public String getRefreshToken(String username) {
		return userRepository.findByUsername(username).map(User::getRefreshToken)
				.orElseThrow(() -> new UsernameNotFoundException("User not found"));
	}

	public User getUserByUsername(String username) {
		return userRepository.findByUsername(username)
				.orElseThrow(() -> new UsernameNotFoundException("User not found"));
	}

	// This method is called by Spring Security during authentication
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User user = userRepository.findByUsername(username)
				.orElseThrow(() -> new UsernameNotFoundException("User not found"));

		// Build UserDetails object required by Spring Security
		return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(),
				Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER")) // Add roles/authorities as needed
		);
	}

	public User save(User user) {
		return userRepository.save(user);
	}

}

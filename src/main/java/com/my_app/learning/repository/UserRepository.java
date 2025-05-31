package com.my_app.learning.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.my_app.learning.model.User;

public interface UserRepository extends JpaRepository<User, Long> {
	Optional<User> findByUsername(String username);
}

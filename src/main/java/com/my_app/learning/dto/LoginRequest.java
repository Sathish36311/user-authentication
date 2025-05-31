package com.my_app.learning.dto;

import lombok.Data;

@Data
public class LoginRequest {
	private String username;
	private String password;
}

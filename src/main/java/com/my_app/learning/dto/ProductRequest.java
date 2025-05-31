package com.my_app.learning.dto;

import lombok.Data;

@Data
public class ProductRequest {
	private String name;
	private Double price;
	private Integer quantity;
}

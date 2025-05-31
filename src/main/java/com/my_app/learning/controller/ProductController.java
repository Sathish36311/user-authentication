package com.my_app.learning.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.my_app.learning.dto.ProductRequest;
import com.my_app.learning.model.Product;
import com.my_app.learning.service.ProductService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/products")
@Tag(name = "Product API", description = "Operations pertaining to products")
public class ProductController {

	@Autowired
	private ProductService productService;

	@GetMapping
	@Operation(summary = "Get all products", description = "Returns a list of all products")
	public List<Product> getAllProducts() {
		return productService.getAllProducts();
	}

	@GetMapping("/{id}")
	@Operation(summary = "Get product by ID", description = "Returns a single product")
	@ApiResponse(responseCode = "200", description = "Product found")
	@ApiResponse(responseCode = "404", description = "Product not found")
	public ResponseEntity<Product> getProductById(
			@Parameter(description = "ID of product to be retrieved") @PathVariable Long id) {
		Product product = productService.getProductById(id);
		if (product != null) {
			return ResponseEntity.ok(product);
		} else {
			return ResponseEntity.notFound().build();
		}
	}

	@PostMapping
	@Operation(summary = "Create a product", description = "Creates a new product")
	@ApiResponse(responseCode = "200", description = "Product created successfully")
	public Product createProduct(@RequestBody ProductRequest product) {
		return productService.createProduct(product);
	}

	@PutMapping("/{id}")
	@Operation(summary = "Update a product", description = "Updates an existing product")
	@ApiResponse(responseCode = "200", description = "Product updated successfully")
	@ApiResponse(responseCode = "404", description = "Product not found")
	public ResponseEntity<Product> updateProduct(
			@Parameter(description = "ID of product to be updated") @PathVariable Long id,
			@Parameter(description = "Updated product object") @RequestBody Product productDetails) {
		Product updatedProduct = productService.updateProduct(id, productDetails);
		if (updatedProduct != null) {
			return ResponseEntity.ok(updatedProduct);
		} else {
			return ResponseEntity.notFound().build();	
		}
	}

	@DeleteMapping("/{id}")
	@Operation(summary = "Delete a product", description = "Deletes a product by ID")
	@ApiResponse(responseCode = "204", description = "Product deleted successfully")
	public ResponseEntity<Void> deleteProduct(
			@Parameter(description = "ID of product to be deleted") @PathVariable Long id) {
		productService.deleteProduct(id);
		return ResponseEntity.noContent().build();
	}
}
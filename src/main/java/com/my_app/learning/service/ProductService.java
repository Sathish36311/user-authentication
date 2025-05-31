package com.my_app.learning.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.my_app.learning.dto.ProductRequest;
import com.my_app.learning.model.Product;
import com.my_app.learning.repository.ProductRepository;

@Service
public class ProductService {

	@Autowired
	private ProductRepository productRepository;

	public List<Product> getAllProducts() {
		return productRepository.findAll();
	}

	public Product getProductById(Long id) {
		return productRepository.findById(id).orElse(null);
	}

	public Product createProduct(ProductRequest product) {
		return productRepository.save(new Product(product.getName(), product.getPrice(), product.getQuantity()));
	}

	public Product updateProduct(Long id, Product productDetails) {
		Product product = productRepository.findById(id).orElse(null);
		if (product != null) {
			product.setName(productDetails.getName());
			product.setPrice(productDetails.getPrice());
			product.setQuantity(productDetails.getQuantity());
			return productRepository.save(product);
		}
		return null;
	}

	public void deleteProduct(Long id) {
		productRepository.deleteById(id);
	}
}
